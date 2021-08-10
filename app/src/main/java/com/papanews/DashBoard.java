package com.papanews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.Duration;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.papanews.ApplicationClass.ACTION_NEXT;
import static com.papanews.ApplicationClass.ACTION_PLAY;
import static com.papanews.ApplicationClass.ACTION_PREVIOUS;
import static com.papanews.ApplicationClass.CHANNEL_ID_2;
import static com.papanews.global.timenotify;


public class DashBoard extends AppCompatActivity {


    List<String> recomend = new ArrayList<String>();
    List<String> send_notification = new ArrayList<String>();
    List<ItemModel> itemsreco = new ArrayList<>();

    Handler mHandler;
    SharedPreferences.Editor editor_list;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    GoogleSignInClient mGoogleSignInClient;

    String selectedlang;
    String timefinal;
    //skip or not
    String skip;
    List<String> sample;
    int load = 1;
    String link;
    int min;
    int hrs;
    int sec;
    int datanews = 42;
    int check = 0;
    int finalcheck;
    int doitnow;
    int checkNoti;

    Adapter adapter;
    Calendar c;

    LayoutInflater inflater_audio;
    View vi_audio;
    Mediacontrol mediacontrol = new Mediacontrol(DashBoard.this);

    FirebaseAnalytics mFirebaseAnalytics;

    ViewPager2 viewPager;
    TabLayout tabLayout;
    ImageView introslapsh;
    RelativeLayout relativeLayout;
    ImageView pp;

//    @BindView(R.id.viewPager) ViewPager viewPager;
//    @BindView(R.id.mainTitletab) TabLayout tabLayout;
//    @BindView(R.id.introsplash) ImageView introslapsh;
//    @BindView(R.id.relative) RelativeLayout relativeLayout;
//    @BindView(R.id.play) ImageView pp;


    @SuppressLint({"ClickableViewAccessibility", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.mainTitletab);
        viewPager = findViewById(R.id.viewPager);
        relativeLayout = findViewById(R.id.relative);
        introslapsh = findViewById(R.id.introsplash);
        pp = findViewById(R.id.play);

        String[] tabTitles;
        //google analytics{analytics start}
        if (!global.skipskip.equals("skip")) {
            tabTitles = new String[]{"Profile", "Recommended", "Technology", "Politics",
                    "Business", "Startup", "Entertainment", "Sports", "International", "Influencer", "Miscellaneous"};
        }else{
            tabTitles = new String[]{"Profile", "Technology", "Politics",
                    "Business", "Startup", "Entertainment", "Sports", "International", "Influencer", "Miscellaneous"};
        }
        //link = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.ecpgr.cgiar.org%2Fin-situ-landraces-best-practice-evidence-based-database%2Flandrace%3FlandraceUid%3D13498&psig=AOvVaw0X67gZ_85hX2R8jytm1OU-&ust=1627987806163000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCNCouamVkvICFQAAAAAdAAAAABAD";

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://papanews.in/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiGetdata apiGetdata = retrofit.create(ApiGetdata.class);
//
//        Call<List<Post>> call = apiGetdata.getPosts();
//
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {
//                if(!response.isSuccessful()){
//                    Log.e("retrofit ", String.valueOf(response));
//                    return;
//                }else {
//                    Log.e("retrofit ", String.valueOf(response));
//                    List<Post> posts = response.body();
//                    Log.e("retrofit posts", String.valueOf(posts));
//
//                    for(Post post : posts){
//                        Log.e("retrofit in", String.valueOf(post.getLongDesc()));
//                        Log.e("retrofit in", String.valueOf(post.getShortDesc()));
//                        Log.e("retrofit in", String.valueOf(post.getUserId()));
//                        Log.e("retrofit in", String.valueOf(post.getTitle()));
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                Log.e("retrofit ", String.valueOf(t));
//            }
//        });






        // Save FCM tokens in the Firebase
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG_tokenfetch", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
//                        String msg = token;
                        Log.d("TAG_tokenfetch", token);
//                        Toast.makeText(DashBoard.this, token, Toast.LENGTH_SHORT).show();
                    }
                });




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(createCardAdapter());
                new TabLayoutMediator(tabLayout, viewPager,
                        new TabLayoutMediator.TabConfigurationStrategy() {
                            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                                tab.setText("Tab " + (position + 1));
//                                viewPager.setPageTransformer(new FlipVerticalPageTransformer());
                                viewPager.setOffscreenPageLimit(1);
                                tabLayout.selectTab(tabLayout.getTabAt(1));
                                tab.setText(tabTitles[position]);
                            }
                        }).attach();

