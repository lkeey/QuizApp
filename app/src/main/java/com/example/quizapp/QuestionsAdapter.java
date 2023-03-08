package com.example.quizapp;

import static com.example.quizapp.DbQuery.ANSWERED;
import static com.example.quizapp.R.color.option_colors;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private Context context;

    public QuestionsAdapter(List<QuestionModel> listQuestions, Context context) {
        this.listQuestions = listQuestions;
        this.context = context;
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
        private Button btnA, btnB, btnC, btnD, previousSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            btnA = itemView.findViewById(R.id.optionA);
            btnB = itemView.findViewById(R.id.optionB);
            btnC = itemView.findViewById(R.id.optionC);
            btnD = itemView.findViewById(R.id.optionD);

            previousSelected = null;

        }

        private void setData(final int position) {
            question.setText(listQuestions.get(position).getQuestion());

            btnA.setText(listQuestions.get(position).getOptionA());
            btnB.setText(listQuestions.get(position).getOptionB());
            btnC.setText(listQuestions.get(position).getOptionC());
            btnD.setText(listQuestions.get(position).getOptionD());

            setOption(btnA, 1, position);
            setOption(btnB, 2, position);
            setOption(btnC, 3, position);
            setOption(btnD, 4, position);

            btnA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(btnA, 1, position);

                }
            });

            btnB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(btnB, 2, position);

                }
            });

            btnC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(btnC, 3, position);

                }
            });

            btnD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(btnD, 4, position);

                }
            });
        }

        private void setOption(Button btn, int optionNum, int questionID) {
            if (DbQuery.questionModelList.get(questionID).getSelectedAnswer() == optionNum) {
                btn.setBackgroundResource(R.drawable.selected_btn);
                btn.setTextColor(context.getResources().getColor(R.color.white));

            } else {
                btn.setBackgroundResource(R.drawable.unselected_btn);
                btn.setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary));

            }
        }


        private void selectOption(Button btn, int optionNum, int questionID) {
            if (previousSelected == null) {
                // not chosen
                btn.setBackgroundResource(R.drawable.selected_btn);
                btn.setTextColor(context.getResources().getColor(R.color.white));

                DbQuery.questionModelList.get(questionID).setSelectedAnswer(optionNum);

                changeStatus(questionID, DbQuery.ANSWERED);

                previousSelected = btn;
            } else {

                if (previousSelected.getId() == btn.getId()) {
                    // delete chosen answer

                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary));

                    DbQuery.questionModelList.get(questionID).setSelectedAnswer(-1);

                    changeStatus(questionID, DbQuery.UNANSWERED);

                    previousSelected = null;
                } else {
                    // change chosen option

                    previousSelected.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);

                    previousSelected.setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                    btn.setTextColor(context.getResources().getColor(R.color.white));

                    DbQuery.questionModelList.get(questionID).setSelectedAnswer(optionNum);

                    changeStatus(questionID, DbQuery.ANSWERED);

                    previousSelected = btn;
                }

            }
        }

        private void changeStatus(int questionID, int status) {
            if (DbQuery.questionModelList.get(questionID).getStatus() != DbQuery.REVIEW) {
                DbQuery.questionModelList.get(questionID).setStatus(status);
            }
        }
    }
}
