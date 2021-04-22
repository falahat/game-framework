package actor;

import state.Direction;
import state.PersonView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SmartPerson extends Person {
    private final Map<PersonView, Map<String, Double>> valueOfActionPerState;

    public SmartPerson(Direction direction) {
        super(direction);
        this.valueOfActionPerState = new HashMap<>();
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(ReadableBoard currentState, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        return super.decide(currentState, allowedActions);
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, ReadableBoard firstState, ReadableBoard nextState) {
        super.learn(decided, firstState, nextState);
    }
}
