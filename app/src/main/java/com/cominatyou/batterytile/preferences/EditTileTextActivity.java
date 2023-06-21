package com.cominatyou.batterytile.preferences;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.cominatyou.batterytile.R;
import com.cominatyou.batterytile.databinding.ActivityEditTileTextBinding;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

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

        final String preference_key = getIntent().getBooleanExtra("isEditingChargingText", false) ? "chargingText" : "dischargingText";

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
                Snackbar.make(binding.getRoot(), R.string.activity_edit_tile_text_save_success, Snackbar.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        binding.topBar.setNavigationOnClickListener(v -> warnForUnsavedChanges());
    }

    @Override
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
