package com.example.anilscreation.Contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anilscreation.DBAccess.DatabaseHelper;
import com.example.anilscreation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddStudentToDB extends AppCompatActivity implements AddStudentDBListner {
    ListView listStudent;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    Button btn_add,btn_clear;
    AddStudentToDBDialog addStudent;
    DatabaseHelper databaseHelper;
    LinearLayout linearLayout;
    String formattedDate;
    Calendar c = Calendar.getInstance();
    StudentListAdapter adapter;
    ArrayList<StudentDataPOJO> contactList  =new ArrayList<StudentDataPOJO>();
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_to_d_b);
//        listStudent=findViewById(R.id.listViewStudent);
        linearLayout=findViewById(R.id.linearLayout);
        recyclerView=findViewById(R.id.recyclerView);
        btn_add=findViewById(R.id.btn_add);
        btn_clear=findViewById(R.id.btn_clearAll);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("My Contact");
        
        
        databaseHelper=new DatabaseHelper(this);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-ddd HH:mm:ss");
        formattedDate = df.format(c.getTime());
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent=new AddStudentToDBDialog(AddStudentToDB.this, (AddStudentDBListner) AddStudentToDB.this,1);
                addStudent.show();
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getContact();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >0) {
                    // Scroll Down
                    if (linearLayout.isShown()) {
                        linearLayout.setVisibility(View.GONE);

                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!linearLayout.isShown()) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void getContact() {
        contactList=databaseHelper.getContactList();
        if(contactList!=null) {
            layoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerAdapter=new AddStudentAdapter(this,contactList);
            recyclerView.setAdapter(recyclerAdapter);
//            listStudent.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//            adapter = new StudentListAdapter(AddStudentToDB.this, contactList);
//            listStudent.setAdapter(adapter);
            recyclerAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "No contact found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void add(String name, String number) {
        boolean isInserted=databaseHelper.insertContact(name,number,formattedDate);

        if(isInserted){
            Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();
            addStudent.clear();
            getContact();
        }else{
            Toast.makeText(this, "Contact not save", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(String name, String Number,StudentDataPOJO studentData) {
        boolean isUpdated=databaseHelper.UpdateContact(name,Number,formattedDate,studentData);
        if(isUpdated){
            Toast.makeText(this, "Contact Updated Successfully", Toast.LENGTH_SHORT).show();
            addStudent.dismiss();
            getContact();
        }else{
            Toast.makeText(this, "Contact not Updated", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void cancel() {
        addStudent.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private class AddStudentAdapter extends RecyclerView.Adapter<MyViewHolder> {
        Context context;
        List<StudentDataPOJO> list;
        public AddStudentAdapter(Context context, ArrayList<StudentDataPOJO> contactList) {
            this.context=context;
            this.list=contactList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student_list,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StudentDataPOJO dataPOJO=list.get(position);
            holder.txtSlNo.setText(dataPOJO.getSlNo());
            holder.txtname.setText(dataPOJO.getName());
            holder.txtNumber.setText(dataPOJO.getPhone());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+dataPOJO.getPhone()));
                    try {
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddStudentToDB.this);
                builder
                        .setTitle("Are you sure ?")
                        .setMessage("You Want To Modify Contact")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                addStudent=new AddStudentToDBDialog(AddStudentToDB.this, (AddStudentDBListner) AddStudentToDB.this,2,dataPOJO);
                                addStudent.show();
                                addStudent.setNamePhone(dataPOJO);
                            }
                        })
                        .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean isDeleted=databaseHelper.deleteContact(dataPOJO);
                                if (isDeleted){
                                    Toast.makeText(AddStudentToDB.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                                    getContact();
                                }else {
                                    Toast.makeText(AddStudentToDB.this, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })                        //Do nothing on no
                        .show();
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSlNo,txtname,txtNumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSlNo=itemView.findViewById(R.id.slno);
            txtname=itemView.findViewById(R.id.name);
            txtNumber=itemView.findViewById(R.id.number);

        }
    }
}