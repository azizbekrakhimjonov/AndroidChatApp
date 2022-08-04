package uz.nits.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<User> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatting-522de-default-rtdb.firebaseio.com/");
    String phone;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        User myUser = DataStorage.readFromShared(this);
        phone = myUser.getPhone();
        name = myUser.getUserName();
        ListView listView = findViewById(R.id.list_view);
        ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list);
        listView.setAdapter(adapter);
        System.out.println(phone);

        if (DataStorage.isNetworkConnected(this)) {
            MyDatabase  database = new MyDatabase(MainActivity.this);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                        String getPhone = dataSnapshot.getKey();
                        if (!getPhone.equals(phone)) {
                            System.out.println(getPhone);
                            System.out.println(dataSnapshot.child("name").getValue(String.class));
                            User user = new User(
                                    getPhone,
                                    dataSnapshot.child("name").getValue(String.class)
                            );
                            list.add(user);
                            database.insertUsers(user);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            list.clear();
            MyDatabase database = new MyDatabase(this);
            list.addAll(database.getUsers());
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("phone", list.get(i).getPhone());
            intent.putExtra("name", list.get(i).getUserName());
            startActivity(intent);
        });
    }
}