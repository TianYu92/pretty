package edu.ecnu.yt.pretty.common.message;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author yt.
 */
public abstract class PrettyMessage<T> {

	private final PrettyHeader header;
	private final T            body;

	public PrettyMessage(PrettyHeader header, T body) {
		this.header = header;
		this.body = body;
	}


	/**
	 * Getter method for property <tt>body</tt>.
	 *
	 * @return property value of body
	 */
	public T body() {
		return body;
	}

	/**
	 * Getter method for property <tt>header</tt>.
	 *
	 * @return property value of header
	 */
	public PrettyHeader header() {
		return header;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
