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

        Bread food = currentGameState.members(location).stream()
                .filter(member -> member instanceof Bread)
                .map(member -> (Bread) member)
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
