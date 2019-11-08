package com.tcal.chamaelas.home.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tcal.chamaelas.R;
import com.tcal.chamaelas.model.Professional;

import java.util.List;

public class ProfessionalAdapter extends RecyclerView.Adapter<ProfessionalAdapter.ProfessionalHolder> {

    private List<Professional> mProfessionalList;

    ProfessionalAdapter(List<Professional> professionalList) {
        mProfessionalList = professionalList;
    }

    @NonNull
    @Override
    public ProfessionalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfessionalHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_professionals, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessionalHolder holder, int position) {
        Professional professional = mProfessionalList.get(position);

        // TODO: use real image
        holder.mImage.setImageResource(R.drawable.mock_avatar);
        holder.mName.setText(professional.getName());
        holder.mSpecialty.setText(professional.getSpecialty());
        holder.mRating.setRating(professional.getRating());
    }

    @Override
    public int getItemCount() {
        return mProfessionalList != null ? mProfessionalList.size() : 0;
    }

    static class ProfessionalHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mName;
        TextView mSpecialty;
        RatingBar mRating;
        ToggleButton mFavorite;

        ProfessionalHolder(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.professional_image);
            mName = itemView.findViewById(R.id.professional_name);
            mSpecialty = itemView.findViewById(R.id.professional_specialty);
            mRating = itemView.findViewById(R.id.professional_rating);
            mFavorite = itemView.findViewById(R.id.professional_favorite);
        }
    }
}
