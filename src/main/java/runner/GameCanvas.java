package runner;

import actor.Board2DActor;
import actor.Person;
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
import java.util.List;

// credit to https://code-knowledge.com/java-create-game-introduction/
public class GameCanvas extends Canvas implements Runnable {

    public static final int NUM_TILES = 16;
    public static final int TILE_SIZE = 50;
    public static final int WIDTH = NUM_TILES * TILE_SIZE;
    public static final int HEIGHT = NUM_TILES * TILE_SIZE;
    public static final int SCALE = 1;
    public final String TITLE = "Game Runner";

    private boolean running = false;
    private Thread thread;

    AntGameRunner gameRunner;

    public void init(){
        requestFocus();
        GameBoard gameBoard = new GameBoard(10, 10);

        List<Board2DActor> actors = new ArrayList<>();
        Person ant = new Person(Direction.NORTH);
        actors.add(ant);

        gameBoard.insert(ant, new Point2D(5, 5));

        gameBoard.insert(new Rock(), new Point2D(7, 6));
        gameBoard.insert(new Rock(), new Point2D(6, 6));
        gameBoard.insert(new Rock(), new Point2D(5, 6));
        gameBoard.insert(new Rock(), new Point2D(4, 6));
        gameBoard.insert(new Rock(), new Point2D(3, 6));
        gameBoard.insert(new Rock(), new Point2D(2, 6));


        gameBoard.insert(new Bread(), new Point2D(7, 5));
        gameBoard.insert(new Bread(), new Point2D(6, 5));
        gameBoard.insert(new Bread(), new Point2D(5, 5));
        gameBoard.insert(new Bread(), new Point2D(4, 5));
        gameBoard.insert(new Bread(), new Point2D(3, 5));
        gameBoard.insert(new Bread(), new Point2D(2, 5));

        gameRunner = new AntGameRunner(gameBoard, actors);
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

        while(running){
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
        g.fillRect(0, 0, WIDTH, HEIGHT);
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


