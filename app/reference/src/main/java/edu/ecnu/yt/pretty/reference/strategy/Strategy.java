/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.strategy;

import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvoker;

/**
 *
 * @author yt
 * @version $Id: Strategy.java, v 0.1 2017年12月14日 下午5:49 yt Exp $
 */
public interface Strategy {
    /**
     * Pre handle boolean.
     *
     * @param invocationProcessor the invoker processor
     * @return the boolean
     */
    boolean preHandle(ReferenceInvoker invocationProcessor);

    /**
     * Post handle boolean.
     *
     * @param invocationProcessor the invoker processor
     * @param response            the response
     *
     * @return the boolean
     */
    boolean postHandle(ReferenceInvoker invocationProcessor, PrettyResponse response);
    
    String getFailedReason();

    /**
     * Exception caught.
     *
     * @param e the e
     */
    void exceptionCaught(Throwable e);

    /**
     * Destroy.
     */
    void destroy();
}