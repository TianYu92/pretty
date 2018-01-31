package edu.ecnu.yt.pretty.publisher.rpc;

import edu.ecnu.yt.pretty.common.message.PrettyRequest;

/**
 * @author yt
 * @date 2017-12-15 14:51:23
 */
public interface AsyncStub<T> {
	/**
	 * Call service.
	 *
	 * @param request  the request
	 * @param callback the callback
	 */
	void callService(PrettyRequest request, Callback<T> callback);
}
