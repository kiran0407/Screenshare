package com.enchcorp.screenshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Otpverify extends AppCompatActivity {
String phno,otp,user="kiran",whiteboard="hello",prefix="com/ench",accesscode;

  TextView textaccess;
    Button ver;
    SessionManager session;
    Button con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        con=(Button)findViewById(R.id.con);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Otpverify.this,WhiteboardActivity.class);
                intent.putExtra("name",user);
                intent.putExtra("whiteboard",whiteboard);
                intent.putExtra("prefix",prefix);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            session.setLogin(false);
            Intent intent = new Intent(Otpverify.this, Login.class);
            startActivity(intent);
            finish();
        }
        Intent intent = getIntent();
//        phno = intent.getStringExtra("mobile");
//        user = intent.getStringExtra("username");
        accesscode = intent.getStringExtra("access");
        textaccess=(TextView)findViewById(R.id.access);

        textaccess.setText(accesscode);
//        ver.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                otp=otpenter.getText().toString();
//                if(otp.isEmpty()){
//                    Toast.makeText(getApplicationContext(),"please enter OTP",Toast.LENGTH_LONG).show();
//                }
//                else {
//                    insert_service(otp,phno);
//                }
//            }
//        });


    }
    private void insert_service(final String otp,final String phno) {

        StringRequest stringreqs = new StringRequest(Request.Method.POST, MyGlobal_Url.MYBASIC_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String responq) {
                try {
                    JSONObject jObj = new JSONObject(responq);
                    boolean abc = jObj.getBoolean("exits");
                    if (!abc)
                    {


                        Intent intent=new Intent(Otpverify.this,WhiteboardActivity.class);
                        intent.putExtra("name",user);
                        intent.putExtra("whiteboard",whiteboard);
                        intent.putExtra("prefix",prefix);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(),phno, Toast.LENGTH_SHORT).show();
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
                uandme.put("otp1", otp);
                uandme.put("num1", phno);

                return uandme;
            }
        };
        AppController.getInstance().addToRequestQueue(stringreqs);

    }



}
