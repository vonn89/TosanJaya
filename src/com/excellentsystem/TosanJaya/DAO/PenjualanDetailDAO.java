/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.PenjualanDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PenjualanDetailDAO {
    
    public static List<PenjualanDetail> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_penjualan_detail where no_penjualan in ( "
                + " select no_penjualan from tt_penjualan_head where left(tgl_penjualan,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenjualanDetail> listPenjualan = new ArrayList<>();
        while(rs.next()){
            PenjualanDetail p = new PenjualanDetail();
            p.setNoPenjualan(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setQty(rs.getDouble(5));
            p.setNilai(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static List<PenjualanDetail> getAll(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_detail "
                + " where no_penjualan = ? ");
        ps.setString(1, noPenjualan);
        List<PenjualanDetail> listPenjualan = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            PenjualanDetail p = new PenjualanDetail();
            p.setNoPenjualan(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setQty(rs.getDouble(5));
            p.setNilai(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static void insert(Connection con, PenjualanDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penjualan_detail values(?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenjualan());
        ps.setInt(2, p.getNoUrut());
        ps.setString(3, p.getKodeBarang());
        ps.setString(4, p.getNamaBarang());
        ps.setDouble(5, p.getQty());
        ps.setDouble(6, p.getNilai());
        ps.setDouble(7, p.getHarga());
        ps.setDouble(8, p.getTotal());
        ps.executeUpdate();
    }
}
