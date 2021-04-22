package state;

import actor.Ant;
import state.board.BoardView;
import state.board.ReadableBoard;

import java.util.Optional;

public class AntView implements BoardView {
    private final Ant actor;
    private final ReadableBoard rawBoard;

    public AntView(ReadableBoard board, Ant actor) {
        this.rawBoard = board;
        this.actor = actor;
    }

    public boolean isBlockedAhead() {
        Optional<Point2D> currentLocation = rawBoard.find(actor);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("This actor does not exist in the board");
        }

        return false; // TODO: implement
    }

    public boolean isAboveFood() {
        return false; // TODO: implement
    }
}
