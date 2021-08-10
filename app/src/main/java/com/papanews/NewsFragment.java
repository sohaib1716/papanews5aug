package com.papanews;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.papanews.App.CHANNEL_ID_1;
import static com.papanews.ApplicationClass.ACTION_NEXT;
import static com.papanews.ApplicationClass.ACTION_PLAY;
import static com.papanews.ApplicationClass.ACTION_PREVIOUS;
import static com.papanews.ApplicationClass.CHANNEL_ID_2;
import static com.papanews.global.proper_audio;
import static com.papanews.global.proper_image;
import static com.papanews.global.proper_newws;
import static com.papanews.global.proper_title;

public class
NewsFragment extends Fragment implements CardStackListener, SwipeRefreshLayout.OnRefreshListener, ActionPlaying, ServiceConnection {

    private static final String TAG = "Technology";
    String frag_shared, views_link;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    List<ItemModel> items;



    Drawable drawable;
    List<ItemModel> productFromShared = new ArrayList<>();
    String jsonPreferences;
    SharedPreferences sharedpreferences;
    int view_update = 0;
    int x = 0;
    String share_Tilte;
    int positionchange = 0;


    JSONObject product;
    String hashMapString;

    Drawable drawable2;
    int sh;

    MaterialTextView normal,title, subtitle, news,daten;
    ImageView rewind_view,imageView, share,fl, sav, shr,pause, forward, play, playimage;
    CardStackView cardStackView;

    SharedPreferences.Editor editor;
    int fromswipe = 0;


    //Audio player media

    int flag, flagflag = 0;


    //    notification
    private NotificationManagerCompat notificationManager;
    private MediaSessionCompat mediaSession;
    PendingIntent btPendingIntent;


    String gettitle, getimage, getnews;

    String url;



    Mediacontrol mediacontrol = new Mediacontrol(getContext());
    String stst = "";
    GifImageView refreshScreen;


    SwipeRefreshLayout swipeRefreshLayout;

    MusicService musicService;
    MediaSessionCompat mediaSessionCompat;
    FirebaseAnalytics mFirebaseAnalytics;

    Intent intent;
    PendingIntent pendingIntent,prevPendingIntent,playPendingIntent,nextPendingIntent;
    Intent prevIntent,playIntent,nextIntent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.item_card, container,false);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        if(global.ppp == 0){
//            global.ppp = 1;

        rewind_view = view.findViewById(R.id.rewind);
//        submit_report = view.findViewById(R.id.cv);
        normal = view.findViewById(R.id.nrmlText);
        news = view.findViewById(R.id.nonews);
        daten = view.findViewById(R.id.datenews);
        refreshScreen = view.findViewById(R.id.refresh);
        //        Audio player media
        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);
        playimage = view.findViewById(R.id.playimage);
        title = view.findViewById(R.id.audioTitlle);
        subtitle = view.findViewById(R.id.audioSub);
        forward = view.findViewById(R.id.forward);


        //music service
        mediaSessionCompat = new MediaSessionCompat(getActivity(), "PlayerAudio");

        //medicontroller
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(android.R.color.black,
                android.R.color.white,
                android.R.color.darker_gray,
                android.R.color.black);

        title.setSelected(true);

        frag_shared = getArguments().getString("frag_shared", "");

