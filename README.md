# ACP Sure Step

![GitHub License](https://img.shields.io/github/license/htynkn/acp-sure-step)
[![DeepWiki](https://img.shields.io/badge/DeepWiki-htynkn%2Facp--sure--step-blue.svg?logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACwAAAAyCAYAAAAnWDnqAAAAAXNSR0IArs4c6QAAA05JREFUaEPtmUtyEzEQhtWTQyQLHNak2AB7ZnyXZMEjXMGeK/AIi+QuHrMnbChYY7MIh8g01fJoopFb0uhhEqqcbWTp06/uv1saEDv4O3n3dV60RfP947Mm9/SQc0ICFQgzfc4CYZoTPAswgSJCCUJUnAAoRHOAUOcATwbmVLWdGoH//PB8mnKqScAhsD0kYP3j/Yt5LPQe2KvcXmGvRHcDnpxfL2zOYJ1mFwrryWTz0advv1Ut4CJgf5uhDuDj5eUcAUoahrdY/56ebRWeraTjMt/00Sh3UDtjgHtQNHwcRGOC98BJEAEymycmYcWwOprTgcB6VZ5JK5TAJ+fXGLBm3FDAmn6oPPjR4rKCAoJCal2eAiQp2x0vxTPB3ALO2CRkwmDy5WohzBDwSEFKRwPbknEggCPB/imwrycgxX2NzoMCHhPkDwqYMr9tRcP5qNrMZHkVnOjRMWwLCcr8ohBVb1OMjxLwGCvjTikrsBOiA6fNyCrm8V1rP93iVPpwaE+gO0SsWmPiXB+jikdf6SizrT5qKasx5j8ABbHpFTx+vFXp9EnYQmLx02h1QTTrl6eDqxLnGjporxl3NL3agEvXdT0WmEost648sQOYAeJS9Q7bfUVoMGnjo4AZdUMQku50McDcMWcBPvr0SzbTAFDfvJqwLzgxwATnCgnp4wDl6Aa+Ax283gghmj+vj7feE2KBBRMW3FzOpLOADl0Isb5587h/U4gGvkt5v60Z1VLG8BhYjbzRwyQZemwAd6cCR5/XFWLYZRIMpX39AR0tjaGGiGzLVyhse5C9RKC6ai42ppWPKiBagOvaYk8lO7DajerabOZP46Lby5wKjw1HCRx7p9sVMOWGzb/vA1hwiWc6jm3MvQDTogQkiqIhJV0nBQBTU+3okKCFDy9WwferkHjtxib7t3xIUQtHxnIwtx4mpg26/HfwVNVDb4oI9RHmx5WGelRVlrtiw43zboCLaxv46AZeB3IlTkwouebTr1y2NjSpHz68WNFjHvupy3q8TFn3Hos2IAk4Ju5dCo8B3wP7VPr/FGaKiG+T+v+TQqIrOqMTL1VdWV1DdmcbO8KXBz6esmYWYKPwDL5b5FA1a0hwapHiom0r/cKaoqr+27/XcrS5UwSMbQAAAABJRU5ErkJggg==)](https://deepwiki.com/htynkn/acp-sure-step)
![GitHub branch check runs](https://img.shields.io/github/check-runs/htynkn/acp-sure-step/master)
![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/htynkn/acp-sure-step)
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
| `BashExecConditionNode` | LiteFlow boolean node for executing bash commands with expected result validation |
| `VariableSetNode` | LiteFlow node for injecting variables into the flow context |
| `SureStepCommand` | CLI entry point with picocli |

### Task Types

The system supports five task types defined in `TaskEnum`:

- `acpInit` - Initialize ACP client with a command
- `acpExec` - Execute a prompt via ACP
- `bashExec` - Execute bash shell commands (supports `${VARIABLE_NAME}` substitution)
- `bashExecCondition` - Execute bash commands and validate output against an expected result (boolean condition node, supports `${VARIABLE_NAME}` substitution)
- `variableSet` - Inject named variables into the flow context for use by subsequent nodes

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
