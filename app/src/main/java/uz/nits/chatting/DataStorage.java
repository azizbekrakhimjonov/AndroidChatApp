package uz.nits.chatting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DataStorage {

    public static void saveToShared(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MYSHARED", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", user.getPhone());
        editor.putString("name", user.getUserName());
        editor.apply();
    }

    public static User readFromShared(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MYSHARED", Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("phone", ""),
                sharedPreferences.getString("name", ""));
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
