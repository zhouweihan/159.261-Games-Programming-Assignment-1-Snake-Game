import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private int size;
    private int directionX;
    private int directionY;
    private int nextDirectionX;
    private int nextDirectionY;
    private int startX;
    private int startY;
    private int startDirX;
    private int startDirY;

    public Snake(int startX, int startY, int startDirX, int startDirY) {
        this.startX = startX;
        this.startY = startY;
        this.startDirX = startDirX;
        this.startDirY = startDirY;
        reset();
    }

    public void reset() {
        body = new ArrayList<>();
        body.add(new Point(startX, startY));
        body.add(new Point(startX - startDirX, startY - startDirY));
        body.add(new Point(startX - 2 * startDirX, startY - 2 * startDirY));
        size = 3;
        directionX = startDirX;
        directionY = startDirY;
        nextDirectionX = startDirX;
        nextDirectionY = startDirY;
    }

    public Point getHead() {
        return body.get(0);
    }

    public List<Point> getBody() {
        return body;
    }

    public int getSize() {
        return size;
    }

    public int getDirectionX() { return directionX; }
    public int getDirectionY() { return directionY; }
    public int getNextDirectionX() { return nextDirectionX; }
    public int getNextDirectionY() { return nextDirectionY; }

    public void setDirection(int dx, int dy) {
        this.nextDirectionX = dx;
        this.nextDirectionY = dy;
    }

    public void applyDirection() {
        directionX = nextDirectionX;
        directionY = nextDirectionY;
    }

    public void shiftPositions() {
        for (int i = body.size() - 1; i > 0; i--) {
            Point previous = body.get(i - 1);
            body.get(i).x = previous.x;
            body.get(i).y = previous.y;
        }
    }

    public void setHeadPosition(int x, int y) {
        body.get(0).x = x;
        body.get(0).y = y;
    }

    public void grow() {
        Point tail = body.get(body.size() - 1);
        body.add(new Point(tail.x, tail.y));
        size++;
    }

    public boolean canGrow(int maxSize) {
        return size < maxSize;
    }

    public boolean checkSelfCollision(int newX, int newY) {
        for (Point segment : body) {
            if (segment.x == newX && segment.y == newY) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisionWithSnake(Snake other, int newX, int newY) {
        for (Point segment : other.getBody()) {
            if (segment.x == newX && segment.y == newY) {
                return true;
            }
        }
        return false;
    }
}