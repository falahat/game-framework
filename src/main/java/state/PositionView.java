package state;

import algorithms.traverse.BreadthFirstTraversal;
import state.PersonView.Sensed;
import state.board.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static state.PersonView.Sensed.BLOCKED;
import static state.PersonView.Sensed.FOOD;
import static state.PersonView.sense;

public class PositionView implements BoardView {
    final Direction personDirection;
    final Map<Direction, Sensed> neighbors;
    final Sensed currentTile;
    final boolean isAheadTileVisited;
    final Direction directionOfClosestFood;
    public PositionView(Direction personDirection,
                        Map<Direction, Sensed> neighbors,
                        Sensed currentTile,
                        boolean isAheadTileVisited,
                        Direction directionOfClosestFood) {

        this.personDirection = personDirection;
        this.neighbors = neighbors;
        this.currentTile = currentTile;
        this.isAheadTileVisited = isAheadTileVisited;
        this.directionOfClosestFood = directionOfClosestFood;
    }

    public boolean isBlockedAhead() {
        Sensed ahead = neighbors.getOrDefault(personDirection, BLOCKED);
        return ahead == Sensed.BLOCKED;
    }

    public boolean isAboveFood() {
        return currentTile == Sensed.FOOD;
    }

    public static PositionView from(BoardWalker actor, ReadableBoard board, boolean relative) {
        Optional<Point2D> currentLocation = board.find(actor);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("This actor does not exist in the board");
        }

        return from(actor, board, currentLocation.get(), relative);
    }

    public static PositionView from(BoardWalker actor, ReadableBoard board, Point2D currentLocation, boolean relative) {
        return from(actor, board, currentLocation, actor.getDirection(), relative);
    }

    public static PositionView from(BoardWalker actor, ReadableBoard board, Point2D currentLocation, Direction direction, boolean relative) {
        Map<Direction, Sensed> neighbors = new HashMap<>();
        for (Direction dir : Direction.values()) {
            Point2D neighborPoint = currentLocation.transform(dir);
            neighbors.put(dir, sense(board, neighborPoint));
        }

        Sensed currentTile = sense(board, currentLocation);

        boolean isAheadTileVisited = actor.hasVisited(currentLocation.transform(direction));

        Point2D closestFood = null;
        for (Point2D point : new BreadthFirstTraversal<>(board.getGraph())) {
            if (board.members(point).stream().anyMatch(boardObj -> boardObj instanceof Bread)) {
                closestFood = point;
                break;
            }
        }

        Direction directionClosestFood = closestFood == null ? Direction.NORTH : currentLocation.directionTo(closestFood);


        while (direction != Direction.NORTH) {
            direction = direction.clockwise();
            directionClosestFood = directionClosestFood.clockwise();

            Map<Direction, Sensed> newNeighbors = new HashMap<>();

            for (Direction currDir : neighbors.keySet()) {
                Sensed sensed = neighbors.get(currDir);
                newNeighbors.put(currDir.clockwise(), sensed);
            }

            neighbors = newNeighbors;
        }

        return new PositionView(direction, neighbors, currentTile, isAheadTileVisited, directionClosestFood);
    }

    @Override
    public int hashCode() {
        if (this.currentTile == Sensed.FOOD) {
            return Objects.hashCode(currentTile);
        }

        return Objects.hash(this.currentTile,
                this.isAheadTileVisited,
                this.personDirection,
                this.directionOfClosestFood
        );
        // Note that neighbors are not included in the hash, I didn't have time to handle hashing a map.
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PositionView))
            return false;

        PositionView other = (PositionView) obj;

        if (this.currentTile == Sensed.FOOD && other.currentTile == FOOD) {
            return true;
        }

        // Confirm map
        for (Direction dir : Direction.values()) {
            if (this.neighbors.get(dir) != other.neighbors.get(dir)) {
                return false;
            }
        }

        return Objects.equals(this.currentTile, other.currentTile)
                && Objects.equals(this.isAheadTileVisited, other.isAheadTileVisited)
                && Objects.equals(this.personDirection, other.personDirection)
                && Objects.equals(this.directionOfClosestFood, other.directionOfClosestFood);
    }
//
//    @Override
//    public String toString() {
//        return String.format("(Current=%s Left=%s Front=%s Right=%s)", this.current, this.front, this.left, this.right);
//    }
}