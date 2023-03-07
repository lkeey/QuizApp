package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder> {

    private static final String TAG = "AdapterTest";
    private List<TestModel> testModelList;
    private Context context;

    public TestAdapter(List<TestModel> testModelList, Context context) {
        this.testModelList = testModelList;
        this.context = context;
    }

    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "createView");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout, parent, false);

        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        int progress = testModelList.get(position).getTopScore();
        Log.i(TAG, "setData");
        Log.i(TAG, String.valueOf(position) + " " + String.valueOf(progress));
        holder.setData(position, progress);
        Log.i(TAG, "setDataEnd");

    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder {
        private TextView textNo;
        private TextView textPercent;
        private ProgressBar progressBar;

        public TestHolder(@NonNull View itemView) {
            super(itemView);

            Log.i(TAG, "created");

            textNo = itemView.findViewById(R.id.textNo);
            textPercent = itemView.findViewById(R.id.textPercent);
            progressBar = itemView.findViewById(R.id.progressBar);

        }

        private void setData(int pos, int progress) {
            Log.i(TAG, "setData1");

            textNo.setText("Test Number: " + String.valueOf(pos + 1));
            Log.i(TAG, "setData2");

            textPercent.setText(progress + "%");
            Log.i(TAG, "setData3");

            progressBar.setProgress(progress);
            Log.i(TAG, "setData4");


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DbQuery.selectedTestIndex = pos;
                        Intent intent = new Intent(context, StartTestActivity.class);

                        Log.i(TAG, context.toString());
                        Log.i(TAG, textNo.getText().toString());
                        context.startActivity(intent);

                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            });

        }
    }
}
