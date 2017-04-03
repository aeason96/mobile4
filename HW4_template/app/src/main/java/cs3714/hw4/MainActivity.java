package cs3714.hw4;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cs3714.hw4.constants.Constants;
import cs3714.hw4.fragments.HomeScreenFragment;
import cs3714.hw4.fragments.MyStepsFragment;
import cs3714.hw4.fragments.TaskFragment;
import cs3714.hw4.fragments.TeamFragment;
import cs3714.hw4.fragments.TeamsRankFragment;
import cs3714.hw4.interfaces.ActivityInteraction;
import cs3714.hw4.interfaces.HomeScreenInteraction;
import cs3714.hw4.interfaces.RetainedFragmentInteraction;

public class MainActivity extends AppCompatActivity implements HomeScreenInteraction,ActivityInteraction {


    private Fragment homeScreenFragment,taskFragment,myStepsFragment, teamFragment;


    private SharedPreferences prefs;
    private FragmentManager fragmentManager;
    private RetainedFragmentInteraction retainedFragmentInteraction;

    public static final int READ_TIMEOUT_MS = 20000;
    public static final int CONNECT_TIMEOUT_MS = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Log.d("hw3","status:"+prefs.getString("status",""));

        if(!prefs.getString("status","").equals(Constants.STATUS_RELOGIN)
                && prefs.getString("status","").length()>2 ){
            Log.d("hw3","logged in");
        }
        else{
            Intent intent = new Intent(this, LoginScreen.class);
            this.startActivity(intent);
            finish();
        }
        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);

        if (taskFragment == null) {

            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {
            homeScreenFragment = new HomeScreenFragment();
            // Set dashboard fragment to be the default fragment shown
            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(HomeScreenFragment.TAG_HOME_FRAGMENT);
            fragmentManager.beginTransaction().replace(R.id.frame, homeScreenFragment ).commit();
        } else {
            // Get referencecs to the fragments if they existed, null otherwise
//            teamStepsFragment = fragmentManager.findFragmentByTag(TeamStepsFragment.TAG_TEAM_STEPS_FRAGMENT);
//            teamRankFragment = fragmentManager.findFragmentByTag(TeamsRankFragment.TAG_TEAM_RANK_FRAGMENT);
            myStepsFragment = fragmentManager.findFragmentByTag(MyStepsFragment.TAG_MY_STEPS_FRAGMENT);
//            homeScreenFragment = fragmentManager.findFragmentByTag(HomeScreenFragment.TAG_HOME_FRAGMENT);
            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(TeamFragment.TAG_TEAM_FRAGMENT);
            teamFragment = fragmentManager.findFragmentByTag(TeamFragment.TAG_TEAM_FRAGMENT);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((RetainedFragmentInteraction) taskFragment).startBackgroundServiceNeeded();
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

        if (id == R.id.action_settings) {

        }
        else if (id == R.id.action_message) {

        }

        else if (id == R.id.action_logout) {
            // Log the user out
            prefs.edit().remove("status").apply();
            Intent intent = new Intent(getBaseContext(), LoginScreen.class);
            startActivity(intent);
finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeFragment(String fragment_name) {


        Fragment fragment;
        Class fragmentClass = null;
        if(fragment_name.equals(TeamFragment.TAG_TEAM_FRAGMENT)){
            fragmentClass = TeamFragment.class;

            Log.d("HW2", "team fragment selected");
        }
        else if(fragment_name.equals(MyStepsFragment.TAG_MY_STEPS_FRAGMENT)){
            fragmentClass = MyStepsFragment.class;

            Log.d("HW2", "team fragment selected");
        }
        else if(fragment_name.equals(TeamsRankFragment.TAG_TEAM_RANK_FRAGMENT)){
            fragmentClass = TeamsRankFragment.class;

            Log.d("HW2", "team rank fragment selected");
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();



                FragmentTransaction ft= fragmentManager.beginTransaction();

                ft.replace(R.id.frame, fragment,
                        ((RetainedFragmentInteraction)taskFragment).getActiveFragmentTag());
                ft.addToBackStack(null);
                ft.commit();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void InitiateLoginActivity() {
        Intent intent = new Intent(getBaseContext(), LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
