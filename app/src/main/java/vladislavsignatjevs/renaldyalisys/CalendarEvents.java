package vladislavsignatjevs.renaldyalisys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.DayViewAdapter;

import vladislavsignatjevs.renaldyalisys.app.AppConfig;
import vladislavsignatjevs.renaldyalisys.app.AppController;
import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.view.View.OnClickListener;


import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import com.squareup.timessquare.DefaultDayViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;



class MyCalendar {
    public String name;
    public String id;
    public MyCalendar(String _name, String _id) {
        name = _name;
        id = _id;
    }
    @Override
    public String toString() {
        return name;
    }
}

public class CalendarEvents extends Activity implements OnClickListener {
    //db helpers
    private SQLiteHandler db;
    private SessionManager session;

    String savedResponse;
    Map<String, String> events = new HashMap<String, String>();
    String eventName, eventTime, eventDescription, eventDate, eCount;

    /*********************************************************************
     * UI part
     */
    private Spinner m_spinner_calender;
    private Button m_button_add;
    private Button m_button_add2;
    private Button m_button_getEvents;
    private TextView m_text_event;
    private static final String tag = FAQ.class.getSimpleName();
    private ProgressDialog pDialog;
    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    @SuppressLint("NewApi")
    private int month, year;
    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //progress dialogue
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();

        Log.d(tag, "Request UID: " + userData.get("uid").toString());
     //   requestEvents(userData.get("uid"));
        /*get calendar list and populate the view*/
        getCalendars();
     //   populateCalendarSpinner();
     //   populateAddBtn();
        populateAddBtn2();
        //  populateTextEvent();
      //  populateGetEventsBtn();




        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Instance " + "Month: " + month + " " + "Year: " + year);

        selectedDayMonthYearButton = (Button) this
                .findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);


    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(CalendarEvents.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

//    private void populateCalendarSpinner() {
//        m_spinner_calender = (Spinner) this.findViewById(R.id.spinner_calendar);
//        ArrayAdapter l_arrayAdapter = new ArrayAdapter(this.getApplicationContext(), android.R.layout.simple_spinner_item, m_calendars);
//        l_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        m_spinner_calender.setAdapter(l_arrayAdapter);
//        m_spinner_calender.setSelection(0);
//        m_spinner_calender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> p_parent, View p_view,
//                                       int p_pos, long p_id) {
//                m_selectedCalendarId = m_calendars[(int) p_id].id;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        });
//    }

//    private void populateAddBtn() {
//        m_button_add = (Button) this.findViewById(R.id.button_add);
//        m_button_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //start activity CreateEvent
//                Intent intent = new Intent(CalendarEvents.this, CreateEvent.class);
//                startActivity(intent);
//
//            }
//        });
//    }

    private void populateAddBtn2() {
        m_button_add2 = (Button) this.findViewById(R.id.addEvent);
        m_button_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarEvents.this, CreateEvent.class);
                startActivity(intent);
            }
        });
    }

