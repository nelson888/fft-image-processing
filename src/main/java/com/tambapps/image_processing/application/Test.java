package com.tambapps.image_processing.application;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.fft_1d.FFTAlgorithms;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import com.tambapps.math.util.ImageConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

  public static void main(String[] args) throws IOException {
    int maxThreads = 4;
    ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);
    FastFourierTransformer2D fft = new FastFourierTransformer2D(executorService, maxThreads);
    BufferedImage image = ImageIO.read(new File("/home/nfonkoua/Téléchargements/img.jpg"));
    Complex2DArray f = ImageConverter.toArrayGrayScale(image);
    System.out.println(image.getType());
    fft.transform(f, FFTAlgorithms.CT_RECURSIVE);
    ImageIO.write(ImageConverter.fromArrayGrayScale(f, BufferedImage.TYPE_3BYTE_BGR), "jpg", new File("/home/nfonkoua/Téléchargements/dft.jpg"));
    fft.inverse(f);
    ImageIO.write(ImageConverter.fromArrayGrayScale(f, BufferedImage.TYPE_3BYTE_BGR), "jpg", new File("/home/nfonkoua/Téléchargements/dft_reversed.jpg"));

    executorService.shutdown();
  }
}
