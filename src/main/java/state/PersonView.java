package state;

import actor.Person;
import state.board.BoardObject;
import state.board.BoardView;
import state.board.Bread;
import state.board.ReadableBoard;

import java.util.Optional;

public class PersonView implements BoardView {
    private final Person actor;
    private final ReadableBoard rawBoard;

    public PersonView(ReadableBoard board, Person actor) {
        this.rawBoard = board;
        this.actor = actor;
    }

    public boolean isBlockedAhead() {
        Optional<Point2D> currentLocation = rawBoard.find(actor);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("This actor does not exist in the board");
        }

        Point2D ahead = currentLocation.get().transform(actor.getDirection());

        if (!rawBoard.locations().contains(ahead)) {
            return true;
        }

        return rawBoard.members(ahead).stream().anyMatch(BoardObject::isBlocking);
    }

    public boolean isAboveFood() {
        Optional<Point2D> currentLocation = rawBoard.find(actor);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("This actor does not exist in the board");
        }

        return rawBoard.members(currentLocation.get()).stream().anyMatch(obj -> obj instanceof Bread);
    }
}
