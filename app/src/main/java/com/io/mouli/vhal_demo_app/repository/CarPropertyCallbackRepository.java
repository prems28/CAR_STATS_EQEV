package com.io.mouli.vhal_demo_app.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.io.mouli.vhal_demo_app.handler.BasePropertyHandler;
import com.io.mouli.vhal_demo_app.handler.CarPropertyHandler;


public class CarPropertyCallbackRepository {

    private static CarPropertyCallbackRepository repository;
    private static final String TAG = CarPropertyCallbackRepository.class.getName();
    private MutableLiveData<Integer> vehicleSideVulnerableRoadUser = new MutableLiveData<>();
    private MutableLiveData<Integer> gearSelection = new MutableLiveData<>();
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

    /**
     * CarPropertyManager callbacks run on a binder thread — use postValue, not setValue.
     */
    public void sendSignal(int vehicleSide) {
        Log.i(TAG, " vehicleSide " + vehicleSide);
        this.vehicleSideVulnerableRoadUser.postValue(vehicleSide);
    }
    public LiveData<Integer> getGearSignalValue(){
        Log.i(TAG, " getVulnerableRoadUserMonitoring ");
        return this.gearSelection;
    }
    public void sendGearSignal(int vehicleSide) {
        Log.i(TAG, " vehicleSide " + vehicleSide);
        this.gearSelection.postValue(vehicleSide);
    }
}
