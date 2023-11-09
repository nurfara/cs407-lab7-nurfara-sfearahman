package com.cs407.lab7;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // callback when permission is either granted or refused
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    // send toast if permission is refused
                    Toast.makeText(this, R.string.please_allow_notification, Toast.LENGTH_LONG).show();
                }
            });

    private void requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // notification permission not required until Android 13 (Tiramisu)
            return;
        }
        // check if already been granted permission
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted, need to request
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind UI to on-click callback.
        // when user clicks button, send the notif
        Button buttonSend = findViewById(R.id.buttonSend);
        EditText editTextSender = findViewById(R.id.editTextSender);
        EditText editTextMessage = findViewById(R.id.editTextMessage);

        requestPermission();
        NotificationHelper.getInstance().createNotificationChannel(getApplicationContext());

        buttonSend.setOnClickListener(view -> {
            NotificationHelper.getInstance().appendNotificationItem(
                    editTextSender.getText().toString(), editTextMessage.getText().toString()
            );
            NotificationHelper.getInstance().showNotification(getApplicationContext(), -1);
        });
    }
}