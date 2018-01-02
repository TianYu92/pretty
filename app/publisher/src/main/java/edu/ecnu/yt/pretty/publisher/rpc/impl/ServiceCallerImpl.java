package edu.ecnu.yt.pretty.publisher.rpc.impl;

import edu.ecnu.yt.pretty.common.message.PrettyMessage;
import edu.ecnu.yt.pretty.common.message.PrettyRequest;
import edu.ecnu.yt.pretty.common.message.RequestBody;
import edu.ecnu.yt.pretty.publisher.rpc.ServiceCaller;
import edu.ecnu.yt.pretty.publisher.rpc.ServiceManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServiceCallerImpl 
		extends SimpleChannelInboundHandler<PrettyRequest> implements ServiceCaller {

	ServiceManager serviceManager;
	
	public ServiceCallerImpl(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	@Override
	public boolean isSharable() {	
		return true;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PrettyRequest request) throws Exception {
		final Channel ch = ctx.channel();
		serviceManager.callService(request, resp -> ch.writeAndFlush(resp));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
	}

	public static String getName() {
		return "service-caller";
	}
}
