package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import com.studytogether.studytogether.R;


// Login Activity
public class LoginActivity extends AppCompatActivity {


    static final int GOOGLE_SIGN_IN = 123;

    // Items
    private EditText userMail, userPassword;
    private Button btnLogin, btnGoogleLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private ImageView loginPhoto;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content with activity_login layout
        setContentView(R.layout.activity_login);

        // Set up items
        userMail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_btn);
        loginProgress = findViewById(R.id.login_progress);
        btnGoogleLogin = findViewById(R.id.google_login_btn);

        // Firebase authorization
        mAuth = FirebaseAuth.getInstance();

        // Get Home intent
        HomeActivity = new Intent(this,com.studytogether.studytogether.Activities.Home.class);
        loginPhoto = findViewById(R.id.login_photo);
        // If a user selects the login photo, go to the register Activity
        // TODO: Make permission for accessing gallery
        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerActivity = new Intent(getApplicationContext(),RegisterActivity.class);
                // Go to the register Activity
                startActivity(registerActivity);
                // Finish this activity
                finish();
            }
        });

        // Set the progress-button as invisible mode
        loginProgress.setVisibility(View.INVISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Listen the login-button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the user pressed the login-button, let the login-button disappear and show up the progress-button
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                // Get user inputs
                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                // If any inputs do not typed,
                if (mail.isEmpty() || password.isEmpty()) {
                    // Show an error message to the user
                    showMessage("Please Verify All Field");
                    // Back up the login-button and let the progress-button disappear
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
                else {
                    // Sign-in
                    signIn(mail,password);
                }
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("google login btn clicked");
                SignInGoogle();
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showMessage("firebaseAuthWithGoogle");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        showMessage("User logged in successfully");
                        loginProgress.setVisibility(View.INVISIBLE);
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUIGoogle(user);
                    } else {
                        showMessage("Could not log in user");
                        loginProgress.setVisibility(View.INVISIBLE);
                        updateUIGoogle(null);
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showMessage("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                showMessage("try");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                showMessage("error");
            }
        }
    }

    // Sign in
    private void signIn(String mail, String password) {
        // Call the Firebase authorization
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // If sign in is working correctly,
                if (task.isSuccessful()) {
                    // Back up the login-button and let the progress-button disappear
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    // Replace the activity into Home activity
                    updateUI();
                }
                else {
                    // If the sign in is failed, show up an error message
                    showMessage(task.getException().getMessage());
                    // Back up the login-button and let the progress-button disappear
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void SignInGoogle() {
        showMessage("SignInGoogle");
        loginProgress.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    // Update UI: replace the activity into Home activity
    private void updateUI() {
        startActivity(HomeActivity);
        // Finish this activity
        finish();
    }

    private void updateUIGoogle(FirebaseUser user) {
        if (user != null) {
            showMessage("User logged in successfully");

            String photo = String.valueOf(user.getPhotoUrl());
            Picasso.with(LoginActivity.this).load(photo).into(loginPhoto);

            startActivity(HomeActivity);
            finish();
        } else {
            showMessage("Could not log in user");
        }
    }

    // Show message to the user
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Block the non-authorized user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null) {
            updateUIGoogle(currentUser);
        } else {
            showMessage("current user is null");
            SignInGoogle();
        }
    }
}