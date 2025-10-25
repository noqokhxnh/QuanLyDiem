package com.example.qldiem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qldiem.models.*;
import com.example.qldiem.utils.PasswordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Helper class - Quản lý SQLite database
 * Cung cấp CRUD operations cho tất cả các bảng
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên tệp cơ sở dữ liệu SQLite
    private static final String DATABASE_NAME = "QuanLySinhVien.db";
    
    // Phiên bản cơ sở dữ liệu, tăng lên khi có thay đổi schema
    private static final int DATABASE_VERSION = 2;

    // Tên các bảng trong cơ sở dữ liệu
    private static final String TABLE_LOP = "LOP";              // Bảng lưu thông tin lớp học
    private static final String TABLE_CHUYEN_NGANH = "CHUYENNGANH"; // Bảng lưu thông tin chuyên ngành
    private static final String TABLE_MON_HOC = "MONHOC";       // Bảng lưu thông tin môn học
    private static final String TABLE_SINH_VIEN = "SINHVIEN";   // Bảng lưu thông tin sinh viên
    private static final String TABLE_TAI_KHOAN = "taiKhoan";   // Bảng lưu thông tin tài khoản người dùng
    private static final String TABLE_DIEM = "DIEM";            // Bảng lưu thông tin điểm số
    private static final String TABLE_EVENT = "EventCalendar";  // Bảng lưu thông tin sự kiện

    // Cột chung dùng cho tất cả bảng - ID tự động tăng
    private static final String COLUMN_ID = "id";

    // Cột cho bảng LOP (Lớp học)
    private static final String COLUMN_MA_LOP = "maLop";        // Mã lớp học
    private static final String COLUMN_TEN_LOP = "tenLop";      // Tên lớp học
    private static final String COLUMN_GVCN = "giangVienChuNhiem"; // Giảng viên chủ nhiệm

    // Cột cho bảng CHUYENNGANH (Chuyên ngành)
    private static final String COLUMN_MA_CHUYEN_NGANH = "maChuyenNganh"; // Mã chuyên ngành
    private static final String COLUMN_TEN_CHUYEN_NGANH = "tenChuyenNganh"; // Tên chuyên ngành

    // Cột cho bảng MONHOC (Môn học)
    private static final String COLUMN_MA_MH = "maMH";          // Mã môn học
    private static final String COLUMN_TEN_MON_HOC = "tenmonhoc"; // Tên môn học

    // Cột cho bảng SINHVIEN (Sinh viên)
    private static final String COLUMN_MA_SV = "maSv";          // Mã sinh viên
    private static final String COLUMN_TEN_SV = "tenSV";        // Tên sinh viên
    private static final String COLUMN_EMAIL = "email";         // Email sinh viên
    private static final String COLUMN_HINH = "hinh";           // Hình ảnh sinh viên (dưới dạng base64)

    // Cột cho bảng taiKhoan (Tài khoản người dùng)
    private static final String COLUMN_TEN_TAI_KHOAN = "tenTaiKhoan"; // Tên đăng nhập
    private static final String COLUMN_MAT_KHAU = "matKhau";          // Mật khẩu (đã mã hóa)
    private static final String COLUMN_VAI_TRO = "vaiTro";            // Vai trò (Admin/Lecturer/Student)
    private static final String COLUMN_HO_TEN = "hoTen";              // Họ tên đầy đủ

    // Cột cho bảng DIEM (Điểm số) - Hệ thống điểm mới
    private static final String COLUMN_DIEM = "diem"; // Cột cũ, giữ lại để tương thích
    private static final String COLUMN_HOC_KY = "hocKy";           // Học kỳ (1 hoặc 2)
    private static final String COLUMN_NAM_HOC = "namHoc";         // Năm học
    private static final String COLUMN_DIEM_CHUYEN_CAN = "diemChuyenCan"; // Điểm chuyên cần (10%)
    private static final String COLUMN_DIEM_GIUA_KY = "diemGiuaKy";      // Điểm giữa kỳ (30%)
    private static final String COLUMN_DIEM_CUOI_KY = "diemCuoiKy";      // Điểm cuối kỳ (60%)

    // Cột cho bảng EventCalendar (Lịch sự kiện)
    private static final String COLUMN_DATE = "Date";   // Ngày sự kiện
    private static final String COLUMN_EVENT = "Event"; // Nội dung sự kiện

    /**
     * Hàm khởi tạo DatabaseHelper để quản lý cơ sở dữ liệu SQLite
     *
     * CÔNG DỤNG:
     * - Khởi tạo SQLiteOpenHelper với tên và phiên bản cơ sở dữ liệu
     * - Chuẩn bị cho việc tạo/mở cơ sở dữ liệu khi cần thiết
     *
     * CÁC BIẾN TRONG HÀM:
     * - context: Context của ứng dụng để truy cập tài nguyên hệ thống
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Context: cung cấp môi trường cho cơ sở dữ liệu, cần thiết để truy cập hệ thống tệp
     * - DATABASE_NAME: tên tệp cơ sở dữ liệu
     * - null: CursorFactory, không sử dụng nên truyền null
     * - DATABASE_VERSION: phiên bản cơ sở dữ liệu, dùng để upgrade khi schema thay đổi
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể khởi tạo DatabaseHelper
     * - Không thể truy cập cơ sở dữ liệu SQLite
     * - Toàn bộ chức năng lưu trữ và truy xuất dữ liệu sẽ không hoạt động
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng LOP
        String createLopTable = "CREATE TABLE " + TABLE_LOP + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MA_LOP + " TEXT UNIQUE NOT NULL, "
                + COLUMN_TEN_LOP + " TEXT NOT NULL, "
                + COLUMN_GVCN + " TEXT)";
        db.execSQL(createLopTable);

        // Tạo bảng CHUYENNGANH
        String createChuyenNganhTable = "CREATE TABLE " + TABLE_CHUYEN_NGANH + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MA_CHUYEN_NGANH + " TEXT UNIQUE NOT NULL, "
                + COLUMN_TEN_CHUYEN_NGANH + " TEXT NOT NULL)";
        db.execSQL(createChuyenNganhTable);

        // Tạo bảng MONHOC
        String createMonHocTable = "CREATE TABLE " + TABLE_MON_HOC + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MA_MH + " TEXT UNIQUE NOT NULL, "
                + COLUMN_TEN_MON_HOC + " TEXT NOT NULL)";
        db.execSQL(createMonHocTable);

        // Tạo bảng SINHVIEN
        String createSinhVienTable = "CREATE TABLE " + TABLE_SINH_VIEN + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MA_SV + " TEXT UNIQUE NOT NULL, "
                + COLUMN_TEN_SV + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_HINH + " TEXT, "
                + COLUMN_MA_LOP + " TEXT, "
                + COLUMN_MA_CHUYEN_NGANH + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_MA_LOP + ") REFERENCES " + TABLE_LOP + "(" + COLUMN_MA_LOP + "), "
                + "FOREIGN KEY(" + COLUMN_MA_CHUYEN_NGANH + ") REFERENCES " + TABLE_CHUYEN_NGANH + "(" + COLUMN_MA_CHUYEN_NGANH + "))";
        db.execSQL(createSinhVienTable);

        // Tạo bảng taiKhoan
        String createTaiKhoanTable = "CREATE TABLE " + TABLE_TAI_KHOAN + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEN_TAI_KHOAN + " TEXT UNIQUE NOT NULL, "
                + COLUMN_MAT_KHAU + " TEXT NOT NULL, "
                + COLUMN_VAI_TRO + " TEXT NOT NULL DEFAULT 'Student', "
                + COLUMN_HO_TEN + " TEXT)";
        db.execSQL(createTaiKhoanTable);

        // Tạo bảng DIEM
        // Tạo bảng DIEM với hệ thống điểm mới
        String createDiemTable = "CREATE TABLE " + TABLE_DIEM + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MA_SV + " TEXT NOT NULL, "
                + COLUMN_MA_MH + " TEXT NOT NULL, "
                + COLUMN_HOC_KY + " INTEGER DEFAULT 1, "
                + COLUMN_NAM_HOC + " INTEGER DEFAULT 2024, "
                + COLUMN_DIEM_CHUYEN_CAN + " REAL DEFAULT 0, "
                + COLUMN_DIEM_GIUA_KY + " REAL DEFAULT 0, "
                + COLUMN_DIEM_CUOI_KY + " REAL DEFAULT 0, "
                + COLUMN_DIEM + " REAL DEFAULT 0, "
                + "FOREIGN KEY(" + COLUMN_MA_SV + ") REFERENCES " + TABLE_SINH_VIEN + "(" + COLUMN_MA_SV + "), "
                + "FOREIGN KEY(" + COLUMN_MA_MH + ") REFERENCES " + TABLE_MON_HOC + "(" + COLUMN_MA_MH + "), "
                + "UNIQUE(" + COLUMN_MA_SV + ", " + COLUMN_MA_MH + ", " + COLUMN_HOC_KY + ", " + COLUMN_NAM_HOC + "))";
        db.execSQL(createDiemTable);

        // Tạo bảng EventCalendar
        String createEventTable = "CREATE TABLE " + TABLE_EVENT + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " TEXT NOT NULL, "
                + COLUMN_EVENT + " TEXT NOT NULL)";
        db.execSQL(createEventTable);

        // Thêm dữ liệu mẫu
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle version 1 to 2 migration - upgrade to new grade system
        if (oldVersion < 2) {
            // Add new columns for the advanced grading system
            db.execSQL("ALTER TABLE " + TABLE_DIEM + " ADD COLUMN " + COLUMN_HOC_KY + " INTEGER DEFAULT 1");
            db.execSQL("ALTER TABLE " + TABLE_DIEM + " ADD COLUMN " + COLUMN_NAM_HOC + " INTEGER DEFAULT 2024");
            db.execSQL("ALTER TABLE " + TABLE_DIEM + " ADD COLUMN " + COLUMN_DIEM_CHUYEN_CAN + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_DIEM + " ADD COLUMN " + COLUMN_DIEM_GIUA_KY + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_DIEM + " ADD COLUMN " + COLUMN_DIEM_CUOI_KY + " REAL DEFAULT 0");
            
            // Migrate old 'diem' values to 'diemCuoiKy'
            db.execSQL("UPDATE " + TABLE_DIEM + " SET " + COLUMN_DIEM_CUOI_KY + " = " + COLUMN_DIEM);
            
            // Add GVCN column to LOP table
            db.execSQL("ALTER TABLE " + TABLE_LOP + " ADD COLUMN " + COLUMN_GVCN + " TEXT");
        }
        
        // For future upgrades beyond version 2, add more conditions as needed
        if (oldVersion < 3) {
            // Add schema changes for version 3 here
        }
    }

    /**
     * Thêm dữ liệu mẫu khi tạo database lần đầu
     */
    private void insertSampleData(SQLiteDatabase db) {
        // Thêm lớp học mẫu
        db.execSQL("INSERT INTO " + TABLE_LOP + " (maLop, tenLop) VALUES ('CNTT01', 'Công nghệ thông tin 01')");
        db.execSQL("INSERT INTO " + TABLE_LOP + " (maLop, tenLop) VALUES ('CNTT02', 'Công nghệ thông tin 02')");
        db.execSQL("INSERT INTO " + TABLE_LOP + " (maLop, tenLop) VALUES ('KTPM01', 'Kỹ thuật phần mềm 01')");

        // Thêm chuyên ngành mẫu
        db.execSQL("INSERT INTO " + TABLE_CHUYEN_NGANH + " (maChuyenNganh, tenChuyenNganh) VALUES ('CNTT', 'Công nghệ thông tin')");
        db.execSQL("INSERT INTO " + TABLE_CHUYEN_NGANH + " (maChuyenNganh, tenChuyenNganh) VALUES ('KTPM', 'Kỹ thuật phần mềm')");
        db.execSQL("INSERT INTO " + TABLE_CHUYEN_NGANH + " (maChuyenNganh, tenChuyenNganh) VALUES ('KHMT', 'Khoa học máy tính')");

        // Thêm môn học mẫu
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " (maMH, tenmonhoc) VALUES ('JAVA', 'Lập trình Java')");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " (maMH, tenmonhoc) VALUES ('ANDROID', 'Lập trình Android')");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " (maMH, tenmonhoc) VALUES ('CSDL', 'Cơ sở dữ liệu')");
        db.execSQL("INSERT INTO " + TABLE_MON_HOC + " (maMH, tenmonhoc) VALUES ('CTDL', 'Cấu trúc dữ liệu')");

        // Thêm tài khoản admin mặc định (mật khẩu: admin123)
        String hashedPassword = PasswordUtils.hashPassword("admin123");
        db.execSQL("INSERT INTO " + TABLE_TAI_KHOAN + " (tenTaiKhoan, matKhau, vaiTro, hoTen) VALUES ('admin', '" + hashedPassword + "', 'Admin', 'Quản trị viên')");
    }

    // ========== CRUD operations cho bảng LOP ==========

    public long addLop(Lop lop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_LOP, lop.getMaLop());
        values.put(COLUMN_TEN_LOP, lop.getTenLop());
        values.put(COLUMN_GVCN, lop.getGiangVienChuNhiem());
        long id = db.insert(TABLE_LOP, null, values);
        db.close();
        return id;
    }

    public List<Lop> getAllLop() {
        List<Lop> lopList = new ArrayList<>();
        String selectQuery = "SELECT l.*, t.hoTen as tenGVCN FROM " + TABLE_LOP + " l " +
                "LEFT JOIN " + TABLE_TAI_KHOAN + " t ON l." + COLUMN_GVCN + " = t." + COLUMN_TEN_TAI_KHOAN +
                " ORDER BY l." + COLUMN_TEN_LOP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Lop lop = new Lop();
                lop.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                lop.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_LOP)));
                lop.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_LOP)));
                int gvcnIndex = cursor.getColumnIndex(COLUMN_GVCN);
                if (gvcnIndex >= 0) {
                    lop.setGiangVienChuNhiem(cursor.getString(gvcnIndex));
                }
                int tenGVCNIndex = cursor.getColumnIndex("tenGVCN");
                if (tenGVCNIndex >= 0) {
                    lop.setTenGVCN(cursor.getString(tenGVCNIndex));
                }
                lopList.add(lop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lopList;
    }

    public Lop getLopByMa(String maLop) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT l.*, t.hoTen as tenGVCN FROM " + TABLE_LOP + " l " +
                "LEFT JOIN " + TABLE_TAI_KHOAN + " t ON l." + COLUMN_GVCN + " = t." + COLUMN_TEN_TAI_KHOAN +
                " WHERE l." + COLUMN_MA_LOP + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{maLop});

        Lop lop = null;
        if (cursor != null && cursor.moveToFirst()) {
            lop = new Lop();
            lop.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            lop.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_LOP)));
            lop.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_LOP)));
            int gvcnIndex = cursor.getColumnIndex(COLUMN_GVCN);
            if (gvcnIndex >= 0) {
                lop.setGiangVienChuNhiem(cursor.getString(gvcnIndex));
            }
            int tenGVCNIndex = cursor.getColumnIndex("tenGVCN");
            if (tenGVCNIndex >= 0) {
                lop.setTenGVCN(cursor.getString(tenGVCNIndex));
            }
            cursor.close();
        }
        db.close();
        return lop;
    }

    public int updateLop(Lop lop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_LOP, lop.getTenLop());
        values.put(COLUMN_GVCN, lop.getGiangVienChuNhiem());
        int rowsAffected = db.update(TABLE_LOP, values, COLUMN_MA_LOP + "=?",
                new String[]{lop.getMaLop()});
        db.close();
        return rowsAffected;
    }

    public int deleteLop(String maLop) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_LOP, COLUMN_MA_LOP + "=?", new String[]{maLop});
        db.close();
        return rowsDeleted;
    }

    // ========== CRUD operations cho bảng CHUYENNGANH ==========

    public long addChuyenNganh(ChuyenNganh chuyenNganh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_CHUYEN_NGANH, chuyenNganh.getMaChuyenNganh());
        values.put(COLUMN_TEN_CHUYEN_NGANH, chuyenNganh.getTenChuyenNganh());
        long id = db.insert(TABLE_CHUYEN_NGANH, null, values);
        db.close();
        return id;
    }

    public List<ChuyenNganh> getAllChuyenNganh() {
        List<ChuyenNganh> chuyenNganhList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHUYEN_NGANH + " ORDER BY " + COLUMN_TEN_CHUYEN_NGANH;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ChuyenNganh cn = new ChuyenNganh();
                cn.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                cn.setMaChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_CHUYEN_NGANH)));
                cn.setTenChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_CHUYEN_NGANH)));
                chuyenNganhList.add(cn);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chuyenNganhList;
    }

    public ChuyenNganh getChuyenNganhByMa(String maChuyenNganh) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHUYEN_NGANH, null, COLUMN_MA_CHUYEN_NGANH + "=?",
                new String[]{maChuyenNganh}, null, null, null);

        ChuyenNganh cn = null;
        if (cursor != null && cursor.moveToFirst()) {
            cn = new ChuyenNganh();
            cn.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            cn.setMaChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_CHUYEN_NGANH)));
            cn.setTenChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_CHUYEN_NGANH)));
            cursor.close();
        }
        db.close();
        return cn;
    }

    public int updateChuyenNganh(ChuyenNganh chuyenNganh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_CHUYEN_NGANH, chuyenNganh.getTenChuyenNganh());
        int rowsAffected = db.update(TABLE_CHUYEN_NGANH, values, COLUMN_MA_CHUYEN_NGANH + "=?",
                new String[]{chuyenNganh.getMaChuyenNganh()});
        db.close();
        return rowsAffected;
    }

    public int deleteChuyenNganh(String maChuyenNganh) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CHUYEN_NGANH, COLUMN_MA_CHUYEN_NGANH + "=?", new String[]{maChuyenNganh});
        db.close();
        return rowsDeleted;
    }

    // ========== CRUD operations cho bảng MONHOC ==========

    public long addMonHoc(MonHoc monHoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_MH, monHoc.getMaMH());
        values.put(COLUMN_TEN_MON_HOC, monHoc.getTenMonHoc());
        long id = db.insert(TABLE_MON_HOC, null, values);
        db.close();
        return id;
    }

    public List<MonHoc> getAllMonHoc() {
        List<MonHoc> monHocList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MON_HOC + " ORDER BY " + COLUMN_TEN_MON_HOC;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MonHoc mh = new MonHoc();
                mh.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                mh.setMaMH(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_MH)));
                mh.setTenMonHoc(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_MON_HOC)));
                monHocList.add(mh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return monHocList;
    }

    public MonHoc getMonHocByMa(String maMH) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MON_HOC, null, COLUMN_MA_MH + "=?",
                new String[]{maMH}, null, null, null);

        MonHoc mh = null;
        if (cursor != null && cursor.moveToFirst()) {
            mh = new MonHoc();
            mh.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            mh.setMaMH(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_MH)));
            mh.setTenMonHoc(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_MON_HOC)));
            cursor.close();
        }
        db.close();
        return mh;
    }

    public int updateMonHoc(MonHoc monHoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_MON_HOC, monHoc.getTenMonHoc());
        int rowsAffected = db.update(TABLE_MON_HOC, values, COLUMN_MA_MH + "=?",
                new String[]{monHoc.getMaMH()});
        db.close();
        return rowsAffected;
    }

    public int deleteMonHoc(String maMH) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_MON_HOC, COLUMN_MA_MH + "=?", new String[]{maMH});
        db.close();
        return rowsDeleted;
    }

    // ========== CRUD operations cho bảng SINHVIEN ==========

    public long addSinhVien(SinhVien sinhVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_SV, sinhVien.getMaSv());
        values.put(COLUMN_TEN_SV, sinhVien.getTenSV());
        values.put(COLUMN_EMAIL, sinhVien.getEmail());
        values.put(COLUMN_HINH, sinhVien.getHinh());
        values.put(COLUMN_MA_LOP, sinhVien.getMaLop());
        values.put(COLUMN_MA_CHUYEN_NGANH, sinhVien.getMaChuyenNganh());
        long id = db.insert(TABLE_SINH_VIEN, null, values);
        db.close();
        return id;
    }

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> sinhVienList = new ArrayList<>();
        String selectQuery = "SELECT sv.*, l.tenLop, cn.tenChuyenNganh FROM " + TABLE_SINH_VIEN + " sv "
                + "LEFT JOIN " + TABLE_LOP + " l ON sv.maLop = l.maLop "
                + "LEFT JOIN " + TABLE_CHUYEN_NGANH + " cn ON sv.maChuyenNganh = cn.maChuyenNganh "
                + "ORDER BY sv.tenSV";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SinhVien sv = new SinhVien();
                sv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                sv.setMaSv(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SV)));
                sv.setTenSV(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SV)));
                sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                sv.setHinh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HINH)));
                sv.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_LOP)));
                sv.setMaChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_CHUYEN_NGANH)));
                sv.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_LOP)));
                sv.setTenChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_CHUYEN_NGANH)));
                sinhVienList.add(sv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sinhVienList;
    }

    public SinhVien getSinhVienByMa(String maSv) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sv.*, l.tenLop, cn.tenChuyenNganh FROM " + TABLE_SINH_VIEN + " sv "
                + "LEFT JOIN " + TABLE_LOP + " l ON sv.maLop = l.maLop "
                + "LEFT JOIN " + TABLE_CHUYEN_NGANH + " cn ON sv.maChuyenNganh = cn.maChuyenNganh "
                + "WHERE sv.maSv = ?";
        Cursor cursor = db.rawQuery(query, new String[]{maSv});

        SinhVien sv = null;
        if (cursor != null && cursor.moveToFirst()) {
            sv = new SinhVien();
            sv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            sv.setMaSv(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SV)));
            sv.setTenSV(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SV)));
            sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            sv.setHinh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HINH)));
            sv.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_LOP)));
            sv.setMaChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_CHUYEN_NGANH)));
            sv.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_LOP)));
            sv.setTenChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_CHUYEN_NGANH)));
            cursor.close();
        }
        db.close();
        return sv;
    }

    // Alias method for consistency
    public SinhVien getSinhVienByMaSV(String maSv) {
        return getSinhVienByMa(maSv);
    }

    public List<SinhVien> searchSinhVien(String keyword) {
        List<SinhVien> sinhVienList = new ArrayList<>();
        String selectQuery = "SELECT sv.*, l.tenLop, cn.tenChuyenNganh FROM " + TABLE_SINH_VIEN + " sv "
                + "LEFT JOIN " + TABLE_LOP + " l ON sv.maLop = l.maLop "
                + "LEFT JOIN " + TABLE_CHUYEN_NGANH + " cn ON sv.maChuyenNganh = cn.maChuyenNganh "
                + "WHERE sv.maSv LIKE ? OR sv.tenSV LIKE ? OR sv.email LIKE ? "
                + "ORDER BY sv.tenSV";
        
        SQLiteDatabase db = this.getReadableDatabase();
        String searchPattern = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchPattern, searchPattern, searchPattern});

        if (cursor.moveToFirst()) {
            do {
                SinhVien sv = new SinhVien();
                sv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                sv.setMaSv(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SV)));
                sv.setTenSV(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SV)));
                sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                sv.setHinh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HINH)));
                sv.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_LOP)));
                sv.setMaChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_CHUYEN_NGANH)));
                sv.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_LOP)));
                sv.setTenChuyenNganh(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_CHUYEN_NGANH)));
                sinhVienList.add(sv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sinhVienList;
    }

    public int updateSinhVien(SinhVien sinhVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_SV, sinhVien.getTenSV());
        values.put(COLUMN_EMAIL, sinhVien.getEmail());
        values.put(COLUMN_HINH, sinhVien.getHinh());
        values.put(COLUMN_MA_LOP, sinhVien.getMaLop());
        values.put(COLUMN_MA_CHUYEN_NGANH, sinhVien.getMaChuyenNganh());
        int rowsAffected = db.update(TABLE_SINH_VIEN, values, COLUMN_MA_SV + "=?",
                new String[]{sinhVien.getMaSv()});
        db.close();
        return rowsAffected;
    }

    public int deleteSinhVien(String maSv) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Xóa điểm của sinh viên trước
        db.delete(TABLE_DIEM, COLUMN_MA_SV + "=?", new String[]{maSv});
        // Xóa sinh viên
        int rowsDeleted = db.delete(TABLE_SINH_VIEN, COLUMN_MA_SV + "=?", new String[]{maSv});
        db.close();
        return rowsDeleted;
    }

    // ========== CRUD operations cho bảng taiKhoan ==========

    public long addTaiKhoan(TaiKhoan taiKhoan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_TAI_KHOAN, taiKhoan.getTenTaiKhoan());
        // Mã hóa mật khẩu trước khi lưu
        values.put(COLUMN_MAT_KHAU, PasswordUtils.hashPassword(taiKhoan.getMatKhau()));
        values.put(COLUMN_VAI_TRO, taiKhoan.getVaiTro());
        values.put(COLUMN_HO_TEN, taiKhoan.getHoTen());
        long id = db.insert(TABLE_TAI_KHOAN, null, values);
        db.close();
        return id;
    }

    /**
     * Hàm xác thực đăng nhập người dùng
     *
     * CÔNG DỤNG:
     * - Xác minh tên đăng nhập và mật khẩu có khớp với cơ sở dữ liệu không
     * - Mã hóa mật khẩu người dùng nhập vào để so sánh với mật khẩu đã lưu
     * - Trả về đối tượng TaiKhoan nếu xác thực thành công, null nếu thất bại
     *
     * CÁC BIẾN TRONG HÀM:
     * - db: đối tượng SQLiteDatabase để thực hiện thao tác đọc
     * - hashedPassword: mật khẩu đã được mã hóa để so sánh
     * - cursor: đối tượng dùng để duyệt qua kết quả truy vấn
     * - taiKhoan: đối tượng TaiKhoan để lưu thông tin tài khoản xác thực thành công
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenTaiKhoan: tên đăng nhập do người dùng nhập
     * - String matKhau: mật khẩu do người dùng nhập (sẽ được mã hóa để so sánh)
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Đối tượng TaiKhoan nếu đăng nhập thành công
     * - null nếu tên đăng nhập hoặc mật khẩu không đúng
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể đăng nhập vào ứng dụng
     * - Không thể xác thực danh tính người dùng
     * - Toàn bộ hệ thống bảo mật và phân quyền sẽ không hoạt động
     */
    public TaiKhoan login(String tenTaiKhoan, String matKhau) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = PasswordUtils.hashPassword(matKhau);
        
        Cursor cursor = db.query(TABLE_TAI_KHOAN, null, 
                COLUMN_TEN_TAI_KHOAN + "=? AND " + COLUMN_MAT_KHAU + "=?",
                new String[]{tenTaiKhoan, hashedPassword}, null, null, null);

        TaiKhoan taiKhoan = null;
        if (cursor != null && cursor.moveToFirst()) {
            taiKhoan = new TaiKhoan();
            taiKhoan.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            taiKhoan.setTenTaiKhoan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_TAI_KHOAN)));
            taiKhoan.setMatKhau(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAT_KHAU)));
            taiKhoan.setVaiTro(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VAI_TRO)));
            taiKhoan.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HO_TEN)));
            cursor.close();
        }
        db.close();
        return taiKhoan;
    }

    public boolean checkTaiKhoanExists(String tenTaiKhoan) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, new String[]{COLUMN_ID}, 
                COLUMN_TEN_TAI_KHOAN + "=?", new String[]{tenTaiKhoan}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    /**
     * Hàm cập nhật thông tin tài khoản người dùng trong cơ sở dữ liệu
     *
     * CÔNG DỤNG:
     * - Cập nhật mật khẩu, họ tên và vai trò của tài khoản
     * - Chỉ cập nhật bản ghi có tên tài khoản trùng với tài khoản được truyền vào
     * - Trả về số lượng bản ghi bị ảnh hưởng bởi thao tác cập nhật
     *
     * CÁC BIẾN TRONG HÀM:
     * - db: đối tượng SQLiteDatabase để thực hiện thao tác ghi
     * - values: đối tượng ContentValues chứa các cặp khóa-giá trị cần cập nhật
     * - rowsAffected: số lượng bản ghi bị ảnh hưởng bởi câu lệnh update
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - TaiKhoan: đối tượng chứa thông tin tài khoản cần cập nhật (mật khẩu, họ tên, vai trò)
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Số lượng bản ghi bị ảnh hưởng (thường là 1 nếu cập nhật thành công, 0 nếu thất bại)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể cập nhật thông tin tài khoản người dùng
     * - Người dùng không thể thay đổi mật khẩu hoặc thông tin cá nhân
     * - Tính năng đổi mật khẩu trong ProfileActivity sẽ không hoạt động
     */
    public int updateTaiKhoan(TaiKhoan taiKhoan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MAT_KHAU, taiKhoan.getMatKhau());
        values.put(COLUMN_HO_TEN, taiKhoan.getHoTen());
        values.put(COLUMN_VAI_TRO, taiKhoan.getVaiTro());
        int rowsAffected = db.update(TABLE_TAI_KHOAN, values, COLUMN_TEN_TAI_KHOAN + "=?",
                new String[]{taiKhoan.getTenTaiKhoan()});
        db.close();
        return rowsAffected;
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TAI_KHOAN, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int usernameIndex = cursor.getColumnIndex(COLUMN_TEN_TAI_KHOAN);
                int passwordIndex = cursor.getColumnIndex(COLUMN_MAT_KHAU);
                int roleIndex = cursor.getColumnIndex(COLUMN_VAI_TRO);
                int fullNameIndex = cursor.getColumnIndex(COLUMN_HO_TEN);
                
                if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1 && 
                    roleIndex != -1 && fullNameIndex != -1) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setId(cursor.getInt(idIndex));
                    tk.setTenTaiKhoan(cursor.getString(usernameIndex));
                    tk.setMatKhau(cursor.getString(passwordIndex));
                    tk.setVaiTro(cursor.getString(roleIndex));
                    tk.setHoTen(cursor.getString(fullNameIndex));
                    list.add(tk);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * Lấy danh sách tài khoản theo vai trò (để lọc)
     */
    public List<TaiKhoan> getTaiKhoanByRole(String vaiTro) {
        List<TaiKhoan> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TAI_KHOAN;
        if (vaiTro != null && !vaiTro.equals("All")) {
            query += " WHERE " + COLUMN_VAI_TRO + "=?";
        }
        
        Cursor cursor;
        if (vaiTro != null && !vaiTro.equals("All")) {
            cursor = db.rawQuery(query, new String[]{vaiTro});
        } else {
            cursor = db.rawQuery(query, null);
        }
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int usernameIndex = cursor.getColumnIndex(COLUMN_TEN_TAI_KHOAN);
                int passwordIndex = cursor.getColumnIndex(COLUMN_MAT_KHAU);
                int roleIndex = cursor.getColumnIndex(COLUMN_VAI_TRO);
                int fullNameIndex = cursor.getColumnIndex(COLUMN_HO_TEN);
                
                if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1 && 
                    roleIndex != -1 && fullNameIndex != -1) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setId(cursor.getInt(idIndex));
                    tk.setTenTaiKhoan(cursor.getString(usernameIndex));
                    tk.setMatKhau(cursor.getString(passwordIndex));
                    tk.setVaiTro(cursor.getString(roleIndex));
                    tk.setHoTen(cursor.getString(fullNameIndex));
                    list.add(tk);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * Lấy danh sách giảng viên (để chọn GVCN)
     */
    public List<TaiKhoan> getAllLecturers() {
        return getTaiKhoanByRole("Lecturer");
    }

    public int deleteTaiKhoan(String tenTaiKhoan) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TAI_KHOAN, COLUMN_TEN_TAI_KHOAN + "=?",
                new String[]{tenTaiKhoan});
        db.close();
        return rowsDeleted;
    }

    /**
     * Hàm thay đổi mật khẩu cho tài khoản người dùng
     *
     * CÔNG DỤNG:
     * - Cập nhật mật khẩu mới cho tài khoản có tên đăng nhập xác định
     * - Mã hóa mật khẩu mới trước khi lưu vào cơ sở dữ liệu
     * - Trả về số lượng bản ghi bị ảnh hưởng bởi thao tác cập nhật
     *
     * CÁC BIẾN TRONG HÀM:
     * - db: đối tượng SQLiteDatabase để thực hiện thao tác ghi
     * - values: đối tượng ContentValues chứa mật khẩu đã mã hóa
     * - rowsAffected: số lượng bản ghi bị ảnh hưởng bởi câu lệnh update
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenTaiKhoan: tên đăng nhập của tài khoản cần đổi mật khẩu
     * - String matKhauMoi: mật khẩu mới chưa mã hóa (sẽ được mã hóa trước khi lưu)
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Số lượng bản ghi bị ảnh hưởng (1 nếu cập nhật thành công, 0 nếu thất bại)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể thay đổi mật khẩu của mình
     * - Tính năng đổi mật khẩu trong ProfileActivity sẽ không hoạt động
     * - Bảo mật tài khoản bị ảnh hưởng do không thể cập nhật mật khẩu định kỳ
     */
    public int changePassword(String tenTaiKhoan, String matKhauMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MAT_KHAU, PasswordUtils.hashPassword(matKhauMoi));
        int rowsAffected = db.update(TABLE_TAI_KHOAN, values, COLUMN_TEN_TAI_KHOAN + "=?",
                new String[]{tenTaiKhoan});
        db.close();
        return rowsAffected;
    }

    /**
     * Hàm lấy thông tin tài khoản người dùng dựa trên tên đăng nhập
     *
     * CÔNG DỤNG:
     * - Truy vấn cơ sở dữ liệu để lấy thông tin tài khoản có tên đăng nhập trùng
     * - Trả về đối tượng TaiKhoan chứa đầy đủ thông tin tài khoản (ID, tên đăng nhập, mật khẩu, vai trò, họ tên)
     *
     * CÁC BIẾN TRONG HÀM:
     * - db: đối tượng SQLiteDatabase để thực hiện thao tác đọc
     * - cursor: đối tượng dùng để duyệt qua kết quả truy vấn
     * - tk: đối tượng TaiKhoan để lưu thông tin tài khoản tìm được
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenTaiKhoan: tên đăng nhập để tìm kiếm tài khoản trong cơ sở dữ liệu
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - Đối tượng TaiKhoan nếu tìm thấy tài khoản
     * - null nếu không tìm thấy tài khoản với tên đăng nhập đã cho
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xác thực người dùng dựa trên tên đăng nhập
     * - Không thể lấy thông tin chi tiết tài khoản để hiển thị
     * - Tính năng đăng nhập và hiển thị thông tin cá nhân sẽ không hoạt động
     */
    public TaiKhoan getTaiKhoanByUsername(String tenTaiKhoan) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAI_KHOAN, null, COLUMN_TEN_TAI_KHOAN + "=?",
                new String[]{tenTaiKhoan}, null, null, null);
        
        TaiKhoan tk = null;
        if (cursor != null && cursor.moveToFirst()) {
            tk = new TaiKhoan();
            tk.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            tk.setTenTaiKhoan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_TAI_KHOAN)));
            tk.setMatKhau(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAT_KHAU)));
            tk.setVaiTro(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VAI_TRO)));
            tk.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HO_TEN)));
            cursor.close();
        }
        db.close();
        return tk;
    }

    // ========== CRUD operations cho bảng DIEM ==========

    public long addDiem(Diem diem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_SV, diem.getMaSv());
        values.put(COLUMN_MA_MH, diem.getMaMH());
        values.put(COLUMN_HOC_KY, diem.getHocKy());
        values.put(COLUMN_NAM_HOC, diem.getNamHoc());
        values.put(COLUMN_DIEM_CHUYEN_CAN, diem.getDiemChuyenCan());
        values.put(COLUMN_DIEM_GIUA_KY, diem.getDiemGiuaKy());
        values.put(COLUMN_DIEM_CUOI_KY, diem.getDiemCuoiKy());
        values.put(COLUMN_DIEM, diem.getDiemTongKet()); // Lưu tổng kết để tương thích
        long id = db.insert(TABLE_DIEM, null, values);
        db.close();
        return id;
    }

    public List<Diem> getDiemBySinhVien(String maSv) {
        List<Diem> diemList = new ArrayList<>();
        String query = "SELECT d.*, sv.tenSV, mh.tenmonhoc FROM " + TABLE_DIEM + " d "
                + "LEFT JOIN " + TABLE_SINH_VIEN + " sv ON d.maSv = sv.maSv "
                + "LEFT JOIN " + TABLE_MON_HOC + " mh ON d.maMH = mh.maMH "
                + "WHERE d.maSv = ? ORDER BY d.namHoc DESC, d.hocKy DESC, mh.tenmonhoc";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{maSv});

        if (cursor.moveToFirst()) {
            do {
                Diem diem = new Diem();
                diem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                diem.setMaSv(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SV)));
                diem.setMaMH(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_MH)));
                
                // Đọc các loại điểm mới
                int hkIndex = cursor.getColumnIndex(COLUMN_HOC_KY);
                int nhIndex = cursor.getColumnIndex(COLUMN_NAM_HOC);
                int ccIndex = cursor.getColumnIndex(COLUMN_DIEM_CHUYEN_CAN);
                int gkIndex = cursor.getColumnIndex(COLUMN_DIEM_GIUA_KY);
                int ckIndex = cursor.getColumnIndex(COLUMN_DIEM_CUOI_KY);
                
                if (hkIndex >= 0) diem.setHocKy(cursor.getInt(hkIndex));
                if (nhIndex >= 0) diem.setNamHoc(cursor.getInt(nhIndex));
                if (ccIndex >= 0) diem.setDiemChuyenCan(cursor.getFloat(ccIndex));
                if (gkIndex >= 0) diem.setDiemGiuaKy(cursor.getFloat(gkIndex));
                if (ckIndex >= 0) diem.setDiemCuoiKy(cursor.getFloat(ckIndex));
                
                diem.setTenSV(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SV)));
                diem.setTenMonHoc(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_MON_HOC)));
                diemList.add(diem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return diemList;
    }

    public List<Diem> getDiemByMonHoc(String maMH) {
        List<Diem> diemList = new ArrayList<>();
        String query = "SELECT d.*, sv.tenSV, mh.tenmonhoc FROM " + TABLE_DIEM + " d "
                + "LEFT JOIN " + TABLE_SINH_VIEN + " sv ON d.maSv = sv.maSv "
                + "LEFT JOIN " + TABLE_MON_HOC + " mh ON d.maMH = mh.maMH "
                + "WHERE d.maMH = ? ORDER BY d.namHoc DESC, d.hocKy DESC, sv.tenSV";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{maMH});

        if (cursor.moveToFirst()) {
            do {
                Diem diem = new Diem();
                diem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                diem.setMaSv(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_SV)));
                diem.setMaMH(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_MH)));
                
                // Đọc các loại điểm mới
                int hkIndex = cursor.getColumnIndex(COLUMN_HOC_KY);
                int nhIndex = cursor.getColumnIndex(COLUMN_NAM_HOC);
                int ccIndex = cursor.getColumnIndex(COLUMN_DIEM_CHUYEN_CAN);
                int gkIndex = cursor.getColumnIndex(COLUMN_DIEM_GIUA_KY);
                int ckIndex = cursor.getColumnIndex(COLUMN_DIEM_CUOI_KY);
                
                if (hkIndex >= 0) diem.setHocKy(cursor.getInt(hkIndex));
                if (nhIndex >= 0) diem.setNamHoc(cursor.getInt(nhIndex));
                if (ccIndex >= 0) diem.setDiemChuyenCan(cursor.getFloat(ccIndex));
                if (gkIndex >= 0) diem.setDiemGiuaKy(cursor.getFloat(gkIndex));
                if (ckIndex >= 0) diem.setDiemCuoiKy(cursor.getFloat(ckIndex));
                
                diem.setTenSV(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_SV)));
                diem.setTenMonHoc(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_MON_HOC)));
                diemList.add(diem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return diemList;
    }

    public float getDiemTrungBinh(String maSv) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Tính điểm trung bình dựa trên điểm tổng kết (CC*0.1 + GK*0.3 + CK*0.6)
        String query = "SELECT AVG(" + COLUMN_DIEM_CHUYEN_CAN + "*0.1 + " +
                COLUMN_DIEM_GIUA_KY + "*0.3 + " + COLUMN_DIEM_CUOI_KY + "*0.6) as dtb " +
                "FROM " + TABLE_DIEM + " WHERE maSv = ?";
        Cursor cursor = db.rawQuery(query, new String[]{maSv});
        
        float dtb = 0;
        if (cursor.moveToFirst()) {
            dtb = cursor.getFloat(0);
        }
        cursor.close();
        db.close();
        return dtb;
    }

    public int updateDiem(Diem diem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DIEM_CHUYEN_CAN, diem.getDiemChuyenCan());
        values.put(COLUMN_DIEM_GIUA_KY, diem.getDiemGiuaKy());
        values.put(COLUMN_DIEM_CUOI_KY, diem.getDiemCuoiKy());
        values.put(COLUMN_DIEM, diem.getDiemTongKet()); // Cập nhật điểm tổng kết
        int rowsAffected = db.update(TABLE_DIEM, values, 
                COLUMN_MA_SV + "=? AND " + COLUMN_MA_MH + "=? AND " + COLUMN_HOC_KY + "=? AND " + COLUMN_NAM_HOC + "=?",
                new String[]{diem.getMaSv(), diem.getMaMH(), String.valueOf(diem.getHocKy()), String.valueOf(diem.getNamHoc())});
        db.close();
        return rowsAffected;
    }

    public int deleteDiem(String maSv, String maMH) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_DIEM, 
                COLUMN_MA_SV + "=? AND " + COLUMN_MA_MH + "=?", 
                new String[]{maSv, maMH});
        db.close();
        return rowsDeleted;
    }

    // ========== CRUD operations cho bảng EventCalendar ==========

    public long addEvent(EventCalendar event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_EVENT, event.getEvent());
        long id = db.insert(TABLE_EVENT, null, values);
        db.close();
        return id;
    }

    public List<EventCalendar> getAllEvents() {
        List<EventCalendar> eventList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENT + " ORDER BY " + COLUMN_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                EventCalendar event = new EventCalendar();
                event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                event.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                event.setEvent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    public List<EventCalendar> getEventsByDate(String date) {
        List<EventCalendar> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENT, null, COLUMN_DATE + "=?",
                new String[]{date}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                EventCalendar event = new EventCalendar();
                event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                event.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                event.setEvent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    public int updateEvent(EventCalendar event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_EVENT, event.getEvent());
        int rowsAffected = db.update(TABLE_EVENT, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(event.getId())});
        db.close();
        return rowsAffected;
    }

    public int deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_EVENT, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }
}
