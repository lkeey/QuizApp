package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog progressBar;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.testRecyclerView);
        toolbar = findViewById(R.id.toolbar);

        progressBar = new Dialog(TestActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(DbQuery.listCategories.get(DbQuery.selectedCategoryIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

//        loadTestData();

        progressBar.show();

        DbQuery.loadTestData(new CompleteListener() {
            @Override
            public void OnSuccess() {
                adapter = new TestAdapter(DbQuery.testModelList, TestActivity.this);
                recyclerView.setAdapter(adapter);

                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(TestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });
    }

    private void loadTestData() {
//        testModelList = new ArrayList<>();
//        testModelList.add(new TestModel("1", 50, 20));
//        testModelList.add(new TestModel("2", 40, 15));
//        testModelList.add(new TestModel("2", 100, 40));
//        testModelList.add(new TestModel("4", 12, 34));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            TestActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}