//        views_link = getArguments().getString("views_link","");


        //        Notification manager
        notificationManager = NotificationManagerCompat.from(getActivity());
        mediaSession = new MediaSessionCompat(getActivity(), "tag");
        imageView = view.findViewById(R.id.doodle);
        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.bgreco);
        imageView.setImageDrawable(drawable);


        if (String.valueOf(addList()).equals("null")) {
            refreshScreen.setVisibility(View.VISIBLE);
            news.setVisibility(View.VISIBLE);
            daten.setVisibility(View.GONE);
            title.setText("No Audio Available");
            subtitle.setVisibility(View.GONE);
        } else if (!String.valueOf(addList()).equals("null")) {
            subtitle.setVisibility(View.VISIBLE);
            daten.setVisibility(View.VISIBLE);
            refreshScreen.setVisibility(View.GONE);
            manager = new CardStackLayoutManager(getContext(), this);
            adapter = new CardStackAdapter(addList(), getContext());
            cardStackView = view.findViewById(R.id.card_stack_view);

            manager.setTranslationInterval(12.0f);
            manager.setDirections(Direction.VERTICAL);
            manager.setStackFrom(StackFrom.Top);
            manager.setVisibleCount(3);
            manager.setScaleInterval(0.95f);
            manager.setSwipeThreshold(0.1f);
            manager.setMaxDegree(20.0f);
            manager.setCanScrollHorizontal(false);
            manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
            news.setVisibility(View.GONE);
            cardStackView.setLayoutManager(manager);
            cardStackView.setAdapter(adapter);
        }
        rewind_view.bringToFront();
        rewind_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                RewindAnimationSetting settings = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(10000)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();


                manager = new CardStackLayoutManager(getContext(), NewsFragment.this);
                adapter = new CardStackAdapter(addList(), getContext());
                cardStackView = view.findViewById(R.id.card_stack_view);
                cardStackView.setLayoutManager(manager);
                manager.setTranslationInterval(12.0f);
                manager.setDirections(Direction.VERTICAL);
                manager.setStackFrom(StackFrom.Top);
                manager.setVisibleCount(3);
                manager.setScaleInterval(0.95f);
                manager.setSwipeThreshold(0.1f);
                manager.setMaxDegree(80.0f);
                manager.setCanScrollHorizontal(false);
                manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                cardStackView.rewind();
                rewind_view.setVisibility(View.GONE);
                normal.setVisibility(View.GONE);

                cardStackView.setAdapter(adapter);
                manager.setRewindAnimationSetting(settings);

            }
        });
//    }

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

