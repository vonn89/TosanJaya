/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Excellent
 */
public class PenyesuaianStokBarangDetail {

    private final StringProperty noPenyesuaian = new SimpleStringProperty();
    private final IntegerProperty noUrut = new SimpleIntegerProperty();
    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final DoubleProperty qty = new SimpleDoubleProperty();
    private final DoubleProperty nilai = new SimpleDoubleProperty();
    private PenyesuaianStokBarangHead penyesuaianStokBarangHead;
    private Barang barang;
    
    public PenyesuaianStokBarangHead getPenyesuaianStokBarangHead() {
        return penyesuaianStokBarangHead;
    }

    public void setPenyesuaianStokBarangHead(PenyesuaianStokBarangHead penyesuaianStokBarangHead) {
        this.penyesuaianStokBarangHead = penyesuaianStokBarangHead;
    }


    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }
    
    public int getNoUrut() {
        return noUrut.get();
    }

    public void setNoUrut(int value) {
        noUrut.set(value);
    }

    public IntegerProperty noUrutProperty() {
        return noUrut;
    }

    public double getNilai() {
        return nilai.get();
    }

    public void setNilai(double value) {
        nilai.set(value);
    }

    public DoubleProperty nilaiProperty() {
        return nilai;
    }


    public double getQty() {
        return qty.get();
    }

    public void setQty(double value) {
        qty.set(value);
    }

    public DoubleProperty qtyProperty() {
        return qty;
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


    public String getNoPenyesuaian() {
        return noPenyesuaian.get();
    }

    public void setNoPenyesuaian(String value) {
        noPenyesuaian.set(value);
    }

    public StringProperty noPenyesuaianProperty() {
        return noPenyesuaian;
    }
    
}
