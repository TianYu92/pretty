package edu.ecnu.yt.pretty.reference;

import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvokerFactory;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvoker;

import java.lang.reflect.Proxy;

/**
 * A facade for Reference of Pretty RPC.
 * @author yt
 * @date 2017-12-12 19:40:00
 */
public class Reference<T> {

    private final ReferenceInvokerFactory invokerFactory;
    private final ReferenceInvoker invoker;
    private T proxyObject;

    /**
     * Constructor.
     *
     * @param proxyInterface the proxy interface
     * @param invokerFactory the invoker factory
     */
    public Reference(Class<T> proxyInterface, ReferenceInvokerFactory invokerFactory) {

        this.invokerFactory = invokerFactory;

        this.invoker = this.invokerFactory.createInvoker();

        this.proxyObject = (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(),
                new Class<?>[] { proxyInterface }, this.invoker);
    }

    /**
     * Gets proxy object.
     *
     * @return the proxy object
     */
    public T getProxyObject() {
        return this.proxyObject;
    }

    /**
     * Getter method for property <tt>invokerFactory</tt>.
     *
     * @return property value of invokerFactory
     */
    public ReferenceInvokerFactory getInvokerFactory() {
        return invokerFactory;
    }

    /**
     * Getter method for property <tt>invoker</tt>.
     *
     * @return property value of invoker
     */
    public ReferenceInvoker getInvoker() {
        return invoker;
    }

    /**
     * Destroy.
     */
    public void destroy() {
        this.invoker.destroy();
    }

}
