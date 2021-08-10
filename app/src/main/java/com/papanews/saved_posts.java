package com.papanews;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.r0adkll.slidr.Slidr;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class saved_posts extends AppCompatActivity implements CardStackListener{
    ImageView cancel;
    MaterialTextView svps;
    CardStackView cardStackView;
    MaterialButton save_rewind;
    RelativeLayout relativeLayout;

    private static final String TAG = "saved_posts";
    private CardStackLayoutManager manager;
    private savePostAdapter adapter;

    JSONObject product;
    Gson gson = new Gson();
    String jsonPreferences;
    List<ItemModel> productFromShared = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);
        cardStackView = findViewById(R.id.card_stack_view);
        relativeLayout= findViewById(R.id.relative);
        svps = findViewById(R.id.savpost);
        save_rewind = findViewById(R.id.rewind_save);

        Slidr.attach(this);


        SharedPreferences prefs = getSharedPreferences("saveDataPush", MODE_PRIVATE);
        jsonPreferences = prefs.getString("savedData", "");




        Type type = new TypeToken<List<ItemModel>>() {}.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);
        Log.e("save save :: ", String.valueOf(jsonPreferences));

        if(jsonPreferences.equals("")){
            svps.setVisibility(View.VISIBLE);
        }else {
            svps.setVisibility(View.GONE);
        }


//        relativeLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                if (manager.getTopPosition() == adapter.getItemCount()) {
                    // -------------------- last position reached, do something ---------------------

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
            public void onCardAppeared(View view, final int position) {

                Log.e("cardcard Posts :: ", String.valueOf(position));
                cancel = view.findViewById(R.id.cancel_card);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(saved_posts.this, "click", Toast.LENGTH_SHORT).show();

                    }
                });

                JSONArray array;
                try {
                    array = new JSONArray(jsonPreferences);
                    product = array.getJSONObject(position);
                    Log.e("savesave Posts :: ", String.valueOf(jsonPreferences));
                    Log.e("savesave length :: ", String.valueOf(array.length()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });

        manager.setTranslationInterval(12.0f);
        manager.setDirections(Direction.VERTICAL);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.1f);
        manager.setMaxDegree(50.0f);
        manager.setCanScrollHorizontal(false);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new savePostAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());


        save_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(saved_posts.this, "click", Toast.LENGTH_SHORT).show();
                RewindAnimationSetting settings = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();

                cardStackView.rewind();
                CardStackLayoutManager cardStackLayoutManager2 = new CardStackLayoutManager(saved_posts.this);
                cardStackLayoutManager2.setRewindAnimationSetting(settings);
                cardStackLayoutManager2.setDirections(Direction.VERTICAL);
                cardStackView.setLayoutManager(cardStackLayoutManager2);
                cardStackLayoutManager2.setCanScrollHorizontal(false);

                save_rewind.setVisibility(View.GONE);
            }
        });

    }

    private void paginate() {
        List<savePost_Model> old = adapter.getItems();
        List<savePost_Model> baru = new ArrayList<>(addList());
        SavedCallback callback = new SavedCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }

    private List<savePost_Model> addList() {
        List<savePost_Model> items = new ArrayList<>();
        try {
            //converting the string to json array object
            JSONArray array = new JSONArray(jsonPreferences);

            Log.e("saved Posts :: ", String.valueOf(jsonPreferences));
            Log.e("saved length :: ", String.valueOf(array.length()));
            //traversing through all the object

            for (int i = 0; i < array.length(); i++) {
                //getting product object from json array
                JSONObject product = array.getJSONObject(i);
                //adding the product to product list
                items.add(new savePost_Model(
                        product.getString("image"),
                        product.getString("title"),
                        product.getString("shortDesc"),
                        product.getString("id"),
                        product.getString("sourceImage"),
                        product.getString("sourceName"),
                        product.getString("sourceViews"),
                        product.getString("longDesc"),
                        product.getString("videoId")
                ));
                Log.e("Sohaib :: ", String.valueOf(items));
            }
            //creating adapter object and setting it to recyclerview
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_right);
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
    }


    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {
        Log.d(TAG, "cardcard: " + position );
    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}