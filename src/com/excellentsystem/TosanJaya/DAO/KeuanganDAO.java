/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import static com.excellentsystem.TosanJaya.Main.sistem;
import com.excellentsystem.TosanJaya.Model.Keuangan;
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
 * @author yunaz
 */
public class KeuanganDAO {
    public static Double getSaldoAwal(Connection con, String tanggal,String tipeKeuangan)throws Exception{
        String sql = "select sum(jumlah_rp) from tt_keuangan where left(tgl_keuangan,10) < ? and status = 'true'"; 
        if(!tipeKeuangan.equals("%"))
            sql = sql + " and tipe_keuangan = '"+tipeKeuangan+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tanggal);
        ResultSet rs = ps.executeQuery();
        double saldo = 0;
        if(rs.next()){
            saldo = rs.getDouble(1);
        }
        return saldo;
    }
    public static Double getSaldoAkhir(Connection con, String tanggal,String tipeKeuangan)throws Exception{
        String sql = "select sum(jumlah_rp) from tt_keuangan where left(tgl_keuangan,10) <= ? and status = 'true'"; 
        if(!tipeKeuangan.equals("%"))
            sql = sql + " and tipe_keuangan = '"+tipeKeuangan+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tanggal);
        ResultSet rs = ps.executeQuery();
        double saldo = 0;
        if(rs.next()){
            saldo = rs.getDouble(1);
        }
        return saldo;
    }
    public static List<Keuangan> getAllByTanggalAndTipeKeuangan(Connection con, String tglMulai, String tglAkhir, String status, String tipeKeuangan)throws Exception{
        String sql = "select * from tt_keuangan where left(tgl_keuangan,10) between ? and ? and status = ?"; 
        if(!tipeKeuangan.equals("%"))
            sql = sql + " and tipe_keuangan = '"+tipeKeuangan+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ps.setString(3, status);
        ResultSet rs = ps.executeQuery();
        List<Keuangan> allKeuangan = new ArrayList<>();
        while(rs.next()){
            Keuangan k = new Keuangan();
            k.setNoKeuangan(rs.getString(1));
            k.setTglKeuangan(rs.getString(2));
            k.setTipeKeuangan(rs.getString(3));
            k.setKategori(rs.getString(4));
            k.setKeterangan(rs.getString(5));
            k.setJumlahRp(rs.getDouble(6));
            k.setKodeUser(rs.getString(7));
            k.setStatus(rs.getString(8));
            k.setTglBatal(rs.getString(9));
            k.setUserBatal(rs.getString(10));
            allKeuangan.add(k);
        }
        return allKeuangan;
    } 
//    public static Keuangan getByKategoriAndKeterangan(Connection con, String tipeKeuangan, String kategori,String keterangan)throws Exception{
//        PreparedStatement ps = con.prepareStatement("select * from tt_keuangan where tipe_keuangan=? and kategori=? and keterangan=?");
//        ps.setString(1, tipeKeuangan);
//        ps.setString(2, kategori);
//        ps.setString(3, keterangan);
//        ResultSet rs = ps.executeQuery();
//        Keuangan k = null;
//        if(rs.next()){
//            k = new Keuangan();
//            k.setNoKeuangan(rs.getString(1));
//            k.setTglKeuangan(rs.getString(2));
//            k.setTipeKeuangan(rs.getString(3));
//            k.setKategori(rs.getString(4));
//            k.setKeterangan(rs.getString(5));
//            k.setJumlahRp(rs.getDouble(6));
//            k.setKodeUser(rs.getString(7));
//            k.setStatus(rs.getString(8));
//            k.setTglBatal(rs.getString(9));
//            k.setUserBatal(rs.getString(10));
//        }
//        return k;
//    }
//    public static Keuangan get(Connection con, String no_keuangan)throws Exception{
//        PreparedStatement ps = con.prepareStatement("select * from tt_keuangan where no_keuangan=?");
//        ps.setString(1, no_keuangan);
//        ResultSet rs = ps.executeQuery();
//        Keuangan k = null;
//        if(rs.next()){
//            k = new Keuangan();
//            k.setNoKeuangan(rs.getString(1));
//            k.setTglKeuangan(rs.getString(2));
//            k.setTipeKeuangan(rs.getString(3));
//            k.setKategori(rs.getString(4));
//            k.setKeterangan(rs.getString(5));
//            k.setJumlahRp(rs.getDouble(6));
//            k.setKodeUser(rs.getString(7));
//            k.setStatus(rs.getString(8));
//            k.setTglBatal(rs.getString(9));
//            k.setUserBatal(rs.getString(10));
//        }
//        return k;
//    }
    public static String getId(Connection con)throws Exception{
        DateFormat tglKode = new SimpleDateFormat("yyMMdd");
        DateFormat tglBarang = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("0000");
        PreparedStatement ps = con.prepareStatement("select max(right(no_keuangan,4)) from tt_keuangan "
                + " where left(no_keuangan,9) = ?");
        ps.setString(1, "KK-"+tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "KK-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(rs.getInt(1)+1);
        else
            return "KK-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(1);
    }
    public static void insert(Connection con, Keuangan k)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_keuangan values (?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, k.getNoKeuangan());
        ps.setString(2, k.getTglKeuangan());
        ps.setString(3, k.getTipeKeuangan());
        ps.setString(4, k.getKategori());
        ps.setString(5, k.getKeterangan());
        ps.setDouble(6, k.getJumlahRp());
        ps.setString(7, k.getKodeUser());
        ps.setString(8, k.getStatus());
        ps.setString(9, k.getTglBatal());
        ps.setString(10, k.getUserBatal());
        ps.executeUpdate();
    }
    public static void deleteAll(Connection con, String noKeuangan) throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tt_keuangan where no_keuangan = ?");
        ps.setString(1, noKeuangan);
        ps.executeUpdate();
    }
    
}
