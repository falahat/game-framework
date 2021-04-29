package actor;

import actor.algorithms.MarkovDecisionProcess;
import state.PositionView;
import state.Direction;
import state.GameStateView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Collection;

public class SmartPerson extends Person {
    private final MarkovDecisionProcess brain;

    public SmartPerson(Direction direction) {
        super(direction);
        this.brain = new MarkovDecisionProcess();
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentState, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        return brain.decide(currentState, allowedActions);
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided,
                      GameStateView firstState,
                      GameStateView nextState, double immediateReward) {
        brain.learn(decided, firstState, nextState, immediateReward);
    }

    public double getMaximumEstimateScore(PositionView state) {
        return brain.getMaximumEstimateScore(state);
    }
}
