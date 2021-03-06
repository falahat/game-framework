package actor.algorithms;

import actor.Action;
import actor.Trainable;
import state.PositionView;
import state.GameStateView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a model-free aproach to estimating state values and which actions are best for a given state.
 * It approximates a value for each state/action pair based on the immediate reward of choosing
 * that action and the value of the state that we end up in.
 *
 * {@link https://en.wikipedia.org/wiki/Q-learning}
 */
public class QALearner implements Trainable<ReadableBoard, WritableBoard> {
    public static final double DEFAULT_SCORE = 50;
    public static final double LEARNING_RATE = 0.12;
    public static final double DISCOUNT_RATE = 0.95;
    public static final boolean ALWAYS_PICK_BEST_ACTION = false;

    private final Map<PositionView, Map<String, Double>> valueOfActionPerState;

    public QALearner() {
        this.valueOfActionPerState = new HashMap<>();
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentState, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        Random random = new Random();

        List<Action<ReadableBoard, WritableBoard>> sorted = allowedActions.stream()
                .sorted(Comparator.comparing(action -> random.nextDouble()-this.getEstimatedActionScore((PositionView) currentState, action)))
                .collect(Collectors.toList());

        for (int i = 0; i < sorted.size()-1; i++) {
            if (ALWAYS_PICK_BEST_ACTION || random.nextBoolean() || random.nextBoolean() || random.nextBoolean()) {
                return sorted.get(i);
            }
        }

        return sorted.get(sorted.size()-1);
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided,
                      GameStateView firstState,
                      GameStateView nextState, double immediateReward) {
        // we were at "firstState" and chose "decided".
        double originalEstimate = getEstimatedActionScore((PositionView) firstState, decided);

        double maxValueFromNewState = getEstimatedStateValue((PositionView) nextState);

        double estimateDiff = (immediateReward + DISCOUNT_RATE * maxValueFromNewState) - originalEstimate;
        double newEstimate = originalEstimate + LEARNING_RATE * estimateDiff;

        this.valueOfActionPerState.putIfAbsent((PositionView) firstState, new HashMap<>());
        this.valueOfActionPerState.get(firstState).put(decided.uniqueActionType(), newEstimate);
    }

    public double getEstimatedActionScore(PositionView state, Action chosen) {
        String actionKey = chosen.uniqueActionType();
        return valueOfActionPerState
                .getOrDefault(state, Collections.emptyMap())
                .getOrDefault(actionKey, DEFAULT_SCORE);
    }

    public double getEstimatedStateValue(PositionView state) {
        return valueOfActionPerState
                .getOrDefault(state, Collections.emptyMap()).values().stream().mapToDouble(a -> a)
                .max()
                .orElse(DEFAULT_SCORE);
    }
}
