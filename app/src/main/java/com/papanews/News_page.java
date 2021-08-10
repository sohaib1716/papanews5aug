package com.papanews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class News_page extends AppCompatActivity {
    ScrollView scrollview;
    RelativeLayout createvideo, createvideo2;

    MaterialButton rmore, cta;

    MaterialTextView minidescription, headline_title, src_name, src_name2, Long_description, head2, mini, vi, vi2;
    MaterialTextView date1, date2;

    ImageView imgback;
    ImageView srcicon, srcicon2;
    ImageView save_data, share_data, tempo;
    ImageView save_data2, share_data2;

    int cc = 0;
    int flag = 0;
    int muteaud = 0;
    int bookm;
    int dontgoin = 0;
    float ytd = 0;
    String audio;
    String videoOfYOUTUBE;
    String jsonPreferences;
    String converted, video, st_time = "0", en_time = "0";
    String hashMapString;

    YouTubePlayerView youTubePlayerView;
    private BottomSheetBehavior mBottomSheetBehavior;

    Drawable drawable2;
    //    voice
    private MediaPlayer mMediaPlayer;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_fullscreen);
        scrollview = findViewById(R.id.scrollview);
        vi = findViewById(R.id.view);
        vi2 = findViewById(R.id.view2);
        createvideo = findViewById(R.id.cv);
        createvideo2 = findViewById(R.id.cv2);
        imgback = findViewById(R.id.backimg);
        rmore = findViewById(R.id.rmore);
        cta = findViewById(R.id.cta);
        minidescription = findViewById(R.id.minidis);
        headline_title = findViewById(R.id.headline);
        srcicon = findViewById(R.id.icon_val);
        srcicon2 = findViewById(R.id.icon_val2);
        src_name = findViewById(R.id.pname);
        src_name2 = findViewById(R.id.pname2);
        Long_description = findViewById(R.id.discription2);
        head2 = findViewById(R.id.headline2);
        mini = findViewById(R.id.minidis2);
        save_data = findViewById(R.id.save);
        save_data2 = findViewById(R.id.save2);
        share_data = findViewById(R.id.share);
        share_data2 = findViewById(R.id.share2);
        tempo = findViewById(R.id.temp);
        date1 = findViewById(R.id.pdate);
        date2 = findViewById(R.id.pdate2);

        //slider
        Slidr.attach(this);
        mMediaPlayer = new MediaPlayer();

        //analitics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(News_page.this);

        SharedPreferences prefs2 = getSharedPreferences("saveDataPush", MODE_PRIVATE);
        jsonPreferences = prefs2.getString("savedData", "");
        bookm = prefs2.getInt("bookmark", 0);


        SharedPreferences prefs = this.getSharedPreferences("passScreen", Context.MODE_PRIVATE);
        final String shdiscprition = prefs.getString("shdisc", null);
        final String title = prefs.getString("title", null);
        final String source_image = prefs.getString("srcImage", null);
        final String source_name = prefs.getString("srcName", null);
        final String source_views = prefs.getString("srcViews", null);
        final String long_disc = prefs.getString("longDisc", null);
        final String image = prefs.getString("image", null);
        final String id = prefs.getString("id", null);
        converted = prefs.getString("converted", null);
        final String date = prefs.getString("date", null);


        audio = prefs.getString("audioty", null);
        videoOfYOUTUBE = prefs.getString("video_youtube", null);


        if (videoOfYOUTUBE.contains("=!")) {
            String[] separated = videoOfYOUTUBE.split("=!");
            try{
                video = separated[0];
                st_time = separated[1];
                en_time = separated[2];
            }catch (Exception e){
                video = separated[0];
            }
        } else {
            video = videoOfYOUTUBE;
        }


        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        initYouTubePlayerView(video);
        date1.setText(date);
        date2.setText(date);

        final String sv = String.valueOf((Integer.parseInt(source_views) - 1));


        checkIfSaved(image, title, id, shdiscprition, source_image, source_name, sv, long_disc);


        Picasso.get().load(image).placeholder(R.drawable.sample1)
                .resize(350, 350)
                .into(tempo);


        share_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FullscreenActivity.this, "Share Share", Toast.LENGTH_SHORT).show();
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(News_page.this);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.METHOD, "Share_news_short");
                mFirebaseAnalytics.logEvent("Share", bundle);

                BitmapDrawable drawable = (BitmapDrawable) tempo.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Intent intent = new Intent(Intent.ACTION_SEND);
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                if(bitmapPath!= null) {
                    Uri uri = Uri.parse(bitmapPath);
                    intent.setType("image/jpg");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }

                intent.putExtra(Intent.EXTRA_TEXT, title +
                        "\nFor more updates download:" +
                        "\n" + getString(R.string.app_link));

                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        share_data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FullscreenActivity.this, "Share Share", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.METHOD, "Share_news_long");
                mFirebaseAnalytics.logEvent("Share", bundle);
                BitmapDrawable drawable = (BitmapDrawable) tempo.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Intent intent = new Intent(Intent.ACTION_SEND);
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                if(bitmapPath!= null) {
                    Uri uri = Uri.parse(bitmapPath);
                    intent.setType("image/jpg");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
                intent.putExtra(Intent.EXTRA_TEXT, title +
                        "\nFor more updates download:" +
                        "\n" + getString(R.string.app_link));

                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        save_data2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
//                Toast.makeText(FullscreenActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Map<String, String> myMap1 = new HashMap<String, String>();
                myMap1.put("image", image);
                myMap1.put("title", title);
                myMap1.put("shortDesc", shdiscprition);
                myMap1.put("id", id);
                myMap1.put("sourceImage", source_image);
                myMap1.put("sourceName", source_name);
                myMap1.put("audio", audio);
                myMap1.put("sourceViews", (sv));
                myMap1.put("longDesc", long_disc);
                myMap1.put("videoId", videoOfYOUTUBE);
                Log.e("check CHECK :: ", String.valueOf(hashMapString));
                if (global.myMap.contains(myMap1)) {
                    drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bookwhite);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
//                    Toast.makeText(FullscreenActivity.this, "Remove saved", Toast.LENGTH_SHORT).show();
//                    save_data2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    save_data.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    global.myMap.remove(myMap1);
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "Save_news_long");
                    mFirebaseAnalytics.logEvent("Save", bundle);

                } else {
                    drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bookmark);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
