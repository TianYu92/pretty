/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.common.message.codec;

import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyMessage;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.message.ResponseBody;

/**
 *
 * @author yt
 * @version $Id: PrettyResponseDecoder.java, v 0.1 2017年12月14日 下午8:19 yt Exp $
 */
public class PrettyResponseDecoder extends PrettyMessageDecoder<ResponseBody> {

    @Override
    protected Class<ResponseBody> getBodyType() {
        return ResponseBody.class;
    }

    @Override
    protected PrettyMessage buildMessage(PrettyHeader header, ResponseBody body) {
        return new PrettyResponse(header, body);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public static String getName() {
        return "response-decoder";
    }
}