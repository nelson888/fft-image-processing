package com.tambapps.math.fourier.ui.stage;

import com.tambapps.math.fourier.ui.view.MyImageView;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ImageProcessingStage extends Stage {

  private GridPane root;
  private MyImageView imageView;
  private ImageView startImageView;

  public ImageProcessingStage(MyImageView imageView, Scene scene) {
    this.imageView = imageView;
    setScene(scene);
    root = (GridPane) scene.lookup("#container");
    startImageView = new ImageView(imageView.getImage());
    startImageView.setFitWidth(getWidth() / 4d);

    root.add(startImageView, 1, 0);
  }

}
