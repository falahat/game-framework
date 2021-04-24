package runner;

import actor.*;
import state.*;
import state.board.Bread;
import state.board.ReadableBoard;
import state.board.GameBoard;
import state.board.WritableBoard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class AntGameRunner implements GameRunner<ReadableBoard, WritableBoard> {
    private static final double MAX_SCORE = 300;
    public static final boolean RELATIVE_POSITION = true;
    public static Color BACKGROUND = Color.lightGray;

    private WritableBoard board;
    private final List<Board2DActor> actors;
    private final Person player; // this is one of the actors, but will be tracked more closely

    private final Map<GameStateView, Color> uniqueColorLabels;

    private int renderCounter = 0;

    private BufferedImage cachedRender = null;

    public AntGameRunner(GameBoard board, Person player, List<Board2DActor> actors) {
        // Assume actors have already been inserted on the board.
        this.board = board;
        this.actors = actors;
        this.player = player;
        uniqueColorLabels = new HashMap<>();
    }

    @Override
    public void render(Graphics originalGraphic) {

        renderCounter = (renderCounter+1) % 20;

        if (cachedRender == null || renderCounter == 0) {
//            Image image = new BufferedImage()
            cachedRender = new BufferedImage(GameCanvas.WIDTH, GameCanvas.HEIGHT, BufferedImage.TYPE_INT_RGB);
        }
        Graphics innerGraphics = cachedRender.createGraphics();

        board.locations().forEach(point -> {
            int rx = GameCanvas.TILE_SIZE * point.x;
            int ry = GameCanvas.TILE_SIZE * point.y;

            int cx = rx + GameCanvas.TILE_SIZE/2;
            int cy = ry + GameCanvas.TILE_SIZE/2;

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

                    PositionView hypothetical = PositionView.from(player, board, point, possibleDir, RELATIVE_POSITION);
                    double scoreForLocation = ((SmartPerson) player).getMaximumEstimateScore(hypothetical);
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
