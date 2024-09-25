package org.openjfx;

import javafx.scene.control.*;

public class DisplayMessage {
  Label messageLabel;
  
  public DisplayMessage(Label messageLabel){
    this.messageLabel = messageLabel;
  }

  public void showMessage(String message){
    messageLabel.setText(message);
  }
}
