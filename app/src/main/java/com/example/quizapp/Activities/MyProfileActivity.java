package com.example.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.R;
import com.google.android.material.navigation.NavigationView;

public class MyProfileActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPhone;
    private Button btnSave;
    private TextView imgText, dialogText, drawerProfileName;
    private Toolbar toolbar;
    private Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.phoneNumber);
        btnSave = findViewById(R.id.btnSave);
        imgText = findViewById(R.id.imgText);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_btn_back);

        Toast.makeText(this, "HEY7", Toast.LENGTH_SHORT).show();

        progressBar = new Dialog(MyProfileActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");

        progressBar.show();

        setEditing(false);
        downloadData();

        setClickers();

        setEditing(true);

        progressBar.dismiss();
    }

    private void setClickers() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    saveData();

                    progressBar.dismiss();
                }

            }
        });
    }

    private void saveData() {
        dialogText.setText("Updating");
        progressBar.show();

        DbQuery.saveProfileData(userName.getText().toString().trim(), userPhone.getText().toString().trim(), new CompleteListener() {
            @Override
            public void OnSuccess() {
                Toast.makeText(MyProfileActivity.this, "Profile successfully updated", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateData() {
        boolean status = true;

        if (userName.getText().toString().trim().isEmpty()) {
            status = false;
            userName.setError("Name cannot be empty");
        } else if (userPhone.getText().toString().trim().isEmpty()) {
            status = false;
            userName.setError("Name cannot be empty");
        } else if (!TextUtils.isDigitsOnly(userPhone.getText().toString().trim())) {
            status = false;
            userName.setError("Enter valid phone number");
        }

        return status;
    }

    private void setEditing(boolean status) {
        userName.setEnabled(status);
        userEmail.setEnabled(status);
        userPhone.setEnabled(status);
        btnSave.setEnabled(status);
    }


    private void downloadData() {

        userName.setText(DbQuery.userProfile.getName());
        userEmail.setText(DbQuery.userProfile.getEmail());

        if (DbQuery.userProfile.getPhone() != null) {
            userPhone.setText(DbQuery.userProfile.getPhone());
        }

        String profileName = DbQuery.userProfile.getName();

        imgText.setText(profileName.toUpperCase().substring(0, 1));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            MyProfileActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
