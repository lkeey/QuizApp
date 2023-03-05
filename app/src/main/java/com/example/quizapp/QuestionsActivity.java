package com.example.quizapp;

import static com.example.quizapp.R.color.option_colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView viewQuestions;
    private TextView questionName, amountTime, categoryName;
    private Button btnSubmit, btnClear, btnMark;
    private ImageButton previousQuestion, nextQuestion;
    private ImageView questionList;
    private int questionID;
    private QuestionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();

        adapter = new QuestionsAdapter(DbQuery.questionModelList, getApplicationContext());
        viewQuestions.setAdapter(adapter);

        LinearLayoutManager manager =new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        viewQuestions.setLayoutManager(manager);

        setSnapHelper();

        setClickListener();

        startTimer();

        //amountTime.setTextColor(getResources().getColorStateList(option_colors));
    }

    private void startTimer() {
        long totalTime = DbQuery.testModelList.get(DbQuery.selectedTestIndex).getTime()*60*1_000;
        CountDownTimer timer = new CountDownTimer(totalTime, 1_000) {
            @Override
            public void onTick(long remainingTime) {
                String time = String.format(
                        "%02d : %02d minutes",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );

                amountTime.setText(time);
            }

            @Override
            public void onFinish() {

            }
        };

        timer.start();
    }

    private void setClickListener() {
        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionID > 0) {
                    viewQuestions.smoothScrollToPosition(questionID - 1);
                }
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionID < DbQuery.questionModelList.size() - 1) {
                    viewQuestions.smoothScrollToPosition(questionID + 1);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbQuery.questionModelList.get(questionID).setSelectedAnswer(-1);

                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(viewQuestions);

        viewQuestions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                questionID = recyclerView.getLayoutManager().getPosition(view);
                questionName.setText(String.valueOf(questionID + 1) + "/" + String.valueOf(DbQuery.questionModelList.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void init() {
        viewQuestions = findViewById(R.id.viewQuestions);
        questionName = findViewById(R.id.questionID);
        amountTime = findViewById(R.id.amountTime);
        categoryName = findViewById(R.id.categoryName);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnClear = findViewById(R.id.btnClear);
        btnMark = findViewById(R.id.btnMark);
        previousQuestion = findViewById(R.id.previousQuestion);
        nextQuestion = findViewById(R.id.nextQuestion);
        questionList = findViewById(R.id.questionsList);

        questionID = 0;

        questionName.setText("1 / " + String.valueOf(DbQuery.questionModelList.size()));
        categoryName.setText(DbQuery.listCategories.get(DbQuery.selectedCategoryIndex).getName());
    }
}
