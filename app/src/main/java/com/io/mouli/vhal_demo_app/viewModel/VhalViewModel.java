package com.io.mouli.vhal_demo_app.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.io.mouli.vhal_demo_app.repository.CarPropertyCallbackRepository;


public class VhalViewModel extends ViewModel {

    private CarPropertyCallbackRepository repository1 = CarPropertyCallbackRepository.getInstance();

    public LiveData<Integer> observeSignal() {

        return repository1.getSignalValue();
    }

    public LiveData<Integer> observeGearSelectionSignal() {

        return repository1.getGearSignalValue();
    }

    public void setValue(int val){
        repository1.setVmSignal(val);
    }
}
