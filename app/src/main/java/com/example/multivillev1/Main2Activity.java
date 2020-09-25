package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.Serializable;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    Button loginBtn,backBtn;
    EditText usernameEditText,passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_main2);

        loginBtn=(Button) findViewById(R.id.loginBtn);
        backBtn=(Button) findViewById(R.id.exitBtn);

        usernameEditText= (EditText) findViewById(R.id.usernameEditText);
        passwordEditText=(EditText) findViewById(R.id.passwordEditText);

        loginBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()){
            case R.id.loginBtn:

                DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
                dataBaseHelper.openDataBase();


                String username=usernameEditText.getText().toString();
                String password=passwordEditText.getText().toString();

                String id=dataBaseHelper.signIn(username,password);
                String role=dataBaseHelper.checkUsersRole(id);


                if(id != ""){

                    if(role.equals("teacher")){
                        Toast.makeText(Main2Activity.this,"Login successful!",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(this,TeacherMainPage.class);
                        i.putExtra("id",id);
                        startActivity(i);
                        SaveSharedPreference.setId(Main2Activity.this,id);
                        finish();

                    }else if(role.equals("student")){



                        Toast.makeText(Main2Activity.this,"Login successful!",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(this,StudentMainPage.class);
                        i.putExtra("id",id);
                        startActivity(i);
                        SaveSharedPreference.setId(Main2Activity.this,id);
                        finish();


                    }else{

                        Toast.makeText(Main2Activity.this,"Please try again!Something went wrong",Toast.LENGTH_LONG).show();
                    }


                    usernameEditText.getText().clear();
                    passwordEditText.getText().clear();



                }else{

                    Toast.makeText(Main2Activity.this,"Incorrect Username or Password!Please Try again ",Toast.LENGTH_LONG).show();
                    usernameEditText.getText().clear();
                    passwordEditText.getText().clear();

                }
                dataBaseHelper.close();
                break;
            case R.id.exitBtn:
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Main2Activity.this, MainActivity.class));
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
