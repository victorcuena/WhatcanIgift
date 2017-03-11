package es.victorcuena.queleregalo.fragments;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.activities.MainActivity;
import es.victorcuena.queleregalo.utils.Routes;
import es.victorcuena.queleregalo.utils.Utils;

import static es.victorcuena.queleregalo.models.Present.Gender.MEN;
import static es.victorcuena.queleregalo.models.Present.Gender.WOMEN;


public class NewsFragment extends Fragment {


    //Save config
    private static final String OFFSET = "offset";
    public static String NEWS_FRAGMENT_TAG = "newsFragmentTag";
    Context context;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    PagerSlidingTabStrip tabLayout;
    ImageView logo;
    FloatingActionButton fab;
    View toolbarImageMask;
    ImageView toolbarImage;
    private float lastOffset = -1;
    private float lastRatio = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Getting Views
        context = getContext();
        final DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        viewPager = (ViewPager) getActivity().findViewById(R.id.mViewPager);
        tabLayout = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        //Offset of the header
        logo = (ImageView) view.findViewById(R.id.header_logo);
        toolbarImageMask = view.findViewById(R.id.toolbar_image_mask);
        toolbarImage = (ImageView) view.findViewById(R.id.toolbar_image);


        //Hamburguer management
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();


        //Toolbar management
        toolbar.setTitle(getResources().getString(R.string.navigation_drawer_news));
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //FAB management
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                ((MainActivity) getActivity()).onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_search));


            }
        });


        //Setting up the viewpager

        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {


            @Override
            public Fragment getItem(int position) {


                String uri = "";

                switch (position % getCount()) {
                    case 0:
                        uri = Routes.PRESENT_MEN_NEW_URI;
                        break;
                    case 1:
                        uri = Routes.PRESENT_WOMEN_NEW_URI;
                        break;

                }

                return RecyclerViewPresentsFragment.newInstance(uri);

            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                String res = "";

                switch (position % getCount()) {
                    case 0:
                        res = getResources().getString(R.string.gender_men);
                        break;
                    case 1:
                        res = getResources().getString(R.string.gender_women);
                        break;

                }
                return res;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                switch (position) {
                    case 0:
                        changePage(MEN);
                        break;
                    case 1:
                        changePage(WOMEN);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setViewPager(viewPager);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                //This is to enhance performance, avoid several operations if the offset is the same
                if (verticalOffset != lastOffset) {

                    float difference = appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset);
                    float ratio = Math.max(difference, 0) / (appBarLayout.getTotalScrollRange());

                    //With the ratio we get how much is the header showed

                    //Alpha management
                    int color = ((ColorDrawable) toolbarImageMask.getBackground()).getColor();
                    color = Utils.colorWithAlpha(color, 1 - ratio);
                    toolbarImageMask.setBackgroundColor(color);
                    logo.setAlpha(ratio);

                    //Margin of the tabs
                    int overlapTop = (int) (getResources().getDimensionPixelOffset(R.dimen.header_recycler_overlap_top) * ratio);
                    tabLayout.setTranslationY(-overlapTop);

                    //lastOffset update
                    lastOffset = verticalOffset;
                    lastRatio = ratio;
                }


            }
        });


    }

    private void changePage(int gender) {


        Drawable newLogo = null;
        int fabColorFrom = 0, fabColorTo = 0;
        int maskHeaderColorFrom = ((ColorDrawable) toolbarImageMask.getBackground()).getColor();
        int maskHeaderColorTo = 0;

        switch (gender) {
            case MEN:

                newLogo = ContextCompat.getDrawable(context, R.drawable.men_1);
                fabColorFrom = ContextCompat.getColor(context, R.color.colorAccentWomen);
                fabColorTo = ContextCompat.getColor(context, R.color.colorAccentMen);
                maskHeaderColorTo = ContextCompat.getColor(context, R.color.colorPrimaryDarkMen);

                break;
            case WOMEN:
                newLogo = ContextCompat.getDrawable(context, R.drawable.women_1);

                fabColorFrom = ContextCompat.getColor(context, R.color.colorAccentMen);
                fabColorTo = ContextCompat.getColor(context, R.color.colorAccentWomen);
                maskHeaderColorTo = ContextCompat.getColor(context, R.color.colorPrimaryDarkWomen);
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccentWomen)));
                break;

        }

        Utils.colorTransition(maskHeaderColorFrom, maskHeaderColorTo, 2000L, new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                toolbarImage.setBackgroundColor(animatedValue);
                toolbarImageMask.setBackgroundColor(Utils.colorWithAlpha(animatedValue, 1 - lastRatio));
            }
        }).start();


        Utils.colorTransition(fabColorFrom, fabColorTo, 2000L, new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int animatedValue = (int) animation.getAnimatedValue();
                fab.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
            }
        }).start();


        if (newLogo != null)
            Utils.transitionToImage(logo, newLogo).startTransition(200);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putFloat(OFFSET, lastOffset);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
