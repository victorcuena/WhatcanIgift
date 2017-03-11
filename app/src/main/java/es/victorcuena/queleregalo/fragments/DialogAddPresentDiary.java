package es.victorcuena.queleregalo.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.activities.MainActivity;
import es.victorcuena.queleregalo.models.Present;
import es.victorcuena.queleregalo.models.Reminder;
import es.victorcuena.queleregalo.utils.Utils;

/**
 * Created by victorcuenagarcia on 09/02/2017.
 */

public class DialogAddPresentDiary extends DialogFragment {

    public static final String PRESENT_PARAMETER = "present";

    private Present p;
    private View toShow;

    private EditText nameEdt, reasonEdt;
    private TextInputLayout error;

    public static DialogAddPresentDiary newInstance(Present p) {

        Bundle args = new Bundle();
        args.putSerializable(PRESENT_PARAMETER, p);

        DialogAddPresentDiary res = new DialogAddPresentDiary();
        res.setArguments(args);
        return res;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_add_present_diary, null);


        TextView heading = (TextView) v.findViewById(R.id.dialog_add_present_heading);
        nameEdt = (EditText) v.findViewById(R.id.dialog_add_present_name);
        reasonEdt = (EditText) v.findViewById(R.id.dialog_add_present_reason);
        error = (TextInputLayout) v.findViewById(R.id.text_input_layout);
        toShow = getActivity().findViewById(R.id.coordinator_layout);

        //Getting the present argument
        p = (Present) getArguments().get(PRESENT_PARAMETER);


        //Getting the heading message

        String headingText = heading.getText().toString();
        headingText = headingText.replace("?", p.getName());
        heading.setText(headingText);


        final Resources res = getResources();
        final Activity act = getActivity();


        builder.setPositiveButton(getString(R.string.dialog_add_present_possitive_button), null);
        builder.setNegativeButton(getString(R.string.dialog_add_present_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.setView(v);
        final Dialog diag = builder.create();

        diag.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (checkErrors()) {

                            String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);

                            Reminder r = new Reminder(
                                    android_id,
                                    Utils.firstLetterToUpperCase(nameEdt.getText().toString()),
                                    Utils.firstLetterToUpperCase(reasonEdt.getText().toString())
                                    , p);

                            Reminder.addReminder(getActivity(), r, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("Response", response.toString());

                                            Snackbar.make(toShow, res.getString(R.string.dialog_add_present_sent_ok), Snackbar.LENGTH_LONG)
                                                    .setAction(res.getString(R.string.dialog_add_present_see_reminders), new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent();

                                                            intent.putExtra("initial", R.id.nav_diary);

                                                            intent.setClass(act.getApplicationContext(), MainActivity.class);
                                                            act.startActivity(intent);
                                                        }
                                                    })
                                                    .show();

                                            diag.dismiss();

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // error
                                            Toast.makeText(act.getApplicationContext(), res.getString(R.string.dialog_add_present_sent_error), Toast.LENGTH_LONG).show();

                                            diag.dismiss();
                                        }
                                    });


                        }
                    }
                });
            }
        });

        return diag;

    }


    private boolean checkErrors() {


        boolean res = false;

        if (nameEdt.getText().toString().trim().length() == 0) {
            error.setError(getActivity().getString(R.string.dialog_field_name_empty));
        } else {
            error.setError(null);
            res = true;
        }


        return res;
    }


}
