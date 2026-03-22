package com.huangyunkun.config.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AcpInitTaskConfig extends BaseTaskConfig{
    private String command;
    private List<String> args;
    private Map<String, String> env;
}
