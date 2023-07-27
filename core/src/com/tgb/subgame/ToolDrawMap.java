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

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.image.CoreImage;
import pulpcore.image.Colors;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.*;
import pulpcore.sprite.Sprite;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.ParticleManager;
import com.tgb.subengine.gamesystems.IGamePart;
import com.tgb.subengine.gfxentities.*;
import com.tgb.subgame.TacticalMapPC;
import com.tgb.subgame.LevelKeeper;

/**
 * @author Alain Becam
 *
 */
public class ToolDrawMap
{
	
	public final static int START_MENU=700; // Where the menu start (so where the map shouldn't be) 
	
	
	static final String myName = "DrawMap";
	
	Scene2D ourScene;
	CoreImage mapImage;

//	ImageSprite renderedMap;
	int[] rawData;
	FlatSpritePC ourMap;
	FlatSpritePC frontMap;
	long idOurMap;
	
	int [][] spriteBump; // Bump to add. 
	
	int xMin,yMin; // Where do we start on the globalMapImage
	int xCurr,yCurr;
	final public static int step=20; // Push one "explode" each 10 pixels.
	
	boolean finished=false;
	
	boolean isBlank=true;
	
	public ToolDrawMap()
	{
		;
	}
	

	/**
	 * Clean the map
	 */
	protected void cleanMap()
	{
		System.out.println("Clean the draw map");

		spriteBump = null;

		System.out.println("Tool draw map cleaned");
	}

	public void removeDrawing()
	{
		RenderingManager.getInstance().removeEntity(idOurMap, 1);
		ourMap=null;
	}
	int percentDone;

	public void transformRound()
	{
		for (double angleDraw=0;angleDraw < 2*Math.PI;angleDraw+=0.1)
		{	
				explodeArea(50, 50, 0, (int )(350+150*Math.cos(angleDraw)), (int )(400+150*Math.sin(angleDraw)));
		}
	}
	
	double angleToDraw=0;
	double dist=150;
	
	public void resetDraw()
	{
		angleToDraw=0;
		dist=150;
	}
	
	public boolean transformRoundStep()
	{
		for (int iStep=0;iStep < 5;iStep++)
		{	
				explodeArea(50, 50, 0, (int )(350+dist*Math.cos(angleToDraw)), (int )(400+dist*Math.sin(angleToDraw)));
				angleToDraw+=0.1;
				if (angleToDraw >= 2*Math.PI)
				{
					angleToDraw=0;
					dist-=20;
				}
				if (dist < 10)
				{
					return (true);
				}
		}
		return false;
	}
	
	
	public void explodeArea(int radius, int force, int dispersion, int x, int y)
    {
    	int xLow=x-radius;
    	int yLow=y-radius;
    	int xHigh=x+radius;
    	int yHigh=y+radius;
    	double xRel=0;
    	double yRel=0;
    	double xyRelStep=Math.PI/(2.0*(double ) radius);
    	int xBump=0;
    	int yBump=0;
    	
    	for (int xToExp=xLow;xToExp<xHigh;xToExp++)
    	{
    		for (int yToExp=yLow;yToExp<yHigh;yToExp++)
    		{
    			if ( (xToExp > 0) && (xToExp < Stage.getWidth()) && (yToExp > 0) && (yToExp < Stage.getHeight()) )
    			{
    				int rgbaValue=Colors.unpremultiply(rawData[yToExp*Stage.getWidth()+xToExp]);
    						
    				int altValue=rgbaValue & 0x00FFFFFF;
    				altValue+=spriteBump[xBump][yBump];//(int )(((double )force)*(Math.sin(xRel)*Math.sin(yRel)));
    				if (altValue > 0xFFFFFF)
    					altValue=0xFFFFFF;
    				

    				int aValue=Colors.getAlpha(rgbaValue);
    				aValue+=spriteBump[xBump][yBump];//(int )(((double )force)*(Math.sin(xRel)*Math.sin(yRel)));
    				if (aValue < 0)
    					aValue=0;
    				if (aValue > 255)
    					aValue=255;
    				
    				//System.out.println(xBump +" - "+yBump);
    				
    				rawData[yToExp*Stage.getWidth()+xToExp]=Colors.premultiply(aValue<<24 | altValue );
    			}
    			yRel+=xyRelStep;
    			xBump++;
    		}
    		yRel=0;
    		xRel+=xyRelStep;
    		xBump=0;
    		yBump++;
    	}
    }
	
	public void prepareBump(int radius, int force, int dispersion)
    {

    	int xHigh=radius*2;
    	int yHigh=radius*2;
    	double xRel=0;
    	double yRel=0;
    	double xyRelStep=Math.PI/(2.0*(double ) radius);
    	double distToCenter;
    	
    	spriteBump= new int[radius*2][radius*2];
    	System.out.println(radius + " - "+spriteBump.length);
    	
    	for (int xToExp=0;xToExp<xHigh;xToExp++)
    	{
    		for (int yToExp=0;yToExp<yHigh;yToExp++)
    		{
    			distToCenter=Math.sqrt((radius-xToExp)*(radius-xToExp)+(radius-yToExp)*(radius-yToExp));
    			
    			if (distToCenter >= radius)
    				spriteBump[xToExp][yToExp]=0;
    			else
    				spriteBump[xToExp][yToExp]=(int )(((double )force)*(Math.cos((Math.PI/2)*distToCenter/radius)));
    			
    			//spriteBump[xToExp][yToExp]=(int )(((double )force)*(Math.sin(xRel)*Math.sin(yRel)));
    			yRel+=xyRelStep;
    		}
    		yRel=0;
    		xRel+=xyRelStep;
    	}
    }
	
