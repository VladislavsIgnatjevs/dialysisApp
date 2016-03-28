package vladislavsignatjevs.renaldyalisys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vladislavsignatjevs.renaldyalisys.R;
import vladislavsignatjevs.renaldyalisys.app.AppConfig;
import vladislavsignatjevs.renaldyalisys.app.AppController;
import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class Profile extends Activity {
    String savedResponse;
    Map<String,String> profile = new HashMap<String,String>();
    String patName,patID,patDOB,patAllergies,patAccessType;
    private TextView name;
    private TextView patient_id;
    private TextView dob;
    private TextView allergies;
    private TextView access_type;


    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private static final String TAG = Profile.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //progress dialogue
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        name = (TextView) findViewById(R.id.patient_name);
        patient_id = (TextView) findViewById(R.id.patient_id);
        dob = (TextView) findViewById(R.id.patient_dob);

        allergies = (TextView) findViewById(R.id.patient_allergies);
        access_type = (TextView) findViewById(R.id.patient_access_type);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();

        Log.d(TAG, "Request UID: " + userData.get("uid").toString());
        requestProfile(userData.get("uid"));



    }

    /**
     * request user profile data via post
     *
     * */
    private void requestProfile(final String email)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_profile";

        pDialog.setMessage("Getting profile data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Request profile response: " + response.toString());



                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        //user exists. retrieve profile data
                        savedResponse =  response.toString();
                        //removing rubbish from string
                        savedResponse = savedResponse.replace("\"","");
                        savedResponse = savedResponse.replace("{","");
                        savedResponse = savedResponse.replace("}","");
                        Log.d(TAG, "SAVED RESPONSE NO RUBBISH "+savedResponse );
                        //split response by "," and put profile data into hashmap profile
                        String[] pairs = savedResponse.split(",");
                        for (int i=0;i<pairs.length;i++) {
                            String pair = pairs[i];

                            String[] keyValue = pair.split(":");

                            profile.put(keyValue[0], keyValue[1]);
                        }
                        //output hashmap into log
                        for (Map.Entry entry : profile.entrySet()){
                            Log.d(TAG, "key: " +entry.getKey() +" value "+entry.getValue() );
                        }

                        patName = profile.get("name");
                        patID = profile.get("patient_id");
                        patDOB = profile.get("dob");
                        patAllergies = profile.get("allergies");
                        patAccessType = profile.get("access_type");


                        //  Displaying profile on the screen

                        name.setText(patName);
                        patient_id.setText(patID);
                        dob.setText(patDOB);
                        allergies.setText(patAllergies);
                        access_type.setText(patAccessType);

                    }
                    else {

                        // Error occurred in profile data retrieval. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Profile data retrieval error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //list of parameters to the hash map
                params.put("email", email);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Profile.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
