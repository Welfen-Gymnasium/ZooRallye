package onl.deepspace.zoorallye;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import onl.deepspace.zoorallye.fragments.MapFragment;
import onl.deepspace.zoorallye.fragments.StatisticsFragment;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.activities.AppCompatAchievementActivity;

public class MainActivity extends AppCompatAchievementActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Tools.ActionBarToggler actionBarToggler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Opens a specific fragment if defined
        int fragment = getIntent().getIntExtra(Const.NAV_FRAGMENT, 0);
        if (fragment != 0) {
            openFragment(getFragmentByID(fragment));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarToggler = new Tools.ActionBarToggler(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        assert drawer != null;
        //noinspection deprecation
        drawer.setDrawerListener(actionBarToggler);
        actionBarToggler.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_rally) {
            final Intent intent = new Intent(this, RallyActivity.class);
            actionBarToggler.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            });
        } else if(id == R.id.nav_statistics) {
            final Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra(Const.QUESTION_ID, "1");
            intent.putExtra(Const.QUESTION, "Ich bin eine Frage, stimmt das?");
            intent.putExtra(Const.QUESTION_TYPE, Const.QUESTION_TYPE_TRUE_FALSE);
            intent.putExtra(Const.QUESTION_IMAGE, "animal_alpensteinbock");
            Bundle bundle = new Bundle();
            bundle.putBoolean(Const.QUESTIONS_ANSWER, true);
            intent.putExtra(Const.QUESTION_BUNDLE, bundle);
            startActivity(intent);
        } else {
            final Fragment fragment = getFragmentByID(item.getItemId());
            actionBarToggler.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    openFragment(fragment);
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void openFragment(Fragment fragment) {
        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    private Fragment getFragmentByID(int id) {
        switch (id) {
            case R.id.nav_map: return new MapFragment();
            case R.id.nav_statistics: return new StatisticsFragment();
            default: return null;
        }
    }

}