//                try {
//                    adapter = new Adapter(getSupportFragmentManager());
//                    viewPager.setOffscreenPageLimit(11);
//                    viewPager.setAdapter(RecyclerView.adapter);
//                    tabLayout.setupWithViewPager(viewPager);
//                    Objects.requireNonNull(tabLayout.getTabAt(1)).select();
//                } catch (Exception e) {
//                    Log.e("thowingexception at", "Toolbar");
////                    Toast.makeText(MainActivity.this, "crashing", Toast.LENGTH_SHORT).show();
//                }
                introslapsh.setVisibility(View.GONE);
//                Log.e("listrecoppppppppp ", String.valueOf(itemsreco));

//              scrollView.setVisibility(View.VISIBLE);
            }
        }, 2000);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);

        if (screenInches > 6 || screenInches == 6) {
            global.size_of_sh = 16;
            global.maxline_of_sh = 5;
            global.size_of_head = 18;
        } else if (screenInches < 6) {
            global.size_of_sh = 14;
            global.maxline_of_sh = 4;
            global.size_of_head = 16;
        }
        //{screen text size End}


        //notification managerfor new news{Notification start}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify", "notify", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        //{Notification end}


        //Check internet connection is available or not{Internet connection start}
        final ConnectionDectector cd = new ConnectionDectector(this);
        if (!(cd.isConnected())) {
            Intent intent = new Intent(this, Aboutus_terms.class);
            intent.putExtra("EXTRA_SESSION_ID", "noi");
            startActivity(intent);
            finish();
        }
        //{Internet connection end}


        //shared preference data from from login,signup and selectcategory activity{start}
        SharedPreferences sharedPreferences;
        sharedPreferences = this.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        final String user = sharedPreferences.getString("username", "");
        final int checkcheck = sharedPreferences.getInt("checkcheck", 0);
        final String fb_full = sharedPreferences.getString("facebook_fullname", "");
        final String fb_user = sharedPreferences.getString("facebook_username", "");
        final String fb_image = sharedPreferences.getString("facebook_image", "");
        global.userlogged.add(fb_user);

        //which language user has selected
        selectedlang = sharedPreferences.getString("selectedlang", "English");
//        Log.e("language_which ", selectedlang);
        subscribe_noti(selectedlang);


        runBusiness();
        runEntertain();
        runInfluence();
        runInternational();
        runMiscel();
        runPolitics();
        runSports();
        runStartup();
        runTechnology();

        //check if it is skipped or logged
        skip = sharedPreferences.getString("skip_now", "");

        //notificcation time checking
        finalcheck = sharedPreferences.getInt("notify", 0);

        this.mHandler = new Handler();

        //shared preferecne for storing the card data
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        global.skipskip = skip;


        // setting layout according to user preference skip or logged in{start}
        if (skip.equals("skip")) {
            global.returnahowmuch = 10;
            load = 2;
            editor.putInt("key", 3);
        } else if (skip.equals("dont_skip")) {
            View ss = inflater.inflate(R.layout.item_card, null); //log.xml is your file.
            final TextView sg = (TextView) ss.findViewById(R.id.selectCategorie);
            try {
                editor.putInt("onceLogged", 1);
                editor.apply();
                global.returnahowmuch = 11;
                load = 1;
                editor.putInt("key", 4);
                Set<String> set = prefs.getStringSet("recomended", null);
                sample = new ArrayList<String>(set);

                Set<String> noti_Set = prefs.getStringSet("notification", null);
                final List<String> noti_sample = new ArrayList<String>(noti_Set);
                recomend = sample;
                send_notification = noti_sample;

//                Log.e("send_notification", String.valueOf(send_notification));
                global.cat_set.addAll(send_notification);
                sg.setVisibility(View.GONE);
            } catch (Exception e) {
                sg.setVisibility(View.VISIBLE);
            }
        }
        //end


        //check if user is logged in using gmail or facebook{check login start}
        if (checkcheck == 1) {
            editor.putString("user_final", user);
        }
        if (checkcheck == 2) {
            editor.putString("user_final", fb_full);
            editor.putString("image_fb", fb_image);
        }

        editor.apply();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DashBoard.this);
        if (checkcheck == 3) {
            if (acct != null) {
                String personName = acct.getDisplayName();
                Uri personPhoto = acct.getPhotoUrl();
                global.userlogged.add(personName);
                editor.putString("user_final", personName);
                editor.putString("google_photo", String.valueOf(personPhoto));
                editor.putInt("method", 1);
                editor.putString("google_image", String.valueOf(personPhoto));
            }

        }
        //{check login end}


        //checking notification{start}
        checkNoti = sharedPreferences.getInt("reguCustome", 0);
        timefinal = sharedPreferences.getString("realTime", "");


        if (checkNoti == 0) {
            timenotify = "8:0:0";
        }
        //{end}


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(DashBoard.this);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, tabTitles[position]);
                bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, tabTitles[position]);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
