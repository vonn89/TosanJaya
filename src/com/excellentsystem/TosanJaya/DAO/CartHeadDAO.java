/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.CartHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class CartHeadDAO {

    public static CartHead get(Connection con, String noCart) throws Exception {
        PreparedStatement ps = con.prepareStatement("select * from tt_cart_head where no_cart = ?");
        ps.setString(1, noCart);
        ResultSet rs = ps.executeQuery();
        CartHead c = null;
        while (rs.next()) {
            c = new CartHead();
            c.setNoCart(rs.getString(1));
        }
        return c;
    }

    public static List<CartHead> getAll(Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("select * from tt_cart_head ");
        ResultSet rs = ps.executeQuery();
        List<CartHead> allCart = new ArrayList<>();
        while (rs.next()) {
            CartHead c = new CartHead();
            c.setNoCart(rs.getString(1));
            allCart.add(c);
        }
        return allCart;
    }

    public static String getId(Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("select min(right(a.no_cart,2)+1) as nextID "
                + " from tt_cart_head a left join tt_cart_head b on right(a.no_cart,2) + 1 = right(b.no_cart,2) "
                + " where b.no_cart is null");
        DecimalFormat df = new DecimalFormat("00");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            if (rs.getInt(1) == 100) {
                return "false";
            } else if (rs.getInt(1) == 0) {
                return "C-" + df.format(1);
            } else {
                return "C-" + df.format(rs.getInt(1));
            }
        } else {
            return "false";
        }
    }

    public static void insert(Connection con, CartHead c) throws Exception {
        PreparedStatement ps = con.prepareStatement("insert into tt_cart_head values(?,?,?)");
        ps.setString(1, c.getNoCart());
        ps.executeUpdate();
    }

    public static void delete(Connection con, CartHead c) throws Exception {
        PreparedStatement ps = con.prepareStatement("delete from tt_cart_head where no_cart=?");
        ps.setString(1, c.getNoCart());
        ps.executeUpdate();
    }
}
