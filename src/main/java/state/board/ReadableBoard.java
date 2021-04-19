package state.board;

import state.GameState;
import state.Point2D;

import java.util.List;
import java.util.Optional;

public interface ReadableBoard extends GameState<ReadableBoard, WritableBoard> {

    List<Point2D> neighbors(Point2D center);

    List<BoardObject> members(Point2D location);

    Optional<Point2D> find(BoardObject toFind);
}
