package com.example.anilscreation.Contact;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.anilscreation.R;


public class AddStudent extends Dialog implements View.OnClickListener {
    private Activity activity;
    private AddStudentListner addStudentListner;
    Button btnAdd,btnCancel;
    EditText txtName,txtNumber;
    public AddStudent(@NonNull Activity activity,AddStudentListner addStudentListner) {
        super(activity);
        this.activity=activity;
        this.addStudentListner=addStudentListner;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dialog);
        btnAdd=findViewById(R.id.buttonAdd);
        btnCancel=findViewById(R.id.buttonCancel);
        txtName=findViewById(R.id.txtName);
        txtNumber=findViewById(R.id.txtPhone);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAdd:
                addStudentListner.add(txtName.getText().toString().trim(),txtNumber.getText().toString().trim());
                break;
            case R.id.buttonCancel:
                addStudentListner.cancel();
                break;

            default:
                dismiss();
                break;
        }
    }
    public void clear(){
        txtName.setText("");
        txtNumber.setText("");
    }


}
