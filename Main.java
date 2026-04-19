import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Main extends GameEngine {
    // constants
    private static final int GRID_SIZE = 20; // size of grid
    private static final int CELL_SIZE = 25; // the size of each cell
    private static final int MAX_SNAKE_SIZE = 20; // maximum size of snake including head

    // game objects images
    private Image headImage;
    private Image bodyImage;
    private Image appleImage;

    // data structure of snake(location of snake and apple)
    private List<Point> snake;
    private Point apple;
    private int snakeSize; // track current size of the snake

    // movement direction
    private int directionX = 1; // the original direction is right
    private int directionY = 0;
    private int nextDirectionX = 1;
    private int nextDirectionY = 0;

    // game statue
    private boolean gameOver = false;
    private int score = 0;

    @Override
    public void init() {
        // set the size of windows
        setWindowSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE + 50);

        // load the images
        headImage = loadImage("head.png");
        bodyImage = loadImage("dot.png");
        appleImage = loadImage("apple.png");

        // initialize the snake （the length is 3 at the beginning）
        snake = new ArrayList<>();
        //start at center
        int startX = GRID_SIZE / 2;
        int startY = GRID_SIZE / 2;
        //create the head of snake
        snake.add(new Point(startX, startY));
        //create the body of snake
        snake.add(new Point(startX - 1, startY));
        //create the tail of snake
        snake.add(new Point(startX - 2, startY));

        // set initial snake size
        snakeSize = 3;

        // generate the first apple
        generateApple();
    }

    @Override
    public void update(double dt) {
        if (gameOver) {
            return;
        }

        // update the direction
        directionX = nextDirectionX;
        directionY = nextDirectionY;

        // get the new location of the head
        Point head = snake.get(0);
        //get the location of the snake head(the first element of the list)
        int newX = head.x + directionX;
        int newY = head.y + directionY;

        // check if out of bound
        if (newX < 0 || newX >= GRID_SIZE || newY < 0 || newY >= GRID_SIZE) {
            gameOver = true;
            return;
        }

        // check if hit itself
        for (Point segment : snake) {
            if (segment.x == newX && segment.y == newY) {
                gameOver = true;
                return;
            }
        }

        // shift all existing snake positions down by one array element
        // move from tail to head to avoid overwriting
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

            // add new element to the end of the snake if not at max size
            if (snakeSize < MAX_SNAKE_SIZE) {
                Point tail = snake.get(snake.size() - 1);
                snake.add(new Point(tail.x, tail.y));
                snakeSize++;
            }

            generateApple();
        }
    }

    @Override
    public void paintComponent() {
        // clear background
        clearBackground(width(), height());
        changeBackgroundColor(black);

        // draw grid lines (optional)
        changeColor(new Color(50, 50, 50));
        for (int i = 0; i <= GRID_SIZE; i++) {
            drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE);
            drawLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE);
        }

        // draw apple
        if (apple != null) {
            drawImage(appleImage, apple.x * CELL_SIZE, apple.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // draw snake
        for (int i = 0; i < snake.size(); i++) {
            Point segment = snake.get(i);
            if (i == 0) {
                // draw snake head
                drawImage(headImage, segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            } else {
                // draw snake body
                drawImage(bodyImage, segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // draw score
        changeColor(black);
        drawBoldText(10, GRID_SIZE * CELL_SIZE + 30, "Score: " + score, "Arial", 20);

        // if game over, display game over message
        if (gameOver) {
            changeColor(red);
            drawBoldText(width() / 2 - 100, height() / 2 - 20, "GAME OVER!", "Arial", 40);
            changeColor(blue);
            drawText(width() / 2 - 150, height() / 2 + 30, "Press R to Restart", "Arial", 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        // prevent reverse movement
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
            case KeyEvent.VK_R:
                if (gameOver) {
                    restartGame();
                }
                break;
        }
    }

    // generate apple at random position
    private void generateApple() {
        boolean validPosition = false;

        while (!validPosition) {
            // use GameEngine's rand() method instead of creating new Random
            int x = rand(GRID_SIZE);
            int y = rand(GRID_SIZE);

            // check if this position is occupied by snake
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

    // restart the game
    private void restartGame() {
        snake.clear();
        int startX = GRID_SIZE / 2;
        int startY = GRID_SIZE / 2;
        snake.add(new Point(startX, startY));
        snake.add(new Point(startX - 1, startY));
        snake.add(new Point(startX - 2, startY));

        directionX = 1;
        directionY = 0;
        nextDirectionX = 1;
        nextDirectionY = 0;

        snakeSize = 3;
        score = 0;
        gameOver = false;

        generateApple();
    }

    public static void main(String[] args) {
        Main game = new Main();
        createGame(game, 6); // set frame rate to 10 to control snake speed
    }
}
