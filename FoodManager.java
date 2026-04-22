import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class FoodManager {
    private Main main;
    private Point apple;
    private Point apple1;
    private Point apple2;
    private Point poison;
    private List<Point> poisons;

    public FoodManager(Main main) {
        this.main = main;
        poisons = new ArrayList<>();
    }

    // Single player apple generation
    public void generateApple(Snake snake) {
        boolean validPosition = false;
        while (!validPosition) {
            int x = main.rand(Main.GRID_SIZE);
            int y = main.rand(Main.GRID_SIZE);
            boolean occupied = false;
            for (Point segment : snake.getBody()) {
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

    // Single player poison generation
    public void generatePoison(Snake snake) {
        boolean validPosition = false;
        while (!validPosition) {
            int x = main.rand(Main.GRID_SIZE);
            int y = main.rand(Main.GRID_SIZE);
            boolean occupied = false;
            for (Point segment : snake.getBody()) {
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

    // Two player apple generation
    public void generateAppleForTwoPlayer(int playerNum, Snake snake1, Snake snake2, Point otherApple) {
        boolean validPosition = false;
        while (!validPosition) {
            int x = main.rand(Main.GRID_SIZE);
            int y = main.rand(Main.GRID_SIZE);
            boolean occupied = false;

            for (Point segment : snake1.getBody()) {
                if (segment.x == x && segment.y == y) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) {
                for (Point segment : snake2.getBody()) {
                    if (segment.x == x && segment.y == y) {
                        occupied = true;
                        break;
                    }
                }
            }
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

    // Two player poison generation
    public void generatePoisonForTwoPlayer(Snake snake1, Snake snake2) {
        boolean validPosition = false;
        while (!validPosition) {
            int x = main.rand(Main.GRID_SIZE);
            int y = main.rand(Main.GRID_SIZE);
            boolean occupied = false;

            for (Point segment : snake1.getBody()) {
                if (segment.x == x && segment.y == y) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) {
                for (Point segment : snake2.getBody()) {
                    if (segment.x == x && segment.y == y) {
                        occupied = true;
                        break;
                    }
                }
            }
            if (!occupied && apple1 != null && apple1.x == x && apple1.y == y) occupied = true;
            if (!occupied && apple2 != null && apple2.x == x && apple2.y == y) occupied = true;
            if (!occupied) {
                for (Point p : poisons) {
                    if (p.x == x && p.y == y) {
                        occupied = true;
                        break;
                    }
                }
            }
            if (!occupied) {
                poisons.add(new Point(x, y));
                validPosition = true;
            }
        }
    }

    // Getters and setters
    public Point getApple() { return apple; }
    public Point getApple1() { return apple1; }
    public Point getApple2() { return apple2; }
    public Point getPoison() { return poison; }
    public List<Point> getPoisons() { return poisons; }
    public void setApple(Point apple) { this.apple = apple; }
    public void setApple1(Point apple1) { this.apple1 = apple1; }
    public void setApple2(Point apple2) { this.apple2 = apple2; }
    public void setPoison(Point poison) { this.poison = poison; }
    public void clearPoisons() { poisons.clear(); }
}