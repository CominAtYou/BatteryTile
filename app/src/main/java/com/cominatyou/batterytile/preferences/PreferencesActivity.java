package com.cominatyou.batterytile.preferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cominatyou.batterytile.R;
import com.google.android.material.color.DynamicColors;

public class PreferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);

        setContentView(R.layout.activity_preferences);
        new PreferencesBottomSheet().show(getSupportFragmentManager(), "preferences");
    }
}
