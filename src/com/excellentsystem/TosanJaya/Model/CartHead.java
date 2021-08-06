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
public class CartHead {

    private final StringProperty noCart = new SimpleStringProperty();
    private List<CartDetail> listCartDetail;

    public List<CartDetail> getListCartDetail() {
        return listCartDetail;
    }

    public void setListCartDetail(List<CartDetail> listCartDetail) {
        this.listCartDetail = listCartDetail;
    }
    
    public String getNoCart() {
        return noCart.get();
    }

    public void setNoCart(String value) {
        noCart.set(value);
    }

    public StringProperty noCartProperty() {
        return noCart;
    }



    
}
