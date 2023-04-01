package com.example.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizapp.Fragments.CategoryFragment;
import com.example.quizapp.Fragments.LeaderBordFragment;
import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private static final String TAG = "ActivityScore";
    private TextView totalScore, timeTaken, totalQuestions, amountCorrect, amountWrong, amountUnAttempted, dialogText;
    private Button btnCheckLeader, btnReAttempt, btnViewAnswers;
    private long timeLeft;
    private Toolbar toolbar;
    private Dialog progressBar;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_btn_back);

        progressBar = new Dialog(ScoreActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");

        progressBar.show();

        init();

        loadData();

        setBookmarks();

        setClickListeners();
    }

    private void setClickListeners() {
        btnCheckLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(mainFrame.getId(), fragment);
//                transaction.commit();
            }
        });

        btnReAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAttempt();
            }
        });

        btnViewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);
            }
        });

        saveResult();

    }

    private void saveResult() {
        DbQuery.saveResult(finalScore, new CompleteListener() {
            @Override
            public void OnSuccess() {


                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(ScoreActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });
    }

    private void reAttempt() {
        for(int i=0; i < DbQuery.questionModelList.size(); i++) {
            DbQuery.questionModelList.get(i).setSelectedAnswer(-1);
            DbQuery.questionModelList.get(i).setStatus(DbQuery.NOT_VISITED);
        }

        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        ScoreActivity.this.finish();
    }

    private void init() {
        totalScore = findViewById(R.id.totalScore);
        timeTaken = findViewById(R.id.timeTaken);
        totalQuestions = findViewById(R.id.totalQuestions);
        amountCorrect = findViewById(R.id.amountCorrect);
        amountWrong = findViewById(R.id.amountWrong);
        amountUnAttempted = findViewById(R.id.amountUnAttempted);
        btnCheckLeader = findViewById(R.id.btnCheckLeader);
        btnReAttempt = findViewById(R.id.btnReAttempt);
        btnViewAnswers = findViewById(R.id.btnViewAnswers);

    }

    private void loadData() {
        int correctQuestions = 0, wrongQuestions = 0, unAttemptedQuestions = 0;

        for (int i = 0; i < DbQuery.questionModelList.size(); i++) {
            if (DbQuery.questionModelList.get(i).getSelectedAnswer() == -1) {
                // unAnswered
                unAttemptedQuestions ++;
            } else {
                if (DbQuery.questionModelList.get(i).getSelectedAnswer() == DbQuery.questionModelList.get(i).getCorrectAnswer()) {
                    // correct answered
                    correctQuestions ++;
                } else {
                    // wrong answered
                    wrongQuestions ++;
                }
            }
        }

        // set amount questions
        amountCorrect.setText(String.valueOf(correctQuestions));
        amountWrong.setText(String.valueOf(wrongQuestions));
        amountUnAttempted.setText(String.valueOf(unAttemptedQuestions));

        totalQuestions.setText(String.valueOf(DbQuery.questionModelList.size()));

        // set score
        finalScore = correctQuestions * 100 / DbQuery.questionModelList.size();
        totalScore.setText(String.valueOf(finalScore));

        // set time
        timeLeft = getIntent().getLongExtra("TIME_TAKEN", 0);

        String time = String.format(
                "%02d : %02d minutes",
                TimeUnit.MILLISECONDS.toMinutes(timeLeft),
                TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft))
        );

        timeTaken.setText(time);
    }

    private void setBookmarks() {
        for (int i=0; i < DbQuery.questionModelList.size(); i++) {
            QuestionModel questionModel = DbQuery.questionModelList.get(i);

            if (questionModel.isBookmarked()) {
                if (! DbQuery.bookmarkIdList.contains(questionModel.getID())) {
                    DbQuery.bookmarkIdList.add(questionModel.getID());
                    Log.i(TAG, "Added Bookmark - " + questionModel.getQuestion() + " - " + questionModel.getID());
                }
            } else {
                if (DbQuery.bookmarkIdList.contains(questionModel.getID())) {
                    DbQuery.bookmarkIdList.remove(questionModel.getID());
                    Log.i(TAG, "Deleted Bookmark - " + questionModel.getQuestion() + " - " + questionModel.getID());

                }
            }
        }

        DbQuery.userProfile.setBookmarksCount(DbQuery.bookmarkIdList.size());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            ScoreActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}