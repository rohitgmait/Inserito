package com.event.inserito.dost;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by rgupt on 02-10-2016.
 */
public class WaterAlarmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, WaterSensorService.class);
        startWakefulService(context, service);
    }
}
