package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TeacherMainPage extends AppCompatActivity implements View.OnClickListener {
    String id ;
    TextView userNameTextView;
    Button logOutBtn,exitBtn,classesBtn,studentsStatsBtn,helpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_teacher_main_page);

        userNameTextView=(TextView) findViewById(R.id.userNameTextView);
        logOutBtn=(Button) findViewById(R.id.logOutBtn);
        exitBtn=(Button) findViewById(R.id.exitBtn);
        classesBtn=(Button) findViewById(R.id.classesBtn);
        studentsStatsBtn=(Button) findViewById(R.id.studentsStatsBtn);
        helpBtn=(Button) findViewById(R.id.helpBtn);

        logOutBtn.setOnClickListener(this);
        exitBtn.setOnClickListener( this);
        classesBtn.setOnClickListener(this);
        studentsStatsBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);

        Intent intent = getIntent();
        id= intent.getStringExtra("id");

        //get username from db
        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

         userNameTextView.setText(dataBaseHelper.getUsernameFromDb(id));


        dataBaseHelper.close();

    }


    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()) {

            case R.id.exitBtn:

                finish();
                System.exit(0);

                break;
            case  R.id.logOutBtn:

                SaveSharedPreference.clearId(TeacherMainPage.this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.classesBtn:

                Intent i = new Intent(this,teacherUnderDevelopment.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();
                break;
            case R.id.studentsStatsBtn:

                Intent j= new Intent(this,searchStudent.class);
                j.putExtra("id",id);
                startActivity(j);
                finish();
                break;
            case R.id.helpBtn:

                startActivity(new Intent(this,viewer_pdf.class));
                break;

        }
    }


    @Override
    public void onBackPressed() {

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
