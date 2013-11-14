package tk.tnoodle.tnt;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tk.tnoodle.tnt.util.TimeUtil;
import tk.tnoodle.tnt.util.ViewUtil;

public class SessionActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.session, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private long startTime = 0;

        private Resources res;

        // Views
        private View rootView;
        private TextView timerView;
        private Button startButton;

        private Handler timerHandler = new Handler();
        private Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                timerView.setText(TimeUtil.getString(PlaceholderFragment.this, millis));
                timerHandler.postDelayed(this, 10);
            }
        };

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            res = getResources();
            rootView = inflater.inflate(R.layout.fragment_session, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            timerView = (TextView) rootView.findViewById(R.id.timer);
            timerView.setText(TimeUtil.getString(PlaceholderFragment.this, 0));
            Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Digiface Regular.ttf");
            timerView.setTypeface(face);
            startButton = (Button) rootView.findViewById(R.id.start_button);
            startButton.setOnTouchListener(getStartButtonOnTouchListener());
            return rootView;
        }

        private View.OnTouchListener getStartButtonOnTouchListener() {
            return new View.OnTouchListener() {
                private boolean isCancelled;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            timerView.setText(TimeUtil.getString(PlaceholderFragment.this, 0));
                            timerView.setTextColor(res.getColor(R.color.green));
                            startButton.setBackgroundColor(res.getColor(R.color.green));
                            isCancelled = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            // For some reason, ACTION_CANCEL doesn't work :-(
                            if (!ViewUtil.isEventInView(v, event)) {
                                timerView.setTextColor(res.getColor(R.color.black));
                                startButton.setBackgroundColor(res.getColor(R.color.black));
                                isCancelled = true;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            timerView.setTextColor(res.getColor(R.color.black));
                            startButton.setBackgroundColor(res.getColor(R.color.black));
                            if (isCancelled) {
                                break;
                            }
                            startTime = System.currentTimeMillis();
                            timerHandler.postDelayed(timerRunnable, 0);
                            startButton.setVisibility(View.INVISIBLE);
                            rootView.setOnTouchListener(getRootViewOnTouchListener());
                            break;
                    }
                    return true;
                }
            };
        }

        private View.OnTouchListener getRootViewOnTouchListener() {
            return new View.OnTouchListener() {
                private boolean isCancelled;
                private float startX;
                private float startY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            rootView.setBackgroundColor(res.getColor(R.color.lt_gray));
                            isCancelled = false;
                            startX = event.getRawX();
                            startY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (ViewUtil.shouldCancel(res, event, startX, startY)) {
                                rootView.setBackgroundColor(res.getColor(R.color.white));
                                isCancelled = true;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            rootView.setBackgroundColor(res.getColor(R.color.white));
                            if (isCancelled) {
                                break;
                            }
                            timerHandler.removeCallbacks(timerRunnable);
                            startButton.setVisibility(View.VISIBLE);
                            rootView.setOnTouchListener(null);
                            break;
                    }
                    return true;
                }
            };
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((SessionActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
