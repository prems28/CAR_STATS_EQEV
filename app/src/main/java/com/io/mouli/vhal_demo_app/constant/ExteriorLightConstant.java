package com.io.mouli.vhal_demo_app.constant;

public class ExteriorLightConstant {
    public static final int CUSTOM_S2I_EXT_WORK_LIGHT_CMD_HMI = 287310850;
    public static final int CUSTOM_I2S_EXT_WORK_LIGHT_VEHICLE_STATUS_HMI = 287310851;

    /**
     * android.car.VehiclePropertyIds.EV_CHARGE_STATE (0x1140040D).
     * Defined here as a fallback because this GM android.car.jar stub
     * is missing the EV_CHARGE_STATE constant.
     */
    public static final int EV_CHARGE_STATE_FALLBACK = 0x1140040D;

    /**
     * android.car.VehiclePropertyIds.EV_BATTERY_AVERAGE_TEMPERATURE (0x11600351).
     * Defined here as a fallback because this GM android.car.jar stub
     * is also missing this constant (FLOAT, VEHICLE | GLOBAL area).
     */
    public static final int EV_BATTERY_AVERAGE_TEMPERATURE_FALLBACK = 0x11600351;
}
