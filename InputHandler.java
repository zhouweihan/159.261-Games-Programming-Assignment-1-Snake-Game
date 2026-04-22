import java.awt.event.KeyEvent;

public class InputHandler {
    private Main main;

    public InputHandler(Main main) {
        this.main = main;
    }

    public void handleKeyPress(KeyEvent event) {
        // Menu mode
        if (!main.isGameStarted()) {
            if (event.getKeyCode() == KeyEvent.VK_1) {
                main.startSinglePlayer();
            } else if (event.getKeyCode() == KeyEvent.VK_2) {
                main.startTwoPlayer();
            }
            return;
        }

        // Game over mode
        if (main.isGameOver()) {
            if (event.getKeyCode() == KeyEvent.VK_R) {
                main.restartGame();
            } else if (event.getKeyCode() == KeyEvent.VK_M) {
                main.returnToMenu();
            }
            return;
        }

        // Pause
        if (event.getKeyCode() == KeyEvent.VK_P) {
            main.togglePause();
            return;
        }

        if (main.isPaused() || main.getGameWorld().isCountdownActive()) {
            return;
        }

        // Game input
        GameWorld world = main.getGameWorld();
        if (!world.isTwoPlayerMode()) {
            handleSinglePlayerInput(event, world.getPlayer1().getSnake());
        } else {
            handleTwoPlayerInput(event, world.getPlayer1().getSnake(), world.getPlayer2().getSnake());
        }
    }

    private void handleSinglePlayerInput(KeyEvent event, Snake snake) {
        int dirX = snake.getDirectionX();
        int dirY = snake.getDirectionY();

        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (dirY != 1) snake.setDirection(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                if (dirY != -1) snake.setDirection(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                if (dirX != 1) snake.setDirection(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                if (dirX != -1) snake.setDirection(1, 0);
                break;
        }
    }

    private void handleTwoPlayerInput(KeyEvent event, Snake snake1, Snake snake2) {
        // Player 1 - Arrow Keys
        int dir1X = snake1.getDirectionX();
        int dir1Y = snake1.getDirectionY();

        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (dir1Y != 1) snake1.setDirection(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                if (dir1Y != -1) snake1.setDirection(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                if (dir1X != 1) snake1.setDirection(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                if (dir1X != -1) snake1.setDirection(1, 0);
                break;
        }

        // Player 2 - WASD
        int dir2X = snake2.getDirectionX();
        int dir2Y = snake2.getDirectionY();

        switch (event.getKeyCode()) {
            case KeyEvent.VK_W:
                if (dir2Y != 1) snake2.setDirection(0, -1);
                break;
            case KeyEvent.VK_S:
                if (dir2Y != -1) snake2.setDirection(0, 1);
                break;
            case KeyEvent.VK_A:
                if (dir2X != 1) snake2.setDirection(-1, 0);
                break;
            case KeyEvent.VK_D:
                if (dir2X != -1) snake2.setDirection(1, 0);
                break;
        }
    }
}