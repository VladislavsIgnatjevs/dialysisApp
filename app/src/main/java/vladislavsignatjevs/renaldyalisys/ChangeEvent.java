/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package vladislavsignatjevs.renaldyalisys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.app.Dialog;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import vladislavsignatjevs.renaldyalisys.R;
import vladislavsignatjevs.renaldyalisys.app.AppConfig;
import vladislavsignatjevs.renaldyalisys.app.AppController;
import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class ChangeEvent extends Activity {
    private static final String TAG = ChangeEvent.class.getSimpleName();
    private Button updateEvent;
    private EditText eventName;
    private EditText eventDetails;
    private TextView eventDate;
    private TextView eventStartTime;
    private TextView eventEndTime;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    int DIALOG_DATE = 1;
    int DIALOG_STARTTIME = 2;
    int DIALOG_ENDTIME = 3;
    int selectedYear,selectedMonth, selectedDay,selectedHoursStart,selectedMinutesStart, selectedHoursEnd, selectedMinutesEnd;
    String dbFormatDate,checkFormatDate, calendarFormatDate, dbFormatStartTime, dbFormatEndTime, calendarFormatStartTime, calendarFormatEndTime, currentDate,currentTime, textStartTime,textEndTime;
    String name_old,details_old,date_old,start_old,end_old;
    Date checkStartTime,checkEndTime, checkCurrentDate, checkEventDate, checkCurrentTime;
    Calendar _calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_event);
        _calendar = Calendar.getInstance(Locale.getDefault());
        //set default selected day,month,year hours minutes as current calendar readings
        selectedDay = _calendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = _calendar.get(Calendar.MONTH);
        Log.d(TAG, "current month" + selectedMonth);
        selectedYear = _calendar.get(Calendar.YEAR);
        selectedHoursStart = _calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinutesStart = _calendar.get(Calendar.MINUTE);
        selectedHoursEnd = _calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinutesEnd = _calendar.get(Calendar.MINUTE);

        //get layouts
        eventName = (EditText) findViewById(R.id.change_event_name);
        eventDetails = (EditText) findViewById(R.id.change_event_description);
        eventDate = (TextView) findViewById(R.id.change_event_date);
        eventStartTime = (TextView) findViewById(R.id.change_event_starttime);
        eventEndTime = (TextView) findViewById(R.id.change_event_endtime);
        //set existing values
        String name = getIntent().getStringExtra("eventName");
        name_old=name;
        String details = getIntent().getStringExtra("eventDescription");
        details_old=details;
        String date = getIntent().getStringExtra("eventDate");
        date_old=date;
        String eventStart = getIntent().getStringExtra("eventStartTime");
        start_old = eventStart;
        String eventEnd = getIntent().getStringExtra("eventEndTime");
        end_old=eventEnd;

        //convert getExtra start time and end time to UK format
        try {

            SimpleDateFormat input = new SimpleDateFormat("HH:mm:ss");
            Date dt = input.parse(eventStart);
            Date et = input.parse(eventEnd);

            SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
            String formattedStartTime = output.format(dt);
            String formattedEndTime = output.format(et);
            textStartTime = formattedStartTime;
            textEndTime = formattedEndTime;
        } catch (ParseException e) {
            //handle exception
        }

        eventName.setText(name);
        eventDetails.setText(details);
        eventDate.setText(date);
        eventStartTime.setText(textStartTime);
        eventEndTime.setText(textEndTime);


        updateEvent = (Button) findViewById(R.id.button_change_event);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }



        //onclick listener for date to start datepicker
        eventDate.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                showDialog(DIALOG_DATE);
            }
        });
        //Selecting start time using
        eventStartTime.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                showDialog(DIALOG_STARTTIME);
            }
        });
        //selecting end time
        eventEndTime.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                showDialog(DIALOG_ENDTIME);
            }
        });


        // Add event button
        updateEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String name = eventName.getText().toString().trim();
                String details = eventDetails.getText().toString().trim();
                String date = eventDate.getText().toString().trim();
                String start = eventStartTime.getText().toString().trim();
                String end = eventEndTime.getText().toString().trim();



                // Check for empty data in the form
                if (!name.isEmpty() && !details.isEmpty() && !date.isEmpty() && !start.isEmpty() && !end.isEmpty()) {
                    //convert both date strings to Date object

                    currentDate = (_calendar.get(Calendar.YEAR)+"-"+_calendar.get(Calendar.MONTH)+"-"+_calendar.get(Calendar.DAY_OF_MONTH));
                    currentTime = (_calendar.get(Calendar.HOUR_OF_DAY)+":"+_calendar.get(Calendar.MINUTE));
                    try {

                        SimpleDateFormat input = new SimpleDateFormat("h:mm a");
                        checkStartTime = input.parse(start);
                        checkEndTime = input.parse(end);
                        SimpleDateFormat currentDateInput = new SimpleDateFormat("yyyy-MM-dd");
                        checkCurrentDate = currentDateInput.parse(currentDate);
                        checkEventDate = currentDateInput.parse(checkFormatDate);
                        SimpleDateFormat calendarDateInput = new SimpleDateFormat("dd-MMM-yyyy");

                        SimpleDateFormat currentTimeInput = new SimpleDateFormat("HH:mm");
                        checkCurrentTime = currentTimeInput.parse(currentTime);

                    } catch (ParseException e) {
                        //handle exception
                    }




                    //check if start date is earlier than today
                    if (checkEventDate.compareTo(checkCurrentDate)<0)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Date cannot be earlier than today.", Toast.LENGTH_LONG)
                                .show();

                    }
                    //check if starttime is later than endtime
                    else if (checkStartTime.compareTo(checkEndTime) >0)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Start time cannot be later than End time.", Toast.LENGTH_LONG)
                                .show();

                    }
                    //check if start time is later than current time
                    else if (checkEventDate.compareTo(checkCurrentDate)==0 && checkStartTime.compareTo(checkCurrentTime)< 0)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Start time cannot be earlier than now.", Toast.LENGTH_LONG)
                                .show();
                    }
                    else if (checkStartTime.compareTo(checkEndTime)== 0)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Start time and End time cannot be same.", Toast.LENGTH_LONG)
                                .show();
                    }



                    // add event in background to the main calendar(with event reminding in 24 hours
                    //add event to database
                    // Fetching user details from sqlite
                    HashMap<String, String> userData = db.getUserDetails();

                    Log.d(TAG, "Request UID: " + userData.get("uid").toString());

                    updateEvent(userData.get("uid"), name,name_old, details,details_old, dbFormatDate,date_old, dbFormatStartTime,start_old, dbFormatEndTime,end_old);

                    //finish end exit to calendar


                } else {
                    // Output errors to users
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty. All fields are required.", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });


    }


    //date picker and time picker

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, dateCallBack, selectedYear, selectedMonth, selectedDay);
            return tpd;
        }
        //change to false to include AM/PM?
        if (id == DIALOG_STARTTIME) {
            TimePickerDialog tst = new TimePickerDialog(this, startTimeCallBack, selectedHoursStart, selectedMinutesStart, true);
            return tst;
        }
        //change to false to include AM/PM?
        if (id == DIALOG_ENDTIME) {
            TimePickerDialog ted = new TimePickerDialog(this, endTimeCallBack, selectedHoursEnd, selectedMinutesEnd, true);
            return ted;
        }
        return super.onCreateDialog(id);
    }

    OnDateSetListener dateCallBack = new OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            //calendar.get(MONTH) starts month count from 0, which causes an error so add 1 to monthOfYear
            dbFormatDate = (year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            //preserve original format for if statements
            checkFormatDate = (year+"-"+monthOfYear+"-"+dayOfMonth);
            //convert time to format that is best for UK users
            try {

                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = input.parse(dbFormatDate);




                SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = output.format(dt);
                calendarFormatDate = formattedDate;

            } catch (ParseException e) {
                //handle exception
            }

            eventDate.setText(calendarFormatDate);
        }
    };

    OnTimeSetListener startTimeCallBack = new OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //adding millis for db format
            dbFormatStartTime = (hourOfDay+":"+minute+":00");
            //convert time to format that is best for UK
            try {

                SimpleDateFormat input = new SimpleDateFormat("HH:mm:ss");
                Date dt = input.parse(dbFormatStartTime);


                SimpleDateFormat output = new SimpleDateFormat("h:mm a");
                String formattedTime = output.format(dt);
                calendarFormatStartTime = formattedTime;
            } catch (ParseException e) {
                //handle exception
            }

            eventStartTime.setText(calendarFormatStartTime);
        }
    };
    OnTimeSetListener endTimeCallBack = new OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //adding millis for db format
            dbFormatEndTime = (hourOfDay+":"+minute+":00");
            //convert time to format that is best for UK
            try {

                SimpleDateFormat input = new SimpleDateFormat("HH:mm:ss");
                Date dt = input.parse(dbFormatEndTime);


                SimpleDateFormat output = new SimpleDateFormat("h:mm a");
                String formattedTime = output.format(dt);
                calendarFormatEndTime = formattedTime;
            } catch (ParseException e) {
                //handle exception
            }
            eventEndTime.setText(calendarFormatEndTime);
        }
    };

    private void updateEvent(final String uid, final String name,final String name_old, final String details,final String details_old, final String date,final String date_old, final String start,final String start_old, final String end, final String end_old )
    {

        // Tag used to cancel the request
        String tag_string_req = "req_create_event";

        pDialog.setMessage("Updating event ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.CHANGE_EVENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Change Event response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //event added toast

                        Toast.makeText(getApplicationContext(),
                                "Event was successfully updated!.", Toast.LENGTH_LONG)
                                .show();


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Event Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("uid", uid);
                params.put("name", name);
                params.put("name_old", name_old);
                params.put("details", details);
                params.put("details_old", details_old);
                params.put("date", date);
                params.put("date_old", date_old);
                params.put("start", start);
                params.put("start_old", start_old);
                params.put("end_old", end_old);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(ChangeEvent.this, CalendarEvents.class);
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

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ChangeEvent.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Add event to default GOOGLE calendar
     * */
//    private void addEvent(final String eventName, final String eventDescription, final String eventDate, final String eventStartTime, final String eventEndTime) {
//        ContentValues l_event = new ContentValues();
//        l_event.put("calendar_id", 1);//default calendar
//        l_event.put("title", eventName);
//        l_event.put("description", eventDescription);
//        l_event.put("eventLocation", "");
//        l_event.put("dtstart", System.currentTimeMillis());
//        l_event.put("dtend", System.currentTimeMillis() + 1800 * 1000);
//        l_event.put("eventTimezone", TimeZone.getDefault().getID());
//        l_event.put("allDay", 0);
//        //status: 0~ tentative; 1~ confirmed; 2~ canceled
//        l_event.put("eventStatus", 1);
//        //0~ default; 1~ confidential; 2~ private; 3~ public
//        //    l_event.put("visibility", 0);
//        //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
//        //     l_event.put("transparency", 0);
//        //0~ false; 1~ true
//        l_event.put("hasAlarm", 1);
//        Uri l_eventUri;
//        if (Build.VERSION.SDK_INT >= 8) {
//            l_eventUri = Uri.parse("content://com.android.calendar/events");
//        } else {
//            l_eventUri = Uri.parse("content://calendar/events");
//        }
//        Uri l_uri = this.getContentResolver().insert(l_eventUri, l_event);
//        Log.v("++++++test", l_uri.toString());
//    }

}

