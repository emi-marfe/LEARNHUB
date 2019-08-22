package com.adminsurfacetech.my_learn_hub.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.adminsurfacetech.my_learn_hub.R;
import com.yasic.library.particletextview.MovingStrategy.RandomMovingStrategy;
import com.yasic.library.particletextview.Object.ParticleTextViewConfig;
import com.yasic.library.particletextview.View.ParticleTextView;

public class Luncherctivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    ParticleTextView particleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncherctivity);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar();
        particleTextView=findViewById(R.id.particleTextView);


        ParticleTextViewConfig config=new ParticleTextViewConfig.Builder()
                .setRowStep(5)
                .setColumnStep(5)
                .setTargetText("Student Hub")
                .setReleasing(0.2)
                .setParticleRadius(2)
                .setMiniDistance(0.1)
                .setTextSize(130)
                .setDelay((long) 4000)
                .setMovingStrategy(new RandomMovingStrategy())
                .instance();
        particleTextView.setConfig(config);
        particleTextView.startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(Luncherctivity.this, IntroActivity.class);
                startActivity(mySuperIntent);
                /* This 'finish()' is for exiting the app when back button pressed
                 *  from Home page which is ActivityHome
                 */
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
