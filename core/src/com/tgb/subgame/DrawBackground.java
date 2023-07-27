/*
 * Created on 10 nov 2008
 */
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

/** 
 * Facility to draw the different needed backgrounds.
 * Copyright (c)2008 Alain Becam
 */

package com.tgb.subgame;

import pulpcore.Stage;
import pulpcore.image.CoreImage;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subengine.gfxentities.FlatSpritePCSpec;

public class DrawBackground
{	
	public static final int WIN = 0;
	public static final int LOSE = 1;
	public static final int NO_AMMO_LEFT = 2;
	
	//static FlatSpritePCSpec background;
	static FlatSpritePC background;
	static FlatSpritePC backgroundIntro;
	static FlatSpritePC backgroundTrans;
	static FlatSpritePC frontWinLose;
	static FlatSpritePC backgroundMap;
	static long idBack,idBackSpl,idBackSpl2,idFrontWL,idBackMap;
	static boolean firstTime=true;
	static boolean firstTimeSplash=true;
	static boolean firstTimeMap=true;
	
	public static void drawBack()
	{
		if (firstTime)
		{
			CoreImage backImage = CoreImage.load("Water3.png");
			CoreImage backImage2 = backImage.scale(Stage.getWidth(), Stage.getHeight());
			// Add background
			//add(new FilledSprite(0xFF00608A));
			//background = new FlatSpritePCSpec(backImage2);
			background = new FlatSpritePC(backImage2);
			background.setPos(Stage.getWidth()/2, Stage.getHeight()/2, 0);
			firstTime = false;
		}
		if (RenderingManager.getInstance().in3D)
			background.is3D = false;
    	idBack = RenderingManager.getInstance().addDrawableEntity(background,0);
	}
	
	public static void drawBackMap()
	{
		if (firstTimeMap)
		{
			CoreImage mainMap = CoreImage.load("MapVis.png");
			backgroundMap = new FlatSpritePC(mainMap);
	    	backgroundMap.setPos(350, mainMap.getHeight()/2, 0);	    

			firstTimeMap = false;	
		}
		idBackMap = RenderingManager.getInstance().addDrawableEntity(backgroundMap,1);
	}
	
	public static void drawBackSplash()
	{
		//if (firstTimeSplash)
		{
			CoreImage backImage = CoreImage.load("WaterSplash2.png");
			CoreImage backImage2 = backImage.scale(Stage.getWidth(), Stage.getHeight());
			
			backgroundIntro = new FlatSpritePC(backImage2);
			backgroundIntro.setPos(Stage.getWidth()/2, Stage.getHeight()/2, 0);
			
			CoreImage backImageTrans = CoreImage.load("WaterSplash3.png");
			CoreImage backImageTrans2 = backImageTrans.scale(Stage.getWidth(), Stage.getHeight());
			// Add background
			//add(new FilledSprite(0xFF00608A));
			backgroundTrans = new FlatSpritePC(backImageTrans2);
			backgroundTrans.setPos(Stage.getWidth()/2, Stage.getHeight()/2, 0);
			firstTimeSplash = false;
		}
    	idBackSpl = RenderingManager.getInstance().addDrawableEntity(backgroundIntro,0);
    	idBackSpl2 = RenderingManager.getInstance().addDrawableEntity(backgroundTrans,0);
	}
	
	public static void revealTitle()
	{
		// Remove the background update, so the explosions (and others) will stay in the
		// mask
		 RenderingManager.getInstance().removeEntity(idBackSpl, 0);
	}

	public static void resetSplash()
	{
		idBackSpl = RenderingManager.getInstance().addDrawableEntity(background,0);
	}
	
	public static void removeSplash()
	{
		RenderingManager.getInstance().removeEntity(idBackSpl , 0);
		RenderingManager.getInstance().removeEntity(idBackSpl2 , 0);
		// We do not need them for a while, so save a little bit of memory
		// by forcing their removing
		backgroundIntro = null;
		backgroundTrans = null;
	}
	
	public static void reset()
	{
		idBack = RenderingManager.getInstance().addDrawableEntity(background,0);
	}
	
	public static void remove()
	{
		RenderingManager.getInstance().removeEntity(idBack , 0);
		RenderingManager.getInstance().removeEntity(idBackMap , 1);
	}
	
	public static void removeMap()
	{
		RenderingManager.getInstance().removeEntity(idBackMap , 1);
	}
	
	public static void drawWinLose(int kindOfWon)
	{
		CoreImage frontImage;
		
		switch (kindOfWon)
		{
			case WIN:
			{
				frontImage = CoreImage.load("Win.png");
			}
			break;
			case LOSE:
			{
				frontImage = CoreImage.load("Lose.png");
			}
			break;
			case NO_AMMO_LEFT:
			{
				frontImage = CoreImage.load("No_ammo_left.png");
			}
			break;
			default:
			{
				frontImage = CoreImage.load("Unknown.png");
			}
		}
		
		frontWinLose = new FlatSpritePC(frontImage);
		frontWinLose.setPos(Stage.getWidth()/3, Stage.getHeight()/2, 0);
		
		idFrontWL = RenderingManager.getInstance().addDrawableEntity(frontWinLose,39);
	}
	
	public static void unload()
	{
		RenderingManager.getInstance().removeEntity(idBackSpl , 0);
		RenderingManager.getInstance().removeEntity(idBackSpl2 , 0);
		RenderingManager.getInstance().removeEntity(idBack , 0);
		RenderingManager.getInstance().removeEntity(idBackMap , 1);
		background=null;
		backgroundTrans=null;
		frontWinLose=null;
		backgroundMap=null;
	}
}
