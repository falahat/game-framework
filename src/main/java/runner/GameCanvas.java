package runner;

import actor.Skeleton;
import actor.SmartPerson;
import state.Direction;
import state.Point2D;
import state.board.Bread;
import state.board.GameBoard;
import state.board.Rock;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.Random;

// credit to https://code-knowledge.com/java-create-game-introduction/
public class GameCanvas extends Canvas implements Runnable {

    public static final int NUM_TILES = 16;
    public static final int TILE_SIZE = 100;
    public static final int INNER_TILE_SIZE = 75;
    public static final int WIDTH = NUM_TILES * TILE_SIZE;
    public static final int HEIGHT = NUM_TILES * TILE_SIZE;
    public static final int SCALE = 1;
    public static final int GRAPH_WIDTH = 10;
    public final String TITLE = "Game Runner";

    private boolean running = false;
    private Thread thread;

    PersonGameRunner gameRunner;
    GameBoard gameBoard;
    SmartPerson player;
    Skeleton enemy;

    public void init(){
        requestFocus();
        gameBoard = generateGameBoard(4);

        player = new SmartPerson(Direction.NORTH);
        enemy = new Skeleton(Direction.NORTH, player);

        gameBoard.insert(player, new Point2D(3, 3));
        gameBoard.insert(enemy, new Point2D(2, 2));

        gameRunner = new PersonGameRunner(gameBoard, player, enemy, Arrays.asList(player, enemy));
    }

    private GameBoard generateGameBoard(int size) {
        GameBoard gameBoard = new GameBoard(size, size);

        Random random = new Random();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (random.nextBoolean()) {
                    gameBoard.insert(new Bread(), new Point2D(x, y));
                } else if (random.nextBoolean() && random.nextBoolean()) {
                    gameBoard.insert(new Rock(), new Point2D(x, y));
                }
            }
        }
        return gameBoard;
    }


    private synchronized void start(){
        if(running){
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if(!running) {
            return;
        }

        running = false;
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }



    public void run(){
        init();

        int round = 0;
        int turn = 1;
        while (running) {
            turn = ((turn + 1) % 500);
            if (turn == 0) {
                round++;
                int size = round <= 3 ? 5 : GRAPH_WIDTH;
                gameBoard = generateGameBoard(size);

                player.clearAllVisited();
                player.setDirection(Direction.NORTH);
                gameBoard.insert(player, new Point2D(3, 3));

                enemy.clearAllVisited();
                enemy.setDirection(Direction.NORTH);
                gameBoard.insert(enemy, new Point2D(2, 2));

                gameRunner = new PersonGameRunner(gameBoard, player, enemy, Arrays.asList(player, enemy));
            }

            tick();
            render();
        }
        stop();
    }

    private void tick() {
        gameRunner.turn();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
//        g.fillRect(0, 0, WIDTH, HEIGHT);
        gameRunner.render(g);

        g.dispose();
        bs.show();
    }

    public static void main (String[] args){
        GameCanvas game = new GameCanvas();

        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }

}


