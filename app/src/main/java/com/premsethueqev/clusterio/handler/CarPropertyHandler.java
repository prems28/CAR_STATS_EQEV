package com.premsethueqev.clusterio.handler;

import android.car.VehicleAreaType;
import android.car.VehiclePropertyIds;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.util.Log;

import com.premsethueqev.clusterio.constant.ExteriorLightConstant;


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
        propertyList.add(VehiclePropertyIds.EV_BATTERY_LEVEL);
        propertyList.add(ExteriorLightConstant.EV_CHARGE_STATE_FALLBACK);
        propertyList.add(VehiclePropertyIds.INFO_EV_BATTERY_CAPACITY);
        propertyList.add(VehiclePropertyIds.EV_CHARGE_PORT_OPEN);
        propertyList.add(VehiclePropertyIds.EV_CHARGE_PORT_CONNECTED);
        propertyList.add(VehiclePropertyIds.INFO_EV_PORT_LOCATION);
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
            case VehiclePropertyIds.EV_BATTERY_LEVEL:
                sendingEvBatteryLevelToRepo(valueToUiFloat(carPropertyValue.getValue()));
                break;
            case ExteriorLightConstant.EV_CHARGE_STATE_FALLBACK:
                sendingEvChargeStateToRepo(valueToUiInt(carPropertyValue.getValue()));
                break;
            case VehiclePropertyIds.INFO_EV_BATTERY_CAPACITY:
                sendingEvBatteryCapacityToRepo(valueToUiFloat(carPropertyValue.getValue()));
                break;
            case VehiclePropertyIds.EV_CHARGE_PORT_OPEN:
                sendingEvChargePortOpenToRepo(valueToUiInt(carPropertyValue.getValue()));
                break;
            case VehiclePropertyIds.EV_CHARGE_PORT_CONNECTED:
                sendingEvChargePortConnectedToRepo(valueToUiInt(carPropertyValue.getValue()));
                break;
            case VehiclePropertyIds.INFO_EV_PORT_LOCATION:
                sendingEvPortLocationToRepo(valueToUiInt(carPropertyValue.getValue()));
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

    private static float valueToUiFloat(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Long) {
            return ((Long) value).floatValue();
        }
        if (value instanceof Double) {
            return ((Double) value).floatValue();
        }
        return 0f;
    }

    /**
     * EV_BATTERY_LEVEL is reported as a raw energy value in Wh on this platform.
     * EV_CURRENT_BATTERY_CAPACITY (which would let us convert this to a percentage)
     * is not present in this GM android.car.jar stub, so we surface the raw Wh
     * value directly. If/when the capacity property becomes available (e.g. via
     * the GM developer-portal NDA jar), this can be extended to compute a %.
     */
    private float toBatteryPercent(float rawBatteryLevelWh) {
        return rawBatteryLevelWh;
    }

    private void sendingSignalToRepo(int side) {
        Log.i(TAG,"sending signal" + side);
        carPropertyCallbackRepository.sendSignal(side);
    }

    private void sendingEvBatteryLevelToRepo(float rawBatteryLevelWh) {
        float percent = toBatteryPercent(rawBatteryLevelWh);
        Log.i(TAG, "EV battery level (raw=" + rawBatteryLevelWh + ") -> " + percent);
        carPropertyCallbackRepository.sendEvBatteryLevel(percent);
    }

    private void sendingEvChargeStateToRepo(int chargeState) {
        Log.i(TAG, "EV charge state " + chargeState);
        carPropertyCallbackRepository.sendEvChargeState(chargeState);
    }

    private void sendingEvBatteryCapacityToRepo(float capacityWh) {
        Log.i(TAG, "EV battery capacity (Wh) " + capacityWh);
        carPropertyCallbackRepository.sendEvBatteryCapacity(capacityWh);
    }

    private void sendingEvChargePortOpenToRepo(int isOpen) {
        Log.i(TAG, "EV charge port open " + isOpen);
        carPropertyCallbackRepository.sendEvChargePortOpen(isOpen);
    }

    private void sendingEvChargePortConnectedToRepo(int isConnected) {
        Log.i(TAG, "EV charge port connected " + isConnected);
        carPropertyCallbackRepository.sendEvChargePortConnected(isConnected);
    }

    private void sendingEvPortLocationToRepo(int location) {
        Log.i(TAG, "EV port location " + location);
        carPropertyCallbackRepository.sendEvPortLocation(location);
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

        if (mCarPropertyManager != null) {
            try {
                float batteryLevelWh = mCarPropertyManager.getFloatProperty(
                        VehiclePropertyIds.EV_BATTERY_LEVEL, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
                sendingEvBatteryLevelToRepo(batteryLevelWh);
            } catch (Throwable t) {
                Log.w(TAG, "EV_BATTERY_LEVEL unavailable: " + t.getMessage());
            }
            try {
                int chargeState = mCarPropertyManager.getIntProperty(
                        ExteriorLightConstant.EV_CHARGE_STATE_FALLBACK, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
                sendingEvChargeStateToRepo(chargeState);
            } catch (Throwable t) {
                Log.w(TAG, "EV_CHARGE_STATE unavailable: " + t.getMessage());
            }
            try {
                float capacityWh = mCarPropertyManager.getFloatProperty(
                        VehiclePropertyIds.INFO_EV_BATTERY_CAPACITY, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
                sendingEvBatteryCapacityToRepo(capacityWh);
            } catch (Throwable t) {
                Log.w(TAG, "INFO_EV_BATTERY_CAPACITY unavailable: " + t.getMessage());
            }
            try {
                boolean portOpen = mCarPropertyManager.getBooleanProperty(
                        VehiclePropertyIds.EV_CHARGE_PORT_OPEN, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
                sendingEvChargePortOpenToRepo(portOpen ? 1 : 0);
            } catch (Throwable t) {
                Log.w(TAG, "EV_CHARGE_PORT_OPEN unavailable: " + t.getMessage());
            }
            try {
                boolean portConnected = mCarPropertyManager.getBooleanProperty(
                        VehiclePropertyIds.EV_CHARGE_PORT_CONNECTED, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
                sendingEvChargePortConnectedToRepo(portConnected ? 1 : 0);
            } catch (Throwable t) {
                Log.w(TAG, "EV_CHARGE_PORT_CONNECTED unavailable: " + t.getMessage());
            }
            try {
                int portLocation = mCarPropertyManager.getIntProperty(
                        VehiclePropertyIds.INFO_EV_PORT_LOCATION, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
                sendingEvPortLocationToRepo(portLocation);
            } catch (Throwable t) {
                Log.w(TAG, "INFO_EV_PORT_LOCATION unavailable: " + t.getMessage());
            }
        }
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
