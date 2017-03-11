package es.victorcuena.queleregalo.models;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.victorcuena.queleregalo.utils.Routes;
import es.victorcuena.queleregalo.utils.VolleySingleton;


public class Reminder implements Comparable<Reminder> {

    private Present present;
    private String toWhom;
    private String reason;
    private String idDevice;


    public Reminder() {
    }

    public Reminder(String idDevice, String toWhom, String reason, Present present) {
        this.idDevice = idDevice;
        this.present = present;
        this.toWhom = toWhom;
        this.reason = reason;
    }

    public static Reminder fillReminder(JSONObject pObj) {

        Reminder r = new Reminder();


        try {

            r.setIdDevice(pObj.getString("id_device"));
            r.setToWhom(pObj.getString("whom"));
            r.setReason(pObj.getString("reason"));
            r.setPresent(Present.fillPresent(pObj));


        } catch (JSONException e) {
            e.printStackTrace();
            r = null;
        }
        return r;

    }

    public static void addReminder(Context context, final Reminder r, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {


        StringRequest request = new StringRequest(Request.Method.POST, Routes.REMINDERS_URI + r.getIdDevice(),
                responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idpresent", r.getPresent().getId());
                params.put("whom", r.getToWhom());
                params.put("reason", r.getReason());

                return params;
            }

        };

        VolleySingleton.
                getInstance(context).
                addToRequestQueue(request);


    }

    public static void removeReminder(Context context, final Reminder r, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {


        VolleySingleton.
                getInstance(context).
                addToRequestQueue(new JsonObjectRequest(Request.Method.DELETE, Routes.REMINDERS_URI + r.getIdDevice() + "/" + r.getPresent().getId(),
                        responseListener, errorListener));


    }

    public Present getPresent() {
        return present;
    }

    public void setPresent(Present present) {
        this.present = present;
    }

    public String getToWhom() {
        return toWhom;
    }

    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                " id_device='" + idDevice + '\'' +
                ", present=" + present.toString() +
                ", toWhom='" + toWhom + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public int compareTo(Reminder another) {

        return toWhom.compareTo(another.getToWhom());

    }

}
