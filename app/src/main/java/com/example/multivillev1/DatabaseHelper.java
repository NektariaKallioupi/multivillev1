package com.example.multivillev1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    @SuppressLint("SdCardPath")
    private static String DB_PATH = "/data/data/com.example.multivillev1/databases/";
    private static String DB_NAME = "farm.db";
    private SQLiteDatabase myDatabase;
    private final Context myContext;
    Cursor c;


    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context,DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
            Log.i("DB....", "database available....");
        } else {


           /*
            File newdir= myContext.getDir("databases", Context.MODE_PRIVATE);
            if (!newdir.exists()) {
                newdir.mkdirs();
            }*/


            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
            Log.i("DB..", "database created.....");
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
            Log.e("CheckDb","DB not found");
            //database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

        // Toast.makeText(myContext, "Copy Done", 300).show();
    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)  {

    }

    //returns the number of rows a table has ,plus one
    public int getRowsCount(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM "+tableName;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;

    }

    public boolean usernameExists(String username) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"username"};
        String selection = "username"+ " =?";
        String[] selectionArgs = {username};
        String limit = "1";

        Cursor cursor = db.query("general_user", columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public ArrayList<String> institutes(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> myStringValues = new ArrayList<String>();
        Cursor result = db.query(true, "institute",
                new String[] {"institute_full_name"}, null, null, null, null,
                null, null);

        if (result.moveToFirst()) {
            do {

                myStringValues.add(result.getString(result.getColumnIndex("institute_full_name")));


            } while (result.moveToNext());
        }
        result.close();

        return myStringValues;
    }

    public String signIn(String username,String password){



        c=myDatabase.rawQuery("SELECT id\n" +

                "FROM general_user\n" +
                "WHERE username = '"+username+"'"+" and hashed_password='"+password+"'",new String[]{});

        StringBuffer buffer=new StringBuffer();
        String id="";
        while ((c.moveToNext())){
             id=c.getString(0);
            buffer.append(""+id);

        }



       return id;


    }

    public String  getStudentsIdFromUsername(String username){

        c=myDatabase.rawQuery("SELECT id\n" +

                "FROM general_user\n" +
                "WHERE username = '"+username+"'",new String[]{});

        StringBuffer buffer=new StringBuffer();
        String id="";
        while ((c.moveToNext())){
            id=c.getString(0);
            buffer.append(""+id);

        }



        return id;
    }


    //insert new user into general_user and update teacherTable with new teacher
    public boolean updateTeacher(String name,String surname,String username,String password,String email,String institute ){


        boolean newUser=false;
        boolean newTeacher=false;

        SQLiteDatabase db = this.getWritableDatabase();

        int count = getRowsCount("general_user")+1;



        String sql="INSERT INTO general_user (id,username, hashed_password, name, surname, email)"+"VALUES ('"+count+"','"+username+"','"+password+"','"+name+"','"+surname+"','"+email+"')";
        try
        {
            db.execSQL(sql);
            newUser=true;

        }
        catch (Exception e)
        {
            Log.e("ERROR", e.toString());
        }


        sql="INSERT INTO teacher (user_id, intitute_id)"+"VALUES ('"+count+"','"+institute+"')";
        try
        {
            db.execSQL(sql);
            newTeacher=true;
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.toString());
        }

        return (newUser && newTeacher);

    }

    //insert new user into general_user and update studentTable with new student
    public boolean updateStudent(String name,String surname,String username,String password,String email,String birthdate,String stats ) {


        boolean newUser=false;
        boolean newStudent=false;

        SQLiteDatabase db = this.getWritableDatabase();

        int count = getRowsCount("general_user")+1;

        String sql="INSERT INTO general_user (id,username, hashed_password, name, surname, email)"+"VALUES ('"+count+"','"+username+"','"+password+"','"+name+"','"+surname+"','"+email+"')";
        try
        {
            db.execSQL(sql);
            newUser=true;

        }
        catch (Exception e)
        {
            Log.e("ERROR", e.toString());
        }

        sql="INSERT INTO student(user_id, level, birthday, has_viewed_last_levels_lesson,stats)"+"VALUES ('"+count+"','1','"+birthdate+"',0,'"+stats+"')";
        try
        {
            db.execSQL(sql);
            newStudent=true;
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.toString());
        }

        return (newUser && newStudent);
    }

    //getUsernameFromDb
    public String getUsernameFromDb(String id){

             c=myDatabase.rawQuery("SELECT username\n" +
                "FROM general_user\n" +
                "WHERE id = '"+id+"'",new String[]{});

             //StringBuffer buffer=new StringBuffer();
             String username="";
             while ((c.moveToNext())){
                username=c.getString(0);
               // buffer.append(""+id);

             }

             return username;
    }


    //getStatsFomDb
    public String getStudentsStats(String id){

        c=myDatabase.rawQuery("SELECT stats\n" +
                "FROM student\n" +
                "WHERE user_id = '"+id+"'",new String[]{});

        //StringBuffer buffer=new StringBuffer();
        String stats="";
        while ((c.moveToNext())){
            stats=c.getString(0);
            // buffer.append(""+id);

        }

        return stats;
    }

    //setStats
    public void setStudentsStats(String id,String stats ){

        SQLiteDatabase db = this.getWritableDatabase();
        String sql= "UPDATE student SET stats = '"+stats+"'  WHERE user_id = '"+id+"'";
        try
        {
            db.execSQL(sql);

        }
        catch (Exception e)
        {
            Log.e("ERROR", e.toString());

        }
    }

    public String checkUsersRole(String id){

        String role="";
        c=myDatabase.rawQuery("SELECT *\n" +
                "FROM student\n" +
                "WHERE user_id= '"+id+"'",new String[]{});

        if(c.moveToFirst()) {

            role="student";

        }else{

            c=myDatabase.rawQuery("SELECT *\n" +
                    "FROM teacher\n" +
                    "WHERE user_id = '"+id+"'",new String[]{});

            if(c.moveToFirst()) {

                role = "teacher";
            }
         // we can add admin later if we want to
        }

        return role;
    }

    public String getStudentsLevel(String id){


        c=myDatabase.rawQuery("SELECT level\n" +
                "FROM student\n" +
                "WHERE user_id = '"+id+"'",new String[]{});
        String level="";
        while ((c.moveToNext())){
            level=c.getString(0);

        }

        return level;
    }

    public void updateStudentsLevel(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        String level=this.getStudentsLevel(id);

        int levelInt=Integer.parseInt(level);



     if(levelInt<=9) {

         levelInt=levelInt+1;

         String sql = "UPDATE student SET level = '" + levelInt + "'  WHERE user_id = '" + id + "'";

         try {
             db.execSQL(sql);

         } catch (Exception e) {
             Log.e("ERROR88888", e.toString());
         }
     }
    }


    public boolean getStudentsLevelLessonViewed(String id){


        c=myDatabase.rawQuery("SELECT has_viewed_last_levels_lesson\n" +
                "FROM student\n" +
                "WHERE user_id = '"+id+"'",new String[]{});
        String lessonViewed="";
        while ((c.moveToNext())){
            lessonViewed=c.getString(0);

        }

        if (lessonViewed.equals("0")){
            return false;

        }else {

            return  true;
        }
    }



    public  void updateStudentsLevelLesson(boolean viewed,String id){




        int passValue;
        if(viewed==false){
            passValue=0;
        }else{
            passValue=1;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        String level=this.getStudentsLevel(id);

        int levelInt=Integer.parseInt(level);

        if(levelInt==10 && getStudentsLevelLessonViewed(id)==true){

            //nothing

        }else {

            String sql =
                    "UPDATE student SET has_viewed_last_levels_lesson = '" + passValue + "'  WHERE user_id = '" + id + "'";
            try {
                db.execSQL(sql);

            } catch (Exception e) {
                Log.e("ERROR99999", e.toString());
            }

        }


    }















}
