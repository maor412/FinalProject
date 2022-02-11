package com.example.finalproject1;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Random;

public class Database {
    private FirebaseDatabase mDatabase;
    private Callback_Queue callback_queue;
    private Callback_Gallery callback_gallery;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    public Database(){
        mDatabase = FirebaseDatabase.getInstance("https://final-project-1-2de06-default-rtdb.firebaseio.com");
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

    }
    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public void setCallback_queue(Callback_Queue callback_queue){
        this.callback_queue = callback_queue;
    }

    public void setCallback_gallery(Callback_Gallery callback_gallery){
        this.callback_gallery = callback_gallery;
    }

    public void addQueue(AppCompatActivity activity, Queue queue){
        String uid = this.getCurrentUser().getUid();
        mDatabase.getReference("Stores").child(uid)
                .child("Queues")
                .child(queue.getId())
                .setValue(queue).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(callback_queue != null)
                    callback_queue.addQueueComplete(task.isSuccessful());

            }
        });
    }

    public void getQueues(){
        String uid = this.getCurrentUser().getUid();
        mDatabase.getReference("Stores").child(uid)
                .child("Queues").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(callback_queue != null){
                    ArrayList<Queue> queues = new ArrayList<>();
                    for(DataSnapshot data : snapshot.getChildren()){
                        Queue queue = data.getValue(Queue.class);
                        queues.add(queue);
                    }

                    callback_queue.getQueuesComplete(queues);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void removeQueue(AppCompatActivity activity, Queue q){
        String uid = this.getCurrentUser().getUid();
        mDatabase.getReference("Stores").child(uid)
                .child("Queues").child(q.getId()).removeValue().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(callback_queue != null){
                    callback_queue.removeQueueComplete(task.isSuccessful());
                }
            }
        });
    }

    private String getFileExtension(AppCompatActivity activity, Uri uri){
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(AppCompatActivity activity, Uri uri){
        Random random = new Random();
        int i = random.nextInt(1000000);
        String image_name = this.getCurrentUser().getUid() + i + "." + getFileExtension(activity, uri);
        String path = "StoreImages/"+this.getCurrentUser().getUid()+"/"+image_name;
        StorageReference storageReference = mStorage.getReference(path);
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(callback_gallery != null){
                    callback_gallery.imageUploadComplete(task.isSuccessful());
                }
            }
        });

    }

    public void downloadGallery(){
        String path = "StoreImages/"+this.getCurrentUser().getUid();
        StorageReference listRef = mStorage.getReference().child(path);
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        ArrayList<String> urls = new ArrayList<>();
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                           item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   if (callback_gallery != null){
                                       urls.add(uri.toString());
                                       callback_gallery.downloadGalleryComplete(urls);
                                   }

                               }
                           });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}


