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
 * @author Excellent
 */
public class PenjualanHead {

    private final StringProperty noPenjualan = new SimpleStringProperty();
    private final StringProperty tglPenjualan = new SimpleStringProperty();
    private final StringProperty kodePelanggan = new SimpleStringProperty();
    
    private final DoubleProperty totalPenjualan = new SimpleDoubleProperty();
    private final DoubleProperty ongkos = new SimpleDoubleProperty();
    private final DoubleProperty diskon = new SimpleDoubleProperty();
    private final DoubleProperty grandtotal = new SimpleDoubleProperty();
    private final DoubleProperty pembayaran = new SimpleDoubleProperty();
    private final DoubleProperty sisaPembayaran = new SimpleDoubleProperty();
    
    private final StringProperty kodeUser = new SimpleStringProperty();
    private final StringProperty statusKirim = new SimpleStringProperty();
    private final StringProperty tglKirim = new SimpleStringProperty();
    private final StringProperty userKirim = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty tglBatal = new SimpleStringProperty();
    private final StringProperty userBatal = new SimpleStringProperty();
    private List<PenjualanDetail> listPenjualanDetail;
    private List<Pembayaran> listPembayaran;
    private Pelanggan pelanggan;

    public String getUserKirim() {
        return userKirim.get();
    }

    public void setUserKirim(String value) {
        userKirim.set(value);
    }

    public StringProperty userKirimProperty() {
        return userKirim;
    }

    public String getTglKirim() {
        return tglKirim.get();
    }

    public void setTglKirim(String value) {
        tglKirim.set(value);
    }

    public StringProperty tglKirimProperty() {
        return tglKirim;
    }

    public String getStatusKirim() {
        return statusKirim.get();
    }

    public void setStatusKirim(String value) {
        statusKirim.set(value);
    }

    public StringProperty statusKirimProperty() {
        return statusKirim;
    }

    public double getDiskon() {
        return diskon.get();
    }

    public void setDiskon(double value) {
        diskon.set(value);
    }

    public DoubleProperty diskonProperty() {
        return diskon;
    }

    public double getOngkos() {
        return ongkos.get();
    }

    public void setOngkos(double value) {
        ongkos.set(value);
    }

    public DoubleProperty ongkosProperty() {
        return ongkos;
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

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
    }


    
    public double getGrandtotal() {
        return grandtotal.get();
    }

    public void setGrandtotal(double value) {
        grandtotal.set(value);
    }

    public DoubleProperty grandtotalProperty() {
        return grandtotal;
    }


    public List<Pembayaran> getListPembayaran() {
        return listPembayaran;
    }

    public void setListPembayaran(List<Pembayaran> listPembayaran) {
        this.listPembayaran = listPembayaran;
    }
    
    
    public List<PenjualanDetail> getListPenjualanDetail() {
        return listPenjualanDetail;
    }

    public void setListPenjualanDetail(List<PenjualanDetail> listPenjualanDetail) {
        this.listPenjualanDetail = listPenjualanDetail;
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

    public double getSisaPembayaran() {
        return sisaPembayaran.get();
    }

    public void setSisaPembayaran(double value) {
        sisaPembayaran.set(value);
    }

    public DoubleProperty sisaPembayaranProperty() {
        return sisaPembayaran;
    }

    public double getPembayaran() {
        return pembayaran.get();
    }

    public void setPembayaran(double value) {
        pembayaran.set(value);
    }

    public DoubleProperty pembayaranProperty() {
        return pembayaran;
    }

    public double getTotalPenjualan() {
        return totalPenjualan.get();
    }

    public void setTotalPenjualan(double value) {
        totalPenjualan.set(value);
    }

    public DoubleProperty totalPenjualanProperty() {
        return totalPenjualan;
    }


    public String getTglPenjualan() {
        return tglPenjualan.get();
    }

    public void setTglPenjualan(String value) {
        tglPenjualan.set(value);
    }

    public StringProperty tglPenjualanProperty() {
        return tglPenjualan;
    }

    public String getNoPenjualan() {
        return noPenjualan.get();
    }

    public void setNoPenjualan(String value) {
        noPenjualan.set(value);
    }

    public StringProperty noPenjualanProperty() {
        return noPenjualan;
    }
    
}
