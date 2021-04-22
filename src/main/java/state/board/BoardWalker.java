package state.board;

import state.Direction;
import state.Point2D;

public interface BoardWalker extends BoardObject {

    Direction getDirection();

    void setDirection(Direction direction);

    boolean hasVisited(Point2D location);
    void markAsVisited(Point2D location);
}
