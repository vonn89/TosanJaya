/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.Function;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.rp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class InputNumberController  {

    @FXML public TextField numberField;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        Function.setNumberField(numberField, rp);
        numberField.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.UP) {
                event.consume();
                numberField.requestFocus();
                Platform.runLater(() -> {
                    if (numberField.isFocused() && !numberField.getText().isEmpty()) {
                        numberField.selectAll();
                    }
                });
            }
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                numberField.requestFocus();
                Platform.runLater(() -> {
                    if (numberField.isFocused() && !numberField.getText().isEmpty()) {
                        numberField.selectAll();
                    }
                });
            }
        });
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setNumber(double number){
        numberField.setText(rp.format(number));
    }
    @FXML
    public void close() {
        mainApp.closeDialog(stage);
    }
}
