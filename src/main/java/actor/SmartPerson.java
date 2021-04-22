package actor;

import state.Direction;
import state.GameStateView;
import state.RelativePositionedView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.*;
import java.util.stream.Collectors;

public class SmartPerson extends Person {
    public static final double DEFAULT_SCORE = 50;
    public static final double LEARNING_RATE = 0.12;
    public static final double DISCOUNT_RATE = 0.5;
    public static final boolean ALWAYS_PICK_BEST_ACTION = false;

    private final Map<RelativePositionedView, Map<String, Double>> valueOfActionPerState;

    public SmartPerson(Direction direction) {
        super(direction);
        this.valueOfActionPerState = new HashMap<>();
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentState, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        Random random = new Random();

        List<Action<ReadableBoard, WritableBoard>> sorted = allowedActions.stream()
                .sorted(Comparator.comparing(action -> random.nextDouble()-this.getEstimatedScore((RelativePositionedView) currentState, action)))
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
        super.learn(decided, firstState, nextState, immediateReward);

        // we were at "firstState" and chose "decided".
        double originalEstimate = getEstimatedScore((RelativePositionedView) firstState, decided);

        double maxValueFromNewState = getMaximumEstimateScore((RelativePositionedView) nextState);

        double estimateDiff = (immediateReward + DISCOUNT_RATE * maxValueFromNewState) - originalEstimate;
        double newEstimate = originalEstimate + LEARNING_RATE * estimateDiff;

        this.valueOfActionPerState.putIfAbsent((RelativePositionedView) firstState, new HashMap<>());
        this.valueOfActionPerState.get(firstState).put(decided.uniqueActionType(), newEstimate);
    }

    public double getEstimatedScore(RelativePositionedView state, Action chosen) {
        String actionKey = chosen.uniqueActionType();
        return valueOfActionPerState
                .getOrDefault(state, Collections.emptyMap())
                .getOrDefault(actionKey, DEFAULT_SCORE);
    }

    public double getMaximumEstimateScore(RelativePositionedView state) {
        return valueOfActionPerState
                .getOrDefault(state, Collections.emptyMap()).values().stream().mapToDouble(a -> a)
                .max()
                .orElse(DEFAULT_SCORE);
    }
}
