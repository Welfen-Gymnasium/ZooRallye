package onl.deepspace.zoorallye.helper;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
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
public final class Liane {

    /**
     * Method to add a Liana to the specified view
     * @param view The view to add the Liana to
     */
    public static void addLiane(final View view){
        // TODO-sese: 02.04.2016 Resolve problems with divide by zero, then re-enable Lianas on all screens
        final ViewOverlay overlay = view.getOverlay();

        view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Drawable> topLianas = new ArrayList<>();
                ArrayList<Drawable> rightLianas = new ArrayList<>();
                ArrayList<Drawable> leftLianas = new ArrayList<>();
                Drawable transverseLiane = ResourcesCompat
                        .getDrawable(view.getResources(), R.drawable.transverse_liane, null);
                if(transverseLiane != null) transverseLiane.setBounds(0, 0, 400, 400);
                overlay.add(transverseLiane);

                int topTimes = view.getWidth() / 750;
                int topWidth = view.getWidth() / topTimes;

                int rlTimes = view.getHeight() / 700;
                int rlHeight = view.getHeight() / rlTimes;

                for(int i = 0; i < rlHeight; i++){
                    leftLianas.add(ResourcesCompat.getDrawable(view.getResources(), R.drawable.left_liane, null));
                    leftLianas.get(i).setBounds(0, rlHeight * i, 70, rlHeight * (i + 1));

                    rightLianas.add(ResourcesCompat.getDrawable(view.getResources(), R.drawable.right_liane, null));
                    rightLianas.get(i).setBounds(view.getWidth() - 70, rlHeight * i, view.getWidth(), rlHeight * (i + 1));

                    overlay.add(leftLianas.get(i));
                    overlay.add(rightLianas.get(i));
                }

                for(int i = 0; i < topTimes; i++){
                    topLianas.add(ResourcesCompat.getDrawable(view.getResources(), R.drawable.top_liane, null));
                    topLianas.get(i).setBounds(topWidth * i, 0, topWidth * (i + 1), 110);
                    overlay.add(topLianas.get(i));
                }

            }
        });

    }
}


