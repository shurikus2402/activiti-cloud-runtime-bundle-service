/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
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

package org.activiti.cloud.services.events.converter;

import org.activiti.cloud.services.api.events.ProcessEngineEvent;
import org.activiti.cloud.services.api.model.converter.ProcessInstanceConverter;
import org.activiti.cloud.services.events.ProcessActivatedEventImpl;
import org.activiti.cloud.services.events.configuration.RuntimeBundleProperties;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.activiti.engine.delegate.event.ActivitiEventType.ENTITY_ACTIVATED;

@Component
public class ProcessActivatedEventConverter extends AbstractEventConverter {

    private final ProcessInstanceConverter processInstanceConverter;

    @Autowired
    public ProcessActivatedEventConverter(ProcessInstanceConverter processInstanceConverter,
                                          RuntimeBundleProperties runtimeBundleProperties) {
        super(runtimeBundleProperties);
        this.processInstanceConverter = processInstanceConverter;
    }

    @Override
    public ProcessEngineEvent from(ActivitiEvent event) {
        return new ProcessActivatedEventImpl(getRuntimeBundleProperties().getAppName(),
                                            getRuntimeBundleProperties().getAppVersion(),
                                            getRuntimeBundleProperties().getServiceName(),
                                            getRuntimeBundleProperties().getServiceFullName(),
                                            getRuntimeBundleProperties().getServiceType(),
                                            getRuntimeBundleProperties().getServiceVersion(),
                                            event.getExecutionId(),
                                             event.getProcessDefinitionId(),
                                             event.getProcessInstanceId(),
                                             processInstanceConverter.from(((ExecutionEntityImpl) ((ActivitiEntityEvent) event).getEntity()).getProcessInstance()));
    }

    @Override
    public String handledType() {
        return "ProcessInstance:" + ENTITY_ACTIVATED.toString();
    }
}
