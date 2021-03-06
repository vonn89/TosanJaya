/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.sistem;
import static com.excellentsystem.TosanJaya.Main.timeout;
import com.excellentsystem.TosanJaya.Service.Service;
import java.sql.Connection;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class PengaturanUmumController {

    @FXML
    private TextField namaField;
    @FXML
    private TextField alamatField;
    @FXML
    private TextField noTelpField;
    @FXML
    private TextField websiteField;
    @FXML
    private TextField printerField;
    @FXML
    private TextArea footnoteField;

    private Main mainApp;
    private Stage stage;

    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        namaField.setText(sistem.getNama());
        alamatField.setText(sistem.getAlamat());
        noTelpField.setText(sistem.getNoTelp());
        websiteField.setText(sistem.getWebsite());
        printerField.setText(sistem.getPrinter());
        footnoteField.setText(sistem.getFootnote());
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

    @FXML
    private void saveData() {
        Task<String> task = new Task<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(timeout);
                try (Connection con = Koneksi.getConnection()) {
                    sistem.setNama(namaField.getText());
                    sistem.setAlamat(alamatField.getText());
                    sistem.setNoTelp(noTelpField.getText());
                    sistem.setWebsite(websiteField.getText());
                    sistem.setPrinter(printerField.getText());
                    sistem.setFootnote(footnoteField.getText());
                    return Service.savePengaturanUmum(con, sistem);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            String status = task.getValue();
            if (status.equals("true")) {
                close();
                MessageController controller = mainApp.showMessage(Modality.APPLICATION_MODAL, "Confirmation",
                        "New settings saved successfully,\nto take effect please restart program");
                controller.OK.setOnAction((ActionEvent event) -> {
                    try {
                        mainApp.MainStage.close();
                        mainApp.start(new Stage());
                    } catch (Exception ex) {
                        System.exit(0);
                    }
                });
            } else {
                mainApp.showMessage(Modality.NONE, "Failed", status);
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
}
