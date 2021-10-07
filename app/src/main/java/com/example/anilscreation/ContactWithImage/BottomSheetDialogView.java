package com.example.anilscreation.ContactWithImage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.an.biometric.LockBottomSheetBehavior;
import com.example.anilscreation.R;
import com.example.anilscreation.RoomDataBase.ContactPojo;
import com.example.anilscreation.RoomDataBase.ContactViewModel;
import com.example.cropper.CropImage;
import com.example.cropper.CropImageView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BottomSheetDialogView extends BottomSheetDialog implements View.OnClickListener {
    Context context;
    Button btnDelete,btnSubmit;
    EditText txtName,txtNumber;
    ImageView imageView;
    ContactPojo  contactPojo;
    BottomSheetBehavior bottomSheetBehavior;
    ContactViewModel contactViewModel;
    public BottomSheetDialogView(@NonNull Context context, ContactPojo contactPojo) {
        super(context);
        this.context=context;
        this.contactPojo=contactPojo;
        setDialogView();
    }

    private void setDialogView() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_contact, null);
        setContentView(bottomSheetView);
        bottomSheetBehavior=BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setDraggable(true);
        btnDelete=bottomSheetView.findViewById(R.id.btnUpload);
        btnSubmit=bottomSheetView.findViewById(R.id.submit);
        txtName=bottomSheetView.findViewById(R.id.name);
        txtNumber=bottomSheetView.findViewById(R.id.number);
        imageView=bottomSheetView.findViewById(R.id.imageView);
        Bitmap bitmap=strToBitmap(contactPojo.getIMAGE());
        txtName.setText(contactPojo.getNAME());
        txtNumber.setText(contactPojo.getNUMBER());
        imageView.setImageBitmap(bitmap);
        contactViewModel= ViewModelProviders.of((FragmentActivity) context).get(ContactViewModel.class);
        btnDelete.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private Bitmap strToBitmap(String image) {
        byte[] bytes= Base64.decode(image,Base64.DEFAULT);
        InputStream inputStream=new ByteArrayInputStream(bytes);
        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                contactViewModel.updateCotact(txtName.getText().toString().trim(),txtNumber.getText().toString().trim(),contactPojo.getID());
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
               break;
            case R.id.btnUpload:

//                ((ContactImageMain)deleteSingleContact(contactPojo);
                contactViewModel.deleteContact(contactPojo);
                Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
        }
    }
}
