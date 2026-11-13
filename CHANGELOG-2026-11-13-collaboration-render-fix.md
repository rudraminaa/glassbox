# Collaboration rendering fix — 2026-11-13

- Fixed collaboration diagram object declarations: the generated PlantUML now uses actual line terminators between declarations rather than literal backslash-n text.
- Kept object aliases and message relationships as separate valid PlantUML statements.
- Explicitly set sequence message label font colour to light for contrast on the dark background.
- Increased collaboration arrow-label contrast and retained readable object attribute colours.

## Verification
- Archive integrity checked after packaging.
- Maven is not installed in the build environment, so a full Java/Maven compile and runtime render could not be executed here.
