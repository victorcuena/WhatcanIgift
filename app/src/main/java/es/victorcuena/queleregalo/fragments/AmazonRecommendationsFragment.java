package es.victorcuena.queleregalo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.adapters.AmazonProductsAdapter;
import es.victorcuena.queleregalo.models.AmazonAnswer;
import es.victorcuena.queleregalo.models.AmazonProduct;
import es.victorcuena.queleregalo.utils.AmazonParser;
import es.victorcuena.queleregalo.utils.VolleySingleton;

import static es.victorcuena.queleregalo.utils.Routes.GET_AMAZON_PRESENTS;


public class AmazonRecommendationsFragment extends Fragment {

    public static final String KEYWORD_PARAMETER = "keyword";
    public static final String PRICE_MIN_PARAMETER = "priceMin";
    public List<AmazonProduct> recommendations = new ArrayList<>();
    private AmazonProductsAdapter mAdapter;

    public static AmazonRecommendationsFragment newInstance(String keyword, String price) {


        Bundle args = new Bundle();
        args.putString(KEYWORD_PARAMETER, keyword);
        args.putString(PRICE_MIN_PARAMETER, price);

        AmazonRecommendationsFragment res = new AmazonRecommendationsFragment();
        res.setArguments(args);
        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.amazon_results, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mAdapter = new AmazonProductsAdapter(recommendations, getContext());


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        if (getArguments() != null)
            fillAdapter(getArguments().getString(KEYWORD_PARAMETER), getArguments().getString(PRICE_MIN_PARAMETER));

    }


    private void fillAdapter(String keyword, String priceMin) {

        StringRequest request = new StringRequest(Request.Method.GET, (GET_AMAZON_PRESENTS + keyword + "&priceMin=" + priceMin).replace(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        StringRequest request = new StringRequest(Request.Method.GET, response,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {


                                        Log.e("Amazon response", response);
                                        AmazonAnswer res = AmazonParser.parse(response);

                                        recommendations.addAll(res.getProducts());

                                        mAdapter.notifyDataSetChanged();


                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("VOLLEY", "Error Volley: " + error.toString());
                                        getView().findViewById(R.id.amazon_results).setVisibility(View.GONE);
                                    }
                                });


                        VolleySingleton.
                                getInstance(getContext()).
                                addToRequestQueue(request);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", "Error Volley: " + error.toString());
                        getView().findViewById(R.id.amazon_results).setVisibility(View.GONE);

                    }
                });


        VolleySingleton.
                getInstance(getContext()).
                addToRequestQueue(request);


    }


}
