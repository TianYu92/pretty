package edu.ecnu.yt.pretty.test;

/**
 * @author yt
 * @date 2017-12-23 18:16:20
 */
public interface TestService {

	/**
	 * Hello test pojo.
	 *
	 * @return the test pojo
	 */
	TestPOJO hello(TestPOJO testPOJO);

	/**
	 * Oops test pojo.
	 *
	 * @param fatal the fatal
	 * @return the test pojo
	 */
	TestPOJO oops(boolean fatal);
}
