package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class studentSignUp extends AppCompatActivity implements View.OnClickListener {

    Button createAccountBtn,backBtn;
    EditText nameEditText,surnameEditText,usernameEditText,passwordEditText,emailEditText,birthDateEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_student_sign_up);


        createAccountBtn=(Button) findViewById(R.id.createAccountBtn );
        backBtn=(Button) findViewById(R.id.backBtn);
        nameEditText=(EditText) findViewById(R.id.nameEditText);
        surnameEditText=(EditText)findViewById(R.id.surnameEditText);
        usernameEditText=(EditText) findViewById(R.id.usernameEditText);
        emailEditText=(EditText) findViewById(R.id.emailEditText);
        birthDateEditText=(EditText) findViewById(R.id.birthDateEditText);
        passwordEditText=(EditText) findViewById(R.id.passwordEditText);

        createAccountBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);





    }




    @Override
    public void onClick(View v) {

        Utils.preventTwoClick(v);

        if ((nameEditText.getText().toString().matches("") || (surnameEditText.getText().toString()
                .matches("")) || (usernameEditText.getText().toString().matches("")) ||
                (passwordEditText.getText().toString().matches("")) || (emailEditText.getText()
                .toString().matches("")) || (birthDateEditText.getText().toString().matches("")))) {

            switch (v.getId()) {
                case R.id.createAccountBtn:
                    Toast.makeText(studentSignUp.this,
                            "You have to fill all the information fields!Please try again.",
                            Toast.LENGTH_LONG).show();
                    break;
                case R.id.backBtn:
                    startActivity(new Intent(studentSignUp.this, selectSignUp.class));
                    finish();
                    break;

            }

        } else if (!(birthDateEditText.getText().toString().matches("\\d{2}/\\d{2}/\\d{4}"))) {

            switch (v.getId()) {
                case R.id.createAccountBtn:
                    Toast.makeText(studentSignUp.this, "Date format is Wrong!Please try again!",
                            Toast.LENGTH_LONG).show();
                    birthDateEditText.getText().clear();
                    break;
                case R.id.backBtn:
                    startActivity(new Intent(studentSignUp.this, selectSignUp.class));
                    finish();
                    break;

            }


        } else {

            switch (v.getId()) {

                case R.id.createAccountBtn:


                    DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
                    dataBaseHelper.openDataBase();
                    boolean inserted = false;

                    String name = nameEditText.getText().toString();
                    String surname = surnameEditText.getText().toString();
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String birthdate = birthDateEditText.getText().toString();


                    if(dataBaseHelper.usernameExists(username)){
                        Toast.makeText(studentSignUp.this, "We are sorry!The username you entered, is already taken. Please choose another one.",
                                Toast.LENGTH_LONG).show();
                        usernameEditText.getText().clear();

                    }else{

                        inserted = dataBaseHelper.updateStudent(name,surname,username,password,email,birthdate,initializeStatsString());
                        if (inserted) {

                            Toast.makeText(studentSignUp.this, "Congratulations!Your account is ready!You can now login through the main page!",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(studentSignUp.this, MainActivity.class));
                            finish();
                        } else {

                            Toast.makeText(studentSignUp.this, "Registration failed!Please try again .",
                                    Toast.LENGTH_LONG).show();
                        }
                        nameEditText.getText().clear();
                        surnameEditText.getText().clear();
                        usernameEditText.getText().clear();
                        passwordEditText.getText().clear();
                        emailEditText.getText().clear();
                        birthDateEditText.getText().clear();


                    }

                    dataBaseHelper.close();


                    break;
                case R.id.backBtn:
                    startActivity(new Intent(studentSignUp.this, selectSignUp.class));
                    finish();
                    break;
            }
        }
    }

    //Αρχικοποίηση string με τα στατιστικά (παντού '-').
    public String initializeStatsString(){
        String  statsString = "";
        for (int i=0; i<1000; i++) { //Για κάθε πράξη...

            statsString += "-";
        }

        return statsString;
    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(studentSignUp.this, selectSignUp.class));
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
