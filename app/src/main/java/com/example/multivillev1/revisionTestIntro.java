package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class revisionTestIntro extends AppCompatActivity implements View.OnClickListener {

    Button backBtn,goToRevisionTestBtn;
    TextView levelsLabelTextView;
    String id,level;
    int levelInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_revision_test_intro);

        backBtn=(Button) findViewById(R.id.backBtn);
        goToRevisionTestBtn=(Button) findViewById(R.id.goToRevisionTestBtn);

        levelsLabelTextView=(TextView) findViewById(R.id.levelsLabelTextView);

        backBtn.setOnClickListener(this);
        goToRevisionTestBtn.setOnClickListener(this);


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
            Toast.makeText(this,"level string to int ",Toast.LENGTH_LONG).show();
        }



    }


    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()) {
            case R.id.backBtn:


                Intent i = new Intent(this, selectTest.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();
                break;
            case R.id.goToRevisionTestBtn:

                Intent j = new Intent(this, revisionTest.class);
                j.putExtra("id",id);
                startActivity(j);
                finish();
                break;
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
