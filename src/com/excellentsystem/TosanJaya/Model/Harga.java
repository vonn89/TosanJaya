/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Excellent
 */
public class Harga {

    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final DoubleProperty qtyMin = new SimpleDoubleProperty();
    private final DoubleProperty qtyMax = new SimpleDoubleProperty();
    private final DoubleProperty harga = new SimpleDoubleProperty();

    public double getHarga() {
        return harga.get();
    }

    public void setHarga(double value) {
        harga.set(value);
    }

    public DoubleProperty hargaProperty() {
        return harga;
    }

    public double getQtyMax() {
        return qtyMax.get();
    }

    public void setQtyMax(double value) {
        qtyMax.set(value);
    }

    public DoubleProperty qtyMaxProperty() {
        return qtyMax;
    }

    public double getQtyMin() {
        return qtyMin.get();
    }

    public void setQtyMin(double value) {
        qtyMin.set(value);
    }

    public DoubleProperty qtyMinProperty() {
        return qtyMin;
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
