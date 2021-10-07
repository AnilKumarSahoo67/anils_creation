package com.an.biometric;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BiometricDialogV23 extends BottomSheetDialog implements View.OnClickListener {

    private Context context;

    private Button btnCancel;
    private ImageView imgLogo;
    private TextView itemTitle, itemDescription, itemSubtitle, itemStatus;

    private BiometricCallback biometricCallback;
    BottomSheetBehavior bottomSheetBehavior;

    public BiometricDialogV23(@NonNull Context context) {
        super(context, R.style.BottomSheetDialogTheme);
        this.context = context.getApplicationContext();
        setDialogView();
    }

    public BiometricDialogV23(@NonNull Context context, BiometricCallback biometricCallback) {
        super(context, R.style.BottomSheetDialogTheme);
        this.context = context.getApplicationContext();
        this.biometricCallback = biometricCallback;
        setDialogView();
    }

    public BiometricDialogV23(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected BiometricDialogV23(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void setDialogView() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.view_bottom_sheet, null);
        setContentView(bottomSheetView);
        bottomSheetBehavior=BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setDraggable(false);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        imgLogo = findViewById(R.id.img_logo);
        itemTitle = findViewById(R.id.item_title);
        itemStatus = findViewById(R.id.item_status);
        itemSubtitle = findViewById(R.id.item_subtitle);
        itemDescription = findViewById(R.id.item_description);

        updateLogo();
//        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheetView);
//        bottomSheetBehavior.setBottomSheetCallback(new MyBottomSheetCallBack());

    }

    public void setTitle(String title) {
        itemTitle.setText(title);
    }

    public void updateStatus(String status) {
        itemStatus.setText(status);
    }

    public void setSubtitle(String subtitle) {
        itemSubtitle.setText(subtitle);
    }

    public void setDescription(String description) {
        itemDescription.setText(description);
    }

    public void setButtonText(String negativeButtonText) {
        btnCancel.setText(negativeButtonText);
    }

    private void updateLogo() {
        try {
            Drawable drawable = getContext().getPackageManager().getApplicationIcon(context.getPackageName());
            imgLogo.setImageDrawable(drawable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_cancel) {
            dismiss();
            biometricCallback.onAuthenticationCancelled();
        }

    }

    private class MyBottomSheetCallBack extends BottomSheetBehavior.BottomSheetCallback {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                if (bottomSheetBehavior instanceof LockBottomSheetBehavior) {
                    ((LockBottomSheetBehavior) bottomSheetBehavior).setLocked(true);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    }
}
