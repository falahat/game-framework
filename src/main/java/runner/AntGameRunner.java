package runner;

import actor.Action;
import actor.Board2DActor;
import state.Board2D;
import state.Board2DView;

import java.util.List;

public class AntGameRunner implements GameRunner<Board2D> {
    private Board2D board;
    private List<Board2DActor> actors;

    public AntGameRunner(Board2D board, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
    }

    @Override
    public void turn() {
        Board2D currentState = board.copy();

        for (Board2DActor actor : actors) {
            Board2DView currentView = actor.getRequestedView(currentState);

            Action<Board2D> decided = actor.decide(currentView);

            Board2D nextState = currentState.copy();
            decided.updateState(nextState);

            Board2DView nextView = actor.getRequestedView(nextState);

            actor.learn(decided, currentState, currentView, nextState, nextView);

            currentState = nextState;
        }
    }

    @Override
    public void draw() {
        // TODO: log or something
    }

    @Override
    public Board2D getCurrentState() {
        return board;
    }
}
