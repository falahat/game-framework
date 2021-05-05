package actor.actions;

import actor.*;
import state.Point2D;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Random;

public class Lunge implements Action<ReadableBoard, WritableBoard> {
    public static final double PROBABILITY_DEATH_IF_JUMPING = 0.50;

    private final Skeleton predator;
    private final Person prey;

    public Lunge(Skeleton predator, Person prey) {
        this.predator = predator;
        this.prey = prey;
    }

    @Override
    public Reward updateState(WritableBoard currentGameState) {
        Reward result = new Reward();

        // TODO: in the future, check if the prey is close enough
        boolean eaten = !this.prey.isJumping() // 100% chance of death if not jumping
                || new Random().nextDouble() < PROBABILITY_DEATH_IF_JUMPING;

        if (eaten) {
            result.addReward(predator, 1000);
            result.addReward(prey, -1000);
            // remove actors? For now, just punish them severely
        } else {
            result.addReward(predator, -50);
            result.addReward(prey, 0);
        }

        Point2D currentLoc = currentGameState.find(predator).orElseThrow();
        Point2D twoTilesAhead = currentLoc
                .transform(predator.getDirection())
                .transform(predator.getDirection());

        if (currentGameState.locations().contains(twoTilesAhead)) {
            currentGameState.move(predator, twoTilesAhead);
        } else {
            currentGameState.move(predator, currentLoc.transform(predator.getDirection()));
        }

        return result;
    }

    @Override
    public ActionPriority priority() {
        return ActionPriority.MEDIUM;
    }

    @Override
    public Actor<ReadableBoard, WritableBoard> getActor() {
        return null;
    }
}
