/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.Report;

import com.excellentsystem.TosanJaya.DAO.BarangDAO;
import com.excellentsystem.TosanJaya.DAO.KategoriBarangDAO;
import com.excellentsystem.TosanJaya.DAO.StokBarangDAO;
import com.excellentsystem.TosanJaya.Function;
import com.excellentsystem.TosanJaya.Koneksi;
import static com.excellentsystem.TosanJaya.Main.rp;
import static com.excellentsystem.TosanJaya.Main.sistem;
import com.excellentsystem.TosanJaya.Model.Barang;
import com.excellentsystem.TosanJaya.Model.KategoriBarang;
import com.excellentsystem.TosanJaya.Model.StokBarang;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class LaporanBarangHampirHabisController {

    @SuppressWarnings("rawtypes")
    private JasperPrint jasperPrint;
    @FXML private ImageView imageView;
    @FXML private TextField pageField;
    @FXML private Label totalPageLabel;
    @FXML private Slider zoomLevel;
    private double zoomFactor;
    private double imageHeight;
    private double imageWidth;

    @FXML private ComboBox<String> kategoriCombo;
    @FXML private TextField searchField;
    private ObservableList<String> allKategori = FXCollections.observableArrayList();
    public void initialize(){
        Function.setNumberField(pageField, rp);
        kategoriCombo.setItems(allKategori);
        getKategori();
    }
    private void getKategori(){
        try(Connection con = Koneksi.getConnection()){
            allKategori.clear();
            allKategori.add("Semua");
            List<KategoriBarang> listKategori = KategoriBarangDAO.getAll(con);
            for(KategoriBarang k : listKategori){
                allKategori.add(k.getKodeKategori());
            }
            kategoriCombo.getSelectionModel().select("Semua");
            getBarang();
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    private void getBarang(){
        try(Connection con = Koneksi.getConnection()){
            String kategori = "%";
            if(!kategoriCombo.getSelectionModel().getSelectedItem().equals("Semua"))
                kategori = kategoriCombo.getSelectionModel().getSelectedItem();
            List<Barang> listBarang = BarangDAO.getAllByKategoriAndStatus(con, kategori, "true");
            List<StokBarang> listStokBarang = StokBarangDAO.getAllByTanggalAndBarang(con, sistem.getTglSystem(), "%");
            List<StokBarang> data = new ArrayList<>();
            for(StokBarang s : listStokBarang){
                for(Barang b : listBarang){
                    if(s.getKodeBarang().equals(b.getKodeBarang()))
                        s.setBarang(b);
                }
                if(s.getBarang()!=null){
                    if(s.getBarang().getStokMinimal()>=s.getStokAkhir())
                        data.add(s);
                }
            }
            List<StokBarang> filterBarang = filterData(data);
            Collections.sort(filterBarang, (o1, o2) -> {
                return ((StokBarang) o1).getBarang().getNamaBarang().compareTo(
                        ((StokBarang) o2).getBarang().getNamaBarang());
            });
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("LaporanBarangHampirHabis.jrxml"));
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(filterBarang);
            Map hash = new HashMap();
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport,hash, beanColDataSource);
            zoomFactor = 1.5d;
            zoomLevel.setValue(100d);
            imageView.setX(0);
            imageView.setY(0);
            imageHeight = jasperPrint.getPageHeight();
            imageWidth = jasperPrint.getPageWidth();
            zoomLevel.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                zoomFactor = newValue.doubleValue() * 1.5 / 100;
                imageView.setFitHeight(imageHeight * zoomFactor);
                imageView.setFitWidth(imageWidth * zoomFactor);
            });
            if(jasperPrint.getPages().size() > 0){
                showImage(1);
                pageField.setText("1");
                totalPageLabel.setText(String.valueOf(jasperPrint.getPages().size()));
            }else{
                imageView.setImage(null);
                pageField.setText("0");
                totalPageLabel.setText("0");
            }
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    private Boolean checkColumn(String column){
        if(column!=null){
            if(column.toLowerCase().contains(searchField.getText().toLowerCase()))
                return true;
        }
        return false;
    }
    private List<StokBarang> filterData(List<StokBarang> listBarang) throws Exception{
        List<StokBarang> filterData = new ArrayList<>();
        for (StokBarang b : listBarang) {
            if (searchField.getText() == null || searchField.getText().equals(""))
                filterData.add(b);
            else{
                if(checkColumn(b.getKodeBarang())||
                    checkColumn(b.getBarang().getKodeKategori())||
                    checkColumn(b.getBarang().getSupplier())||
                    checkColumn(b.getBarang().getNamaBarang()))
                    filterData.add(b);
            }
        }
        return filterData;
    }
    @FXML
    private void changePage(){
        try {
            int pageNumber = Integer.parseInt(pageField.getText().replaceAll(",", ""));
            if(1<=pageNumber && pageNumber<=jasperPrint.getPages().size()){
                showImage(pageNumber);
                pageField.setText(rp.format(pageNumber));
            }else{
                pageField.setText("0");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    public void prevPage(){
        try {
            if(Integer.parseInt(pageField.getText().replaceAll(",", ""))>1){
                int pageNumber = Integer.parseInt(pageField.getText().replaceAll(",", ""))-1;
                showImage(pageNumber);
                pageField.setText(rp.format(pageNumber));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    public void nextPage(){
        try {
            if(Integer.parseInt(pageField.getText().replaceAll(",", ""))<jasperPrint.getPages().size()){
                int pageNumber = Integer.parseInt(pageField.getText().replaceAll(",", ""))+1;
                showImage(pageNumber);
                pageField.setText(rp.format(pageNumber));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    private void firstPage(){
        try {
            if(jasperPrint.getPages().size() > 0){
                showImage(1);
                pageField.setText("1");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    private void lastPage(){
        try {
            if(jasperPrint.getPages().size() > 0){
                showImage(jasperPrint.getPages().size());
                pageField.setText(rp.format(jasperPrint.getPages().size()));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    private void showImage(int pageNumber)throws Exception{
        imageView.setFitHeight(imageHeight * zoomFactor);
        imageView.setFitWidth(imageWidth * zoomFactor);
        BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, pageNumber-1, 2);
        WritableImage fxImage = new WritableImage(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
        imageView.setImage(SwingFXUtils.toFXImage(image, fxImage));
    }
    @FXML
    private void print() {
        try{
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(JRException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    
    
}
