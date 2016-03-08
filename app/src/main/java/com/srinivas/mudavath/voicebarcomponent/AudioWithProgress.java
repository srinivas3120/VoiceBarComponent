package com.srinivas.mudavath.voicebarcomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.srinivas.mudavath.utils.RecordingPlayer;

/**
 * Created by Mudavath Srinivas on 04-12-2015.
 */
public class AudioWithProgress extends LinearLayout {

    private View rootView;
    private ProgressBar pb_audio;
    public ImageView iv_play_pause;
    private TextView tv_duration;
    private LinearLayout ll_progressbar;
    private ImageView iv_heart;
    private ImageView iv_fb;
    private ImageView iv_iv_share;
    private ImageView iv_twitter;
    private View view;
    private ProgressBar downloadProgressBar;

    private Handler handler = new Handler();
    private Context context;

    private int playedDuration = 0;
    private int totalPlayDuration = 0;
    private boolean isPlaying;
    private RelativeLayout rl_play_audio;
    private boolean isReceive;

    private String audioPath;
    private RecordingPlayer recordingPlayer;


    public AudioWithProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.AudioWithProgress, 0, 0);
        int textColor = typedArray.getColor(R.styleable.AudioWithProgress_text_color, Color.RED);
        int progressBarStyle = typedArray.getResourceId(R.styleable.AudioWithProgress_progress_style, R.drawable.custom_sender_progress_bar);

        rootView = inflate(context, R.layout.voice_progress_layout, this);
        rl_play_audio = (RelativeLayout) rootView.findViewById(R.id.rl_play_audio);
        ll_progressbar = (LinearLayout) rootView.findViewById(R.id.ll_progressbar);
        downloadProgressBar = (ProgressBar) rootView.findViewById(R.id.downloadProgressBar);
        view=rootView.findViewById(R.id.view);

        pb_audio = (ProgressBar) rootView.findViewById(R.id.pb_audio);
        iv_play_pause = (ImageView) rootView.findViewById(R.id.iv_play_pause);
        tv_duration = (TextView) rootView.findViewById(R.id.tv_duration);

        iv_fb= (ImageView) rootView.findViewById(R.id.iv_fb);
        iv_heart= (ImageView) rootView.findViewById(R.id.iv_heart);
        iv_iv_share= (ImageView) rootView.findViewById(R.id.iv_iv_share);
        iv_twitter= (ImageView) rootView.findViewById(R.id.iv_twitter);

        changeBackgroundStyle(progressBarStyle);
        changeDurationTextColor(textColor);

    }

    /**
     *
     * @throws InvalidFileException couldn't play file. either file is empty or doesn't exists.
     */
    public void playAudio() throws InvalidFileException{
        if (isPlaying) {
            stopAudio();
        } else {
            if(Common.isContentAvailable(audioPath)){
                isPlaying=true;
                if(recordingPlayer==null){
                    recordingPlayer=RecordingPlayer.getInstance();
                }
                recordingPlayer.playVoicMsg(0, audioPath, playedDuration * 1000, context);
                iv_play_pause.setImageResource(R.drawable.ic_pause);
                handler.post(pbRunnable);
            }else {
                throw new InvalidFileException("couldn't play file. either file is empty or doesn't exists.");
            }
        }
    }

    /**
     * @throws InvalidFileException - couldn't play file. either file is empty or doesn't exists.
     * @param audioPath
     */
    public void playAudio(String audioPath) throws InvalidFileException{

        if (isPlaying) {
            stopAudio();
        } else {
            if(Common.isContentAvailable(audioPath)){
                setAudioPath(audioPath);
                playAudio();
            }else {
                throw new InvalidFileException("couldn't play file. either file is empty or doesn't exists.");
            }
        }
    }


    class InvalidFileException extends Exception{
        InvalidFileException(String s){
            super(s);
        }
    }


    private void stopAudio() {
        isPlaying = false;
        iv_play_pause.setImageResource(R.drawable.audio_play);
        if(recordingPlayer!=null){
            recordingPlayer.pausePlaying();
        }
        handler.removeCallbacks(pbRunnable);
        tv_duration.setText(Common.setDuration(playedDuration));
        pb_audio.setProgress(playedDuration);
    }


    private float calculateWeight(int totalPlayDuration) {

        double weight = 0.4 + (0.6 * (totalPlayDuration) / 120);
        if (weight > 1) {
            weight = 1;
        }
        return (float)weight;
    }

    Runnable pbRunnable = new Runnable() {

        @Override
        public void run() {
            pb_audio.setMax(totalPlayDuration);
            pb_audio.setProgress(playedDuration);
            if (playedDuration <= totalPlayDuration) {
                tv_duration.setText(Common.setDuration(playedDuration));
                handler.postDelayed(pbRunnable, 1000);
                playedDuration++;
            } else {
                clearPlayingAudio();
            }
        }
    };

    public void clearPlayingAudio() {
        stopAudio();
        handler.removeCallbacks(pbRunnable);
        playedDuration = 0;
        pb_audio.setProgress(playedDuration);
        tv_duration.setText(Common.setDuration(totalPlayDuration));
        iv_play_pause.setImageResource(R.drawable.audio_play);
    }

    public void changeBackgroundStyle() {
        isReceive = !isReceive;
        clearPlayingAudio();
        int layoutColor;
        if (isReceive) {
            layoutColor = R.drawable.custom_receiver_progress_bar;

        } else {
            layoutColor = R.drawable.custom_sender_progress_bar;
        }

        changeBackgroundStyle(layoutColor);

    }

    /**
     *
     * @param drawableId
     */
    public void changeBackgroundStyle(int drawableId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb_audio.setProgressDrawable(getContext().getResources().getDrawable(drawableId, getContext().getTheme()));
        } else {
            pb_audio.setProgressDrawable(getContext().getResources().getDrawable(drawableId));
        }
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
        if(recordingPlayer==null){
            recordingPlayer=RecordingPlayer.getInstance();
        }
        totalPlayDuration =recordingPlayer.getTotalDuration(Uri.parse(audioPath), context);
        setTotalPlayDuration(totalPlayDuration);
    }

    public int getTotalPlayDuration() {
        return totalPlayDuration;
    }


    public int getPlayedDurationInSeconds() {
        return playedDuration;
    }

    public int getProgressBarCount() {
        return playedDuration;
    }

    /**
     * Sets the text color to duration for all the states (normal, selected, focused) to be this color.
     * @param color
     */
    public void changeDurationTextColor(int color){
        tv_duration.setTextColor(color);
    }

    /**
     *
     * @param totalPlayDuration on setting total play duration, component size is automatically re sized.
    for durations 0s to 120s - component size will increases linearly from 0.4 to 1 times the
    maximum available space for component. if duration is greater than 120s, it will occupy whole
    maximum available space.

     */
    public void setTotalPlayDuration(int totalPlayDuration) {
        this.totalPlayDuration = totalPlayDuration;
        tv_duration.setText(Common.setDuration(this.totalPlayDuration));
        float weight = calculateWeight(totalPlayDuration);
        rl_play_audio.setLayoutParams(new LinearLayout.LayoutParams(0, rl_play_audio.getLayoutParams().height, weight));
        view.setLayoutParams(new LinearLayout.LayoutParams(0,rl_play_audio.getLayoutParams().height, 1-weight));
        invalidate();
    }

    /**
     *
     * @param visibility One of VISIBLE, INVISIBLE, or GONE.
     */
    public void setFBVisibility(int visibility) {
        iv_fb.setVisibility(visibility);
    }

    /**
     *
     * @param visibility One of VISIBLE, INVISIBLE, or GONE.
     */
    public void setTwitterVisibility(int visibility) {
        iv_twitter.setVisibility(visibility);
    }

    /**
     *
     * @param visibility One of VISIBLE, INVISIBLE, or GONE.
     */
    public void setHeartVisibility(int visibility) {
        iv_heart.setVisibility(visibility);
    }

    /**
     *
     * @param visibility One of VISIBLE, INVISIBLE, or GONE.
     */
    public void setIVShareVisibility(int visibility) {
        iv_iv_share.setVisibility(visibility);
    }

    /**
     *
     * @param visibility One of VISIBLE, INVISIBLE, or GONE.
     */
    public void setDownloadProgressBarVisibility(int visibility) {
        downloadProgressBar.setVisibility(visibility);
    }

}
