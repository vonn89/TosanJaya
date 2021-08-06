/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View;

import com.excellentsystem.TosanJaya.DAO.BarangDAO;
import com.excellentsystem.TosanJaya.DAO.CartHeadDAO;
import com.excellentsystem.TosanJaya.DAO.HargaDAO;
import com.excellentsystem.TosanJaya.DAO.PelangganDAO;
import com.excellentsystem.TosanJaya.DAO.PembayaranDAO;
import com.excellentsystem.TosanJaya.Function;
import static com.excellentsystem.TosanJaya.Function.pembulatan;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.timeout;
import static com.excellentsystem.TosanJaya.Main.user;
import com.excellentsystem.TosanJaya.Model.Barang;
import com.excellentsystem.TosanJaya.Model.CartDetail;
import com.excellentsystem.TosanJaya.Model.CartHead;
import com.excellentsystem.TosanJaya.Model.Pelanggan;
import com.excellentsystem.TosanJaya.Model.Pembayaran;
import com.excellentsystem.TosanJaya.Model.PenjualanDetail;
import com.excellentsystem.TosanJaya.Model.PenjualanHead;
import com.excellentsystem.TosanJaya.PrintOut.PrintOut;
import com.excellentsystem.TosanJaya.Service.Service;
import com.excellentsystem.TosanJaya.View.Dialog.CariBarangController;
import com.excellentsystem.TosanJaya.View.Dialog.CariPelangganController;
import com.excellentsystem.TosanJaya.View.Dialog.CartController;
import com.excellentsystem.TosanJaya.View.Dialog.InputNumberController;
import com.excellentsystem.TosanJaya.View.Dialog.ListBarangController;
import com.excellentsystem.TosanJaya.View.Dialog.CariPenjualanController;
import com.excellentsystem.TosanJaya.View.Dialog.DetailPenjualanController;
import com.excellentsystem.TosanJaya.View.Dialog.NamaCartController;
import com.excellentsystem.TosanJaya.View.Dialog.PembayaranController;
import com.excellentsystem.TosanJaya.View.Dialog.TotalPenjualanController;
import com.excellentsystem.TosanJaya.View.Dialog.UbahBarangController;
import com.excellentsystem.TosanJaya.View.Dialog.VerifikasiController;
import com.excellentsystem.TosanJaya.View.Dialog.WarningController;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class PenjualanController {

    @FXML
    public TableView<PenjualanDetail> penjualanDetailTable;
    @FXML
    private TableColumn numberColumn = new TableColumn();
    @FXML
    private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> qtyColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> totalColumn;

    @FXML
    public TextField searchField;
    @FXML
    private TextField totalPenjualanField;

    @FXML
    private Label cartLabel;

    @FXML
    private Label kategoriLabel;

    private CartHead cartHead;
    private Main mainApp;
    public ObservableList<PenjualanDetail> allBarang = FXCollections.observableArrayList();

    public void initialize() {
        namaBarangColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.7));
        qtyColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.1));
        hargaColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.1));
        totalColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.1));

        penjualanDetailTable.getColumns().addListener(new ListChangeListener() {
            private boolean suspended;

            @Override
            public void onChanged(ListChangeListener.Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    penjualanDetailTable.getColumns().setAll(namaBarangColumn, qtyColumn, hargaColumn,
                            totalColumn);
                    this.suspended = false;
                }
            }
        });

        numberColumn.setCellFactory((p) -> new TableCell() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                setText(empty ? null : getIndex() + 1 + "");
            }
        });
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory((param) -> Function.getTableCell(qty));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory((param) -> Function.getTableCell(rp));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        totalColumn.setCellFactory((param) -> Function.getTableCell(rp));

        final ContextMenu rm = new ContextMenu();
        MenuItem cari = new MenuItem("Cari Barang");
        cari.setOnAction((ActionEvent event) -> {
            cariBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            penjualanDetailTable.refresh();
        });
        rm.getItems().addAll(cari, refresh);
        penjualanDetailTable.setContextMenu(rm);
        penjualanDetailTable.setRowFactory(ttv -> {
            TableRow<PenjualanDetail> row = new TableRow<PenjualanDetail>() {
                @Override
                public void updateItem(PenjualanDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rm);
                    } else {
                        ContextMenu rm = new ContextMenu();
                        MenuItem cari = new MenuItem("Cari Barang");
                        cari.setOnAction((ActionEvent event) -> {
                            cariBarang();
                        });
                        MenuItem ubah = new MenuItem("Ubah Barang");
                        ubah.setOnAction((ActionEvent event) -> {
                            ubahBarang(item, false);
                        });
                        MenuItem hapus = new MenuItem("Hapus Barang");
                        hapus.setOnAction((ActionEvent event) -> {
                            hapusBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            penjualanDetailTable.refresh();
                        });
                        rm.getItems().add(cari);
                        rm.getItems().add(ubah);
                        rm.getItems().add(hapus);
                        rm.getItems().add(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && row.getItem() != null) {
                    ubahBarang(row.getItem(), false);
                }
            });
            return row;
        });
        penjualanDetailTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                if (penjualanDetailTable.getSelectionModel().getSelectedItem() != null) {
                    hapusBarang(penjualanDetailTable.getSelectionModel().getSelectedItem());
                }
            }
            if (event.getCode() == KeyCode.SPACE) {
                if (penjualanDetailTable.getSelectionModel().getSelectedItem() != null) {
                    ubahBarang(penjualanDetailTable.getSelectionModel().getSelectedItem(), false);
                }
            }
        });
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                getBarang();
            }
        });
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        penjualanDetailTable.setItems(allBarang);
        searchField.requestFocus();
    }

    public void setKategoriPenjualan(String kategori) {
        kategoriLabel.setText(kategori);
    }

    @FXML
    private void showKeuangan() {
        mainApp.showDataKeuangan();
    }

    @FXML
    public void showPengirimanBarang() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/CariPenjualan.fxml");
        CariPenjualanController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.getPenjualanBelumDikirim();
        x.penjualanHeadTable.setRowFactory(table -> {
            TableRow<PenjualanHead> row = new TableRow<PenjualanHead>() {
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        mainApp.closeDialog(child);

                        showDetailPengiriman(row.getItem().getNoPenjualan());
                    }
                }
            });
            return row;
        });
        x.penjualanHeadTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (x.penjualanHeadTable.getSelectionModel().getSelectedItem() != null) {
                    mainApp.closeDialog(child);
                    showDetailPengiriman(x.penjualanHeadTable.getSelectionModel().getSelectedItem().getNoPenjualan());
                }
            }
        });
    }

    private void showDetailPengiriman(String noPenjualan) {
        Stage child2 = new Stage();
        FXMLLoader loader2 = mainApp.showDialog(mainApp.MainStage, child2, "View/Dialog/DetailPenjualan.fxml");
        DetailPenjualanController x2 = loader2.getController();
        x2.setMainApp(mainApp, child2);
        x2.setDetailPenjualan(noPenjualan, true);
        child2.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                mainApp.closeDialog(child2);
            }
            if (e.getCode() == KeyCode.F6) {
                if (x2.printCheck.isSelected()) {
                    x2.printCheck.setSelected(false);
                } else if (!x2.printCheck.isSelected()) {
                    x2.printCheck.setSelected(true);
                }
            }
            if (e.getCode() == KeyCode.F12) {
                mainApp.closeDialog(child2);
                savePengiriman(x2.p, x2.printCheck.isSelected());
            }
        });
    }

    private void savePengiriman(PenjualanHead p, boolean printCheck) {
        Task<String> task = new Task<String>() {
            @Override
            public String call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    p.setStatusKirim("true");
                    p.setTglKirim(Function.getSystemDate());
                    p.setUserKirim(user.getKodeUser());
                    return Service.savePengiriman(con, p);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((evt) -> {
            mainApp.closeLoading();
            String status = task.getValue();
            if (status.equals("true")) {
                mainApp.showWarning("Warning", "Pengiriman berhasil disimpan");
                if (printCheck) {
                    try {
                        PrintOut po = new PrintOut();
                        po.printStrukPenjualanDirect(p);
                    } catch (Exception e) {
                        mainApp.showWarning("Error", e.toString());
                    }
                }
            } else {
                mainApp.showWarning("Error", status);
            }
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showWarning("Error", task.getException().toString());
        });
        new Thread(task).start();
    }

    @FXML
    public void showPelunasan() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/CariPenjualan.fxml");
        CariPenjualanController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.getPenjualanBelumLunas();
        x.penjualanHeadTable.setRowFactory(table -> {
            TableRow<PenjualanHead> row = new TableRow<PenjualanHead>() {
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        mainApp.closeDialog(child);
                        pembayaran(x.penjualanHeadTable.getSelectionModel().getSelectedItem());
                    }
                }
            });
            return row;
        });
        x.penjualanHeadTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (x.penjualanHeadTable.getSelectionModel().getSelectedItem() != null) {
                    mainApp.closeDialog(child);
                    pembayaran(x.penjualanHeadTable.getSelectionModel().getSelectedItem());
                }
            }
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
                mainApp.showWarning("Warning", "Jumlah yang dibayar melebihi dari sisa pembayaran");
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
                    if (task.getValue().equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showWarning("Warning", "Pembayaran penjualan berhasil disimpan");
                    } else {
                        mainApp.showWarning("Error", task.getValue());
                    }
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showWarning("Error", task.getException().toString());
                });
                new Thread(task).start();
            }
        });
    }

    @FXML
    public void showReturBarang() {
    }

    @FXML
    public void showPenerimaanBarang() {

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
                        Task<Barang> task = new Task<Barang>() {
                            @Override
                            public Barang call() throws Exception {
                                Thread.sleep(timeout);
                                try (Connection con = Koneksi.getConnection()) {
                                    Barang b = row.getItem();
                                    b.setListHarga(HargaDAO.getAllByKodeBarang(con, b.getKodeBarang()));
                                    return b;
                                }
                            }
                        };
                        task.setOnRunning((e) -> {
                            mainApp.showLoadingScreen();
                        });
                        task.setOnSucceeded((WorkerStateEvent e) -> {
                            mainApp.closeLoading();
                            setBarang(task.getValue());
                            mainApp.closeDialog(child);
                        });
                        task.setOnFailed((e) -> {
                            mainApp.closeLoading();
                            mainApp.showWarning("Error", task.getException().toString());
                        });
                        new Thread(task).start();
                    }
                }
            });
            return row;
        });
        x.barangTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (x.barangTable.getSelectionModel().getSelectedItem() != null) {
                    Task<Barang> task = new Task<Barang>() {
                        @Override
                        public Barang call() throws Exception {
                            Thread.sleep(timeout);
                            try (Connection con = Koneksi.getConnection()) {
                                Barang b = x.barangTable.getSelectionModel().getSelectedItem();
                                b.setListHarga(HargaDAO.getAllByKodeBarang(con, b.getKodeBarang()));
                                return b;
                            }
                        }
                    };
                    task.setOnRunning((e) -> {
                        mainApp.showLoadingScreen();
                    });
                    task.setOnSucceeded((WorkerStateEvent e) -> {
                        mainApp.closeLoading();
                        setBarang(task.getValue());
                        mainApp.closeDialog(child);
                    });
                    task.setOnFailed((e) -> {
                        mainApp.closeLoading();
                        mainApp.showWarning("Error", task.getException().toString());
                    });
                    new Thread(task).start();
                }
            }
        });
        child.setOnHiding(event -> {
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
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
                    mainApp.showWarning("Warning", "Kode Barcode " + searchField.getText() + " Tidak Ditemukan");
                } else {
                    if (listBarang.size() == 1) {
                        setBarang(listBarang.get(0));
                    } else {
                        Stage child = new Stage();
                        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/ListBarang.fxml");
                        ListBarangController controller = loader.getController();
                        controller.setMainApp(mainApp, child);
                        controller.setBarang(listBarang, "Penjualan");
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
                            penjualanDetailTable.getSelectionModel().clearSelection();
                        });
                    }
                }
                searchField.setText("");
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showWarning("Error", task.getException().toString());
            });
            new Thread(task).start();
        } else {
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        }
    }

    private void setBarang(Barang b) {
        PenjualanDetail x = null;
        for (PenjualanDetail p : allBarang) {
            if (p.getKodeBarang().equals(b.getKodeBarang())) {
                x = p;
            }
        }
        if (x == null) {
            PenjualanDetail p = new PenjualanDetail();
            p.setBarang(b);
            p.setNoPenjualan("");
            p.setNoUrut(0);
            p.setKodeBarang(b.getKodeBarang());
            p.setNamaBarang(b.getNamaBarang());
            p.setQty(0);
            p.setNilai(0);
            p.setHarga(0);
            p.setTotal(0);
            ubahBarang(p, true);
        } else {
            ubahBarang(x, false);
        }
    }

    public void ubahBarang(PenjualanDetail p, boolean isNew) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/UbahBarang.fxml");
        UbahBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setPenjualan(p);
        child.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                p.setQty(Double.parseDouble(controller.qtyField.getText().replaceAll(",", "")));
                p.setHarga(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                p.setTotal(pembulatan(p.getHarga() * p.getQty()));

                if (isNew) {
                    allBarang.add(0, p);
                }

                penjualanDetailTable.refresh();
                penjualanDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
                mainApp.closeDialog(child);
                event.consume();
            }
        });
        child.setOnHiding(event -> {
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        });
    }

    public void hitungTotal() {
        double total = 0;
        for (PenjualanDetail p : allBarang) {
            total = total + p.getTotal();
        }
        totalPenjualanField.setText(rp.format(total));
    }

    private void hapusBarang(PenjualanDetail p) {
        WarningController warningController = mainApp.showWarning("Confirmation",
                "Hapus barang " + p.getNamaBarang() + " - " + qty.format(p.getQty()) + " ?");
        warningController.OK.setOnAction((event) -> {
            allBarang.remove(p);
            if (allBarang.isEmpty()) {
                cartLabel.setText("");
                cartHead = null;
            }
            penjualanDetailTable.refresh();
            penjualanDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
            mainApp.closeWarning();
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        });
    }

    @FXML
    public void loadCart() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Cart.fxml");
        CartController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        child.setOnHiding(event -> {
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        });
        controller.cartTable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (controller.cartTable.getSelectionModel().getSelectedItem() != null) {
                    if (controller.allBarang.isEmpty()) {
                        mainApp.showWarning("Warning", "Keranjang belanja belum dipilih");
                    } else {
                        WarningController warningController = mainApp.showWarning("Confirmation", "Lanjutkan penjualan pada keranjang - "
                                + controller.cartTable.getSelectionModel().getSelectedItem().getNoCart() + " ?");
                        warningController.OK.setOnAction((ev) -> {
                            Task<String> task = new Task<String>() {
                                @Override
                                public String call() throws Exception {
                                    Thread.sleep(timeout);
                                    try (Connection con = Koneksi.getConnection()) {
                                        String status = "true";
                                        String noCart = "";
                                        List<PenjualanDetail> listPenjualan = new ArrayList<>();
                                        for (CartDetail c : controller.allBarang) {
                                            noCart = c.getNoCart();
                                            PenjualanDetail d = new PenjualanDetail();
                                            d.setNoPenjualan("");
                                            d.setNoUrut(0);
                                            d.setKodeBarang(c.getKodeBarang());
                                            d.setQty(c.getQty());
                                            d.setHarga(c.getHarga());
                                            d.setTotal(c.getTotal());

                                            d.setBarang(BarangDAO.get(con, d.getKodeBarang()));
                                            if (d.getBarang() == null) {
                                                status = d.getKodeBarang() + " - " + c.getNamaBarang() + " tidak ditemukan";
                                            } else {
                                                d.setNamaBarang(d.getBarang().getNamaBarang());
                                            }

                                            d.getBarang().setListHarga(HargaDAO.getAllByKodeBarang(con, d.getKodeBarang()));
                                            listPenjualan.add(d);
                                        }
                                        cartHead = CartHeadDAO.get(con, noCart);
                                        cartHead.setListCartDetail(controller.allBarang);
                                        allBarang.clear();
                                        allBarang.addAll(listPenjualan);
                                        return status;
                                    }
                                }
                            };
                            task.setOnRunning((e) -> {
                                mainApp.showLoadingScreen();
                            });
                            task.setOnSucceeded((e) -> {
                                mainApp.closeLoading();
                                if (task.getValue().equals("true")) {
                                    cartLabel.setText("Cart : " + cartHead.getNoCart());
                                    penjualanDetailTable.refresh();
                                    penjualanDetailTable.getSelectionModel().clearSelection();
                                    hitungTotal();
                                    mainApp.closeWarning();
                                    mainApp.closeDialog(child);
                                } else {
                                    cartHead = null;
                                    cartLabel.setText("");
                                    allBarang.clear();
                                    penjualanDetailTable.refresh();
                                    penjualanDetailTable.getSelectionModel().clearSelection();
                                    hitungTotal();
                                    mainApp.showWarning("Error", task.getValue());
                                }
                                searchField.requestFocus();
                                penjualanDetailTable.getSelectionModel().clearSelection();
                            });
                            task.setOnFailed((e) -> {
                                mainApp.closeLoading();
                                mainApp.showWarning("Error", task.getException().toString());
                            });
                            new Thread(task).start();
                        });
                    }
                }
            }
        });
    }

    @FXML
    public void saveCart() {
        if (allBarang.isEmpty()) {
            mainApp.showWarning("Warning", "Barang masih kosong");
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        } else if (cartHead == null) {
            try (Connection con = Koneksi.getConnection()) {
                String noCart = CartHeadDAO.getId(con);
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/NamaCart.fxml");
                NamaCartController controller = loader.getController();
                controller.setMainApp(mainApp, child);
                controller.setNamaCart(noCart);
                controller.namaCartField.selectAll();
                child.setOnHiding(event -> {
                    searchField.requestFocus();
                    penjualanDetailTable.getSelectionModel().clearSelection();
                });
                controller.saveButton.setOnAction((event) -> {
                    Task<String> task = new Task<String>() {
                        @Override
                        public String call() throws Exception {
                            try (Connection con = Koneksi.getConnection()) {
                                return Service.saveNewCart(con, controller.namaCartField.getText(),
                                        allBarang, null);
                            }
                        }
                    };
                    task.setOnRunning((e) -> {
                        mainApp.showLoadingScreen();
                    });
                    task.setOnSucceeded((WorkerStateEvent e) -> {
                        mainApp.closeLoading();
                        String status = task.getValue();
                        if (status.equals("true")) {
                            mainApp.showWarning("Warning", "Keranjang belanja berhasil disimpan di cart- " + controller.namaCartField.getText());

                            allBarang.clear();
                            cartLabel.setText("");
                            cartHead = null;
                            penjualanDetailTable.refresh();
                            hitungTotal();
                            mainApp.closeDialog(child);
                        } else {
                            mainApp.showWarning("Error", status);
                        }
                        searchField.requestFocus();
                        penjualanDetailTable.getSelectionModel().clearSelection();
                    });
                    task.setOnFailed((e) -> {
                        mainApp.closeLoading();
                        mainApp.showWarning("Error", task.getException().toString());
                    });
                    new Thread(task).start();
                });
            } catch (Exception e) {
                mainApp.showWarning("Error", e.toString());
            }
        } else {
            WarningController warningController = mainApp.showWarning("Confirmation", "Simpan penjualan pada keranjang - "
                    + cartHead.getNoCart() + " ?");
            warningController.OK.setOnAction((event) -> {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        try (Connection con = Koneksi.getConnection()) {
                            return Service.saveNewCart(con, cartHead.getNoCart(),
                                    allBarang, cartHead);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((WorkerStateEvent e) -> {
                    mainApp.closeLoading();
                    String status = task.getValue();
                    if (status.equals("true")) {
                        mainApp.showWarning("Warning", "Keranjang belanja berhasil disimpan di cart- " + cartHead.getNoCart());

                        allBarang.clear();
                        cartLabel.setText("");
                        cartHead = null;
                        penjualanDetailTable.refresh();
                        hitungTotal();
                    } else {
                        mainApp.showWarning("Error", status);
                    }
                    searchField.requestFocus();
                    penjualanDetailTable.getSelectionModel().clearSelection();
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showWarning("Error", task.getException().toString());
                });
                new Thread(task).start();
            });
        }
    }

    @FXML
    public void reset() {
        WarningController warningController = mainApp.showWarning("Confirmation", "Hapus semua barang penjualan ?");
        warningController.OK.setOnAction((event) -> {
            mainApp.closeWarning();

            allBarang.clear();
            cartLabel.setText("");
            cartHead = null;
            penjualanDetailTable.refresh();
            hitungTotal();

            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        });
    }

    @FXML
    public void checkOut() {
        if (allBarang.isEmpty()) {
            mainApp.showWarning("Warning", "Barang masih kosong");
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        } else {
            Stage child2 = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child2, "View/Dialog/TotalPenjualan.fxml");
            TotalPenjualanController controller = loader.getController();
            controller.setMainApp(mainApp, child2);
            child2.setOnHiding(event -> {
                searchField.requestFocus();
                penjualanDetailTable.getSelectionModel().clearSelection();
            });
            controller.setText(kategoriLabel.getText(), totalPenjualanField.getText());
            controller.saveButton.setOnAction((event) -> {
                mainApp.closeDialog(child2);
                savePenjualan(controller.pelanggan, Double.parseDouble(controller.diskonLabel.getText().replaceAll(",", "")),
                        Double.parseDouble(controller.ongkosKirimLabel.getText().replaceAll(",", "")), controller.pengirimanCheck.isSelected(),
                        controller.printCheck.isSelected(),
                        Double.parseDouble(controller.totalPembayaranLabel.getText().replaceAll(",", "")));
            });
        }
    }

    private void savePenjualan(Pelanggan pelanggan, double diskon, double ongkosKirim, boolean pengirimanCheck, boolean printCheck, double pembayaran) {
        PenjualanHead p = new PenjualanHead();
        Task<String> task = new Task<String>() {
            @Override
            public String call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    p.setNoPenjualan("");
                    p.setTglPenjualan(Function.getSystemDate());
                    if (pelanggan != null) {
                        p.setKodePelanggan(pelanggan.getKodePelanggan());
                        p.setPelanggan(pelanggan);
                    } else {
                        p.setKodePelanggan("");
                    }

                    p.setTotalPenjualan(Double.parseDouble(totalPenjualanField.getText().replaceAll(",", "")));
                    p.setOngkos(ongkosKirim);
                    p.setDiskon(diskon);
                    p.setGrandtotal(Double.parseDouble(totalPenjualanField.getText().replaceAll(",", "")) + ongkosKirim - diskon);

                    List<Pembayaran> listPembayaran = new ArrayList<>();
                    Pembayaran pp = new Pembayaran();
                    pp.setJenisPembayaran("Cash");
                    pp.setKeterangan("");
                    pp.setJumlahPembayaran(pembayaran);
                    listPembayaran.add(pp);
                    p.setListPembayaran(listPembayaran);

                    p.setPembayaran(pembayaran);
                    p.setSisaPembayaran(Double.parseDouble(totalPenjualanField.getText().replaceAll(",", "")) + ongkosKirim - diskon - pembayaran);
                    p.setKodeUser(user.getKodeUser());

                    if (pengirimanCheck) {
                        p.setStatusKirim("open");
                        p.setTglKirim("2000-01-01 00:00:00");
                        p.setUserKirim("");
                    } else {
                        p.setStatusKirim("false");
                        p.setTglKirim(Function.getSystemDate());
                        p.setUserKirim(user.getKodeUser());
                    }

                    p.setStatus("true");
                    p.setTglBatal("2000-01-01 00:00:00");
                    p.setUserBatal("");
                    List<PenjualanDetail> allDetail = new ArrayList<>();
                    for (PenjualanDetail d : allBarang) {
                        PenjualanDetail detail = new PenjualanDetail();
                        detail.setBarang(d.getBarang());
                        detail.setNoPenjualan("");
                        detail.setNoUrut(1);
                        detail.setKodeBarang(d.getKodeBarang());
                        detail.setNamaBarang(d.getNamaBarang());
                        detail.setQty(d.getQty());
                        detail.setNilai(0);
                        detail.setHarga(d.getHarga());
                        detail.setTotal(detail.getHarga() * detail.getQty());
                        allDetail.add(detail);
                    }
                    p.setListPenjualanDetail(allDetail);
                    return Service.savePenjualan(con, p, cartHead);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((evt) -> {
            mainApp.closeLoading();
            String status = task.getValue();
            if (status.equals("true")) {
                mainApp.showWarning("Warning", "Penjualan berhasil disimpan");
                if (printCheck) {
                    try {
                        PrintOut po = new PrintOut();
                        po.printStrukPenjualanDirect(p);
                    } catch (Exception e) {
                        mainApp.showWarning("Error", e.toString());
                    }
                }
                allBarang.clear();
                cartLabel.setText("");
                cartHead = null;
                penjualanDetailTable.refresh();
                hitungTotal();
                searchField.requestFocus();
                penjualanDetailTable.getSelectionModel().clearSelection();
            } else {
                mainApp.showWarning("Error", status);
            }
            searchField.requestFocus();
            penjualanDetailTable.getSelectionModel().clearSelection();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showWarning("Error", task.getException().toString());
        });
        new Thread(task).start();
    }

}
