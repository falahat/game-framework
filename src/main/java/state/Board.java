package state;

import model.AdjacencyListGraph;
import model.Graph;
import state.graph.BoardTile;
import state.graph.TileEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board implements GameState<Board> {
    private Graph<BoardTile, TileEdge> tileGraph;
    private Map<Point2D, BoardTile> locationToTile;

    public Board(int boardWidth, int boardHeight) {
        this.locationToTile = new HashMap<>();
        this.tileGraph = new AdjacencyListGraph<>();

        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                Point2D point = new Point2D(x, y);
                BoardTile tile = new BoardTile(point);
                locationToTile.put(point, tile);
            }
        }

        regenerateGraphEdges(tileGraph, locationToTile);
    }

    private static void regenerateGraphEdges(Graph<BoardTile, TileEdge> tileGraph, Map<Point2D, BoardTile> locationToTile) {
        // Clear all edges
        tileGraph.edges().forEach(tileGraph::remove);

        // Regenerate Edges
        for (Point2D point : locationToTile.keySet()) {
            BoardTile currentTile = locationToTile.get(point);
            Stream.of(point.left(), point.right(), point.up(), point.down())
                    .filter(otherPoint -> locationToTile.containsKey(otherPoint))
                    .map(otherPoint -> locationToTile.get(otherPoint))
                    .filter(otherTile -> !otherTile.isBlocking())
                    .forEach(otherTile -> tileGraph.add(new TileEdge(currentTile, otherTile)));
        }
    }

    private Board(Graph<BoardTile, TileEdge> tileGraph, Map<Point2D, BoardTile> locationToTile) {
        this.locationToTile = locationToTile;
        this.tileGraph = tileGraph;
        regenerateGraphEdges(tileGraph, locationToTile);
    }

    public void insert(BoardObject boardObject, Point2D firstLoc) {
        if (!locationToTile.containsKey(firstLoc)) {
            throw new IllegalArgumentException("No tile found at location");
        }
        BoardTile tile = locationToTile.get(firstLoc);
    }

    public void move(BoardObject boardObject, Point2D newLocation) {

    }

    public void remove(BoardObject boardObject) {

    }

    @Override
    public Board copy() {
        Map<Point2D, BoardTile> locationToTileCopy = this.locationToTile
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Graph<BoardTile, TileEdge> graphCopy = new AdjacencyListGraph<>();
        this.tileGraph.nodes().forEach(graphCopy::add);

        return new Board(graphCopy, locationToTileCopy);
    }

}
