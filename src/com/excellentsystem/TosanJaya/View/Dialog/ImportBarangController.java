/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import static com.excellentsystem.TosanJaya.Function.createRow;
import static com.excellentsystem.TosanJaya.Function.getTableCell;
import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import com.excellentsystem.TosanJaya.Model.Barang;
import com.excellentsystem.TosanJaya.Model.Harga;
import java.io.File;
import java.io.FileOutputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class ImportBarangController {

    @FXML
    private TableView<Barang> barangTable;
    @FXML
    private TableColumn<Barang, String> kodeKategoriColumn;
    @FXML
    private TableColumn<Barang, String> namaBarangColumn;
    @FXML
    private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML
    private TableColumn<Barang, String> keteranganColumn;
    @FXML
    private TableColumn<Barang, String> satuanDasarColumn;

    @FXML
    private TableView<Harga> hargaTable;
    @FXML
    private TableColumn<Harga, Number> qtyMinColumn;
    @FXML
    private TableColumn<Harga, Number> qtyMaxColumn;
    @FXML
    private TableColumn<Harga, Number> hargaColumn;

    @FXML
    public Button saveButton;

    private Stage stage;
    private Main mainApp;
    public ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private ObservableList<Harga> allHarga = FXCollections.observableArrayList();

    public void initialize() {
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        satuanDasarColumn.setCellValueFactory(cellData -> cellData.getValue().satuanDasarProperty());

        qtyMinColumn.setCellValueFactory(cellData -> cellData.getValue().qtyMinProperty());
        qtyMinColumn.setCellFactory((c) -> getTableCell(qty));
        qtyMaxColumn.setCellValueFactory(cellData -> cellData.getValue().qtyMaxProperty());
        qtyMaxColumn.setCellFactory((c) -> getTableCell(qty));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory((c) -> getTableCell(rp));

        barangTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectBarang(newValue));

        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNewBarang = new MenuItem("Tambah Barang Baru");
        addNewBarang.setOnAction((ActionEvent e) -> {
            newBarang();
        });
        MenuItem importBarang = new MenuItem("Import Excel");
        importBarang.setOnAction((ActionEvent e) -> {
            importBarang();
        });
        MenuItem download = new MenuItem("Download Format Excel");
        download.setOnAction((ActionEvent e) -> {
            exportExcel();
        });
        MenuItem refreshBarang = new MenuItem("Refresh");
        refreshBarang.setOnAction((ActionEvent e) -> {
            barangTable.refresh();
        });
        rowMenu.getItems().addAll(addNewBarang, importBarang, download, refreshBarang);
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
                        MenuItem addNewBarang = new MenuItem("Tambah Barang Baru");
                        addNewBarang.setOnAction((ActionEvent e) -> {
                            newBarang();
                        });
                        MenuItem editBarang = new MenuItem("Ubah Barang");
                        editBarang.setOnAction((ActionEvent e) -> {
                            updateBarang(item);
                        });
                        MenuItem deleteBarang = new MenuItem("Hapus Barang");
                        deleteBarang.setOnAction((ActionEvent e) -> {
                            deleteBarang(item);
                        });
                        MenuItem importBarang = new MenuItem("Import Excel");
                        importBarang.setOnAction((ActionEvent e) -> {
                            importBarang();
                        });
                        MenuItem download = new MenuItem("Download Format Excel");
                        download.setOnAction((ActionEvent e) -> {
                            exportExcel();
                        });
                        MenuItem refreshBarang = new MenuItem("Refresh");
                        refreshBarang.setOnAction((ActionEvent e) -> {
                            barangTable.refresh();
                        });
                        rowMenu.getItems().addAll(addNewBarang, editBarang, deleteBarang, importBarang, download, refreshBarang);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && row.getItem() != null) {
                    updateBarang(row.getItem());
                }
            });
            return row;
        });
        final ContextMenu hargaRowMenu = new ContextMenu();
        MenuItem addNewHarga = new MenuItem("Tambah Harga Baru");
        addNewHarga.setOnAction((ActionEvent e) -> {
            addNewHarga(barangTable.getSelectionModel().getSelectedItem());
        });
        MenuItem refreshHarga = new MenuItem("Refresh");
        refreshHarga.setOnAction((ActionEvent e) -> {
            hargaTable.refresh();
        });
        if (barangTable.getSelectionModel().getSelectedItem() != null) {
            hargaRowMenu.getItems().addAll(addNewHarga);
        }
        hargaRowMenu.getItems().addAll(refreshHarga);
        hargaTable.setContextMenu(hargaRowMenu);
        hargaTable.setRowFactory(table -> {
            TableRow<Harga> row = new TableRow<Harga>() {
                @Override
                public void updateItem(Harga item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNewHarga = new MenuItem("Tambah Harga Baru");
                        addNewHarga.setOnAction((ActionEvent e) -> {
                            addNewHarga(barangTable.getSelectionModel().getSelectedItem());
                        });
                        MenuItem editHarga = new MenuItem("Ubah Harga");
                        editHarga.setOnAction((ActionEvent e) -> {
                            editHarga(barangTable.getSelectionModel().getSelectedItem(), item);
                        });
                        MenuItem deleteHarga = new MenuItem("Hapus Harga");
                        deleteHarga.setOnAction((ActionEvent e) -> {
                            Barang b = barangTable.getSelectionModel().getSelectedItem();
                            b.getListHarga().remove(item);
                            selectBarang(b);
                        });
                        MenuItem refreshHarga = new MenuItem("Refresh");
                        refreshHarga.setOnAction((ActionEvent e) -> {
                            hargaTable.refresh();
                        });
                        rowMenu.getItems().addAll(addNewHarga, editHarga, deleteHarga);
                        rowMenu.getItems().addAll(refreshHarga);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && row.getItem() != null) {
                    editHarga(barangTable.getSelectionModel().getSelectedItem(), row.getItem());
                }
            });
            return row;
        });
    }

    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        barangTable.setItems(allBarang);
        hargaTable.setItems(allHarga);
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }

    private void selectBarang(Barang value) {
        allHarga.clear();
        if (value != null) {
            allHarga.addAll(value.getListHarga());
        } else {
            barangTable.getSelectionModel().clearSelection();
        }
    }

    private void importBarang() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select .xls or .xlsx files");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Document 2007", "*.xlsx"), new FileChooser.ExtensionFilter("Excel Document 1997-2007", "*.xls"));
