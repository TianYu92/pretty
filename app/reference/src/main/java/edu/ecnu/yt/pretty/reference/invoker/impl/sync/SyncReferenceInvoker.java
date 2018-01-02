/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.invoker.impl.sync;

import edu.ecnu.yt.pretty.reference.invoker.impl.AbstractReferenceInvoker;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author yt
 * @version $Id: SyncReferenceInvoker.java, v 0.1 2017年12月13日 上午11:28 yt Exp $
 */
public class SyncReferenceInvoker extends AbstractReferenceInvoker {

    @Override
    public Object getResult() throws Throwable {
        try {
            return this.rpcFuture.get().body().getResult();
        } catch (InterruptedException | ExecutionException e) {
            throw e.getCause();
        }
    }

}