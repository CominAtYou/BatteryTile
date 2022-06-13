package com.cominatyou.batterytile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class QuickSettingsTileService extends TileService {
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int plugState = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            final boolean isCharging = plugState == BatteryManager.BATTERY_PLUGGED_AC || plugState == BatteryManager.BATTERY_PLUGGED_USB || plugState == BatteryManager.BATTERY_PLUGGED_WIRELESS;

            getQsTile().setSubtitle((isCharging ? "Charging, " : "") + batteryLevel + "%");
            getQsTile().updateTile();
        }
    };

    @Override
    public void onStartListening() {
        super.onStartListening();

        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(receiver, filter);

        final int batteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int plugState = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        final boolean isCharging = plugState == BatteryManager.BATTERY_PLUGGED_AC || plugState == BatteryManager.BATTERY_PLUGGED_USB || plugState == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        getQsTile().setSubtitle((isCharging ? "Charging, " : "") + batteryLevel + "%");
        getQsTile().setState(Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onStopListening() {
        unregisterReceiver(receiver);
    }
}
