package edu.ecnu.yt.pretty.test;

import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ecnu.yt.pretty.publisher.Publisher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Starter {

    public static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);


	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = null;
		Publisher publisher = null;
		try {
			context = new ClassPathXmlApplicationContext("META-INF/pretty-test.xml");
			publisher = context.getBean(Publisher.class);

            TestService service2 = context.getBean("ref2", TestService.class);
			TestService service3 = context.getBean("ref3", TestService.class);
			//for (int i = 0; i < 5; i++) {
			//	if (LOGGER.isInfoEnabled()) {
			//	  LOGGER.info("{}", service2.hello());
			//	}
			//	Thread.sleep(8000);
			//}
			for (int i = 0; i < 5; i++) {
				service3.oops(false);
				Thread.sleep(12000);
			}

			Thread.sleep(30000);

		} finally {
			if (publisher != null) {
				publisher.close();
			}
			if (context != null) {
				context.close();
			}
		}

	}

}
