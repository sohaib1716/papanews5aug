package com.papanews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mannan.translateapi.TranslateAPI;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Profile extends Fragment implements CardStackListener {
    CardStackView cardStackView;
    MaterialTextView user_name, cat, save_news, about_us, terms, lang, notification_send, logout, text_verify;
    ImageView imageView, image_ver;
    ImageView catImage, logoutImage, papaimage;
    View bb1, bb2;
    MaterialButton btlo;
    RelativeLayout cat_rel, lang_rel, save_rel, noti_rel, about_rel, term_rel, logout_rel;

    //    Image upload by user
    String encodeImageString;
    Bitmap bitmap;
    String email;
    String response_data;
    String skipfornow;
    int permission;

    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String final_user_email;

    LinearLayout verify;
    int verifiedd;
    String check_date;
    String currentDate;


    @SuppressLint({"SetTextI18n", "CutPasteId", "ResourceAsColor"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor edit = prefs.edit();


        sharedPreferences = getActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        final String user = sharedPreferences.getString("user_final", "");
        final String user_google = sharedPreferences.getString("user_Google", "");
        final String google_image = sharedPreferences.getString("google_image", "");
        final String image_fb = sharedPreferences.getString("image_fb", "");
        final String bitmapimage = sharedPreferences.getString("bitmapimage", "");
        final String selectedlang = sharedPreferences.getString("selectedlang", "English");


        // verification
        verify = view.findViewById(R.id.profileverified);
        image_ver = view.findViewById(R.id.imgver);
        text_verify = view.findViewById(R.id.ver);

//
//        verifiedd = sharedPreferences.getInt("verified", 0);
//        if (verifiedd == 1) {
//            image_ver.setVisibility(View.VISIBLE);
//            verify.setVisibility(View.GONE);
//        } else {
//            verify.setVisibility(View.VISIBLE);
//            image_ver.setVisibility(View.GONE);
//        }


        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Log.e("todays_date ", currentDate);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!currentDate.equals(check_date)) {
                    check_date = currentDate;
                    Intent i = new Intent(getActivity(), SignUp.class);
                    global.verifyOrnot = 2;
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "You have exceeded today's limit, please try again tomorrow", Toast.LENGTH_SHORT).show();
                }

            }
        });


        skipfornow = sharedPreferences.getString("skip_now", null);
        bb1 = view.findViewById(R.id.bb1);
        bb2 = view.findViewById(R.id.view2);

        editor.putInt("permission", 0);
        editor.apply();

        permission = sharedPreferences.getInt("permission", 0);

        btlo = view.findViewById(R.id.lologin);
        user_name = view.findViewById(R.id.finalUsername);
        catImage = view.findViewById(R.id.catImage);
        logoutImage = view.findViewById(R.id.lgImage);
        imageView = view.findViewById(R.id.profile_image);
        lang = view.findViewById(R.id.proflang);
        logout = view.findViewById(R.id.logout);
        cat = view.findViewById(R.id.categories);
        save_news = view.findViewById(R.id.savedNews);
        about_us = view.findViewById(R.id.about1);
        terms = view.findViewById(R.id.tanc1);
        notification_send = view.findViewById(R.id.notification_Send);
        papaimage = view.findViewById(R.id.papaimage);


        //on click
        cat_rel = view.findViewById(R.id.category_relateive);
        noti_rel = view.findViewById(R.id.notification_relateive);
        lang_rel = view.findViewById(R.id.language_relateive);
        save_rel = view.findViewById(R.id.save_relateive);
        about_rel = view.findViewById(R.id.about_relateive);
        term_rel = view.findViewById(R.id.tnc_relateive);
        logout_rel = view.findViewById(R.id.logout_relateive);

        if (skipfornow.equals("skip")) {
            papaimage.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            user_name.setVisibility(View.GONE);
            logoutImage.setVisibility(View.GONE);
            bb2.setVisibility(View.GONE);
            btlo.setVisibility(View.VISIBLE);
            cat.setTextColor(Color.GRAY);
            save_news.setTextColor(Color.GRAY);
            notification_send.setTextColor(Color.GRAY);
        } else {
            papaimage.setVisibility(View.GONE);
            cat.setTextColor(Color.WHITE);
            save_news.setTextColor(Color.WHITE);
            notification_send.setTextColor(Color.WHITE);
            btlo.setVisibility(View.GONE);
            bb2.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            user_name.setVisibility(View.VISIBLE);
            catImage.setVisibility(View.VISIBLE);
            logoutImage.setVisibility(View.VISIBLE);
        }


        logout_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putInt("key", 0);
                editor.remove("user_final");
                editor.remove("google_image");
                editor.remove("image_fb");
                editor.putInt("checkcheck", 0);
                editor.apply();

