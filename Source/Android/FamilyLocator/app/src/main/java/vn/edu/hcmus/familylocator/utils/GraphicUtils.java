package vn.edu.hcmus.familylocator.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by Quang Cat on 3/13/2017.
 */

public class GraphicUtils {

    public static Bitmap createCircleBitmap(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        output.eraseColor(0x00000000);

        Canvas canvas = new Canvas(output);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
