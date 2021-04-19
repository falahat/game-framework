package state.board;

import model.AdjacencyListGraph;
import model.graph.Graph;
import state.GameState;
import state.Point2D;
import state.graph.BoardTile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class WritableBoard implements ReadableBoard, GameState<ReadableBoard, WritableBoard> {

    private Graph<BoardTile> tileGraph;
    private Map<Point2D, BoardTile> locationToTile;

    public WritableBoard() {
        this.locationToTile = new HashMap<>();
        this.tileGraph = new AdjacencyListGraph<>();
    }

    public WritableBoard(int boardWidth, int boardHeight) {
        this();
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

    private WritableBoard(Graph<BoardTile> tileGraph, Map<Point2D, BoardTile> locationToTile) {
        this.locationToTile = locationToTile;
        this.tileGraph = tileGraph;
        regenerateGraphEdges(tileGraph, locationToTile);
        // TODO: Deep-copy the data correctly
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
    public ReadableBoard immutableCopy() {
        return this; // we can be cheaper
    }

    @Override
    public WritableBoard mutableCopy() {
        return this.copy();
    }

    private WritableBoard copy() {
        return new WritableBoard(this.tileGraph, this.locationToTile);
    }

    @Override
    public List<Point2D> neighbors(Point2D center) {
        return null;
    }
}
