package actor.actions;

import actor.Action;
import state.Direction;
import state.Point2D;
import state.board.BoardWalker;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Optional;

public class TurnRight implements Action<ReadableBoard, WritableBoard> {
    private final BoardWalker walker;

    public TurnRight(BoardWalker walker) {
        this.walker = walker;
    }

    @Override
    public double updateState(WritableBoard currentGameState) {
        walker.setDirection(walker.getDirection().clockwise());
        return 0;
    }
}
