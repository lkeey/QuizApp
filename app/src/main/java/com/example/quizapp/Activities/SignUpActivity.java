package com.example.quizapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPassword, userConfirmedPassword;
    private Button btnSignUp;
    private TextView lblHaveAcc, dialogText;
    private ImageView imageBack;
    private FirebaseAuth mAuth;
    private String nameString, emailString, passwordString, passwordConfirmedString;
    private Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.editTextName);
        userEmail = findViewById(R.id.editTextEmail);
        userPassword = findViewById(R.id.editTextPassword);
        userConfirmedPassword = findViewById(R.id.editTextConfirmedPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        lblHaveAcc = findViewById(R.id.labelHaveAccount);
        imageBack = findViewById(R.id.imageBack);

        progressBar = new Dialog(SignUpActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Registering");

        mAuth = FirebaseAuth.getInstance();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    signUpUser();
                }
            }
        });

        lblHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateData() {
        boolean result = true;

        nameString = userName.getText().toString().trim();
        emailString = userEmail.getText().toString().trim();
        passwordString = userPassword.getText().toString().trim();
        passwordConfirmedString = userConfirmedPassword.getText().toString().trim();

        if(nameString.isEmpty()) {
            userName.setError("Enter Name");
            result = false;
        } else if (emailString.isEmpty()) {
            userEmail.setError("Enter Email");
            result = false;
        } else if (passwordString.isEmpty()) {
            userPassword.setError("Enter Password");
            result = false;
        } else if (!passwordConfirmedString.equals(passwordString)) {
            userConfirmedPassword.setError("Passwords don't matches");
            result = false;
        }

        return result;
    }

    private void signUpUser() {

        progressBar.show();

        mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(SignUpActivity.this, "Sign Up Was Successfully", Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(emailString, nameString, new CompleteListener(){
                                @Override
                                public void OnSuccess() {

                                    DbQuery.loadData(new CompleteListener() {
                                        @Override
                                        public void OnSuccess() {
                                            progressBar.dismiss();

                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void OnFailure() {
                                            Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            progressBar.dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void OnFailure() {
                                    Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    progressBar.dismiss();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.dismiss();
                        }
                    }
                });
    }
}