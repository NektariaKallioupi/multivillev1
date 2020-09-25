package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

public class stats extends AppCompatActivity implements View.OnClickListener {

    String id,statsString,role,teachersId;
    Button backBtn;
    TextView num1TextView,num2TextView,num3TextView,num4TextView,num5TextView,num6TextView,num7TextView,num8TextView,num9TextView,num10TextView,userNameTextView;
    ImageView stars1ImageView,stars2ImageView,stars3ImageView,stars4ImageView,stars5ImageView,stars6ImageView,stars7ImageView,stars8ImageView,stars9ImageView,stars10ImageView;

    float[] stats = new float[100]; //Ποσοστό επιτυχίας για κάθε πράξη.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_stats);

        backBtn=(Button) findViewById(R.id.backBtn);
        num1TextView=(TextView) findViewById(R.id.num1TextView);
        num2TextView=(TextView) findViewById(R.id.num2TextView);
        num3TextView=(TextView) findViewById(R.id.num3TextView);
        num4TextView=(TextView) findViewById(R.id.num4TextView);
        num5TextView=(TextView) findViewById(R.id.num5TextView);
        num6TextView=(TextView) findViewById(R.id.num6TextView);
        num7TextView=(TextView) findViewById(R.id.num7TextView);
        num8TextView=(TextView) findViewById(R.id.num8TextView);
        num9TextView=(TextView) findViewById(R.id.num9TextView);
        num10TextView=(TextView) findViewById(R.id.num10TextView);
        userNameTextView=(TextView) findViewById(R.id.userNameTextView);

        stars1ImageView=(ImageView) findViewById(R.id.stars1ImageView);
        stars2ImageView=(ImageView) findViewById(R.id.stars2ImageView);
        stars3ImageView=(ImageView) findViewById(R.id.stars3ImageView);
        stars4ImageView=(ImageView) findViewById(R.id.stars4ImageView);
        stars5ImageView=(ImageView) findViewById(R.id.stars5ImageView);
        stars6ImageView=(ImageView) findViewById(R.id.stars6ImageView);
        stars7ImageView=(ImageView) findViewById(R.id.stars7ImageView);
        stars8ImageView=(ImageView) findViewById(R.id.stars8ImageView);
        stars9ImageView=(ImageView) findViewById(R.id.stars9ImageView);
        stars10ImageView=(ImageView) findViewById(R.id.stars10ImageView);

        backBtn.setOnClickListener(this);


        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        role=intent.getStringExtra("role");
        teachersId=intent.getStringExtra("teachersId");



        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        userNameTextView.setText(dataBaseHelper.getUsernameFromDb(id));
        statsString =dataBaseHelper.getStudentsStats(id);


        dataBaseHelper.close();

        createStatsTable();

        num1TextView.setText(String.valueOf(getAverage(1)));

        fillTextView(num1TextView, 1);
        fillTextView(num2TextView, 2);
        fillTextView(num3TextView, 3);
        fillTextView(num4TextView, 4);
        fillTextView(num5TextView, 5);
        fillTextView(num6TextView, 6);
        fillTextView(num7TextView, 7);
        fillTextView(num8TextView, 8);
        fillTextView(num9TextView, 9);
        fillTextView(num10TextView, 10);

        fillImageView(stars1ImageView, 1);
        fillImageView(stars2ImageView, 2);
        fillImageView(stars3ImageView, 3);
        fillImageView(stars4ImageView, 4);
        fillImageView(stars5ImageView, 5);
        fillImageView(stars6ImageView, 6);
        fillImageView(stars7ImageView, 7);
        fillImageView(stars8ImageView, 8);
        fillImageView(stars9ImageView, 9);
        fillImageView(stars10ImageView, 10);


    }

    //Γεμίζει τον float πίνακα stats, αποκρυπτογραφώντας το string εισόδου.
    public void createStatsTable(){

        String line;
        int sum, count;
        for (int i=0; i<statsString.length(); i+=10){ //Για κάθε πράξη...
            line = statsString.substring(i,i+10);

            sum = 0; //Πλήθος σωστών απαντήσεων
            count = 0; //Πλήθος συνολικών απαντήσεων

            for (int j=0; j<10; j++){ //Για κάθε μία από τις 10 προσπάθειες...

                if (String.valueOf(line.charAt(j)).equals("1") || String.valueOf(line.charAt(j)).equals("0")){ //Αν έχει δοθεί απάντηση
                    sum+=Integer.parseInt(String.valueOf(line.charAt(j)));
                    count++;
                }

            }

            if (count == 0){
                stats[i/10 + i%10] = -1f; //Δεν έχει δοθεί καμία απάντηση.
            } else {
                stats[i/10 + i%10] = 100*(sum/count); //Ποσοστό σωστών απαντήσεων.
            }
        }
        Log.d("stats", Arrays.toString(stats));
    }

    //Επιστρέφει το μέσο όρο για την προπαίδεια του αριθμού που δίνεται.
    public float getAverage(int number){
        int sum = 0;
        int count = 0;


        for (int i=number-1; i<100; i+=10){
            if (stats[i] != -1){
                sum += stats[i];
                count++;
            }
        }

        if (count == 0){
            return -1;
        } else {
            return sum/count;
        }
    }

    public void fillTextView(TextView textView, int number){
        float average = getAverage(number);
        if (average != -1){

            textView.setText(Math.round(average) + "%");

        }
    }


    public void fillImageView(ImageView imageView, int number){
        float average = getAverage(number);

        if (average >= 70){
            imageView.setImageResource(R.drawable.stars3);
        } else if (average >= 40){
            imageView.setImageResource(R.drawable.stars2);
        } else if (average >= 0){
            imageView.setImageResource(R.drawable.star1);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch (v.getId()) {

            case R.id.backBtn:

                if (role.equals("student") ) {

                    Intent i = new Intent(this,StudentMainPage.class);
                    i.putExtra("id",id);
                    startActivity(i);
                    finish();

                }else if (!(teachersId.equals("no"))){

                Intent j = new Intent(this, searchStudent.class);
                j.putExtra("id", teachersId);
                startActivity(j);
                finish();

                }

                break;
        }
    }

    @Override
    public void onBackPressed() {



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