//                Log.e("fragpresent",tabTitles[position]);
                MusicService.news_name = tabTitles[position];

                @SuppressLint("InflateParams")
                View playIcon = inflater.inflate(R.layout.item_card, null);
                ImageView play = (ImageView) playIcon.findViewById(R.id.play);

                try {
                    if (!mediacontrol.media_is_playing()) {
                        play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                        Log.e("whichfg",tabTitles[position] + "if");
                    }else{
                        Log.e("whichfg",tabTitles[position] + "else");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater
                .setDisplay(Display.DIALOG)
//                .setDisplay(Display.NOTIFICATION)
//                .setDisplay(Display.SNACKBAR)
//                .setDuration(Duration.INDEFINITE)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setButtonDismissClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideSystemUI();
                    }
                })
                .setButtonDoNotShowAgain(null)
                .setButtonDismiss("Maybe later")
                .setCancelable(false);
//
        appUpdater.start();

//        hideSystemUI();

//        sendNotification("Turkish influencer prosecuted 'for photos at Amsterdam sex museum'","123","http://papanews.in/PapaNews/images/influence/IMG1800624333.jpg");

    }

    private void subscribe_noti(String topic){
        Log.d("TAG_topic_dashboard", topic);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Topic subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Topic not subscribed";

                        }
                        Log.d("TAG", msg);
//                        Toast.makeText(DashBoard.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }



    private Adapter createCardAdapter() {
        Adapter adapter = new Adapter(this);
        return adapter;
    }


