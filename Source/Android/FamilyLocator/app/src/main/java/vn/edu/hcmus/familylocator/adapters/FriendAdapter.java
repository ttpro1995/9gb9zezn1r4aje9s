package vn.edu.hcmus.familylocator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.models.Friend;

public class FriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Friend> data = new ArrayList<>();

    public FriendAdapter(Context context) {
        this.context = context;
    }

    public void addAll(ArrayList<Friend> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView tv = (TextView) view;
        tv.setText(data.get(i).firstName + " " + data.get(i).lastName);
        return view;
    }

}