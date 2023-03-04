package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModel> listQuestions;

    public QuestionsAdapter(List<QuestionModel> listQuestions) {
        this.listQuestions = listQuestions;
    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return listQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView question;
        private Button btnA, btnB, btnC, btnD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            btnA = itemView.findViewById(R.id.optionA);
            btnB = itemView.findViewById(R.id.optionB);
            btnC = itemView.findViewById(R.id.optionC);
            btnD = itemView.findViewById(R.id.optionD);

        }

        private void setData(final int position) {
            question.setText(listQuestions.get(position).getQuestion());

            btnA.setText(listQuestions.get(position).getOptionA());
            btnB.setText(listQuestions.get(position).getOptionB());
            btnC.setText(listQuestions.get(position).getOptionC());
            btnD.setText(listQuestions.get(position).getOptionD());
        }
    }
}
