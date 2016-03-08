package com.srinivas.mudavath.utils;

public class MediaMgr {
	public static final boolean isOpusEnabled = true;

	// set it to .wav if need to enable PCM format.
	public static final String MEDIA_FORMAT_DEFAULT_EXTN = ".iv";
	public static final String MEDIA_FORMAT_PCM = "pcm";
	public static final String MEDIA_FORMAT_OPUS = "opus";
	public static final String MEDIA_FORMAT_OPUS_EXTN = ".opus";
	public static final String MEDIA_FORMAT_IV_EXTN = ".iv";
	public static final String MEDIA_FORMAT_MP4_EXTN = ".mp4";
	public static final String MEDIA_FORMAT_WAV_EXTN = ".wav";
	private static final CharSequence MEDIA_FORMAT_PCM_EXTN = ".pcm";

	/**
	 * @param alawFileName
	 * @return
	 */
	public static String getOpusFileName(String alawFileName) {
		return (alawFileName.replace(MEDIA_FORMAT_WAV_EXTN,
				MEDIA_FORMAT_DEFAULT_EXTN));
	}

	public static String getAlawFileName(String opusFileName) {
		if (opusFileName.endsWith(MEDIA_FORMAT_IV_EXTN)) {
			return (opusFileName.replace(MEDIA_FORMAT_IV_EXTN,
					MEDIA_FORMAT_WAV_EXTN));
		} else {
			return (opusFileName.replace(MEDIA_FORMAT_OPUS_EXTN,
					MEDIA_FORMAT_WAV_EXTN));
		}
	}

	public static String getPcmFileName(String opusFileName) {
		if (opusFileName.endsWith(MEDIA_FORMAT_IV_EXTN)) {
			return (opusFileName.replace(MEDIA_FORMAT_IV_EXTN,
					MEDIA_FORMAT_PCM_EXTN));
		} else {
			return (opusFileName.replace(MEDIA_FORMAT_OPUS_EXTN,
					MEDIA_FORMAT_PCM_EXTN));
		}
	}

	public static boolean isOpusFile(String filePath) {
		boolean isOpus = false;
		if (filePath.endsWith(MEDIA_FORMAT_IV_EXTN)
				|| filePath.endsWith(MEDIA_FORMAT_OPUS_EXTN))
			isOpus = true;

		return isOpus;
	}

	public static String getMediaExtn(String msgFormat) {
		return MEDIA_FORMAT_OPUS.equals(msgFormat) ? MEDIA_FORMAT_IV_EXTN
				: MEDIA_FORMAT_WAV_EXTN;
	}

	public static String getMediaFormat() {
		if (isOpusEnabled)
			return MEDIA_FORMAT_OPUS;
		else
			return MEDIA_FORMAT_PCM;
	}

}
