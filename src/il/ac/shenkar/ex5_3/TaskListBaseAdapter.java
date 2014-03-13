package il.ac.shenkar.ex5_3;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class TaskListBaseAdapter extends BaseAdapter {

	private String str = "TaskListBaseAdapter";
    
    private LayoutInflater l_inflater;
    private DatabaseHandler db;
    Context context;

    public TaskListBaseAdapter(Context context) 
    {
        this.context = context;
        this.db = DatabaseHandler.getInstance(context);
        this.l_inflater = LayoutInflater.from(context);
    }

    
    @Override
    public int getCount() 
    {
        return db.getCount();
    }

    @Override
    public boolean isEmpty()
    {
        return db.isEmpty();
    }

    @Override
    public Task getItem(int i)
    {
        return db.getItem(i);
    }

    @Override
    public long getItemId(int i)
    {
        return getItem(i).getId();
    }

    @Override
    public boolean hasStableIds ()
    {
        return true;
    }

    private final View.OnClickListener doneTask = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) 
        {
            int position = (Integer) view.getTag();
            Log.d(str,getItem(position).getDescription());
            db.removeTask(getItem(position));
            notifyDataSetChanged();
        }
    };
    
    private final View.OnClickListener editTask = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(context, CreateTaskActivity.class);
            intent.putExtra("urlIndex", (Integer) view.getTag());
            context.startActivity(intent);

        }
    };

    static class ViewHolder 
    {
        TextView description;
        Button doneButton,editButton;
    }
    
    @Override
    public View getView (int position, View convertView, ViewGroup parent) 
    {
        ViewHolder holder;
        if (convertView==null)
        {
            convertView = this.l_inflater.inflate(R.layout.task_done, null);
            holder = new ViewHolder();
            holder.description = (TextView) convertView.findViewById(R.id.task_description);
            holder.doneButton = (Button) convertView.findViewById(R.id.done_button);
            holder.doneButton.setOnClickListener(doneTask);
            holder.editButton = (Button) convertView.findViewById(R.id.edit_button);
            holder.editButton.setOnClickListener(editTask);
            convertView.setTag(holder);
        } 
        else 
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.description.setText(getItem(position).getDescription());
        holder.doneButton.setTag(position);
        holder.editButton.setTag(position);
        return convertView;
    }
}