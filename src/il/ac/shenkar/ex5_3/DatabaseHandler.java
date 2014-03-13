package il.ac.shenkar.ex5_3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper 
{
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "taskManager";
 
    // Tasks table name
    private static final String TABLE_TASKS = "tasks";
 
    // Tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DES = "description";
    private static DatabaseHandler instance = null;
    Context context;
    private List<Task> tasks = null;
 
    public DatabaseHandler(Context context) 
    {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

       // Creating Tables
       public void onCreate(SQLiteDatabase db) 
       {
           String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                   + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DES + " TEXT"+ ");";
           db.execSQL(CREATE_TASKS_TABLE);
       }

       // Upgrading database
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
       {
           // Drop older table if existed
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

           // Create tables again
           onCreate(db);
       }


       //public accessor for the class
       public synchronized static DatabaseHandler getInstance(Context context)
       {
           if (instance==null)
           {
               instance = new DatabaseHandler(context);
           }
           return instance;
       }

       //add new task
       public void pushTask(String description)
       {
    	   SQLiteDatabase db = this.getWritableDatabase();
           ContentValues values = new ContentValues();
           // Contact Name
           values.put(KEY_DES, description); // Contact Name
           // Inserting Row
           db.insert(TABLE_TASKS, null, values);
           db.close(); // Closing database connection
           tasks = getAllTasks();
       }

       public Task getItem (int position) 
       { 
    	   return tasks.get(tasks.size()-position-1);
       }

       //remove any task
       public void removeTask(Task taskToRemove)
       {
         //  tasks.remove(taskToRemove);
           SQLiteDatabase db = this.getWritableDatabase();
           db.delete(TABLE_TASKS, KEY_DES + " = ?",new String[] { String.valueOf(taskToRemove.getDescription()) });
           db.close();
           tasks = getAllTasks();
       }
       
    // Updating single task
       public void editTask(Task task)
       {
           SQLiteDatabase db = null;
           db = this.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put(KEY_DES, task.getDescription());
           /** updating */
           db.update(TABLE_TASKS, values, KEY_ID + " = ?", new String[]{String.valueOf(task.getId())});
           db.close(); // Closing database connection
       }

       
       public int getCount() 
       {
           return tasks.size();
       }

       public boolean isEmpty()
       {
           return tasks.isEmpty();
       }

       // Getting All Tasks
       public List<Task> getAllTasks() 
       {
           List<Task> taskList = new ArrayList<Task>();
           // Select All Query
           String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
           SQLiteDatabase db = this.getWritableDatabase();
           Cursor cursor = db.rawQuery(selectQuery, null);
           // looping through all rows and adding to list
           if (cursor.moveToFirst()) 
           {
               do 
               {
                   Task contact = new Task(Integer.parseInt(cursor.getString(0)),cursor.getString(1));                    
                   taskList.add(contact);// Adding contact to list
               } 
               while (cursor.moveToNext());
           }
           // return contact list
           return taskList;
       }
       
       public void FillList()
       {
           Log.d("Reading: ", "Reading all tasks..");
           tasks = getAllTasks();
           for (Task cn : tasks) 
           {
               String log = "Id: "+cn.getId()+" ,Name: " + cn.getDescription() ;
               // Writing tasks to log
               Log.d("Name: ", log);
           }
       }
}