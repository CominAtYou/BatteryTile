package com.cominatyou.batterytile.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Class for user-configurable scanf-style formatting of tile text.
 */
public class TileTextFormatter {
    private final HashMap<String, Object> formatters = new HashMap<>();

    public TileTextFormatter(Context context) {
        final BatteryManager bm = context.getSystemService(BatteryManager.class);
        final Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;

        // CURRENT_NOW and CURRENT_AVERAGE are positive when charging and negative when discharging
        if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            // Samsung devices, for some reason, returns this in milli-amperes instead of microamperes like the Android documentation specifies
            formatters.put("c", bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW));
            formatters.put("a", bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE));
        }
        else {
            // Android provides these as microamperes, so divide by 1000 to convert to mA
            formatters.put("c", bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000);
            formatters.put("a", bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE) / 1000);
        }

        // Temperature of the battery
        formatters.put("t", batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f); // Celsius
        formatters.put("f", (int) (batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10 * 1.8 + 32)); // Fahrenheit

        // Voltage of the battery - provided as mV by Android, so divide by 1000 to get volts
        formatters.put("v", new DecimalFormat("#.##").format(batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000f));

        // Battery level
        formatters.put("l", batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
    }

    public String format(String format) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == '%') {
                if (i + 1 < format.length()) {
                    final String formatter = format.substring(i + 1, i + 2);
                    if (formatters.containsKey(formatter)) {
                        formatted.append(formatters.get(formatter));
                        i++;
                    }
                    else {
                        formatted.append('%');
                    }
                }
                else {
                    formatted.append('%');
                }
            }
            else {
                formatted.append(format.charAt(i));
            }
        }

        return formatted.toString();
    }
}
