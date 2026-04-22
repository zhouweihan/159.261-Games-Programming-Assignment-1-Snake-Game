import java.awt.Point;

public class GameWorld {
    private Main main;
    private boolean twoPlayerMode;
    private boolean gameOver;
    private int highScore;

    // Players
    private PlayerData player1;
    private PlayerData player2;

    // Food
    private FoodManager foodManager;

    // Countdown
    private int countdownSecond;
    private boolean countdownActive;
    private double countdownTimer;

    public GameWorld(Main main) {
        this.main = main;
        this.foodManager = new FoodManager(main);
        this.highScore = 0;
        this.countdownActive = false;
    }

    public void initSinglePlayer() {
        twoPlayerMode = false;
        gameOver = false;

        Snake snake = new Snake(Main.GRID_SIZE / 2, Main.GRID_SIZE / 2, 1, 0);
        player1 = new PlayerData(snake, Main.INITIAL_LIVES);
        player2 = null;

        foodManager.setApple(null);
        foodManager.setPoison(null);
        foodManager.generateApple(snake);
        foodManager.generatePoison(snake);
    }

    public void initTwoPlayer() {
        twoPlayerMode = true;
        gameOver = false;

        Snake snake1 = new Snake(5, Main.GRID_SIZE / 2, 1, 0);
        Snake snake2 = new Snake(Main.GRID_SIZE - 6, Main.GRID_SIZE / 2, -1, 0);

        player1 = new PlayerData(snake1, Main.INITIAL_LIVES);
        player2 = new PlayerData(snake2, Main.INITIAL_LIVES);

        foodManager.setApple1(null);
        foodManager.setApple2(null);
        foodManager.clearPoisons();

        foodManager.generateAppleForTwoPlayer(1, snake1, snake2, null);
        foodManager.generateAppleForTwoPlayer(2, snake1, snake2, foodManager.getApple1());
        foodManager.generatePoisonForTwoPlayer(snake1, snake2);
        foodManager.generatePoisonForTwoPlayer(snake1, snake2);

        countdownActive = true;
        countdownTimer = 0;
        countdownSecond = 3;
    }

    public void update(double dt) {
        if (countdownActive) {
            countdownTimer += dt;
            if (countdownTimer >= 1.0) {
                countdownTimer -= 1.0;
                countdownSecond--;
                if (countdownSecond <= 0) {
                    countdownActive = false;
                    main.setPaused(false);
                }
            }
            return;
        }

        if (!twoPlayerMode) {
            updateSinglePlayer();
        } else {
            updateTwoPlayer();
        }
    }

    private void updateSinglePlayer() {
        Snake snake = player1.getSnake();
        snake.applyDirection();

        Point head = snake.getHead();
        int newX = head.x + snake.getDirectionX();
        int newY = head.y + snake.getDirectionY();

        // Wall collision
        if (newX < 0 || newX >= Main.GRID_SIZE || newY < 0 || newY >= Main.GRID_SIZE) {
            handleSinglePlayerCollision();
            return;
        }

        // Self collision
        if (snake.checkSelfCollision(newX, newY)) {
            handleSinglePlayerCollision();
            return;
        }

        // SHIFT all existing snake positions down by one array element
        snake.shiftPositions();
        snake.setHeadPosition(newX, newY);

        // Check apple
        Point apple = foodManager.getApple();
        if (newX == apple.x && newY == apple.y) {
            player1.addScore(10);
            if (snake.canGrow(Main.MAX_SNAKE_SIZE)) {
                snake.grow();
            }
            foodManager.generateApple(snake);
        }

        // Check poison
        Point poison = foodManager.getPoison();
        if (poison != null && newX == poison.x && newY == poison.y) {
            player1.loseLife();
            foodManager.generatePoison(snake);
            if (!player1.isAlive()) {
                gameOver = true;
                if (player1.getScore() > highScore) {
                    highScore = player1.getScore();
                }
            }
        }
    }

