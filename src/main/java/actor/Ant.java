package actor;

import actor.actions.Eat;
import actor.actions.MoveAhead;
import actor.actions.TurnLeft;
import actor.actions.TurnRight;
import state.AntView;
import state.Direction;
import state.board.BoardWalker;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class Ant implements Board2DActor, BoardWalker {
    private Direction direction;

    public Ant(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(ReadableBoard currentState,
                                                       Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        // Ant can only see the block in front of it
        Action<ReadableBoard, WritableBoard> decided = null;

        // This assumes the moves are legal
        for (Action<ReadableBoard, WritableBoard> possible : allowedActions) {
            if ((possible instanceof Eat)) {
                // Ant will eat if there is food directly in front of it
                return possible;
            } else {
                decided = possible;
                if (new Random().nextBoolean()) {
                    return decided; // This is meant to randomize a little bit, TODO: improve
                }
            }
        }

        if (decided == null) {
            throw new IllegalArgumentException("Did not have any available actions");
        } else {
            return decided;
        }
    }

    @Override
    public Collection<Action<ReadableBoard, WritableBoard>> getAllowedActions(ReadableBoard currentView) {
        AntView visible = getVisibleState(currentView);

        List<Action<ReadableBoard, WritableBoard>> actions = new ArrayList<>();

        // Can always turn left/right
        actions.add(new TurnLeft(this));
        actions.add(new TurnRight(this));

        if (visible.isAboveFood()) {
            actions.add(new Eat(this));
        }

        if (!visible.isBlockedAhead()) {
            actions.add(new MoveAhead(this));
        }

        // Can attempt to go forward, but might be blocked
        // If we are 100% sure the tile in front is blocked, we will not go forward
        return actions;
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, ReadableBoard firstState, ReadableBoard nextState) {
        // TODO: implement
    }

    private AntView getVisibleState(ReadableBoard fullState) {
        return new AntView(fullState, this);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
