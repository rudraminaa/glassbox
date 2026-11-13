# Responsive UML diagram update — 2026-11-13

## Fixed
- UML image width now follows the inspector's live viewport width.
- At 100% zoom the image fits the available width; vertical diagrams scroll vertically.
- Added a Fit control and bounded zoom controls.
- Narrow inspector widths no longer force a 240px minimum image that creates unwanted horizontal overflow.

## Simplified Structure diagram
- Reduced the class model to the central domain hierarchy and key relationships.
- Limited member rows per class to reduce visual density.
- Grouped live piece instances beneath White and Black collection objects.
- Kept every occupied-square piece as an individual runtime UML object with ID, type, color, square, and moved state.
- Preserved selection highlighting and board refresh behaviour.

## Notes
- Component diagrams are architectural and intentionally static.
- Structure and runtime behaviour diagrams are generated from current board state and runtime event messages.
- Full Maven/JavaFX execution could not be performed in this environment because Maven is not installed.
