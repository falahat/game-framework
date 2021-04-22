package state.board;

import model.UndirectedAdjacencyListGraph;
import model.graph.DelegatingLabeledGraph;
import state.Point2D;
import state.graph.BoardTile;

public class GameMapGraph extends DelegatingLabeledGraph<Point2D, BoardTile> {
    public GameMapGraph() {
        super(new UndirectedAdjacencyListGraph<>());
    }
}
