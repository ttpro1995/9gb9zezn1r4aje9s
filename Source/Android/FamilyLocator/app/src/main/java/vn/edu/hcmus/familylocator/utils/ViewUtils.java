package vn.edu.hcmus.familylocator.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.core.MainApplication;

/**
 * Created by quangcat on 5/1/17.
 */

public class ViewUtils {

    private static final float density;
    private static final float scaledDensity;
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;

    static {
        DisplayMetrics dm = MainApplication.getAppContext().getResources().getDisplayMetrics();
        density = dm.density;
        scaledDensity = dm.scaledDensity;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
    }

    public static int dp(float value) {
        return (int) Math.ceil(density * value);
    }

    public static int sp(float value) {
        return (int) Math.ceil(scaledDensity * value);
    }

    public static int getTextWidth(TextPaint paint, String text) {
        if (TextUtils.isEmpty(text) || paint == null) {
            return 0;
        }
        return (int) paint.measureText(text);
    }

    private static Rect rect;

    public static int getTextHeight(TextPaint paint, String text) {
        if (paint == null || text == null || TextUtils.isEmpty(text)) {
            return 0;
        }
        if (rect == null) {
            rect = new Rect();
        }
        paint.getTextBounds(text, 0, 1, rect);
        return rect.height();
    }

    // Progress Dialog

    private static ProgressDialog dlg;

    public static void showProgressDialog(Context context) {
        dlg = new ProgressDialog(context, R.style.Theme_AppCompat_Light_Dialog);
        dlg.setMessage("Đang xử lý...");
        dlg.setCancelable(false);
        dlg.show();
    }

    public static void closeProgressDialog() {
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
    }

    // --------------------------------------

    public static Dialog createDialog(Context context, @LayoutRes int layout, boolean cancelable) {
        Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setContentView(layout);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public static Dialog createConfirmDialog(Context context, String message, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener listener, boolean cancelable) {
        Dialog dialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog)
                .setMessage(message)
                .setPositiveButton(positiveTitle, listener)
                .setNegativeButton(negativeTitle, listener)
                .setCancelable(cancelable)
                .create();
        return dialog;
    }

    public static Dialog createConfirmDialog(Context context, String message, DialogInterface.OnClickListener listener, boolean cancelable) {
        return createConfirmDialog(context, message, "Đồng ý", "Hủy bỏ", listener, cancelable);
    }

    public static void setupActionBar(AppCompatActivity activity, String title, boolean showButton, @DrawableRes int resID) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showButton);
            actionBar.setHomeAsUpIndicator(resID);
            actionBar.setTitle(title);
        }
    }

}
