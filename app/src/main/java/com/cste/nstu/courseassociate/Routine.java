package com.cste.nstu.courseassociate;

import android.app.Activity;
        import android.os.Bundle;
        import android.support.v4.view.GestureDetectorCompat;
        import android.view.GestureDetector;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Window;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.Button;
        import android.widget.ExpandableListView;
        import android.widget.Toast;
        import android.widget.ViewFlipper;

        import java.util.ArrayList;


public class Routine extends Activity implements
        GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;

    Button buttonPrev, buttonNext;
    ExpandableListView ExpandList1,ExpandList2,ExpandList3,ExpandList4,ExpandList5;
    ArrayList<Group> ExpListItems;
    RoutineExpandableListAdapter ExpAdapter;
    ViewFlipper viewFlipper;

    Animation slide_in_left, slide_out_right;
    Animation slide_in_right, slide_out_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.routine);

        mDetector = new GestureDetectorCompat(this,this);

        buttonPrev = (Button) findViewById(R.id.prev);
        buttonNext = (Button) findViewById(R.id.next);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        ExpandList1 = (ExpandableListView) findViewById(R.id.exp_list1);
        ExpandList2 = (ExpandableListView) findViewById(R.id.exp_list2);
        ExpandList3 = (ExpandableListView) findViewById(R.id.exp_list3);
        ExpandList4 = (ExpandableListView) findViewById(R.id.exp_list4);
        ExpandList5 = (ExpandableListView) findViewById(R.id.exp_list5);

        ExpListItems = SetStandardGroups();
        ExpAdapter = new RoutineExpandableListAdapter(Routine.this, ExpListItems);
        ExpandList1.setAdapter(ExpAdapter);
        ExpandList2.setAdapter(ExpAdapter);
        ExpandList3.setAdapter(ExpAdapter);
        ExpandList4.setAdapter(ExpAdapter);
        ExpandList5.setAdapter(ExpAdapter);
        slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        slide_in_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slide_out_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        viewFlipper.setInAnimation(slide_in_left);
        viewFlipper.setOutAnimation(slide_out_right);

        buttonPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewFlipper.setInAnimation(slide_in_right);
                viewFlipper.setOutAnimation(slide_out_left);
                viewFlipper.showPrevious();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewFlipper.setInAnimation(slide_in_left);
                viewFlipper.setOutAnimation(slide_out_right);
                viewFlipper.showNext();
            }
        });

    }

    public ArrayList<Group> SetStandardGroups() {

        String group_names[] = { "08:00 to 8:55", "09:00 to 9:55", "10:00 to 10:55", "11:00 to 11:55", "12:00 to 12:55 ",
                "02:00 to 02:55","03:00 to 03:55","04:00 to 04:55"
        };

        String country_names[] = { "CSTE 1201", "CSTE 2201", "CSTE 3203", "CSTE 4101","CSTE 2104", "CSTE 2205",
                "CSTE 3106", "CSTE 2301", "CSTE 4201", "CSTE 3206", "CSTE 3204", "CSTE 2206",
                "CSTE 2203", "CSTE 2107", "CSTE 3204", "CSTE 3105" };


        //String times[]={"8:00","9:00","10:00","11:00","12:00","2:00","3:00","4:00","8:00","9:00","10:00","11:00","12:00","2:00","3:00","4:00"};



        String times[] = {
                "Computer Fundamental","Computer Fundamental Lab","Data Structure & Algorithm","Data Structure & Algorithm Lab",
                "Operating System Concepts","Operating System Concepts Lab","Wireless Communication","Digital Pulse Technique",
                "Digital Pulse Technique Lab","Computer Fundamental","Computer Fundamental Lab","Data Structure & Algorithm","Data Structure & Algorithm Lab",
                "Operating System Concepts","Operating System Concepts Lab","Wireless Communication"
        };
        ArrayList<Group> list = new ArrayList<Group>();

        ArrayList<Child> ch_list;

        int size =2;
        int j = 0;

        for (String group_name : group_names) {
            Group gru = new Group();
            gru.setName(group_name);

            ch_list = new ArrayList<Child>();
            for (; j < size; j++) {
                Child ch = new Child();


                ch.setTime(times[j]);
                ch.setName(country_names[j]);
                ch_list.add(ch);
            }
            gru.setItems(ch_list);
            list.add(gru);

            size = size + 2;
        }

        return list;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float sensitvity = 50;

        if((e1.getX() - e2.getX()) > sensitvity){
            viewFlipper.setInAnimation(slide_in_right);
            viewFlipper.setOutAnimation(slide_out_left);
            viewFlipper.showPrevious();
            Toast.makeText(Routine.this,
                    "Previous", Toast.LENGTH_SHORT).show();
        }else if((e2.getX() - e1.getX()) > sensitvity){
            viewFlipper.setInAnimation(slide_in_left);
            viewFlipper.setOutAnimation(slide_out_right);
            viewFlipper.showNext();
            Toast.makeText(Routine.this,
                    "Next", Toast.LENGTH_SHORT).show();
        }

        return true;
    }



    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

}