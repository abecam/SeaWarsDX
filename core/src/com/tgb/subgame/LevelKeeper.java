/*
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

import pulpcore.Stage;
import pulpcore.image.Colors;
import pulpcore.image.CoreImage;

/**
 * Simple facility class to keep a level transformed by a special class into another.
 * @author Alain Becam
 *
 */

public class LevelKeeper {
	
	public static final int MAIN_MENU = -1;
	public static final int GENERATED_LEVEL = 1;
	public static final int GENERATED_QUICK_LEVEL = 2;
	public static final int WON_SCREEN = 3;
	public static final int TUTORIAL_LEVEL = 4;
	public static final int SUB_GENERATED_LEVEL = 5;
	public static final int SUB_GENERATED_QUICK__LEVEL = 6;
	
	
	
	CoreImage currentLevel=null;
	boolean firstInit=true;
	boolean ready=false;
	boolean isBlank=true;
	
	int[] rawData;
	protected int nextLevelWanted = -1; // which level will the "level map" load using the "LevelLoader"
	
	transient protected int currentMap = -1; // which map are we playing on.
	
	transient static private LevelKeeper instance=null;
	
	int nextMapX,nextMapY;
	
	private LevelKeeper()
	{
		currentLevel = new CoreImage(Stage.getWidth(), Stage.getHeight(),false);
		rawData=currentLevel.getData();
	}

	public static LevelKeeper getInstance()
	{
		if (instance== null)
			instance = new LevelKeeper();
		
		return instance;
	}
	
	public static void unload()
	{
		if (instance != null)
		{
			instance.removeAll();
			instance=null;
		}
	}
	
	public void removeAll()
	{
		currentLevel=null;
		rawData=null;
	}
	
	public void setNextMapCoord(int x,int y)
	{
		nextMapX=x;
		nextMapY=y;
	}
	
	
	public int getNextMapX() {
		return nextMapX;
	}

	public void setNextMapX(int nextMapX) {
		this.nextMapX = nextMapX;
	}

	public int getNextMapY() {
		return nextMapY;
	}

	public void setNextMapY(int nextMapY) {
		this.nextMapY = nextMapY;
	}

	public CoreImage getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(CoreImage currentLevel) {
		this.currentLevel = currentLevel;
		rawData=currentLevel.getData();
		ready=true;
	}
	
	public void reset() {
		firstInit=true;
	}
	
	public boolean loadSprite()
	{
		if (firstInit && ready)
		{
			firstInit=false;
			return true;
		}
		else
			return false;
	}
	
	public int getAlpha(int x,int y)
	{
		if ((x < Stage.getWidth()) && (x > 0) && (y < Stage.getHeight()) && (y > 0))
		{
			int returnValue=rawData[y*Stage.getWidth()+x]>>24;
			//System.out.println("Alpha "+returnValue+ "~alpha "+~returnValue);
			if (returnValue < 0)
				returnValue=256+returnValue;
			return (returnValue);
		}
		else
			return 0;
	}

	public boolean isBlank() {
		return isBlank;
	}

	public void setBlank(boolean isBlank) {
		this.isBlank = isBlank;
	}

	public int getNextLevelWanted()
	{
		return nextLevelWanted;
	}

	public void setNextLevelWanted(int nextLevelWanted)
	{
		this.nextLevelWanted = nextLevelWanted;
	}
}
