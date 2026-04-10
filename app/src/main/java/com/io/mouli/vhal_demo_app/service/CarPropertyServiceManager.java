package com.io.mouli.vhal_demo_app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.io.mouli.vhal_demo_app.core.CarPropertyManager;


public class CarPropertyServiceManager extends Service  {

    private static final String TAG = CarPropertyServiceManager.class.getName();
    private CarPropertyManager carPropertyManager;
    private final IBinder iBinder = new CarInfoServiceClassBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        Context context = getApplicationContext();
        carPropertyManager = new CarPropertyManager(context);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand()");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, " onDestroy");
        if (carPropertyManager != null) {
            carPropertyManager.unRegisterAllpropertyId();
        }
    }

    private final class CarInfoServiceClassBinder extends Binder {
        CarPropertyServiceManager getService() {
            return CarPropertyServiceManager.this;
        }
    }

}
