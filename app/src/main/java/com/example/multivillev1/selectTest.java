package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class selectTest extends AppCompatActivity implements View.OnClickListener {

    Button backBtn,revisionTestBtn,testPerNumberBtn;
    String id;
    int levelOfStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_select_test);

        backBtn=(Button) findViewById(R.id.backBtn);
        revisionTestBtn=(Button) findViewById(R.id.revisionTestBtn);
        testPerNumberBtn=(Button) findViewById(R.id.testsPerNumberBtn);

        backBtn.setOnClickListener(this);
        revisionTestBtn.setOnClickListener(this);
        testPerNumberBtn.setOnClickListener(this);

        Intent intent = getIntent();
        id= intent.getStringExtra("id");

        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        try {
            levelOfStudent= Integer.parseInt(dataBaseHelper.getStudentsLevel(id));
        } catch(NumberFormatException nfe) {
            Toast.makeText(selectTest.this,"level string to int ",Toast.LENGTH_LONG).show();
        }

        dataBaseHelper.close();

    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){
            case R.id.backBtn:
                Intent i = new Intent(this,StudentMainPage .class);
                i.putExtra("id",id);
                startActivity(i);
                finish();

                break;
            case R.id.revisionTestBtn:
                if(levelOfStudent==0 || levelOfStudent ==1){
                    Toast.makeText(selectTest.this,"You have to pass the test of number 1 first! ",Toast.LENGTH_LONG).show();
                }else {
                    Intent j = new Intent(this, revisionTestIntro.class);
                    j.putExtra("id", id);
                    startActivity(j);
                    finish();
                }
                break;
            case R.id.testsPerNumberBtn:

                Intent k= new Intent(this,testsPerNumber.class);
                k.putExtra("id",id);
                startActivity(k);
                finish();
                break;
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
