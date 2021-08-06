/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.PembelianDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class PembelianDetailDAO {
    
    public static List<PembelianDetail> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_pembelian_detail where no_pembelian in ( "
                + " select no_pembelian from tt_pembelian_head where left(tgl_pembelian,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PembelianDetail> listPembelian = new ArrayList<>();
        while(rs.next()){
            PembelianDetail p = new PembelianDetail();
            p.setNoPembelian(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setQty(rs.getDouble(5));
            p.setQtyDiterima(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPembelian.add(p);
        }
        return listPembelian;
    }
    public static List<PembelianDetail> getAll(Connection con, String noPembelian)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_detail "
                + " where no_pembelian = ? ");
        ps.setString(1, noPembelian);
        List<PembelianDetail> listPembelian = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            PembelianDetail p = new PembelianDetail();
            p.setNoPembelian(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setQty(rs.getDouble(5));
            p.setQtyDiterima(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPembelian.add(p);
        }
        return listPembelian;
    }
    public static void insert(Connection con, PembelianDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_pembelian_detail values(?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPembelian());
        ps.setInt(2, p.getNoUrut());
        ps.setString(3, p.getKodeBarang());
        ps.setString(4, p.getNamaBarang());
        ps.setDouble(5, p.getQty());
        ps.setDouble(6, p.getQtyDiterima());
        ps.setDouble(7, p.getHarga());
        ps.setDouble(8, p.getTotal());
        ps.executeUpdate();
    }
}
