package com.tcal.chamaelas.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcal.chamaelas.R;
import com.tcal.chamaelas.home.category.ProfessionalActivity;
import com.tcal.chamaelas.util.Constants;

public class HomeFragment extends Fragment {

    private HomeViewModel mHomeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        initViews(root);

        return root;
    }

    /**
     * Initializes the screen views
     */
    private void initViews(View root) {
        CategoryAdapter adapter = new CategoryAdapter(categoryType -> {
            Intent intent = new Intent(getContext(), ProfessionalActivity.class);
            intent.putExtra(Constants.CATEGORY_TYPE, categoryType);
            startActivity(intent);
        });

        RecyclerView recyclerView = root.findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }
}