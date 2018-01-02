package edu.ecnu.yt.pretty.common.exceptions;

/**
 * @author yt
 * @date 2017-12-13 19:19:49
 */
public class PrettyRpcException extends RuntimeException {

	private static final long serialVersionUID = 1213712690731426876L;

	private Code code;

	/**
	 * Constructor.
	 *
	 * @param code    the code
	 * @param message the message
	 */
	public PrettyRpcException(Code code, String message) {
		this(code, message, null);
	}

	/**
	 * Constructor.
	 *
	 * @param code    the code
	 * @param message the message
	 * @param e       the e
	 */
	public PrettyRpcException(Code code, String message, Throwable e) {
		super(message, e);
		this.code = code;
	}

	/**
	 * Gets code.
	 *
	 * @return the code
	 */
	public Code getCode() {
		return code;
	}

	/**
	 * Sets code.
	 *
	 * @param code the code
	 */
	public void setCode(Code code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return String.format("pretty error: [%s] %s", code, getMessage());
	}
}
