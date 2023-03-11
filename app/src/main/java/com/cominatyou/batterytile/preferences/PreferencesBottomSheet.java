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

import com.cominatyou.batterytile.databinding.BottomSheetPreferencesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PreferencesBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetPreferencesBinding binding;
    private void forceTappableTile(boolean force) {
        binding.tappableTileSwitch.setChecked(force);
        binding.tappableTileSwitch.setEnabled(!force);
        binding.tappableTileTitle.setAlpha(force ? 0.4f : 1);
        binding.tappableTileDescription.setAlpha(force ? 0.4f : 1);
        binding.tappableTileLayout.setEnabled(!force);
    }
    private boolean checkForPermission() {
        return requireContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetPreferencesBinding.inflate(inflater, container, false);

        binding.tappableTileLayout.setOnClickListener(self -> {
            if (checkForPermission()) {
                binding.tappableTileSwitch.toggle();
            }
            else {
                AdbDialog.show(requireContext());
            }
        });

        binding.tappableTileSwitch.setOnCheckedChangeListener((self, state) -> {
            if (!checkForPermission() && state) {
                self.setChecked(false);
                AdbDialog.show(requireContext());
            }
            else {
                requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("tappableTileEnabled", state)
                        .apply();
            }
        });

        binding.emulatePowerSaveTilePreferenceLayout.setOnClickListener(self -> {
            if (checkForPermission()) {
                binding.emulatePowerSaveTilePreferenceSwitch.toggle();
            }
            else {
                AdbDialog.show(requireContext());
            }
        });

        binding.emulatePowerSaveTilePreferenceSwitch.setOnCheckedChangeListener((self, state) -> {
            if (!checkForPermission() && state) {
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

        return binding.getRoot();
    }


    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        requireActivity().finish();
    }
}
