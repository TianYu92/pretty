package edu.ecnu.yt.pretty.test.dubbo;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ecnu.yt.pretty.test.Config;
import edu.ecnu.yt.pretty.test.TestPOJO;
import edu.ecnu.yt.pretty.test.TestService;


public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"META-INF/dubbo-demo-consumer.xml"});
        context.start();
        // obtain proxy object for remote invocation
        TestService testService = (TestService) context.getBean("demoService");
        // execute remote invocation
        CountDownLatch cdl = new CountDownLatch(Config.THREAD_COUNT);
        TestPOJO testPOJO = new TestPOJO("123", 321);
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
    }
}