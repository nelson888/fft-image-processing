package com.tambapps.math.util;

import static org.junit.Assert.assertEquals;

import com.tambapps.math.array_2d.Complex2DArray;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageHolderConverterTest {

  @Test
  public void convertTest() throws IOException {
    //TODO
    /*
    BufferedImage image = ImageIO.read(ImageHolderConverterTest.class.getResource("/img.jpg"));
    System.err.println(image.getType());
    Complex2DArray array = ImageConverter.toArray(image);
    BufferedImage image2 = ImageConverter.fromArray(array);
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        int p1 = image.getRGB(j, i);
        int p2 = image2.getRGB(j, i);
        assertEquals(Integer.toString(p1, 16) +" != " + Integer.toString(p2, 16), p1, p2);
      }
    }*/
  }
}
