/*
 * Copyright (c) 2013 Kirusa, Inc.
 * All rights reserved. Patents pending.
 *
 * This software is the confidential and proprietary information of
 * Kirusa, Inc.  Use it only in accordance with the terms of the
 * license agreement with Kirusa.
 *
 * KIRUSA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. KIRUSA
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 *
 * File: MediaCOnverterMgr.java
 * Author: Ajay
 * Created on: Feb 03, 2014
 */

package com.kirusa.opus.service;

import android.util.Log;

import com.kirusa.opus.header.OpusDecoder;
import com.kirusa.opus.header.OpusEncoder;
import com.srinivas.mudavath.utils.MediaMgr;

import java.io.File;

public class MediaConverterMgr {

	public static int opusToPcm(String opusFilePath, String pcmFilePath) {

		long stime = 0; //System.currentTimeMillis();

			stime = System.currentTimeMillis();
			Log.d("IVBlogs", "opusToPcm opusFilePath [" + opusFilePath
					+ "] pcmFilePath [" + pcmFilePath + "]");
		int value = -2;
		String tmpFilePath = null;
		try {
			String arr[] = new String[2];
			arr[0] = opusFilePath;
			arr[1] = pcmFilePath;
			// int value = opusDecoder.DoOpusDecode(arr.length, arr);

			// String outFile = file.getParentFile().getAbsolutePath()
			// + File.pathSeparator
			// + file.getName().substring(0, file.getName().indexOf("."))
			// + ".pcm";
			tmpFilePath = MediaMgr.getPcmFileName(opusFilePath);
			value = OpusDecoder.opusDecode(8000, opusFilePath, tmpFilePath,
					pcmFilePath);
		} catch (Exception e) {
			Log.e("IVBlogs", "opusToPcm opusFilePath [" + opusFilePath
					+ "] pcmFilePath [" + pcmFilePath + "] reason ["
					+ e.getMessage() + "]");
			return value;
		} finally {
			if (tmpFilePath != null) {
				File file = new File(tmpFilePath);
				file.delete();
			}
			if (opusFilePath != null) {
				File file1 = new File(opusFilePath);
				file1.delete();
			}
		}
		Log.d("IVBlogs", "opusToPcm response [" + value + "] time ["
				+ (System.currentTimeMillis() - stime) + "]");

//		System.out.println("opusToPcm response [" + value + "] time ["
//				+ (System.currentTimeMillis() - stime) + "]");
				
		return value;
	}

	public static int opusToAlaw(String opusFilePath, String alawFilePath) {
		// TODO
		return 0;
	}

	public static int pcmToOpus(String pcmFilePath, String opusFilePath) {

		long stime = 0; //System.currentTimeMillis();

		stime = System.currentTimeMillis();
		Log.d("IVBlogs", "pcmToOpus pcmFilePath [" + pcmFilePath
					+ "] opusFilePath [" + opusFilePath + "]");


		int value = -2;
		String arr[] = new String[4];
		arr[0] = "--bitrate";
		arr[1] = "16";
		arr[2] = pcmFilePath;
		arr[3] = opusFilePath;
		try {
			value = OpusEncoder.opusEncodee(8000, 12000, 1104, pcmFilePath,
					opusFilePath);// DoOpusEncode(arr.length, arr);
		} catch (Exception e) {
			Log.e("IVBlogs", "pcmToOpus pcmFilePath [" + pcmFilePath
					+ "] opusFilePath [" + opusFilePath + "] reason ["
					+ e.getMessage() + "]");
			return value;
		}
		Log.d("IVBlogs", "pcmToOpus response [" + value + "] time ["
				+ (System.currentTimeMillis() - stime) + "]");

//		System.out.println("pcmToOpus response [" + value + "] time ["
//				+ (System.currentTimeMillis() - stime) + "]");
		return value;
	}
}
