# GlassBox UML inspector

The inspector uses PlantUML class, object, sequence, communication, and component notation. It does not use a card-grid UI as a substitute for diagrams.

## Structure tab (combined class + object model)

- The upper package is the code-level class model. Class members are obtained through Java reflection from the GlassBox chess classes.
- Generalization arrows connect `Piece` to its concrete piece subclasses. Composition connectors show `ChessGame` owning one `Board`, and `Board` owning its squares and occupied pieces. Multiplicities are shown on the connectors.
- The lower package is a snapshot of the live board. It contains the actual `Piece` instances currently occupying squares, with stable identity-based IDs, type, color, square, and moved state. Captured pieces are absent because the model is generated from the current board.
- Selecting a piece highlights both its object instance and its class stereotype.
- The board snapshot is regenerated on every controller refresh, including moves, captures, undo, and redo.

## Sequence tab

Sequence messages are generated from `DomainEvents.RuntimeMessage` events. The newest 28 events are rendered in chronological order. The sender and receiver become lifelines; method names become message arrows; event details become notes. Player and AI Agent are actors when they participate in the trace.

## Collaboration tab

The communication diagram uses the same runtime event stream. Objects are created from observed endpoints and numbered message arrows represent the most recent 18 calls. Undo and redo publish trace events too.

## Component tab

The component diagram is intentionally architectural and static: component boundaries and provided/required interfaces do not change with a chess move. It is not presented as a live runtime trace.

## Rendering and diagnostics

PlantUML renders locally inside the JavaFX application. The source toggle exposes the exact generated PlantUML. Syntax-error PNGs are detected and reported as errors instead of being shown as if they were valid diagrams. The diagram viewport supports scrolling and zoom.


## Responsive diagram layout (2026-11-13)

- The UML canvas recalculates the diagram image width from the ScrollPane viewport whenever the inspector is resized.
- At 100% zoom, diagrams fit the available inspector width; tall diagrams scroll vertically rather than being clipped.
- Zoom above 100% intentionally allows horizontal scrolling/panning. The Fit control returns to width-fit mode.
- The combined Structure diagram is simplified into a compact class model and a grouped runtime object tree. The live pieces remain individual UML object instances, grouped under White and Black collection objects to reduce crossing connectors.
- The class model shows the core game, board, square, piece inheritance hierarchy, validator, and AI strategy contract; less central implementation classes and excess members are omitted to keep the diagram legible.


## Layout refinement (2026-11-13)
- Structure packages now have explicit aliases and a hidden top-to-bottom layout constraint so the class model sits above the live object tree instead of beside it.
- The live object tree remains grouped by White and Black, with individual piece instances beneath each group.
- Collaboration now shows the latest eight runtime messages, aggregates repeated calls between the same object pair, and uses larger node spacing and readable typography to reduce label collisions.


## Separate Class Diagram and Runtime Structure tabs
- `Structure` now contains only the live object graph for the current chess position.
- `Class Diagram` contains only the static class model, inheritance, dependencies, composition and multiplicities.
- Runtime piece objects are grouped under White and Black collection objects. Hidden vertical ordering links keep the objects from being laid out as one extremely wide horizontal row.
- Runtime object labels preserve PlantUML line breaks so object IDs, type, square and moved state are readable.


## Collaboration and sequence rendering correction
- Fixed collaboration object declarations to terminate with actual newline characters in the generated PlantUML source. Previously, literal `\\n` text was emitted between declarations, which could cause PlantUML's renderer to fail with an `IllegalStateException`.
- Explicitly set sequence message font colour to a high-contrast light colour so arrow labels remain readable on the dark canvas.
- Set collaboration arrow-label font colour to the same high-contrast light colour.
