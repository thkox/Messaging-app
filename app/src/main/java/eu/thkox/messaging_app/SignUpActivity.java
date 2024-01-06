package eu.thkox.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import eu.thkox.messaging_app.data.model.User;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextNickname, editTextEmail, editTextPassword;
    TextView textViewSignUpError;
    Button buttonSignUp;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextNickname = findViewById(R.id.editTextSignUpTextNickname);
        editTextEmail = findViewById(R.id.editTextSignUpTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextSignUpTextPassword);
        textViewSignUpError = findViewById(R.id.textViewSignInError);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        textViewSignUpError.setVisibility(TextView.INVISIBLE);
    }

    public void signUpUser(View view) {
        String nickname = editTextNickname.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            textViewSignUpError.setVisibility(TextView.VISIBLE);
            textViewSignUpError.setText(R.string.all_fields_are_required);
        } else if (password.length() < 6) {
            textViewSignUpError.setVisibility(TextView.VISIBLE);
            textViewSignUpError.setText(R.string.password_must_be_at_least_4_characters_long);
        } else {
            registerUser(nickname, email, password);
        }
    }

    private void registerUser(String nickname, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser firebaseUser = auth.getCurrentUser();
                assert firebaseUser != null;
                String userid = firebaseUser.getUid();

                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                User newUser = new User(email, nickname, userid, "default");

                reference.setValue(newUser).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        ActivityUtils.goToChatsActivity(SignUpActivity.this);
                    }
                });
            }else{
                textViewSignUpError.setVisibility(TextView.VISIBLE);
                textViewSignUpError.setText(R.string.you_can_t_register_with_this_email_or_password);
            }
        });
    }

}