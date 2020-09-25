package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class searchStudent extends AppCompatActivity implements View.OnClickListener {

    Button backBtn,goToStatBtn;
    String id,studentId;
    EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_search_student);

        backBtn=(Button) findViewById(R.id.backBtn);
        goToStatBtn=(Button) findViewById(R.id.goToStatsBtn);
        usernameEditText=(EditText)findViewById(R.id.usernameEditText);

        backBtn.setOnClickListener(this);
        goToStatBtn.setOnClickListener(this);


        Intent intent = getIntent();
        id= intent.getStringExtra("id");
    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()) {

            case R.id.backBtn:

                Intent i = new Intent(this,TeacherMainPage.class);
                i.putExtra("id",id);
                startActivity(i);
                finish();

                break;
            case R.id.goToStatsBtn:

                if((usernameEditText.getText().toString().matches(""))){
                    Toast.makeText(this,
                            "You have to fill all the information fields!Please try again.",
                            Toast.LENGTH_LONG).show();
                }else{
                    DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
                    dataBaseHelper.openDataBase();

                    String username = usernameEditText.getText().toString();


                    if(dataBaseHelper.usernameExists(username)){


                            studentId = dataBaseHelper.getStudentsIdFromUsername(username);
                            Intent k = new Intent(this, stats.class);
                            k.putExtra("id", studentId);
                            k.putExtra("role", "teacher");
                            k.putExtra("teachersId", id);
                            startActivity(k);
                            finish();

                    }else{
                        Toast.makeText(this, "We are sorry!The username you entered, doesn't exist . Please enter another one.",
                                Toast.LENGTH_LONG).show();
                        usernameEditText.getText().clear();

                    }
                    dataBaseHelper.close();
                }


                break;

        }

    }



    @Override
    public void onBackPressed() {

        Intent i = new Intent(this,TeacherMainPage.class);
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