    private void updateTwoPlayer() {
        updatePlayer(player1, foodManager.getApple1(), 1);
        updatePlayer(player2, foodManager.getApple2(), 2);

        // Keep exactly 2 poisons
        while (foodManager.getPoisons().size() < 2) {
            foodManager.generatePoisonForTwoPlayer(player1.getSnake(), player2.getSnake());
        }
    }

    private void updatePlayer(PlayerData player, Point playerApple, int playerNum) {
        Snake snake = player.getSnake();
        snake.applyDirection();

        Point head = snake.getHead();
        int newX = head.x + snake.getDirectionX();
        int newY = head.y + snake.getDirectionY();

        // Wall collision
        if (newX < 0 || newX >= Main.GRID_SIZE || newY < 0 || newY >= Main.GRID_SIZE) {
            handlePlayerCollision(player, playerNum);
            return;
        }

        // Self collision
        if (snake.checkSelfCollision(newX, newY)) {
            handlePlayerCollision(player, playerNum);
            return;
        }

        // Collision with other snake
        PlayerData other = (playerNum == 1) ? player2 : player1;
        if (other != null && snake.checkCollisionWithSnake(other.getSnake(), newX, newY)) {
            handlePlayerCollision(player, playerNum);
            return;
        }

        // SHIFT all existing snake positions down by one array element
        snake.shiftPositions();
        snake.setHeadPosition(newX, newY);

        // Check apple
        if (newX == playerApple.x && newY == playerApple.y) {
            player.addScore(10);
            if (snake.canGrow(Main.MAX_SNAKE_SIZE)) {
                snake.grow();
            }
            if (playerNum == 1) {
                foodManager.generateAppleForTwoPlayer(1, player1.getSnake(), player2.getSnake(), foodManager.getApple2());
            } else {
                foodManager.generateAppleForTwoPlayer(2, player1.getSnake(), player2.getSnake(), foodManager.getApple1());
            }
        }

        // Check poison
        for (int i = 0; i < foodManager.getPoisons().size(); i++) {
            Point poison = foodManager.getPoisons().get(i);
            if (newX == poison.x && newY == poison.y) {
                foodManager.getPoisons().remove(i);
                player.loseLife();
                if (!player.isAlive()) {
                    gameOver = true;
                } else {
                    resetPlayerPosition(player, playerNum);
                }
                break;
            }
        }
    }

    private void handleSinglePlayerCollision() {
        player1.loseLife();
        if (!player1.isAlive()) {
            gameOver = true;
            if (player1.getScore() > highScore) {
                highScore = player1.getScore();
            }
        } else {
            resetSinglePlayerSnake();
        }
    }

    private void handlePlayerCollision(PlayerData player, int playerNum) {
        player.loseLife();
        if (!player.isAlive()) {
            gameOver = true;
        } else {
            resetPlayerPosition(player, playerNum);
        }
    }

    private void resetSinglePlayerSnake() {
        Snake snake = player1.getSnake();
        snake.reset();
    }

    private void resetPlayerPosition(PlayerData player, int playerNum) {
        Snake snake = player.getSnake();
        snake.reset();
    }

    // Getters
    public boolean isTwoPlayerMode() { return twoPlayerMode; }
    public boolean isGameOver() { return gameOver; }
    public boolean isCountdownActive() { return countdownActive; }
    public int getCountdownSecond() { return countdownSecond; }
    public int getScore1() { return player1 != null ? player1.getScore() : 0; }
    public int getScore2() { return player2 != null ? player2.getScore() : 0; }
    public int getLives1() { return player1 != null ? player1.getLives() : 0; }
    public int getLives2() { return player2 != null ? player2.getLives() : 0; }
    public int getHighScore() { return highScore; }
    public PlayerData getPlayer1() { return player1; }
    public PlayerData getPlayer2() { return player2; }
    public FoodManager getFoodManager() { return foodManager; }
}