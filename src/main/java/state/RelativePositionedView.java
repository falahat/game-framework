package state;

import actor.Person;
import state.PersonView.Sensed;
import state.board.ReadableBoard;

import java.util.HashMap;
import java.util.Map;

import static state.PersonView.sense;

public class RelativePositionedView extends AbsolutePositionedView {
    public RelativePositionedView(Map<Direction, Sensed> relativeNeighbors,
                                  Sensed currentTile,
                                  boolean isAheadTileVisited) {
        super(Direction.NORTH, relativeNeighbors, currentTile, isAheadTileVisited);
    }

    public static RelativePositionedView from(Person actor, ReadableBoard board) {
        Point2D location =  board.find(actor).orElseThrow(() -> new IllegalStateException("Unable to find location"));

        return from(actor, board, location, actor.getDirection());
    }

    public static RelativePositionedView from(Person actor, ReadableBoard board, Point2D currentLocation, Direction direction) {
        Map<Direction, Sensed> absoluteNeighbors = new HashMap<>();
        for (Direction dir : Direction.values()) {
            Point2D neighborPoint = currentLocation.transform(dir);
            absoluteNeighbors.put(dir, sense(board, neighborPoint));
        }

        Map<Direction, Sensed> relativeNeighbors = new HashMap<>();

        for (Direction absoluteDirection : absoluteNeighbors.keySet()) {
            Sensed sensed = absoluteNeighbors.get(absoluteDirection);
            Direction newDir;
            switch (direction) {
                case WEST:
                    newDir = absoluteDirection.clockwise();
                    break;
                case SOUTH:
                    newDir = absoluteDirection.clockwise().clockwise();
                    break;
                case EAST:
                    newDir = absoluteDirection.counterClockwise();
                    break;
                case NORTH:
                default:
                    newDir = absoluteDirection; // no change
                    break;
            }

            relativeNeighbors.remove(Direction.SOUTH); // can't see behind you
            relativeNeighbors.remove(Direction.WEST);

            relativeNeighbors.put(newDir, sensed);
        }

        Sensed currentTile = sense(board, currentLocation);

        boolean isAheadTileVisited = actor.hasVisited(currentLocation.transform(direction));

        return new RelativePositionedView(relativeNeighbors, currentTile, isAheadTileVisited);
    }

}
