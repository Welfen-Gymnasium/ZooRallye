package onl.deepspace.zoorallye.helper;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewOverlay;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;

/**
 * Created by Sese on 17.03.2016.
 *
 * Helper class for adding Lianas to any {@link View}
 **/
@SuppressWarnings("unused")
public final class Liana {

    /**
     * Method to add a Liana to the specified view
     * @param view The view to add the Liana to
     */
    public static void addLiana(final View view){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) { //Overlay API level 18
            final ViewOverlay overlay = view.getOverlay();

            view.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    try {
                        ArrayList<Drawable> topLianas = new ArrayList<>();
                        ArrayList<Drawable> rightLianas = new ArrayList<>();
                        ArrayList<Drawable> leftLianas = new ArrayList<>();
                        Drawable transverseLiane = ResourcesCompat
                                .getDrawable(view.getResources(), R.drawable.liana_transverse, null);

                        int transverseSize = view.getWidth() < view.getHeight() ? (int) (view.getWidth() / 2.8) : (int) (view.getHeight() / 2.3);
                        if (transverseLiane != null) transverseLiane.setBounds(0, 0, transverseSize, transverseSize);
                        overlay.add(transverseLiane);

                        int topTimes = view.getWidth() / 750;
                        int topWidth = view.getWidth() / topTimes;

                        int rlTimes = view.getHeight() / 700;
                        int rlHeight = view.getHeight() / rlTimes;

                        for (int i = 0; i < rlHeight; i++) {
                            leftLianas.add(ResourcesCompat.getDrawable(view.getResources(), R.drawable.liana_left, null));
                            leftLianas.get(i).setBounds(0, rlHeight * i, 70, rlHeight * (i + 1));

                            rightLianas.add(ResourcesCompat.getDrawable(view.getResources(), R.drawable.liana_right, null));
                            rightLianas.get(i).setBounds(view.getWidth() - 70, rlHeight * i, view.getWidth(), rlHeight * (i + 1));

                            overlay.add(leftLianas.get(i));
                            overlay.add(rightLianas.get(i));
                        }

                        for (int i = 0; i < topTimes; i++) {
                            topLianas.add(ResourcesCompat.getDrawable(view.getResources(), R.drawable.liana_top, null));
                            topLianas.get(i).setBounds(topWidth * i, 0, topWidth * (i + 1), 110);
                            overlay.add(topLianas.get(i));
                        }

                    } catch (ArithmeticException e){
                        //could not get screen size, divide by zero!
                        Log.e(Const.LOGTAG, e.getMessage());
                        overlay.clear();
                    }
                }
            });

            //redraw view
            view.invalidate();
        }
    }
}


