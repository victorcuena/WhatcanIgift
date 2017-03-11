package es.victorcuena.queleregalo.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.utils.Routes;
import es.victorcuena.queleregalo.utils.VolleySingleton;

public class SuggestActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private EditText email,content;
    private TextInputLayout errorEmail, errorContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView sendButton = (TextView) findViewById(R.id.send_button);
        email = (EditText) findViewById(R.id.suggest_email);
        content = (EditText) findViewById(R.id.suggest_content);
        errorEmail = (TextInputLayout) findViewById(R.id.suggest_email_input);
        errorContent = (TextInputLayout) findViewById(R.id.suggest_content_input);


        //Toolbar management
        toolbar.setTitle(getString(R.string.suggestion_title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkErrors()) {


                    VolleySingleton.
                            getInstance(getApplicationContext()).
                            addToRequestQueue(new StringRequest(Request.Method.POST, Routes.SUGGESTION_URI,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("Response", response);
                                            Toast.makeText(getApplicationContext(), getString(R.string.suggestion_sent_successfully), Toast.LENGTH_LONG).show();

                                            onBackPressed();


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // error

                                            Log.e("VOLLEY", "Error Volley: " + error.toString());
                                            Toast.makeText(getApplicationContext(), VolleySingleton.getErrorMessage(getApplicationContext(),error), Toast.LENGTH_LONG).show();

                                            onBackPressed();
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("email", email.getText().toString());
                                    params.put("content", content.getText().toString());

                                    return params;
                                }
                            });
                }
                }

        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }





    private boolean checkErrors() {


        boolean res = false;

        //Check email
        if (email.getText().toString().trim().length() == 0) {
            errorEmail.setError(getResources().getString(R.string.email_mandatory));
        } else {
            errorEmail.setError(null);
            res = true;
        }

        if (content.getText().toString().trim().length() == 0) {
            errorContent.setError(getResources().getString(R.string.content_mandatory));
            res = false;
        } else {
            errorContent.setError(null);
            res = true;
        }




        return res;
    }

}
