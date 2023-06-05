package com.example.exampleapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DAOUser {
    private DatabaseReference databaseReference;
    DAOUser(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }


    // 등록
    public Task<Void> add(User user){
        return databaseReference.push().setValue(user);
    }

    // 조회
    public Query get(){
        return databaseReference;
    }
}
