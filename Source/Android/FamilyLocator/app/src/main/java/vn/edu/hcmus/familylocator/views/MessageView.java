package vn.edu.hcmus.familylocator.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import vn.edu.hcmus.familylocator.utils.ViewUtils;
import vn.edu.hcmus.familylocator.models.Message;

/**
 * Created by quangcat on 4/23/17.
 */

public class MessageView extends View {

    private static final int BUBBLE_PADDING = ViewUtils.dp(10);
    private static final int BUBBLE_RADIUS = ViewUtils.dp(20);
    private static final int BUBBLE_MARGIN_RIGHT = ViewUtils.dp(25);
    private static final int BUBBLE_MINE_COLOR = 0xff1788fb;
    private static final int BUBBLE_COLOR = 0xffe9eaeb;

    private static final int TEXT_SIZE = ViewUtils.sp(15);
    private static final int TEXT_MINE_COLOR = 0xfffeffff;
    private static final int TEXT_COLOR = 0xff010102;

    private static final int AVATAR_MARGIN_LEFT_RIGHT = ViewUtils.dp(10);
    public static final int AVATAR_SIZE;
    private static final int CONTENT_MAX_WIDTH;

    static {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TEXT_SIZE);

        StaticLayout staticLayout;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            staticLayout = StaticLayout.Builder.obtain("", 0, 0, textPaint, 0)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setIncludePad(false)
                    .setLineSpacing(0, 1)
                    .build();
        } else {
            staticLayout = new StaticLayout("", textPaint, 0, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        }

        AVATAR_SIZE = staticLayout.getHeight() + BUBBLE_PADDING * 2;
        CONTENT_MAX_WIDTH = (int) ((ViewUtils.SCREEN_WIDTH - AVATAR_SIZE - AVATAR_MARGIN_LEFT_RIGHT * 2) * 0.9) - BUBBLE_PADDING * 2;
    }

    private Message mMessage;
    private StaticLayout mContentLayout;
    private Paint mBubblePaint;
    private RectF mBubbleRect;

    public MessageView(Context context) {
        super(context);
    }

    public void setMessage(Message msg) {
        mMessage = msg;
        mBubblePaint = new Paint();
        mBubblePaint.setAntiAlias(true);
        mBubblePaint.setColor(mMessage.mine ? BUBBLE_MINE_COLOR : BUBBLE_COLOR);

        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setColor(mMessage.mine ? TEXT_MINE_COLOR : TEXT_COLOR);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContentLayout = StaticLayout.Builder.obtain(mMessage.content, 0, mMessage.content.length(), mTextPaint, CONTENT_MAX_WIDTH)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setIncludePad(false)
                    .setLineSpacing(0, 1)
                    .build();
        } else {
            mContentLayout = new StaticLayout(mMessage.content, mTextPaint, CONTENT_MAX_WIDTH, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        }

        int top = 0;
        int bottom = BUBBLE_PADDING * 2 + mContentLayout.getHeight();
        int left, right;
        if (mMessage.mine) {
            right = ViewUtils.SCREEN_WIDTH - BUBBLE_MARGIN_RIGHT;
            left = (int) (right - BUBBLE_PADDING * 2 - getContentWidth());
        } else {
            left = AVATAR_MARGIN_LEFT_RIGHT * 2 + AVATAR_SIZE;
            right = (int) (left + BUBBLE_PADDING * 2 + getContentWidth());
        }
        mBubbleRect = new RectF(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(ViewUtils.SCREEN_WIDTH, mContentLayout.getHeight() + BUBBLE_PADDING * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mMessage.mine && mMessage.avatar != null) {
            canvas.drawBitmap(mMessage.avatar, AVATAR_MARGIN_LEFT_RIGHT, 0, null);
        }
        canvas.drawRoundRect(mBubbleRect, BUBBLE_RADIUS, BUBBLE_RADIUS, mBubblePaint);
        canvas.save();
        canvas.translate(mBubbleRect.left + BUBBLE_PADDING, mBubbleRect.top + BUBBLE_PADDING);
        mContentLayout.draw(canvas);
        canvas.restore();
    }

    private float getContentWidth() {
        if (mContentLayout.getLineCount() == 0) {
            return 0;
        }
        float width = mContentLayout.getLineWidth(0);
        for (int i = 1; i < mContentLayout.getLineCount(); i++) {
            if (mContentLayout.getLineWidth(i) > width) {
                width = mContentLayout.getLineWidth(i);
            }
        }
        return width;
    }

}
