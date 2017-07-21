package com.enchcorp.screenshare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.List;

import static com.enchcorp.screenshare.R.id.phno;

public class Student extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button connectstudent;
    String user,uid,umail,uphno;
    TextView navname,navemail;
    ListView mystdlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  connectstudent=(Button)findViewById(R.id.connect);
        mystdlist=(ListView)findViewById(R.id.teacherlist1);
        new kilomilo().execute(MyGlobal_Url.MYBASIC_ACCTEACHER);
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

//        connectstudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            startActivity(new Intent(Student.this,Otpverify.class));
//            }
//        });


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
                holder.sname=(TextView) convertView.findViewById(R.id.studentname);

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


            public TextView sname;



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
            //
        }
        @Override
        protected void onPostExecute(final List<StudentName> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode!=null)
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.studentslist, movieMode);
                mystdlist.setAdapter(adapter);
                mystdlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StudentName item = movieMode.get(position);
                        startActivity(new Intent(Student.this,Otpverify.class));

                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
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
