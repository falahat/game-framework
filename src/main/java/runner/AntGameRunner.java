package runner;

import actor.Action;
import actor.Actor;
import actor.Board2DActor;
import state.board.Board;
import state.board.ReadableBoard;

import java.util.ArrayList;
import java.util.List;

public class AntGameRunner implements GameRunner<ReadableBoard> {
    private Board board;
    private List<Board2DActor> actors;

    public AntGameRunner(Board board, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
    }

    @Override
    public List<Actor<ReadableBoard, ?>> getActorsForCurrentTurn() {
        List<Actor<ReadableBoard, ?>> result = new ArrayList<>();
        actors.forEach(result::add);
        return result;
    }

    @Override
    public void draw() {
        // TODO: log or something
    }

    @Override
    public Board getCurrentState() {
        return board;
    }

    @Override
    public ReadableBoard calculateNextState(ReadableBoard previousState, List<Action<ReadableBoard>> chosen) {
        return null;
    }
}
