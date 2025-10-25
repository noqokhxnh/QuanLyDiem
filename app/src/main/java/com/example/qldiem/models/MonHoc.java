package com.example.qldiem.models;

/**
 * Model class cho bảng MONHOC (Môn học)
 */
public class MonHoc {
    private int id;
    private String maMH;
    private String tenMonHoc;

    public MonHoc() {
    }

    public MonHoc(int id, String maMH, String tenMonHoc) {
        this.id = id;
        this.maMH = maMH;
        this.tenMonHoc = tenMonHoc;
    }

    public MonHoc(String maMH, String tenMonHoc) {
        this.maMH = maMH;
        this.tenMonHoc = tenMonHoc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    @Override
    public String toString() {
        return tenMonHoc;
    }
}
