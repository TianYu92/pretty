package edu.ecnu.yt.pretty.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ecnu.yt.pretty.publisher.Publisher;

public class PublisherStarter {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("META-INF/pretty-publisher-test.xml");
			context.getBean(Publisher.class);
			System.in.read();
		} finally {
			context.close();
		}
	}
}