//                    save_data2.setBackgroundColor(Color.parseColor("#F4931D"));
//                    save_data.setBackgroundColor(Color.parseColor("#F4931D"));
//                    Toast.makeText(FullscreenActivity.this, "Saving first time", Toast.LENGTH_SHORT).show();
                    global.myMap.add(myMap1);
                    Log.e("Lappas save :: ", String.valueOf(global.myMap));
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));
                }
            }
        });

        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FullscreenActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Map<String, String> myMap1 = new HashMap<String, String>();
                myMap1.put("image", image);
                myMap1.put("title", title);
                myMap1.put("shortDesc", shdiscprition);
                myMap1.put("id", id);
                myMap1.put("sourceImage", source_image);
                myMap1.put("sourceName", source_name);
                myMap1.put("audio", audio);
                myMap1.put("sourceViews", (sv));
                myMap1.put("longDesc", long_disc);
                myMap1.put("videoId", videoOfYOUTUBE);
                Log.e("check CHECK :: ", String.valueOf(hashMapString));

                if (global.myMap.contains(myMap1)) {
                    drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bookwhite);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
//                    Toast.makeText(FullscreenActivity.this, "Remove saved", Toast.LENGTH_SHORT).show();
//                    save_data2.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    save_data.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    global.myMap.remove(myMap1);
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "Save_news_short");
                    mFirebaseAnalytics.logEvent("Save", bundle);
                } else {
                    drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bookmark);
                    save_data.setImageDrawable(drawable2);
                    save_data2.setImageDrawable(drawable2);
//                    save_data2.setBackgroundColor(Color.parseColor("#F4931D"));
//                    save_data.setBackgroundColor(Color.parseColor("#F4931D"));
//                    Toast.makeText(FullscreenActivity.this, "Saving first time", Toast.LENGTH_SHORT).show();
                    global.myMap.add(myMap1);
                    Log.e("Lappas save :: ", String.valueOf(global.myMap));
                    Gson gson = new Gson();
                    hashMapString = gson.toJson(global.myMap);
                    Log.e("myMap :: ", String.valueOf(global.myMap));
                    Log.e("position :: ", String.valueOf(jsonPreferences));
                    SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
                    prefs.edit().putString("savedData", hashMapString).apply();
                    prefs.edit().putInt("bookmark", 2).apply();
                    Log.e("storing data :: ", String.valueOf(hashMapString));
                }
            }
        });


        vi.setText(source_views);
        vi2.setText(source_views);
        minidescription.setText(shdiscprition);
        mini.setText(shdiscprition);
        headline_title.setText(title);
        head2.setText(title);
        Long_description.setText(long_disc);

        Picasso.get().load(source_image).placeholder(R.drawable.sample1)
                .resize(150, 150)
                .into(srcicon);
        Picasso.get().load(source_image).placeholder(R.drawable.sample1)
                .resize(150, 150)
                .into(srcicon2);

        src_name.setText(source_name);
        src_name2.setText(source_name);


        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        rmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cc == 0) {
                    cc = 1;
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    cc = 0;
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });


        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        mTextViewState.setText("Collapsed");
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
//                        mTextViewState.setText("Dragging...");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
//                        mTextViewState.setText("Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
//                        mTextViewState.setText("Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
//                        mTextViewState.setText("Settling...");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                mTextViewState.setText("Sliding...");
            }
        });


