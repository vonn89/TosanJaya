/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View;

import com.excellentsystem.TosanJaya.DAO.BarangDAO;
import com.excellentsystem.TosanJaya.DAO.HargaDAO;
import com.excellentsystem.TosanJaya.DAO.KategoriBarangDAO;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.timeout;
import com.excellentsystem.TosanJaya.Model.Barang;
import com.excellentsystem.TosanJaya.Model.Harga;
import com.excellentsystem.TosanJaya.Model.KategoriBarang;
import com.excellentsystem.TosanJaya.Service.Service;
import com.excellentsystem.TosanJaya.View.Dialog.DetailBarangController;
import com.excellentsystem.TosanJaya.View.Dialog.ImportBarangController;
import com.excellentsystem.TosanJaya.View.Dialog.MessageController;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class DataBarangController {

    @FXML
    private TableView<Barang> barangTable;
    @FXML
    private TableColumn<Barang, String> kodeKategoriColumn;
    @FXML
    private TableColumn<Barang, String> kodeBarangColumn;
    @FXML
    private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML
    private TableColumn<Barang, String> namaBarangColumn;
    @FXML
    private TableColumn<Barang, String> keteranganColumn;
    @FXML
    private TableColumn<Barang, String> satuanDasarColumn;
    @FXML
    private TableColumn<Barang, Number> stokColumn;
    @FXML
    private TableColumn<Barang, Number> keepColumn;
    @FXML
    private TableColumn<Barang, Number> orderColumn;

    @FXML
    private Label totalQtyLabel;
    @FXML
    private ComboBox<String> kategoriCombo;
    @FXML
    private TextField searchField;
    private Main mainApp;
    private ObservableList<String> allKategori = FXCollections.observableArrayList();
    private ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private ObservableList<Barang> filterData = FXCollections.observableArrayList();

    public void initialize() {
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        kodeBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarangProperty());
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        satuanDasarColumn.setCellValueFactory(cellData -> cellData.getValue().satuanDasarProperty());
        stokColumn.setCellValueFactory(cellData -> cellData.getValue().stokProperty());
        stokColumn.setCellFactory((c) -> getTableCell(qty));
        keepColumn.setCellValueFactory(cellData -> cellData.getValue().keepProperty());
        keepColumn.setCellFactory((c) -> getTableCell(qty));
        orderColumn.setCellValueFactory(cellData -> cellData.getValue().orderProperty());
        orderColumn.setCellFactory((c) -> getTableCell(qty));

        allBarang.addListener((ListChangeListener.Change<? extends Barang> change) -> {
            searchBarang();
        });
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarang();
        });
        filterData.addAll(allBarang);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Barang Baru");
        addNew.setOnAction((ActionEvent e) -> {
            newBarang();
        });
        MenuItem importBarang = new MenuItem("Import Data Barang");
        importBarang.setOnAction((ActionEvent e) -> {
            importBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            getBarang();
        });
        rowMenu.getItems().addAll(addNew, importBarang, refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah Barang Baru");
                        addNew.setOnAction((ActionEvent e) -> {
                            newBarang();
                        });
                        MenuItem edit = new MenuItem("Ubah Barang");
                        edit.setOnAction((ActionEvent e) -> {
                            updateBarang(item);
                        });
                        MenuItem ubahHarga = new MenuItem("Ubah Harga");
                        ubahHarga.setOnAction((ActionEvent e) -> {
                            ubahHarga(item);
                        });
                        MenuItem delete = new MenuItem("Hapus Barang");
                        delete.setOnAction((ActionEvent e) -> {
                            deleteBarang(item);
                        });
                        MenuItem importBarang = new MenuItem("Import Data Barang");
                        importBarang.setOnAction((ActionEvent e) -> {
                            importBarang();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getBarang();
                        });
                        rowMenu.getItems().addAll(addNew, edit, ubahHarga, delete, new SeparatorMenuItem(), importBarang, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && row.getItem() != null) {
                    ubahHarga(row.getItem());
                }
            });
            return row;
        });
        kategoriCombo.setItems(allKategori);
        barangTable.setItems(filterData);
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                searchField.requestFocus();
                searchField.selectAll();
            }
        });
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        getKategori();
    }

    private void getKategori() {
        Task<List<KategoriBarang>> task = new Task<List<KategoriBarang>>() {
            @Override
            public List<KategoriBarang> call() throws Exception {
                Thread.sleep(timeout);
                try (Connection con = Koneksi.getConnection()) {
                    return KategoriBarangDAO.getAll(con);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            List<KategoriBarang> listKategori = task.getValue();
            allKategori.clear();
            allKategori.add("Semua");
            for (KategoriBarang k : listKategori) {
                allKategori.add(k.getKodeKategori());
            }
            kategoriCombo.getSelectionModel().select("Semua");
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    @FXML
    private void getBarang() {
        Task<List<Barang>> task = new Task<List<Barang>>() {
            @Override
            public List<Barang> call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    String kategori = "%";
                    if (!kategoriCombo.getSelectionModel().getSelectedItem().equals("Semua")) {
                        kategori = kategoriCombo.getSelectionModel().getSelectedItem();
                    }
                    List<Barang> listBarang = BarangDAO.getAllByKategoriAndStatus(con, kategori, "true");
                    List<Harga> listHarga = HargaDAO.getAll(con);
                    for (Barang b : listBarang) {
                        List<Harga> harga = new ArrayList<>();
                        for (Harga h : listHarga) {
                            if (b.getKodeBarang().equals(h.getKodeBarang())) {
                                harga.add(h);
                            }
                        }
                        b.setListHarga(harga);
                    }
                    return listBarang;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allBarang.clear();
            allBarang.addAll(task.getValue());
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    private Boolean checkColumn(String column) {
        return column != null && column.toLowerCase().contains(searchField.getText().toLowerCase());
    }

    private void searchBarang() {
        filterData.clear();
        allBarang.forEach((temp) -> {
            if (searchField.getText() == null || searchField.getText().equals("")) {
                filterData.add(temp);
            } else {
                if (checkColumn(temp.getKodeBarang())
                        || checkColumn(temp.getKodeKategori())
                        || checkColumn(temp.getNamaBarang())
                        || checkColumn(temp.getKeterangan())
                        || checkColumn(temp.getSatuanDasar())
                        || checkColumn(qty.format(temp.getStok()))
                        || checkColumn(qty.format(temp.getKeep()))
                        || checkColumn(qty.format(temp.getOrder()))
                        || checkColumn(temp.getKodeBarcode())) {
                    filterData.add(temp);
                }
            }
        });
        totalQtyLabel.setText(rp.format(filterData.size()));
    }

    private void newBarang() {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setNewBarang();
        x.saveButton.setOnAction((event) -> {
            if (x.namaBarangField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            } else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            } else if (x.allHarga.isEmpty()) {
                mainApp.showMessage(Modality.NONE, "Warning", "harga barang masih kosong");
            } else {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(timeout);
                        try (Connection con = Koneksi.getConnection()) {
                            Barang b = new Barang();
                            b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                            b.setKodeBarcode(x.kodeBarcodeField.getText());
                            b.setNamaBarang(x.namaBarangField.getText());
                            b.setKeterangan(x.keteranganField.getText());
                            b.setSatuanDasar(x.satuanDasarField.getText());
                            b.setStatus("true");
                            b.setListHarga(x.allHarga);
                            return Service.saveNewBarang(con, b);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if (status.equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil disimpan");
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
        });
    }

    private void updateBarang(Barang b) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang(b.getKodeBarang());
        x.saveButton.setOnAction((event) -> {
            if (x.namaBarangField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            } else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            } else if (x.allHarga.isEmpty()) {
                mainApp.showMessage(Modality.NONE, "Warning", "harga barang masih kosong");
            } else {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(timeout);
                        try (Connection con = Koneksi.getConnection()) {
                            b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                            b.setKodeBarcode(x.kodeBarcodeField.getText());
                            b.setNamaBarang(x.namaBarangField.getText());
                            b.setKeterangan(x.keteranganField.getText());
                            b.setSatuanDasar(x.satuanDasarField.getText());
                            b.setStatus("true");
                            b.setListHarga(x.allHarga);
                            return Service.saveUpdateBarang(con, b);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if (status.equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil disimpan");
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
        });
    }

    private void ubahHarga(Barang b) {
//        Stage stage = new Stage();
//        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/UbahHarga.fxml");
//        UbahHargaController x = loader.getController();
//        x.setMainApp(mainApp, stage);
//        x.setHarga(b.getSatuan().getHargaRetail(), b.getSatuan().getHargaGrosir(), b.getSatuan().getHargaGrosirBesar());
//        x.saveButton.setOnAction((event) -> {
//            if (x.hargaRetailField.getText().equals("")) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
//            } else if (x.hargaGrosirField.getText().equals("")) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
//            } else if (x.hargaGrosirBesarField.getText().equals("")) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir besar masih kosong");
//            } else {
//                Task<String> task = new Task<String>() {
//                    @Override
//                    public String call() throws Exception {
//                        Thread.sleep(timeout);
//                        try (Connection con = Koneksi.getConnection()) {
//                            b.getSatuan().setHargaGrosirBesar(Double.parseDouble(x.hargaGrosirBesarField.getText().replaceAll(",", "")));
//                            b.getSatuan().setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
//                            b.getSatuan().setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
//                            return Service.saveUpdateHarga(con, b.getSatuan());
//                        }
//                    }
//                };
//                task.setOnRunning((e) -> {
//                    mainApp.showLoadingScreen();
//                });
//                task.setOnSucceeded((e) -> {
//                    mainApp.closeLoading();
//                    getBarang();
//                    String status = task.getValue();
//                    if (status.equals("true")) {
//                        mainApp.closeDialog(stage);
//                        mainApp.showMessage(Modality.NONE, "Success", "Ubah harga berhasil disimpan");
//                    } else {
//                        mainApp.showMessage(Modality.NONE, "Failed", status);
//                    }
//                });
//                task.setOnFailed((e) -> {
//                    mainApp.closeLoading();
//                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
//                });
//                new Thread(task).start();
//            }
//        });
    }

    private void deleteBarang(Barang barang) {
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus barang " + barang.getKodeBarang() + "-" + barang.getNamaBarang() + " ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(timeout);
                    try (Connection con = Koneksi.getConnection()) {
                        return Service.deleteBarang(con, barang);
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                mainApp.closeLoading();
                getBarang();
                String status = task.getValue();
                if (status.equals("true")) {
                    mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil dihapus");
                } else {
                    mainApp.showMessage(Modality.NONE, "Failed", status);
                }
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        });
    }

    private void importBarang() {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/ImportBarang.fxml");
        ImportBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.saveButton.setOnAction((event) -> {
            MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                    "Import " + x.allBarang.size() + " barang ?");
            controller.OK.setOnAction((ActionEvent ev) -> {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(timeout);
                        try (Connection con = Koneksi.getConnection()) {
                            return Service.saveImportBarang(con, x.allBarang);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if (status.equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil disimpan");
                    } else {
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                    }
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            });
        });
    }

}
