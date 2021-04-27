package actor;

import actor.actions.TurnLeft;
import actor.actions.TurnRight;
import runner.AntGameRunner;
import runner.Drawable;
import state.Direction;
import state.GameStateView;
import state.Point2D;
import state.PositionView;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Skeleton implements WalkingActor, Drawable {
    private BufferedImage spriteSheet;
    private Direction direction;
    private final Set<Point2D> visited;

    public Skeleton(Direction direction) {
        this.direction = direction;
        this.visited = new HashSet<>();
    }

    @Override
    public Collection<Action<ReadableBoard, WritableBoard>> getAllowedActions(GameStateView currentView) {
        return Collections.singleton(new TurnLeft(this));
    }

    @Override
    public GameStateView generateView(ReadableBoard readonlyState) {
        return PositionView.from(this, readonlyState, AntGameRunner.RELATIVE_POSITION);
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentView, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        return allowedActions.stream().findAny().orElse(new TurnRight(this));
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, GameStateView firstView, GameStateView nextView, double immediateReward) {

    }


    @Override
    public BufferedImage getImage() throws IOException {
        if (spriteSheet == null) {
            spriteSheet = loadImage("/BODY_skeleton.png");
        }

        int spriteSize = 64;

        switch (getDirection()) {
            case SOUTH:
                return spriteSheet.getSubimage(0, 0, spriteSize, spriteSize);
            case NORTH:
                return spriteSheet.getSubimage(0, spriteSize, spriteSize, spriteSize);
            case WEST:
            case EAST: // TODO: flip
            default:
                return spriteSheet.getSubimage(0, 2*spriteSize, spriteSize, spriteSize);
        }
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
    public boolean hasVisited(Point2D location) {
        return visited.contains(location);
    }

    @Override
    public void markAsVisited(Point2D location) {
        visited.add(location);
    }

    @Override
    public void clearAllVisited() {
        visited.clear();
    }
}
