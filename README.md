# 🌍 Java Procedural Planet Generator

![Java](https://img.shields.io/badge/Java-17+-orange)
![Rendering](https://img.shields.io/badge/Rendering-Java%20Swing-blue)
![Procedural](https://img.shields.io/badge/Procedural%20Generation-Noise-green)

A **real-time procedural planet renderer** written in **pure Java (Swing)** that generates infinite rotating planets using noise-based terrain generation.

Each planet is created from a random seed and rendered as a 3D sphere with terrain shading, lighting, atmosphere, and multiple planet types.

---

# Demo

![Planet Demo](demo.gif)

---

# ✨ Features

### 🌍 Procedural Terrain
Terrain is generated using **fractal noise**, producing continents, mountains, and coastlines.

### 🔄 Real-Time Planet Rotation
Planets rotate smoothly by rotating the noise sampling coordinates.

### ☀️ Lighting System
Includes:

- directional sunlight
- ambient lighting
- limb darkening

### ⛰ Terrain Relief Shading
Slope-based shading enhances mountains and valleys.

### 🌫 Atmospheric Glow
Subtle atmospheric scattering around the planet edge.

### 🪐 Multiple Planet Types

- 🌍 Earth-like
- 🏜 Desert
- 🧊 Ice
- 🌋 Lava

### 🎲 Infinite Planets
Each seed generates a completely unique world.

---

# 🎮 Controls

| Key | Action |
|----|----|
| **R** | Generate a new random seed |
| **P** | Cycle planet type |

Example output:

```

Planet: ICE
Seed: -492381234987123

```

---

# 🧠 How It Works

## 1️⃣ Sphere Projection

Each screen pixel is projected onto a unit sphere:

```

x² + y² + z² = 1

```

This converts a 2D image into a 3D planet surface.

---

## 2️⃣ Fractal Noise Terrain

Terrain height uses **multiple noise octaves**:

```

height =
noise(frequency₁) +
noise(frequency₂) +
noise(frequency₃) +
noise(frequency₄)

```

This produces large continents and fine terrain detail.

---

## 3️⃣ Planet Rotation

Instead of rotating geometry, the terrain sampling coordinates rotate:

```

rx = dx*cos(rotation) - dz*sin(rotation)
rz = dx*sin(rotation) + dz*cos(rotation)

```

This creates smooth planet rotation.

---

## 4️⃣ Lighting Model

Directional lighting uses Lambert shading:

```

brightness = normal · lightDirection

```

Additional effects include:

- ambient light
- limb shading
- terrain slope shading

---

## 5️⃣ Atmospheric Rim

A thin atmospheric glow is added near the edge of the planet.

---

# 📂 Project Structure

```

src
├── Main.java
├── PlanetPanel.java
├── PlanetGenerator.java
├── PlanetType.java
└── Noise.java

```

| File | Description |
|-----|-----|
| `PlanetPanel` | Rendering and input handling |
| `PlanetGenerator` | Planet color rules |
| `PlanetType` | Enum defining planet types |
| `Noise` | Procedural terrain noise |
| `Main` | Application entry point |

---

# 🧩 Concepts Used

- Procedural generation
- Fractal noise
- Sphere projection
- Terrain shading
- Lambert lighting
- Atmosphere scattering

---

# 🔮 Possible Improvements

Future extensions could include:

- ☁️ Cloud layers
- 🌊 Ocean reflections
- 🪐 Planet rings
- 🌌 Star background
- 🚀 OpenGL / GPU rendering

---
