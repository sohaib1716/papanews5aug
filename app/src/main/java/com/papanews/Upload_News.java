package com.papanews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Upload_News extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText t1, t2;
    Button browse, upload;
    ImageView img;
    Bitmap bitmap;
    EditText longphp, locationphp;
    DatePicker picker;
    TextView tvw;
    ProgressBar progressBar;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String username, google_image, image_fb;
    Button browseVideo,uploadLogin;
    ScrollView up_showornot;
    RelativeLayout skipthen_show;

    LinearLayout verify_profile;

    private static final int SELECT_VIDEO = 5;
    private String selectedVideoPath;
    String skipNow;
    private static final String url = "http://papanews.in/PapaNews/userUploads.php";
    String[] users = {"Politics", "Technology", "Sports", "Startup", "Entertaintment",
            "Business", "International", "Influence", "Miscellaneous"};
    String encodeImageString = "", encodeSourceImage = "", Document = "", catSelected, audio = "0";
    int verifieddd;
    MaterialButton verify_you;
    TextView complete_text;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadvideo);
        img =  findViewById(R.id.img);
        upload = findViewById(R.id.upload);
        browse =  findViewById(R.id.browse);
        progressBar = findViewById(R.id.prog);
        uploadLogin = findViewById(R.id.uploadLogin);
        tvw = findViewById(R.id.selectDate);
        picker = findViewById(R.id.datePicker1);
        browseVideo = findViewById(R.id.browusevideo);
        skipthen_show = findViewById(R.id.relaitve_login);
        up_showornot = findViewById(R.id.relative_upload);
        longphp =  findViewById(R.id.longPhp);
        complete_text = findViewById(R.id.completeall);

        locationphp = findViewById(R.id.location);
        t1 =  findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);


        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("user_final", "");
        google_image = sharedPreferences.getString("google_image", "");
        image_fb = sharedPreferences.getString("image_fb", "");

        verify_profile = findViewById(R.id.upload_verify);
        verify_you = findViewById(R.id.verify_your_profile);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        skipNow = sharedPreferences.getString("skip_now", null);
        verifieddd = sharedPreferences.getInt("verified", 0);

        if(skipNow.equals("skip")){
            uploadLogin.setVisibility(View.VISIBLE);
            up_showornot.setVisibility(View.GONE);
            if(verifieddd==1){
                verify_you.setVisibility(View.GONE);
            }else {
                verify_you.setVisibility(View.VISIBLE);
            }
        }else{
            uploadLogin.setVisibility(View.GONE);
            if(verifieddd==1){
                complete_text.setVisibility(View.GONE);
                up_showornot.setVisibility(View.VISIBLE);
                verify_you.setVisibility(View.GONE);
            }else {
                up_showornot.setVisibility(View.GONE);
                verify_you.setVisibility(View.VISIBLE);
            }
        }

        verify_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Upload_News.this, SignUp.class);
                global.verifyOrnot = 2;
                startActivity(i);
            }
        });


        uploadLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("key", 0);
                editor.apply();
                Intent i = new Intent(Upload_News.this, Login.class);
                startActivity(i);
                finishAffinity();
            }
        });



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        browseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_VIDEO);
            }
        });


        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Upload_News.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.setVisibility(View.VISIBLE);
                picker.bringToFront();
                tvw.setText(picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());
                uploaddatatodb();
            }
        });


    }

    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
                img.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                selectedVideoPath = getPath(data.getData());
                try {
                    if (selectedVideoPath == null) {
                        Log.e("videoPath", "selected video path = null!");
                        finish();
                    } else {
                        img.setVisibility(View.GONE);
                        Log.e("videoPath", selectedVideoPath);
                    }
                } catch (Exception ex) {
                    //#debug
                    ex.printStackTrace();
                    Log.e("error video :", String.valueOf(ex));
                }
            }

        }

        if (!image_fb.equals("")) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(image_fb));
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeSource(bitmap);
            } catch (Exception ex) {
            }
        }
        if (!google_image.equals("")) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(image_fb));
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeSource(bitmap);
            } catch (Exception ex) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        ConvertToString(uri);
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    public void ConvertToString(Uri uri) {
        String uriString = uri.toString();
        Log.d("data uri", "onActivityResult: uri" + uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            Log.d("data length", "onActivityResult: bytes size=" + bytes.length);
            Log.d("data encode", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            Log.d("data ansValue", ansValue);
            Document = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


    private void encodeSource(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeSourceImage = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


    private void uploaddatatodb() {
        progressBar.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_LONG).show();
        final String title = t1.getText().toString().trim();
        final String shdesc = t2.getText().toString().trim();
        final String longDescription = longphp.getText().toString().trim();
        final String location = locationphp.getText().toString().trim();
        final String date = tvw.getText().toString().trim();

        if(!title.equals("") || !shdesc.equals("") || !longDescription.equals("") || !location.equals("")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("File Uploaded Successfully")){
                                progressBar.setVisibility(View.GONE);
                                t1.setText("");
                                t2.setText("");
                                longphp.setText("");
                                locationphp.setText("");
                                img.setImageResource(R.drawable.ic_launcher_foreground);
                            }
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("t1", title);
                            map.put("t2", shdesc);
                            map.put("long", longDescription);
                            map.put("sorname", username);
                            map.put("upload", encodeImageString);
                            map.put("location", location);
                            map.put("date", date);
                            map.put("sorimage", encodeSourceImage);
                            map.put("videou", Document);
                            return map;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                }
            }, 2000);

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: " + users[position], Toast.LENGTH_SHORT).show();
//        String[] users = { "Politics", "Technology", "Sports", "Startup", "Entertaintment", "Business" };
        if (users[position].equals("Politics")) {
            catSelected = "politics";
        }
        if (users[position].equals("Technology")) {
            catSelected = "Tech";
        }
        if (users[position].equals("Sports")) {
            catSelected = "sports";
        }
        if (users[position].equals("Startup")) {
            catSelected = "sports";
        }
        if (users[position].equals("Entertaintment")) {
            catSelected = "entertain";
        }
        if (users[position].equals("Business")) {
            catSelected = "business";
        }
        if (users[position].equals("International")) {
            catSelected = "international";
        }
        if (users[position].equals("Influence")) {
            catSelected = "influence";
        }
        if (users[position].equals("Miscellaneous")) {
            catSelected = "miscell";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}