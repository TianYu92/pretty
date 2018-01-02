/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.common.message;

/**
 *
 * @author yt
 * @version $Id: PrettyResponse.java, v 0.1 2017年12月14日 下午8:26 yt Exp $
 */
public class PrettyResponse extends PrettyMessage<ResponseBody> {

    public PrettyResponse(PrettyHeader header, ResponseBody body) {
        super(header, body);
    }

}