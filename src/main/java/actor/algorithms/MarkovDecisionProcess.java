package actor.algorithms;

import actor.Action;
import actor.Trainable;
import state.GameStateView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is an algorithm that approximates a probability of how a state could transform given a certain action, and the
 * expected reward of this transformation. In parallel, we also develop an optimal policy, which chooses the action
 * which maximizes the total expected reward.
 *
 * https://en.wikipedia.org/wiki/Markov_decision_process
 */
public class MarkovDecisionProcess implements Trainable<ReadableBoard, WritableBoard> {
    public static final double LEARNING_RATE = 0.20;
    public static final double DISCOUNT_RATE = 0.95;
    public static final boolean ALWAYS_PICK_BEST_ACTION = false;
    public static final int INITIAL_STATE_VALUE = 100;
    public static final int MINIMUM_DATAPOINTS = 1;

    private int counter = 0;

    // This is one of the ugliest things I've created in recent memory:
    // for each game state, it stores a map of possible actions.
    // for each of those actions, we track the "next state" that this action transformed the original gamestate to
    // we keep a count of the "next states", which can be used to approximate probability outcomes
    private final Map<GameStateView, Map<String, Map<GameStateView, Integer>>> outcomes;

    private final Map<GameStateView, Map<String, Map<GameStateView, Double>>> rewards;
    private final Map<GameStateView, Double> estimateStateValues; // stores the estimate best values
    private final Set<GameStateView> seenStates = new HashSet<>();

    public MarkovDecisionProcess() {
        this.outcomes = new HashMap<>();
        this.rewards = new HashMap<>();
        this.estimateStateValues = new HashMap<>();
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentView, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        Random random = new Random();

        List<Action<ReadableBoard, WritableBoard>> sorted = allowedActions.stream()
                .sorted(Comparator.comparing(action -> random.nextDouble()-this.calculateRewardForAction(currentView, action.uniqueActionType())))
                .collect(Collectors.toList());

        for (int i = 0; i < sorted.size()-1; i++) {
            if (ALWAYS_PICK_BEST_ACTION || random.nextBoolean() || random.nextBoolean()) {
                return sorted.get(i);
            }
        }

        return sorted.get(sorted.size()-1);
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided,
                      GameStateView firstView, GameStateView nextView, double immediateReward) {

        // // update historical data for the probability model
        outcomes.putIfAbsent(firstView, new HashMap<>());
        outcomes.get(firstView).putIfAbsent(decided.uniqueActionType(), new HashMap<>()); // outcomes[state][action] = a count of possible outcomes

        int currCount = outcomes.get(firstView).get(decided.uniqueActionType()).getOrDefault(nextView, 0);
        outcomes.get(firstView).get(decided.uniqueActionType()).put(nextView, currCount + 1);

        // Update the estimated rewards of each state
        rewards.putIfAbsent(firstView, new HashMap<>());
        rewards.get(firstView).putIfAbsent(decided.uniqueActionType(), new HashMap<>()); // outcomes[state][action] = a count of possible outcomes
        double currReward = rewards.get(firstView).get(decided.uniqueActionType()).getOrDefault(nextView, 0.0);
        if (currReward != immediateReward) {
            currReward = (1-LEARNING_RATE)*currReward + LEARNING_RATE*immediateReward;
        }
        rewards.get(firstView).get(decided.uniqueActionType()).put(nextView, currReward);

        seenStates.add(firstView);

        counter++;
        if ((counter % 100) == 0) {
            counter = 1;
            for (GameStateView view : seenStates) {
                this.estimateStateValues.put(view, calculateMaximumScore((view)));
            }
        }
    }

    private double probability(GameStateView start, GameStateView next, String actionKey) {
        if (!outcomes.containsKey(start) || !outcomes.get(start).containsKey(actionKey)) {
            return 0.0; // TODO: default probability high to make them explore?
        }

        int totalPoints = outcomes.get(start).get(actionKey).values().stream().mapToInt(i->i).sum();
        if (totalPoints < MINIMUM_DATAPOINTS) {
            return 0.0;
        }

        int numMatching = outcomes.get(start).get(actionKey).getOrDefault(next, 0);
        return (numMatching*1.0) / totalPoints;
    }

    public double getMaximumEstimateScore(GameStateView state) {
        return estimateStateValues.getOrDefault(state, 1.0 * INITIAL_STATE_VALUE);
    }

    public double calculateMaximumScore(GameStateView state) {
        if (!outcomes.containsKey(state)) {
            return INITIAL_STATE_VALUE; // TODO: default probability high to make them explore?
        }

        if (!rewards.containsKey(state)) {
            throw new IllegalStateException("Had outcomes but no rewards recorded");
        }

        double bestEstimate = Double.MIN_VALUE;

        for (String action : outcomes.get(state).keySet()) {
            double rewardForAction = calculateRewardForAction(state, action);

            if (rewardForAction > bestEstimate) {
                bestEstimate = rewardForAction;
            }
        }

        return bestEstimate;
    }

    public double calculateRewardForAction(GameStateView start, String actionKey) {
        double rewardForAction = 0;

        if (!rewards.containsKey(start) || !outcomes.containsKey(start) || !outcomes.get(start).containsKey(actionKey)) {
            return INITIAL_STATE_VALUE;
        }

        Map<String, Map<GameStateView, Double>> rewardsForState = rewards.get(start);

        Set<GameStateView> nextPossibleStates = outcomes.get(start).get(actionKey).keySet();
        Map<GameStateView, Double> rewardsForAction = rewardsForState.get(actionKey);
        if (nextPossibleStates.isEmpty()) {
            throw new IllegalStateException("We had a recorded Action in outcomes, but no states from that action");
        }

        double probability = 0;

        for (GameStateView nextState : nextPossibleStates) {
            double currentProbability = this.probability(start, nextState, actionKey);
            double currentReward = rewardsForAction.get(nextState);
            double nextValue = getMaximumEstimateScore(nextState);
            probability += currentProbability;
            rewardForAction += currentProbability * (currentReward + DISCOUNT_RATE * nextValue);
        }

        if (Math.abs(1-probability) > 0.001) {
            throw new IllegalStateException("Probability did not ad up to 100%");
        }

        return rewardForAction;
    }

}
