package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.Notification.NotificationAlarmActivity;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.appName);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appName.setTypeface(typeface);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.my_anim);
        appName.setAnimation(animation);

        mAuth = FirebaseAuth.getInstance();

        // Access a Cloud Firestore instance from your Activity

        DbQuery.firestore = FirebaseFirestore.getInstance();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (mAuth.getCurrentUser() != null) {

                DbQuery.loadData(new CompleteListener() {
                    @Override
                    public void OnSuccess() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                        startActivity(intent);
                        SplashActivity.this.finish();
                    }

                    @Override
                    public void OnFailure() {
                        Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }

        }).start();
    }
}