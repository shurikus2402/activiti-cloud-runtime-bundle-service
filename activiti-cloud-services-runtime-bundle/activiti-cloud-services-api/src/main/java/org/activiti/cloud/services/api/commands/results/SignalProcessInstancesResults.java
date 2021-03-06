package org.activiti.cloud.services.api.commands.results;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SignalProcessInstancesResults implements CommandResults {

    private String id;
    private String commandId;

    public SignalProcessInstancesResults() {
        this.id = UUID.randomUUID().toString();
    }

    @JsonCreator
    public SignalProcessInstancesResults(@JsonProperty("commandId") String commandId) {
        this();
        this.commandId = commandId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCommandId() {
        return commandId;
    }
}
