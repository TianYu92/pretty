package edu.ecnu.yt.pretty.publisher.rpc;

/**
 * @author yt
 * @date 2017-12-23 20:16:27
 */
public interface PrettyService {
	/**
	 * unsafe invoking in multiple-thread.
	 * @param method method name to invoke
	 * @param params the parameters that method need.
	 * @return the response data object returned by invoking.
	 * 
	 * @throws Exception Exception when execute.
	 */
	Object invoke(String method, Object[] params) throws Exception;

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	String getName();
}
