package com.tambapps.math.fourier.application;

import com.tambapps.math.fourier.application.controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FFTApplication extends Application {

  public static final  int MAX_FFT_THREADS = Runtime.getRuntime().availableProcessors() + 1;
  public static final ExecutorService FFT_EXECUTOR_SERVICE = Executors
      .newFixedThreadPool(FFTApplication.MAX_FFT_THREADS);
  public static final ExecutorService TASK_EXECUTOR_SERVICE = Executors
      .newCachedThreadPool();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(final Stage stage) throws Exception {
    setUserAgentStylesheet(STYLESHEET_MODENA);
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

  @Override
  public void stop() {
    FFT_EXECUTOR_SERVICE.shutdown();
    TASK_EXECUTOR_SERVICE.shutdown();
  }
}
