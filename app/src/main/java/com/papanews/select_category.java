package com.papanews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.r0adkll.slidr.Slidr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class select_category extends AppCompatActivity {

    MaterialCardView technews, politicsnews, entertainnews, businessnews, international, startup, sports, infu, miscell;
    MaterialButton nex;

    int t1, p1, e1, b1, c1;
    int startup_int, sports_int, influence_int, miscelen_int;
    int loggedinornot=0;
    String skip;
    Set<String> set = new HashSet<String>();
    Set<String> noti_set = new HashSet<String>();
    int count = 0;
    List<String> reco = new ArrayList<>();
    List<String> notification_select = new ArrayList<>();
    private static final String URL_INTERNATIONAL = "http://papanews.in/PapaNews/getIntetrnational.php";
    private static final String URL_INFLUENCE = "http://papanews.in/PapaNews/getInfluence.php";
    private static final String URL_MISCEL = "http://papanews.in/PapaNews/getMiscel.php";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);


        businessnews = findViewById(R.id.b1);
        startup = findViewById(R.id.b2);
        technews = findViewById(R.id.b3);
        entertainnews = findViewById(R.id.b4);
        sports = findViewById(R.id.b5);
        politicsnews = findViewById(R.id.b6);
        international = findViewById(R.id.b7);
        infu =  findViewById(R.id.b8);
        miscell = findViewById(R.id.b9);
        nex = findViewById(R.id.next);
        //slider
        Slidr.attach(this);
        businessnews.setCardBackgroundColor(Color.parseColor("#454545"));
        startup.setCardBackgroundColor(Color.parseColor("#454545"));
        technews.setCardBackgroundColor(Color.parseColor("#454545"));
        entertainnews.setCardBackgroundColor(Color.parseColor("#454545"));
        sports.setCardBackgroundColor(Color.parseColor("#454545"));
        politicsnews.setCardBackgroundColor(Color.parseColor("#454545"));
        international.setCardBackgroundColor(Color.parseColor("#454545"));
        infu.setCardBackgroundColor(Color.parseColor("#454545"));
        miscell.setCardBackgroundColor(Color.parseColor("#454545"));
        SharedPreferences prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor edit = prefs.edit();
        t1 = prefs.getInt("t1", 0);
        p1 = prefs.getInt("p1", 0);
        e1 = prefs.getInt("e1", 0);
        b1 = prefs.getInt("b1", 0);
        c1 = prefs.getInt("c1", 0);

        startup_int = prefs.getInt("startup", 0);
        sports_int = prefs.getInt("sports", 0);
        influence_int = prefs.getInt("influence", 0);
        miscelen_int = prefs.getInt("miss", 0);
        global.cat_set.add(0, "Profile");


        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        skip = sharedPreferences.getString("skip_now", null);
        Log.e("skipskipskip ", skip);
        if(skip.equals("skip")){
            Intent i = new Intent(select_category.this, DashBoard.class);
            startActivity(i);
        }


        loggedinornot = sharedPreferences.getInt("onceLogged", 0);
        Log.e("skipskipskip ", String.valueOf(loggedinornot));
        if (loggedinornot == 1) {
            Intent i = new Intent(select_category.this, DashBoard.class);
            startActivity(i);
        }


        if (t1 == 5) {
            technews.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add("http://papanews.in/PapaNews/getTech.php");
            notification_select.add("tech");
            count++;
        }
        if (p1 == 5) {
            politicsnews.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add("http://papanews.in/PapaNews/getPolitics.php");
            notification_select.add("politics");
            count++;
        }
        if (e1 == 5) {
            entertainnews.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add("http://papanews.in/PapaNews/getEntertain.php");
            notification_select.add("entertain");
            count++;
        }
        if (b1 == 5) {
            businessnews.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add("http://papanews.in/PapaNews/getBusiness.php");
            notification_select.add("business");
            count++;
        }
        if (startup_int == 5) {
            startup.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add("http://papanews.in/PapaNews/getStartup.php");
            notification_select.add("startup");
            count++;
        }
        if (sports_int == 5) {
            sports.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add("http://papanews.in/PapaNews/getSports.php");
            notification_select.add("sports");
            count++;
        }
        if (influence_int == 5) {
            infu.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add(URL_INFLUENCE);
            notification_select.add("influence");
            count++;
        }
        if (miscelen_int == 5) {
            miscell.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add(URL_MISCEL);
            notification_select.add("miscell");
            count++;
        }
        if (c1 == 5) {
            international.setCardBackgroundColor(Color.parseColor("#F4931D"));
            reco.add(URL_INTERNATIONAL);
            notification_select.add("international");
            count++;
        }

        businessnews.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (b1 == 0) {
                    businessnews.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("b1", 5);
                    Log.e("b1 list ::", String.valueOf(b1));
                    edit.commit();
                    reco.add("http://papanews.in/PapaNews/getBusiness.php");
                    notification_select.add("business");
//                    Toast.makeText(select_category.this, "select", Toast.LENGTH_SHORT).show();
                    b1 = 1;
                    count++;
                } else {
                    businessnews.setCardBackgroundColor(Color.parseColor("#454545"));
                    Log.e("b1 list ::", String.valueOf(b1));
                    edit.putInt("b1", 0);
                    edit.commit();
                    reco.remove("http://papanews.in/PapaNews/getBusiness.php");
                    notification_select.remove("business");
//                    Toast.makeText(select_category.this, "not", Toast.LENGTH_SHORT).show();
                    b1 = 0;
                    count--;
                }
            }
        });

        technews.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (t1 == 0) {
                    technews.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("t1", 5);
                    Log.e("t1 list ::", String.valueOf(t1));
                    edit.commit();
                    t1 = 1;
                    reco.add("http://papanews.in/PapaNews/getTech.php");
                    notification_select.add("tech");
                    count++;
                } else {
                    technews.setCardBackgroundColor(Color.parseColor("#454545"));
                    Log.e("t1 list ::", String.valueOf(t1));
                    edit.putInt("t1", 0);
                    edit.commit();
                    t1 = 0;
                    reco.remove("http://papanews.in/PapaNews/getTech.php");
                    notification_select.remove("tech");
                    count--;
                }
            }
        });


        politicsnews.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (p1 == 0) {
                    politicsnews.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("p1", 5);
                    Log.e("p1 list ::", String.valueOf(p1));
                    edit.commit();
                    p1 = 1;
                    reco.add("http://papanews.in/PapaNews/getPolitics.php");
                    notification_select.add("politics");
                    count++;
                } else {
                    politicsnews.setCardBackgroundColor(Color.parseColor("#454545"));
                    Log.e("p1 list ::", String.valueOf(p1));
                    edit.putInt("p1", 0);
                    edit.commit();
                    p1 = 0;
                    reco.remove("http://papanews.in/PapaNews/getPolitics.php");
                    notification_select.remove("politics");
                    count--;
                }
            }
        });


        startup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Satrtup", Toast.LENGTH_SHORT).show();
                if (startup_int == 0) {
                    startup.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("startup", 5);
                    edit.apply();
                    reco.add("http://papanews.in/PapaNews/getStartup.php");
                    notification_select.add("startup");
//                    Toast.makeText(select_category.this, "select", Toast.LENGTH_SHORT).show();
                    startup_int = 1;
                    count++;
                } else {
                    startup.setCardBackgroundColor(Color.parseColor("#454545"));
                    edit.putInt("startup", 0);
                    edit.commit();
                    reco.remove("http://papanews.in/PapaNews/getStartup.php");
                    notification_select.remove("startup");
//                    Toast.makeText(select_category.this, "not", Toast.LENGTH_SHORT).show();
                    startup_int = 0;
                    count--;
                }
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "sports", Toast.LENGTH_SHORT).show();
                if (sports_int == 0) {
                    sports.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("sports", 5);
                    edit.apply();
                    reco.add("http://papanews.in/PapaNews/getSports.php");
                    notification_select.add("sports");
//                    Toast.makeText(select_category.this, "select", Toast.LENGTH_SHORT).show();
                    sports_int = 1;
                    count++;
                } else {
                    sports.setCardBackgroundColor(Color.parseColor("#454545"));
                    edit.putInt("sports", 0);
                    edit.commit();
                    reco.remove("http://papanews.in/PapaNews/getSports.php");
                    notification_select.remove("sports");
//                    Toast.makeText(select_category.this, "not", Toast.LENGTH_SHORT).show();
                    sports_int = 0;
                    count--;
                }
            }
        });


        entertainnews.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (e1 == 0) {
                    entertainnews.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("e1", 5);
                    Log.e("t1 list ::", String.valueOf(e1));
                    edit.commit();
                    reco.add("http://papanews.in/PapaNews/getEntertain.php");
                    notification_select.add("entertain");
                    e1 = 1;
                    count++;
                } else {
                    entertainnews.setCardBackgroundColor(Color.parseColor("#454545"));
                    Log.e("t1 list ::", String.valueOf(e1));
                    edit.putInt("e1", 0);
                    edit.commit();
                    reco.remove("http://papanews.in/PapaNews/getEntertain.php");
                    notification_select.remove("entertain");
                    e1 = 0;
                    count--;
                }
            }
        });


        infu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "influence", Toast.LENGTH_SHORT).show();
                if (influence_int == 0) {
                    infu.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("influence", 5);
                    edit.apply();
                    reco.add(URL_INFLUENCE);
                    notification_select.add("influence");
//                    Toast.makeText(select_category.this, "select", Toast.LENGTH_SHORT).show();
                    influence_int = 1;
                    count++;
                } else {
                    infu.setCardBackgroundColor(Color.parseColor("#454545"));
                    edit.putInt("influence", 0);
                    edit.commit();
//                    reco.remove("Buisness");
                    reco.remove(URL_INFLUENCE);
                    notification_select.remove("influence");
//                    Toast.makeText(select_category.this, "not", Toast.LENGTH_SHORT).show();
                    influence_int = 0;
                    count--;
                }
            }
        });

        miscell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "misscelaneous", Toast.LENGTH_SHORT).show();
                if (miscelen_int == 0) {
                    miscell.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("miss", 5);
                    edit.apply();
//                    reco.add("Buisness");
                    reco.add(URL_MISCEL);
                    notification_select.add("miscell");
//                    Toast.makeText(select_category.this, "select", Toast.LENGTH_SHORT).show();
                    miscelen_int = 1;
                    count++;
                } else {
                    miscell.setCardBackgroundColor(Color.parseColor("#454545"));
                    edit.putInt("miss", 0);
                    edit.commit();
                    reco.remove(URL_MISCEL);
                    notification_select.remove("miscell");
//                    reco.remove("Buisness");
//                    Toast.makeText(select_category.this, "not", Toast.LENGTH_SHORT).show();
                    miscelen_int = 0;
                    count--;
                }
            }
        });


        international.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (c1 == 0) {
                    international.setCardBackgroundColor(Color.parseColor("#F4931D"));
                    edit.putInt("c1", 5);
                    Log.e("c1 list ::", String.valueOf(c1));
                    edit.commit();
                    reco.add(URL_INTERNATIONAL);
                    notification_select.add("international");
//                    reco.add("Currentaf");
                    c1 = 1;
                    count++;
                    Log.e("counting", String.valueOf(count));
                    Log.e("recomended ::", String.valueOf(reco));
                } else {
                    international.setCardBackgroundColor(Color.parseColor("#454545"));
                    edit.putInt("c1", 0);
                    Log.e("c1 list ::", String.valueOf(c1));
                    edit.commit();
                    reco.remove(URL_INTERNATIONAL);
                    notification_select.remove("international");
//                    reco.remove("Currentaf");
                    c1 = 0;
                    count--;
                }
            }
        });


        nex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < 1) {
                    Toast.makeText(getApplicationContext(), "Please select any one categories", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt("onceLogged", 1);
                    editor.apply();
                    noti_set.addAll(notification_select);
                    edit.putStringSet("notification", noti_set);

                    set.addAll(reco);
                    edit.putStringSet("recomended", set);
                    Log.e("recomended list ::", String.valueOf(set));
                    edit.commit();

                    Intent i = new Intent(getApplicationContext(), DashBoard.class);
                    startActivity(i);
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
    public void onBackPressed() {
        super.onBackPressed();
//        finishAffinity();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
    }
}