//    private final Runnable m_Runnable = new Runnable() {
//        public void run() {
//            runDatabase();
//            MainActivity.this.mHandler.postDelayed(m_Runnable, 2400000);
//        }
//    };

    public void runThread() {
        new Thread() {
            public void run() {
                addListBusiness(global.GET_NEWS + "?language=English&limit=1200");
                addListEntertain(global.BASE_URL + global.entertain + "?language=English&limit=1200");
                addListInfluence(global.BASE_URL + global.influence + "?language=English&limit=1200");
                addListInternational(global.BASE_URL + global.international + "?language=English&limit=1200");
                addListMiscell(global.BASE_URL + global.miscel + "?language=English&limit=1200");
                addListPolitics(global.BASE_URL + global.politics + "?language=English&limit=1200");
                addListSport(global.BASE_URL + global.sports + "?language=English&limit=1200");
                addListStartup(global.BASE_URL + global.startup + "?language=English&limit=1200");
                addListTechnology(global.BASE_URL + global.technology + "?language=English&limit=1200");
                Log.e("thread_GG", "hehe");
            }
        }.start();
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            addListBusiness(global.BASE_URL + global.business + "?language=English&limit=1200");
            addListEntertain(global.BASE_URL + global.entertain + "?language=English&limit=1200");
            addListInfluence(global.BASE_URL + global.influence + "?language=English&limit=1200");
            addListInternational(global.BASE_URL + global.international + "?language=English&limit=1200");
            addListMiscell(global.BASE_URL + global.miscel + "?language=English&limit=1200");
            addListPolitics(global.BASE_URL + global.politics + "?language=English&limit=1200");
            addListSport(global.BASE_URL + global.sports + "?language=English&limit=1200");
            addListStartup(global.BASE_URL + global.startup + "?language=English&limit=1200");
            addListTechnology(global.BASE_URL + global.technology + "?language=English&limit=1200");
            Log.e("thread_GG", "hehe");
        }
    }


    public void runTechnology() {
        new Thread() {
            public void run() {
                addListTechnology(global.BASE_URL + global.technology + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "tech");
            }
        }.start();
    }

    public void runPolitics() {
        new Thread() {
            public void run() {
                addListPolitics(global.BASE_URL + global.politics + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "politics");
            }
        }.start();
    }

    public void runSports() {
        new Thread() {
            public void run() {
                addListSport(global.BASE_URL + global.sports + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "sports");
            }
        }.start();
    }

    public void runStartup() {
        new Thread() {
            public void run() {
                addListStartup(global.BASE_URL + global.startup + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "startup");
            }
        }.start();
    }

    public void runBusiness() {
        new Thread() {
            public void run() {
                addListBusiness(global.BASE_URL + global.business + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "business");
            }
        }.start();
    }

    public void runMiscel() {
        new Thread() {
            public void run() {
                addListMiscell(global.BASE_URL + global.miscel + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "miscell");
            }
        }.start();
    }

    public void runInfluence() {
        new Thread() {
            public void run() {
                addListInfluence(global.BASE_URL + global.influence + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "influence");
            }
        }.start();
    }

    public void runEntertain() {
        new Thread() {
            public void run() {
                addListEntertain(global.BASE_URL + global.entertain + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "entertain");
            }
        }.start();
    }

    public void runInternational() {
        new Thread() {
            public void run() {
                addListInternational(global.BASE_URL + global.international + "?language=" + selectedlang + "&limit=" + datanews);
                Log.e("thread_", "international");
            }
        }.start();
    }

    public void updateTimeOnEachSecond() {
        //mute the audio player
        inflater_audio = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi_audio = inflater_audio.inflate(R.layout.item_card, null); //log.xml is your file.



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                c = Calendar.getInstance();
                hrs = c.get(Calendar.HOUR_OF_DAY);
                min = c.get(Calendar.MINUTE);
                sec = c.get(Calendar.SECOND);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (global.mediplayer_Setting == 1) {
//                            Log.e("timetime check", hrs + ":" + min + ":" + sec);
                            pp.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                            global.mediplayer_Setting = 0;

                        }


                        if (timenotify.equals(hrs + ":" + min + ":" + sec)) {
                            doitnow = 1;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doitnow = 0;
                                }
                            }, 2000);
                        }

                    }
                });
            }
        }, 0, 1000);
    }

    private List<ItemModel> addListPolitics(String url) {
        final List<ItemModel> items_politics = new ArrayList<>();
        setDataFromSharedPreferences(null, "politics");
        final List<String> check_duplicate_politics = new ArrayList<String>();

//        items_politics.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","politics"
//        ));


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            Log.e("response_politics :: ", response);

                            JSONArray array = new JSONArray(response);

                            //traversing through all the object

                            int sn = 0;
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);


                                if (items_politics.size() == 0) {
                                    setDataFromSharedPreferences(null, "Politics");
                                }

                                //adding the product to product list
                                if (selectedlang.equals(product.getString("language"))) {

                                    if (!check_duplicate_politics.contains(product.getString("longDesc"))) {
                                        check_duplicate_politics.add(product.getString("longDesc"));
                                        sn++;
                                        items_politics.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                        Collections.reverse(items_politics);
                                        setDataFromSharedPreferences(items_politics, "Politics");

                                        if (send_notification.contains("politics") && !skip.equals("skip")) {

                                            itemsreco.add(new ItemModel(
                                                    product.getString("title"),
                                                    product.getString("image"),
                                                    product.getString("shortDesc"),
                                                    product.getString("id"),
                                                    product.getString("sourceImage"),
                                                    product.getString("sourceName"),
                                                    product.getString("sourceViews"),
                                                    product.getString("longDesc"),
                                                    product.getString("videoId"),
                                                    product.getString("audioType"),
                                                    product.getString("converted"),
                                                    product.getString("date"),
                                                    product.getString("category")
                                            ));

//                                            Collections.reverse(itemsreco);
                                            setDataFromSharedPreferences(itemsreco, "Recommended");

                                            if (checkNoti == 0 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                            if (checkNoti == 1 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                        }

                                    }

                                }
                            }

                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_politics;
    }

    private List<ItemModel> addListTechnology(String url) {
        final List<ItemModel> items_tech = new ArrayList<>();
        final List<String> check_duplicate_tech = new ArrayList<String>();

//        items_tech.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","technology"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("response_Technology ", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
//                            Log.e("length_tech :: ", String.valueOf(array.length()));

                            //traversing through all the object
                            int sn = 0;
                            for (int i = 0; i < array.length(); i++) {


                                if (items_tech.size() == 0) {
                                    setDataFromSharedPreferences(null, "Technology");
                                }

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
//                                Log.e("vddsgratgy :: ", String.valueOf(product.get("language")));

                                if (selectedlang.equals(product.getString("language"))) {


                                    if (!check_duplicate_tech.contains(product.getString("longDesc"))) {
                                        check_duplicate_tech.add(product.getString("longDesc"));
                                        sn++;

                                        //adding the product to product list
                                        items_tech.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                        Collections.reverse(items_tech);
                                        setDataFromSharedPreferences(items_tech, "Technology");

                                        if (send_notification.contains("tech") && !skip.equals("skip")) {
                                            itemsreco.add(new ItemModel(
                                                    product.getString("title"),
                                                    product.getString("image"),
                                                    product.getString("shortDesc"),
                                                    product.getString("id"),
                                                    product.getString("sourceImage"),
                                                    product.getString("sourceName"),
                                                    product.getString("sourceViews"),
                                                    product.getString("longDesc"),
                                                    product.getString("videoId"),
                                                    product.getString("audioType"),
                                                    product.getString("converted"),
                                                    product.getString("date"),
                                                    product.getString("category")
                                            ));

//                                            Collections.reverse(itemsreco);
                                            setDataFromSharedPreferences(itemsreco, "Recommended");


                                            if (checkNoti == 0 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                            if (checkNoti == 1 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                        }

                                    }


                                }

                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_tech;
    }

    private List<ItemModel> addListBusiness(String url) {

        final List<ItemModel> items_business = new ArrayList<>();
        final List<String> check_duplicate_business = new ArrayList<String>();

//        items_business.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","business"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

//                            Log.e("response_Business ", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                if (items_business.size() == 0) {
                                    setDataFromSharedPreferences(null, "Business");
                                }


                                if (!check_duplicate_business.contains(product.getString("longDesc"))) {
                                    check_duplicate_business.add(product.getString("longDesc"));
                                    //adding the product to product list
                                    items_business.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date"),
                                            product.getString("category")
                                    ));

//                                        Collections.reverse(items_business);
                                    setDataFromSharedPreferences(items_business, "Business");

                                    if (send_notification.contains("business") && !skip.equals("skip")) {
                                        itemsreco.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                            Collections.reverse(itemsreco);
                                        setDataFromSharedPreferences(itemsreco, "Recommended");


                                        if (checkNoti == 0 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }


                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_business;
    }

    private List<ItemModel> addListEntertain(String url) {

        final List<ItemModel> items_entertaintment = new ArrayList<>();
        final List<String> check_duplicate_entertaintment = new ArrayList<String>();

//        items_entertaintment.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","entertaintment"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("response_entertain ", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object


                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                if (items_entertaintment.size() == 0) {
                                    setDataFromSharedPreferences(null, "Entertainment");
                                }


                                if (!check_duplicate_entertaintment.contains(product.getString("longDesc"))) {
                                    check_duplicate_entertaintment.add(product.getString("longDesc"));

                                    //adding the product to product list
                                    items_entertaintment.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date"),
                                            product.getString("category")
                                    ));

//                                        Collections.reverse(items_entertaintment);
                                    setDataFromSharedPreferences(items_entertaintment, "Entertainment");

                                    if (send_notification.contains("entertain") && !skip.equals("skip")) {
                                        itemsreco.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                            Collections.reverse(itemsreco);
                                        setDataFromSharedPreferences(itemsreco, "Recommended");


                                        if (checkNoti == 0 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }
                                }


                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        return items_entertaintment;
    }

    private List<ItemModel> addListSport(String url) {

        final List<ItemModel> items_sports = new ArrayList<>();
        final List<String> check_duplicate_sports = new ArrayList<String>();

//        items_sports.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","sports"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("response_sports ", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object

                            int sn = 0;
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                //adding the product to product list
                                if (items_sports.size() == 0) {
                                    setDataFromSharedPreferences(null, "Sports");
                                }


                                if (!check_duplicate_sports.contains(product.getString("longDesc"))) {
                                    check_duplicate_sports.add(product.getString("longDesc"));
                                    sn++;
                                    //adding the product to product list
                                    items_sports.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date"),
                                            product.getString("category")

                                    ));

//                                        Collections.reverse(items_sports);
                                    setDataFromSharedPreferences(items_sports, "Sports");

                                    if (send_notification.contains("sport") && !skip.equals("skip")) {

                                        itemsreco.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                            Collections.reverse(itemsreco);
                                        setDataFromSharedPreferences(itemsreco, "Recommended");


                                        if (checkNoti == 0 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }


                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_sports;
    }

    private List<ItemModel> addListStartup(String url) {

        final List<ItemModel> items_startup = new ArrayList<>();
        final List<String> check_duplicate_startup = new ArrayList<String>();

//        items_startup.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","startup"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("response_startup", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object

                            int sn = 0;
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                if (items_startup.size() == 0) {
                                    setDataFromSharedPreferences(null, "Startup");
                                }
                                //adding the product to product list

                                if (selectedlang.equals(product.getString("language"))) {

                                    if (!check_duplicate_startup.contains(product.getString("longDesc"))) {
                                        check_duplicate_startup.add(product.getString("longDesc"));
                                        sn++;
                                        //adding the product to product list
                                        items_startup.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")

                                        ));

//                                        Collections.reverse(items_startup);
                                        setDataFromSharedPreferences(items_startup, "Startup");

                                        if (send_notification.contains("startup") && !skip.equals("skip")) {
                                            itemsreco.add(new ItemModel(
                                                    product.getString("title"),
                                                    product.getString("image"),
                                                    product.getString("shortDesc"),
                                                    product.getString("id"),
                                                    product.getString("sourceImage"),
                                                    product.getString("sourceName"),
                                                    product.getString("sourceViews"),
                                                    product.getString("longDesc"),
                                                    product.getString("videoId"),
                                                    product.getString("audioType"),
                                                    product.getString("converted"),
                                                    product.getString("date"),
                                                    product.getString("category")
                                            ));

//                                            Collections.reverse(itemsreco);
                                            setDataFromSharedPreferences(itemsreco, "Recommended");


                                            if (checkNoti == 0 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                            if (checkNoti == 1 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                        }

                                    }


                                }


                            }
                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        return items_startup;
    }

    private List<ItemModel> addListInternational(String url) {

        final List<ItemModel> items_international = new ArrayList<>();
        final List<String> check_duplicate_international = new ArrayList<String>();

//        items_international.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","international"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("response_international ", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object

                            int sn = 0;
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);


                                if (items_international.size() == 0) {
                                    setDataFromSharedPreferences(null, "International");
                                }
                                //adding the product to product list
                                if (selectedlang.equals(product.getString("language"))) {

                                    if (!check_duplicate_international.contains(product.getString("longDesc"))) {
                                        check_duplicate_international.add(product.getString("longDesc"));
                                        sn++;
                                        items_international.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                        Collections.reverse(items_international);
                                        setDataFromSharedPreferences(items_international, "International");

                                        if (send_notification.contains("international") && !skip.equals("skip")) {

                                            itemsreco.add(new ItemModel(
                                                    product.getString("title"),
                                                    product.getString("image"),
                                                    product.getString("shortDesc"),
                                                    product.getString("id"),
                                                    product.getString("sourceImage"),
                                                    product.getString("sourceName"),
                                                    product.getString("sourceViews"),
                                                    product.getString("longDesc"),
                                                    product.getString("videoId"),
                                                    product.getString("audioType"),
                                                    product.getString("converted"),
                                                    product.getString("date"),
                                                    product.getString("category")
                                            ));

//                                            Collections.reverse(itemsreco);
                                            setDataFromSharedPreferences(itemsreco, "Recommended");

                                            if (checkNoti == 0 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                            if (checkNoti == 1 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                        }


                                    }


                                }

                            }

                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_international;
    }

    private List<ItemModel> addListInfluence(String url) {

        final List<ItemModel> items_influencer = new ArrayList<>();
        final List<String> check_duplicate_influence = new ArrayList<String>();

//        items_influencer.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0",
//                "0","29/2/2021","influence"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("response_influence ", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object

                            int sn = 0;
                            for (int i = 0; i < array.length(); i++) {

                                if (items_influencer.size() == 0) {
                                    setDataFromSharedPreferences(null, "Influencer");
                                }

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                //adding the product to product list
                                if (selectedlang.equals(product.getString("language"))) {


                                    if (!check_duplicate_influence.contains(product.getString("longDesc"))) {
                                        check_duplicate_influence.add(product.getString("longDesc"));
                                        sn++;
                                        items_influencer.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                        Collections.reverse(items_influencer);
                                        setDataFromSharedPreferences(items_influencer, "Influencer");

                                        if (send_notification.contains("influence") && !skip.equals("skip")) {

                                            itemsreco.add(new ItemModel(
                                                    product.getString("title"),
                                                    product.getString("image"),
                                                    product.getString("shortDesc"),
                                                    product.getString("id"),
                                                    product.getString("sourceImage"),
                                                    product.getString("sourceName"),
                                                    product.getString("sourceViews"),
                                                    product.getString("longDesc"),
                                                    product.getString("videoId"),
                                                    product.getString("audioType"),
                                                    product.getString("converted"),
                                                    product.getString("date"),
                                                    product.getString("category")
                                            ));

//                                            Collections.reverse(itemsreco);
                                            setDataFromSharedPreferences(itemsreco, "Recommended");

                                            if (checkNoti == 0 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                            if (checkNoti == 1 && doitnow == 1) {
                                                notificationFunction(product.getString("sourceName"),
                                                        product.getString("title"));
                                            }
                                        }
                                    }


                                }

                            }

                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        return items_influencer;
    }

    private List<ItemModel> addListMiscell(String url) {

        final List<ItemModel> items_miscell = new ArrayList<>();
        final List<String> check_duplicate_miscell = new ArrayList<String>();

//        items_miscell.add(new ItemModel("title",link,"PapaNews","",link,
//                "times","20","PapaNews","0","0","0","29/2/2021","miscel"
//        ));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

//                            Log.e("response_miscellaneous ", response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object

                            int sn = 0;
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                if (items_miscell.size() == 0) {
                                    setDataFromSharedPreferences(null, "Miscellaneous");
                                }

                                //adding the product to product list


                                if (!check_duplicate_miscell.contains(product.getString("longDesc"))) {
                                    check_duplicate_miscell.add(product.getString("longDesc"));
                                    sn++;
                                    items_miscell.add(new ItemModel(
                                            product.getString("title"),
                                            product.getString("image"),
                                            product.getString("shortDesc"),
                                            product.getString("id"),
                                            product.getString("sourceImage"),
                                            product.getString("sourceName"),
                                            product.getString("sourceViews"),
                                            product.getString("longDesc"),
                                            product.getString("videoId"),
                                            product.getString("audioType"),
                                            product.getString("converted"),
                                            product.getString("date"),
                                            product.getString("category")
                                    ));

//                                        Collections.reverse(items_miscell);
                                    setDataFromSharedPreferences(items_miscell, "Miscellaneous");

                                    if (send_notification.contains("miscell") && !skip.equals("skip")) {

                                        itemsreco.add(new ItemModel(
                                                product.getString("title"),
                                                product.getString("image"),
                                                product.getString("shortDesc"),
                                                product.getString("id"),
                                                product.getString("sourceImage"),
                                                product.getString("sourceName"),
                                                product.getString("sourceViews"),
                                                product.getString("longDesc"),
                                                product.getString("videoId"),
                                                product.getString("audioType"),
                                                product.getString("converted"),
                                                product.getString("date"),
                                                product.getString("category")
                                        ));

//                                            Collections.reverse(itemsreco);
                                        setDataFromSharedPreferences(itemsreco, "Recommended");

                                        if (checkNoti == 0 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                        if (checkNoti == 1 && doitnow == 1) {
                                            notificationFunction(product.getString("sourceName"),
                                                    product.getString("title"));
                                        }
                                    }

                                }

                            }


                            //creating adapter object and setting it to recyclerview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        return items_miscell;
    }

    @SuppressLint("WrongConstant")
    private void notificationFunction(String title, String Short) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(DashBoard.this, "notify");
        builder.setPriority(NotificationManager.IMPORTANCE_NONE);
        builder.setContentTitle(title);
        builder.setContentText(Short);
        builder.setBadgeIconType(R.drawable.main_logo);
        builder.setSmallIcon(R.drawable.main_logo);
        builder.setCategory("Politics");
        builder.setAutoCancel(true);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DashBoard.class), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(DashBoard.this);
        managerCompat.notify(check, builder.build());
        check++;

    }

    private void setDataFromSharedPreferences(List<ItemModel> politicsnews, String datatype) {
        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(politicsnews);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("urlData", Context.MODE_PRIVATE);
        editor_list = sharedPref.edit();

        editor_list.putString(datatype, jsonCurProduct);
        editor_list.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        relativeLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        relativeLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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
        hideSystemUI();
    }

    //    private void addListReco() {
