package com.tcal.chamaelas.util;

/**
 * A generic callback for recycler view items
 */
public interface OnItemClickListener {
    /**
     * Called when a Category item is clicked.
     */
    void onItemClicked(CategoryEnum categoryType);
}
