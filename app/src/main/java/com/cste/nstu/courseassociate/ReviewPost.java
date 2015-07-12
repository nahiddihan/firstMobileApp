package com.cste.nstu.courseassociate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReviewPost extends Activity {


    private static final String STUDENT_COURSE_REVIEW = "http://192.168.56.1/course_associate/student_course_review.php";
    private static final String TEACHER_COURSE_REVIEW = "http://192.168.56.1/course_associate/teacher_course_review.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COURSE = "review";
    ProgressDialog pDialog;
    JSONObject json;
    JSONParser jsonParser = new JSONParser();
    HashMap<String, String> hashMap;
    String semisterId;
    String teacherId;
    ArrayList<HashMap<String, String>> reviewList;
    JSONArray reviewArray;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);
       reviewList =new ArrayList<HashMap<String, String>>();
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


    }



    class getCourseList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(ReviewPost.this);
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
                            STUDENT_COURSE_REVIEW, "GET", param);

                    Log.d("course Details", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        reviewArray = json .getJSONArray(TAG_COURSE);
                        Log.d("course Details", reviewArray.toString());
                        for (int i = 0; i < reviewArray.length(); i++) {
                            JSONObject c = reviewArray.getJSONObject(i);


                            String courseTitle = c.getString("course_title");
                            String statusDetails = c.getString("status_details");

                            String date=c.getString("date");
                            String time=c.getString("time");

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("time",time);
                            map.put("date",date);
                            map.put("course_title", courseTitle);
                            map.put("status_details", statusDetails);
                            reviewList.add(map);

                        }
                    }else{

                    }

                }else if(hashMap.get("user_type").equalsIgnoreCase("teacher")){
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("teacher_id", teacherId));
                    json = jsonParser.makeHttpRequest(
                            TEACHER_COURSE_REVIEW, "GET", param);

                    Log.d("course Details", json.toString());


                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {


                        reviewArray = json .getJSONArray(TAG_COURSE); // JSON Array
                        Log.d("review Details", reviewArray.toString());
                        for (int i = 0; i < reviewArray.length(); i++) {
                            JSONObject c = reviewArray.getJSONObject(i);


                            String courseTitle = c.getString("course_title");
                            String statusDetails = c.getString("status_details");

                            String date=c.getString("date");
                            String time=c.getString("time");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("time",time);
                            map.put("date",date);
                            map.put("course_title", courseTitle);
                            map.put("status_details", statusDetails);
                            reviewList.add(map);
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
                            ReviewPost.this, reviewList,
                            R.layout.review_item, new String[] { "time", "date","course_title","status_details"},
                            new int[] { R.id.idTime, R.id.idDate,R.id.tvCourseTitle,R.id.tvPostDetails });

                    lv.setAdapter(adapter);

                }
            });

        }
    }

}
