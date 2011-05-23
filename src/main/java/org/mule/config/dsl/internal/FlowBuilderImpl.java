/*
 * ---------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.config.dsl.internal;

import com.google.inject.Injector;
import org.mule.api.MuleContext;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.source.MessageSource;
import org.mule.config.dsl.FlowBuilder;
import org.mule.config.dsl.InboundEndpointBuilder;
import org.mule.config.dsl.internal.util.PropertyPlaceholder;
import org.mule.construct.SimpleFlowConstruct;

import static org.mule.config.dsl.internal.util.Preconditions.checkNotEmpty;

public class FlowBuilderImpl extends PipelineBuilderImpl<FlowBuilder> implements FlowBuilder {

    private final String name;
    private InboundEndpointBuilderImpl<FlowBuilder> inboundEndpointBuilder;


    public FlowBuilderImpl(String name) {
        super(null);
        this.name = checkNotEmpty(name, "name");
    }

    public InboundEndpointBuilder<FlowBuilder> from(String uri) {
        this.inboundEndpointBuilder = new InboundEndpointBuilderImpl<FlowBuilder>(this, uri);
        return inboundEndpointBuilder;
    }

    public FlowConstruct build(MuleContext muleContext, Injector injector, PropertyPlaceholder placeholder) {
        final SimpleFlowConstruct flow = new SimpleFlowConstruct(name, muleContext);
        if (inboundEndpointBuilder != null) {
            flow.setMessageSource((MessageSource) inboundEndpointBuilder.build(muleContext, injector, placeholder));
        }
        if (!isProcessorListEmpty()) {
            flow.setMessageProcessors(buildProcessorList(muleContext, injector, placeholder));
        }
        return flow;
    }
}
