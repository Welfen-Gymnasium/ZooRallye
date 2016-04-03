package onl.deepspace.zoorallye.helper;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import onl.deepspace.zoorallye.MainActivity;
import onl.deepspace.zoorallye.R;

/**
 * Created by Sese on 03.04.2016.
 */
public class Tools {
    public static class ActionBarToggler extends ActionBarDrawerToggle{

        private Runnable runnable;

        public ActionBarToggler (Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes){
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE){
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable){
            this.runnable = runnable;
        }
    }
}