//        for (int k = 0; k < recomend.size(); k++) {
//            Log.e("recomended array :: ", String.valueOf(recomend.get(k)));
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, recomend.get(k),
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Map<String, String> notiFirst = new HashMap<String, String>();
//                            try {
//                                //converting the string to json array object
//                                array = new JSONArray(response);
//                                Log.e("response :: ", String.valueOf(response));
//                                Log.e("xyz123123 :: ", String.valueOf(array.length()));
//                                //traversing through all the object
//                                for (int i = 0; i < array.length(); i++) {
//
//                                    //getting product object from json array
//                                    JSONObject product = array.getJSONObject(i);
//
//                                    //adding the product to product list
//                                    Log.e("papanewspre one :: ", String.valueOf(itemsreco));
//                                    Log.e("itemsrecomended", String.valueOf(itemsreco.size()));
//
//                                    if (itemsreco.size() == 0) {
//                                        setDataFromSharedPreferences(null, "recomended");
//                                    }
//
//                                    if (selectedlang.equals(product.getString("language"))) {
//                                        Log.e("duplicate out:: ", String.valueOf(check_duplicate));
//                                        if (!check_duplicate.contains(product.getString("longDesc"))) {
//                                            check_duplicate.add(product.getString("longDesc"));
//
//                                            Log.e("duplicate In:: ", String.valueOf(check_duplicate));
//
//                                            itemsreco.add(new ItemModel(
//                                                    product.getString("title"),
//                                                    product.getString("image"),
//                                                    product.getString("shortDesc"),
//                                                    product.getString("id"),
//                                                    product.getString("sourceImage"),
//                                                    product.getString("sourceName"),
//                                                    product.getString("sourceViews"),
//                                                    product.getString("longDesc"),
//                                                    product.getString("videoId"),
//                                                    product.getString("audioType"),
//                                                    product.getString("converted"),
//                                                    product.getString("date")
//
//                                            ));
//
//                                            Collections.reverse(itemsreco);
//                                            setDataFromSharedPreferences(itemsreco, "recomended");
//                                        }
//                                    }
//
//                                    Log.e("dhurval final :: ", String.valueOf(product));
//
//                                    notiFirst.put("image", product.getString("image"));
//                                    notiFirst.put("title", product.getString("title"));
//                                    notiFirst.put("shortDesc", product.getString("shortDesc"));
//                                    notiFirst.put("id", product.getString("id"));
//                                    notiFirst.put("sourceImage", product.getString("sourceImage"));
//                                    notiFirst.put("sourceName", product.getString("sourceName"));
//                                    notiFirst.put("sourceViews", product.getString("sourceViews"));
//                                    notiFirst.put("longDesc", product.getString("longDesc"));
//                                    notiFirst.put("videoId", product.getString("videoId"));
//                                    notiFirst.put("audio", product.getString("audioType"));
//                                    global.notificationArray.add(notiFirst);
//
//                                    Log.e("notification check :: ", String.valueOf(global.notificationArray));
//                                    Log.e("notification length :: ", String.valueOf(global.notificationArray.size()));
//                                    Log.e("notification inside :: ",
//                                            String.valueOf(global.notificationArray.get(0).get("image")));
//
//
//                                }
//
//
//                                //creating adapter object and setting it to recyclerview
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                        }
//                    });
//            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
//        }
//    }
    Intent intent;
    PendingIntent pendingIntent,prevPendingIntent,playPendingIntent,nextPendingIntent;
    Intent prevIntent,playIntent,nextIntent;
    MediaSessionCompat mediaSessionCompat;
    private MediaSessionCompat mediaSession;
    private NotificationManagerCompat notificationManager;


