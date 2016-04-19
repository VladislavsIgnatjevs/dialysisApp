package vladislavsignatjevs.renaldyalisys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import vladislavsignatjevs.renaldyalisys.R;
import vladislavsignatjevs.renaldyalisys.app.AppConfig;
import vladislavsignatjevs.renaldyalisys.app.AppController;
import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;

public class Healthcheck extends Activity {
    String savedResponse;
    Map<String,String> profile = new HashMap<String,String>();



    private Button checkHealthButton;
    private Button viewGraphsButton;

    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private static final String tag = Healthcheck.class.getSimpleName();


    String eventName, eventTime, eventDescription, eventDate, eCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcheck);

        //progress dialogue
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        checkHealthButton = (Button) findViewById(R.id.checkHealthButton);
        viewGraphsButton = (Button) findViewById(R.id.viewGraphsButton);

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

        checkHealthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Launching the login activity
                Intent intent = new Intent(Healthcheck.this, HealthcheckCheckHealth.class);
                startActivity(intent);
            }
        });

        viewGraphsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Launching the login activity
                Intent intent = new Intent(Healthcheck.this, Healthchecks_view_graphs.class);
                startActivity(intent);
            }
        });


       // requestProfile(userData.get("uid"));



    }





    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Healthcheck.this, LoginActivity.class);
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
