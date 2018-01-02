package edu.ecnu.yt.pretty.spring.parser;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParser());
		registerBeanDefinitionParser("publisher", new PublisherBeanDefinitionParser());
	}

}
