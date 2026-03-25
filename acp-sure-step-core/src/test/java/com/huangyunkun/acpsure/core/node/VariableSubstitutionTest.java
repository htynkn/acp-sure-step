package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.util.VariableSubstitutionUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class VariableSubstitutionTest {

    @Test
    void shouldSubstituteVariablesInBashCommand() {
        Map<String, String> variables = new HashMap<>();
        variables.put("TARGET", "world");

        String result = VariableSubstitutionUtil.substitute("echo hello ${TARGET}", variables);

        assertThat(result, is("echo hello world"));
    }

    @Test
    void shouldSubstituteMultipleVariables() {
        Map<String, String> variables = new HashMap<>();
        variables.put("NAME", "foo");
        variables.put("FILE", "bar.txt");

        String result = VariableSubstitutionUtil.substitute("echo ${NAME} > ${FILE}", variables);

        assertThat(result, is("echo foo > bar.txt"));
    }

    @Test
    void shouldReturnOriginalCommandWhenNoVariables() {
        String command = "echo hello world";
        String result = VariableSubstitutionUtil.substitute(command, new HashMap<>());

        assertThat(result, is(command));
    }

    @Test
    void shouldReturnOriginalCommandWhenVariablesIsNull() {
        String command = "echo hello ${TARGET}";
        String result = VariableSubstitutionUtil.substitute(command, null);

        assertThat(result, is(command));
    }

    @Test
    void shouldLeaveUnknownVariablesUnchanged() {
        Map<String, String> variables = new HashMap<>();
        variables.put("KNOWN", "value");

        String result = VariableSubstitutionUtil.substitute("echo ${KNOWN} and ${UNKNOWN}", variables);

        assertThat(result, is("echo value and ${UNKNOWN}"));
    }
}

