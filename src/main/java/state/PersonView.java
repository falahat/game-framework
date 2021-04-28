package state;

import actor.Person;
import state.board.BoardObject;
import state.board.BoardView;
import state.board.Bread;
import state.board.ReadableBoard;

import java.util.Objects;
import java.util.Optional;

public class PersonView implements BoardView {

    private final Sensed front;
    private final Sensed current;
    private final Sensed left;
    private final Sensed right;

    private PersonView(Sensed front, Sensed current, Sensed left, Sensed right) {
        this.front = front;
        this.current = current;
        this.left = left;
        this.right = right;
    }

    public boolean isBlockedAhead() {
        return front == Sensed.BLOCKED;
    }

    public boolean isAboveFood() {
        return current == Sensed.FOOD;
    }

    public static PersonView from(Person actor, ReadableBoard board) {
        Optional<Point2D> currentLocation = board.find(actor);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("This actor does not exist in the board");
        }

        return from(actor, board, currentLocation.get());
    }

    public static PersonView from(Person actor, ReadableBoard board, Point2D currentLocation) {
        Point2D ahead = currentLocation.transform(actor.getDirection());
        Point2D left = currentLocation.transform(actor.getDirection().counterClockwise());
        Point2D right = currentLocation.transform(actor.getDirection().clockwise());

        return new PersonView(sense(board, ahead),
                sense(board, currentLocation),
                sense(board, left),
                sense(board, right));
    }

    public static Sensed sense(ReadableBoard board, Point2D location) {
        if (!board.locations().contains(location) || board.members(location).stream().anyMatch(BoardObject::isBlocking)) {
            return Sensed.BLOCKED;
        } else if (board.members(location).stream().anyMatch(obj -> obj instanceof Bread)) {
            return Sensed.FOOD;
        } else if (board.members(location).stream().anyMatch(obj -> obj instanceof Person)) {
            return Sensed.PERSON;
        } else {
            return Sensed.NONE;
        }
    }

    @Override
    public int hashCode() {
        if (this.current == Sensed.FOOD) {
            return Objects.hashCode(current);
        }

        return Objects.hash(this.front,
                this.current,
//                this.left,
                this.right
        ); // easy boolean hash
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PersonView))
            return false;

        PersonView other = (PersonView) obj;

        if (this.current == Sensed.FOOD && other.current == Sensed.FOOD) {
            return true;
        }

        return Objects.equals(this.current, other.current)
            && Objects.equals(this.front, other.front)
//                && Objects.equals(this.left, other.left);
            && Objects.equals(this.right, other.right);
    }

    @Override
    public String toString() {
        return String.format("(Current=%s Left=%s Front=%s Right=%s)", this.current, this.front, this.left, this.right);
    }

    public enum Sensed {
        NONE,
        BLOCKED,
        FOOD,
        PERSON
    }
}
