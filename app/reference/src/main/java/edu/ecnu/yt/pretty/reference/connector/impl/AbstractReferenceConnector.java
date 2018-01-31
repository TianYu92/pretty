package edu.ecnu.yt.pretty.reference.connector.impl;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyHeader.*;
import edu.ecnu.yt.pretty.common.message.PrettyRequest;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.message.RequestBody;
import edu.ecnu.yt.pretty.common.message.codec.PrettyMessageEncoder;
import edu.ecnu.yt.pretty.common.message.codec.PrettyMessageFrameDecoder;
import edu.ecnu.yt.pretty.common.message.codec.PrettyResponseDecoder;
import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.connector.impl.handler.ReferenceHandler;
import edu.ecnu.yt.pretty.reference.service.ExecutorManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author yt
 * @date 2017-12-12 19:36:08
 */
public abstract class AbstractReferenceConnector implements ReferenceConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReferenceConnector.class);
    
    protected final ExecutorManager executorManager;
    private final Bootstrap bt = new Bootstrap();
    protected ReferenceHandler handler = null;

    private final String host;
    private final int    port;
    protected Channel channel;
    
	public AbstractReferenceConnector(ExecutorManager executorManager, String host, int port, boolean lazyConnect) {
        this.host = host;
        this.port = port;
        this.executorManager = executorManager;
        init();
        if (!lazyConnect) {
        	connect();
        }
	}

	private void init() {
        bt.group(executorManager.getNioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new PrettyMessageFrameDecoder())
                                .addLast(new PrettyResponseDecoder())
                                .addLast(new PrettyMessageEncoder())
                                .addLast(ReferenceHandler.getName(), new ReferenceHandler(executorManager));
                    }

                    ;
                });
    }

    protected abstract ConnectionType getConnectionType();

    protected PrettyRequest getRequest(RequestBody body, boolean oneway) {
        PrettyHeader.Builder headerBuilder = PrettyHeader.newBuilder();
        headerBuilder.setConnectionType(getConnectionType());
        headerBuilder.setMessageId(UUID.randomUUID().getMostSignificantBits());
        headerBuilder.setMessageType(oneway ? MessageType.ONE_WAY : MessageType.NORMAL_REQ);
        return new PrettyRequest(headerBuilder.build(), body);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public CompletableFuture<PrettyResponse> sendMessage(RequestBody body) {
        connect();
        PrettyRequest request = getRequest(body, false);
        return handler.call(request);
    }

    @Override
    public void sendMessageOneWay(RequestBody body) {
        connect();
        PrettyRequest request = getRequest(body, true);
        handler.notify(request);
    }

    /**
     * Do connect channel.
     */
    @Override
    public void connect() {
        ChannelFuture future = bt.connect(host, port);
        try {
            boolean timeout = future.await(5000);
            if (future.isSuccess()) {
                this.channel = future.channel();
            } else {
                future.channel().close().sync();
                if (timeout) {
                    LOGGER.warn("channel connect timeout");
                    throw new PrettyRpcException(Code.CONNECT_ERROR, "连接超时");
                } else {
                    LOGGER.warn("channel connect failed");
                    throw new PrettyRpcException(Code.CONNECT_ERROR, "连接失败");
                }
            }
        } catch (InterruptedException e) {
            throw new PrettyRpcException(Code.CONNECT_ERROR, "连接被中断", e);
        }
    }

}
