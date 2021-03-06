/*
 * ---------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.config.dsl;

import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.source.MessageSource;
import org.mule.construct.Flow;
import org.mule.routing.MessageFilter;
import org.mule.routing.filters.ExpressionFilter;
import org.mule.routing.filters.WildcardFilter;

import java.util.Iterator;

import static org.fest.assertions.Assertions.assertThat;
import static org.mule.config.dsl.expression.CoreExpr.string;
import static org.mule.config.dsl.expression.CoreExpr.wildcard;

public class TestExpressionFilter {

    @Test
    public void testChainFilterExpression() {
        final MuleContext muleContext = Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .filter(wildcard("foo*"))
                        .filter(string("'some text here'"));
            }
        }).advanced().muleContext();

        assertThat(muleContext.getRegistry().lookupFlowConstructs()).isNotEmpty().hasSize(1);

        final FlowConstruct flowConstruct = muleContext.getRegistry().lookupFlowConstructs().iterator().next();

        assertThat(flowConstruct.getName()).isEqualTo("MyFlow");
        assertThat(flowConstruct).isInstanceOf(Flow.class);

        final MessageSource messageSource = ((Flow) flowConstruct).getMessageSource();

        assertThat(messageSource).isNotNull().isInstanceOf(InboundEndpoint.class);

        final InboundEndpoint inboundEndpoint = (InboundEndpoint) messageSource;

        assertThat(inboundEndpoint.getExchangePattern()).isEqualTo(MessageExchangePattern.ONE_WAY);

        assertThat(inboundEndpoint.getProtocol()).isNotNull().isEqualTo("file");

        assertThat(inboundEndpoint.getAddress()).isNotNull().isEqualTo("file:///Users/porcelli/test");

        assertThat(((Flow) flowConstruct).getMessageProcessors()).isNotEmpty().hasSize(2);

        final Iterator<MessageProcessor> iterator = ((Flow) flowConstruct).getMessageProcessors().iterator();

        final MessageProcessor filterProcessor = iterator.next();

        assertThat(filterProcessor).isNotNull().isInstanceOf(MessageFilter.class);

        final MessageFilter filter = (MessageFilter) filterProcessor;

        assertThat(filter.getFilter()).isInstanceOf(WildcardFilter.class);

        final WildcardFilter exprFilter = (WildcardFilter) filter.getFilter();

        assertThat(exprFilter.getPattern()).isEqualTo("foo*");

        final MessageProcessor filterProcessor2 = iterator.next();

        assertThat(filterProcessor2).isNotNull().isInstanceOf(MessageFilter.class);

        final MessageFilter filter2 = (MessageFilter) filterProcessor2;

        assertThat(filter2.getFilter()).isInstanceOf(ExpressionFilter.class);

        final ExpressionFilter exprFilter2 = (ExpressionFilter) filter2.getFilter();

        assertThat(exprFilter2.getExpression()).isEqualTo("'some text here'");
        assertThat(exprFilter2.getEvaluator()).isEqualTo("string");
        assertThat(exprFilter2.getCustomEvaluator()).isNull();
    }
}