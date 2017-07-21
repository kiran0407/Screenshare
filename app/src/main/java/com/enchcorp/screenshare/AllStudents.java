package com.enchcorp.screenshare;

import android.content.Context;
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

public class AllStudents extends AppCompatActivity {
    ListView studentlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_students);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        studentlist=(ListView)findViewById(R.id.teamlist1);
        new kilomilo().execute(MyGlobal_Url.MYBASIC_ALLSTUDENTS);

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
        }
        @Override
        protected void onPostExecute(final List<StudentName> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode!=null)
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.studentslist, movieMode);
                studentlist.setAdapter(adapter);
                studentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StudentName item = movieMode.get(position);
//                        Intent intent=new Intent(AllStudents.this,Selection.class);
//                      //  intent.putExtra("pname",user1);
//
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
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
