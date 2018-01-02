/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.invoker.impl;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.common.message.RequestBody;
import edu.ecnu.yt.pretty.common.util.SignatureGenerator;
import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvoker;
import edu.ecnu.yt.pretty.reference.strategy.Strategy;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author yt
 * @version $Id: AbstractReferenceInvoker.java, v 0.1 2017年12月13日 下午12:05 yt Exp $
 */
public abstract class AbstractReferenceInvoker implements ReferenceInvoker {

    private         String                            serviceName;
    protected       ReferenceConnector                connector;
    private         LinkedHashMap<String, Strategy>   strategyMap = new LinkedHashMap<>();
    protected       CompletableFuture<PrettyResponse> rpcFuture;

    @Override
    public void registryStrategy(String name, Strategy strategy) {
        if (strategyMap.containsKey(name)) {
            strategy.destroy();
        }
        strategyMap.put(name, strategy);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 策略预处理
        preHandle();

        RequestBody body = getBody(method, args);

        // 执行RPC命令
        if (isVoidReturnType(method.getReturnType())) {
            // 单向命令
            this.connector.sendMessageOneWay(body);
            return null;
        } else {
            // 需要回调结果的命令
            this.rpcFuture = this.connector.sendMessage(body);

            afterInvoke(proxy, method, args);

            rpcFuture.whenComplete((resp, e) -> {
                if (e != null) {
                    handleException(e);
                } else {
                    postHandle(resp);
                }
            });

            return getResult();
        }
    }

    protected void afterInvoke(Object proxy, Method method, Object[] args) {
        // do nothing
    }

    protected RequestBody getBody(Method method, Object[] params) {
        String signature = SignatureGenerator.generateParamDescriptor(method.getName(),
                method.getParameterTypes());
        RequestBody requestBody = new RequestBody();
        requestBody.setServiceName(getServiceName());
        requestBody.setMethod(signature);
        requestBody.setParams(params);
        return requestBody;
    }

    protected void preHandle() {
        for (Strategy strategy : strategyMap.values()) {
            if (!strategy.preHandle(this)) {
                throw new PrettyRpcException(Code.STRATEGY_PRE_HANDLE_ERROR, strategy.getFailedReason());
            }
        }
    }

    protected void handleException(Throwable e) {
        for (Strategy strategy : strategyMap.values()) {
            strategy.exceptionCaught(e);
        }
    }

    protected void postHandle(PrettyResponse response) {
        for (Strategy strategy : strategyMap.values()) {
            if (!strategy.postHandle(this, response)) {
                throw new PrettyRpcException(Code.STRATEGY_POST_HANDLE_ERROR, strategy.getFailedReason());
            }
        }
    }

    protected boolean isVoidReturnType(Class<?> retType) {
        return retType != null && Void.class.equals(retType) || void.class.equals(retType);
    }

    /**
     * Sets connector.
     *
     * @param connector the connector
     */
    @Override
    public void setConnector(ReferenceConnector connector) {
        this.connector = connector;
    }

    /**
     * Gets connector.
     *
     * @return the connector
     */
    @Override
    public ReferenceConnector getConnector() {
        return null;
    }

    /**
     * Sets service name.
     *
     * @param serviceName the service name
     */
    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Gets service name.
     *
     * @return the service name
     */
    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void destroy() {
        this.connector.destroy();
    }
}