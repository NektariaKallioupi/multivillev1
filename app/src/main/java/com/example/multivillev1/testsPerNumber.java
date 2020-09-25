package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class testsPerNumber extends AppCompatActivity implements View.OnClickListener {

    Button backBtn,test1Btn,test2Btn,test3Btn,test4Btn,test5Btn,test6Btn,test7Btn,test8Btn,test9Btn,test10Btn;
    String id,level;
    int levelInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_tests_per_number);

        backBtn=(Button)findViewById(R.id.backBtn);
        test1Btn=(Button) findViewById(R.id.test1Btn);
        test2Btn=(Button) findViewById(R.id.test2Btn);
        test3Btn=(Button)findViewById(R.id.test3Btn);
        test4Btn=(Button) findViewById(R.id.test4Btn);
        test5Btn=(Button) findViewById(R.id.test5Btn);
        test6Btn=(Button)findViewById(R.id.test6Btn);
        test7Btn=(Button) findViewById(R.id.test7Btn);
        test8Btn=(Button) findViewById(R.id.test8Btn);
        test9Btn=(Button)findViewById(R.id.test9Btn);
        test10Btn=(Button)findViewById(R.id.test10Btn);


        backBtn.setOnClickListener(this);
        test1Btn.setOnClickListener(this);
        test2Btn.setOnClickListener(this);
        test3Btn.setOnClickListener(this);
        test4Btn.setOnClickListener(this);
        test5Btn.setOnClickListener(this);
        test6Btn.setOnClickListener(this);
        test7Btn.setOnClickListener(this);
        test8Btn.setOnClickListener(this);
        test9Btn.setOnClickListener(this);
        test10Btn.setOnClickListener(this);



        Intent intent = getIntent();
        id= intent.getStringExtra("id");

        //get level from db
        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        level=dataBaseHelper.getStudentsLevel(id);

        dataBaseHelper.close();

        try {
            levelInt= Integer.parseInt(level);
        } catch(NumberFormatException nfe) {
            Toast.makeText(this,"problem",Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch(v.getId()) {

            case R.id.backBtn:

                Intent i = new Intent(this,selectTest.class);
                i.putExtra("id",id);
                i.putExtra("level",level);
                startActivity(i);
                finish();
                break;
            case R.id.test1Btn:

                checkLevel(1);
                break;
            case R.id.test2Btn:

                checkLevel(2);
                break;
            case R.id.test3Btn:

                checkLevel(3);
                break;
            case R.id.test4Btn:

                checkLevel(4);
                break;
            case R.id.test5Btn:

                checkLevel(5);
                break;
            case R.id.test6Btn:

                checkLevel(6);
                break;
            case R.id.test7Btn:

                checkLevel(7);
                break;
            case R.id.test8Btn:

                checkLevel(8);
                break;
            case R.id.test9Btn:

                checkLevel(9);
                break;
            case R.id.test10Btn:

                checkLevel(10);
                break;
        }
    }

    public void checkLevel(int numberPressed){

        if (levelInt < numberPressed) {

            Toast.makeText(this,
                    "Sorry this test is locked!You have to complete all the previous lessons and tests first!",
                    Toast.LENGTH_LONG).show();

        } else {

            DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
            dataBaseHelper.openDataBase();

            boolean viewLesson=dataBaseHelper.getStudentsLevelLessonViewed(id);

            dataBaseHelper.close();


           if(viewLesson) {

                Intent i = new Intent(this, selectedNumberTest.class);
                i.putExtra("testPressed", numberPressed);
                i.putExtra("id", id);
                startActivity(i);
                finish();
           }else{

                Toast.makeText(this,
                        "Sorry this test is locked!Go watch the next lesson please!",
                        Toast.LENGTH_LONG).show();

            }

        }




    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,selectTest.class);
        i.putExtra("id",id);
        startActivity(i);
        finish();

    }



    private void hideSystemUI() {
        // Enables sticky immersive mode.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    public void onResume(){
        super.onResume();
        hideSystemUI();
    }

}
