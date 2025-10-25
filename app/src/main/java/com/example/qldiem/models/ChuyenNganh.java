package com.example.qldiem.models;

/**
 * Model class cho bảng CHUYENNGANH (Chuyên ngành)
 */
public class ChuyenNganh {
    private int id;
    private String maChuyenNganh;
    private String tenChuyenNganh;

    public ChuyenNganh() {
    }

    public ChuyenNganh(int id, String maChuyenNganh, String tenChuyenNganh) {
        this.id = id;
        this.maChuyenNganh = maChuyenNganh;
        this.tenChuyenNganh = tenChuyenNganh;
    }

    public ChuyenNganh(String maChuyenNganh, String tenChuyenNganh) {
        this.maChuyenNganh = maChuyenNganh;
        this.tenChuyenNganh = tenChuyenNganh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaChuyenNganh() {
        return maChuyenNganh;
    }

    public void setMaChuyenNganh(String maChuyenNganh) {
        this.maChuyenNganh = maChuyenNganh;
    }

    public String getTenChuyenNganh() {
        return tenChuyenNganh;
    }

    public void setTenChuyenNganh(String tenChuyenNganh) {
        this.tenChuyenNganh = tenChuyenNganh;
    }

    @Override
    public String toString() {
        return tenChuyenNganh;
    }
}
