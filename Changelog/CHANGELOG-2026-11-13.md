# Diagram rendering and live-trace repair — 2026-11-13

- Removed unsupported inline `#line:...;back:...` class/object styling that caused PlantUML syntax errors.
- Replaced inline highlighting with documented stereotype-based styling (`<<Selected>>`) and high-contrast class/object palettes.
- Increased diagram typography and zoomable canvas width so class members remain readable.
- Updated PlantUML from 1.2024.6 to 1.2026.10.
- Added PlantUML parser diagnostics and a rendered-error-page guard so a syntax-error PNG is no longer reported as a successful render.
- Kept Class and Object together in one Structure diagram: code-level class hierarchy plus live board object composition tree.
- Sequence now displays the latest 28 runtime messages instead of freezing on the first 24.
- Collaboration now derives nodes and numbered message edges from the runtime event stream instead of showing a fixed mock-up.
- Undo now emits runtime trace events. Component remains intentionally static because it describes architectural modules, not per-move runtime behavior.
