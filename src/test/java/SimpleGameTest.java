import actor.Person;
import actor.Board2DActor;
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
        Person person = new Person(Direction.NORTH);
        actors.add(person);
        gameBoard.insert(person, new Point2D(5, 5));

        PersonGameRunner runner = new PersonGameRunner(gameBoard, person, actors);
        runner.turn();
    }
}
