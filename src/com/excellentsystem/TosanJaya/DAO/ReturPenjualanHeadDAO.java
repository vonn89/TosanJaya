/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import static com.excellentsystem.TosanJaya.Main.sistem;
import com.excellentsystem.TosanJaya.Model.ReturPenjualanHead;
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
public class ReturPenjualanHeadDAO {
    
    public static List<ReturPenjualanHead> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_retur_penjualan_head where left(tgl_retur,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<ReturPenjualanHead> listPenjualan = new ArrayList<>();
        while(rs.next()){
            ReturPenjualanHead p = new ReturPenjualanHead();
            p.setNoReturPenjualan(rs.getString(1));
            p.setTglRetur(rs.getString(2));
            p.setNoPenjualan(rs.getString(3));
            p.setTotalRetur(rs.getDouble(4));
            p.setPembayaran(rs.getDouble(5));
            p.setSisaPembayaran(rs.getDouble(6));
            p.setKodeUser(rs.getString(7));
            p.setStatus(rs.getString(8));
            p.setTglBatal(rs.getString(9));
            p.setUserBatal(rs.getString(10));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static ReturPenjualanHead get(Connection con, String noRetur)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_retur_penjualan_head "
                + " where no_retur_penjualan = ?");
        ps.setString(1, noRetur);
        ReturPenjualanHead p = null;
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            p = new ReturPenjualanHead();
            p.setNoReturPenjualan(rs.getString(1));
            p.setTglRetur(rs.getString(2));
            p.setNoPenjualan(rs.getString(3));
            p.setTotalRetur(rs.getDouble(4));
            p.setPembayaran(rs.getDouble(5));
            p.setSisaPembayaran(rs.getDouble(6));
            p.setKodeUser(rs.getString(7));
            p.setStatus(rs.getString(8));
            p.setTglBatal(rs.getString(9));
            p.setUserBatal(rs.getString(10));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        DateFormat tglKode = new SimpleDateFormat("yyMMdd");
        DateFormat tglBarang = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("000");
        PreparedStatement ps = con.prepareStatement("select max(right(no_retur_penjualan,3)) from tt_retur_penjualan_head " 
                + " where left(no_retur_penjualan,9) = ?");
        ps.setString(1, "RJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "RJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(rs.getInt(1)+1);
        else
            return "RJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(1);
    }
    public static void insert(Connection con, ReturPenjualanHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_retur_penjualan_head values(?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoReturPenjualan());
        ps.setString(2, p.getTglRetur());
        ps.setString(3, p.getNoPenjualan());
        ps.setDouble(4, p.getTotalRetur());
        ps.setDouble(5, p.getPembayaran());
        ps.setDouble(6, p.getSisaPembayaran());
        ps.setString(7, p.getKodeUser());
        ps.setString(8, p.getStatus());
        ps.setString(9, p.getTglBatal());
        ps.setString(10, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, ReturPenjualanHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_retur_penjualan_head "
                + " set tgl_retur = ?, no_penjualan = ?, total_retur = ?, pembayaran = ?, sisa_pembayaran = ?, "
                + " kode_user = ?, status = ?, tgl_batal = ?, user_batal = ? where no_retur_penjualan = ?");
        ps.setString(1, p.getTglRetur());
        ps.setString(2, p.getNoPenjualan());
        ps.setDouble(3, p.getTotalRetur());
        ps.setDouble(4, p.getPembayaran());
        ps.setDouble(5, p.getSisaPembayaran());
        ps.setString(6, p.getKodeUser());
        ps.setString(7, p.getStatus());
        ps.setString(8, p.getTglBatal());
        ps.setString(9, p.getUserBatal());
        ps.setString(10, p.getNoReturPenjualan());
        ps.executeUpdate();
    }
}
