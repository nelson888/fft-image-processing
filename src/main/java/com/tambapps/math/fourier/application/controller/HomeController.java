package com.tambapps.math.fourier.application.controller;

import com.tambapps.math.fourier.application.FFTApplication;
import com.tambapps.math.fourier.application.model.ImageTask;
import com.tambapps.math.fourier.application.ui.view.MyImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class HomeController {

  private ObservableList<ImageTask> imageTasks = FXCollections.observableArrayList();

  @FXML
  private GridPane gridPane;
  @FXML
  private Button addButton;
  private Stage stage;
  private FileChooser fileChooser;
  private int nbImages;

  @FXML
  private void initialize() {
    fileChooser = new FileChooser();
    nbImages = 0;
    fileChooser.setTitle("Pick an image");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files",
        "*.png", "*.jpg", "*.gif", "*.jpeg", "*.bmp"));

    addButton.setOnAction((event -> pickImages()));

  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  private void pickImages() {
    List<File> files = fileChooser.showOpenMultipleDialog(stage);
    if (files == null) {
      return;
    }

    for (int i = nbImages; i < files.size(); i++) {
      BufferedImage image;
      try {
        image = ImageIO.read(files.get(i));
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("handle me better", e);
      }

      ImageTask imageTask = new ImageTask(image);
      imageTasks.add(imageTask);

      MyImageView imageView = createImageView(image);
      gridPane.add(imageView, nbImages % 4, nbImages / 4); //TODO check when nbImages == 8
      gridPane.getChildren().remove(addButton);
      nbImages++;
    }
  }

  private MyImageView createImageView(BufferedImage image) {
    MyImageView imageView = new MyImageView();
    imageView.setFitWidth(stage.getWidth() / 5d);
    imageView.setFitHeight(stage.getHeight() / 5d);
    imageView.setImage(SwingFXUtils.toFXImage(image, null));

    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      System.out.println("Tile pressed ");
      event.consume();
    });
    return imageView;
  }

  private void showImageTaskView(String title) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(FFTApplication.class.getResource("/view/image_task_view.fxml"));
    Stage stage = new Stage();
    stage.setTitle(title);


    //      new FFTStage(fftRoot, ScreenDimensions.WIDTH * 0.75, ScreenDimensions.HEIGHT * 0.75, ipsParent).show();
  }
}
