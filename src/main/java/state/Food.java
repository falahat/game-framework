package state;

public class Food implements BoardObject {
    @Override
    public boolean isBlocking() {
        return false;
    }
}
