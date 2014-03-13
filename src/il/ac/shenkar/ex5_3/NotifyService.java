package il.ac.shenkar.ex5_3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NotifyService extends Service 
{
	// Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification  
    public static final String INTENT_NOTIFY = "il.ac.shenkar.todoapp.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;
 // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    
  
    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder 
    {
        NotifyService getService() 
        {
            return NotifyService.this;
        }
    }
     
    @Override
    public void onCreate() 
    {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        String message = intent.getStringExtra(CreateTaskActivity.EXTRA_MESSAGE);
        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification(message);
         
        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }
 
    @Override
    public IBinder onBind(Intent intent) 
    {
        return mBinder;
    }
 
    
 
    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    @SuppressWarnings("deprecation")
	private void showNotification(String message) 
    {
        Toast.makeText(this, "You Have a Notification", Toast.LENGTH_LONG).show();
        // This is the 'title' of the notification
        CharSequence title = message;
        // This is the icon to use on the notification
        int icon = R.drawable.ic_launcher;
        // This is the scrolling text of the notification
        CharSequence text = "Your notification time is upon us";       
        // What time to show on the notification
        long time = System.currentTimeMillis();
         
        Notification notification = new Notification(icon, text, time);
 
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
 
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, title, text, contentIntent);
        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;  
        // Send the notification to the system.
        mNM.notify(NOTIFICATION, notification);
         
        // Stop the service when we are finished
        stopSelf();
    }
}