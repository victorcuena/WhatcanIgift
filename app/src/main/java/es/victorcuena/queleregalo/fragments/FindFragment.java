package es.victorcuena.queleregalo.fragments;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.activities.MainActivity;
import es.victorcuena.queleregalo.fragments.find.FindAgeFragment;
import es.victorcuena.queleregalo.fragments.find.FindGenderFragment;
import es.victorcuena.queleregalo.fragments.find.FindPriceFragment;
import es.victorcuena.queleregalo.fragments.find.FindResultsFragment;
import es.victorcuena.queleregalo.utils.Utils;


public class FindFragment extends Fragment implements FindGenderFragment.OnGenderEventListener, FindAgeFragment.OnAgeEventListener, FindPriceFragment.OnPriceEventListener, FindResultsFragment.OnFindResultsEventListener {


    public static String FIND_FRAGMENT_TAG = "findFragmentTag";

    private static int SLIDE_DIRECTION_RIGHT = 0;
    private static int SLIDE_DIRECTION_LEFT = 1;


    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private LinearLayout viewpagerIndexs;
    private ImageView background;

    private int genderCode = -1;
    private int ageCode = -1;
    private int priceCode = -1;

    private int currentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar_layout);
        ImageView reloadIcon = (ImageView) view.findViewById(R.id.find_reload_icon);
        ImageView cancelIcon = (ImageView) view.findViewById(R.id.find_cancel_icon);
        background = (ImageView) view.findViewById(R.id.background);
        background.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.morning), PorterDuff.Mode.MULTIPLY);


        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //Toolbar management
        toolbar.setTitle(getString(R.string.results_title));

        //Hamburguer management
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewpagerIndexs = (LinearLayout) view.findViewById(R.id.viewpager_indexs);


        reloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadFragment();
            }
        });
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                ((MainActivity) getActivity()).onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_news));

            }
        });


        //Initial fragment
        changePage(0);

    }


    @Override
    public void onGenderSelected(int genderCode) {

        this.genderCode = genderCode;
        if (ageCode == -1) { //Not selected yet
            changePage(1);
            getView().findViewById(R.id.viewpager_index_2).setBackgroundResource(R.drawable.viewpager_index_selected);
        } else
            changePage(3);
    }

    @Override
    public void onAgeSelected(int ageCode) {

        this.ageCode = ageCode;
        if (priceCode == -1) { //Not selected yet
            changePage(2);
            getView().findViewById(R.id.viewpager_index_3).setBackgroundResource(R.drawable.viewpager_index_selected);
        } else
            changePage(3);
    }

    @Override
    public void onPriceSelected(int priceCode) {

        this.priceCode = priceCode;
        changePage(3);
    }

    private void changePage(int index) {
        //Default left
        changePagewithDirection(index, SLIDE_DIRECTION_LEFT);
    }


    private void changePagewithDirection(int index, int slideDirection) {

        //Setting the backgroundTransition
        int colorFrom = getColorFromPageIndex(currentPage);
        int colorTo = getColorFromPageIndex(index);
        backgroundColorTransition(colorFrom, colorTo);

        //Change the page in the viewpager
        currentPage = index;
        loadFragment(getFragment(index), slideDirection);

        //Check and update the common features
        if (index != 3 && toolbar.getVisibility() != View.GONE) {
            appBarLayout.setVisibility(View.GONE);
        } else {
            appBarLayout.setVisibility(View.VISIBLE);
            viewpagerIndexs.setVisibility(View.GONE);
        }

    }


    public void reloadFragment() {

        //Set default values as the beginning
        changePage(0);
        genderCode = -1;
        ageCode = -1;
        priceCode = -1;

        getView().findViewById(R.id.viewpager_index_2).setBackgroundResource(R.drawable.viewpager_index_non_selected);
        getView().findViewById(R.id.viewpager_index_3).setBackgroundResource(R.drawable.viewpager_index_non_selected);

        viewpagerIndexs.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.GONE);

    }

    private void backgroundColorTransition(int from, int to) {

        Utils.colorTransition(from, to, 2000L, new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int animatedValue = (int) animation.getAnimatedValue();
                background.getBackground().setColorFilter(animatedValue, PorterDuff.Mode.MULTIPLY);

            }
        }).start();

    }

    private int getColorFromPageIndex(int index) {

        switch (index) {
            case 0:
                return ContextCompat.getColor(getContext(), R.color.morning);
            case 1:
                return ContextCompat.getColor(getContext(), R.color.evening);
            case 2:
                return ContextCompat.getColor(getContext(), R.color.night);
            default:
                return ContextCompat.getColor(getContext(), R.color.night);

        }

    }

    private Fragment getFragment(int index) {

        //Fragment still present
        Fragment f = getChildFragmentManager().findFragmentByTag(getFragmentTagByIndex(index));

        if (f != null)
            return f;

        switch (index) {
            case 0:


                return new FindGenderFragment();
            case 1:
                return new FindAgeFragment();
            case 2:
                return new FindPriceFragment();
            case 3:
                return FindResultsFragment.newInstance(genderCode, ageCode, priceCode);
            default:
                return new FindResultsFragment();

        }

    }

    private String getFragmentTagByIndex(int index) {

        switch (index) {
            case 0:
                return FindGenderFragment.GENDER_FRAGMENT_TAG;
            case 1:
                return FindAgeFragment.AGE_FRAGMENT_TAG;
            case 2:
                return FindPriceFragment.PRICE_FRAGMENT_TAG;
            default:
                return FindResultsFragment.RESULTS_FRAGMENT_TAG;

        }

    }

    private void loadFragment(Fragment f, int slideDirection) {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        if (slideDirection == SLIDE_DIRECTION_LEFT)
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        else if (slideDirection == SLIDE_DIRECTION_RIGHT)
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);


        ft.replace(R.id.pager_fragment, f);
        ft.commit();


    }

    public void onChangeFilter(int field) {
        changePagewithDirection(field, SLIDE_DIRECTION_RIGHT);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("gender", genderCode);
        outState.putInt("age", ageCode);
        outState.putInt("price", priceCode);
        outState.putInt("currentPage", currentPage);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            genderCode = savedInstanceState.getInt("gender");
            ageCode = savedInstanceState.getInt("age");
            priceCode = savedInstanceState.getInt("price");
            currentPage = savedInstanceState.getInt("currentPage");

            changePage(currentPage);

            if (currentPage == 2) {
                getView().findViewById(R.id.viewpager_index_2).setBackgroundResource(R.drawable.viewpager_index_selected);
                getView().findViewById(R.id.viewpager_index_3).setBackgroundResource(R.drawable.viewpager_index_selected);

            } else if (currentPage == 1) {

                getView().findViewById(R.id.viewpager_index_2).setBackgroundResource(R.drawable.viewpager_index_selected);
                getView().findViewById(R.id.viewpager_index_3).setBackgroundResource(R.drawable.viewpager_index_non_selected);
            }


        }


    }
}
