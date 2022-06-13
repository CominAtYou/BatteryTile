package com.cominatyou.batterytile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class QuickSettingsTileService extends TileService {
    private void setBatteryInfo(Intent intent) {
        final int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        final int plugState = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        final boolean isPluggedIn = plugState == BatteryManager.BATTERY_PLUGGED_AC || plugState == BatteryManager.BATTERY_PLUGGED_USB || plugState == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        final boolean isFullyCharged = isPluggedIn && batteryLevel == 100;

        getQsTile().setSubtitle(isFullyCharged ? "Charged, 100%" : (isPluggedIn ? "Charging, " : "") + batteryLevel + "%");
        getQsTile().updateTile();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BatteryTile", "Received ACTION_BATTERY_CHANGED");
            setBatteryInfo(intent);
        }
    };

    @Override
    public void onStartListening() {
        super.onStartListening();

        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = registerReceiver(receiver, filter);

        getQsTile().setState(Tile.STATE_ACTIVE);
        setBatteryInfo(intent);
    }

    @Override
    public void onStopListening() {
        unregisterReceiver(receiver);
    }
}
