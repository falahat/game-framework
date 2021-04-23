package actor;

import runner.Drawable;
import state.Direction;
import state.GameStateView;
import state.Point2D;
import state.board.BoardWalker;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Skeleton implements Board2DActor, BoardWalker, Drawable {
    private BufferedImage spriteSheet;
    private Direction direction;
    private final Set<Point2D> visited;

    public Skeleton(Direction direction) {
        this.direction = direction;
        this.visited = new HashSet<>();
    }

    @Override
    public Collection<Action<ReadableBoard, WritableBoard>> getAllowedActions(GameStateView currentView) {
        return null;
    }

    @Override
    public GameStateView generateView(ReadableBoard readonlyState) {
        return null;
    }

    @Override
    public Action<ReadableBoard, WritableBoard> decide(GameStateView currentView, Collection<Action<ReadableBoard, WritableBoard>> allowedActions) {
        return null;
    }

    @Override
    public void learn(Action<ReadableBoard, WritableBoard> decided, GameStateView firstView, GameStateView nextView, double immediateReward) {

    }

    @Override
    public BufferedImage getImage() throws IOException {
        return null;
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
