package com.enchcorp.screenshare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DetailsActivity extends AppCompatActivity {
    SessionManager session;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // logout = (Button) findViewById(R.id.logout);
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            session.setLogin(false);
            Intent intent = new Intent(DetailsActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
        ((EditText) findViewById(R.id.whiteboardName)).setText(Utils.genWhiteboardName());

        // Display the randomly generated username
        ((EditText) findViewById(R.id.userName)).setText(Utils.generateEnglishNames(this));

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the whiteboard
                onStartClicked();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void onStartClicked() {
        // Check for errors
        boolean error = false;
        if (((EditText) findViewById(R.id.userName)).getText().toString().trim().length() == 0) {
            ((EditText) findViewById(R.id.userName)).setHintTextColor(Color.RED);
            error = true;
        }
        if (((EditText) findViewById(R.id.whiteboardName)).getText().toString().trim().length() == 0) {
            ((EditText) findViewById(R.id.whiteboardName)).setHintTextColor(Color.RED);
            error = true;
        }
        if (error) {
            return;
        }

        // Create whiteboard activity intent
        Intent WhiteboardActivityIntent = new Intent(this, WhiteboardActivity.class);
        // Send necessary parameters
        WhiteboardActivityIntent
                .putExtra("name", ((EditText) findViewById(R.id.userName)).getText().toString())
                .putExtra("whiteboard", ((EditText) findViewById(R.id.whiteboardName)).getText().toString())
                .putExtra("prefix", ((EditText) findViewById(R.id.prefixName)).getText().toString());
        // Start whiteboard activity
        startActivity(WhiteboardActivityIntent);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

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
            session.logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
