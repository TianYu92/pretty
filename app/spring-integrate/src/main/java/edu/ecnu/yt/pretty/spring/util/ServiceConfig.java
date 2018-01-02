package edu.ecnu.yt.pretty.spring.util;


/**
 * @author yt
 * @date 2017-12-26 9:42:10
 */
public class ServiceConfig {
	
	private String name;
	private String interfaceName;
	private String ref;

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets interface name.
	 *
	 * @return the interface name
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * Sets interface name.
	 *
	 * @param interfaceName the interface name
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * Gets ref.
	 *
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * Sets ref.
	 *
	 * @param ref the ref
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

}
