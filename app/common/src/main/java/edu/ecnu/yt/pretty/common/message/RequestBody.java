package edu.ecnu.yt.pretty.common.message;

import java.util.Map;

/**
 * @author yt
 * @date 2017-12-23 20:09:01
 */
public class RequestBody {

	private String              serviceName = null;
	private String              method      = null;
	private Object[]            params      = null;
	private Map<String, String> attributes  = null;

	/**
	 * Constructor.
	 */
	public RequestBody() {
	}

	/**
	 * Constructor.
	 *
	 * @param serviceName the service name
	 * @param method  the method name
	 * @param params      the params
	 */
	public RequestBody(String serviceName, String method, Object[] params) {
		this(serviceName, method, params, null);
	}

	/**
	 * Constructor.
	 *
	 * @param serviceName the service name
	 * @param method  the method name
	 * @param params      the params
	 * @param attributes  the attributes
	 */
	public RequestBody(String serviceName, String method, Object[] params, Map<String, String> attributes) {
		this.serviceName = serviceName;
		this.method = method;
		this.params = params;
		this.setAttributes(attributes);
	}

	/**
	 * Gets service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets service name.
	 *
	 * @param serviceName the service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets method name.
	 *
	 * @return the method name
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets method name.
	 *
	 * @param method the method name
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Gets params.
	 *
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}

	/**
	 * Sets params.
	 *
	 * @param params the params
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}

	/**
	 * Gets attributes.
	 *
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * Sets attributes.
	 *
	 * @param attributes the attributes
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
