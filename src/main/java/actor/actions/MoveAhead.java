package actor.actions;

import actor.Action;
import state.Direction;
import state.Point2D;
import state.board.BoardWalker;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Optional;

public class MoveAhead implements Action<ReadableBoard, WritableBoard> {
    private final BoardWalker walker;

    public MoveAhead(BoardWalker walker) {
        this.walker = walker;
    }

    @Override
    public void updateState(WritableBoard currentGameState) {
        Direction toMove = walker.getDirection();
        Optional<Point2D> currentLocation = currentGameState.find(walker);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("Walker was not found on the board, did this actor get removed?");
        }

        Point2D newLocation = currentLocation.get().transform(toMove);
        currentGameState.move(walker, newLocation);
    }

    @Override
    public double getImmediateReward() {
        return 0;
    }
}
