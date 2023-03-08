package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView viewQuestions;
    private TextView questionName, amountTime, categoryName;
    private Button btnSubmit, btnClear, btnMark;
    private ImageButton previousQuestion, nextQuestion;
    private ImageView questionList, bookMarkImage;
    private int questionID;
    private QuestionsAdapter adapterQuestions;
    private DrawerLayout drawer;
    private GridView gridQuestions;
    private ImageView imageMark;
    private QuestionsGridAdapter adapterGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list_layout);

        init();

        adapterQuestions = new QuestionsAdapter(DbQuery.questionModelList, getApplicationContext());
        viewQuestions.setAdapter(adapterQuestions);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        viewQuestions.setLayoutManager(manager);

        adapterGrid = new QuestionsGridAdapter(this, DbQuery.questionModelList.size());
        gridQuestions.setAdapter(adapterGrid);

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

                adapterQuestions.notifyDataSetChanged();
            }
        });

        questionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.END)) {
                    adapterGrid.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBookmark();
            }
        });

    }

    private void setBookmark() {
        //Toast.makeText(QuestionsActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();

        if (imageMark.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

            imageMark.setVisibility(View.VISIBLE);

            DbQuery.questionModelList.get(questionID).setStatus(DbQuery.REVIEW);

            bookMarkImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.yellow)));

        } else {
            imageMark.setVisibility(View.GONE);

            if (DbQuery.questionModelList.get(questionID).getSelectedAnswer() != -1) {
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                DbQuery.questionModelList.get(questionID).setStatus(DbQuery.ANSWERED);
            } else {
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();

                DbQuery.questionModelList.get(questionID).setStatus(DbQuery.UNANSWERED);
            }

            bookMarkImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white)));

        }
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

                if (DbQuery.questionModelList.get(questionID).getStatus() == DbQuery.NOT_VISITED) {
                    DbQuery.questionModelList.get(questionID).setStatus(DbQuery.UNANSWERED);
                }

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
        drawer = findViewById(R.id.drawerLayout);
        gridQuestions = findViewById(R.id.gridQuestions);
        imageMark = findViewById(R.id.imageMark);
        bookMarkImage = findViewById(R.id.bookMark);

        questionID = 0;

        questionName.setText("1 / " + String.valueOf(DbQuery.questionModelList.size()));
        categoryName.setText(DbQuery.listCategories.get(DbQuery.selectedCategoryIndex).getName());
    }

    public void goToQuestion(int position) {
        viewQuestions.smoothScrollToPosition(position);

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }
}
