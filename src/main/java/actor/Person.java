package actor;

import actor.actions.Eat;
import actor.actions.MoveAhead;
import actor.actions.TurnLeft;
import actor.actions.TurnRight;
import runner.Drawable;
import state.PersonView;
import state.Direction;
import state.Point2D;
import state.board.BoardWalker;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Person implements Board2DActor, BoardWalker, Drawable {
    private BufferedImage spriteSheet;
    private Direction direction;
    private final Set<Point2D> visited;

    public Person(Direction direction) {
        this.direction = direction;
        this.visited = new HashSet<>();
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(ReadableBoard currentState,
                                                       Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        // Ant can only see the block in front of it
        Action<ReadableBoard, WritableBoard> decided = null;

        // This assumes the moves are legal
        for (Action<ReadableBoard, WritableBoard> possible : allowedActions) {
            if ((possible instanceof Eat)) {
                // Ant will eat if there is food directly in front of it
                return possible;
            } else {
                decided = possible;
                if (new Random().nextBoolean()) {
                    return decided; // This is meant to randomize a little bit, TODO: improve
                }
            }
        }

        if (decided == null) {
            throw new IllegalArgumentException("Did not have any available actions");
        } else {
            return decided;
        }
    }

    @Override
    public Collection<Action<ReadableBoard, WritableBoard>> getAllowedActions(ReadableBoard currentView) {
        PersonView visible = getVisibleState(currentView);

        List<Action<ReadableBoard, WritableBoard>> actions = new ArrayList<>();

        // Can always turn left/right
//        actions.add(new TurnLeft(this)); // only turn right to decrease the chance of spinning in place
        actions.add(new TurnRight(this));

        if (visible.isAboveFood()) {
            actions.add(new Eat(this));
        }

        if (!visible.isBlockedAhead()) {
            actions.add(new MoveAhead(this));
        }

        // Can attempt to go forward, but might be blocked
        // If we are 100% sure the tile in front is blocked, we will not go forward
        return actions;
    }

    @Override
    public void markAsVisited(Point2D location) {
        visited.add(location);
    }

    @Override
    public boolean hasVisited(Point2D location) {
        return visited.contains(location);
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, ReadableBoard firstState, ReadableBoard nextState) {
        // TODO: implement
    }

    private PersonView getVisibleState(ReadableBoard fullState) {
        return PersonView.from(this, fullState);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        if (spriteSheet == null) {
            spriteSheet = loadImage("/hero1.png");
        }
        // down
        // side (right)
        // up
        switch (getDirection()) {
            case NORTH:
                return spriteSheet.getSubimage(0, 0, 32, 32);
            case SOUTH:
                return spriteSheet.getSubimage(0, 2*32, 32, 32);
            case WEST:
            case EAST: // TODO: flip
            default:
                return spriteSheet.getSubimage(0, 32, 32, 32);
        }
    }

}
