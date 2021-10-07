package com.example.anilscreation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.anilscreation.Contact.AddStudent;
import com.example.anilscreation.Contact.AddStudentFragment;
import com.example.anilscreation.Contact.AddStudentListner;
import com.example.anilscreation.Contact.StudentDataPOJO;
import com.example.anilscreation.Contact.StudentListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;


public class ContactFragment extends Fragment implements AddStudentListner {

    ListView listStudent;
    Button btn_add,btn_clear;
    AddStudent addStudent;
    ArrayList<String>serialnumber;
    ArrayList<String> studentName;
    ArrayList<String> studentNumber;
    StudentDataPOJO studentData;
    ArrayList<StudentDataPOJO> studentDataList;
    ArrayList<StudentDataPOJO> BackUppojoList;
    ImageView img_delete;
    StudentListAdapter adapter;
    SharedPreferences pref;
    AddStudentListner addStudentListner;
    private static final String SHARED_PREF_NAME="mypref";
    int i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_contact, container, false);
        listStudent=v.findViewById(R.id.listViewStudent);
        btn_add=v.findViewById(R.id.btn_add);
        btn_clear=v.findViewById(R.id.btn_clearAll);
//        img_delete=findViewById(R.id.image_delete);
        pref= Objects.requireNonNull(this.getActivity()).getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        pref= getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);

                DialogFragment dialogFragment = new AddStudentFragment(getContext(),addStudentListner);
                dialogFragment.show(fragmentTransaction, "dialog");
            }
        });
        btn_clear.setEnabled(false);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudentListAdapter)listStudent.getAdapter()).clearAll();
//                studentDataList = new ArrayList<>();
//                Gson gson=new Gson();
//                String Contact=gson.toJson(studentDataList);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("MY_CONTACT_LIST","");
                editor.apply();
            }
        });
        studentName=new ArrayList<>();
        serialnumber=new ArrayList<>();
        studentNumber=new ArrayList<>();
        readContactList();
        if (null==studentDataList) {
            studentDataList = new ArrayList<>();

            if (null!=BackUppojoList){
                btn_clear.setEnabled(true);
                for (StudentDataPOJO data :BackUppojoList) {
                    studentDataList.add(data);
                    i = Integer.parseInt(data.getSlNo());
                }
            }
        }

        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudentDataPOJO dataPOJO=studentDataList.get(position);
                view.setBackgroundColor(Color.WHITE);
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+dataPOJO.getPhone()));
                try {
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        listStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final StudentDataPOJO data=studentDataList.get(position);
                view.setBackgroundColor(Color.YELLOW);
                Toast.makeText(getContext(), data.getName(), Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_baseline_delete_outline_24)
                        .setTitle("Are you sure ?")
                        .setMessage("You want to delete this contact")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                studentDataList.remove(data);
//                                ((StudentListAdapter)listStudent.getAdapter()).deleteItem(position);
                                adapter.notifyDataSetChanged();
                                saveList();
                            }

                        }).setNegativeButton("No",null)
                        .show();
                return true;
            }

        });
        return v;
    }

    private void readContactList() {
        Gson gson=new Gson();
        String contact_list=pref.getString("MY_CONTACT_LIST","");
        if (contact_list.isEmpty()){
            Toast.makeText(getContext(), "No Contact Found", Toast.LENGTH_SHORT).show();
        }else{
            Type type=new TypeToken<ArrayList<StudentDataPOJO>>(){

            }.getType();
            BackUppojoList=new ArrayList<>();
            BackUppojoList=gson.fromJson(contact_list,type);
            listStudent.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            adapter=new StudentListAdapter(this.getContext(),BackUppojoList);
            listStudent.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void add(String name, String number) {
        if (null!=studentDataList){
            btn_clear.setEnabled(true);
            String slNo=String.valueOf(i+1);
            studentData=new StudentDataPOJO(slNo,name,number);
            studentDataList.add(studentData);
//            serialnumber.add(slNo);
//            studentName.add(name);
//            studentNumber.add(number);
//            StudentListAdapter adapter=new StudentListAdapter(AddStudentDetailsActivity.this,serialnumber,studentName,studentNumber);
            listStudent.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            adapter=new StudentListAdapter(this.getContext(),studentDataList);
            listStudent.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            addStudent.clear();
            i++;
            saveList();
        }
    }

    private void saveList() {
        Gson gson=new Gson();
        String Contact=gson.toJson(studentDataList);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("MY_CONTACT_LIST",Contact);
        editor.apply();
    }

    @Override
    public void cancel() {
        addStudent.dismiss();
    }
}