package edu.ecnu.yt.pretty.publisher;

import java.util.List;

import edu.ecnu.yt.pretty.publisher.rpc.ServiceCaller;
import edu.ecnu.yt.pretty.publisher.rpc.ServiceManager;
import edu.ecnu.yt.pretty.publisher.rpc.impl.CachedService;
import edu.ecnu.yt.pretty.publisher.rpc.impl.ServiceCallerImpl;
import edu.ecnu.yt.pretty.publisher.rpc.impl.ServiceManagerImpl;

/**
 * A facade for publisher in RPC.
 * @author yt
 */
public class Publisher {
	
	Connector serverChannel;
	ServiceManager serviceManager = new ServiceManagerImpl();
	ServiceCaller  caller         = new ServiceCallerImpl(serviceManager);

	private Publisher(long heartbeatTimeout, long idleTimeForHeartbeat, int port) {
		serverChannel = new Connector(heartbeatTimeout,
				idleTimeForHeartbeat, caller, port);
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * 快速入口，用于快速在一个端口上注册一个服务。
	 * @param service 服务对象
	 */
	public void export(Class<?> interfaceClass, Object service) {
		// 包装对象（严格来说不能算装饰者，因为为了通用性，CachedService不能实现service的接口）
		CachedService cachedService = new CachedService(interfaceClass, service);
		serviceManager.registryService(cachedService);
	}
	
	/**
	 * 快速入口，用于多态
	 * @param name 服务名称
	 * @param service 服务对象
	 */
	public void export(String name, Class<?> interfaceClass, Object service) {
		CachedService cachedService = new CachedService(name, interfaceClass, service);
		serviceManager.registryService(cachedService);
	}
	
	public static class Builder {
		private long heartbeatTimeout = 20;
		private long idleTimeForHeartbeat = 60;
		private int port = 9876;
		private Builder() {}
		public Builder idleTimeForHeartbeat(long idleTimeForHeartbeat) {
			this.idleTimeForHeartbeat = idleTimeForHeartbeat;
			return this;
		}
		public Builder heartbeatTimeout(long heartbeatTimeout) {
			this.heartbeatTimeout = heartbeatTimeout;
			return this;
		}
		public Builder port(int port) {
			this.port = port;
			return this;
		}
		public Publisher build() {
			return new Publisher( heartbeatTimeout, idleTimeForHeartbeat, port);
		}
	}

	public void publish() {
		serverChannel.bind();		
	}
	
	public void close() {
		serverChannel.close();
		serviceManager.close();
	}

	public List<String> listAllServices() {
		return serviceManager.listAllServices();
	}
}