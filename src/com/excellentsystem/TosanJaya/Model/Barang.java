/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.Model;

import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author yunaz
 */
public class Barang {

    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final StringProperty kodeKategori = new SimpleStringProperty();
    private final StringProperty kodeBarcode = new SimpleStringProperty();
    private final StringProperty namaBarang = new SimpleStringProperty();
    private final StringProperty keterangan = new SimpleStringProperty();
    private final StringProperty satuanDasar = new SimpleStringProperty();
    private final DoubleProperty stok = new SimpleDoubleProperty();
    private final DoubleProperty keep = new SimpleDoubleProperty();
    private final DoubleProperty order = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty();
    private StokBarang stokAkhir;
    private List<Harga> listHarga;

    public List<Harga> getListHarga() {
        return listHarga;
    }

    public void setListHarga(List<Harga> listHarga) {
        this.listHarga = listHarga;
    }

    public String getKodeBarcode() {
        return kodeBarcode.get();
    }

    public void setKodeBarcode(String value) {
        kodeBarcode.set(value);
    }

    public StringProperty kodeBarcodeProperty() {
        return kodeBarcode;
    }

    public double getKeep() {
        return keep.get();
    }

    public void setKeep(double value) {
        keep.set(value);
    }

    public DoubleProperty keepProperty() {
        return keep;
    }

    public double getOrder() {
        return order.get();
    }

    public void setOrder(double value) {
        order.set(value);
    }

    public DoubleProperty orderProperty() {
        return order;
    }

    public double getStok() {
        return stok.get();
    }

    public void setStok(double value) {
        stok.set(value);
    }

    public DoubleProperty stokProperty() {
        return stok;
    }

    public String getSatuanDasar() {
        return satuanDasar.get();
    }

    public void setSatuanDasar(String value) {
        satuanDasar.set(value);
    }

    public StringProperty satuanDasarProperty() {
        return satuanDasar;
    }

    public String getKeterangan() {
        return keterangan.get();
    }

    public void setKeterangan(String value) {
        keterangan.set(value);
    }

    public StringProperty keteranganProperty() {
        return keterangan;
    }

    public StokBarang getStokAkhir() {
        return stokAkhir;
    }

    public void setStokAkhir(StokBarang stokAkhir) {
        this.stokAkhir = stokAkhir;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getNamaBarang() {
        return namaBarang.get();
    }

    public void setNamaBarang(String value) {
        namaBarang.set(value);
    }

    public StringProperty namaBarangProperty() {
        return namaBarang;
    }

    public String getKodeKategori() {
        return kodeKategori.get();
    }

    public void setKodeKategori(String value) {
        kodeKategori.set(value);
    }

    public StringProperty kodeKategoriProperty() {
        return kodeKategori;
    }

    public String getKodeBarang() {
        return kodeBarang.get();
    }

    public void setKodeBarang(String value) {
        kodeBarang.set(value);
    }

    public StringProperty kodeBarangProperty() {
        return kodeBarang;
    }

}
