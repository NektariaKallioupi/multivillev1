package com.example.multivillev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class selectedNumberTest extends AppCompatActivity implements View.OnClickListener {
    String id;
    int level,levelOfStudent;//The times table in which the student is examined.
    TextView questionTextView, farmerTextView;
    Button button1, button2, button3, nextQuestionButton,backBtn;
    List<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)); //Gets randomized and provides the 10 questions.
    int questionNumber;
    int correct_answers;
    List<Integer> possible_answers = new ArrayList<>();
    GifImageView gifImageView; //For theory revision in case of <7 correct answers.
    boolean extra_slide;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_selected_number_test);

        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        level=intent.getIntExtra("testPressed",0);

        questionTextView = findViewById(R.id.questionTextView);
        farmerTextView = findViewById(R.id.farmerTextView);
        button1 = findViewById(R.id.answerButton1);
        button2 = findViewById(R.id.answerButton2);
        button3 = findViewById(R.id.answerButton3);
        backBtn=(Button) findViewById(R.id.backBtn);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);

        /*
        button1.setBackgroundColor(Color.WHITE);
        button2.setBackgroundColor(Color.WHITE);
        button3.setBackgroundColor(Color.WHITE);
        nextQuestionButton.setBackgroundColor(Color.WHITE);*/

        backBtn.setOnClickListener(this);

        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        try {
            levelOfStudent= Integer.parseInt(dataBaseHelper.getStudentsLevel(id));
        } catch(NumberFormatException nfe) {
            Toast.makeText(selectedNumberTest.this,"level string to int ",Toast.LENGTH_LONG).show();
        }

        dataBaseHelper.close();


        startTest();
    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch(v.getId()) {

           case R.id.backBtn:
                Intent i = new Intent(this, testsPerNumber.class);
                i.putExtra("id", id);
                startActivity(i);
                finish();
        }
    }



    //Randomizes the ArrayList "numbers" and multiplies each value with the current level to form the questions.
    private void startTest(){
        Collections.shuffle(numbers);
        questionNumber = 0;
        correct_answers = 0;
        fillTextViews();
        createAnswers(numbers.get(0));
    }




    /*
    Creates the possible answers for each question.
    One of them is always the correct one: level*second_number (Randomized number from 1 to 10. Different for each question.)
    The two additional (wrong) answers are creating by multiplying the level with a random number between 1-10, excluding the correct number.
    This ensures that even the wrong answers are from that times table, and thus, are believable.
    */
    private void createAnswers(int second_number){
        fillPossibleAnswers(second_number);

        Collections.shuffle(possible_answers);
        button1.setText(possible_answers.get(0).toString());
        button2.setText(possible_answers.get(1).toString());
        button3.setText(possible_answers.get(2).toString());
    }


    //Fills the list with all 3 possible answers.
    private void fillPossibleAnswers(int second_number){
        possible_answers.clear();
        possible_answers.add(level*second_number); //Correct answer

        List<Integer> temp_numbers = new ArrayList<>(numbers); //Clone of the shuffled number list from 1-10.
        temp_numbers.remove(new Integer(second_number)); //Removes the second_number from the clone in order not to get the same answer twice.

        Random r = new Random();
        int x = r.nextInt(9);
        possible_answers.add(level*temp_numbers.get(x)); //Randomly chosen number, multiplied by level gives the first wrong answer.

        temp_numbers.remove(x); //Removes the first wrong number from the clone in order not to get the same answer twice.

        x = r.nextInt(8);
        possible_answers.add(level*temp_numbers.get(x)); //Randomly chosen number, multiplied by level gives the second wrong answer.
    }


    //Creates the next question or shows results if the test is over.
    public void nextQuestion(){
        //Resets the background colors
        button1.setBackgroundResource(R.drawable.backbtn);
        button2.setBackgroundResource(R.drawable.backbtn);
        button3.setBackgroundResource(R.drawable.backbtn);

        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);

        //Disables the transition to the next question until an answer is clicked.
        nextQuestionButton.setEnabled(false);

        questionNumber++;
        fillTextViews();
        createAnswers(numbers.get(questionNumber));

        if (questionNumber == 9){ //No more questions
            nextQuestionButton.setText("Show Results");
        }
    }


    //Fills the text views with the question and the question number
    private void fillTextViews(){
        farmerTextView.setText("Question " + (questionNumber+1));
        questionTextView.setText(numbers.get(questionNumber) + " x " +  level);

        nextQuestionButton.setEnabled(false);
    }



    //Whenever an answer is clicked, checks if the button's text is the correct answer.
    public void answerClicked(View view){
        Utils.preventTwoClick(view);
        Button b = (Button)view;
        int buttons_answer = Integer.parseInt(b.getText().toString()); //Number clicked

        if (buttons_answer == numbers.get(questionNumber) * level){ //Correct answer
            correct_answers++;
            correctAnswer(b);
        } else { //Wrong answer
            wrongAnswer(b);
        }

        //Cannot select a second answer.
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);

        //Allows the transition to the next question.
        nextQuestionButton.setEnabled(true);
    }




    public void nextQuestionButtonClick(View view){
        Utils.preventTwoClick(view);
        if (questionNumber < 9){ //Next question
            nextQuestion();
        } else if (questionNumber == 9) { //Show results
            showResults();



        } else {
            if (extra_slide){
                showTheory();

            } else { //Return to menu

                Intent intent = new Intent(getBaseContext(),testsPerNumber.class);
                intent.putExtra("id",id);
                startActivity(intent);
               finish();
            }
        }
    }


    //Is called when a correct answer is selected.
    private void correctAnswer(Button b){
        farmerTextView.setText("Correct!"); //Farmer's bubble
        b.setBackgroundColor(Color.GREEN);
        answerQuestion(numbers.get(questionNumber), level, true);
    }

    //Is called when a correct answer is selected.
    private void wrongAnswer(Button b){
        farmerTextView.setText("Wrong! The correct answer is " + numbers.get(questionNumber)*level + "."); //Farmer's bubble
        b.setBackgroundColor(Color.RED);
        answerQuestion(numbers.get(questionNumber), level, false);
    }





    public void answerQuestion(int firstNumber, int secondNumber, boolean answeredCorrectly){

        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        String statsString =dataBaseHelper.getStudentsStats(id);


        String oldStatsString = statsString;
        String newStatsString = "";
        String line;

        int targetLine = ((firstNumber-1)*10 + (secondNumber-1))*10; //Η γραμμή που πρέπει να αλλαχθεί.
        Log.d("HERE",oldStatsString);
        for (int i=0; i<oldStatsString.length(); i+=10) { //Για κάθε πράξη...

            line = oldStatsString.substring(i,i+10);
            Log.d("line",line);
            if (i==targetLine){
                StringBuffer sb = new StringBuffer(line);
                sb.deleteCharAt(sb.length()-1); //Διαγραφή τελευταίας εγγραφής.

                //Προσθήκη νέας εγγραφής στην αρχή.
                if (answeredCorrectly){
                    line = "1" + sb.toString();
                } else {
                    line = "0" + sb.toString();
                }
                Log.d("line2",line);
            }

            newStatsString += line;
        }


        //Εισαγωγή στη βάση.
        dataBaseHelper.setStudentsStats(id,newStatsString);
        dataBaseHelper.close();
    }







    //Shows the test results.
    public void showResults(){
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);

        questionNumber++; //(=10) Just to pass the info to nextQuestionButtonClick and return to menu if clicked again.

        questionTextView.setVisibility(View.GONE); //Empties the board
        farmerTextView.setTextSize(20);

        String s = String.valueOf(correct_answers);
        s += " correct answers! ";

        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        //Extra message
        if (correct_answers <= 4) {
            s += "You need more practice!";

        } else if (correct_answers < 7) {
            s += "You're almost there!";
        } else if (correct_answers <= 9) {
            s += "Great job!";

            //update level
            if(levelOfStudent==level){

                dataBaseHelper.updateStudentsLevel(id);
                dataBaseHelper.updateStudentsLevelLesson(false,id);
            }

        } else { //correct_answers == 10
            s += "Perfect score! Well done!";
            //update
            if(levelOfStudent==level){
                dataBaseHelper.updateStudentsLevel(id);
                dataBaseHelper.updateStudentsLevelLesson(false,id);
            }
        }
        dataBaseHelper.close();
        farmerTextView.setText(s);

        if (correct_answers < 7){
            nextQuestionButton.setText("Next");
            extra_slide = true; //Is going to show the extra (theory) panel.
        } else {
            nextQuestionButton.setText("Back to Menu");
            extra_slide = false;
        }
    }




    private void showTheory(){
        TextView rememberTextView = findViewById(R.id.rememberTextView);
        rememberTextView.setTextColor(Color.BLACK);
        rememberTextView.setVisibility(View.VISIBLE);

        gifImageView = findViewById(R.id.gifView);
        gifImageView.setVisibility(View.VISIBLE);

        //Shows slide (theory panel) depending on level (times table). 6 and 9 not optimal!!!!!!
        switch (level){
            case 1:
                gifImageView.setImageResource(R.drawable.slide1_3);
                break;
            case 2:
                gifImageView.setImageResource(R.drawable.slide2_3);
                break;
            case 3:
                gifImageView.setImageResource(R.drawable.slide3_2);
                break;
            case 4:
                gifImageView.setImageResource(R.drawable.slide4_2);
                break;
            case 5:
                gifImageView.setImageResource(R.drawable.slide5_3);
                break;
            case 6:
                gifImageView.setImageResource(R.drawable.slide6_13);
                break;
            case 7:
                gifImageView.setImageResource(R.drawable.slide7_3);
                break;
            case 8:
                gifImageView.setImageResource(R.drawable.slide8_2);
                break;
            case 9:
                gifImageView.setImageResource(R.drawable.slide9_4);
                break;
            case 10:
                gifImageView.setImageResource(R.drawable.slide10_3);
                break;
            default:
                gifImageView.setImageResource(R.drawable.slide1_3);
                break;

        }


        nextQuestionButton.setText("Next");
        extra_slide = false;
    }









    @Override
    public void onBackPressed() {

       //nothing


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
