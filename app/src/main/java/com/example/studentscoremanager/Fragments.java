package com.example.studentscoremanager;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Fragments {
    public static Intent intentFor(Context context, Fragment fragment) {
        Intent intent = new Intent(context, StudentMainActivity.class);
        if (fragment instanceof StudentsFragment) {
            intent.putExtra("open_fragment", "students");
        } else if (fragment instanceof ScoresFragment) {
            intent.putExtra("open_fragment", "scores");
        }
        return intent;
    }
}


