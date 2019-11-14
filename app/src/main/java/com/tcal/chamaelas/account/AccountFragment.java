package com.tcal.chamaelas.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.tcal.chamaelas.R;
import com.tcal.chamaelas.util.Constants;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private AccountViewModel mAccountViewModel;

    private View mAccountInfoLayout;
    private ImageView mAccountImage;
    private View mAccountInfo;
    private TextView mAccountName;
    private TextView mAccountEmail;
    private TextView mLogin;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        mAccountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        mAccountViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        mAccountInfoLayout = root.findViewById(R.id.account_info_layout);
        mAccountInfoLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), LogInActivity.class);
            startActivityForResult(intent, Constants.LOGIN_REQUEST_CODE);
        });

        mAccountImage = root.findViewById(R.id.account_image);
        mAccountInfo = root.findViewById(R.id.account_info);
        mAccountName = root.findViewById(R.id.account_name);
        mAccountEmail = root.findViewById(R.id.account_email);
        mLogin = root.findViewById(R.id.account_login);
        // TODO: check if user is already logged in
        setLoginView();

        View aboutView = root.findViewById(R.id.account_about);
        aboutView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AboutActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            setAccountView(currentUser);
        } else {
            setLoginView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK
                    && data != null && data.hasExtra(Constants.LOGIN_REQUEST_EXTRA)) {
                FirebaseUser currentUser = data.getParcelableExtra(Constants.LOGIN_REQUEST_EXTRA);
                // update screen
                setAccountView(currentUser);
            }
        }
    }

    private void setLoginView() {
        setAccountViewState(false);
    }

    private void setAccountView(FirebaseUser user) {
        setAccountViewState(true);

        mAccountName.setText(user.getDisplayName());
        mAccountEmail.setText(user.getEmail());
        Uri accountImageUri = user.getPhotoUrl();
        if (accountImageUri != null) {
            Picasso.get()
                    .load(accountImageUri)
                    .transform(new CropCircleTransformation())
                    .into(mAccountImage);
        }
    }

    private void setAccountViewState(boolean isLogged) {
        mAccountInfoLayout.setEnabled(!isLogged);
        mLogin.setVisibility(isLogged ? View.GONE : View.VISIBLE);
        mAccountInfo.setVisibility(isLogged ? View.VISIBLE : View.GONE);
    }
}