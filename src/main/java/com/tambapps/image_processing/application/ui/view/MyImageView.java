package com.tambapps.image_processing.application.ui.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

public class MyImageView extends ImageView {
  private static final Color SHADOW_COLOR = Color.color(0d, 12d / 255d, 155d / 255d);
  private BufferedImage originImage;

  public MyImageView() {
    super();

    setPreserveRatio(true);
    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.DARKSLATEGRAY);
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
