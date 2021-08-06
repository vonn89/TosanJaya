/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class VerifikasiController {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    private Main mainApp;
    private Stage stage;

    public void setMainApp(Main main, Stage stage) {
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        usernameField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                passwordField.requestFocus();
                Platform.runLater(() -> {
                    if (passwordField.isFocused() && !passwordField.getText().isEmpty()) {
                        passwordField.selectAll();
                    }
                });
            }
        });
        passwordField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.UP) {
                event.consume();
                usernameField.requestFocus();
                Platform.runLater(() -> {
                    if (usernameField.isFocused() && !usernameField.getText().isEmpty()) {
                        usernameField.selectAll();
                    }
                });
            }
        });
        usernameField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
                passwordField.selectAll();
            }
        });
        usernameField.requestFocus();
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
    }

    @FXML
    public void close() {
        mainApp.closeDialog(stage);
    }
}
