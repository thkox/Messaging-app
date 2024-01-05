package eu.thkox.messaging_app;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference;

    public static FirebaseUser getSignedInUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void logOutUser() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void registerUser(String nickname, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
    }

}
