package com.tambapps.math.fourier;

import com.tambapps.math.fourier.ui.stage.FFTStage;
import com.tambapps.math.fourier.ui.ScreenDimensions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FFTApplication extends Application {

  private static final String START_LAYOUT = "/start_layout.fxml",
      IMAGES_PICKER_LAYOUT = "/images_picker_layout.fxml",
      IMAGE_PROCESSING_LAYOUT = "/image_processing_layout.fxml";

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(final Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource(START_LAYOUT));
    Parent fftRoot = FXMLLoader.load(getClass().getResource(IMAGES_PICKER_LAYOUT));
    Parent ipsParent = FXMLLoader.load(getClass().getResource(IMAGE_PROCESSING_LAYOUT));
    stage.setTitle("Fast Fourier Transform App");
    Scene scene = new Scene(root, ScreenDimensions.WIDTH * 0.5, ScreenDimensions.HEIGHT * 0.4);
    Button button = (Button) scene.lookup("#start");
    button.setOnAction(actionEvent -> {
      stage.close();
      new FFTStage(fftRoot, ScreenDimensions.WIDTH * 0.75, ScreenDimensions.HEIGHT * 0.75, ipsParent).show();
    });
    stage.setScene(scene);
    stage.show();
  }

}
