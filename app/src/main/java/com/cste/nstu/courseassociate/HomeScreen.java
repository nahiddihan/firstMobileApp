package com.cste.nstu.courseassociate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;

public class HomeScreen extends Activity {
    GridView grid;
    String[] list;
    int[] imageId;
String userType;
    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
        userType=hashMap.get("user_type");
        Log.v("profile HashMapTest", hashMap.get("user_type"));
        if(hashMap.get("user_type").equalsIgnoreCase("teacher")){

             list= new String[]{ "Profile", "Courses", "Course Schedule", "Class Routine","Notification","Review" } ;
             imageId = new int[]{ R.drawable.profile, R.drawable.course,R.drawable.schedul,R.drawable.routine, R.drawable.notify,R.drawable.review };

        }else if(hashMap.get("user_type").equalsIgnoreCase("student")){
            list = new String[]{ "Profile", "Courses", "Course Schedule", "Class Routine","Course Teachers","Notifications" } ;
            imageId = new int[]{ R.drawable.profile, R.drawable.course,R.drawable.schedul,R.drawable.routine, R.drawable.teacher,R.drawable.review };
        }
        CustomGridView adapter = new CustomGridView(HomeScreen.this, list, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        /*Intent intent = getIntent();
         hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");*/
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch(position){
                    case 0:

                        Intent intent = new Intent(getApplicationContext(),Profile.class);
                        intent.putExtra("map", hashMap);
                        startActivity(intent);

                        break;
                    case 1:
                        Intent courseIntent = new Intent(getApplicationContext(),CourseList.class);
                        courseIntent.putExtra("map", hashMap);
                        startActivity(courseIntent);

                        break;
                    case 2:
                        Intent courseScheduleIntent = new Intent(getApplicationContext(),CourseSchedule.class);
                        courseScheduleIntent.putExtra("map", hashMap);
                        startActivity(courseScheduleIntent);

                        break;
                    case 3:
                        Intent routineIntent = new Intent(getApplicationContext(),Routine.class);
                        routineIntent.putExtra("map", hashMap);
                        startActivity(routineIntent);
                        break;
                    case 4:
                        if(userType.equalsIgnoreCase("teacher")){
                            Intent notifyIntent = new Intent(getApplicationContext(),Notify.class);
                            notifyIntent.putExtra("map", hashMap);
                            startActivity(notifyIntent);

                        }else{
                            Intent notifyIntent = new Intent(getApplicationContext(),CourseTeacher.class);
                            notifyIntent.putExtra("map", hashMap);
                            startActivity(notifyIntent);
                        }

                        break;
                    case 5:
                        Intent reviewIntent = new Intent(getApplicationContext(),ReviewPost.class);
                        reviewIntent.putExtra("map", hashMap);
                        startActivity(reviewIntent);
                        break;
                }


            }
        });

    }



}

