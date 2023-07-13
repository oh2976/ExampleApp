package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
//
//    ArrayList<String> arrayList = new ArrayList<>();
//    ArrayAdapter<String> adapter;
//
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
//
//    ListView listView;
//
//    EditText nameEdit;
//    Button addBtn;
//
//    String sOldValue;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//
//        // 컴포넌트 변수에 담기
//
//        nameEdit = findViewById(R.id.name_edit02);
//        addBtn = findViewById(R.id.add_btn02);
//        listView = findViewById(R.id.list_view);
//
//        //어뎁터 초기화
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
//
//        //데이터 베이스 초기와
//        firebaseDatabase = FirebaseDatabase.getInstance();
//
//        //러퍼런스 초기화
//        databaseReference = firebaseDatabase.getReference().child("Data");
//
//        //데이터 조회
//        getValue();
//
//         //데이터 등록
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //입력값 변수에 담기
//                String sName = nameEdit.getText().toString();
//
//                if (addBtn.getText().toString().equals("쓰기")) {
//                    //키 생성
//                    String sKey = databaseReference.push().getKey();
//                    //sKey가 null이 아니면 sKey 값으로 데이터를 저장한다.
//                    if (sKey != null) {
//                        databaseReference.child(sKey).child("value").setValue(sName);
//
//                        //입력창 초기화
//                        nameEdit.setText("");
//                    }
//                } else {
//                        //수정
//                        //orderByChild : value 값으로 정렬
//                        // equalTo : 해당값 반환
//
//                        Query query = databaseReference.orderByChild("value").equalTo(sOldValue);
//
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) { //수정된 입력창의 값으로 데이터를 수정한다.
//                                    dataSnapshot.getRef().child("value").setValue(sName);
//
//                                    //입력창 초기와
//                                    nameEdit.setText("");
//
//                                    //버튼 값 쓰기로 변경
//                                    addBtn.setText("쓰기");
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//
//            }//onClick
//        }); //setOnClickListener
//
//        //리스트뷰 아이템 클릭 이벤트
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                //변수에 현재값을 담는다.
//                sOldValue = arrayList.get(position);
//
//                //입력창에 선택 값을 보여준다.
//                nameEdit.setText(sOldValue);
//
//                //버튼 명을 수정으로 바꾼다.
//                addBtn.setText("수정");
//
//            }
//        });
//
//        //리스트뷰 아이템 길게 클릭 이벤트
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//                String sValue = arrayList.get(position);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
//                builder.setTitle("삭제");
//                builder.setMessage("삭제하시겠습니까?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //쿼리 초기화
//                        Query query = databaseReference.orderByChild("value").equalTo(sValue);
//
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    //데이터 삭제
//                                    dataSnapshot.getRef().removeValue();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Toast.makeText(MainActivity2.this, "error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//
//
//
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//                        //닫기
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                //보여주기
//                builder.show();
//
//                return true;
//            }
//        });
//
//    }//onCreate
//
//    //파이어베이스에서 데이터 가져오기
//    private void getValue(){
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // 리스트 초기화
//                arrayList.clear();
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    //데이터 가져오기
//                    try {
//                        String sValue = dataSnapshot.child("value").getValue().toString();
//                        //리스트에 변수를 담는다.
//                        arrayList.add(sValue);
//                    } catch (NullPointerException e){
//                        Log.d("BasicSyntax", "값이 없음. method= Log.d");
//                    }
//
//
//
//
//
//                }
//                //리스트뷰 어뎁터 설정
//                listView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                Toast.makeText(MainActivity2.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }//getValue
}