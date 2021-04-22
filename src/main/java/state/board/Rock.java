package state.board;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Rock implements BoardObject {

    @Override
    public boolean isBlocking() {
        return true;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        return loadImage("/Bug.png");
    }
}
