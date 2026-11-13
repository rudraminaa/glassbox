# Real-time OOM visualization model

GlassBox's inspector is a read-only projection of the running chess application. It is not allowed to mutate the board or influence move validation.

## Five views

1. **Object** — scans the current 64 board squares and renders one object card for each live `Piece` instance. An `IdentityHashMap` assigns stable session IDs to object identities. The card shows the concrete runtime class, color, algebraic position, `hasMoved`, and identity hash. A selected piece is shown as a pink preview. Captured pieces disappear from the live snapshot; undo restores the original object identity when the command restores that piece.
2. **Sequence** — consumes `DomainEvents.RuntimeMessage` events. The actor is `Player` or `AI Agent`, and the message trace records selection, legal-move lookup, command creation, move execution, board update, and observer notification.
3. **Collaboration** — shows the object network and numbered messages among the actor, `ChessGame`, `Board`, `CommandHistory`, the active piece type, and the AI strategy.
4. **Component** — shows the UI, core game engine, polymorphic piece rules, AI strategy implementation, and read-only runtime inspector as modular components with interface contracts.
5. **Static** — discovers the actual compiled project classes using Java reflection and displays declared fields, methods, class/interface/abstract status. The concrete class corresponding to the selected piece receives a pink highlight.

## Event pipeline

`ChessController` publishes runtime messages at user/AI interaction boundaries. The shared `EventBus` isolates subscriber exceptions, so inspector failures cannot break gameplay. The panel updates from the live `Board` snapshot after controller refreshes. The inspector never calls mutating board methods.

## Limitations

The object view represents the live chessboard heap slice (game facade, board, and currently placed pieces), not every JVM object or garbage collector heap allocation. Java object identity is session-local; IDs are not persisted across application restarts. Sequence and collaboration views visualize instrumented domain interactions rather than every JVM method invocation.

## Current diagram presentation

The inspector presents four UML diagrams, not a collection of entity cards:

- **Structure** combines a reflection-derived class hierarchy with the live object graph in one PlantUML canvas. Generalization arrows show the `Piece` inheritance tree; composition connectors and multiplicities show `ChessGame` → `Board` → `Piece` ownership. Each live piece is an object instance with a stable session-local ID, color, square, and moved state. The selected concrete class and selected object are highlighted.
- **Sequence** renders participant lifelines and ordered messages from the runtime message event stream. Player and AI have distinct actor lifelines.
- **Collaboration** renders runtime objects as UML object nodes connected by numbered messages and association/composition links with multiplicities.
- **Component** renders modules, interfaces, and dependency connectors.

All four views are generated as PlantUML source and rendered locally. The inspector does not fall back to a card-grid representation when rendering succeeds; rendering errors are surfaced in the diagram status area and the source remains inspectable.
