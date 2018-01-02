/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.invoker;

import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.strategy.Strategy;

import java.lang.reflect.InvocationHandler;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author yt
 * @version $Id: ReferenceInvoker.java, v 0.1 2017年12月13日 上午11:28 yt Exp $
 */
public interface ReferenceInvoker extends InvocationHandler {

    /**
     * Sets service name.
     *
     * @param serviceName the service name
     */
    void setServiceName(String serviceName);

    /**
     * Gets service name.
     *
     * @return the service name
     */
    String getServiceName();

    /**
     * Registry strategy.
     *
     * @param name     the name
     * @param strategy the strategy
     */
    void registryStrategy(String name, Strategy strategy);

    /**
     * Sets connector.
     *
     * @param connector the connector
     */
    void setConnector(ReferenceConnector connector);

    /**
     * Gets connector.
     *
     * @return the connector
     */
    ReferenceConnector getConnector();

    /**
     * Gets result.
     *
     * @return the result
     */
    Object getResult() throws Throwable;

    /**
     * Destroy.
     */
    void destroy();
}