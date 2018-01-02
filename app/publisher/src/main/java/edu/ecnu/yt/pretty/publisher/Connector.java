package edu.ecnu.yt.pretty.publisher;

import edu.ecnu.yt.pretty.common.message.codec.PrettyMessageEncoder;
import edu.ecnu.yt.pretty.common.message.codec.PrettyMessageFrameDecoder;
import edu.ecnu.yt.pretty.common.message.codec.PrettyRequestDecoder;
import edu.ecnu.yt.pretty.publisher.rpc.ServiceCaller;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yt
 * @date 2017-12-25 15:10:45
 */
public class Connector {

	private static final Logger logger = LoggerFactory.getLogger(Connector.class);

	private int port;
	private ServerBootstrap bt = null;
	private EventLoopGroup bossGroup = new NioEventLoopGroup(2);
	private EventLoopGroup workerGroup = new NioEventLoopGroup(12);

	private ConnectionManager connectionManager;
	private PrettyMessageEncoder messageEncoder = new PrettyMessageEncoder();
	private ServiceCaller serviceCaller;
	private ChannelFuture channelFuture;

	/**
	 * Constructor.
	 *
	 * @param heartbeatTimeout     the heartbeat timeout
	 * @param idleTimeForHeartbeat the idle time for heartbeat
	 * @param serviceCaller        the service caller
	 * @param port                 the port
	 */
	public Connector(long heartbeatTimeout, long idleTimeForHeartbeat,
					 ServiceCaller serviceCaller, int port) {
		connectionManager = new ConnectionManager(heartbeatTimeout, idleTimeForHeartbeat);
		this.port = port;
		this.serviceCaller = serviceCaller;
		initEventLoop();
	}

	private void initEventLoop() {
		bt = new ServerBootstrap();
		bt.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 2048)
			.option(ChannelOption.SO_REUSEADDR, true)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// 编码handler和connectionManager为共享handler。
					// Ps. 解码handler因为需要处理半包等问题所以不能使共享的！
					ch.pipeline()
						.addLast(PrettyMessageFrameDecoder.getName(), new PrettyMessageFrameDecoder())
						.addLast(PrettyRequestDecoder.getName(), new PrettyRequestDecoder())
						.addLast(PrettyMessageEncoder.getName(), messageEncoder)
						.addLast(ConnectionManager.getName(), connectionManager)
						.addLast(ServiceCaller.getName(), serviceCaller);
				}
			});
	}

	/**
	 * Shut down gracefully.
	 */
	public void shutDownGracefully() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	/**
	 * Bind.
	 */
	public void bind() {
		this.channelFuture = bt.bind(port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (logger.isInfoEnabled()) {
                    logger.info("the publisher has been published.");
                }
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("publish failed.");
                }
            }
        });
	}

	/**
	 * Close.
	 */
	public void close() {
		if (this.channelFuture != null) {
			try {
				this.channelFuture.channel().close().sync();
				shutDownGracefully();
			} catch (InterruptedException e) {
			}
		}
	}
}