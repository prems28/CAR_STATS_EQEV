package com.premsethueqev.clusterio.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.premsethueqev.clusterio.R;
import com.premsethueqev.clusterio.viewModel.VhalViewModel;



public class DemoFragment extends Fragment {
    private static String TAG = DemoFragment.class.getName();

    private TextView  tvEvBatteryLevel, tvEvChargeState,
            tvEvBatteryCapacity, tvEvChargePortOpen, tvEvChargePortConnected, tvEvPortLocation,
            tvEvEstimatedRange, tvEvMinEstimatedRange;
    private Button btnRefresh;
    private ImageView ivRefresh, ivChargePortOpen, ivChargePortConnected;

    private VhalViewModel viewModel;

    /** Cached capacity in Wh — updated by observeEvBatteryCapacity(), read by observeEvBatteryLevel() */
    private float cachedCapacityWh = 0f;
    /** Cached battery level in Wh — used to recompute SoC % when charge state updates */
    private float cachedBatteryLevelWh = 0f;


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
        tvEvMinEstimatedRange = view.findViewById(R.id.tvEvMinEstimatedRange);
        tvEvChargePortOpen = view.findViewById(R.id.tvEvChargePortOpen);
        tvEvChargePortConnected = view.findViewById(R.id.tvEvChargePortConnected);
        tvEvPortLocation = view.findViewById(R.id.tvEvPortLocation);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        ivRefresh = view.findViewById(R.id.ivRefresh);
        ivChargePortOpen = view.findViewById(R.id.ivChargePortOpen);
        ivChargePortConnected = view.findViewById(R.id.ivChargePortConnected);

        // Wire refresh icon click
        ivRefresh.setOnClickListener(v -> {
            Log.i(TAG, "Manual refresh triggered");
            viewModel.refresh();
        });
        // Also keep hidden button wired for safety
        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> viewModel.refresh());
        }
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

    private static final float WH_PER_MILE = 3.5f;
    private static final float WH_PER_MILE_MIN = 2.5f;

    private void updateSocDisplay(float batteryLevelWh) {
        float kWh = batteryLevelWh / 1000f;
        tvEvBatteryLevel.setText(String.format("%s\n%.2f kWh",
                getString(R.string.ev_battery_level_label), kWh));
        // Also refresh charge state display to include SoC %
        updateChargeStateDisplay(batteryLevelWh);
    }

    private void updateChargeStateDisplay(float batteryLevelWh) {
        if (tvEvChargeState == null) return;
        cachedBatteryLevelWh = batteryLevelWh;
        Integer chargeState = viewModel.observeEvChargeState().getValue();
        if (cachedCapacityWh > 0f) {
            float socPercent = (batteryLevelWh / cachedCapacityWh) * 100f;
            if (chargeState != null) {
                tvEvChargeState.setText(String.format("%s\n%d\n%.1f%%",
                        getString(R.string.ev_charge_state_label), chargeState, socPercent));
            } else {
                tvEvChargeState.setText(String.format("%s\n%.1f%%",
                        getString(R.string.ev_charge_state_label), socPercent));
            }
        } else {
            if (chargeState != null) {
                tvEvChargeState.setText(String.format("%s\n%d",
                        getString(R.string.ev_charge_state_label), chargeState));
            } else {
                tvEvChargeState.setText(getString(R.string.ev_charge_state_label));
            }
        }
    }

    private void observeEvBatteryLevel() {
        if (tvEvBatteryLevel == null) {
            return;
        }
        viewModel.observeEvBatteryLevel().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvBatteryLevel.setText(String.format("%s %s", getString(R.string.ev_battery_level_label), getString(R.string.value_not_available)));
                if (tvEvEstimatedRange != null) tvEvEstimatedRange.setText(String.format("%s %s", getString(R.string.ev_estimated_range_label), getString(R.string.value_not_available)));
                if (tvEvMinEstimatedRange != null) tvEvMinEstimatedRange.setText(String.format("%s %s", getString(R.string.ev_min_estimated_range_label), getString(R.string.value_not_available)));
                return;
            }
            Log.i(TAG, "EV battery level: " + value);
            cachedBatteryLevelWh = value;
            updateSocDisplay(value);
            if (tvEvEstimatedRange != null) {
                tvEvEstimatedRange.setText(String.format("%s %.1f mi", getString(R.string.ev_estimated_range_label), value * WH_PER_MILE / 1000));
            }
            if (tvEvMinEstimatedRange != null) {
                tvEvMinEstimatedRange.setText(String.format("%s %.1f mi", getString(R.string.ev_min_estimated_range_label), value * WH_PER_MILE_MIN / 1000));
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
            if (cachedBatteryLevelWh > 0f && cachedCapacityWh > 0f) {
                float socPercent = (cachedBatteryLevelWh / cachedCapacityWh) * 100f;
                tvEvChargeState.setText(String.format("%s\n%d\n%.1f%%",
                        getString(R.string.ev_charge_state_label), value, socPercent));
            } else {
                tvEvChargeState.setText(String.format("%s\n%d",
                        getString(R.string.ev_charge_state_label), value));
            }
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
            cachedCapacityWh = value;
            tvEvBatteryCapacity.setText(String.format("%s %.2f kWh", getString(R.string.ev_battery_capacity_label), value / 1000f));
            Float batteryLevel = viewModel.observeEvBatteryLevel().getValue();
            if (batteryLevel != null && tvEvBatteryLevel != null) {
                updateSocDisplay(batteryLevel);
            }
            // Refresh charge state circle now that capacity is known
            if (cachedBatteryLevelWh > 0f) {
                updateChargeStateDisplay(cachedBatteryLevelWh);
            }
        });
    }

    private void observeEvChargePortOpen() {
        if (tvEvChargePortOpen == null) {
            return;
        }
        viewModel.observeEvChargePortOpen().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvChargePortOpen.setText(String.format("%s %s", getString(R.string.ev_charge_port_open_label), getString(R.string.value_not_available)));
                if (ivChargePortOpen != null) ivChargePortOpen.setColorFilter(0xFF888888);
                return;
            }
            Log.i(TAG, "EV charge port open: " + value);
            tvEvChargePortOpen.setText(String.format("%s %s", getString(R.string.ev_charge_port_open_label), value == 1 ? "Yes" : "No"));
            if (ivChargePortOpen != null) {
                // Green when open, grey when closed
                ivChargePortOpen.setColorFilter(value == 1 ? 0xFF4CAF50 : 0xFF888888);
            }
        });
    }

    private void observeEvChargePortConnected() {
        if (tvEvChargePortConnected == null) {
            return;
        }
        viewModel.observeEvChargePortConnected().observe(getViewLifecycleOwner(), value -> {
            if (value == null) {
                tvEvChargePortConnected.setText(String.format("%s %s", getString(R.string.ev_charge_port_connected_label), getString(R.string.value_not_available)));
                if (ivChargePortConnected != null) ivChargePortConnected.setColorFilter(0xFF888888);
                return;
            }
            Log.i(TAG, "EV charge port connected: " + value);
            tvEvChargePortConnected.setText(String.format("%s %s", getString(R.string.ev_charge_port_connected_label), value == 1 ? "Yes" : "No"));
            if (ivChargePortConnected != null) {
                // Blue when connected, grey when not
                ivChargePortConnected.setColorFilter(value == 1 ? 0xFF4fa5d5 : 0xFF888888);
            }
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