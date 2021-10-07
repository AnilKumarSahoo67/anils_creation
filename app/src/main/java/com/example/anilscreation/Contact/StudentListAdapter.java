package com.example.anilscreation.Contact;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.anilscreation.R;

import java.util.ArrayList;

public class StudentListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> slnumber;
    private ArrayList<String> namelist;
    private ArrayList<String> numberlist;
    private ArrayList<StudentDataPOJO> studentDataList;

    public StudentListAdapter(Context context, ArrayList<String> slnumber, ArrayList<String> name, ArrayList<String> number) {
        this.context = context;
        this.slnumber = slnumber;
        this.namelist = name;
        this.numberlist = number;
    }

    public StudentListAdapter(Context context, ArrayList<StudentDataPOJO> studentDataList) {
        this.context=context;
        this.studentDataList=studentDataList;
    }

    @Override
    public int getCount() {
        return studentDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        Holder mHolder=null;
        StudentDataPOJO data=studentDataList.get(position);
        LayoutInflater layoutInflater;
        if (row==null){
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.layout_student_list,null);
            mHolder=new Holder();
            mHolder.SerialNumber=row.findViewById(R.id.slno);
            mHolder.tvName=row.findViewById(R.id.name);
            mHolder.tvPhone=row.findViewById(R.id.number);
            row.setTag(mHolder);
        }else {
            mHolder= (Holder) row.getTag();
        }
        mHolder.SerialNumber.setText(data.getSlNo());
        mHolder.tvName.setText(data.getName());
        mHolder.tvPhone.setText(data.getPhone());
        return row;
    }
    public void clearAll(){
        if (!(studentDataList.size() >0))
        {
            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
        }else {
            studentDataList.clear();
            notifyDataSetChanged();
        }
    }
    public void deleteItem(int item_position){
        for (int i=0;i<studentDataList.size();i++){
            if(i==item_position) {
                StudentDataPOJO dataPOJO = studentDataList.get(i);
                studentDataList.remove(dataPOJO);
            }
        }
        notifyDataSetChanged();
    }

    private class Holder {
        TextView SerialNumber;
        TextView tvName;
        TextView tvPhone;
    }
}
