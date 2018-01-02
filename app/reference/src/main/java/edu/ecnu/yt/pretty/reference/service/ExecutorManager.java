/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.service;

import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author yt
 * @version $Id: ExecutorManager.java, v 0.1 2017年12月13日 上午10:31 yt Exp $
 */
public class ExecutorManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorManager.class);

    private final int SCHEDULE_EXECUTOR_SIZE    = 4;
    private final int SERVICE_EXECUTOR_SIEZ     = 4;
    private static final int NIO_EVENT_LOOP_GROUP_SIEZ = 8;

    private final ScheduledExecutorService scheduledExecutors   = Executors.newScheduledThreadPool(SCHEDULE_EXECUTOR_SIZE);
    private final ExecutorService          serviceExecutors     = Executors.newFixedThreadPool(SERVICE_EXECUTOR_SIEZ);
    private final NioEventLoopGroup        nioEventLoopGroup    = new NioEventLoopGroup(NIO_EVENT_LOOP_GROUP_SIEZ);

    /**
     * Constructor.
     */
    public ExecutorManager() {
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("ExecutorManager: initialized.");
        }
    }

    /**
     * Getter method for property <tt>serviceExecutors</tt>.
     *
     * @return property value of serviceExecutors
     */
    public ExecutorService getServiceExecutors() {
        return serviceExecutors;
    }

    /**
     * Getter method for property <tt>scheduledExecutors</tt>.
     *
     * @return property value of scheduledExecutors
     */
    public ScheduledExecutorService getScheduledExecutors() {
        return scheduledExecutors;
    }

    /**
     * Getter method for property <tt>nioEventLoopGroup</tt>.
     *
     * @return property value of nioEventLoopGroup
     */
    public NioEventLoopGroup getNioEventLoopGroup() {
        return nioEventLoopGroup;
    }

    /**
     * Destroy.
     */
    public void destroy() {
        scheduledExecutors.shutdown();
        serviceExecutors.shutdown();
        nioEventLoopGroup.shutdownGracefully();
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("executor manager destroyed.");
        }
    }

}