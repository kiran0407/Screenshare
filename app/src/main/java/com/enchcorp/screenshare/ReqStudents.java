package com.enchcorp.screenshare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqStudents extends AppCompatActivity {
        ListView reqstd;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_students);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reqstd=(ListView)findViewById(R.id.reqstudlist1);

        new kilomilo().execute(MyGlobal_Url.MYBASIC_STDREQ);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public class MovieAdap extends ArrayAdapter {
        private List<StudentName> movieModelList;
        private int resource;
        private int selectedPosition = -1;
        Context context;
        private LayoutInflater inflater;
        MovieAdap(Context context, int resource, List<StudentName> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context =context;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getViewTypeCount() {
            return 1;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder  ;
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
                holder = new ViewHolder();
                //  holder.logo=(ImageView) convertView.findViewById(R.id.teamlogo);
                holder.sname=(TextView) convertView.findViewById(R.id.teachername);
                holder.accept=(TextView) convertView.findViewById(R.id.accept);
                holder.reject=(TextView) convertView.findViewById(R.id.reject);
                final StudentName studentName=movieModelList.get(position);
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      uid=studentName.getUid();
                        Toast.makeText(getApplicationContext(),"You Accepted the student",Toast.LENGTH_SHORT).show();
                        StringRequest stringreqs = new StringRequest(Request.Method.POST, MyGlobal_Url.MYBASIC_ACCEPT, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                startActivity(new Intent(ReqStudents.this,Selection.class));
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
                                uandme.put("uid", uid);

                                return uandme;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(stringreqs);

                    }
                });
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uid=studentName.getUid();
                        Toast.makeText(getApplicationContext(),"You Rejected the student",Toast.LENGTH_SHORT).show();
                        StringRequest stringreqs = new StringRequest(Request.Method.POST, MyGlobal_Url.MYBASIC_REJECT, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                               startActivity(new Intent(ReqStudents.this,Selection.class));
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
                                uandme.put("uid", uid);

                                return uandme;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(stringreqs);


                    }
                });
               // holder.reqsend=(Button) convertView.findViewById(R.id.ver);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            //  holder.checkBox.setTag(position);
            StudentName cc=movieModelList.get(position);
            holder.sname.setText(cc.getUsername());
            //holder.tname.setText(getItem(position).getName());


            //Toast.makeText(getApplicationContext(),cc.getTeamlogo(),Toast.LENGTH_LONG).show();
            // Picasso.with(context).load(cc.getTeamlogo()).fit().error(R.drawable.footballlogo).fit().into(holder.logo);
            //   Glide.with(context).load(cc.getTeamlogo()).into(holder.logo);
            return convertView;
        }

        class ViewHolder{


            public TextView sname,accept,reject;



        }
    }
    public class kilomilo extends AsyncTask<String,String, List<StudentName>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<StudentName> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("result");
                List<StudentName> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    StudentName catego = gson.fromJson(finalObject.toString(), StudentName.class);
                    milokilo.add(catego);
                }
                return milokilo;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(final List<StudentName> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode!=null)
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.reqstd, movieMode);
                reqstd.setAdapter(adapter);
                reqstd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StudentName item = movieMode.get(position);
//                        Intent intent=new Intent(AllStudents.this,Selection.class);
//                      //  intent.putExtra("pname",user1);
//
//                        startActivity(intent);
//                        s=item.getUid();
//                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
//
//


                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
