package com.tambapps.math.fourier.application.ui.stage;

import com.tambapps.math.fourier.application.ui.view.MyImageView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FFTStage extends Stage {

  private final FileChooser fileChooser;
  private final Parent ipsParent;
  private int nbImages;

  public FFTStage(Parent root, double width, double height,
                  Parent ipsParent) {
    this.ipsParent = ipsParent;
    Scene scene = new Scene(root, width, height);
    fileChooser = new FileChooser();
    fileChooser.setTitle("Pick an image");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files",
        "*.png", "*.jpg", "*.gif", "*.jpeg", "*.bmp"));

    setScene(scene);

    Button button = (Button) scene.lookup("#pick_image");
    GridPane grid = (GridPane) scene.lookup("#container");
    button.setOnAction((event) -> pickImages(grid));
  }

  private void pickImages(GridPane grid) {
    List<File> files = fileChooser.showOpenMultipleDialog(this);
    if (files == null) {
      return;
    }
    for (int i = 0; i < files.size(); i++) {
      MyImageView imageView = new MyImageView();
      try {
        BufferedImage image = ImageIO.read(files.get(i));
        imageView.setImage(image);
      } catch (IOException e) {
        e.printStackTrace(); //TODO handle it better
        throw new RuntimeException(e);
      }

      imageView.setFitWidth(getWidth() / 6d);
      imageView.setFitHeight(imageView.getFitWidth());
      imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        showImageProcessingStage(imageView);
        event.consume();
      });
      grid.add(imageView, nbImages + i + 1, 1);
    }
    nbImages += files.size();
  }

  private void showImageProcessingStage(MyImageView imageView) {
    ImageProcessingStage ips = new ImageProcessingStage(imageView,
        new Scene(ipsParent, getWidth(), getHeight()));
    ips.show();
  }

}
