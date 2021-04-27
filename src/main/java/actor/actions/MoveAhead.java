package actor.actions;

import actor.Action;
import actor.ActionPriority;
import actor.Actor;
import actor.WalkingActor;
import state.Direction;
import state.Point2D;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Optional;

public class MoveAhead implements Action<ReadableBoard, WritableBoard> {
    private final WalkingActor walker;

    public MoveAhead(WalkingActor walker) {
        this.walker = walker;
    }

    @Override
    public Reward updateState(WritableBoard currentGameState) {

        Direction toMove = walker.getDirection();
        Optional<Point2D> currentLocation = currentGameState.find(walker);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("Walker was not found on the board, did this actor get removed?");
        }

        Point2D newLocation = currentLocation.get().transform(toMove);
        currentGameState.move(walker, newLocation);

        double score;
        if (walker.hasVisited(newLocation)) {
            score = -10; // penalty for going the same places, encourage the robot to spin around?
        } else {
            walker.markAsVisited(newLocation);
            score = 1; // slight boost for seeing an unvisited location
        }

        return Reward.forActor(getActor(), score);
    }

    @Override
    public ActionPriority priority() {
        return ActionPriority.MEDIUM;
    }

    @Override
    public Actor<ReadableBoard, WritableBoard> getActor() {
        return walker;
    }
}
