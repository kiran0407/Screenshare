package com.enchcorp.screenshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.enchcorp.screenshare.R.id.phno;

public class Student extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button connectstudent;
    String user,uid,umail,uphno;
    TextView navname,navemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        connectstudent=(Button)findViewById(R.id.connect);
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        umail = intent.getStringExtra("email");
        uphno = intent.getStringExtra("mobile");
        uid = intent.getStringExtra("uid");
//        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Student.this);
//        SharedPreferences.Editor editor = pref.edit();
//         uid = pref.getString("uid", "8");
//         user = pref.getString("username", "8");
//        umail = pref.getString("email", "8");

        connectstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(Student.this,Otpverify.class));
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v=navigationView.getHeaderView(0);
        navname=(TextView) v.findViewById(R.id.sname);
        navemail=(TextView) v.findViewById(R.id.smail);
        navname.setText(user);
        navemail.setText(umail);


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.allteacher) {
            // Handle the camera action
            Intent intent = new Intent(Student.this, AllTeacher.class);
            intent.putExtra("uid", uid);
            intent.putExtra("mobile", phno);
            startActivity(intent);
        } else if (id == R.id.reqteacher) {

        } else if (id == R.id.myteacher) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
