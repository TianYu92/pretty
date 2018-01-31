package edu.ecnu.yt.pretty.publisher.rpc.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.message.PrettyRequest;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.message.RequestBody;
import edu.ecnu.yt.pretty.common.message.PrettyHeader.MessageType;
import edu.ecnu.yt.pretty.common.util.ResponseParser;
import edu.ecnu.yt.pretty.publisher.rpc.Callback;
import edu.ecnu.yt.pretty.publisher.rpc.PrettyService;
import edu.ecnu.yt.pretty.publisher.rpc.ServiceManager;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServiceManagerImpl implements ServiceManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManagerImpl.class);
	
	private ConcurrentHashMap<String, PrettyService> services =
			new ConcurrentHashMap<>();
	
	private ExecutorService executor = Executors.newFixedThreadPool(16);

	@Override
	public boolean registryService(String name, PrettyService service) {
		if (services.containsKey(name)) {
			throw new PrettyRpcException(Code.SERVICE_REGISTRY_ERROR, "服务重复注册：" + name);
		}
		services.put(name, service);
		return true;		
	}
	
	@Override
	public boolean  registryService(PrettyService service) {
		return this.registryService(service.getName(), service);
	}
	
	@Override
	public PrettyService getService(String name) {
		return services.get(name);
	}
	
	@Override
	public List<String> listAllServices() {
		return new ArrayList<>(services.keySet());
	}
	
	@Override
	public void callService(PrettyRequest request, Callback<PrettyResponse> callback) {
		executor.submit(() -> {
            PrettyResponse response = null;
            try {
                RequestBody body = request.body();
                String serviceName = body.getServiceName();
                PrettyService service = services.get(serviceName);
//                if (LOGGER.isInfoEnabled()) {
//                  LOGGER.info("recieve RPC service: {}, method: {}, params: {}",
//					  serviceName, body.getMethod(), ArrayUtils.toString(body.getParams()));
//                }
                if (service == null) {
                    throw new PrettyRpcException(Code.SERVICE_EXECUTION_ERROR, "service[" + serviceName + "] is not published yet.");
                }
                Object result = service.invoke(body.getMethod(), body.getParams());
                response = ResponseParser.parseToResponse(request.header(), MessageType.NORMAL_RES, result);
            } catch (PrettyRpcException e) {
                response = ResponseParser.parseToResponse(request.header(), MessageType.NORMAL_RES, e);
            } catch (Throwable e) {
            	if (e instanceof InvocationTargetException) {
					e = ((InvocationTargetException) e).getTargetException();
				}
				if (e instanceof PrettyRpcException) {
					response = ResponseParser.parseToResponse(request.header(), MessageType.NORMAL_RES, e);
				} else {
					e = new PrettyRpcException(Code.UNEXPECTED_ERROR_ON_SERVER, e.getMessage());
					response = ResponseParser.parseToResponse(request.header(), MessageType.NORMAL_RES, e);
				}
			} finally {
                callback.accept(response);
            }
        });
	}

	@Override
	public void close() {
		executor.shutdown();
	}
}
