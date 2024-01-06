package eu.thkox.messaging_app.activity;

import static eu.thkox.messaging_app.utils.FirebaseUtils.getSignedInUser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

import eu.thkox.messaging_app.R;
import eu.thkox.messaging_app.utils.ActivityUtils;
import eu.thkox.messaging_app.utils.FirebaseUtils;

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
        FirebaseUser firebaseUser = getSignedInUser();
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