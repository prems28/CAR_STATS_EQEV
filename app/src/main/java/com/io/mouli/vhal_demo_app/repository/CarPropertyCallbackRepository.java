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
    public void setVmSignal(int val){
        getCarPropertyHandlerInstance();
        carPropertyHandler.setSignalValue(val);
    }
    public void sendSignal(int vehicleSide){
        //side 0 == BothSideObjectDetected
        // side 1 = LeftSideObjectDetected
        // side 2 = RightSideObjectDetected
        Log.i(TAG, " vehicleSide "+ vehicleSide);
        this.vehicleSideVulnerableRoadUser.setValue(vehicleSide);
    }
    public LiveData<Integer> getGearSignalValue(){
        Log.i(TAG, " getVulnerableRoadUserMonitoring ");
        return this.gearSelection;
    }
    public void sendGearSignal(int vehicleSide){
        //side 0 == BothSideObjectDetected
        // side 1 = LeftSideObjectDetected
        // side 2 = RightSideObjectDetected
        Log.i(TAG, " vehicleSide "+ vehicleSide);
        this.gearSelection.setValue(vehicleSide);
    }
}
