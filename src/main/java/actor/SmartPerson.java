package actor;

import state.Direction;
import state.GameStateView;
import state.PersonView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SmartPerson extends Person {
    public static final double DEFAULT_SCOPE = 1.5;
    private final Map<PersonView, Map<String, Double>> valueOfActionPerState;

    public SmartPerson(Direction direction) {
        super(direction);
        this.valueOfActionPerState = new HashMap<>();
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentState, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        return super.decide(currentState, allowedActions);
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided,
                      GameStateView firstState,
                      GameStateView nextState, double immediateReward) {
        super.learn(decided, firstState, nextState, immediateReward);

        // we were at "firstState" and chose "decided".
        double originalEstimate = getEstimatedScore((PersonView) firstState, decided);
        double LEARNING_RATE = 0.5;
        double DISCOUNT_RATE = 0.9;

        double maxValueFromNewState = getMaximumEstimateScore((PersonView) nextState);

        double estimateDiff = (immediateReward + DISCOUNT_RATE * maxValueFromNewState) - originalEstimate;
        double newEstimate = originalEstimate + LEARNING_RATE * estimateDiff;

        this.valueOfActionPerState.putIfAbsent((PersonView) firstState, new HashMap<>());
        this.valueOfActionPerState.get(firstState).put(decided.uniqueActionType(), newEstimate);
    }

    public double getEstimatedScore(PersonView state, Action chosen) {
        String actionKey = chosen.uniqueActionType();
        return valueOfActionPerState
                .getOrDefault(state, Collections.emptyMap())
                .getOrDefault(actionKey, DEFAULT_SCOPE);
    }

    public double getMaximumEstimateScore(PersonView state) {
        return valueOfActionPerState
                .getOrDefault(state, Collections.emptyMap()).values().stream().mapToDouble(a -> a)
                .max()
                .orElse(DEFAULT_SCOPE);
    }
}
