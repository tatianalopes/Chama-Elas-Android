package com.tcal.chamaelas.util;

/**
 * A generic callback for dialogs
 */
public interface DialogCallback {
    /**
     * Called when the negative button is clicked
     */
    void onNegativeButtonClick();

    /**
     * Called when the positive button is clicked
     */
    void onPositiveButtonClick();
}