//            try {
//                mediacontrol.media_load("");
//                play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//            } catch (Exception e) {
//                Log.e("catch error ", String.valueOf(e));
//            }

            Log.e("visibleto ", String.valueOf(isVisibleToUser));
            Log.e("visibleto ", String.valueOf(frag_shared));
        }
    }
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("sender reciever", "Got message: " + message);
        }
    };

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myReceiver);
        super.onDestroyView();
    }


    @SuppressLint("LongLogTag")
    private List<ItemModel> addList() {

        String shred = frag_shared;
        Log.e("frag_shared", frag_shared + "123");
        Gson gson = new Gson();
        SharedPreferences sharedPref = getContext().getSharedPreferences("urlData", MODE_PRIVATE);
        jsonPreferences = sharedPref.getString(shred, "");

        Type type = new TypeToken<List<ItemModel>>() {
        }.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        Log.e("productFromShared sohaib :: ", frag_shared + "  -   " + String.valueOf(jsonPreferences));

        return productFromShared;
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkIfSaved(String image, String title, String id, String shdiscprition, String source_image, String source_name,
                              String source_views, String long_disc, String audio, String videoOfYOUTUBE) {
        Map<String, String> myMap1 = new HashMap<String, String>();
        myMap1.put("image", image);
        myMap1.put("title", title);
        myMap1.put("shortDesc", shdiscprition);
        myMap1.put("id", id);
        myMap1.put("sourceImage", source_image);
        myMap1.put("sourceName", source_name);
        myMap1.put("audio", audio);
        myMap1.put("sourceViews", (source_views));
        myMap1.put("longDesc", long_disc);
        myMap1.put("videoId", videoOfYOUTUBE);
        Log.e("checking myMap :: ", String.valueOf(myMap1));
        Log.e("checking global :: ", String.valueOf(global.myMap));


        if (global.myMap.contains(myMap1)) {
            Log.e("cgecking checking :: ", "Yess done done");
            drawable2 = ContextCompat.getDrawable(getActivity(), R.drawable.bookmark);
            sav.setImageDrawable(drawable2);


        } else {
            drawable2 = ContextCompat.getDrawable(getActivity(), R.drawable.bookwhite);
            sav.setImageDrawable(drawable2);

        }

    }


    private void addDataToDatabase(final String Views, final String imageId, String category) {

        switch (category) {
            case "startup":
                url = global.updviews + "Startup.php";
                break;
            case "business":
                url = global.updviews + "Business.php";
                break;
            case "entertain":
                url = global.updviews + "Startup.php";
                break;
            case "influence":
                url = global.updviews + "Influence.php";
                break;
            case "international":
                url = global.updviews + "International.php";
                break;
            case "miscel":
                url = global.updviews + "Miscel.php";
                break;
            case "politics":
                url = global.updviews + "Politics.php";
                break;
            case "sports":
                url = global.updviews + "Sports.php";
                break;
            case "technology":
                url = global.updviews + "Tech.php";
                break;
        }


        // creating a new variable for our request queue

        if (url != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("TAG", "RESPONSE IS " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {

                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("sourceViews", Views);
                    params.put("image", imageId);
                    return params;
                }
            };

            queue.add(request);

        }
    }


    @Override
    public void onCardDisappeared(View view, int position) {

    }
//    public static Fragment newInstance(String frag_shared, String views_link) {
//        Technology f = new Technology();
//        f.frag_shared = frag_shared;
//        f.views_link = views_link;
//        Log.e("shared_data",frag_shared +" , "+ views_link);
//        return f;
//    }


    @Override
    public void onCardDragging(Direction direction, float ratio) { }

    @Override
    public void onCardSwiped(Direction direction) {

//        if (manager.getTopPosition() == 8) {
//            paginate();
//        }

        if (manager.getTopPosition() == adapter.getItemCount()) {
            daten.setText("");
            // -------------------- last position reached, do something ---------------------
            if (!global.mMediaPlayernew.isPlaying()) {
                play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                Drawable drawablenew = ContextCompat.getDrawable(getActivity(), R.drawable.noimage);
                playimage.setImageDrawable(drawablenew);
                title.setText("No Audio Available");
                subtitle.setText("");
            }


            rewind_view.setVisibility(View.VISIBLE);
            normal.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onCardRewound() { }
    @Override
    public void onCardCanceled() { }
    @Override
    public void onCardAppeared(final View view, final int position) {

        sh = 0;
        sav = view.findViewById(R.id.save_card);
        shr = view.findViewById(R.id.share_card);
        share = view.findViewById(R.id.imageimage);
        JSONArray array = null;


        try {
            array = new JSONArray(jsonPreferences);
            product = array.getJSONObject(position);


            daten.setText(parseDateToddMMyyyy(product.getString("date")));


            getimage = product.getString("image");
            getnews = product.getString("sourcename");
            gettitle = product.getString("title");


            Picasso.get().load(product.getString("image")).placeholder(R.drawable.noimage)
                    .resize(250, 250)
                    .into(playimage);

            title.setText(product.getString("title"));
            subtitle.setText(product.getString("sourcename"));


            Log.e("buhuhuhu_c :: ", product.getString("category"));


            global.mMediaPlayernew.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("swipydw 0", String.valueOf(fromswipe));
                    cardStackView.swipe();
                }
            });

            if (flagflag == 1) {
                play.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                mediacontrol.media_load(product.getString("converted"));
                mediacontrol.media_play();
            } else if (flagflag == 0) {
                play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            }


            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardStackView.swipe();
                }
            });
            proper_audio = product.getString("converted");
            proper_title = product.getString("title");
            proper_newws = product.getString("sourcename");
            proper_image = product.getString("image");
