package com.example.qld.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.qld.R;
import com.google.android.material.snackbar.Snackbar;

public class ErrorHandler {

    /**
     * Shows an error message using Toast
     * @param context The context
     * @param message The error message to show
     */
    public static void showErrorToast(Context context, String message) {
        Toast.makeText(context, "Lỗi: " + message, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows an error message using Snackbar
     * @param view The view to attach the snackbar to
     * @param message The error message to show
     */
    public static void showErrorSnackbar(View view, String message) {
        Snackbar.make(view, "Lỗi: " + message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.error_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    /**
     * Shows an information message using Snackbar
     * @param view The view to attach the snackbar to
     * @param message The information message to show
     */
    public static void showInfoSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.info_color))
                .setTextColor(Color.WHITE)
                .show();
    }

    /**
     * Shows a success message using Snackbar
     * @param view The view to attach the snackbar to
     * @param message The success message to show
     */
    public static void showSuccessSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.success_color))
                .setTextColor(Color.WHITE)
                .show();
    }
}