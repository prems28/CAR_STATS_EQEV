/**/
package com.premsethueqev.clusterio.core;

import android.car.Car;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.premsethueqev.clusterio.handler.BasePropertyHandler;
import com.premsethueqev.clusterio.handler.CarPropertyHandler;
import com.premsethueqev.clusterio.interfaces.IBasePropertyHandler;


public class CarPropertyManager {

    private static final String TAG = CarPropertyManager.class.getName();
    private android.car.hardware.property.CarPropertyManager mCarPropertyManager;

    private IBasePropertyHandler iBasePropertyHandler;

    public static Context context;
    public static android.car.hardware.property.CarPropertyManager mCarPropertyManagerinstance;


    private int isDescreteDatachanged = 0;


    public CarPropertyManager(Context ctx) {
        context = ctx;
        connectToCar(ctx);


    }

    private void connectToCar(Context context) {
        Log.i(TAG, "ConnectToCar()");
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
            Log.w(TAG, "Skipping Car API: not an Android Automotive device (phone/tablet emulators have no car_service / VHAL).");
            return;
        }
        Car mCarApiClient = null;
        try {
            mCarApiClient = Car.createCar(context);
            boolean isCarConnected = mCarApiClient.isConnected();
            Log.i(TAG, "isConnected=" + isCarConnected);
            if (isCarConnected) {
                handleCarLifeCycleEvent(mCarApiClient, isCarConnected);
            } else {
                Log.w(TAG, "Car service present but not connected yet.");
            }
        } catch (RuntimeException e) {
            Log.w(TAG, "Car API unavailable: " + e.getMessage());
        } finally {
            if (mCarApiClient != null && mCarPropertyManager == null) {
                try {
                    mCarApiClient.disconnect();
                } catch (RuntimeException ignored) {
                }
            }
        }
    }

    /*
     * Method for generating, registering
     * and getting default property from VHAL
     * */
    private void handleCarLifeCycleEvent(Car mCarApiClient, boolean isCarConnected) {
        Log.i(TAG, String.valueOf(isCarConnected));
        if (mCarApiClient != null) {
            Log.i(TAG, "Car is now ready so register all properties and get all default values.");
            mCarPropertyManager = (android.car.hardware.property.CarPropertyManager) mCarApiClient.getCarManager(Car.PROPERTY_SERVICE);
            BasePropertyHandler.mCarUnitManager = mCarPropertyManager;
            mCarPropertyManagerinstance = mCarPropertyManager;
            generatePropertyHandler();
            registerAllPropertyId();
            getDefaultValuesFromVHAL();
        }
    }

    /*
     * Method for getting Default property
     * */
    private void getDefaultValuesFromVHAL() {
        if (mCarPropertyManager != null) {

            iBasePropertyHandler.getDefaultProperties();


        }
    }
    /*
     * Method for registering all propertyid
     * */
    private void registerAllPropertyId() {
        if (mCarPropertyManager != null) {

            iBasePropertyHandler.registerProperties();


        }
    }
    /*
     * Initialization all property handler class here
     * */
    private void generatePropertyHandler() {
        if (mCarPropertyManager != null) {
            Log.i(TAG, "registerAllPropertyId()");

            iBasePropertyHandler = new CarPropertyHandler(mCarPropertyManager);

        }
    }

    /*
     * Method for un registering all property id
     * */
    public void unRegisterAllpropertyId() {
        if (mCarPropertyManager != null && iBasePropertyHandler != null) {
            iBasePropertyHandler.unRegisterProperties();
        }
    }




}
