# 159.261-Games-Programming-Assignment-1-Snake-Game


A Snake game implemented in Java using the GameEngine framework with object-oriented architecture.

##  Table of Contents
- [How to Run](#how-to-run)
- [Game Features](#game-features)
- [Controls](#controls)
- [Architecture](#architecture)
- [Additional Features](#additional-features)

---

##  How to Run

### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Java compiler (`javac`)
- Java runtime (`java`)

### Running the Game

#### Using IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Navigate to `src/Main.java`
3. Right-click on the file and select "Run 'Main.main()'"
4. Or click the green play button next to the main method

### Required Files

Make sure the following files are in the `src` directory:

**Image Resources:**
- `head.png` - Snake head image
- `dot.png` - Snake body segment image
- `apple.png` - Regular apple image
- `apple2.png` - Special apple image for two-player mode
- `poison.png` - Poison apple image

**Audio Resources:**
- `background.wav` - Background music
- `gameover.wav` - Game over sound effect

---

##  Game Features

### Core Gameplay
-  Classic snake movement and growth mechanics
-  Grid-based gameplay (20x20 grid)
-  Collision detection (walls, self, other players)
-  Score tracking system
-  Life system (5 lives per player)
-  High score tracking

### Game Modes

#### Single Player Mode
- Traditional snake gameplay
- Eat apples to grow and earn points
- Avoid poison apples and obstacles
- Maximum snake size: 20 segments

#### Two Player Mode
- Competitive gameplay for 2 players
- Player 1 (Blue): Arrow keys
- Player 2 (Green): WASD keys
- Each player has independent score and lives
- Shared poison system
- Countdown timer before game starts

---

##  Controls

### Menu Navigation
- **Mouse Click**: Select menu options (Play, Help, Quit)
- **Number Keys**: Quick mode selection (1 = Single Player, 2 = Two Players)

### In-Game Controls

#### Single Player
- **↑ Up Arrow**
- **↓ Down Arrow**
- **← Left Arrow**
- **→ Right Arrow**
- **P**: Pause/Resume game
- **R**: Restart game (when game over)
- **M**: Return to main menu (when game over)

#### Two Player Mode
**Player 1 (Blue Snake):**
- **↑ ↓ ← →**: Arrow keys for movement

**Player 2 (Green Snake):**
- **W**
- **S**
- **A**
- **D**

**Both Players:**
- **P**: Pause/Resume game
- **R**: Restart game (when game over)
- **M**: Return to main menu (when game over)

---

##  Architecture

### Object-Oriented Design

The game follows a clean object-oriented architecture with clear separation of concerns:

#### Core Classes

1. **Main.java** - Main game class extending GameEngine
   - Manages game lifecycle
   - Handles initialization
   - Coordinates game components
   - Renders UI elements

2. **GameWorld.java** - Game state and logic manager
   - Manages player data
   - Handles game updates
   - Processes collisions
   - Tracks scores and lives

3. **Snake.java** - Snake entity
   - Manages snake body segments
   - Handles movement and direction
   - Implements position shifting algorithm
   - Growth mechanics

4. **FoodManager.java** - Food generation and management
   - Random apple placement
   - Poison generation
   - Collision avoidance with game objects
   - Supports single and two-player modes

5. **PlayerData.java** - Player information container
   - Stores score and lives
   - Manages player state

6. **InputHandler.java** - Input processing
   - Keyboard event handling
   - Maps input to player actions
   - Supports multiple control schemes

7. **GameRenderer.java** - Game rendering
   - Draws snakes, apples, and poisons
   - Renders game information
   - Handles visual effects

8. **MenuRenderer.java** - Menu system
   - Main menu with Play, Help, Quit buttons
   - Game mode selection screen
   - Help screen with controls
   - Mouse interaction handling

9. **GameEngine.java** - Base game engine (provided)
   - Graphics rendering
   - Event handling
   - Audio support
   - Game loop management

---

##  Additional Features

### 1. **Life System** 
- Each player starts with 5 lives
- Lose a life when hitting walls, yourself, or poison
- Game continues until all lives are lost
- Snake resets to initial position after losing a life

### 2. **Poison Apples**
- Poison appears randomly on the grid
- Eating poison causes immediate life loss
- Different visual appearance from regular apples
- Strategic element added to gameplay

### 3. **Two-Player Competitive Mode** 
- Local multiplayer support
- Independent scoring and life systems
- Independent apple and different images for that
- Collision detection between players
- Winner determination based on scores
- Shared poison field (both players affected)

### 4. **Countdown Timer** 
- 3-second countdown before two-player game starts
- Displays player controls reminder
- Builds anticipation for competitive matches
- Prevents unfair early starts

### 5. **Pause Functionality** 
- Press 'P' to pause/resume game
- Visual "PAUSED" overlay
- Helpful resume instruction displayed
- Useful for taking breaks

### 6. **Complete Menu System** 
- Interactive main menu with buttons
- Game mode selection screen
- Comprehensive help screen
- Mouse-based navigation
- Back navigation support

### 7. **Help Screen** 
- Detailed game instructions
- Control schemes for both modes
- Game object explanations
- Visual back button
- Accessible from main menu

### 8. **High Score Tracking** 
- Tracks highest score in single-player mode
- Persists during session
- Displayed on game over screen
- Motivates replayability

### 9. **Audio System** 
- Background music loop
- Game over sound effect
- Automatic audio management
- Stops music on game over

### 10. **Visual Enhancements** 
- Different apple types with unique visuals
- Color-coded players in two-player mode
- Grid lines for better visibility
- Smooth rendering

### 11. **Game State Management** 
- Clean state transitions
- Restart functionality
- Return to menu option
- Proper resource cleanup


---

##  Scoring System

- **Apple eaten**: +10 points
- **Maximum snake size**: 20 segments
- **Starting lives**: 5 per player
- **High score**: Tracked in single-player mode

---

##  Game Objects

| Object | Description | Effect |
|--------|-------------|--------|
|  Green Apple | Regular food | +10 points, grows snake |
|  Blue Apple | Player 2 food (two-player) | +10 points, grows snake |
|  Poison | Dangerous item | -1 life |
|  Blue Snake | Player 1 | Controlled by arrow keys |
|  Green Snake | Player 2 | Controlled by WASD |

---

##  Technical Details

### Grid System
- Grid size: 20x20 cells
- Cell size: 25x25 pixels
- Window size: 500x560 pixels (includes info bar)

### Game Loop
- Frame rate: 6 FPS (controlled snake speed)
- Update cycle: Movement → Collision → Rendering
- Delta time based updates

### Data Structures
- Snake body: `ArrayList<Point>`
- Food positions: `Point` objects
- Multiple poisons: `List<Point>`

---

##  Code Quality

-  Object-oriented design principles
-  Clear separation of concerns
-  Encapsulation of game logic
-  Reusable components
-  No compilation errors or warnings
-  Commented code for clarity

---

##  Known Limitations

- High score resets when game is closed (no persistent storage)
- Maximum 2 players (local only)
- Fixed grid size and window dimensions

---

##  License

This project was created as an academic assignment for educational purposes.

---

##  Author claim

Created for Assignment 1 - Snake-game for 159.261 Game Programming

---


Enjoy the game! 


