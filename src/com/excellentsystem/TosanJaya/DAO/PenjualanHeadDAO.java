/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import static com.excellentsystem.TosanJaya.Main.sistem;
import com.excellentsystem.TosanJaya.Model.PenjualanHead;
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
public class PenjualanHeadDAO {

    public static List<PenjualanHead> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status) throws Exception {
        String sql = "select * from tt_penjualan_head where left(tgl_penjualan,10) between ? and ? ";
        if (!status.equals("%")) {
            sql = sql + " and status = '" + status + "' ";
        }
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenjualanHead> listPenjualan = new ArrayList<>();
        while (rs.next()) {
            PenjualanHead p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKodePelanggan(rs.getString(3));
            p.setTotalPenjualan(rs.getDouble(4));
            p.setOngkos(rs.getDouble(5));
            p.setDiskon(rs.getDouble(6));
            p.setGrandtotal(rs.getDouble(7));
            p.setPembayaran(rs.getDouble(8));
            p.setSisaPembayaran(rs.getDouble(9));
            p.setKodeUser(rs.getString(10));
            p.setStatusKirim(rs.getString(11));
            p.setTglKirim(rs.getString(12));
            p.setUserKirim(rs.getString(13));
            p.setStatus(rs.getString(14));
            p.setTglBatal(rs.getString(15));
            p.setUserBatal(rs.getString(16));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }

    public static List<PenjualanHead> getAllByStatusKirim(
            Connection con, String status) throws Exception {
        String sql = "select * from tt_penjualan_head where status_kirim = ? and status = 'true'";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();
        List<PenjualanHead> listPenjualan = new ArrayList<>();
        while (rs.next()) {
            PenjualanHead p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKodePelanggan(rs.getString(3));
            p.setTotalPenjualan(rs.getDouble(4));
            p.setOngkos(rs.getDouble(5));
            p.setDiskon(rs.getDouble(6));
            p.setGrandtotal(rs.getDouble(7));
            p.setPembayaran(rs.getDouble(8));
            p.setSisaPembayaran(rs.getDouble(9));
            p.setKodeUser(rs.getString(10));
            p.setStatusKirim(rs.getString(11));
            p.setTglKirim(rs.getString(12));
            p.setUserKirim(rs.getString(13));
            p.setStatus(rs.getString(14));
            p.setTglBatal(rs.getString(15));
            p.setUserBatal(rs.getString(16));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }

    public static List<PenjualanHead> getAllBelumLunas(
            Connection con) throws Exception {
        String sql = "select * from tt_penjualan_head where sisa_pembayaran != 0 and status = 'true'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<PenjualanHead> listPenjualan = new ArrayList<>();
        while (rs.next()) {
            PenjualanHead p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKodePelanggan(rs.getString(3));
            p.setTotalPenjualan(rs.getDouble(4));
            p.setOngkos(rs.getDouble(5));
            p.setDiskon(rs.getDouble(6));
            p.setGrandtotal(rs.getDouble(7));
            p.setPembayaran(rs.getDouble(8));
            p.setSisaPembayaran(rs.getDouble(9));
            p.setKodeUser(rs.getString(10));
            p.setStatusKirim(rs.getString(11));
            p.setTglKirim(rs.getString(12));
            p.setUserKirim(rs.getString(13));
            p.setStatus(rs.getString(14));
            p.setTglBatal(rs.getString(15));
            p.setUserBatal(rs.getString(16));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }

    public static PenjualanHead get(Connection con, String noPenjualan) throws Exception {
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_head "
                + " where no_penjualan = ?");
        ps.setString(1, noPenjualan);
        PenjualanHead p = null;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKodePelanggan(rs.getString(3));
            p.setTotalPenjualan(rs.getDouble(4));
            p.setOngkos(rs.getDouble(5));
            p.setDiskon(rs.getDouble(6));
            p.setGrandtotal(rs.getDouble(7));
            p.setPembayaran(rs.getDouble(8));
            p.setSisaPembayaran(rs.getDouble(9));
            p.setKodeUser(rs.getString(10));
            p.setStatusKirim(rs.getString(11));
            p.setTglKirim(rs.getString(12));
            p.setUserKirim(rs.getString(13));
            p.setStatus(rs.getString(14));
            p.setTglBatal(rs.getString(15));
            p.setUserBatal(rs.getString(16));
        }
        return p;
    }

    public static String getId(Connection con) throws Exception {
        DateFormat tglKode = new SimpleDateFormat("yyMMdd");
        DateFormat tglBarang = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("000");
        PreparedStatement ps = con.prepareStatement("select max(right(no_penjualan,3)) from tt_penjualan_head "
                + " where left(no_penjualan,9) = ?");
        ps.setString(1, "PJ-" + tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return "PJ-" + tglKode.format(tglBarang.parse(sistem.getTglSystem())) + df.format(rs.getInt(1) + 1);
        } else {
            return "PJ-" + tglKode.format(tglBarang.parse(sistem.getTglSystem())) + df.format(1);
        }
    }

    public static void insert(Connection con, PenjualanHead p) throws Exception {
        PreparedStatement ps = con.prepareStatement("insert into tt_penjualan_head values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenjualan());
        ps.setString(2, p.getTglPenjualan());
        ps.setString(3, p.getKodePelanggan());
        ps.setDouble(4, p.getTotalPenjualan());
        ps.setDouble(5, p.getOngkos());
        ps.setDouble(6, p.getDiskon());
        ps.setDouble(7, p.getGrandtotal());
        ps.setDouble(8, p.getPembayaran());
        ps.setDouble(9, p.getSisaPembayaran());
        ps.setString(10, p.getKodeUser());
        ps.setString(11, p.getStatusKirim());
        ps.setString(12, p.getTglKirim());
        ps.setString(13, p.getUserKirim());
        ps.setString(14, p.getStatus());
        ps.setString(15, p.getTglBatal());
        ps.setString(16, p.getUserBatal());
        ps.executeUpdate();
    }

    public static void update(Connection con, PenjualanHead p) throws Exception {
        PreparedStatement ps = con.prepareStatement("update tt_penjualan_head "
                + " set tgl_penjualan = ?, kode_pelanggan = ?, total_penjualan = ?, ongkos = ?, diskon = ?, grandtotal = ?, pembayaran = ?, sisa_pembayaran = ?, "
                + " kode_user = ?, status_kirim = ?, tgl_kirim = ?, user_kirim = ?, status = ?, tgl_batal = ?, user_batal = ? where no_penjualan = ?");
        ps.setString(1, p.getTglPenjualan());
        ps.setString(2, p.getKodePelanggan());
        ps.setDouble(3, p.getTotalPenjualan());
        ps.setDouble(4, p.getOngkos());
        ps.setDouble(5, p.getDiskon());
        ps.setDouble(6, p.getGrandtotal());
        ps.setDouble(7, p.getPembayaran());
        ps.setDouble(8, p.getSisaPembayaran());
        ps.setString(9, p.getKodeUser());
        ps.setString(10, p.getStatusKirim());
        ps.setString(11, p.getTglKirim());
        ps.setString(12, p.getUserKirim());
        ps.setString(13, p.getStatus());
        ps.setString(14, p.getTglBatal());
        ps.setString(15, p.getUserBatal());
        ps.setString(16, p.getNoPenjualan());
        ps.executeUpdate();
    }
}
