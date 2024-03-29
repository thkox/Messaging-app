package eu.thkox.messaging_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import eu.thkox.messaging_app.R;
import eu.thkox.messaging_app.utils.ActivityUtils;

public class SignInActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    TextView textViewSignInError;
    Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = findViewById(R.id.editTextSignInTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextSignInTextPassword);

        textViewSignInError = findViewById(R.id.textViewSignInError);
        textViewSignInError.setVisibility(TextView.INVISIBLE);

        buttonSignIn = findViewById(R.id.buttonSignIn);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.app_background_color));
    }

    public void signInUser(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            textViewSignInError.setVisibility(TextView.VISIBLE);
            textViewSignInError.setText(R.string.all_fields_are_required);
        } else {
            signIn(email, password);
        }

    }

    private void signIn(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ActivityUtils.goToChatsActivity(SignInActivity.this);
            } else {
                textViewSignInError.setVisibility(TextView.VISIBLE);
                textViewSignInError.setText(R.string.you_have_entered_an_invalid_email_or_password);
            }
        });
    }

    public void goToMainActivity(View view) {
        ActivityUtils.goToMainActivity(this);
    }
}