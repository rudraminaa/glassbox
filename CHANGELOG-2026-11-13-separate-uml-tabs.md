# Separate UML views — 2026-11-13

- Split the combined diagram into independent `Structure` and `Class Diagram` tabs.
- Structure now renders only live ChessGame, Board, White/Black piece collections, and actual piece instances.
- Added vertical hidden ordering links between piece objects so the runtime structure is not laid out as one long horizontal row.
- Kept composition relationships between Board and side collections, and between each collection and its piece objects.
- Class Diagram now independently renders the reflected class hierarchy, attributes, methods, dependencies, and multiplicities.
- Preserved selected-class highlighting.
