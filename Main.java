import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Main extends GameEngine {
    // constants
    public static final int GRID_SIZE = 20;
    public static final int CELL_SIZE = 25;
    public static final int MAX_SNAKE_SIZE = 20;
    public static final int INITIAL_LIVES = 5;

    // images
    private Image headImage;
    private Image bodyImage;
    private Image appleImage;
    private Image poisonImage;
    private Image appleImage2;

    // audio
    private AudioClip backgroundMusic;
    private AudioClip gameOverSound;

    // game components
    private GameWorld gameWorld;
    private InputHandler inputHandler;
    private GameRenderer gameRenderer;
    private MenuRenderer menuRenderer;

    // game flow
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean paused = false;

    @Override
    public void init() {
        setWindowSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE + 60);

        // load images
        headImage = loadImage("head.png");
        bodyImage = loadImage("dot.png");
        appleImage = loadImage("apple.png");
        poisonImage = loadImage("poison.png");
        appleImage2 = loadImage("apple2.png");

        // load audio
        backgroundMusic = loadAudio("background.wav");
        gameOverSound = loadAudio("gameover.wav");
        if (backgroundMusic != null) {
            startAudioLoop(backgroundMusic, -10.0f);
        }

        // initialize components
        gameRenderer = new GameRenderer(this);
        menuRenderer = new MenuRenderer(this);
        inputHandler = new InputHandler(this);

        // initialize game
        gameWorld = new GameWorld(this);
        gameWorld.initSinglePlayer();
    }

    // Game initialization methods
    public void startSinglePlayer() {
        gameWorld = new GameWorld(this);
        gameWorld.initSinglePlayer();
        gameStarted = true;
        gameOver = false;
        paused = false;
    }

    public void startTwoPlayer() {
        gameWorld = new GameWorld(this);
        gameWorld.initTwoPlayer();
        gameStarted = true;
        gameOver = false;
        paused = false;
    }

    public void restartGame() {
        if (!gameWorld.isTwoPlayerMode()) {
            gameWorld.initSinglePlayer();
        } else {
            gameWorld.initTwoPlayer();
        }
        gameOver = false;
        if (backgroundMusic != null) {
            startAudioLoop(backgroundMusic, -10.0f);
        }
    }

    public void returnToMenu() {
        gameStarted = false;
        gameOver = false;
        if (backgroundMusic != null) {
            startAudioLoop(backgroundMusic, -10.0f);
        }
    }

    public void togglePause() {
        if (!gameOver) {
            paused = !paused;
        }
    }

    public void setGameOver(boolean over) {
        this.gameOver = over;
        if (over) {
            if (backgroundMusic != null) stopAudioLoop(backgroundMusic);
            if (gameOverSound != null) playAudio(gameOverSound);
        }
    }

    @Override
    public void update(double dt) {
        if (!gameStarted || gameOver || paused) {
            return;
        }
        gameWorld.update(dt);
        if (gameWorld.isGameOver()) {
            gameOver = true;
            if (backgroundMusic != null) stopAudioLoop(backgroundMusic);
            if (gameOverSound != null) playAudio(gameOverSound);
        }
    }

    @Override
    public void paintComponent() {
        clearBackground(width(), height());
        changeBackgroundColor(black);

        if (!gameStarted) {
            menuRenderer.render();
            return;
        }

        // draw grid
        changeColor(new Color(50, 50, 50));
        for (int i = 0; i <= GRID_SIZE; i++) {
            drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE);
            drawLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE);
        }

        // draw game
        gameRenderer.render(gameWorld);

        // draw countdown
        if (gameWorld.isCountdownActive()) {
            drawCountdown();
        }

        // draw pause
        if (paused && !gameWorld.isCountdownActive()) {
            changeColor(black);
            drawBoldText(width() / 2 - 80, height() / 2, "PAUSED", "Arial", 40);
            drawText(width() / 2 - 120, height() / 2 + 40, "Press P to Resume", "Arial", 20);
        }

        // draw game over
        if (gameOver) {
            drawGameOver();
        }
    }

    private void drawCountdown() {
        changeColor(new Color(0, 0, 0, 150));
        drawSolidRectangle(0, 0, width(), height());
        changeColor(blue);
        drawBoldText(width() / 2 - 200, height() / 2 - 120, "Player 1 (Blue): Arrow Keys", "Arial", 28);
        changeColor(green);
        drawBoldText(width() / 2 - 200, height() / 2 - 70, "Player 2 (Green): WASD", "Arial", 28);
        changeColor(yellow);
        drawBoldText(width() / 2 - 30, height() / 2 + 20, String.valueOf(gameWorld.getCountdownSecond()), "Arial", 100);
        drawBoldText(width() / 2 - 120, height() / 2 + 100, "GET READY!", "Arial", 32);
    }

    private void drawGameOver() {
        changeColor(red);
        drawBoldText(width() / 2 - 100, height() / 2 - 60, "GAME OVER!", "Arial", 40);
        changeColor(black);

        if (!gameWorld.isTwoPlayerMode()) {
            drawText(width() / 2 - 130, height() / 2 - 10, "Final Score: " + gameWorld.getScore1(), "Arial", 20);
            drawText(width() / 2 - 130, height() / 2 + 15, "High Score: " + gameWorld.getHighScore(), "Arial", 20);
        } else {
            drawText(width() / 2 - 150, height() / 2 - 10,
                    "P1 Score: " + gameWorld.getScore1() + " | P2 Score: " + gameWorld.getScore2(), "Arial", 20);
            String winner = (gameWorld.getScore1() > gameWorld.getScore2()) ? "Player 1 Wins!" :
                    (gameWorld.getScore2() > gameWorld.getScore1()) ? "Player 2 Wins!" : "It's a Tie!";
            changeColor(green);
            drawBoldText(width() / 2 - 100, height() / 2 + 15, winner, "Arial", 24);
        }

        changeColor(black);
        drawText(width() / 2 - 150, height() / 2 + 50, "Press R to Restart", "Arial", 20);
        drawText(width() / 2 - 150, height() / 2 + 75, "Press M for Menu", "Arial", 20);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        inputHandler.handleKeyPress(event);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (!gameStarted) {
            menuRenderer.handleMouseClick(event);
        }
    }

    // Getters
    public boolean isGameStarted() { return gameStarted; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public void setGameStarted(boolean started) { gameStarted = started; }
    public void setPaused(boolean p) { paused = p; }
    public GameWorld getGameWorld() { return gameWorld; }
    public Image getHeadImage() { return headImage; }
    public Image getBodyImage() { return bodyImage; }
    public Image getAppleImage() { return appleImage; }
    public Image getAppleImage2() { return appleImage2; }
    public Image getPoisonImage() { return poisonImage; }
    public AudioClip getBackgroundMusic() { return backgroundMusic; }
    public AudioClip getGameOverSound() { return gameOverSound; }

    public static void main(String[] args) {
        Main game = new Main();
        createGame(game, 6);
    }
}