package com.example.com.sqlforlowlevel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateResturant extends AppCompatActivity {
    public  MyDatabase dbhelper;
    EditText re_name;
    EditText re_adress;
    Button button ;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .toolbar1,menu  ) ;
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_resturant);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar3) ;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar .setDisplayHomeAsUpEnabled(true ) ;//返回键出现
        }
        re_name =(EditText)  findViewById(R.id.re_name1) ;
        re_adress=(EditText)  findViewById(R.id.re_adress1 ) ;
        button =(Button)  findViewById(R.id.update1) ;
        dbhelper=new MyDatabase(this,"MyMeiTuan.db",null,8) ;
        SQLiteDatabase db=dbhelper .getWritableDatabase();
        Intent intent=getIntent() ;
        String name=intent .getStringExtra("name") ;
        String adress=intent .getStringExtra("adress") ;
       final String id=intent .getStringExtra("id") ;
        re_name .setText(name) ;
        re_adress .setText(adress ) ;
        ContentValues values = new ContentValues();
        button .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=re_name.getText().toString();
                String adress=re_adress .getText() .toString() ;
                SQLiteDatabase db=dbhelper .getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name",name) ;
                values .put("adress",adress ) ;
                db.update("Resturant",values ,"id=?",new String[]{id}) ;
                finish() ;
            }
        }) ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

