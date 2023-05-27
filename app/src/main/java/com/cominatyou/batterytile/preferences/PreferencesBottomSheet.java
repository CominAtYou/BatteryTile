package com.cominatyou.batterytile.preferences;

import android.content.Context;
import android.content.DialogInterface;
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
                requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("tappableTileEnabled", state)
                        .apply();
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
                requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("emulatePowerSaveTile", state)
                        .apply();
            }
        });

        if (requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE).getBoolean("emulatePowerSaveTile", false)) {
            binding.emulatePowerSaveTilePreferenceSwitch.setChecked(true);
            forceTappableTile(true);
        }
        else if (requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE).getBoolean("tappableTileEnabled", false)) {
            binding.tappableTileSwitch.setChecked(true);
        }

        showDialog = true;

        return binding.getRoot();
    }


    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        requireActivity().finish();
    }
}
