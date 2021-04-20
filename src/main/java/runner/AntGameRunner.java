package runner;

import actor.Action;
import actor.Actor;
import actor.Board2DActor;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.util.ArrayList;
import java.util.List;

public class AntGameRunner implements GameRunner<ReadableBoard, WritableBoard> {
    private WritableBoard board;
    private final List<Board2DActor> actors;

    public AntGameRunner(WritableBoard board, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
    }

    @Override
    public List<Actor<ReadableBoard, WritableBoard>> getActorsForCurrentTurn() {
        List<Actor<ReadableBoard, WritableBoard>> result = new ArrayList<>();
        actors.forEach(a -> result.add(a));
        return result;
    }

    @Override
    public WritableBoard getCurrentState() {
        return board;
    }

    @Override
    public WritableBoard calculateNextState(ReadableBoard previousState, List<Action<ReadableBoard, WritableBoard>> chosen) {
        return null;
    }

    @Override
    public void setCurrentState(WritableBoard newState) {
        this.board = newState;
    }
}
