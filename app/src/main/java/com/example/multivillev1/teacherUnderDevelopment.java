package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class teacherUnderDevelopment extends AppCompatActivity  {

    TextView userNameTextView;
    String  id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_teacher_under_development);

        userNameTextView=(TextView) findViewById(R.id.userNameTextView);


        Intent intent = getIntent();
        id= intent.getStringExtra("id");


        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        userNameTextView.setText(dataBaseHelper.getUsernameFromDb(id));

        dataBaseHelper.close();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Intent i = new Intent(teacherUnderDevelopment.this,TeacherMainPage.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();

            }
        }, 4000);
    }


    @Override
    public void onBackPressed() {



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
