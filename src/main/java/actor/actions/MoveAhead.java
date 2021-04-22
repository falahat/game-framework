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
    public double updateState(WritableBoard currentGameState) {
        Direction toMove = walker.getDirection();
        Optional<Point2D> currentLocation = currentGameState.find(walker);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("Walker was not found on the board, did this actor get removed?");
        }

        Point2D newLocation = currentLocation.get().transform(toMove);
        currentGameState.move(walker, newLocation);

        if (walker.hasVisited(newLocation)) {
            return 0;
        } else {
            walker.markAsVisited(newLocation);
            return 1; // slight boost for seeing an unvisited location
        }
    }
}
