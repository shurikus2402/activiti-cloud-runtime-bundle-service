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

package org.activiti.services.connectors.model;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegrationResultEvent {

    private String id;

    private String executionId;

    private String flowNodeId;

    private Map<String, Object> variables;

    private String appName;
    private String appVersion;
    private String serviceName;
    private String serviceFullName;
    private String serviceType;
    private String serviceVersion;


    //used by json deserialization
    public IntegrationResultEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public IntegrationResultEvent(String executionId,
                                  Map<String, Object> variables,
                                  String appName,
                                  String appVersion,
                                  String serviceName,
                                  String serviceFullName,
                                  String serviceType,
                                  String serviceVersion) {
        this();
        this.executionId = executionId;
        this.variables = variables;
        this.appName = appName;
        this.appVersion = appVersion;
        this.serviceName = serviceName;
        this.serviceFullName = serviceFullName;
        this.serviceType = serviceType;
        this.serviceVersion = serviceVersion;
    }

    public String getId() {
        return id;
    }

    public String getExecutionId() {
        return executionId;
    }

    public String getFlowNodeId() {
        return flowNodeId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceFullName() {
        return serviceFullName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }
}
