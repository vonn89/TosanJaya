/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.DAO.BarangDAO;
import com.excellentsystem.TosanJaya.DAO.HargaDAO;
import com.excellentsystem.TosanJaya.DAO.KategoriBarangDAO;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import com.excellentsystem.TosanJaya.Model.Barang;
import com.excellentsystem.TosanJaya.Model.Harga;
import com.excellentsystem.TosanJaya.Model.KategoriBarang;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class DetailBarangController {

    @FXML
    private GridPane gridPane;
    @FXML
    public TextField kodeBarangField;
    @FXML
    public ComboBox<String> kodeKategoriCombo;
    @FXML
    public TextField kodeBarcodeField;
    @FXML
    public TextField namaBarangField;
    @FXML
    public TextField keteranganField;
    @FXML
    public TextField satuanDasarField;

    @FXML
    public Button saveButton;
    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Harga> hargaTable;
    @FXML
    private TableColumn<Harga, Number> qtyMinColumn;
    @FXML
    private TableColumn<Harga, Number> qtyMaksColumn;
    @FXML
    private TableColumn<Harga, Number> hargaColumn;

    private Main mainApp;
    private Stage stage;
    private ObservableList<String> allKategori = FXCollections.observableArrayList();
    public ObservableList<Harga> allHarga = FXCollections.observableArrayList();

    public void initialize() {
        qtyMinColumn.setCellValueFactory(cellData -> cellData.getValue().qtyMinProperty());
        qtyMinColumn.setCellFactory(col -> getTableCell(qty));
        qtyMaksColumn.setCellValueFactory(cellData -> cellData.getValue().qtyMaxProperty());
        qtyMaksColumn.setCellFactory(col -> getTableCell(qty));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory(col -> getTableCell(rp));

        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Harga Baru");
        addNew.setOnAction((ActionEvent e) -> {
            addNewHarga();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            hargaTable.refresh();
        });
        rowMenu.getItems().addAll(addNew, refresh);
        hargaTable.setContextMenu(rowMenu);
        hargaTable.setRowFactory(t -> {
            TableRow<Harga> row = new TableRow<Harga>() {
                @Override
                public void updateItem(Harga item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah Harga Baru");
                        addNew.setOnAction((ActionEvent e) -> {
                            addNewHarga();
                        });
                        MenuItem edit = new MenuItem("Ubah Harga");
                        edit.setOnAction((ActionEvent e) -> {
                            editHarga(item);
                        });
                        MenuItem delete = new MenuItem("Hapus Harga");
                        delete.setOnAction((ActionEvent e) -> {
                            allHarga.remove(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            hargaTable.refresh();
                        });
                        if (saveButton.isVisible()) {
                            rowMenu.getItems().addAll(addNew, edit, delete);
                        }
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && row.getItem() != null) {
                    if (saveButton.isVisible()) {
                        editHarga(row.getItem());
                    }
                }
            });
            return row;
        });
        kodeKategoriCombo.setItems(allKategori);
        hargaTable.setItems(allHarga);
    }

    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        getKategori();
    }

    private void getKategori() {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    List<KategoriBarang> kategori = KategoriBarangDAO.getAll(con);
                    allKategori.clear();
                    for (KategoriBarang temp : kategori) {
                        allKategori.add(temp.getKodeKategori());
                    }
                    return null;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    public void setNewBarang() {
        kodeBarangField.setText("New Barang");
        kodeKategoriCombo.getSelectionModel().clearSelection();
        kodeBarcodeField.setText("");
        namaBarangField.setText("");
        keteranganField.setText("");
        satuanDasarField.setText("");
        allHarga.clear();
    }

    public void setBarang(Barang b) {
        allHarga.addAll(b.getListHarga());
        kodeBarangField.setText(b.getKodeBarang());
        kodeBarcodeField.setText(b.getKodeBarcode());
        kodeKategoriCombo.getSelectionModel().select(b.getKodeKategori());
        namaBarangField.setText(b.getNamaBarang());
        keteranganField.setText(b.getKeterangan());
        satuanDasarField.setText(b.getSatuanDasar());
    }

    public void setBarang(String kodeBarang) {
        Task<Barang> task = new Task<Barang>() {
            @Override
            public Barang call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    Barang b = BarangDAO.get(con, kodeBarang);
                    b.setListHarga(HargaDAO.getAllByKodeBarang(con, b.getKodeBarang()));
                    return b;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            Barang b = task.getValue();
            if (b != null) {
                setBarang(b);
            } else {
                mainApp.showMessage(Modality.NONE, "Warning", "Barang tidak ditemukan");
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
        kodeBarangField.setDisable(true);
        kodeBarcodeField.setDisable(true);
        kodeKategoriCombo.setDisable(true);
        namaBarangField.setDisable(true);
        keteranganField.setDisable(true);
        satuanDasarField.setDisable(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);

        MenuItem deleteMenu = null;
        for (MenuItem m : hargaTable.getContextMenu().getItems()) {
            if (m.getText().equals("Tambah Harga Baru")) {
                deleteMenu = m;
            }
        }
        hargaTable.getContextMenu().getItems().remove(deleteMenu);

        gridPane.getRowConstraints().get(9).setMinHeight(0);
        gridPane.getRowConstraints().get(9).setPrefHeight(0);
        gridPane.getRowConstraints().get(9).setMaxHeight(0);
    }

    private void addNewHarga() {
//        Stage child = new Stage();
//        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/HargaBarang.fxml");
//        HargaBarangController x = loader.getController();
//        x.setMainApp(mainApp, child);
//        x.saveButton.setOnAction((event) -> {
//            if ("".equals(x.qtyMinField.getText()) || Double.parseDouble(x.qtyMinField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Qty Minimal masih kosong");
//            } else if ("".equals(x.qtyMaxField.getText()) || Double.parseDouble(x.qtyMaxField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Qty Maksimal masih kosong");
//            } else if ("".equals(x.hargaField.getText())) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga masih kosong");
//            } else {
//                Harga harga = new Harga();
//                harga.setKodeBarang(kodeBarangField.getText());
//                harga.setQtyMin(Double.parseDouble(x.qtyMinField.getText().replaceAll(",", "")));
//                harga.setQtyMax(Double.parseDouble(x.qtyMaxField.getText().replaceAll(",", "")));
//                harga.setHarga(Double.parseDouble(x.hargaField.getText().replaceAll(",", "")));
//                allHarga.add(harga);
//                hargaTable.refresh();
//                x.close();
//            }
//        });
    }

    private void editHarga(Harga h) {
//        Stage child = new Stage();
//        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/HargaBarang.fxml");
//        HargaBarangController x = loader.getController();
//        x.setMainApp(mainApp, child);
//        x.qtyMinField.setText(qty.format(h.getQtyMin()));
//        x.qtyMaxField.setText(qty.format(h.getQtyMax()));
//        x.hargaField.setText(rp.format(h.getHarga()));
//        x.saveButton.setOnAction((event) -> {
//            if ("".equals(x.qtyMinField.getText()) || Double.parseDouble(x.qtyMinField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Qty Minimal masih kosong");
//            } else if ("".equals(x.qtyMaxField.getText()) || Double.parseDouble(x.qtyMaxField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Qty Maksimal masih kosong");
//            } else if ("".equals(x.hargaField.getText())) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga masih kosong");
//            } else {
//                Harga harga = new Harga();
//                harga.setKodeBarang(kodeBarangField.getText());
//                harga.setQtyMin(Double.parseDouble(x.qtyMinField.getText().replaceAll(",", "")));
//                harga.setQtyMax(Double.parseDouble(x.qtyMaxField.getText().replaceAll(",", "")));
//                harga.setHarga(Double.parseDouble(x.hargaField.getText().replaceAll(",", "")));
//                allHarga.remove(h);
//                allHarga.add(harga);
//                hargaTable.refresh();
//                x.close();
//            }
//        });
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

}
