package vladislavsignatjevs.renaldyalisys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vladislavsignatjevs.renaldyalisys.app.AppConfig;
import vladislavsignatjevs.renaldyalisys.app.AppController;
import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class calendar_events_view extends AppCompatActivity {
    private TextView viewDate;
    private static final String tag = FAQ.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;

    String savedResponse;
    Map<String, String> events = new HashMap<String, String>();
    String eventName, eventTime, eventDescription, eventDate, eCount, dbFormatDate;
    int checkEvents =0 ;
    int numberingOfEvent=1;
    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_events_view);
        String date = getIntent().getStringExtra("date");
        viewDate = (TextView) this.findViewById(R.id.calendar_eventsview_date);
        Log.d(tag, "Date: " +date);
        viewDate.setText("Date: "+date);

        //getting dbFormat date
        Date calendarDate = new Date(date);
        SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dbFormatDate = dbFormatter.format(calendarDate);

        //progress dialogue
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();

        Log.d(tag, "Request UID: " + userData.get("uid").toString());
        requestEvents(userData.get("uid"));


        createEventButton = (Button) this.findViewById(R.id.button_add_new_event);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start activity CreateEvent
                Intent intent = new Intent(calendar_events_view.this, CreateEvent.class);
                startActivity(intent);

            }
        });
    }
    /**
     * request events via post
     */
    private void requestEvents(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_events";

        pDialog.setMessage("Getting Events...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.EVENTS_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(tag, "Request Events response: " + response.toString());


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
                            events.put(keyValue[0], keyValue[1]);
                        }
                        //output hashmap into log
                        for (Map.Entry entry : events.entrySet()) {
                            Log.d(tag, "key: " + entry.getKey() + " value " + entry.getValue());
                        }

                          TableLayout table = (TableLayout) findViewById(R.id.calendar_events_main_table);
                        int eventCount = 1;


                        //fetching number of events from hashmap
                        eCount = events.get("eCount");
                        //replacing rubbish
                        eCount = eCount.replace("\\", "");
                        eCount = eCount.replace("\"", "");
                        //trimming that string and parsing int that will be used as a counter
                        int eCountInt = Integer.parseInt(eCount.trim());
                        checkEvents=0;
                        //for loop to get all questions from hashmap
                        for (int a = 0; a < eCountInt; a++) {
                            //getting events from hashmap
                            eventName = events.get("name" + eventCount);
                            eventDescription = events.get("description" + eventCount);
                            eventTime = events.get("time" + eventCount);
                            eventDate = events.get("date" + eventCount);

                            //     eventDescription = eventDescription.replace("<koma>",",");
                            //    eventName = eventName.replace("<koma>",",");
                            //output to console for debugging
                            Log.d(tag, "EVENT"+eventCount +" NAME IS  " + eventName);
                            Log.d(tag, "EVENT"+eventCount +" TIME IS  " + eventTime);
                            Log.d(tag, "EVENT"+eventCount +" DATE IS :" + eventDate);
                            Log.d(tag, "EVENT"+eventCount +" DESCRIPTION IS :" + eventDescription);
                            Log.d(tag, "EVENTS COUNT IS :" + eCount);
                            if (dbFormatDate.equals(eventDate))
                            {
                                try {

                                SimpleDateFormat input = new SimpleDateFormat("HH:mm:ss");
                                Date dt = input.parse(eventTime);


                                SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
                                String formattedDate = output.format(dt);
                                eventTime = formattedDate;
                                } catch (ParseException e) {
                                    //handle exception
                                }
                                //  Displaying events on the screen
                                //setting event title to display in a row with a number of event on the day(events sorted by time on server side query
                                TableRow row = new TableRow(calendar_events_view.this);
                                //making unique ids for each row and text view
                                row.setId(eventCount + 10);
                                TextView eventTitle = new TextView(calendar_events_view.this);
                                eventTitle.setId(eventCount + 11);
                                //use numberingOfEvent to present events in a list using numbering 1 to max value of eventCount
                                eventTitle.setText(numberingOfEvent + ": " + eventName);
                                eventTitle.setTextSize(18);
                                eventTitle.setBackgroundColor(Color.LTGRAY);
                                eventTitle.setPadding(10, 10, 10, 10);
                                row.addView(eventTitle);
                                table.addView(row, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                //setting event time to display in a row
                                TableRow row1 = new TableRow(calendar_events_view.this);
                                //making unique ids for each row and text view
                                row1.setId(eventCount + 12);
                                TextView timeOfEvent = new TextView(calendar_events_view.this);
                                timeOfEvent.setId(eventCount + 13);
                                timeOfEvent.setText("Starts at: " + eventTime);
                                timeOfEvent.setBackgroundColor(getResources().getColor(R.color.lightgray));
                                timeOfEvent.setTextSize(18);
                                timeOfEvent.setPadding(10, 10, 10, 10);
                                row1.addView(timeOfEvent);
                                table.addView(row1, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                //setting event time to display in a row
                                TableRow row3 = new TableRow(calendar_events_view.this);
                                //making unique ids for each row and text view
                                row3.setId(eventCount + 14);
                                TextView descriptionEvent = new TextView(calendar_events_view.this);
                                descriptionEvent.setId(eventCount + 15);
                                descriptionEvent.setText("Details: " + eventDescription);
                                descriptionEvent.setBackgroundColor(getResources().getColor(R.color.lightgray02));
                                descriptionEvent.setTextSize(18);
                                descriptionEvent.setPadding(10, 10, 10, 10);
                                row3.addView(descriptionEvent);
                                table.addView(row3, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                //empty space between question-answer
                                TableRow row2 = new TableRow(calendar_events_view.this);
                                row2.setId(eventCount + 13);
                                row2.setPadding(10, 10, 10, 10);
                                table.addView(row2, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                numberingOfEvent++;
                            }
                                eventCount++;

                        }

                        table.requestLayout();
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
                Log.e(tag, "Faq retrieval error: " + error.getMessage());
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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}


