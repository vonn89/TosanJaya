/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View;

import com.excellentsystem.TosanJaya.DAO.SupplierDAO;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.timeout;
import com.excellentsystem.TosanJaya.Model.Supplier;
import com.excellentsystem.TosanJaya.Service.Service;
import com.excellentsystem.TosanJaya.View.Dialog.DetailSupplierController;
import com.excellentsystem.TosanJaya.View.Dialog.MessageController;
import com.excellentsystem.TosanJaya.View.Dialog.UbahHargaController;
import java.sql.Connection;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
 * @author Excellent
 */
public class DataSupplierController {

    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, String> kodeSupplierColumn;
    @FXML
    private TableColumn<Supplier, String> namaColumn;
    @FXML
    private TableColumn<Supplier, String> alamatColumn;
    @FXML
    private TableColumn<Supplier, String> noTelpColumn;
    @FXML
    private TableColumn<Supplier, Number> hutangColumn;

    @FXML
    private Label totalHutangLabel;
    @FXML
    private TextField searchField;
    private Main mainApp;
    private ObservableList<Supplier> allSupplier = FXCollections.observableArrayList();
    private ObservableList<Supplier> filterData = FXCollections.observableArrayList();

    public void initialize() {
        kodeSupplierColumn.setCellValueFactory(cellData -> cellData.getValue().kodeSupplierProperty());
        namaColumn.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
        alamatColumn.setCellValueFactory(cellData -> cellData.getValue().alamatProperty());
        noTelpColumn.setCellValueFactory(cellData -> cellData.getValue().noTelpProperty());
        hutangColumn.setCellValueFactory(cellData -> cellData.getValue().hutangProperty());
        hutangColumn.setCellFactory((c) -> getTableCell(rp));

        allSupplier.addListener((ListChangeListener.Change<? extends Supplier> change) -> {
            searchSupplier();
        });
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchSupplier();
        });
        filterData.addAll(allSupplier);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Supplier Baru");
        addNew.setOnAction((ActionEvent e) -> {
            newSupplier();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            getSupplier();
        });
        rowMenu.getItems().addAll(addNew, refresh);
        supplierTable.setContextMenu(rowMenu);
        supplierTable.setRowFactory(table -> {
            TableRow<Supplier> row = new TableRow<Supplier>() {
                @Override
                public void updateItem(Supplier item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah Supplier Baru");
                        addNew.setOnAction((ActionEvent e) -> {
                            newSupplier();
                        });
                        MenuItem edit = new MenuItem("Ubah Supplier");
                        edit.setOnAction((ActionEvent e) -> {
                            updateSupplier(item);
                        });
                        MenuItem delete = new MenuItem("Hapus Supplier");
                        delete.setOnAction((ActionEvent e) -> {
                            deleteSupplier(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getSupplier();
                        });
                        rowMenu.getItems().addAll(addNew, edit, delete, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && row.getItem() != null) {
                    updateSupplier(row.getItem());
                }
            });
            return row;
        });
        supplierTable.setItems(filterData);
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                searchField.requestFocus();
                searchField.selectAll();
            }
        });
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        getSupplier();
    }

    private void getSupplier() {
        Task<List<Supplier>> task = new Task<List<Supplier>>() {
            @Override
            public List<Supplier> call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    return SupplierDAO.getAllByStatus(con, "true");
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allSupplier.clear();
            allSupplier.addAll(task.getValue());
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

    private void searchSupplier() {
        filterData.clear();
        allSupplier.forEach((temp) -> {
            if (searchField.getText() == null || searchField.getText().equals("")) {
                filterData.add(temp);
            } else {
                if (checkColumn(temp.getKodeSupplier())
                        || checkColumn(temp.getNama())
                        || checkColumn(temp.getAlamat())
                        || checkColumn(temp.getNoTelp())
                        || checkColumn(rp.format(temp.getHutang()))) {
                    filterData.add(temp);
                }
            }
        });
        hitungTotal();
    }

    private void hitungTotal() {
        double total = 0;
        for (Supplier p : filterData) {
            total = total + p.getHutang();
        }
        totalHutangLabel.setText(rp.format(total));
    }

    private void newSupplier() {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailSupplier.fxml");
        DetailSupplierController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setNewSupplier();
        x.saveButton.setOnAction((event) -> {
            if (x.namaField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama masih kosong");
            } else if (x.alamatField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Alamat masih kosong");
            } else if (x.noTelpField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "No telp masih kosong");
            } else {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(timeout);
                        try (Connection con = Koneksi.getConnection()) {
                            Supplier b = new Supplier();
                            b.setNama(x.namaField.getText());
                            b.setAlamat(x.alamatField.getText());
                            b.setNoTelp(x.noTelpField.getText());
                            b.setHutang(0);
                            b.setStatus("true");
                            return Service.saveNewSupplier(con, b);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getSupplier();
                    String status = task.getValue();
                    if (status.equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data supplier berhasil disimpan");
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

    private void updateSupplier(Supplier b) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailSupplier.fxml");
        DetailSupplierController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setSupplier(b.getKodeSupplier());
        x.saveButton.setOnAction((event) -> {
            if (x.namaField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama masih kosong");
            } else if (x.alamatField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Alamat masih kosong");
            } else if (x.noTelpField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "No telp masih kosong");
            } else {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(timeout);
                        try (Connection con = Koneksi.getConnection()) {
                            b.setNama(x.namaField.getText());
                            b.setAlamat(x.alamatField.getText());
                            b.setNoTelp(x.noTelpField.getText());
                            return Service.saveUpdateSupplier(con, b);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getSupplier();
                    String status = task.getValue();
                    if (status.equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data supplier berhasil disimpan");
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

    private void deleteSupplier(Supplier supplier) {
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus supplier " + supplier.getKodeSupplier() + "-" + supplier.getNama() + " ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(timeout);
                    try (Connection con = Koneksi.getConnection()) {
                        return Service.deleteSupplier(con, supplier);
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                mainApp.closeLoading();
                getSupplier();
                String status = task.getValue();
                if (status.equals("true")) {
                    mainApp.showMessage(Modality.NONE, "Success", "Data supplier berhasil dihapus");
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
}
