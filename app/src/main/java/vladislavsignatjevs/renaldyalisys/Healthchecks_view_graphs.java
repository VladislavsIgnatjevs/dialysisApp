package vladislavsignatjevs.renaldyalisys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private String eCount, uid;
    private TextView date_added, creative_protein, iron, transferrin, satn_transferrin, phosphate, bicarbonate, ferritin, glucose, healthcheck_verdict;
    private TextView magnesium, sodium, potassium, urea, creatinine, alt, bilirubins, alkaline_phosphatase, albumin, calcium, calcium_corrected, est_gfr;
    private ArrayList<Entry> date_addedArray, creative_proteinArray, ironArray, transferrinArray, satn_transferrinArray, phosphateArray, bicarbonateArray, ferritinArray, glucoseArray;
    private ArrayList<Entry> magnesiumArray, sodiumArray, potassiumArray, ureaArray, creatinineArray, altArray, bilirubinsArray, alkaline_phosphataseArray, albuminArray, calciumArray, calcium_correctedArray, est_gfrArray;

    ArrayList<String> xVals = new ArrayList<String>();

    private String date_addedToast, creative_proteinToast, ironToast, transferrinToast, satn_transferrinToast, phosphateToast, bicarbonateToast, ferritinToast, glucoseToast;
    private String magnesiumToast, sodiumToast, potassiumToast, ureaToast, creatinineToast, altToast, bilirubinsToast, alkaline_phosphataseToast, albuminToast, calciumToast, calcium_correctedToast, est_gfrToast;


    private TextView date_addedTitle, creative_proteinTitle, ironTitle, transferrinTitle, satn_transferrinTitle, phosphateTitle, bicarbonateTitle, ferritinTitle, glucoseTitle;
    private TextView magnesiumTitle, sodiumTitle, potassiumTitle, ureaTitle, creatinineTitle, altTitle, bilirubinsTitle, alkaline_phosphataseTitle, albuminTitle, calciumTitle, calcium_correctedTitle, est_gfrTitle;

    private String date_addedValue, creative_proteinValue, ironValue, transferrinValue, satn_transferrinValue, phosphateValue, bicarbonateValue, ferritinValue, glucoseValue;
    private String magnesiumValue, sodiumValue, potassiumValue, ureaValue, creatinineValue, altValue, bilirubinsValue, alkaline_phosphataseValue, albuminValue, calciumValue, calcium_correctedValue, est_gfrValue;
    AlertDialog.Builder alertDialogBuilder;

    private LineChart creative_proteinChart, ironChart, transferrinChart, satn_transferrinChart, phosphateChart, bicarbonateChart, ferritinChart, glucoseChart;
    private LineChart magnesiumChart, sodiumChart, potassiumChart, ureaChart, creatinineChart, altChart, bilirubinsChart, alkaline_phosphataseChart, albuminChart, calciumChart, calcium_correctedChart, est_gfrChart;


    String savedResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthchecks_view_graphs);


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




        //get textviews for titles
        date_addedTitle = (TextView) this.findViewById(R.id.healthcheck_date_added_title);
        creative_proteinTitle = (TextView) this.findViewById(R.id.healthcheck_creative_protein_title);
        ironTitle = (TextView) this.findViewById(R.id.healthcheck_iron_title);
        transferrinTitle = (TextView) this.findViewById(R.id.healthcheck_transferrin_title);
        satn_transferrinTitle = (TextView) this.findViewById(R.id.healthcheck_satn_transferrin_title);
        phosphateTitle = (TextView) this.findViewById(R.id.healthcheck_phosphate_title);
        bicarbonateTitle = (TextView) this.findViewById(R.id.healthcheck_bicarbonate_title);
        ferritinTitle = (TextView) this.findViewById(R.id.healthcheck_ferritin_title);
        glucoseTitle = (TextView) this.findViewById(R.id.healthcheck_glucose_title);
        magnesiumTitle = (TextView) this.findViewById(R.id.healthcheck_magnesium_title);
        sodiumTitle = (TextView) this.findViewById(R.id.healthcheck_sodium_title);
        potassiumTitle = (TextView) this.findViewById(R.id.healthcheck_potassium_title);
        ureaTitle = (TextView) this.findViewById(R.id.healthcheck_urea_title);

        creatinineTitle = (TextView) this.findViewById(R.id.healthcheck_creatinine_title);
        altTitle = (TextView) this.findViewById(R.id.healthcheck_alt_title);
        bilirubinsTitle = (TextView) this.findViewById(R.id.healthcheck_bilirubins_title);
        alkaline_phosphataseTitle = (TextView) this.findViewById(R.id.healthcheck_alkaline_phosphatase_title);
        albuminTitle = (TextView) this.findViewById(R.id.healthcheck_albumin_title);
        calciumTitle = (TextView) this.findViewById(R.id.healthcheck_calcium_title);
        calcium_correctedTitle = (TextView) this.findViewById(R.id.healthcheck_calcium_corrected_title);
        est_gfrTitle = (TextView) this.findViewById(R.id.healthcheck_gfr_title);


        //get textviews for values from layout
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
        date_addedToast = getResources().getString(R.string.healthcheck_date_added_desc);
        creative_proteinToast = getResources().getString(R.string.healthcheck_creative_protein_desc);
        ironToast = getResources().getString(R.string.healthcheck_iron_desc);
        transferrinToast = getResources().getString(R.string.healthcheck_transferrin_desc);
        satn_transferrinToast = getResources().getString(R.string.healthcheck_satn_transferrin_desc);
        phosphateToast = getResources().getString(R.string.healthcheck_phosphate_desc);
        bicarbonateToast = getResources().getString(R.string.healthcheck_bicarbonate_desc);
        ferritinToast = getResources().getString(R.string.healthcheck_ferritin_desc);
        glucoseToast = getResources().getString(R.string.healthcheck_glucose_desc);

        magnesiumToast = getResources().getString(R.string.healthcheck_magnesium_desc);
        sodiumToast = getResources().getString(R.string.healthcheck_sodium_desc);
        potassiumToast = getResources().getString(R.string.healthcheck_potassium_desc);
        ureaToast = getResources().getString(R.string.healthcheck_urea_desc);
        creatinineToast = getResources().getString(R.string.healthcheck_creatinine_desc);
        altToast = getResources().getString(R.string.healthcheck_alt_desc);
        bilirubinsToast = getResources().getString(R.string.healthcheck_bilirubins_desc);
        alkaline_phosphataseToast = getResources().getString(R.string.healthcheck_alkaline_phosphatase_desc);
        albuminToast = getResources().getString(R.string.healthcheck_albumin_desc);
        calciumToast = getResources().getString(R.string.healthcheck_calcium_desc);
        calcium_correctedToast = getResources().getString(R.string.healthcheck_calcium_corrected_desc);
        est_gfrToast = getResources().getString(R.string.healthcheck_date_gfr_desc);

        healthcheck_verdict = (TextView) this.findViewById(R.id.healthcheck_verdict);



        //finding charts
        creative_proteinChart = (LineChart) this.findViewById(R.id.creative_protein_chart);
        ironChart = (LineChart) this.findViewById(R.id.iron_chart);
        transferrinChart = (LineChart) this.findViewById(R.id.transferrin_chart);
        satn_transferrinChart = (LineChart) this.findViewById(R.id.satn_transferrin_chart);
        phosphateChart = (LineChart) this.findViewById(R.id.phosphate_chart);
        bicarbonateChart = (LineChart) this.findViewById(R.id.bicarbonate_chart);
        ferritinChart = (LineChart) this.findViewById(R.id.ferritin_chart);
        glucoseChart = (LineChart) this.findViewById(R.id.glucose_chart);
        magnesiumChart = (LineChart) this.findViewById(R.id.magnesium_chart);
        sodiumChart = (LineChart) this.findViewById(R.id.sodium_chart);
        potassiumChart = (LineChart) this.findViewById(R.id.potassium_chart);
        ureaChart = (LineChart) this.findViewById(R.id.urea_chart);

        creatinineChart = (LineChart) this.findViewById(R.id.creatinine_chart);
        altChart = (LineChart) this.findViewById(R.id.alt_chart);
        bilirubinsChart = (LineChart) this.findViewById(R.id.bilirubins_chart);
        alkaline_phosphataseChart = (LineChart) this.findViewById(R.id.alkaline_phosphatase_chart);
        albuminChart = (LineChart) this.findViewById(R.id.albumin_chart);
        calciumChart = (LineChart) this.findViewById(R.id.calcium_chart);
        calcium_correctedChart = (LineChart) this.findViewById(R.id.calcium_corrected_chart);
        est_gfrChart = (LineChart) this.findViewById(R.id.gfr_chart);

        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();
        uid = userData.get("uid").toString();
        Log.d(tag, "Request UID: " + userData.get("uid").toString());

        requestMedHistoryCharts(uid);



    }

    /**
     * request medical history for charts via post
     */
    private void requestMedHistoryCharts(final String email) {
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
                        savedResponse = savedResponse.replace("{", "");
                        savedResponse = savedResponse.replace("}", "");
                        //  savedResponse = savedResponse.replace("\"}}", "");
                        savedResponse = savedResponse.replace("\"error\":false,\"data\":\"", "");
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

                        //for loop to sets of results from hashmap
                        for (int a = 0; a < eCountInt; a++) {
                        //getting medical data from hashmap
                        //add in array lists for charts
                        //c-reactive protein chart
                        int cProtValue = Integer.parseInt(med_history_map.get("creative_protein" + entryCount));
                        date_addedValue = med_history_map.get("date_added" + entryCount);
                        Entry creactiveEntry = new Entry(cProtValue, entryCount);
                        creative_proteinArray.add(creactiveEntry);

                            LineDataSet setComp1 = new LineDataSet(creative_proteinArray, "C-reactive protein");
                            setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(setComp1);
                            xVals.add(date_addedValue);

                            LineData data = new LineData(xVals, dataSets);
                            creative_proteinChart.setData(data);
                            creative_proteinChart.invalidate();



                        creative_proteinValue = med_history_map.get("creative_protein" + entryCount);


                        //assign to textviews


                        //output to console for debugging
                        Log.d(tag, "dateadded" + entryCount + ": " + date_added);

                        Log.d(tag, "Number of entries (eCount) :" + eCount);


                        }
                        entryCount++;

                    } else {

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
