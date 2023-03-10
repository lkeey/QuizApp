package com.example.quizapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizapp.Models.CategoryModel;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.R;
import com.example.quizapp.Activities.TestActivity;

import java.util.List;

public class CategoryAdapterOld extends BaseAdapter {

    private List<CategoryModel> categoryModelList;
    private Context context;
    private static final String TAG = "AdapterCategoryOLD";

    public CategoryAdapterOld(List<CategoryModel> categoryModelList, Context context) {
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
        View myView = null;

        if (view == null) {
            Log.i(TAG, "BEGIN");

//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);

            //Log.i(TAG, view.toString());
            Log.i(TAG, parent.toString());
            Log.i(TAG, parent.getContext().toString());
            Log.i(TAG, LayoutInflater.from(parent.getContext()).toString());
            Log.i(TAG, String.valueOf(R.layout.category_item_layout));

            try {
                myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        } else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
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

        TextView categoryName = myView.findViewById(R.id.categoryName);
        TextView noOfTests = myView.findViewById(R.id.noOfTests);

        categoryName.setText(categoryModelList.get(position).getName());
        noOfTests.setText(String.valueOf(categoryModelList.get(position).getNoOfTests()));

        return myView;
    }
}
