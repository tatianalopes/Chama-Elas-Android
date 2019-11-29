package com.tcal.chamaelas.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tcal.chamaelas.R;

/**
 * This class provides methods for the generation of the needed dialogs
 */
public class DialogUtils {
    /**
     * Creates and displays a generic dialog with a text input field
     *
     * @param context the caller Activity context
     * @param title the resource ID of the title string
     * @param content the content of the edit text
     * @param positiveButtonText the resource ID of the positive button string
     * @param callback the callback to execute the needed action
     */
    public static void createInputDialog(Context context, int title, int content,
                                         int positiveButtonText, InputDialogCallback callback) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(title);

        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        dialogContent.setText(content);

        TextInputLayout dialogInputLayout = dialogView.findViewById(R.id.dialog_input_Layout);
        dialogInputLayout.setVisibility(View.VISIBLE);
        TextInputEditText dialogInput = dialogView.findViewById(R.id.dialog_input);
        dialogInput.setInputType(InputType.TYPE_CLASS_TEXT);

        MaterialButton negativeButton = dialogView.findViewById(R.id.button_negative);
        negativeButton.setText(R.string.button_cancel);
        MaterialButton positiveButton = dialogView.findViewById(R.id.button_positive);
        positiveButton.setText(positiveButtonText);
        positiveButton.setEnabled(false);

        AlertDialog dialog = dialogBuilder.create();
        negativeButton.setOnClickListener(v -> dialog.dismiss());
        positiveButton.setOnClickListener(v -> {
            callback.onClick(dialog, dialogInputLayout, dialogInput);
        });

        dialogInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    positiveButton.setEnabled(false);
                } else {
                    positiveButton.setEnabled(true);
                }
            }
        });

        // when the popup appears the focus should be on the editText
        dialogInput.requestFocus();

        // show the soft keyboard
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    /**
     * Creates and displays a generic dialog to confirm or cancel a dangerous action
     *
     * @param context the caller Activity context
     * @param title the resource ID of the title string
     * @param content the resource ID of the dialog content string
     * @param positiveButtonText the resource ID of the positive button string
     * @param negativeButtonText the resource ID of the negative button string
     * @param callback the callback to execute the needed action
     */
    public static void createTwoButtonsActionDialog(Context context, String title, String content,
                                                    int positiveButtonText, int negativeButtonText,
                                                    DialogCallback callback) {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(title);

        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        dialogContent.setText(content);

        Button negativeButton = dialogView.findViewById(R.id.button_negative);
        if (negativeButtonText != Constants.NO_NEGATIVE_BUTTON) {
            negativeButton.setText(negativeButtonText);
        } else {
            negativeButton.setVisibility(View.GONE);
        }
        Button positiveButton = dialogView.findViewById(R.id.button_positive);
        positiveButton.setText(positiveButtonText);

        AlertDialog dialog = dialogBuilder.create();

        if (negativeButtonText != Constants.NO_NEGATIVE_BUTTON) {
            negativeButton.setOnClickListener(v -> {
                dialog.dismiss();
                callback.onNegativeButtonClick();
            });
        }
        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            callback.onPositiveButtonClick();
        });

        dialog.show();
    }
}
