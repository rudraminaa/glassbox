# ♟️ GlassBox Engine

### A Live UML-Driven Chess Engine & Object-Oriented Programming Visualizer

GlassBox Engine is a Java-based chess application built to demonstrate
how object-oriented software architecture works in a real, interactive
system.

It combines a playable chess engine, an AI opponent, and a live
architectural inspector that visualizes the relationships and
interactions between software components as the game progresses.

Rather than treating UML diagrams as static documentation, GlassBox
makes the architecture observable at runtime --- connecting actual
gameplay events to the underlying object model, class relationships,
message flows, and component interactions.

------------------------------------------------------------------------

## ✨ Overview

GlassBox brings together three core ideas:

-   **Chess Engine:** A playable chess implementation with move
    validation, game-state management, and move history.
-   **AI Opponent:** An integrated computer opponent with selectable
    difficulty.
-   **Live UML Inspector:** A visualization environment that exposes the
    internal architecture and runtime behaviour of the application.

The result is an interactive demonstration of how object-oriented design
principles translate into a functioning software system.

------------------------------------------------------------------------

## 🖥️ Application Preview

The interface uses a dark, minimal workspace inspired by modern
developer tools. The chessboard occupies the main workspace, while a
resizable inspector provides access to architectural visualizations.

### Main Chess Interface

![GlassBox Chess Interface](docs/images/chess-interface.png)

### Architecture & UML Visualizations

The inspector provides separate views of the application's architecture
and runtime behaviour.

  -----------------------------------------------------------------------
  Visualization                       Purpose
  ----------------------------------- -----------------------------------
  Structure                           Runtime object structure and
                                      relationships

  Class Diagram                       Classes, interfaces, inheritance,
                                      attributes and methods

  Sequence                            Ordered interactions between system
                                      participants

  Collaboration                       Communication and relationships
                                      between interacting objects

  Component                           High-level software components and
                                      dependencies

  Game                                Current game state, turn
                                      information and move history
  -----------------------------------------------------------------------

#### Structure View

![Structure Visualization](docs/images/structure.png)

#### Class Diagram

![Class Diagram](docs/images/class-diagram.png)

#### Sequence Diagram

![Sequence Diagram](docs/images/sequence.png)

#### Collaboration Diagram

![Collaboration Diagram](docs/images/collaboration.png)

#### Component Diagram

![Component Diagram](docs/images/component.png)

#### Game Inspector

![Game Inspector](docs/images/game.png)

------------------------------------------------------------------------

## 🚀 Features

### 1. Interactive Chess Engine

-   Interactive chessboard.
-   Legal move validation.
-   Piece movement and capture.
-   Move history.
-   Undo and redo.
-   Board orientation controls.
-   Game-state inspection.

### 2. AI Opponent

Play against an integrated chess AI.

-   Player-versus-AI gameplay.
-   Selectable difficulty levels.
-   AI strategy abstraction.
-   Minimax-based search implementation.
-   Position evaluation.

### 3. Live UML Inspector

The central feature of GlassBox is its architectural visualization
system.

Instead of displaying disconnected informational cards, the inspector
aims to represent the actual software architecture through conventional
UML-style diagrams.

**Structure** - Runtime objects and their relationships. - Object
ownership and composition. - Hierarchical organization.

**Class Diagram** - Classes and interfaces. - Inheritance and
implementation. - Attributes and operations. - Associations and
multiplicities.

**Sequence Diagram** - Actors and participating objects. - Lifelines and
activation. - Method calls and responses. - Ordered runtime
interactions.

**Collaboration Diagram** - Interacting objects. - Communication
links. - Numbered messages. - Runtime interaction relationships.

**Component Diagram** - Major architectural modules. - Interfaces and
dependencies. - High-level component relationships.

**Game View** - Current player and game status. - Move history. -
Game-state information.

### 4. Inspector Controls

-   Switch between visualization tabs.
-   Show or hide the inspector.
-   Resize the inspector panel.
-   Zoom in and out.
-   Fit diagrams to the available viewport.
-   Inspect generated PlantUML source.

------------------------------------------------------------------------

## 🏗️ Architecture

GlassBox follows a modular, object-oriented architecture that separates
gameplay logic from presentation and visualization.

The main architectural areas include:

  Module          Responsibility
  --------------- ----------------------------------------------------
  `chess`         Chess domain model, board, pieces and game rules
  `engine`        Core game abstractions and shared functionality
  `ai`            AI strategies, search and position evaluation
  `ui`            JavaFX interface, board rendering and interaction
  `uml`           Diagram generation and architectural visualization
  `persistence`   Game data and persistence functionality
  `exception`     Application-specific exception hierarchy
  `checkers`      Checkers extension and domain foundations