//        cta.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tracker = new YouTubePlayerTracker();
//                youTubeView.addListener(tracker);
//
////                Log.e("dataaaaaa1", String.valueOf(tracker.getState()));
////                Log.e("dataaaaaa2", String.valueOf(tracker.getCurrentSecond()));
////                Log.e("dataaaaaa3", String.valueOf(tracker.getVideoDuration()));
//
//            }
//        });


        cta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.METHOD, "CTA_BUTTON");
                mFirebaseAnalytics.logEvent("CTA", bundle);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lapaas.com/"));
                startActivity(intent);
            }
        });


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        createvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
                muteaud = 1;
                startActivity(new Intent(News_page.this, Upload_News.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);

            }
        });

        createvideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
                muteaud = 1;
                startActivity(new Intent(News_page.this, Upload_News.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);

            }
        });


    }

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkIfSaved(String image, String title, String id, String shdiscprition, String source_image, String source_name,
                              String source_views, String long_disc) {
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
        Log.e("local myMap :: ", String.valueOf(myMap1));
        Log.e("local global :: ", String.valueOf(global.myMap));


        if (global.myMap.contains(myMap1)) {
            Log.e("local checking :: ", "Yess yess");
            drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bookmark);
            save_data.setImageDrawable(drawable2);
            save_data2.setImageDrawable(drawable2);

        } else {
            drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bookwhite);
            save_data.setImageDrawable(drawable2);
            save_data2.setImageDrawable(drawable2);
        }

    }


    private void initYouTubePlayerView(final String video) {
//        youTubePlayerView.inflateCustomPlayerUi(R.layout.activity_fullscreen);

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(0)
                .rel(0)
                .ivLoadPolicy(3)
                .ccLoadPolicy(1)
                .build();

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                youTubePlayer.loadVideo(video, Float.parseFloat(st_time));
                if (audio.equals("1")) {
                    youTubePlayer.mute();
                }


                youTubePlayer.addListener(new YouTubePlayerListener() {
                    @Override
                    public void onReady(@NotNull YouTubePlayer youTubePlayer) {

                    }

                    @Override
                    public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState playerState) {
                        Log.e("pss", "cs " + playerState);

//                       pause media once it is played
                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                flag = 4;
                            }
                        });


                        if (playerState.equals(PlayerConstants.PlayerState.PAUSED)) {
                            mMediaPlayer.pause();
                        } else if (playerState.equals(PlayerConstants.PlayerState.PLAYING)) {
                            if (audio.equals("1") && flag == 0 && dontgoin == 0) {
                                try {
                                    Log.e("audioprob in ", "audio");
                                    flag = 1;
                                    mMediaPlayer.setDataSource(converted);
                                    mMediaPlayer.prepare();
                                    mMediaPlayer.start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (flag == 1) {
                                mMediaPlayer.start();
                            }

                        }
                    }

                    @Override
                    public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

                    }

                    @Override
                    public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

                    }

                    @Override
                    public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError playerError) {

                    }

                    @Override
                    public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {
                        if (v > (ytd - 1)) {
                            youTubePlayer.seekTo(Float.parseFloat(st_time));
                        }
                        if (muteaud == 1) {
                            youTubePlayer.mute();
//                            mMediaPlayer.pause();
                        }
                        Log.e("yyyy", "cs " + v);
                    }

                    @Override
                    public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {
                        if (Float.parseFloat(en_time) > 0) {
                            ytd = Float.parseFloat(en_time);
                        } else {
                            ytd = v;
                        }

//                        Log.e("yyyyyyyy", "ytd " + ytd);
                    }

                    @Override
                    public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

                    }

                    @Override
                    public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

                    }
                });

            }
        });
    }


    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    public void onPause() {
        mMediaPlayer.stop();
        super.onPause();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
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
//        Log.e("finiishsss ", "finish");
        dontgoin = 1;
        Log.e("audioprob out ", "audio");
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
    }
}