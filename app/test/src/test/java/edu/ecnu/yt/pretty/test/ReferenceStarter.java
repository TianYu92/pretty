package edu.ecnu.yt.pretty.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

public class ReferenceStarter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ReferenceStarter.class);


	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("META-INF/pretty-reference-test.xml");
            TestService testService = context.getBean("ref", TestService.class);
            TestPOJO testPOJO = new TestPOJO("123", 321);
            CountDownLatch cdl = new CountDownLatch(Config.THREAD_COUNT);
			long time = System.currentTimeMillis();
			for (int i = 0; i < Config.THREAD_COUNT; i++) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < Config.CALL_TIMES; i++) {
							testService.hello(testPOJO);
						}
						cdl.countDown();
					}
				});
				t.start();
			}		
			cdl.await();
			time = System.currentTimeMillis() - time;
			System.out.println("total cost:" + time / 1000);
		} finally {
			if (context != null) {
				context.close();
			}
		}

	}

}
