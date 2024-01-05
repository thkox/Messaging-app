package eu.thkox.messaging_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button buttonLogIn;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is already logged in
        FirebaseUser firebaseUser = FirebaseUtils.getSignedInUser();
        if (firebaseUser != null) {
            ActivityUtils.goToChatsActivity(this);
        }
    }

    public void goToSignInActivity(View view) {
        ActivityUtils.goToSignInActivity(this);
    }

    public void goToSignUpActivity(View view) {
        ActivityUtils.goToSignUpActivity(this);
    }
}