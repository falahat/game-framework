package actor.actions;

import actor.Action;
import actor.ActionPriority;
import actor.Actor;
import actor.Person;
import state.board.ReadableBoard;
import state.board.WritableBoard;

public class Jump implements Action<ReadableBoard, WritableBoard> {

    private final Person person;

    public Jump(Person person) {
        this.person = person;
    }

    @Override
    public Reward updateState(WritableBoard currentGameState) {
        this.person.setJumping(true);
        return Reward.forActor(getActor(), -10);
    }

    @Override
    public ActionPriority priority() {
        return ActionPriority.HIGH;
    }

    @Override
    public Actor<ReadableBoard, WritableBoard> getActor() {
        return person;
    }
}