	public void prepareBump2(int radius, int force, int dispersion,double variation)
    {

    	int xHigh=radius*2;
    	int yHigh=radius*2;
    	double xRel=0;
    	double yRel=0;
    	double xyRelStep=Math.PI/(2.0*(double ) radius);
    	double distToCenter;
    	
    	spriteBump= new int[radius*2][radius*2];
    	System.out.println(radius + " - "+spriteBump.length);
    	
    	for (int xToExp=0;xToExp<xHigh;xToExp++)
    	{
    		for (int yToExp=0;yToExp<yHigh;yToExp++)
    		{
    			distToCenter=Math.sqrt((radius-xToExp)*(radius-xToExp)+(radius-yToExp)*(radius-yToExp));
    			
    			if (distToCenter >= radius)
    				spriteBump[xToExp][yToExp]=0;
    			else
    				spriteBump[xToExp][yToExp]=(int )(((double )force)*(Math.cos((Math.PI/2)*distToCenter/radius))*(1+Math.random()*variation));
    			
    			//spriteBump[xToExp][yToExp]=(int )(((double )force)*(Math.sin(xRel)*Math.sin(yRel)));
    			yRel+=xyRelStep;
    		}
    		yRel=0;
    		xRel+=xyRelStep;
    	}
    }
	
	public void convertRelief()
	{
		for (int xDrawMap = 0 ; xDrawMap < Stage.getWidth(); xDrawMap++)
		{
			for (int yDrawMap = 0 ; yDrawMap < Stage.getHeight(); yDrawMap++)
			{
				int rgbaValue=Colors.unpremultiply(rawData[yDrawMap*Stage.getWidth()+xDrawMap]);
				
				int altValue=rgbaValue & 0x00FFFFFF;
				
				int aValue=Colors.getAlpha(rgbaValue);
				
				int rValue=0;
				int bValue=0;
				int gValue=0;
				
				if (altValue < 700)
				{
					gValue=altValue/6;
					bValue=altValue/14;
					rValue=(1000-altValue)/16;
				}
				if ((altValue >= 700) && (altValue < 2500))
				{
					gValue=116+(altValue-700)/8;
					bValue=50+(altValue-700)/8;
					rValue=19+(altValue-700)/8;
//					if (gValue < 100)
//						gValue=100;
					if (gValue > 150)
						gValue=150;
					if (bValue > 150)
						bValue=150;
					if (rValue > 150)
						rValue=150;
				}
				if (altValue >= 2500)
				{
					gValue=150+(altValue-2500)/6;
					bValue=150+(altValue-2500)/6;
					rValue=150+(altValue-2500)/4;
					if (gValue > 255)
						gValue=255;
					if (bValue>255)
						bValue=255;
					if (rValue > 255)
						rValue=255;
				}
				
				rawData[yDrawMap*Stage.getWidth()+xDrawMap]=Colors.premultiply(aValue<<24 | rValue<<16 | gValue << 8 | bValue );
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#start()
	 */
	public void start()
	{
		// TODO Auto-generated method stub
		System.out.println("Create draw map");
		
		LevelKeeper.getInstance().reset();
		
		ourScene=(Scene2D )Stage.getScene();
		
		// For test first !!!
		xMin= LevelKeeper.getInstance().getNextMapX(); // (int )(Math.random()*664);//420;
		yMin= LevelKeeper.getInstance().getNextMapY(); // (int )(Math.random()*760); //266;
		
		// Init of the cursor, might change for non-visual tranformation
		xCurr=0;
		yCurr=0;
		
		finished=false;
		
		isBlank=true;
		
		//mapImage = new CoreImage(Stage.getWidth(), Stage.getHeight(),false);
		mapImage=LevelKeeper.getInstance().getCurrentLevel();
		
		rawData = mapImage.getData();
		
		ourMap= new FlatSpritePC(mapImage);
		ourMap.setPos(Stage.getWidth()/2,  Stage.getHeight()/2, 0);
		
		idOurMap = RenderingManager.getInstance().addDrawableEntity(ourMap,1);
//
//		renderedMap = new ImageSprite(globalMapImage,0,0,Stage.getWidth(),Stage.getHeight());
//		
//		renderedMap.alpha.set(55);
//		//renderedMap.setBlendMode(BlendMode.Add());
//		ourScene.add(renderedMap);
		// Add background
		//add(new FilledSprite(0xFF00608A));
	
		
		
		int iIndex=0;
		
		for (int xDrawMap = 0 ; xDrawMap < Stage.getWidth(); xDrawMap++)
		{
			for (int yDrawMap = 0 ; yDrawMap < Stage.getHeight(); yDrawMap++)
			{
				rawData[iIndex++]=0x00000000; // (int )(0x8FFFFFFF*Math.random()); //
			}
		}
		
		//this.prepareBump(50, 20, 0);
		this.prepareBump2(50, 20, 0,0.5);
		
		percentDone = 0;
	}
}
