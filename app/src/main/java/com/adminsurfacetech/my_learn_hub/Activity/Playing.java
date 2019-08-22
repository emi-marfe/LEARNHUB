package com.adminsurfacetech.my_learn_hub.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adminsurfacetech.my_learn_hub.Common.Common;
import com.adminsurfacetech.my_learn_hub.R;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {
    final static long INTERVAL = 2000; //1 sec
    final static long TIMEOUT = 10000;// 5 sec
    int progressValue = 0;

    CountDownTimer mcountDown;

    int index=0,score=0,thisQuestion=0,totalQuestion,correctAnswer;


    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD;
    TextView txtscore, txtQuestionNum,question_text, rightanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        // views
        txtscore = (TextView)findViewById(R.id.txtScore);
        txtQuestionNum = (TextView)findViewById(R.id.txtTotalQuestion);
        question_text = (TextView)findViewById(R.id.question_text);
        rightanswer = (TextView)findViewById(R.id.correctanswer);
        question_image = (ImageView) findViewById(R.id.question_image);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        mcountDown.cancel();
        if(index < totalQuestion)// still have questions in the list
        {
            Button clickedButton = (Button)view;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
            {
                //choose correct answer
                score+=5;
                correctAnswer++;
                showQuestion(++index);// next question
            }
            else{
                // choose wrong answer i'll still have to continue showing the question

                //Intent intent = new Intent(this,Done.class);
                //                Bundle datasend = new Bundle();
                //                datasend.putInt("SCORE", score);
                //                datasend.putInt("TOTAL", totalQuestion);
                //                datasend.putInt("CORRECT", correctAnswer);
                //                intent.putExtras(datasend);
                //                startActivity(intent);
                //
                //                finish();
                rightanswer.setVisibility(View.VISIBLE);
                rightanswer.setText(Common.questionList.get(index).getCorrectAnswer());
                score+=0;
                correctAnswer++;
                showQuestion(++index);
                rightanswer.setVisibility(View.GONE);

            }
            txtscore.setText(String.format("%d", score ));
        }

    }

    private void showQuestion(int index) {

        if (index < totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;

            if (Common.questionList.get(index).getIsImageQuestion().equals("true"))
            {
                // if its image
                Picasso.get()
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else{
                question_text.setText(Common.questionList.get(index).getQuestion());
                //if the question is text image will be visible
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }
            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mcountDown.start();// start timmer;
        }
        else
        {
            // if its final question
            Intent intent = new Intent(this,Done.class);
            Bundle datasend = new Bundle();
            datasend.putInt("SCORE", score);
            datasend.putInt("TOTAL", totalQuestion);
            datasend.putInt("CORRECT", correctAnswer);
            intent.putExtras(datasend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        mcountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;

            }

            @Override
            public void onFinish() {
                mcountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}
