package state;

import model.AdjacencyListGraph;
import model.graph.Graph;
import state.graph.BoardTile;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Board implements GameState<Board> {
    private Graph<BoardTile> tileGraph;
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

    private static void regenerateGraphEdges(Graph<BoardTile> tileGraph, Map<Point2D, BoardTile> locationToTile) {
        // Clear all edges
        tileGraph.edges().forEach(edge -> tileGraph.disconnect(edge.source(), edge.destination()));

        // Regenerate Edges
        for (Point2D point : locationToTile.keySet()) {
            BoardTile currentTile = locationToTile.get(point);
            Stream.of(point.left(), point.right(), point.up(), point.down())
                    .filter(otherPoint -> locationToTile.containsKey(otherPoint))
                    .map(otherPoint -> locationToTile.get(otherPoint))
                    .filter(otherTile -> !otherTile.isBlocking())
                    .forEach(otherTile -> tileGraph.connect(currentTile, otherTile));
        }
    }

    private Board(Graph<BoardTile> tileGraph, Map<Point2D, BoardTile> locationToTile) {
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
//        Map<Point2D, BoardTile> locationToTileCopy = this.locationToTile
//                .entrySet()
//                .stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//        AdjacencyListGraph<BoardTile, TileEdge> graphCopy = new AdjacencyListGraph<>();
//        this.tileGraph.nodes().forEach(graphCopy::add);
//
//        return new Board(graphCopy, locationToTileCopy);
        return null;
        // TODO: Implement this when we have a better way of transfering nodes from graph => graph. Need to get rid of the whole V/E business
    }

}
