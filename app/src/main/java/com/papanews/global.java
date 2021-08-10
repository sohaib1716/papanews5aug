package com.papanews;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class global {
    public static final List<Map<String, String>> myMap = new ArrayList<Map<String, String>>();

    public static final List<Map<String, String>> notificationArray = new ArrayList<Map<String, String>>();

    public static final List<String> cat_set = new ArrayList<String>();

    public static final List<String> userlogged = new ArrayList<String>();

    public static final List<Integer> priority = new ArrayList<>();

    public static String date = "";
    public static String timenotify = "";

    public static int mediplayer_Setting = 0;

    public static int returnahowmuch = 0;
    public static String skipskip = "";

    public static String hindilang = "";


    public static int mm = 0;
    public static Bitmap bitmapimage;

    public static int logincheck = 0;

    //  notification mediplayer
    public static int playpause = 0;

    public static int verifyOrnot = 0;

    //    location
    public static String userLoc = "";

    public static MediaPlayer mMediaPlayernew = new MediaPlayer();
    public static MediaPlayer mMediaPlayer = new MediaPlayer();
    public static MediaPlayer mMediaPlayerTech = new MediaPlayer();
    public static MediaPlayer mMediaPlayerStartup = new MediaPlayer();
    public static MediaPlayer mMediaPlayerBusiness = new MediaPlayer();
    public static MediaPlayer mMediaPlayerEntertaintment = new MediaPlayer();
    public static MediaPlayer mMediaPlayerSports = new MediaPlayer();
    public static MediaPlayer mMediaPlayerPolitics = new MediaPlayer();
    public static MediaPlayer mMediaPlayerInternational = new MediaPlayer();
    public static MediaPlayer mMediaPlayerInfluence = new MediaPlayer();
    public static MediaPlayer mMediaPlayerMiscellaneous = new MediaPlayer();


    public static String proper_audio = "default";
    public static String proper_title = "default";
    public static String proper_newws = "default";
    public static String proper_image = "default";


    //    short description size
    public static int size_of_sh = 16;
    public static int maxline_of_sh = 16;
    public static int size_of_head = 16;


    public static String convertHindi(String data){
        final TranslateAPI translateAPI = new TranslateAPI(Language.AUTO_DETECT, Language.HINDI, data);
        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                hindilang = translatedText;
                Log.e("hellohello h", hindilang);
            }
            @Override
            public void onFailure(String ErrorText) {
//                Toast.makeText(, ErrorText, Toast.LENGTH_SHORT).show();
            }
        });

        return hindilang;
    }


    //urls for getting data
    public static final String BASE_URL = "https://papanews.in/PapaNews/";
    public static final String GET_NEWS = BASE_URL + "getNews.php";
    public static final String politics = "getPolitics.php";
    public static final String technology = "getTech.php";
    public static final String business = "getBusiness.php";
    public static final String entertain = "getEntertain.php";
    public static final String sports = "getSports.php";
    public static final String startup = "getStartup.php";
    public static final String international = "getIntetrnational.php";
    public static final String influence = "getInfluence.php";
    public static final String miscel = "getMiscel.php";

    //urls for signup and login
    public static final String checkEmail = "emailExist.php";
    public static final String newSignup = "signUpnew.php";
    public static final String checkUsername = "userNameexist.php";

    //update views
    public static final String updviews = global.BASE_URL + "updateViews/views";

    //temp check
    public static int ppp = 0;

    //        frag_shared
    public static String frag_shared ="";

}
