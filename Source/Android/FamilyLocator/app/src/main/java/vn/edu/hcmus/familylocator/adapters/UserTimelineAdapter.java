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
import vn.edu.hcmus.familylocator.models.UserPost;
import vn.edu.hcmus.familylocator.utils.Utils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

/**
 * Created by quangcat on 4/23/17.
 */

public class UserTimelineAdapter extends BaseAdapter {

    private static final String URL = "https://maps.googleapis.com/maps/api/staticmap?center=%s&markers=color:red|%s&zoom=17&size=%s&scale=2&maptype=roadmap&key=AIzaSyBksDFgkOLByumPs9hKHkfozDcHLc50NJE";


    private Context context;
    private ArrayList<UserPost> data = new ArrayList<>();

    public UserTimelineAdapter(Context context) {
        this.context = context;
    }

    public void addAll(ArrayList<UserPost> items) {
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_timeline_post, viewGroup, false);
        }

        view.setTag(position);

        final TextView tvAddress = (TextView) view.findViewById(R.id.address);
        TextView tvTime = (TextView) view.findViewById(R.id.time);

        final UserPost userPost = data.get(position);
        if (TextUtils.isEmpty(userPost.address)) {
            final View finalView = view;
            Utils.getCompleteAddressString(context, userPost.lat, userPost.lon, new Utils.Callback() {
                @Override
                public void onDone(String address) {
                    userPost.address = address;
                    if((int)finalView.getTag() == position) {
                        tvAddress.setText(userPost.address);
                    }
                }
            });
            tvAddress.setText("Đang lấy thông tin vị trí...");
        } else {
            tvAddress.setText(userPost.address);
        }
        tvTime.setText(Utils.convertMsToTimeString(userPost.from, true) + " - " + Utils.convertMsToTimeString(userPost.to, false));
        ImageView map = (ImageView) view.findViewById(R.id.map);
        String latLon = String.valueOf(userPost.lat) + "," + String.valueOf(userPost.lon);
        Picasso.with(context)
                .load(String.format(URL, latLon, latLon,
                        ViewUtils.dp(400) + "x" + ViewUtils.dp(200)))
                .fit()
                .centerCrop()
                .into(map);



//        ((TimelineView) view).setContent(data.get(i).toString(),
//                DateUtils.getRelativeTimeSpanString(userPost.from, userPost.to,
//                        DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME).toString());
        return view;
    }

}
