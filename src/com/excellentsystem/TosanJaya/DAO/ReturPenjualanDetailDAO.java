/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.ReturPenjualanDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class ReturPenjualanDetailDAO {
    
    public static List<ReturPenjualanDetail> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_retur_penjualan_detail where no_retur_penjualan in ( "
                + " select no_retur_penjualan from tt_retur_penjualan_head where left(tgl_retur,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<ReturPenjualanDetail> listPenjualan = new ArrayList<>();
        while(rs.next()){
            ReturPenjualanDetail p = new ReturPenjualanDetail();
            p.setNoPenjualan(rs.getString(1));
            p.setNoReturPenjualan(rs.getString(2));
            p.setNoUrut(rs.getInt(3));
            p.setKodeBarang(rs.getString(4));
            p.setNamaBarang(rs.getString(5));
            p.setQty(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static List<ReturPenjualanDetail> getAll(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_retur_penjualan_detail "
                + " where no_retur_penjualan = ? ");
        ps.setString(1, noPenjualan);
        List<ReturPenjualanDetail> listPenjualan = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            ReturPenjualanDetail p = new ReturPenjualanDetail();
            p.setNoPenjualan(rs.getString(1));
            p.setNoReturPenjualan(rs.getString(2));
            p.setNoUrut(rs.getInt(3));
            p.setKodeBarang(rs.getString(4));
            p.setNamaBarang(rs.getString(5));
            p.setQty(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static void insert(Connection con, ReturPenjualanDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_retur_penjualan_detail values(?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenjualan());
        ps.setInt(2, p.getNoUrut());
        ps.setString(3, p.getKodeBarang());
        ps.setString(4, p.getNamaBarang());
        ps.setDouble(5, p.getQty());
        ps.setDouble(7, p.getHarga());
        ps.setDouble(8, p.getTotal());
        ps.executeUpdate();
    }
}
