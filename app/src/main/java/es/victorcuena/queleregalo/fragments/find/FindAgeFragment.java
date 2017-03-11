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


public class FindAgeFragment extends Fragment {

    public static final int BACKGROUND_COLOR = R.color.evening;
    public static String AGE_FRAGMENT_TAG = "ageFragmentTag";
    private OnAgeEventListener mListener;

    public FindAgeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_age, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView preschool = (ImageView) view.findViewById(R.id.find_age_preschool);
        ImageView children = (ImageView) view.findViewById(R.id.find_age_children);
        ImageView teen = (ImageView) view.findViewById(R.id.find_age_teen);
        ImageView adult = (ImageView) view.findViewById(R.id.find_age_adult);
        ImageView all = (ImageView) view.findViewById(R.id.find_age_all_ages);

        preschool.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        children.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        teen.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        adult.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        all.setOnTouchListener(new OnImageFindTouchListener(getContext()));


        preschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAgeSelected(Present.Age.PRESCHOOL);
            }
        });
        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAgeSelected(Present.Age.CHILDREN);
            }
        });
        teen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAgeSelected(Present.Age.TEENAGE);
            }
        });
        adult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAgeSelected(Present.Age.ADULT);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAgeSelected(Present.Age.ALL);
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Fragment f = getFragmentManager().findFragmentByTag(FIND_FRAGMENT_TAG);
        Fragment f = getParentFragment();

        if (f != null) {
            mListener = (OnAgeEventListener) f;
        } else {
            throw new RuntimeException(f.toString() + "Must implement OnAgeEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAgeEventListener {
        void onAgeSelected(int ageCode);
    }
}
