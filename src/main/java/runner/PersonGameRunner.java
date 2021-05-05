package runner;

import actor.*;
import actor.actions.Reward;
import state.Direction;
import state.GameStateView;
import state.Point2D;
import state.PositionView;
import state.board.GameBoard;
import state.board.ReadableBoard;
import state.board.WritableBoard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

public class PersonGameRunner implements GameRunner<ReadableBoard, WritableBoard> {
    private static final double MAX_SCORE = 300;
    public static final boolean RELATIVE_POSITION = false;
    public static final Color BACKGROUND = Color.lightGray;
    public static final int RENDER_FREQUENCY = 1;

    private WritableBoard board;
    private final List<Board2DActor> actors;
    private final Person player; // this is one of the actors, but will be tracked more closely
    private final Skeleton enemy;

    private final Map<GameStateView, Color> uniqueColorLabels;

    private int renderCounter = 0;

    private BufferedImage cachedRender = null;

    public PersonGameRunner(GameBoard board, Person player, Skeleton enemy, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
        this.player = player;
        this.enemy = enemy;
        uniqueColorLabels = new HashMap<>();
    }

    @Override
    public void render(Graphics originalGraphic) {

        renderCounter = (renderCounter+1) % RENDER_FREQUENCY;

        if (cachedRender == null || renderCounter == 0) {
            cachedRender = new BufferedImage(GameCanvas.WIDTH, GameCanvas.HEIGHT, BufferedImage.TYPE_INT_RGB);
        }
        Graphics innerGraphics = cachedRender.createGraphics();

        board.locations().forEach(point -> {
            int rx = GameCanvas.TILE_SIZE * point.x;
            int ry = GameCanvas.TILE_SIZE * point.y;

            Point2D playerLoc = board.find(player).orElseThrow();
            Point2D distance = playerLoc.minus(point);

            if (renderCounter != 0 && (Math.abs(distance.x) > 1 || Math.abs(distance.y) > 1)) {
                return; // skip rendering sometimes
            }

            innerGraphics.setColor(BACKGROUND);
            innerGraphics.fillRect(rx, ry, GameCanvas.TILE_SIZE, GameCanvas.TILE_SIZE);

            boolean isScoring = player instanceof SmartPerson;
            if (isScoring) {
                // (255, 0, 0) => (0, 255, 0)
                Direction[] possibleDirections = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
                int[] angles = new int[]{90, 0, 270, 180};

                double bestScore = Integer.MIN_VALUE;
                for (int i=0; i < possibleDirections.length; i++) {
                    Direction possibleDir = possibleDirections[i];
                    int centerAngle = angles[i];

//                    PositionView hypothetical = PositionView.from(player, board, point, possibleDir, RELATIVE_POSITION, obj -> obj instanceof Bread);
                    PositionView hypothetical = PositionView.from(enemy, board, point, possibleDir, RELATIVE_POSITION, obj -> obj instanceof Person);

                    double scoreForLocation = enemy.getBrain().getMaximumEstimateScore(hypothetical);

                    bestScore = Math.max(bestScore, scoreForLocation);

                    // Paint the expected score
                    int rVal = (int) (255 * Math.max(0, Math.min(1, scoreForLocation/MAX_SCORE)));
                    Color scoreColor = new Color(255-rVal, rVal, 0);
                    innerGraphics.setColor(scoreColor);
                    innerGraphics.fillArc(rx + GameCanvas.TILE_SIZE/2 - GameCanvas.INNER_TILE_SIZE/2,
                            ry + GameCanvas.TILE_SIZE/2 - GameCanvas.INNER_TILE_SIZE/2,
                            GameCanvas.INNER_TILE_SIZE, GameCanvas.INNER_TILE_SIZE, (centerAngle-45)%365, 90);

                    // Label the unique states
                    Random random = new Random();
                    uniqueColorLabels.putIfAbsent(hypothetical, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    innerGraphics.setColor(uniqueColorLabels.get(hypothetical));
                    innerGraphics.fillArc(rx + GameCanvas.TILE_SIZE/2 - GameCanvas.INNER_TILE_SIZE/8,
                            ry + GameCanvas.TILE_SIZE/2 - GameCanvas.INNER_TILE_SIZE/8,
                            GameCanvas.INNER_TILE_SIZE/4, GameCanvas.INNER_TILE_SIZE/4, (centerAngle-45)%365, 90);
                }

                innerGraphics.setColor(Color.white);
                innerGraphics.drawString(String.format("%.2f", bestScore), rx+2, ry+10);
                innerGraphics.drawRect(rx, ry, GameCanvas.TILE_SIZE, GameCanvas.TILE_SIZE);

            } else {
                boolean isVisited = player.hasVisited(point);
                innerGraphics.setColor(isVisited ? Color.green : Color.gray);
            }

            innerGraphics.setColor(Color.green);
            board.members(point).forEach(boardObj -> boardObj.render(innerGraphics, rx, ry));
        });

        originalGraphic.drawImage(cachedRender, 0, 0, null);
        innerGraphics.dispose();
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
    public Reward calculateNextState(WritableBoard nextState,
                                     List<Action<ReadableBoard, WritableBoard>> chosen) {

        Reward[] rewards = new Reward[chosen.size()];
        for (int i = 0; i < chosen.size(); i++) {
            rewards[i] = chosen.get(i).updateState(nextState);
        }

        return Reward.merge(rewards);
    }

    @Override
    public void setCurrentState(WritableBoard newState) {
        this.board = newState;
    }
}
