package state.board;

import state.GameState;
import state.Point2D;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReadableBoard extends GameState<ReadableBoard, WritableBoard> {

    GameMapGraph getGraph();

    List<Point2D> neighbors(Point2D center);

    Set<Point2D> locations();

    List<BoardObject> members(Point2D location);

    Optional<Point2D> find(BoardObject toFind);
}
