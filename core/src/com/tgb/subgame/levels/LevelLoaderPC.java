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

package com.tgb.subgame.levels;

import com.tgb.subgame.*;

/**
 * Super class to load a level, background+units.
 * Can be enhanced by subclasses.
 * 
 * For example (and the beginning), hard-coded levels!!
 * @author Alain Becam
 *
 */
public class LevelLoaderPC implements ILevelPC
{
	private ILevelPC currentLevel;
	private LevelMap theMap;
	
	public LevelLoaderPC (LevelMap theMap)
	{
		this.theMap=theMap;
	}
	
	public void loadLevel(int level)
	{
		switch (level)
		{
			// 0 is for test only !!!
			case 0: currentLevel = new FirstLevelPC(theMap);
			break;
			case LevelKeeper.GENERATED_LEVEL : 
				currentLevel = new LevelGenerated();
				MapKeeper2.getInstance().buildLevel(theMap);
				break;
			case LevelKeeper.GENERATED_QUICK_LEVEL : 
				currentLevel = new LevelGenerated();
				RandomMapCreator.getInstance().buildLevel(theMap);
				break;
				// Won map
			case LevelKeeper.WON_SCREEN : currentLevel = new LevelGenerated();
			MapKeeper2.getInstance().buildLevelShowAlliedArmy(theMap, 0);
			break;
			// Tutorial
			case LevelKeeper.TUTORIAL_LEVEL : 
				// Nothing yet, the tutorial creates its own level	
				currentLevel = new LevelGenerated();
				//RandomMapCreator.getInstance().buildLevel(theMap);
				break;
			case LevelKeeper.SUB_GENERATED_LEVEL : 
				currentLevel = new LevelGenerated();
				MapKeeper2.getInstance().buildLevel(theMap);
				break;
			case LevelKeeper.SUB_GENERATED_QUICK__LEVEL: 
				currentLevel = new LevelGenerated();
				RandomMapCreator.getInstance().buildLevel(theMap);
				break;
			case -1: currentLevel = new SplashLevel(theMap);
			break;
			default:
				currentLevel = new LevelGenerated();
				MapKeeper2.getInstance().buildLevel(theMap);
		}
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getAlliesBoats()
	 */
	public void addAlliesBoats()
	{
		// TODO Auto-generated method stub
		currentLevel.addAlliesBoats();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getAlliesSubs()
	 */
	public void addAlliesSubs()
	{
		// TODO Auto-generated method stub
		currentLevel.addAlliesSubs();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getEnemiesBoats()
	 */
	public void addEnemiesBoats()
	{
		// TODO Auto-generated method stub
		currentLevel.addEnemiesBoats();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getEnemiesSubs()
	 */
	public void addEnemiesSubs()
	{
		// TODO Auto-generated method stub
		currentLevel.addEnemiesSubs();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getNeutralBoats()
	 */
	public void addNeutralBoats()
	{
		// TODO Auto-generated method stub
		currentLevel.addNeutralBoats();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getNeutralSubs()
	 */
	public void addNeutralSubs()
	{
		// TODO Auto-generated method stub
		currentLevel.addNeutralSubs();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getOurBoats()
	 */
	public void addOurBoats()
	{
		// TODO Auto-generated method stub
		currentLevel.addOurBoats();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getOurSubs()
	 */
	public void addOurSubs()
	{
		// TODO Auto-generated method stub
		currentLevel.addOurSubs();
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.levels.ILevel#getGlobalSensors()
	 */
	public void addGlobalSensor()
	{
		currentLevel.addGlobalSensor();
	}
	
	
	public void addOthers()
	{
		// TODO Auto-generated method stub
		
	}
}
