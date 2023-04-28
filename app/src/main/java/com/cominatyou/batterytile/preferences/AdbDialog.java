package com.cominatyou.batterytile.preferences;

import android.content.Context;

import android.Manifest;
import android.text.Html;

import com.cominatyou.batterytile.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AdbDialog {
    public static void show(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.requires_adb_dialog_title)
                .setMessage(Html.fromHtml(context.getString(R.string.requires_adb_dialog_description) + "<br><br><tt>adb shell pm grant " + context.getPackageName() + " " + Manifest.permission.WRITE_SECURE_SETTINGS +
                        "</tt><br><br>" + context.getString(R.string.requires_adb_dialog_description_second_half), Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
