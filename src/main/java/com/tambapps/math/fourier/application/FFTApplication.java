package com.tambapps.math.fourier.application;

import com.tambapps.math.fourier.application.controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//TODO USER JAVAFX SCENE BUILDER TO MAKE UI
public class FFTApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(final Stage stage) throws Exception {
    stage.setTitle("Fourier Image Processing");

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(FFTApplication.class.getResource("/view/home.fxml"));
    VBox rootLayout = loader.load();

    Scene scene = new Scene(rootLayout);
    stage.setScene(scene);
    HomeController controller = loader.getController();
    controller.setStage(stage);

    stage.show();
  }

}
