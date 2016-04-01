package onl.deepspace.zoorallye.questions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;

import onl.deepspace.zoorallye.R;

/**
 * Created by Dennis on 01.04.2016.
 */
public class PinDrawable extends Drawable {

    float mProgress;
    Context mContext;

    public PinDrawable(Context context) {
        this.mContext = context;
    }

    public PinDrawable(Context context, Color color) {
        this.mContext = context;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public float getProgress() {
        return mProgress;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public void setColorFilter(int color, PorterDuff.Mode mode) {
        super.setColorFilter(color, mode);
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public int getOpacity() {
        return 0;
    }
}
