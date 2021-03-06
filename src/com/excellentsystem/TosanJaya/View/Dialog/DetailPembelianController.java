/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.DAO.BarangDAO;
import com.excellentsystem.TosanJaya.DAO.HargaDAO;
import com.excellentsystem.TosanJaya.DAO.PembelianDetailDAO;
import com.excellentsystem.TosanJaya.DAO.PembelianHeadDAO;
import com.excellentsystem.TosanJaya.DAO.SupplierDAO;
import com.excellentsystem.TosanJaya.Function;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import static com.excellentsystem.TosanJaya.Function.pembulatan;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.sistem;
import static com.excellentsystem.TosanJaya.Main.tglLengkap;
import static com.excellentsystem.TosanJaya.Main.tglSql;
import static com.excellentsystem.TosanJaya.Main.timeout;
import com.excellentsystem.TosanJaya.Model.Barang;
import com.excellentsystem.TosanJaya.Model.PembelianDetail;
import com.excellentsystem.TosanJaya.Model.PembelianHead;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class DetailPembelianController {

    @FXML
    private GridPane gridPane;
    @FXML
    private TableView<PembelianDetail> pembelianDetailTable;
    @FXML
    private TableColumn numberColumn = new TableColumn("Number");
    @FXML
    private TableColumn<PembelianDetail, String> kodeBarangColumn;
    @FXML
    private TableColumn<PembelianDetail, String> namaBarangColumn;
    @FXML
    private TableColumn<PembelianDetail, Number> qtyColumn;
    @FXML
    private TableColumn<PembelianDetail, Number> hargaColumn;
    @FXML
    private TableColumn<PembelianDetail, Number> totalColumn;

    @FXML
    private TextField noPembelianField;
    @FXML
    private TextField tglPembelianField;
    @FXML
    public TextField supplierField;
    @FXML
    public TextField paymentTermField;
    @FXML
    public DatePicker jatuhTempoPicker;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button cariButton;
    @FXML
    public TextField totalPembelianField;
    @FXML
    public Button saveButton;

    private PembelianHead p;
    private Stage stage;
    private Main mainApp;
    public ObservableList<PembelianDetail> allBarang = FXCollections.observableArrayList();

    private Callback<TableColumn<PembelianDetail, Number>, TableCell<PembelianDetail, Number>> getQtyTableCell() {
        return (final TableColumn<PembelianDetail, Number> p) -> new TableCell<PembelianDetail, Number>() {
            final TextField textField = new TextField();

            @Override
            public void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(qty.format(item));
                    textField.setMaxWidth(Double.MAX_VALUE);
                    textField.setPrefHeight(30);
                    textField.getStyleClass().clear();
                    textField.getStyleClass().add("transparent-text-field");
                    textField.setOnKeyReleased((event) -> {
                        try {
                            PembelianDetail detail = (PembelianDetail) getTableView().getItems().get(getIndex());
                            detail.setQty(Double.parseDouble(textField.getText().replaceAll(",", "")));
                            detail.setTotal(Function.pembulatan(detail.getQty() * detail.getHarga()));
                            hitungTotal();
                        } catch (NumberFormatException e) {
                            textField.undo();
                        }
                    });
                    textField.setDisable(!saveButton.isVisible());
                    Platform.runLater(() -> {
                        textField.end();
                    });
                    setGraphic(textField);
                }
            }
        };
    }
