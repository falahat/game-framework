package state.graph;

import state.board.BoardObject;
import state.Point2D;

import java.util.HashSet;
import java.util.Set;

public class BoardTile {
    private final Point2D point;
    private final Set<BoardObject> members;

    public BoardTile(Point2D point) {
        this.point = point;
        this.members = new HashSet<>();
    }

    public void add(BoardObject newMember) {
        this.members.add(newMember);
    }

    public Set<BoardObject> getMembers() {
        return members;
    }

    public boolean isBlocking() {
        return this.members.stream().anyMatch(BoardObject::isBlocking);
    }

    public Point2D getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return String.format("Tile[%d, %d]", point.x, point.y);
    }
}
