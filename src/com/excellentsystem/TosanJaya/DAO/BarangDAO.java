/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.Barang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class BarangDAO {
    public static Barang get(Connection con, String kodeBarang)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_barang where kode_barang = ?");
        ps.setString(1, kodeBarang);
        ResultSet rs = ps.executeQuery();
        Barang b = null;
        if(rs.next()){
            b = new Barang();
            b.setKodeBarang(rs.getString(1));
            b.setKodeKategori(rs.getString(2));
            b.setKodeBarcode(rs.getString(3));
            b.setNamaBarang(rs.getString(4));
            b.setKeterangan(rs.getString(5));
            b.setSatuanDasar(rs.getString(6));
            b.setStok(rs.getDouble(7));
            b.setKeep(rs.getDouble(8));
            b.setOrder(rs.getDouble(9));
            b.setStatus(rs.getString(10));
        }
        return b;
    }
    public static List<Barang> getAllByKategoriAndStatus(Connection con, String kategori, String status)throws Exception{
        String sql = "select * from tm_barang where kode_barang !='' ";
        if(!kategori.equals("%")) 
            sql = sql + " and kode_kategori = '"+kategori+"'";
        if(!status.equals("%")) 
            sql = sql + " and status = '"+status+"'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Barang> allBarang = new ArrayList<>();
        while(rs.next()){
            Barang b = new Barang();
            b.setKodeBarang(rs.getString(1));
            b.setKodeKategori(rs.getString(2));
            b.setKodeBarcode(rs.getString(3));
            b.setNamaBarang(rs.getString(4));
            b.setKeterangan(rs.getString(5));
            b.setSatuanDasar(rs.getString(6));
            b.setStok(rs.getDouble(7));
            b.setKeep(rs.getDouble(8));
            b.setOrder(rs.getDouble(9));
            b.setStatus(rs.getString(10));
            allBarang.add(b);
        }
        return allBarang;
    }
    public static List<Barang> getAllByKodeBarcode(Connection con, String kodeBarcode)throws Exception{
        String sql = "select * from tm_barang where status = 'true' ";
        if(!kodeBarcode.equals("%")) 
            sql = sql + " and kode_barcode = '"+kodeBarcode+"'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Barang> allBarang = new ArrayList<>();
        while(rs.next()){
            Barang b = new Barang();
            b.setKodeBarang(rs.getString(1));
            b.setKodeKategori(rs.getString(2));
            b.setKodeBarcode(rs.getString(3));
            b.setNamaBarang(rs.getString(4));
            b.setKeterangan(rs.getString(5));
            b.setSatuanDasar(rs.getString(6));
            b.setStok(rs.getDouble(7));
            b.setKeep(rs.getDouble(8));
            b.setOrder(rs.getDouble(9));
            b.setStatus(rs.getString(10));
            allBarang.add(b);
        }
        return allBarang;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(kode_barang,5)) from tm_barang ");
        DecimalFormat df = new DecimalFormat("00000");
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "B-"+df.format(rs.getInt(1)+1);
        else
            return "B-"+df.format(1);
    }
    public static void insert(Connection con, Barang b)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_barang values(?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, b.getKodeBarang());
        ps.setString(2, b.getKodeKategori());
        ps.setString(3, b.getKodeBarcode());
        ps.setString(4, b.getNamaBarang());
        ps.setString(5, b.getKeterangan());
        ps.setString(6, b.getSatuanDasar());
        ps.setDouble(7, b.getStok());
        ps.setDouble(8, b.getKeep());
        ps.setDouble(9, b.getOrder());
        ps.setString(10, b.getStatus());
        ps.executeUpdate();
    }
    public static void update(Connection con, Barang b)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_barang set "
                + " kode_kategori=?, kode_barcode=?, nama_barang=?, keterangan=?, satuan_dasar=?, stok=?, keep=?, `order`=?, `status`=? "
                + " where kode_barang=?");
        ps.setString(1, b.getKodeKategori());
        ps.setString(2, b.getKodeBarcode());
        ps.setString(3, b.getNamaBarang());
        ps.setString(4, b.getKeterangan());
        ps.setString(5, b.getSatuanDasar());
        ps.setDouble(6, b.getStok());
        ps.setDouble(7, b.getKeep());
        ps.setDouble(8, b.getOrder());
        ps.setString(9, b.getStatus());
        ps.setString(10, b.getKodeBarang());
        ps.executeUpdate();
    }
    public static void deleteAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_barang");
        ps.executeUpdate();
    }
}
