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

public class revisionTest extends AppCompatActivity implements View.OnClickListener {

    TextView questionTextView, farmerTextView;
    Button button1, button2, button3, nextQuestionButton,backBtn;
    int questionNumber;
    int correct_answers;
    int number1, number2;
    List<Integer> possible_answers = new ArrayList<>();
    float[] stats = new float[100]; //Ποσοστό επιτυχίας για κάθε πράξη.

    List <Integer> testQuestions = new ArrayList <Integer>();

    List <Integer> redBucket = new ArrayList <Integer>();
    List <Integer> orangeBucket = new ArrayList <Integer>();
    List <Integer> yellowBucket = new ArrayList <Integer>();
    List <Integer> greenBucket = new ArrayList <Integer>();
    List <Integer> darkGreenBucket = new ArrayList <Integer>();
    String id;
    int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_revision_test);

        questionTextView = findViewById(R.id.questionTextViewb);
        farmerTextView = findViewById(R.id.farmerTextViewb);
        button1 = findViewById(R.id.answerButton1b);
        button2 = findViewById(R.id.answerButton2b);
        button3 = findViewById(R.id.answerButton3b);
        backBtn=(Button)findViewById(R.id.backBtn);
        nextQuestionButton = findViewById(R.id.nextQuestionButtonb);

        /*
        button1.setBackgroundColor(Color.WHITE);
        button2.setBackgroundColor(Color.WHITE);
        button3.setBackgroundColor(Color.WHITE);
        nextQuestionButton.setBackgroundColor(Color.WHITE); */

        backBtn.setOnClickListener(this);

        Intent intent = getIntent();
        id= intent.getStringExtra("id");

        DatabaseHelper dataBaseHelper =new DatabaseHelper(this);
        dataBaseHelper.openDataBase();

        try {
            level= Integer.parseInt(dataBaseHelper.getStudentsLevel(id));
        } catch(NumberFormatException nfe) {
            Toast.makeText(revisionTest.this,"level string to int ",Toast.LENGTH_LONG).show();
        }


        String statsString =dataBaseHelper.getStudentsStats(id);
        dataBaseHelper.close();

        createStatsTable(statsString);

        createBuckets();
        createQuestions();
    }

    @Override
    public void onClick(View v) {
        Utils.preventTwoClick(v);
        switch(v.getId()) {

            case R.id.backBtn:
                Intent i = new Intent(this, selectTest.class);
                i.putExtra("id", id);
                startActivity(i);
                finish();
        }
    }

    //Γεμίζει τον float πίνακα stats, αποκρυπτογραφώντας το string εισόδου.
    public void createStatsTable(String statsString){
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


    //Δημιουργεί τα 5 buckets ανάλογα με το ποσοστό επιτυχίας για κάθε πράξη.
    public void createBuckets(){


        for (int i=0; i<100; i++) {
            //Προσθήκη στο κατάλληλο bucket (οι ερωτήσεις που δεν έχουν απαντηθεί καθόλου δεν εισάγονται σε κανένα).
            if (stats[i] >= 90){
                darkGreenBucket.add(i);
            } else if (stats[i] >= 70){
                greenBucket.add(i);
            } else if (stats[i] >= 50){
                yellowBucket.add(i);
            } else if (stats[i] >= 30){
                orangeBucket.add(i);
            } else if (stats[i] >= 0) {
                redBucket.add(i);
            }
        }
    }



    //region Recommendations

    public int nrOfRecommendationsfromRed()
    {
        boolean areThereAnyReds = false;
        boolean areThereEnoughReds = false;

        if(redBucket.size()>0)
        {
            areThereAnyReds=true;
        }

        if(redBucket.size()>=4)
        {
            areThereEnoughReds=true;
        }

        if(!areThereAnyReds)
        {
            return 0;
        }
        else if((areThereAnyReds) && (!areThereEnoughReds))
        {
            int howManyReds=redBucket.size();
            return howManyReds;
        }
        else
        {

            return 4;
        }
    }

    public int nrOfRecommendationsfromOrange(int remainder)
    {
        boolean areThereAnyOranges = false;
        boolean areThereEnoughOranges = false;

        if(orangeBucket.size()>0)
        {
            areThereAnyOranges=true;
        }

        if(orangeBucket.size()>=3+remainder)
        {
            areThereEnoughOranges=true;
        }

        if(!areThereAnyOranges)
        {
            return 0;
        }
        else if((areThereAnyOranges) && (!areThereEnoughOranges))
        {
            int howManyOranges=orangeBucket.size();

            return howManyOranges;
        }
        else
        {
            return 3;
        }
    }

    public int nrOfRecommendationsfromYellow(int remainder)
    {
        boolean areThereAnyYellows = false;
        boolean areThereEnoughYellows = false;

        if(yellowBucket.size()>0)
        {
            areThereAnyYellows=true;
        }

        if(yellowBucket.size()>=2+remainder)
        {
            areThereEnoughYellows=true;
        }

        if(!areThereAnyYellows)
        {
            return 0;
        }
        else if((areThereAnyYellows) && (!areThereEnoughYellows))
        {
            int howManyYellows=yellowBucket.size();

            return howManyYellows;
        }
        else
        {
            return 2;
        }
    }

    public int nrOfRecommendationsfromGreen(int remainder)
    {
        boolean areThereAnyGreens = false;
        boolean areThereEnoughGreens = false;

        if(greenBucket.size()>0)
        {
            areThereAnyGreens=true;
        }

        if(greenBucket.size()>=1+remainder)
        {
            areThereEnoughGreens=true;
        }

        if(!areThereEnoughGreens)
        {
            return 0;
        }
        else if((areThereAnyGreens) && (!areThereEnoughGreens))
        {
            int howManyGreens=greenBucket.size();
            return howManyGreens;
        }
        else
        {
            return 1;
        }
    }

    //endregion


    public void createQuestions()
    {
        Random r = new Random();

        int nrOfReds = nrOfRecommendationsfromRed();
        int rem1 = 4 - nrOfReds;
        int nrOfOranges = nrOfRecommendationsfromOrange(rem1);
        int rem2 = 3+rem1 - nrOfOranges;
        int nrOfYellows = nrOfRecommendationsfromYellow(rem2);
        int rem3 = 2+rem2 - nrOfYellows;
        int nrOfGreens = nrOfRecommendationsfromGreen(rem3);
        int nrOfDarkGreens = 1+rem3-nrOfGreens;

        Log.d("REDDDDDDDDDD",String.valueOf(nrOfReds));
        Log.d("GREENNNNNNN",String.valueOf(nrOfDarkGreens));

        for (int i=0; i<nrOfReds; i++){
            int random_index = r.nextInt(redBucket.size());
            testQuestions.add(redBucket.get(random_index));
            redBucket.remove(random_index);
        }

        for (int i=0; i<nrOfOranges; i++){
            int random_index = r.nextInt(orangeBucket.size());
            testQuestions.add(orangeBucket.get(random_index));
            orangeBucket.remove(random_index);
        }

        for (int i=0; i<nrOfYellows; i++){
            int random_index = r.nextInt(yellowBucket.size());
            testQuestions.add(yellowBucket.get(random_index));
            yellowBucket.remove(random_index);
        }

        for (int i=0; i<nrOfGreens; i++){
            int random_index = r.nextInt(greenBucket.size());
            testQuestions.add(greenBucket.get(random_index));
            greenBucket.remove(random_index);
        }

        for (int i=0; i<nrOfDarkGreens; i++){
            try{
                int random_index = r.nextInt(darkGreenBucket.size());
                testQuestions.add(darkGreenBucket.get(random_index));
                darkGreenBucket.remove(random_index);
            } catch (Exception e){

                Log.d("WRONGGGGGGG",e.getMessage());
                Toast.makeText(this, "Unable to load question", Toast.LENGTH_LONG).show();
            }
        }


        startTest();
    }




    public void startTest(){
        Collections.shuffle(testQuestions);
        questionNumber = 0;
        correct_answers = 0;
        number1 = testQuestions.get(0)/10 +1;
        number2 = testQuestions.get(0)%10 +1;
        fillTextViews();
        createAnswers();
    }


    private void createAnswers(){
        fillPossibleAnswers();

        Collections.shuffle(possible_answers);
        button1.setText(possible_answers.get(0).toString());
        button2.setText(possible_answers.get(1).toString());
        button3.setText(possible_answers.get(2).toString());
    }


    //Fills the list with all 3 possible answers.
    private void fillPossibleAnswers(){
        possible_answers.clear();
        possible_answers.add(number1*number2); //Correct answer

        List<Integer> temp_numbers = new ArrayList<>(); //Clone of the shuffled number list from 1-10.
        for (int i=1; i<11; i++){
            temp_numbers.add(i);
        }

        temp_numbers.remove(new Integer(number1)); //Removes the second_number from the clone in order not to get the same answer twice.

        Random r = new Random();
        int x = r.nextInt(9);
        possible_answers.add(number2*temp_numbers.get(x)); //Randomly chosen number, multiplied by level gives the first wrong answer.

        temp_numbers.remove(x); //Removes the first wrong number from the clone in order not to get the same answer twice.

        x = r.nextInt(8);
        possible_answers.add(number2*temp_numbers.get(x)); //Randomly chosen number, multiplied by level gives the second wrong answer.
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
        number1 = testQuestions.get(questionNumber)/10 +1;
        number2 = testQuestions.get(questionNumber)%10 +1;
        fillTextViews();
        createAnswers();

        if (questionNumber == 9){ //No more questions
            nextQuestionButton.setText("Show Results");
        }
    }


    //Fills the text views with the question and the question number
    private void fillTextViews(){
        farmerTextView.setText("Question " + (questionNumber+1));
        questionTextView.setText(number1 + " x " +  number2);

        nextQuestionButton.setEnabled(false);
    }



    //Whenever an answer is clicked, checks if the button's text is the correct answer.
    public void answerClicked(View view){
        Utils.preventTwoClick(view);
        Button b = (Button)view;
        int buttons_answer = Integer.parseInt(b.getText().toString()); //Number clicked

        if (buttons_answer == number1 * number2){ //Correct answer
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
            Intent intent = new Intent(getBaseContext(),selectTest.class);
            intent.putExtra("id",id);
            startActivity(intent);
            finish();
        }
    }


    //Is called when a correct answer is selected.
    private void correctAnswer(Button b){
        farmerTextView.setText("Correct!"); //Farmer's bubble
        b.setBackgroundColor(Color.GREEN);
        answerQuestion(number1, number2,true);
    }

    //Is called when a correct answer is selected.
    private void wrongAnswer(Button b){
        farmerTextView.setText("Wrong! The correct answer is " + number1*number2 + "."); //Farmer's bubble
        b.setBackgroundColor(Color.RED);
        answerQuestion(number1, number2,false);
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

        //Extra message
        if (correct_answers <= 4) {
            s += "You need more practice!";
        } else if (correct_answers < 7) {
            s += "You're almost there!";
        } else if (correct_answers <= 9) {
            s += "Great job!";
        } else { //correct_answers == 10
            s += "Perfect score! Well done!";
        }

        farmerTextView.setText(s);
        nextQuestionButton.setText("Back to Menu");
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
