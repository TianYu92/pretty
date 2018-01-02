/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.invoker.impl.sync;

import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvoker;
import edu.ecnu.yt.pretty.reference.invoker.impl.AbstractReferenceInvokerFactory;
import edu.ecnu.yt.pretty.reference.strategy.Strategy;

import java.util.LinkedHashMap;

/**
 *
 * @author yt
 * @version $Id: SyncReferenceInvokerFactory.java, v 0.1 2017年12月16日 下午5:21 yt Exp $
 */
public class SyncReferenceInvokerFactory extends AbstractReferenceInvokerFactory {

    public SyncReferenceInvokerFactory(String serviceName, ReferenceConnector connector) {
        super(serviceName, connector);
    }

    @Override
    protected ReferenceInvoker getInstance() {
        return new SyncReferenceInvoker();
    }
}