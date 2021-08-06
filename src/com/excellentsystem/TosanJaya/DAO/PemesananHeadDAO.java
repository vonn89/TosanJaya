/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import static com.excellentsystem.TosanJaya.Main.sistem;
import com.excellentsystem.TosanJaya.Model.PemesananHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PemesananHeadDAO {
    
    public static List<PemesananHead> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_pemesanan_head where left(tgl_pemesanan,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PemesananHead> listPenjualan = new ArrayList<>();
        while(rs.next()){
            PemesananHead p = new PemesananHead();
            p.setNoPemesanan(rs.getString(1));
            p.setTglPemesanan(rs.getString(2));
            p.setJenisPemesanan(rs.getString(3));
            p.setKodePelanggan(rs.getString(3));
            p.setTotalPemesanan(rs.getDouble(4));
            p.setSisaPemesanan(rs.getDouble(5));
            p.setDownPayment(rs.getDouble(6));
            p.setSisaDownPayment(rs.getDouble(7));
            p.setCatatan(rs.getString(10));
            p.setKodeUser(rs.getString(10));
            p.setStatus(rs.getString(11));
            p.setTglBatal(rs.getString(12));
            p.setUserBatal(rs.getString(13));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static PemesananHead get(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pemesanan_head "
                + " where no_pemesanan = ?");
        ps.setString(1, noPenjualan);
        PemesananHead p = null;
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            p = new PemesananHead();
            p.setNoPemesanan(rs.getString(1));
            p.setTglPemesanan(rs.getString(2));
            p.setJenisPemesanan(rs.getString(3));
            p.setKodePelanggan(rs.getString(3));
            p.setTotalPemesanan(rs.getDouble(4));
            p.setSisaPemesanan(rs.getDouble(5));
            p.setDownPayment(rs.getDouble(6));
            p.setSisaDownPayment(rs.getDouble(7));
            p.setCatatan(rs.getString(10));
            p.setKodeUser(rs.getString(10));
            p.setStatus(rs.getString(11));
            p.setTglBatal(rs.getString(12));
            p.setUserBatal(rs.getString(13));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        DateFormat tglKode = new SimpleDateFormat("yyMMdd");
        DateFormat tglBarang = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("000");
        PreparedStatement ps = con.prepareStatement("select max(right(no_pemesanan,3)) from tt_pemesanan_head " 
                + " where left(no_pemesanan,10) = ?");
        ps.setString(1, "PSN-"+tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PSN-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(rs.getInt(1)+1);
        else
            return "PSN-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(1);
    }
    public static void insert(Connection con, PemesananHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_pemesanan_head values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPemesanan());
        ps.setString(2, p.getTglPemesanan());
        ps.setString(3, p.getJenisPemesanan());
        ps.setString(4, p.getKodePelanggan());
        ps.setDouble(5, p.getTotalPemesanan());
        ps.setDouble(6, p.getSisaPemesanan());
        ps.setDouble(7, p.getDownPayment());
        ps.setDouble(8, p.getSisaDownPayment());
        ps.setString(9, p.getCatatan());
        ps.setString(10, p.getKodeUser());
        ps.setString(11, p.getStatus());
        ps.setString(12, p.getTglBatal());
        ps.setString(13, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, PemesananHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_pemesanan_head "
                + " set tgl_pemesanan = ?, jenis_pemesanan = ?, kode_pelanggan = ?, total_pemesanan = ?, sisa_pemesanan = ?, down_payment = ?, sisa_down_payment = ?,"
                + " catatan = ?, kode_user = ?, status = ?, tgl_batal = ?, user_batal = ? where no_pemesanan = ?");
        ps.setString(1, p.getTglPemesanan());
        ps.setString(2, p.getJenisPemesanan());
        ps.setString(3, p.getKodePelanggan());
        ps.setDouble(4, p.getTotalPemesanan());
        ps.setDouble(5, p.getSisaPemesanan());
        ps.setDouble(6, p.getDownPayment());
        ps.setDouble(7, p.getSisaDownPayment());
        ps.setString(8, p.getCatatan());
        ps.setString(9, p.getKodeUser());
        ps.setString(10, p.getStatus());
        ps.setString(11, p.getTglBatal());
        ps.setString(12, p.getUserBatal());
        ps.setString(13, p.getNoPemesanan());
        ps.executeUpdate();
    }
}
