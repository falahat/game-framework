package runner;

import actor.Action;
import actor.Actor;
import actor.Board2DActor;
import actor.Person;
import state.board.ReadableBoard;
import state.board.GameBoard;
import state.board.WritableBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AntGameRunner implements GameRunner<ReadableBoard, WritableBoard> {
    private WritableBoard board;
    private final List<Board2DActor> actors;
    private final Person player; // this is one of the actors, but will be tracked more closely

    public AntGameRunner(GameBoard board, Person player, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
        this.player = player;
    }

    @Override
    public void render(Graphics g) {
        board.locations().forEach(point -> {
            int rx = GameCanvas.TILE_SIZE * point.x;
            int ry = GameCanvas.TILE_SIZE * point.y;

            boolean isVisited = player.hasVisited(point);
            
            g.setColor(isVisited ? Color.green : Color.gray);
            g.fillOval(rx, ry, GameCanvas.TILE_SIZE, GameCanvas.TILE_SIZE);

            g.setColor(Color.green);
            board.members(point).forEach(boardObj -> boardObj.render(g, rx, ry));
        });
    }

    @Override
    public List<Actor<ReadableBoard, WritableBoard>> getActorsForCurrentTurn() {
        return new ArrayList<>(actors);
    }

    @Override
    public WritableBoard getCurrentState() {
        return board;
    }

    @Override
    public WritableBoard calculateNextState(ReadableBoard previousState, List<Action<ReadableBoard, WritableBoard>> chosen) {
        // later, we can make changes which don't depend on an action, such as increasing hunger by 1 each round.
        WritableBoard nextState = previousState.mutableCopy();
        chosen.forEach(action -> action.updateState(nextState));
        return nextState;
    }

    @Override
    public void setCurrentState(WritableBoard newState) {
        this.board = newState;
    }
}
