package com.example.quizapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.RankModel;
import com.example.quizapp.R;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<RankModel> userList;

    public RankAdapter(List<RankModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();

        holder.setData(name, score, rank);
    }

    @Override
    public int getItemCount() {
        if (userList.size() > 10) {
            return 10;
        }

        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userImgTxt, userScore, userRank, userName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImgTxt = itemView.findViewById(R.id.userImgTxt);
            userScore = itemView.findViewById(R.id.userScore);
            userRank = itemView.findViewById(R.id.userRank);
            userName = itemView.findViewById(R.id.userName);
        }

        private void setData(String name, int score, int rank) {
            userName.setText(name);
            userScore.setText("Score: " + score);
            userRank.setText("Rank: " + rank);
            userImgTxt.setText(name.toUpperCase().substring(0, 1));
        }
    }
}
