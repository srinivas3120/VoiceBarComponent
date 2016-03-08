package com.srinivas.mudavath.voicebarcomponent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Ajay on 7/28/2015.
 */
public class Common {

    public static int PAGE_COUNT = 1;
    public static int PIC_URI_TYPE = 2;
    public static int PAGE_SIZE = 1;
    public static int TOTAL_PAGES = 1;
    public static int PAGE_NUMBER = 0;


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connManager.getActiveNetworkInfo() != null
                && connManager.getActiveNetworkInfo().isConnected();
    }

    public static File getAudioPath(String fileName, String folderName){
        File file  = new File(Environment.getExternalStorageDirectory() + File.separator + "InstavoiceBlogs" +
                File.separator + "audio" + File.separator +
                folderName);
        if (!file.exists()){
            file.mkdirs();
        }
        File audioFile = new File(file,"aud_"+fileName);
        return audioFile;
    }

    public static File getVideoPath(String fileName, String folderName){
        File file  = new File(Environment.getExternalStorageDirectory() + File.separator + "InstavoiceBlogs" +
                File.separator + "video" + File.separator +
                folderName);
        if (!file.exists()){
            file.mkdirs();
        }
        File videoFile = new File(file,"vid_"+fileName);
        return videoFile;
    }

    public static File getImagePath(String fileName){
        File file  = new File(Environment.getExternalStorageDirectory() + File.separator + "InstavoiceBlogs/image");
        if (!file.exists()){
            file.mkdirs();
        }
        File imageFile = new File(file, fileName);
        return imageFile;
    }

    public static File getVideoPath(String fileName){
        File file  = new File(Environment.getExternalStorageDirectory() + File.separator + "InstavoiceBlogs/video");
        if (!file.exists()){
            file.mkdirs();
        }
        File imageFile = new File(file, fileName);
        return imageFile;
    }

    private static Typeface typeFaceRobotoMedium = null;
    private static Typeface typeFaceRobotoRegular = null;

    public static Typeface getTypeFaceMedium(Context context) {
        if (typeFaceRobotoMedium == null) {
            typeFaceRobotoMedium = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Medium.ttf");
        }
        return typeFaceRobotoMedium;
    }


    public static Typeface getTypeFaceRobotoRegular(Context context){
        if(typeFaceRobotoRegular == null){
            typeFaceRobotoRegular = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf");
        }
        return typeFaceRobotoRegular;
    }


    public static void deleteFile(String outputFile) {
        if(TextUtils.isEmpty(outputFile)){
            return;
        }
        File file = new File(outputFile);
        if (file.exists()) {
            file.delete();
        }
    }

    private static String[] suffix = new String[]{"","K", "M", "B", "T"};
    private static int MAX_LENGTH = 4;

    public static String formatNumber(int number){
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
            r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
        }

        return r;
    }

    public static String setDuration(int dur) {
        String duration_annotation;
        if(dur <60) {
            duration_annotation = "00:";
        }else {
            int duration = dur/60;
            dur = dur%60;
            duration_annotation = "0"+duration+":";
        }
        if (dur < 10) {
            duration_annotation += "0" + dur;
        } else {
            duration_annotation += dur;
        }
        return duration_annotation;
    }

    public static boolean isContentAvailable(String audioFile) {
        if(TextUtils.isEmpty(audioFile)){
            return false;
        }
        File file;
        try {
            file=new File(audioFile);
            if(file.length()>100){
                return true;
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }
    public static boolean isContentAvailable(Uri audioUri) {
        return audioUri!=null?isContentAvailable(audioUri.toString()):false;
    }

    static Uri.Builder uriBuilder=new Uri.Builder();
    public static Uri getUri(String path){
        return uriBuilder.path(path).build();
    }
}