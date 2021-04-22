package state;

import actor.Person;
import state.PersonView.Sensed;
import state.board.BoardView;
import state.board.ReadableBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static state.PersonView.Sensed.BLOCKED;
import static state.PersonView.Sensed.FOOD;
import static state.PersonView.sense;

public class AbsolutePositionedView implements BoardView {
    final Direction personDirection;
    final Map<Direction, Sensed> neighbors;
    final Sensed currentTile;
    final boolean isAheadTileVisited;

    public AbsolutePositionedView(Direction personDirection,
                                  Map<Direction, Sensed> neighbors,
                                  Sensed currentTile,
                                  boolean isAheadTileVisited) {

        this.personDirection = personDirection;
        this.neighbors = neighbors;
        this.currentTile = currentTile;
        this.isAheadTileVisited = isAheadTileVisited;
    }

    public boolean isBlockedAhead() {
        Sensed ahead = neighbors.getOrDefault(personDirection, BLOCKED);
        return ahead == Sensed.BLOCKED;
    }

    public boolean isAboveFood() {
        return currentTile == Sensed.FOOD;
    }

    public static AbsolutePositionedView from(Person actor, ReadableBoard board) {
        Optional<Point2D> currentLocation = board.find(actor);
        if (currentLocation.isEmpty()) {
            throw new IllegalStateException("This actor does not exist in the board");
        }

        return from(actor, board, currentLocation.get());
    }

    public static AbsolutePositionedView from(Person actor, ReadableBoard board, Point2D currentLocation) {
        return from(actor, board, currentLocation, actor.getDirection());
    }

    public static AbsolutePositionedView from(Person actor, ReadableBoard board, Point2D currentLocation, Direction direction) {
        Map<Direction, Sensed> neighbors = new HashMap<>();
        for (Direction dir : Direction.values()) {
            Point2D neighborPoint = currentLocation.transform(dir);
            neighbors.put(dir, sense(board, neighborPoint));
        }

        Sensed currentTile = sense(board, currentLocation);

        boolean isAheadTileVisited = actor.hasVisited(currentLocation.transform(direction));

        return new AbsolutePositionedView(direction, neighbors, currentTile, isAheadTileVisited);
    }

    @Override
    public int hashCode() {
        if (this.currentTile == Sensed.FOOD) {
            return Objects.hashCode(currentTile);
        }

        return Objects.hash(this.currentTile,
                this.isAheadTileVisited,
                this.personDirection
        );
        // Note that neighbors are not included in the hash, I didn't have time to handle hashing a map.
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbsolutePositionedView))
            return false;

        AbsolutePositionedView other = (AbsolutePositionedView) obj;

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
                && Objects.equals(this.personDirection, other.personDirection);
    }
//
//    @Override
//    public String toString() {
//        return String.format("(Current=%s Left=%s Front=%s Right=%s)", this.current, this.front, this.left, this.right);
//    }
}
