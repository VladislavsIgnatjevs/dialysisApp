package vladislavsignatjevs.renaldyalisys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.text.Html;
import android.text.Spanned;

import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class Treatment extends Activity {
    private static final String tag = Treatment.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private ViewFlipper vFlip;
    private VideoView videoView;
    TextView fluidHtml,foodHtml;

    CounterClass timer;
    TextView timeLeftValue;
    TimePickerDialog timePickerDialog;
    final static int RQS_1 = 1;
    TimePicker myTimePicker;
    Button setTimerDialogButton;
    Button startTreatmentTimerButton, abandonTreatmentButton;





    private Button startPreparation, handsNext,fluidNext,foodNext,startTreatment,endTreatment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);


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


        // Fetching user details from sqlite
        HashMap<String, String> userData = db.getUserDetails();

        Log.d(tag, "Request UID: " + userData.get("uid").toString());

        //view flipper

        vFlip = (ViewFlipper) findViewById( R.id.viewFlipper );
        startPreparation = (Button) findViewById(R.id.button_start_prep);
        handsNext = (Button) findViewById(R.id.button_next_step_hands);
        fluidNext = (Button) findViewById(R.id.button_next_step_fluid);
        foodNext = (Button) findViewById(R.id.button_next_step_food);
        startTreatment = (Button) findViewById(R.id.button_start_treatment);
        //setting videoView
        videoView = (VideoView)findViewById(R.id.washHandsVideo);
        Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hand_washing_converted);
        //setting washing hands video
        videoView.setVideoURI(video1);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();






        // get html strings
        String fluid_text_html = getString(R.string.treatment_prep_fluid_text_html);
        String food_text_html = getString(R.string.treatment_prep_food_text_html);
        //making spanned for displaying as html for those strings
        Spanned fluid_text_spanned = Html.fromHtml(fluid_text_html); // used by textViewFluidHtml
        Spanned food_text_spanned = Html.fromHtml(food_text_html); // used by textViewFoodHtml

        // set the html content on a food and fluid parts of layout
        fluidHtml = (TextView) findViewById(R.id.textViewFluidHtml);
        foodHtml = (TextView) findViewById(R.id.textViewFoodHtml);
        fluidHtml.setText(fluid_text_spanned);
        foodHtml.setText(food_text_spanned);



        //on button start preparation show next

        startPreparation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vFlip.showNext();








            }
        });
        //onClick for video view
//        videoView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//
//                videoView.start();
//            }
//        });
        //in wash hands on button next step show next
        handsNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vFlip.showNext();
            }
        });
        //in fluids on button next step show next
        fluidNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vFlip.showNext();
            }
        });
        //in food on button next step show next

        foodNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vFlip.showNext();
            }
        });

        startTreatment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vFlip.showNext();
            }
        });






        startTreatmentTimerButton = (Button) findViewById(R.id.button_timer_set);
        abandonTreatmentButton = (Button) findViewById(R.id.button_abandon_treatment);
        timeLeftValue = (TextView) findViewById(R.id.treatment_second_stage_timer_value);




        startTreatmentTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog(false);

            }
        });
        abandonTreatmentButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                timer.cancel();
            }
        });


        setTimerDialogButton = (Button) findViewById(R.id.begin);
        setTimerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.start();
                vFlip.showNext();
            }
        });




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
        Intent intent = new Intent(Treatment.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Author: noev
     * http://stackoverflow.com/posts/34125164/revisions
     *
     */

    private void openTimePickerDialog(boolean is24r) {


        timePickerDialog = new TimePickerDialog(
                Treatment.this,
                onTimeSetListener,
                4,
                0,
                is24r);
        timePickerDialog.setTitle("Set dialysis time");

        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timer = new CounterClass((minute * 60 * 1000) + (hourOfDay * 60* 60 * 1000), 1000);
            long millis = (minute * 60 * 1000) + (hourOfDay * 60* 60 * 1000);
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);
            timeLeftValue.setText(hms + "left" );

        }};


    public class CounterClass extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);
            timeLeftValue.setText(hms);

        }

        @Override
        public void onFinish() {

        }



}}

