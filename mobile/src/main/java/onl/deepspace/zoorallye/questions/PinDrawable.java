package onl.deepspace.zoorallye.questions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by Dennis on 01.04.2016.
 *
 */
@SuppressWarnings("unused")
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
    public void setColorFilter(int color, @NonNull PorterDuff.Mode mode) {
        super.setColorFilter(color, mode);
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public int getOpacity() {
        return 0;
    }
}
