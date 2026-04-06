package com.io.mouli.vhal_demo_app.handler;

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
                int val = getIntProperty(ExteriorLightConstant.CUSTOM_I2S_EXT_WORK_LIGHT_VEHICLE_STATUS_HMI);
                sendingSignalToRepo(val);
                break;
            case VehiclePropertyIds.GEAR_SELECTION:
                int currentGearVal = getIntProperty(VehiclePropertyIds.GEAR_SELECTION);
                sendingSignalToRepo(currentGearVal);
                break;



        }
    }

    private void sendingSignalToRepo(int side) {
        Log.i(TAG,"sending signal" + side);
        carPropertyCallbackRepository.sendSignal(side);
    }

    private void sendingGearSignalToRepo(int gearValue) {
        Log.i(TAG,"Gear Value" + gearValue);
        carPropertyCallbackRepository.sendGearSignal(gearValue);
    }
    public void setSignalValue(int val){
        setIntProperty(ExteriorLightConstant.CUSTOM_S2I_EXT_WORK_LIGHT_CMD_HMI, val);
    }

    @Override
    public void onErrorEvent(int i, int i1) {
        Log.i(TAG,"onErrrorEvent" + i + "i1: " +i1);
    }

    @Override
    public void getDefaultProperties() {
        int areaDefault = getIntProperty(ExteriorLightConstant.CUSTOM_S2I_EXT_WORK_LIGHT_CMD_HMI);
        Log.i(TAG,"areaDefault" + areaDefault);
        sendingSignalToRepo(areaDefault);

    }
}
