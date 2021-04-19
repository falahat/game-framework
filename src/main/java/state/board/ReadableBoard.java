package state.board;

import state.GameState;
import state.Point2D;

import java.util.List;

public interface ReadableBoard extends GameState<ReadableBoard, WritableBoard> {

    List<Point2D> neighbors(Point2D center);
}
