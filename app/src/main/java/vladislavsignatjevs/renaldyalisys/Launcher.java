package vladislavsignatjevs.renaldyalisys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Launcher extends Activity {


    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
//        dialog=new ProgressDialog(this);
//        dialog.setMessage("Loading");
//        dialog.setCancelable(false);
//        dialog.setInverseBackgroundForced(false);
//        dialog.show();
        //timer for first screen
        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
               // dialog.hide();
                Intent intent = new Intent(Launcher.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();



    }
}
