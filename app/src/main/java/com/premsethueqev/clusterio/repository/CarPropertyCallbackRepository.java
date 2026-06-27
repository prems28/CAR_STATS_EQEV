package com.premsethueqev.clusterio.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.premsethueqev.clusterio.handler.BasePropertyHandler;
import com.premsethueqev.clusterio.handler.CarPropertyHandler;


public class CarPropertyCallbackRepository {

    private static CarPropertyCallbackRepository repository;
    private static final String TAG = CarPropertyCallbackRepository.class.getName();
    private MutableLiveData<Integer> vehicleSideVulnerableRoadUser = new MutableLiveData<>();
    private final MutableLiveData<Float> evBatteryLevel = new MutableLiveData<>();
    private MutableLiveData<Integer> evChargeState = new MutableLiveData<>();
    private MutableLiveData<Float> evBatteryCapacity = new MutableLiveData<>();
    private MutableLiveData<Integer> evChargePortOpen = new MutableLiveData<>();
    private MutableLiveData<Integer> evChargePortConnected = new MutableLiveData<>();
    private MutableLiveData<Integer> evPortLocation = new MutableLiveData<>();
    private CarPropertyHandler carPropertyHandler;

    public void getCarPropertyHandlerInstance(){
        if(carPropertyHandler == null){
            carPropertyHandler = new CarPropertyHandler(BasePropertyHandler.getCarPropertyManagerInstance());
        }
    }

    public static CarPropertyCallbackRepository getInstance(){
        synchronized (CarPropertyCallbackRepository.class){
            if(repository == null){
                repository = new CarPropertyCallbackRepository();
            }
            return repository;
        }

    }

    public LiveData<Integer> getSignalValue(){
        Log.i(TAG, " getVulnerableRoadUserMonitoring ");
        return this.vehicleSideVulnerableRoadUser;
    }
    public void setVmSignal(int val) {
        try {
            getCarPropertyHandlerInstance();
            if (carPropertyHandler != null) {
                carPropertyHandler.setSignalValue(val);
            }
        } catch (Throwable t) {
            Log.w(TAG, "setVmSignal: " + t.getMessage());
        }
    }

    public void refreshProperties() {
        try {
            getCarPropertyHandlerInstance();
            if (carPropertyHandler != null) {
                carPropertyHandler.getDefaultProperties();
            }
        } catch (Throwable t) {
            Log.w(TAG, "refreshProperties: " + t.getMessage());
        }
    }

    /**
     * CarPropertyManager callbacks run on a binder thread — use postValue, not setValue.
     */
    public void sendSignal(int vehicleSide) {
        Log.i(TAG, " vehicleSide " + vehicleSide);
        this.vehicleSideVulnerableRoadUser.postValue(vehicleSide);
    }

    public LiveData<Float> getEvBatteryLevel() {
        return this.evBatteryLevel;
    }

    /**
     * CarPropertyManager callbacks run on a binder thread — use postValue, not setValue.
     * @param percent state of charge as a percentage (0-100)
     */
    public void sendEvBatteryLevel(float percent) {
        Log.i(TAG, "evBatteryLevel (%) " + percent);
        this.evBatteryLevel.postValue(percent);
    }

    public LiveData<Integer> getEvChargeState() {
        return this.evChargeState;
    }

    /**
     * @param state raw VHAL EV_CHARGE_STATE value (see android.car.VehiclePropertyIds#EV_CHARGE_STATE)
     */
    public void sendEvChargeState(int state) {
        Log.i(TAG, "evChargeState " + state);
        this.evChargeState.postValue(state);
    }

    public LiveData<Float> getEvBatteryCapacity() {
        return this.evBatteryCapacity;
    }

    /**
     * @param capacityWh INFO_EV_BATTERY_CAPACITY, the EV's total battery capacity in Wh
     */
    public void sendEvBatteryCapacity(float capacityWh) {
        Log.i(TAG, "evBatteryCapacity (Wh) " + capacityWh);
        this.evBatteryCapacity.postValue(capacityWh);
    }

    public LiveData<Integer> getEvChargePortOpen() {
        return this.evChargePortOpen;
    }

    /**
     * @param isOpen 1 if EV_CHARGE_PORT_OPEN is true, else 0
     */
    public void sendEvChargePortOpen(int isOpen) {
        Log.i(TAG, "evChargePortOpen " + isOpen);
        this.evChargePortOpen.postValue(isOpen);
    }

    public LiveData<Integer> getEvChargePortConnected() {
        return this.evChargePortConnected;
    }

    /**
     * @param isConnected 1 if EV_CHARGE_PORT_CONNECTED is true, else 0
     */
    public void sendEvChargePortConnected(int isConnected) {
        Log.i(TAG, "evChargePortConnected " + isConnected);
        this.evChargePortConnected.postValue(isConnected);
    }

    public LiveData<Integer> getEvPortLocation() {
        return this.evPortLocation;
    }

    /**
     * @param location raw INFO_EV_PORT_LOCATION enum value (see android.car.VehiclePropertyIds#INFO_EV_PORT_LOCATION)
     */
    public void sendEvPortLocation(int location) {
        Log.i(TAG, "evPortLocation " + location);
        this.evPortLocation.postValue(location);
    }
}
