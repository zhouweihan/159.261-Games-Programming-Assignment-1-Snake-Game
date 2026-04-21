import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Main extends GameEngine {
    // constants
    private static final int GRID_SIZE = 20; // size of grid
    private static final int CELL_SIZE = 25; // the size of each cell
    private static final int MAX_SNAKE_SIZE = 20; // maximum size of snake including head
    private static final int INITIAL_LIVES = 5; // five lives for each snake

    // game objects images
    private Image headImage;
    private Image bodyImage;
    private Image appleImage;
    private Image poisonImage;
    private Image appleImage2;

    //audio clips
    private AudioClip backgroundMusic;
    private AudioClip gameOverSound;

    // single player mode data
    private List<Point> snake;
    private Point apple;
    private Point poison;
    private int snakeSize;

    // two player mode data
    private List<Point> snake1;
    private List<Point> snake2;
    private Point apple1;
    private Point apple2;
    private List<Point> poisons;
    private int snake1Size;
    private int snake2Size;

    // movement direction - player 1 (arrow keys)
    private int directionX = 1;
    private int directionY = 0;
    private int nextDirectionX = 1;
    private int nextDirectionY = 0;

    // movement direction - player 2 (WASD)
    private int direction2X = -1;
    private int direction2Y = 0;
    private int nextDirection2X = -1;
    private int nextDirection2Y = 0;

    // game state
    private boolean gameOver = false;
    private boolean paused = false;
    private boolean gameStarted = false;
    private boolean twoPlayerMode = false;
    private int score = 0;
    private int score2 = 0;
    private int highScore = 0;
    private int lives = INITIAL_LIVES;
    private int lives2 = INITIAL_LIVES;

    //countdown state for two player mode
    private int countdownSecond = 3;
    private boolean countdownActive = false;
    private double countdownTimer = 0;

    @Override
    public void init() {
        // set the size of windows
        setWindowSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE + 60);

        // load the images
        headImage = loadImage("head.png");
        bodyImage = loadImage("dot.png");
        appleImage = loadImage("apple.png");
        poisonImage = loadImage("poison.png");
        appleImage2 = loadImage("apple2.png");

        backgroundMusic = loadAudio("background.wav");
        gameOverSound = loadAudio("gameover.wav");

        if(backgroundMusic != null){
            startAudioLoop(backgroundMusic,-10.0f);
        }
        // initialize single player
        initSinglePlayer();
    }

    // initialize single player game
    private void initSinglePlayer() {
        twoPlayerMode = false;
        snake = new ArrayList<>();
        int startX = GRID_SIZE / 2;
        int startY = GRID_SIZE / 2;
        snake.add(new Point(startX, startY));
        snake.add(new Point(startX - 1, startY));
        snake.add(new Point(startX - 2, startY));
        snakeSize = 3;
        lives = INITIAL_LIVES;
        score = 0;
        generateApple();
        generatePoison();
    }

    // initialize two player game
    private void initTwoPlayer() {
        twoPlayerMode = true;

        // Player 1 (starts on left, moves right)
        snake1 = new ArrayList<>();
        int startX1 = 5;
        int startY1 = GRID_SIZE / 2;
        snake1.add(new Point(startX1, startY1));
        snake1.add(new Point(startX1 - 1, startY1));
        snake1.add(new Point(startX1 - 2, startY1));
        snake1Size = 3;
        lives = INITIAL_LIVES;
        score = 0;

        // Player 2 (starts on right, moves left)
        snake2 = new ArrayList<>();
        int startX2 = GRID_SIZE - 6;
        int startY2 = GRID_SIZE / 2;
        snake2.add(new Point(startX2, startY2));
        snake2.add(new Point(startX2 + 1, startY2));
        snake2.add(new Point(startX2 + 2, startY2));
        snake2Size = 3;
        lives2 = INITIAL_LIVES;
        score2 = 0;

        // Generate apples for both players
        generateAppleForTwoPlayer(1);
        generateAppleForTwoPlayer(2);

        // Generate poisons for both players
        poisons =new ArrayList<>();
        generatePoisonForTwoPlayer();
        generatePoisonForTwoPlayer();

        //start the countdown
        countdownActive = true;
        countdownTimer = 0;
        countdownSecond = 3;
        paused = true;
    }

    @Override
    public void update(double dt) {
        if (gameOver || !gameStarted) {
            return;
        }

        //handle countdown for two player mode
        if(countdownActive){
            countdownTimer += dt;
            if(countdownTimer >= 1.0){
                countdownTimer -= 1.0;
                countdownSecond--;
                if(countdownSecond <= 0){
                    countdownActive = false;
                    paused = false;
                }
            }
            return;
        }
        if(paused)
        {
            return;
        }
        if (!twoPlayerMode) {
            updateSinglePlayer();
        } else {
            updateTwoPlayer();
        }
    }

    // update single player game
    private void updateSinglePlayer() {
        // update the direction
        directionX = nextDirectionX;
        directionY = nextDirectionY;

        // get the new location of the head
        Point head = snake.get(0);
        int newX = head.x + directionX;
        int newY = head.y + directionY;

        // check if out of bound
        if (newX < 0 || newX >= GRID_SIZE || newY < 0 || newY >= GRID_SIZE) {
            handleCollision();
            return;
        }

        // check if hit itself
        for (Point segment : snake) {
            if (segment.x == newX && segment.y == newY) {
                handleCollision();
                return;
            }
        }

        // shift all existing snake positions down by one array element
        for (int i = snake.size() - 1; i > 0; i--) {
            Point previous = snake.get(i - 1);
            snake.get(i).x = previous.x;
            snake.get(i).y = previous.y;
        }

        // update the head position
        snake.get(0).x = newX;
        snake.get(0).y = newY;

        // check if eat apple
        if (newX == apple.x && newY == apple.y) {
            score += 10;
            if (snakeSize < MAX_SNAKE_SIZE) {
                Point tail = snake.get(snake.size() - 1);
                snake.add(new Point(tail.x, tail.y));
                snakeSize++;
            }
            generateApple();
        }

        // check if eat poison
        if (poison != null && newX == poison.x && newY == poison.y) {
            lives--;
            generatePoison();
            if (lives <= 0) {
                gameOver = true;
                if (score > highScore) {
                    highScore = score;
                }
                if(backgroundMusic != null){
                    stopAudioLoop(backgroundMusic);
                }
                if(gameOverSound != null){
                    playAudio(gameOverSound);
                }
            }
        }
    }

    // update two player game
    private void updateTwoPlayer() {
        updatePlayerSnake(snake1, directionX, directionY, nextDirectionX, nextDirectionY, 1);
        updatePlayerSnake(snake2, direction2X, direction2Y, nextDirection2X, nextDirection2Y, 2);
    }

    // update individual player snake
    private void updatePlayerSnake(List<Point> playerSnake, int dirX, int dirY, int nextDirX, int nextDirY, int playerNum) {
        // update direction
        if (playerNum == 1) {
            directionX = nextDirectionX;
            directionY = nextDirectionY;
            dirX = directionX;
            dirY = directionY;
        } else {
            direction2X = nextDirection2X;
            direction2Y = nextDirection2Y;
            dirX = direction2X;
            dirY = direction2Y;
        }

        Point head = playerSnake.get(0);
        int newX = head.x + dirX;
        int newY = head.y + dirY;

        // check wall collision
        if (newX < 0 || newX >= GRID_SIZE || newY < 0 || newY >= GRID_SIZE) {
            handlePlayerCollision(playerNum);
            return;
        }

        // check self collision
        for (Point segment : playerSnake) {
            if (segment.x == newX && segment.y == newY) {
                handlePlayerCollision(playerNum);
                return;
            }
        }

        // check collision with other snake
        List<Point> otherSnake = (playerNum == 1) ? snake2 : snake1;
        for (Point segment : otherSnake) {
            if (segment.x == newX && segment.y == newY) {
                handlePlayerCollision(playerNum);
                return;
            }
        }

        // shift positions
        for (int i = playerSnake.size() - 1; i > 0; i--) {
            Point previous = playerSnake.get(i - 1);
            playerSnake.get(i).x = previous.x;
            playerSnake.get(i).y = previous.y;
        }

        playerSnake.get(0).x = newX;
        playerSnake.get(0).y = newY;

        // check apple
        Point applePoint = (playerNum == 1) ? apple1 : apple2;
        if (newX == applePoint.x && newY == applePoint.y) {
            if (playerNum == 1) {
                score += 10;
                if (snake1Size < MAX_SNAKE_SIZE) {
                    Point tail = snake1.get(snake1.size() - 1);
                    snake1.add(new Point(tail.x, tail.y));
                    snake1Size++;
                }
                generateAppleForTwoPlayer(1);
            } else {
                score2 += 10;
                if (snake2Size < MAX_SNAKE_SIZE) {
                    Point tail = snake2.get(snake2.size() - 1);
                    snake2.add(new Point(tail.x, tail.y));
                    snake2Size++;
                }
                generateAppleForTwoPlayer(2);
            }
        }

        // check poison
        boolean hitpoison = false;
        int poisonIndex=-1;
        for(int i=0;i<poisons.size();i++)
        {
            Point poisonPoint = poisons.get(i);
            if (poisonPoint != null && newX == poisonPoint.x && newY == poisonPoint.y) {
                hitpoison = true;
                poisonIndex=i;
                break;
            }
        }

        if(hitpoison){
            poisons.remove(poisonIndex);

            if(playerNum ==1)
            {
                lives--;
                if(lives<=0){
                    gameOver = true;
                    if(backgroundMusic!=null){
                        stopAudioLoop(backgroundMusic);
                    }
                    if(gameOverSound!=null){
                        playAudio(gameOverSound);
                    }
                }else{
                    resetPlayerPosition(1);
                }
            }
            if(playerNum == 2){
                lives2--;
                if(lives2<=0){
                    gameOver = true;
                    if(backgroundMusic!=null){
                        stopAudioLoop(backgroundMusic);
                    }
                    if(gameOverSound!=null){
                        playAudio(gameOverSound);
                    }
                }else{
                    resetPlayerPosition(2);
                }
            }
        }

        if(poisons.size()<2){
            generatePoisonForTwoPlayer();
        }

    }

    // handle collision in single player
    private void handleCollision() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
            if (score > highScore) {
                highScore = score;
            }
            if(backgroundMusic!=null){
                stopAudioLoop(backgroundMusic);
            }
            if(gameOverSound!=null){
                playAudio(gameOverSound);
            }
        } else {
            resetSnakePosition();
        }
    }

    // handle collision for specific player in two player mode
    private void handlePlayerCollision(int playerNum) {
        if (playerNum == 1) {
            lives--;
            if (lives <= 0) {
                gameOver = true;
                if(backgroundMusic!=null){
                    stopAudioLoop(backgroundMusic);
                }
                if(gameOverSound!=null){
                    playAudio(gameOverSound);
                }
            } else {
                resetPlayerPosition(1);
            }
        } else {
            lives2--;
            if (lives2 <= 0) {
                gameOver = true;
                if(backgroundMusic!=null){
                    stopAudioLoop(backgroundMusic);
                }
                if(gameOverSound!=null){
                    playAudio(gameOverSound);
                }
            } else {
                resetPlayerPosition(2);
            }
        }
    }

    // reset snake position after losing a life
    private void resetSnakePosition() {
        snake.clear();
        int startX = GRID_SIZE / 2;
        int startY = GRID_SIZE / 2;
        snake.add(new Point(startX, startY));
        snake.add(new Point(startX - 1, startY));
        snake.add(new Point(startX - 2, startY));
        snakeSize = 3;
        directionX = 1;
        directionY = 0;
        nextDirectionX = 1;
        nextDirectionY = 0;
    }

    // reset player position in two player mode
    private void resetPlayerPosition(int playerNum) {
        if (playerNum == 1) {
            snake1.clear();
            int startX = 5;
            int startY = GRID_SIZE / 2;
            snake1.add(new Point(startX, startY));
            snake1.add(new Point(startX - 1, startY));
            snake1.add(new Point(startX - 2, startY));
            snake1Size = 3;
            directionX = 1;
            directionY = 0;
            nextDirectionX = 1;
            nextDirectionY = 0;
        } else {
            snake2.clear();
            int startX = GRID_SIZE - 6;
            int startY = GRID_SIZE / 2;
            snake2.add(new Point(startX, startY));
            snake2.add(new Point(startX + 1, startY));
            snake2.add(new Point(startX + 2, startY));
            snake2Size = 3;
            direction2X = -1;
            direction2Y = 0;
            nextDirection2X = -1;
            nextDirection2Y = 0;
        }
    }

    @Override
    public void paintComponent() {
        // clear background
        clearBackground(width(), height());
        changeBackgroundColor(black);

        if (!gameStarted) {
            drawStartMenu();
            return;
        }

        // draw grid lines
        changeColor(new Color(50, 50, 50));
        for (int i = 0; i <= GRID_SIZE; i++) {
            drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE);
            drawLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE);
        }

        if (!twoPlayerMode) {
            paintSinglePlayer();
        } else {
            paintTwoPlayer();
        }

        if(countdownActive){
            drawCountdown();
        }
        // draw pause message
        if (paused &&!countdownActive) {
            changeColor(black);
            drawBoldText(width() / 2 - 80, height() / 2, "PAUSED", "Arial", 40);
            changeColor(black);
            drawText(width() / 2 - 120, height() / 2 + 40, "Press P to Resume", "Arial", 20);
        }

        // draw game over message
        if (gameOver) {
            drawGameOver();
        }
    }

    // paint single player game
    private void paintSinglePlayer() {
        // draw apple
        if (apple != null) {
            drawImage(appleImage, apple.x * CELL_SIZE, apple.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // draw poison
        if (poison != null) {
            if (poisonImage != null) {
                drawImage(poisonImage, poison.x * CELL_SIZE, poison.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // draw snake
        for (int i = 0; i < snake.size(); i++) {
            Point segment = snake.get(i);
            if (i == 0) {
                drawImage(headImage, segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            } else {
                drawImage(bodyImage, segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // draw info
        changeColor(black);
        drawBoldText(10, GRID_SIZE * CELL_SIZE + 20, "Score: " + score + " | Lives: " + lives + " | High: " + highScore, "Arial", 16);
        drawText(10, GRID_SIZE * CELL_SIZE + 45, "Controls: Arrow Keys | P: Pause | R: Restart", "Arial", 14);
    }

    // paint two player game
    private void paintTwoPlayer() {
        // draw apples
        if (apple1 != null) {
            drawImage(appleImage2, apple1.x * CELL_SIZE, apple1.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        if (apple2 != null) {
            drawImage(appleImage, apple2.x * CELL_SIZE, apple2.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // draw poisons
        if(poisons !=null)
        {
            for(Point poison:poisons){
                if(poisonImage != null){
                    drawImage(poisonImage, poison.x * CELL_SIZE, poison.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // draw snake 1 (blue)
        changeColor(blue);
        for (int i = 0; i < snake1.size(); i++) {
            Point segment = snake1.get(i);
            if (i == 0) {
                drawSolidCircle(segment.x * CELL_SIZE + CELL_SIZE / 2, segment.y * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE / 2 - 1);
            } else {
                drawSolidCircle(segment.x * CELL_SIZE + CELL_SIZE / 2, segment.y * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE / 2 - 2);
            }
        }

        // draw snake 2 (green)
        changeColor(green);
        for (int i = 0; i < snake2.size(); i++) {
            Point segment = snake2.get(i);
            if (i == 0) {
                drawSolidCircle(segment.x * CELL_SIZE + CELL_SIZE / 2, segment.y * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE / 2 - 1);
            } else {
                drawSolidCircle(segment.x * CELL_SIZE + CELL_SIZE / 2, segment.y * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE / 2 - 2);
            }
        }

        // draw info
        changeColor(black);
        drawBoldText(10, GRID_SIZE * CELL_SIZE + 20, "P1 Score: " + score + " Lives: " + lives + " | P2 Score: " + score2 + " Lives: " + lives2, "Arial", 14);
        drawText(10, GRID_SIZE * CELL_SIZE + 45, "P1: Arrow Keys | P2: WASD | P: Pause", "Arial", 14);
    }

    // draw start menu
    private void drawStartMenu() {
        changeColor(green);
        drawBoldText(width() / 2 - 100, height() / 2 - 100, "SNAKE GAME", "Arial", 50);

        changeColor(black);
        drawText(width() / 2 - 180, height() / 2 - 40, "1. Single Player - Arrow Keys", "Arial", 20);
        drawText(width() / 2 - 180, height() / 2 - 10, "2. Two Players - Arrow + WASD", "Arial", 20);

        changeColor(black);
        drawBoldText(width() / 2 - 180, height() / 2 + 40, "Press 1 or 2 to Select Mode", "Arial", 24);

        changeColor(black);
        drawText(width() / 2 - 150, height() / 2 + 80, "P: Pause | R: Restart", "Arial", 18);
    }

    //draw countdown screen for two player mode
    private void drawCountdown() {
        changeColor(new Color (0,0,0,150));
        drawSolidRectangle(0,0,width(),height());

        changeColor(blue);
        drawBoldText(width() / 2 - 200, height() / 2 - 120, "Player 1 (Blue): Arrow Keys", "Arial", 28);
        changeColor(green);
        drawBoldText(width() / 2 - 200, height() / 2 - 70, "Player 2 (Green): WASD", "Arial", 28);

        changeColor(yellow);
        String countdownText = String.valueOf(countdownSecond);
        drawBoldText(width() / 2 - 30, height() / 2+20, countdownText, "Arial", 100);
        changeColor(black);
        drawBoldText(width() / 2 - 120, height() / 2+100, "GET READY!", "Arial", 32);
    }

    // draw game over screen
    private void drawGameOver() {
        changeColor(red);
        drawBoldText(width() / 2 - 100, height() / 2 - 60, "GAME OVER!", "Arial", 40);

        changeColor(black);
        if (!twoPlayerMode) {
            drawText(width() / 2 - 130, height() / 2 - 10, "Final Score: " + score, "Arial", 20);
            drawText(width() / 2 - 130, height() / 2 + 15, "High Score: " + highScore, "Arial", 20);
        } else {
            drawText(width() / 2 - 150, height() / 2 - 10, "P1 Score: " + score + " | P2 Score: " + score2, "Arial", 20);
            String winner = (score > score2) ? "Player 1 Wins!" : (score2 > score) ? "Player 2 Wins!" : "It's a Tie!";
            changeColor(green);
            drawBoldText(width() / 2 - 100, height() / 2 + 15, winner, "Arial", 24);
        }

        changeColor(black);
        drawText(width() / 2 - 150, height() / 2 + 50, "Press R to Restart", "Arial", 20);
        drawText(width() / 2 - 150, height() / 2 + 75, "Press M for Menu", "Arial", 20);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        // select game mode from menu
        if (!gameStarted) {
            if (event.getKeyCode() == KeyEvent.VK_1) {
                initSinglePlayer();
                gameStarted = true;
            } else if (event.getKeyCode() == KeyEvent.VK_2) {
                initTwoPlayer();
                gameStarted = true;
            }
            return;
        }

        // restart game
        if (gameOver && event.getKeyCode() == KeyEvent.VK_R) {
            if (!twoPlayerMode) {
                initSinglePlayer();
            } else {
                initTwoPlayer();
            }
            gameOver = false;
            if(backgroundMusic!=null){
                startAudioLoop(backgroundMusic,-10.0f);
            }
            return;
        }

        // back to menu
        if (gameOver && event.getKeyCode() == KeyEvent.VK_M) {
            gameStarted = false;
            gameOver = false;
            if(backgroundMusic!=null){
                startAudioLoop(backgroundMusic,-10.0f);
            }
            return;
        }

        // toggle pause
        if (event.getKeyCode() == KeyEvent.VK_P && !gameOver) {
            paused = !paused;
            return;
        }

        if (paused || gameOver||countdownActive) {
            return;
        }

        if (!twoPlayerMode) {
            handleSinglePlayerInput(event);
        } else {
            handleTwoPlayerInput(event);
        }
    }

    // handle input for single player
    private void handleSinglePlayerInput(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (directionY != 1) {
                    nextDirectionX = 0;
                    nextDirectionY = -1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (directionY != -1) {
                    nextDirectionX = 0;
                    nextDirectionY = 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (directionX != 1) {
                    nextDirectionX = -1;
                    nextDirectionY = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (directionX != -1) {
                    nextDirectionX = 1;
                    nextDirectionY = 0;
                }
                break;
        }
    }

    // handle input for two players
    private void handleTwoPlayerInput(KeyEvent event) {
        // Player 1 - Arrow Keys
        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (directionY != 1) {
                    nextDirectionX = 0;
                    nextDirectionY = -1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (directionY != -1) {
                    nextDirectionX = 0;
                    nextDirectionY = 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (directionX != 1) {
                    nextDirectionX = -1;
                    nextDirectionY = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (directionX != -1) {
                    nextDirectionX = 1;
                    nextDirectionY = 0;
                }
                break;
        }

        // Player 2 - WASD
        switch (event.getKeyCode()) {
            case KeyEvent.VK_W:
                if (direction2Y != 1) {
                    nextDirection2X = 0;
                    nextDirection2Y = -1;
                }
                break;
            case KeyEvent.VK_S:
                if (direction2Y != -1) {
                    nextDirection2X = 0;
                    nextDirection2Y = 1;
                }
                break;
            case KeyEvent.VK_A:
                if (direction2X != 1) {
                    nextDirection2X = -1;
                    nextDirection2Y = 0;
                }
                break;
            case KeyEvent.VK_D:
                if (direction2X != -1) {
                    nextDirection2X = 1;
                    nextDirection2Y = 0;
                }
                break;
        }
    }

    // generate apple for single player
    private void generateApple() {
        boolean validPosition = false;
        while (!validPosition) {
            int x = rand(GRID_SIZE);
            int y = rand(GRID_SIZE);
            boolean occupied = false;
            for (Point segment : snake) {
                if (segment.x == x && segment.y == y) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) {
                apple = new Point(x, y);
                validPosition = true;
            }
        }
    }

    // generate poison for single player
    private void generatePoison() {
        boolean validPosition = false;
        while (!validPosition) {
            int x = rand(GRID_SIZE);
            int y = rand(GRID_SIZE);
            boolean occupied = false;
            for (Point segment : snake) {
                if (segment.x == x && segment.y == y) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied && (apple == null || apple.x != x || apple.y != y)) {
                poison = new Point(x, y);
                validPosition = true;
            }
        }
    }

    // generate apple for two player mode
    private void generateAppleForTwoPlayer(int playerNum) {
        boolean validPosition = false;
        while (!validPosition) {
            int x = rand(GRID_SIZE);
            int y = rand(GRID_SIZE);
            boolean occupied = false;

            List<Point> s1 = snake1;
            List<Point> s2 = snake2;

            for (Point segment : s1) {
                if (segment.x == x && segment.y == y) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) {
                for (Point segment : s2) {
                    if (segment.x == x && segment.y == y) {
                        occupied = true;
                        break;
                    }
                }
            }

            Point otherApple = (playerNum == 1) ? apple2 : apple1;
            if (!occupied && otherApple != null && otherApple.x == x && otherApple.y == y) {
                occupied = true;
            }

            if (!occupied) {
                if (playerNum == 1) {
                    apple1 = new Point(x, y);
                } else {
                    apple2 = new Point(x, y);
                }
                validPosition = true;
            }
        }
    }

    // generate poison for two player mode
    private void generatePoisonForTwoPlayer() {
        boolean validPosition = false;
        while (!validPosition) {
            int x = rand(GRID_SIZE);
            int y = rand(GRID_SIZE);
            boolean occupied = false;

            for(Point segment : snake1) {
                if (segment.x == x && segment.y == y) {
                    occupied = true;
                    break;
                }
            }

            if(!occupied)
            {
                for(Point segment : snake2)
                {
                    if (segment.x == x && segment.y == y) {
                        occupied = true;
                        break;
                    }
                }
            }

            if(! occupied && apple1 != null && apple1.x == x && apple1.y == y)
            {
                occupied = true;
            }
            if(! occupied && apple2 != null && apple2.x == x && apple2.y == y)
            {
                occupied = true;
            }

            if (!occupied) {
                for (Point p : poisons) {
                    if(p.x == x && p.y == y){
                        occupied = true;
                        break;
                    }

                }
            }

            if(!occupied){
                poisons.add(new Point(x,y));
                validPosition = true;
            }
        }
    }

    public static void main(String[] args) {
        Main game = new Main();
        createGame(game, 6); // set frame rate to 6 to control snake speed
    }
}
