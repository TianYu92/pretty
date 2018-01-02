package edu.ecnu.yt.pretty.common.message;

import edu.ecnu.yt.pretty.common.exceptions.Code;

/**
 * @author yt
 * @date 2017-12-23 18:59:41
 */
public class ResponseBody {
	
	private Code   code;
	private Object result;

	/**
	 * Constructor.
	 */
	public ResponseBody() {

	}

	/**
	 * Constructor.
	 *
	 * @param result the result
	 */
	public ResponseBody(Object result) {
		this(Code.OK, result);
	}

	/**
	 * Constructor.
	 *
	 * @param code	 	  the code
	 * @param result      the result
	 */
	public ResponseBody(Code code, Object result) {
		this.code = code;
		this.result = result;
	}

	/**
	 * Gets result.
	 *
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * Gets code.
	 *
	 * @return the code
	 */
	public Code getCode() {
		return code;
	}

}
