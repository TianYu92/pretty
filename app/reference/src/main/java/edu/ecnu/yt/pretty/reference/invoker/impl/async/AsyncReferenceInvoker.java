/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.invoker.impl.async;

import com.google.common.base.Preconditions;
import edu.ecnu.yt.pretty.common.message.RequestBody;
import edu.ecnu.yt.pretty.reference.invoker.impl.AbstractReferenceInvoker;

import java.lang.reflect.Method;

/**
 *
 * @author yt
 * @version $Id: AsyncReferenceInvoker.java, v 0.1 2017年12月13日 上午11:28 yt Exp $
 */
public class AsyncReferenceInvoker extends AbstractReferenceInvoker {

    private final RpcCallback callback;

    public AsyncReferenceInvoker(RpcCallback callback) {
        this.callback = Preconditions.checkNotNull(callback, "异步调用必须有callback");
    }

    @Override
    protected void afterInvoke(Object proxy, Method method, Object[] args) {
        this.rpcFuture.whenComplete((resp, e) -> {
            if (e != null) {
                callback.exceptionCaught(e);
            } else {
                callback.accept(method, resp.body().getResult());
            }
        });
    }

    @Override
    public Object getResult() {
        return null;
    }
}