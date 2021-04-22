package state.board;

import state.Direction;

public interface BoardWalker extends BoardObject {

    Direction getDirection();

    void setDirection(Direction direction);
}
