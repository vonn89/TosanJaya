/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.DAO;

import com.excellentsystem.TosanJaya.Model.Sistem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author yunaz
 */
public class SistemDAO {
    public static void update(Connection con, Sistem s)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_system set "
                + " nama=?, alamat=?, no_telp=?, website=?, "
                + " tgl_system=?, printer=?, footnote=?");
        ps.setString(1, s.getNama());
        ps.setString(2, s.getAlamat());
        ps.setString(3, s.getNoTelp());
        ps.setString(4, s.getWebsite());
        ps.setString(5, s.getTglSystem());
        ps.setString(6, s.getPrinter());
        ps.setString(7, s.getFootnote());
        ps.executeUpdate();
    }
    public static Sistem get(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_system ");
        ResultSet rs = ps.executeQuery();
        Sistem s = null;
        if(rs.next()){
            s = new Sistem();
            s.setNama(rs.getString(1));
            s.setAlamat(rs.getString(2));
            s.setNoTelp(rs.getString(3));
            s.setWebsite(rs.getString(4));
            s.setTglSystem(rs.getString(5));
            s.setPrinter(rs.getString(6));
            s.setFootnote(rs.getString(7));
        }
        return s;
    }
}
