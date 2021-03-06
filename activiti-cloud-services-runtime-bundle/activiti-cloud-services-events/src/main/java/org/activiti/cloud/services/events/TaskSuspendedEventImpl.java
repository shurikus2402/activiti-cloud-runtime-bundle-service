/*
 * Copyright 2018 Alfresco and/or its affiliates.
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
 *
 */

package org.activiti.cloud.services.events;



import org.activiti.cloud.services.api.model.Task;

public class TaskSuspendedEventImpl extends AbstractProcessEngineEvent implements TaskSuspendedEvent {

    private Task task;

    public TaskSuspendedEventImpl() {
    }

    public TaskSuspendedEventImpl(String appName, String appVersion, String serviceName, String serviceFullName, String serviceType, String serviceVersion,

                                  String executionId,
                                  String processDefinitionId,
                                  String processInstanceId,
                                  Task task) {
        super(appName,appVersion,serviceName,serviceFullName,serviceType,serviceVersion,

              executionId,
              processDefinitionId,
              processInstanceId);
        this.task = task;
    }

    @Override
    public Task getTask() {
        return task;
    }

    @Override
    public String getEventType() {
        return "TaskSuspendedEvent";
    }

    @Override
    public String toString() {
        return "TaskSuspendedEventImpl{" +
                "task=" + task +
                ", appName='" + getAppName() + '\'' +
                ", appVersion='" + getAppVersion() + '\'' +
                ", serviceName='" + getServiceName() + '\'' +
                ", serviceFullName='" + getServiceFullName() + '\'' +
                ", serviceType='" + getServiceType() + '\'' +
                ", serviceVersion='" + getServiceVersion() + '\'' +
                ", executionId='" + getExecutionId() + '\'' +
                ", processDefinitionId='" + getProcessDefinitionId() + '\'' +
                ", processInstanceId='" + getProcessInstanceId() + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
