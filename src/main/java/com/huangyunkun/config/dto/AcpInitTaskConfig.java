package com.huangyunkun.config.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcpInitTaskConfig extends BaseTaskConfig{
    private String command;
    private List<String> args;
    private Map<String, String> env;
}
