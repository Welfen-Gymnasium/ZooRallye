package onl.deepspace.zoorallye.helper;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;

/**
 * Created by Sese on 17.03.2016.
 **/

public class Liane {

    public static void addLiane(View uview){

        final View view = uview;
        final ViewOverlay overlay = view.getOverlay();

        view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Drawable> topLianes = new ArrayList<>();
                ArrayList<Drawable> rightLianes = new ArrayList<>();
                ArrayList<Drawable> leftLianes = new ArrayList<>();
                Drawable transverseLiane = view.getResources().getDrawable(R.drawable.transverse_liane);

                transverseLiane.setBounds(0, 0, 400, 400);
                overlay.add(transverseLiane);

                int topTimes = view.getWidth() / 750;
                int topWidth = view.getWidth() / topTimes;

                int rlTimes = view.getHeight() / 700;
                int rlHeight = view.getHeight() / rlTimes;

                for(int i = 0; i < rlHeight; i++){
                    leftLianes.add(view.getResources().getDrawable(R.drawable.left_liane));
                    leftLianes.get(i).setBounds(0, rlHeight * i, 70, rlHeight * (i + 1));

                    rightLianes.add(view.getResources().getDrawable(R.drawable.right_liane));
                    rightLianes.get(i).setBounds(view.getWidth() - 70, rlHeight * i, view.getWidth(), rlHeight * (i + 1));

                    overlay.add(leftLianes.get(i));
                    overlay.add(rightLianes.get(i));
                }

                for(int i = 0; i < topTimes; i++){
                    topLianes.add(view.getResources().getDrawable(R.drawable.top_liane));
                    topLianes.get(i).setBounds(topWidth * i, 0, topWidth * (i + 1), 110);
                    overlay.add(topLianes.get(i));
                }

            }
        });

    }
}


