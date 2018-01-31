package edu.ecnu.yt.pretty.common.heartbeat;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import edu.ecnu.yt.pretty.common.message.*;
import edu.ecnu.yt.pretty.common.util.ResponseParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yt
 * @date 2017-12-25 15:12:11
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatHandler.class);

	private ScheduledFuture<?> scheduledFuture = null;
	private long heartBeatTimeout;

	/**
	 * Constructor.
	 *
	 * @param heartBeatTimeout the heart beat timeout
	 */
	public HeartbeatHandler(long heartBeatTimeout) {
		this.heartBeatTimeout = heartBeatTimeout;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public static String getName() {
		return "heartbeat-connector";
	}

	/**
	 * Channel read.
	 *
	 * @param ctx the ctx
	 * @param msg the msg
	 * @throws Exception the exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof PrettyMessage) {
			PrettyMessage<?> message = (PrettyMessage<?>) msg;
			PrettyHeader.MessageType messageType = message.header().messageType();
			if (scheduledFuture != null) {
				if (LOGGER.isDebugEnabled()) {
				    LOGGER.debug("reset heartbeat timer");
				}
				// 取消scheduledFuture计划任务。
				scheduledFuture.cancel(true);
				scheduledFuture = null;
			}
			if (messageType == PrettyHeader.MessageType.HEARTBEAT_REQ) {
				PrettyResponse response = ResponseParser.parseToResponse(message.header(),
						PrettyHeader.MessageType.HEARTBEAT_RES);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("receive HEARTBEAT_REQ: " + message.header().messageId());
				}
				ctx.writeAndFlush(response);
			} else {

				ctx.fireChannelRead(msg);
			}
		}
	}

	/**
	 * User event triggered.
	 *
	 * @param ctx the ctx
	 * @param evt the evt
	 * @throws Exception the exception
	 */
	// 依赖于idleStateHandler。如果没有注册IdleStateHandler，就不会触发主动发送ping的请求。
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
			if (scheduledFuture == null) {
				// 发送心跳消息包。将该心跳包MessageID设置为一个UUID随机数，防止重复发送request
				PrettyRequest request = createHeartbeatRequest();
				ctx.writeAndFlush(request).addListener(future -> {
					// 如果20s内没有取消scheduledFuture的话，表示没有在channelRead中监听到针对该messageID的response，
					// 那么执行计划任务断开连接。
					scheduledFuture = ctx.executor().schedule(() -> {
                        LOGGER.warn("PING HAS NOT RESPONSE: " + ctx.channel().remoteAddress());
                        ctx.close();
                    }, heartBeatTimeout, TimeUnit.SECONDS);
				});
			}
		}
	}

	/**
	 * Create heartbeat request pretty request.
	 *
	 * @return the pretty request
	 */
	public PrettyRequest createHeartbeatRequest() {
		PrettyHeader header = PrettyHeader.newBuilder()
				.setConnectionType(PrettyHeader.ConnectionType.LONG)
				.setMessageType(PrettyHeader.MessageType.HEARTBEAT_REQ)
				.setMessageId(UUID.randomUUID().getMostSignificantBits())
				.build();
		return new PrettyRequest(header, null);
	}

	/**
	 * Exception caught.
	 *
	 * @param ctx   the ctx
	 * @param cause the cause
	 * @throws Exception the exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error(cause.getMessage(), cause);
		ctx.fireExceptionCaught(cause);
	}
}