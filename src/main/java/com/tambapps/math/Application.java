package com.tambapps.math;

import com.tambapps.math.complex.Complex;
import com.tambapps.math.fft_2d.FastFourier;
import com.tambapps.math.util.ImageConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Application {

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.out.println("give one image path and output path");
      return;
    }

    BufferedImage image = ImageIO.read(new File(args[0]));
    System.out.println("FINISHED READING");
    System.out.println("TRANSFORMING");
    Complex[][] ft = FastFourier.transform(ImageConverter.toArray(image));
    System.out.println("FINISHED TRANSFORMING");
    System.out.println("INVERSING...");
    ft = FastFourier.inverse(ft);
    System.out.println("FINISHED INVERSING\nWRITTING...");
    ImageIO.write(ImageConverter.fromArray(ft, BufferedImage.TYPE_INT_RGB), "jpg", new File(args[1] + "test_2.jpg"));
    System.out.println("FINISHED EVEYTHING");
  }

}
