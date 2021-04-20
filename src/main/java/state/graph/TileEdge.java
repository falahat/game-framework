package state.graph;

import state.Direction;

// TODO: move this logic elsewhere (labeled graph)
public class TileEdge {
    private final BoardTile tile1, tile2;
    private final Direction direction;

    public TileEdge(BoardTile tile1, BoardTile tile2) {
        this.tile1 = tile1;
        this.tile2 = tile2;
        this.direction = tile1.getPoint().directionTo(tile2.getPoint());
    }
}
