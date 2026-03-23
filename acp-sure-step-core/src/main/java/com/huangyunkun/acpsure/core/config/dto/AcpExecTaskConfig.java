package com.huangyunkun.acpsure.core.config.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcpExecTaskConfig extends BaseTaskConfig {
    private String prompt;
}