//    private void populateGetEventsBtn() {
//        m_button_getEvents = (Button) findViewById(R.id.button_get_events);
//        m_button_getEvents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLastThreeEvents();
//            }
//        });
//    }
/*    private void populateTextEvent() {
        m_text_event = (TextView) findViewById(R.id.text_event);
        String l_str = "title: roman10 calendar tutorial test\n" +
                "description: This is a simple test for calendar api\n" +
                "eventLocation: @home\n" +
                "start time:" + getDateTimeStr(0) + "\n" +
                "end time: " + getDateTimeStr(30) + "\n" +
                "event status: confirmed\n" +
                "all day: no\n" +
                "has alarm: yes\n";
        m_text_event.setText(l_str);
    }*/
    /****************************************************************
     * Data part
     */
    /*retrieve a list of available calendars*/
    private MyCalendar m_calendars[];
    private String m_selectedCalendarId = "0";

    private void getCalendars() {
        String[] l_projection = new String[]{"_id", CalendarContract.Calendars.CALENDAR_DISPLAY_NAME};
        Uri l_calendars;
        if (Build.VERSION.SDK_INT >= 8) {
            l_calendars = Uri.parse("content://com.android.calendar/calendars");
        } else {
            l_calendars = Uri.parse("content://calendar/calendars");
        }
        Cursor l_managedCursor = this.managedQuery(l_calendars, l_projection, null, null, null);    //all calendars
        //Cursor l_managedCursor = this.managedQuery(l_calendars, l_projection, "selected=1", null, null);   //active calendars
        if (l_managedCursor.moveToFirst()) {
            m_calendars = new MyCalendar[l_managedCursor.getCount()];
            String l_calName;
            String l_calId;
            int l_cnt = 0;
            int l_nameCol = l_managedCursor.getColumnIndex(l_projection[1]);
            int l_idCol = l_managedCursor.getColumnIndex(l_projection[0]);
            do {
                l_calName = l_managedCursor.getString(l_nameCol);
                l_calId = l_managedCursor.getString(l_idCol);
                m_calendars[l_cnt] = new MyCalendar(l_calName, l_calId);
                ++l_cnt;
            } while (l_managedCursor.moveToNext());
        }
    }

    /*add an event to calendar*/
    private void addEvent() {
        ContentValues l_event = new ContentValues();
        l_event.put("calendar_id", m_selectedCalendarId);
        l_event.put("title", "roman10 calendar tutorial test");
        l_event.put("description", "This is a simple test for calendar api");
        l_event.put("eventLocation", "@home");
        l_event.put("dtstart", System.currentTimeMillis());
        l_event.put("dtend", System.currentTimeMillis() + 1800 * 1000);
        l_event.put("eventTimezone", TimeZone.getDefault().getID());
        l_event.put("allDay", 0);
        //status: 0~ tentative; 1~ confirmed; 2~ canceled
        l_event.put("eventStatus", 1);
        //0~ default; 1~ confidential; 2~ private; 3~ public
        //    l_event.put("visibility", 0);
        //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
        //     l_event.put("transparency", 0);
        //0~ false; 1~ true
        l_event.put("hasAlarm", 1);
        Uri l_eventUri;
        if (Build.VERSION.SDK_INT >= 8) {
            l_eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            l_eventUri = Uri.parse("content://calendar/events");
        }
        Uri l_uri = this.getContentResolver().insert(l_eventUri, l_event);
        Log.v("++++++test", l_uri.toString());
    }

    /*add an event through intent, this doesn't require any permission
     * just send intent to android calendar
     * http://www.openintents.org/en/uris*/
    private void addEvent2() {
        Intent l_intent = new Intent(Intent.ACTION_EDIT);
        l_intent.setType("vnd.android.cursor.item/event");
        //l_intent.putExtra("calendar_id", m_selectedCalendarId);  //this doesn't work
        l_intent.putExtra("title", "roman10 calendar tutorial test");
        l_intent.putExtra("description", "This is a simple test for calendar api");
        l_intent.putExtra("eventLocation", "@home");
        l_intent.putExtra("beginTime", System.currentTimeMillis());
        l_intent.putExtra("endTime", System.currentTimeMillis() + 1800 * 1000);
        l_intent.putExtra("allDay", 0);
        //status: 0~ tentative; 1~ confirmed; 2~ canceled
        l_intent.putExtra("eventStatus", 1);
        //0~ default; 1~ confidential; 2~ private; 3~ public
        l_intent.putExtra("visibility", 0);
        //0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
        l_intent.putExtra("transparency", 0);
        //0~ false; 1~ true
        l_intent.putExtra("hasAlarm", 1);
        try {
            startActivity(l_intent);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
        }
    }

    /*get a list of events
     * http://jimblackler.net/blog/?p=151*/
    private void getLastThreeEvents() {
        Uri l_eventUri;
        if (Build.VERSION.SDK_INT >= 8) {
            l_eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            l_eventUri = Uri.parse("content://calendar/events");
        }
        String[] l_projection = new String[]{"title", "dtstart", "dtend"};
        Cursor l_managedCursor = this.managedQuery(l_eventUri, l_projection, "calendar_id=" + m_selectedCalendarId, null, "dtstart DESC, dtend DESC");
        //Cursor l_managedCursor = this.managedQuery(l_eventUri, l_projection, null, null, null);
        if (l_managedCursor.moveToFirst()) {
            int l_cnt = 0;
            String l_title;
            String l_begin;
            String l_end;
            StringBuilder l_displayText = new StringBuilder();
            int l_colTitle = l_managedCursor.getColumnIndex(l_projection[0]);
            int l_colBegin = l_managedCursor.getColumnIndex(l_projection[1]);
            int l_colEnd = l_managedCursor.getColumnIndex(l_projection[1]);
            do {
                l_title = l_managedCursor.getString(l_colTitle);
                l_begin = getDateTimeStr(l_managedCursor.getString(l_colBegin));
                l_end = getDateTimeStr(l_managedCursor.getString(l_colEnd));
                l_displayText.append(l_title + "\n" + l_begin + "\n" + l_end + "\n----------------\n");
                ++l_cnt;
            } while (l_managedCursor.moveToNext() && l_cnt < 3);
            m_text_event.setText(l_displayText.toString());
        }
    }

    /************************************************
     * utility part
     */
    private static final String DATE_TIME_FORMAT = "yyyy MMM dd, HH:mm:ss";

    public static String getDateTimeStr(int p_delay_min) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        if (p_delay_min == 0) {
            return sdf.format(cal.getTime());
        } else {
            Date l_time = cal.getTime();
            l_time.setMinutes(l_time.getMinutes() + p_delay_min);
            return sdf.format(l_time);
        }
    }

    public static String getDateTimeStr(String p_time_in_millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date l_time = new Date(Long.parseLong(p_time_in_millis));
        return sdf.format(l_time);
    }


    /**
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }

    }

    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying View ...");
        super.onDestroy();
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        int checkEvents =0 ;
        String dbFormat;
        String calendarFormat;

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();






            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }


        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();


            //fetching user from db
            // SqLite database handler
            db = new SQLiteHandler(getApplicationContext());


            // Fetching user details from sqlite
            HashMap<String, String> userData = db.getUserDetails();

            Log.d(tag, "Request UID GETNUMEVENTS: " + userData.get("uid").toString());
            requestEvents(userData.get("uid"));

            return map;
        }






        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {





            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.dialysis_calendar_screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            java.util.Date calendarDate = new Date(theday + "-" + themonth + "-" + theyear);
            SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dbFormat = dbFormatter.format(calendarDate);
            calendarFormat = (theday + "-" + themonth + "-" + theyear);
            //logging for debug
            Log.d(tag, "dbFormat: " + dbFormat + " cFormat " + calendarFormat);


//            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
//                if (eventsPerMonthMap.containsKey(theday)) {
//                    num_events_per_day = (TextView) row
//                            .findViewById(R.id.num_events_per_day);
//                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
//                    num_events_per_day.setText(numEvents.toString());
//                }
//            }

            //converting calendar appearing date in db format date
           // int checkEvents = getNumberOfEvents(dbFormat);

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);
            //current prev month
            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources()
                        .getColor(R.color.gray));
                if (checkEvents >0)
                {
                    gridcell.setTypeface(Typeface.create(gridcell.getTypeface(),Typeface.BOLD));
                }
            }
            //current month
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(
                        R.color.dark));
                if (checkEvents >0)
                {
                    gridcell.setTypeface(Typeface.create(gridcell.getTypeface(),Typeface.BOLD));
                }
            }
            //current day
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.sky));
                if (checkEvents >0)
                {
                    gridcell.setTypeface(Typeface.create(gridcell.getTypeface(),Typeface.BOLD));
                }
            }
            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            selectedDayMonthYearButton.setText("Selected: " + date_month_year);



            Intent eventsView = new Intent(CalendarEvents.this, calendar_events_view.class);
            eventsView.putExtra("date", date_month_year);
            CalendarEvents.this.startActivity(eventsView);

            Log.e("Selected date", date_month_year);
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);
                Log.d(tag, "Parsed Date: " + parsedDate.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
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
                            //retrieve events
                            savedResponse = response.toString();
                            Log.d(tag, "SAVED RESPONSE WITH RUBBISH TEST " + savedResponse);
                            //removing rubbish from string
                            savedResponse = savedResponse.replace("{","");
                            savedResponse = savedResponse.replace("}", "");
                          //  savedResponse = savedResponse.replace("\"}}", "");
                            savedResponse = savedResponse.replace("\"error\":false,\"data\":\"","");
                            Log.d(tag, "SAVED RESPONSE NO RUBBISH " + savedResponse);
                            //split response by "," and put questions and answers into hashmap events

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

                            //   TableLayout table = (TableLayout) findViewById(R.id.faq_table);
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
                                if (eventDate.equals(dbFormat))
                                {
                                    checkEvents++;

                                }
                                //output to console for debugging
                                Log.d(tag, "EVENT"+eventCount +" NAME IS  " + eventName);
                                Log.d(tag, "EVENT"+eventCount +" TIME IS  " + eventTime);
                                Log.d(tag, "EVENT"+eventCount +" DATE IS :" + eventDate);
                                Log.d(tag, "EVENT"+eventCount +" DESCRIPTION IS :" + eventDescription);
                                Log.d(tag, "EVENTS COUNT IS :" + eCount);

                              eventCount++;

                            }


                        } else {

                            // Error occurred in events retrieval. Get the error
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
    }




    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




    public int getNumberOfEvents(String date){



        int eventCount = 1;
        int eventsNum=0;
        //fetching number of events from hashmap
        eCount = events.get("date1");
        Log.d(tag, "get number of events ecount  " + eCount);

        //replacing rubbish
        eCount = eCount.replace("\"", "");
        //trimming that string and parsing int that will be used as a counter
        int eCountInt = Integer.parseInt(eCount.trim());
        //for loop to get all questions from hashmap
        for (int a = 0; a < eCountInt; a++) {
            //getting events from hashmap
            eventDate = events.get("date" + eventCount);

        }
        return eventsNum;
    }
}