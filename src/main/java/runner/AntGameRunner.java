package runner;

import actor.*;
import state.Direction;
import state.AbsolutePositionedView;
import state.RelativePositionedView;
import state.board.ReadableBoard;
import state.board.GameBoard;
import state.board.WritableBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AntGameRunner implements GameRunner<ReadableBoard, WritableBoard> {
    private WritableBoard board;
    private final List<Board2DActor> actors;
    private final Person player; // this is one of the actors, but will be tracked more closely

    public AntGameRunner(GameBoard board, Person player, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
        this.player = player;
    }

    @Override
    public void render(Graphics g) {
        board.locations().forEach(point -> {
            int rx = GameCanvas.TILE_SIZE * point.x;
            int ry = GameCanvas.TILE_SIZE * point.y;

            int cx = rx + GameCanvas.TILE_SIZE/2;
            int cy = ry + GameCanvas.TILE_SIZE/2;

            boolean isScoring = player instanceof SmartPerson;
            if (isScoring) {
                // (255, 0, 0) => (0, 255, 0)
                double MAX_SCORE = 250;
                Direction[] possibleDirections = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
                int[] angles = new int[]{90, 0, 270, 180};

                double bestScore = Integer.MIN_VALUE;
                for (int i=0; i < possibleDirections.length; i++) {
                    Direction possibleDir = possibleDirections[i];
                    int centerAngle = angles[i];

                    RelativePositionedView hypothetical = RelativePositionedView.from(player, board, point, possibleDir);
                    double scoreForLocation = ((SmartPerson) player).getMaximumEstimateScore(hypothetical);

                    bestScore = Math.max(bestScore, scoreForLocation);

                    int rVal = (int) (255 * Math.max(0, Math.min(1, scoreForLocation/MAX_SCORE)));
                    Color scoreColor = new Color(255-rVal, rVal, 0);
//                    scoreColor = possibleDir == Direction.NORTH ? Color.cyan : scoreColor;

                    g.setColor(scoreColor);
                    g.fillArc(rx + GameCanvas.TILE_SIZE/2 - GameCanvas.INNER_TILE_SIZE/2,
                            ry + GameCanvas.TILE_SIZE/2 - GameCanvas.INNER_TILE_SIZE/2,
                            GameCanvas.INNER_TILE_SIZE, GameCanvas.INNER_TILE_SIZE, (centerAngle-45)%365, 90);
                }
                g.drawRect(rx, ry, GameCanvas.TILE_SIZE, GameCanvas.TILE_SIZE);
                g.setColor(Color.black);
                g.drawString(String.format("%.2f", bestScore), rx+2, ry+10);
            } else {
                boolean isVisited = player.hasVisited(point);
                g.setColor(isVisited ? Color.green : Color.gray);
            }


            g.setColor(Color.green);
            board.members(point).forEach(boardObj -> boardObj.render(g, cx, cy));
        });
    }

    @Override
    public List<Actor<ReadableBoard, WritableBoard>> getActorsForCurrentTurn() {
        return new ArrayList<>(actors);
    }

    @Override
    public WritableBoard getCurrentState() {
        return board;
    }

    @Override
    public WritableBoard calculateNextState(ReadableBoard previousState, List<Action<ReadableBoard, WritableBoard>> chosen, List<Double> rewardsPerAction) {
        // later, we can make changes which don't depend on an action, such as increasing hunger by 1 each round.
        WritableBoard nextState = previousState.mutableCopy();
        chosen.forEach(action -> rewardsPerAction.add(action.updateState(nextState)));
        return nextState;
    }

    @Override
    public void setCurrentState(WritableBoard newState) {
        this.board = newState;
    }
}
