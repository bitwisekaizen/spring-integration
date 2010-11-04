/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.xmpp.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

/**
 * This class parses the schema for XMPP support.
 *
 * @author Josh Long
 * @author Mark Fisher
 * @since 2.0
 */
public class XmppNamespaceHandler extends NamespaceHandlerSupport {

	private static final String PACKAGE_NAME = "org.springframework.integration.xmpp";

	public void init() {
		// connection
		registerBeanDefinitionParser("xmpp-connection", new XmppConnectionParser());

		// send/receive messages
		registerBeanDefinitionParser("message-inbound-channel-adapter", new XmppMessageInboundEndpointParser());
		registerBeanDefinitionParser("message-outbound-channel-adapter", new XmppMessageOutboundEndpointParser());

		// presence
		registerBeanDefinitionParser("roster-event-inbound-channel-adapter", new XmppRosterEventInboundEndpointParser());
		registerBeanDefinitionParser("roster-event-outbound-channel-adapter", new XmppRosterEventOutboundEndpointParser());

		registerBeanDefinitionParser("header-enricher", new XmppHeaderEnricherParser());
	}

	private static class XmppMessageInboundEndpointParser extends AbstractSingleBeanDefinitionParser {

		@Override
		protected String getBeanClassName(Element element) {
			return PACKAGE_NAME + ".messages.XmppMessageDrivenEndpoint";
		}

		@Override
		protected boolean shouldGenerateIdAsFallback() {
			return true;
		}

		@Override
		protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
			String connectionName = element.getAttribute("xmpp-connection");
			Assert.hasText(connectionName, "'xmpp-connection' must be defined");
			builder.addPropertyReference("xmppConnection", connectionName);
			IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "channel", "requestChannel");
			IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "extract-payload");
			IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "auto-startup");
		}
	}

	private static class XmppRosterEventInboundEndpointParser extends AbstractSingleBeanDefinitionParser {

		@Override
		protected String getBeanClassName(Element element) {
			return PACKAGE_NAME + ".presence.XmppRosterEventMessageDrivenEndpoint";
		}

		@Override
		protected boolean shouldGenerateIdAsFallback() {
			return true;
		}

		@Override
		protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
			String connectionName = element.getAttribute("xmpp-connection");
			Assert.hasText(connectionName, "'xmpp-connection' must be defined");
			builder.addPropertyReference("xmppConnection", connectionName);
			IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "channel", "requestChannel");
			IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "auto-startup");
		}
	}
}
