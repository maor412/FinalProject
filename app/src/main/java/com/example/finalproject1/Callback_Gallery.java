package com.example.finalproject1;

import java.util.ArrayList;

public interface Callback_Gallery {
    void imageUploadComplete(boolean requestStatus);
    void downloadGalleryComplete(ArrayList<String> urls);
}
