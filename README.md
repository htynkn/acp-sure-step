# ACP Sure Step

![GitHub License](https://img.shields.io/github/license/htynkn/acp-sure-step)
[![DeepWiki](https://img.shields.io/badge/DeepWiki-htynkn%2Facp--sure--step-blue.svg?logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACwAAAAyCAYAAAAnWDnqAAAAAXNSR0IArs4c6QAAA05JREFUaEPtmUtyEzEQhtWTQyQLHNak2AB7ZnyXZMEjXMGeK/AIi+QuHrMnbChYY7MIh8g01fJoopFb0uhhEqqcbWTp06/uv1saEDv4O3n3dV60RfP947Mm9/SQc0ICFQgzfc4CYZoTPAswgSJCCUJUnAAoRHOAUOcATwbmVLWdGoH//PB8mnKqScAhsD0kYP3j/Yt5LPQe2KvcXmGvRHcDnpxfL2zOYJ1mFwrryWTz0advv1Ut4CJgf5uhDuDj5eUcAUoahrdY/56ebRWeraTjMt/00Sh3UDtjgHtQNHwcRGOC98BJEAEymycmYcWwOprTgcB6VZ5JK5TAJ+fXGLBm3FDAmn6oPPjR4rKCAoJCal2eAiQp2x0vxTPB3ALO2CRkwmDy5WohzBDwSEFKRwPbknEggCPB/imwrycgxX2NzoMCHhPkDwqYMr9tRcP5qNrMZHkVnOjRMWwLCcr8ohBVb1OMjxLwGCvjTikrsBOiA6fNyCrm8V1rP93iVPpwaE+gO0SsWmPiXB+jikdf6SizrT5qKasx5j8ABbHpFTx+vFXp9EnYQmLx02h1QTTrl6eDqxLnGjporxl3NL3agEvXdT0WmEost648sQOYAeJS9Q7bfUVoMGnjo4AZdUMQku50McDcMWcBPvr0SzbTAFDfvJqwLzgxwATnCgnp4wDl6Aa+Ax283gghmj+vj7feE2KBBRMW3FzOpLOADl0Isb5587h/U4gGvkt5v60Z1VLG8BhYjbzRwyQZemwAd6cCR5/XFWLYZRIMpX39AR0tjaGGiGzLVyhse5C9RKC6ai42ppWPKiBagOvaYk8lO7DajerabOZP46Lby5wKjw1HCRx7p9sVMOWGzb/vA1hwiWc6jm3MvQDTogQkiqIhJV0nBQBTU+3okKCFDy9WwferkHjtxib7t3xIUQtHxnIwtx4mpg26/HfwVNVDb4oI9RHmx5WGelRVlrtiw43zboCLaxv46AZeB3IlTkwouebTr1y2NjSpHz68WNFjHvupy3q8TFn3Hos2IAk4Ju5dCo8B3wP7VPr/FGaKiG+T+v+TQqIrOqMTL1VdWV1DdmcbO8KXBz6esmYWYKPwDL5b5FA1a0hwapHiom0r/cKaoqr+27/XcrS5UwSMbQAAAABJRU5ErkJggg==)](https://deepwiki.com/htynkn/acp-sure-step)
![GitHub branch check runs](https://img.shields.io/github/check-runs/htynkn/acp-sure-step/master)
![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/htynkn/acp-sure-step)
[![](https://jitpack.io/v/htynkn/acp-sure-step.svg)](https://jitpack.io/#htynkn/acp-sure-step)

## Project Overview

**ACP Sure Step** is a Java-based workflow engine that executes task flows using ACP (Agent Client Protocol) agents. Powered by [LiteFlow](https://liteflow.cc/), it lets you define and run complex task sequences combining AI agent interactions with shell commands. It ships as both a **CLI tool** and a **Spring Boot web application**.

### Key Features

- **Workflow orchestration** – Chain tasks in sequence or conditional branches using LiteFlow XML configuration
- **ACP integration** – Initialize and drive AI agent sessions (e.g. Qwen) via the ACP SDK
- **Bash execution** – Run shell commands with `${VARIABLE_NAME}` substitution support
- **Boolean conditions** – Gate workflow branches on the output of bash commands
- **Variable injection** – Pass runtime variables between workflow steps
- **Dual interfaces** – Run workflows from the command line or through a web UI

## Repository Structure

```
acp-sure-step/
├── acp-sure-step-core/          # Core workflow engine and ACP integration
│   └── src/main/java/…/core/
│       ├── acp/                 # ACP client service
│       ├── config/              # Configuration loading and DTOs
│       ├── node/                # LiteFlow node components
│       ├── util/                # Utility classes
│       ├── SureStep.java        # Main workflow executor
│       └── RunningContainer.java
├── acp-sure-step-cli/           # CLI application (Spring Boot + picocli)
│   └── src/
│       ├── main/                # SureStepCliApplication, SureStepCommand
│       └── integration-test/    # Integration tests and sample configs
├── acp-sure-step-web/           # Web application (Spring Boot + Thymeleaf)
│   └── src/main/java/…/web/
│       ├── controller/          # REST controllers
│       ├── service/             # Flow execution service
│       └── templates/           # Thymeleaf UI templates
├── website/                     # Jekyll documentation site
├── pom.xml                      # Parent Maven POM
├── mvnw / mvnw.cmd              # Maven wrapper scripts
└── LICENSE                      # Apache License 2.0
```

## Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Runtime |
| Spring Boot | 3.4.4 | Application framework |
| LiteFlow | 2.15.3 | Workflow / rule engine |
| picocli | 4.7.6 | CLI framework |
| ACP Core SDK | 0.9.0-SNAPSHOT | Agent Client Protocol integration |
| Thymeleaf | (via Spring Boot) | Web UI templating |
| Jackson | 2.15.2 | JSON processing |
| zt-exec | 1.12 | Subprocess / bash execution |
| Lombok | 1.18.30 | Boilerplate reduction |
| JUnit 5 / Hamcrest | — | Testing |
| ArchUnit | 1.3.0 | Architecture constraint tests |

## Prerequisites

- **JDK 17** or higher
- **Maven 3.6+** (or use the included `./mvnw` wrapper — no installation needed)

## Setup & Installation

```bash
# Clone the repository
git clone https://github.com/htynkn/acp-sure-step.git
cd acp-sure-step

# Compile all modules
./mvnw clean compile

# Package all modules (produces executable JARs)
./mvnw clean package -DskipTests
```

## Running

### CLI

```bash
java -jar acp-sure-step-cli/target/acp-sure-step-cli-1.0-SNAPSHOT.jar \
  -t path/to/task.json \
  -f path/to/config.xml
```

| Option | Required | Description |
|--------|----------|-------------|
| `-t, --task` | Yes | Path to task configuration file (`task.json`) |
| `-f, --flow` | Yes | Path to flow configuration file (`config.xml`) |
| `-h, --help` | — | Show help message |

### Web Application

```bash
java -jar acp-sure-step-web/target/acp-sure-step-web-1.0-SNAPSHOT.jar
```

The web UI is available at `http://localhost:8080` by default.

## Configuration

Workflows are defined by two files.

### task.json — task definitions

```json
{
  "tasks": [
    {
      "id": "acpInit",
      "type": "acpInit",
      "command": "qwen",
      "args": ["--acp"],
      "workspace": "/optional/path/to/workspace"
    },
    {
      "id": "getInfo",
      "type": "bashExec",
      "bash": "echo hello > output.txt"
    }
  ]
}
```

### config.xml — flow chain

```xml
<?xml version="1.0" encoding="UTF-8"?>
<flow>
    <chain name="defaultChain">
        THEN(acpInit, getInfo);
    </chain>
</flow>
```

### Supported Task Types

| Type | Description |
|------|-------------|
| `acpInit` | Initialize an ACP client session with a given command |
| `acpExec` | Execute a prompt via an active ACP session |
| `bashExec` | Run a shell command (supports `${VARIABLE_NAME}` substitution) |
| `bashExecCondition` | Run a shell command and validate its output (boolean gate) |
| `variableSet` | Inject named variables into the flow context |

## Building & Testing

```bash
# Compile only
./mvnw clean compile

# Unit tests
./mvnw test

# Integration tests
./mvnw failsafe:integration-test

# Full build + all tests + code coverage
./mvnw clean verify

# Package JARs (skip tests)
./mvnw clean package -DskipTests
```

Integration tests live in `src/integration-test/java` and are suffixed with `IT`. Sample configurations for them are in `src/integration-test/resources/`.

## Module Overview

| Module | Responsibility | Entry Point |
|--------|----------------|-------------|
| `acp-sure-step-core` | Workflow engine, ACP integration, LiteFlow nodes, configuration loading | `SureStep.java` |
| `acp-sure-step-cli` | CLI application, picocli command handler, integration tests | `SureStepCliApplication.java` |
| `acp-sure-step-web` | Web UI and REST API for workflow execution | `SureStepWebApplication.java` |

## Contributing

1. Fork the repository and create a feature branch from `master`.
2. Make your changes and add tests where appropriate.
3. Ensure the full build passes: `./mvnw clean verify`.
4. Open a pull request against `master` with a clear description of your changes.

- Follow the existing package naming convention (`com.huangyunkun.acpsure.{core|cli|web}`).
- Use Lombok to reduce boilerplate where it is already in use.
- Unit tests go in `src/test/java`; integration tests go in `src/integration-test/java` and must be suffixed `IT`.

## License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](LICENSE) file for details.
