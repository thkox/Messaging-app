package eu.thkox.messaging_app.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import eu.thkox.messaging_app.activity.ChatsActivity;
import eu.thkox.messaging_app.activity.MainActivity;
import eu.thkox.messaging_app.activity.SearchUserChatActivity;
import eu.thkox.messaging_app.activity.SignInActivity;
import eu.thkox.messaging_app.activity.SignUpActivity;

public class ActivityUtils {

    public static void goToMainActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goToSignInActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, SignInActivity.class);
        startActivity(activity, intent, null);
    }

    public static void goToSignUpActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, SignUpActivity.class);
        startActivity(activity, intent, null);
    }

    public static void goToChatsActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, ChatsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goToSearchUserChatActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, SearchUserChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

}
