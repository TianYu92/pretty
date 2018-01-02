/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.invoker.impl.async;

import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvoker;
import edu.ecnu.yt.pretty.reference.invoker.impl.AbstractReferenceInvokerFactory;

/**
 *
 * @author yt
 * @version $Id: AsyncReferenceInvokerFacotory.java, v 0.1 2017年12月16日 下午5:09 yt Exp $
 */
public class AsyncReferenceInvokerFacotory extends AbstractReferenceInvokerFactory {

    private final RpcCallback callback;

    public AsyncReferenceInvokerFacotory(String serviceName, ReferenceConnector connector,
                                         RpcCallback callback) {
        super(serviceName, connector);
        this.callback = callback;
    }

    @Override
    protected ReferenceInvoker getInstance() {
        return new AsyncReferenceInvoker(this.callback);
    }
}