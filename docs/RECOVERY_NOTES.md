# Recovery and current implementation notes

This project was reconstructed from the supplied archive and transcript. The UI and inspection work in this revision follows the requested order:

1. **UI polish:** dark green/charcoal desktop theme, clearer toolbar and inspector hierarchy, responsive board layout, rank/file coordinates, legal-target/selection/last-move/check highlights, and a compact status line.
2. **Live UML inspection:** local PlantUML rendering for Class, Object, Sequence, Activity, State, and Use-Case views. The Object view reflects the current board; Sequence and Activity views include recent runtime domain events. Each view can switch to its PlantUML source.
3. **Chess interaction:** click-to-select and click-to-move, legal destination highlighting, promotion choice, board flip, undo, redo, and terminal-state move blocking.

## Verification performed in the build environment

- Core Java sources (chess, engine interfaces/commands, exceptions, and UML event/model code) compile with the available JDK.
- Initial-position perft smoke test returns depth 1 = 20, depth 2 = 400, depth 3 = 8902.
- The full Maven/JavaFX build was not run in this environment because Maven is not installed here. Run `mvn clean test` on the target machine after Maven downloads the dependencies, then launch with `mvn javafx:run`.

## Remaining limitations

- UML diagrams are live visualizations, but pan/zoom, PNG export, and two-way diagram-to-code highlighting are not included.
- The checkers package remains a plugin foundation, not a complete checkers implementation.
- The chess AI and PGN/SAN support are basic and should not be considered tournament-grade or fully standards-complete.
- Persistence does not yet provide a complete restorable board snapshot.


## UI / AI integration update

The desktop shell now includes a ChatGPT-inspired neutral dark theme, an actual JavaFX `SplitPane` between the board and inspector, and an integrated computer-opponent control. The AI runs on a daemon background thread against a deep-copied board snapshot; its result is mapped back to a legal move on the live board before it is committed on the JavaFX thread. Easy uses random legal moves, Medium uses a depth-limited minimax search, and Hard uses a longer time-budgeted search.

UML views now have a zoomable and scrollable render canvas, source toggle, and explicit render diagnostics. The class view includes the AI strategy/evaluator types, while the object view is refreshed from the current board. PlantUML still needs to be exercised on a machine with Maven dependencies resolved; this environment did not have Maven or JavaFX installed, so a full JavaFX launch could not be verified here.
