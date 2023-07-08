package com.example.exampleapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReferenceProduct;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Cart> arrayList;

//    private OrderCompleteAdapter completeAdapter;
//    private List<MyOrder> myOrderList;

    Product product = null;

    private TextView overTotalAmount;

    private TextView orderName;
    private TextView orderPhone;
    private TextView orderAddress;

    private String strOrderName;
    private String strOrderPhone;
    private String strOrderAddress;
//    private String orderId;







    int total = 0;

    Button btnPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerView_order); //아디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>(); // Product 객체를 담을 어레이리스트(어댑터 쪽으로 날릴 거임)

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        overTotalAmount = (TextView)findViewById(R.id.order_totalPrice);
        orderName = (TextView)findViewById(R.id.order_name);
        orderPhone = (TextView)findViewById(R.id.order_phone);
        orderAddress = (TextView)findViewById(R.id.order_address);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("UserAccount");

        databaseReference = FirebaseDatabase.getInstance().getReference("CurrentUser");
        databaseReferenceProduct = FirebaseDatabase.getInstance().getReference("Product");

        String myOrderId = databaseReference.child("MyOrder").push().getKey();

        databaseReference2.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                // 반복문으로 데이터 List를 추출해냄

                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
                orderName.setText(userAccount.getUsername());
                orderPhone.setText(userAccount.getPhone());
                orderAddress.setText(userAccount.getAddress());
                strOrderName = userAccount.getUsername();
                strOrderPhone = userAccount.getPhone();
                strOrderAddress = userAccount.getAddress();

//                    arrayList.add(userAccount); // 담은 데이터들을 배열 리스트에 넣고 리사이클러뷰로 보낼 준비

//                    userAccount.setDataId(dataId);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("OrderActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });


        databaseReference.child(firebaseUser.getUid()).child("AddToCart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // 반복문으로 데이터 List를 추출해냄
                    String dataId = dataSnapshot.getKey();

                    Cart cart = snapshot.getValue(Cart.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
                    arrayList.add(cart); // 담은 데이터들을 배열 리스트에 넣고 리사이클러뷰로 보낼 준비

                    cart.setDataId(dataId);
                    total += cart.getTotalPrice();
                    Log.d("OrderActivity", total+"");
                    overTotalAmount.setText(String.valueOf(total));

                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("OrderActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new CartAdapter(this, arrayList);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결

        final String[] orderId = {databaseReference.push().getKey()};

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        List<Cart> list = (ArrayList<Cart>) getIntent().getSerializableExtra("itemList");

        Log.d("OrderActivity1", "list : " + list);


//        myOrderList = new ArrayList<>();

        btnPayment = (Button) findViewById(R.id.btnPayment);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list != null && list.size() > 0) {
                    for (Cart model : list) {

                        final HashMap<String, Object> cartMap = new HashMap<>();

                        cartMap.put("productName", model.getProductName());
                        cartMap.put("productPrice", model.getProductPrice());
                        cartMap.put("totalQuantity", model.getTotalQuantity());
                        cartMap.put("totalPrice", model.getTotalPrice());
                        cartMap.put("productId", model.getpId());
                        cartMap.put("overTotalPrice", total);
                        cartMap.put("userName", strOrderName);
                        cartMap.put("phone", strOrderPhone);
                        cartMap.put("address", strOrderAddress);
                        cartMap.put("orderId", myOrderId);
                        cartMap.put("orderDate", getTime());
                        cartMap.put("orderImg", model.getProductImg());
//                        cartMap.put("dataId", dataId+"");
                        Log.d("OrderActivity1", total+"");

                        int totalStock = model.getProductStock() - Integer.valueOf(model.getTotalQuantity());

                        orderId[0] = model.getDataId();
//                        Toast.makeText(OrderActivity.this, orderId[0], Toast.LENGTH_SHORT).show();

                        databaseReference.child(firebaseUser.getUid()).child("MyOrder").child(myOrderId).child(model.getDataId()).setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(OrderActivity.this, "주문완료", Toast.LENGTH_SHORT).show();

                                int b = list.size();
                                while (b > 0){
//                                   Log.d("OrderActivity2", databaseReference.child(firebaseUser.getUid()).child("AddToCart").child(list.get(b-1).getDataId())+"");

                                    int pId = model.getpId();

//                                    int totalStock = 0;
//                                    databaseReferenceProduct.child(model.getDataId()).child("stock");

                                    databaseReferenceProduct.child(String.valueOf(pId)).child("stock").setValue(totalStock).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Toast.makeText(OrderActivity.this, "재고 변동 완료", Toast.LENGTH_SHORT).show();
                                        }
                                    });


//                                    Log.d("OrderActivity2", databaseReference.child(firebaseUser.getUid()).child("MyOrder").child(model.getDataId()).child(productId)+"");

                                    databaseReference.child(firebaseUser.getUid()).child("AddToCart").child(list.get(b-1).getDataId())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    Toast.makeText(OrderActivity.this, "해당 장바구니 데이터 삭제, " + totalStock , Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    b--;

                                }


                            }
                        });

                    }
                }

//                databaseReference.child(firebaseUser.getUid()).child("MyOrder").child(myOrderId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()){
//                                MyOrder myOrder = dataSnapshot.getValue(MyOrder.class);
//                                myOrderList.add(myOrder);
//
//                            }
//                        }
//                    }
//                });

                Intent intent = new Intent(OrderActivity.this, OrderCompleteActivity.class);
                intent.putExtra("orderId", orderId[0]);
                intent.putExtra("myOrderId", myOrderId);
//                intent.putExtra("orderList", (Serializable) myOrderList);
                startActivity(intent);
            }


        });

    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}