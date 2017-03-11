package es.victorcuena.queleregalo.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import es.victorcuena.queleregalo.R;

public class VolleySingleton {


    // Atributos
    private static VolleySingleton singleton;
    private static Context context;
    private RequestQueue requestQueue;


    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new VolleySingleton(context.getApplicationContext());
        }
        return singleton;
    }

    public static String getErrorMessage(Context context, VolleyError error){

        String res = "";

        if(error instanceof TimeoutError){
            res = context.getResources().getString(R.string.error_network_timeout);
        }else if (isServerProblem(error)){
            res = handleServerError(error ,context);

        }else if(error instanceof NetworkError){
            res =  context.getResources().getString(R.string.error_network);
        }else
            res =  context.getResources().getString(R.string.error_general);

        return res;
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    //Error handling

    private static String handleServerError(Object error, Context context) {

        VolleyError er = (VolleyError)error;
        NetworkResponse response = er.networkResponse;
        if(response != null){
            switch (response.statusCode){
                case 404:
                    return context.getResources().getString(R.string.error_404);
                default:
                    return context.getResources().getString(R.string.error_network_timeout);
            }
        }

        return context.getResources().getString(R.string.error_general);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }


}