package com.example.qld.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Utility class để hiển thị loading indicators
 */
public class ProgressDialogUtil {
    private ProgressDialog progressDialog;
    
    /**
     * Hiển thị progress dialog
     * @param context context
     * @param message thông báo
     */
    public void showProgressDialog(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
        
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
    
    /**
     * Ẩn progress dialog
     */
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    
    /**
     * Kiểm tra xem progress dialog có đang hiển thị không
     * @return true nếu đang hiển thị, false nếu không
     */
    public boolean isProgressDialogShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }
}