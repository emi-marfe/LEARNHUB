package com.adminsurfacetech.my_learn_hub.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adminsurfacetech.my_learn_hub.Common.Common;
import com.adminsurfacetech.my_learn_hub.Models.QuestionScore;
import com.adminsurfacetech.my_learn_hub.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion, nameid;
    ProgressBar progressBar;
    ImageView profile;

    FirebaseDatabase database;
    DatabaseReference question_score;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        txtResultScore = (TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);
        profile = (ImageView)findViewById(R.id.profileimg);
        nameid = (TextView) findViewById(R.id.nameinput);
        progressBar = (ProgressBar)findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button)findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();
            }
        });
        nameid.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profile);


        //  get data from the database and set views for all

        Bundle extra = getIntent().getExtras();
        if (extra != null ){
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d",score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d", correctAnswer, totalQuestion));


            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

//            String uid = firebaseUser.getUid();
 //           String uname = firebaseUser.getDisplayName();
            //upload point to database
            question_score.child(String.format("%s_%s", currentUser.getDisplayName(),
                    Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", currentUser.getDisplayName(),
                            Common.categoryId),
                            currentUser.getDisplayName(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName,
                            currentUser.getPhotoUrl().toString()));
        }
    }
}
