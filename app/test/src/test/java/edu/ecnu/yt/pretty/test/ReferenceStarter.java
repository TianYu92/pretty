package edu.ecnu.yt.pretty.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ReferenceStarter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ReferenceStarter.class);

	static volatile boolean flag = true;

	static volatile long time = 0;
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("META-INF/pretty-reference-test.xml");
            TestService testService = context.getBean("ref", TestService.class);
            TestPOJO testPOJO = new TestPOJO("123", 321);
            CyclicBarrier barrier = new CyclicBarrier(Config.THREAD_COUNT);
            CountDownLatch cdl = new CountDownLatch(Config.THREAD_COUNT);
            // warm up
            for (int i = 0; i < Config.WARM_UP_TIMES; i++) {
				testService.hello(testPOJO);
			}
			for (int i = 0; i < Config.THREAD_COUNT; i++) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							barrier.await();
						} catch (InterruptedException | BrokenBarrierException e) {
						}
						synchronized (ReferenceStarter.class) {
							if (flag) {
								time = System.currentTimeMillis();
								flag = false;
								System.out.println("start call...");
							}
						}
						
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
