package edu.ecnu.yt.pretty.spring.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import edu.ecnu.yt.pretty.spring.beans.ReferenceProxyFactoryBean;

import static edu.ecnu.yt.pretty.spring.util.XmlConstant.*;

/**
 * @author yt
 * @date 2017-12-26 9:49:14
 */
public class ReferenceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		return ReferenceProxyFactoryBean.class;
	}
	
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {

		builder.addPropertyValue(INTERFACE_NAME, element.getAttribute(INTERFACE_NAME));

		addProperty(element, builder, HOST);
		addProperty(element, builder, PORT);
		addProperty(element, builder, ASYNC);
		addProperty(element, builder, CALLBACK_ID);
		addProperty(element, builder, PERSIST);
		addProperty(element, builder, IDLE_TIME_FOR_HEARTBEAT);
		addProperty(element, builder, HEARTBEAT_TIMEOUT);

		if (element.hasAttribute(NAME)) {
			builder.addPropertyValue(NAME, element.getAttribute(NAME));
		} else {
			builder.addPropertyValue(NAME, element.getAttribute(INTERFACE_NAME));
		}

	}

	private void addProperty(Element element, BeanDefinitionBuilder builder, String prop) {
		if (element.hasAttribute(prop)) {
			builder.addPropertyValue(prop, element.getAttribute(prop));
		}
	}

}
