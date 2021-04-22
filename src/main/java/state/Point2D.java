package state;

import java.util.Objects;

/**
 * Representation of an (X, Y) Cartesian coordinate. Each value is an integer.
 */
public class Point2D {
    public final int x, y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point2D
                && ((Point2D) obj).x == this.x
                && ((Point2D) obj).y == this.y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    public Point2D transform(int xDiff, int yDiff) {
        return new Point2D(x + xDiff, y + yDiff);
    }

    public Point2D transform(Direction direction) {
        switch (direction) {
            case NORTH:
                return transform(0, -1);
            case SOUTH:
                return transform(0, 1);
            case WEST:
                return transform(-1, 0);
            case EAST:
                return transform(1, 0);
            default:
                throw new IllegalArgumentException(String.format("Unhandled direction: %s", direction));
        }
    }

    public Point2D left() {
        return transform(Direction.WEST);
    }

    public Point2D right() {
        return transform(Direction.EAST);
    }

    public Point2D up() {
        return transform(Direction.NORTH);
    }

    public Point2D down() {
        return transform(Direction.SOUTH);
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
