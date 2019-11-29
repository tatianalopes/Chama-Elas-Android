package com.tcal.chamaelas.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.tcal.chamaelas.R;
import com.tcal.chamaelas.util.CLog;
import com.tcal.chamaelas.util.Constants;

import static com.tcal.chamaelas.util.Constants.FB_ERROR_EMAIL_ALREADY_IN_USE;
import static com.tcal.chamaelas.util.Constants.FB_ERROR_INVALID_EMAIL;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private TextInputEditText mNameView;
    private TextInputEditText mSurnameView;
    private TextInputLayout mEmailLayout;
    private TextInputEditText mEmailView;
    private TextInputEditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setToolbar();

        TextInputLayout nameLayout = findViewById(R.id.register_name_layout);
        mNameView = findViewById(R.id.register_name_text);
        TextInputLayout surnameLayout = findViewById(R.id.register_surname_layout);
        mSurnameView = findViewById(R.id.register_surname_text);
        mEmailLayout = findViewById(R.id.register_email_layout);
        mEmailView = findViewById(R.id.register_email_text);
        TextInputLayout passwordLayout = findViewById(R.id.register_password_layout);
        mPasswordView = findViewById(R.id.register_password_text);

        MaterialButton registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(view -> {
            boolean keepGoing = true;

            String name = mNameView.getText().toString().trim();
            if (name.isEmpty()) {
                nameLayout.setError(getString(R.string.register_error_empty));
                keepGoing = false;
            }

            String surname = mSurnameView.getText().toString().trim();
            if (surname.isEmpty()) {
                surnameLayout.setError(getString(R.string.register_error_empty));
                keepGoing = false;
            }

            String email = mEmailView.getText().toString().trim();
            if (email.isEmpty()) {
                mEmailLayout.setError(getString(R.string.register_error_empty));
                keepGoing = false;
            }

            String password = mPasswordView.getText().toString().trim();
            if (password.isEmpty()) {
                passwordLayout.setError(getString(R.string.register_error_empty));
                keepGoing = false;
            }

            if (keepGoing) {
                nameLayout.setError(null);
                surnameLayout.setError(null);
                mEmailLayout.setError(null);
                passwordLayout.setError(null);

                registerUser();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

    private void registerUser() {
        String userName = mNameView.getText().toString().trim() + " " + mSurnameView.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(mEmailView.getText().toString(), mPasswordView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName).build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                                Intent intent = new Intent();
                                intent.putExtra(Constants.LOGIN_REQUEST_EXTRA, user);
                                setResult(RESULT_OK, intent);
                                finish();
                            });
                        } else {
                            handleRegisterErrors(task.getException());
                        }
                    }
                });
    }

    private void handleRegisterErrors(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) exception;

            switch (firebaseAuthException.getErrorCode()) {
                case FB_ERROR_INVALID_EMAIL:
                    mEmailLayout.setError(getString(R.string.error_invalid_email));
                    break;
                case FB_ERROR_EMAIL_ALREADY_IN_USE:
                    mEmailLayout.setError(getString(R.string.register_error_duplicate));
                    break;
                default:
                    showErrorMessage(exception, R.string.register_snackbar_error);
            }
        } else {
            showErrorMessage(exception, R.string.register_snackbar_error);
        }
    }

    private void showErrorMessage(Exception exception, int messageResourceId) {
        Snackbar.make(findViewById(R.id.register_view),
                messageResourceId, Snackbar.LENGTH_LONG).show();
        CLog.e(TAG, "Failed to setLoginViews. Exception: ", exception);
    }

    /**
     * Initializes the screen toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.register_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
