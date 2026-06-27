package com.premsethueqev.clusterio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.premsethueqev.clusterio.R;
import com.premsethueqev.clusterio.fragment.DemoFragment;
import com.premsethueqev.clusterio.service.CarPropertyServiceManager;

public class DemoActivity extends AppCompatActivity {

    Toolbar toolbar;
    private static final String TAG = DemoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
       // requestPermission();
        changeFragment();
    }
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public void changeFragment() {
        startService(new Intent(this, CarPropertyServiceManager.class));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DemoFragment demoFragment = new DemoFragment();
        fragmentTransaction.add(R.id.fragment_container, demoFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "on permission request call");
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeFragment();
            } else {
                requestPermission();
                Log.i(TAG, "requestPermission");
            }
        }
    }

    public void requestPermission() {
        int speedPermission = ContextCompat.checkSelfPermission(this, "android.car.permission.CAR_SPEED");
        if (speedPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { "android.car.permission.CAR_SPEED" }, 1);
            Log.i(TAG, "requesting permission");
        } else {
            // Permission already granted, proceed with accessing car speed
            Log.i(TAG, "Permission granted");
            changeFragment();
        }
    }

}