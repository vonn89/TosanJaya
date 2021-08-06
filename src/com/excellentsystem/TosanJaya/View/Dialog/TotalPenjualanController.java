/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.Function;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.rp;
import com.excellentsystem.TosanJaya.Model.Pelanggan;
import com.excellentsystem.TosanJaya.Model.PenjualanDetail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class TotalPenjualanController {

    @FXML
    private Label kategoriLabel;
    @FXML
    private Label namaPelangganLabel;
    @FXML
    private Label totalPenjualanlabel;
    @FXML
    public CheckBox pengirimanCheck;
    @FXML
    public Label ongkosKirimLabel;
    @FXML
    public Label diskonLabel;
    @FXML
    private Label grandtotallabel;
    @FXML
    public Label totalPembayaranLabel;
    @FXML
    public CheckBox printCheck;
    @FXML
    public Button saveButton;
    public Pelanggan pelanggan;
    private Main mainApp;
    private Stage stage;

    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                closeDialog();
            }
            if (event.getCode() == KeyCode.F5) {
                cariPelanggan();
            }
            if (event.getCode() == KeyCode.F6) {
                if (printCheck.isSelected()) {
                    printCheck.setSelected(false);
                } else if (!printCheck.isSelected()) {
                    printCheck.setSelected(true);
                }
            }
            if (event.getCode() == KeyCode.F10) {
                ubahOngkosKirim();
            }
            if (event.getCode() == KeyCode.F11) {
                ubahDiskon();
            }
            if (event.getCode() == KeyCode.F12) {
                ubahPembayaran();
            }
        });
    }

    public void setText(String kategori, String totalPenjualan) {
        namaPelangganLabel.setText("");
        kategoriLabel.setText(kategori);
        totalPenjualanlabel.setText(totalPenjualan);
        if (Double.parseDouble(ongkosKirimLabel.getText().replaceAll(",", "")) == 0) {
            pengirimanCheck.setSelected(false);
        }
        ongkosKirimLabel.setText("0");
        diskonLabel.setText("0");
        hitungTotal();
    }

    public void hitungTotal() {
        double total = Double.parseDouble(totalPenjualanlabel.getText().replaceAll(",", ""));
        double diskon = Double.parseDouble(diskonLabel.getText().replaceAll(",", ""));
        double ongkosKirim = Double.parseDouble(ongkosKirimLabel.getText().replaceAll(",", ""));
        grandtotallabel.setText(rp.format(total + ongkosKirim - diskon));
        totalPembayaranLabel.setText(grandtotallabel.getText());
    }

    @FXML
    public void cariPelanggan() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/CariPelanggan.fxml");
        CariPelangganController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.pelangganTable.setRowFactory(table -> {
            TableRow<Pelanggan> row = new TableRow<Pelanggan>() {
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        mainApp.closeDialog(child);
                        pelanggan = row.getItem();
                        namaPelangganLabel.setText(pelanggan.getNama());
                    }
                }
            });
            return row;
        });
        x.pelangganTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (x.pelangganTable.getSelectionModel().getSelectedItem() != null) {
                    mainApp.closeDialog(child);
                    pelanggan = x.pelangganTable.getSelectionModel().getSelectedItem();
                    namaPelangganLabel.setText(pelanggan.getNama());
                }
            }
        });
    }

    @FXML
    public void ubahDiskon() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/InputNumber.fxml");
        InputNumberController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setNumber(Double.parseDouble(diskonLabel.getText().replaceAll(",", "")));
        child.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                x.close();
            }
        });
        x.numberField.setOnAction((event) -> {
            x.close();
            Stage verifikasi = new Stage();
            FXMLLoader verLoader = mainApp.showDialog(mainApp.MainStage, verifikasi, "View/Dialog/Verifikasi.fxml");
            VerifikasiController verController = verLoader.getController();
            verController.setMainApp(mainApp, verifikasi);
            verController.passwordField.setOnAction((e) -> {
                verController.close();
                if (Function.verifikasi(verController.usernameField.getText(), verController.passwordField.getText(), "Diskon")) {
                    diskonLabel.setText(x.numberField.getText());
                    hitungTotal();
                } else {
                    mainApp.showWarning("Warning", "username & password masih salah \n"
                            + "atau otoritas tidak diijinkan");
                }
            });
        });
    }

    @FXML
    public void ubahOngkosKirim() {
        if (pengirimanCheck.isSelected()) {
            pengirimanCheck.setSelected(false);
            ongkosKirimLabel.setText("0");
            hitungTotal();
        } else if (!pengirimanCheck.isSelected()) {
            if (namaPelangganLabel.getText().equals("")) {
                mainApp.showWarning("Warning", "Pelanggan belum dipilih");
            } else {
                pengirimanCheck.setSelected(true);

                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/InputNumber.fxml");
                InputNumberController x = loader.getController();
                x.setMainApp(mainApp, child);
                x.setNumber(Double.parseDouble(ongkosKirimLabel.getText().replaceAll(",", "")));
                child.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        x.close();
                    }
                });
                x.numberField.setOnAction((event) -> {
                    ongkosKirimLabel.setText(x.numberField.getText());
                    hitungTotal();
                    x.close();
                });
            }
        }
    }

    @FXML
    private void ubahPembayaran() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/InputNumber.fxml");
        InputNumberController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setNumber(Double.parseDouble(totalPembayaranLabel.getText().replaceAll(",", "")));
        child.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                x.close();
            }
        });
        x.numberField.setOnAction((event) -> {
            if (Double.parseDouble(grandtotallabel.getText().replaceAll(",", "")) >= Double.parseDouble(x.numberField.getText().replaceAll(",", ""))) {
                totalPembayaranLabel.setText(x.numberField.getText());
                x.close();
            } else {
                mainApp.showWarning("Warning", "Pembayaran yang dibayar melebihi total penjualan !!");
            }
        });
    }

    public void closeDialog() {
        mainApp.closeDialog(stage);
    }

}
