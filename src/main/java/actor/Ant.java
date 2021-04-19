package actor;

import state.AntView;
import state.board.BoardObject;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.Collection;


public class Ant implements Board2DActor, BoardObject {

    @Override
    public Action<ReadableBoard, WritableBoard> decide(ReadableBoard currentState,
                                                       Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        // Ant can only see the block in front of it
        // Ant will eat if there is food directly in front of it

        // Cast the "currentState" to be a localized state, specific to this actor's perspective
        return null;
    }

    @Override
    public Collection<Action<ReadableBoard, WritableBoard>> getAllowedActions(ReadableBoard currentView) {
        // Can always turn left/right
        // Can attempt to go forward, but might be blocked
        // If we are 100% sure the tile in front is blocked, we will not go forward
        return null;
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, ReadableBoard firstState, ReadableBoard nextState) {

    }

    private AntView getVisibleState(ReadableBoard fullState) {
        // TODO: implement
        return null;
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
