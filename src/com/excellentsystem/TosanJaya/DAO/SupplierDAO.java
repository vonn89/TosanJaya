/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class SupplierDAO {
    
    public static Supplier get(Connection con, String kodeSupplier)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_supplier where kode_supplier = ?");
        ps.setString(1, kodeSupplier);
        ResultSet rs = ps.executeQuery();
        Supplier p = null;
        if(rs.next()){
            p = new Supplier();
            p.setKodeSupplier(rs.getString(1));
            p.setNama(rs.getString(2));
            p.setAlamat(rs.getString(3));
            p.setNoTelp(rs.getString(4));
            p.setHutang(rs.getDouble(5));
            p.setStatus(rs.getString(6));
        }
        return p;
    }
    public static List<Supplier> getAllByStatus(Connection con, String status)throws Exception{
        String sql = "select * from tm_supplier ";
        if(!status.equals("%")) 
            sql = sql + " where status = '"+status+"'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Supplier> allSupplier = new ArrayList<>();
        while(rs.next()){
            Supplier p = new Supplier();
            p.setKodeSupplier(rs.getString(1));
            p.setNama(rs.getString(2));
            p.setAlamat(rs.getString(3));
            p.setNoTelp(rs.getString(4));
            p.setHutang(rs.getDouble(5));
            p.setStatus(rs.getString(6));
            allSupplier.add(p);
        }
        return allSupplier;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(kode_supplier,4)) from tm_supplier ");
        DecimalFormat df = new DecimalFormat("0000");
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "S-"+df.format(rs.getInt(1)+1);
        else
            return "S-"+df.format(1);
    }
    public static void insert(Connection con, Supplier b)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_supplier values(?,?,?,?,?,?)");
        ps.setString(1, b.getKodeSupplier());
        ps.setString(2, b.getNama());
        ps.setString(3, b.getAlamat());
        ps.setString(4, b.getNoTelp());
        ps.setDouble(5, b.getHutang());
        ps.setString(6, b.getStatus());
        ps.executeUpdate();
    }
    public static void update(Connection con, Supplier b)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_supplier set "
                + " nama=?, alamat=?, no_telp=?, hutang=?, status=? "
                + " where kode_supplier=? ");
        ps.setString(1, b.getNama());
        ps.setString(2, b.getAlamat());
        ps.setString(3, b.getNoTelp());
        ps.setDouble(4, b.getHutang());
        ps.setString(5, b.getStatus());
        ps.setString(6, b.getKodeSupplier());
        ps.executeUpdate();
    }
    public static void delete(Connection con, String kodeSupplier)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_supplier where kode_supplier=?");
        ps.setString(1, kodeSupplier);
        ps.executeUpdate();
    }
}
