package vladislavsignatjevs.renaldyalisys;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import vladislavsignatjevs.renaldyalisys.R;
import vladislavsignatjevs.renaldyalisys.app.AppConfig;
import vladislavsignatjevs.renaldyalisys.app.AppController;
import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class FAQ extends Activity {
    String savedResponse;
    Map<String,String> faq = new HashMap<String,String>();
    String quest,ans,qCount;
    private TextView faq_heading;
    private TextView faq_question_heading;
    private TextView faq_question;
    private TextView faq_answer_heading;
    private TextView faq_answer;

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private static final String TAG = FAQ.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

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

        requestFaq();
    }

    /**
     * request faq via post
     *
     * */
    private void requestFaq()
    {
        // Tag used to cancel the request
        String tag_string_req = "req_faq";

        pDialog.setMessage("Getting FAQ ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FAQ_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Request FAQ response: " + response.toString());



                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        //retrieve faq
                        savedResponse =  response.toString();
                        Log.d(TAG, "SAVED RESPONSE WITH RUBBISH TEST "+savedResponse );
                        //removing rubbish from string
                        savedResponse = savedResponse.replace("{","");
                        savedResponse = savedResponse.replace("}", "");
                        //  savedResponse = savedResponse.replace("\"}}", "");
                        savedResponse = savedResponse.replace("\"error\":false,\"data\":\"","");
                        Log.d(TAG, "SAVED RESPONSE NO RUBBISH "+savedResponse );
                        //split response by "," and put questions and answers into hashmap faq
                        String[] pairs = savedResponse.split("\",\"");
                        for (int i=0;i<pairs.length;i++) {
                            String pair = pairs[i];

                            String[] keyValue = pair.split("\":\"");
                            Log.d(TAG, "keyvalue 0: " +keyValue[0] +" keyvalue1 "+keyValue[1] );
                            faq.put(keyValue[0], keyValue[1]);
                        }
                        //output hashmap into log
                        for (Map.Entry entry : faq.entrySet()){
                            Log.d(TAG, "key: " +entry.getKey() +" value "+entry.getValue() );
                        }

                        TableLayout table = (TableLayout) findViewById(R.id.faq_table);
                        int questionCount = 1;



                        //fetching number of questions from hashmap
                        qCount = faq.get("qCount");
                        //replacing rubbish
                        qCount = qCount.replace("\\", "");
                        qCount=qCount.replace("\"","");
                        //trimming that string and parsing int that will be used as a counter
                        int qCountInt = Integer.parseInt(qCount.trim());
                        //for loop to get all questions from hashmap
                        for(int a=0; a<qCountInt; a++)
                        {
                            //getting questions from hashmap
                            quest = faq.get("question"+questionCount);
                            ans = faq.get("answer"+questionCount);

                            //output to console for debugging
                            Log.d(TAG, "QUESTION IS  "+quest );
                            Log.d(TAG, "ANSWER IS  "+ans );
                            Log.d(TAG, "COUNT IS :"+qCount );
                            //  Displaying question/answer on the screen
                            //setting question to display in a row
                            TableRow row = new TableRow(FAQ.this);
                            //making unique ids for each row and text view
                            row.setId(questionCount + 10);
                            TextView question = new TextView(FAQ.this);
                            question.setId(questionCount + 11);
                            question.setText(quest);
                            question.setTextSize(18);
                            question.setBackgroundColor(Color.WHITE);
                            question.setPadding(10, 10, 10, 10);
                            question.setCompoundDrawablesWithIntrinsicBounds(R.drawable.question_icon, 0, 0, 0);
                            row.addView(question);
                            table.addView(row, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                            //empty space between question and answer
                            TableRow separator = new TableRow(FAQ.this);
                            separator.setId(questionCount + 14);
                            separator.setPadding(10, 10, 10, 10);

                            table.addView(separator, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));



                            //setting answer to display in a row
                            TableRow row1 = new TableRow(FAQ.this);
                            //making unique ids for each row and text view
                            row1.setId(questionCount+12);
                            TextView answer = new TextView(FAQ.this);
                            answer.setId(questionCount + 13);
                            answer.setText(ans);
                            answer.setBackgroundColor(Color.WHITE);
                            answer.setTextSize(18);
                            answer.setPadding(10, 10, 10, 10);
                            answer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.answer_icon,0 );
                            row1.addView(answer);
                            table.addView(row1, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                            //empty space between question-answer
                            TableRow row2 = new TableRow(FAQ.this);
                            row2.setId(questionCount+13);
                            row2.setPadding(10, 40, 40, 10);

                            table.addView(row2, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            questionCount++;

                        }

                        table.requestLayout();
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
                Log.e(TAG, "Faq retrieval error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) ;

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
        Intent intent = new Intent(FAQ.this, LoginActivity.class);
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
