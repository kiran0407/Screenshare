package com.enchcorp.screenshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Selection extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
Button start,send;

    String user,whiteboard="hello",prefix="com/ench",accesscode,uid,umail;
    SessionManager session;
    TextView navname,navemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            session.setLogin(false);
            Intent intent = new Intent(Selection.this, Login.class);
            startActivity(intent);
            finish();
        }
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        umail = intent.getStringExtra("email");
        uid = intent.getStringExtra("uid");
//        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Selection.this);
//        SharedPreferences.Editor editor = pref.edit();
//        uid = pref.getString("uid", "8");
//        user = pref.getString("username", "8");
//        umail = pref.getString("email", "8");
      //  Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_LONG).show();
        start=(Button)findViewById(R.id.start);
        send=(Button)findViewById(R.id.msg);

        final Random myRandom = new Random();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accesscode=String.valueOf(myRandom.nextInt());
                insert_service(accesscode);




            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Selection.this);

                // Setting Dialog Title
                alertDialog.setTitle("Screen share");

                // Setting Dialog Message
                alertDialog.setMessage("your message to share your students is   "+String.valueOf(myRandom.nextInt()));

                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.delete);

                // On pressing Settings button
                alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                       // startActivity(new Intent(Selection.this,WhiteboardActivity.class));
                        Intent intent=new Intent(Selection.this,Otpverify.class);
                        intent.putExtra("name",user);
                        intent.putExtra("whiteboard",whiteboard);
                        intent.putExtra("prefix",prefix);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
             //   Toast.makeText(getApplicationContext(),String.valueOf(myRandom.nextInt()),Toast.LENGTH_LONG).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v=navigationView.getHeaderView(0);
        navname=(TextView) v.findViewById(R.id.navname);
        navemail=(TextView) v.findViewById(R.id.navemail);
        navname.setText(user);
        navemail.setText(umail);


        navigationView.setNavigationItemSelectedListener(this);
    }
    private void insert_service(final String accesscode) {

        StringRequest stringreqs = new StringRequest(Request.Method.POST, MyGlobal_Url.MYBASIC_ACCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("error");
                    if (abc) {
                        JSONObject users = jObj.getJSONObject("users");
                        String access = users.getString("access");
                        session.setLogin(true);
                        Intent intent = new Intent(Selection.this, Otpverify.class);

                        intent.putExtra("access", access);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                //     Toast.makeText(getApplicationContext(), access, Toast.LENGTH_SHORT).show();
//                       Toast.makeText(getApplicationContext(), email1, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        String msg=jObj.getString("messeade");
                        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG,
                                TastyToast.WARNING);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(), "INTERNET CONNECTION NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                TastyToast.makeText(getApplicationContext(), "INTERNET CONNECTION NOT AVAILABLE", TastyToast.LENGTH_LONG,
                        TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> uandme = new HashMap<String, String>();
                uandme.put("access", accesscode);

                return uandme;
            }
        };
        AppController.getInstance().addToRequestQueue(stringreqs);

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
        getMenuInflater().inflate(R.menu.selection, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.allstudnts) {
            // Handle the camera action
            startActivity(new Intent(Selection.this,AllStudents.class));
        } else if (id == R.id.reqstdnts) {
            startActivity(new Intent(Selection.this,ReqStudents.class));
        } else if (id == R.id.mystdnts) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
