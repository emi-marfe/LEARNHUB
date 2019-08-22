package com.adminsurfacetech.my_learn_hub.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adminsurfacetech.my_learn_hub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView logggggin , forgotpaa;
    private EditText Usermail;
    private TextInputEditText Userpassword;
    private Button btnlogin;
    private ProgressBar loginprogress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Window w = getWindow();
        //        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //        getSupportActionBar();

        registerAlarm();

        //views for settings

        logggggin = findViewById(R.id.haveaccout);
        forgotpaa = findViewById(R.id.forgot_password);

        logggggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Sign_up = new Intent(getApplicationContext(),Sign_upActivity.class);
                startActivity(Sign_up);
                finish();
            }
        });
        forgotpaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot = new Intent(getApplicationContext(),ForgotActivity.class);
                startActivity(forgot);
                finish();
            }
        });

        // input to login
        FirebaseApp.initializeApp(this);
        Usermail = findViewById(R.id.login_mail);
        Userpassword = findViewById(R.id.login_password);
        btnlogin = findViewById(R.id.btn_login);
        loginprogress = findViewById(R.id.loginprogress);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this, Home.class);

        loginprogress.setVisibility(View.INVISIBLE);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginprogress.setVisibility(View.VISIBLE);
                btnlogin.setVisibility(View.INVISIBLE);

                final String mail = Usermail.getText().toString();
                final String password = Userpassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty()){
                    showMessage("please fill in all field");
                    btnlogin.setVisibility(View.VISIBLE);
                    loginprogress.setVisibility(View.INVISIBLE);
                }
                else {

                    SignIn(mail,password);
                }
            }
        });
    }

    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,2);//this means the ninth hour
        calendar.set(Calendar.MINUTE, 39);//
        calendar.set(Calendar.SECOND,0);
// i have to change it to alarm activity
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void SignIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    loginprogress.setVisibility(View.INVISIBLE);
                    btnlogin.setVisibility(View.VISIBLE);
                    UpdateUI();
                }

                else {
                    showMessage(task.getException().getMessage());
                    btnlogin.setVisibility(View.VISIBLE);
                    loginprogress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void UpdateUI() {

        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user !=null){
            //user already loginned
            UpdateUI();
        }
    }
}
