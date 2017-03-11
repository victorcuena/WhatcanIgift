package es.victorcuena.queleregalo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.fragments.DiaryFragment;
import es.victorcuena.queleregalo.fragments.FindFragment;
import es.victorcuena.queleregalo.fragments.NewsFragment;

import static es.victorcuena.queleregalo.fragments.DiaryFragment.DIARY_FRAGMENT_TAG;
import static es.victorcuena.queleregalo.fragments.FindFragment.FIND_FRAGMENT_TAG;
import static es.victorcuena.queleregalo.fragments.NewsFragment.NEWS_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String FRAGMENT_DISPLAYED = "fragDisplayed";
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Fragment currentFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the views
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //NavigationView management
        navigationView.setNavigationItemSelectedListener(this);

        //Displaying the initial fragment

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_DISPLAYED);

        } else {
            //
            if (getIntent().getExtras() != null)
                onNavigationItemSelected(navigationView.getMenu().findItem(getIntent().getExtras().getInt("initial")));
            else
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_news));


        }

        //To
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!sharedPreferences.getBoolean("alreadyShowed",false)){

            drawer.openDrawer(GravityCompat.START);
            createNoteDialog().show();
            editor.putBoolean("alreadyShowed",true);
            editor.apply();

        }

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;


        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        Intent i;
        String tag = "";

        currentFragment = null;
        //Changing the active fragment or init the activity
        switch (item.getItemId()) {
            case R.id.nav_news:
                tag = NEWS_FRAGMENT_TAG;
                currentFragment = new NewsFragment();
                break;
            case R.id.nav_search:
                tag = FIND_FRAGMENT_TAG;
                currentFragment = new FindFragment();
                break;
            case R.id.nav_diary:
                tag = DIARY_FRAGMENT_TAG;
                currentFragment = new DiaryFragment();
                break;
            case R.id.nav_suggest:

                i = new Intent();
                i.setClass(this, SuggestActivity.class);
                startActivity(i);

                break;
            case R.id.nav_contact:

                i = new Intent();
                i.setClass(this, ContactActivity.class);
                startActivity(i);

                break;

        }


        //replacing the main fragment
        if (currentFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, currentFragment, tag);
            ft.commit();

        }

        //Close the drawer and check the item selected
        drawer.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(item.getItemId());


        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        getSupportFragmentManager().putFragment(outState, FRAGMENT_DISPLAYED, currentFragment);
    }


    public AlertDialog createNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.note_title))
                .setMessage(getString(R.string.note_text))
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });


        return builder.create();
    }

}

