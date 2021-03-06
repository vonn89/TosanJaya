/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TosanJaya.Service;

import com.excellentsystem.TosanJaya.DAO.BarangDAO;
import com.excellentsystem.TosanJaya.DAO.CartDetailDAO;
import com.excellentsystem.TosanJaya.DAO.CartHeadDAO;
import com.excellentsystem.TosanJaya.DAO.HargaDAO;
import com.excellentsystem.TosanJaya.DAO.KategoriBarangDAO;
import com.excellentsystem.TosanJaya.DAO.KategoriTransaksiDAO;
import com.excellentsystem.TosanJaya.DAO.KeuanganDAO;
import com.excellentsystem.TosanJaya.DAO.LogBarangDAO;
import com.excellentsystem.TosanJaya.DAO.OtoritasDAO;
import com.excellentsystem.TosanJaya.DAO.PelangganDAO;
import com.excellentsystem.TosanJaya.DAO.PembayaranDAO;
import com.excellentsystem.TosanJaya.DAO.PembelianHeadDAO;
import com.excellentsystem.TosanJaya.DAO.PenjualanDetailDAO;
import com.excellentsystem.TosanJaya.DAO.PenjualanHeadDAO;
import com.excellentsystem.TosanJaya.DAO.SistemDAO;
import com.excellentsystem.TosanJaya.DAO.StokBarangDAO;
import com.excellentsystem.TosanJaya.DAO.SupplierDAO;
import com.excellentsystem.TosanJaya.DAO.UserDAO;
import com.excellentsystem.TosanJaya.Function;
import static com.excellentsystem.TosanJaya.Function.pembulatan;
import static com.excellentsystem.TosanJaya.Main.allUser;
import static com.excellentsystem.TosanJaya.Main.sistem;
import static com.excellentsystem.TosanJaya.Main.tglBarang;
import static com.excellentsystem.TosanJaya.Main.user;
import com.excellentsystem.TosanJaya.Model.Barang;
import com.excellentsystem.TosanJaya.Model.CartDetail;
import com.excellentsystem.TosanJaya.Model.CartHead;
import com.excellentsystem.TosanJaya.Model.Harga;
import com.excellentsystem.TosanJaya.Model.KategoriBarang;
import com.excellentsystem.TosanJaya.Model.KategoriTransaksi;
import com.excellentsystem.TosanJaya.Model.Keuangan;
import com.excellentsystem.TosanJaya.Model.LogBarang;
import com.excellentsystem.TosanJaya.Model.Otoritas;
import com.excellentsystem.TosanJaya.Model.Pelanggan;
import com.excellentsystem.TosanJaya.Model.Pembayaran;
import com.excellentsystem.TosanJaya.Model.PembelianHead;
import com.excellentsystem.TosanJaya.Model.PenjualanDetail;
import com.excellentsystem.TosanJaya.Model.PenjualanHead;
import com.excellentsystem.TosanJaya.Model.PenyesuaianStokBarangHead;
import com.excellentsystem.TosanJaya.Model.Sistem;
import com.excellentsystem.TosanJaya.Model.StokBarang;
import com.excellentsystem.TosanJaya.Model.Supplier;
import com.excellentsystem.TosanJaya.Model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class Service {

    public static String updateStokMasuk(Connection con, String kategori, String keterangan, String kodeBarang,
            double qty, double nilai, String status) throws Exception {
        StokBarang s = StokBarangDAO.get(con, sistem.getTglSystem(), kodeBarang);
        if (s == null) {
            s = new StokBarang();
            s.setTanggal(sistem.getTglSystem());
            s.setKodeBarang(kodeBarang);
            s.setStokAwal(0);
            s.setStokMasuk(qty);
            s.setStokKeluar(0);
            s.setStokAkhir(qty);
            StokBarangDAO.insert(con, s);
        } else {
            s.setStokMasuk(pembulatan(s.getStokMasuk() + qty));
            s.setStokAkhir(pembulatan(s.getStokAkhir() + qty));
            StokBarangDAO.update(con, s);
        }
        double stokAwal = 0;
        double nilaiAwal = 0;
        LogBarang logBefore = LogBarangDAO.getLastByBarang(con, kodeBarang);
        if (logBefore != null) {
            stokAwal = logBefore.getStokAkhir();
            nilaiAwal = logBefore.getNilaiAkhir();
        }
        LogBarang l = new LogBarang();
        l.setTanggal(Function.getSystemDate());
        l.setKodeBarang(kodeBarang);
        l.setKategori(kategori);
        l.setKeterangan(keterangan);
        l.setStokAwal(stokAwal);
        l.setNilaiAwal(nilaiAwal);
        l.setStokMasuk(qty);
        l.setNilaiMasuk(nilai);
        l.setStokKeluar(0);
        l.setNilaiKeluar(0);
        l.setStokAkhir(pembulatan(stokAwal + qty));
        l.setNilaiAkhir(pembulatan(nilaiAwal + nilai));
        LogBarangDAO.insert(con, l);

        return status;
    }

    public static String updateStokKeluar(Connection con, String kategori, String keterangan, String kodeBarang,
            double qty, double nilai, String status) throws Exception {
        StokBarang s = StokBarangDAO.get(con, sistem.getTglSystem(), kodeBarang);
        if (s == null) {
            s = new StokBarang();
            s.setTanggal(sistem.getTglSystem());
            s.setKodeBarang(kodeBarang);
            s.setStokAwal(0);
            s.setStokMasuk(0);
            s.setStokKeluar(qty);
            s.setStokAkhir(-qty);
            StokBarangDAO.insert(con, s);
        } else {
            s.setStokKeluar(pembulatan(s.getStokKeluar() + qty));
            s.setStokAkhir(pembulatan(s.getStokAkhir() - qty));
            StokBarangDAO.update(con, s);

            LogBarang logBefore = LogBarangDAO.getLastByBarang(con, kodeBarang);
            if (logBefore == null) {
                LogBarang l = new LogBarang();
                l.setTanggal(Function.getSystemDate());
                l.setKodeBarang(kodeBarang);
                l.setKategori(kategori);
                l.setKeterangan(keterangan);
                l.setStokAwal(0);
                l.setNilaiAwal(0);
                l.setStokMasuk(0);
                l.setNilaiMasuk(0);
                l.setStokKeluar(qty);
                l.setNilaiKeluar(nilai);
                l.setStokAkhir(pembulatan(-qty));
                l.setNilaiAkhir(pembulatan(-nilai));
                LogBarangDAO.insert(con, l);
            } else {
                double stokAwal = logBefore.getStokAkhir();
                double nilaiAwal = logBefore.getNilaiAkhir();

                LogBarang l = new LogBarang();
                l.setTanggal(Function.getSystemDate());
                l.setKodeBarang(kodeBarang);
                l.setKategori(kategori);
                l.setKeterangan(keterangan);
                l.setStokAwal(stokAwal);
                l.setNilaiAwal(nilaiAwal);
                l.setStokMasuk(0);
                l.setNilaiMasuk(0);
                l.setStokKeluar(qty);
                l.setNilaiKeluar(nilai);
                l.setStokAkhir(pembulatan(stokAwal - qty));
                l.setNilaiAkhir(pembulatan(nilaiAwal - nilai));
                LogBarangDAO.insert(con, l);
            }
        }
        return status;
    }

    public static void insertKeuangan(Connection con, String noKeuangan,
            String tipeKeuangan, String kategori, String keterangan, double jumlahRp, String kodeUser) throws Exception {
        Keuangan k = new Keuangan();
        k.setNoKeuangan(noKeuangan);
        k.setTglKeuangan(Function.getSystemDate());
        k.setTipeKeuangan(tipeKeuangan);
        k.setKategori(kategori);
        k.setKeterangan(keterangan);
        k.setJumlahRp(jumlahRp);
        k.setKodeUser(kodeUser);
        k.setStatus("true");
        k.setTglBatal("2000-01-01 00:00:00");
        k.setUserBatal("");
        KeuanganDAO.insert(con, k);
    }

    public static String setSistem(Connection con) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            sistem = SistemDAO.get(con);
            while (tglBarang.parse(sistem.getTglSystem()).before(tglBarang.parse(tglBarang.format(new Date())))) {
                LocalDate yesterday = LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE);
                LocalDate today = yesterday.plusDays(1);
                StokBarangDAO.insertStokAwal(con, yesterday.toString(), today.toString());
                sistem.setTglSystem(today.toString());
                SistemDAO.update(con, sistem);
            }
            if (tglBarang.parse(sistem.getTglSystem()).after(tglBarang.parse(tglBarang.format(new Date())))) {
                status = "Application error - Tanggal system tidak cocok dengan tanggal komputer";
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String savePengaturanUmum(Connection con, Sistem s) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            SistemDAO.update(con, s);

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveNewUser(Connection con, User u) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            List<User> allUser = UserDAO.getAllByStatus(con, "true");
            for (User user : allUser) {
                if (user.getKodeUser().equals(u.getKodeUser())) {
                    status = "Username sudah terdaftar";
                }
            }
            if (status.equals("true")) {
                UserDAO.insert(con, u);
                for (Otoritas o : u.getOtoritas()) {
                    o.setKodeUser(u.getKodeUser());
                    OtoritasDAO.insert(con, o);
                }
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveUpdateUser(Connection con, User user) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            UserDAO.update(con, user);
            OtoritasDAO.deleteAllByKodeUser(con, user.getKodeUser());
            for (Otoritas o : user.getOtoritas()) {
                OtoritasDAO.insert(con, o);
            }
            allUser = UserDAO.getAllByStatus(con, "true");
            List<Otoritas> listOtoritas = OtoritasDAO.getAll(con);
            for (User u : allUser) {
                List<Otoritas> otoritas = new ArrayList<>();
                for (Otoritas o : listOtoritas) {
                    if (o.getKodeUser().equals(u.getKodeUser())) {
                        otoritas.add(o);
                    }
                }
                u.setOtoritas(otoritas);
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveUpdatePassword(Connection con, User us) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            UserDAO.update(con, us);

            user = UserDAO.get(con, us.getKodeUser());
            allUser = UserDAO.getAllByStatus(con, "true");
            List<Otoritas> listOtoritas = OtoritasDAO.getAll(con);
            for (User u : allUser) {
                List<Otoritas> otoritas = new ArrayList<>();
                for (Otoritas o : listOtoritas) {
                    if (o.getKodeUser().equals(u.getKodeUser())) {
                        otoritas.add(o);
                    }
                }
                u.setOtoritas(otoritas);
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String deleteUser(Connection con, User u) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            u.setStatus("false");
            UserDAO.update(con, u);
            OtoritasDAO.deleteAllByKodeUser(con, u.getKodeUser());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveNewKategoriBarang(Connection con, KategoriBarang k) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            KategoriBarangDAO.insert(con, k);

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String deleteKategoriBarang(Connection con, KategoriBarang k) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            List<Barang> listBarang = BarangDAO.getAllByKategoriAndStatus(con, k.getKodeKategori(), "true");
            if (!listBarang.isEmpty()) {
                status = "tidak dapat dihapus ,karena masih ada barang dengan kategori " + k.getKodeKategori();
            }
            KategoriBarangDAO.delete(con, k);

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveImportBarang(Connection con, List<Barang> listBarang) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            for (Barang b : listBarang) {
                b.setKodeKategori(b.getKodeKategori().toUpperCase());
                if (KategoriBarangDAO.get(con, b.getKodeKategori()) == null) {
                    KategoriBarang k = new KategoriBarang();
                    k.setKodeKategori(b.getKodeKategori());
                    KategoriBarangDAO.insert(con, k);
                }

                b.setKodeBarang(BarangDAO.getId(con));
                b.setStatus("true");
                BarangDAO.insert(con, b);
                for (Harga s : b.getListHarga()) {
                    s.setKodeBarang(b.getKodeBarang());
                    HargaDAO.insert(con, s);
                }
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveNewBarang(Connection con, Barang b) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            b.setKodeBarang(BarangDAO.getId(con));
            BarangDAO.insert(con, b);
            for (Harga s : b.getListHarga()) {
                s.setKodeBarang(b.getKodeBarang());
                HargaDAO.insert(con, s);
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveUpdateBarang(Connection con, Barang b) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            BarangDAO.update(con, b);
            HargaDAO.deleteAllByKodeBarang(con, b.getKodeBarang());
            for (Harga s : b.getListHarga()) {
                s.setKodeBarang(b.getKodeBarang());
                HargaDAO.insert(con, s);
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String deleteBarang(Connection con, Barang b) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            double stok = 0;
            List<StokBarang> allStok = StokBarangDAO.getAllByTanggalAndBarang(con, sistem.getTglSystem(), b.getKodeBarang());
            for (StokBarang s : allStok) {
                stok = stok + s.getStokAkhir();
            }
            if (stok == 0) {
                HargaDAO.deleteAllByKodeBarang(con, b.getKodeBarang());
                b.setStatus("false");
                BarangDAO.update(con, b);
            } else {
                status = "tidak dapat dihapus ,karena stok barang masih ada";
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveNewPelanggan(Connection con, Pelanggan p) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            p.setKodePelanggan(PelangganDAO.getId(con));
            PelangganDAO.insert(con, p);

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveUpdatePelanggan(Connection con, Pelanggan b) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            PelangganDAO.update(con, b);

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String deletePelanggan(Connection con, Pelanggan b) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            if (b.getHutang() == 0 && b.getDeposit() == 0) {
                b.setStatus("false");
                PelangganDAO.update(con, b);
            } else {
                status = "tidak dapat dihapus ,karena pelanggan masih memiliki hutang / deposit";
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveNewSupplier(Connection con, Supplier p) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            p.setKodeSupplier(SupplierDAO.getId(con));
            SupplierDAO.insert(con, p);

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveUpdateSupplier(Connection con, Supplier b) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            SupplierDAO.update(con, b);

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String deleteSupplier(Connection con, Supplier b) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            if (b.getHutang() == 0) {
                b.setStatus("false");
                SupplierDAO.update(con, b);
            } else {
                status = "tidak dapat dihapus ,karena masih ada hutang";
            }

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String savePenyesuaianStokBarang(Connection con, PenyesuaianStokBarangHead p) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

//            p.setNoPenyesuaian(PenyesuaianStokBarangHeadDAO.getId(con));
//            PenyesuaianStokBarangHeadDAO.insert(con, p);
//
//            int i = 1;
//            List<PenyesuaianStokBarangDetail> group = new ArrayList<>();
//            for (PenyesuaianStokBarangDetail d : p.getListPenyesuaianStokBarangDetail()) {
//                d.setNoPenyesuaian(p.getNoPenyesuaian());
//                d.setNoUrut(i);
//                PenyesuaianStokBarangDetailDAO.insert(con, d);
//
//                i++;
//
//                boolean statusGroup = true;
//                for (PenyesuaianStokBarangDetail temp : group) {
//                    if (d.getKodeBarang().equals(temp.getKodeBarang())) {
//                        temp.setQtyStok(temp.getQtyStok() + d.getQtyStok());
//                        temp.setTotalNilai(pembulatan(temp.getTotalNilai() + d.getTotalNilai()));
//                        statusGroup = false;
//                    }
//                }
//                if (statusGroup) {
//                    group.add(d);
//                }
//            }
//            double totalNilai = 0;
//            for (PenyesuaianStokBarangDetail temp : group) {
//                if (p.getJenisPenyesuaian().equals("Penambahan Stok Barang")) {
//                    status = updateStokMasuk(con, "Penyesuaian Stok Barang", p.getNoPenyesuaian(), temp.getKodeBarang(),
//                            temp.getQtyStok(), temp.getTotalNilai(), status);
//                    totalNilai = pembulatan(totalNilai + temp.getTotalNilai());
//                } else if (p.getJenisPenyesuaian().equals("Pengurangan Stok Barang")) {
//                    status = updateStokKeluar(con, "Penyesuaian Stok Barang", p.getNoPenyesuaian(), temp.getKodeBarang(),
//                            temp.getQtyStok(), temp.getTotalNilai(), status);
//                    totalNilai = pembulatan(totalNilai - temp.getTotalNilai());
//                }
//            }
//
//            String nokeuangan = KeuanganDAO.getId(con);
//            insertKeuangan(con, nokeuangan, "Modal", "Penyesuaian Stok Barang", p.getNoPenyesuaian(), totalNilai, p.getKodeUser());
//            insertKeuangan(con, nokeuangan, "Stok Barang", "Penyesuaian Stok Barang", p.getNoPenyesuaian(), totalNilai, p.getKodeUser());
            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                e.printStackTrace();
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveNewCart(Connection con, String namaCart, List<PenjualanDetail> listPenjualan, CartHead cartLama) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            if (cartLama != null) {
                CartHeadDAO.delete(con, cartLama);
                CartDetailDAO.deleteAll(con, cartLama.getNoCart());
            }

            CartHead c = new CartHead();
            c.setNoCart(namaCart);
            CartHeadDAO.insert(con, c);

            int i = 1;
            for (PenjualanDetail p : listPenjualan) {
                CartDetail d = new CartDetail();
                d.setNoCart(namaCart);
                d.setNoUrut(i);
                d.setKodeBarang(p.getKodeBarang());
                d.setNamaBarang(p.getNamaBarang());
                d.setQty(p.getQty());
                d.setHarga(p.getHarga());
                d.setTotal(p.getTotal());

                CartDetailDAO.insert(con, d);
                i++;
            }

            con.commit();
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String deleteCart(Connection con, CartHead c) throws Exception {
        try {
            String status = "true";
            con.setAutoCommit(false);

            CartHeadDAO.delete(con, c);
            CartDetailDAO.deleteAll(con, c.getNoCart());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String savePengiriman(Connection con, PenjualanHead p) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            PenjualanHeadDAO.update(con, p);
            
            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String savePenjualan(Connection con, PenjualanHead p, CartHead c) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            String nokeuangan = KeuanganDAO.getId(con);

            p.setNoPenjualan(PenjualanHeadDAO.getId(con));
            PenjualanHeadDAO.insert(con, p);
            System.out.println(new Date() + " insert penjualan");
            if (p.getPelanggan() != null && !p.getPelanggan().equals("")) {
                Pelanggan plg = PelangganDAO.get(con, p.getKodePelanggan());
                if (plg != null) {
                    plg.setHutang(plg.getHutang() + p.getSisaPembayaran());
                    PelangganDAO.update(con, plg);
                }
            }
            System.out.println(new Date() + " update pelanggan");

            int i = 1;
            double totalNilai = 0;
            for (PenjualanDetail d : p.getListPenjualanDetail()) {
                double nilai = 0;
                d.setNoUrut(i);
                d.setNilai(pembulatan(nilai));
                d.setNoPenjualan(p.getNoPenjualan());
                PenjualanDetailDAO.insert(con, d);

                status = updateStokKeluar(con, "Penjualan", p.getNoPenjualan(), d.getKodeBarang(),
                        d.getQty(), pembulatan(d.getQty() * d.getNilai()), status);
                totalNilai = totalNilai + pembulatan(d.getQty() * d.getNilai());
                i++;
            }
            for (Pembayaran pp : p.getListPembayaran()) {
                pp.setNoPembayaran(PembayaranDAO.getId(con));
                pp.setTglPembayaran(p.getTglPenjualan());
                pp.setNoTransaksi(p.getNoPenjualan());
                pp.setKodeUser(user.getKodeUser());
                pp.setStatus("true");
                pp.setTglBatal("2000-01-01 00:00:00");
                pp.setUserBatal("");

                PembayaranDAO.insert(con, pp);

                insertKeuangan(con, nokeuangan, "Kas", "Penjualan",
                        p.getNoPenjualan(), p.getPembayaran(), p.getKodeUser());
                System.out.println(new Date() + " insert pembayaran");
            }

            insertKeuangan(con, nokeuangan, "Piutang Penjualan", "Penjualan",
                    p.getNoPenjualan(), p.getSisaPembayaran(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Penjualan", "Penjualan",
                    p.getNoPenjualan(), p.getGrandtotal(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "HPP Penjualan", "Penjualan",
                    p.getNoPenjualan(), -totalNilai, p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Stok Barang", "Penjualan",
                    p.getNoPenjualan(), -totalNilai, p.getKodeUser());

            System.out.println(new Date() + " insert keuangan");

            if (c != null) {
                CartHeadDAO.delete(con, c);
                CartDetailDAO.deleteAll(con, c.getNoCart());
            }
            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveBatalPenjualan(Connection con, PenjualanHead p) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            String nokeuangan = KeuanganDAO.getId(con);

            PenjualanHeadDAO.update(con, p);

            if (p.getPelanggan() != null && !p.getPelanggan().equals("")) {
                Pelanggan plg = PelangganDAO.get(con, p.getKodePelanggan());
                if (plg != null) {
                    plg.setHutang(plg.getHutang() - p.getSisaPembayaran());
                    PelangganDAO.update(con, plg);
                }
            }

            double totalNilai = 0;
            for (PenjualanDetail d : p.getListPenjualanDetail()) {
                status = updateStokMasuk(con, "Batal Penjualan", p.getNoPenjualan(), d.getKodeBarang(),
                        d.getQty(), pembulatan(d.getQty() * d.getNilai()), status);
                totalNilai = totalNilai + pembulatan(d.getQty() * d.getNilai());
            }

            p.setListPembayaran(PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPenjualan(), "true"));
            for (Pembayaran pp : p.getListPembayaran()) {
                pp.setStatus("false");
                pp.setTglBatal(p.getTglBatal());
                pp.setUserBatal(p.getUserBatal());
                PembayaranDAO.update(con, pp);

                insertKeuangan(con, nokeuangan, "Kas", "Batal Penjualan",
                        p.getNoPenjualan(), -pp.getJumlahPembayaran(), p.getKodeUser());
            }

            insertKeuangan(con, nokeuangan, "Piutang Penjualan", "Batal Penjualan",
                    p.getNoPenjualan(), -p.getSisaPembayaran(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Penjualan", "Batal Penjualan",
                    p.getNoPenjualan(), -p.getGrandtotal(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "HPP Penjualan", "Batal Penjualan",
                    p.getNoPenjualan(), totalNilai, p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Stok Barang", "Batal Penjualan",
                    p.getNoPenjualan(), totalNilai, p.getKodeUser());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String savePembayaranPenjualan(Connection con, PenjualanHead p, Pembayaran pp) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            String nokeuangan = KeuanganDAO.getId(con);

            PenjualanHeadDAO.update(con, p);

            if (p.getPelanggan() != null && !p.getPelanggan().equals("")) {
                Pelanggan plg = PelangganDAO.get(con, p.getKodePelanggan());
                if (plg != null) {
                    plg.setHutang(plg.getHutang() - pp.getJumlahPembayaran());
                    PelangganDAO.update(con, plg);
                }
            }

            PembayaranDAO.insert(con, pp);

            insertKeuangan(con, nokeuangan, "Piutang Penjualan", "Pembayaran Penjualan",
                    pp.getNoPembayaran(), -pp.getJumlahPembayaran(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Kas", "Pembayaran Penjualan",
                    pp.getNoPembayaran(), pp.getJumlahPembayaran(), p.getKodeUser());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveBatalPembayaranPenjualan(Connection con, Pembayaran pp) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            String nokeuangan = KeuanganDAO.getId(con);
            PenjualanHead p = PenjualanHeadDAO.get(con, pp.getNoTransaksi());
            p.setPembayaran(p.getPembayaran() - pp.getJumlahPembayaran());
            p.setSisaPembayaran(p.getSisaPembayaran() + pp.getJumlahPembayaran());
            PenjualanHeadDAO.update(con, p);

            if (p.getPelanggan() != null && !p.getPelanggan().equals("")) {
                Pelanggan plg = PelangganDAO.get(con, p.getKodePelanggan());
                if (plg != null) {
                    plg.setHutang(plg.getHutang() + pp.getJumlahPembayaran());
                    PelangganDAO.update(con, plg);
                }
            }
            PembayaranDAO.update(con, pp);

            insertKeuangan(con, nokeuangan, "Piutang Penjualan", "Batal Pembayaran Penjualan",
                    pp.getNoPembayaran(), pp.getJumlahPembayaran(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Kas", "Batal Pembayaran Penjualan",
                    pp.getNoPembayaran(), -pp.getJumlahPembayaran(), p.getKodeUser());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String savePembelian(Connection con, PembelianHead p) {
        try {
            String status = "true";
            con.setAutoCommit(false);

//            String nokeuangan = KeuanganDAO.getId(con);
//
//            p.setNoPembelian(PembelianHeadDAO.getId(con));
//            PembelianHeadDAO.insert(con, p);
//
//            int i = 1;
//            List<PembelianDetail> group = new ArrayList<>();
//            for (PembelianDetail d : p.getListPembelianDetail()) {
//                d.setNoPembelian(p.getNoPembelian());
//                d.setNoUrut(i);
//                PembelianDetailDAO.insert(con, d);
//
//                boolean statusGroup = true;
//                for (PembelianDetail temp : group) {
//                    if (d.getKodeBarang().equals(temp.getKodeBarang())) {
//                        temp.setQtyMasuk(pembulatan(temp.getQtyMasuk() + d.getQtyMasuk()));
//                        temp.setTotal(pembulatan(temp.getTotal() + d.getTotal()));
//                        statusGroup = false;
//                    }
//                }
//                if (statusGroup) {
//                    group.add(d);
//                }
//
//                i = i + 1;
//            }
//            for (PembelianDetail d : group) {
//                status = updateStokMasuk(con, "Pembelian", p.getNoPembelian(), d.getKodeBarang(),
//                        d.getQtyMasuk(), d.getTotal(), status);
//                Barang b = BarangDAO.get(con, d.getKodeBarang());
//                b.setHargaBeli(d.getTotal() / d.getQtyMasuk());
//                BarangDAO.update(con, b);
//            }
//
//            insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Pembelian",
//                    p.getNoPembelian(), p.getTotalPembelian(), p.getKodeUser());
//            insertKeuangan(con, nokeuangan, "Stok Barang", "Pembelian",
//                    p.getNoPembelian(), p.getTotalPembelian(), p.getKodeUser());
            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveEditPembelian(Connection con, PembelianHead pbLama, PembelianHead pbBaru) {
        try {
            String status = "true";
            con.setAutoCommit(false);

//            String nokeuangan = KeuanganDAO.getId(con);
//
//            PembelianHeadDAO.update(con, pbLama);
//
//            pbLama.setListPembayaran(PembayaranDAO.getAllByNoTransaksiAndStatus(con, pbLama.getNoPembelian(), "true"));
//            for (Pembayaran pp : pbLama.getListPembayaran()) {
//                pp.setStatus("false");
//                pp.setTglBatal(pbLama.getTglBatal());
//                pp.setUserBatal(pbLama.getUserBatal());
//                PembayaranDAO.update(con, pp);
//
//                insertKeuangan(con, nokeuangan, "Kas", "Batal Pembelian",
//                        pbLama.getNoPembelian(), pp.getJumlahPembayaran(), pbLama.getKodeUser());
//                insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Batal Pembelian",
//                        pbLama.getNoPembelian(), pp.getJumlahPembayaran(), pbLama.getKodeUser());
//            }
//
//            pbLama.setListPembelianDetail(PembelianDetailDAO.getAll(con, pbLama.getNoPembelian()));
//            insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Batal Pembelian",
//                    pbLama.getNoPembelian(), -pbLama.getTotalPembelian(), pbLama.getKodeUser());
//            insertKeuangan(con, nokeuangan, "Stok Barang", "Batal Pembelian",
//                    pbLama.getNoPembelian(), -pbLama.getTotalPembelian(), pbLama.getKodeUser());
//
//            List<PembelianDetail> groupKeluar = new ArrayList<>();
//            for (PembelianDetail d : pbLama.getListPembelianDetail()) {
//                boolean statusGroup = true;
//                for (PembelianDetail temp : groupKeluar) {
//                    if (d.getKodeBarang().equals(temp.getKodeBarang())) {
//                        temp.setQtyMasuk(pembulatan(temp.getQtyMasuk() + d.getQtyMasuk()));
//                        temp.setTotal(pembulatan(temp.getTotal() + d.getTotal()));
//                        statusGroup = false;
//                    }
//                }
//                if (statusGroup) {
//                    groupKeluar.add(d);
//                }
//            }
//            for (PembelianDetail d : groupKeluar) {
//                double stokAwal = 0;
//                double nilaiAwal = 0;
//                LogBarang logBefore = LogBarangDAO.getLastByBarang(con, d.getKodeBarang());
//                if (logBefore != null) {
//                    stokAwal = logBefore.getStokAkhir();
//                    nilaiAwal = logBefore.getNilaiAkhir();
//                }
//                LogBarang l = new LogBarang();
//                l.setTanggal(Function.getSystemDate());
//                l.setKodeBarang(d.getKodeBarang());
//                l.setKategori("Batal Pembelian");
//                l.setKeterangan(pbLama.getNoPembelian());
//                l.setStokAwal(stokAwal);
//                l.setNilaiAwal(nilaiAwal);
//                l.setStokMasuk(0);
//                l.setNilaiMasuk(0);
//                l.setStokKeluar(d.getQtyMasuk());
//                l.setNilaiKeluar(d.getTotal());
//                l.setStokAkhir(pembulatan(stokAwal - d.getQtyMasuk()));
//                l.setNilaiAkhir(pembulatan(nilaiAwal - d.getTotal()));
//                LogBarangDAO.insert(con, l);
//
//                PembelianDetail detailLama = PembelianDetailDAO.getLastPembelianByBarang(con, d.getKodeBarang());
//                double hargaLama = 0;
//                if (detailLama != null) {
//                    hargaLama = detailLama.getTotal() / detailLama.getQtyMasuk();
//                }
//                Barang b = BarangDAO.get(con, d.getKodeBarang());
//                b.setHargaBeli(hargaLama);
//                BarangDAO.update(con, b);
//            }
//
//            pbBaru.setNoPembelian(PembelianHeadDAO.getId(con));
//            PembelianHeadDAO.insert(con, pbBaru);
//
//            List<PembelianDetail> groupMasuk = new ArrayList<>();
//            for (PembelianDetail d : pbBaru.getListPembelianDetail()) {
//                d.setNoPembelian(pbBaru.getNoPembelian());
//                PembelianDetailDAO.insert(con, d);
//
//                boolean statusGroup = true;
//                for (PembelianDetail temp : groupMasuk) {
//                    if (d.getKodeBarang().equals(temp.getKodeBarang())) {
//                        temp.setQtyMasuk(pembulatan(temp.getQtyMasuk() + d.getQtyMasuk()));
//                        temp.setTotal(pembulatan(temp.getTotal() + d.getTotal()));
//                        statusGroup = false;
//                    }
//                }
//                if (statusGroup) {
//                    groupMasuk.add(d);
//                }
//            }
//            for (PembelianDetail d : groupMasuk) {
//                double stokAwal = 0;
//                double nilaiAwal = 0;
//                LogBarang logBefore = LogBarangDAO.getLastByBarang(con, d.getKodeBarang());
//                if (logBefore != null) {
//                    stokAwal = logBefore.getStokAkhir();
//                    nilaiAwal = logBefore.getNilaiAkhir();
//                }
//
//                LogBarang l = new LogBarang();
//                l.setTanggal(Function.getSystemDate());
//                l.setKodeBarang(d.getKodeBarang());
//                l.setKategori("Pembelian");
//                l.setKeterangan(pbBaru.getNoPembelian());
//                l.setStokAwal(stokAwal);
//                l.setNilaiAwal(nilaiAwal);
//                l.setStokMasuk(d.getQtyMasuk());
//                l.setNilaiMasuk(d.getTotal());
//                l.setStokKeluar(0);
//                l.setNilaiKeluar(0);
//                l.setStokAkhir(pembulatan(stokAwal + d.getQtyMasuk()));
//                l.setNilaiAkhir(pembulatan(nilaiAwal + d.getTotal()));
//                LogBarangDAO.insert(con, l);
//
//                Barang b = BarangDAO.get(con, d.getKodeBarang());
//                b.setHargaBeli(d.getTotal() / d.getQtyMasuk());
//                BarangDAO.update(con, b);
//            }
//            for (Pembayaran pp : pbBaru.getListPembayaran()) {
//                pp.setNoPembayaran(PembayaranDAO.getId(con));
//                pp.setTglPembayaran(pbBaru.getTglPembelian());
//                pp.setNoTransaksi(pbBaru.getNoPembelian());
//                pp.setKodeUser(user.getKodeUser());
//                pp.setStatus("true");
//                pp.setTglBatal("2000-01-01 00:00:00");
//                pp.setUserBatal("");
//
//                PembayaranDAO.insert(con, pp);
//
//                insertKeuangan(con, nokeuangan, "Kas", "Pembelian",
//                        pbBaru.getNoPembelian(), -pp.getJumlahPembayaran(), pbBaru.getKodeUser());
//                insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Pembelian",
//                        pbBaru.getNoPembelian(), -pp.getJumlahPembayaran(), pbBaru.getKodeUser());
//            }
//
//            insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Pembelian",
//                    pbBaru.getNoPembelian(), pbBaru.getTotalPembelian(), pbBaru.getKodeUser());
//            insertKeuangan(con, nokeuangan, "Stok Barang", "Pembelian",
//                    pbBaru.getNoPembelian(), pbBaru.getTotalPembelian(), pbBaru.getKodeUser());
//
//            List<StokBarang> listStok = new ArrayList<>();
//            for (PembelianDetail d : groupKeluar) {
//                boolean statusGroup = true;
//                for (StokBarang temp : listStok) {
//                    if (d.getKodeBarang().equals(temp.getKodeBarang())) {
//                        temp.setStokKeluar(pembulatan(temp.getStokKeluar() + d.getQtyMasuk()));
//                        statusGroup = false;
//                    }
//                }
//                if (statusGroup) {
//                    StokBarang s = new StokBarang();
//                    s.setKodeBarang(d.getKodeBarang());
//                    s.setStokMasuk(0);
//                    s.setStokKeluar(d.getQtyMasuk());
//                    listStok.add(s);
//                }
//            }
//            for (PembelianDetail d : groupMasuk) {
//                boolean statusGroup = true;
//                for (StokBarang temp : listStok) {
//                    if (d.getKodeBarang().equals(temp.getKodeBarang())) {
//                        temp.setStokMasuk(pembulatan(temp.getStokMasuk() + d.getQtyMasuk()));
//                        statusGroup = false;
//                    }
//                }
//                if (statusGroup) {
//                    StokBarang s = new StokBarang();
//                    s.setKodeBarang(d.getKodeBarang());
//                    s.setStokMasuk(d.getQtyMasuk());
//                    s.setStokKeluar(0);
//                    listStok.add(s);
//                }
//
//            }
//            for (StokBarang d : listStok) {
//                StokBarang s = StokBarangDAO.get(con, sistem.getTglSystem(), d.getKodeBarang());
//                if (s == null) {
//                    s = new StokBarang();
//                    s.setTanggal(sistem.getTglSystem());
//                    s.setKodeBarang(d.getKodeBarang());
//                    s.setStokAwal(0);
//                    s.setStokMasuk(d.getStokMasuk());
//                    s.setStokKeluar(d.getStokKeluar());
//                    s.setStokAkhir(pembulatan(d.getStokMasuk() - d.getStokKeluar()));
//                    StokBarangDAO.insert(con, s);
//                } else {
//                    s.setStokMasuk(pembulatan(s.getStokMasuk() + d.getStokMasuk()));
//                    s.setStokKeluar(pembulatan(s.getStokKeluar() + d.getStokKeluar()));
//                    s.setStokAkhir(pembulatan(s.getStokAkhir() + d.getStokMasuk() - d.getStokKeluar()));
//                    StokBarangDAO.update(con, s);
//                }
//            }
            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveBatalPembelian(Connection con, PembelianHead p) {
        try {
            String status = "true";
            con.setAutoCommit(false);

//            String nokeuangan = KeuanganDAO.getId(con);
//
//            PembelianHeadDAO.update(con, p);
//
//            List<PembelianDetail> group = new ArrayList<>();
//            for (PembelianDetail d : p.getListPembelianDetail()) {
//                boolean statusGroup = true;
//                for (PembelianDetail temp : group) {
//                    if (d.getKodeBarang().equals(temp.getKodeBarang())) {
//                        temp.setQtyMasuk(pembulatan(temp.getQtyMasuk() + d.getQtyMasuk()));
//                        temp.setTotal(pembulatan(temp.getTotal() + d.getTotal()));
//                        statusGroup = false;
//                    }
//                }
//                if (statusGroup) {
//                    group.add(d);
//                }
//            }
//            for (PembelianDetail d : group) {
//                status = updateStokKeluar(con, "Batal Pembelian", p.getNoPembelian(), d.getKodeBarang(),
//                        d.getQtyMasuk(), d.getTotal(), status);
//
//                PembelianDetail detailLama = PembelianDetailDAO.getLastPembelianByBarang(con, d.getKodeBarang());
//                double hargaLama = 0;
//                if (detailLama != null) {
//                    hargaLama = detailLama.getTotal() / detailLama.getQtyMasuk();
//                }
//                Barang b = BarangDAO.get(con, d.getKodeBarang());
//                b.setHargaBeli(hargaLama);
//                BarangDAO.update(con, b);
//            }
//            insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Batal Pembelian",
//                    p.getNoPembelian(), -p.getTotalPembelian(), p.getKodeUser());
//            insertKeuangan(con, nokeuangan, "Stok Barang", "Batal Pembelian",
//                    p.getNoPembelian(), -p.getTotalPembelian(), p.getKodeUser());
//
//            p.setListPembayaran(PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPembelian(), "true"));
//            for (Pembayaran pp : p.getListPembayaran()) {
//                pp.setStatus("false");
//                pp.setTglBatal(p.getTglBatal());
//                pp.setUserBatal(p.getUserBatal());
//                PembayaranDAO.update(con, pp);
//
//                insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Batal Pembayaran Pembelian",
//                        pp.getNoPembayaran(), pp.getJumlahPembayaran(), p.getKodeUser());
//                insertKeuangan(con, nokeuangan, "Kas", "Batal Pembayaran Pembelian",
//                        pp.getNoPembayaran(), pp.getJumlahPembayaran(), p.getKodeUser());
//            }
            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String savePembayaranPembelian(Connection con, PembelianHead p, Pembayaran pp) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            String nokeuangan = KeuanganDAO.getId(con);

            PembelianHeadDAO.update(con, p);

            PembayaranDAO.insert(con, pp);

            insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Pembayaran Pembelian",
                    pp.getNoPembayaran(), -pp.getJumlahPembayaran(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Kas", "Pembayaran Pembelian",
                    pp.getNoPembayaran(), -pp.getJumlahPembayaran(), p.getKodeUser());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveBatalPembayaranPembelian(Connection con, Pembayaran pp) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            String nokeuangan = KeuanganDAO.getId(con);
            PembelianHead p = PembelianHeadDAO.get(con, pp.getNoTransaksi());
            p.setPembayaran(p.getPembayaran() - pp.getJumlahPembayaran());
            p.setSisaPembayaran(p.getSisaPembayaran() + pp.getJumlahPembayaran());
            PembelianHeadDAO.update(con, p);

            PembayaranDAO.update(con, pp);

            insertKeuangan(con, nokeuangan, "Hutang Pembelian", "Batal Pembayaran Pembelian",
                    pp.getNoPembayaran(), pp.getJumlahPembayaran(), p.getKodeUser());
            insertKeuangan(con, nokeuangan, "Kas", "Batal Pembayaran Pembelian",
                    pp.getNoPembayaran(), pp.getJumlahPembayaran(), p.getKodeUser());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveTransaksiKeuangan(Connection con, String tipeKeuangan, String kategori, String keterangan, double jumlahRp) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            String nokeuangan = KeuanganDAO.getId(con);

            KategoriTransaksi kt = KategoriTransaksiDAO.get(con, kategori);
            double k1 = 0;
            double k2 = 0;
            if (kt.getKategoriKeuangan().equals("Piutang")) {
                if (kt.getJenisTransaksi().equals("D")) {
                    k1 = jumlahRp;
                    k2 = -jumlahRp;
                } else if (kt.getJenisTransaksi().equals("K")) {
                    k1 = -jumlahRp;
                    k2 = jumlahRp;
                }
            } else if (kt.getKategoriKeuangan().equals("Hutang")) {
                if (kt.getJenisTransaksi().equals("D")) {
                    k1 = jumlahRp;
                    k2 = -jumlahRp;
                } else if (kt.getJenisTransaksi().equals("K")) {
                    k1 = -jumlahRp;
                    k2 = jumlahRp;
                }
            } else if (kt.getKategoriKeuangan().equals("Modal")) {
                if (kt.getJenisTransaksi().equals("D")) {
                    k1 = jumlahRp;
                    k2 = jumlahRp;
                } else if (kt.getJenisTransaksi().equals("K")) {
                    k1 = -jumlahRp;
                    k2 = -jumlahRp;
                }
            } else if (kt.getKategoriKeuangan().equals("Pendapatan")) {
                if (kt.getJenisTransaksi().equals("D")) {
                    k1 = jumlahRp;
                    k2 = jumlahRp;
                } else if (kt.getJenisTransaksi().equals("K")) {
                    k1 = -jumlahRp;
                    k2 = -jumlahRp;
                }
            } else if (kt.getKategoriKeuangan().equals("Beban")) {
                if (kt.getJenisTransaksi().equals("D")) {
                    k1 = jumlahRp;
                    k2 = jumlahRp;
                } else if (kt.getJenisTransaksi().equals("K")) {
                    k1 = -jumlahRp;
                    k2 = -jumlahRp;
                }
            } else if (kt.getKategoriKeuangan().equals("Aset Tetap")) {
                if (kt.getJenisTransaksi().equals("D")) {
                    k1 = jumlahRp;
                    k2 = jumlahRp;
                } else if (kt.getJenisTransaksi().equals("K")) {
                    k1 = -jumlahRp;
                    k2 = -jumlahRp;
                }
            }

            insertKeuangan(con, nokeuangan, tipeKeuangan, kategori, keterangan, k1, user.getKodeUser());
            insertKeuangan(con, nokeuangan, kt.getKategoriKeuangan(), kategori, keterangan, k2, user.getKodeUser());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }

    public static String saveBatalTransaksiKas(Connection con, Keuangan k) {
        try {
            String status = "true";
            con.setAutoCommit(false);

            KeuanganDAO.deleteAll(con, k.getNoKeuangan());

            if (status.equals("true")) {
                con.commit();
            } else {
                con.rollback();
            }
            con.setAutoCommit(true);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
                return e.toString();
            } catch (SQLException ex) {
                return ex.toString();
            }
        }
    }
}
