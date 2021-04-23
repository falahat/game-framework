package actor;

import actor.algorithms.QALearner;
import state.Direction;
import state.GameStateView;
import state.RelativePositionedView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.*;

public class SmartPerson extends Person {


    private final QALearner brain;

    public SmartPerson(Direction direction) {
        super(direction);
        this.brain = new QALearner();
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

    public double getEstimatedScore(RelativePositionedView state, Action chosen) {
        return brain.getEstimatedScore(state, chosen);
    }

    public double getMaximumEstimateScore(RelativePositionedView state) {
        return brain.getMaximumEstimateScore(state);
    }
}
