package com.example.quizapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizapp.Activities.MainActivity;
import com.example.quizapp.Adapters.CategoryAdapter;
import com.example.quizapp.Database.DbQuery;
import com.example.quizapp.R;

public class CategoryFragment extends Fragment {

    private RecyclerView categoryView;
//    public static List<CategoryModel> categoryList = new ArrayList<>();

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        categoryView = view.findViewById(R.id.gridCategory);

        //loadCategories();

        CategoryAdapter adapter = new CategoryAdapter(DbQuery.listCategories, getActivity());
        categoryView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);

        categoryView.setLayoutManager(manager);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Feed");

        return view;
    }

    private void loadCategories() {
//        categoryList.clear();

//        categoryList.add(new CategoryModel("1", "GK", 20));
//        categoryList.add(new CategoryModel("1", "GK", 20));
//        categoryList.add(new CategoryModel("3", "GKya", 200));

    }
}