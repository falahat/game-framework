package actor.actions;

import actor.Action;
import state.board.BoardWalker;
import state.board.ReadableBoard;
import state.board.WritableBoard;

public class TurnLeft implements Action<ReadableBoard, WritableBoard> {
    private final BoardWalker walker;

    public TurnLeft(BoardWalker walker) {
        this.walker = walker;
    }

    @Override
    public void updateState(WritableBoard currentGameState) {
        walker.setDirection(walker.getDirection().counterClockwise());
    }

    @Override
    public double getImmediateReward() {
        return 0;
    }
}
