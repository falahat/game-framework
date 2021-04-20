package state.board;

import model.UndirectedAdjacencyListGraph;
import model.graph.DelegatingLabeledGraph;
import state.GameState;
import state.Point2D;
import state.graph.BoardTile;

import java.util.*;
import java.util.stream.Stream;

public class WritableBoard implements ReadableBoard, GameState<ReadableBoard, WritableBoard> {

    private DelegatingLabeledGraph<Point2D, BoardTile> tileGraph;
    private Map<BoardObject, BoardTile> memberToTile;

    public WritableBoard() {
        this.memberToTile = new HashMap<>();
        this.tileGraph = new DelegatingLabeledGraph<>(new UndirectedAdjacencyListGraph<>());
    }

    public WritableBoard(int boardWidth, int boardHeight) {
        this();
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                Point2D point = new Point2D(x, y);
                BoardTile tile = new BoardTile(point);
                tileGraph.put(point, tile);
            }
        }

        regenerateGraphEdges(tileGraph);
    }

    private static void regenerateGraphEdges(DelegatingLabeledGraph<Point2D, BoardTile> tileGraph) {
        // Regenerate Edges
        for (Point2D currentPoint : tileGraph.nodes()) {
            Stream.of(currentPoint.left(), currentPoint.right(), currentPoint.up(), currentPoint.down())
                    .filter(tileGraph::contains) // If not contained, this point might be out of bounds
                    .filter(otherPoint -> !tileGraph.get(otherPoint).isBlocking())
                    .forEach(otherPoint -> tileGraph.connect(currentPoint, otherPoint));
        }
    }

    private WritableBoard(DelegatingLabeledGraph<Point2D, BoardTile> tileGraph,
                          Map<BoardObject, BoardTile> memberToTile) {
        this.tileGraph = tileGraph;
        this.memberToTile = memberToTile;
        regenerateGraphEdges(tileGraph);
        // TODO: Deep-copy the data correctly
    }

    public void insert(BoardObject boardObject, Point2D firstLoc) {
        assertMemberDoesNotExist(boardObject);
        assertLocationExists(firstLoc);

        move(boardObject, null, tileGraph.get(firstLoc));
    }


    public void move(BoardObject boardObject, Point2D newLocation) {
        assertMemberExists(boardObject);
        assertLocationExists(newLocation);

        move(boardObject, memberToTile.get(boardObject), tileGraph.get(newLocation));
    }

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
    public WritableBoard mutableCopy() {
        return this.copy();
    }

    private WritableBoard copy() {
        return new WritableBoard(this.tileGraph, new HashMap<>(this.memberToTile));
    }

    @Override
    public List<Point2D> neighbors(Point2D center) {
        assertLocationExists(center);

        return new ArrayList<>(tileGraph.edges(center)); // TODO: do a last-second check to make sure they are not blocked?
    }

    @Override
    public List<BoardObject> members(Point2D location) {
        assertLocationExists(location);

        return new ArrayList<>(tileGraph.get(location).getMembers());
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
        if (!tileGraph.contains(firstLoc)) {
            throw new IllegalArgumentException("No tile found at location");
        }
    }

    public void assertMemberDoesNotExist(BoardObject boardObject) {
        if (memberToTile.containsKey(boardObject)) {
            throw new IllegalStateException("Attempting to insert an object that already exists");
        }
    }
}
