package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<TestModel> testModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        int categoryIndex = getIntent().getIntExtra("categoryIndex", 0);

        recyclerView = findViewById(R.id.testRecyclerView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(CategoryFragment.categoryList.get(categoryIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        loadTestData();

        TestAdapter adapter = new TestAdapter(testModelList);
        recyclerView.setAdapter(adapter);

    }

    private void loadTestData() {
        testModelList = new ArrayList<>();
        testModelList.add(new TestModel("1", 50, 20));
        testModelList.add(new TestModel("2", 40, 15));
        testModelList.add(new TestModel("2", 100, 40));
        testModelList.add(new TestModel("4", 12, 34));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            TestActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}