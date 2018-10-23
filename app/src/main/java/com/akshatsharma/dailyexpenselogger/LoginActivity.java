package com.akshatsharma.dailyexpenselogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity {
//        implements View.OnClickListener {

    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID and basic profile information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();

        // Build a GoogleSignInClient with the options specified by the gso
        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);

        signInButton = (findViewById(R.id.btn_signin));
        //Makes the button bigger and blue in colour
        signInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_DARK);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // then the GoogleSignInAccount will be non-null.
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_signin:
//                signIn();
//                break;
//            case R.id.btn_skip_signin:
//
//        }
//    }

    // This method checks the value of GoogleSignInAccount.
    // If it isn't null, the user is redirected to MainActivity
    private void updateUI(GoogleSignInAccount account) {
//        if(account == null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
    }
}
