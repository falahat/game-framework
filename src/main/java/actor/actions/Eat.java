package actor.actions;

import actor.Action;
import actor.ActionPriority;
import actor.Actor;
import actor.WalkingActor;
import state.Point2D;
import state.board.Bread;
import state.board.ReadableBoard;
import state.board.WritableBoard;

public class Eat implements Action<ReadableBoard, WritableBoard> {
    private final WalkingActor eater;

    public Eat(WalkingActor eater) {
        this.eater = eater;
    }

    @Override
    public Reward updateState(WritableBoard currentGameState) {
        Point2D location = currentGameState.find(eater)
                .orElseThrow(() -> new IllegalStateException("Actor did not exist"));

        Bread food = currentGameState.members(location).stream()
                .filter(member -> member instanceof Bread)
                .map(member -> (Bread) member)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Tried to eat but no food found on tile"));

        // Eat / remove the sugar
        currentGameState.remove(food);

        return Reward.forActor(getActor(), 300);
    }

    @Override
    public ActionPriority priority() {
        return ActionPriority.MEDIUM;
    }

    @Override
    public Actor<ReadableBoard, WritableBoard> getActor() {
        return eater;
    }
}