//        File selectedFile = fileChooser.showOpenDialog(mainApp.MainStage);
//        if (selectedFile != null) {
//            Task<String> task = new Task<String>() {
//                @Override
//                public String call() throws Exception {
//                    String status = "";
//                    List<Barang> listBarang = new ArrayList<>();
//                    String excelFilePath = selectedFile.getPath();
//                    try (FileInputStream inputStream = new FileInputStream(selectedFile)) {
//                        Workbook workbook;
//                        if (excelFilePath.endsWith("xlsx")) {
//                            workbook = new XSSFWorkbook(inputStream);
//                        } else if (excelFilePath.endsWith("xls")) {
//                            workbook = new HSSFWorkbook(inputStream);
//                        } else {
//                            throw new IllegalArgumentException("The specified file is not Excel file");
//                        }
//                        workbook.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                        Sheet firstSheet = workbook.getSheetAt(0);
//                        Iterator<Row> iterator = firstSheet.iterator();
//                        iterator.next();
//                        while (iterator.hasNext()) {
//                            Row row = iterator.next();
//                            int i = 0;
//                            Barang barang = new Barang();
//                            barang.setKodeBarang("New Barang");
//                            barang.setListHarga(new ArrayList<>());
//                            Harga harga = new Harga();
//                            for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//                                if (i == 0) {
//                                    Cell cell = row.getCell(cn);
//                                    if (cell == null) {
//                                        return "kode kategori masih ada yang kosong";
//                                    } else {
//                                        barang.setKodeKategori(cell.getStringCellValue());
//                                    }
//                                } else if (i == 1) {
//                                    Cell cell = row.getCell(cn);
//                                    if (cell == null) {
//                                        return "nama barang masih ada yang kosong";
//                                    } else {
//                                        barang.setNamaBarang(cell.getStringCellValue());
//                                    }
//                                } else if (i == 2) {
//                                    Cell cell = row.getCell(cn);
//                                    if (cell == null) {
//                                        return barang.getNamaBarang() + " masih memiliki kode harga yang kosong";
//                                    } else {
//                                        harga.setQtyMin(cell.getStringCellValue());
//                                    }
//                                } else if (i == 3) {
//                                    Cell cell = row.getCell(cn);
//                                    if (cell == null) {
//                                        harga.setKodeBarcode("");
//                                    } else {
//                                        try {
//                                            harga.setKodeBarcode(new DecimalFormat("#").format(cell.getNumericCellValue()));
//                                        } catch (Exception e) {
//                                            harga.setKodeBarcode(cell.getStringCellValue());
//                                        }
//                                    }
//                                } else if (i == 4) {
//                                    Cell cell = row.getCell(cn);
//                                    if (cell == null) {
//                                        return barang.getNamaBarang() + "-" + harga.getKodeHarga() + " qty masih kosong";
//                                    } else {
//                                        harga.setQty(cell.getNumericCellValue());
//                                    }
//                                } else if (i == 5) {
//                                    Cell cell = row.getCell(cn);
//                                    if (cell == null) {
//                                        harga.setHargaGrosir(0);
//                                    } else {
//                                        harga.setHargaGrosir(cell.getNumericCellValue());
//                                    }
//                                } else if (i == 6) {
//                                    Cell cell = row.getCell(cn);
//                                    if (cell == null) {
//                                        harga.setHargaRetail(0);
//                                    } else {
//                                        harga.setHargaRetail(cell.getNumericCellValue());
//                                    }
//                                }
//                                i = i + 1;
//                            }
//                            if (barang.getKodeKategori() == null || barang.getKodeKategori().equals("")) {
//                                return barang.getNamaBarang() + " kategori masih kosong";
//                            } else if (barang.getNamaBarang() == null || barang.getKodeKategori().equals("")) {
//                                return "Nama barang masih ada yang kosong";
//                            } else if (harga.getKodeHarga() == null || harga.getKodeHarga().equals("")) {
//                                return barang.getNamaBarang() + " harga masih  kosong";
//                            } else if (harga.getQty() == 0) {
//                                return barang.getNamaBarang() + " qty masih kosong";
//                            } else {
//                                boolean statusInput = false;
//                                for (Barang b : listBarang) {
//                                    if (b.getNamaBarang().equalsIgnoreCase(barang.getNamaBarang())) {
//                                        if (b.getKodeKategori().equalsIgnoreCase(barang.getKodeKategori())) {
//                                            //cek dalam satu barang tidak boleh ada 2 harga sama
//                                            for (Harga s : b.getAllHarga()) {
//                                                if (s.getKodeHarga().equals(harga.getKodeHarga())) {
//                                                    status = status + b.getNamaBarang() + " memiliki 2 atau lebih harga " + s.getKodeHarga();
//                                                }
//                                            }
//
//                                            b.getAllHarga().add(harga);
//                                            statusInput = true;
//                                        } else {
//                                            //cek dalam apa ada 2 / lbih barang dengan nama sama tapi kategori berbeda
//                                            status = status + "2 / lebih barang dengan nama " + b.getNamaBarang() + " memiliki kategori berbeda";
//                                        }
//                                    }
//                                }
//                                if (!statusInput) {
//                                    barang.setSupplier("");
//                                    barang.setStokMinimal(0);
//                                    barang.getAllHarga().add(harga);
//                                    listBarang.add(barang);
//                                }
//                            }
//                        }
//                        workbook.close();
//                    }
//                    //cek nama barang sudah ada di list belum?
//                    for (Barang b : listBarang) {
//                        for (Barang barang : allBarang) {
//                            if (b.getNamaBarang().equalsIgnoreCase(barang.getNamaBarang())) {
//                                status = status + b.getNamaBarang() + " sudah terdaftar";
//                            }
//                        }
//                    }
//                    allBarang.addAll(listBarang);
//                    if (status.equals("")) {
//                        return "true";
//                    } else {
//                        return status;
//                    }
//                }
//            };
//            task.setOnRunning((e) -> {
//                mainApp.showLoadingScreen();
//            });
//            task.setOnSucceeded((ev) -> {
//                mainApp.closeLoading();
//                if (!task.getValue().equals("true")) {
//                    mainApp.showMessage(Modality.NONE, "Failed", task.getValue());
//                } else {
//                    allBarang.clear();
//                }
//            });
//            task.setOnFailed((e) -> {
//                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
//                mainApp.closeLoading();
//            });
//            new Thread(task).start();
//        }
    }

    private void exportExcel() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select location to export");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Document 2007", "*.xlsx"),
                    new FileChooser.ExtensionFilter("Excel Document 1997-2007", "*.xls")
            );
            File file = fileChooser.showSaveDialog(mainApp.MainStage);
            if (file != null) {
                Workbook workbook;
                if (file.getPath().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook();
                } else if (file.getPath().endsWith("xls")) {
                    workbook = new HSSFWorkbook();
                } else {
                    throw new IllegalArgumentException("The specified file is not Excel file");
                }
                Sheet sheet = workbook.createSheet("Format Excel - Data Barang");
                int rc = 0;
                int c = 7;

                createRow(workbook, sheet, rc, c, "Header");
                sheet.getRow(rc).getCell(0).setCellValue("Kode Kategori");
                sheet.getRow(rc).getCell(1).setCellValue("Nama Barang");
                sheet.getRow(rc).getCell(2).setCellValue("Harga");
                sheet.getRow(rc).getCell(3).setCellValue("Kode Barcode");
                sheet.getRow(rc).getCell(4).setCellValue("Qty");
                sheet.getRow(rc).getCell(5).setCellValue("Harga Grosir");
                sheet.getRow(rc).getCell(6).setCellValue("Harga Retail");
                rc++;

                for (int i = 0; i < c; i++) {
                    sheet.autoSizeColumn(i);
                }
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }
            }
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
            e.printStackTrace();
        }
    }

    private void newBarang() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setNewBarang();
        x.saveButton.setOnAction((event) -> {
            boolean status = false;
            for (Barang barang : allBarang) {
                if (x.namaBarangField.getText().equalsIgnoreCase(barang.getNamaBarang())) {
                    status = true;
                }
            }
            if (x.namaBarangField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            } else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            } else if (x.allHarga.isEmpty()) {
                mainApp.showMessage(Modality.NONE, "Warning", "Harga barang masih kosong");
            } else if (status) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang " + x.namaBarangField.getText() + " sudah terdaftar");
            } else {
                Barang b = new Barang();
                b.setKodeBarang("New Barang");
                b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                b.setKodeBarcode(x.kodeBarcodeField.getText());
                b.setNamaBarang(x.namaBarangField.getText());
                b.setKeterangan(x.keteranganField.getText());
                b.setSatuanDasar(x.satuanDasarField.getText());
                b.setStatus("true");
                b.setListHarga(x.allHarga);
                allBarang.add(b);
                selectBarang(null);
                mainApp.closeDialog(child);
            }
        });
    }

    private void updateBarang(Barang barang) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setBarang(barang);
        x.namaBarangField.setDisable(true);
        x.saveButton.setOnAction((event) -> {
            if (x.namaBarangField.getText().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            } else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            } else if (x.allHarga.isEmpty()) {
                mainApp.showMessage(Modality.NONE, "Warning", "Harga barang masih kosong");
            } else {
                for (Barang b : allBarang) {
                    if (b.getNamaBarang().equalsIgnoreCase(barang.getNamaBarang())) {
                        b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                        b.setKodeBarcode(x.kodeBarcodeField.getText());
                        b.setNamaBarang(x.namaBarangField.getText());
                        b.setKeterangan(x.keteranganField.getText());
                        b.setSatuanDasar(x.satuanDasarField.getText());
                        b.setStatus("true");
                        b.setListHarga(x.allHarga);
                    }
                }
                selectBarang(null);
                mainApp.closeDialog(child);
            }
        });
    }

    private void deleteBarang(Barang barang) {
        allBarang.remove(barang);
        selectBarang(null);
    }

    private void addNewHarga(Barang barang) {
//        Stage child = new Stage();
//        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/HargaBarang.fxml");
//        HargaBarangController x = loader.getController();
//        x.setMainApp(mainApp, child);
//        x.saveButton.setOnAction((event) -> {
//            if ("".equals(x.kodeHargaField.getText())) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Kode harga masih kosong");
//            } else if ("".equals(x.qtyField.getText()) || Double.parseDouble(x.qtyField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Qty masih kosong");
//            } else if ("".equals(x.hargaRetailField.getText()) || Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
//            } else if ("".equals(x.hargaGrosirField.getText()) || Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
//            } else {
//                Harga harga = null;
//                for (Harga temp : barang.getAllHarga()) {
//                    if (temp.getKodeHarga().equals(x.kodeHargaField.getText())) {
//                        harga = temp;
//                    }
//                }
//                if (harga == null) {
//                    harga = new Harga();
//                    harga.setKodeBarang(barang.getKodeBarang());
//                    harga.setKodeBarcode(x.kodeBarcodeField.getText());
//                    harga.setKodeHarga(x.kodeHargaField.getText());
//                    harga.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
//                    harga.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
//                    harga.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
//                    barang.getAllHarga().add(harga);
//                } else {
//                    Harga newHarga = new Harga();
//                    newHarga.setKodeBarang(barang.getKodeBarang());
//                    newHarga.setKodeBarcode(x.kodeBarcodeField.getText());
//                    newHarga.setKodeHarga(x.kodeHargaField.getText());
//                    newHarga.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
//                    newHarga.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
//                    newHarga.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
//                    barang.getAllHarga().remove(harga);
//                    barang.getAllHarga().add(newHarga);
//                }
//                selectBarang(barang);
//                x.close();
//            }
//        });
    }

    private void editHarga(Barang b, Harga s) {
//        Stage child = new Stage();
//        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/HargaBarang.fxml");
//        HargaBarangController x = loader.getController();
//        x.setMainApp(mainApp, child);
//        x.kodeHargaField.setText(s.getKodeHarga());
//        if (s.getKodeBarang() != null && s.getKodeBarcode() != null && !s.getKodeBarcode().startsWith(s.getKodeBarang())) {
//            x.kodeBarcodeField.setText(s.getKodeBarcode());
//        }
//        x.qtyField.setText(qty.format(s.getQty()));
//        x.hargaRetailField.setText(rp.format(s.getHargaRetail()));
//        x.hargaGrosirField.setText(rp.format(s.getHargaGrosir()));
//        x.kodeHargaField.setDisable(true);
//        x.saveButton.setOnAction((event) -> {
//            if ("".equals(x.qtyField.getText()) || Double.parseDouble(x.qtyField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Qty masih kosong");
//            } else if ("".equals(x.hargaRetailField.getText()) || Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
//            } else if ("".equals(x.hargaGrosirField.getText()) || Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")) <= 0) {
//                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
//            } else {
//                Harga harga = null;
//                for (Harga temp : b.getAllHarga()) {
//                    if (temp.getKodeHarga().equals(x.kodeHargaField.getText())) {
//                        harga = temp;
//                    }
//                }
//                if (harga == null) {
//                    harga = new Harga();
//                    harga.setKodeBarang(b.getKodeBarang());
//                    harga.setKodeBarcode(x.kodeBarcodeField.getText());
//                    harga.setKodeHarga(x.kodeHargaField.getText());
//                    harga.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
//                    harga.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
//                    harga.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
//                    b.getAllHarga().add(harga);
//                } else {
//                    Harga newHarga = new Harga();
//                    newHarga.setKodeBarang(b.getKodeBarang());
//                    newHarga.setKodeBarcode(x.kodeBarcodeField.getText());
//                    newHarga.setKodeHarga(x.kodeHargaField.getText());
//                    newHarga.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
//                    newHarga.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
//                    newHarga.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
//                    b.getAllHarga().remove(harga);
//                    b.getAllHarga().add(newHarga);
//                }
//                selectBarang(b);
//                x.close();
//            }
//        });
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

}
