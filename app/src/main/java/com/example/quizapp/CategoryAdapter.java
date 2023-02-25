package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModel> categoryModelList;
    private Context context;
    private static final String TAG = "AdapterCategory";

    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryModelList.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        View myView;

        if (view == null) {
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        } else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, TestActivity.class);
                    intent.putExtra("categoryIndex", position);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }
        });

        TextView categoryName = myView.findViewById(R.id.categoryName);
        TextView noOfTests = myView.findViewById(R.id.noOfTests);

        categoryName.setText(categoryModelList.get(position).getName());
        noOfTests.setText(String.valueOf(categoryModelList.get(position).getNoOfTests()));

        return myView;
    }
}
