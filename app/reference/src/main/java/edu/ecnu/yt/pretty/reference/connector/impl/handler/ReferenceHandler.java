package edu.ecnu.yt.pretty.reference.connector.impl.handler;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.message.PrettyRequest;
import edu.ecnu.yt.pretty.common.message.PrettyResponse;
import edu.ecnu.yt.pretty.reference.service.ExecutorManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yt
 * @date 2017-12-12 19:47:15
 */
public class ReferenceHandler extends SimpleChannelInboundHandler<PrettyResponse> {

    private final ExecutorManager executorManager;
    private Map<Long, CompletableFuture<PrettyResponse>> futureMap = new ConcurrentHashMap<>();
    private volatile ChannelHandlerContext ctx = null;

    public ReferenceHandler(ExecutorManager executorManager) {
        this.executorManager = executorManager;
    }

    /**
     * Is sharable boolean.
     *
     * @return the boolean
     */
//    @Override
//    public boolean isSharable() {
//        return true;
//    }

    /**
     * Channel active.
     *
     * @param ctx the ctx
     * @throws Exception the exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    /**
     * Channel inactive.
     *
     * @param ctx the ctx
     * @throws Exception the exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        this.ctx = null;
    }

    /**
     * Call completable future.
     *
     * @param request the request
     * @return the completable future
     */
    public CompletableFuture<PrettyResponse> call(PrettyRequest request) {

        if (ctx == null || !ctx.channel().isActive()) {
            throw new PrettyRpcException(Code.COMMUNICATION_ERROR, "连接中断，无法发送消息：" + request);
        }

        CompletableFuture<PrettyResponse> future = new CompletableFuture<>();

        long messageId = request.header().messageId();
        futureMap.put(messageId, future);

        registerTimeoutTask(messageId);

        ctx.writeAndFlush(request);
        return future;
    }

    private void registerTimeoutTask(long messageId) {
        executorManager.getScheduledExecutors().schedule(() -> {
            CompletableFuture<PrettyResponse> timeoutFuture = futureMap.remove(messageId);
            if (timeoutFuture != null) {
                timeoutFuture.completeExceptionally(new PrettyRpcException(Code.INVOKING_ERROR, "timeout"));
            }
        }, 20, TimeUnit.SECONDS);
    }

    /**
     * Notify.
     *
     * @param request the request
     */
    public void notify(PrettyRequest request) {
        if (ctx == null || !ctx.channel().isActive()) {
            throw new PrettyRpcException(Code.COMMUNICATION_ERROR, "连接中断，无法发送消息：" + request);
        }
        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PrettyResponse resp) {
        CompletableFuture<PrettyResponse> future = futureMap.remove(resp.header().messageId());
        if (future != null) {
            executorManager.getServiceExecutors().execute(() -> {
                Code code = resp.body().getCode();
                if (code.equals(Code.OK)) {
                    future.complete(resp);
                } else {
                    PrettyRpcException pre = new PrettyRpcException(code,
                            resp.body().getResult().toString());
                    future.completeExceptionally(pre);
                }
            });
        }
        ctx.fireChannelRead(resp);
        // TODO handle { future == null }
    }

    /**
     * Exception caught.
     *
     * @param ctx   the ctx
     * @param cause the cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public static String getName() {
        return "reference-connector";
    }

}