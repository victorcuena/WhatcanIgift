package es.victorcuena.queleregalo.fragments.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.models.Present;
import es.victorcuena.queleregalo.utils.OnImageFindTouchListener;


public class FindGenderFragment extends Fragment {


    public static final int BACKGROUND_COLOR = R.color.morning;
    public static String GENDER_FRAGMENT_TAG = "genderFragmentTag";

    private OnGenderEventListener mListener;

    public FindGenderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_gender, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView women = (ImageView) view.findViewById(R.id.find_gender_women);
        ImageView men = (ImageView) view.findViewById(R.id.find_gender_men);

        women.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        men.setOnTouchListener(new OnImageFindTouchListener(getContext()));

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGenderSelected(Present.Gender.WOMEN);
            }
        });

        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mListener.onGenderSelected(Present.Gender.MEN);
            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        Fragment f = getParentFragment();

        if (f != null) {
            mListener = (OnGenderEventListener) f;
        } else {
            throw new RuntimeException("Must implement OnGenderEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGenderEventListener {
        void onGenderSelected(int genderCode);
    }
}
