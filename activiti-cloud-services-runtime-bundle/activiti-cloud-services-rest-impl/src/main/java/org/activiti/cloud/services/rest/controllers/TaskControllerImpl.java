/*
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

package org.activiti.cloud.services.rest.controllers;

import java.util.Map;

import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.services.api.commands.ClaimTaskCmd;
import org.activiti.cloud.services.api.commands.CompleteTaskCmd;
import org.activiti.cloud.services.api.commands.CreateTaskCmd;
import org.activiti.cloud.services.api.commands.ReleaseTaskCmd;
import org.activiti.cloud.services.api.commands.UpdateTaskCmd;
import org.activiti.cloud.services.api.model.Task;
import org.activiti.cloud.services.api.model.converter.TaskConverter;
import org.activiti.cloud.services.core.AuthenticationWrapper;
import org.activiti.cloud.services.core.ProcessEngineWrapper;
import org.activiti.cloud.services.rest.api.TaskController;
import org.activiti.cloud.services.rest.api.resources.TaskResource;
import org.activiti.cloud.services.rest.assemblers.TaskResourceAssembler;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class TaskControllerImpl implements TaskController {

    private ProcessEngineWrapper processEngine;

    private final TaskResourceAssembler taskResourceAssembler;

    private AuthenticationWrapper authenticationWrapper;

    private final AlfrescoPagedResourcesAssembler<Task> pagedResourcesAssembler;

    private final TaskConverter taskConverter;

    @Autowired
    public TaskControllerImpl(ProcessEngineWrapper processEngine,
                              TaskResourceAssembler taskResourceAssembler,
                              AuthenticationWrapper authenticationWrapper,
                              AlfrescoPagedResourcesAssembler<Task> pagedResourcesAssembler,
                              TaskConverter taskConverter) {
        this.authenticationWrapper = authenticationWrapper;
        this.processEngine = processEngine;
        this.taskResourceAssembler = taskResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.taskConverter = taskConverter;
    }

    @ExceptionHandler(ActivitiObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppException(ActivitiObjectNotFoundException ex) {
        return ex.getMessage();
    }

    @Override
    public PagedResources<TaskResource> getTasks(Pageable pageable) {
        Page<Task> page = processEngine.getTasks(pageable);
        return pagedResourcesAssembler.toResource(pageable, page,
                                                  taskResourceAssembler);
    }

    @Override
    public Resource<Task> getTaskById(@PathVariable String taskId) {
        Task task = processEngine.getTaskById(taskId);
        if (task == null) {
            throw new ActivitiObjectNotFoundException("Unable to find task for the given id: " + taskId);
        }
        return taskResourceAssembler.toResource(task);
    }

    @Override
    public Resource<Task> claimTask(@PathVariable String taskId) {
        String assignee = authenticationWrapper.getAuthenticatedUserId();
        if (assignee == null) {
            throw new IllegalStateException("Assignee must be resolved from the Identity/Security Layer");
        }

        return taskResourceAssembler.toResource(processEngine.claimTask(new ClaimTaskCmd(taskId,
                                                                                         assignee)));
    }

    @Override
    public Resource<Task> releaseTask(@PathVariable String taskId) {

        return taskResourceAssembler.toResource(processEngine.releaseTask(new ReleaseTaskCmd(taskId)));
    }

    @Override
    public ResponseEntity<Void> completeTask(@PathVariable String taskId,
                                             @RequestBody(required = false) CompleteTaskCmd completeTaskCmd) {
        Map<String, Object> outputVariables = null;
        if (completeTaskCmd != null) {
            outputVariables = completeTaskCmd.getOutputVariables();
        }
        processEngine.completeTask(new CompleteTaskCmd(taskId,
                                                       outputVariables));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public void deleteTask(@PathVariable String taskId) {
        processEngine.deleteTask(taskId);
    }

    @Override
    public Resource<Task> createNewTask(@RequestBody CreateTaskCmd createTaskCmd) {
        return taskResourceAssembler.toResource(processEngine.createNewTask(createTaskCmd));
    }

    @Override
    public ResponseEntity<Void> updateTask(@PathVariable String taskId,
                                           @RequestBody UpdateTaskCmd updateTaskCmd) {
        processEngine.updateTask(taskId,
                                 updateTaskCmd);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Resource<Task> createSubtask(@PathVariable String taskId,
                                        @RequestBody CreateTaskCmd createSubtaskCmd) {

        return taskResourceAssembler.toResource(processEngine.createNewSubtask(taskId,
                                                                               createSubtaskCmd));
    }

    @Override
    public Resources<TaskResource> getSubtasks(@PathVariable String taskId) {

        return new Resources<>(taskResourceAssembler.toResources(taskConverter.from(processEngine.getSubtasks(taskId))),
                               linkTo(TaskControllerImpl.class).withSelfRel());
    }

    public AuthenticationWrapper getAuthenticationWrapper() {
        return authenticationWrapper;
    }
    
    public void setAuthenticationWrapper(AuthenticationWrapper authenticationWrapper) {
        this.authenticationWrapper = authenticationWrapper;
    }
}
