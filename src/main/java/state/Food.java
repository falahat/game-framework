package state;

import state.board.BoardObject;

public class Food implements BoardObject {
    @Override
    public boolean isBlocking() {
        return false;
    }
}
