package com.example.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        Button creatDatabase=(Button) findViewById(R.id.create_database);
        Button addData=(Button) findViewById(R.id.add_data);
        Button updateData=(Button) findViewById(R.id.update_data);
        Button deleteData=(Button) findViewById(R.id.delete_data);
        Button queryData=(Button) findViewById(R.id.query_data);
        Button replaceData=(Button) findViewById(R.id.replace_data);
        creatDatabase.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		dbHelper.getWritableDatabase();
        	}
        });
        addData.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		SQLiteDatabase db=dbHelper.getWritableDatabase();
        		ContentValues values=new ContentValues();
        		values.put("name", "The Da Vinci Code");
        		values.put("author", "Dan Brown");
        		values.put("pages", 454);
        		values.put("price",16.96);
        		db.insert("Book", null, values);
        		values.clear();
        		values.put("name", "The Lost Symbol");
        		values.put("author", "Dan Brown");
        		values.put("pages", 510);
        		values.put("price",19.95);
        		db.insert("Book", null, values);
        	}
        });
        updateData.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		SQLiteDatabase db=dbHelper.getWritableDatabase();
        		ContentValues values=new ContentValues();
        		values.put("price", 10.99);
        		db.update("Book", values, "name=?", new String[]{"The Da Vinci Code"});
        	}
        });
        deleteData.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		SQLiteDatabase db=dbHelper.getWritableDatabase();
        		db.delete("Book","id>?",new String[]{"2"});
        	}
        });
        queryData.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		SQLiteDatabase db=dbHelper.getWritableDatabase();
        		Cursor cursor=db.query("Book",null,null,null,null,null,null);
        		if(cursor.moveToFirst()){
        			do{
        				String name=cursor.getString(cursor.getColumnIndex("name"));
        				int pages=cursor.getInt(cursor.getColumnIndex("pages"));
        				Log.d("MainActivity",name);
        				Log.d("MainActivity","pages="+pages);
        			}while(cursor.moveToNext());
        		}
        		cursor.close();
        	}
        });
        replaceData.setOnClickListener(new OnClickListener(){
        	public void onClick(View w){
        		SQLiteDatabase db=dbHelper.getWritableDatabase();
        		db.beginTransaction();
        		try{
        			db.delete("Book", null, null);
        			ContentValues values=new ContentValues();
            		values.put("name", "Game of Thrones");
            		values.put("author", "George Martin");
            		values.put("pages", 720);
            		values.put("price",20.85);
            		db.insert("Book", null, values);
        			db.setTransactionSuccessful();
        		}catch(Exception e){
        			e.printStackTrace();
        		}finally{
        			db.endTransaction();
        		}
        	}
        });
    }
}
