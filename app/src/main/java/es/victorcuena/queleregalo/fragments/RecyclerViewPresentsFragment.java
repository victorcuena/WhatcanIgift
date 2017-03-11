package es.victorcuena.queleregalo.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.activities.DetailActivity;
import es.victorcuena.queleregalo.adapters.PresentsAdapter;
import es.victorcuena.queleregalo.models.Present;
import es.victorcuena.queleregalo.utils.VolleySingleton;

import static android.view.View.GONE;
import static es.victorcuena.queleregalo.utils.Constants.OK_CODE;


public class RecyclerViewPresentsFragment extends Fragment {

    public static final String URI_PARAMETER = "uri";
    public static final String ID_EXTRA = "id";
    public static final String NAME_EXTRA = "name";
    public static final String PRICE_EXTRA = "price";
    public List<Present> presents = new ArrayList<>();
    private PresentsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String uri;
    private LinearLayout errorLayout;

    public RecyclerViewPresentsFragment() {
    }


    public static RecyclerViewPresentsFragment newInstance(String uri) {


        Bundle args = new Bundle();
        args.putString(URI_PARAMETER, uri);
        RecyclerViewPresentsFragment res = new RecyclerViewPresentsFragment();
        res.setArguments(args);
        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.presents_recycler_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout);
        errorLayout = (LinearLayout) getView().findViewById(R.id.error_layout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new PresentsAdapter(presents, getContext(), new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                // Starting the detail activity
                Intent i = new Intent(getContext(), DetailActivity.class);

                i.putExtra(ID_EXTRA, (String) v.getTag());
                i.putExtra(NAME_EXTRA, ((TextView) v.findViewById(R.id.present_name)).getText());
                i.putExtra(PRICE_EXTRA, (int) v.findViewById(R.id.present_price).getTag());


                //Configurating the shared elements of the transition
                Pair<View, String> p1 = Pair.create(v.findViewById(R.id.present_name), getResources().getString(R.string.transition_name_detail));
                Pair<View, String> p2 = Pair.create(v.findViewById(R.id.present_pic), getResources().getString(R.string.transition_image_detail));

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), p1, p2);

                //More dynamic exit


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setExitTransition(new Explode());
                }

                ActivityCompat.startActivity(getActivity(), i, options.toBundle());


            }
        });


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });


        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });


        if (getArguments() != null) {

            //To show initial loading
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });

            fillAdapter(getArguments().getString(URI_PARAMETER));
        }


    }


    public void fillAdapter(String uri) {

        Log.e("CARGANDO", uri);

        this.uri = uri;
        errorLayout.setVisibility(GONE);

        VolleySingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                uri,
                                (String) null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        mAdapter.clear();
                                        processResponse(response);

                                        swipeRefreshLayout.setRefreshing(false);

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {


                                        //Clean the adapter
                                        mAdapter.clear();
                                        Log.e("VOLLEY", "Error Volley: " + error.getMessage());

                                        TextView errorMessage = (TextView) errorLayout.findViewById(R.id.error_text);
                                        errorMessage.setText(VolleySingleton.getErrorMessage(getContext(),error));



                                        errorLayout.setVisibility(View.VISIBLE);

                                        Log.d("VOLLEY", "Error Volley: " + error.getMessage());
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }

                        )
                );


    }

    private void processResponse(JSONObject response) {
        try {

            int status = response.getInt("status");

            switch (status) {
                case OK_CODE:

                    JSONArray presentsArray = response.getJSONArray("data");

                    for (int i = 0; i < presentsArray.length(); i++) {

                        JSONObject pObj = presentsArray.getJSONObject(i);
                        presents.add(Present.fillPresent(pObj));

                    }

                    mAdapter.notifyDataSetChanged();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void reload() {
        fillAdapter(uri);
    }


}
