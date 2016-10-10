package com.event.inserito.dost;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by rgupt on 09-10-2016.
 */
public class IntruderAlarmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, IntruderSensorService.class);
        startWakefulService(context, service);
    }
}
