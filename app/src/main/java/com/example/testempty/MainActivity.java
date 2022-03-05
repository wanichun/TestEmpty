package com.example.testempty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainAt";


    private Context mContext = this;
    public File mediaStorageDir;
    String filePathInfo_old;
    String filePathInfo;
    String filePathSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //filePathSave = Environment.DIRECTORY_DOCUMENTS + File.separator + "Test";
        filePathSave = Environment.DIRECTORY_PICTURES;// + File.separator + "Test";
        galleryAddFolder(new File(filePathSave));

        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), filePathSave);
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return;
            }
        }
        Log.d(TAG,"mediaStorageDir;" + mediaStorageDir.getPath() + " " + mediaStorageDir.exists());

        filePathInfo_old = mediaStorageDir.getPath() + File.separator + "tmp.txt";
        Log.d(TAG,"filePathInfo_old;" +filePathInfo_old + " " + (new File(filePathInfo_old).exists()));

        File newDir = new File(mContext.getExternalFilesDir(null).getPath());
        if(!newDir.exists()){
            newDir.mkdir();
        }
        Log.d(TAG,"newDir;"  +  newDir.getPath() + " "+ newDir.exists());

        filePathInfo = newDir.getPath() + File.separator + "tmp.txt";
        Log.d(TAG,"filePathInfo;" + filePathInfo + " " +(new File(filePathInfo).exists()));

    }

    protected void OnDestroy() {
        super.onDestroy();
    }
    /*
    private Bitmap bmp;

    private InputStream getImageInputStram() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapData);

        return bs;
    }

    public byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

     */

    private void galleryAddFolder(File file){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        mediaScanIntent.setData(uri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    private boolean copyFileToInternalStorage(String oldPath, String newPath)
    {
        try{
/*
            InputStream inputStream = mContext.getContentResolver().openInputStream(Uri.fromFile(new File(oldPath)));
            File backupDB = new File(newPath);
            FileOutputStream outputStream = new FileOutputStream(backupDB);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != - 1){
                outputStream.write(buffers, 0, read);
            }
            inputStream.close();
            outputStream.close();

 */

            File currentDB = new File(oldPath);
            File backupDB = new File(newPath);
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } catch(Exception e){
            Log.d(TAG,"copy ; "  + e);
            return false;
        }
        return true;
    }

    private void readFile(Uri uri){//String strPath){


        BufferedReader br = null;
        FileReader fr = null;
        Log.d(TAG,"read ; "  + uri.getPath());
        try{
            //File file = new File(strPath);
            //Uri uri = Uri.fromFile(file);
            ContentResolver resolver = mContext.getApplicationContext().getContentResolver();
            ParcelFileDescriptor pdf = resolver.openFileDescriptor(uri,"r");

            fr = new FileReader(pdf.getFileDescriptor());
            br = new BufferedReader(fr);

            String str = br.readLine();
            Log.d(TAG,"read ; "  + str );

            br.close();
            fr.close();
            Toast.makeText(this, "File read successfully", Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){
            e.printStackTrace();
            Log.d(TAG,"read ; "  + e);
            Toast.makeText(this, "Fail to read", Toast.LENGTH_SHORT).show();
        }

        /**/

    }
    public static final int PICKFILE_RESULT_CODE = 1;

    public void buttonRead(View v) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
        /*
        boolean bCopy = copyFileToInternalStorage(filePathInfo_old, filePathInfo);
        Log.d(TAG,"copyFileToInternalStorage: " + bCopy);

        if(!new File(filePathInfo).exists()){
            Log.d(TAG,"filePathInfo no file");
        }

        readFile(filePathInfo_old);
        readFile(filePathInfo);
         */

    }

    private Uri fileUri;
    private String filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    readFile(fileUri);
                }
                break;
        }
    }

    public void buttonWrite(View v)
    {
        String displayName = "tmp.txt";
        OutputStream outputStream ;

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain"); //text/plane
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, filePathSave);
            values.put(MediaStore.Files.FileColumns.IS_PENDING, 1);

            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), values);      //important!

            outputStream = getContentResolver().openOutputStream(uri);

            String str = "heloo";
            byte[] strToByte = str.getBytes();
            outputStream.write(strToByte);

            outputStream.flush();
            outputStream.close();
            values.put(MediaStore.Files.FileColumns.IS_PENDING, 0);
            contentResolver.update(uri, values, null, null);

            Log.d(TAG,"write: " + filePathSave + ", " + filePathSave);

            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Fail to create file", Toast.LENGTH_SHORT).show();
        }

        // append 추가 필요
        /*

        ContentValues values = new ContentValues();
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "test.txt");
        values.put(MediaStore.Files.FileColumns.MIME_TYPE,"text/"); //text/plane
        values.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES + File.separator+"SMC");


        values.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
        ContentResolver contentResolver = getContentResolver();
        //Uri item = Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), String.valueOf(0));

        //Uri item = contentResolver.insert(MediaStore.Files.Media.EXTERNAL_CONTENT_URI,values);
        //Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        //Uri item = contentResolver.insert(collection, values);
        Uri item = MediaStore.Files.getContentUri("external");
        try {
            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);

            if (pdf == null) {
                Log.d(TAG, "null");
            } else {
                String str = "heloo";
                byte[] strToByte = str.getBytes();
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                fos.write(strToByte);
                fos.close();

                values.clear();
                values.put(MediaStore.Files.FileColumns.IS_PENDING, 0);
                contentResolver.update(item, values, null, null);

            }
        } catch (Exception e) {
            Toast.makeText(this, "Image Not Not  Saved: \n "+e, Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

         */
        /*

        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = { MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String[] selectionArgs = new String[] {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")
        };

        try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            // 하나의 파일만 읽고 싶을때는 아래 코드에서 처리
            if (cursor != null) {
                cursor.moveToFirst();
                int idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                long id = cursor.getLong(idColumn);
                Uri contentUri = Uri.withAppendedPath( MediaStore.Files.getContentUri("external"), String.valueOf(id));
                System.out.println("Content Uri : " + contentUri);
                System.out.println("Content Uri toString :" + contentUri.toString());
                System.out.println("Content Uri getPath :" + contentUri.getPath());
                String realUri = uri + File.separator + cursor.getString(0);
                StringBuilder stringBuffer = new StringBuilder();

                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(realUri));
                if(inputStream == null){ return; }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    stringBuffer.append(line).append("\n");
                }
                System.out.println("Text read :: " + stringBuffer.toString());
            } else {
                System.out.println("Cursor is null");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

         */



        /*


        ContentValues values = new ContentValues();
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "test.txt");
        values.put(MediaStore.Files.FileColumns.MIME_TYPE,"text/"); //text/plane
        values.put(MediaStore.Images.Media.IS_PENDING, 1);

        ContentResolver contentResolver = getContentResolver();
        Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri item = contentResolver.insert(collection, values);

        try {
            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);
            if (pdf == null) {
                Log.d(TAG, "null");

            } else {
                //InputStream inputStream = getImageInputStram();
                //byte[] strToByte = getBytes(inputStream);
                String str = "heloo";
                byte[] strToByte = str.getBytes();
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                fos.write(strToByte);
                fos.close();
                //inputStream.close();
                pdf.close();

                values.clear();
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                contentResolver.update(item, values, null, null);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Image Not Not  Saved: \n "+e, Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        values.clear();
        // 파일을 모두 write하고 다른곳에서 사용할 수 있도록 0으로 업데이트를 해줍니다.
        values.put(MediaStore.Images.Media.IS_PENDING, 0);
        contentResolver.update(item, values, null, null);
         */
