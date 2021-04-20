package state;

import actor.Ant;
import state.board.BoardView;
import state.board.ReadableBoard;

public class AntView implements BoardView {
    private final Ant actor;
    private final ReadableBoard rawBoard;

    public AntView(ReadableBoard board, Ant actor) {
        this.rawBoard = board;
        this.actor = actor;
    }

    public boolean isBlockedAhead() {
        return false; // TODO: implement
    }

    public boolean isFoodAhead() {
        return false; // TODO: implement
    }
}
