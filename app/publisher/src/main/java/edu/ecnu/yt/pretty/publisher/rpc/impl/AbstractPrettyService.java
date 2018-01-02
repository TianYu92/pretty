package edu.ecnu.yt.pretty.publisher.rpc.impl;

import edu.ecnu.yt.pretty.publisher.rpc.PrettyService;

/**
 * wrapping the real service for invoking.
 * @author yutian

 */
public abstract class AbstractPrettyService implements PrettyService {
	
	protected final Object service;
	protected String name;
	
	public AbstractPrettyService(Class<?> interfaceClass, Object service) {
		if (!interfaceClass.isInterface()) {
			throw new IllegalArgumentException(String.format(
					"the class of %s is not an interface", interfaceClass.getName()));
		}
		this.name = interfaceClass.getName();
		this.service = service;
	}
	
	@Override
	public String getName() {
		return name;
	}
}