The architecture is designed around separation of concerns: the UI
handles presentation, the domain layer owns game behaviour, AI operates
through strategy abstractions, and the UML inspector observes the
system.

------------------------------------------------------------------------

## 🧩 Object-Oriented Design

GlassBox is also an exploration of object-oriented programming and
software engineering principles.

Key concepts demonstrated include:

-   **Abstraction:** Common piece behaviour and AI strategy contracts.
-   **Encapsulation:** Game state and domain behaviour are managed by
    dedicated objects.
-   **Inheritance:** Specialized chess pieces build on shared
    abstractions.
-   **Polymorphism:** Piece-specific movement behaviour and
    interchangeable AI strategies.
-   **Interfaces:** Contracts between the game engine and its
    collaborators.
-   **Composition:** Relationships between games, boards, squares and
    pieces.

### Design Patterns

The architecture incorporates patterns such as:

-   Strategy --- interchangeable AI algorithms.
-   Command --- representing and executing moves.
-   Observer --- publishing runtime events to interested components.

------------------------------------------------------------------------

## 🛠️ Technology Stack

  Technology   Usage
  ------------ ---------------------------------
  Java 17      Core application language
  JavaFX       Desktop graphical interface
  Maven        Dependency and build management
  PlantUML     UML diagram generation
  JUnit 5      Automated testing

------------------------------------------------------------------------

## ⚙️ Getting Started

### Prerequisites

Install the following:

-   Java Development Kit (JDK) 17 or later.
-   Apache Maven 3.9 or later.
-   Git.

Verify your installations:

``` bash
java -version
mvn -version
git --version
```

### Clone the Repository

``` bash
git clone https://github.com/rudraminaa/glassbox-engine.git
cd glassbox-engine
```

### Build the Project

``` bash
mvn clean install
```

### Run Tests

``` bash
mvn test
```

### Launch the Application

``` bash
mvn javafx:run
```

> The JavaFX launch command assumes the Maven JavaFX plugin and
> application entry point are configured in the project.

------------------------------------------------------------------------

## 🎮 How to Use

1.  Launch GlassBox Engine.
2.  Start a new chess game.
3.  Select pieces and make moves on the chessboard.
4.  Use Undo and Redo to navigate game history.
5.  Select **Play vs AI** to play against the computer.
6.  Choose an AI difficulty level.
7.  Open the Inspector to explore the architecture.
8.  Switch between Structure, Class Diagram, Sequence, Collaboration,
    Component and Game views.
9.  Zoom, fit, resize and inspect the generated UML source.

------------------------------------------------------------------------

## 🎯 Project Goals

GlassBox Engine explores an important idea:

**Software architecture should not remain hidden behind the code.**

The project aims to make object-oriented architecture tangible by
connecting the system's design to its actual runtime behaviour.

It serves as:

-   A practical demonstration of Object-Oriented Methodology.
-   An exploration of UML and software architecture.
-   A modular Java application.
-   An interactive learning and experimentation environment.
-   A foundation for extending the visualization system to additional
    games and domains.

------------------------------------------------------------------------

## 🔮 Future Scope

Potential directions for further development include:

-   More detailed runtime object inspection.
-   Expanded UML coverage.
-   Improved diagram layout and navigation.
-   Additional AI strategies and evaluation techniques.
-   PGN import and export.
-   Additional board games using shared engine abstractions.
-   Architecture snapshots and exportable diagrams.

------------------------------------------------------------------------

## 📁 Repository Structure

``` text
glassbox-engine/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/glassbox/
│   │   │       ├── chess/
│   │   │       ├── engine/
│   │   │       ├── ai/
│   │   │       ├── ui/
│   │   │       ├── uml/
│   │   │       ├── persistence/
│   │   │       ├── exception/
│   │   │       └── checkers/
│   │   └── resources/
│   │
│   └── test/
│
├── docs/
│   ├── images/
│   ├── UML_DIAGRAMS.md
│   └── architecture.puml
│
├── pom.xml
├── README.md
└── .gitignore
```

*The structure above illustrates the intended organization; consult the
repository for the current implementation.*

------------------------------------------------------------------------

## 👨‍💻 Author

**Rudra Mina**

GitHub: [@rudraminaa](https://github.com/rudraminaa)

Project: [GlassBox
Engine](https://github.com/rudraminaa/glassbox-engine)

------------------------------------------------------------------------

## 📄 License

See the repository for licensing information.

------------------------------------------------------------------------

```{=html}
<p align="center">
```
`<strong>`{=html}GlassBox Engine`</strong>`{=html}`<br/>`{=html}
`<em>`{=html}Making software architecture visible, interactive, and
alive.`</em>`{=html}
```{=html}
</p>
```
