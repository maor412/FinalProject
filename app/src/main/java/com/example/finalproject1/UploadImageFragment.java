package com.example.finalproject1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class UploadImageFragment extends Fragment {
    private static final String TAG = "UploadImageFragment";
    private MaterialButton upload_BTN_uploadImg;
    private ImageView upload_IMG_galleryImg;
    private AppCompatActivity activity;
    private Database db;
    private Uri imageUri = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_image,container,false);
        findViews(view);
        initViews();
        return view;
    }

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }

    private void initViews() {
        db = new Database();
        db.setCallback_gallery(callback_gallery);
        upload_BTN_uploadImg.setEnabled(false);
        upload_BTN_uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null)
                    db.uploadImage(activity, imageUri);
            }
        });

        upload_IMG_galleryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void findViews(View view) {
        upload_BTN_uploadImg = view.findViewById(R.id.upload_BTN_uploadImg);
        upload_IMG_galleryImg = view.findViewById(R.id.upload_IMG_galleryImg);

    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        try {
                            imageUri = data.getData();
                            final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            upload_IMG_galleryImg.setImageBitmap(selectedImage);
                            upload_BTN_uploadImg.setEnabled(true);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(activity, "You haven't picked Image",Toast.LENGTH_LONG).show();
                    }
                }
            });

    public void uploadImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }

    private Callback_Gallery callback_gallery = new Callback_Gallery() {
        @Override
        public void imageUploadComplete(boolean requestStatus) {
            if(requestStatus){
                upload_BTN_uploadImg.setEnabled(false);
                upload_IMG_galleryImg.setImageResource(R.drawable.add_image);
                Toast.makeText(activity, "Image uploaded!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "Image upload failed", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void downloadGalleryComplete(ArrayList<String> urls) {

        }
    };

}
