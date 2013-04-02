package ca.ualberta.cs.team07recipefinder;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Copyright 2012  Bryan Liles <iam@smartic.us> and Abram Hindle <abram.hindle@softwareprocess.es> . All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are
 permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of
 conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list
 of conditions and the following disclaimer in the documentation and/or other materials
 provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY Bryan Liles <iam@smartic.us> and Abram Hindle <abram.hindle@softwareprocess.es> ''AS IS'' AND ANY EXPRESS OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those of the
 authors and should not be interpreted as representing official policies, either expressed
 or implied, of Bryan Liles <iam@smartic.us> and Abram Hindle <abram.hindle@softwareprocess.es>.

 * 
 */
public class BogoPicGen {
	public static Bitmap generateBitmap(int width, int height) {
		// Algorithms based on:
		// http://countercomplex.blogspot.com/2011/10/some-deep-analysis-of-one-line-music.html

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		int t = (int) (255.0 * Math.random());
		int r = (int) (255.0 * Math.random());
		int g = (int) (255.0 * Math.random());
		int b = (int) (255.0 * Math.random());
		int offset1 = (int) (255 * Math.random());
		int offset2 = (int) (255 * Math.random());
		int offset3 = (int) (255 * Math.random());
		int rm1 = 1 + (int) (12 * Math.random());
		int gm1 = 1 + (int) (12 * Math.random());
		int bm1 = 1 + (int) (12 * Math.random());
		int rm2 = 1 + (int) (12 * Math.random());
		int gm2 = 1 + (int) (12 * Math.random());
		int bm2 = 1 + (int) (12 * Math.random());
		int rm3 = 1 + (int) (12 * Math.random());
		int gm3 = 1 + (int) (12 * Math.random());
		int bm3 = 1 + (int) (12 * Math.random());

		int[] mods = { 65535, width, height, 255, 64, 32, 512, 1024, width,
				height, width, height };
		int rmod = mods[(int) (mods.length * Math.random())];
		int gmod = mods[(int) (mods.length * Math.random())];
		int bmod = mods[(int) (mods.length * Math.random())];

		int rs1 = 1 + (int) (11 * Math.random());
		int gs1 = 1 + (int) (11 * Math.random());
		int bs1 = 1 + (int) (11 * Math.random());
		int rs2 = 1 + (int) (11 * Math.random());
		int gs2 = 1 + (int) (11 * Math.random());
		int bs2 = 1 + (int) (11 * Math.random());

		int rf = (int) (2 * Math.random());
		int bf = (int) (2 * Math.random());
		int gf = (int) (2 * Math.random());

		int[] pixels = new int[width * height];
		float[] hsv = new float[3];
		boolean isHSV = (Math.random() > 0.5);
		int constantT = (Math.random() > 0.5) ? 1 : 0;
		int c;
		// public void setPixels (int[] pixels, int offset, int stride, int x,
		// int y, int width, int height)
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				r = (r
						+ offset1
						+ (b * rf | t * rm1 & t >> rs1 | t * rm2 & t >> rs2 | t
								* rm3 & t / 1024) - 1)
						% rmod;
				g = (g
						+ offset2
						+ (r * gf | t * gm1 & t >> gs1 | t * gm2 & t >> gs2 | t
								* gm3 & t / 1024) - 1)
						% gmod;
				b = (b
						+ offset3
						+ (g * bf | t * bm1 & t >> bs1 | t * bm2 & t >> bs2 | t
								* bm3 & t / 1024) - 1)
						% bmod;
				if (isHSV) {
					hsv[0] = (float) r / (float) 255.0;
					hsv[1] = (float) g / (float) 255.0;
					hsv[2] = (float) b / (float) 255.0;
					// int c = Color.rgb(r,g,b);
					c = Color.HSVToColor(hsv);
				} else {
					c = Color.rgb(r, g, b);
				}
				pixels[i * width + j] = c;
				t = t + constantT * 1;

			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
