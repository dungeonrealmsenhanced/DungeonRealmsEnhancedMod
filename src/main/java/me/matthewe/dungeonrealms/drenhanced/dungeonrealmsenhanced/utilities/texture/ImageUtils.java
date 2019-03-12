package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by Matthew E on 12/31/2018 at 5:23 PM for the project DungeonRealmsDREnhanced
 */
public class ImageUtils {
    /** Used to read Input Stream image (for displaying window icon */
    public static ByteBuffer readImage(InputStream par1File) throws IOException {

        BufferedImage bufferedimage = ImageIO.read(par1File);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[]) null, 0,
                bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];
            bytebuffer.putInt(k << 8 | k >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }
}
