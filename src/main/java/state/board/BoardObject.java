package state.board;

import runner.Drawable;

public interface BoardObject extends Drawable {
    boolean isBlocking();
}
