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
 * File: AbstractCoder.java
 * Author: Prafulla Kashyap
 * Created on: Jan 30, 2014
 */
package com.kirusa.opus.header;

/**
 * @author Gautam Singh
 * 
 */
public abstract class AbstractCoder {

	public static boolean isOpusEnabled;

	static {
		try {
			System.loadLibrary("kopus");
			isOpusEnabled = true;
		} catch (Exception e) {
			isOpusEnabled = false;
		}
	}
}
