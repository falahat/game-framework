package actor;

import state.Board2D;
import state.Board2DView;
import state.GameStateView;

import java.util.Collection;


public class Ant implements Board2DActor {
    @Override
    public Action<Board2D> decide(GameStateView<Board2D> currentState, Collection<Action<Board2D>> allowedActions) {
        return null;
    }

    @Override
    public Action<Board2D> decide(GameStateView<Board2D> currentState) {
        return null;
    }

    @Override
    public Collection<Action<Board2D>> getAllowedActions(Board2D currentState) {
        return null;
    }

    @Override
    public Board2DView getRequestedView(Board2D currentState) {
        return null;
    }

    @Override
    public void learn(Action<Board2D> decided, Board2D firstState, Board2DView firstView, Board2D nextState, Board2DView nextView) {

    }
}
