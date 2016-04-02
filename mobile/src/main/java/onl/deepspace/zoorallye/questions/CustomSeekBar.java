package onl.deepspace.zoorallye.questions;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Dennis on 31.03.2016.
 *
 * Custom seekbar implementing functionality like floating point numbers, step size and minimum value
 */
// TODO-dennis: 01.04.2016 Add a label pin to CustomSeekBar
public class CustomSeekBar extends SeekBar {

    private float mMin = Float.POSITIVE_INFINITY;
    private float mMax = Float.POSITIVE_INFINITY;
    private float mStepSize = Float.POSITIVE_INFINITY;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setFloatRange(float min, float max, float stepSize) {
        mMin = min;
        mMax = max;
        mStepSize = stepSize;
        calculateDimensions();
    }

    @SuppressWarnings("unused")
    public float getFloatMin() {
        return mMin;
    }

    @SuppressWarnings("unused")
    public float getFloatMax() {
        return mMax;
    }

    @SuppressWarnings("unused")
    public float getFloatStepSize() {
        return mStepSize;
    }

    @SuppressWarnings("unused")
    public void setFloatProgress(float progress) {
        // Check if progress is between min and max;
        if(progress < mMin || progress > mMax)
            throw new IllegalStateException("progress has to be between min and max");
        // Check if you can with an natural number from
        float delta = progress - mMin;
        if(isReachable(delta, mStepSize))
            throw new IllegalStateException("Make sure you can get with an natural number from min to progress");

        int steps = (int) (delta / mStepSize);
        setProgress(steps);
    }

    public float getFloatProgress() {
        int normalProgress = getProgress();
        int decimals = getDecimals(mStepSize);
        return round(mMin + mStepSize * normalProgress, decimals);
    }

    private void calculateDimensions() {
        float delta = mMax - mMin;

        // Check if max is greater than min
        if(delta == 0) throw new IllegalStateException("Min and max are equals. Max has to be greater than min");
        else if(delta < 0) throw new IllegalStateException("Min is greater than max. Max has to be greater than min");

        // Check if you can get with an natural number from min to max
        if (isReachable(delta, mStepSize)) throw new IllegalStateException("Values min, max and step doesn't work " +
                "together make sure (max - min) % step == 0");

        // Normalise step size to 1
        int mSteps = (int) (delta / mStepSize);

        setMax(mSteps);
    }

    private int getDecimals(float number) {
        String text = Float.toString(number);
        int integerPlaces = text.indexOf('.');
        return text.length() - integerPlaces - 1;
    }

    private float round(float number, int decimalPlaces) {
        float roundFactor = (float) Math.pow(10, decimalPlaces);
        return Math.round(number * roundFactor) / roundFactor;
    }

    private boolean isReachable(float delta, float stepSize) {
        boolean isReachable = false;

        float i = 0;
        while (i < delta) {
            if(i == delta) isReachable = true;
            i += stepSize;
        }

        return isReachable;
    }
}
