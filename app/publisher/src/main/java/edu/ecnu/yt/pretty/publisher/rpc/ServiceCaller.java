package edu.ecnu.yt.pretty.publisher.rpc;

import io.netty.channel.ChannelHandler;

public interface ServiceCaller extends ChannelHandler {
	
	static String getName() {
		return "service-caller";
	}
	
}
