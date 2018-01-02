package edu.ecnu.yt.pretty.publisher.rpc.impl;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author yt
 * @date 2017-12-23 20:18:14
 */
public class PartialPublishedService extends CachedService {
	
	List<String> methodFilter;
	boolean letUserKnownDetail = false;

	/**
	 * Constructor.
	 *
	 * @param interfaceClass     the interface class
	 * @param service            the service
	 * @param letUserKnownDetail the let user known detail
	 * @param publishMethodsName the publish methods name
	 */
	public PartialPublishedService(Class<?> interfaceClass, Object service, boolean letUserKnownDetail, String ... publishMethodsName) {
		this(interfaceClass, service, publishMethodsName);
		this.letUserKnownDetail = letUserKnownDetail;
	}

	/**
	 * Constructor.
	 *
	 * @param interfaceClass     the interface class
	 * @param service            the service
	 * @param publishMethodsName the publish methods name
	 */
	public PartialPublishedService(Class<?> interfaceClass, Object service, String ... publishMethodsName) {
		super(interfaceClass, service);
		methodFilter = Arrays.asList(publishMethodsName);
	}

	/**
	 * Invoke object.
	 *
	 * @param method the method name
	 * @param params     the params
	 * @return the object
	 * @throws Exception the exception
	 */
	@Override
	public Object invoke(String method, Object[] params)
            throws InvocationTargetException, IllegalAccessException {
		if (!methodFilter.contains(method)) {
			if (letUserKnownDetail) {
				throw new PrettyRpcException(Code.SERVICE_EXECUTION_ERROR, String.format("方法:[%s]未发布", method));
			} else {
				throw new PrettyRpcException(Code.SERVICE_EXECUTION_ERROR, "未找到服务");
			}

		}; 
		return super.invoke(method, params);
	}
	
}
