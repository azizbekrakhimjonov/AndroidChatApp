package uz.nits.chatting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uz.nits.chatting.adapter.ChatListAdapter;
import uz.nits.chatting.adapter.ChatModel;

public class    ChatActivity extends AppCompatActivity {

    EditText chatEdit;
    ImageView sendBtn;

    User user;
    ArrayList<ChatModel> list;
    ListView listView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatting-522de-default-rtdb.firebaseio.com/");
    int chatId = 1;
    int checkCount = 0;
    String user2 = "";
    ChatListAdapter adapter;
    MyDatabase database;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        database = new MyDatabase(this);
        chatEdit = findViewById(R.id.chat_edit);
        sendBtn = findViewById(R.id.send_message_id);
        listView = findViewById(R.id.chat_list_view);
        list = new ArrayList<>();
        adapter = new ChatListAdapter(ChatActivity.this, list);
        listView.setAdapter(adapter);
        user = DataStorage.readFromShared(this);
        user2 = getIntent().getStringExtra("phone");
        if (DataStorage.isNetworkConnected(this)) {
            checkedFirebaseChats();
            readFromFirebase();
        } else {
            readFromDatabase();
        }
        sendBtn.setOnClickListener(view -> {
            if (DataStorage.isNetworkConnected(this)) {
                saveToDatabase();
                saveToFirebase();
            } else {
                saveToDatabase();
                chatEdit.setText("");
                readFromDatabase();
            }
        });
    }

    private void checkedFirebaseChats() {
        checkCount = 0;
        chatId = 1;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("chats")) {
                    checkCount = (int) snapshot.child("chats").getChildrenCount();
                }
                if (checkCount < database.getCountChat() - 1 && database.getCountChat() > 1) {
                    ArrayList<ChatModel> checkList = database.getChats();
                    for (int i = checkCount; i < database.getCountChat() - 1; i++) {
                        if (snapshot.hasChild("chats"))
                            chatId = (int) snapshot.child("chats").getChildrenCount() + 1;

                        databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("sms").setValue(checkList.get(i).getMessage());
                        databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("time").setValue(checkList.get(i).getTime());
                        databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("user1").setValue(checkList.get(i).getUser1());
                        databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("user2").setValue(checkList.get(i).getUser2());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readFromDatabase() {
        list.clear();
        list.addAll(database.getChats());
        adapter.notifyDataSetChanged();
    }

    private void saveToDatabase() {
        @SuppressLint("SimpleDateFormat")
        String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        database.insertChats(new ChatModel(
                user.getPhone(),
                user2,
                "",
                "",
                chatEdit.getText().toString(),
                currentTime
        ));
    }

    private void saveToFirebase() {
        @SuppressLint("SimpleDateFormat")
        String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("chats")) {
                    chatId = (int) snapshot.child("chats").getChildrenCount() + 1;
                }
                databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("sms").setValue(chatEdit.getText().toString());
                databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("time").setValue(currentTime);
                databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("user1").setValue(user.getPhone());
                databaseReference.child("chats").child(String.valueOf(chatId)).child("message").child("user2").setValue(user2);
                chatEdit.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                int countDB = database.getCountChat() - 1;
                int countFB = 0;

                if (snapshot.hasChild("chats")) {
                    for (DataSnapshot dataSnapshot : snapshot.child("chats").getChildren()) {
                        countFB++;
                        if (dataSnapshot.hasChild("message")) {
                            String u1 = dataSnapshot.child("message").child("user1").getValue(String.class);
                            String u2 = dataSnapshot.child("message").child("user2").getValue(String.class);
                            if (user.phone.equals(u1) && user2.equals(u2)
                                    || user.phone.equals(u2) && user2.equals(u1)) {
                                System.out.println("SMS:" + dataSnapshot.child("message").child("sms").getValue(String.class));
                                System.out.println("TIME:" + dataSnapshot.child("message").child("time").getValue(String.class));
                                ChatModel chatModel = new ChatModel(
                                        u1,
                                        u2,
                                        "",
                                        "",
                                        dataSnapshot.child("message").child("sms").getValue(String.class),
                                        dataSnapshot.child("message").child("time").getValue(String.class)
                                );
                                list.add(chatModel);
                                if (countFB > countDB) {
                                    database.insertChats(chatModel);
                                    countDB = database.getCountChat() - 1;
                                }
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                listView.setSelection(list.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
