package il.ac.shenkar.ex5_3;

import java.util.Calendar;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateTaskActivity extends Activity 
{
	private DatabaseHandler db;
	 // This is a handle so that we can call methods on our service
    // This is the date picker used to select the date for our notification
    private DatePicker datePicker;
    private TimePicker timePicker;
    public static String EXTRA_MESSAGE = "il.ac.shenkar.todoapp.MESSAGE";
    String descTask = "";
    // The context to start the service in
    private Context mContext,gContext;
    // The android system alarm manager
    private AlarmManager am;
    private int index = -1;
    Task taskInfo = null;
    private EditText description;
    Context context;
    Double Lat,Lon;
    private LocationManager locationManager;
    Calendar c;

    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        // Get a reference to our date picker & time picker
        context = this;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);// Gets the Location manager
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        db = DatabaseHandler.getInstance(this);
        description = (EditText) findViewById(R.id.enter_task);
        index = (int) getIntent().getIntExtra("urlIndex", -1);
        if ( index > -1 )    // Preperation for edit button
        {
            ((Button) this.findViewById(R.id.create_button)).setText("Edit");
            taskInfo = db.getItem(index);
            description.setText(taskInfo.getDescription());
        }
        
        final Button CreateButton = (Button) findViewById(R.id.create_button);       
        CreateButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View view) 
            {              	
            	descTask = description.getText().toString();
            	if ( index > -1 )    // edit
                {
                    taskInfo.setDescription(descTask);
                    db.editTask(taskInfo);
                }
            	else db.pushTask(descTask);
            	
            	onDateSelectedButtonClick(view);
            	if (Lat!=null && Lon!=null)
            	{
            		createNotification(view);
            	}
            		
                finish();
            }
        });
        final Button BackButton = (Button) findViewById(R.id.back_button);
        BackButton.setOnClickListener(new View.OnClickListener() 
        {
        	@Override
            public void onClick(View view) 
            {  
        		finish();
            }
        });
        final Button GPSButton = (Button) findViewById(R.id.retrieve_button);
        GPSButton.setOnClickListener(new View.OnClickListener() 
        {
        	@Override
            public void onClick(View view) 
            {  
        		Intent intentGPS = new Intent(context, LocationActivity.class);
                final int result=1;
                startActivityForResult(intentGPS,result);
            }
        });
    }
    
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	Lat = data.getExtras().getDouble("LAT");
        Lon = data.getExtras().getDouble("LON");
        if (Lat!=0 && Lon!=0)
        	Toast.makeText(this, "Saved Location", Toast.LENGTH_LONG).show();
    }
    
    /**
     * This is the onClick called from the XML to set a new notification 
     */
    public void onDateSelectedButtonClick(View v)
    {
    	mContext = getApplicationContext();
        /** Create a new calendar set to the date chosen
          * we set the time to midnight (i.e. the first minute of that day) */
        c = Calendar.getInstance();
        c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        /** Request to start are service when the alarm date is upon us
          * We don't start an activity as we just want to pop up a notification into the system bar not a full activity */
        Intent intent = new Intent(mContext, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(EXTRA_MESSAGE,descTask);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, intent, 0);
        
        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
        // Notify the user what they just did
        if (timePicker.getCurrentMinute() <10)
        	Toast.makeText(this,"DateTime Notification set for:\n\t\t  "+ datePicker.getDayOfMonth() +"/"+ (datePicker.getMonth()+1) +"/"+ datePicker.getYear() + "\t\t" + timePicker.getCurrentHour() + ":0" + timePicker.getCurrentMinute(), Toast.LENGTH_LONG).show();
        else Toast.makeText(this,"DateTime Notification set for:\n\t\t  "+ datePicker.getDayOfMonth() +"/"+ (datePicker.getMonth()+1) +"/"+ datePicker.getYear() + "\t\t" + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute(), Toast.LENGTH_LONG).show();

    }
    
    public void createNotification(View view)
    { 
        gContext = getApplicationContext();
        Intent intent = new Intent(gContext, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(EXTRA_MESSAGE,descTask);
        Toast.makeText(this, "titleTask: "+descTask, Toast.LENGTH_LONG).show();
        PendingIntent pendingIntent = PendingIntent.getService(gContext, 0, intent, 0);
        @SuppressWarnings("unused")
 	   	AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        locationManager.addProximityAlert(Lat, Lon, 50, -1, pendingIntent);
        // Notify the user what they just did
        Toast.makeText(this,"GPS Notification set for:\n"+ Lat+", "+Lon, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onStart() 
    {
      super.onStart();
      // The rest of your onStart() code.
      EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() 
    {
      super.onStop();
      // The rest of your onStop() code.
      EasyTracker.getInstance().activityStop(this);
    }
}
