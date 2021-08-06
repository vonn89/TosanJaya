/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import static com.excellentsystem.TosanJaya.Main.sistem;
import static com.excellentsystem.TosanJaya.Main.tglBarang;
import static com.excellentsystem.TosanJaya.Main.tglKode;
import com.excellentsystem.TosanJaya.Model.PenerimaanBarangHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class PenerimaanBarangHeadDAO {
    
    public static List<PenerimaanBarangHead> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_penerimaan_barang_head where left(tgl_penerimaan,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenerimaanBarangHead> listPembelian = new ArrayList<>();
        while(rs.next()){
            PenerimaanBarangHead p = new PenerimaanBarangHead();
            p.setNoPenerimaan(rs.getString(1));
            p.setTglPenerimaan(rs.getString(2));
            p.setNoPembelian(rs.getString(3));
            p.setKodeSupplier(rs.getString(4));
            p.setCatatan(rs.getString(5));
            p.setKodeUser(rs.getString(6));
            p.setStatus(rs.getString(7));
            p.setTglBatal(rs.getString(8));
            p.setUserBatal(rs.getString(9));
            listPembelian.add(p);
        }
        return listPembelian;
    }
    public static PenerimaanBarangHead get(Connection con, String noPembelian)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penerimaan_barang_head "
                + " where no_penerimaan = ?");
        ps.setString(1, noPembelian);
        PenerimaanBarangHead p = null;
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            p = new PenerimaanBarangHead();
            p.setNoPenerimaan(rs.getString(1));
            p.setTglPenerimaan(rs.getString(2));
            p.setNoPembelian(rs.getString(3));
            p.setKodeSupplier(rs.getString(4));
            p.setCatatan(rs.getString(5));
            p.setKodeUser(rs.getString(6));
            p.setStatus(rs.getString(7));
            p.setTglBatal(rs.getString(8));
            p.setUserBatal(rs.getString(9));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(no_penerimaan,3)) from tt_penerimaan_barang_head " 
                + " where left(no_penerimaan,9) = ?");
        DecimalFormat df = new DecimalFormat("000");
        ps.setString(1, "PM-"+tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PM-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(rs.getInt(1)+1);
        else
            return "PM-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(1);
    }
    public static void insert(Connection con, PenerimaanBarangHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penerimaan_barang_head values(?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenerimaan());
        ps.setString(2, p.getTglPenerimaan());
        ps.setString(3, p.getNoPembelian());
        ps.setString(4, p.getKodeSupplier());
        ps.setString(5, p.getCatatan());
        ps.setString(6, p.getKodeUser());
        ps.setString(7, p.getStatus());
        ps.setString(8, p.getTglBatal());
        ps.setString(9, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, PenerimaanBarangHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_penerimaan_barang_head set "
                + " tgl_penerimaan = ?, no_pembelian = ?, kode_supplier = ?, catatan = ?, "
                + " kode_user = ?, status = ?, tgl_batal = ?, user_batal = ? where no_penerimaan = ?");
        ps.setString(1, p.getTglPenerimaan());
        ps.setString(2, p.getNoPembelian());
        ps.setString(3, p.getKodeSupplier());
        ps.setString(4, p.getCatatan());
        ps.setString(5, p.getKodeUser());
        ps.setString(6, p.getStatus());
        ps.setString(7, p.getTglBatal());
        ps.setString(8, p.getUserBatal());
        ps.setString(9, p.getNoPenerimaan());
        ps.executeUpdate();
    }
}
