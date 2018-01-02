package edu.ecnu.yt.pretty.reference.invoker.impl.async;

import java.lang.reflect.Method;

/**
 * Pretty 异步调用接口。
 * @author yt
 */
public interface RpcCallback {

	/**
	 * Accept.
	 *
	 * @param method the method
	 * @param result the result
	 */
	void accept(Method method, Object result);

	/**
	 * Exception caught.
	 *
	 * @param e the e
	 */
	void exceptionCaught(Throwable e);

}