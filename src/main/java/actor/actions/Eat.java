package actor.actions;

import actor.Action;
import state.Point2D;
import state.board.*;

public class Eat implements Action<ReadableBoard, WritableBoard> {
    private final BoardObject eater;

    public Eat(BoardWalker eater) {
        this.eater = eater;
    }

    @Override
    public void updateState(WritableBoard currentGameState) {
        Point2D location = currentGameState.find(eater)
                .orElseThrow(() -> new IllegalStateException("Actor did not exist"));

        Sugar food = currentGameState.members(location).stream()
                .filter(member -> member instanceof Sugar)
                .map(member -> (Sugar) member)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Tried to eat but no food found on tile"));

        // Eat / remove the sugar
        currentGameState.remove(food);
    }

    @Override
    public double getImmediateReward() {
        return 100; // assumes you will succeed
    }
}
