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

public class HealthcheckCheckHealth extends Activity {

    Map<String, String> med_history_map = new HashMap<String, String>();
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private static final String tag = Healthcheck.class.getSimpleName();

    private String eCount, uid;
    private TextView date_added,creative_protein,iron,transferrin,satn_transferrin,phosphate,bicarbonate,ferritin,glucose;
    private TextView magnesium,sodium,potassium,urea,creatinine,alt,bilirubins,alkaline_phosphatase,albumin,calcium,calcium_corrected,est_gfr;
    private String [] date_addedArray,creative_proteinArray,ironArray,transferrinArray,satn_transferrinArray,phosphateArray,bicarbonateArray,ferritinArray,glucoseArray;
    private String [] magnesiumArray,sodiumArray,potassiumArray,ureaArray,creatinineArray,altArray,bilirubinsArray,alkaline_phosphataseArray,albuminArray,calciumArray,calcium_correctedArray,est_gfrArray;

    private String date_addedToast,creative_proteinToast,ironToast,transferrinToast,satn_transferrinToast,phosphateToast,bicarbonateToast,ferritinToast,glucoseToast;
    private String magnesiumToast,sodiumToast,potassiumToast,ureaToast,creatinineToast,altToast,bilirubinsToast,alkaline_phosphataseToast,albuminToast,calciumToast,calcium_correctedToast,est_gfrToast;


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

                 //blood test descriptions
                date_addedToast=getResources().getString(R.string.healthcheck_date_added_desc);
                creative_proteinToast=getResources().getString(R.string.healthcheck_date_added_desc);
                ironToast=getResources().getString(R.string.healthcheck_date_added_desc);
                transferrinToast=getResources().getString(R.string.healthcheck_date_added_desc);
                satn_transferrinToast=getResources().getString(R.string.healthcheck_date_added_desc);
                phosphateToast=getResources().getString(R.string.healthcheck_date_added_desc);
                bicarbonateToast=getResources().getString(R.string.healthcheck_date_added_desc);
                ferritinToast=getResources().getString(R.string.healthcheck_date_added_desc);
                glucoseToast=getResources().getString(R.string.healthcheck_date_added_desc);

                magnesiumToast=getResources().getString(R.string.healthcheck_date_added_desc);
                sodiumToast=getResources().getString(R.string.healthcheck_date_added_desc);
                potassiumToast=getResources().getString(R.string.healthcheck_date_potassium_desc);
                ureaToast=getResources().getString(R.string.healthcheck_date_added_desc);
                creatinineToast=getResources().getString(R.string.healthcheck_date_added_desc);
                altToast=getResources().getString(R.string.healthcheck_date_added_desc);
                bilirubinsToast=getResources().getString(R.string.healthcheck_date_added_desc);
                alkaline_phosphataseToast=getResources().getString(R.string.healthcheck_date_added_desc);
                albuminToast=getResources().getString(R.string.healthcheck_date_added_desc);
                calciumToast=getResources().getString(R.string.healthcheck_date_added_desc);
                calcium_correctedToast=getResources().getString(R.string.healthcheck_date_added_desc);
                est_gfrToast=getResources().getString(R.string.healthcheck_date_added_desc);




