package onl.deepspace.zoorallye;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import onl.deepspace.zoorallye.fragments.GroupFragment;
import onl.deepspace.zoorallye.fragments.MapFragment;
import onl.deepspace.zoorallye.fragments.StartRallyFragment;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Liana;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.activities.AppCompatAchievementActivity;

public class RallyActivity extends AppCompatAchievementActivity implements
        NavigationView.OnNavigationItemSelectedListener, StartRallyFragment.OnStartRallyListener {

    private static final String ARG_RALLY_ACTIVE = "rallyActive";
    Tools.ActionBarToggler toggle;
    private boolean mRallyActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rally);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rally);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup NavigationView/NavigationDrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_rally);
        toggle = new Tools.ActionBarToggler(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_rally);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        if(savedInstanceState == null) savedInstanceState = new Bundle();
        mRallyActive = savedInstanceState.getBoolean(ARG_RALLY_ACTIVE, false);

        // Setup Tab Layout
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.rally_viewpager);
        assert viewPager != null;
        viewPager.setAdapter(adapter);

        //Lianas
        Liana.addLiana((findViewById(R.id.rally_lianas)));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        if(tab1 != null) tab1.setIcon(mRallyActive ? R.drawable.ic_map : R.drawable.ic_play_arrow);
        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        if(tab2 != null) tab2.setIcon(mRallyActive ? R.drawable.ic_people : R.drawable.ic_map);
        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        if(tab3 != null) tab3.setIcon(R.drawable.ic_info);
    }

    @Override
    public void onStartRally(Bundle bundle) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRallyActive) outState.putBoolean(ARG_RALLY_ACTIVE, true);
    }

    @Override
    public void onMessageReceived(String messageString) {
        super.onMessageReceived(messageString);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id != R.id.nav_rally) {
            final Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Const.NAV_FRAGMENT, id);
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_rally);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mRallyActive ? new MapFragment() :
                            StartRallyFragment.newInstance();
                case 1:
                    return mRallyActive ? new GroupFragment() : new MapFragment();
                case 2:
                    return new MapFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
