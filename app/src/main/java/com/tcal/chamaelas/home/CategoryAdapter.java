package com.tcal.chamaelas.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcal.chamaelas.R;
import com.tcal.chamaelas.util.AppContextUtil;
import com.tcal.chamaelas.util.CategoryEnum;
import com.tcal.chamaelas.util.OnItemClickListener;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private OnItemClickListener mItemClickListener;

    CategoryAdapter(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        CategoryEnum categoryType = CategoryEnum.values()[position];

        holder.mCard.setCardBackgroundColor(AppContextUtil.getContext()
                .getColor(categoryType.getColorId()));
        holder.mTitle.setText(categoryType.getStringId());
        holder.mImage.setImageResource(categoryType.getImageId());

        holder.bind(categoryType, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return CategoryEnum.values().length;
    }

    static class CategoryHolder extends RecyclerView.ViewHolder {

        CardView mCard;
        TextView mTitle;
        ImageView mImage;

        CategoryHolder(@NonNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.category_card);
            mTitle = itemView.findViewById(R.id.category_title);
            mImage = itemView.findViewById(R.id.category_image);
        }

        /**
         * Binds the item with its click listener
         */
        void bind(final CategoryEnum item, final OnItemClickListener listener) {
            itemView.setOnClickListener(view -> {
                listener.onItemClicked(item);
            });
        }
    }
}
