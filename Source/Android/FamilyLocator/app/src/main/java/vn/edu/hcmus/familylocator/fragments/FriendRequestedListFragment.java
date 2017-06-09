package vn.edu.hcmus.familylocator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.adapters.FriendAdapter;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.models.Friend;
import vn.edu.hcmus.familylocator.utils.DataUtils;

/**
 * Created by quangcat on 5/14/17.
 */

public class FriendRequestedListFragment extends Fragment implements AbsListView.OnScrollListener {

    private static final int ITEM_COUNT_PER_PAGE = 15;

    private ListView listView;
    private FriendAdapter adapter;
    private int currentPage = 0;
    private boolean loading = false;
    private int total = Integer.MAX_VALUE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (listView == null) {
            listView = new ListView(getContext());
            listView.setOnScrollListener(this);
            adapter = new FriendAdapter(getContext());
            listView.setAdapter(adapter);
            nextPage();
        }
        return listView;
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//        ViewUtils.createConfirmDialog(getContext(), "Bạn có muốn xóa người bạn này?", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == DialogInterface.BUTTON_POSITIVE) {
//                    String json = new JSONStringBuilder()
//                            .put("uidTo", ((Friend) adapter.getItem(position)).id)
//                            .create();
//                    RestAPI.unfriend(json, DataUtils.getToken(), new RestAPI.Callback() {
//                        @Override
//                        public void onDone(JSONObject responseData, boolean hasError) {
//                            if (hasError) {
//                                Toast.makeText(getContext(), "Xóa bạn thất bại.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                adapter.remove(position);
//                            }
//                        }
//                    }, true, getContext());
//                }
//            }
//        }, true).show();
//        return true;
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount - 3) {
            nextPage();
        }
    }

    private void nextPage() {
        if (loading)
            return;

        int from = currentPage * ITEM_COUNT_PER_PAGE;
        if (from > total)
            return;

        loading = true;
        RestAPI.getListRequested(DataUtils.getToken(), new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                if (hasError) {
                    if (responseData.optInt("error_code") == 1010) {
                        Toast.makeText(getContext(), "Danh sách bạn trống.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Không thể lấy danh sách bạn.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    JSONObject data = responseData.optJSONObject("data");
                    total = data.optInt("total");
                    ArrayList<Friend> pageData = new ArrayList<>();
                    JSONArray array = data.optJSONArray("users");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.optJSONObject(i);
                        Friend item = new Friend();
                        item.id = obj.optLong("id");
                        item.firstName = obj.optString("firstName");
                        item.lastName = obj.optString("lastName");
                        item.email = obj.optString("email");
                        item.phoneNumber = obj.optString("phoneNumber");
                        item.avatar = obj.optString("avatar");
                        pageData.add(item);
                    }
                    adapter.addAll(pageData);
                    currentPage++;
                }
                loading = false;
            }
        }, false, null);
    }

}
