package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private RecyclerView.LayoutManager layoutManager;


    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        recyclerView = findViewById(R.id.recyclerview_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        firebaseAuth = FirebaseAuth.getInstance();

        cartAdapter = new CartAdapter(this, cartList);

        recyclerView.setAdapter(cartAdapter);


        cartList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동


        firebaseDatabase.getReference("Cart").child(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    for(DataSnapshot dataSnapshot: task.getResult().getChildren()){
                        Cart cart = dataSnapshot.getValue(Cart.class);
                        cartList.add(cart);
                        cartAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

//        databaseReference = firebaseDatabase.getReference("Cart"); //DB 연결 성공

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                cartList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    // 반복문으로 데이터 List를 추출해냄
//                    Cart cart = snapshot.getValue(Cart.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
//                    cartList.add(cart); // 담은 데이터들을 배열 리스트에 넣고 리사이클러뷰로 보낼 준비
//
//                }
//                cartAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // 디비를 가져오던 중 에러 발생 시
//                Log.e("CartAcivity", String.valueOf(databaseError.toException())); // 에러문 출력
//            }
//        });




    }
}