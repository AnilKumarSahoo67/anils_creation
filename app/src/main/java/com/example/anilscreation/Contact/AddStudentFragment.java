package com.example.anilscreation.Contact;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.anilscreation.R;


public class AddStudentFragment extends DialogFragment implements View.OnClickListener {
    private Context activity;
    private AddStudentListner addStudentListner;
    Button btnAdd,btnCancel;
    EditText txtName,txtNumber;

    public AddStudentFragment(Context context,AddStudentListner context1) {
        this.activity=context;
        this.addStudentListner=context1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.student_dialog,container,false);
        btnAdd=v.findViewById(R.id.buttonAdd);
        btnCancel=v.findViewById(R.id.buttonCancel);
        txtName=v.findViewById(R.id.txtName);
        txtNumber=v.findViewById(R.id.txtPhone);

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
       return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }
}
