package com.dvrblacktech.coronahelper;

import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;


public class MainActivity extends AppCompatActivity {

    String userEmail="";
    public static String rohitdh;
    public static String vidya;

    public static String phoneNo;
    public static String mailplusphone;


    private AppBarConfiguration mAppBarConfiguration;
    private static final int PERMISSIONS_REQUEST = 1;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
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
            mailplusphone = rohitdh + phoneNo;
            Toast.makeText(MainActivity.this," WELCOME  " + mailplusphone,Toast.LENGTH_LONG).show();
            startService(new Intent(MainActivity.this, TrackerService.class));

        } else {
            // No user is signed in
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_mantra,R.id.nav_support)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null)
        {
            SendUserToLoginActivity();

        }

    }





    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this,activity_login.class);
        startActivity(loginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        finish();
    }

    public static String encodeForFirebaseKey(String s) {
        return s
                .replace("_", "_UNDERSCORE_")
                .replace(".", "_DOT_")
                .replace("$", "_DOLLER_")
                .replace("#", "_HASH_")
                .replace("[", "_LEFTBIGBRACKET_")
                .replace("]", "_RIGHTBIGBRACKET_")
                .replace("/", "_SLASH_")
                .replace("+", "_PLUS_")
                ;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }




}
