package actor;

import actor.actions.*;
import actor.algorithms.QALearner;
import runner.PersonGameRunner;
import runner.Drawable;
import state.Direction;
import state.GameStateView;
import state.Point2D;
import state.PositionView;
import state.board.BoardObject;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Skeleton implements WalkingActor, Drawable {
    private BufferedImage spriteSheet;
    private Direction direction;
    private final Set<Point2D> visited;
    private final Person prey;

    private final QALearner brain;

    public Skeleton(Direction direction, Person prey) {
        this.direction = direction;
        this.visited = new HashSet<>();
        this.prey = prey;
        this.brain = new QALearner();
    }

    @Override
    public Collection<Action<ReadableBoard, WritableBoard>> getAllowedActions(GameStateView currentView) {
        PositionView visible = (PositionView) currentView;

        List<Action<ReadableBoard, WritableBoard>> actions = new ArrayList<>();
        actions.add(new TurnLeft(this));
        actions.add(new TurnRight(this));

        if (visible.isFoodAhead()) {
            actions.add(new Lunge(this, prey));
        }

        if (visible.isFreeAhead()) {
            actions.add(new MoveAhead(this));
        }

        return actions;
    }

    @Override
    public GameStateView generateView(ReadableBoard readonlyState) {
        Point2D currentLocation = readonlyState.find(this).orElseThrow();
        return PositionView.from(this, readonlyState, currentLocation, getDirection(), PersonGameRunner.RELATIVE_POSITION, Skeleton::isFood);
    }

    private static boolean isFood(BoardObject possible) {
        return possible instanceof Person;
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentView, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        return brain.decide(currentView, allowedActions);
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, GameStateView firstView, GameStateView nextView, double immediateReward) {
        brain.learn(decided, firstView, nextView, immediateReward);
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
