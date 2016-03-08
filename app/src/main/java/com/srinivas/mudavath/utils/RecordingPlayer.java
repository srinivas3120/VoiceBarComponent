package com.srinivas.mudavath.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.kirusa.opus.service.MediaConverterMgr;

import java.io.File;
import java.io.IOException;

/**
 * The RecordingPlayer class is used to handle the various stage of media player
 * like start, stop resume etc.
 *
 * @author Mudavath Srinivas
 */
public class RecordingPlayer {
    private static RecordingPlayer playerObj = null;
    private String TAG = "RecordingPlayer";
    private static MediaPlayer mediaPlayer = null;
    private boolean playFlag = true, res = false;
    private int playedDurationRp;
    private String filePathRp;
    //	private int seekTo;
    public static boolean prevSpeakerOn = false;
    public static int prevAudioMode = -1;
    private static boolean audioValuesChanged = false;


    Context mctx;

    private RecordingPlayer() {
//		mELogger = new ELogger();
//		mELogger.setTag(TAG);
    }

    public static RecordingPlayer getInstance() {
        if (playerObj == null)
            playerObj = new RecordingPlayer();
        return playerObj;
    }

    public void replay(int mode, Context ctx) {
        if (TextUtils.isEmpty(filePathRp))
            return;
        playVoicMsg(mode, filePathRp, mediaPlayer.getCurrentPosition(), ctx);
    }

