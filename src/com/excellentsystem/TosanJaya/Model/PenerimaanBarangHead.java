/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Excellent
 */
public class PenerimaanBarangHead {

    private final StringProperty noPenerimaan = new SimpleStringProperty();
    private final StringProperty tglPenerimaan = new SimpleStringProperty();
    private final StringProperty noPembelian = new SimpleStringProperty();
    private final StringProperty kodeSupplier = new SimpleStringProperty();
    private final StringProperty catatan = new SimpleStringProperty();
    private final StringProperty kodeUser = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty tglBatal = new SimpleStringProperty();
    private final StringProperty userBatal = new SimpleStringProperty();

    public String getUserBatal() {
        return userBatal.get();
    }

    public void setUserBatal(String value) {
        userBatal.set(value);
    }

    public StringProperty userBatalProperty() {
        return userBatal;
    }

    public String getTglBatal() {
        return tglBatal.get();
    }

    public void setTglBatal(String value) {
        tglBatal.set(value);
    }

    public StringProperty tglBatalProperty() {
        return tglBatal;
    }

    public String getKodeUser() {
        return kodeUser.get();
    }

    public void setKodeUser(String value) {
        kodeUser.set(value);
    }

    public StringProperty kodeUserProperty() {
        return kodeUser;
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

    public String getCatatan() {
        return catatan.get();
    }

    public void setCatatan(String value) {
        catatan.set(value);
    }

    public StringProperty catatanProperty() {
        return catatan;
    }

    public String getKodeSupplier() {
        return kodeSupplier.get();
    }

    public void setKodeSupplier(String value) {
        kodeSupplier.set(value);
    }

    public StringProperty kodeSupplierProperty() {
        return kodeSupplier;
    }

    public String getNoPembelian() {
        return noPembelian.get();
    }

    public void setNoPembelian(String value) {
        noPembelian.set(value);
    }

    public StringProperty noPembelianProperty() {
        return noPembelian;
    }

    public String getTglPenerimaan() {
        return tglPenerimaan.get();
    }

    public void setTglPenerimaan(String value) {
        tglPenerimaan.set(value);
    }

    public StringProperty tglPenerimaanProperty() {
        return tglPenerimaan;
    }

    public String getNoPenerimaan() {
        return noPenerimaan.get();
    }

    public void setNoPenerimaan(String value) {
        noPenerimaan.set(value);
    }

    public StringProperty noPenerimaanProperty() {
        return noPenerimaan;
    }
    
}
