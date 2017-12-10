package com.example.user.logintest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Created by chentingyu on 2017/8/1.
 * edited by itingchen on 2017/09/26 to create add to my favorite function
 */

public class SQLiteHelper extends SQLiteOpenHelper{
    private static SQLiteHelper instance = null;

    //public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    //   super(context, name, factory, version);}

    // CREATE  TABLE "main"."attr" ("_id" INTEGER PRIMARY KEY  NOT NULL , "cdate" DATETIME NOT NULL , "info" VARCHAR, "amount" INTEGER) 

    @Override
    public void onCreate (SQLiteDatabase db){

        db.execSQL("CREATE  TABLE main.attr " +
                "(_id INTEGER PRIMARY KEY NOT NULL,"+
                "name VARCHAR NOT NULL)"
        );
    }

    public static SQLiteHelper getInstance(Context ctx){
        if (instance==null){
            instance = new SQLiteHelper(ctx, "attr.db", null, 1);
        }
        return instance;
    }
    private SQLiteHelper(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + "main.attr";
        //Cursor res = db.rawQuery(query,null);
        Cursor res = db.query(true, "main.attr", new String[] { "_id","name"}, null, null,"name", null, null, null);
        return res;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}

