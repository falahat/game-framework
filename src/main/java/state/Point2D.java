package state;

public class Point2D {
    public final int x, y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2D transform(int xDiff, int yDiff) {
        return new Point2D(x + xDiff, y + yDiff);
    }

    public Point2D left() {
        return transform(-1, 0);
    }

    public Point2D right() {
        return transform(1, 0);
    }

    public Point2D up() {
        return transform(0, 1);
    }

    public Point2D down() {
        return transform(0, -1);
    }

    public Point2D minus(Point2D other) {
        return this.transform(-other.x, -other.y);
    }


    public Direction directionTo(Point2D other) {
        Point2D diff = this.minus(other);
        if (diff.x < 0) {
            return Direction.EAST; // Add to x
        } else if (diff.x > 0) {
            return Direction.WEST;
        } else if (diff.y < 0) {
            return Direction.NORTH;
        } else if (diff.y > 0) {
            return Direction.SOUTH;
        } else {
            throw new IllegalArgumentException("Other point had no difference with this point");
        }
    }

}
