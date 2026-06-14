package com.io.mouli.vhal_demo_app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.io.mouli.vhal_demo_app.R;
import com.io.mouli.vhal_demo_app.viewModel.VhalViewModel;



public class DemoFragment extends Fragment {
    private static String TAG = DemoFragment.class.getName();

    private TextView  tvEvBatteryLevel, tvEvChargeState,
            tvEvBatteryCapacity, tvEvChargePortOpen, tvEvChargePortConnected, tvEvPortLocation, tvEvEstimatedRange;
    private Button btnRefresh;

    private VhalViewModel viewModel;


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
        observeEvBatteryLevel();
        observeEvChargeState();
        observeEvBatteryCapacity();
        observeEvChargePortOpen();
        observeEvChargePortConnected();
        observeEvPortLocation();

    }

    private void initView(View view) {
        tvEvBatteryLevel = view.findViewById(R.id.tvEvBatteryLevel);
        tvEvChargeState = view.findViewById(R.id.tvEvChargeState);
        tvEvBatteryCapacity = view.findViewById(R.id.tvEvBatteryCapacity);
        tvEvEstimatedRange = view.findViewById(R.id.tvEvEstimatedRange);
        tvEvChargePortOpen = view.findViewById(R.id.tvEvChargePortOpen);
        tvEvChargePortConnected = view.findViewById(R.id.tvEvChargePortConnected);
        tvEvPortLocation = view.findViewById(R.id.tvEvPortLocation);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            Log.i(TAG, "Manual refresh triggered");
            viewModel.refresh();
        });
    }

    private void observeBlindSpotViewModel() {
        if (viewModel.observeSignal() != null) {
            viewModel.observeSignal().observe(getViewLifecycleOwner(), value -> {
                if (value == null) {
                    return;
                }
                Log.i(TAG, getString(R.string.vehiclesideforblindmonitoring) + value);
            });
        }
    }

    private static final float WH_PER_MILE = 250f; // 2.5 kWh/mile = 250 Wh/mile

    private void observeEvBatteryLevel() {
        if (tvEvBatteryLevel == null) {
            return;
        }
        viewModel.observeEvBatteryLevel().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvBatteryLevel.setText(String.format("%s %s", getString(R.string.ev_battery_level_label), getString(R.string.value_not_available)));
                if (tvEvEstimatedRange != null) {
                    tvEvEstimatedRange.setText(String.format("%s %s", getString(R.string.ev_estimated_range_label), getString(R.string.value_not_available)));
                }
                return;
            }
            Log.i(TAG, "EV battery level: " + value);
            tvEvBatteryLevel.setText(String.format("%s %.2f kWh", getString(R.string.ev_battery_level_label), value / 1000f));

            if (tvEvEstimatedRange != null) {
                float estimatedMiles = value / WH_PER_MILE;
                tvEvEstimatedRange.setText(String.format("%s %.1f mi", getString(R.string.ev_estimated_range_label), estimatedMiles));
            }
        });
    }

    private void observeEvChargeState() {
        if (tvEvChargeState == null) {
            return;
        }
        viewModel.observeEvChargeState().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvChargeState.setText(String.format("%s %s", getString(R.string.ev_charge_state_label), getString(R.string.value_not_available)));
                return;
            }
            Log.i(TAG, "EV charge state: " + value);
            tvEvChargeState.setText(String.format("%s %d", getString(R.string.ev_charge_state_label), value));
        });
    }

    private void observeEvBatteryCapacity() {
        if (tvEvBatteryCapacity == null) {
            return;
        }
        viewModel.observeEvBatteryCapacity().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvBatteryCapacity.setText(String.format("%s %s", getString(R.string.ev_battery_capacity_label), getString(R.string.value_not_available)));
                return;
            }
            Log.i(TAG, "EV battery capacity: " + value);
            tvEvBatteryCapacity.setText(String.format("%s %.2f kWh", getString(R.string.ev_battery_capacity_label), value / 1000f));
        });
    }

    private void observeEvChargePortOpen() {
        if (tvEvChargePortOpen == null) {
            return;
        }
        viewModel.observeEvChargePortOpen().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvChargePortOpen.setText(String.format("%s %s", getString(R.string.ev_charge_port_open_label), getString(R.string.value_not_available)));
                return;
            }
            Log.i(TAG, "EV charge port open: " + value);
            tvEvChargePortOpen.setText(String.format("%s %s", getString(R.string.ev_charge_port_open_label), value == 1 ? "Yes" : "No"));
        });
    }

    private void observeEvChargePortConnected() {
        if (tvEvChargePortConnected == null) {
            return;
        }
        viewModel.observeEvChargePortConnected().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvChargePortConnected.setText(String.format("%s %s", getString(R.string.ev_charge_port_connected_label), getString(R.string.value_not_available)));
                return;
            }
            Log.i(TAG, "EV charge port connected: " + value);
            tvEvChargePortConnected.setText(String.format("%s %s", getString(R.string.ev_charge_port_connected_label), value == 1 ? "Yes" : "No"));
        });
    }

    private void observeEvPortLocation() {
        if (tvEvPortLocation == null) {
            return;
        }
        viewModel.observeEvPortLocation().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvPortLocation.setText(String.format("%s %s", getString(R.string.ev_port_location_label), getString(R.string.value_not_available)));
                return;
            }
            Log.i(TAG, "EV port location: " + value);
            tvEvPortLocation.setText(String.format("%s %d", getString(R.string.ev_port_location_label), value));
        });
    }

}