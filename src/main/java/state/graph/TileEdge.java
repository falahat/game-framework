package state.graph;

import model.Edge;
import state.Direction;

public class TileEdge implements Edge<BoardTile> {
    private final BoardTile tile1, tile2;
    private final Direction direction;

    public TileEdge(BoardTile tile1, BoardTile tile2) {
        this.tile1 = tile1;
        this.tile2 = tile2;
        this.direction = tile1.getPoint().directionTo(tile2.getPoint());
    }

    @Override
    public BoardTile node1() {
        return tile1;
    }

    @Override
    public BoardTile node2() {
        return tile2;
    }

    @Override
    public boolean isDirected() {
        return true;
    }
}
