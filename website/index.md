---
layout: default
title: Home
permalink: /
---

# ACP Sure Step

**ACP Sure Step** is a Java-based CLI and web tool that executes task flows using [ACP (Agent Client Protocol)](https://agentclientprotocol.org/) agents. It provides a workflow engine powered by [LiteFlow](https://liteflow.cc/), allowing users to define and execute complex task sequences that can interact with AI agents and execute shell commands.

## Key Features

- **Declarative Workflow Definitions** — Define tasks in JSON and execution flows in XML
- **AI Agent Integration** — Connect to and orchestrate AI agents via the Agent Client Protocol
- **Shell Command Execution** — Run bash commands with variable substitution support
- **Conditional Logic** — Branch workflows based on command output validation
- **Variable Management** — Store and substitute variables across workflow steps
- **CLI & Web Interface** — Run workflows from the command line or through a web UI with real-time status tracking
- **Spring Boot Powered** — Built on Spring Boot for robust, production-ready execution

## Quick Start

### Prerequisites

- JDK 17 or higher
- Maven 3.6+

### Build

```bash
./mvnw clean package
```

### Run

```bash
java -jar acp-sure-step-cli/target/acp-sure-step-cli-1.0-SNAPSHOT.jar \
  -t path/to/task.json \
  -f path/to/config.xml
```

## Project Structure

ACP Sure Step is organized as a Maven multi-module project:

| Module | Description |
|--------|-------------|
| **acp-sure-step-core** | Core workflow engine, ACP integration, and LiteFlow node components |
| **acp-sure-step-cli** | Command-line interface application with picocli |
| **acp-sure-step-web** | Web UI with real-time flow visualization and REST API |

## Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Language |
| Spring Boot 3.4.4 | Application framework |
| LiteFlow 2.15.3 | Workflow / rule engine |
| picocli 4.7.6 | CLI framework |
| ACP SDK | Agent Client Protocol integration |
| Jackson | JSON processing |
| zt-exec | Process execution for bash commands |
| JUnit 5 | Testing framework |

## License

ACP Sure Step is released under the [Apache License 2.0](https://github.com/htynkn/acp-sure-step/blob/master/LICENSE).
