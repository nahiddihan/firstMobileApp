package com.cste.nstu.courseassociate;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notify extends Activity {
	
	// notify activity
	
    private static final String STUDENT_COURSE = "http://192.168.56.1/course_associate/student_course.php";
    private static final String TEACHER_COURSE = "http://192.168.56.1/course_associate/teacher_course.php";
    private static final String NOTIFY = "http://192.168.56.1/course_associate/notify.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COURSE = "course";
    ProgressDialog pDialog;
    JSONObject json;
    JSONParser jsonParser = new JSONParser();
    HashMap<String, String> hashMap;
    String semisterId;
    String teacherId;
    ArrayList<HashMap<String, String>> courseList;
    JSONArray courseArray;
ArrayList<String> courseTitleList;
    ArrayList<String> courseIdList;
    Spinner courseListSpinner;
    Button cancel;
    Button notify;
    String courseId;
    String description;

    TextView Tvdescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notify);
        Tvdescription=(TextView)findViewById(R.id.tvDescription);
        courseTitleList=new ArrayList<String>();
        courseIdList =new ArrayList<String>();

        courseList=new ArrayList<HashMap<String, String>>();
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        Log.v("profile HashMapTest", hashMap.get("user_type"));
        if(hashMap.get("user_type").equalsIgnoreCase("teacher")){
            teacherId=hashMap.get("teacher_id");
        }else if(hashMap.get("user_type").equalsIgnoreCase("student")){
            semisterId=hashMap.get("semister_id");
        }
        courseListSpinner= (Spinner)findViewById(R.id.spCourseList);
        cancel=(Button)findViewById(R.id.cancelButton);
        notify=(Button)findViewById(R.id.notifyButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


       new getCourseList().execute();
        courseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int viewPosition=courseListSpinner.getSelectedItemPosition();
                courseId= courseIdList.get(viewPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description=Tvdescription.getText().toString();
                if(description.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Empty Notification ",Toast.LENGTH_SHORT).show();

                }else{
                    new insertPost().execute();
                }




            }
        });

    }

    class getCourseList extends AsyncTask<String, String, String> {
		
		
		// async task for course list

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(Notify.this);
            //  pDialog.setMessage("Loading courses. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            int success;
            try {
                if(hashMap.get("user_type").equalsIgnoreCase("student")){
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("semister_id", semisterId));
                    json = jsonParser.makeHttpRequest(
                            STUDENT_COURSE, "GET", param);

                    Log.d("course Details", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        courseArray = json .getJSONArray(TAG_COURSE);
                        Log.d("course Details", courseArray.toString());
                        for (int i = 0; i < courseArray.length(); i++) {
                            JSONObject c = courseArray.getJSONObject(i);
                            String courseId = c.getString("course_id");
                            String courseCode = c.getString("course_code");
                            String CourseTitle = c.getString("course_title");
                            String syllabus=c.getString("syllabus");
                            String credit=c.getString("credit");
                            String creditHour=c.getString("credit_hour");
                            courseTitleList.add(CourseTitle);
                            courseIdList.add(courseId);
                            HashMap<String, String> map = new HashMap<String, String>();


                            map.put("course_id", courseId);
                            map.put("course_code", courseCode);
                            map.put("course_title", CourseTitle);
                            map.put("syllabus",syllabus);
                            map.put("credit",credit);
                            map.put("credit_hour",creditHour);


                            courseList.add(map);
                        }
                    }else{

                    }

                }else if(hashMap.get("user_type").equalsIgnoreCase("teacher")){
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("teacher_id", teacherId));
                    json = jsonParser.makeHttpRequest(
                            TEACHER_COURSE, "GET", param);

                    Log.d("course Details", json.toString());


                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        courseArray = json .getJSONArray(TAG_COURSE); // JSON Array
                        Log.d("course Details", courseArray.toString());
                        for (int i = 0; i < courseArray.length(); i++) {
                            JSONObject c = courseArray.getJSONObject(i);


                            String courseId = c.getString("course_id");
                            String courseCode = c.getString("course_code");
                            String CourseTitle = c.getString("course_title");
                            String syllabus=c.getString("syllabus");
                            String credit=c.getString("credit");
                            String creditHour=c.getString("credit_hour");
                            courseTitleList.add(CourseTitle);
                            courseIdList.add(courseId);

                        }

                    }else{

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

            runOnUiThread(new Runnable() {
                public void run() {



                    ArrayAdapter<String>spinnerAdapter=new ArrayAdapter<String>
                            (getApplicationContext(),R.layout.notify_spinner,R.id.tvnotifyspinner,courseTitleList );
                    courseListSpinner.setAdapter(spinnerAdapter);

                }
            });


        }
    }
    class insertPost extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(Notify.this);
            //  pDialog.setMessage("Loading courses. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("teacher_id", teacherId));
                param.add(new BasicNameValuePair("course_id", courseId));
                param.add(new BasicNameValuePair("description", description));

                    json = jsonParser.makeHttpRequest(
                            NOTIFY, "GET", param);
                    Log.d("course Details", json.toString());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            finish();



        }
    }

}

