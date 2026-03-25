---
layout: page
title: Use Cases
permalink: /use-cases/
---

ACP Sure Step is designed for scenarios where you need to orchestrate multi-step workflows that combine AI agent interactions with system operations. Here are some practical use cases.

## AI-Assisted Code Generation

Use an AI agent to generate code based on project context, then validate the output automatically.

**Workflow:**
1. Initialize the AI agent with a project workspace
2. Set variables for the target language and output paths
3. Send a code generation prompt to the agent
4. Run tests to validate the generated code
5. Conditionally proceed based on test results

```json
{
  "tasks": [
    {
      "id": "initAgent",
      "type": "acpInit",
      "command": "qwen",
      "args": ["--acp"],
      "workspace": "/projects/my-app"
    },
    {
      "id": "setVars",
      "type": "variableSet",
      "variables": {
        "LANGUAGE": "java",
        "OUTPUT": "/projects/my-app/src"
      }
    },
    {
      "id": "generateCode",
      "type": "acpExec",
      "prompt": "prompts/generate-service.txt"
    },
    {
      "id": "runTests",
      "type": "bashExecCondition",
      "bash": "cd /projects/my-app && mvn test -q && echo pass || echo fail",
      "expectedResult": "pass"
    }
  ]
}
```

```xml
<flow>
    <chain name="defaultChain">
        THEN(initAgent, setVars, generateCode,
             IF(runTests, reportSuccess, reportFailure));
    </chain>
</flow>
```

---

## Automated DevOps Pipeline

Combine shell commands with AI-powered decision making for deployment workflows.

**Workflow:**
1. Check system prerequisites with bash commands
2. Initialize an AI agent for deployment guidance
3. Run deployment scripts
4. Validate deployment success

```json
{
  "tasks": [
    {
      "id": "checkDocker",
      "type": "bashExecCondition",
      "bash": "docker info > /dev/null 2>&1 && echo yes || echo no",
      "expectedResult": "yes"
    },
    {
      "id": "setEnv",
      "type": "variableSet",
      "variables": {
        "APP_VERSION": "1.2.0",
        "DEPLOY_ENV": "staging"
      }
    },
    {
      "id": "buildImage",
      "type": "bashExec",
      "bash": "docker build -t myapp:${APP_VERSION} ."
    },
    {
      "id": "deploy",
      "type": "bashExec",
      "bash": "docker run -d --name myapp-${DEPLOY_ENV} myapp:${APP_VERSION}"
    },
    {
      "id": "healthCheck",
      "type": "bashExecCondition",
      "bash": "curl -s http://localhost:8080/health | grep -q UP && echo healthy || echo unhealthy",
      "expectedResult": "healthy"
    }
  ]
}
```

---

## Document Generation with AI

Leverage AI agents to generate documentation from source code, then post-process the results.

**Workflow:**
1. Set up the project workspace and variables
2. Initialize the AI agent
3. Send a documentation generation prompt
4. Post-process the generated docs with shell commands

```json
{
  "tasks": [
    {
      "id": "setVars",
      "type": "variableSet",
      "variables": {
        "PROJECT_ROOT": "/projects/my-library",
        "DOCS_DIR": "/projects/my-library/docs"
      }
    },
    {
      "id": "initAgent",
      "type": "acpInit",
      "command": "qwen",
      "args": ["--acp"],
      "workspace": "/projects/my-library"
    },
    {
      "id": "generateDocs",
      "type": "acpExec",
      "prompt": "prompts/generate-api-docs.txt"
    },
    {
      "id": "formatDocs",
      "type": "bashExec",
      "bash": "cd ${DOCS_DIR} && find . -name '*.md' -exec sed -i 's/\\r$//' {} +"
    }
  ]
}
```

```xml
<flow>
    <chain name="defaultChain">
        THEN(setVars, initAgent, generateDocs, formatDocs);
    </chain>
</flow>
```

---

## Environment Setup and Validation

Automate environment setup with a combination of system checks and AI-assisted configuration.

**Workflow:**
1. Check if required tools are installed
2. Set up project variables
3. Create directory structures
4. Initialize AI agent for configuration generation

```json
{
  "tasks": [
    {
      "id": "checkJava",
      "type": "bashExecCondition",
      "bash": "java -version 2>&1 | head -1 | grep -q '17' && echo yes || echo no",
      "expectedResult": "yes"
    },
    {
      "id": "checkMaven",
      "type": "bashExecCondition",
      "bash": "mvn --version > /dev/null 2>&1 && echo yes || echo no",
      "expectedResult": "yes"
    },
    {
      "id": "setupDirs",
      "type": "bashExec",
      "bash": "mkdir -p /tmp/workspace/{src,config,output}"
    },
    {
      "id": "setVars",
      "type": "variableSet",
      "variables": {
        "WORKSPACE": "/tmp/workspace",
        "CONFIG_DIR": "/tmp/workspace/config"
      }
    }
  ]
}
```

```xml
<flow>
    <chain name="defaultChain">
        IF(checkJava,
           IF(checkMaven,
              THEN(setupDirs, setVars),
              mavenNotFound),
           javaNotFound);
    </chain>
</flow>
```

---

## Multi-Step Data Processing

Chain together data extraction, AI analysis, and report generation.

**Workflow:**
1. Extract data from a source system
2. Set up processing variables
3. Use an AI agent to analyze the data
4. Generate a summary report

```json
{
  "tasks": [
    {
      "id": "extractData",
      "type": "bashExec",
      "bash": "psql -h db.example.com -U reader -d analytics -c 'SELECT * FROM metrics' > /tmp/data.csv"
    },
    {
      "id": "setVars",
      "type": "variableSet",
      "variables": {
        "DATA_FILE": "/tmp/data.csv",
        "REPORT_DIR": "/tmp/reports"
      }
    },
    {
      "id": "initAgent",
      "type": "acpInit",
      "command": "qwen",
      "args": ["--acp"]
    },
    {
      "id": "analyzeData",
      "type": "acpExec",
      "prompt": "prompts/analyze-metrics.txt"
    },
    {
      "id": "generateReport",
      "type": "bashExec",
      "bash": "mkdir -p ${REPORT_DIR} && cp ${DATA_FILE} ${REPORT_DIR}/"
    }
  ]
}
```

```xml
<flow>
    <chain name="defaultChain">
        THEN(extractData, setVars, initAgent, analyzeData, generateReport);
    </chain>
</flow>
```

---

## Why ACP Sure Step?

| Feature | Benefit |
|---------|---------|
| **Declarative Configuration** | Define workflows in simple JSON and XML — no programming required |
| **AI Agent Integration** | Seamlessly connect to AI agents via the standardized Agent Client Protocol |
| **Conditional Logic** | Build intelligent workflows that adapt based on runtime conditions |
| **Variable Substitution** | Share data between steps with `${VARIABLE_NAME}` syntax |
| **Multiple Interfaces** | Run from the CLI for automation or the Web UI for monitoring |
| **Extensible Architecture** | Add custom node types by extending the LiteFlow component model |
| **Spring Boot Foundation** | Leverage the mature Spring ecosystem for reliability and extensibility |
