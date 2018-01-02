package edu.ecnu.yt.pretty.spring.beans;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.reference.Reference;
import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.connector.impl.PersistentReferenceConnector;
import edu.ecnu.yt.pretty.reference.connector.impl.TransientReferenceConnector;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvokerFactory;
import edu.ecnu.yt.pretty.reference.invoker.impl.async.AsyncReferenceInvokerFacotory;
import edu.ecnu.yt.pretty.reference.invoker.impl.async.RpcCallback;
import edu.ecnu.yt.pretty.reference.invoker.impl.sync.SyncReferenceInvokerFactory;
import edu.ecnu.yt.pretty.reference.service.ExecutorManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author yt
 * @date 2017-12-17 14:47:07
 */
public class ReferenceProxyFactoryBean implements FactoryBean<Object>, DisposableBean, ApplicationContextAware {

    private static final String EXECUTOR_MANAGER_ID = "executorManager";

    private String interfaceName;
    private String callbackId;
    private String name;

    private String host = "localhost";
    private boolean async = false;
    private boolean persist = false;
    private long idleHeartbeatTimeout = 10;
    private long heartbeatTimeout = 15;
    private int    port = 9875;

    private Reference<?> reference;
    private ApplicationContext context;
    private boolean destroyExecutorManager = false;
    private static ExecutorManager executorManager = null;

    /**
     * Gets object.
     *
     * @return the object
     * @throws Exception the exception
     */
    @Override
    public Object getObject() throws Exception {

        ReferenceConnector connector;

        ExecutorManager executorManager = getExecutorManager();

        if (persist) {
            connector = new PersistentReferenceConnector(executorManager, host, port, idleHeartbeatTimeout, heartbeatTimeout);
        } else {
            connector = new TransientReferenceConnector(executorManager, host, port);
        }

        ReferenceInvokerFactory invokerFactory;

        if (async) {
            invokerFactory = new AsyncReferenceInvokerFacotory(name, connector, context.getBean(callbackId, RpcCallback.class));
        } else {
            invokerFactory = new SyncReferenceInvokerFactory(name, connector);
        }

        reference = new Reference<>(Class.forName(interfaceName), invokerFactory);

        return reference.getProxyObject();
    }

    private ExecutorManager getExecutorManager() {
        if (executorManager != null) {
            return executorManager;
        }
        if (!context.containsBean(EXECUTOR_MANAGER_ID)) {
            destroyExecutorManager = true;
            executorManager = new ExecutorManager();
        } else {
            executorManager = context.getBean(EXECUTOR_MANAGER_ID, ExecutorManager.class);
        }
        return executorManager;
    }

    @Override
    public void destroy() {
        if (reference != null) {
            reference.destroy();
        }
        if (destroyExecutorManager) {
            executorManager.destroy();
        }
    }

    /**
     * Gets object type.
     *
     * @return the object type
     */
    @Override
    public Class<?> getObjectType() {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new PrettyRpcException(Code.INIT_ERROR, "初始化错误", e);
        }
        return clazz;
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
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
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
     * Getter method for property <tt>async</tt>.
     *
     * @return property value of async
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Setter method for property <tt>async</tt>.
     *
     * @param async value to be assigned to property async
     */
    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * Getter method for property <tt>callbackId</tt>.
     *
     * @return property value of callbackId
     */
    public String getCallbackId() {
        return callbackId;
    }

    /**
     * Setter method for property <tt>callbackId</tt>.
     *
     * @param callbackId value to be assigned to property callbackId
     */
    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }


    /**
     * Getter method for property <tt>persist</tt>.
     *
     * @return property value of persist
     */
    public boolean isPersist() {
        return persist;
    }

    /**
     * Setter method for property <tt>persist</tt>.
     *
     * @param persist value to be assigned to property persist
     */
    public void setPersist(boolean persist) {
        this.persist = persist;
    }

    /**
     * Getter method for property <tt>idleHeartbeatTimeout</tt>.
     *
     * @return property value of idleHeartbeatTimeout
     */
    public long getIdleHeartbeatTimeout() {
        return idleHeartbeatTimeout;
    }

    /**
     * Setter method for property <tt>idleHeartbeatTimeout</tt>.
     *
     * @param idleHeartbeatTimeout value to be assigned to property idleHeartbeatTimeout
     */
    public void setIdleHeartbeatTimeout(long idleHeartbeatTimeout) {
        this.idleHeartbeatTimeout = idleHeartbeatTimeout;
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
     * Getter method for property <tt>reference</tt>.
     *
     * @return property value of reference
     */
    public Reference<?> getReference() {
        return reference;
    }

    /**
     * Sets application context.
     *
     * @param applicationContext the application context
     * @throws BeansException the beans exception
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}


