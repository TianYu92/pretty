/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.common.message;

/**
 *
 * @author yt
 * @version $Id: PrettyRequest.java, v 0.1 2017年12月14日 下午8:26 yt Exp $
 */
public class PrettyRequest extends PrettyMessage<RequestBody> {

    public PrettyRequest(PrettyHeader header, RequestBody body) {
        super(header, body);
    }
}