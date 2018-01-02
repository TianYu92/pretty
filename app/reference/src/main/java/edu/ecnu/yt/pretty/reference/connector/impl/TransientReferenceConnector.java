/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.connector.impl;

import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyRequest;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.message.RequestBody;
import edu.ecnu.yt.pretty.reference.connector.impl.handler.ReferenceHandler;
import edu.ecnu.yt.pretty.reference.service.ExecutorManager;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author yt
 * @version $Id: TransientReferenceConnector.java, v 0.1 2017年12月14日 下午7:09 yt Exp $
 */
public class TransientReferenceConnector extends AbstractReferenceConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransientReferenceConnector.class);

    private ScheduledFuture<?> idleListener = null;

    /**
     * Constructor.
     *
     * @param executorManager the executor manager
     * @param host            the host
     * @param port            the port
     */
    public TransientReferenceConnector(ExecutorManager executorManager, String host, int port) {
        super(executorManager, host, port);
    }

    @Override
    protected PrettyHeader.ConnectionType getConnectionType() {
        return PrettyHeader.ConnectionType.SHORT;
    }

    @Override
    public void connect() {
        synchronized (this) {
            if (idleListener != null) {
                idleListener.cancel(true);
            }

            idleListener = executorManager.getScheduledExecutors().schedule(() -> {
                synchronized (this) {
                    channel.close();
                    if (LOGGER.isDebugEnabled()) {
                      LOGGER.debug("transient channel closed!");
                    }
                    idleListener = null;
                    handler = null;
                    channel = null;
                }
            }, 10, TimeUnit.SECONDS);

            if (channel == null) {
                super.connect();
                this.handler = channel.pipeline().get(ReferenceHandler.class);
            }
        }
    }
}