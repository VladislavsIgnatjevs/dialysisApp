package vladislavsignatjevs.renaldyalisys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vladislavsignatjevs.renaldyalisys.app.AppConfig;
import vladislavsignatjevs.renaldyalisys.app.AppController;
import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class Healthchecks_view_graphs extends Activity {

    Map<String, String> med_history_map = new HashMap<String, String>();
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private static final String tag = Healthcheck.class.getSimpleName();

    private String eCount,uid;
    private TextView date_added,creative_protein,iron,transferrin,satn_transferrin,phosphate,bicarbonate,ferritin,glucose;
    private TextView magnesium,sodium,potassium,urea,creatinine,alt,bilirubins,alkaline_phosphatase,albumin,calcium,calcium_corrected,est_gfr;
    private String [] date_addedArray,creative_proteinArray,ironArray,transferrinArray,satn_transferrinArray,phosphateArray,bicarbonateArray,ferritinArray,glucoseArray;
    private String [] magnesiumArray,sodiumArray,potassiumArray,ureaArray,creatinineArray,altArray,bilirubinsArray,alkaline_phosphataseArray,albuminArray,calciumArray,calcium_correctedArray,est_gfrArray;

    String savedResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcheck_check_health);



        //progress dialogue
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        //get textviews from layout
        date_added = (TextView) this.findViewById(R.id.healthcheck_date_added_val);
        creative_protein = (TextView) this.findViewById(R.id.healthcheck_creative_protein_val);
        iron = (TextView) this.findViewById(R.id.healthcheck_iron_val);
        transferrin = (TextView) this.findViewById(R.id.healthcheck_transferrin_val);
        satn_transferrin = (TextView) this.findViewById(R.id.healthcheck_satn_transferrin_val);
        phosphate = (TextView) this.findViewById(R.id.healthcheck_phosphate_val);
        bicarbonate = (TextView) this.findViewById(R.id.healthcheck_bicarbonate_val);
        ferritin = (TextView) this.findViewById(R.id.healthcheck_ferritin_val);
        glucose = (TextView) this.findViewById(R.id.healthcheck_glucose_val);
        magnesium = (TextView) this.findViewById(R.id.healthcheck_magnesium_val);
        sodium = (TextView) this.findViewById(R.id.healthcheck_sodium_val);
        potassium = (TextView) this.findViewById(R.id.healthcheck_potassium_val);
        urea = (TextView) this.findViewById(R.id.healthcheck_urea_val);

        creatinine = (TextView) this.findViewById(R.id.healthcheck_creatinine_val);
        alt = (TextView) this.findViewById(R.id.healthcheck_alt_val);
        bilirubins = (TextView) this.findViewById(R.id.healthcheck_bilirubins_val);
        alkaline_phosphatase = (TextView) this.findViewById(R.id.healthcheck_alkaline_phosphatase_val);
        albumin = (TextView) this.findViewById(R.id.healthcheck_albumin_val);
        calcium = (TextView) this.findViewById(R.id.healthcheck_calcium_val);
        calcium_corrected = (TextView) this.findViewById(R.id.healthcheck_calcium_corrected_val);
        est_gfr = (TextView) this.findViewById(R.id.healthcheck_gfr_val);

        //get descriptions





        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();

        Log.d(tag, "Request UID: " + userData.get("uid").toString());




    }

    /**
     * request mmedical history via post
     */
    private void requestMedHistory(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_med_history";

        pDialog.setMessage("Getting Medical History...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REQUEST_MED_HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(tag, "Request Med_History response: " + response.toString());


                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        //retrieve faq
                        savedResponse = response.toString();
                        Log.d(tag, "SAVED RESPONSE WITH RUBBISH TEST " + savedResponse);
                        //removing rubbish from string
                        savedResponse = savedResponse.replace("{","");
                        savedResponse = savedResponse.replace("}", "");
                        //  savedResponse = savedResponse.replace("\"}}", "");
                        savedResponse = savedResponse.replace("\"error\":false,\"data\":\"","");
                        Log.d(tag, "SAVED RESPONSE NO RUBBISH " + savedResponse);
                        //split response by "," and put questions and answers into hashmap faq

                        String[] pairs = savedResponse.split("\",\"");
                        for (int i = 0; i < pairs.length; i++) {
                            String pair = pairs[i];

                            String[] keyValue = pair.split("\":\"");
                            Log.d(tag, "keyvalue 0: " + keyValue[0] + " keyvalue1 " + keyValue[1]);
                            med_history_map.put(keyValue[0], keyValue[1]);
                        }
                        //output hashmap into log
                        for (Map.Entry entry : med_history_map.entrySet()) {
                            Log.d(tag, "key: " + entry.getKey() + " value " + entry.getValue());
                        }

                        TableLayout table = (TableLayout) findViewById(R.id.calendar_events_main_table);
                        int entryCount = 1;


                        //fetching number of events from hashmap
                        eCount = med_history_map.get("eCount");
                        //replacing rubbish
                        eCount = eCount.replace("\\", "");
                        eCount = eCount.replace("\"", "");
                        //trimming that string and parsing int that will be used as a counter
                        int eCountInt = Integer.parseInt(eCount.trim());

                        //for loop to get all questions from hashmap
                        for (int a = 0; a < eCountInt; a++) {
                            //getting medical data from hashmap
                            //add in array for view_graphs
//                            date_added = med_history_map.get("date_added" + entryCount);
//
//                            serum_creatine = med_history_map.get("serum_creatine" + entryCount);
//                            gfr = med_history_map.get("gfr" + entryCount);
//                            bun = med_history_map.get("bun" + entryCount);
//                            body_temp  = med_history_map.get("body_temp" + entryCount);
//                            urine_protein = med_history_map.get("urine_protein" + entryCount);
//                            microalbuminuria  = med_history_map.get("microalbuminuria" + entryCount);
//                            urine_creatine = med_history_map.get("urine_creatine" + entryCount);
//                            serum_albumin = med_history_map.get("serum_albumin" + entryCount);
//                            npna = med_history_map.get("npna" + entryCount);
//                            sga = med_history_map.get("sga" + entryCount);
//                            hemoglobin = med_history_map.get("hemoglobin" + entryCount);
//                            hematocrit = med_history_map.get("hematocrit" + entryCount);
//                            tsat = med_history_map.get("tsat" + entryCount);
//                            serum_ferritin = med_history_map.get("serum_ferritin" + entryCount);
//                            pth = med_history_map.get("pth" + entryCount);
//                            calcium = med_history_map.get("calcium" + entryCount);
//                            phosphorus = med_history_map.get("phosphorus" + entryCount);
//                            potassium = med_history_map.get("potassium" + entryCount);
//                            body_weight = med_history_map.get("body_weight" + entryCount);
//                            blood_pressure = med_history_map.get("blood_pressure" + entryCount);
//                            total_cholesterol = med_history_map.get("total_cholesterol" + entryCount);
//                            hdl_cholesterol = med_history_map.get("hdl_cholesterol" + entryCount);
//                            triglyceride = med_history_map.get("triglyceride" + entryCount);

                            //assign to textviews



                            //output to console for debugging
                            Log.d(tag, "dateadded"+entryCount +": " + date_added);

                            Log.d(tag, "Number of entries (eCount) :" + eCount);



                        }
                        entryCount++;

                    }


                    else {

                        // Error occurred in faq retrieval. Get the error
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
                Log.e(tag, "Medical history retrieval error: " + error.getMessage());
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
        Intent intent = new Intent(Healthchecks_view_graphs.this, LoginActivity.class);
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
