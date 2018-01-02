package edu.ecnu.yt.pretty.publisher;

import edu.ecnu.yt.pretty.common.heartbeat.HeartbeatHandler;
import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyHeader.ConnectionType;
import edu.ecnu.yt.pretty.common.message.PrettyHeader.MessageType;
import edu.ecnu.yt.pretty.common.message.PrettyRequest;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.util.ResponseParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author yt
 * @date 2017-12-25 14:37:37
 */
public class ConnectionManager extends SimpleChannelInboundHandler<PrettyRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private long heartbeatTimeout;
    private long idleTimeForHeartbeat;

    /**
     * Constructor.
     *
     * @param heartbeatTimeout     the heartbeat timeout
     * @param idleTimeForHeartbeat the idle time for heartbeat
     */
    public ConnectionManager(long heartbeatTimeout, long idleTimeForHeartbeat) {
        if (heartbeatTimeout < 0 || idleTimeForHeartbeat < 0) {
            throw new IllegalArgumentException("heartbeat timeout and idle time for heartbeat can not "
                    + "be a negative number.");
        }
        this.heartbeatTimeout = heartbeatTimeout;
        this.idleTimeForHeartbeat = idleTimeForHeartbeat;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public static String getName() {
        return "connection-manager";
    }

    @Override
    public boolean isSharable() {
        return true;
    }

    /**
     * Channel unregistered.
     *
     * @param ctx the ctx
     * @throws Exception the exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("channel closed on publisher");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PrettyRequest request) throws Exception {
        PrettyHeader header = request.header();
        if (header.messageType() == MessageType.HANDSHAKE_REQ &&
                header.connectionType() == ConnectionType.LONG) {

            PrettyResponse resp = ResponseParser.parseToResponse(request.header(),
                    MessageType.HANDSHAKE_RES);
            registerHeartbeatHandler(ctx);
            ctx.writeAndFlush(resp);
            return;
        }

        ctx.fireChannelRead(request);
    }

    private void registerHeartbeatHandler(ChannelHandlerContext ctx) {
        String connMgrName = ConnectionManager.getName();
        ctx.pipeline().addAfter(connMgrName, HeartbeatHandler.getName(), new HeartbeatHandler(heartbeatTimeout));
        // 如果两个heartbeat时间控制变量任意一个为0，则无需新增主动发起heartbeat的功能。

        if (heartbeatTimeout != 0 && idleTimeForHeartbeat != 0) {
            // 出现一分钟以上的读/写空闲时间才会触发Idle事件。
            ctx.pipeline().addAfter(connMgrName, "idle-state-listener", new IdleStateHandler(0, 0,
                    idleTimeForHeartbeat, TimeUnit.SECONDS));
        }
    }

    /**
     * Exception caught.
     *
     * @param ctx   the ctx
     * @param cause the cause
     * @throws Exception the exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.error(cause.getMessage(), cause);
        ctx.close().sync();
    }
}