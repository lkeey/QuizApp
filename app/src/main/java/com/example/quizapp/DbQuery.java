package com.example.quizapp;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore firestore;

    public static void createUserData(String email, String name, CompleteListener listener) {
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        WriteBatch batch = firestore.batch();

        batch.set(userDoc, userData);

        DocumentReference docReference = firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(docReference, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.OnFailure();
                    }
                });
    }
}
