package com.papanews;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Login extends AppCompatActivity {


    ConstraintLayout constraintLayout;
    TextInputEditText textInputEditTextUsername, TextInputEditTextPassowrd;
    MaterialTextView textViewSignup;
    //skip for now
    MaterialTextView skipnow;
    ProgressBar progressBar;
    RelativeLayout loginGoogle;
    RelativeLayout log;

    int RC_SIGN_IN = 0;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    Set<String> set = new HashSet<String>();
    CallbackManager callbackManager;

    LoginButton faceLogin;
    GoogleSignInClient mGoogleSignInClient;

    String first_name, last_name;
    String facebookEmail;

    private LocationManager locationManager;
    private int REQUEST_LOCATION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        edit = prefs.edit();
        set = prefs.getStringSet("recomended", null);


        loginGoogle = findViewById(R.id.googlelogin);
        faceLogin = findViewById(R.id.facebook);
        log = findViewById(R.id.facebooklogin);
        textInputEditTextUsername = findViewById(R.id.username);
        TextInputEditTextPassowrd = findViewById(R.id.password);
        textViewSignup = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);
        constraintLayout = findViewById(R.id.logic);
        //skip for now
        skipnow = findViewById(R.id.skipfornow);


        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final int j = sharedpreferences.getInt("key", 0);
        Log.i("hffhgdfszcsdf", String.valueOf(j));


        if (j > 0) {
            Log.i("Loogged in", "your in");
            Intent activity = new Intent(getApplicationContext(), DashBoard.class);
            startActivity(activity);
        }

        skipnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedpreferences.edit();
                editor.putString("skip_now", "skip");
                editor.apply();

                Intent i = new Intent(Login.this, Aboutus_terms.class);
                i.putExtra("EXTRA_SESSION_ID", "lang");
                startActivity(i);

//                throw new RuntimeException("Boom!");
            }
        });



        final ConnectionDectector cd = new ConnectionDectector(this);
        if (!(cd.isConnected())) {
            Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Aboutus_terms.class);
            intent.putExtra("EXTRA_SESSION_ID", "noi");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            finish();
        }


//        getTheUserPermission();
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceLogin.performClick();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                throw new RuntimeException("Test Crash");
                signIn();
            }
        });


        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.logincheck = 0;
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
                finish();
            }
        });


        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        faceLogin.setPermissions(Arrays.asList("email", "public_profile"));

        callbackManager = CallbackManager.Factory.create();

//        if (!loggedOut) {
//
//        }

        faceLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String name = loginResult.getAccessToken().getUserId();
                String token = loginResult.getAccessToken().getToken();

                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                getUserProfile(AccessToken.getCurrentAccessToken());
                global.logincheck = 2;

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    private void getTheUserPermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationGetter locationGetter = new LocationGetter(Login.this, REQUEST_LOCATION, locationManager);


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationGetter.OnGPS();

        }
    }
    private void checkEmail(final String email){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest request = new StringRequest(Request.Method.POST,
                        global.BASE_URL+global.checkEmail, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editor.putString("skip_now", "dont_skip");
                        editor.apply();
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        if(response.equals("exist")){
                            startActivity(new Intent(Login.this, Tutorial.class));
                        }else{
                            startActivity(new Intent(Login.this, SignUp.class));
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
                        map.put("email", email);
                        return map;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }

        }, 1000);

    }


    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("facebook_login", object.toString());
                        try {
                            first_name = object.getString("first_name");
                            last_name = object.getString("last_name");
                            facebookEmail = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            Log.d("facebook_login", image_url);


                            editor = sharedpreferences.edit();
                            editor.putString("photogoogle", image_url);

                            editor.putInt("checkcheck", 2);
                            editor.putString("facebook_fullname", first_name + " " + last_name);
                            editor.putString("facebook_username", facebookEmail);
                            editor.putString("facebook_password", first_name);
                            editor.putString("final_email", facebookEmail);
                            editor.putString("facebook_image", image_url);
                            editor.apply();

                            checkEmail(facebookEmail);

//                            addDataToDatabase(first_name + " " + last_name, facebookEmail, first_name, facebookEmail, image_url);

//                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            txtEmail.setText(email);
//                            Picasso.with(MainActivity.this).load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void signIn() {
        editor = sharedpreferences.edit();
        editor.putInt("checkcheck", 3);
        editor.apply();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Uri photo = account.getPhotoUrl();
            String email = account.getEmail();
            Log.d("photo_google", String.valueOf(photo));

            editor = sharedpreferences.edit();
            editor.putString("final_email", email);
            editor.putString("photogoogle", String.valueOf(photo));
            editor.apply();
            checkEmail(email);
            global.logincheck = 1;

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(Login.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
//            startActivity(new Intent(Login.this, MainActivity.class));
            mGoogleSignInClient.signOut();
        }
        LoginManager.getInstance().logOut();
        super.onStart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        hideSystemUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }


    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LOW_PROFILE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }


}