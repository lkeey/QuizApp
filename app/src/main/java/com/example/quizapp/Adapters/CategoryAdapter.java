package com.example.quizapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Models.CategoryModel;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.R;
import com.example.quizapp.Activities.TestActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;
    private Context context;
    private static final String TAG = "AdapterCategory";

    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        try {
            Log.i(TAG, "BEGIN");

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName, noOfTests;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            noOfTests = itemView.findViewById(R.id.noOfTests);

        }


        public void setData(int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        DbQuery.selectedCategoryIndex = position;
                        Log.i(TAG, String.valueOf(position));
                        Intent intent = new Intent(context, TestActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            });

            categoryName.setText(categoryModelList.get(position).getName());
            noOfTests.setText(String.valueOf(categoryModelList.get(position).getNoOfTests()));

        }
    }
}
