package vladislavsignatjevs.renaldyalisys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class Treatment extends Activity {
    private static final String tag = Treatment.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private ViewFlipper vFlip;
    private VideoView videoView;
    private String videoURL = "android.resource://com.my.package/raw/hand_washing.mp4";

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
        videoView.setVideoURI(Uri.parse(videoURL));
        videoView.setMediaController(new MediaController(this));



        //on button start preparation show next

        startPreparation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vFlip.showNext();



                videoView.requestFocus();
                videoView.start();




            }
        });

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

}

