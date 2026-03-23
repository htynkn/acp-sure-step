package com.huangyunkun.acpsure.core.config.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AcpInitTaskConfig.class, name = "acpInit"),
        @JsonSubTypes.Type(value = AcpExecTaskConfig.class, name = "acpExec"),
        @JsonSubTypes.Type(value = BashExecTaskConfig.class, name = "bashExec")
})
public class BaseTaskConfig {
    private String id;
    private TaskEnum type;
}
