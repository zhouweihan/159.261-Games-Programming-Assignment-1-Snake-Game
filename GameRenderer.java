import java.awt.Color;
import java.awt.Point;
import java.util.List;

public class GameRenderer {
    private Main main;

    public GameRenderer(Main main) {
        this.main = main;
    }

    public void render(GameWorld world) {
        if (!world.isTwoPlayerMode()) {
            renderSinglePlayer(world);
        } else {
            renderTwoPlayer(world);
        }
    }

    private void renderSinglePlayer(GameWorld world) {
        // Draw apple
        Point apple = world.getFoodManager().getApple();
        if (apple != null) {
            main.drawImage(main.getAppleImage(), apple.x * Main.CELL_SIZE, apple.y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
        }

        // Draw poison
        Point poison = world.getFoodManager().getPoison();
        if (poison != null && main.getPoisonImage() != null) {
            main.drawImage(main.getPoisonImage(), poison.x * Main.CELL_SIZE, poison.y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
        }

        // Draw snake
        Snake snake = world.getPlayer1().getSnake();
        List<Point> body = snake.getBody();
        for (int i = 0; i < body.size(); i++) {
            Point segment = body.get(i);
            if (i == 0) {
                main.drawImage(main.getHeadImage(), segment.x * Main.CELL_SIZE, segment.y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
            } else {
                main.drawImage(main.getBodyImage(), segment.x * Main.CELL_SIZE, segment.y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
            }
        }

        // Draw info
        main.changeColor(Color.BLACK);
        main.drawBoldText(10, Main.GRID_SIZE * Main.CELL_SIZE + 20,
                "Score: " + world.getScore1() + " | Lives: " + world.getLives1() + " | High: " + world.getHighScore(), "Arial", 16);
        main.drawText(10, Main.GRID_SIZE * Main.CELL_SIZE + 45,
                "Controls: Arrow Keys | P: Pause | R: Restart", "Arial", 14);
    }

    private void renderTwoPlayer(GameWorld world) {
        // Draw apples
        Point apple1 = world.getFoodManager().getApple1();
        Point apple2 = world.getFoodManager().getApple2();
        if (apple1 != null) {
            main.drawImage(main.getAppleImage2(), apple1.x * Main.CELL_SIZE, apple1.y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
        }
        if (apple2 != null) {
            main.drawImage(main.getAppleImage(), apple2.x * Main.CELL_SIZE, apple2.y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
        }

        // Draw poisons
        for (Point poison : world.getFoodManager().getPoisons()) {
            if (main.getPoisonImage() != null) {
                main.drawImage(main.getPoisonImage(), poison.x * Main.CELL_SIZE, poison.y * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
            }
        }

        // Draw snake 1 (blue circles)
        Snake snake1 = world.getPlayer1().getSnake();
        main.changeColor(main.blue);
        renderSnakeAsCircles(snake1, true);

        // Draw snake 2 (green circles)
        Snake snake2 = world.getPlayer2().getSnake();
        main.changeColor(main.green);
        renderSnakeAsCircles(snake2, false);

        // Draw info
        main.changeColor(Color.BLACK);
        main.drawBoldText(10, Main.GRID_SIZE * Main.CELL_SIZE + 20,
                "P1 Score: " + world.getScore1() + " Lives: " + world.getLives1() +
                        " | P2 Score: " + world.getScore2() + " Lives: " + world.getLives2(), "Arial", 14);
        main.drawText(10, Main.GRID_SIZE * Main.CELL_SIZE + 45,
                "P1: Arrow Keys | P2: WASD | P: Pause", "Arial", 14);
    }

    private void renderSnakeAsCircles(Snake snake, boolean isHeadLarge) {
        List<Point> body = snake.getBody();
        for (int i = 0; i < body.size(); i++) {
            Point segment = body.get(i);
            int radius;
            if (i == 0 && isHeadLarge) {
                radius = Main.CELL_SIZE / 2 - 1;
            } else {
                radius = Main.CELL_SIZE / 2 - 2;
            }
            main.drawSolidCircle(segment.x * Main.CELL_SIZE + Main.CELL_SIZE / 2,
                    segment.y * Main.CELL_SIZE + Main.CELL_SIZE / 2, radius);
        }
    }
}