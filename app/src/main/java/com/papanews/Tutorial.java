package com.papanews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class Tutorial extends AppCompatActivity {

    ImageView tutoMimage, play_tutorial;
    MaterialButton skip;
    MaterialTextView titleTuto, subTuto, ttaudio,tutorialDesc;
    RelativeLayout relativre_touch,musicPlayer;
    CardStackView cardStackView;
    GifImageView gif, taphere, playGif, audioGif, news, more;

    int firstTime;
    private static final String TAG = "MainActivity";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    int flag = 1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        hideSystemUI();

        musicPlayer = findViewById(R.id.tutorial_musicPlayer);
        skip = findViewById(R.id.skipTutorial);
        gif = findViewById(R.id.swipeup);
        taphere = findViewById(R.id.taphere);
        tutorialDesc = findViewById(R.id.descTutorial);
        tutoMimage = findViewById(R.id.tutoAudioImage);
        titleTuto = findViewById(R.id.tutoAudioTitle);
        subTuto = findViewById(R.id.tutoSub);
        ttaudio = findViewById(R.id.ttaud);
        playGif = findViewById(R.id.playgif);
        audioGif = findViewById(R.id.audioGif);
        titleTuto.setSelected(true);
        play_tutorial = findViewById(R.id.play_tut);
        relativre_touch = findViewById(R.id.reltouch);
        news = findViewById(R.id.newsre);
        more = findViewById(R.id.moreDetails);


        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firstTime = sharedPreferences.getInt("first_time", 0);

        Log.e("firsttimelogin ", String.valueOf(firstTime));


        if (firstTime != 0) {
            Intent i = new Intent(Tutorial.this, select_category.class);
            startActivity(i);
            Tutorial.this.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
        }


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("first_time", 2);
                editor.apply();
                Intent i = new Intent(Tutorial.this, select_category.class);
                startActivity(i);
                Tutorial.this.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);


            }
        });

        relativre_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("eventTouch", "touch");

                if (flag == 1) {
                    news.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                    gif.setVisibility(View.GONE);
                    taphere.setVisibility(View.VISIBLE);
                    tutorialDesc.setText("Tap on card for more details");
                    Log.e("touchevent", String.valueOf(flag));
                    flag = 2;
                } else if (flag == 2) {
                    more.setVisibility(View.GONE);
                    Log.e("touchevent", String.valueOf(flag));
                    skip.setVisibility(View.GONE);
                    audioGif.setVisibility(View.VISIBLE);
                    playGif.setVisibility(View.VISIBLE);
                    ttaudio.setVisibility(View.VISIBLE);
                    tutorialDesc.setVisibility(View.GONE);
                    gif.setVisibility(View.GONE);
                    taphere.setVisibility(View.GONE);
                    cardStackView.setVisibility(View.GONE);
                    musicPlayer.setVisibility(View.VISIBLE);
                    flag = 3;
                } else if (flag == 3) {
                    editor.putInt("first_time", 2);
                    editor.apply();
                    Intent i = new Intent(Tutorial.this, select_category.class);
                    startActivity(i);
                    Tutorial.this.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);



                }
            }
        });

        cardStackView = findViewById(R.id.tutorial_card_Stack);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d("dsdfdfvbfd", "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                // Paginating
                if (manager.getTopPosition() == 1) {
                    manager.setCanScrollHorizontal(false);
                    manager.setCanScrollVertical(false);
                    gif.setVisibility(View.GONE);

                    news.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                    gif.setVisibility(View.GONE);
                    taphere.setVisibility(View.VISIBLE);
                    tutorialDesc.setText("Tap on card for more details");
                    Log.e("touchevent", String.valueOf(flag));
                    flag = 2;
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {

                Picasso.get().load("http://papanews.in/PapaNews/images/Technology/IMG1540745773.jpg")
                        .placeholder(R.drawable.noimage)
                        .resize(250, 250)
                        .into(tutoMimage);
                titleTuto.setText("Apple may finally launch device it had cancelled in 2019");
                subTuto.setText("IndiaToday");


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag == 2) {
                            more.setVisibility(View.GONE);
                            Log.e("touchevent", String.valueOf(flag));
                            skip.setVisibility(View.GONE);
                            audioGif.setVisibility(View.VISIBLE);
                            playGif.setVisibility(View.VISIBLE);
                            ttaudio.setVisibility(View.VISIBLE);
                            tutorialDesc.setVisibility(View.GONE);
                            gif.setVisibility(View.GONE);
                            taphere.setVisibility(View.GONE);
                            cardStackView.setVisibility(View.GONE);
                            musicPlayer.setVisibility(View.VISIBLE);
                            flag = 3;
                        }
                    }
                });

//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d("gdfgdfg", "onCardAppeared: ");
//
//                        SharedPreferences preferences = getSharedPreferences("passScreen", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//
//                        editor.putString("shdisc","Apple is expected to launch new iPhone thirteen collection, a " +
//                                "new iPad Mini and a lot extra this year.");
//                        editor.putString("title", "Apple may finally launch device it had cancelled in 2019");
//                        editor.putString("srcName", "IndiaToday");
//                        editor.putString("srcImage", "http://papanews.in/PapaNews/images/Source/IMG1847726800.jpg");
//                        editor.putString("longDisc", "Apple is expected to launch new iPhone thirteen collection, a new " +
//                                "iPad Mini and a lot extra this year. The Cupertino massive might also launch a product" +
//                                " it had cancelled in 2019. Apple’s AirPower- a wireless charging mat- which in no way " +
//                                "noticed the mild of day, can also in the end debut this year. The product became first" +
//                                " announced in 2017 at some point of a release event. However, because of technical roadblocks," +
//                                " Apple determined to scrap the idea of launching the product in 2019.");
//                        editor.putString("video_youtube", "8lBkVB6RkUc");
//                        editor.putString("image", "http://papanews.in/PapaNews/images/Technology/IMG1540745773.jpg");
//                        editor.putString("audioty", "1");
//                        editor.putString("id", "59");
//                        editor.putString("audio", "1");
//                        editor.putString("converted", "http://papanews.in/PapaNews/audioFiles/technology/audio861586273.mp3");
//                        editor.putString("date", "10/7/2021");
//                        editor.putString("srcViews", "434");
//                        editor.apply();
//                        Intent i = new Intent(Tutorial.this, FullscreenActivity.class);
//                        startActivity(i);
//                        Tutorial.this.overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
//
//                    }
//                });
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(false);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardStackAdapter(addList(), getApplicationContext());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("touchevent", "touch");
        return super.onTouchEvent(event);
    }

    private void paginate() {
        List<ItemModel> old = adapter.getItems();
        List<ItemModel> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }

    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel("Apple may finally launch device it had cancelled in 2019",
                "http://papanews.in/PapaNews/images/Technology/IMG1540745773.jpg",
                "Apple is expected to launch new iPhone thirteen collection, a new iPad Mini and a lot extra this year.",
                "Jember", "http://papanews.in/PapaNews/images/Source/IMG1847726800.jpg"
                , "IndiaToday", "345", "", "", "", "", "",""));

        items.add(new ItemModel("Android 10 for OnePlus 5, OnePlus 5T coming soon",
                "http://papanews.in/PapaNews/images/Technology/IMG1863736469.jpg",
                "OnePlus has made a call for itself for supplying excessive-give up specifications at rather low cost costs, with the latest OnePlus eight series phones reminding that.",
                "Jember", "http://papanews.in/PapaNews/images/Source/IMG1973275706.jpg"
                , "IndiaToday", "237", "", "", "", "", "",""));

        return items;
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