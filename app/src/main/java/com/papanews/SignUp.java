package com.papanews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.text.InputType;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignUp extends AppCompatActivity {
    String fullName = "", userName = "", password = "", email = "", location = "", number = "", profession = "";
    String URL = "http://papanews.in/PapaNews/insertImage.php";
    final int CODE_GALLERY_REQUEST = 999;
    String usergender = "male", dateofbirth = "";
    String pushusername;
    String pushemail, pushImage;
    int nextint = 1;
    int otp;
    Bitmap bitmap;
    private LocationManager locationManager;
    private int REQUEST_LOCATION = 1;

    RelativeLayout cs;
    TextInputEditText textInputEditTextFullName, textInputEditTextUsername, TextInputEditTextPassowrd, TextInputEditTextEmail;
    TextInputEditText textInputNumber, textInputLocation, textInputProffesion, textOtp;
    MaterialButton buttonSignUp,select, upload_php,btnGet;
    MaterialTextView textViewLogin,gendertext,tvw,dab,tepro,notop;
    ProgressBar progressBar;
    MaterialRadioButton male, female, other;
    ImageView phpImage;
    DatePicker datePicker;
    DatePickerDialog picker;
    EditText eText;
    TextInputLayout patext,usernamevisi, mobilenumbervisi, textinputOTP;

    RadioGroup genderselectvisi;
    Spinner locationvisi, proffesionvisi;


//    @BindView(R.id.fullname) TextInputEditText textInputEditTextFullName;
//    @BindView(R.id.username) TextInputEditText textInputEditTextUsername;
//    @BindView(R.id.password) TextInputEditText TextInputEditTextPassowrd;
//    @BindView(R.id.email) TextInputEditText TextInputEditTextEmail;
//    @BindView(R.id.number) TextInputEditText textInputNumber;
//    @BindView(R.id.locationin) TextInputEditText textInputLocation;
//    @BindView(R.id.profession) TextInputEditText textInputProffesion;
//    @BindView(R.id.getotp) TextInputEditText textOtp;
//
//    @BindView(R.id.textinputProfession) Spinner proffesionvisi;
//    @BindView(R.id.textinputLocation) Spinner locationvisi;
//
//    @BindView(R.id.buttonSignUp) MaterialButton buttonSignUp;
//    @BindView(R.id.choose) MaterialButton select;
//    @BindView(R.id.upload) MaterialButton upload_php;
//    @BindView(R.id.button1) MaterialButton btnGet;
//
//    @BindView(R.id.loginText) MaterialTextView textViewLogin;
//    @BindView(R.id.gendertext) MaterialTextView gendertext;
//    @BindView(R.id.textView1) MaterialTextView tvw;
//    @BindView(R.id.dab) MaterialTextView dab;
//    @BindView(R.id.professionText) MaterialTextView tepro;
//
//    @BindView(R.id.progress) ProgressBar progressBar;
//    @BindView(R.id.male) MaterialRadioButton male;
//    @BindView(R.id.female) MaterialRadioButton female;
//    @BindView(R.id.other) MaterialRadioButton other;
//
//    @BindView(R.id.profileImagephp) ImageView phpImage;
//    @BindView(R.id.date_picker) DatePicker datePicker;
//    @BindView(R.id.textinputnumber) DatePickerDialog picker;
//    @BindView(R.id.editText1) EditText eText;
//
//    @BindView(R.id.textInputLayoutPassword) TextInputLayout patext;
//    @BindView(R.id.textInputLayoutUsername) TextInputLayout usernamevisi;
//    @BindView(R.id.textinputnumber) TextInputLayout mobilenumbervisi;
//    @BindView(R.id.textinputOtp) TextInputLayout textinputOTP;
//
//    @BindView(R.id.signmain) RelativeLayout cs;
//    @BindView(R.id.genderselect) RadioGroup genderselectvisi;

    String final_photo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String final_email;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernamevisi = findViewById(R.id.textInputLayoutUsername);
        mobilenumbervisi = findViewById(R.id.textinputnumber);
        textInputEditTextFullName = findViewById(R.id.fullname);
        textInputEditTextUsername = findViewById(R.id.username);
        TextInputEditTextPassowrd = findViewById(R.id.password);
        TextInputEditTextEmail = findViewById(R.id.email);
        textInputLocation = findViewById(R.id.locationin);
        textInputNumber = findViewById(R.id.number);
        textInputProffesion = findViewById(R.id.profession);
        locationvisi = findViewById(R.id.textinputLocation);
        proffesionvisi = findViewById(R.id.textinputProfession);
        genderselectvisi = findViewById(R.id.genderselect);
        gendertext = findViewById(R.id.gendertext);
        tepro = findViewById(R.id.professionText);
        dab = findViewById(R.id.dab);
        textinputOTP = findViewById(R.id.textinputOtp);
        textOtp = findViewById(R.id.getotp);
        phpImage = findViewById(R.id.profileImagephp);
        select = findViewById(R.id.choose);
        upload_php = findViewById(R.id.upload);
        cs = findViewById(R.id.signmain);
        patext = findViewById(R.id.textInputLayoutPassword);
        datePicker = (findViewById(R.id.date_picker_select));
        tvw = findViewById(R.id.textView1);
        eText = findViewById(R.id.editText1);
        notop = findViewById(R.id.noOtp);

        btnGet = findViewById(R.id.button1);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        usernamevisi.setVisibility(View.GONE);



        //verify
//        btnVerify = findViewById(R.id.buttonVerify);
//        textinputverifyOtp = findViewById(R.id.textInputOtp);
//        enterOtp = findViewById(R.id.enterOtp);


        ButterKnife.bind(this);

        final ConnectionDectector cd = new ConnectionDectector(this);
        if (!(cd.isConnected())) {
//            Toast.makeText(SignUp.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


        getTheUserPermission();



        sharedPreferences = this.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final_email = sharedPreferences.getString("final_email", "");

        final_photo = sharedPreferences.getString("photogoogle", "");
        Log.d("email_fb_sign", final_email);


        if (global.logincheck == 2) {
            try {
                final String fb_full = sharedPreferences.getString("facebook_fullname", "");
                final String fb_user = sharedPreferences.getString("facebook_username", "");
                final String fb_email = sharedPreferences.getString("facebook_email", "");
                final String fb_image = sharedPreferences.getString("facebook_image", "");

                Log.d("image_signup fb", fb_image);
                pushemail = fb_email;
                pushusername = fb_full;
                pushImage = fb_image;
            } catch (Exception e) {
                Log.e("ThrowException ", String.valueOf(e));
            }

        }


        if (global.logincheck == 1) {
            try {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignUp.this);
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String imagegoogle = String.valueOf(acct.getPhotoUrl());

                Log.d("image_signup goo", imagegoogle);
                pushemail = personEmail;
                pushusername = personName;
                pushImage = imagegoogle;
            } catch (Exception e) {
                Log.e("ThrowException ", String.valueOf(e));
            }

        }


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -12);
        datePicker.setMaxDate(calendar.getTimeInMillis());
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String currentDate = (monthOfYear+1) + "/" + dayOfMonth + "/" + year;
                dateofbirth = currentDate;
            }
        });



        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR) - 12;
                // date picker dialog


                picker = new DatePickerDialog(SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                dateofbirth = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            }
                        }, year, month, day);

                picker.getDatePicker().setSpinnersShown(true);
                picker.getDatePicker().setCalendarViewShown(false);
                picker.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 3.784e+11));
                picker.show();

            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: " + eText.getText());
            }
        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        SignUp.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST

                );
            }
        });

        upload_php.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Error : " + error, Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        String imageData = imageToString(bitmap);
                        params.put("image", imageData);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                requestQueue.add(stringRequest);
            }
        });




        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                finish();
            }
        });

        locationvisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                location = locationvisi.getSelectedItem().toString();

