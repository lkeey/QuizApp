package com.example.quizapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private GridView categoryView;
    public static List<CategoryModel> categoryList = new ArrayList<>();

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        categoryView = view.findViewById(R.id.gridCategory);

        loadCategories();

        CategoryAdapter adapter = new CategoryAdapter(categoryList, getActivity());
        categoryView.setAdapter(adapter);

        return view;
    }

    private void loadCategories() {
        categoryList.clear();
        categoryList.add(new CategoryModel("1", "GK", 20));
        categoryList.add(new CategoryModel("1", "GK", 20));
        categoryList.add(new CategoryModel("3", "GKya", 200));

    }
}