package com.huangyunkun.acpsure.core.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class VariableSubstitutionUtil {

    private VariableSubstitutionUtil() {
    }

    public static String substitute(String command, Map<String, String> variables) {
        if (command == null || variables == null || variables.isEmpty()) {
            return command;
        }
        return StringSubstitutor.replace(command, variables);
    }
}
