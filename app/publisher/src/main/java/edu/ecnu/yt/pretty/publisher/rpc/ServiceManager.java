package edu.ecnu.yt.pretty.publisher.rpc;

import edu.ecnu.yt.pretty.common.message.PrettyResponse;

import java.util.List;

public interface ServiceManager extends AsyncStub<PrettyResponse> {

	PrettyService getService(String name);

	boolean registryService(PrettyService service);

	boolean registryService(String name, PrettyService service);

	List<String> listAllServices();

	void close();
}
