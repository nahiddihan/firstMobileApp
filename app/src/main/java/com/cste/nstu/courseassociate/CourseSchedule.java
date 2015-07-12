package com.cste.nstu.courseassociate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseSchedule extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
     List<String> listDataHeader ;
     HashMap<String, List<String>> listDataChild ;
    HashMap<String, String> hashMap;
    String teacherId;
    String semisterId;
    ProgressDialog pDialog;
    JSONObject json;
    JSONParser jsonParser = new JSONParser();
    JSONArray courseArray;
    ArrayList<HashMap<String, String>> courseList;
    private static final String STUDENT_SCHEDULE = "http://192.168.56.1/course_associate/student_course_schedule.php";
    private static final String TEACHER_SCHEDULE = "http://192.168.56.1/course_associate/teacher_course_schedule.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SCHEDULE = "schedule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_schedule);



        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        Log.v("schedule HashMapTest", hashMap.get("user_type"));
        if(hashMap.get("user_type").equalsIgnoreCase("teacher")){
            teacherId=hashMap.get("teacher_id");
        }else if(hashMap.get("user_type").equalsIgnoreCase("student")){
            semisterId=hashMap.get("semister_id");
        }



        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        new getScheduleList().execute();


        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
              /*  Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });


        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });


        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
               /* Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                return false;
            }
        });
    }



    class getScheduleList extends AsyncTask<String, String, String> {
        List<String> row;
        int m=0;

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(CourseSchedule.this);
            //  pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            int success;
            try {
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();

                if(hashMap.get("user_type").equalsIgnoreCase("student")){
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("semister_id", semisterId));
                    json = jsonParser.makeHttpRequest(
                            STUDENT_SCHEDULE, "GET", param);

                    Log.d("schedule Details", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        courseArray = json .getJSONArray(TAG_SCHEDULE);
                        Log.d("schedule array Details", courseArray.toString());
                        for (int i = 0; i < courseArray.length(); i++) {
                            row=new ArrayList<String>();
                            JSONObject c = courseArray.getJSONObject(i);
                            if(listDataHeader.isEmpty()){
                                listDataHeader.add(c.getString("course_title"));
                                for(int j=0;j<courseArray.length();j++){
                                    JSONObject d = courseArray.getJSONObject(j);
                                    if(c.getString("course_title").equalsIgnoreCase(d.getString("course_title"))){
                                        String text=d.getString("day")+" at "+d.getString("time")+"\n"+" duration"+": "+d.getString("duration")+"minutes";
                                        row.add(text);
                                    }
                                }
                                int position=listDataHeader.size();
                                listDataChild.put(listDataHeader.get(position-1), row);

                            }else{
                                boolean a=false;


                                for(int j=0;j<listDataHeader.size();j++) {

                                    if (c.getString("course_title").equalsIgnoreCase(listDataHeader.get(j))) {
                                        a = true;

                                    }
                                }
                                    if(a==false) {
                                        listDataHeader.add(c.getString("course_title"));
                                    }
                                        for(int k=0;k<courseArray.length();k++){
                                            JSONObject d = courseArray.getJSONObject(k);
                                            if(c.getString("course_title").equalsIgnoreCase(d.getString("course_title"))){
                                                String text=d.getString("day")+" at "+d.getString("time")+"  \n"+" duration"+": "+d.getString("duration")+"minutes";
                                                row.add(text);
                                            }
                                        }

                                        int position2=listDataHeader.size();
                                        listDataChild.put(listDataHeader.get(position2-1), row);

                            }
                            }
                        }
                }else if(hashMap.get("user_type").equalsIgnoreCase("teacher")){
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("teacher_id", teacherId));
                    json = jsonParser.makeHttpRequest(
                            TEACHER_SCHEDULE, "GET", param);

                    Log.d("course Details", json.toString());


                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        courseArray = json .getJSONArray(TAG_SCHEDULE); // JSON Array
                        Log.d("schedule array Details", courseArray.toString());
                        for (int i = 0; i < courseArray.length(); i++) {
                            row=new ArrayList<String>();
                            JSONObject c = courseArray.getJSONObject(i);
                            if(listDataHeader.isEmpty()){
                                listDataHeader.add(c.getString("course_title"));
                                for(int j=0;j<courseArray.length();j++){
                                    JSONObject d = courseArray.getJSONObject(j);
                                    if(c.getString("course_title").equalsIgnoreCase(d.getString("course_title"))){
                                        String text=d.getString("day")+" at "+d.getString("time")+"  \n"+"duration"+": "+d.getString("duration")+"minutes";
                                        row.add(text);
                                    }
                                }
                                int position=listDataHeader.size();
                                listDataChild.put(listDataHeader.get(position-1), row);

                            }else{
                                boolean a=false;


                                for(int j=0;j<listDataHeader.size();j++) {

                                    if (c.getString("course_title").equalsIgnoreCase(listDataHeader.get(j))) {
                                        a = true;

                                    }
                                }
                                if(a==false) {
                                    listDataHeader.add(c.getString("course_title"));
                                }
                                for(int k=0;k<courseArray.length();k++){
                                    JSONObject d = courseArray.getJSONObject(k);
                                    if(c.getString("course_title").equalsIgnoreCase(d.getString("course_title"))){
                                        String text=d.getString("day")+" at "+d.getString("time")+"   \n"+"duration"+": "+d.getString("duration")+"minutes";
                                        row.add(text);
                                    }
                                }

                                int position2=listDataHeader.size();
                                listDataChild.put(listDataHeader.get(position2-1), row);

                            }
                        }
                    }

                    else{

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;



        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);



        }
    }



}
