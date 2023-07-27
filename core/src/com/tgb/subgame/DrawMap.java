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
 * Generate and draw a zoom on the general map. We do that by actually drawing 
 * using a bump buffer to have a relief on 24 bits and an alpha, 
 * then we convert that to a picture using the alpha channel for the water.
 * @author Alain Becam
 *
 */
public class DrawMap implements IGamePart
{
	
	public final static int START_MENU=700; // Where the menu start (so where the map shouldn't be) 
	
	
	static final String myName = "DrawMap";
	
	Scene2D ourScene;
	CoreImage mapImage;
	Label progress;
	FilledSprite progressBack;
	FilledSprite progressBack2;
//	ImageSprite renderedMap;
	int[] rawData;
	FlatSpritePC ourMap;
	long idOurMap;
	
	int [][] spriteBump; // Bump to add. 
	
	CoreImage globalMapImage;
	int[] rawRefData;
	int xMin,yMin; // Where do we start on the globalMapImage
	int xCurr,yCurr;
	final public static int step=20; // Push one "explode" each 10 pixels.
	
	boolean finished=false;
	
	boolean isBlank=true;
	
	public DrawMap()
	{
		// Load the global map ref
		globalMapImage = CoreImage.load("MapRef.png");
		
		rawRefData= globalMapImage.getData();
	}
	

	/**
	 * Clean the map
	 */
	protected void cleanMap()
	{
		System.out.println("Clean the draw map");
		// TODO Auto-generated method stub
		
		//ourScene.remove(renderedMap);
		
		//RenderingManager.getInstance().removeEntity(idOurMap);
		
		RenderingManager.getInstance().washAllAndPrepare();
		RenderingManager.removeMe();
		ParticleManager.removeMe();
		
		//globalMapImage = null;
		//rawRefData = null;
		spriteBump = null;
		
		ourScene.remove(progress);
		ourScene.remove(progressBack);
		ourScene.remove(progressBack2);
		
		progress = null;
		progressBack = null;
		progressBack2 = null;
		
		System.out.println("Draw map cleaned");
	}

	int percentDone;
	
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#doLoop()
	 */
	public String doLoop()
	{
		boolean toTacticalMap=false;
		
		int mouseX=0,mouseY=0;
		boolean mousePressed=false;
		boolean rightMousePressed = false;
		
		mouseX = Input.getMouseX();
		mouseY = Input.getMouseY();
		
//		if (Input.isMousePressed())
//		{
//			mousePressed = true;
//			
////			int indexInRaw=mouseY*Stage.getWidth()+mouseX;
////			
////			rawData[indexInRaw]=0x00000000;
//			
//			explodeArea(100,50,5,mouseX,mouseY);
//		}
		
		if (!finished)
		{
			transformFromRef(2);
			percentDone+=50;
			progress.setText("Creating level - "+(percentDone/10)+"%");
			progressBack2.scaleTo((200*percentDone)/1000, 40, 0);
			
			if (finished)
			{
				this.convertRelief();
				LevelKeeper.getInstance().setCurrentLevel(mapImage);
				if (LevelKeeper.getInstance().getNextLevelWanted() == LevelKeeper.GENERATED_LEVEL)
				{
					return TacticalMapPC.myName;
				}
				else if (LevelKeeper.getInstance().getNextLevelWanted() == LevelKeeper.SUB_GENERATED_LEVEL)
				{
					return TacticalMapPCSubs.myName;
				}
				else if (LevelKeeper.getInstance().getNextLevelWanted() == LevelKeeper.GENERATED_QUICK_LEVEL)
				{
					double randomNb = Math.random()*100;
					
					if (randomNb > 50)
					{
						// Inform the Game keeper of the current type (default is good, so only the sub is needed), here for clarity only
						//LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.GENERATED_LEVEL);
						return TacticalMapPC.myName;
					}
					// Inform the Game keeper of the current type
					LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.SUB_GENERATED_QUICK__LEVEL);
					return TacticalMapPCSubs.myName;
				}
				else if (LevelKeeper.getInstance().getNextLevelWanted() == 3)
				{	
					return WonMap.myName;
				}
				else
				{	
					return ScriptedMap.myName;
				}
				
			}
		}
		
		if (Input.isDown(Input.KEY_R))
			this.convertRelief();
		
		if (Input.isDown(Input.KEY_S))
		{
			LevelKeeper.getInstance().setCurrentLevel(mapImage);		
		}
		
		if (Input.isDown(Input.KEY_Q))
			toTacticalMap=true;
		
