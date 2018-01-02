package edu.ecnu.yt.pretty.common.util;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.message.ResponseBody;

/**
 * @author yt
 * @date 2017-12-23 18:27:31
 */
public class ResponseParser {
	
	private static PrettyHeader createHeader(PrettyHeader reqHeader, PrettyHeader.MessageType msgType) {
		PrettyHeader.Builder headerBuilder = PrettyHeader.newBuilder();
		headerBuilder.setMessageType(msgType);
		headerBuilder.setMessageId(reqHeader.messageId());
		headerBuilder.setConnectionType(reqHeader.connectionType());
		return headerBuilder.build();
	}
	
	private static PrettyResponse createResponse(PrettyHeader reqHeader, PrettyHeader.MessageType msgType, ResponseBody body) {
		PrettyHeader header = createHeader(reqHeader, msgType);
		return new PrettyResponse(header, body);
	}

	/**
	 * Parse to response pretty response.
	 *
	 * @param reqHeader the req header
	 * @param msgType   the msg type
	 * @param e         the e
	 * @return the pretty response
	 */
	public static PrettyResponse parseToResponse(PrettyHeader reqHeader, PrettyHeader.MessageType msgType, PrettyRpcException e) {
		return createResponse(reqHeader, msgType, new ResponseBody(e.getCode(), e.getMessage()));
	}

	/**
	 * Parse to response pretty response.
	 *
	 * @param reqHeader the req header
	 * @param msgType   the msg type
	 * @param data      the data
	 * @return the pretty response
	 */
	public static PrettyResponse parseToResponse(PrettyHeader reqHeader, PrettyHeader.MessageType msgType, Object data) {
		return createResponse(reqHeader, msgType, new ResponseBody(Code.OK, data));
	}

	/**
	 * Parse to response pretty response.
	 *
	 * @param reqHeader the req header
	 * @param msgType   the msg type
	 * @return the pretty response
	 */
	public static PrettyResponse parseToResponse(PrettyHeader reqHeader, PrettyHeader.MessageType msgType) {
		return createResponse(reqHeader, msgType, new ResponseBody(Code.OK, null));
	}

}
