package com.tcal.chamaelas.util;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * The generic input dialog callback
 */
public interface InputDialogCallback {

    /**
     * Called when the positive button is clicked.
     *
     * @param dialog the dialog
     * @param dialogInputLayout the text input layout
     * @param dialogInput the text input field
     */
    void onClick(AlertDialog dialog, TextInputLayout dialogInputLayout, TextInputEditText dialogInput);
}
