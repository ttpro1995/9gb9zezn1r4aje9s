package vn.edu.hcmus.familylocator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import vn.edu.hcmus.familylocator.R;

/**
 * Created by quangcat on 4/23/17.
 */

public class MessageAdapter extends BaseAdapter {

    private Context mContext;

    public MessageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item_view, viewGroup);
        return v;
    }

}
