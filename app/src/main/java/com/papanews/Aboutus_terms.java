package com.papanews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.r0adkll.slidr.Slidr;

public class Aboutus_terms extends AppCompatActivity {
    RelativeLayout abtus,tabdc,noin,langu;
    RelativeLayout cons;
    MaterialCardView langub1,langub2;
    MaterialButton noir,langureferesh;
    int cs1=0,cs2=0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String selectedlang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likhaputti);
        abtus = findViewById(R.id.aboutus);
        tabdc = findViewById(R.id.tandc);
        noin = findViewById(R.id.noin);
        cons = findViewById(R.id.cons);
        noir = findViewById(R.id.noireferesh);
        langu = findViewById(R.id.langu);
        langub1 = findViewById(R.id.langub1);
        langub2 = findViewById(R.id.langub2);
        langureferesh = findViewById(R.id.langureferesh);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        selectedlang = sharedPreferences.getString("selectedlang","English");
        editor = sharedPreferences.edit();

        int Eng = sharedPreferences.getInt("English", 0);

        if(Eng==0 ){
            langub1.setCardBackgroundColor(Color.parseColor("#F4931D"));
            langub2.setCardBackgroundColor(Color.parseColor("#454545"));
        }
        langub1.setCardBackgroundColor(Color.parseColor("#F4931D"));

        if(selectedlang.equals("Hindi")){
            langub2.setCardBackgroundColor(Color.parseColor("#F4931D"));
            langub1.setCardBackgroundColor(Color.parseColor("#454545"));
        }
        if(selectedlang.equals("English")){
            langub1.setCardBackgroundColor(Color.parseColor("#F4931D"));
            langub2.setCardBackgroundColor(Color.parseColor("#454545"));
        }



        Slidr.attach(this);

        final String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        if(sessionId.equals("abtus")){
            abtus.setVisibility(View.VISIBLE);
            tabdc.setVisibility(View.GONE);
            noin.setVisibility(View.GONE);
            langu.setVisibility(View.GONE);
        }else if(sessionId.equals("tandc")){
            tabdc.setVisibility(View.VISIBLE);
            abtus.setVisibility(View.GONE);
            noin.setVisibility(View.GONE);
            langu.setVisibility(View.GONE);

        }else if(sessionId.equals("noi")){
            tabdc.setVisibility(View.GONE);
            abtus.setVisibility(View.GONE);
            langu.setVisibility(View.GONE);
            noin.setVisibility(View.VISIBLE);
            cons.setBackgroundColor(Color.parseColor("#111111"));
        }else if(sessionId.equals("lang")){
            tabdc.setVisibility(View.GONE);
            abtus.setVisibility(View.GONE);
            langub1.setCardBackgroundColor(Color.parseColor("#454545"));
            langub2.setCardBackgroundColor(Color.parseColor("#454545"));
            langu.setVisibility(View.VISIBLE);
            noin.setVisibility(View.GONE);
            cons.setBackgroundColor(Color.parseColor("#111111"));
        }

        noir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConnectionDectector cd = new ConnectionDectector(Aboutus_terms.this);
                if (!(cd.isConnected())) {
                    Toast.makeText(Aboutus_terms.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Aboutus_terms.this, Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.nothing, R.anim.slide_out_left);
                    finish();
                }
            }
        });

        langub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cs1 == 0){
                    cs1=1;
                    cs2=0;
                    langub1.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    langub2.setCardBackgroundColor(Color.parseColor("#454545"));
                }else{
                    cs1=0;
                    cs2=0;
                    langub1.setCardBackgroundColor(Color.parseColor("#454545"));
                    langub2.setCardBackgroundColor(Color.parseColor("#454545"));
                }
            }
        });
        langub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cs2 == 0){
                    cs2=1;
                    cs1=0;
                    langub2.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    langub1.setCardBackgroundColor(Color.parseColor("#454545"));
                }else{
                    cs2=0;
                    cs1=0;
                    langub2.setCardBackgroundColor(Color.parseColor("#454545"));
                    langub1.setCardBackgroundColor(Color.parseColor("#454545"));
                }
            }
        });

        langureferesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cs1 == 1){
//                    Toast.makeText(likhaputti.this, "passinge", Toast.LENGTH_SHORT).show();

                    editor = sharedPreferences.edit();
                    editor.putString("selectedlang", "English");
                    editor.putInt("English",1);
                    editor.apply();

//                    Intent intent = new Intent(v.getContext(), Profile.class);
//                    v.getContext().startActivity(intent);
                    Intent intent = new Intent(Aboutus_terms.this, Tutorial.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.nothing, R.anim.slide_in_right);
                    finish();


//                    Intent intent = new Intent(likhaputti.this, Profile.class);
//                    intent.putExtra("EXTRA_SESSION_ID", "english");
//                    startActivity(intent);

                }else if(cs2==1){
//                    Toast.makeText(likhaputti.this, "passingh", Toast.LENGTH_SHORT).show();

                    editor = sharedPreferences.edit();
                    editor.putString("selectedlang", "Hindi");
                    editor.putInt("Hindi",1);
                    editor.apply();

//                    Intent intent = new Intent(v.getContext(), Profile.class);
//                    v.getContext().startActivity(intent);
//                    overridePendingTransition(R.anim.nothing, R.anim.slide_in_right);
//                    finish();

                    Intent intent = new Intent(Aboutus_terms.this, Tutorial.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.nothing, R.anim.slide_in_right);
                    finish();
                }else{
                    Toast.makeText(Aboutus_terms.this, "Please select your language", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
    }



}