package com.tambapps.math.fourier.ui.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.image.BufferedImage;

public class MyImageView extends ImageView {

  private BufferedImage originImage;

  public MyImageView() {
    super();

    setPreserveRatio(true);
    DropShadow shadow = new DropShadow();
    addEventHandler(MouseEvent.MOUSE_ENTERED,
        e -> setEffect(shadow));
    addEventHandler(MouseEvent.MOUSE_EXITED,
        e -> setEffect(null));
  }

  public BufferedImage getOriginImage() {
    return originImage;
  }

  public void setImage(BufferedImage image) {
    this.originImage = image;
    setImage(SwingFXUtils.toFXImage(image, null));
  }
}