//                Toast.makeText(getBaseContext(), locationvisi.getSelectedItem().toString(),
//                        Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        proffesionvisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                profession = proffesionvisi.getSelectedItem().toString();
//                Toast.makeText(getBaseContext(), proffesionvisi.getSelectedItem().toString(),
//                        Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        notop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextint = 6;
                onBackPressed();
            }
        });


        if(global.verifyOrnot==0){
            progressBar.setVisibility(View.GONE);
            proffesionvisi.setVisibility(View.GONE);
//            btnVerify.setVisibility(View.GONE);
//            textinputverifyOtp.setVisibility(View.GONE);
            nextint = 1;
            mobilenumbervisi.setVisibility(View.GONE);
            genderselectvisi.setVisibility(View.VISIBLE);
            gendertext.setVisibility(View.VISIBLE);
            eText.setVisibility(View.GONE);
            datePicker.setVisibility(View.VISIBLE);
            dab.setVisibility(View.VISIBLE);
        }else if(global.verifyOrnot==2){
            nextint = 6;
            progressBar.setVisibility(View.GONE);
            proffesionvisi.setVisibility(View.GONE);
//            btnVerify.setVisibility(View.VISIBLE);
//            textinputverifyOtp.setVisibility(View.VISIBLE);
            mobilenumbervisi.setVisibility(View.VISIBLE);
            genderselectvisi.setVisibility(View.GONE);
            gendertext.setVisibility(View.GONE);
            eText.setVisibility(View.GONE);
            datePicker.setVisibility(View.GONE);
            dab.setVisibility(View.GONE);
            notop.setVisibility(View.GONE);
            buttonSignUp.setText("Next");
//            textInputNumber.setText("8000420648");

        }

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nextint == 6 && textInputNumber.getText().length()==10) {
                    buttonSignUp.setText("Verify OTP");
                    number = String.valueOf(textInputNumber.getText());
                    Log.e("entered no - ",number);
                    userName = String.valueOf(textInputEditTextUsername.getText());

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String apiKey = "xAwhP3qZM2JKP2iZx25819hVMVSTfSyGz0eg1INgFKhJ1u3OoXn57BTmrjXl";
                                String sendId = "FSTSMS";
                                //important step...
                                otp = new Random().nextInt(8999) + 1000;//[1000 - 9999]
                                Log.e("otp_sended",otp+"");
                                String message = String.valueOf(otp);
                                message = URLEncoder.encode(message, "UTF-8");
                                String language = "english";
                                String route = "p";
//https://www.fast2sms.com/dev/bulkV2?authorization=xAwhP3qZM2JKP2iZx25819hVMVSTfSyGz0eg1INgFKhJ1u3OoXn57BTmrjXl&variables_values=5599&route=otp&numbers=8000420648
                                //String myUrl = "https://www.fast2sms.com/dev/bulk?authorization=" + apiKey + "&sender_id=" + sendId + "&message=" + message + "&language=" + language + "&route=" + route + "&numbers=8000420648";

//                                Toast.makeText(SignUp.this, "OTP sended = "+otp, Toast.LENGTH_SHORT).show();

//                                String myUrl = "https://www.fast2sms.com/dev/bulk?authorization=" + apiKey + "&sender_id=" + sendId + "&variables_values=" + "&message=" + message + "&language=" + language + "&route=" + route + "&numbers="+number;
                                String myUrl = "https://www.fast2sms.com/dev/bulkV2?authorization=" + apiKey +"&variables_values=" + message + "&route=otp&numbers="+number;
                                //sending get request using java..
                                java.net.URL url = new URL(myUrl);
                                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                                con.setRequestMethod("GET");
                                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                                con.setRequestProperty("cache-control", "no-cache");
                                System.out.println("Wait..............");
                                int code = con.getResponseCode();
                                System.out.println("Response code : " + code);
                                StringBuffer response = new StringBuffer();
                                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                while (true) {
                                    String line = br.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    response.append(line);
                                }
                                System.out.println(response);
                                if (String.valueOf(response).contains("true")) {
                                    Log.e("verify otp to - ",otp+"");
                                    nextint=7;
                                }else{
                                    Log.e("No OTP","Didn't got any OTP");
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textInputNumber.setText("");
                            textInputNumber.setHint("Enter OTP");
                            notop.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                }else{
                    Toast.makeText(SignUp.this, "Phone number is not correct", Toast.LENGTH_SHORT).show();
                }
                if(nextint == 7){
                    //send otp
                    buttonSignUp.setText("Verifying");
                    String intuputotp = String.valueOf(textInputNumber.getText());
                    if(intuputotp.equals(String.valueOf(otp))) {
                        updatePhonenumber(final_email,number);
                        editor = sharedPreferences.edit();
                        editor.putInt("verified", 1);
                        editor.apply();
                        Toast.makeText(SignUp.this, "Number Verified", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), DashBoard.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                        finish();
                    }else{
                        Toast.makeText(SignUp.this, "OTP Not verified please try again later", Toast.LENGTH_SHORT).show();
                    }
                }

                fullName = String.valueOf(textInputEditTextFullName.getText());
                userName = String.valueOf(textInputEditTextUsername.getText());
                number = String.valueOf(textInputNumber.getText());


                if (nextint == 1) {
                    if (!dateofbirth.equals("")) {
                        nextint = 2;
                        genderselectvisi.setVisibility(View.GONE);
                        gendertext.setVisibility(View.GONE);
                        eText.setVisibility(View.GONE);
                        datePicker.setVisibility(View.GONE);
                        dab.setVisibility(View.GONE);
                        locationvisi.setVisibility(View.GONE);
                        tepro.setVisibility(View.VISIBLE);
                        proffesionvisi.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SignUp.this, "Please Select your birthdate", Toast.LENGTH_LONG).show();
                    }
                } else if (nextint == 2) {
                    fullName = String.valueOf(textInputEditTextFullName.getText());
                    userName = String.valueOf(textInputEditTextUsername.getText());
                    password = String.valueOf(TextInputEditTextPassowrd.getText());
                    email = String.valueOf(TextInputEditTextEmail.getText());

                    number = String.valueOf(textInputNumber.getText());


                    if (!profession.equals("") && !usergender.equals("")) {
                        progressBar.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                StringRequest request = new StringRequest(Request.Method.POST,
                                        global.BASE_URL + global.newSignup, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("File Uploaded Successfully")) {
                                            progressBar.setVisibility(View.GONE);
//                                            Toast.makeText(SignUp.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignUp.this, Tutorial.class);
                                            startActivity(i);
//                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(SignUp.this, "Username Already in use.please try another", Toast.LENGTH_SHORT).show();
                                        }
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
                                        map.put("fullname", pushusername);
                                        map.put("username", "");
                                        map.put("email", final_email);
                                        map.put("password", password);
                                        map.put("location", global.userLoc);
                                        map.put("gender", usergender);
                                        map.put("number", number);
                                        map.put("profession", profession);
                                        map.put("dob", dateofbirth);
                                        map.put("image", final_photo);
                                        return map;
                                    }
                                };
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                queue.add(request);
                            }

                        }, 1000);

                    } else {
                        Toast.makeText(SignUp.this, "Please Input all fields", Toast.LENGTH_LONG).show();
                    }

                }
            }

        });

    }

    public void updatePhonenumber(String femail,String number) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest request = new StringRequest(Request.Method.POST,
                        global.BASE_URL+"updateNumber.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("email", femail);
                        map.put("number", number);
                        return map;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }
        }, 2000);

    }


    private void checkUsername(final String userdatabase) {

        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest request = new StringRequest(Request.Method.POST,
                        global.BASE_URL + global.checkUsername, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        if (response.equals("exist")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "username already exist", Toast.LENGTH_LONG).show();
                        } else {
//                            progressBar.setVisibility(View.GONE);
//                            proffesionvisi.setVisibility(View.GONE);
//                            nextint = 1;
//                            usernamevisi.setVisibility(View.GONE);
//                            mobilenumbervisi.setVisibility(View.GONE);
//                            genderselectvisi.setVisibility(View.VISIBLE);
//                            gendertext.setVisibility(View.VISIBLE);
//                            eText.setVisibility(View.GONE);
//                            datePicker.setVisibility(View.VISIBLE);
//                            dab.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username", userdatabase);
                        return map;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }

        }, 1000);

    }


    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.male:
                if (checked)
                    usergender = "Male";
                break;
            case R.id.female:
                if (checked)
                    usergender = "female";
                break;
            case R.id.other:
                if (checked)
                    usergender = "other";
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                phpImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImages = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImages;
    }


    @Override
    public void onBackPressed() {
        if (nextint == 1 || nextint ==  6) {
            super.onBackPressed();
        } else if (nextint == 2) {
            nextint = 1;
            genderselectvisi.setVisibility(View.VISIBLE);
            gendertext.setVisibility(View.VISIBLE);
            eText.setVisibility(View.GONE);
            datePicker.setVisibility(View.VISIBLE);
            dab.setVisibility(View.VISIBLE);
            locationvisi.setVisibility(View.GONE);
            proffesionvisi.setVisibility(View.GONE);
        }else if(nextint ==7){
            nextint = 6;
            mobilenumbervisi.setVisibility(View.VISIBLE);
            buttonSignUp.setText("Next");
            if(!number.equals("")){
                textInputNumber.setText(number);
            }
        }


    }

    private void getTheUserPermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationGetter locationGetter = new LocationGetter(SignUp.this, REQUEST_LOCATION, locationManager);


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationGetter.OnGPS();
        } else {

            locationGetter.getLocation();
        }
    }


}