/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.PenerimaanBarangDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PenerimaanBarangDetailDAO {
    
    public static List<PenerimaanBarangDetail> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_penerimaan_barang_detail where no_penerimaan in ( "
                + " select no_penerimaan from tt_penerimaan_barang_head where left(tgl_penerimaan,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenerimaanBarangDetail> listPemesanan = new ArrayList<>();
        while(rs.next()){
            PenerimaanBarangDetail p = new PenerimaanBarangDetail();
            p.setNoPenerimaan(rs.getString(1));
            p.setNoPembelian(rs.getString(2));
            p.setNoUrut(rs.getInt(3));
            p.setKodeBarang(rs.getString(4));
            p.setNamaBarang(rs.getString(5));
            p.setQty(rs.getDouble(6));
            p.setKeterangan(rs.getString(7));
            listPemesanan.add(p);
        }
        return listPemesanan;
    }
    public static List<PenerimaanBarangDetail> getAll(Connection con, String noPemesanan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penerimaan_barang_detail "
                + " where no_penerimaan = ? ");
        ps.setString(1, noPemesanan);
        List<PenerimaanBarangDetail> listPemesanan = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            PenerimaanBarangDetail p = new PenerimaanBarangDetail();
            p.setNoPenerimaan(rs.getString(1));
            p.setNoPembelian(rs.getString(2));
            p.setNoUrut(rs.getInt(3));
            p.setKodeBarang(rs.getString(4));
            p.setNamaBarang(rs.getString(5));
            p.setQty(rs.getDouble(6));
            p.setKeterangan(rs.getString(7));
            listPemesanan.add(p);
        }
        return listPemesanan;
    }
    public static void insert(Connection con, PenerimaanBarangDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penerimaan_barang_detail values(?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenerimaan());
        ps.setString(2, p.getNoPembelian());
        ps.setInt(3, p.getNoUrut());
        ps.setString(4, p.getKodeBarang());
        ps.setString(5, p.getNamaBarang());
        ps.setDouble(6, p.getQty());
        ps.setString(7, p.getKeterangan());
        ps.executeUpdate();
    }
}
