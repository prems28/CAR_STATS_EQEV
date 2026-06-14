package com.io.mouli.vhal_demo_app.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.io.mouli.vhal_demo_app.repository.CarPropertyCallbackRepository;


public class VhalViewModel extends ViewModel {

    private CarPropertyCallbackRepository repository1 = CarPropertyCallbackRepository.getInstance();

    public LiveData<Integer> observeSignal() {

        return repository1.getSignalValue();
    }

    public LiveData<Float> observeEvBatteryLevel() {
        return repository1.getEvBatteryLevel();
    }

    public LiveData<Integer> observeEvChargeState() {
        return repository1.getEvChargeState();
    }

    public LiveData<Float> observeEvBatteryCapacity() {
        return repository1.getEvBatteryCapacity();
    }

    public LiveData<Integer> observeEvChargePortOpen() {
        return repository1.getEvChargePortOpen();
    }

    public LiveData<Integer> observeEvChargePortConnected() {
        return repository1.getEvChargePortConnected();
    }

    public LiveData<Integer> observeEvPortLocation() {
        return repository1.getEvPortLocation();
    }

    public void setValue(int val){
        repository1.setVmSignal(val);
    }

    public void refresh() {
        repository1.refreshProperties();
    }
}
