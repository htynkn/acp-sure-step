package com.huangyunkun.acpsure.core.util;

import java.util.Map;

public class VariableSubstitutionUtil {

    private VariableSubstitutionUtil() {
    }

    public static String substitute(String command, Map<String, String> variables) {
        if (command == null || variables == null || variables.isEmpty()) {
            return command;
        }
        String result = command;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
