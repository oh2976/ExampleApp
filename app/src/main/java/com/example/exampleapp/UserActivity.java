package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        EditText name_edit = findViewById(R.id.name_edit);
        EditText age_edit = findViewById(R.id.age_edit);
        Button addBtn = findViewById(R.id.add_btn);

        DAOUser dao = new DAOUser();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력값 변수에 담기
                String name = name_edit.getText().toString(); // 이름
                String age = age_edit.getText().toString(); // 나이

                User user = new User("", name, age);

                // 데이터베이스 사용자 등록
                dao.add(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();

                        // 입력창 초기화
                        name_edit.setText("");
                        age_edit.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } // onClick
        });
        //리스트 버튼

        Button listBtn = findViewById(R.id.list_btn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }// onCreate
}