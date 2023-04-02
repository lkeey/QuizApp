package com.example.quizapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.QuestionModel;
import com.example.quizapp.R;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<QuestionModel> bookmarksList;

    public BookmarkAdapter(List<QuestionModel> bookmarksList) {
        this.bookmarksList = bookmarksList;
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout, parent, false);
        return new BookmarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {
        QuestionModel model = bookmarksList.get(position);

        String question = model.getQuestion();
        String optionA = model.getOptionA();
        String optionB = model.getOptionB();
        String optionC = model.getOptionC();
        String optionD = model.getOptionD();

        int selectedAnswer = model.getSelectedAnswer();
        int correctAnswer = model.getCorrectAnswer();

        holder.setData(position, question, optionA, optionB, optionC, optionD, correctAnswer);

    }

    @Override
    public int getItemCount() {
        return bookmarksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView questionNo, questionTxt, optionA, optionB, optionC, optionD, resultTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionNo = itemView.findViewById(R.id.questionNo);
            questionTxt = itemView.findViewById(R.id.questionTxt);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            resultTxt = itemView.findViewById(R.id.resultTxt);

        }

        private void setData(int position, String questionText, String optionAText, String optionBText, String optionCText, String optionDText, int correctAnswer) {
            questionNo.setText("Question No. " + (position + 1));
            questionTxt.setText(questionText);

            optionA.setText("A. " + optionAText);
            optionB.setText("B. " + optionBText);
            optionC.setText("C. " + optionCText);
            optionD.setText("D. " + optionDText);

            if (correctAnswer == 1) {
                resultTxt.setText("Answer: " + optionAText);
            } else if (correctAnswer == 2) {
                resultTxt.setText("Answer: " + optionBText);
            } else if (correctAnswer == 3) {
                resultTxt.setText("Answer: " + optionCText);
            } else {
                resultTxt.setText("Answer: " + optionDText);
            }

            resultTxt.setTextColor(itemView.getContext().getResources().getColor(R.color.purple_200));
            setOptionColor(correctAnswer, R.color.green);
        }

        private void setOptionColor(int selectedAnswer, int color) {
            switch (selectedAnswer) {
                case 1:
                    optionA.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 2:
                    optionB.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 3:
                    optionC.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 4:
                    optionD.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;

            }
        }

    }
}
