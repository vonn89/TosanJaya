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
 * @author yunaz
 */
public class Sistem {

    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty alamat = new SimpleStringProperty();
    private final StringProperty noTelp = new SimpleStringProperty();
    private final StringProperty website = new SimpleStringProperty();
    private final StringProperty tglSystem = new SimpleStringProperty();
    private final StringProperty printer = new SimpleStringProperty();
    private final StringProperty footnote = new SimpleStringProperty();

    


    public String getFootnote() {
        return footnote.get();
    }

    public void setFootnote(String value) {
        footnote.set(value);
    }

    public StringProperty footnoteProperty() {
        return footnote;
    }


    public String getPrinter() {
        return printer.get();
    }

    public void setPrinter(String value) {
        printer.set(value);
    }

    public StringProperty printerProperty() {
        return printer;
    }


    public String getTglSystem() {
        return tglSystem.get();
    }

    public void setTglSystem(String value) {
        tglSystem.set(value);
    }

    public StringProperty tglSystemProperty() {
        return tglSystem;
    }

    public String getWebsite() {
        return website.get();
    }

    public void setWebsite(String value) {
        website.set(value);
    }

    public StringProperty websiteProperty() {
        return website;
    }

    public String getNoTelp() {
        return noTelp.get();
    }

    public void setNoTelp(String value) {
        noTelp.set(value);
    }

    public StringProperty noTelpProperty() {
        return noTelp;
    }

    public String getAlamat() {
        return alamat.get();
    }

    public void setAlamat(String value) {
        alamat.set(value);
    }

    public StringProperty alamatProperty() {
        return alamat;
    }

    public String getNama() {
        return nama.get();
    }

    public void setNama(String value) {
        nama.set(value);
    }

    public StringProperty namaProperty() {
        return nama;
    }
    
}
