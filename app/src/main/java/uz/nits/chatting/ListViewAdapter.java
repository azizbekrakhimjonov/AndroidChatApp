package uz.nits.chatting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<User> list;

    public ListViewAdapter(Context context, ArrayList<User> list) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View root = LayoutInflater.from(context).inflate(R.layout.custom_list_layout, viewGroup, false);
        TextView circleText = root.findViewById(R.id.circle_text);
        TextView nameText = root.findViewById(R.id.name_text);
        TextView phoneText = root.findViewById(R.id.phone_text);

        String circle = "";
        String[] names = list.get(i).getUserName().split("\\s+");
        System.out.println(Arrays.toString(names));
        if (names.length == 1)
            circle = circle.concat(String.valueOf(names[0].charAt(0)).toUpperCase());
        else
            circle = circle.concat(String.valueOf(names[0].charAt(0)).toUpperCase()).concat(String.valueOf(names[names.length - 1].charAt(0)).toUpperCase());

        circleText.setText(circle);
        nameText.setText(list.get(i).getUserName());
        phoneText.setText(list.get(i).phone);

        return root;
    }
}
