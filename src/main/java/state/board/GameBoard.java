package state.board;

import state.Point2D;
import state.graph.BoardTile;

import java.util.*;
import java.util.stream.Stream;

public class GameBoard implements ReadableBoard, WritableBoard {

    private final GameMapGraph mapGraph;
    private final Map<BoardObject, BoardTile> memberToTile;

    public GameBoard() {
        this.memberToTile = new HashMap<>();
        this.mapGraph = new GameMapGraph();
    }

    public GameBoard(int boardWidth, int boardHeight) {
        this();
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                Point2D point = new Point2D(x, y);
                BoardTile tile = new BoardTile(point);
                mapGraph.put(point, tile);
            }
        }

        regenerateGraphEdges(mapGraph);
    }

    private static void regenerateGraphEdges(GameMapGraph tileGraph) {
        // Regenerate Edges
        for (Point2D currentPoint : tileGraph.nodes()) {
            Stream.of(currentPoint.left(), currentPoint.right(), currentPoint.up(), currentPoint.down())
                    .filter(tileGraph::contains) // If not contained, this point might be out of bounds
                    .filter(otherPoint -> !tileGraph.get(otherPoint).isBlocking())
                    .forEach(otherPoint -> tileGraph.connect(currentPoint, otherPoint)); // may be redundant
        }
    }

    private GameBoard(GameMapGraph tileGraph,
                      Map<BoardObject, BoardTile> memberToTile) {
        this.mapGraph = tileGraph;
        this.memberToTile = memberToTile;
        regenerateGraphEdges(tileGraph);
        // TODO: Deep-copy the data correctly
    }

    @Override
    public void insert(BoardObject boardObject, Point2D firstLoc) {
        assertMemberDoesNotExist(boardObject);
        assertLocationExists(firstLoc);

        move(boardObject, null, mapGraph.get(firstLoc));
    }

    @Override
    public void move(BoardObject boardObject, Point2D newLocation) {
        assertMemberExists(boardObject);
        assertLocationExists(newLocation);

        move(boardObject, memberToTile.get(boardObject), mapGraph.get(newLocation));
    }

    @Override
    public void remove(BoardObject boardObject) {
        assertMemberExists(boardObject);

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
    public GameBoard mutableCopy() {
        return this.copy();
    }

    private GameBoard copy() {
        return new GameBoard(this.mapGraph, new HashMap<>(this.memberToTile));
    }

    @Override
    public List<Point2D> neighbors(Point2D center) {
        assertLocationExists(center);

        return new ArrayList<>(mapGraph.edges(center)); // TODO: do a last-second check to make sure they are not blocked?
    }

    @Override
    public List<BoardObject> members(Point2D location) {
        assertLocationExists(location);

        return new ArrayList<>(mapGraph.get(location).getMembers());
    }

    @Override
    public Optional<Point2D> find(BoardObject toFind) {
        return Optional.ofNullable(this.memberToTile.get(toFind)).map(BoardTile::getPoint);
    }

    public void assertMemberExists(BoardObject boardObject) {
        if (!memberToTile.containsKey(boardObject)) {
            throw new IllegalStateException("Attempting to move an object that does not exist");
        }
    }

    public void assertLocationExists(Point2D firstLoc) {
        if (!mapGraph.contains(firstLoc)) {
            throw new IllegalArgumentException(String.format("No tile found at location: %s", firstLoc));
        }
    }

    public void assertMemberDoesNotExist(BoardObject boardObject) {
        if (memberToTile.containsKey(boardObject)) {
            throw new IllegalStateException("Attempting to insert an object that already exists");
        }
    }
}
