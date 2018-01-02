/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.connector;

import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.message.RequestBody;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author yt
 * @version $Id: ReferenceConnector.java, v 0.1 2017年12月14日 下午7:00 yt Exp $
 */
public interface ReferenceConnector {

    /**
     * Connect.
     */
    void connect();

    /**
     * Destroy
     */
    void destroy();

    /**
     * Send message completable future.
     *
     * @param body the body
     * @return the completable future
     */
    CompletableFuture<PrettyResponse> sendMessage(RequestBody body);

    /**
     * Send message one way.
     *
     * @param body the body
     */
    void sendMessageOneWay(RequestBody body);
}