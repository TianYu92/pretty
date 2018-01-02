package edu.ecnu.yt.pretty.spring.beans;

import edu.ecnu.yt.pretty.publisher.Publisher;
import edu.ecnu.yt.pretty.spring.util.ServiceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * @author yt
 * @date 2017-12-17 14:48:06
 */
public class PublisherProxyFactoryBean implements FactoryBean<Object>, ApplicationContextAware {
	
	private int                 port;
	private long                idleTimeForHeartbeat = 10;
	private long                heartbeatTimeout = 15;
	private List<ServiceConfig> serviceConfigs;
	private ApplicationContext  context;

	/**
	 * Gets object.
	 *
	 * @return the object
	 * @throws Exception the exception
	 */
	@Override
	public Object getObject() throws Exception {
		Publisher publisher = Publisher.newBuilder()
				.port(port)
				.heartbeatTimeout(heartbeatTimeout)
				.idleTimeForHeartbeat(idleTimeForHeartbeat)
				.build();
		for (ServiceConfig serviceConfig : serviceConfigs) {
			publisher.export(serviceConfig.getName(), 
					Class.forName(serviceConfig.getInterfaceName()), 
					context.getBean(serviceConfig.getRef()));
		}
		publisher.publish();
		return publisher;
	}

	/**
	 * Gets object type.
	 *
	 * @return the object type
	 */
	@Override
	public Class<?> getObjectType() {
		return Publisher.class;
	}

	/**
	 * Is singleton boolean.
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Gets port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets port.
	 *
	 * @param port the port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets service configs.
	 *
	 * @return the service configs
	 */
	public List<ServiceConfig> getServiceConfigs() {
		return serviceConfigs;
	}

	/**
	 * Sets service configs.
	 *
	 * @param serviceConfigs the service configs
	 */
	public void setServiceConfigs(List<ServiceConfig> serviceConfigs) {
		this.serviceConfigs = serviceConfigs;
	}


	/**
	 * Getter method for property <tt>idleTimeForHeartbeat</tt>.
	 *
	 * @return property value of idleTimeForHeartbeat
	 */
	public long getIdleTimeForHeartbeat() {
		return idleTimeForHeartbeat;
	}

	/**
	 * Setter method for property <tt>idleTimeForHeartbeat</tt>.
	 *
	 * @param idleTimeForHeartbeat value to be assigned to property idleTimeForHeartbeat
	 */
	public void setIdleTimeForHeartbeat(long idleTimeForHeartbeat) {
		this.idleTimeForHeartbeat = idleTimeForHeartbeat;
	}

	/**
	 * Getter method for property <tt>heartbeatTimeout</tt>.
	 *
	 * @return property value of heartbeatTimeout
	 */
	public long getHeartbeatTimeout() {
		return heartbeatTimeout;
	}

	/**
	 * Setter method for property <tt>heartbeatTimeout</tt>.
	 *
	 * @param heartbeatTimeout value to be assigned to property heartbeatTimeout
	 */
	public void setHeartbeatTimeout(long heartbeatTimeout) {
		this.heartbeatTimeout = heartbeatTimeout;
	}


	/**
	 * Sets application context.
	 *
	 * @param applicationContext the application context
	 * @throws BeansException the beans exception
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

}
