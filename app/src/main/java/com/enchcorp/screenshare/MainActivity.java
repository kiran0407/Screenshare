package com.enchcorp.screenshare;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText user,phno,email,address;
    String user1,phno1,email1,address1,sp1,sp;
    boolean valid;
    TextView login;
    Button signup;
    String uname,uphnno,umail=null;
    int logtype1;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        user=(EditText)findViewById(R.id.username);
        phno=(EditText)findViewById(R.id.phno);
        email=(EditText)findViewById(R.id.email);
        address=(EditText)findViewById(R.id.pswd);
        signup=(Button)findViewById(R.id.loginBtn);
        login=(TextView)findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });
        Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        uname=c.getString(c.getColumnIndex("display_name"));
        if (c.moveToFirst()){
            // Toast.makeText(HomeScreen.this,b,Toast.LENGTH_SHORT).show();
            user.setText(uname);
        }

        c.close();


        String main_data[] = {"data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"};
        Object object = getContentResolver().query(Uri.withAppendedPath(android.provider.ContactsContract.Profile.CONTENT_URI, "data"),
                main_data, "mimetype=?",
                new String[]{"vnd.android.cursor.item/phone_v2"},
                "is_primary DESC");
        if (object != null) {
            do {
                if (!((Cursor) (object)).moveToNext())
                    break;
                uphnno= ((Cursor) (object)).getString(4);
                //Toast.makeText(HomeScreen.this,s1,Toast.LENGTH_SHORT).show();
                phno.setText(uphnno);

            } while (true);
            ((Cursor) (object)).close();
        }


        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                umail = account.name;
            }
        }

        email.setText(umail);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.school, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 sp = String.valueOf(spinner.getSelectedItem());
                if(sp.equals("Teacher")){
                    sp1=Integer.toString(1);
                }
                else {
                    sp1=Integer.toString(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user1=user.getText().toString();
                phno1=phno.getText().toString();
                email1=email.getText().toString();
                address1=address.getText().toString();
                valid = true;
                if (user1.isEmpty()) {
                    user.setError("at least 3 characters");
                    valid = false;
                }
                else if (email1.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                    email.setError("enter a valid email address");
                    valid = false;
                }
                else if (phno1.isEmpty()) {
                    phno.setError("enter 10 digit phone number");
                    valid = false;
                }
                else if (address1.isEmpty()) {
                    address.setError("at least 3 characters");
                    valid = false;
                }

            else {
                    insert_service(user1,phno1,email1,address1,sp1);
                }
            }
        });



    }
    private void insert_service(final String user1, final String phno1,final String email1, final String address1,final String sp1) {

        StringRequest stringreqs = new StringRequest(Request.Method.POST, MyGlobal_Url.MYBASIC_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("error");
                    if (abc) {
                        JSONObject users = jObj.getJSONObject("users");
                        String uid = users.getString("uid");
                        String phno = users.getString("mobile");
                        String user=users.getString("username");
                        String email=users.getString("email");
                        String logtype=users.getString("log_type");
                        logtype1=Integer.parseInt(logtype);
                        if(logtype1==1) {
                            session.setLogin(true);
                            Intent intent = new Intent(MainActivity.this, Selection.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("mobile", phno);
                            intent.putExtra("username", user);
                            intent.putExtra("email", email);
//                            SharedPreferences pref = PreferenceManager
//                                    .getDefaultSharedPreferences(MainActivity.this);
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putString ("uid", uid);
//                            editor.putString ("mobile", phno);
//                            editor.putString ("username", user);
//                            editor.putString ("email", email);
//                            editor.commit();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else{
                            session.setLogin(true);
                            Intent intent = new Intent(MainActivity.this, Student.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("mobile", phno);
                            intent.putExtra("username", user);
                            intent.putExtra("email", email);
//                            SharedPreferences pref = PreferenceManager
//                                    .getDefaultSharedPreferences(MainActivity.this);
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putString ("uid", uid);
//                            editor.putString ("mobile", phno);
//                            editor.putString ("username", user);
//                            editor.putString ("email", email);
//                            editor.commit();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
//                       Toast.makeText(getApplicationContext(),   name1, Toast.LENGTH_SHORT).show();
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
                uandme.put("user1", user1);
                uandme.put("phno1", phno1);
                uandme.put("email1", email1);
                uandme.put("address1", address1);
                uandme.put("sp1", sp1);
                return uandme;
            }
        };
        AppController.getInstance().addToRequestQueue(stringreqs);

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
