package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

//import pl.droidsonroids.gif.GifImageView;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class  lessonSelected extends AppCompatActivity implements View.OnClickListener {

    String id,level;
    List<Integer> slides = new ArrayList<>();
    int current_slide = 1;
    GifImageView gifImageView;
    int lessonPressed;
    Button backToLessonsMenuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_lesson_selected);

        gifImageView = findViewById(R.id.gifView);
        backToLessonsMenuBtn=findViewById(R.id.backToLessonsMenuBtn);
        backToLessonsMenuBtn.setOnClickListener(this);

        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        lessonPressed=intent.getIntExtra("lessonPressed",0);


        //get level from db
        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        level=dataBaseHelper.getStudentsLevel(id);

        dataBaseHelper.close();


        fillSlides(lessonPressed);

        gifImageView = findViewById(R.id.gifView);

        gifImageView.setImageResource(slides.get(current_slide));



    }

    //Fills the slides depending on the player's level
    public void fillSlides(int lvl){
        switch (lvl){
            case 0:
                slides.add(R.drawable.slide0_1);
                slides.add(R.drawable.slide0_2);
                slides.add(R.drawable.slide0_3);
                slides.add(R.drawable.slide0_4);
                slides.add(R.drawable.slide0_5);
                slides.add(R.drawable.slide0_6);
                slides.add(R.drawable.slide0_7);
                slides.add(R.drawable.slide0_8);
                slides.add(R.drawable.slide0_9);
                slides.add(R.drawable.slide0_10);
                slides.add(R.drawable.slide0_11);
                slides.add(R.drawable.slide0_12);
                slides.add(R.drawable.slide0_13);
                slides.add(R.drawable.slide0_14);
                slides.add(R.drawable.slide0_15);
                slides.add(R.drawable.slide0_16);
                break;
            case 1:
                slides.add(R.drawable.slide1_1);
                slides.add(R.drawable.slide1_2);
                slides.add(R.drawable.slide1_3);
                slides.add(R.drawable.slide1_4);
                slides.add(R.drawable.slide1_5);
                slides.add(R.drawable.slide1_6);
                slides.add(R.drawable.slide1_7);
                break;
            case 2:
                slides.add(R.drawable.slide2_1);
                slides.add(R.drawable.slide2_2);
                slides.add(R.drawable.slide2_3);
                slides.add(R.drawable.slide2_4);
                slides.add(R.drawable.slide2_5);
                slides.add(R.drawable.slide2_6);
                break;
            case 3:
                slides.add(R.drawable.slide3_1);
                slides.add(R.drawable.slide3_2);
                slides.add(R.drawable.slide3_3);
                slides.add(R.drawable.slide3_2);
                slides.add(R.drawable.slide3_3);
                break;
            case 4:
                slides.add(R.drawable.slide4_1);
                slides.add(R.drawable.slide4_2);
                slides.add(R.drawable.slide4_3);
                slides.add(R.drawable.slide4_4);
                slides.add(R.drawable.slide4_5);
                break;
            case 5:
                slides.add(R.drawable.slide5_1);
                slides.add(R.drawable.slide5_2);
                slides.add(R.drawable.slide5_3);
                slides.add(R.drawable.slide5_4);
                slides.add(R.drawable.slide5_5);
                slides.add(R.drawable.slide5_6);
                slides.add(R.drawable.slide5_7);
                slides.add(R.drawable.slide5_8);
                break;
            case 6:
                slides.add(R.drawable.slide6_1);
                slides.add(R.drawable.slide6_2);
                slides.add(R.drawable.slide6_3);
                slides.add(R.drawable.slide6_4);
                slides.add(R.drawable.slide6_5);
                slides.add(R.drawable.slide6_6);
                slides.add(R.drawable.slide6_7);
                slides.add(R.drawable.slide6_8);
                slides.add(R.drawable.slide6_9);
                slides.add(R.drawable.slide6_10);
                slides.add(R.drawable.slide6_11);
                slides.add(R.drawable.slide6_12);
                slides.add(R.drawable.slide6_13);
                slides.add(R.drawable.slide6_14);
                break;
            case 7:
                slides.add(R.drawable.slide7_1);
                slides.add(R.drawable.slide7_2);
                slides.add(R.drawable.slide7_3);
                slides.add(R.drawable.slide7_4);
                slides.add(R.drawable.slide7_5);
                break;
            case 8:
                slides.add(R.drawable.slide8_1);
                slides.add(R.drawable.slide8_2);
                slides.add(R.drawable.slide8_3);
                slides.add(R.drawable.slide8_4);
                slides.add(R.drawable.slide8_5);
                break;
            case 9:
                slides.add(R.drawable.slide9_1);
                slides.add(R.drawable.slide9_2);
                slides.add(R.drawable.slide9_3);
                slides.add(R.drawable.slide9_4);
                slides.add(R.drawable.slide9_5);
                slides.add(R.drawable.slide9_6);
                break;
            case 10:
                slides.add(R.drawable.slide10_1);
                slides.add(R.drawable.slide10_2);
                slides.add(R.drawable.slide10_3);
                slides.add(R.drawable.slide10_4);
                slides.add(R.drawable.slide10_5);
                slides.add(R.drawable.slide10_6);
                slides.add(R.drawable.slide10_7);
                slides.add(R.drawable.slide10_8);
                slides.add(R.drawable.slide10_9);
                slides.add(R.drawable.slide10_10);
                slides.add(R.drawable.slide10_11);
                break;
        }
    }


    //"Previous slide" button
    public void previousSlide(View view){
        Utils.preventTwoClick(view);
        if (current_slide > 0){
            current_slide--;
            gifImageView.setImageResource(slides.get(current_slide));
        }
    }


    //"Next slide" button
    public void nextSlide(View view){
        Utils.preventTwoClick(view);
        if (current_slide < slides.size()-1){
            current_slide++;
            gifImageView.setImageResource(slides.get(current_slide));

        } else if (current_slide == slides.size()-1){ //No more slides to show



           if(lessonPressed==0){
               Toast.makeText(this, "Tutorial Completed!", Toast.LENGTH_LONG).show();


           }else {


               int levelInt = Integer.parseInt(level);


               if (lessonPressed == levelInt) {
                   //updating level on db

                   DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
                   dataBaseHelper.openDataBase();

                   dataBaseHelper.updateStudentsLevelLesson(true, id);

                   dataBaseHelper.close();
               }


               Toast.makeText(this, "Lesson viewed!", Toast.LENGTH_LONG).show();


           }

            Intent i = new Intent(this, lessons.class);
            i.putExtra("id", id);
            startActivity(i);
            finish();

        }


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,lessons.class);
        i.putExtra("id",id);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){
            case R.id.backToLessonsMenuBtn:
                Intent i = new Intent(this,lessons.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();
                break;
        }
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
