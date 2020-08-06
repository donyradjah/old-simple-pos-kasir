package com.possederhana.kasir.model;

public class Transaksi {

    int idTransaksi;
    int perangkatId;
    String namaPemesan;
    String waktuPesan;
    String status;
    int totalPembelian;
    String waktuBayar;
    String waktuSelesai;
    String namaPerangkat;
    String nomorMeja;

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getPerangkatId() {
        return perangkatId;
    }

    public void setPerangkatId(int perangkatId) {
        this.perangkatId = perangkatId;
    }

    public String getNamaPemesan() {
        return namaPemesan;
    }

    public void setNamaPemesan(String namaPemesan) {
        this.namaPemesan = namaPemesan;
    }

    public String getWaktuPesan() {
        return waktuPesan;
    }

    public void setWaktuPesan(String waktuPesan) {
        this.waktuPesan = waktuPesan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPembelian() {
        return totalPembelian;
    }

    public void setTotalPembelian(int totalPembelian) {
        this.totalPembelian = totalPembelian;
    }

    public String getWaktuBayar() {
        return waktuBayar;
    }

    public void setWaktuBayar(String waktuBayar) {
        this.waktuBayar = waktuBayar;
    }

    public String getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public String getNamaPerangkat() {
        return namaPerangkat;
    }

    public void setNamaPerangkat(String namaPerangkat) {
        this.namaPerangkat = namaPerangkat;
    }

    public String getNomorMeja() {
        return nomorMeja;
    }

    public void setNomorMeja(String nomorMeja) {
        this.nomorMeja = nomorMeja;
    }
}
