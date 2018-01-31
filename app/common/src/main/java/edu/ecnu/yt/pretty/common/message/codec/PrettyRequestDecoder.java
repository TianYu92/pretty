/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.common.message.codec;

import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyMessage;
import edu.ecnu.yt.pretty.common.message.PrettyRequest;
import edu.ecnu.yt.pretty.common.message.RequestBody;
/**
 *
 * @author yt
 * @version $Id: PrettyRequestDecoder.java, v 0.1 2017年12月14日 下午8:19 yt Exp $
 */
public class PrettyRequestDecoder extends PrettyMessageDecoder<RequestBody> {

    @Override
    protected Class<RequestBody> getBodyType() {
        return RequestBody.class;
    }

    @Override
    protected PrettyMessage<RequestBody> buildMessage(PrettyHeader header, RequestBody body) {
        return new PrettyRequest(header, body);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public static String getName() {
        return "request-decoder";
    }
}