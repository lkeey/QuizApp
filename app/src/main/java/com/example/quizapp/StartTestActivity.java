package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StartTestActivity extends AppCompatActivity {

    private static final String TAG = "TestStartActivity";
    private TextView categoryName, testNo, amountQuestions, totalScore, amountTime, dialogText;
    private Button btnStart;
    private ImageView btnBack;
    private Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init();

        DbQuery.loadQuestions(new CompleteListener() {
            @Override
            public void OnSuccess() {
                setData();

                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(StartTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });
    }

    private void init() {

        categoryName = findViewById(R.id.nameCategory);
        testNo = findViewById(R.id.nameNO);
        amountQuestions = findViewById(R.id.amountQuestions);
        totalScore = findViewById(R.id.totalScore);
        amountTime = findViewById(R.id.amountTime);
        btnStart = findViewById(R.id.startBtn);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTestActivity.this.finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
                startActivity(intent);
                StartTestActivity.this.finish();
            }
        });

        progressBar = new Dialog(StartTestActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");

        progressBar.show();
    }

    private void setData() {
//        Log.i(TAG, DbQuery.listCategories.get(DbQuery.selectedCategoryIndex).getName());
//        Log.i(TAG, "Test No. " + String.valueOf(DbQuery.selectedTestIndex + 1));
//        Log.i(TAG, String.valueOf(DbQuery.questionModelList.size()));
//        Log.i(TAG,String.valueOf(DbQuery.testModelList.get(DbQuery.selectedTestIndex).getTopScore()));
//        Log.i(TAG, String.valueOf(DbQuery.testModelList.get(DbQuery.selectedTestIndex).getTime()));

        categoryName.setText(DbQuery.listCategories.get(DbQuery.selectedCategoryIndex).getName());
        testNo.setText("Test No. " + String.valueOf(DbQuery.selectedTestIndex + 1));
        amountQuestions.setText(String.valueOf(DbQuery.questionModelList.size()));
        totalScore.setText(String.valueOf(DbQuery.testModelList.get(DbQuery.selectedTestIndex).getTopScore()));
        amountTime.setText(String.valueOf(DbQuery.testModelList.get(DbQuery.selectedTestIndex).getTime()));
    }
}