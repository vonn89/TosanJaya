/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.View.Dialog;

import com.excellentsystem.TosanJaya.Main;
import static com.excellentsystem.TosanJaya.Main.qty;
import static com.excellentsystem.TosanJaya.Main.rp;
import com.excellentsystem.TosanJaya.Model.Harga;
import com.excellentsystem.TosanJaya.Model.PembelianDetail;
import com.excellentsystem.TosanJaya.Model.PenjualanDetail;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class UbahBarangController {

    @FXML
    private Label namaBarangLabel;
    @FXML
    private Label hintLabel;
    @FXML
    public TextField qtyField;
    @FXML
    public TextField hargaField;
    @FXML
    public TextField totalField;

    private List<Harga> listHarga;

    private Main mainApp;
    private Stage stage;

    public void initialize() {
        qtyField.setOnKeyReleased((event) -> {
            try {
                String string = qtyField.getText();
                if (string.contains("-")) {
                    qtyField.undo();
                } else {
                    if (string.indexOf(".") > 0) {
                        String string2 = string.substring(string.indexOf(".") + 1, string.length());
                        if (string2.contains(".")) {
                            qtyField.undo();
                        } else if (!string2.equals("") && Double.parseDouble(string2) != 0) {
                            qtyField.setText(qty.format(Double.parseDouble(string.replaceAll(",", ""))));
                        }
                    } else {
                        qtyField.setText(qty.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }
                }
                qtyField.end();
            } catch (Exception e) {
                qtyField.undo();
            }
            getHarga();
        });
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
        });
    }

    private void getHarga() {
        double qty = Double.parseDouble(qtyField.getText().replaceAll(",", ""));
        double harga = 0;
        for (Harga h : listHarga) {
            if (h.getQtyMin() <= qty && qty <= h.getQtyMax()) {
                harga = h.getHarga();
            }
        }
        hargaField.setText(rp.format(harga));
        totalField.setText(rp.format(
                Double.parseDouble(qtyField.getText().replaceAll(",", ""))
                * Double.parseDouble(hargaField.getText().replaceAll(",", ""))));
    }

    public void setPenjualan(PenjualanDetail p) {
        listHarga = p.getBarang().getListHarga();

        namaBarangLabel.setText(p.getBarang().getNamaBarang());
        qtyField.setText(qty.format(p.getQty()));
        hargaField.setText(rp.format(p.getHarga()));
        String hint = "";
        if (listHarga != null || !listHarga.isEmpty()) {
            for (Harga h : listHarga) {
                hint = hint + qty.format(h.getQtyMin()) + " - " + qty.format(h.getQtyMax()) + " = Rp " + rp.format(h.getHarga()) + "\n";
            }
        }
        hintLabel.setText(hint);
        getHarga();

        qtyField.requestFocus();
        qtyField.selectAll();
    }

    public void setPembelian(PembelianDetail p) {
//        title.setText(p.getBarang().getNamaBarang());
//        satuanField.setText(p.getBarang().getSatuanDasar());
//        qtyField.setText(qty.format(p.getQty()));
//        nilaiField.setText(rp.format(p.getHarga()));
//
//        qtyField.requestFocus();
//        qtyField.selectAll();
    }

    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }
}
