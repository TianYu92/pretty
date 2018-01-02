/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.test;

import edu.ecnu.yt.pretty.reference.invoker.impl.async.RpcCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 *
 * @author yt
 * @version $Id: TestCallback.java, v 0.1 2017年12月23日 下午5:56 yt Exp $
 */
public class TestCallback implements RpcCallback{

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCallback.class);

    @Override
    public void accept(Method method, Object result) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} - {}", method.getName(), result);
        }
    }

    @Override
    public void exceptionCaught(Throwable e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
}