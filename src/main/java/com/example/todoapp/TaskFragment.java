package com.example.todoapp;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TaskFragment extends Fragment {

    public static String ARG_TASK_ID = "com.example.todoapp.ARG_TASK_ID";
    private Task task = new Task();
    private final Calendar calendar = Calendar.getInstance();
    private EditText dateField;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        UUID id = (UUID) getArguments().getSerializable(ARG_TASK_ID);

        Task taskNew = TaskStorage.getInstance().getTask(id);
        assert taskNew != null;
        this.task = taskNew;
    }

    public static TaskFragment newInstance (UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID, id);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        dateField= view.findViewById(R.id.task_date);
        setupDateFieldValue(task.getDate());
        DatePickerDialog.OnDateSetListener date=(view12,year,month,day)->{
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            setupDateFieldValue(calendar.getTime());
            task.setDate(calendar.getTime());
        };
        dateField.setOnClickListener(view1->
                new DatePickerDialog(getContext(),date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                        .show());
        EditText name = view.findViewById(R.id.task_name);
        CheckBox checkBox = view.findViewById(R.id.task_done);
        Spinner categorySpinner = view.findViewById(R.id.task_category);
        categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item,Category.values()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
            {
                task.setCategory(Category.values()[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //empty
            }
        });
        categorySpinner.setSelection(task.getCategory().ordinal());
        name.setText(task.getName());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty
            }

            @Override
            public void onTextChanged(CharSequence name, int start, int before, int count) {
                task.setName(name.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //empty
            }
        });

        checkBox.setChecked(task.isDone());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
        });

        return view;
    }

    private void setupDateFieldValue(Date date) {
        Locale locale=new Locale("pl","PL");
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy",locale);
        dateField.setText(dateFormat.format(date));
    }
}