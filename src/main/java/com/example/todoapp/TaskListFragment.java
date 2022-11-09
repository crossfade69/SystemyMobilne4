package com.example.todoapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class TaskListFragment extends Fragment {

    public static final String KEY_EXTRA_TASK_ID = "todoapp-task-id";
    private RecyclerView recyclerView;
    private boolean subtitleVisible;
    public TaskListFragment() {
        //empty constructor
    }
    public void updateSubtitle(){
        TaskStorage taskStorage=TaskStorage.getInstance();
        List<Task> tasks =taskStorage.getTasks();
        int todoTasksCount=0;
        for(Task task:tasks){
            if(!task.isDone())
            {
                todoTasksCount++;
            }
        }
        String subtitle=getString(R.string.subtitle_format,todoTasksCount);
        if(!subtitleVisible)
        {
            subtitle=null;
        }
        AppCompatActivity appCompatActivity=(AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_task_menu,menu);
        MenuItem subtitleItem=menu.findItem(R.id.show_subtitle);
        if(subtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else
        {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.new_task:
                Task task=new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent=new Intent(getActivity(),MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                subtitleVisible=!subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView () {
        TaskStorage storage = TaskStorage.getInstance();
        List<Task> tasks = storage.getTasks();

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            recyclerView.setAdapter(new TaskAdapter(tasks));
            return;
        }

        adapter.notifyDataSetChanged();
        updateSubtitle();
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView date;
        private Task task = null;
        private ImageView iconImageView;
        private CheckBox checkBox;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
            });
            iconImageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.task_date);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        void bind (Task task) {
            this.task = task;
            name.setText(task.getName());
            date.setText(task.getDate().toString());
            if(task.getCategory().equals(Category.Home)){
                iconImageView.setImageResource(R.drawable.ic_menu_add);
            }else{
                iconImageView.setImageResource(R.drawable.ic_studies);
            }
            checkBox.setChecked(task.isDone());
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private final List<Task> tasks;

        public TaskAdapter (List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TaskHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            holder.bind(tasks.get(position));
            CheckBox checkBox=holder.getCheckBox();
            checkBox.setChecked((tasks.get(position).isDone()));
            checkBox.setOnCheckedChangeListener((buttonView,isChecked)->
                tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked));

        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}