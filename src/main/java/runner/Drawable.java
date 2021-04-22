package runner;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface Drawable {

    /**
     * @param g - javax graphics object which will be drawn to
     * @param x - The x coordinate of the top-left corner of the drawn image
     * @param y - The y coordinate of the top-left corner of the drawn image
     */
    default void render(Graphics g, int x, int y) {
        try {
            BufferedImage toDraw = getImage();
//            toDraw = Thumbnails.of(toDraw).size(2*toDraw.getWidth(), 2*toDraw.getHeight()).asBufferedImage();

            g.drawImage(toDraw, x, y, null);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle the error
        }
    }

    BufferedImage getImage() throws IOException;

    default BufferedImage loadImage(String filename) throws IOException {
        return ImageIO.read(getClass().getResource(filename));
    }
}
