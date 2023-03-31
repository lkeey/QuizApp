package com.example.quizapp.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Activities.MainActivity;
import com.example.quizapp.Activities.TestActivity;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.Interfaces.CompleteListener;
import com.example.quizapp.R;
import com.example.quizapp.Adapters.RankAdapter;

public class LeaderBordFragment extends Fragment {

    private TextView totalUsers, userImgTxt, userScore, userRank, dialogText;
    private RecyclerView recyclerUser;
    private RankAdapter adapter;
    private Dialog progressBar;

    public LeaderBordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_bord, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBord");

        init(view);

        progressBar = new Dialog(getContext());
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading...");

        progressBar.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerUser.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DbQuery.usersList);
        recyclerUser.setAdapter(adapter);

        DbQuery.getTopUsers(new CompleteListener() {
            @Override
            public void OnSuccess() {
                adapter.notifyDataSetChanged();

                if (DbQuery.myPerformance.getScore() != 0) {
                    if (! DbQuery.isUserOnTopList) {
                        calculateRank();
                    }

                    userScore.setText("Score: " + DbQuery.myPerformance.getScore());
                    userRank.setText("Rank: " + DbQuery.myPerformance.getRank());

                }

                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                progressBar.dismiss();
                Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        totalUsers.setText("Total Users: " + DbQuery.countUsers);

        return view;
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

    private void init(View view) {
        totalUsers = view.findViewById(R.id.totalUsers);
        userImgTxt = view.findViewById(R.id.userImgTxt);
        userScore = view.findViewById(R.id.userScore);
        userRank = view.findViewById(R.id.userRank);
        recyclerUser = view.findViewById(R.id.recyclerUser);

    }
}