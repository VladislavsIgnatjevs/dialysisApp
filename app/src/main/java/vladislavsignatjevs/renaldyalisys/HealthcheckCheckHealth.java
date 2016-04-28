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
    private TextView date_added, creative_protein, iron, transferrin, satn_transferrin, phosphate, bicarbonate, ferritin, glucose, healthcheck_verdict;
    private TextView magnesium, sodium, potassium, urea, creatinine, alt, bilirubins, alkaline_phosphatase, albumin, calcium, calcium_corrected, est_gfr;
    private String[] date_addedArray, creative_proteinArray, ironArray, transferrinArray, satn_transferrinArray, phosphateArray, bicarbonateArray, ferritinArray, glucoseArray;
    private String[] magnesiumArray, sodiumArray, potassiumArray, ureaArray, creatinineArray, altArray, bilirubinsArray, alkaline_phosphataseArray, albuminArray, calciumArray, calcium_correctedArray, est_gfrArray;

    private String date_addedToast, creative_proteinToast, ironToast, transferrinToast, satn_transferrinToast, phosphateToast, bicarbonateToast, ferritinToast, glucoseToast;
    private String magnesiumToast, sodiumToast, potassiumToast, ureaToast, creatinineToast, altToast, bilirubinsToast, alkaline_phosphataseToast, albuminToast, calciumToast, calcium_correctedToast, est_gfrToast;


    private TextView date_addedTitle, creative_proteinTitle, ironTitle, transferrinTitle, satn_transferrinTitle, phosphateTitle, bicarbonateTitle, ferritinTitle, glucoseTitle;
    private TextView magnesiumTitle, sodiumTitle, potassiumTitle, ureaTitle, creatinineTitle, altTitle, bilirubinsTitle, alkaline_phosphataseTitle, albuminTitle, calciumTitle, calcium_correctedTitle, est_gfrTitle;

    private String date_addedValue, creative_proteinValue, ironValue, transferrinValue, satn_transferrinValue, phosphateValue, bicarbonateValue, ferritinValue, glucoseValue;
    private String magnesiumValue, sodiumValue, potassiumValue, ureaValue, creatinineValue, altValue, bilirubinsValue, alkaline_phosphataseValue, albuminValue, calciumValue, calcium_correctedValue, est_gfrValue;
    AlertDialog.Builder alertDialogBuilder;

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


        //onClick listeners for showing toast with bloodtest description
        date_addedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        date_addedToast, Toast.LENGTH_LONG).show();

            }
        });

        creative_proteinTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        creative_proteinToast, Toast.LENGTH_LONG).show();

            }
        });

        ironTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        ironToast, Toast.LENGTH_LONG).show();

            }
        });
        transferrinTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        transferrinToast, Toast.LENGTH_LONG).show();

            }
        });
        satn_transferrinTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        satn_transferrinToast, Toast.LENGTH_LONG).show();

            }
        });
        phosphateTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        phosphateToast, Toast.LENGTH_LONG).show();

            }
        });
        bicarbonateTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        bicarbonateToast, Toast.LENGTH_LONG).show();

            }
        });


        ferritinTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        ferritinToast, Toast.LENGTH_LONG).show();

            }
        });
        glucoseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        glucoseToast, Toast.LENGTH_LONG).show();

            }
        });
        magnesiumTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        magnesiumToast, Toast.LENGTH_LONG).show();

            }
        });
        sodiumTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        sodiumToast, Toast.LENGTH_LONG).show();

            }
        });
        potassiumTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        potassiumToast, Toast.LENGTH_LONG).show();

            }
        });
        ureaTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        ureaToast, Toast.LENGTH_LONG).show();

            }
        });
        creatinineTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        creatinineToast, Toast.LENGTH_LONG).show();

            }
        });
        altTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        altToast, Toast.LENGTH_LONG).show();

            }
        });
        bilirubinsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        bilirubinsToast, Toast.LENGTH_LONG).show();

            }
        });
        alkaline_phosphataseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        alkaline_phosphataseToast, Toast.LENGTH_LONG).show();

            }
        });
        albuminTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        albuminToast, Toast.LENGTH_LONG).show();

            }
        });
        calciumTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        calciumToast, Toast.LENGTH_LONG).show();

            }
        });
        calcium_correctedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        calcium_correctedToast, Toast.LENGTH_LONG).show();

            }
        });
        est_gfrTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast with description
                Toast.makeText(getApplicationContext(),
                        est_gfrToast, Toast.LENGTH_LONG).show();

            }
        });


        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();
        uid = userData.get("uid").toString();
        Log.d(tag, "Request UID: " + userData.get("uid").toString());

        requestMedHistory(uid);



    }

    /**
     * request medical history via post
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
                        //for (int a = 0; a < eCountInt; a++) {
                        //getting medical data from hashmap
                        //add in array for view_graphs
                        date_addedValue = med_history_map.get("date_added" + entryCount);
                        date_added.setText(date_addedValue);


                        creative_proteinValue = med_history_map.get("creative_protein" + entryCount);
                        if (Integer.parseInt(creative_proteinValue) > 10 || Integer.parseInt(creative_proteinValue) < 0)
                        //bold white with red bg
                        {
                            creative_protein.setText(creative_proteinValue + "mg/L");
                            creative_protein.setTypeface(Typeface.create(creative_protein.getTypeface(), Typeface.BOLD));
                            creative_protein.setTextColor(getResources().getColor(R.color.darkred));
                            //     creative_protein.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            creative_protein.setText(creative_proteinValue + "mg/L");
                        }

                        ironValue = med_history_map.get("iron" + entryCount);

                        if (Integer.parseInt(ironValue) > 35 || Integer.parseInt(ironValue) < 7)
                        //bold white with red bg
                        {
                            iron.setText(ironValue + "umol/L");
                            iron.setTypeface(Typeface.create(iron.getTypeface(), Typeface.BOLD));
                            iron.setTextColor(getResources().getColor(R.color.darkred));
                            //  iron.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            iron.setText(ironValue + "umol/L");
                        }


                        transferrinValue = med_history_map.get("transferrin" + entryCount);
                        if (Double.parseDouble(transferrinValue) > 4 || Double.parseDouble(transferrinValue) < 2)
                        //bold white with red bg
                        {
                            transferrin.setText(transferrinValue + "g/L");
                            transferrin.setTypeface(Typeface.create(transferrin.getTypeface(), Typeface.BOLD));
                            transferrin.setTextColor(getResources().getColor(R.color.darkred));
                            //  transferrin.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            transferrin.setText(transferrinValue + "g/L");
                        }


                        satn_transferrinValue = med_history_map.get("satn_transferrin" + entryCount);
                        if (Double.parseDouble(satn_transferrinValue) > 55 || Double.parseDouble(satn_transferrinValue) < 22)
                        //bold white with red bg
                        {
                            satn_transferrin.setText(satn_transferrinValue + "%");
                            satn_transferrin.setTypeface(Typeface.create(satn_transferrin.getTypeface(), Typeface.BOLD));
                            satn_transferrin.setTextColor(getResources().getColor(R.color.darkred));
                            // satn_transferrin.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            satn_transferrin.setText(satn_transferrinValue + "%");
                        }


                        phosphateValue = med_history_map.get("phosphate" + entryCount);
                        if (Double.parseDouble(phosphateValue) > 1.5 || Double.parseDouble(phosphateValue) < 0.8)
                        //bold white with red bg
                        {
                            phosphate.setText(phosphateValue + "mmol/L");
                            phosphate.setTypeface(Typeface.create(phosphate.getTypeface(), Typeface.BOLD));
                            phosphate.setTextColor(getResources().getColor(R.color.darkred));
                            //  phosphate.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            phosphate.setText(phosphateValue + "mmol/L");
                        }


                        bicarbonateValue = med_history_map.get("bicarbonate" + entryCount);
                        if (Double.parseDouble(bicarbonateValue) > 29 || Double.parseDouble(bicarbonateValue) < 22)
                        //bold white with red bg
                        {
                            bicarbonate.setText(bicarbonateValue + "mmol/L");
                            bicarbonate.setTypeface(Typeface.create(bicarbonate.getTypeface(), Typeface.BOLD));
                            bicarbonate.setTextColor(getResources().getColor(R.color.darkred));
                            // bicarbonate.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            bicarbonate.setText(bicarbonateValue + "mmol/L");
                        }


                        ferritinValue = med_history_map.get("ferritin" + entryCount);
                        if (Double.parseDouble(ferritinValue) > 400 || Double.parseDouble(ferritinValue) < 30)
                        //bold white with red bg
                        {
                            ferritin.setText(ferritinValue + "ug/L");
                            ferritin.setTypeface(Typeface.create(ferritin.getTypeface(), Typeface.BOLD));
                            ferritin.setTextColor(getResources().getColor(R.color.darkred));
                            // ferritin.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            ferritin.setText(ferritinValue + "ug/L");
                        }


                        glucoseValue = med_history_map.get("glucose" + entryCount);
                        if (Double.parseDouble(glucoseValue) > 5.8 || Double.parseDouble(glucoseValue) < 3.3)
                        //bold white with red bg
                        {
                            glucose.setText(glucoseValue + "mmol/L");
                            glucose.setTypeface(Typeface.create(glucose.getTypeface(), Typeface.BOLD));
                            glucose.setTextColor(getResources().getColor(R.color.darkred));
                            //  glucose.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            glucose.setText(glucoseValue + "mmol/L");
                        }


                        magnesiumValue = med_history_map.get("magnesium" + entryCount);
                        if (Double.parseDouble(magnesiumValue) > 1 || Double.parseDouble(magnesiumValue) < 0.7)
                        //bold white with red bg
                        {
                            magnesium.setText(magnesiumValue + "mmol/L");
                            magnesium.setTypeface(Typeface.create(magnesium.getTypeface(), Typeface.BOLD));
                            magnesium.setTextColor(getResources().getColor(R.color.darkred));
                            // magnesium .setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            magnesium.setText(magnesiumValue + "mmol/L");
                        }


                        sodiumValue = med_history_map.get("sodium" + entryCount);
                        if (Double.parseDouble(sodiumValue) > 146 || Double.parseDouble(sodiumValue) < 133)
                        //bold white with red bg
                        {
                            sodium.setText(sodiumValue + "mmol/L");
                            sodium.setTypeface(Typeface.create(sodium.getTypeface(), Typeface.BOLD));
                            sodium.setTextColor(getResources().getColor(R.color.darkred));
                            // sodium.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            sodium.setText(sodiumValue + "mmol/L");
                        }


                        potassiumValue = med_history_map.get("potassium" + entryCount);
                        if (Double.parseDouble(potassiumValue) > 5.3 || Double.parseDouble(potassiumValue) < 3.5)
                        //bold white with red bg
                        {
                            potassium.setText(potassiumValue + "mmol/L");
                            potassium.setTypeface(Typeface.create(potassium.getTypeface(), Typeface.BOLD));
                            potassium.setTextColor(getResources().getColor(R.color.darkred));
                            // potassium.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            potassium.setText(potassiumValue + "mmol/L");
                        }


                        ureaValue = med_history_map.get("urea" + entryCount);
                        if (Double.parseDouble(ureaValue) > 7.8 || Double.parseDouble(ureaValue) < 2.5)
                        //bold white with red bg
                        {
                            urea.setText(ureaValue + "mmol/L");
                            urea.setTypeface(Typeface.create(urea.getTypeface(), Typeface.BOLD));
                            urea.setTextColor(getResources().getColor(R.color.darkred));
                            //urea.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            urea.setText(ureaValue + "mmol/L");
                        }


                        creatinineValue = med_history_map.get("creatinine" + entryCount);
                        if (Double.parseDouble(creatinineValue) > 1000 || Double.parseDouble(creatinineValue) < 44)
                        //normal value for dialisys <1000 >44
                        //bold white with red bg
                        {
                            creatinine.setText(creatinineValue + "umol/L");
                            creatinine.setTypeface(Typeface.create(creatinine.getTypeface(), Typeface.BOLD));
                            creatinine.setTextColor(getResources().getColor(R.color.darkred));
                            // creatinine.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            creatinine.setText(creatinineValue + "umol/L");
                        }


                        altValue = med_history_map.get("alt" + entryCount);
                        if (Double.parseDouble(altValue) > 55 || Double.parseDouble(altValue) < 5)
                        //bold white with red bg
                        {
                            alt.setText(altValue + "U/L");
                            alt.setTypeface(Typeface.create(alt.getTypeface(), Typeface.BOLD));
                            alt.setTextColor(getResources().getColor(R.color.darkred));
                            //  alt.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            alt.setText(altValue + "U/L");
                        }


                        bilirubinsValue = med_history_map.get("bilirubins" + entryCount);
                        if (Double.parseDouble(bilirubinsValue) > 21 || Double.parseDouble(bilirubinsValue) < 0)
                        //bold white with red bg
                        {
                            bilirubins.setText(bilirubinsValue + "umol/L");
                            bilirubins.setTypeface(Typeface.create(bilirubins.getTypeface(), Typeface.BOLD));
                            bilirubins.setTextColor(getResources().getColor(R.color.darkred));
                            //  bilirubins.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            bilirubins.setText(bilirubinsValue + "umol/L");
                        }


                        alkaline_phosphataseValue = med_history_map.get("alkaline_phosphatase" + entryCount);
                        if (Double.parseDouble(alkaline_phosphataseValue) > 130 || Double.parseDouble(alkaline_phosphataseValue) < 30)
                        //bold red
                        {
                            alkaline_phosphatase.setText(alkaline_phosphataseValue + "U/L");
                            alkaline_phosphatase.setTypeface(Typeface.create(alkaline_phosphatase.getTypeface(), Typeface.BOLD));
                            alkaline_phosphatase.setTextColor(getResources().getColor(R.color.darkred));
                            // alkaline_phosphatase.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            alkaline_phosphatase.setText(alkaline_phosphataseValue + "U/L");
                        }


                        albuminValue = med_history_map.get("albumin" + entryCount);
                        if (Double.parseDouble(albuminValue) > 50 || Double.parseDouble(albuminValue) < 35)
                        //bold white with red bg
                        {
                            albumin.setText(albuminValue + "g/L");
                            albumin.setTypeface(Typeface.create(albumin.getTypeface(), Typeface.BOLD));
                            albumin.setTextColor(getResources().getColor(R.color.darkred));
                            // albumin.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            albumin.setText(albuminValue + "g/L");
                        }


                        calciumValue = med_history_map.get("calcium" + entryCount);
                        if (Double.parseDouble(calciumValue) > 2.55 || Double.parseDouble(calciumValue) < 2.10)
                        //bold white with red bg
                        {
                            calcium.setText(calciumValue + "mmol/L");
                            calcium.setTypeface(Typeface.create(calcium.getTypeface(), Typeface.BOLD));
                            calcium.setTextColor(getResources().getColor(R.color.darkred));
                            // calcium.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            calcium.setText(calciumValue + "mmol/L");
                        }


                        calcium_correctedValue = med_history_map.get("calcium_corrected" + entryCount);
                        if (Double.parseDouble(calcium_correctedValue) > 2.55 || Double.parseDouble(calcium_correctedValue) < 2.10)
                        //bold white with red bg
                        {
                            calcium_corrected.setText(calcium_correctedValue + "mmol/L");
                            calcium_corrected.setTypeface(Typeface.create(calcium_corrected.getTypeface(), Typeface.BOLD));
                            calcium_corrected.setTextColor(getResources().getColor(R.color.darkred));
                            //calcium_corrected.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            calcium_corrected.setText(calcium_correctedValue + "mmol/L");
                        }


                        est_gfrValue = med_history_map.get("est_gfr" + entryCount);
                        if (Double.parseDouble(est_gfrValue) < 15)
                        //bold white with red bg
                        //normal value for dialisys <15
                        {
                            est_gfr.setText(est_gfrValue + "mL/min");
                            est_gfr.setTypeface(Typeface.create(est_gfr.getTypeface(), Typeface.BOLD));
                            est_gfr.setTextColor(getResources().getColor(R.color.darkred));
                            // est_gfr.setBackgroundColor(getResources().getColor(R.color.darkred));
                            healthcheck_verdict.setText("Failed");
                            healthcheck_verdict.setTextColor(getResources().getColor(R.color.red));


                        } else {
                            est_gfr.setText(est_gfrValue + "mL/min");
                        }


                        //assign to textviews


                        //output to console for debugging
                        Log.d(tag, "dateadded" + entryCount + ": " + date_added);

                        Log.d(tag, "Number of entries (eCount) :" + eCount);

                        if (healthcheck_verdict.getText().toString().equals("Failed"))
                        {
                            checkIfEmergency();
                        }
                        //}
                        //entryCount++;

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

    //checking if healthcheck failed and outputting options to call nhs24 or access contacts
    private void checkIfEmergency()
    {

        alertDialogBuilder = new AlertDialog.Builder(HealthcheckCheckHealth.this);
        alertDialogBuilder.setTitle("Are you feeling unwell?");
        alertDialogBuilder.setMessage("Dialysis Check has detected that you have failed your last health check. If you are feeling unwell, you can call NHS24 now.");
        alertDialogBuilder.setPositiveButton("Call NHS24", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent caller = new Intent(Intent.ACTION_DIAL);
                caller.setData(Uri.parse("tel:" + 111));
                startActivity(caller);
            }
        });

//        alertDialogBuilder.setNeutralButton("Access contacts",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(HealthcheckCheckHealth.this, Contacts.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);
        alertDialogBuilder.show();

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
