package state.board;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bread implements BoardObject {
    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public BufferedImage getImage() throws IOException {
        return loadImage("/Bread.png");
    }
}
