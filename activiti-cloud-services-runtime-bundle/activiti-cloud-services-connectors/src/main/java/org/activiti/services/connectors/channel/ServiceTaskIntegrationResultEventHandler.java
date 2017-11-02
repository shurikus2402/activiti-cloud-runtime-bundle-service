/*
 * Copyright 2017 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.services.connectors.channel;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextEntity;
import org.activiti.engine.integration.IntegrationContextService;
import org.activiti.services.connectors.model.IntegrationResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(ProcessEngineIntegrationChannels.class)
public class ServiceTaskIntegrationResultEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskIntegrationResultEventHandler.class);

    private final RuntimeService runtimeService;
    private final IntegrationContextService integrationContextService;

    @Autowired
    public ServiceTaskIntegrationResultEventHandler(RuntimeService runtimeService,
                                                    IntegrationContextService integrationContextService) {
        this.runtimeService = runtimeService;
        this.integrationContextService = integrationContextService;
    }

    @StreamListener(ProcessEngineIntegrationChannels.INTEGRATION_RESULTS_CONSUMER)
    public synchronized void receive(IntegrationResultEvent integrationResultEvent) {
        IntegrationContextEntity integrationContext = integrationContextService.findIntegrationContextByExecutionId(integrationResultEvent.getExecutionId());

        if (integrationContext != null) {
            integrationContextService.deleteIntegrationContext(integrationContext);
            runtimeService.trigger(integrationContext.getExecutionId(),
                                   integrationResultEvent.getVariables());
        } else {
            LOGGER.warn("No task is waiting for integration result with execution id `" +
                                integrationResultEvent.getExecutionId() +
                                "`. The integration result `" + integrationResultEvent.getId() + "` will be ignored." );
        }
    }
}