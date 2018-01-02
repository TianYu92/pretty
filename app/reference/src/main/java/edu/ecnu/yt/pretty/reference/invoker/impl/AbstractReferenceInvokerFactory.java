/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.invoker.impl;

import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvoker;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvokerFactory;

/**
 *
 * @author yt
 * @version $Id: AbstractReferenceInvokerFactory.java, v 0.1 2017年12月15日 上午10:22 yt Exp $
 */
public abstract class AbstractReferenceInvokerFactory implements ReferenceInvokerFactory {

    private final String serviceName;
    private final ReferenceConnector connector;

    public AbstractReferenceInvokerFactory(String serviceName, ReferenceConnector connector) {
        this.serviceName = serviceName;
        this.connector = connector;
    }

    protected abstract ReferenceInvoker getInstance();

    @Override
    public ReferenceInvoker createInvoker() {

        ReferenceInvoker invoker = getInstance();

        // fixed field
        invoker.setServiceName(serviceName);
        invoker.setConnector(connector);

        return invoker;
    }
}