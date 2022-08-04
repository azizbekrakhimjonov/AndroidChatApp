package uz.nits.chatting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uz.nits.chatting.DataStorage;
import uz.nits.chatting.R;
import uz.nits.chatting.User;

public class ChatListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ChatModel> list;

    public ChatListAdapter(Context context, ArrayList<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root;
        TextView messageTv;
        TextView timeTv;
        User user = DataStorage.readFromShared(context);
        if (user.getPhone().equals(list.get(i).getUser1())) {
            root = LayoutInflater.from(context).inflate(R.layout.chat_right_custom_layout, viewGroup, false);
            messageTv = root.findViewById(R.id.right_message);
            timeTv = root.findViewById(R.id.right_time);
        } else {
            root = LayoutInflater.from(context).inflate(R.layout.chat_left_custom_layout, viewGroup, false);
            messageTv = root.findViewById(R.id.left_message);
            timeTv = root.findViewById(R.id.left_time);
        }
        messageTv.setText(list.get(i).getMessage() + "\t\t\t\t\t\t\t\t");
        timeTv.setText(list.get(i).getTime());
        return root;
    }
}
