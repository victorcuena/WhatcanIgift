package es.victorcuena.queleregalo.fragments.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.models.Present;
import es.victorcuena.queleregalo.utils.OnImageFindTouchListener;
import es.victorcuena.queleregalo.views.DoubleCircleView;


public class FindPriceFragment extends Fragment {


    public static final int BACKGROUND_COLOR = R.color.night;
    public static String PRICE_FRAGMENT_TAG = "priceFragmentTag";

    private OnPriceEventListener mListener;

    public FindPriceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_price, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DoubleCircleView fromFree = (DoubleCircleView) view.findViewById(R.id.find_price_free);
        DoubleCircleView from10 = (DoubleCircleView) view.findViewById(R.id.find_price_10);
        DoubleCircleView from20 = (DoubleCircleView) view.findViewById(R.id.find_price_20);
        DoubleCircleView from50 = (DoubleCircleView) view.findViewById(R.id.find_price_50);
        DoubleCircleView from100 = (DoubleCircleView) view.findViewById(R.id.find_price_100);

        fromFree.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        from10.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        from20.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        from50.setOnTouchListener(new OnImageFindTouchListener(getContext()));
        from100.setOnTouchListener(new OnImageFindTouchListener(getContext()));


        fromFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPriceSelected(Present.Price.FREE);
            }
        });

        from10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPriceSelected(Present.Price.FROM_10);
            }
        });

        from20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPriceSelected(Present.Price.FROM_20);
            }
        });

        from50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPriceSelected(Present.Price.FROM_50);
            }
        });

        from100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPriceSelected(Present.Price.FROM_100);
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //     Fragment f = getFragmentManager().findFragmentByTag(FIND_FRAGMENT_TAG);
        Fragment f = getParentFragment();

        if (f != null) {
            mListener = (OnPriceEventListener) f;
        } else {
            throw new RuntimeException(f.toString() + "Must implement OnPriceEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPriceEventListener {
        void onPriceSelected(int priceCode);
    }
}
