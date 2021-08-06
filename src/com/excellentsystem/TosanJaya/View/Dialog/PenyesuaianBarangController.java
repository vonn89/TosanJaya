/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.Function;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import com.excellentsystem.TosanJaya.Model.Barang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class PenyesuaianBarangController {

    @FXML
    public ComboBox<String> jenisPenyesuaianCombo;
    @FXML
    public TextField kodeBarangField;
    @FXML
    private TextField namaBarangField;
    @FXML
    public TextField qtyField;
    @FXML
    public TextField nilaiField;
    @FXML
    public TextField satuanDasarField;

    @FXML
    public TextArea catatanField;
    @FXML
    public Button saveButton;

    private Stage stage;
    private Main mainApp;
    private ObservableList<String> listJenisPenyesuaian = FXCollections.observableArrayList();

    public void initialize() {
        Function.setNumberField(qtyField, qty);
        Function.setNumberField(nilaiField, rp);
        jenisPenyesuaianCombo.setItems(listJenisPenyesuaian);
    }

    public void setMainApp(Main main, Stage stage) {
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        listJenisPenyesuaian.clear();
        listJenisPenyesuaian.add("Penambahan Stok Barang");
        listJenisPenyesuaian.add("Pengurangan Stok Barang");
        jenisPenyesuaianCombo.getSelectionModel().selectFirst();
    }

    public void setBarang(Barang b) {
        kodeBarangField.setText(b.getKodeBarang());
        namaBarangField.setText(b.getNamaBarang());
        satuanDasarField.setText(b.getSatuanDasar());
        qtyField.setText("1");
        nilaiField.setText("0");
    }

    @FXML
    public void cariBarang() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/CariBarang.fxml");
        CariBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setCari();
        x.barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        mainApp.closeDialog(child);
                        Barang b = row.getItem();
                        setBarang(b);
                    }
                }
            });
            return row;
        });
        x.barangTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (x.barangTable.getSelectionModel().getSelectedItem() != null) {
                    mainApp.closeDialog(child);
                    setBarang(x.barangTable.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }
}
