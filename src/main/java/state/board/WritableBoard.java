package state.board;

import state.Point2D;

public interface WritableBoard extends ReadableBoard {

    void insert(BoardObject boardObject, Point2D firstLoc);

    void move(BoardObject boardObject, Point2D newLocation);

    void remove(BoardObject boardObject);
}
