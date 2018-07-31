package com.tambapps.math.fourier.application.ui.stage;

import com.tambapps.math.array_2d.Complex2DArray;
import com.tambapps.math.fourier.application.ui.view.MyImageView;
import com.tambapps.math.fourier.fft_2d.FastFourierTransformer2D;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProcessingStage extends Stage {

  private GridPane root;
  private MyImageView imageView;
  private ImageView startImageView;

  private static final int NB_THREADS = 5;
  private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(NB_THREADS);
  private static final FastFourierTransformer2D FF_TRANSFORMER =
      new FastFourierTransformer2D(EXECUTOR_SERVICE, NB_THREADS);

  public ImageProcessingStage(MyImageView imageView, Scene scene) {
    this.imageView = imageView;
    setScene(scene);
    root = (GridPane) scene.lookup("#container");
    startImageView = new ImageView(imageView.getImage());
    startImageView.setFitWidth(getWidth() / 4d);

    root.add(startImageView, 1, 0);

    Button button = (Button) scene.lookup("#compute_fft");
    button.setOnAction(event -> {
      //TODO
    });
  }

}
