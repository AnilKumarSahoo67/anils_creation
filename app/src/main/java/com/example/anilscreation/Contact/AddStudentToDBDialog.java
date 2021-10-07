package com.example.anilscreation.Contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anilscreation.R;


public class AddStudentToDBDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private AddStudentDBListner addStudentListner;
    Button btnAdd,btnCancel,btnUpdate;
    EditText txtName,txtNumber;
    int mode;
    private StudentDataPOJO studentDataPOJO;

    public AddStudentToDBDialog(Activity activity, AddStudentDBListner addStudentListner, int mode) {  ///mode 1--add    2--Update
        super(activity);
        this.activity=activity;
        this.addStudentListner=addStudentListner;
        this.mode=mode;
    }

    public AddStudentToDBDialog(Activity activity, AddStudentDBListner addStudentListner, int mode, StudentDataPOJO studentDataPOJO) {  ///mode 1--add    2--Update
        super(activity);
        this.activity=activity;
        this.addStudentListner=addStudentListner;
        this.mode=mode;
        this.studentDataPOJO=studentDataPOJO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sttudent_to_d_b_dialog);
        btnAdd=findViewById(R.id.buttonAdd);
        btnUpdate=findViewById(R.id.buttonUpdate);
        btnCancel=findViewById(R.id.buttonCancel);
        txtName=findViewById(R.id.txtName);
        txtNumber=findViewById(R.id.txtPhone);
        if(mode==1){
            btnAdd.setVisibility(View.VISIBLE);
        }else {
            btnAdd.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
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
            case R.id.buttonUpdate:
                addStudentListner.update(txtName.getText().toString().trim(),txtNumber.getText().toString().trim(),studentDataPOJO);
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
    public void setNamePhone(StudentDataPOJO dataPOJO) {
        txtName.setText(dataPOJO.getName());
        txtNumber.setText(dataPOJO.getPhone());
    }
}