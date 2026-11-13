# GlassBox Engine — Project Website

A responsive, static showcase website for https://github.com/rudraminaa/glassbox-engine.

## Preview locally
No build tools or package installation are required.

- Open `index.html` directly in a browser, or
- Run a local server from this folder:

```bash
python -m http.server 8000
```

Then visit http://localhost:8000.

## Deploy with GitHub Pages
1. Create a repository for the website (or add these files to a `docs/` folder in the project repository).
2. Push the files to GitHub.
3. Open **Settings → Pages**.
4. Choose **Deploy from a branch**.
5. Select the branch and folder containing `index.html` (`/root` or `/docs`).
6. Save and wait for the Pages deployment.

## Contents
- `index.html` — semantic page structure and project copy
- `styles.css` — responsive visual system
- `script.js` — visualization tabs, screenshot lightbox, mobile navigation and copy command
- `assets/` — six supplied GlassBox screenshots and favicon

The site is static HTML/CSS/JavaScript. It does not embed or attempt to run the JavaFX desktop application.
