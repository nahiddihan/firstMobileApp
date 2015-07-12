package com.cste.nstu.courseassociate;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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


public class CourseList extends Activity {


    private static final String STUDENT_COURSE = "http://192.168.56.1/course_associate/student_course.php";
    private static final String TEACHER_COURSE = "http://192.168.56.1/course_associate/teacher_course.php";
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



    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.course_list);



        courseList=new ArrayList<HashMap<String, String>>();
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        Log.v("profile HashMapTest", hashMap.get("user_type"));
        if(hashMap.get("user_type").equalsIgnoreCase("teacher")){
            teacherId=hashMap.get("teacher_id");
        }else if(hashMap.get("user_type").equalsIgnoreCase("student")){
            semisterId=hashMap.get("semister_id");
        }
       new  getCourseList().execute();
     //   ListView lv = getListView();
         lv =(ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent courseIntent = new Intent(getApplicationContext(),Syllabus.class);
                courseIntent.putExtra("course_code",courseList.get(position).get("course_code"));
                courseIntent.putExtra("course_title",courseList.get(position).get("course_title"));
                courseIntent.putExtra("syllabus",courseList.get(position).get("syllabus"));
                courseIntent.putExtra("credit",courseList.get(position).get("credit"));
                courseIntent.putExtra("credit_hour",courseList.get(position).get("credit_hour"));
                startActivity(courseIntent);
            }
        });

    }



    class getCourseList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(CourseList.this);
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
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            CourseList.this, courseList,
                            R.layout.list_item, new String[] { "course_id", "course_code","course_title"},
                            new int[] { R.id.courseId, R.id.courseCode,R.id.courseTitle });

                   lv.setAdapter(adapter);

                }
            });

        }
    }

}
