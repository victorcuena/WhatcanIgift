package es.victorcuena.queleregalo.fragments;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.activities.MainActivity;
import es.victorcuena.queleregalo.adapters.RemindersAdapter;
import es.victorcuena.queleregalo.models.Reminder;
import es.victorcuena.queleregalo.utils.VolleySingleton;

import static es.victorcuena.queleregalo.utils.Constants.OK_CODE;
import static es.victorcuena.queleregalo.utils.Routes.REMINDERS_URI;


public class DiaryFragment extends Fragment {

    public static String DIARY_FRAGMENT_TAG = "diaryFragmentTag";
    private LinearLayout errorLayout;
    private Toolbar toolbar;
    private RemindersAdapter mAdapter;
    private List<Reminder> reminders = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);

        //Toolbar management
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        toolbar.setTitle(getString(R.string.your_diary_title));
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting up the recyclerview


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reminders_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RemindersAdapter(reminders, getContext());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        fillAdapter(androidId);


        //Setting up to remove reminders
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                final Reminder r = (Reminder) viewHolder.itemView.getTag();
                final int pos = reminders.indexOf(r);


                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.your_diary_sure_question))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //SURE! REMOVE!
                                Reminder.removeReminder(getActivity(), r, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {

                                                    int status = response.getInt("status");

                                                    switch (status) {
                                                        case OK_CODE:
                                                            reminders.remove(r);
                                                            if (pos != -1)
                                                                mAdapter.notifyItemRemoved(pos);

                                                            checkEmpty();
                                                            break;
                                                        default:
                                                            mAdapter.notifyDataSetChanged();
                                                            Toast.makeText(getContext(), R.string.error_general, Toast.LENGTH_LONG).show();

                                                    }

                                                } catch (JSONException e) {
                                                    mAdapter.notifyDataSetChanged();
                                                    Log.e("ERROR", e.getMessage());
                                                    Toast.makeText(getContext(), R.string.error_general, Toast.LENGTH_LONG).show();
                                                }


                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Log.e("VOLLEY", "Error Volley: " + error.getMessage());

                                                String errorMessage = getString(R.string.error_general);

                                                if (error instanceof NetworkError)
                                                    errorMessage = getString(R.string.error_network);
                                                else if (error instanceof TimeoutError)
                                                    errorMessage = getString(R.string.error_network_timeout);
                                                else if (error.networkResponse.statusCode == 404)
                                                    errorMessage = getString(R.string.error_404);


                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();


                                                mAdapter.notifyDataSetChanged();

                                            }
                                        });


                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }


    private void fillAdapter(String deviceId) {

        getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);

        VolleySingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                REMINDERS_URI + deviceId,
                                (String) null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        processResponse(response);
                                        getView().findViewById(R.id.loading).setVisibility(View.GONE);


                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Log.e("VOLLEY", "Error Volley: " + error.getMessage());

                                        TextView errorMessage = (TextView) getView().findViewById(R.id.error_text);
                                        errorMessage.setText(VolleySingleton.getErrorMessage(getContext(), error));

                                        checkEmpty();

                                        errorLayout.setVisibility(View.VISIBLE);
                                        getView().findViewById(R.id.loading).setVisibility(View.GONE);
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
                        reminders.add(Reminder.fillReminder(pObj));

                    }

                    //Empty
                    checkEmpty();

                    mAdapter.notifyDataSetChanged();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void checkEmpty() {

        if (reminders.size() == 0) {
            ImageView errorIcon = (ImageView) errorLayout.findViewById(R.id.error_icon);
            TextView errorText = (TextView) errorLayout.findViewById(R.id.error_text);
            TextView errorReload = (TextView) errorLayout.findViewById(R.id.error_try_again);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                errorIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_cloud_queue_black_24dp));
            else
                errorIcon.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_cloud_queue_black_24dp));

            errorText.setText(getString(R.string.your_diary_empty));
            errorReload.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }


}