//            ((DashBoard)getActivity()).showNotification(getContext(),R.drawable.ic_baseline_play_arrow_24,
//                    proper_title, proper_newws, proper_image);

            play.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {

                    Log.e("getsempty_a ", proper_audio + "1234");
                    Log.e("getsempty_t ", proper_title + "1234");
                    Log.e("getsempty_new ", proper_newws + "1234");

                    playClicked(TAG);

                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {
                    Log.e("gfdsfsfs :: ", String.valueOf(flag));
                    if (flag == 1) {
                        try {
                            play.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                            mediacontrol.media_load(product.getString("converted"));
                            mediacontrol.media_play();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        flag = 0;
                        flagflag = 1;
                    } else if (flag == 0) {
                        rewind_view.setVisibility(View.GONE);
                        normal.setVisibility(View.GONE);
                        cardStackView.rewind();
                    }
                }
            });


            checkIfSaved(product.getString("image"), product.getString("title"), product.getString("longd")
                    , product.getString("shortd"), product.getString("sourceimage"),
                    product.getString("sourcename"), product.getString("Views"),
                    product.getString("LongText"), product.getString("audioType"), product.getString("video"));


            sav.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {
                    Map<String, String> myMap1 = new HashMap<String, String>();
                    try {
                        myMap1.put("image", product.getString("image"));
                        myMap1.put("title", product.getString("title"));
                        myMap1.put("shortDesc", product.getString("shortd"));
                        myMap1.put("id", product.getString("longd"));
                        myMap1.put("sourceImage", product.getString("sourceimage"));
                        myMap1.put("sourceName", product.getString("sourcename"));
                        myMap1.put("sourceViews", product.getString("Views"));
                        myMap1.put("longDesc", product.getString("LongText"));
                        myMap1.put("videoId", product.getString("video"));
                        myMap1.put("audio", product.getString("audioType"));
                        Log.e("check CHECK :: ", String.valueOf(hashMapString));

                        if (global.myMap.contains(myMap1)) {
                            drawable2 = ContextCompat.getDrawable(getActivity(), R.drawable.bookwhite);
                            sav.setImageDrawable(drawable2);
//                            Toast.makeText(getActivity(), "Remove saved", Toast.LENGTH_SHORT).show();
//                            sav.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            global.myMap.remove(myMap1);
                            Gson gson = new Gson();
                            hashMapString = gson.toJson(global.myMap);
                            Log.e("myMap :: ", String.valueOf(global.myMap));
                            Log.e("position :: ", String.valueOf(jsonPreferences));
                            SharedPreferences prefs = getContext().getSharedPreferences("saveDataPush", MODE_PRIVATE);
                            prefs.edit().putString("savedData", hashMapString).apply();
                            prefs.edit().putInt("bookmark", sh).apply();
                            Log.e("storing data :: ", String.valueOf(hashMapString));
                            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "Save_card");
                            mFirebaseAnalytics.logEvent("Save", bundle);
                        } else {
                            sh = 2;
                            drawable2 = ContextCompat.getDrawable(getActivity(), R.drawable.bookmark);
                            sav.setImageDrawable(drawable2);
//                            sav.setBackgroundColor(Color.parseColor("#F4931D"));
//                            Toast.makeText(getActivity(), "Saving first time", Toast.LENGTH_SHORT).show();
                            global.myMap.add(myMap1);
                            Log.e("Lappas save :: ", String.valueOf(global.myMap));
                            Gson gson = new Gson();
                            hashMapString = gson.toJson(global.myMap);
                            Log.e("myMap :: ", String.valueOf(global.myMap));
                            Log.e("position :: ", String.valueOf(jsonPreferences));
                            SharedPreferences prefs = getContext().getSharedPreferences("saveDataPush", MODE_PRIVATE);
                            prefs.edit().putString("savedData", hashMapString).apply();
                            prefs.edit().putInt("bookmark", sh).apply();
                            Log.e("storing data :: ", String.valueOf(hashMapString));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            shr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dexter.withActivity(getActivity())
                            .withPermission(READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {

                                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
                                    Bundle bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.METHOD, "Share_card");
                                    mFirebaseAnalytics.logEvent("Share", bundle);
                                    BitmapDrawable drawable = (BitmapDrawable) share.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();

                                    String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);

                                    Uri uri = Uri.parse(bitmapPath);
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/jpg");
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    try {
                                        intent.putExtra(Intent.EXTRA_TEXT, product.getString("title") +
                                                "\nFor more updates download:" +
                                                "\n" + getString(R.string.app_link));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(Intent.createChooser(intent, "Share"));

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

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global.mMediaPlayernew.reset();
                play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);

                SharedPreferences preferences = getActivity().getSharedPreferences("passScreen", MODE_PRIVATE);//Frequent to get SharedPreferences need to add a step getActivity () method
                SharedPreferences.Editor editor = preferences.edit();
                try {
                    editor.putString("shdisc", product.getString("shortd"));
                    editor.putString("title", product.getString("title"));
                    editor.putString("srcName", product.getString("sourcename"));
                    editor.putString("srcImage", product.getString("sourceimage"));
                    editor.putString("longDisc", product.getString("LongText"));
                    editor.putString("video_youtube", product.getString("video"));
                    editor.putString("image", product.getString("image"));
                    editor.putString("audioty", product.getString("audioType"));
                    editor.putString("id", product.getString("longd"));
                    editor.putString("audio", product.getString("audioType"));
                    editor.putString("converted", product.getString("converted"));
                    editor.putString("date", product.getString("date"));

                    share_Tilte = product.getString("title");
                    view_update = Integer.parseInt(product.getString("Views")) + 1;
                    editor.putString("srcViews", String.valueOf(view_update));
                    addDataToDatabase(String.valueOf(view_update), product.getString("image"), product.getString("category"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("position :: ", String.valueOf(jsonPreferences));
                SharedPreferences prefs = getContext().getSharedPreferences("saveDataPush", MODE_PRIVATE);
                prefs.edit().putString("savedData", hashMapString).apply();
                Log.e("sohaib sohaib :: ", String.valueOf(hashMapString));
                editor.commit();
                Intent i = new Intent(getActivity(), News_page.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MusicService.news_name = TAG;
        Intent ik = new Intent(getActivity(), MusicService.class);
        getContext().bindService(ik, NewsFragment.this, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unbindService(NewsFragment.this);
    }

    @Override
    public void onRefresh() {
        Intent i = new Intent(getActivity(), DashBoard.class);
        startActivity(i);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void nextClicked(String news_name) {
        Log.e("News_name_fragment :: ", "NEXT - "+ news_name + "action_frag - "+frag_shared);

        if(news_name.equals(TAG)) {
            cardStackView.swipe();
        }
    }

    @Override
    public void previousClicked(String news_name) {
        Log.e("News_name_fragment :: ", "previous - "+ news_name + "action_frag - "+frag_shared);

        if(news_name.equals(TAG)) {
            cardStackView.rewind();
        }
    }

    @Override
    public void playClicked(String news_name) {
        Log.e("News_name_fragment :: ", "play - "+ news_name + "action_frag - "+frag_shared);

        if(news_name.equals(TAG)) {
            Log.e("getsempty_a ", proper_audio + "1234");
            Log.e("getsempty_t ", proper_title + "1234");
            Log.e("getsempty_new ", proper_newws + "1234");

            try {
                Log.e("gfdsfsfs :: ", String.valueOf(mediacontrol.media_is_playing()));
                if (mediacontrol.media_is_playing()) {
                    try {
                        mediacontrol.media_pause();
                        flagflag = 0;
                        play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//                        ((DashBoard) getActivity()).showNotification(getContext(), R.drawable.ic_baseline_play_arrow_24,
//                                proper_title, proper_newws, proper_image);
//                    sendNotification(proper_title, proper_newws, proper_image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        mediacontrol.media_load(proper_audio);
                        mediacontrol.media_play();
                        flag = 1;
                        flagflag = 1;
                        play.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                        ((DashBoard) getActivity()).showNotification(getContext(), R.drawable.ic_baseline_pause_24,
//                                proper_title, proper_newws, proper_image);
//                    sendNotification(proper_title, proper_newws, proper_image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void card_next(String news_name) {
        Log.e("News_name_fragment :: ", "NEXT - "+ news_name + "action_frag - "+frag_shared);

        if(news_name.equals(TAG)) {
            cardStackView.swipe();
            Log.e("nexting", "in frag ");
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
        musicService.setCallBack(NewsFragment.this);
        Log.e("Connecteddd ", musicService + "");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
        Log.e("disConnecteddd ", musicService + "");
    }





    public void sendNotification(final String title, final String text, String image) {

        Picasso.get().load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)
                Intent buttonIntent = new Intent(getActivity(), MediaPlayerService.class);
                buttonIntent.putExtra("notificationId", 1);
                btPendingIntent = PendingIntent.getBroadcast(getContext(), 1, buttonIntent, 0);


                Notification channel = new NotificationCompat.Builder(getContext(), CHANNEL_ID_1)
                        .setSmallIcon(R.drawable.main_logo)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setLargeIcon(bitmap)
                        .addAction(R.drawable.ic_baseline_cancel_24, "DISMISS", btPendingIntent)
                        .build();
                notificationManager.notify(1, channel);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

    }

    void paginate() {
        List<ItemModel> old = adapter.getItems();
        List<ItemModel> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd MMMM yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            assert date != null;
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
