public class PlayerData {
    private int score;
    private int lives;
    private Snake snake;

    public PlayerData(Snake snake, int initialLives) {
        this.snake = snake;
        this.lives = initialLives;
        this.score = 0;
    }

    public int getScore() { return score; }
    public void addScore(int points) { score += points; }
    public int getLives() { return lives; }
    public void loseLife() { lives--; }
    public boolean isAlive() { return lives > 0; }
    public Snake getSnake() { return snake; }
    public void setSnake(Snake snake) { this.snake = snake; }
    public void resetScore() { score = 0; }
    public void resetLives(int initialLives) { lives = initialLives; }
}