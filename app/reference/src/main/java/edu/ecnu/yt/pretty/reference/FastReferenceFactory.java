package edu.ecnu.yt.pretty.reference;

import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.connector.impl.PersistentReferenceConnector;
import edu.ecnu.yt.pretty.reference.connector.impl.TransientReferenceConnector;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvokerFactory;
import edu.ecnu.yt.pretty.reference.invoker.impl.async.AsyncReferenceInvokerFacotory;
import edu.ecnu.yt.pretty.reference.invoker.impl.async.RpcCallback;
import edu.ecnu.yt.pretty.reference.invoker.impl.sync.SyncReferenceInvokerFactory;
import edu.ecnu.yt.pretty.reference.service.ExecutorManager;

import java.util.concurrent.TimeoutException;

/**
 * @author yt
 * @date 2017-12-12 19:47:18
 */
public class FastReferenceFactory {

	public final long DEFAULT_IDEL_TIMEOUT_FOR_HEART_BEAT = 30;
	public final long DEFAULT_HEARTBEAT_TIMEOUT = 10;
	private final ExecutorManager executorManager;

	public FastReferenceFactory() {
		this.executorManager = new ExecutorManager();
	}

	public void destory() {
		this.executorManager.destroy();
	}

	/**
	 * Refer a rpc reference invoker.
	 *
	 * @param <T>            the type parameter
	 * @param serviceName    the service name
	 * @param interfaceClass the interface class
	 * @param host           the host
	 * @param port           the port
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T referSync(String serviceName, Class<T> interfaceClass,
			String host, int port) {
		ReferenceConnector connector = new TransientReferenceConnector(executorManager, host, port);
		ReferenceInvokerFactory factory = new SyncReferenceInvokerFactory(serviceName, connector);
		Reference<T> reference = new Reference<>(interfaceClass, factory);
		return reference.getProxyObject();
	}

	/**
	 * Refer async t.
	 *
	 * @param <T>            the type parameter
	 * @param serviceName    the service name
	 * @param interfaceClass the interface class
	 * @param host           the host
	 * @param port           the port
	 * @param callback       the callback
	 * @return the t
	 */
	public <T> T referAsync(String serviceName, Class<T> interfaceClass,
								  String host, int port, RpcCallback callback) {
		ReferenceConnector connector = new TransientReferenceConnector(executorManager, host, port);
		ReferenceInvokerFactory factory = new AsyncReferenceInvokerFacotory(serviceName, connector, callback);
		Reference<T> reference = new Reference<>(interfaceClass, factory);
		return reference.getProxyObject();
	}

	/**
	 * Refer a persistent rpc reference invoker.
	 *
	 * @param <T>            the type parameter
	 * @param serviceName    the service name
	 * @param interfaceClass the interface class
	 * @param host           the host
	 * @param port           the port
	 * @return the t
	 * @throws InterruptedException the interrupted exception
	 * @throws TimeoutException     the timeout exception
	 */
	public <T> T referPersistentSync(String serviceName, Class<T> interfaceClass,
			String host, int port) {
		return referPersistentSync(serviceName, interfaceClass,
				host, port, DEFAULT_IDEL_TIMEOUT_FOR_HEART_BEAT, DEFAULT_HEARTBEAT_TIMEOUT);
	}

	/**
	 * Refer a persistent rpc reference invoker.
	 *
	 * @param <T>            the type parameter
	 * @param serviceName    the service name
	 * @param interfaceClass the interface class
	 * @param host           the host
	 * @param port           the port
	 * @return the t
	 * @throws InterruptedException the interrupted exception
	 * @throws TimeoutException     the timeout exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T referPersistentSync(String serviceName, Class<T> interfaceClass,
			String host, int port, long idleTimeoutForHeartbeat, long heartbeatTimeout) {
		ReferenceConnector connector = new PersistentReferenceConnector(executorManager, host, port,
				idleTimeoutForHeartbeat, heartbeatTimeout);
		ReferenceInvokerFactory factory = new SyncReferenceInvokerFactory(serviceName, connector);
		Reference<T> reference = new Reference<>(interfaceClass, factory);
		return reference.getProxyObject();
	}

	/**
	 * Refer persistent async t.
	 *
	 * @param <T>            the type parameter
	 * @param serviceName    the service name
	 * @param interfaceClass the interface class
	 * @param host           the host
	 * @param port           the port
	 * @param callback       the callback
	 * @return the t
	 */
	public <T> T referPersistentAsync(String serviceName, Class<T> interfaceClass,
											 String host, int port, RpcCallback callback) {
		return referPersistentAsync(serviceName, interfaceClass,
				host, port, callback, DEFAULT_IDEL_TIMEOUT_FOR_HEART_BEAT, DEFAULT_HEARTBEAT_TIMEOUT);
	}

	/**
	 * Refer persistent async t.
	 *
	 * @param <T>                     the type parameter
	 * @param serviceName             the service name
	 * @param interfaceClass          the interface class
	 * @param host                    the host
	 * @param port                    the port
	 * @param callback                the callback
	 * @param idleTimeoutForHeartbeat the idle timeout for heartbeat
	 * @param heartbeatTimeout        the heartbeat timeout
	 * @return the t
	 */
	public <T> T referPersistentAsync(String serviceName, Class<T> interfaceClass,
											String host, int port, RpcCallback callback,
											long idleTimeoutForHeartbeat, long heartbeatTimeout) {

		ReferenceConnector connector = new PersistentReferenceConnector(executorManager, host, port,
				idleTimeoutForHeartbeat, heartbeatTimeout);
		ReferenceInvokerFactory factory = new AsyncReferenceInvokerFacotory(serviceName, connector, callback);
		Reference<T> reference = new Reference<>(interfaceClass, factory);
		return reference.getProxyObject();
	}

}
