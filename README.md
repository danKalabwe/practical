# Dan Kalabwe - Portfolio Website

A modern, responsive personal portfolio website built with plain HTML, CSS, and JavaScript, served by a custom Java HTTP server.

## Project Overview

This project showcases a professional portfolio for Dan Kalabwe, a Software Engineering student from Tanzania. The website features a clean, minimalist design with smooth animations and full mobile responsiveness.

## Project Structure

```
practical/
├── src/
│   └── SimpleServer.java     # Custom Java HTTP server (no frameworks)
├── public/
│   ├── index.html            # Portfolio HTML structure
│   ├── style.css             # Modern CSS styling with animations
│   └── script.js             # Smooth scrolling & interactions
└── README.md
```

## Features

- **Fixed Navigation** - Sticky navbar with smooth scroll functionality
- **Hero Section** - Eye-catching introduction with call-to-action
- **About Section** - Bio with statistics (Projects, Technologies, Experience)
- **Projects Showcase** - Featured projects with tags and descriptions
- **Skills Display** - Technologies organized by category (Frontend, Backend, Tools)
- **Contact Section** - Direct links to Email, WhatsApp, LinkedIn, and GitHub
- **Fully Responsive** - Optimized for desktop, tablet, and mobile devices
- **Smooth Animations** - Professional transitions and hover effects

##  Technologies Used

### Backend
- **Java** - Custom HTTP server using `com.sun.net.httpserver`
- No frameworks or external dependencies

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Modern styling with CSS variables, gradients, and animations
- **JavaScript** - Smooth scrolling and interactive elements

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- A web browser

### Compile & Run

From the project root directory:

```bash
# 1. Compile the Java server
javac src/SimpleServer.java

# 2. Run the server
java -cp src SimpleServer
```

The server will start on **http://localhost:8000/**

Open your browser and navigate to the URL to view the portfolio.

## How It Works

- The Java server listens on port 8000
- Serves static files from the `public/` directory
- Routes:
  - `/` → serves `public/index.html`
  - `/public/*` → serves CSS, JS, and other static assets
- Built without any frameworks - pure Java and vanilla JavaScript

## Contact

**Dan Kalabwe**
- Email: dkalabwe@gmail.com
- Phone/WhatsApp: +255 762 081 538
- LinkedIn: [dan-kalabwe-63a6aa331](http://linkedin.com/in/dan-kalabwe-63a6aa331)
- GitHub: [danKalabwe](https://github.com/danKalabwe)

## License

© 2026 Dan Kalabwe. All rights reserved.

---
