package actor.actions;

import actor.Action;
import actor.ActionPriority;
import actor.Actor;
import actor.WalkingActor;
import state.board.ReadableBoard;
import state.board.WritableBoard;

public class TurnRight implements Action<ReadableBoard, WritableBoard> {
    private final WalkingActor walker;

    public TurnRight(WalkingActor walker) {
        this.walker = walker;
    }

    @Override
    public Reward updateState(WritableBoard currentGameState) {
        walker.setDirection(walker.getDirection().clockwise());
        return Reward.forActor(getActor(), -10);
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
