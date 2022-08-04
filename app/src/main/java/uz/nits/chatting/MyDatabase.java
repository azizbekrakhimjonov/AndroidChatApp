package uz.nits.chatting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import uz.nits.chatting.adapter.ChatModel;

public class MyDatabase extends SQLiteOpenHelper {
    public static String DATA_BASE_NAME = "MY_DATA_BASE";
    public static String TABLE_USERS = "users";
    public static String TABLE_CHATS = "chats";
    public static int DATA_BASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUsersQuery = "create table " + TABLE_USERS +
                "(userId text, " +
                "name text);";
        sqLiteDatabase.execSQL(createUsersQuery);


        String createChatsQuery = "create table " + TABLE_CHATS +
                "(chatId integer primary key, " +
                "sms text, " +
                "time text, " +
                "user1 text, " +
                "user2 text);";
        sqLiteDatabase.execSQL(createChatsQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    public void insertUsers(User user) {
        if (checkUser(user)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userId", user.getPhone());
            cv.put("name", user.getUserName());

            db.insert(TABLE_USERS, null, cv);
            db.close();
        }
    }

    public boolean checkUser(User user) {
        ArrayList<User> userList = new ArrayList<>(getUsers());
        for (User allUsers : userList) {
            if (allUsers.getPhone().equals(user.getPhone()))
                return false;
        }
        return true;
    }

    public void insertChats(ChatModel chatModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sms", chatModel.getMessage());
        cv.put("time", chatModel.getTime());
        cv.put("user1", chatModel.getUser1());
        cv.put("user2", chatModel.getUser2());

        db.insert(TABLE_CHATS, null, cv);
        db.close();
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> userList = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            userList.add(
                    new User(
                            cursor.getString(0),
                            cursor.getString(1))
            );
        }

        cursor.close();
        System.out.println(userList.size());
        return userList;
    }

    public int getCountChat() {
        String selectQuery = "select count(*) from " + TABLE_CHATS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return count + 1;
    }

    public ArrayList<ChatModel> getChats() {
        ArrayList<ChatModel> chatList = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_CHATS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            chatList.add(new ChatModel(
                    cursor.getString(3),
                    cursor.getString(4),
                    "",
                    "",
                    cursor.getString(1),
                    cursor.getString(2)
            ));
        }

        cursor.close();
        return chatList;
    }


}
