package state;

import actor.Person;
import state.board.BoardObject;
import state.board.BoardView;
import state.board.Bread;
import state.board.ReadableBoard;

import java.util.Optional;

public class PersonView implements BoardView {
    private final boolean isBlockedAhead;
    private final boolean isAboveFood;

    public PersonView(boolean isBlockedAhead, boolean isAboveFood) {
        this.isBlockedAhead = isBlockedAhead;
        this.isAboveFood = isAboveFood;
    }

    public boolean isBlockedAhead() {
        return isBlockedAhead;
    }

    public boolean isAboveFood() {
        return isAboveFood;
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

        boolean isAboveFood = board.members(currentLocation).stream().anyMatch(obj -> obj instanceof Bread);
        boolean isBlocked = !board.locations().contains(ahead) || board.members(ahead).stream().anyMatch(BoardObject::isBlocking);

        return new PersonView(isBlocked, isAboveFood);
    }
}
