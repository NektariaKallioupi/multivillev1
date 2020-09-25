package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;



public class loadingScreen extends AppCompatActivity {

    SaveSharedPreference preference;
    String role,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_loading_screen);

        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);

        dataBaseHelper.openDataBase();
        id=preference.getId(this);
        role=dataBaseHelper.checkUsersRole(id);

        dataBaseHelper.close();


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                // this code will be executed after 2 seconds
                if(id.length() == 0)
                {
                    // call Login Activity
                    startActivity(new Intent(loadingScreen.this, MainActivity.class));
                    finish();

                }
                else
                {
                    if(role.equals("teacher")){

                        // go to  teacher main page
                        Intent j = new Intent(loadingScreen.this,TeacherMainPage.class);
                        j.putExtra("id",preference.getId(loadingScreen.this));
                        startActivity(j);


                    }else {

                        // go to  Student main page
                        Intent i = new Intent(loadingScreen.this,StudentMainPage.class);
                        i.putExtra("id",preference.getId(loadingScreen.this));
                        startActivity(i);

                    }




                }

                finish();
            }
        }, 2000);

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
