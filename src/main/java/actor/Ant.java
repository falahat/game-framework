package actor;

import state.Board;
import state.BoardView;
import state.GameStateView;

import java.util.Collection;


public class Ant implements Board2DActor {
    @Override
    public Action<Board> decide(GameStateView<Board> currentState, Collection<Action<Board>> allowedActions) {
        return null;
    }

    @Override
    public Action<Board> decide(GameStateView<Board> currentState) {
        return null;
    }

    @Override
    public Collection<Action<Board>> getAllowedActions(Board currentState) {
        return null;
    }

    @Override
    public BoardView getRequestedView(Board currentState) {
        return null;
    }

    @Override
    public void learn(Action<Board> decided, Board firstState, BoardView firstView, Board nextState, BoardView nextView) {

    }
}
