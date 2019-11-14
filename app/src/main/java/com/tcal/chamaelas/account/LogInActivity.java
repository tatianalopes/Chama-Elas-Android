package com.tcal.chamaelas.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tcal.chamaelas.R;
import com.tcal.chamaelas.util.CLog;
import com.tcal.chamaelas.util.Constants;
import com.tcal.chamaelas.util.DialogCallback;
import com.tcal.chamaelas.util.DialogUtils;
import com.tcal.chamaelas.util.InputDialogCallback;

import static com.tcal.chamaelas.util.Constants.FB_ERROR_INVALID_EMAIL;
import static com.tcal.chamaelas.util.Constants.FB_ERROR_USER_NOT_FOUND;
import static com.tcal.chamaelas.util.Constants.FB_ERROR_WRONG_PASSWORD;

public class LogInActivity extends AppCompatActivity {

    public static final String TAG = LogInActivity.class.getSimpleName();

    private static final int GOOGLE_LOGIN_REQUEST_CODE = 1;
    private static final int REGISTER_REQUEST_CODE = 2;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    
    private TextInputEditText mEmailView;
    private TextInputLayout mEmailLayout;
    private TextInputEditText mPasswordView;
    private MaterialButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setToolbar();

        mAuth = FirebaseAuth.getInstance();

        setLoginViews();
        setLoginWithGoogleView();
        setRegisterView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_LOGIN_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                CLog.e(TAG, "Google sign in failed. ", e);
            }
        } else if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private void setLoginViews() {
        mEmailView = findViewById(R.id.login_email_text);
        mEmailLayout = findViewById(R.id.login_email_layout);
        mPasswordView = findViewById(R.id.login_password_text);
        mLoginButton = findViewById(R.id.login_button);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsForEmptyValues();
            }
        };
        mEmailView.addTextChangedListener(textWatcher);
        mPasswordView.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues();
        mLoginButton.setOnClickListener(view -> {
            mEmailLayout.setError(null);
            hideSoftKeyboard(view);

            mAuth.signInWithEmailAndPassword(mEmailView.getText().toString(), mPasswordView.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                updateUI(mAuth.getCurrentUser());
                            } else {
                                handleLoginErrors(task.getException());
                            }
                        }
                    });

        });

        TextView forgotPassword = findViewById(R.id.login_forgot_password);
        forgotPassword.setOnClickListener(view -> {
            showNewPasswordDialog();
        });
    }

    private void checkFieldsForEmptyValues(){
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean isEmpty = email.isEmpty() || password.isEmpty();
        mLoginButton.setEnabled(!isEmpty);
    }

    public void hideSoftKeyboard(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    private void handleLoginErrors(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) exception;

            switch (firebaseAuthException.getErrorCode()) {
                case FB_ERROR_INVALID_EMAIL:
                    mEmailLayout.setError(getString(R.string.error_invalid_email));
                    break;
                case FB_ERROR_USER_NOT_FOUND:
                    showInvalidUserDialog();
                    break;
                case FB_ERROR_WRONG_PASSWORD:
                    showWrongPasswordDialog();
                    break;
                default:
                    showErrorMessage(exception, R.string.login_error_snackbar);
            }
        } else {
            showErrorMessage(exception, R.string.login_error_snackbar);
        }
    }

    private void showInvalidUserDialog() {
        DialogCallback dialogCallback = new DialogCallback() {
            @Override
            public void onNegativeButtonClick() {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER_REQUEST_CODE);
            }

            @Override
            public void onPositiveButtonClick() {
                mEmailView.getText().clear();
                mEmailView.requestFocus();
                mPasswordView.getText().clear();
            }
        };

        DialogUtils.createTwoButtonsActionDialog(this,
                getString(R.string.login_invalid_user_dialog_title),
                getString(R.string.login_invalid_user_dialog_content, mEmailView.getText().toString()),
                R.string.button_try_again, R.string.button_create_account, dialogCallback);
    }

    private void showWrongPasswordDialog() {
        DialogCallback dialogCallback = new DialogCallback() {
            @Override
            public void onNegativeButtonClick() { }

            @Override
            public void onPositiveButtonClick() {
                mPasswordView.getText().clear();
                mPasswordView.requestFocus();
            }
        };

        DialogUtils.createTwoButtonsActionDialog(this,
                getString(R.string.login_wrong_password_dialog_title),
                getString(R.string.login_wrong_password_dialog_content),
                R.string.button_ok, Constants.NO_NEGATIVE_BUTTON, dialogCallback);
    }


    private void showNewPasswordDialog() {
        InputDialogCallback inputDialogCallback = (dialog, dialogInputLayout, dialogInput) -> {
            String email = dialogInput.getText().toString().trim();

            if (email.length() == 0) {
                dialogInputLayout.setError(getString(R.string.error_invalid_email));
            } else {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Snackbar.make(findViewById(R.id.login_view),
                                    R.string.login_change_password_snackbar_success,
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            handleNewPasswordErrors(task.getException(), dialog, dialogInputLayout);
                        }
                    }
                });;
            }
        };

        DialogUtils.createInputDialog(this, R.string.login_password_dialog_title,
                R.string.login_password_dialog_message, R.string.button_send, inputDialogCallback);
    }

    private void handleNewPasswordErrors(Exception exception,
                                         AlertDialog dialog, TextInputLayout textInputLayout) {
        if (exception instanceof FirebaseAuthException) {
            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) exception;

            switch (firebaseAuthException.getErrorCode()) {
                case FB_ERROR_INVALID_EMAIL:
                    textInputLayout.setError(getString(R.string.error_invalid_email));
                    break;
                case FB_ERROR_USER_NOT_FOUND:
                    textInputLayout.setError(getString(R.string.error_no_record_email));
                    break;
                default:
                    dialog.dismiss();
                    showErrorMessage(exception, R.string.login_change_password_snackbar_error);
            }
        } else {
            dialog.dismiss();
            showErrorMessage(exception, R.string.login_change_password_snackbar_error);
        }
    }

    private void setLoginWithGoogleView() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("583592351351-bp3pho6un25cpba3cb15kgtbfdhr9vpe.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        SignInButton googleLoginButton = findViewById(R.id.google_login_button);
        View buttonChildView = googleLoginButton.getChildAt(0);
        if (buttonChildView instanceof TextView) {
            TextView textView = (TextView) buttonChildView;
            textView.setText(R.string.login_google_button);
        }
        googleLoginButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_LOGIN_REQUEST_CODE);
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            CLog.e(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void setRegisterView() {
        TextView registerView = findViewById(R.id.login_register);
        registerView.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, REGISTER_REQUEST_CODE);
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent();
        intent.putExtra(Constants.LOGIN_REQUEST_EXTRA, user);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showErrorMessage(Exception exception, int messageResourceId) {
        Snackbar.make(findViewById(R.id.login_view),
                messageResourceId, Snackbar.LENGTH_LONG).show();
        CLog.e(TAG, "Failed to setLoginViews. Exception: ", exception);
    }

    /**
     * Initializes the screen toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.account_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
