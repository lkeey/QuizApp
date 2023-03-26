package com.example.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Adapters.TestAdapter;
import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.R;

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

        //
//        ActionBar actionBar = getSupportActionBar(); // As you said you are using support library
//        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.custom_actionbar_layout, null);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(v);
        //

        recyclerView = findViewById(R.id.testRecyclerView);

        progressBar = new Dialog(TestActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(DbQuery.listCategories.get(DbQuery.selectedCategoryIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_btn_back);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

//        loadTestData();

        progressBar.show();

        DbQuery.loadTestData(new CompleteListener() {
            @Override
            public void OnSuccess() {
                DbQuery.loadMyScores(new CompleteListener() {
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