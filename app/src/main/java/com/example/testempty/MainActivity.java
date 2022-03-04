package com.example.testempty;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private Context mContext = this;
    public File mediaStorageDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


    public void buttonRead(View v) {
        String fileName = "test.txt";
        String displayName = "test.txt";
        String text = " EmptyTest 1";
        //Bitmap imageBitmap;
        OutputStream outputStream;

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "test");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain"); //text/plane
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + File.separator + "SMC");
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


            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Fail to create file", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonWrite(View v)
    {
        String fileName = "test.txt";
        String displayName = "test.txt";
        String text = " EmptyTest 1" ;
        //Bitmap imageBitmap;
        OutputStream outputStream ;

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "test");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain"); //text/plane
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + File.separator + "SMC");
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