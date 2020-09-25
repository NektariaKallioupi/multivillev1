package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class selectSignUp extends AppCompatActivity implements View.OnClickListener {

    Button teacherBtn,studentBtn,backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_select_sign_up);

        teacherBtn=(Button) findViewById(R.id.teacherBtn);
        studentBtn=(Button) findViewById(R.id.studentBtn);
        backBtn=(Button) findViewById(R.id.backBtn);

        teacherBtn.setOnClickListener(this);
        studentBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()) {
            case R.id.teacherBtn:
                startActivity(new Intent(selectSignUp.this, teacherSignUp.class));
                finish();
                break;
            case R.id.studentBtn:
                startActivity(new Intent(selectSignUp.this,studentSignUp.class));
                finish();
                break;
            case R.id.backBtn:
                startActivity(new Intent(selectSignUp.this, MainActivity.class));
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(selectSignUp.this, MainActivity.class));
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
