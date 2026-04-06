package com.io.mouli.vhal_demo_app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.io.mouli.vhal_demo_app.R;
import com.io.mouli.vhal_demo_app.viewModel.VhalViewModel;



public class DemoFragment extends Fragment {
    private static String TAG = DemoFragment.class.getName();

    Toolbar toolbar;

    private TextView  rxText, sendTxt;

    private VhalViewModel viewModel;
    private int statCheck = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VhalViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo_app, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        observeBlindSpotViewModel();

    }

    private void initView(View view) {
        rxText = view.findViewById(R.id.tvRecieveSignal);
        sendTxt = view.findViewById(R.id.tvSendSignal);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i( TAG, getString(R.string.onclick_button_1_triggered));
                if (statCheck == 0) {
                    viewModel.setValue(3);
                    statCheck = 1;
                } else {
                    viewModel.setValue(1);
                    statCheck = 0;
                }

            }
        });
    }

    private void observeBlindSpotViewModel() {
        if (viewModel.observeSignal() != null) {
            viewModel.observeSignal().observe(getViewLifecycleOwner(), value -> {
                Log.i(TAG, getString(R.string.vehiclesideforblindmonitoring) + value);

                rxText.setText(String.format("%s%s", getString(R.string.received_value_is), value.toString()));

            });
        }
    }

    private void observeGearSelectionFromViewModel() {
        if (viewModel.observeSignal() != null) {
            viewModel.observeGearSelectionSignal().observe(getViewLifecycleOwner(), value -> {
                Log.i(TAG, getString(R.string.vehiclesideforblindmonitoring) + value);

                rxText.setText(String.format("%s%s", getString(R.string.received_value_is), value.toString()));

            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}