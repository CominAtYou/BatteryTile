package com.cominatyou.batterytile.preferences;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.cominatyou.batterytile.R;
import com.cominatyou.batterytile.databinding.ActivityEditTileTextBinding;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class EditTileTextActivity extends AppCompatActivity {
    private boolean hasTextBeenChanged = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);
        final ActivityEditTileTextBinding binding = ActivityEditTileTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT, this::warnForUnsavedChanges);
        }

        final boolean isEditingChargingText = getIntent().getBooleanExtra("isEditingChargingText", false);
        final String preference_key = isEditingChargingText ? "charging_text" : "discharging_text";

        final @StringRes int infoTextRes = isEditingChargingText ? R.string.activity_edit_charging_tile_text_info_text : R.string.activity_edit_discharging_tile_text_info_text;

        binding.infoText.setText(getString(infoTextRes, getString(isEditingChargingText ? R.string.activity_edit_tile_text_info_text_default_charging : R.string.activity_edit_tile_text_info_text_default_discharging)));

        binding.editTileTextEditText.setText(getSharedPreferences("preferences", MODE_PRIVATE).getString(preference_key, ""));

        binding.editTileTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasTextBeenChanged = true;
            }
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            public void afterTextChanged(Editable s) {}
        });

        binding.topBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.save_menu_item) {
                getSharedPreferences("preferences", MODE_PRIVATE).edit().putString(preference_key, Objects.requireNonNull(binding.editTileTextEditText.getText()).toString().trim()).apply();
                hasTextBeenChanged = false;
                Toast.makeText(this, R.string.activity_edit_tile_text_save_success, Toast.LENGTH_LONG).show();
                finish();
                return true;
            }
            return false;
        });

        binding.topBar.setNavigationOnClickListener(v -> warnForUnsavedChanges());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            warnForUnsavedChanges();
        }
        else {
            super.onBackPressed();
        }
    }

    private void warnForUnsavedChanges() {
        if (hasTextBeenChanged) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.activity_edit_text_discard_dialog_title)
                    .setMessage(R.string.activity_edit_text_discard_dialog_description)
                    .setNegativeButton(R.string.activity_edit_text_discard_dialog_negative_button, (dialog, which) -> finish())
                    .setPositiveButton(R.string.activity_edit_text_discard_dialog_positive_button, (dialog, which) -> dialog.dismiss())
                    .show();
        }
        else {
            finish();
        }
    }
}
