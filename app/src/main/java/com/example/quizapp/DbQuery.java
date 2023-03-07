package com.example.quizapp;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {

    public static final String TAG = "QueryDB";

    public static FirebaseFirestore firestore;
    public static List<CategoryModel> listCategories = new ArrayList<>();
    public static int selectedCategoryIndex = 0;
    public static int selectedTestIndex = 0;
    public static List<TestModel> testModelList = new ArrayList<>();
    public static ProfileModel userProfile = new ProfileModel("NAME", null);
    public static List<QuestionModel> questionModelList = new ArrayList<>();

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

    private static void loadCategories(CompleteListener listener) {
        listCategories.clear();

        firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> documentSnapshotMap = new ArrayMap<>();

                        Log.i(TAG, "BEGIN LOADING CATEGORIES");

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            documentSnapshotMap.put(documentSnapshot.getId(), documentSnapshot);
                            Log.i(TAG, String.valueOf(documentSnapshot.getId()));
                        }

                        Log.i(TAG, String.valueOf(documentSnapshotMap.size()));

                        QueryDocumentSnapshot categoriesDocument = documentSnapshotMap.get("CATEGORIES");

                        long categoriesCount = categoriesDocument.getLong("COUNT");

                        for(int i=1; i <= categoriesCount; i++) {
                            String categoryID = categoriesDocument.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot categoryDocument = documentSnapshotMap.get(categoryID);

                            Log.i(TAG, categoryDocument.getId());
                            Log.i(TAG, String.valueOf(categoryDocument.getLong("NO_OF_TESTS").intValue()));
                            int noOfTests = categoryDocument.getLong("NO_OF_TESTS").intValue();
                            String categoryName = categoryDocument.getString("NAME");

                            Log.i(TAG, categoryName);

                            listCategories.add(new CategoryModel(
                                    categoryID, categoryName, noOfTests
                            ));

                            Log.i(TAG, "SUCCESSULLY ADDED");

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

    public static void loadTestData(CompleteListener listener) {
        testModelList.clear();
        Log.i(TAG, "BEGIN");

        firestore.collection("QUIZ").document(listCategories.get(selectedCategoryIndex).getDocID())
                .collection("TESTS_LIST").document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i(TAG, "BEGIN2");
                        int noOfTests = listCategories.get(selectedCategoryIndex).getNoOfTests();

                        for(int i=1; i <= noOfTests; i++) {
                            Log.i(TAG, "BEGIN3");
                            Log.i(TAG, String.valueOf(selectedCategoryIndex) + " " + String.valueOf(i));
                            Log.i(TAG, documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"));

                            testModelList.add(new TestModel(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue()
                            ));
                            Log.i(TAG, "BEGIN4");
                        }

                        listener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.OnFailure();
                        Log.i(TAG, e.getMessage());
                    }
                });
    }

    public static void getUserData(CompleteListener listener) {
        firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userProfile.setName(documentSnapshot.getString("NAME"));
                        userProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));

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

    public static void loadData(CompleteListener listener) {
        loadCategories(new CompleteListener() {
            @Override
            public void OnSuccess() {
                getUserData(listener);
            }

            @Override
            public void OnFailure() {
                listener.OnFailure();
            }
        });
    }

    public static void loadQuestions(CompleteListener listener) {
        questionModelList.clear();

        firestore.collection("QUESTIONS")
                .whereEqualTo("CATEGORY", listCategories.get(selectedCategoryIndex).getDocID())
                .whereEqualTo("TEST", testModelList.get(selectedTestIndex).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i(TAG, "BEGIN");

                        for (DocumentSnapshot document: queryDocumentSnapshots) {
                            Log.i(TAG, document.getString("QUESTION"));

                            questionModelList.add(
                                    new QuestionModel(
                                            document.getString("QUESTION"),
                                            document.getString("A"),
                                            document.getString("B"),
                                            document.getString("C"),
                                            document.getString("D"),
                                            document.getLong("ANSWER").intValue(),
                                            -1
                                )
                            );
                        }
                        Log.i(TAG, "OKEY");

                        listener.OnSuccess();

                        Log.i(TAG, "OKEY2");

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