/*

        ContentResolver resolver = this.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_"+".txt");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg"); //text/plane
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES + File.separator+"SMC");


        Log.d(TAG, "buttonWrite ");
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        try {
            outputStream =  resolver.openOutputStream(Objects.requireNonNull(imageUri) );
            //imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            //Objects.requireNonNull(outputStream);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(text);
            writer.flush();
            writer.close();
            outputStream.close();

            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Image Not Not  Saved: \n "+e, Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

 */


        /*
        ContentResolver cr = mContext.getContentResolver();
        ContentValues values;

        FileWriter fw = null;
        BufferedWriter bufwr = null;

        mediaStorageDir = new File(Environment.DIRECTORY_DOWNLOADS);

        Log.d(TAG, "buttonWrite1 " + mediaStorageDir + " " + text + " " + getExternalMediaDirs());


        try{
            File file = new File(mediaStorageDir.getPath() + File.separator + displayName);

            Log.d(TAG, "buttonWrite2 " + file + " " + text);

            fw = new FileWriter(file, true);
            bufwr = new BufferedWriter(fw);

            bufwr.newLine();;
            bufwr.write(text);
            bufwr.flush();



        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(bufwr !=null)
                bufwr.close();
            if(fw != null)
                fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        Log.d(TAG, "buttonWrite ");


         */
            /*
            values = new ContentValues();

            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            values.put(MediaStore.MediaColumns.IS_PENDING, true);
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);

            Uri target;
            target = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

            Uri uri = cr.insert(target, values);
            if(uri == null)
                return;

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(text);
            writer.flush();
            writer.close();
            os.close();

             */
    }




}