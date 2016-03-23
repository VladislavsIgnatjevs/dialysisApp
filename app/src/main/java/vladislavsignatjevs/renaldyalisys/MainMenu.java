package vladislavsignatjevs.renaldyalisys;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import vladislavsignatjevs.renaldyalisys.helper.SQLiteHandler;
import vladislavsignatjevs.renaldyalisys.helper.SessionManager;


public class MainMenu extends AppCompatActivity {
    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        List<ItemObject> allItems = getAllItemObject();
        CustomAdapter customAdapter = new CustomAdapter(MainMenu.this, allItems);
        gridview.setAdapter(customAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(MainMenu.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                //swiitch statement to define which activity to start depending on position

                switch (position) {
                    case 0: Intent treatment = new Intent(MainMenu.this, Treatment.class);
                            startActivity(treatment);
                            break;
                    case 1: Intent profile = new Intent(MainMenu.this, Profile.class);
                            startActivity(profile);
                            break;
                    case 2: Intent healthcheck = new Intent(MainMenu.this, Healthcheck.class);
                            startActivity(healthcheck);
                            break;
                    case 3: Intent calendar = new Intent(MainMenu.this, Calendar.class);
                            startActivity(calendar);
                            break;
                    case 4: Intent contacts = new Intent(MainMenu.this, Contacts.class);
                            startActivity(contacts);
                            break;
                    case 5: Intent faq = new Intent(MainMenu.this, FAQ.class);
                            startActivity(faq);
                            break;
                    case 6: logoutUser();
                        break;
                }

            }
        });
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private List<ItemObject> getAllItemObject(){
        ItemObject itemObject = null;
        List<ItemObject> items = new ArrayList<>();
        items.add(new ItemObject("Start Treatment", "one"));
        items.add(new ItemObject("My Details", "two"));
        items.add(new ItemObject("Check", "three"));
        items.add(new ItemObject("Calendar", "four"));
        items.add(new ItemObject("Contacts", "five"));
        items.add(new ItemObject("FaQ", "six"));
        items.add(new ItemObject("Logout", "seven"));
        return items;
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainMenu.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}