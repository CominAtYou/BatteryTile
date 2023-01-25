package com.cominatyou.batterytile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import java.time.Duration;

public class QuickSettingsTileService extends TileService {
    private void setBatteryInfo(Intent intent) {
        final int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        final int plugState = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        final int batteryState = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        final boolean isPluggedIn = plugState == BatteryManager.BATTERY_PLUGGED_AC || plugState == BatteryManager.BATTERY_PLUGGED_USB || plugState == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        final boolean isCharging = batteryState == BatteryManager.BATTERY_STATUS_CHARGING;
        final boolean isFullyCharged = isPluggedIn && batteryLevel == 100;

        if (isFullyCharged) {
            getQsTile().setSubtitle(getString(R.string.fully_charged));
        }
        else if (isCharging) {
            final long remainingTime = getSystemService(BatteryManager.class).computeChargeTimeRemaining();

            // computeChargeTimeRemaining() returns 0 at times for some reason, so check for < 1, not -1
            if (remainingTime < 1) {
                getQsTile().setSubtitle(getString(R.string.charging_no_time_estimate, batteryLevel));
            }
            else {
                Duration duration = Duration.ofMillis(remainingTime);
                final long hours = duration.toHours();
                final long minutes = duration.minusHours(hours).toMinutes();

                if (hours > 0) {
                    getQsTile().setSubtitle(getString(R.string.charging_more_than_one_hour_left, batteryLevel, hours, minutes));
                }
                else {
                    getQsTile().setSubtitle(getString(R.string.charging_less_than_one_hour_left, batteryLevel, minutes));
                }
            }

        }
        else {
            getQsTile().setSubtitle(batteryLevel + "%");
        }

        getQsTile().updateTile();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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
