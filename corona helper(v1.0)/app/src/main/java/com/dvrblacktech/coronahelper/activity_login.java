package com.dvrblacktech.coronahelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.dvrblacktech.coronahelper.MainActivity.encodeForFirebaseKey;
import static com.dvrblacktech.coronahelper.MainActivity.mailplusphone;
import static com.dvrblacktech.coronahelper.MainActivity.phoneNo;
import static com.dvrblacktech.coronahelper.MainActivity.rohitdh;


public class activity_login extends AppCompatActivity
{

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    private Button LoginButton, PhoneLoginButtton;
    private EditText UserEmail, UserPassword;
    private TextView NeedNewAccountLink, ForgetPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        InitializeFields();

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                SendUserToRegisterActivity();
            }
        });

    LoginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            AllowUserToLogin();
        }
    });

    PhoneLoginButtton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent phoneLoginIntent= new Intent(activity_login.this, PhoneLoginActivity.class);
            startActivity(phoneLoginIntent);
        }
    });

    }

    private void AllowUserToLogin()
    {
        String email= UserEmail.getText().toString();
        String password= UserPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter your email...",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter your password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Signing In To Your Account");
            loadingBar.setMessage("PLease wait......");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {

                                SendUserToTrackActivity();
                                Toast.makeText(activity_login.this,"...Login Successful...",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }

                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(activity_login.this, "Error" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });

        }
    }

    private void stopTrackerService()
    {
        stopService(new Intent(this, TrackerService.class));
    }

    private void InitializeFields() {

        LoginButton = (Button) findViewById(R.id.login_button);
        PhoneLoginButtton = (Button) findViewById(R.id.phone_login_button);
        UserEmail= (EditText) findViewById(R.id.login_email);
        UserPassword= (EditText) findViewById(R.id.login_password);
        NeedNewAccountLink = (TextView) findViewById(R.id.need_new_account_link);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);
        loadingBar= new ProgressDialog(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser != null)
        {
            SendUserToTrackActivity();
        }

    }

    private void SendUserToTrackActivity()
    {
        Intent loginIntent = new Intent(activity_login.this,TrackActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToRegisterActivity()
    {
        Intent RegisterIntent = new Intent(activity_login.this,activity_register.class);
        startActivity(RegisterIntent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }
}
