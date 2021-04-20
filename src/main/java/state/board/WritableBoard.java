package state.board;

import model.AdjacencyListGraph;
import model.UndirectedAdjacencyListGraph;
import model.graph.Graph;
import state.GameState;
import state.Point2D;
import state.graph.BoardTile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class WritableBoard implements ReadableBoard, GameState<ReadableBoard, WritableBoard> {

    private Graph tileGraph;
    private Map<Point2D, BoardTile> locationToTile;
    private Map<BoardObject, BoardTile> memberToTile;

    public WritableBoard() {
        this.locationToTile = new HashMap<>();
        this.memberToTile = new HashMap<>();
        this.tileGraph = new UndirectedAdjacencyListGraph();
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

    private static void regenerateGraphEdges(Graph tileGraph, Map<Point2D, BoardTile> locationToTile) {
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

    private WritableBoard(Graph tileGraph,
                          Map<Point2D, BoardTile> locationToTile,
                          Map<BoardObject, BoardTile> memberToTile) {
        this.locationToTile = locationToTile;
        this.tileGraph = tileGraph;
        this.memberToTile = memberToTile;
        regenerateGraphEdges(tileGraph, locationToTile);
        // TODO: Deep-copy the data correctly
    }

    public void insert(BoardObject boardObject, Point2D firstLoc) {
        if (memberToTile.containsKey(boardObject)) {
            throw new IllegalStateException("Attempting to insert an object that already exists");
        } else if (!locationToTile.containsKey(firstLoc)) {
            throw new IllegalArgumentException("No tile found at location");
        }

        move(boardObject, null, locationToTile.get(firstLoc));
    }

    public void move(BoardObject boardObject, Point2D newLocation) {
        if (!memberToTile.containsKey(boardObject)) {
            throw new IllegalStateException("Attempting to move an object that does not exist");
        } else if (!locationToTile.containsKey(newLocation)) {
            throw new IllegalArgumentException("No tile found at location");
        }

        move(boardObject, memberToTile.get(boardObject), locationToTile.get(newLocation));
    }

    public void remove(BoardObject boardObject) {
        if (!memberToTile.containsKey(boardObject)) {
            throw new IllegalStateException("Attempting to delete an object that does not exist");
        }

        move(boardObject, memberToTile.get(boardObject), null);
    }

    private void move(BoardObject boardObject, BoardTile oldLocation, BoardTile newLocation) {
        if (oldLocation != null) {
            oldLocation.remove(boardObject);
        }

        if (newLocation == null) {
            memberToTile.remove(boardObject);
        } else {
            newLocation.add(boardObject);
            memberToTile.put(boardObject, newLocation);
        }
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
        return new WritableBoard(this.tileGraph, new HashMap<>(this.locationToTile), new HashMap<>(this.memberToTile));
    }

    @Override
    public List<Point2D> neighbors(Point2D center) {
        return null;
    }

    @Override
    public List<BoardObject> members(Point2D location) {
        return null;
    }

    @Override
    public Optional<Point2D> find(BoardObject toFind) {
        return Optional.empty();
    }
}
