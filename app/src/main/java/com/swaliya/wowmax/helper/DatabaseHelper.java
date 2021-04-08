package com.swaliya.wowmax.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.swaliya.wowmax.model.DbModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "First_Database";
    public static final String TABLE_NAME = "First_Table";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "(Id Integer Primary key autoincrement, name text,desp text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String s, String s1) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NAME + "(name,desp" + ")" + " VALUES " + "  ('" + s + "','" + s1 + "')");
      /* ContentValues contentValues = new ContentValues();
      contentValues.put("name", s);
      contentValues.put("desp", s1);
      db.insert(CONTACTS_TABLE_NAME, null, contentValues);*/
        return true;
    }
    public ArrayList<DbModel> getAllData() {
        ArrayList<DbModel> doglist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);

        while(res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            String name = res.getString(1);
            String age = res.getString(2);

            DbModel dbModel=new DbModel(id,name,age);
          //  Dog newDog = new Dog(id, name, age, breed, weight);
            doglist.add(dbModel);
        }
        return doglist;
    }
}
