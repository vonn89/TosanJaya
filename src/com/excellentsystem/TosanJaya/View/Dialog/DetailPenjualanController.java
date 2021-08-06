/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.DAO.PelangganDAO;
import com.excellentsystem.TosanJaya.DAO.PenjualanDetailDAO;
import com.excellentsystem.TosanJaya.DAO.PenjualanHeadDAO;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.tglLengkap;
import static com.excellentsystem.TosanJaya.Main.tglSql;
import static com.excellentsystem.TosanJaya.Main.timeout;
import com.excellentsystem.TosanJaya.Model.PenjualanDetail;
import com.excellentsystem.TosanJaya.Model.PenjualanHead;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class DetailPenjualanController {

    @FXML
    private GridPane gridPane;
    @FXML
    private VBox pengirimanVBox;
    @FXML
    private TableView<PenjualanDetail> penjualanDetailTable;
    @FXML
    private TableColumn numberColumn = new TableColumn("Number");
    @FXML
    private TableColumn<PenjualanDetail, String> kodeBarangColumn;
    @FXML
    private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> qtyColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> totalColumn;

    @FXML
    private TextField noPenjualanField;
    @FXML
    private TextField tglPenjualanField;
    @FXML
    private TextField pelangganField;
    @FXML
    private TextField kodeUserField;
    @FXML
    private Label totalPenjualanLabel;
    @FXML
    private Label ongkosKirimLabel;
    @FXML
    private Label diskonLabel;
    @FXML
    private Label grandtotalLabel;

    @FXML
    public CheckBox printCheck;

    public PenjualanHead p;
    private Stage stage;
    private Main mainApp;
    public ObservableList<PenjualanDetail> allBarang = FXCollections.observableArrayList();

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
        qtyColumn.setCellFactory(col -> getTableCell(qty));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory(col -> getTableCell(rp));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        totalColumn.setCellFactory(col -> getTableCell(rp));

        final ContextMenu rm = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            penjualanDetailTable.refresh();
        });
        rm.getItems().addAll(refresh);
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
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            penjualanDetailTable.refresh();
                        });
                        rm.getItems().add(detail);
                        rm.getItems().add(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        detailBarang(row.getItem());
                    }
                }
            });
            return row;
        });
        penjualanDetailTable.setItems(allBarang);
    }

    public void setMainApp(Main main, Stage stage) {
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
    }

    public void setDetailPenjualan(String noPenjualan, boolean isPengiriman) {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                Thread.sleep(timeout);
                try (Connection con = Koneksi.getConnection()) {
                    p = PenjualanHeadDAO.get(con, noPenjualan);
                    if (p.getKodePelanggan() != null && !p.getKodePelanggan().equals("")) {
                        p.setPelanggan(PelangganDAO.get(con, p.getKodePelanggan()));
                    }
                    p.setListPenjualanDetail(PenjualanDetailDAO.getAll(con, noPenjualan));
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
                noPenjualanField.setText(p.getNoPenjualan());
                tglPenjualanField.setText(tglLengkap.format(tglSql.parse(p.getTglPenjualan())));
                if (p.getKodePelanggan() != null && !p.getKodePelanggan().equals("")) {
                    pelangganField.setText(p.getPelanggan().getNama());
                }
                kodeUserField.setText(p.getKodeUser());
                totalPenjualanLabel.setText(rp.format(p.getTotalPenjualan()));
                ongkosKirimLabel.setText(rp.format(p.getOngkos()));
                diskonLabel.setText(rp.format(p.getDiskon()));
                grandtotalLabel.setText(rp.format(p.getGrandtotal()));
                allBarang.clear();
                allBarang.addAll(p.getListPenjualanDetail());
                if (isPengiriman) {
                    printCheck.setVisible(true);
                    pengirimanVBox.setVisible(true);
                } else {
                    gridPane.getRowConstraints().get(8).setMinHeight(0);
                    gridPane.getRowConstraints().get(8).setPrefHeight(0);
                    gridPane.getRowConstraints().get(8).setMaxHeight(0);
                    printCheck.setVisible(false);
                    pengirimanVBox.setVisible(false);
                }
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

    private void detailBarang(PenjualanDetail d) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setBarang(d.getKodeBarang());
        x.setViewOnly();
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

}
