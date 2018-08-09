package com.tambapps.image_processing.application;

import com.tambapps.image_processing.application.model.ColoredFourierImage;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ColoredFourierImageTest {

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.out.println("You must provide an image path");
      return;
    }
    final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS + 1);
    final FastFourierTransformer2D transformer =
        new FastFourierTransformer2D(executorService, MAX_THREADS);
    File imageFile = new File(args[0]);
    BufferedImage image = ImageIO.read(imageFile);
    ColoredFourierImage fourierImage = new ColoredFourierImage(image, imageFile.getName().endsWith(".png"));
    System.out.println("Computing transform");
    fourierImage.computeTransform(transformer);
    File directory = imageFile.getParentFile();
    System.out.println("Writing transform");
    ImageIO.write(fourierImage.getTransform(), "jpg",
        new File(directory, "colored_ft_" + imageFile.getName()));
    System.out.println("Computing inverse");
    fourierImage.computeInverse(transformer);
    System.out.println("Writing image");
    ImageIO.write(fourierImage.getInverse(), "jpg",
        new File(directory, "colored_inverse_" + imageFile.getName()));
    System.out.println("Finished");
    executorService.shutdown();
  }
}