//                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);


                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        cat_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Ctegory Activity", Toast.LENGTH_SHORT).show();
                editor.putInt("onceLogged", 0);
                editor.apply();
                Intent i = new Intent(getActivity(), select_category.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });

        save_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ki = new Intent(getActivity(), saved_posts.class);
                startActivity(ki);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });


        noti_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent li = new Intent(getActivity(), activity_notification.class);
                startActivity(li);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });

        btlo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("key", 0);
                editor.apply();
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
            }
        });


        final_user_email = sharedPreferences.getString("final_email", "");
        String google_photo = sharedPreferences.getString("photogoogle", "");
        Log.d("photo_google", google_photo);
        Log.d("final_usr_email", final_user_email);
        bringUserdata(final_user_email);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
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


        if (selectedlang.equals("Hindi")) {
            final TranslateAPI translateAPI = new TranslateAPI(com.mannan.translateapi.Language.AUTO_DETECT,
                    com.mannan.translateapi.Language.HINDI, "Categories");
            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    cat.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });

            final TranslateAPI translateAPI1 = new TranslateAPI(com.mannan.translateapi.Language.AUTO_DETECT,
                    com.mannan.translateapi.Language.HINDI, "Language");
            translateAPI1.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    lang.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });

            final TranslateAPI translateAPI2 = new TranslateAPI(com.mannan.translateapi.Language.AUTO_DETECT,
                    com.mannan.translateapi.Language.HINDI, "Saved News");
            translateAPI2.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    save_news.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });

            final TranslateAPI translateAPI3 = new TranslateAPI(com.mannan.translateapi.Language.AUTO_DETECT,
                    com.mannan.translateapi.Language.HINDI, "Notification Manager");
            translateAPI3.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    notification_send.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });

            final TranslateAPI translateAPI4 = new TranslateAPI(com.mannan.translateapi.Language.AUTO_DETECT,
                    com.mannan.translateapi.Language.HINDI, "About Us");
            translateAPI4.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    about_us.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });

            final TranslateAPI translateAPI5 = new TranslateAPI(com.mannan.translateapi.Language.AUTO_DETECT,
                    com.mannan.translateapi.Language.HINDI, "Terms and condition");
            translateAPI5.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    terms.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });

            final TranslateAPI translateAPI6 = new TranslateAPI(com.mannan.translateapi.Language.AUTO_DETECT,
                    com.mannan.translateapi.Language.HINDI, "Logout");
            translateAPI6.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    logout.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            cat.setText("Categories");
            lang.setText("Language");
            save_news.setText("Saved News");
            notification_send.setText("Notification Manager");
            about_us.setText("About Us");
            terms.setText("Terms and condition");
            logout.setText("Logout");
        }


        about_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Aboutus_terms.class);
                intent.putExtra("EXTRA_SESSION_ID", "abtus");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });

        term_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Aboutus_terms.class);
                intent.putExtra("EXTRA_SESSION_ID", "tandc");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
            }
        });


        lang_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Aboutus_terms.class);
                intent.putExtra("EXTRA_SESSION_ID", "lang");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.nothing);
//                finish();
            }
        });


        manager = new CardStackLayoutManager(getContext(), this);
        adapter = new CardStackAdapter(addList(), getContext());
        cardStackView = view.findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        manager.setTranslationInterval(55f);
        manager.setDirections(Direction.VERTICAL);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(4);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.1f);
        manager.setMaxDegree(0.0f);
        manager.setCanScrollHorizontal(false);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        return view;
    }


    public void bringUserdata(String emaill) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest request = new StringRequest(Request.Method.POST,
                        global.BASE_URL + "temp.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        response_data = response;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("user_response ::", response);

                            String number_fin = jsonObject.getString("number");

                            if (number_fin.equals("0")) {
                                verify.setVisibility(View.VISIBLE);
                                image_ver.setVisibility(View.GONE);
                            } else {
                                editor = sharedPreferences.edit();
                                editor.putInt("verified", 1);
                                editor.apply();
                                image_ver.setVisibility(View.VISIBLE);
                                verify.setVisibility(View.GONE);
                            }


                            String image = jsonObject.getString("image");

                            user_name.setText(jsonObject.getString("fullname"));

                            Picasso.get().load(image).placeholder(R.drawable.pr)
                                    .resize(350, 350)
                                    .into(imageView);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("PHPBRINGING new ", String.valueOf(e));
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
                        map.put("email", emaill);
                        return map;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }
        }, 200);

    }

    private JSONObject getJsonObject(String name, String age) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONObject userJson = new JSONObject();
        userJson.put("name", name);
        userJson.put("age", age);
        jsonObject.put("user", userJson);
        return jsonObject;
    }


    public void uploadImageProfile() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest request = new StringRequest(Request.Method.POST,
                        global.BASE_URL + "updateImage.php", new Response.Listener<String>() {
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
                        map.put("email", email);
                        map.put("image", encodeImageString);
                        return map;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }
        }, 2000);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filepath);
                Log.d("bitmap_image 1", String.valueOf(inputStream));
                bitmap = BitmapFactory.decodeStream(inputStream);
                Log.d("bitmap_image 2", String.valueOf(bitmap));
                imageView.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
//                global.bitmapimage = bitmap;
//                //Default is 0 so autologin is disabled
//                editor = sharedPreferences.edit();
//                editor.putString("bitmapimage", String.valueOf(bitmap));
//                editor.apply();
            } catch (Exception ex) {
            }
        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        if (!encodeImageString.equals("")) {
            uploadImageProfile();
        }
    }

    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel("poli2",
                "Markonah", "24",
                "Jember", "poli2",
                "", "", "", "",
                "", "", "", ""));
        return items;
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

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}