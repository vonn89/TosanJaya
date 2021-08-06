/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.PemesananDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PemesananDetailDAO {
    
    public static List<PemesananDetail> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_pemesanan_detail where no_penjualan in ( "
                + " select no_pemesanan from tt_pemesanan_head where left(tgl_pemesanan,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PemesananDetail> listPemesanan = new ArrayList<>();
        while(rs.next()){
            PemesananDetail p = new PemesananDetail();
            p.setNoPemesanan(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setQty(rs.getDouble(5));
            p.setQtyTerkirim(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPemesanan.add(p);
        }
        return listPemesanan;
    }
    public static List<PemesananDetail> getAll(Connection con, String noPemesanan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pemesanan_detail "
                + " where no_pemesanan = ? ");
        ps.setString(1, noPemesanan);
        List<PemesananDetail> listPemesanan = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            PemesananDetail p = new PemesananDetail();
            p.setNoPemesanan(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setQty(rs.getDouble(5));
            p.setQtyTerkirim(rs.getDouble(6));
            p.setHarga(rs.getDouble(7));
            p.setTotal(rs.getDouble(8));
            listPemesanan.add(p);
        }
        return listPemesanan;
    }
    public static void insert(Connection con, PemesananDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_pemesanan_detail values(?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPemesanan());
        ps.setInt(2, p.getNoUrut());
        ps.setString(3, p.getKodeBarang());
        ps.setString(4, p.getNamaBarang());
        ps.setDouble(5, p.getQty());
        ps.setDouble(6, p.getQtyTerkirim());
        ps.setDouble(7, p.getHarga());
        ps.setDouble(8, p.getTotal());
        ps.executeUpdate();
    }
}
