# ACP Sure Step

[![](https://jitpack.io/v/htynkn/acp-sure-step.svg)](https://jitpack.io/#htynkn/acp-sure-step)

## Project Overview

**ACP Sure Step** is a Java-based CLI tool that executes task flows using ACP (Agent Client Protocol) agents. It provides a workflow engine powered by LiteFlow, allowing users to define and execute complex task sequences that can interact with AI agents and execute shell commands.

### Architecture

The project is structured as a **Maven multi-module project**:

```
acp-sure-step/
├── acp-sure-step-core/    # Core business logic and workflow engine
└── acp-sure-step-cli/     # CLI application and integration tests
```

### Technology Stack

- **Java 17**
- **Spring Boot 3.4.4** - Application framework
- **LiteFlow 2.15.3** - Workflow/rule engine
- **picocli 4.7.6** - Command-line interface framework
- **ACP SDK 0.9.0-SNAPSHOT** - Agent Client Protocol for AI agent interaction
- **Lombok** - Boilerplate reduction
- **Jackson** - JSON processing
- **zt-exec** - Process execution for bash commands
- **JUnit 5** - Testing framework

### Package Structure

```
com.huangyunkun.acpsure.core/
├── acp/           # ACP client service
├── config/        # Configuration loading and DTOs
├── node/          # LiteFlow node components
├── util/          # Utilities
├── SureStep.java  # Main workflow executor
└── RunningContainer.java  # Context bean for workflow execution

com.huangyunkun.acpsure.cli/
└── SureStepCommand.java  # CLI command handler
```

### Core Components

| Component | Description |
|-----------|-------------|
| `SureStep` | Main workflow executor that initializes and drives the flow |
| `ConfigService` | Loads task configurations from JSON files |
| `AcpService` | Manages ACP client connections and prompt execution |
| `AcpInitNode` | LiteFlow node for initializing ACP sessions |
| `AcpExecNode` | LiteFlow node for executing ACP prompts |
| `BashExecNode` | LiteFlow node for executing bash commands |
| `SureStepCommand` | CLI entry point with picocli |

### Task Types

The system supports three task types defined in `TaskEnum`:

- `acpInit` - Initialize ACP client with a command
- `acpExec` - Execute a prompt via ACP
- `bashExec` - Execute bash shell commands

## Building and Running

### Prerequisites

- JDK 17 or higher
- Maven 3.6+

### Build Commands

```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Run integration tests
./mvnw failsafe:integration-test

# Full build with all tests
./mvnw clean verify

# Package as executable JAR
./mvnw clean package
```

### Running the CLI

```bash
# Run with task and flow configuration files
java -jar acp-sure-step-cli/target/acp-sure-step-cli-1.0-SNAPSHOT.jar \
  -t path/to/task.json \
  -f path/to/config.xml
```

### CLI Options

| Option | Description |
|--------|-------------|
| `-t, --task` | Path to task configuration file (task.json) |
| `-f, --flow` | Path to flow configuration file (config.xml) |
| `-h, --help` | Show help message |

### Configuration Files

**task.json** - Defines the tasks to execute:
```json
{
  "tasks": [
    {
      "id": "acpInit",
      "type": "acpInit",
      "command": "qwen",
      "args": ["--acp"]
    },
    {
      "id": "getInfo",
      "type": "bashExec",
      "bash": "echo hello > output.txt"
    }
  ]
}
```

**config.xml** - Defines the flow chain:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<flow>
    <chain name="defaultChain">
        THEN(acpInit, getInfo);
    </chain>
</flow>
```

## Development Conventions

### Code Style

- **Package naming**: `com.huangyunkun.acpsure.{core|cli}`
- **Lombok**: Used for reducing boilerplate (`@Data`, `@Component`, etc.)
- **Spring annotations**: Component scanning across core and cli packages

### Testing Practices

- **Unit tests**: Located in `src/test/java`, run with `mvn test`
- **Integration tests**: Located in `src/integration-test/java`, suffixed with `IT`, run with `mvn failsafe:integration-test`
- **Test frameworks**: JUnit 5, Hamcrest assertions
- **Test resources**: Located in `src/test/resources` and `src/integration-test/resources`

### Git Workflow

- **Main branch**: `master`
- **Feature branch**: `feature/spilt-to-cli`
- **CI**: GitHub Actions runs `mvn compile` on push and pull requests

### Module Responsibilities

| Module | Responsibility |
|--------|----------------|
| `acp-sure-step-core` | Core workflow engine, ACP integration, node components |
| `acp-sure-step-cli` | CLI application, Spring Boot runner, integration tests |
