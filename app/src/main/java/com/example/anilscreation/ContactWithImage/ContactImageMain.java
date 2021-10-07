package com.example.anilscreation.ContactWithImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.an.biometric.LockBottomSheetBehavior;
import com.example.anilscreation.MainActivity;
import com.example.anilscreation.R;
import com.example.anilscreation.RoomDataBase.ContactPojo;
import com.example.anilscreation.RoomDataBase.ContactViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContactImageMain extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    Button imageUpload,save;
    EditText txtName,txtPhone;
    RelativeLayout layoutBottomSheet;
    ContactViewModel contactViewModel;
    String name,number,image;
    RecyclerView recyclerView;
    List<ContactPojo> list;
    static int uid = 1;
    BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_image_main);
        fab=findViewById(R.id.fab);
        recyclerView=findViewById(R.id.recyclerImageContact);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.Adapter recyclerViewImageAdapter=new RecyclerViewImageAdapter(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_contact, null);
//                setContentView(bottomSheetView);
//                imageUpload=findViewById(R.id.btnUpload);
//                save=findViewById(R.id.submit);
//                txtName=findViewById(R.id.name);
//                txtPhone=findViewById(R.id.number);
//                save.setOnClickListener(this);
//                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                } else {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
                Intent intent=new Intent(ContactImageMain.this,EntryForContact.class);
                startActivityForResult(intent,5000);
            }
        });
        contactViewModel= ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, new Observer<List<ContactPojo>>() {
            @Override
            public void onChanged(List<ContactPojo> contactPojos) {
                list=contactPojos;
                recyclerView.setAdapter(recyclerViewImageAdapter);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5000 && data!=null ){
            image = data.getStringExtra("Image");
            number = data.getStringExtra("Number");
            name = data.getStringExtra("Name");
            ContactPojo contactPojo = new ContactPojo(name, number, image);
            contactViewModel.insert(contactPojo);
            Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Contact Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private class RecyclerViewImageAdapter extends RecyclerView.Adapter<ContactViewHolder> {
        Activity activity;
        public RecyclerViewImageAdapter(Activity activity) {
            this.activity=activity;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(activity).inflate(R.layout.image_contact_container,parent,false);
            return new ContactViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
            ContactPojo contactPojo=list.get(position);
            Bitmap bitmap = strToBitmap(contactPojo.getIMAGE());
            holder.imageView.setImageBitmap(bitmap);
            holder.txtNumber.setText(contactPojo.getNUMBER());
            holder.txtName.setText(contactPojo.getNAME());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialogView bottomSheetDialogView=new BottomSheetDialogView(ContactImageMain.this,contactPojo);
                    bottomSheetDialogView.setCanceledOnTouchOutside(false);
                    bottomSheetDialogView.show();
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri=getFileUri("IMG_",contactPojo.getIMAGE());
                    if (uri != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setDataAndType(uri , "image/*");

                        PackageManager pm = activity.getPackageManager();
                        if (intent.resolveActivity(pm) != null)
                            try {
                                activity.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }else{
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private Uri getFileUri(String profile, String image) {
            File imageFile = null;
            Uri uri = null;
            byte[] imageByte = Base64.decode(image, 0);
            try {
                imageFile = createImageFile(profile);

                imageFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(imageFile.getPath());
                fo.write(imageByte);
                fo.flush();
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                uri = FileProvider.getUriForFile(activity, "com.example.anilscreation.provider", imageFile);
            }

            return uri;
        }
        private File createImageFile(String profile) throws IOException {
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(profile, ".jpg", storageDir);
            return imageFile;
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
        private Bitmap strToBitmap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.preview){
//
        }
        else if(item.getItemId()==R.id.delete){
            Toast.makeText(getApplicationContext(),"sending sms code",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }

    private class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageMenu;
        TextView txtName, txtNumber;
        LinearLayout linearLayout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_dp);
            txtName = itemView.findViewById(R.id.name_view);
            imageMenu = itemView.findViewById(R.id.menu_Spinner);
            txtNumber = itemView.findViewById(R.id.number_view);
            linearLayout = findViewById(R.id.linearLayout);

        }

    }
}