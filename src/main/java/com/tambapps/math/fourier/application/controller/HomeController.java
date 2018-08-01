package com.tambapps.math.fourier.application.controller;

import com.tambapps.math.fourier.application.FFTApplication;
import com.tambapps.math.fourier.application.model.ImageTask;
import com.tambapps.math.fourier.application.ui.view.MyImageView;
import com.tambapps.math.util.ImageConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class HomeController {

  private final ObservableList<ImageTask> imageTasks = FXCollections.observableArrayList();

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
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void pickImages(ActionEvent event) {
    List<File> files = fileChooser.showOpenMultipleDialog(stage);
    if (files == null) {
      return;
    }

    gridPane.getChildren().remove(addButton);
    for (int i = nbImages; i < files.size(); i++) {
      File file = files.get(i);
      BufferedImage image;
      try {
        image = ImageIO.read(file);
      } catch (IOException e) {
        throw new RuntimeException("Couldn't read image", e);
      }

      ImageTask imageTask = new ImageTask(ImageConverter.copy(image));
      imageTasks.add(imageTask);

      MyImageView imageView = createImageView(image);
      imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,
          e -> showImageTaskView(imageTask, file.getName()));
      gridPane.add(imageView, nbImages % 4, nbImages / 4); //TODO check when nbImages == 8
      imageTask.processedImageProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
          imageView.setImage(SwingFXUtils.toFXImage(newValue, null));
        }
      });
      nbImages++;
    }

    if (nbImages < FFTApplication.MAX_FFT_THREADS) {
      gridPane.add(addButton, nbImages % 4, nbImages / 4);
    }
  }

  private MyImageView createImageView(BufferedImage image) {
    MyImageView imageView = new MyImageView();
    imageView.setFitWidth(stage.getWidth() / 5d);
    imageView.setFitHeight(stage.getHeight() / 5d);
    imageView.setImage(SwingFXUtils.toFXImage(image, null));
    return imageView;
  }

  private void showImageTaskView(ImageTask imageTask, String title) {
    FXMLLoader loader = new FXMLLoader();
    String path;
    BufferedImage image = imageTask.getImage();
    double width = image.getWidth();
    double height = image.getHeight();
    if (width / height < 0.85) {
      path = "/view/image_task_view_vertical.fxml"; //TODO modify scene with gluon scene editor
    } else {
      path = "/view/image_task_view_horizontal.fxml";
    }
    loader.setLocation(FFTApplication.class.getResource(path));
    Stage stage = new Stage();
    stage.setTitle(title);
    AnchorPane root;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load root", e);
    }

    Scene scene = new Scene(root);
    stage.setScene(scene);
    ImageTaskController controller = loader.getController();
    controller.setImageTask(imageTask);

    stage.show();
  }
}
