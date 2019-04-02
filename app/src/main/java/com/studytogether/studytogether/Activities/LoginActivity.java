package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studytogether.studytogether.R;

// Login Activity
public class LoginActivity extends AppCompatActivity {

    // Items
    private EditText userMail,userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private ImageView loginPhoto;



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

    // Update UI: replace the activity into Home activity
    private void updateUI() {
        startActivity(HomeActivity);
        // Finish this activity
        finish();
    }

    // Show message to the user
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        // TODO: Block the non-authorized user
        if(user != null) {
            updateUI();
        }
    }
}