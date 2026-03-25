---
layout: page
title: How It Works
permalink: /how-it-works/
---

ACP Sure Step uses a **declarative configuration approach** where you define your tasks in a JSON file and the execution flow in an XML file. The workflow engine then orchestrates the execution of these tasks in the specified order.

## Architecture Overview

```
┌──────────────────────────────────────────────────┐
│              Configuration Files                  │
│  task.json (tasks)    config.xml (flow chain)     │
└──────────────┬───────────────────┬───────────────┘
               │                   │
               ▼                   ▼
       ┌───────────────────────────────────┐
       │     SureStep (Main Orchestrator)  │
       │  ConfigService   FlowExecutor     │
       └───────────────┬───────────────────┘
                       │
                       ▼
       ┌───────────────────────────────────┐
       │   RunningContainer (Context)      │
       │  sessionId, variables, configs    │
       └───────────────┬───────────────────┘
                       │
                       ▼
       ┌───────────────────────────────────┐
       │   LiteFlow Node Execution Chain   │
       │  AcpInit → BashExec → AcpExec    │
       └───────────────────────────────────┘
```

## Core Components

### SureStep

The main orchestrator that initializes and drives the workflow. It loads task configurations and creates a LiteFlow executor from the flow definition.

### ConfigService

Reads the `task.json` file and dynamically registers LiteFlow nodes based on the task definitions. Each task type maps to a specific node component.

### RunningContainer

A shared execution context that flows through all nodes. It stores:
- **Session ID** — The active ACP agent session
- **Variables** — Key-value pairs for variable substitution
- **Task Configs** — The loaded task definitions
- **Workspaces** — Agent and code workspace paths

### LiteFlow Nodes

Each task type is implemented as a LiteFlow node:

| Node | Type | Description |
|------|------|-------------|
| `AcpInitNode` | Common | Initializes an ACP agent session by launching an agent process |
| `AcpExecNode` | Common | Sends a prompt to the ACP agent and captures the response |
| `BashExecNode` | Common | Executes bash shell commands with variable substitution |
| `BashExecConditionNode` | Boolean | Executes bash commands and returns true/false based on expected output |
| `VariableSetNode` | Common | Injects named variables into the execution context |

## Configuration

### Task Definition (task.json)

Tasks are defined as a JSON array. Each task has an `id`, a `type`, and type-specific properties:

```json
{
  "tasks": [
    {
      "id": "initAgent",
      "type": "acpInit",
      "command": "qwen",
      "args": ["--acp"],
      "workspace": "/path/to/workspace"
    },
    {
      "id": "setupVars",
      "type": "variableSet",
      "variables": {
        "PROJECT_NAME": "my-project",
        "OUTPUT_DIR": "/tmp/output"
      }
    },
    {
      "id": "createDir",
      "type": "bashExec",
      "bash": "mkdir -p ${OUTPUT_DIR}"
    },
    {
      "id": "askAgent",
      "type": "acpExec",
      "prompt": "prompts/generate-code.txt"
    },
    {
      "id": "checkResult",
      "type": "bashExecCondition",
      "bash": "test -f ${OUTPUT_DIR}/result.txt && echo yes || echo no",
      "expectedResult": "yes"
    }
  ]
}
```

### Flow Definition (config.xml)

The execution order is defined using LiteFlow's chain DSL:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<flow>
    <chain name="defaultChain">
        THEN(initAgent, setupVars, createDir, askAgent);
    </chain>
</flow>
```

LiteFlow supports various control structures:

- **THEN** — Sequential execution
- **IF** — Conditional branching (uses boolean nodes like `BashExecConditionNode`)
- **WHEN** — Parallel execution

Example with conditional logic:

```xml
<flow>
    <chain name="defaultChain">
        THEN(initAgent, setupVars, createDir, askAgent,
             IF(checkResult, successHandler, failureHandler));
    </chain>
</flow>
```

## Task Types

### acpInit

Initializes an ACP agent session. This launches an agent process (e.g., Qwen) and establishes a communication session via the Agent Client Protocol.

**Properties:**
- `command` — The agent command to launch
- `args` — Command-line arguments for the agent
- `env` — Environment variables (optional)
- `workspace` — Working directory for the agent (optional)

### acpExec

Executes a prompt through the initialized ACP agent. The prompt is read from a file path relative to the task base directory.

**Properties:**
- `prompt` — Path to the prompt file

### bashExec

Executes bash shell commands. Supports `${VARIABLE_NAME}` substitution from the execution context.

**Properties:**
- `bash` — The bash command to execute

### bashExecCondition

Executes a bash command and compares the output to an expected result. Returns `true` if matched, `false` otherwise. Used as a boolean condition node in LiteFlow IF expressions.

**Properties:**
- `bash` — The bash command to execute
- `expectedResult` — The expected output to compare against

### variableSet

Injects variables into the execution context. These variables can be referenced by subsequent `bashExec` and `bashExecCondition` tasks using the `${VARIABLE_NAME}` syntax.

**Properties:**
- `variables` — A map of key-value pairs

## CLI Options

| Option | Description |
|--------|-------------|
| `-t, --task` | Path to task configuration file (task.json) |
| `-f, --flow` | Path to flow configuration file (config.xml) |
| `-h, --help` | Show help message |

## Web Interface

The `acp-sure-step-web` module provides a web-based interface for:

- **Loading** flow configurations through a file browser
- **Executing** flows asynchronously with real-time status updates
- **Monitoring** step-by-step execution state (PENDING → RUNNING → COMPLETED/FAILED)
- **Viewing** per-node execution logs
