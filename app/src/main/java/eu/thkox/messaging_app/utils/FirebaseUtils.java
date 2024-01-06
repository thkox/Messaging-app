package eu.thkox.messaging_app.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public static DatabaseReference getTheReferenceUsers() {
        return FirebaseDatabase.getInstance().getReference("Users");
    }

    public static DatabaseReference getTheReferenceUser() {
        FirebaseUser currentUser = getSignedInUser();
        return FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
    }

    public static DatabaseReference getTheReferenceReceiver(String userId) {
        return FirebaseDatabase.getInstance().getReference("Users").child(userId);
    }

    public static DatabaseReference getTheReferenceChats() {
        return FirebaseDatabase.getInstance().getReference("Chats");
    }

    public static DatabaseReference getTheReferenceChat(String chatId) {
        return getTheReferenceChats().child(chatId);
    }

    public static DatabaseReference getTheReferenceMessages(String chatId) {
        return getTheReferenceChat(chatId).child("messages");
    }

    public static void registerUser(String nickname, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
    }

    public static String generateChatId(String user1Id, String user2Id) {
        List<String> ids = Arrays.asList(user1Id, user2Id);
        Collections.sort(ids);
        String concatenatedIds = String.join("_", ids);
        return Integer.toString(concatenatedIds.hashCode());
    }

}
