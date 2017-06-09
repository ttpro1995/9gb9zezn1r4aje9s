package vn.edu.hcmus.familylocator.adapters;

import android.content.Context;
import android.text.TextUtils;
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
import vn.edu.hcmus.familylocator.models.GroupPost;
import vn.edu.hcmus.familylocator.utils.Utils;

/**
 * Created by quangcat on 5/28/17.
 */

public class GroupTimelineAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GroupPost> data;

    public GroupTimelineAdapter(Context context, ArrayList<GroupPost> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_timeline_post, null);
        }

        convertView.setTag(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(position, false);
            }
        });

        final GroupPost p = data.get(position);

        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        Picasso.with(context).load(RestAPI.URL_BASE + "/" + p.avatar).into(avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(position, true);
            }
        });

        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        tvName.setText(p.getFullname());
        final TextView tvAddress = (TextView) convertView.findViewById(R.id.description);
        if (TextUtils.isEmpty(p.address)) {
            final View finalConvertView = convertView;
            Utils.getCompleteAddressString(context, p.lat, p.lon, new Utils.Callback() {
                @Override
                public void onDone(String address) {
                    p.address = address;
                    if ((int) finalConvertView.getTag() == position) {
                        tvAddress.setText(p.address);
                    }
                }
            });
            tvAddress.setText("Đang lấy thông tin vị trí");
        } else {
            tvAddress.setText(p.address);
        }
        ((TextView) convertView.findViewById(R.id.battery)).setText(String.valueOf(p.battery) + "%");

        return convertView;
    }

    private GroupTimelineListener listener;

    public void setGroupTimelineListener(GroupTimelineListener listener) {
        this.listener = listener;
    }

    public interface GroupTimelineListener {
        void onItemClick(int position, boolean avatarClicked);
    }
}
