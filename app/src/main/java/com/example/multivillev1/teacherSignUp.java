package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class teacherSignUp extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button createBtn,backBtn;
    EditText nameEditText,surnameEditText,usernameEditText,passwordEditText,emailEditText,instituteEditText;
    Spinner instituteSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_teacher_sign_up);

        createBtn=(Button) findViewById(R.id.createBtn );
        backBtn=(Button) findViewById(R.id.backBtn);
        nameEditText=(EditText) findViewById(R.id.nameEditText);
        surnameEditText=(EditText)findViewById(R.id.surnameEditText);
        usernameEditText=(EditText) findViewById(R.id.usernameEditText);
        emailEditText=(EditText) findViewById(R.id.emailEditText);
        passwordEditText=(EditText) findViewById(R.id.passwordEditText);
        instituteSpinner=(Spinner) findViewById(R.id.instituteSpinner);

        createBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        ArrayList<String> institutes=dataBaseHelper.institutes();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(teacherSignUp.this,
                android.R.layout.simple_spinner_item,institutes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instituteSpinner.setAdapter(adapter);
        instituteSpinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        if((nameEditText.getText().toString().matches("")||(surnameEditText.getText().toString().matches(""))||(usernameEditText.getText().toString().matches(""))||
                (passwordEditText.getText().toString().matches(""))||(emailEditText.getText().toString().matches(""))||(instituteSpinner.getSelectedItem().toString().matches("")))){

            switch (v.getId()) {
                case R.id.createBtn:
                    Toast.makeText(teacherSignUp.this,"You have to fill all the information fields!Please try again.",Toast.LENGTH_LONG).show();
                    break;
                case R.id.backBtn:
                    startActivity(new Intent(teacherSignUp.this, selectSignUp.class));
                    finish();
                    break;

            }

        }else {

            switch (v.getId()) {
                case R.id.createBtn:

                    DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
                    dataBaseHelper.openDataBase();
                    boolean inserted=false;

                    String name = nameEditText.getText().toString();
                    String surname = surnameEditText.getText().toString();
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String institute = instituteSpinner.getSelectedItem().toString();

                    if(dataBaseHelper.usernameExists(username)){
                          Toast.makeText(teacherSignUp.this, "We are sorry!The username you entered, is already taken. Please choose another one.",
                                Toast.LENGTH_LONG).show();
                           usernameEditText.getText().clear();

                    }else{

                       inserted = dataBaseHelper.updateTeacher(name, surname, username, password, email, institute);
                        if (inserted) {

                            Toast.makeText(teacherSignUp.this, "Congratulations!Your account is ready!You can now login through the main page!",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(teacherSignUp.this, MainActivity.class));
                            finish();

                        } else {

                            Toast.makeText(teacherSignUp.this, "Registration failed!Please try again .",
                                    Toast.LENGTH_LONG).show();
                        }
                        nameEditText.getText().clear();
                        surnameEditText.getText().clear();
                        usernameEditText.getText().clear();
                        passwordEditText.getText().clear();
                        emailEditText.getText().clear();


                    }

                    dataBaseHelper.close();




                    break;

                case R.id.backBtn:
                    startActivity(new Intent(teacherSignUp.this, selectSignUp.class));
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(teacherSignUp.this, selectSignUp.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
