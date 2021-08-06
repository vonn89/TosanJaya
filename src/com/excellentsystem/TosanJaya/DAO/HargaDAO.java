/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.Harga;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class HargaDAO {
    public static List<Harga> getAll(Connection con)throws Exception{
        String sql = "select * from tm_harga ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Harga> allHarga = new ArrayList<>();
        while(rs.next()){
            Harga h = new Harga();
            h.setKodeBarang(rs.getString(1));
            h.setQtyMin(rs.getDouble(2));
            h.setQtyMax(rs.getDouble(3));
            h.setHarga(rs.getDouble(4));
            allHarga.add(h);
        }
        return allHarga;
    }
    public static List<Harga> getAllByKategoriBarangAndStatus(Connection con, String kategori, String status)throws Exception{
        String sql = "select * from tm_harga where kode_barang !='' ";
        if(!kategori.equals("%")) 
            sql = sql + " and kode_kategori = '"+kategori+"'";
        if(!status.equals("%")) 
            sql = sql + " and status = '"+status+"'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Harga> allHarga = new ArrayList<>();
        while(rs.next()){
            Harga h = new Harga();
            h.setKodeBarang(rs.getString(1));
            h.setQtyMin(rs.getDouble(2));
            h.setQtyMax(rs.getDouble(3));
            h.setHarga(rs.getDouble(4));
            allHarga.add(h);
        }
        return allHarga;
    }
    public static List<Harga> getAllByKodeBarang(Connection con, String kodeBarang)throws Exception{
        String sql = "select * from tm_harga where kode_barang=? ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, kodeBarang);
        ResultSet rs = ps.executeQuery();
        List<Harga> allHarga = new ArrayList<>();
        while(rs.next()){
            Harga h = new Harga();
            h.setKodeBarang(rs.getString(1));
            h.setQtyMin(rs.getDouble(2));
            h.setQtyMax(rs.getDouble(3));
            h.setHarga(rs.getDouble(4));
            allHarga.add(h);
        }
        return allHarga;
    }
    public static Harga get(Connection con, String kodeBarang, double qtyMin, double qtyMax)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_harga where kode_barang=? and qty_min=? and qty_max=?");
        ps.setString(1, kodeBarang);
        ps.setDouble(2, qtyMin);
        ps.setDouble(3, qtyMax);
        ResultSet rs = ps.executeQuery();
        Harga h = null;
        if(rs.next()){
            h = new Harga();
            h.setKodeBarang(rs.getString(1));
            h.setQtyMin(rs.getDouble(2));
            h.setQtyMax(rs.getDouble(3));
            h.setHarga(rs.getDouble(4));
        }
        return h;
    }
    public static void insert(Connection con, Harga h)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_harga values(?,?,?,?)");
        ps.setString(1, h.getKodeBarang());
        ps.setDouble(2, h.getQtyMin());
        ps.setDouble(3, h.getQtyMax());
        ps.setDouble(4, h.getHarga());
        ps.executeUpdate();
    }
    public static void update(Connection con, Harga h)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_harga set harga=? "
                + " where kode_barang=? and qty_min=? and qty_max=? ");
        ps.setDouble(1, h.getHarga());
        ps.setString(2, h.getKodeBarang());
        ps.setDouble(3, h.getQtyMin());
        ps.setDouble(4, h.getQtyMax());
        ps.executeUpdate();
    }
    public static void delete(Connection con, Harga h)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_harga where kode_barang=? and qty_min=? and qty_max=? ");
        ps.setString(1, h.getKodeBarang());
        ps.setDouble(2, h.getQtyMin());
        ps.setDouble(3, h.getQtyMax());
        ps.executeUpdate();
    }
    public static void deleteAllByKodeBarang(Connection con, String kodeBarang)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_harga where kode_barang=?");
        ps.setString(1, kodeBarang);
        ps.executeUpdate();
    }
    public static void deleteAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_harga");
        ps.executeUpdate();
    }
}
