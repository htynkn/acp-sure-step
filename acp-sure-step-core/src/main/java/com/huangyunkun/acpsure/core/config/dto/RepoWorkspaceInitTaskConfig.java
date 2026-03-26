package com.huangyunkun.acpsure.core.config.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepoWorkspaceInitTaskConfig extends BaseTaskConfig {
    private List<String> repos;
    private String workspace;
}
