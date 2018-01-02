package edu.ecnu.yt.pretty.publisher.rpc.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.util.SignatureGenerator;

/**
 * @author yt
 * @date 2017-12-23 20:18:22
 */
public class CachedService extends AbstractPrettyService {
	
	protected final Map<String, Method> methodsCache = 
			new ConcurrentHashMap<>();

	/**
	 * Constructor.
	 *
	 * @param name the name
	 * @param interfaceClass the interface class
	 * @param service the service
	 */
	public CachedService(String name, Class<?> interfaceClass, Object service) {
		super(interfaceClass, service);
		this.name = name;
		buildCache();
	}

	/**
	 * Constructor.
	 *
	 * @param interfaceClass the interface class
	 * @param service the service
	 */
	public CachedService(Class<?> interfaceClass, Object service) {
		super(interfaceClass, service);
	}

	/**
	 * Invoke object.
	 *
	 * @param method the method name
	 * @param params the params
	 * @return the object
	 * @throws Exception the exception
	 */
	@Override
	public Object invoke(String method, Object[] params) throws InvocationTargetException, IllegalAccessException {
			Method m = findMethod(method);
			if (params == null || params.length == 0) {
				return m.invoke(service);
			} else {
				return m.invoke(service, params);
			}
	}

	private Method findMethod(String methodName) {
		Method method = methodsCache.get(methodName);
		if (method == null) {
			throw new PrettyRpcException(Code.SERVICE_EXECUTION_ERROR, "can not found service method for calling parameters.");
		}
		return method;
	}

	private void buildCache() {

		Method[] methods = service.getClass().getDeclaredMethods();

		for (Method method : methods) {
			String signature = SignatureGenerator.generateParamDescriptor(method.getName(),
					method.getParameterTypes());
			methodsCache.put(signature, method);
		}
	}

}