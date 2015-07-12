package com.cste.nstu.courseassociate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends Activity {



    String[] user = new String[]{"Teacher", "Student"};
    String userTable;
    Spinner logInSpinner;
    Button login;
    EditText editTextEmail;
    EditText editTextPassword;
    String email;
    String password;
    JSONObject json;

    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String student_logIn = "http://192.168.56.1/course_associate/student_login.php";
    private static final String teacher_logIn = "http://192.168.56.1/course_associate/teacher_login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.blogin);
        editTextEmail =(EditText)findViewById(R.id.etEmail);
        editTextPassword =(EditText)findViewById(R.id.etPassword);

        logInSpinner = (Spinner) findViewById(R.id.spinnerId);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.
                R.layout.simple_list_item_1, user);

        logInSpinner.setAdapter(adapter);


        logInSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                int sid = logInSpinner.getSelectedItemPosition();
                userTable = user[sid];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }

        });


        editTextEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEmail.setText("");
            }
        });
        editTextPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editTextPassword.setText("");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                // some thing to write

                new getUserInfo().execute();

            }
        });



    }



    class getUserInfo extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
           // pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         *  background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", email));
                        params.add(new BasicNameValuePair("password",password));


                        if(userTable.equalsIgnoreCase("Teacher")){
                             json = jsonParser.makeHttpRequest(
                                    teacher_logIn, "GET", params);
                            Log.d("Single teacher Details", json.toString());

                            // json success tag
                            success = json.getInt(TAG_SUCCESS);
                            if (success == 1) {

                                JSONArray userArray = json
                                        .getJSONArray(TAG_USER); // JSON Array
                                Log.d("Single teacher Details", userArray.toString());


                   JSONObject user = userArray.getJSONObject(0);
                              Log.d("Single teacher Details",user.toString());

                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("user_type","teacher");
                                hashMap.put("teacher_id",user.getString("teacher_id"));
                                hashMap.put("teacher_name",user.getString("teacher_name"));
                                hashMap.put("designation",user.getString("designation"));
                                hashMap.put("email",user.getString("email"));
                                hashMap.put("mobile",user.getString("mobile"));
                                hashMap.put("department_id",user.getString("department_id"));
                                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                intent.putExtra("map", hashMap);
                                startActivity(intent);
                                finish();



                            }else{
                                Toast.makeText(getApplicationContext(),"Invalid Email & Password combination",Toast.LENGTH_LONG).show();
                            }

                        }else if(userTable=="Student"){
                            json = jsonParser.makeHttpRequest(
                                    student_logIn, "GET", params);
                            Log.d("Single student Details", json.toString());


                            success = json.getInt(TAG_SUCCESS);
                            if (success == 1) {

                                JSONArray userObject = json
                                        .getJSONArray(TAG_USER); // JSON Array


                                JSONObject user = userObject.getJSONObject(0);
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("user_type","student");
                                hashMap.put("student_id",user.getString("student_id"));
                                hashMap.put("student_name",user.getString("student_name"));
                                hashMap.put("roll",user.getString("roll"));
                                hashMap.put("student_mobile",user.getString("student_mobile"));
                                hashMap.put("student_email",user.getString("student_email"));
                                hashMap.put("department_id",user.getString("department_id"));
                                hashMap.put("semister_id",user.getString("semister_id"));
                                hashMap.put("session",user.getString("session"));
                                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                intent.putExtra("map", hashMap);
                                startActivity(intent);
                                finish();






                            }else{
                                Toast.makeText(getApplicationContext(),"Invalid Email & Password combination",Toast.LENGTH_LONG).show();
                            }

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