    /**
     * This method used for playing a voice Message
     *
     * @param filePath
     */
    public boolean playVoicMsg(int mode, String filePath, int plyedDuration,
                               Context ctx) {

        mctx = ctx;
        /*if (Engine.IS_INTERNAL_RELEASE)// This log is not print now.
			mELogger.debug("RecordingPlayer : playVoiceMsg() : ENTER");*/
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            if (mediaPlayer.isPlaying()) {
//				stopPlaying(ctx);
            }

        }
        playedDurationRp = plyedDuration;
        filePathRp = filePath;
        return playMedia(mode, filePath, plyedDuration, ctx);
    }

    private boolean playMedia(int mode, String filePath, int plyedDuration,
                              Context ctx) {
        try {

            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            AudioManager am = (AudioManager) ctx
                    .getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            prevSpeakerOn = am.isSpeakerphoneOn();
            prevAudioMode = am.getMode();
            System.out.println("RecordingPlayer prevSpeakerOn :: " + prevSpeakerOn);
            System.out.println("RecordingPlayer am mode :: " + am.getMode());
			
			/*switch (mode) {
			case ConversationActivity.SPEAKER:
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				if(am.isSpeakerphoneOn() == false)
				{
					am.setSpeakerphoneOn(true);
					am.setMode(AudioManager.STREAM_MUSIC);
				}
				break;
			case ConversationActivity.MUTE:
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
				if(am.isSpeakerphoneOn() == true)
				{
					am.setSpeakerphoneOn(false);
					am.setMode(AudioManager.STREAM_MUSIC);
				}
				break;
			case ConversationActivity.BLUETOOTH:
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				AudioManager audioManager = (AudioManager) ctx
						.getSystemService(Context.AUDIO_SERVICE);
				if (Engine.IS_INTERNAL_RELEASE)// This log is not print now.
					mELogger.debug("RecordingPlayer : isBluetoothA2dpOn() : "
							+ audioManager.isBluetoothA2dpOn()
							+ " isBluetoothScoOn  "
							+ audioManager.isBluetoothScoOn());
				break;
			case ConversationActivity.WIREDHEADSET:
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

				break;

			}*/
            audioValuesChanged = true;
            if (MediaMgr.isOpusFile(filePath)) {
                String pcmFilePath = MediaMgr.getAlawFileName(filePath);
                if (!new File(pcmFilePath).exists()) {
                    MediaConverterMgr.opusToPcm(filePath, pcmFilePath);

                }
                filePath = pcmFilePath;
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(filePath);
        } catch (Exception e1) {
            return false;
        }

        try {
            mediaPlayer.prepare();
            handleMediaPlayerEvent(plyedDuration, ctx);
            Log.e("IVBlogs", "plyedDuration : " + plyedDuration);
        } catch (IllegalStateException e) {
            return false;
        } catch (IOException e) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            return false;
        }

        return true;

    }

    /**
     * This method is used for stop a current playing message
     *
     * @param filePath
     */
    public boolean stopPlaying() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            //Reset all the mediaplayer and audioManager properties to default once stopped
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            AudioManager am = (AudioManager) mctx
                    .getSystemService(Context.AUDIO_SERVICE);

            System.out.println("RecordingPlayer :: audioValuesChanged :: " + audioValuesChanged);
            if (audioValuesChanged) {
                am.setSpeakerphoneOn(prevSpeakerOn);
                am.setMode(prevAudioMode);
                audioValuesChanged = false;
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            am.abandonAudioFocus(null);

        } else {
        }
        return false;
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
        }
        return false;
    }

    /**
     * This method is used for pause a current playing Message
     */
    public boolean pausePlaying() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
            }
        } else {
        }

        return true;
    }

    /**
     * This method resume a pause message
     *
     * @return
     */
	/*
	 * public boolean resumePlaying(String filePath , int playedDuration) {
	 * if(mediaPlayer != null) { if(!mediaPlayer.isPlaying()) {
	 * mediaPlayer.start(); if(Engine.IS_INTERNAL_RELEASE)//This log is not
	 * print now.
	 * mELogger.debug("RecordingPlayer resumePlaying() : resume playing"); }
	 * else { if(Engine.IS_INTERNAL_RELEASE)//This log is not print now.
	 * mELogger.error("RecordingPlayer resumePlayContent() : already playing");
	 * } } else { playVoicMsg(filePath,playedDuration); }
	 * 
	 * 
	 * 
	 * return true; }
	 */
    public void handleMediaPlayerEvent(final int seekTo, final Context ctx) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mp == null) {
                        Log.e("IVBlogs", "MP is Null");
                        return;
                    }
                    Log.e("IVBlog", "SeekTo : " + seekTo);
                    playFlag = true;
                    mp.start();
                    mp.seekTo(seekTo);

                }
            });

			/*
			 * implement completion listener
			 */
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playFlag = false;
                    ((AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE))
                            .abandonAudioFocus(null);

                }
            });

			/*
			 * handle error event
			 */
            mediaPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                            break;

                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            break;

                        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                            break;

                        default:
                            // Toast.makeText(getApplicationContext(),
                            // "Requested file could not be found",
                            // Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
        }
    }


    /**
     * This function is to get current time of playing file
     *
     * @return
     */
    public int getCurrentTime() {
        int curTime = 0;
        if (mediaPlayer != null) {
            curTime = mediaPlayer.getCurrentPosition();
        }
        return curTime;
    }

    /**
     * This function is to get total duration of audio file
     *
     * @return
     */
    public int getPlayedDuration() {
        int totalDuration = 0;
        if (mediaPlayer != null)
            totalDuration = mediaPlayer.getCurrentPosition();
        return totalDuration;

    }

    public void forwardVoiceMsg(final Context ctx) {
        int val = 0, duration = 0;
        if (mediaPlayer != null) {
            val = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();
            Log.e("IVBlogs", "Total val : " + duration);
        }
        Log.e("IVBlogs", "val : " + val);
        int seekTO = (val + 5 * 1000);
        Log.e("IVBlogs", "seekTO : " + seekTO);
        if (seekTO <= duration) {
            Log.e("IVBlogs", "seekTO Forward : " + seekTO);
            mediaPlayer.seekTo(seekTO);
        } else {
            Log.e("IVBlogs", "seekTO Forward else : " + seekTO);
        }
    }

    public void backwardVoiceMsg(final Context ctx) {
        int val = 0, duration = 0;
        if (mediaPlayer != null) {
            val = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();
            Log.e("IVBlogs", "Total val : " + duration);
        }
        Log.e("IVBlogs", "val : " + val);
        int seekTO = (val - 5 * 1000);
        Log.e("IVBlogs", "seekTO : " + seekTO);
        if (seekTO <= duration) {
            Log.e("IVBlogs", "seekTO Forward : " + seekTO);
            mediaPlayer.seekTo(seekTO);
        } else {
            Log.e("IVBlogs", "seekTO Forward else : " + seekTO);
        }
    }

    public int getCurrentPosition() {
        int currentPosition = 0;
        if (mediaPlayer != null)
            currentPosition = mediaPlayer.getCurrentPosition();
        return currentPosition;
    }

    public int getTotalDuration() {
        int duration = 0;
        if (mediaPlayer != null)
            duration = mediaPlayer.getDuration() / 1000;
        return duration;
    }

    public int getTotalDuration(Uri path, Context context) {
        int duration = 0;
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        Log.e("duration", "path : " + path);
        duration = mediaPlayer.create(context, path).getDuration() / 1000;
        Log.e("duration", "duration : " + duration);

        return duration;
    }
}
