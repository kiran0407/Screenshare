package com.enchcorp.screenshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    Button login;
    EditText num,pass;
    Boolean valid;
    String num1,pass1;
    TextView signuplink;
    SessionManager session;
    int logtype1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        num=(EditText)findViewById(R.id.phno);
        pass=(EditText)findViewById(R.id.pswd);
        login=(Button)findViewById(R.id.login);
        signuplink=(TextView)findViewById(R.id.link_signup);
        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,MainActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 num1=num.getText().toString();
                pass1=pass.getText().toString();
                valid = true;
                if(num1.isEmpty()){
                    num.setError("please enter your phone number");
                    valid = false;
                }
                else if(pass1.isEmpty()){
                    pass.setError("please enter password");
                    valid = false;
                }
                else {
                    insert_service(num1,pass1);
                }
            }
        });

    }
    private void insert_service(final String num1,final String pass1) {

        StringRequest stringreqs = new StringRequest(Request.Method.POST, MyGlobal_Url.MYBASIC_SIGNIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean abc = jObj.getBoolean("error");
                    if (abc)
                    {
                        JSONObject users = jObj.getJSONObject("users");
                        String uid = users.getString("uid");
                        String phno = users.getString("mobile");
                        String user=users.getString("username");
                        String email=users.getString("email");
                        String logtype=users.getString("log_type");
                        logtype1=Integer.parseInt(logtype);
                        if(logtype1==1) {
                            session.setLogin(true);
                            Intent intent = new Intent(Login.this, Selection.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("mobile", phno);
                            intent.putExtra("username", user);
                            intent.putExtra("email", email);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else{
                            session.setLogin(true);
                            Intent intent = new Intent(Login.this, Student.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("mobile", phno);
                            intent.putExtra("username", user);
                            intent.putExtra("email", email);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        //Toast.makeText(getApplicationContext(),phno, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
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
                uandme.put("num1", num1);
                uandme.put("pswd", pass1);

                return uandme;
            }
        };
        AppController.getInstance().addToRequestQueue(stringreqs);

    }


}
