package com.tambapps.math.fourier.application.controller;

import com.tambapps.math.array_2d.Complex2DArray;
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
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
  private Stage imageTaskStage = new Stage();

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
    gridPane.widthProperty().addListener((obs, oldVal, newVal) -> {
      for (Node node : gridPane.getChildren()) {
        if (node instanceof MyImageView) {
          ((MyImageView) node).setFitWidth(gridPane.getWidth() / 5d);
        }
      }
    });

    gridPane.heightProperty().addListener((obs, oldVal, newVal) -> {
      for (Node node : gridPane.getChildren()) {
        if (node instanceof MyImageView) {
          ((MyImageView) node).setFitHeight(gridPane.getHeight() / 3d);
        }
      }
    });
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void pickImages(ActionEvent event) {
    if (nbImages == 8) {
      maxImagesDialog();
      return;
    }
    List<File> files = fileChooser.showOpenMultipleDialog(stage);
    if (files == null) {
      return;
    }

    gridPane.getChildren().remove(addButton);
    for (File file : files) {
      if (nbImages == 8) {
        maxImagesDialog();
        return;
      }
      BufferedImage image;
      try {
        image = ImageIO.read(file);
      } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Couldn't read image");
        alert.setContentText(String
            .format("An error occurred while reading image %s. Please, retry.", file.getName()));
        alert.show();
        continue;
      }

      BufferedImage originalImage = ImageConverter.copy(image);
      ImageTask imageTask = new ImageTask();
      imageTask.setImage(Complex2DArray.immutableCopy(ImageConverter.toArray(image)));
      imageTasks.add(imageTask);

      MyImageView imageView = createImageView(image);
      imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,
          e -> showImageTaskView(imageTask, image, file.getName() + String
              .format(" (%d x %d)", originalImage.getWidth(), originalImage.getHeight())));
      gridPane.add(imageView, nbImages % 4, nbImages / 4);
      GridPane.setHalignment(imageView, HPos.CENTER);
      GridPane.setValignment(imageView, VPos.CENTER);

      imageTask.processedImageProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
          imageView.setImage(SwingFXUtils.toFXImage(ImageConverter.fromArray(newValue), null));
        }
      });
      nbImages++;
    }

    if (nbImages < 8) {
      gridPane.add(addButton, nbImages % 4, nbImages / 4);
    }
  }

  private void maxImagesDialog() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Max number of images reached");
    alert.setContentText("You cannot have more than 8 images.");
    alert.show();
  }

  private MyImageView createImageView(BufferedImage image) {
    MyImageView imageView = new MyImageView();
    imageView.setFitWidth(stage.getWidth() / 5d);
    imageView.setFitHeight(stage.getHeight() / 3d);
    imageView.setImage(SwingFXUtils.toFXImage(image, null));

    return imageView;
  }

  private void showImageTaskView(ImageTask imageTask, BufferedImage image, String title) {
    FXMLLoader loader = new FXMLLoader();
    String path;

    double width = image.getWidth();
    double height = image.getHeight();
    if (width / height > 0.85) {
      path = "/view/image_task_view_vertical.fxml";
    } else {
      path = "/view/image_task_view_horizontal.fxml";
    }
    loader.setLocation(FFTApplication.class.getResource(path));
    if (imageTaskStage == null) {
      imageTaskStage = new Stage();
    } else {
      imageTaskStage.close();
    }
    imageTaskStage.setTitle(title);
    AnchorPane root;
    try {
      root = loader.load();
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load root", e);
    }

    Scene scene = new Scene(root);
    imageTaskStage.setScene(scene);
    ImageTaskController controller = loader.getController();
    controller.setImageTask(imageTask);

    imageTaskStage.show();
  }
}
