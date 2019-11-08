package com.tcal.chamaelas.home.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tcal.chamaelas.R;
import com.tcal.chamaelas.model.Professional;
import com.tcal.chamaelas.util.CategoryEnum;
import com.tcal.chamaelas.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalActivity extends AppCompatActivity {

    private CategoryEnum mCategoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);

        Intent intent = getIntent();
        mCategoryType = (CategoryEnum) intent.getSerializableExtra(Constants.CATEGORY_TYPE);

        setToolbar();

        ProfessionalAdapter professionalAdapter = new ProfessionalAdapter(getData());

        RecyclerView recyclerView = findViewById(R.id.professional_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(professionalAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializes the screen toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mCategoryType.getStringId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Fetches professionals data from the database
     */
    private List<Professional> getData() {
        // TODO: get real values
        List<Professional> professionalList = new ArrayList<>();
        professionalList.add(new Professional("Carolina", "Eletricista", 4.5f));
        professionalList.add(new Professional("Manoela", "Encanadora", 2.5f));
        professionalList.add(new Professional("Gabriela", "Pintora", 1f));
        professionalList.add(new Professional("Bianca", "Mestre de obra", 5f));
        professionalList.add(new Professional("Thaís", "Pedreira", 3.7f));
        professionalList.add(new Professional("Beatriz", "Pintora", 4.6f));

        return professionalList;
    }
}
