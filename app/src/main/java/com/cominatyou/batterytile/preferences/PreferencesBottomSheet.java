package com.cominatyou.batterytile.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.Manifest;

import com.cominatyou.batterytile.R;
import com.cominatyou.batterytile.databinding.BottomSheetPreferencesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PreferencesBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetPreferencesBinding binding;
    boolean showDialog = false;

    private void forceTappableTile(boolean force) {
        binding.tappableTileSwitch.setChecked(force);
        binding.tappableTileSwitch.setEnabled(!force);
        binding.tappableTileTitle.setAlpha(force ? 0.4f : 1);
        binding.tappableTileDescription.setAlpha(force ? 0.4f : 1);
        binding.tappableTileLayout.setEnabled(!force);
    }

    private boolean checkIfPermissionIsDenied() {
        return requireContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetPreferencesBinding.inflate(inflater, container, false);
        final SharedPreferences preferences = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        binding.tappableTileLayout.setOnClickListener(self -> binding.tappableTileSwitch.toggle());

        binding.tappableTileSwitch.setOnCheckedChangeListener((self, state) -> {
            if (checkIfPermissionIsDenied() && state) {
                self.setChecked(false);
                AdbDialog.show(requireContext());
            }
            else {
                if (state && showDialog) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle(R.string.power_save_tile_warning_title)
                            .setMessage(R.string.power_save_system_warning_description)
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                            .show();
                }
                preferences
                        .edit()
                        .putBoolean("tappableTileEnabled", state)
                        .apply();

                setTileStatePreferenceEnabled(!state);
            }
        });

        binding.emulatePowerSaveTilePreferenceLayout.setOnClickListener(self -> binding.emulatePowerSaveTilePreferenceSwitch.toggle());

        binding.emulatePowerSaveTilePreferenceSwitch.setOnCheckedChangeListener((self, state) -> {
            if (checkIfPermissionIsDenied() && state) {
                self.setChecked(false);
                AdbDialog.show(requireContext());
            }
            else {
                forceTappableTile(state);
                preferences.edit().putBoolean("emulatePowerSaveTile", state).apply();
                setTileStatePreferenceEnabled(!state);
            }
        });

        binding.infoInTitlePreferenceLayout.setOnClickListener(self -> binding.infoInTitleSwitch.toggle());

        binding.infoInTitleSwitch.setOnCheckedChangeListener((self, state) -> preferences
                .edit()
                .putBoolean("infoInTitle", state)
                .apply()
        );

        if (preferences.getBoolean("emulatePowerSaveTile", false)) {
            binding.emulatePowerSaveTilePreferenceSwitch.setChecked(true);
            forceTappableTile(true);
        }
        else if (preferences.getBoolean("tappableTileEnabled", false)) {
            binding.tappableTileSwitch.setChecked(true);
        }

        showDialog = true;

        if (preferences.getBoolean("infoInTitle", false)) {
            binding.infoInTitleSwitch.setChecked(true);
        }

        binding.tileStateLayout.setOnClickListener(v -> TileStatePickerDialog.show(requireContext(), this::updateTileStateDescription));
        updateTileStateDescription();

        return binding.getRoot();
    }

    private void updateTileStateDescription() {
        final SharedPreferences preferences = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        if (preferences.getBoolean("emulatePowerSaveTile", false) || preferences.getBoolean("tappableTileEnabled", false)) {
            binding.tileStateDescription.setText(R.string.bottom_sheet_preferences_tile_state_disabled_reason);
            return;
        }

        switch (preferences.getInt("tileState", 0)) {
            case 0:
                binding.tileStateDescription.setText(R.string.tile_state_always_on);
                break;
            case 1:
                binding.tileStateDescription.setText(R.string.tile_state_on_when_charging);
                break;
            case 2:
                binding.tileStateDescription.setText(R.string.tile_state_always_off);
                break;
        }
    }

    private void setTileStatePreferenceEnabled(boolean enabled) {
        binding.tileStateTitle.setAlpha(enabled ? 1 : 0.4f);
        binding.tileStateDescription.setAlpha(enabled ? 1 : 0.4f);
        binding.tileStateLayout.setEnabled(enabled);
        updateTileStateDescription();
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        requireActivity().finish();
    }
}
