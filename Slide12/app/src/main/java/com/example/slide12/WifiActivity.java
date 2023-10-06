package com.example.slide12;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends AppCompatActivity {

    private Switch onOffWifiSwitch;
    private Button refreshButton;
    private ListView wifiListView;
    private WifiManager wifiManager;

    private TextView wifiStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_layout);

        onOffWifiSwitch = findViewById(R.id.on_off_wifi);
        refreshButton = findViewById(R.id.btnRefresh);
        wifiListView = findViewById(R.id.wifiListView);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiStatus = findViewById(R.id.wifiStatus);

        if (wifiManager.isWifiEnabled()) {
            onOffWifiSwitch.setChecked(true);
            wifiStatus.setText(wifiManager.getConnectionInfo().toString());
            Log.d("WifiActivity", wifiManager.getConnectionInfo().toString());

        } else {
            onOffWifiSwitch.setChecked(false);
        }

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWifiList();
            }
        });

        onOffWifiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                wifiManager.setWifiEnabled(true);
                showToast("WiFi has been turned on.");
            } else {
                wifiManager.setWifiEnabled(false);
                showToast("WiFi has been turned off.");
            }
        });

        refreshWifiList();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void refreshWifiList() {
        List<String> wifiList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            wifiList.add("Location permission is not granted.");
            return;
        }
        List<ScanResult> scanResults = wifiManager.getScanResults();

        if (wifiManager.isWifiEnabled()) {
            for (ScanResult result : scanResults) {
                wifiList.add(result.SSID + " - " + result.capabilities);
            }
        } else {
            wifiList.add("WiFi is not enabled.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiList);
        wifiListView.setAdapter(adapter);
    }
}