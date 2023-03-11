package com.cominatyou.batterytile.preferences;

import android.content.Context;

import android.Manifest;
import android.text.Html;

import com.cominatyou.batterytile.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AdbDialog {
    public static void show(Context context) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.requires_adb_dialog_title)
                .setMessage(Html.fromHtml("<p>" +
                        context.getString(R.string.requires_adb_dialog_description) + "</p><br><tt>adb shell pm grant " + context.getPackageName() + " " + Manifest.permission.WRITE_SECURE_SETTINGS +
                        "</tt><br><br><p>" + context.getString(R.string.requires_adb_dialog_description_second_half) + "</p>", Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
