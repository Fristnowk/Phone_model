package com.example.phonemodel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    MyHelper myHelper;
    private EditText etName;
    private EditText etPhone;
    private TextView tvShow;
    private Button btnAdd;
    private Button btnQuery;
    private Button btnUpdate;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHelper = new MyHelper(this);
        init(); //初始化控件
    }
    private void init(){
        etName = (EditText)findViewById(R.id.et_name);
        etPhone = (EditText)findViewById(R.id.et_phone);
        tvShow = (TextView)findViewById(R.id.tv_show);
        btnAdd = (Button)findViewById(R.id.btn_add);
        btnQuery = (Button)findViewById(R.id.btn_query);
        btnUpdate = (Button)findViewById(R.id.btn_update);
        btnDelete = (Button)findViewById(R.id.btn_delete);
        btnAdd.setOnClickListener(this);   //Button控件设置监听
        btnQuery.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        tvShow.setMovementMethod(ScrollingMovementMethod.getInstance()); //设置文本滚动
    }
    @Override
    public void onClick(View v){
        String name;
        String phone;
        SQLiteDatabase db;
        switch (v.getId()){
            case R.id.btn_add:  //添加联系人
                name = etName.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                db = myHelper.getWritableDatabase();
                if (name.equals("") || phone.equals("")){ //联系人信息不能为空
                    Toast.makeText(this,"联系人信息添加失败",Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("insert into person (name,phone) values(?,?)", new Object[]{name, phone});
                    Toast.makeText(this,"联系人信息添加成功",Toast.LENGTH_SHORT).show();
                }
                db.close();
                break;
            case R.id.btn_query: //查询联系人
                db = myHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select name,phone from person",null);
                if (cursor.getCount() == 0){
                    tvShow.setText("");
                    Toast.makeText(this,"空目录",Toast.LENGTH_SHORT).show();
                }else {
                    cursor.moveToFirst();
                    tvShow.setText("Name：" + cursor.getString(0) + " ; Tel：" + cursor.getString(1));
                    while (cursor.moveToNext()){
                        tvShow.append("\n" + "Name：" + cursor.getString(0) + " ; Tel：" + cursor.getString(1));
                    }
                }
                cursor.close();
                db.close();
                break;
            case R.id.btn_update: //修改联系人
                db = myHelper.getWritableDatabase();
                name = etName.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                if (name.equals("") || phone.equals("")){ //联系人信息不能为空
                    Toast.makeText(this,"联系人信息修改失败",Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("update person set name=?,phone=? where name=?", new Object[]{name, phone, name});
                    Toast.makeText(this,"联系人信息修改成功",Toast.LENGTH_SHORT).show();
                }
                db.close();
                break;
            case R.id.btn_delete: //删除联系人
                db = myHelper.getWritableDatabase();
                name = etName.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                if (name.equals("") || phone.equals("")){ //联系人信息不能为空
                    Toast.makeText(this,"联系人信息删除失败",Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("delete from person where name=? and phone=?", new Object[]{name, phone});
                    Toast.makeText(this,"联系人信息删除成功",Toast.LENGTH_SHORT).show();
                }
                db.close();
                break;
        }
    }
}

//MyHelper类 (数据库文件)
class MyHelper extends SQLiteOpenHelper {


    public MyHelper(Context context){
        super(context, "alan.db", null ,2);
    }
    @Override

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table person(id integer primary key autoincrement,name varchar(20),phone varchar(20) unique)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}