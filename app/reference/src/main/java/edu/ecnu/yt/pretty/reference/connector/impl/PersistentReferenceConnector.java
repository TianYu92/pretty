/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.connector.impl;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.heartbeat.HeartbeatHandler;
import edu.ecnu.yt.pretty.common.message.*;
import edu.ecnu.yt.pretty.reference.connector.impl.handler.ReferenceHandler;
import edu.ecnu.yt.pretty.reference.service.ExecutorManager;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author yt
 * @version $Id: PersistentReferenceConnector.java, v 0.1 2017年12月14日 下午7:08 yt Exp $
 */
public class PersistentReferenceConnector extends AbstractReferenceConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistentReferenceConnector.class);

    private long idleTimeForHeartbeat;
    private long heartbeatTimeout;

    /**
     * Constructor.
     *
     * @param executorManager      the executor manager
     * @param host                 the host
     * @param port                 the port
     * @param idleTimeForHeartbeat the idle time for heartbeat
     * @param heartbeatTimeout     the heartbeat timeout
     */
    public PersistentReferenceConnector(ExecutorManager executorManager,
                                        String host, int port, long idleTimeForHeartbeat,
                                        long heartbeatTimeout) {
        super(executorManager, host, port);
        this.idleTimeForHeartbeat = idleTimeForHeartbeat;
        this.heartbeatTimeout = heartbeatTimeout;
    }

    /**
     * use dcl to increase spent.
     */
    @Override
    public void connect() {
        if (channel == null || !channel.isActive()) {
            synchronized (this) {
                if (channel == null || !channel.isActive()) {
                    super.connect();
                    this.handler = this.channel.pipeline().get(ReferenceHandler.class);
                    try {
                        doHandshake();
                    } catch (Throwable e) {
                        this.channel.close();
                        this.channel = null;
                    }
                }
            }
        }
    }

    @Override
    protected PrettyHeader.ConnectionType getConnectionType() {
        return PrettyHeader.ConnectionType.LONG;
    }

    private void doHandshake() {
        PrettyRequest request = createHandShakeRequest();
        try {
            this.handler.call(request).get(5, TimeUnit.SECONDS);
            registerHearbeatHandler();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("persist connection connected");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new PrettyRpcException(Code.CONNECT_ERROR, "长连接模式握手失败:连接中断 ", e);
        } catch (TimeoutException e) {
            throw new PrettyRpcException(Code.CONNECT_ERROR, "长连接模式握手失败:超时 ", e);
        }
    }

    /**
     * Create hand shake request pretty request.
     *
     * @return the pretty request
     */
    public static PrettyRequest createHandShakeRequest() {
        RequestBody body = new RequestBody();
        PrettyHeader header = PrettyHeader.newBuilder()
                .setMessageType(PrettyHeader.MessageType.HANDSHAKE_REQ)
                .setMessageId(UUID.randomUUID().getMostSignificantBits())
                .setConnectionType(PrettyHeader.ConnectionType.LONG)
                .build();

        return new PrettyRequest(header, body);
    }

    /**
     * Register hearbeat handler.
     */
    private void registerHearbeatHandler() {

        // 如果idleTimeForHeartbeat为0，表示不主动发送心跳，即不加入idleStateHandler。
        if (idleTimeForHeartbeat == 0) {
            // 长连接不做关闭处理，但需要注册HeartBeat的策略。60秒读/写空闲时发送消息。
            channel.pipeline().addBefore(ReferenceHandler.getName(),
                    "idle-state-listener",
                    new IdleStateHandler(0, 0, idleTimeForHeartbeat, TimeUnit.SECONDS));
        }

        // 但是为了响应服务器的heartbeat，hearbeathandler还是需要加入的。
        channel.pipeline().addBefore(ReferenceHandler.getName(),
                HeartbeatHandler.getName(),
                new HeartbeatHandler(heartbeatTimeout));
    }

    @Override
    public void destroy() {
        if (this.channel != null) {
            this.channel.close();
        }
    }
}