//    private Callback<TableColumn<PembelianDetail, Number>, TableCell<PembelianDetail, Number>> getHargaTableCell(){
//        return (final TableColumn<PembelianDetail, Number> p) -> new TableCell<PembelianDetail, Number>(){
//            final TextField textField = new TextField();
//            @Override
//            public void updateItem( Number item, boolean empty ){
//                super.updateItem( item, empty );
//                if ( empty ){
//                    setGraphic( null );
//                }else{
//                    textField.setText(rp.format(item));
//                    textField.setMaxWidth(Double.MAX_VALUE);
//                    textField.setPrefHeight(30);
//                    textField.getStyleClass().clear();
//                    textField.getStyleClass().add("transparent-text-field");
//                    textField.setOnKeyReleased((event) -> {
//                        try{
//                            PembelianDetail detail = (PembelianDetail) getTableView().getItems().get(getIndex());
//                            detail.setHargaBeli(Double.parseDouble(textField.getText().replaceAll(",", "")));
//                            detail.setTotal(Function.pembulatan(detail.getQty()*detail.getHargaBeli()));
//                            hitungTotal();
//                        }catch(NumberFormatException e){
//                            textField.undo();
//                        }
//                    });
//                    textField.setDisable(!saveButton.isVisible());
//                    Platform.runLater(() -> {
//                        textField.end();
//                    });
//                    setGraphic(textField);
//                }
//            }
//        };
//    }

    private Callback<TableColumn<PembelianDetail, Number>, TableCell<PembelianDetail, Number>> getTotalTableCell() {
        return (final TableColumn<PembelianDetail, Number> p) -> new TableCell<PembelianDetail, Number>() {
            final TextField textField = new TextField();

            @Override
            public void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(rp.format(item));
                    textField.setMaxWidth(Double.MAX_VALUE);
                    textField.setPrefHeight(30);
                    textField.getStyleClass().clear();
                    textField.getStyleClass().add("transparent-text-field");
                    textField.setOnKeyReleased((event) -> {
                        try {
                            PembelianDetail detail = (PembelianDetail) getTableView().getItems().get(getIndex());
                            detail.setTotal(Double.parseDouble(textField.getText().replaceAll(",", "")));
                            detail.setHarga(Function.pembulatan(detail.getTotal() / detail.getQty()));
                            hitungTotal();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            textField.undo();
                        }
                    });
                    textField.setDisable(!saveButton.isVisible());
                    Platform.runLater(() -> {
                        textField.end();
                    });
                    setGraphic(textField);
                }
            }
        };
    }

    public void initialize() {
        numberColumn.setCellFactory((p) -> new TableCell() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                setText(empty ? null : getIndex() + 1 + "");
            }
        });
        kodeBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarangProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());

        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(getQtyTableCell());

        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory(col -> getTableCell(rp));

        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        totalColumn.setCellFactory(getTotalTableCell());

        final ContextMenu rm = new ContextMenu();
        MenuItem tambah = new MenuItem("Tambah Barang");
        tambah.setOnAction((ActionEvent e) -> {
            tambahBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            pembelianDetailTable.refresh();
        });
        rm.getItems().add(tambah);
        rm.getItems().addAll(refresh);
        pembelianDetailTable.setContextMenu(rm);
        pembelianDetailTable.setRowFactory(ttv -> {
            TableRow<PembelianDetail> row = new TableRow<PembelianDetail>() {
                @Override
                public void updateItem(PembelianDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rm);
                    } else {
                        ContextMenu rm = new ContextMenu();
                        MenuItem tambah = new MenuItem("Tambah Barang");
                        tambah.setOnAction((ActionEvent e) -> {
                            tambahBarang();
                        });
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item);
                        });
                        MenuItem ubah = new MenuItem("Ubah Barang");
                        ubah.setOnAction((ActionEvent e) -> {
                            ubahBarang(item);
                        });
                        MenuItem hapus = new MenuItem("Hapus Barang");
                        hapus.setOnAction((ActionEvent e) -> {
                            hapusBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            pembelianDetailTable.refresh();
                        });
                        if (saveButton.isVisible()) {
                            rm.getItems().add(tambah);
                            rm.getItems().add(ubah);
                            rm.getItems().add(hapus);
                        } else {
                            rm.getItems().add(detail);
                        }
                        rm.getItems().add(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        if (saveButton.isVisible()) {
                            ubahBarang(row.getItem());
                        } else {
                            detailBarang(row.getItem());
                        }
                    }
                }
            });
            return row;
        });
        pembelianDetailTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (saveButton.isVisible()) {
                if (event.getCode() == KeyCode.DELETE) {
                    if (pembelianDetailTable.getSelectionModel().getSelectedItem() != null) {
                        hapusBarang(pembelianDetailTable.getSelectionModel().getSelectedItem());
                    }
                }
                if (event.getCode() == KeyCode.SPACE) {
                    if (pembelianDetailTable.getSelectionModel().getSelectedItem() != null) {
                        ubahBarang(pembelianDetailTable.getSelectionModel().getSelectedItem());
                    }
                }
            }
        });
        pembelianDetailTable.setItems(allBarang);
        jatuhTempoPicker.setConverter(Function.getTglConverter());
        jatuhTempoPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        jatuhTempoPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellDisableBefore(LocalDate.parse(sistem.getTglSystem())));
    }

    public void setMainApp(Main main, Stage stage) {
        mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (saveButton.isVisible()) {
                if (event.getCode() == KeyCode.ENTER) {
                    searchField.requestFocus();
                    searchField.selectAll();
                }
                if (event.getCode() == KeyCode.DOWN) {
                    if (!pembelianDetailTable.isFocused()) {
                        Platform.runLater(() -> {
                            pembelianDetailTable.requestFocus();
                            pembelianDetailTable.getSelectionModel().select(0);
                            pembelianDetailTable.getFocusModel().focus(0);
                        });
                    }
                }
                if (event.getCode() == KeyCode.F5) {
                    tambahBarang();
                }
                if (event.getCode() == KeyCode.F12) {
                    if (!allBarang.isEmpty()) {
                        pembelianDetailTable.getSelectionModel().selectFirst();
                        ubahBarang(pembelianDetailTable.getSelectionModel().getSelectedItem());
                    }
                }
            }
        });
    }

    public void setNewPembelian() {
        Task<String> task = new Task<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(timeout);
                try (Connection con = Koneksi.getConnection()) {
                    return PembelianHeadDAO.getId(con);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent ev) -> {
            try {
                mainApp.closeLoading();
                p = new PembelianHead();
                noPembelianField.setText(task.getValue());
                tglPembelianField.setText(tglLengkap.format(tglSql.parse(Function.getSystemDate())));
                supplierField.setText("");
                totalPembelianField.setText(rp.format(0));
                allBarang.clear();
            } catch (Exception e) {
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    public void setDetailPembelian(String noPembelian) {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                Thread.sleep(timeout);
                try (Connection con = Koneksi.getConnection()) {
                    p = PembelianHeadDAO.get(con, noPembelian);
                    p.setSupplier(SupplierDAO.get(con, p.getKodeSupplier()));
                    p.setListPembelianDetail(PembelianDetailDAO.getAll(con, noPembelian));
                    return null;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent ev) -> {
            try {
                mainApp.closeLoading();
                noPembelianField.setText(p.getNoPembelian());
                tglPembelianField.setText(tglLengkap.format(tglSql.parse(p.getTglPembelian())));
                supplierField.setText(p.getSupplier().getNama());
                paymentTermField.setText(p.getPaymentTerm());
                jatuhTempoPicker.setValue(LocalDate.parse(p.getJatuhTempo(), DateTimeFormatter.ISO_DATE));
                totalPembelianField.setText(rp.format(p.getTotalPembelian()));
                allBarang.clear();
                allBarang.addAll(p.getListPembelianDetail());
                List<MenuItem> removeMenu = new ArrayList<>();
                for (MenuItem m : pembelianDetailTable.getContextMenu().getItems()) {
                    if (m.getText().equals("Tambah Barang")) {
                        removeMenu.add(m);
                    }
                }
                pembelianDetailTable.getContextMenu().getItems().removeAll(removeMenu);

                supplierField.setDisable(true);
                paymentTermField.setDisable(true);
                jatuhTempoPicker.setDisable(true);
                searchField.setVisible(false);
                searchButton.setVisible(false);
                saveButton.setVisible(false);
                cariButton.setVisible(false);
                gridPane.getRowConstraints().get(7).setMinHeight(0);
                gridPane.getRowConstraints().get(7).setPrefHeight(0);
                gridPane.getRowConstraints().get(7).setMaxHeight(0);
                gridPane.getRowConstraints().get(10).setMinHeight(0);
                gridPane.getRowConstraints().get(10).setPrefHeight(0);
                gridPane.getRowConstraints().get(10).setMaxHeight(0);
            } catch (Exception e) {
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    public void setEditPembelian(String noPembelian) {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                Thread.sleep(timeout);
                try (Connection con = Koneksi.getConnection()) {
                    p = PembelianHeadDAO.get(con, noPembelian);
                    p.setSupplier(SupplierDAO.get(con, p.getKodeSupplier()));
                    p.setListPembelianDetail(PembelianDetailDAO.getAll(con, noPembelian));
                    for (PembelianDetail d : p.getListPembelianDetail()) {
                        d.setBarang(BarangDAO.get(con, d.getKodeBarang()));
                    }
                    return null;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent ev) -> {
            try {
                mainApp.closeLoading();
                noPembelianField.setText(p.getNoPembelian());
                tglPembelianField.setText(tglLengkap.format(tglSql.parse(p.getTglPembelian())));
                supplierField.setText(p.getSupplier().getNama());
                paymentTermField.setText(p.getPaymentTerm());
                jatuhTempoPicker.setValue(LocalDate.parse(p.getJatuhTempo(), DateTimeFormatter.ISO_DATE));
                totalPembelianField.setText(rp.format(p.getTotalPembelian()));
                allBarang.clear();
                allBarang.addAll(p.getListPembelianDetail());
            } catch (Exception e) {
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    private void detailBarang(PembelianDetail d) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setBarang(d.getKodeBarang());
        x.setViewOnly();
    }

    @FXML
    private void tambahBarang() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/CariBarang.fxml");
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
    private void getBarang() {
        if (!"".equals(searchField.getText())) {
            Task<List<Barang>> task = new Task<List<Barang>>() {
                @Override
                public List<Barang> call() throws Exception {
                    Thread.sleep(timeout);
                    try (Connection con = Koneksi.getConnection()) {
                        List<Barang> listBarang = BarangDAO.getAllByKodeBarcode(con, searchField.getText());
                        for (Barang b : listBarang) {
                            b.setListHarga(HargaDAO.getAllByKodeBarang(con, b.getKodeBarang()));
                        }
                        return listBarang;
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                mainApp.closeLoading();
                List<Barang> listBarang = task.getValue();
                if (listBarang.isEmpty()) {
                    mainApp.showWarning("Warning", "Kode Barcode "+searchField.getText()+ " Tidak Ditemukan");
                } else {
                    if (listBarang.size() == 1) {
                        setBarang(listBarang.get(0));
                    } else {
                        Stage child = new Stage();
                        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/ListBarang.fxml");
                        ListBarangController controller = loader.getController();
                        controller.setMainApp(mainApp, child);
                        controller.setBarang(listBarang, "Pembelian");
                        for (Node n : controller.flowPane.getChildren()) {
                            if (n instanceof VBox) {
                                VBox v = (VBox) n;
                                v.setOnMouseClicked((event) -> {
                                    for (Barang brg : listBarang) {
                                        if (brg.getKodeBarang().equals(v.getId().substring(0, 7))) {
                                            mainApp.closeDialog(child);
                                            setBarang(brg);
                                        }
                                    }
                                });
                            }
                        }
                        child.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                for (Node n : controller.flowPane.getChildren()) {
                                    if (n instanceof VBox) {
                                        VBox v = (VBox) n;
                                        if (v.isFocused()) {
                                            for (Barang brg : listBarang) {
                                                if (brg.getKodeBarang().equals(v.getId().substring(0, 7))) {
                                                    mainApp.closeDialog(child);
                                                    setBarang(brg);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        child.setOnHiding(event -> {
                            searchField.requestFocus();
                        });
                    }
                }
                searchField.setText("");
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        } else {
            searchField.requestFocus();
        }
    }

    private void setBarang(Barang b) {
        boolean statusBarang = true;
        for (PembelianDetail d : allBarang) {
            if (d.getKodeBarang().equals(b.getKodeBarang())) {
                d.setQty(pembulatan(d.getQty() + 1));
                d.setTotal(pembulatan(d.getHarga() * d.getQty()));
                statusBarang = false;
                pembelianDetailTable.refresh();
                pembelianDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
            }
        }
        if (statusBarang) {
            PembelianDetail d = new PembelianDetail();
            d.setBarang(b);
            d.setNoPembelian("");
            d.setNoUrut(0);
            d.setKodeBarang(b.getKodeBarang());
            d.setNamaBarang(b.getNamaBarang());
            d.setQty(1);
            d.setHarga(0);
            d.setTotal(pembulatan(d.getHarga() * d.getQty()));
            allBarang.add(0, d);
            pembelianDetailTable.refresh();
            pembelianDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
        }
        searchField.requestFocus();
    }

    private void ubahBarang(PembelianDetail d) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/UbahBarang.fxml");
        UbahBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setPembelian(d);
        controller.qtyField.requestFocus();
        controller.qtyField.selectAll();
        child.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                d.setQty(Double.parseDouble(controller.qtyField.getText().replaceAll(",", "")));
                d.setHarga(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                d.setTotal(pembulatan(d.getHarga() * d.getQty()));

                pembelianDetailTable.refresh();
                pembelianDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
                mainApp.closeDialog(child);
            }
        });
        child.setOnHiding(event -> {
            searchField.requestFocus();
        });
    }

    private void hapusBarang(PembelianDetail d) {
        WarningController warningController = mainApp.showWarning("Confirmation", "Hapus barang " + d.getNamaBarang() + " - " + qty.format(d.getQty()) + " ?");
        warningController.OK.setOnAction((event) -> {
            allBarang.remove(d);
            pembelianDetailTable.refresh();
            pembelianDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
            mainApp.closeWarning();
        });
    }

    @FXML
    private void hitungTotal() {
        double total = 0;
        for (PembelianDetail d : allBarang) {
            total = total + d.getTotal();
        }
        totalPembelianField.setText(rp.format(total));
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

}
