package state;

public class Board implements GameState<Board> {

    public void insert(BoardObject boardObject, Point2D firstLoc) {

    }

    public void move(BoardObject boardObject, Point2D newLocation) {

    }

    public void remove(BoardObject boardObject) {

    }

    @Override
    public Board copy() {
        return null; // TODO: Implement
    }

    public static class Point2D {
        public final int x, y;

        public Point2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
