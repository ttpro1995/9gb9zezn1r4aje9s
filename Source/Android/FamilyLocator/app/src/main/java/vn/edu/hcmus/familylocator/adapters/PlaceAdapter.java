package vn.edu.hcmus.familylocator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.models.Appointment;
import vn.edu.hcmus.familylocator.models.Place;

public class PlaceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Place> data = new ArrayList<>();

    public PlaceAdapter(Context context) {
        this.context = context;
    }

    public void addAll(ArrayList<Place> items) {
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
        tv.setText(data.get(i).name);
        return view;
    }

}