package runner;

import actor.Action;
import actor.Board2DActor;
import state.Board;
import state.BoardView;

import java.util.List;

public class AntGameRunner implements GameRunner<Board> {
    private Board board;
    private List<Board2DActor> actors;

    public AntGameRunner(Board board, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
    }

    @Override
    public void turn() {
        Board currentState = board.copy();

        for (Board2DActor actor : actors) {
            BoardView currentView = actor.getRequestedView(currentState);

            Action<Board> decided = actor.decide(currentView);

            Board nextState = currentState.copy();
            decided.updateState(nextState);

            BoardView nextView = actor.getRequestedView(nextState);

            actor.learn(decided, currentState, currentView, nextState, nextView);

            currentState = nextState;
        }
    }

    @Override
    public void draw() {
        // TODO: log or something
    }

    @Override
    public Board getCurrentState() {
        return board;
    }
}
