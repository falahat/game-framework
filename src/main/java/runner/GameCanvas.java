package runner;

import actor.Board2DActor;
import actor.Person;
import actor.Skeleton;
import actor.SmartPerson;
import state.Direction;
import state.Point2D;
import state.board.Bread;
import state.board.GameBoard;
import state.board.Rock;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    AntGameRunner gameRunner;
    GameBoard gameBoard;
    SmartPerson player;

    public void init(){
        requestFocus();
        gameBoard = generateGameBoard();

        player = new SmartPerson(Direction.NORTH);

        Skeleton enemy = new Skeleton(Direction.NORTH);

        gameBoard.insert(player, new Point2D(5, 5));
        gameBoard.insert(enemy, new Point2D(4, 4));

        gameRunner = new AntGameRunner(gameBoard, player, Collections.singletonList(player));
    }

    private GameBoard generateGameBoard() {
        GameBoard gameBoard = new GameBoard(GRAPH_WIDTH, GRAPH_WIDTH);

        Random random = new Random();
        for (int x = 0; x < GRAPH_WIDTH; x++) {
            for (int y = 0; y < GRAPH_WIDTH; y++) {
                if (random.nextBoolean()) {
                    gameBoard.insert(new Bread(), new Point2D(x, y));
                } else if (random.nextBoolean()) {
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

        int i = 1;

        while(running){
            i = ((i + 1) % 500);
            if (i == 0) {
                gameBoard = generateGameBoard();

                player.clearAllVisited();
                player.setDirection(Direction.NORTH);
                gameBoard.insert(player, new Point2D(5, 5));

                gameRunner = new AntGameRunner(gameBoard, player, Collections.singletonList(player));
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

    public BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }
}


