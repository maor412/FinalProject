package com.example.finalproject1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";
    private Database database;
    private int currentIndex = 0;
    private ArrayList<String> imagesUrl;
    private ImageView gallery_image, gallery_IMG_next, gallery_IMG_prev;
    private AppCompatActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);

        database = new Database();
        database.setCallback_gallery(callback_gallery);
        database.downloadGallery();

        findViews(view);
        initViews();
        return view;
    }

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }

    private void initViews() {
        imagesUrl = new ArrayList<>();


        gallery_IMG_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagesUrl.size()>0)
                    nextImage();
            }
        });

        gallery_IMG_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagesUrl.size()>0)
                    prevImage();
            }
        });

    }

    private void findViews(View view) {
        gallery_image = view.findViewById(R.id.gallery_image);
        gallery_IMG_next = view.findViewById(R.id.gallery_IMG_next);
        gallery_IMG_prev = view.findViewById(R.id.gallery_IMG_prev);

    }

    private void nextImage(){
        currentIndex += 1;
        if(currentIndex >= imagesUrl.size()){
            currentIndex = 0;
        }

        Glide.with(activity).load(imagesUrl.get(currentIndex)).into(gallery_image);

    }

    private void prevImage(){
        currentIndex -= 1;
        if(currentIndex < 0){
            currentIndex = imagesUrl.size() - 1;
        }

        Glide.with(activity).load(imagesUrl.get(currentIndex)).into(gallery_image);

    }

    private Callback_Gallery callback_gallery = new Callback_Gallery() {
        @Override
        public void imageUploadComplete(boolean requestStatus) {

        }

        @Override
        public void downloadGalleryComplete(ArrayList<String> urls) {
            imagesUrl = urls;
            Glide.with(activity).load(imagesUrl.get(currentIndex)).into(gallery_image);
        }
    };


}
