package com.dvrblacktech.coronahelper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import android.app.Notification;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.Manifest;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static com.dvrblacktech.coronahelper.MainActivity.encodeForFirebaseKey;
import static com.dvrblacktech.coronahelper.MainActivity.phoneNo;
import static com.dvrblacktech.coronahelper.MainActivity.rohitdh;
import static com.dvrblacktech.coronahelper.MainActivity.vidya;

public class TrackerService extends Service {
    String userEmail="";
    private static final String TAG = TrackerService.class.getSimpleName();

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        loginToFirebase();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildNotification() {
        String NOTIFICATION_CHANNEL_ID = "com.dvrblacktech.coronahelper";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);



        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        NotificationCompat.Builder notification = builder.setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_tracker);
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            Log.d(TAG, "firebase auth success");

            requestLocationUpdates();
        } else {
            // No user is signed in
            Log.d(TAG, "firebase auth failed");

        }

    }

    private void requestLocationUpdates() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MainActivity.vidya = FirebaseInstanceId.getInstance().getToken();
        MainActivity.vidya= encodeForFirebaseKey(MainActivity.vidya);
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
        MainActivity.mailplusphone = rohitdh + phoneNo + vidya;
            userEmail = user.getEmail();
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(500);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);



        final String path = getString(R.string.firebase_path) + "/" + MainActivity.mailplusphone;
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        ref.setValue(location);
                    }
                }
            }, null);
        }
    }

}