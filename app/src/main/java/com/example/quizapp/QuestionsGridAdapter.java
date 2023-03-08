package com.example.quizapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class QuestionsGridAdapter extends BaseAdapter {

    private int numOfQuestions;
    private Context context;

    public QuestionsGridAdapter(Context context, int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numOfQuestions;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View myView;

        if (view == null) {
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_grid_item, viewGroup, false);

        } else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof  QuestionsActivity) {
                    ((QuestionsActivity) context).goToQuestion(position);
                }
            }
        });

        TextView questionNumber = myView.findViewById(R.id.questionNumber);
        questionNumber.setText(String.valueOf(position + 1));

        switch (DbQuery.questionModelList.get(position).getStatus()) {

            case DbQuery.NOT_VISITED:
                questionNumber.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.grey)));
                break;

            case DbQuery.UNANSWERED:
                questionNumber.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.red)));
                break;

            case DbQuery.ANSWERED:
                questionNumber.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.green)));
                break;

            case DbQuery.REVIEW:
                questionNumber.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.pink)));
                break;

            default:
                break;
        }

        return myView;
    }
}
