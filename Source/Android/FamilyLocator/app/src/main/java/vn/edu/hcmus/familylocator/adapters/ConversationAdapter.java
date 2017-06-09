package vn.edu.hcmus.familylocator.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.utils.GraphicUtils;
import vn.edu.hcmus.familylocator.models.Message;
import vn.edu.hcmus.familylocator.views.MessageView;

/**
 * Created by quangcat on 4/23/17.
 */

public class ConversationAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Message> mData;

    public ConversationAdapter(Context context) {
        mContext = context;
        mData = dummyData();
    }

    private ArrayList<Message> dummyData() {
        Bitmap avatar = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar);
        avatar = ThumbnailUtils.extractThumbnail(avatar, MessageView.AVATAR_SIZE, MessageView.AVATAR_SIZE);
        avatar = GraphicUtils.createCircleBitmap(avatar);

        ArrayList<Message> result = new ArrayList<>();
        result.add(new Message(false, "Xin chào!", avatar));
        result.add(new Message(true, "Chào bạn.", null));
        result.add(new Message(false, "Bạn tên là gì? Bạn là sinh viên trường nào?", avatar));
        result.add(new Message(true, "Mình tên Mụp, mình đến từ KHTN, còn bạn?", null));
        result.add(new Message(false, "Mình tên Quậy, ủa vậy tụi mình chung trường rồi đó.", null));
        result.add(new Message(false, "Hihi, có duyên ghê ta.", null));
        result.add(new Message(false, "Mà bạn khóa mấy vậy? Mình khóa 12", avatar));
        result.add(new Message(true, "Hihi, em khóa 13. Anh học ngành gì á?", null));
        result.add(new Message(true, "Anh sắp thi chưa?", null));
        result.add(new Message(false, "Anh học IT nà em, cũng sắp rồi em à, còn em học ngành gì?", avatar));
        result.add(new Message(true, "Ý! Em cũng IT nè, hời duyên quá dạ.", null));
        result.add(new Message(false, "Kaka, rất vui được biết em nha.", avatar));
        result.add(new Message(true, "Hehe, em cũng vậy, rất vui được biết anh.", null));

        return result;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(new MessageView(mContext));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageView v = (MessageView) holder.itemView;
        v.setMessage(mData.get(position));
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        ItemHolder(View itemView) {
            super(itemView);
        }
    }

}
