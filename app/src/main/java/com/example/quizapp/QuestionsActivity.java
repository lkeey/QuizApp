package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView viewQuestions;
    private TextView questionId, amountTime, categoryName;
    private Button btnSubmit, btnClear, btnMark;
    private ImageButton previousQuestion, nextQuestion;
    private ImageView questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();

        QuestionsAdapter adapter = new QuestionsAdapter(DbQuery.questionModelList);
        viewQuestions.setAdapter(adapter);
    }

    private void init() {
        viewQuestions = findViewById(R.id.viewQuestions);
        questionId = findViewById(R.id.questionID);
        amountTime = findViewById(R.id.amountTime);
        categoryName = findViewById(R.id.categoryName);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnClear = findViewById(R.id.btnClear);
        btnMark = findViewById(R.id.btnMark);
        previousQuestion = findViewById(R.id.previousQuestion);
        nextQuestion = findViewById(R.id.nextQuestion);
        questionList = findViewById(R.id.questionsList);
    }
}