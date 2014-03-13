package il.ac.shenkar.ex5_3;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity 
{
	Context context;
    TaskListBaseAdapter adapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        DatabaseHandler db = DatabaseHandler.getInstance(this);
        db.FillList();
        ListView listView = (ListView) findViewById(R.id.task_list);
        adapter = new TaskListBaseAdapter(this);
        listView.setAdapter(adapter);
        Button newTaskButton = (Button) findViewById(R.id.new_task_button);
        newTaskButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View view) 
            {
                startActivity(new Intent(context, CreateTaskActivity.class));
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
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