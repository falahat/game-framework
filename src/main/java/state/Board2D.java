package state;

import java.util.Collection;
import java.util.Map;

public class Board2D implements GameState<Board2D> {

    private Map<Board2DObject, Point2D> objectToLocation;
    private Map<Point2D, Collection<Board2DObject>> locationToObject;

    public Board2D copy() {
        return null;
    }

    public void insert(Board2DObject board2DObject, Point2D firstLoc) {
        if (objectToLocation.containsKey(board2DObject)) {
            throw new IllegalStateException("Added an object which already exists");
        }


    }

    public void move(Board2DObject boardObject, Point2D newLocation) {

    }

    public void remove(Board2DObject board2DObject) {

    }

    public class Point2D {
        public final int x, y;

        public Point2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
