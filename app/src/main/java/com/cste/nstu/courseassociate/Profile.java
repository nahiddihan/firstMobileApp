package com.cste.nstu.courseassociate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class Profile extends Activity{

    HashMap<String, String> hashMap;
    TextView tvSName,tvSRoll,tvSMoblile,tvSEmail,tvSSession,tvSYear,tvSTerm,tvSDepartment;
    TextView  tvTName,tvTMoblile,tvTEmail,tvTDepartment,tvTDesignation;

    private static final String DEPARTMENT = "http://192.168.56.1/course_associate/department.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DEPARTMENT = "department";
    ProgressDialog pDialog;
    String departmentId;
    public static String departmentName;
    JSONObject json;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        Log.v("profile HashMapTest", hashMap.get("user_type"));
        departmentId=hashMap.get("department_id");

        new getDepartmentTitle().execute();


        if(hashMap.get("user_type").equalsIgnoreCase("teacher")){

            setContentView(R.layout.teacher_profile);

            tvTName=(TextView)findViewById(R.id.teacherName);
            tvTEmail=(TextView)findViewById(R.id.teacher_email);
            tvTMoblile=(TextView)findViewById(R.id.teacher_mobile);
            tvTDepartment=(TextView)findViewById(R.id.teacher_department);
            tvTDesignation=(TextView)findViewById(R.id.teacher_designation);



            tvTName.setText(hashMap.get("teacher_name"));
           tvTMoblile.setText(hashMap.get("mobile"));
          tvTEmail.setText(hashMap.get("email"));
          tvTDesignation.setText(hashMap.get("designation"));




        }else {
            setContentView(R.layout.student_profile);
            tvSName=(TextView)findViewById(R.id.studentName);
            tvSRoll=(TextView)findViewById(R.id.rollId);
            tvSMoblile=(TextView)findViewById(R.id.student_mobile);
            tvSEmail=(TextView)findViewById(R.id.student_email);
            tvSSession=(TextView)findViewById(R.id.student_session);
            tvSYear=(TextView)findViewById(R.id.tvyear);
            tvSTerm=(TextView)findViewById(R.id.tvTerm);
            tvSDepartment=(TextView)findViewById(R.id.student_department);

            tvSName.setText(hashMap.get("student_name"));
            tvSMoblile.setText(hashMap.get("student_mobile"));
            tvSEmail.setText(hashMap.get("student_email"));
            tvSSession.setText(hashMap.get("session"));
            tvSRoll.setText(hashMap.get("roll"));
            int semister=Integer.parseInt(hashMap.get("semister_id"));
            int term=(semister%2==0)?2:1;
            if(semister==1){
                tvSTerm.setText(""+semister);
            }else{
                tvSTerm.setText(term+"");
            }
            tvSYear.setText(""+(semister/2+semister%2));
        }


    }

    class getDepartmentTitle extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Profile.this);
            // pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("department_id", departmentId));




                            json = jsonParser.makeHttpRequest(
                                    DEPARTMENT, "GET", params);
                            Log.d("Single department Details", json.toString());


                            success = json.getInt(TAG_SUCCESS);
                            if (success == 1) {

                                JSONArray departmentArray = json
                                        .getJSONArray(TAG_DEPARTMENT);
                                Log.d("department Details", departmentArray.toString());


                                JSONObject departmentObject = departmentArray.getJSONObject(0);
                                Log.d("Single department Details", departmentObject.toString());

                                departmentName=departmentObject.getString("department_title");
                                if(hashMap.get("user_type").equalsIgnoreCase("teacher")){
                                    tvTDepartment.setText(departmentName);
                                }else if(hashMap.get("user_type").equalsIgnoreCase("student")){
                                    tvSDepartment.setText(departmentName);
                                }

                            }else{
                                Toast.makeText(getApplicationContext(),"Invalid Email & Password combination",Toast.LENGTH_LONG).show();
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

        }
    }

}