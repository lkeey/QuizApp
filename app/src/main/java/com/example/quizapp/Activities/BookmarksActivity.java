package com.example.quizapp.Activities;

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

import com.example.quizapp.Adapters.AnswersAdapter;
import com.example.quizapp.Adapters.BookmarkAdapter;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.R;

public class BookmarksActivity extends AppCompatActivity {

    private RecyclerView bookmarkRecyclerView;
    private Toolbar toolbar;
    private Dialog progressBar;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        toolbar = findViewById(R.id.toolbar);

        bookmarkRecyclerView = findViewById(R.id.bookmarksRecyclerView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_btn_back);

        progressBar = new Dialog(BookmarksActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");

        progressBar.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        bookmarkRecyclerView.setLayoutManager(layoutManager);

        DbQuery.loadBookmarks(new CompleteListener() {
            @Override
            public void OnSuccess() {
                BookmarkAdapter adapter = new BookmarkAdapter(DbQuery.bookmarkList);
                bookmarkRecyclerView.setAdapter(adapter);

                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                progressBar.dismiss();
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            BookmarksActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}