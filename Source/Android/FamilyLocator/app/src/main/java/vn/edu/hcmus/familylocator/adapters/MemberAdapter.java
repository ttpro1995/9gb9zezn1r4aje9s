package vn.edu.hcmus.familylocator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.models.Member;

public class MemberAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Member> data = new ArrayList<>();

    public MemberAdapter(Context context) {
        this.context = context;
    }

    public void addAll(ArrayList<Member> items) {
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
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.user_info, null);

        Member member = data.get(i);
        Picasso.with(context)
                .load(RestAPI.URL_BASE + "/" + member.avatar)
                .into((ImageView) view.findViewById(R.id.avatar));
        ((TextView) view.findViewById(R.id.name)).setText(member.getFullname());
        return view;
    }

}