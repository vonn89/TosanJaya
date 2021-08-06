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
public class PemesananHead {

    private final StringProperty noPemesanan = new SimpleStringProperty();
    private final StringProperty tglPemesanan = new SimpleStringProperty();
    private final StringProperty jenisPemesanan = new SimpleStringProperty();
    private final StringProperty kodePelanggan = new SimpleStringProperty();
    private final DoubleProperty totalPemesanan = new SimpleDoubleProperty();
    private final DoubleProperty sisaPemesanan = new SimpleDoubleProperty();
    private final DoubleProperty downPayment = new SimpleDoubleProperty();
    private final DoubleProperty sisaDownPayment = new SimpleDoubleProperty();
    private final StringProperty catatan = new SimpleStringProperty();
    private final StringProperty kodeUser = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty tglBatal = new SimpleStringProperty();
    private final StringProperty userBatal = new SimpleStringProperty();

    public String getKodeUser() {
        return kodeUser.get();
    }

    public void setKodeUser(String value) {
        kodeUser.set(value);
    }

    public StringProperty kodeUserProperty() {
        return kodeUser;
    }

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

    public double getSisaDownPayment() {
        return sisaDownPayment.get();
    }

    public void setSisaDownPayment(double value) {
        sisaDownPayment.set(value);
    }

    public DoubleProperty sisaDownPaymentProperty() {
        return sisaDownPayment;
    }

    public double getDownPayment() {
        return downPayment.get();
    }

    public void setDownPayment(double value) {
        downPayment.set(value);
    }

    public DoubleProperty downPaymentProperty() {
        return downPayment;
    }

    public double getSisaPemesanan() {
        return sisaPemesanan.get();
    }

    public void setSisaPemesanan(double value) {
        sisaPemesanan.set(value);
    }

    public DoubleProperty sisaPemesananProperty() {
        return sisaPemesanan;
    }

    public double getTotalPemesanan() {
        return totalPemesanan.get();
    }

    public void setTotalPemesanan(double value) {
        totalPemesanan.set(value);
    }

    public DoubleProperty totalPemesananProperty() {
        return totalPemesanan;
    }

    public String getKodePelanggan() {
        return kodePelanggan.get();
    }

    public void setKodePelanggan(String value) {
        kodePelanggan.set(value);
    }

    public StringProperty kodePelangganProperty() {
        return kodePelanggan;
    }

    public String getJenisPemesanan() {
        return jenisPemesanan.get();
    }

    public void setJenisPemesanan(String value) {
        jenisPemesanan.set(value);
    }

    public StringProperty jenisPemesananProperty() {
        return jenisPemesanan;
    }

    public String getTglPemesanan() {
        return tglPemesanan.get();
    }

    public void setTglPemesanan(String value) {
        tglPemesanan.set(value);
    }

    public StringProperty tglPemesananProperty() {
        return tglPemesanan;
    }

    public String getNoPemesanan() {
        return noPemesanan.get();
    }

    public void setNoPemesanan(String value) {
        noPemesanan.set(value);
    }

    public StringProperty noPemesananProperty() {
        return noPemesanan;
    }
    
}
