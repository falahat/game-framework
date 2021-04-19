package actor;

import state.board.Board;
import state.board.BoardView;
import state.GameStateView;
import state.board.ReadableBoard;

import java.util.Collection;


public class Ant implements Board2DActor {

    @Override
    public Action<ReadableBoard> decide(GameStateView<ReadableBoard> currentView, Collection<Action<ReadableBoard>> allowedActions) {
        return null;
    }

    @Override
    public Action<ReadableBoard> decide(GameStateView<ReadableBoard> currentView) {
        return null;
    }

    @Override
    public Collection<Action<ReadableBoard>> getAllowedActions(ReadableBoard currentView) {
        return null;
    }

    @Override
    public BoardView getRequestedView(ReadableBoard currentState) {
        return null;
    }

    @Override
    public void learn(Action<ReadableBoard> decided, ReadableBoard firstState, BoardView firstView, ReadableBoard nextState, BoardView nextView) {

    }
}
