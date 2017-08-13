package com.example.user.logintest;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.database.Cursor ;

/**
 * Created by chentingyu on 2017/8/12.
 */

public class Downloads {
    static Context mContext;
    static DownloadManager downloadManager;

    public Downloads(Context mContext) {
        this.mContext = mContext;
    }

    public static String DownloadImg(String imguri) {
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        String uriString = "";
        //Uri uri = Uri.parse("http://140.112.107.125:47155/html/uploaded/sample.mp4");
        Uri uri = Uri.parse(imguri);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        long downloadImgId = downloadManager.enqueue(request);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadImgId);
        Cursor c = downloadManager.query(query);

        if (c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            /*if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                //uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                //uriString = DownloadManager.COLUMN_LOCAL_URI;
            }*/
        }
        if(downloadImgId == c.getInt(0)){
            uriString = c.getString(c.getColumnIndex("local_uri"));
        }
        //uriString = downloadManager.COLUMN_LOCAL_URI;
        //c.close();
        return uriString;

    }
}
