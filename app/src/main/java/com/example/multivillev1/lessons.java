package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import pl.droidsonroids.gif.GifImageView;

import static java.lang.Boolean.TRUE;

public class lessons extends AppCompatActivity implements View.OnClickListener {

    String id,level;
    int levelInt;

    Button backBtn,lesson1Btn,lesson2Btn,lesson3Btn,lesson4Btn,lesson5Btn,lesson6Btn,lesson7Btn,lesson8Btn,lesson9Btn,lesson10Btn,tutorialBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_lessons);

        backBtn=(Button)findViewById(R.id.backBtn);
        lesson1Btn=(Button) findViewById(R.id.lesson1Btn);
        lesson2Btn=(Button) findViewById(R.id.lesson2Btn);
        lesson3Btn=(Button)findViewById(R.id.lesson3Btn);
        lesson4Btn=(Button) findViewById(R.id.lesson4Btn);
        lesson5Btn=(Button) findViewById(R.id.lesson5Btn);
        lesson6Btn=(Button)findViewById(R.id.lesson6Btn);
        lesson7Btn=(Button) findViewById(R.id.lesson7Btn);
        lesson8Btn=(Button) findViewById(R.id.lesson8Btn);
        lesson9Btn=(Button)findViewById(R.id.lesson9Btn);
        lesson10Btn=(Button)findViewById(R.id.lesson10Btn);
        tutorialBtn=(Button) findViewById(R.id.tutorialBtn);

        backBtn.setOnClickListener(this);
        lesson1Btn.setOnClickListener(this);
        lesson2Btn.setOnClickListener(this);
        lesson3Btn.setOnClickListener(this);
        lesson4Btn.setOnClickListener(this);
        lesson5Btn.setOnClickListener(this);
        lesson6Btn.setOnClickListener(this);
        lesson7Btn.setOnClickListener(this);
        lesson8Btn.setOnClickListener(this);
        lesson9Btn.setOnClickListener(this);
        lesson10Btn.setOnClickListener(this);
        tutorialBtn.setOnClickListener(this);

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
            Toast.makeText(lessons.this,"level string to int ",Toast.LENGTH_LONG).show();
        }

        if (!(SaveSharedPreference.getFirstTimeViewLessonOrNot(lessons.this))) {

            Toast.makeText(lessons.this,
                    "First let's watch a brief tutorial my friend!",
                    Toast.LENGTH_LONG).show();
            SaveSharedPreference.setFirstTimeViewLessonOrNot(lessons.this, TRUE);
            Intent j = new Intent(this, lessonSelected.class);
            j.putExtra("lessonPressed", 0);
            j.putExtra("id", id);
            startActivity(j);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        Utils.preventTwoClick(v);
        switch(v.getId()){
            case R.id.backBtn:

                Intent i = new Intent(this,StudentMainPage.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();
                backBtn.setEnabled(false);
                break;
            case R.id.lesson1Btn:

                checkLevel(1);
                break;
            case R.id.lesson2Btn:

                checkLevel(2);
                break;
            case R.id.lesson3Btn:

                checkLevel(3);
                break;
            case R.id.lesson4Btn:

                checkLevel(4);
                break;
            case R.id.lesson5Btn:

                checkLevel(5);
                break;
            case R.id.lesson6Btn:

                checkLevel(6);
                break;
            case R.id.lesson7Btn:

                checkLevel(7);
                break;
            case R.id.lesson8Btn:

                checkLevel(8);
                break;
            case R.id.lesson9Btn:

                checkLevel(9);
                break;
            case R.id.lesson10Btn:

                checkLevel(10);
                break;
            case R.id.tutorialBtn:

                checkLevel(0);
                break;
        }


    }


    public void checkLevel(int numberPressed){

        if(numberPressed==0){

            Intent k = new Intent(this, lessonSelected.class);
            k.putExtra("lessonPressed", numberPressed);
            k.putExtra("id", id);
            startActivity(k);
            finish();


        }else {


            if (levelInt < numberPressed) {

                Toast.makeText(lessons.this,
                        "Sorry this number is locked!You have to complete all the previous lessons and tests first!",
                        Toast.LENGTH_LONG).show();

            } else {

                Intent l = new Intent(this, lessonSelected.class);
                l.putExtra("lessonPressed", numberPressed);
                l.putExtra("id", id);
                startActivity(l);
                finish();


            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,StudentMainPage.class);
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
