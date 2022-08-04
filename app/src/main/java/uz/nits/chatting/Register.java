package uz.nits.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uz.nits.chatting.adapter.ChatModel;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatting-522de-default-rtdb.firebaseio.com/");

    EditText userNameEdit;
    EditText phoneEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        userNameEdit = findViewById(R.id.name_edit);
        phoneEdit = findViewById(R.id.phone_edit);

        User user = DataStorage.readFromShared(this);
        System.out.println("User: " + user.phone);
        if (!user.phone.equals("")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        findViewById(R.id.regBtn).setOnClickListener(view -> {
            String userName = userNameEdit.getText().toString().trim();
            String phone = phoneEdit.getText().toString().trim();

            if (DataStorage.isNetworkConnected(this)) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("users").hasChild(phone)) {
                            Toast.makeText(Register.this, "This is already created", Toast.LENGTH_SHORT).show();
                        } else {
//                        snapshot.child("users").child("phone").child(phone).child("name").child(userName);
                            databaseReference.child("users").child(phone).child("name").setValue(userName);
                            Toast.makeText(Register.this, "Successful", Toast.LENGTH_SHORT).show();
                            DataStorage.saveToShared(Register.this, new User(phone, userName));
                            MyDatabase database = new MyDatabase(Register.this);
                            database.insertUsers(new User(phone, userName));
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(this, "Internet no connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
