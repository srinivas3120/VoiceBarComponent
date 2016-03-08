package com.srinivas.mudavath.voicebarcomponent;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AudioWithProgress awp;
    TextView tv_play;
    TextView tv_clear;
    TextView tv_reposition;
    TextView tv_awp_color;
    TextView tv_fb;
    TextView tv_twitter;
    TextView tv_iv_share;
    TextView tv_heart;

    ImageView iv_play_pause;
    private boolean isReceive;
    private boolean fBShare =false;
    private boolean twitterShare;
    private boolean iVShare;
    private boolean heart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        awp= (AudioWithProgress) findViewById(R.id.awp);
        tv_play= (TextView) findViewById(R.id.tv_play);
        tv_clear= (TextView) findViewById(R.id.tv_clear);
        tv_reposition= (TextView) findViewById(R.id.tv_reposition);
        tv_awp_color= (TextView) findViewById(R.id.tv_awp_color);
        iv_play_pause= (ImageView) findViewById(R.id.iv_play_pause);

        tv_fb= (TextView) findViewById(R.id.tv_fb);
        tv_heart= (TextView) findViewById(R.id.tv_heart);
        tv_iv_share= (TextView) findViewById(R.id.tv_iv_share);
        tv_twitter= (TextView) findViewById(R.id.tv_twitter);

        tv_clear.setOnClickListener(this);
        tv_play.setOnClickListener(this);
        iv_play_pause.setOnClickListener(this);
        tv_reposition.setOnClickListener(this);
        tv_awp_color.setOnClickListener(this);

        tv_fb.setOnClickListener(this);
        tv_heart.setOnClickListener(this);
        tv_twitter.setOnClickListener(this);
        tv_iv_share.setOnClickListener(this);

        //awp.setTotalPlayDuration((int) ((Math.random() + 0.5 * Math.random()) * 120));
        awp.setAudioPath(Environment.getExternalStorageDirectory() + File.separator + "InstavoiceBlogs" +
                File.separator + "audio" + File.separator +
                "received"+File.separator +"file.wav");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clear:
                awp.clearPlayingAudio();
                break;
            case R.id.tv_play:
                try {
                    awp.playAudio(Environment.getExternalStorageDirectory() + File.separator + "InstavoiceBlogs" +
                            File.separator + "audio" + File.separator +
                            "received"+File.separator +"file.wav");
                } catch (AudioWithProgress.InvalidFileException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_play_pause:
                try {
                    awp.playAudio(Environment.getExternalStorageDirectory() + File.separator + "InstavoiceBlogs" +
                            File.separator + "audio" + File.separator +
                            "received"+File.separator +"file.wav");
                } catch (AudioWithProgress.InvalidFileException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_reposition:
                awp.setTotalPlayDuration((int)((Math.random()+0.5*Math.random())*120));
                awp.clearPlayingAudio();
                break;
            case R.id.tv_awp_color:
                awp.changeBackgroundStyle();
                break;
            case R.id.tv_fb:
                if(!fBShare){
                    awp.setFBVisibility(View.VISIBLE);
                }else {
                    awp.setFBVisibility(View.GONE);
                }
                fBShare =!fBShare;
                break;
            case R.id.tv_twitter:
                if(!twitterShare){
                    awp.setTwitterVisibility(View.VISIBLE);
                }else {
                    awp.setTwitterVisibility(View.GONE);
                }
                twitterShare =!twitterShare;
                break;
            case R.id.tv_iv_share:
                if(!iVShare){
                    awp.setIVShareVisibility(View.VISIBLE);
                }else {
                    awp.setIVShareVisibility(View.GONE);
                }
                iVShare =!iVShare;
                break;
            case R.id.tv_heart:
                if(!heart){
                    awp.setHeartVisibility(View.VISIBLE);
                }else {
                    awp.setHeartVisibility(View.GONE);
                }
                heart=!heart;
                break;
            default:
                break;
        }
    }
}
