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

    private static final String DATABASE_NAME = "spots.db";
    private static final int DATABASE_VERSION = 2;
    private static final String ID="_id";
    private static final String NAME="name";
    private static final String LAT="lat";
    private static final String LNG="lng";
    private static final String LOCATION_TABLE="tpe_intro";


    public LocationsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade(DATABASE_VERSION);
    }

    public ArrayList<Locations> getLocations(){

        SQLiteDatabase db=getReadableDatabase();
        String[] columns={LocationsDatabase.ID,LocationsDatabase.NAME,LocationsDatabase.LAT,LocationsDatabase.LNG};
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
            questionsArrayList.add(questions);
        }
        db.close();
        cursor.close();

        return questionsArrayList;

    }


}