        //onClick listeners for showing toast with bloodtest description
        date_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description


            }
        });

        creative_protein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        date_addedToast, Toast.LENGTH_LONG).show();

            }
        });

        iron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                //toast with description
                Toast.makeText(getApplicationContext(),
                        ironToast, Toast.LENGTH_LONG).show();

            }
        });
        transferrin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        transferrinToast, Toast.LENGTH_LONG).show();

            }
        });
        satn_transferrin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        satn_transferrinToast, Toast.LENGTH_LONG).show();

            }
        });
        phosphate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        phosphateToast, Toast.LENGTH_LONG).show();

            }
        });
        bicarbonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        bicarbonateToast, Toast.LENGTH_LONG).show();

            }
        });


        ferritin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        ferritinToast, Toast.LENGTH_LONG).show();

            }
        });
        glucose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        glucoseToast, Toast.LENGTH_LONG).show();

            }
        });
        magnesium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        magnesiumToast, Toast.LENGTH_LONG).show();

            }
        });
        sodium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        sodiumToast, Toast.LENGTH_LONG).show();

            }
        });
        potassium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        potassiumToast, Toast.LENGTH_LONG).show();

            }
        });
        urea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        ureaToast, Toast.LENGTH_LONG).show();

            }
        });
        creatinine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        creatinineToast, Toast.LENGTH_LONG).show();

            }
        });
        alt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        altToast, Toast.LENGTH_LONG).show();

            }
        });
        bilirubins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        bilirubinsToast, Toast.LENGTH_LONG).show();

            }
        });
        alkaline_phosphatase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        alkaline_phosphataseToast, Toast.LENGTH_LONG).show();

            }
        });
        albumin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        albuminToast, Toast.LENGTH_LONG).show();

            }
        });
        calcium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        calciumToast, Toast.LENGTH_LONG).show();

            }
        });
        calcium_corrected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        calcium_correctedToast, Toast.LENGTH_LONG).show();

            }
        });
        est_gfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        est_gfrToast, Toast.LENGTH_LONG).show();

            }
        });



        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();
        uid =userData.get("uid").toString();
        Log.d(tag, "Request UID: " + userData.get("uid").toString());

        requestMedHistory(uid);



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
                        //for (int a = 0; a < eCountInt; a++) {
                            //getting medical data from hashmap
                            //add in array for view_graphs
                        date_added.setText( med_history_map.get("date_added" + entryCount));

                        creative_protein.setText( med_history_map.get("creative_protein" + entryCount)+"mg/L");
                        iron.setText( med_history_map.get("iron" + entryCount)+"umol/L");
                        transferrin.setText( med_history_map.get("transferrin" + entryCount)+"g/L");
                        satn_transferrin.setText(  med_history_map.get("satn_transferrin" + entryCount)+"%");
                        phosphate.setText(med_history_map.get("phosphate" + entryCount)+"mmol/L");
                        bicarbonate.setText(  med_history_map.get("bicarbonate" + entryCount)+"mmol/L");
                        ferritin.setText( med_history_map.get("ferritin" + entryCount)+"ug/L");
                        glucose.setText( med_history_map.get("glucose" + entryCount)+"mmol/L");
                        magnesium.setText( med_history_map.get("magnesium" + entryCount)+"mmol/L");

                        sodium.setText( med_history_map.get("sodium" + entryCount)+"mmol/L");
                        potassium.setText( med_history_map.get("potassium" + entryCount)+"mmol/L");
                        urea.setText( med_history_map.get("urea" + entryCount)+"mmol/L");
                        creatinine.setText( med_history_map.get("creatinine" + entryCount)+"umol/L");
                        alt.setText( med_history_map.get("alt" + entryCount)+"U/L");
                        bilirubins.setText( med_history_map.get("bilirubins" + entryCount)+"umol/L");
                        alkaline_phosphatase.setText( med_history_map.get("alkaline_phosphatase" + entryCount)+"U/L");
                        albumin.setText(med_history_map.get("albumin" + entryCount)+"g/L");
                        calcium.setText(  med_history_map.get("calcium" + entryCount)+"mmol/L");
                        calcium_corrected.setText( med_history_map.get("calcium_corrected" + entryCount)+"mmol/L");
                        est_gfr.setText( med_history_map.get("est_gfr" + entryCount)+"mL/min");


                            //assign to textviews



                            //output to console for debugging
                            Log.d(tag, "dateadded"+entryCount +": " + date_added);

                            Log.d(tag, "Number of entries (eCount) :" + eCount);



                            //}
                            //entryCount++;

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
        Intent intent = new Intent(HealthcheckCheckHealth.this, LoginActivity.class);
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
