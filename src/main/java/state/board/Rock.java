package state.board;

public class Rock implements BoardObject {

    @Override
    public boolean isBlocking() {
        return true;
    }
}
