package com.example.user.logintest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.DialogInterface;
import android.widget.Toast;
import android.util.Log;
import android.os.Handler;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {

    private Button uploadbutton;
    private Spinner type;
    private EditText intro ;
    private String filename;
    private String lat ;
    private String lng ;
    private String name ;
    private String account ;
    private EditText audioname ;
    private TextView attrName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadbutton = (Button)findViewById(R.id.btn_file) ;
        intro = (EditText)findViewById(R.id.et_intro);
        audioname = (EditText)findViewById(R.id.et_audioName);
        attrName = (TextView)findViewById(R.id.tv_attrName);
        account = XclSingleton.getInstance().get("AccountID").toString();
        type = (Spinner)findViewById(R.id.spinner_type) ;

        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(
                this, R.array.audio_types, android.R.layout.simple_spinner_item );
        nAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(nAdapter);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        Bundle bundleAttr = getIntent().getExtras();
        lat = bundleAttr.getString("lat") ;
        lng = bundleAttr.getString("lng") ;
        name = bundleAttr.getString("name");
        attrName.setText(name);

        audioname.setHint("必填");
        intro.setHint("必填");

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput(intro) & checkInput(audioname) ) {
                    new MaterialFilePicker()
                            .withActivity(UploadActivity.this)
                            .withRequestCode(10)
                            .start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "請填入所有資訊",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public boolean checkInput(EditText et){
        if(TextUtils.isEmpty(et.getText().toString())){
            return false;
        }
        else {
            return true ;
        }
    }


    private void enable_button() {

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new MaterialFilePicker()
                            .withActivity(UploadActivity.this)
                            .withRequestCode(10)
                            .start();
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

    ProgressDialog progress;
    Thread uploadThread ;
    Thread dbThread ;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK) {


            File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            String file_path = f.getAbsolutePath();
            filename = file_path.substring(file_path.lastIndexOf("/") + 1);
            //new MyAsyncTask().execute(filename, intro.getText().toString(), type.getText().toString());
            String filetype = filename.substring(filename.lastIndexOf('.'), filename.length()).trim();

            progress = new ProgressDialog(UploadActivity.this);
            progress.setTitle("上傳中");
            progress.setMessage(filename);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
            progress.setProgress(100);


            if (!filetype.equals(".m4a") && !filetype.equals(".mp3")) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(UploadActivity.this);
                builder.setMessage("您選擇的檔案格式不符")
                        .setTitle("請重新選擇檔案");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // Intent backintent = new Intent();
                        //backintent.setClass(UploadActivity.this, UploadActivity.class);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(UploadActivity.this);
                builder.setMessage(filename)
                        .setTitle("已選擇檔案");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        uploadThread.start();
                        progress.show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


                uploadThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                        String content_type = getMimeType(f.getPath());

                        String file_path = f.getAbsolutePath();
                        OkHttpClient client_file = new OkHttpClient();
                        //RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);
                        RequestBody file_body = RequestBody.create(MediaType.parse("audio/*; charset=big5"), f);

                        try {
                            RequestBody request_body = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("type", "audio/*; charset=big5")
                                    .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                                    .build();

                            Request request = new Request.Builder()
                                    .url("http://140.112.107.125:47155/html/upload.php")
                                    .post(request_body)
                                    .build();


                            Response response = client_file.newCall(request).execute();
                            Log.e("response", response.toString());

                            if (!response.isSuccessful()) {
                                Log.e("msg", "fail ");
                                Looper.prepare();
                                AlertDialog.Builder builder_fail;
                                builder_fail = new AlertDialog.Builder(UploadActivity.this);
                                builder_fail.setMessage("請重新上傳")
                                        .setTitle("上傳失敗");
                                final AlertDialog dialog_fail = builder_fail.create();
                                dialog_fail.show();
                                Looper.loop();

                            } else {
                                dbThread.start();
                                progress.dismiss();
                                Log.e("msg", "success ");
                                Looper.prepare();
                                AlertDialog.Builder builder_success;
                                builder_success = new AlertDialog.Builder(UploadActivity.this);
                                builder_success.setMessage("請到上傳音檔頁查看")
                                        .setTitle("上傳成功");
                                final AlertDialog dialog_success = builder_success.create();
                                dialog_success.show();
                                Looper.loop();
                            }

                            //progress.dismiss();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        }
                    });


                    dbThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient();
                            try {
                                Log.e("sending", "to server");
                                RequestBody formBody = new FormBody.Builder()
                                        .add("lat", lat)
                                        .add("lng", lng)
                                        .add("filename", filename)
                                        .add("audioname", audioname.getText().toString())
                                        .add("intro", intro.getText().toString())
                                        .add("type", type.getSelectedItem().toString())
                                        .add("account", account).build() ;

                                Request request = new Request.Builder().url("http://140.112.107.125:47155/html/uploadAudio.php")
                                        .post(formBody)
                                        .build();

                                Response response2 = client.newCall(request).execute();
                                //final String resStr = response.body().string();
                                if (!response2.isSuccessful()) {
                                    throw new IOException("Error : " + response2);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });


            }
        }
    }


    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.layout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        Global check = (Global)getApplicationContext();
        int checkk = check.getWord();
        MenuItem registrar = menu.findItem(R.id.我上傳的音檔);
        registrar.setVisible(checkk==0); //if is  guide then is visible
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //取的intent中的bundle物件
        Bundle bundleID =this.getIntent().getExtras();

        String UserID = bundleID.getString("AccountID");
        switch (item.getItemId())
        {
            case R.id.我的最愛:
                //放入點擊後的結果
                Intent intent = new Intent();
                intent.setClass(UploadActivity.this,FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.我上傳的音檔:
                //跳到上傳音檔頁，顯示上傳的音檔名字
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setClass(UploadActivity.this,MyuploadMusic.class);
                //儲存帳號
                Bundle bundle = new Bundle();
                bundle.putString("AccountID",UserID);
                //將Bundle物件assign給intent
                intent2.putExtras(bundle);
                startActivity(intent2);
                //顯示按鈕的名字
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.我的帳戶:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent6 = new Intent();
                intent6.setClass(UploadActivity.this,MyAccountActivity.class);
                startActivity(intent6);
                break;
            case R.id.條列式瀏覽:
                Toast.makeText(getApplicationContext(), item.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent();
                intent5.setClass(UploadActivity.this,SpotView.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
