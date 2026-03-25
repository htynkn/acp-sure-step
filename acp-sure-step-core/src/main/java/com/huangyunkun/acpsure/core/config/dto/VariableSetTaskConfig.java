package com.huangyunkun.acpsure.core.config.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableSetTaskConfig extends BaseTaskConfig {
    private Map<String, String> variables;
}
