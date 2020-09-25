package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;

public class StudentMainPage extends AppCompatActivity implements View.OnClickListener {
    String id,level;
    TextView userNameTextView;
    Button logOutBtn,exitBtn,lessonBtn,testBtn,statsBtn,helpBtn;
    String stats;
    int levelOfStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_student_main_page);

        userNameTextView=(TextView) findViewById(R.id.userNameTextView);
        logOutBtn=(Button) findViewById(R.id.logOutBtn);
        exitBtn=(Button) findViewById(R.id.exitBtn);
        lessonBtn=(Button) findViewById(R.id.lessonsBtn);
        testBtn=(Button) findViewById(R.id.testBtn);
        statsBtn=(Button) findViewById(R.id.statsBtn);
        helpBtn=(Button) findViewById(R.id.helpBtn);




        logOutBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        lessonBtn.setOnClickListener(this);
        testBtn.setOnClickListener(this );
        statsBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);

        Intent intent = getIntent();
        id= intent.getStringExtra("id");



        //get username from db
        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        userNameTextView.setText(dataBaseHelper.getUsernameFromDb(id));
        level=dataBaseHelper.getStudentsLevel(id);



        dataBaseHelper.close();


        try {
            levelOfStudent= Integer.parseInt(level);
        } catch(NumberFormatException nfe) {
            Toast.makeText(StudentMainPage.this,"level string to int ",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){

            case R.id.exitBtn:

                finish();
                System.exit(0);


                break;
            case R.id.logOutBtn:

                SaveSharedPreference.clearId(StudentMainPage.this);
                startActivity(new Intent(StudentMainPage.this, MainActivity.class));
                finish();

                break;
            case R.id.lessonsBtn:
                Intent i = new Intent(this,lessons.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();


                break;
            case R.id.testBtn:

                Intent j = new Intent(this,selectTest.class);
                j.putExtra("id",id);
                startActivity(j);
                finish();
                break;

            case R.id.statsBtn:

                if(levelOfStudent==0 || levelOfStudent ==1){
                    Toast.makeText(this,"You have to pass the test of number 1 first,to view Statistics! ",Toast.LENGTH_LONG).show();
                }else {
                    Intent l = new Intent(this, stats.class);
                    l.putExtra("id", id);
                    l.putExtra("role", "student");
                    l.putExtra("teachersId", "no");
                    startActivity(l);
                    finish();
                }
                break;
            case R.id.helpBtn:

                startActivity(new Intent(StudentMainPage.this,viewer_pdf.class));

                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
       // System.exit(0);
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
