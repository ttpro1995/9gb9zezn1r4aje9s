package vn.edu.hcmus.familylocator.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class QDrawerLayout extends DrawerLayout {

    private Rect mInterceptRect;

    public QDrawerLayout(Context context) {
        super(context);
    }

    public QDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) Math.ceil(ev.getX());
        int y = (int) Math.ceil(ev.getY());
        if (mInterceptRect != null && mInterceptRect.contains(x, y)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setInterceptRectange(Rect rect) {
        mInterceptRect = rect;
    }

}