		if (toTacticalMap)
			return TacticalMapPC.myName;
		else
			return null;
	}

	public void transformFromRef()
	{
		while (!finished)
		{
			int rgbaValue=Colors.unpremultiply(rawRefData[(yMin+yCurr)*700+(xMin+xCurr)]);
			//System.out.println("Working... "+(rgbaValue & 0xFF));	
			for (int iStep = 0 ; iStep < (((rgbaValue & 0xFF)-0x80)/2) ; iStep++)
			{
				isBlank=false;
				explodeArea(50, 50, 0, xCurr*step, yCurr*step);
			}
			//rawRefData[(yMin+yCurr)*700+(xMin+xCurr)]=Colors.premultiply(rgbaValue|0xFF);
			xCurr++;
			if ((xCurr*step) > Stage.getWidth())
			{
				xCurr=0;
				yCurr++;
			}
			if ((yCurr*step) > Stage.getHeight())
				finished=true;
		}
		LevelKeeper.getInstance().setBlank(isBlank);
	}
	
	public void transformFromRef(int yToDo)
	{
		int yDone=0;
		
		while ((!finished) && (yDone < yToDo))
		{
			
			int rgbaValue=Colors.unpremultiply(rawRefData[(yMin+yCurr)*700+(xMin+xCurr)]);
			//System.out.println("Working... "+(rgbaValue & 0xFF));	
			int maxValue = (((rgbaValue & 0xFF)-0x80)/2);
			if (maxValue > 20)
			{
				maxValue = 20;
			}
			for (int iStep = 0 ; iStep < maxValue ; iStep++)
			{
				isBlank=false;
				explodeArea(50, 50, 0, xCurr*step, yCurr*step);
			}
			//rawRefData[(yMin+yCurr)*700+(xMin+xCurr)]=Colors.premultiply(rgbaValue|0xFF);
			xCurr++;
			if ((xCurr*step) > Stage.getWidth())
			{
				xCurr=0;
				yCurr++;
				yDone++;
			}
			if ((yCurr*step) > Stage.getHeight())
				finished=true;
		}
		LevelKeeper.getInstance().setBlank(isBlank);
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
    	
    	int xToExp=xLow;
    	int yToExp=yLow;
    	int stageWidth=Stage.getWidth();
    	int stageHeight=Stage.getHeight();
    	
    	for (xToExp=xLow;xToExp<xHigh;xToExp++)
    	{
    		for (yToExp=yLow;yToExp<yHigh;yToExp++)
    		{
    			if ( (xToExp > 0) && (xToExp < stageWidth) && (yToExp > 0) && (yToExp < stageHeight) )
    			{
    				int rgbaValue=Colors.unpremultiply(rawData[yToExp*stageWidth+xToExp]);
    						
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
    				
    				rawData[yToExp*stageWidth+xToExp]=Colors.premultiply(aValue<<24 | altValue );
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
		int stageWidth=Stage.getWidth();
    	int stageHeight=Stage.getHeight();
    	
		for (int xDrawMap = 0 ; xDrawMap < stageWidth; xDrawMap++)
		{
			for (int yDrawMap = 0 ; yDrawMap < stageHeight; yDrawMap++)
			{
				int rgbaValue=Colors.unpremultiply(rawData[yDrawMap*stageWidth+xDrawMap]);
				
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
				
				rawData[yDrawMap*stageWidth+xDrawMap]=Colors.premultiply(aValue<<24 | rValue<<16 | gValue << 8 | bValue );
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#getStateName()
	 */
	public String getStateName()
	{
		// TODO Auto-generated method stub
		return myName;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#keyPressed(java.awt.event.KeyEvent)
	 */
	public String keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#keyReleased(java.awt.event.KeyEvent)
	 */
	public String keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#keyTyped(java.awt.event.KeyEvent)
	 */
	public String keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#mouseClicked(java.awt.event.MouseEvent)
	 */
	public String mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#mouseEntered(java.awt.event.MouseEvent)
	 */
	public String mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#mouseExited(java.awt.event.MouseEvent)
	 */
	public String mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#mousePosition(int, int)
	 */
	public String mousePosition(int x, int y)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#mousePressed(java.awt.event.MouseEvent)
	 */
	public String mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#mouseReleased(java.awt.event.MouseEvent)
	 */
	public String mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#quit()
	 */
	public void quit()
	{
		// TODO Auto-generated method stub
		try
		{
			System.out.println("Quit the draw map");
			cleanMap();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#sleep()
	 */
	public void sleep()
	{
		// TODO Auto-generated method stub

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
		
		//mapImage = new CoreImage(stageWidth, Stage.getHeight(),false);
		mapImage=LevelKeeper.getInstance().getCurrentLevel();
		
		rawData = mapImage.getData();
		
//		ourMap= new FlatSpritePC(mapImage);
//		ourMap.setPos(Stage.getWidth()/2,  Stage.getHeight()/2, 0);
		
//		idOurMap = RenderingManager.getInstance().addDrawableEntity(ourMap,1);
//
//		renderedMap = new ImageSprite(globalMapImage,0,0,Stage.getWidth(),Stage.getHeight());
//		
//		renderedMap.alpha.set(55);
//		//renderedMap.setBlendMode(BlendMode.Add());
//		ourScene.add(renderedMap);
		progressBack = new FilledSprite(450, 400, 200, 40, 0xffffffaa); 
		progressBack.setAnchor(Sprite.CENTER);
		ourScene.add(progressBack);
		progressBack2 = new FilledSprite(350, 400, 1, 40, 0xff22ff11); 
		progressBack2.setAnchor(Sprite.WEST);
		ourScene.add(progressBack2);
		progress = new Label("Creating level -  0%", 450, 400);
		progress.setAnchor(Sprite.CENTER);
		ourScene.add(progress);
		
		
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
