/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.DAO.SupplierDAO;
import com.excellentsystem.TosanJaya.Function;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.rp;
import com.excellentsystem.TosanJaya.Model.Supplier;
import java.sql.Connection;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class DetailSupplierController {

    @FXML
    private GridPane gridPane;
    @FXML
    public TextField namaField;
    @FXML
    public TextArea alamatField;
    @FXML
    public TextField noTelpField;

    @FXML
    public Button saveButton;

    private Main mainApp;
    private Stage stage;

    public void initialize() {
        namaField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                alamatField.requestFocus();
                Platform.runLater(() -> {
                    if (alamatField.isFocused() && !alamatField.getText().isEmpty()) {
                        alamatField.selectAll();
                    }
                });
            }
        });
        alamatField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.UP) {
                event.consume();
                namaField.requestFocus();
                Platform.runLater(() -> {
                    if (namaField.isFocused() && !namaField.getText().isEmpty()) {
                        namaField.selectAll();
                    }
                });
            }
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                noTelpField.requestFocus();
                Platform.runLater(() -> {
                    if (noTelpField.isFocused() && !noTelpField.getText().isEmpty()) {
                        noTelpField.selectAll();
                    }
                });
            }
        });
        noTelpField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.UP) {
                event.consume();
                alamatField.requestFocus();
                Platform.runLater(() -> {
                    if (alamatField.isFocused() && !alamatField.getText().isEmpty()) {
                        alamatField.selectAll();
                    }
                });
            }
        });
        namaField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                alamatField.requestFocus();
                alamatField.selectAll();
            }
        });
        alamatField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                noTelpField.requestFocus();
                noTelpField.selectAll();
            }
        });
        noTelpField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                saveButton.fire();
            }
        });
    }

    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
        namaField.requestFocus();
    }

    public void setNewSupplier() {
        namaField.setText("");
        alamatField.setText("");
        noTelpField.setText("");
    }

    public void setSupplier(Supplier b) {
        namaField.setText(b.getNama());
        alamatField.setText(b.getAlamat());
        noTelpField.setText(b.getNoTelp());
    }

    public void setSupplier(String kodeSupplier) {
        Task<Supplier> task = new Task<Supplier>() {
            @Override
            public Supplier call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    return SupplierDAO.get(con, kodeSupplier);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            Supplier b = task.getValue();
            if (b != null) {
                setSupplier(b);
            } else {
                mainApp.showMessage(Modality.NONE, "Warning", "Supplier tidak ditemukan");
            }
        });
        task.setOnFailed((e) -> {
            task.getException().printStackTrace();
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    public void setViewOnly() {
        namaField.setDisable(true);
        alamatField.setDisable(true);
        noTelpField.setDisable(true);
        saveButton.setVisible(false);

        gridPane.getRowConstraints().get(7).setMinHeight(0);
        gridPane.getRowConstraints().get(7).setPrefHeight(0);
        gridPane.getRowConstraints().get(7).setMaxHeight(0);
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

}
