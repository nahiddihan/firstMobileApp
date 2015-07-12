package com.cste.nstu.courseassociate;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


public class CourseTeacher extends Activity {


    private static final String COURSE__TEACHER = "http://192.168.56.1/course_associate/course_teacher.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COURSE = "course_teacher";
    ProgressDialog pDialog;
    JSONObject json;
    JSONParser jsonParser = new JSONParser();
    HashMap<String, String> hashMap;
    String semisterId;
    String teacherId;
    ArrayList<HashMap<String, String>> courseTeacherList;
    JSONArray courseArray;



    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_teacher);


        courseTeacherList = new ArrayList<HashMap<String, String>>();
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("map");
        Log.v("profile HashMapTest", hashMap.get("user_type"));
        if (hashMap.get("user_type").equalsIgnoreCase("teacher")) {
            teacherId = hashMap.get("teacher_id");
        } else if (hashMap.get("user_type").equalsIgnoreCase("student")) {
            semisterId = hashMap.get("semister_id");
        }
        new getCourseTeacher().execute();
        //   ListView lv = getListView();
        lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                /*Intent courseIntent = new Intent(getApplicationContext(),Syllabus.class);
                courseIntent.putExtra("course_code", courseTeacherList.get(position).get("course_code"));
                courseIntent.putExtra("course_title", courseTeacherList.get(position).get("course_title"));
                courseIntent.putExtra("syllabus", courseTeacherList.get(position).get("syllabus"));
                courseIntent.putExtra("credit", courseTeacherList.get(position).get("credit"));
                courseIntent.putExtra("credit_hour", courseTeacherList.get(position).get("credit_hour"));
                startActivity(courseIntent);*/

                final Dialog dialog = new Dialog(CourseTeacher.this);
                dialog.setTitle("Course Teacher Information");
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbox);
                dialog.setContentView(R.layout.course_teacher_dialog);
                dialog.show();

                TextView teacherName=(TextView)dialog.findViewById(R.id.tvTeacherName);
                TextView designation=(TextView)dialog.findViewById(R.id.tvDesignation);
                TextView mobile=(TextView)dialog.findViewById(R.id.tvmobile);
                TextView email=(TextView)dialog.findViewById(R.id.tvemail);
                TextView department=(TextView)dialog.findViewById(R.id.tvdepartment);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancelBt);
                teacherName.setText(courseTeacherList.get(position).get("teacher_name"));
                designation.setText(courseTeacherList.get(position).get("designation"));
                mobile.setText(courseTeacherList.get(position).get("mobile"));
                email.setText(courseTeacherList.get(position).get("email"));
                department.setText(courseTeacherList.get(position).get("department_title"));




                cancelButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });
            }

        });


    }
    class getCourseTeacher extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(CourseTeacher.this);
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
                            COURSE__TEACHER, "GET", param);

                    Log.d("course Details", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        courseArray = json .getJSONArray(TAG_COURSE);
                        Log.d("course Details", courseArray.toString());
                        for (int i = 0; i < courseArray.length(); i++) {
                            JSONObject c = courseArray.getJSONObject(i);




                            String CourseTitle = c.getString("course_title");
                            String teacher_name=c.getString("teacher_name");
                            String designation=c.getString("designation");
                            String email=c.getString("email");
                            String mobile=c.getString("mobile");
                            String department_title=c.getString("department_title");



                            HashMap<String, String> map = new HashMap<String, String>();


                            map.put("course_title", CourseTitle);
                            map.put("teacher_name", teacher_name);
                            map.put("department_title", department_title);
                            map.put("designation",designation);
                            map.put("email",email);
                            map.put("mobile",mobile);


                            courseTeacherList.add(map);
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
                            CourseTeacher.this, courseTeacherList,
                            R.layout.course_teacher_list, new String[] { "course_title","teacher_name"},
                            new int[] { R.id.tvCourseTitle,R.id.tvTeacherName });

                    lv.setAdapter(adapter);

                }
            });

        }
    }

}
