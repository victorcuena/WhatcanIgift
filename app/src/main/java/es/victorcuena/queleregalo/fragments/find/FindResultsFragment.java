package es.victorcuena.queleregalo.fragments.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.fragments.RecyclerViewPresentsFragment;
import es.victorcuena.queleregalo.models.Present;
import es.victorcuena.queleregalo.utils.OnImageFindTouchListener;
import es.victorcuena.queleregalo.views.DoubleCircleView;

import static es.victorcuena.queleregalo.utils.Routes.PRESENT_FIND_URI;


public class FindResultsFragment extends Fragment {


    public static String RESULTS_FRAGMENT_TAG = "resultsFragmentTag";

    private OnFindResultsEventListener mListener;


    private ImageView genderImage;
    private ImageView ageImage;
    private DoubleCircleView priceImage;
    private TextView genderText;
    private TextView ageText;

    private int genderCode;
    private int ageCode;
    private int priceCode;

    private RecyclerViewPresentsFragment recyclerViewFragment;

    public FindResultsFragment() {
    }

    public static FindResultsFragment newInstance(int genderCode, int ageCode, int priceCode) {
        Bundle args = new Bundle();
        args.putInt("gender", genderCode);
        args.putInt("age", ageCode);
        args.putInt("price", priceCode);

        FindResultsFragment res = new FindResultsFragment();
        res.setArguments(args);
        return res;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_results, container, false);


        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        genderImage = (ImageView) getView().findViewById(R.id.find_results_gender);
        ageImage = (ImageView) getView().findViewById(R.id.find_results_age);
        priceImage = (DoubleCircleView) getView().findViewById(R.id.find_results_price);

        genderText = (TextView) getView().findViewById(R.id.find_results_gender_text);
        ageText = (TextView) getView().findViewById(R.id.find_results_age_text);


        //Adding elevations effects
        genderImage.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        ageImage.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        priceImage.setOnTouchListener(new OnImageFindTouchListener(getContext()));


        //Changing the filter so onClickButtons

        genderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChangeFilter(0);
            }
        });

        ageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChangeFilter(1);
            }
        });

        priceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChangeFilter(2);
            }
        });


        if (savedInstanceState != null) {

            genderCode = savedInstanceState.getInt("gender");
            ageCode = savedInstanceState.getInt("age");
            priceCode = savedInstanceState.getInt("price");
        }

        if (getArguments() != null) {
            genderCode = getArguments().getInt("gender");
            ageCode = getArguments().getInt("age");
            priceCode = getArguments().getInt("price");


        }


        loadParameters();
        recyclerViewFragment = RecyclerViewPresentsFragment.newInstance(PRESENT_FIND_URI + genderCode + "/" + ageCode + "/" + priceCode);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.results, recyclerViewFragment);
        ft.commit();


    }


    private void loadParameters() {

        genderImage.setImageDrawable(Present.Gender.getDrawableFromGenderCode(getContext(), genderCode));
        genderText.setText(Present.Gender.getStringValueFromGender(getContext(), genderCode));

        ageImage.setImageDrawable(Present.Age.getDrawableFromAgeCode(getContext(), ageCode));
        ageText.setText(Present.Age.getStringValueFromAge(getContext(), ageCode));

        priceImage.setColor(Present.Price.getColorFromPrice(getContext(), priceCode));


        String coin = "";
        if (priceCode != Present.Price.FREE)
            coin = " " + getString(R.string.price_coin);


        priceImage.setText(Present.Price.getStringValueFromPrice(getContext(), priceCode) + coin);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment f = getParentFragment();

        if (f != null) {
            mListener = (OnFindResultsEventListener) f;
        } else {
            throw new RuntimeException("Must implement OnFindResultsEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putInt("gender", genderCode);
        outState.putInt("age", ageCode);
        outState.putInt("price", priceCode);

    }

    public interface OnFindResultsEventListener {
        void onChangeFilter(int field);
    }
}
