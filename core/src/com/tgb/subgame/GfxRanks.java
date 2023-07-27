/*
 * This software is distributed under the MIT License
 *
 * Copyright (c) 2008-2020 Alain Becam
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:

 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
*/

package com.tgb.subgame;

import pulpcore.image.CoreImage;

/**
 * Static facility class to create the units' ranks
 * @author Alain Becam
 *
 */
public class GfxRanks
{
	public static CoreImage imageR1Ball;
	public static CoreImage imageR2Ball;
	public static CoreImage imageR3Ball;
	public static CoreImage imageRBBall;
	public static CoreImage imageR1SilverBall;
	public static CoreImage imageR2SilverBall;
	public static CoreImage imageR3SilverBall;
	public static CoreImage imageRBSilverBall;
	public static CoreImage imageR1GoldBall;
	public static CoreImage imageR2GoldBall;
	public static CoreImage imageR3GoldBall;
	public static CoreImage imageRBGoldBall;
	public static CoreImage imageRCrystalBall;
	public static CoreImage imageRShiningBall;
	
	
	public static boolean created=false;
	
	public static void callMeFirst()
	{
		if (!created)
			createGfx();
		created = true;
	}
	
	public static void removeMe()
	{
		imageR1Ball = null;
		imageR2Ball = null;
		imageR3Ball = null;
		imageRBBall = null;
		imageR1SilverBall = null;
		imageR2SilverBall = null;
		imageR3SilverBall = null;
		imageRBSilverBall = null;
		imageR1GoldBall = null;
		imageR2GoldBall = null;
		imageR3GoldBall = null;
		imageRBGoldBall = null;
		imageRCrystalBall = null;
		imageRShiningBall = null;
		
	}
	
	public static void createGfx()
	{	
		imageR1Ball = CoreImage.load("BallNorm1.png");
		imageR2Ball = CoreImage.load("BallNorm2.png");;
		imageR3Ball = CoreImage.load("BallNorm3.png");;
		imageRBBall = CoreImage.load("BallNorm4.png");;
		imageR1SilverBall = CoreImage.load("BallSilver1.png");;
		imageR2SilverBall = CoreImage.load("BallSilver2.png");;
		imageR3SilverBall = CoreImage.load("BallSilver3.png");;
		imageRBSilverBall = CoreImage.load("BallSilver4.png");;
		imageR1GoldBall = CoreImage.load("BallGold1.png");;
		imageR2GoldBall = CoreImage.load("BallGold2.png");;
		imageR3GoldBall = CoreImage.load("BallGold3.png");;
		imageRBGoldBall = CoreImage.load("BallGold4.png");;
		imageRCrystalBall = CoreImage.load("BallCristal.png");;
		imageRShiningBall = CoreImage.load("BallShining.png");;
	}

	public static boolean isCreated()
	{
		return created;
	}

	public static CoreImage getImageR1Ball()
	{
		return imageR1Ball;
	}

	public static CoreImage getImageR2Ball()
	{
		return imageR2Ball;
	}

	public static CoreImage getImageR3Ball()
	{
		return imageR3Ball;
	}

	public static CoreImage getImageRBBall()
	{
		return imageRBBall;
	}

	public static CoreImage getImageR1SilverBall()
	{
		return imageR1SilverBall;
	}

	public static CoreImage getImageR2SilverBall()
	{
		return imageR2SilverBall;
	}

	public static CoreImage getImageR3SilverBall()
	{
		return imageR3SilverBall;
	}

	public static CoreImage getImageRBSilverBall()
	{
		return imageRBSilverBall;
	}

	public static CoreImage getImageR1GoldBall()
	{
		return imageR1GoldBall;
	}

	public static CoreImage getImageR2GoldBall()
	{
		return imageR2GoldBall;
	}

	public static CoreImage getImageR3GoldBall()
	{
		return imageR3GoldBall;
	}

	public static CoreImage getImageRBGoldBall()
	{
		return imageRBGoldBall;
	}

	public static CoreImage getImageRCrystalBall()
	{
		return imageRCrystalBall;
	}

	public static CoreImage getImageRShiningBall()
	{
		return imageRShiningBall;
	}
}
