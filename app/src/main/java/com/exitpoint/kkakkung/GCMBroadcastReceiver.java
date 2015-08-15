package com.exitpoint.kkakkung;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by S on 2015-08-12.
 */
public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "GCMBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //GCM Server로부터 메시지를 받으면 이부분이 활성화 됌.
        Log.d(TAG, "onReceive() called.");

        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);

    }
}
