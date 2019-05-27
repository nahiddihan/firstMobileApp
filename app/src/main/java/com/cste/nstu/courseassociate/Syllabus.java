package com.cste.nstu.courseassociate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by NAKODIKO on 04-Jun-15.
 */
public class Syllabus extends Activity {
	
	// syllabus activity
	
    TextView tvCourseTitle,tvCourseCode,tvCourseCredit,tvCreditHour,tvSyllabus;
    Button btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syllabus_layout);
        tvCourseCode=(TextView)findViewById(R.id.tvCourseCode);
        tvCourseTitle=(TextView)findViewById(R.id.tvCoursetitle);
        tvCourseCredit=(TextView)findViewById(R.id.tvCourseCredit);
        tvCourseCode=(TextView)findViewById(R.id.tvCourseCode);
        tvCreditHour=(TextView)findViewById(R.id.tvCreditHour);
        tvSyllabus=(TextView)findViewById(R.id.tvSyllabus);
        btBack=(Button)findViewById(R.id.btBack);

        Intent intent = getIntent();
        String sc="SYLLABUS ";
        tvCourseCode.setText(sc+":"+intent.getStringExtra("course_code"));
        tvCourseTitle.setText(intent.getStringExtra("course_title"));
        tvCreditHour.setText(intent.getStringExtra("credit_hour"));
        tvCourseCredit.setText(intent.getStringExtra("credit"));
        tvSyllabus.setText(intent.getStringExtra("syllabus"));
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				
				// onback press 
				
              finish();
            }
        });






    }
}
