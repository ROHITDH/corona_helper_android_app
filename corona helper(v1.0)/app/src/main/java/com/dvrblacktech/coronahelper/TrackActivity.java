package com.dvrblacktech.coronahelper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.dvrblacktech.coronahelper.MainActivity.encodeForFirebaseKey;
import static com.dvrblacktech.coronahelper.MainActivity.phoneNo;
import static com.dvrblacktech.coronahelper.MainActivity.rohitdh;

public class TrackActivity extends Activity {

    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            rohitdh = user.getEmail();
            rohitdh= encodeForFirebaseKey(rohitdh);

            phoneNo=user.getPhoneNumber();
            if(phoneNo==null)
            {
                phoneNo = "";
            }
            if(rohitdh==null)
            {
                rohitdh= "";
            }
            MainActivity.mailplusphone = rohitdh + phoneNo;

            SendUserToMainActivity();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }



    private void SendUserToMainActivity()
    {
        Intent trackIntent = new Intent(TrackActivity.this,MainActivity.class);
        startActivity(trackIntent);
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {
            finish();
        }
    }
}