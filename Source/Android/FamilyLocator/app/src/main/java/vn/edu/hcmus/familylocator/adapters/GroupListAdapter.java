package vn.edu.hcmus.familylocator.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.utils.DataUtils;

public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pair<Long, String>> data = new ArrayList<>();
    private RecyclerView recyclerView;

    public GroupListAdapter(RecyclerView rv) {
        recyclerView = rv;
        RestAPI.getListGroupOfUser(DataUtils.getToken(), new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                if (hasError) {
                    Toast.makeText(recyclerView.getContext(), "Không thể lấy danh sách nhóm.", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray groups = responseData.optJSONObject("data").optJSONArray("groups");
                    for (int i = 0; i < groups.length(); i++) {
                        JSONObject item = groups.optJSONObject(i);
                        long id = item.optInt("id");
                        String name = item.optString("name");
                        data.add(new Pair<>(id, name));
                    }
                    notifyDataSetChanged();
                }
            }
        }, false, null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.circle, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) view.getTag();
                if (pos != 0) {
                    Pair<Long, String> temp = data.get(0);
                    data.set(0, data.get(pos));
                    data.set(pos, temp);
                    notifyItemChanged(0);
                    notifyItemRangeChanged(0, data.size());
                    recyclerView.smoothScrollToPosition(0);
                    if (listener != null) {
                        listener.onFirstItemChanged();
                    }
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    int pos = (int) view.getTag();
                    listener.onItemLongClick(pos, data.get(pos));
                    return true;
                }
                return false;
            }
        });
        return new ViewHolder(view);
    }

    private long currentId = -1;

    public long getCurrentId() {
        return currentId;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position == 0) {
            viewHolder.imageView.setImageResource(R.drawable.checked);
            long tmp = currentId;
            currentId = data.get(position).first;
            if (currentId != tmp && listener != null) {
                listener.onFirstItemChanged();
            }
        } else {
            viewHolder.imageView.setImageResource(R.drawable.uncheck);
        }
        viewHolder.textView.setText(data.get(position).second);
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateItemDataAtPosition(int position, Pair<Long, String> itemData) {
        data.set(position, itemData);
        notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    private GroupListAdapterListener listener;

    public void setOnItemLongClickListener(GroupListAdapterListener listener) {
        this.listener = listener;
    }

    public interface GroupListAdapterListener {
        void onItemLongClick(int position, Pair<Long, String> itemData);

        void onFirstItemChanged();
    }

}