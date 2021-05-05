import actor.Board2DActor;
import actor.Skeleton;
import actor.SmartPerson;
import org.junit.jupiter.api.Test;
import runner.PersonGameRunner;
import state.Direction;
import state.Point2D;
import state.board.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class SimpleGameTest {

    @Test
    public void simpleGame() {
        GameBoard gameBoard = new GameBoard(10, 10);

        List<Board2DActor> actors = new ArrayList<>();
        SmartPerson person = new SmartPerson(Direction.NORTH);
        Skeleton enemy = new Skeleton(Direction.NORTH, person);

        actors.add(person);
        gameBoard.insert(person, new Point2D(5, 5));

        PersonGameRunner runner = new PersonGameRunner(gameBoard, person, enemy, actors);
        runner.turn();
    }
}
