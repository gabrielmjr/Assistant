package com.gabrielMJr.twaire.assistant.device_hw_sw;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.BatteryManager;

// Extending broadcast for continue data receiver
public class BatteryReceiver extends BroadcastReceiver
{

    // Attributes
    private static String status;
    private static String health;
    private static String percentage;

    private static String action;


    // Final Strings 
    public static final String STATUS_CHARGING = "charging";
    public static final String STATUS_DISCHARGING = "discharging";
    public static final String STATUS_FULL = "full";
    public static final String HEALTH_COLD = "cold";
    public static final String HEALTH_DEAD = "dead";
    public static final String HEALTH_GOOD = "good";
    public static final String HEALTH_OVER_VOLTAGE = "over_voltage";
    public static final String HEALTH_OVERHEAT = "overheat";
    public static final String HEALTH_UNSPECIFIED_FAILURE = "unspecified_failure";
    public static final String UNKNOWN = "unknown";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        action = intent.getAction();

        if (action != null
            &&
            action == intent.ACTION_BATTERY_CHANGED)
        {

            // Getting battery status
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            switch (status)
             {
             // If charging
             case BatteryManager.BATTERY_STATUS_CHARGING:
             this.status = STATUS_CHARGING;
             break;

             // If discharging
             case BatteryManager.BATTERY_STATUS_DISCHARGING:
             this.status = STATUS_DISCHARGING;
             break;

             // If full
             case BatteryManager.BATTERY_STATUS_FULL:
             this.status = STATUS_FULL;
             break;

             // If unknown status
             case BatteryManager.BATTERY_STATUS_UNKNOWN:
             this.status = UNKNOWN;
             break;

             default:
             this.status = null;
             break;
             }


             // For battery health
             status = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

             switch (status)
             {
             // Battery cold
             case BatteryManager.BATTERY_HEALTH_COLD:
             health = HEALTH_COLD;
             break;

             // Battery dead
             case BatteryManager.BATTERY_HEALTH_DEAD:
             health = HEALTH_DEAD;
             break;

             // Battery good
             case BatteryManager.BATTERY_HEALTH_GOOD:
             health = HEALTH_GOOD;
             break;

             // Battery over voltage
             case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
             health = HEALTH_OVER_VOLTAGE;
             break;

             // Battery over heat
             case BatteryManager.BATTERY_HEALTH_OVERHEAT:
             health = HEALTH_OVERHEAT;
             break;

             // Battery health unknown
             case BatteryManager.BATTERY_HEALTH_UNKNOWN:
             health = UNKNOWN;
             break;

             // Battery health unspecified
             case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
             health = HEALTH_UNSPECIFIED_FAILURE;
             break;

             default:
             health = null;
             break;
             }


             // Perccentage
             int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
             int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

             percentage = String.valueOf((100 * level) / scale);
        }
    }

    // Setters and getters
     public String getStatus()
     {
     return status;
     }

     public String getHealth()
     {
     return health;
     }

     public String getPercentage()
     {
     return percentage;
     }

      @Override
     public String toString()
     {
     return status 
     + ", with " 
     + health
     + " health and " 
     + percentage
     + " percent.";
     }
}
