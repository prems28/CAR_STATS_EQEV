package com.io.mouli.vhal_demo_app.handler;

import android.car.VehicleAreaType;
import android.car.VehiclePropertyIds;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.util.Log;

import com.io.mouli.vhal_demo_app.constant.ExteriorLightConstant;


public class CarPropertyHandler extends BasePropertyHandler {

    private static final String TAG = CarPropertyHandler.class.getName();

    public CarPropertyHandler(CarPropertyManager carPropertyManager) {
        super(carPropertyManager);
        Log.i(TAG,"Constructor initialized");
    }

    @Override
    void generatePropertyList() {
        propertyList.add(VehiclePropertyIds.GEAR_SELECTION);
        propertyList.add(ExteriorLightConstant.CUSTOM_S2I_EXT_WORK_LIGHT_CMD_HMI);
        propertyList.add(ExteriorLightConstant.CUSTOM_I2S_EXT_WORK_LIGHT_VEHICLE_STATUS_HMI);
         Log.i(TAG,"Property generated.");

    }

    @Override
    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        Log.i(TAG, "onChangeEvent() id " + carPropertyValue.getPropertyId() + " , value "
                + carPropertyValue.getValue());
        int propertyId = carPropertyValue.getPropertyId();
        switch (propertyId) {

            case ExteriorLightConstant.CUSTOM_I2S_EXT_WORK_LIGHT_VEHICLE_STATUS_HMI:
            case ExteriorLightConstant.CUSTOM_S2I_EXT_WORK_LIGHT_CMD_HMI:
            case VehiclePropertyIds.GEAR_SELECTION:
                sendingSignalToRepo(valueToUiInt(carPropertyValue.getValue()));
                break;
            default:
                break;
        }
    }

    /** VHAL may use Integer or Boolean (or other) for the same vendor ID on different builds. */
    private static int valueToUiInt(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        }
        if (value instanceof Float) {
            return Math.round((Float) value);
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        return 0;
    }

    private void sendingSignalToRepo(int side) {
        Log.i(TAG,"sending signal" + side);
        carPropertyCallbackRepository.sendSignal(side);
    }

    private void sendingGearSignalToRepo(int gearValue) {
        Log.i(TAG,"Gear Value" + gearValue);
        carPropertyCallbackRepository.sendGearSignal(gearValue);
    }
    public void setSignalValue(int val) {
        if (mCarPropertyManager == null) {
            sendingSignalToRepo(val);
            return;
        }
        int prop = ExteriorLightConstant.CUSTOM_S2I_EXT_WORK_LIGHT_CMD_HMI;
        try {
            mCarPropertyManager.setIntProperty(prop, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL, val);
            return;
        } catch (Throwable first) {
            Log.w(TAG, "setIntProperty failed, trying boolean: " + first.getMessage());
        }
        try {
            mCarPropertyManager.setBooleanProperty(prop, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL, val != 0);
            return;
        } catch (Throwable t) {
            Log.w(TAG, "Cannot write work-light 0x11200402 (emulator/OEM blocks this app): " + t.getMessage());
            // Emulator often denies write — still push to UI so the button visibly does something (demo / learning).
            sendingSignalToRepo(val);
        }
    }

    @Override
    public void onErrorEvent(int i, int i1) {
        Log.i(TAG,"onErrrorEvent" + i + "i1: " +i1);
    }

    @Override
    public void getDefaultProperties() {
        int areaDefault = readPropertyAsIntWithBooleanFallback(
                ExteriorLightConstant.CUSTOM_S2I_EXT_WORK_LIGHT_CMD_HMI);
        Log.i(TAG, "areaDefault" + areaDefault);
        sendingSignalToRepo(areaDefault);
    }

    private int readPropertyAsIntWithBooleanFallback(int propId) {
        if (mCarPropertyManager == null) {
            return 0;
        }
        try {
            return mCarPropertyManager.getIntProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
        } catch (IllegalArgumentException e) {
            return mCarPropertyManager.getBooleanProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL) ? 1 : 0;
        }
    }
}
