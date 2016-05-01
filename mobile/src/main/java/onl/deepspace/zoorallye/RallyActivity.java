package onl.deepspace.zoorallye;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import onl.deepspace.zoorallye.fragments.BeaconsOverlayFragment;
import onl.deepspace.zoorallye.fragments.InfoFragment;
import onl.deepspace.zoorallye.fragments.MapFragment;
import onl.deepspace.zoorallye.fragments.StartRallyFragment;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Exceptions;
import onl.deepspace.zoorallye.helper.Liana;
import onl.deepspace.zoorallye.helper.Question;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.activities.AppCompatAchievementActivity;
import onl.deepspace.zoorallye.helper.interfaces.BeaconListener;

public class RallyActivity extends AppCompatAchievementActivity implements
        NavigationView.OnNavigationItemSelectedListener, StartRallyFragment.OnStartRallyListener,
        MapFragment.OverlayShow, BeaconListener, BeaconsOverlayFragment.BeaconOverlayListener {

    private static final String ARG_RALLY_ACTIVE = "rallyActive";
    private static final String ARG_QUESTIONS = "questions";
    private static final String ARG_TOTAL_SCORE = "totalScore";
    private static final String ARG_QUESTIONS_ANSWERED = "questionsAnswered";

    public static final int SHOW_QUESTION = 384;

    private Fragment mBeaconOverlayFragment;

    Tools.ActionBarToggler toggle;
    private boolean mRallyActive;
    private ArrayList<Question> mQuestions;
    private int mTotalScore;
    private int mQuestionsAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rally);
        //GoogleAnalytics
        // AnalyticsTrackers.initialize(this);

        //Lianas
        Liana.addLiana((findViewById(R.id.rally_content)));

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
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mRallyActive = intent.getBooleanExtra(ARG_RALLY_ACTIVE, false);
            mQuestions = intent.getParcelableArrayListExtra(ARG_QUESTIONS);
            mTotalScore = intent.getIntExtra(ARG_TOTAL_SCORE, 0);
            mQuestionsAnswered = intent.getIntExtra(ARG_QUESTIONS_ANSWERED, 0);
        } else {
            mRallyActive = savedInstanceState.getBoolean(ARG_RALLY_ACTIVE, false);
            mQuestions = savedInstanceState.getParcelableArrayList(ARG_QUESTIONS);
            mTotalScore = savedInstanceState.getInt(ARG_TOTAL_SCORE, 0);
            mQuestionsAnswered = savedInstanceState.getInt(ARG_QUESTIONS_ANSWERED, 0);
        }

        // Setup Tab Layout
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.rally_viewpager);
        assert viewPager != null;
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        if (tab1 != null) tab1.setIcon(mRallyActive ? R.drawable.ic_map : R.drawable.ic_play_arrow);
        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        if (tab2 != null) tab2.setIcon(mRallyActive ? R.drawable.ic_info : R.drawable.ic_map);
    }

    @Override
    public void onStartRally(ArrayList<Question> questions) {
        mRallyActive = true;
        mQuestions = questions;

        Intent intent = new Intent(this, RallyActivity.class);
        intent.putExtra(ARG_RALLY_ACTIVE, mRallyActive);
        intent.putExtra(ARG_QUESTIONS, mQuestions);

        finish();
        startActivity(intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRallyActive) outState.putBoolean(ARG_RALLY_ACTIVE, true);
        if (mQuestions != null) outState.putParcelableArrayList(ARG_QUESTIONS, mQuestions);
        outState.putInt(ARG_TOTAL_SCORE, mTotalScore);
        outState.putInt(ARG_QUESTIONS_ANSWERED, mQuestionsAnswered);
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

    @Override
    public void onBeaconClick(Location location) {
        Log.d(Const.LOGTAG, "Beacon click " + location.getLongitude() + " " + location.getLatitude());
        ArrayList<JSONObject> nearBeacons = Tools.getEnclosures(this, "4P1shyVmM4", location, 1); //near beacons in 1m range

        if (nearBeacons.size() == 1 && mRallyActive) {
            showBeaconOverlay(nearBeacons.get(0), getQuestionsForBeacon(nearBeacons.get(0)));
        } else {
            Log.e(Const.LOGTAG, "There is no beacon defined for location " + String.valueOf(location));
        }
    }

    @Override
    public void onOverlayShow(JSONObject beacon, Question questions) {
        if (mRallyActive) showBeaconOverlay(beacon, getQuestionsForBeacon(beacon));
    }

    /**
     * Show an overlay on on tap for several beacons,
     * such as enclosures, animal houses, kiosks, etc.
     *
     * @param beacon    The beacon object with information about the beacon
     * @param questions A Bundle of questions to display in the overlay with information to
     *                  invoke the {@link onl.deepspace.zoorallye.QuestionActivity}
     */
    private void showBeaconOverlay(JSONObject beacon, ArrayList<Question> questions) {
        if (questions != null) {
            try {
                JSONArray animals = beacon.getJSONArray(Const.ZOO_ANIMALS);
                ArrayList<String> animalList = Tools.jsonArrayToArrayList(animals);

                mBeaconOverlayFragment = BeaconsOverlayFragment.newInstance(beacon.getString("type"), animalList, questions);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.drawer_layout_rally, mBeaconOverlayFragment);
                transaction.commit();
            } catch (JSONException e) {
                Log.e(Const.LOGTAG, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(Const.LOGTAG, "No questions for this beacon.");
        }

    }

    @Override
    public void hideBeaconOverlay() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(mBeaconOverlayFragment);
        transaction.commit();
    }

    @Override
    public void showQuestion(Question question) {
        if (question.getState() == Question.STATE_UNKNOWN) {
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra(Const.QUESTION, question);
            startActivityForResult(intent, SHOW_QUESTION);
            hideBeaconOverlay();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SHOW_QUESTION) {
            Question answeredQuestion = data.getParcelableExtra(Const.QUESTION);
            int score = data.getIntExtra(Const.QUESTION_SCORE, 0);
            mTotalScore += score;
            updatePlayGames();

            String type = answeredQuestion.getType();
            String id = answeredQuestion.getId();
            int state = answeredQuestion.getState();
            int index = getIndexForQuestion(type, id);
            if (index >= 0) {
                mQuestions.get(index).setState(state);
            }
        }
    }

    private void updatePlayGames() {
        try {
            submitLeaderBoardScore(
                    getString(R.string.leaderboard_total_question_points_earned), mTotalScore);
            if (mQuestionsAnswered >= 1)
                unlockAchievement(getString(R.string.achievement_answered_first_question));
            if (mQuestionsAnswered >= 10)
                unlockAchievement(getString(R.string.achievement_answered_first_question));
            if (mQuestionsAnswered >= 25)
                unlockAchievement(getString(R.string.achievement_answered_first_question));
            if (mQuestionsAnswered >= 50)
                unlockAchievement(getString(R.string.achievement_answered_first_question));
        } catch (Exceptions.GooglePlayUnconnectedException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            signIn();
        }
    }

    private int getIndexForQuestion(String type, String id) {
        for (int i=0; i<mQuestions.size(); i++) {
            Question question = mQuestions.get(i);
            if (type.equals(question.getType()) && id.equals(question.getId())) {
                return i;
            }
        }
        return -1;
    }

    private ArrayList<Question> getQuestionsForBeacon(JSONObject beacon) {
        ArrayList<Question> returnList = null;
        try {
            for (int i = 0; i < mQuestions.size(); i++) {
                String questionsEnclosure = mQuestions.get(i).getValue().getString("enclosure");
                JSONArray beaconEnclosureList = beacon.getJSONArray("animals");

                for (int h = 0; h < beaconEnclosureList.length(); h++) {
                    if (beaconEnclosureList.getString(h).equals(questionsEnclosure)) {
                        if(returnList == null){
                            returnList = new ArrayList<>(); //need this to parse null if no question set
                        }
                        returnList.add(mQuestions.get(i));
                    }
                }

            }
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }


        return returnList;

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
                            new StartRallyFragment();
                case 1:
                    return mRallyActive ? new InfoFragment() : new MapFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
