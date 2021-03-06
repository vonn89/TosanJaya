/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View;

import com.excellentsystem.TosanJaya.DAO.PelangganDAO;
import com.excellentsystem.TosanJaya.DAO.PembayaranDAO;
import com.excellentsystem.TosanJaya.DAO.PenjualanDetailDAO;
import com.excellentsystem.TosanJaya.DAO.PenjualanHeadDAO;
import com.excellentsystem.TosanJaya.Function;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.sistem;
import static com.excellentsystem.TosanJaya.Main.tglLengkap;
import static com.excellentsystem.TosanJaya.Main.tglSql;
import static com.excellentsystem.TosanJaya.Main.user;
import com.excellentsystem.TosanJaya.Model.Pelanggan;
import com.excellentsystem.TosanJaya.Model.Pembayaran;
import com.excellentsystem.TosanJaya.Model.PenjualanDetail;
import com.excellentsystem.TosanJaya.Model.PenjualanHead;
import com.excellentsystem.TosanJaya.PrintOut.PrintOut;
import com.excellentsystem.TosanJaya.Service.Service;
import com.excellentsystem.TosanJaya.View.Dialog.DetailPembayaranController;
import com.excellentsystem.TosanJaya.View.Dialog.DetailPenjualanController;
import com.excellentsystem.TosanJaya.View.Dialog.MessageController;
import com.excellentsystem.TosanJaya.View.Dialog.PembayaranController;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class DataPenjualanController {

    @FXML
    private TableView<PenjualanHead> penjualanHeadTable;
    @FXML
    private TableColumn<PenjualanHead, String> noPenjualanColumn;
    @FXML
    private TableColumn<PenjualanHead, String> tglPenjualanColumn;
    @FXML
    private TableColumn<PenjualanHead, String> pelangganColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> totalPenjualanColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> diskonColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> ongkosColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> grandtotalColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> pembayaranColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> sisaPembayaranColumn;
    @FXML
    private TableColumn<PenjualanHead, String> kodeUserColumn;

    @FXML
    private Label totalPenjualanLabel;
    @FXML
    private Label pembayaranLabel;
    @FXML
    private Label sisaPembayaranLabel;

    @FXML
    private TextField searchField;
    @FXML
    private DatePicker tglAwalPicker;
    @FXML
    private DatePicker tglAkhirPicker;

    private Main mainApp;
    private final ObservableList<PenjualanHead> allPenjualan = FXCollections.observableArrayList();
    private final ObservableList<PenjualanHead> filterData = FXCollections.observableArrayList();

    public void initialize() {
        noPenjualanColumn.setCellValueFactory(cellData -> cellData.getValue().noPenjualanProperty());
        noPenjualanColumn.setCellFactory(col -> Function.getWrapTableCell(noPenjualanColumn));

        tglPenjualanColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getTglPenjualan())));
            } catch (Exception ex) {
                return null;
            }
        });
        tglPenjualanColumn.setComparator(Function.sortDate(tglLengkap));
        tglPenjualanColumn.setCellFactory(col -> Function.getWrapTableCell(tglPenjualanColumn));

        pelangganColumn.setCellValueFactory(cellData -> cellData.getValue().getPelanggan().namaProperty());
        pelangganColumn.setCellFactory(col -> Function.getWrapTableCell(pelangganColumn));

        totalPenjualanColumn.setCellValueFactory(cellData -> cellData.getValue().totalPenjualanProperty());
        totalPenjualanColumn.setCellFactory(col -> getTableCell(rp));

        diskonColumn.setCellValueFactory(cellData -> cellData.getValue().diskonProperty());
        diskonColumn.setCellFactory(col -> getTableCell(rp));

        ongkosColumn.setCellValueFactory(cellData -> cellData.getValue().ongkosProperty());
        ongkosColumn.setCellFactory(col -> getTableCell(rp));

        grandtotalColumn.setCellValueFactory(cellData -> cellData.getValue().grandtotalProperty());
        grandtotalColumn.setCellFactory(col -> getTableCell(rp));

        pembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().pembayaranProperty());
        pembayaranColumn.setCellFactory(col -> getTableCell(rp));

        sisaPembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().sisaPembayaranProperty());
        sisaPembayaranColumn.setCellFactory(col -> getTableCell(rp));

        kodeUserColumn.setCellValueFactory(cellData -> cellData.getValue().kodeUserProperty());
        kodeUserColumn.setCellFactory(col -> Function.getWrapTableCell(kodeUserColumn));

        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));

        final ContextMenu rowMenu = new ContextMenu();
        MenuItem penjualan = new MenuItem("Tambah Penjualan Baru");
        penjualan.setOnAction((ActionEvent e) -> {
            newPenjualan();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getPenjualan();
        });
        rowMenu.getItems().add(penjualan);
        rowMenu.getItems().addAll(refresh);
        penjualanHeadTable.setContextMenu(rowMenu);
        penjualanHeadTable.setRowFactory(table -> {
            TableRow<PenjualanHead> row = new TableRow<PenjualanHead>() {
                @Override
                public void updateItem(PenjualanHead item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem penjualan = new MenuItem("Tambah Penjualan Baru");
                        penjualan.setOnAction((ActionEvent e) -> {
                            newPenjualan();
                        });
                        MenuItem detail = new MenuItem("Detail Penjualan");
                        detail.setOnAction((ActionEvent e) -> {
                            detailPenjualan(item);
                        });
                        MenuItem batal = new MenuItem("Batal Penjualan");
                        batal.setOnAction((ActionEvent e) -> {
                            batalPenjualan(item);
                        });
                        MenuItem detailBayar = new MenuItem("Detail Pembayaran");
                        detailBayar.setOnAction((ActionEvent e) -> {
                            detailPembayaran(item);
                        });
                        MenuItem bayar = new MenuItem("Pembayaran Penjualan");
                        bayar.setOnAction((ActionEvent e) -> {
                            pembayaran(item);
                        });
                        MenuItem print = new MenuItem("Print Struk Penjualan");
                        print.setOnAction((ActionEvent e) -> {
                            printPenjualanDirect(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getPenjualan();
                        });
                        rowMenu.getItems().add(penjualan);
                        rowMenu.getItems().add(detail);
                        rowMenu.getItems().add(batal);
                        rowMenu.getItems().add(detailBayar);
                        rowMenu.getItems().add(bayar);
                        rowMenu.getItems().add(print);
                        rowMenu.getItems().add(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        detailPenjualan(row.getItem());
                    }
                }
            });
            return row;
        });

        allPenjualan.addListener((ListChangeListener.Change<? extends PenjualanHead> change) -> {
            searchPenjualan();
        });
        searchField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    searchPenjualan();
                });
        filterData.addAll(allPenjualan);
        penjualanHeadTable.setItems(filterData);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        getPenjualan();
    }

    @FXML
    private void getPenjualan() {
        Task<List<PenjualanHead>> task = new Task<List<PenjualanHead>>() {
            @Override
            public List<PenjualanHead> call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    List<PenjualanDetail> listPenjualanDetail = PenjualanDetailDAO.getAllByDateAndStatus(
                            con, tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
                    List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllByDateAndStatus(
                            con, tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
                    for (PenjualanHead p : listPenjualanHead) {
                        List<PenjualanDetail> listDetail = new ArrayList<>();
                        for (PenjualanDetail d : listPenjualanDetail) {
                            if (d.getNoPenjualan().equals(p.getNoPenjualan())) {
                                listDetail.add(d);
                            }
                        }
                        p.setListPenjualanDetail(listDetail);
                        if (p.getPelanggan() != null && !p.getPelanggan().equals("")) {
                            p.setPelanggan(PelangganDAO.get(con, p.getKodePelanggan()));
                        }
                        if (p.getPelanggan() == null) {
                            Pelanggan plg = new Pelanggan();
                            plg.setKodePelanggan("");
                            plg.setNama("");
                            plg.setAlamat("");
                            plg.setNoTelp("");
                            plg.setHutang(0);
                            plg.setDeposit(0);
                            plg.setStatus("");

                            p.setPelanggan(plg);
                        }
                    }
                    return listPenjualanHead;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allPenjualan.clear();
            allPenjualan.addAll(task.getValue());
            penjualanHeadTable.refresh();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    private Boolean checkColumn(String column) {
        if (column != null) {
            if (column.toLowerCase().contains(searchField.getText().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void searchPenjualan() {
        try {
            filterData.clear();
            for (PenjualanHead temp : allPenjualan) {
                if (searchField.getText() == null || searchField.getText().equals("")) {
                    filterData.add(temp);
                } else {
                    if (checkColumn(temp.getNoPenjualan())
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getTglPenjualan())))
                            || checkColumn(temp.getPelanggan().getNama())
                            || checkColumn(rp.format(temp.getTotalPenjualan()))
                            || checkColumn(rp.format(temp.getDiskon()))
                            || checkColumn(rp.format(temp.getOngkos()))
                            || checkColumn(rp.format(temp.getPembayaran()))
                            || checkColumn(rp.format(temp.getSisaPembayaran()))
                            || checkColumn(rp.format(temp.getGrandtotal()))
                            || checkColumn(temp.getKodeUser())) {
                        filterData.add(temp);
                    } else {
                        boolean status = false;
                        for (PenjualanDetail d : temp.getListPenjualanDetail()) {
                            if (checkColumn(d.getNamaBarang())
                                    || checkColumn(d.getKodeBarang())) {
                                status = true;
                            }
                        }
                        if (status) {
                            filterData.add(temp);
                        }
                    }
                }
            }
            hitungTotal();
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void hitungTotal() {
        double totalPenjualan = 0;
        double pembayaran = 0;
        double sisaPembayaran = 0;
        for (PenjualanHead d : filterData) {
            totalPenjualan = totalPenjualan + d.getGrandtotal();
            pembayaran = pembayaran + d.getPembayaran();
            sisaPembayaran = sisaPembayaran + d.getSisaPembayaran();
        }
        totalPenjualanLabel.setText(rp.format(totalPenjualan));
        pembayaranLabel.setText(rp.format(pembayaran));
        sisaPembayaranLabel.setText(rp.format(sisaPembayaran));
    }

    private void newPenjualan() {
        mainApp.showPenjualan();
    }

    private void detailPenjualan(PenjualanHead p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPenjualan.fxml");
        DetailPenjualanController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setDetailPenjualan(p.getNoPenjualan(), false);
    }

    private void batalPenjualan(PenjualanHead p) {
        Task<String> task = new Task<String>() {
            @Override
            public String call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    p.setListPenjualanDetail(PenjualanDetailDAO.getAll(con, p.getNoPenjualan()));
                    p.setListPembayaran(PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPenjualan(), "true"));
                    p.setStatus("false");
                    p.setTglBatal(Function.getSystemDate());
                    p.setUserBatal(user.getKodeUser());
                    return Service.saveBatalPenjualan(con, p);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            getPenjualan();
            String statusBatal = task.getValue();
            if (statusBatal.equals("true")) {
                mainApp.showMessage(Modality.NONE, "Success", "Penjualan berhasil dibatal");
            } else {
                mainApp.showMessage(Modality.NONE, "Failed", statusBatal);
            }
        });
        task.setOnFailed((e) -> {
            task.getException().printStackTrace();
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    private void printPenjualanDirect(PenjualanHead p) {
        try (Connection con = Koneksi.getConnection()) {
            p.setListPenjualanDetail(PenjualanDetailDAO.getAll(con, p.getNoPenjualan()));
            p.setListPembayaran(PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPenjualan(), "true"));
            PrintOut po = new PrintOut();
            po.printStrukPenjualanDirect(p);
        } catch (Exception e) {
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }


    private void detailPembayaran(PenjualanHead p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPembayaran.fxml");
        DetailPembayaranController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setDetailPenjualan(p);
        x.pembayaranTable.setRowFactory((TableView<Pembayaran> tableView) -> {
            final TableRow<Pembayaran> row = new TableRow<Pembayaran>() {
                @Override
                public void updateItem(Pembayaran item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        final ContextMenu rm = new ContextMenu();
                        MenuItem batal = new MenuItem("Batal Pembayaran");
                        batal.setOnAction((ActionEvent e) -> {
                            batalPembayaran(item, stage);
                        });
                        rm.getItems().add(batal);
                        setContextMenu(rm);
                    }
                }
            };
            return row;
        });
    }

    private void batalPembayaran(Pembayaran pembayaran, Stage stage) {
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Batal pembayaran " + pembayaran.getNoPembayaran() + " ?");
        controller.OK.setOnAction((ActionEvent e) -> {
            mainApp.closeMessage();
            Task<String> task = new Task<String>() {
                @Override
                public String call() throws Exception {
                    try (Connection con = Koneksi.getConnection()) {
                        pembayaran.setTglBatal(Function.getSystemDate());
                        pembayaran.setUserBatal(user.getKodeUser());
                        pembayaran.setStatus("false");
                        return Service.saveBatalPembayaranPenjualan(con, pembayaran);
                    }
                }
            };
            task.setOnRunning((ex) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent ex) -> {
                mainApp.closeLoading();
                getPenjualan();
                if (task.getValue().equals("true")) {
                    mainApp.closeDialog(stage);
                    mainApp.showMessage(Modality.NONE, "Success", "Data pembayaran berhasil dibatal");
                } else {
                    mainApp.showMessage(Modality.NONE, "Error", task.getValue());
                }
            });
            task.setOnFailed((ex) -> {
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                mainApp.closeLoading();
            });
            new Thread(task).start();
        });
    }

    private void pembayaran(PenjualanHead p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/Pembayaran.fxml");
        PembayaranController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setPembayaranPenjualan(p);
        controller.saveButton.setOnAction((event) -> {
            double jumlahBayar = Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", ""));
            if (jumlahBayar > p.getSisaPembayaran()) {
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah yang dibayar melebihi dari sisa pembayaran");
            } else {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        try (Connection con = Koneksi.getConnection()) {
                            p.setPembayaran(p.getPembayaran() + Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", "")));
                            p.setSisaPembayaran(p.getSisaPembayaran() - Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", "")));

                            Pembayaran pembayaran = new Pembayaran();
                            pembayaran.setNoPembayaran(PembayaranDAO.getId(con));
                            pembayaran.setTglPembayaran(Function.getSystemDate());
                            pembayaran.setNoTransaksi(p.getNoPenjualan());
                            pembayaran.setJenisPembayaran("Cash");
                            pembayaran.setKeterangan("");
                            pembayaran.setJumlahPembayaran(Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", "")));
                            pembayaran.setKodeUser(user.getKodeUser());
                            pembayaran.setTglBatal("2000-01-01 00:00:00");
                            pembayaran.setUserBatal("");
                            pembayaran.setStatus("true");
                            return Service.savePembayaranPenjualan(con, p, pembayaran);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getPenjualan();
                    if (task.getValue().equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Pembayaran penjualan berhasil disimpan");
                    } else {
                        mainApp.showMessage(Modality.NONE, "Error", task.getValue());
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
}
