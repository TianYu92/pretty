package edu.ecnu.yt.pretty.spring.parser;

import java.util.ArrayList;
import java.util.List;

import edu.ecnu.yt.pretty.spring.util.XmlConstant;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.ecnu.yt.pretty.spring.beans.PublisherProxyFactoryBean;
import edu.ecnu.yt.pretty.spring.util.ServiceConfig;

import static edu.ecnu.yt.pretty.spring.util.XmlConstant.*;

/**
 * @author yt
 * @date 2017-12-23 14:31:22
 */
public class PublisherBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		return PublisherProxyFactoryBean.class;
	}
	
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		
		addProperty(element, builder, PORT);
		addProperty(element, builder, HEARTBEAT_TIMEOUT);
		addProperty(element, builder, IDLE_TIME_FOR_HEARTBEAT);
		
		NodeList nodeList = element.getElementsByTagName(SERVICE_TAG);
		
		List<ServiceConfig> configs = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element child = (Element) nodeList.item(i);
			ServiceConfig config = new ServiceConfig();

			// optional
			if (child.hasAttribute(NAME)) {
				config.setName(child.getAttribute(NAME));
			} else {
				config.setName(child.getAttribute(INTERFACE_NAME));
			}

			config.setInterfaceName(child.getAttribute(INTERFACE_NAME));
			builder.addDependsOn(child.getAttribute(REF));
			config.setRef(child.getAttribute(REF));
			configs.add(config);
		}
		builder.addPropertyValue(SERVICE_CONFIGS, configs);
	}

	private void addProperty(Element element, BeanDefinitionBuilder builder, String prop) {
		if (element.hasAttribute(prop)) {
			builder.addPropertyValue(prop, element.getAttribute(prop));
		}
	}
}
