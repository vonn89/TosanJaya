/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.CartDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class CartDetailDAO {
    
    public static List<CartDetail> getAll(Connection con, String noCart)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_cart_detail "
                + " where no_cart = ? ");
        ps.setString(1, noCart);
        List<CartDetail> listCart = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            CartDetail d = new CartDetail();
            d.setNoCart(rs.getString(1));
            d.setNoUrut(rs.getInt(2));
            d.setKodeBarang(rs.getString(3));
            d.setNamaBarang(rs.getString(4));
            d.setQty(rs.getDouble(5));
            d.setHarga(rs.getDouble(6));
            d.setTotal(rs.getDouble(7));
            listCart.add(d);
        }
        return listCart;
    }
    public static void insert(Connection con, CartDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_cart_detail values(?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoCart());
        ps.setInt(2, p.getNoUrut());
        ps.setString(3, p.getKodeBarang());
        ps.setString(4, p.getNamaBarang());
        ps.setDouble(5, p.getQty());
        ps.setDouble(6, p.getHarga());
        ps.setDouble(7, p.getTotal());
        ps.executeUpdate();
    }
    public static void update(Connection con, CartDetail d)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_cart_detail set "
                + " kode_barang = ?, nama_barang = ?, qty = ?, harga = ?, total = ?, "
                + " no_cart= ? and no_urut = ? ");
        ps.setString(1, d.getKodeBarang());
        ps.setString(2, d.getNamaBarang());
        ps.setDouble(3, d.getQty());
        ps.setDouble(4, d.getHarga());
        ps.setDouble(5, d.getTotal());
        ps.setString(6, d.getNoCart());
        ps.setInt(7, d.getNoUrut());
        ps.executeUpdate();
    }
    public static void deleteAll(Connection con, String noCart)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tt_cart_detail where no_cart = ?");
        ps.setString(1, noCart);
        ps.executeUpdate();
    }
}
