package com.example.quizapp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Activities.BookmarksActivity;
import com.example.quizapp.Activities.LoginActivity;
import com.example.quizapp.Activities.MainActivity;
import com.example.quizapp.Activities.MyProfileActivity;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    private static final String TAG = "FragmentAccount";
    private LinearLayout layoutLogout, layoutLeader, layoutBookmark, layoutProfile;
    private TextView imgProfileTxt, nameTxt, scoreTxt, rankTxt, dialogText;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private Dialog progressBar;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        init(view);

        progressBar = new Dialog(getContext());
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading...");

        setListeners();

        setUserData();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        return view;
    }

    private void setUserData() {
        String nameUser = DbQuery.userProfile.getName();
        imgProfileTxt.setText(nameUser.toUpperCase().substring(0, 1));
        nameTxt.setText(nameUser);

        scoreTxt.setText(String.valueOf(DbQuery.myPerformance.getScore()));


        if (DbQuery.usersList.size() == 0) {
            progressBar.show();

            DbQuery.getTopUsers(new CompleteListener() {
                @Override
                public void OnSuccess() {
                    if (DbQuery.myPerformance.getScore() != 0) {
                        if (! DbQuery.isUserOnTopList) {
                            calculateRank();
                        }

                        scoreTxt.setText(String.valueOf(DbQuery.myPerformance.getScore()));
                        rankTxt.setText(String.valueOf(DbQuery.myPerformance.getRank()));

                    }

                    progressBar.dismiss();
                }

                @Override
                public void OnFailure() {
                    progressBar.dismiss();
                    Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            scoreTxt.setText(String.valueOf(DbQuery.myPerformance.getScore()));
            rankTxt.setText(String.valueOf(DbQuery.myPerformance.getRank()));

        }

    }

    private void setListeners() {
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mClient = GoogleSignIn.getClient(getActivity(), gso);
                mClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "You have successfully logout", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                            );
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        layoutBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookmarksActivity.class);
                startActivity(intent);
            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        layoutLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_leader);
            }
        });
    }

    private void init(View view) {

        imgProfileTxt = view.findViewById(R.id.profileImgTxt);
        nameTxt = view.findViewById(R.id.txtName);
        rankTxt = view.findViewById(R.id.userRank);
        scoreTxt = view.findViewById(R.id.userScore);
        layoutBookmark = view.findViewById(R.id.layoutBookmark);
        layoutLeader = view.findViewById(R.id.layoutLeaderBord);
        layoutProfile = view.findViewById(R.id.layoutProfile);
        layoutLogout = view.findViewById(R.id.layoutLogout);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    private void calculateRank() {
        int lowTopScore =  DbQuery.usersList.get(DbQuery.usersList.size() - 1).getScore();

        int remainingSlots = DbQuery.countUsers - 20;

        int userSlot = DbQuery.myPerformance.getScore()*remainingSlots / lowTopScore;

        int rank;

        if (lowTopScore != DbQuery.myPerformance.getScore()) {
            rank = DbQuery.countUsers - userSlot;
        } else {
            rank = 21;
        }

        DbQuery.myPerformance.setRank(rank);
    }
}