package com.io.mouli.vhal_demo_app.handler;

import android.car.VehicleAreaType;
import android.car.hardware.property.CarPropertyManager;
import android.util.Log;

import com.io.mouli.vhal_demo_app.constant.BaseVhalConstant;
import com.io.mouli.vhal_demo_app.interfaces.IBasePropertyHandler;
import com.io.mouli.vhal_demo_app.repository.CarPropertyCallbackRepository;


import java.util.ArrayList;


public abstract class BasePropertyHandler implements CarPropertyManager.CarPropertyEventCallback,
        IBasePropertyHandler {
    private static final String TAG = BasePropertyHandler.class.getName();
    protected ArrayList<Integer> propertyList = new ArrayList<>();
    public  CarPropertyManager mCarPropertyManager;
    public  static CarPropertyManager mCarUnitManager;
    public CarPropertyCallbackRepository carPropertyCallbackRepository;


    public BasePropertyHandler(CarPropertyManager carPropertyManager) {
        mCarPropertyManager = carPropertyManager;
        generatePropertyList();
        carPropertyCallbackRepository = CarPropertyCallbackRepository.getInstance();

    }

    public static CarPropertyManager getCarPropertyManagerInstance(){
        return mCarUnitManager;
    }

    abstract void generatePropertyList();


    @Override
    public void registerProperties() {
        Log.i(TAG, "register properties");
        if (propertyList.isEmpty()) {
            Log.i(TAG, "registerProperties() propertyList is empty");
            return;
        }
        propertyList.forEach(prop -> {
            mCarPropertyManager.registerCallback(this, prop, BaseVhalConstant.PROP_RATE);
        });

    }

    @Override
    public void unRegisterProperties() {
        Log.i(TAG, "un-register properties");
        if (propertyList.isEmpty()) {
            Log.i(TAG, "registerProperties() propertyList is empty");
            return;
        }
        propertyList.forEach(prop -> {
            mCarPropertyManager.unregisterCallback(this, prop);
        });
    }
    /**
     * @return boolean
     * @description : Gets Current State for incoming property ID.
     */
    public boolean getBooleanProperty(int propId) {
        if (mCarPropertyManager != null) {
            return mCarPropertyManager.getBooleanProperty(propId, VehicleAreaType.
                    VEHICLE_AREA_TYPE_GLOBAL);
        } else {
            return false;
        }
    }
    /**
     * @return integer array
     * @description : Gets Current State for incoming property ID.
     */
    private Integer[] getIntArrayProperty(int propId) {
        if (mCarPropertyManager != null) {
            int[] array = mCarPropertyManager.getIntArrayProperty(propId, VehicleAreaType.
                    VEHICLE_AREA_TYPE_GLOBAL);
            Integer[] wrappedArray = new Integer[array.length];
            for (int index = 0; index < array.length; index++) {
                wrappedArray[index] = array[index];
            }
            return wrappedArray;
        } else {
            return null;
        }
    }



    /**
     * @return float

     * @description : Gets Current State for incoming property ID.
     */
    protected float getFloatProperty(int propId) {
        if (mCarPropertyManager != null) {
            return mCarPropertyManager.getFloatProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
        } else {
            return 0f;
        }
    }

    /**
     * @return int
     * @description : Gets Current State for incoming property ID.
     */
    protected int getIntProperty(int propId) {
        if (mCarPropertyManager != null) {
            return mCarPropertyManager.getIntProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
        } else {
            return 0;
        }
    }
    /**
     * @return void
     * @description : Set Current State (value-Float) for incoming property ID.
     */
    protected void setIntProperty(int propId, int val) {
        if (mCarPropertyManager != null) {
            mCarPropertyManager.setIntProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,val);
        }
    }
    /**
     * @return void
     * @description : Set Current State (value-Float) for incoming property ID.
     */
    protected void setBooleanProperty(int propId, boolean val) {
        if (mCarPropertyManager != null) {
            mCarPropertyManager.setBooleanProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,val);
        }
    }
    /**
     * @return void
     * @description : Set Current State(value-Float) for incoming property ID.
     */
    protected void setFloatProperty(int propId, float val) {
        if (mCarPropertyManager != null) {
            mCarPropertyManager.setFloatProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL,val);
        }
    }

}
