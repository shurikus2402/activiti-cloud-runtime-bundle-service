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

package org.activiti.cloud.starter.tests.runtime;

import java.util.HashMap;
import java.util.Map;
import org.activiti.cloud.services.events.configuration.RuntimeBundleProperties;
import org.activiti.services.connectors.model.IntegrationRequestEvent;
import org.activiti.services.connectors.model.IntegrationResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(ConnectorIntegrationChannels.class)
public class ServiceTaskConsumerHandler {

    private final BinderAwareChannelResolver resolver;
    private final RuntimeBundleProperties runtimeBundleProperties;

    @Autowired
    public ServiceTaskConsumerHandler(BinderAwareChannelResolver resolver, RuntimeBundleProperties runtimeBundleProperties) {
        this.resolver = resolver;
        this.runtimeBundleProperties = runtimeBundleProperties;
    }

    @StreamListener(value = ConnectorIntegrationChannels.INTEGRATION_EVENTS_CONSUMER)
    public void receive(IntegrationRequestEvent integrationRequestEvent) {
        Map<String, Object> requestVariables = integrationRequestEvent.getVariables();
        String variableToUpdate = "age";

        HashMap<String, Object> resultVariables = new HashMap<>();
        resultVariables.put(variableToUpdate,
                            ((Integer) requestVariables.get(variableToUpdate)) + 1);
        Message<IntegrationResultEvent> message = MessageBuilder.withPayload(new IntegrationResultEvent(integrationRequestEvent.getExecutionId(),
                                                                                                        resultVariables,
                                                                                                        runtimeBundleProperties.getAppName(),
                                                                                                        runtimeBundleProperties.getAppVersion(),
                                                                                                        runtimeBundleProperties.getServiceName(),
                                                                                                        runtimeBundleProperties.getServiceFullName(),
                                                                                                        runtimeBundleProperties.getServiceType(),
                                                                                                        runtimeBundleProperties.getServiceVersion())).build();
        resolver.resolveDestination("integrationResult:" + runtimeBundleProperties.getServiceFullName()).send(message);
    }
}
