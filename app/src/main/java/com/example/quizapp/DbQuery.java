package com.example.quizapp;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {

    public static FirebaseFirestore firestore;
    public static List<CategoryModel> listCategories = new ArrayList<>();

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

    public static void loadCategories(CompleteListener listener) {
        listCategories.clear();

        firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> documentSnapshotMap = new ArrayMap<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            documentSnapshotMap.put(documentSnapshot.getId(), documentSnapshot);
                        }

                        QueryDocumentSnapshot categoriesDocument = documentSnapshotMap.get("CATEGORIES");

                        long categoriesCount = categoriesDocument.getLong("COUNT");

                        for(int i=1; i <= categoriesCount; i++) {
                            String categoryID = categoriesDocument.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot categoryDocument = documentSnapshotMap.get(categoryID);

                            int noOfTests = categoryDocument.getLong("NO_OF_TESTS").intValue();
                            String categoryName = categoryDocument.getString("NAME");

                            listCategories.add(new CategoryModel(
                                    categoryID, categoryName, noOfTests
                            ));
                        }

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
