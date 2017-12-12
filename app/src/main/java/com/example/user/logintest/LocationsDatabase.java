package com.example.user.logintest;

/**
 * Created by chentingyu on 2017/8/4.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;


public class LocationsDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "spotsb.db";
    private static final int DATABASE_VERSION = 2;
    private static final String ID="_id";
    private static final String NAME="name";
    private static final String LAT="lat";
    private static final String LNG="lng";
    private static final String LOCATION_TABLE="tpe_intro";
    private static final String TYPE="type";
    private static final String ENAME="ename";
    private static final String CLASSIFIED="classified";
    private static final String ADDRESS = "address";


    public LocationsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(DATABASE_VERSION);
    }

    public ArrayList<Locations> getsearch(String s){

        SQLiteDatabase db=getReadableDatabase();
        //String classified = LocationsDatabase.CLASSIFIED;
        //欲查詢的欄位
        String[] columns={LocationsDatabase.ID, LocationsDatabase.NAME,LocationsDatabase.LAT,LocationsDatabase.LNG};

        String selection = "name LIKE ?";
        //SelectionArgs 以相同順序的元素職替換?因為?可能代表多個所以為字串陣列
        String[] selectionArgs = {"%"+s+"%"};
        //需要傳遞多個參數，每個參數都代表SQL查詢語法的一部分，null代表略過該部分語法

//        String[] selectionArgs={categoryId+"",subjectId+"",yearId+""};
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LOCATION_TABLE);

        Cursor cursor = qb.query(db, columns,selection,selectionArgs,null,null,null);

        //Cursor cursor = qb.query(db, columns,null,null,null,null,null);
        // /Cursor cursor=db.query(LocationsDatabase.LOCATION_TABLE, columns, null, null, null, null, null);
//        Cursor cursor=db.query(MyDatabase.TABLE_NAME, columns, null,null, null, null, null);
        ArrayList<Locations> questionsArrayList=new ArrayList<>();

        while(cursor.moveToNext()){
            Locations questions=new Locations();
            questions.id=cursor.getInt(cursor.getColumnIndex(LocationsDatabase.ID));
            questions.name=cursor.getString(cursor.getColumnIndex(LocationsDatabase.NAME));
            questions.lat=cursor.getDouble(cursor.getColumnIndex(LocationsDatabase.LAT));
            questions.lng=cursor.getDouble(cursor.getColumnIndex(LocationsDatabase.LNG));
            questionsArrayList.add(questions);
        }
        db.close();
        cursor.close();

        return questionsArrayList;

    }

    public ArrayList<Locations> getLocations(){

        SQLiteDatabase db=getReadableDatabase();
        String[] columns={LocationsDatabase.ID,LocationsDatabase.NAME,LocationsDatabase.LAT,LocationsDatabase.LNG,LocationsDatabase.TYPE,LocationsDatabase.ENAME,LocationsDatabase.CLASSIFIED,LocationsDatabase.ADDRESS};
//        String[] selectionArgs={categoryId+"",subjectId+"",yearId+""};
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LOCATION_TABLE);
        Cursor cursor = qb.query(db, columns,null,null,null,null,null);
        // /Cursor cursor=db.query(LocationsDatabase.LOCATION_TABLE, columns, null, null, null, null, null);
//        Cursor cursor=db.query(MyDatabase.TABLE_NAME, columns, null,null, null, null, null);
        ArrayList<Locations> questionsArrayList=new ArrayList<>();

        while(cursor.moveToNext()){
            Locations questions=new Locations();
            questions.id=cursor.getInt(cursor.getColumnIndex(LocationsDatabase.ID));
            questions.name=cursor.getString(cursor.getColumnIndex(LocationsDatabase.NAME));
            questions.lat=cursor.getDouble(cursor.getColumnIndex(LocationsDatabase.LAT));
            questions.lng=cursor.getDouble(cursor.getColumnIndex(LocationsDatabase.LNG));
            questions.type=cursor.getString(cursor.getColumnIndex(LocationsDatabase.TYPE));
            questions.ename=cursor.getString(cursor.getColumnIndex(LocationsDatabase.ENAME));
            questions.classified=cursor.getString(cursor.getColumnIndex(LocationsDatabase.CLASSIFIED));
            questions.address=cursor.getString(cursor.getColumnIndex(LocationsDatabase.ADDRESS));
            questionsArrayList.add(questions);
        }
        db.close();
        cursor.close();

        return questionsArrayList;

    }


}

