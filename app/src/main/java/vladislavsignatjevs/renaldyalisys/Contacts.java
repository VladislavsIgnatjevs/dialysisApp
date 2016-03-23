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

public class Contacts extends Activity {
    String savedResponse;
    Map<String,String> contacts = new HashMap<String,String>();
    String consName,consNumber,consLocation,dietName,dietNumber,dietLocation,docName,docNumber,docLocation,wdName,wdNumber,wdLocation;
    private TextView consultantsName;
    private TextView consultantsNumber;
    private TextView consultantsLocation;
    private TextView dietitiansName;
    private TextView dietitiansNumber;
    private TextView dietitiansLocation;
    private TextView doctorsName;
    private TextView doctorsNumber;
    private TextView doctorsLocation;
    private TextView wardsName;
    private TextView wardsNumber;
    private TextView wardsLocation;

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private static final String TAG = Contacts.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //progress dialogue
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        consultantsName = (TextView) findViewById(R.id.consultant_name);
        consultantsNumber = (TextView) findViewById(R.id.consultant_number);
        consultantsLocation = (TextView) findViewById(R.id.consultant_location);

        dietitiansName = (TextView) findViewById(R.id.dietitian_name);
        dietitiansNumber = (TextView) findViewById(R.id.diatitian_number);
        dietitiansLocation = (TextView) findViewById(R.id.dietitian_location);

        doctorsName = (TextView) findViewById(R.id.doctor_name);
        doctorsNumber = (TextView) findViewById(R.id.doctor_number);
        doctorsLocation = (TextView) findViewById(R.id.doctor_location);

        wardsName = (TextView) findViewById(R.id.ward_name);
        wardsNumber = (TextView) findViewById(R.id.ward_number);
        wardsLocation = (TextView) findViewById(R.id.ward_location);

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
        requestEssentialContacts(userData.get("uid"));







    }

    /**
     * request user contacts via post
     *
     * */
    private void requestEssentialContacts(final String email)
        {
        // Tag used to cancel the request
        String tag_string_req = "req_contacts";

        pDialog.setMessage("Getting essential contacts ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CONTACTS_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Request Contacts response: " + response.toString());



                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        //user exists. retrieve contacts
                        savedResponse =  response.toString();
                        //removing rubbish from string
                        savedResponse = savedResponse.replace("\"","");
                        savedResponse = savedResponse.replace("{","");
                        savedResponse = savedResponse.replace("}","");
                        Log.d(TAG, "SAVED RESPONSE NO RUBBISH "+savedResponse );
                        //split response by "," and put contacts into hashmap contacts
                        String[] pairs = savedResponse.split(",");
                        for (int i=0;i<pairs.length;i++) {
                            String pair = pairs[i];

                            String[] keyValue = pair.split(":");

                            contacts.put(keyValue[0], keyValue[1]);
                        }
                        //output hashmap into log
                        for (Map.Entry entry : contacts.entrySet()){
                            Log.d(TAG, "key: " +entry.getKey() +" value "+entry.getValue() );
                        }

                        consName = contacts.get("consultant_name");
                        consNumber = contacts.get("consultant_number");
                        consLocation = contacts.get("consultant_location");
                        dietName = contacts.get("dietitian_name");
                        dietNumber = contacts.get("dietitian_number");
                        dietLocation = contacts.get("dietitian_location");
                        docName = contacts.get("doctor_name");
                        docNumber = contacts.get("doctor_number");
                        docLocation = contacts.get("doctor_location");
                        wdName = contacts.get("ward_name");
                        wdNumber = contacts.get("ward_number");
                        wdLocation = contacts.get("ward_location");

                        //  Displaying contacts on the screen

                        consultantsName.setText(" "+consName);
                        consultantsNumber.setText(" "+consNumber);
                        consultantsLocation.setText(" "+consLocation);
                        dietitiansName.setText(" "+dietName);
                        dietitiansNumber.setText(" "+dietNumber);
                        dietitiansLocation.setText(" "+dietLocation);
                        doctorsName.setText(" "+docName);
                        doctorsNumber.setText(" "+docNumber);
                        doctorsLocation.setText(" "+docLocation);
                        wardsName.setText(" "+wdName);
                        wardsNumber.setText(" "+wdNumber);
                        wardsLocation.setText(" "+wdLocation);
                    }
                      else {

                        // Error occurred in contacts retrieval. Get the error
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
                Log.e(TAG, "Contacts retrieval error: " + error.getMessage());
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
        Intent intent = new Intent(Contacts.this, LoginActivity.class);
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
