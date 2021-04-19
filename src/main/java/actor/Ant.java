package actor;

import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Collection;


public class Ant implements Board2DActor {

    @Override
    public Action<ReadableBoard, WritableBoard> decide(ReadableBoard currentState, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        return null;
    }

    @Override
    public Collection<Action<ReadableBoard, WritableBoard>> getAllowedActions(ReadableBoard currentView) {
        return null;
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, ReadableBoard firstState, ReadableBoard nextState) {

    }
}
