package edu.ecnu.yt.pretty.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ecnu.yt.pretty.reference.Reference;
import edu.ecnu.yt.pretty.reference.connector.ReferenceConnector;
import edu.ecnu.yt.pretty.reference.connector.impl.PersistentReferenceConnector;
import edu.ecnu.yt.pretty.reference.connector.impl.TransientReferenceConnector;
import edu.ecnu.yt.pretty.reference.invoker.ReferenceInvokerFactory;
import edu.ecnu.yt.pretty.reference.invoker.impl.async.AsyncReferenceInvokerFacotory;
import edu.ecnu.yt.pretty.reference.invoker.impl.async.RpcCallback;
import edu.ecnu.yt.pretty.reference.service.ExecutorManager;

import java.lang.reflect.Method;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MultiConnectionTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(MultiConnectionTest.class);
    private static ExecutorManager executorManager = new ExecutorManager();

	static volatile boolean flag = true;
	
	static class Callback implements RpcCallback {
		
		private CountDownLatch cdl;
		
		public Callback(CountDownLatch cdl) {
			this.cdl = cdl;
		}

		@Override
		public void accept(Method method, Object result) {
//			System.out.println(result);
			cdl.countDown();
		}

		@Override
		public void exceptionCaught(Throwable e) {
		}
		
	}

	static volatile long time = 0;
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException {

        CyclicBarrier barrier = new CyclicBarrier(Config.THREAD_COUNT);
        CountDownLatch cdl = new CountDownLatch(Config.THREAD_COUNT * Config.CALL_TIMES);
        Callback callback = new Callback(cdl);
        
		try {
			TestService[] services = new TestService[Config.THREAD_COUNT];
			for (int i = 0; i < Config.THREAD_COUNT; i++) {
//				ReferenceConnector connector = new PersistentReferenceConnector(executorManager, "192.168.1.107", 1234, 15, 10, false);	        
				ReferenceConnector connector = new TransientReferenceConnector(executorManager, "192.168.1.107", 1234, false);
//		        ReferenceInvokerFactory invokerFactory = new SyncReferenceInvokerFactory("test", connector);
		        ReferenceInvokerFactory invokerFactory = new AsyncReferenceInvokerFacotory("test", connector, callback);
		        services[i] = (TestService) new Reference<>(Class.forName("edu.ecnu.yt.pretty.test.TestService"), invokerFactory).getProxyObject();
			}
	        
            TestPOJO testPOJO = new TestPOJO("123", 321);
            
            // warm up
//             
			for (int i = 0; i < Config.THREAD_COUNT; i++) {
				final TestService testService = services[i];
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						
						try {
							barrier.await();
						} catch (InterruptedException | BrokenBarrierException e) {
						}
						
						synchronized (MultiConnectionTest.class) {
							if (flag) {
								time = System.currentTimeMillis();
								flag = false;
								System.out.println("start call...");
							}
						}
						
						for (int i = 0; i < Config.CALL_TIMES; i++) {
							testService.hello(testPOJO);
						}
						
//						cdl.countDown();
					}
				});
				t.start();
			}
			cdl.await();
			time = System.currentTimeMillis() - time;
			System.out.println("total cost:" + time / 1000);
		} finally {
			System.out.println("done.");
		}
	}

}
