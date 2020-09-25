package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_main);

        Button signInBtn=(Button) findViewById(R.id.signInBtn);
        Button signUpBtn=(Button) findViewById(R.id.signUpBtn);
        Button exitBtn=(Button) findViewById(R.id.exitBtn);

        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){
            case R.id.signInBtn:

                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                finish();

                break;
            case R.id.signUpBtn:

                startActivity(new Intent(MainActivity.this, selectSignUp.class));
                finish();
                break;
            case R.id.exitBtn:

                finish();
                System.exit(0);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
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
