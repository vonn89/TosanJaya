/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.DAO.PelangganDAO;
import com.excellentsystem.TosanJaya.DAO.PenjualanHeadDAO;
import com.excellentsystem.TosanJaya.Function;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import com.excellentsystem.TosanJaya.Koneksi;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.tglLengkap;
import static com.excellentsystem.TosanJaya.Main.tglSql;
import com.excellentsystem.TosanJaya.Model.PenjualanHead;
import java.sql.Connection;
import java.text.ParseException;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
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
 * @author excellent
 */
public class CariPenjualanController {

    @FXML
    public TableView<PenjualanHead> penjualanHeadTable;
    @FXML
    private TableColumn<PenjualanHead, String> noPenjualanColumn;
    @FXML
    private TableColumn<PenjualanHead, String> tglPenjualanColumn;
    @FXML
    private TableColumn<PenjualanHead, String> pelangganColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> totalPenjualanColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> ongkosKirimColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> diskonColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> grandtotalColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> pembayaranColumn;
    @FXML
    private TableColumn<PenjualanHead, Number> sisaPembayaranColumn;
    @FXML
    private TableColumn<PenjualanHead, String> kodeUserColumn;

    @FXML
    private TextField searchField;
    private Main mainApp;
    private Stage stage;
    private final ObservableList<PenjualanHead> allPenjualan = FXCollections.observableArrayList();
    private final ObservableList<PenjualanHead> filterData = FXCollections.observableArrayList();

    public void initialize() {
        noPenjualanColumn.setCellValueFactory(cellData -> cellData.getValue().noPenjualanProperty());
        tglPenjualanColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getTglPenjualan())));
            } catch (ParseException ex) {
                return null;
            }
        });
        tglPenjualanColumn.setComparator(Function.sortDate(tglLengkap));
        pelangganColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPelanggan() != null) {
                return cellData.getValue().getPelanggan().namaProperty();
            } else {
                return null;
            }
        });
        totalPenjualanColumn.setCellValueFactory(cellData -> cellData.getValue().totalPenjualanProperty());
        totalPenjualanColumn.setCellFactory(col -> getTableCell(rp));
        ongkosKirimColumn.setCellValueFactory(cellData -> cellData.getValue().ongkosProperty());
        ongkosKirimColumn.setCellFactory(col -> getTableCell(rp));
        diskonColumn.setCellValueFactory(cellData -> cellData.getValue().diskonProperty());
        diskonColumn.setCellFactory(col -> getTableCell(rp));
        grandtotalColumn.setCellValueFactory(cellData -> cellData.getValue().grandtotalProperty());
        grandtotalColumn.setCellFactory(col -> getTableCell(rp));
        pembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().pembayaranProperty());
        pembayaranColumn.setCellFactory(col -> getTableCell(rp));
        sisaPembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().sisaPembayaranProperty());
        sisaPembayaranColumn.setCellFactory(col -> getTableCell(rp));
        kodeUserColumn.setCellValueFactory(cellData -> cellData.getValue().kodeUserProperty());

        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            penjualanHeadTable.refresh();
        });
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
                        MenuItem detail = new MenuItem("Detail Penjualan");
                        detail.setOnAction((ActionEvent e) -> {
                            detailPenjualan(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            penjualanHeadTable.refresh();
                        });
                        rowMenu.getItems().add(detail);
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
            if (event.getCode() == KeyCode.UP) {
                if (penjualanHeadTable.isFocused() && penjualanHeadTable.getFocusModel().isFocused(0)) {
                    Platform.runLater(() -> {
                        penjualanHeadTable.getSelectionModel().clearSelection();
                        searchField.requestFocus();
                        searchField.selectAll();
                    });
                }
            }
            if (event.getCode() == KeyCode.DOWN) {
                if (!penjualanHeadTable.isFocused()) {
                    Platform.runLater(() -> {
                        penjualanHeadTable.requestFocus();
                        penjualanHeadTable.getSelectionModel().select(0);
                        penjualanHeadTable.getFocusModel().focus(0);
                    });
                }
            }
        });
    }

    public void getPenjualanBelumDikirim() {
        Task<List<PenjualanHead>> task = new Task<List<PenjualanHead>>() {
            @Override
            public List<PenjualanHead> call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllByStatusKirim(con, "open");
                    for (PenjualanHead p : listPenjualanHead) {
                        if (p.getKodePelanggan() != null || !p.getKodePelanggan().equals("")) {
                            p.setPelanggan(PelangganDAO.get(con, p.getKodePelanggan()));
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

    public void getPenjualanBelumLunas() {
        Task<List<PenjualanHead>> task = new Task<List<PenjualanHead>>() {
            @Override
            public List<PenjualanHead> call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllBelumLunas(con);
                    for (PenjualanHead p : listPenjualanHead) {
                        p.setPelanggan(PelangganDAO.get(con, p.getKodePelanggan()));
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
            for (PenjualanHead p : allPenjualan) {
                if (searchField.getText() == null || searchField.getText().equals("")) {
                    filterData.add(p);
                } else {
                    if (checkColumn(p.getNoPenjualan())
                            || checkColumn(tglLengkap.format(tglSql.parse(p.getTglPenjualan())))
                            || checkColumn(p.getKodeUser())
                            || checkColumn(rp.format(p.getTotalPenjualan()))) {
                        filterData.add(p);
                    }
                }
            }
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void detailPenjualan(PenjualanHead p) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailPenjualan.fxml");
        DetailPenjualanController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setDetailPenjualan(p.getNoPenjualan(), false);
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }
}