//    public void showNotification(Context context,int playButton, String title, String shti, String image) {
//        intent = new Intent(context, NewsFragment2.class);
//        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        Picasso.get().load(image).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//                try{
//                    mediaSessionCompat = new MediaSessionCompat(DashBoard.this, "PlayerAudio");
//                    notificationManager = NotificationManagerCompat.from(DashBoard.this);
//                    mediaSession = new MediaSessionCompat(DashBoard.this, "tag");
//                    // loaded bitmap is here (bitmap)
//                    prevIntent = new Intent(context, NotificationReciever.class).setAction(ACTION_PREVIOUS);
//                    prevPendingIntent = PendingIntent.getBroadcast(context, 0,
//                            prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    playIntent = new Intent(context, NotificationReciever.class).setAction(ACTION_PLAY);
//                    playPendingIntent = PendingIntent.getBroadcast(context, 0,
//                            playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    nextIntent = new Intent(context, NotificationReciever.class).setAction(ACTION_NEXT);
//                    nextPendingIntent = PendingIntent.getBroadcast(context, 0,
//                            nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//                    Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_2)
//                            .setSmallIcon(R.drawable.main_logo)
//                            .setLargeIcon(bitmap)
//                            .setContentTitle("mo")
//                            .setContentText("moo")
//                            .addAction(R.drawable.ic_baseline_navigate_before_24, "Previous", prevPendingIntent)
//                            .addAction(playButton, "Play", playPendingIntent)
//                            .addAction(R.drawable.ic_baseline_navigate_next_24, "Next", nextPendingIntent)
//                            .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
//                                    .setMediaSession(mediaSessionCompat.getSessionToken()))
//                            .setPriority(NotificationCompat.PRIORITY_LOW)
//                            .setOnlyAlertOnce(true)
//                            .build();
//
//                    NotificationManager notificationManager = (NotificationManager)
//                            context.getSystemService(NOTIFICATION_SERVICE);
//
//                    notificationManager.notify(0, notification);
//                }catch (Exception e){
//
//                }
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        });
//
//
//    }
//private void sendNotification(String title, String messageBody, String imageUrl) {
//    NotificationManagerCompat notificationManager;
//    notificationManager = NotificationManagerCompat.from(this);
//    RemoteViews collapsedView = new RemoteViews(getPackageName(),
//            R.layout.notification_collapsed);
//    @SuppressLint("RemoteViewLayout") RemoteViews expandedView = new RemoteViews(getPackageName(),
//            R.layout.notification_expanded);
//    Intent clickIntent = new Intent(this, MyFirebaseMessagingService.class);
//    PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
//            0, clickIntent, 0);
//    collapsedView.setTextViewText(R.id.text_view_collapsed_1, title);
////    try {
////        URL url = new URL(imageUrl);
////
////        InputStream in;
////        //message = params[0] + params[1];
////
////        //URL url = new URL(params[2]);
////        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////        connection.setDoInput(true);
////        connection.connect();
////        in = connection.getInputStream();
////        Bitmap image = BitmapFactory.decodeStream(in);
////            Drawable d = new BitmapDrawable(getResources(), image);
////            URL url = new URL(imageUrl);
////        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
////        expandedView.setImageViewBitmap(R.id.image_view_expanded, image);
//                expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.main_logo);
////
////    } catch (MalformedURLException e) {
////        e.printStackTrace();
////        Log.e("noti_error", String.valueOf(e));
////    } catch (IOException e) {
////        e.printStackTrace();
////        Log.e("noti_error", String.valueOf(e));
////    }
//
//
//    expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);
//    Notification notification = new NotificationCompat.Builder(this, "CHANNEL_1")
//            .setSmallIcon(R.drawable.main_logo)
//            .setCustomContentView(collapsedView)
//            .setCustomBigContentView(expandedView)
////            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//            .build();
//    notificationManager.notify(1, notification);
//    Log.e("noti_sended", String.valueOf("noti"));
//
//}
}
