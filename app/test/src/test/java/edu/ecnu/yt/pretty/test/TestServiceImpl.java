package edu.ecnu.yt.pretty.test;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;

/**
 * @author yt
 * @date 2017-12-12 16:03:40
 */
public class TestServiceImpl implements TestService {

	@Override
	public TestPOJO hello() {
		return null;
	}

	@Override
	public TestPOJO oops(boolean fatal) {
		if (fatal) {
			throw new PrettyRpcException(Code.SERVICE_EXECUTION_ERROR, "fvk !!!!");
		} else {
			return new TestPOJO("TEST", 123);
		}
	}
}
