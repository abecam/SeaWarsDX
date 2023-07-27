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
import java.util.ArrayList;
import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.*;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.ParticleManager;
import com.tgb.subengine.gamesystems.IGamePart;
import com.tgb.subengine.gameentities.*;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subgame.levels.*;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.unitspc.sensors.*;

/**
 * The level to print the score. Quite empty now.
 * @author Alain Becam
 *
 */
public class LevelScoreMap extends LevelMap implements IGamePart
{
	public final static int START_MENU=700; // Where the menu start (so where the map shouldn't be) 
	
	boolean upKey,downKey,leftKey,rightKey=false;
	
	static final String myName = "LevelScoreMap";
	
	WPDrawer myWPDrawer;
	
	ProgrammableUnit currentUnit=null; // The selected unit.
	Submarine currentSub=null; // If it is a sub
	Boat currentBoat=null; // If it is a boat
	boolean isBoat=true;

	boolean unitSelected=false;
	boolean unitToSelect=false;
	
	boolean endAsked=false;
	long timeSinceClick=0;
	
	String nameTypeUnit;
	
	
	boolean mapFree=true; // can we click freely on the map
	
	boolean moveWP=false;
//	boolean exitMoveWP=false;
	int indexWPToMove=0;
	
	Journey wpMissiles;
	Journey wpTorpedoes;
	
	double timeTot;
	boolean dummyCreated;
	boolean titleRevealed;
	
	Button startButton;
	Button quickStartButton;
	TextField scoreTxtField;
	Button addScoreButton;
	
	public LevelScoreMap()
	{
		; // should remain empty, push all in start() !!!
	}
	

	/**
	 * Clean the map
	 */
	protected void cleanMap()
	{
		System.out.println("Clean the tactical map");

		this.cleanUnit();
		long totalXPWon=0;
		
		if (enemiesSubs != null)
		{
			for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
			{
				// Update !!!
				enemiesSubs.get(iSub).removeMe();
			}
			enemiesSubs.clear();
		}
		if (enemiesBoats != null)
		{
			for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
			{
				// Update !!!
				enemiesBoats.get(iBoat).resetAirplaneInFlight();
				enemiesBoats.get(iBoat).removeMe();
			}
			enemiesBoats.clear();
		}
		if (enemiesBases != null)
		{
			for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
			{
				// Update !!!
				enemiesBases.get(iBoat).resetAirplaneInFlight();
				enemiesBases.get(iBoat).removeMe();
			}
			enemiesBases.clear();
		}
		
		if (ourSubs != null)
		{
			for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
			{
				// Update !!!
				ourSubs.get(iSub).removeMe();
			}
			ourSubs.clear();
		}
		if (ourBoats != null)
		{
			for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
			{
				// Update !!!
				ourBoats.get(iBoat).resetAirplaneInFlight();
				ourBoats.get(iBoat).removeMe();
			}
			ourBoats.clear();
		}
		if (alliesSubs != null)
		{
			for (int iSub = 0; iSub < alliesSubs.size() ; iSub++)
			{
				// Update !!!
				alliesSubs.get(iSub).removeMe();
			}
			alliesSubs.clear();
		}
		if (alliesBoats != null)
		{
			for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
			{
				// Update !!!
				alliesBoats.get(iBoat).resetAirplaneInFlight();
				alliesBoats.get(iBoat).removeMe();
			}
			alliesBoats.clear();
		}
		
		if (neutralSubs != null)
		{
			neutralSubs.clear();
		}
		if (neutralBoats != null)
		{
			neutralBoats.clear();
		}
		if (globalSensors != null)
		{
			for (int iSensor = 0; iSensor < globalSensors.size() ; iSensor++)
			{
				// Update !!!
				globalSensors.get(iSensor).removeMe();
			}
			globalSensors.clear();
		}
		if (missiles != null)
		{
			for (int iBoat = 0; iBoat < missiles.size() ; iBoat++)
			{
				// Update !!!
				missiles.get(iBoat).removeMe();
			}
			missiles.clear();
		}
		if (torpedoes != null)
		{
			for (int iBoat = 0; iBoat < torpedoes.size() ; iBoat++)
			{
				// Update !!!
				torpedoes.get(iBoat).removeMe();
			}
			torpedoes.clear();
		}
		if (alliesAirplanes != null)
		{
			for (int iBoat = 0; iBoat < alliesAirplanes.size() ; iBoat++)
			{
				// Update !!!
				alliesAirplanes.get(iBoat).removeMe();
			}
			alliesAirplanes.clear();
		}
		if (ourAirplanes != null)
		{
			for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
			{
				// Update !!!
				ourAirplanes.get(iBoat).removeMe();
			}
			ourAirplanes.clear();
		}
		if (enemiesAirplanes != null)
		{
			for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
			{
				// Update !!!
				enemiesAirplanes.get(iBoat).removeMe();
			}
			enemiesAirplanes.clear();
		}
		if (ourBases != null)
		{
			for (int iBoat = 0; iBoat < ourBases.size() ; iBoat++)
			{
				// Update !!!
				ourBases.get(iBoat).resetAirplaneInFlight();
				ourBases.get(iBoat).removeMe();
			}
			ourBases.clear();
		}
		if (alliesBases != null)
		{
			for (int iBoat = 0; iBoat < alliesBases.size() ; iBoat++)
			{
				// Update !!!
				alliesBases.get(iBoat).resetAirplaneInFlight();
				alliesBases.get(iBoat).removeMe();
			}
			alliesBases.clear();
		}
		
		if (neutralBases != null)
		{
			for (int iBoat = 0; iBoat < neutralBases.size() ; iBoat++)
			{
				// Update !!!
				neutralBases.get(iBoat).resetAirplaneInFlight();
				neutralBases.get(iBoat).removeMe();
			}
			neutralBases.clear();
		}
		
		ourKD.removeMeFromMap();
		
		myWPDrawer.removeMe();
		
		myWPDrawer = null;
		
		DrawBackground.removeSplash();
		
		RenderingManager.getInstance().washAllAndPrepare();
		RenderingManager.removeMe();
		ParticleManager.removeMe();
		
//		if (!LevelKeeper.getInstance().isBlank())
//			ourScene.remove(spriteLevel);
		ourScene.remove(startButton);
		ourScene.remove(quickStartButton);
		ourScene.remove(scoreTxtField);
		ourScene.remove(addScoreButton);
		
		cleanMemory();
		 
		System.out.println("Scripted map cleaned");
	}

	private void cleanMemory()
	{
		currentSignals= null;

		globalSensors = null;

		missiles = null;

		torpedoes = null;


		ourSubs = null;
		ourBoats = null;
		alliesSubs = null;
		alliesBoats = null;
		enemiesSubs = null;
		enemiesBoats = null;
		neutralSubs = null;
		neutralBoats = null;


		ourAirplanes = null;
		alliesAirplanes = null;
		enemiesAirplanes = null;
		neutralAirplanes = null;

		ourBases = null;
		alliesBases = null;
		enemiesBases = null;
		neutralBases = null;

		wpMissiles= null;
		wpTorpedoes= null;
		ourScene=null;


		levelImage=null;
		spriteLevel= null;


		ourKD = null;
		myWPDrawer = null;


		startButton = null;

		quickStartButton = null;
	}
	
	public void cleanUnit()
	{
		if (isBoat)
		{
			if (currentBoat != null)
				currentBoat.setView(false);
		}
		if (!isBoat)
		{
			if (currentSub != null)
				currentSub.setView(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#doLoop()
	 */
	public String doLoop()
	{
		int mouseX=0,mouseY=0;
		boolean mousePressed=false;
		boolean rightMousePressed = false;
		double divider=10000;
		//boolean doNotHide=false;
		
		tmpTime=new java.util.Date().getTime();
		timeInterval=tmpTime-lastTime;
		lastTime = tmpTime;
		
		timeUsed= ((double )timeInterval)/divider;
		
		timeTot+=timeUsed;
		
		
/////////////////////////////////////////////////////
///// End
		if (startButton.isClicked())
		{
			// Generated levels
			LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.GENERATED_LEVEL);
			// Push up the current info and return to the map
			return StrategicMap.myName;
		}
		
		if (quickStartButton.isClicked())
		{
			// Generated levels
			LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.GENERATED_QUICK_LEVEL);
			// Push up the current info and return to the map
			return PrepareRandomMap.myName;
		}
		
		if (addScoreButton != null)
		{
			if (addScoreButton.isClicked())
			{
				// Send the score
				java.sql.Date mySQLDate = new java.sql.Date(new java.util.Date().getTime());

				HighScoreManager.appendLevelScore(scoreTxtField.getText(), mySQLDate.toString(), ScoreKeeper.getInstance().getScore(), ScoreKeeper.getInstance().getNbAmmo(), ScoreKeeper.getInstance().getNbAmmoEne(), ScoreKeeper.getInstance().getNbMissiles(), ScoreKeeper.getInstance().getNbMissilesEne(), ScoreKeeper.getInstance().getNbTorpedoes(), ScoreKeeper.getInstance().getNbTorpedoesEne(), ScoreKeeper.getInstance().getMiaOur(), ScoreKeeper.getInstance().getMiaEnemies(), ScoreKeeper.getInstance().costOur, ScoreKeeper.getInstance().costEnemies);
				ourScene.remove(scoreTxtField);
				ourScene.remove(addScoreButton);
				// Should set a flag to block the addition of score until the conquest is resetted.
			}
		}
		if (Input.isDown(Input.KEY_UP))
		{
			upKey= true;
		}
		else
			upKey = false;
		if (Input.isDown(Input.KEY_DOWN))
		{
			downKey=true;
		}
		else
			downKey=false;
		if (Input.isDown(Input.KEY_RIGHT))
		{
			rightKey= true;
		}
		else
			rightKey = false;
		if (Input.isDown(Input.KEY_LEFT))
		{
			leftKey=true;
		}
		else
			leftKey=false;
		
		// Go through the units and update them!
		if (ourSubs != null)
		{
			
			for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
			{
				// Update !!!
				ourSubs.get(iSub).doUpdate(timeUsed);
				
				if ((mousePressed) && mapFree)
				{
					if ((Math.abs((ourSubs.get(iSub).getPosX() - mouseX)) < 20) && (Math.abs((ourSubs.get(iSub).getPosY()  - mouseY)) < 20))
					{
						cleanUnit();
						currentSub = ourSubs.get(iSub);
						isBoat = false;
						// This boat is selected
						ourSubs.get(iSub).drawWP();
						unitToSelect=true;
					}
				}
			}
		}
		if (ourBoats != null)
		{
			for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
			{
				//System.out.println("Update our boat "+iBoat);
				// Update !!!
				ourBoats.get(iBoat).doUpdate(timeUsed);

				if ((mousePressed) && mapFree)
				{
					if ((Math.abs((ourBoats.get(iBoat).getPosX() - mouseX)) < 20) && (Math.abs((ourBoats.get(iBoat).getPosY()  - mouseY)) < 20))
					{
						cleanUnit();
						currentBoat = ourBoats.get(iBoat);
						isBoat = true;
						// This boat is selected
						ourBoats.get(iBoat).drawWP();
						unitToSelect=true;
					}
				}
			}
		}
		if (alliesSubs != null)
		{
			for (int iSub = 0; iSub < alliesSubs.size() ; iSub++)
			{
				// Update !!!
				alliesSubs.get(iSub).doUpdate(timeUsed);
				
				if ((mousePressed) && mapFree)
				{
					if ((Math.abs((alliesSubs.get(iSub).getPosX() - mouseX)) < 20) && (Math.abs((alliesSubs.get(iSub).getPosY()  - mouseY)) < 20))
					{
						cleanUnit();
						currentSub = alliesSubs.get(iSub);
						isBoat = false;
						// This boat is selected
						alliesSubs.get(iSub).drawWP();
						unitToSelect=true;
					}
				}
			}
		}
		if (alliesBoats != null)
		{
			for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
			{
				// Update !!!
				alliesBoats.get(iBoat).doUpdate(timeUsed);
				if ((mousePressed) && mapFree)
				{
					if ((Math.abs((alliesBoats.get(iBoat).getPosX() - mouseX)) < 20) && (Math.abs((alliesBoats.get(iBoat).getPosY()  - mouseY)) < 20))
					{
						cleanUnit();
						currentBoat = alliesBoats.get(iBoat);
						isBoat = true;
						// This boat is selected
						alliesBoats.get(iBoat).drawWP();
						unitToSelect=true;
					}
				}
			}
		}
		if (enemiesSubs != null)
		{
			for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
			{
				// Update !!!
				enemiesSubs.get(iSub).doUpdate(timeUsed);
				if (Input.isPressed(Input.KEY_D))
					enemiesSubs.get(iSub).showMe();
			}
		}
		if (enemiesBoats != null)
		{
			for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
			{
				// Update !!!
				enemiesBoats.get(iBoat).doUpdate(timeUsed);
				if (Input.isPressed(Input.KEY_D))
					enemiesBoats.get(iBoat).showMe();
			}
		}
		if (neutralSubs != null)
		{
			for (int iSub = 0; iSub < neutralSubs.size() ; iSub++)
			{
				// Update !!!
				neutralSubs.get(iSub).doUpdate(timeUsed);
			}
		}
		if (neutralBoats != null)
		{
			for (int iBoat = 0; iBoat < neutralBoats.size() ; iBoat++)
			{
				// Update !!!
				neutralBoats.get(iBoat).doUpdate(timeUsed);
			}
		}
		if (globalSensors != null)
		{
			for (int iSensors = 0; iSensors < globalSensors.size() ; iSensors++)
			{
				// Update !!!
				globalSensors.get(iSensors).doUpdate(timeUsed);
			}
		}
		if (alliesAirplanes != null)
		{
			for (int iBoat = 0; iBoat < alliesAirplanes.size() ; iBoat++)
			{
				// Update !!!
				tmpAirplane = alliesAirplanes.get(iBoat);
				tmpAirplane.doUpdate(timeUsed);
				if (tmpAirplane.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpAirplane.removeMe();
					alliesAirplanes.remove(iBoat);
					iBoat--;
				}
			}
		}
		if (ourAirplanes != null)
		{
			for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
			{
				// Update !!!
				tmpAirplane = ourAirplanes.get(iBoat);
				tmpAirplane.doUpdate(timeUsed);
				if (tmpAirplane.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpAirplane.removeMe();
					ourAirplanes.remove(iBoat);
					iBoat--;
				}
			}
		}
		if (enemiesAirplanes != null)
		{
			for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
			{
				// Update !!!
				tmpAirplane = enemiesAirplanes.get(iBoat);
				tmpAirplane.doUpdate(timeUsed);
				if (tmpAirplane.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpAirplane.removeMe();
					enemiesAirplanes.remove(iBoat);
					iBoat--;
				}
			}
		}
		if (ourBases != null)
		{
			for (int iBoat = 0; iBoat < ourBases.size() ; iBoat++)
			{
				// Update !!!
				ourBases.get(iBoat).doUpdate(timeUsed);
			}
		}
		if (alliesBases != null)
		{
			for (int iBoat = 0; iBoat < alliesBases.size() ; iBoat++)
			{
				// Update !!!
				alliesBases.get(iBoat).doUpdate(timeUsed);
			}
		}
		if (enemiesBases != null)
		{
			for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
			{
				// Update !!!
				enemiesBases.get(iBoat).doUpdate(timeUsed);
			}
		}
		if (neutralBases != null)
		{
			for (int iBoat = 0; iBoat < neutralBases.size() ; iBoat++)
			{
				// Update !!!
				neutralBases.get(iBoat).doUpdate(timeUsed);
			}
		}
		if (missiles != null)
		{
			for (int iMissiles = 0; iMissiles < missiles.size() ; iMissiles++)
			{
				tmpMissile = missiles.get(iMissiles);
				tmpMissile.doUpdate(timeUsed);
				if (tmpMissile.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpMissile.removeMe();
					missiles.remove(iMissiles);
					iMissiles--;
				}
			}
		}
		if (torpedoes != null)
		{
			for (int iTorpedoes = 0; iTorpedoes < torpedoes.size() ; iTorpedoes++)
			{
				tmpTorpedo = torpedoes.get(iTorpedoes);
				tmpTorpedo.doUpdate(timeUsed);
				if (tmpTorpedo.toRemove())
				{
					//System.out.println("Remove torpedo "+torpedoes.size());
					tmpTorpedo.removeMe();
					torpedoes.remove(iTorpedoes);
					iTorpedoes--;
				}
			}
		}
	
		return null;
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
			System.out.println("Quit the scripted map");
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
		System.out.println("Create score map");
		
		ScoreKeeper.getInstance().calculateScore();
		
		timeTot=0;
		dummyCreated=false;
		titleRevealed=false;
		
		// Init all
		nbOurSubs=0;
		nbOurBoats=0;
		nbAlliesSubs=0;
		nbAlliesBoats=0;
		nbTotalGood=0;
		
		nbEnemiesSubs=0;
		nbEnemiesBoats=0;
		nbEnemiesTotal=0;
		
		nbOurAirplanes=0;
		nbEnemiesAirplanes=0;
		nbAlliesAirplanes=0;
		
		nbSignal = 0;
		
		currentUnit=null; // The selected unit.
		currentSub=null; // If it is a sub
		currentBoat=null; // If it is a boat
		isBoat=true;

		unitSelected=false;
		unitToSelect=false;
		
		endAsked=false;
		timeSinceClick=0;
		
		mapFree=true; // can we click freely on the map
		
		moveWP=false;
		
		
		myLevelLoader = new LevelLoaderPC(this);
		
		currentSignals= new ArrayList<Signal>();
		
		globalSensors = new ArrayList<Sensor>();
		
		missiles = new ArrayList<Missile>();
		
		torpedoes = new ArrayList<Torpedo>();

		
		ourSubs = new ArrayList<Submarine>();
		ourBoats = new ArrayList<Boat>();
		alliesSubs = new ArrayList<Submarine>();
		alliesBoats = new ArrayList<Boat>();
		enemiesSubs = new ArrayList<Submarine>();
		enemiesBoats = new ArrayList<Boat>();
		neutralSubs = new ArrayList<Submarine>();
		neutralBoats = new ArrayList<Boat>();
		
		
		ourAirplanes = new ArrayList<Airplane>();
		alliesAirplanes = new ArrayList<Airplane>();
		enemiesAirplanes = new ArrayList<Airplane>();
		neutralAirplanes = new ArrayList<Airplane>();
		
		ourBases = new ArrayList<Base> ();
		alliesBases = new ArrayList<Base> ();
		enemiesBases = new ArrayList<Base> ();
		neutralBases = new ArrayList<Base> ();
		
		wpMissiles= new Journey();
		wpTorpedoes= new Journey();
		// Load the map
		DrawBackground.drawBackSplash();
		
		// Load the sprites
		gfxSprites.callMeFirst();
		ourScene=(Scene2D )Stage.getScene();
		
		if (!LevelKeeper.getInstance().isBlank())
		{
			levelImage=LevelKeeper.getInstance().getCurrentLevel();
			spriteLevel= new FlatSpritePC(levelImage);
			spriteLevel.setPos(Stage.getWidth()/2,  Stage.getHeight()/2, 0);
			
			idSpriteLevel = RenderingManager.getInstance().addDrawableEntity(spriteLevel,1);
		}
        
		
		ourKD = new KnownDatas(this);
		
		// Load the good level
		
		
		// TODO Auto-generated method stub
		setLevel(4);
		lastTime=new java.util.Date().getTime();
		myWPDrawer = new WPDrawer();
		
		for (int iGlobalSensors=0;iGlobalSensors<this.globalSensors.size();iGlobalSensors++)
		{
			this.globalSensors.get(iGlobalSensors).setHiddenFUnits(true);
		}
		
		
		startButton = Button.createLabeledToggleButton("CONQUEST", 500, 440);
		startButton.setAnchor(Sprite.CENTER);
		
		quickStartButton = Button.createLabeledToggleButton("QUICK GAME", 500, 480);
		quickStartButton.setAnchor(Sprite.CENTER);
		//ourScene.add(startButton);
		
		if (ScoreKeeper.getInstance().isHasWon())
		{
			scoreTxtField = new TextField("Your name",500, 280);
			scoreTxtField.setAnchor(Sprite.CENTER);
			scoreTxtField.setMaxNumChars(30);
			scoreTxtField.selectAll();
			scoreTxtField.setFocus(true);


			addScoreButton = Button.createLabeledToggleButton("Send score", 500, 320);
			addScoreButton.setAnchor(Sprite.CENTER);

			ourScene.add(scoreTxtField);
			ourScene.add(addScoreButton);
		}
		ourScene.add(startButton);
		ourScene.add(quickStartButton);
	}

	public void addSignal(Signal oneSignal)
	{
		currentSignals.add(oneSignal);
	}
	
	public void removeSignal(Signal oneSignal)
	{
		currentSignals.remove(oneSignal);
	}

	public WPDrawer getMyWPDrawer() {
		return myWPDrawer;
	}
	
	/**
	 * Create the dummy bases to reveal the title
	 */
	public void createDummyBases()
	{
		// TODO Auto-generated method stub
		// One base, for test
		Base mainBase = new Base(this);
		
		mainBase.setEnergy(1000);
		mainBase.setOrientation(0);
		mainBase.setName("Main base");
		mainBase.setResistance(4);
		mainBase.setPosX(300);
		mainBase.setPosY(100);
		mainBase.setTypeFaction(FUnit.BASE_OUR);
		mainBase.setType(Base.DUMMYBASE);
		mainBase.createGfx(300, 100, 0, 0, 20);
		mainBase.setView(false);
		
		this.addBase(mainBase);
		
		Base mainBase2 = new Base(this);
		
		mainBase2.setEnergy(1000);
		mainBase2.setOrientation(0);
		mainBase2.setName("Main base");
		mainBase2.setResistance(4);
		mainBase2.setPosX(400);
		mainBase2.setPosY(100);
		mainBase2.setTypeFaction(FUnit.BASE_OUR);
		mainBase2.setType(Base.DUMMYBASE);
		mainBase2.createGfx(400, 100, 0, 0, 20);
		mainBase2.setView(false);
		
		this.addBase(mainBase2);
		
		Base mainBase3 = new Base(this);
		
		mainBase3.setEnergy(1000);
		mainBase3.setOrientation(0);
		mainBase3.setName("Main base");
		mainBase3.setResistance(4);
		mainBase3.setPosX(500);
		mainBase3.setPosY(100);
		mainBase3.setTypeFaction(FUnit.BASE_OUR);
		mainBase3.setType(Base.DUMMYBASE);
		mainBase3.createGfx(500, 100, 0, 0, 20);
		mainBase3.setView(false);
		
		this.addBase(mainBase3);
		
		Base mainBase4 = new Base(this);
		
		mainBase4.setEnergy(1000);
		mainBase4.setOrientation(0);
		mainBase4.setName("Main base");
		mainBase4.setResistance(4);
		mainBase4.setPosX(600);
		mainBase4.setPosY(100);
		mainBase4.setTypeFaction(FUnit.BASE_OUR);
		mainBase4.setType(Base.DUMMYBASE);
		mainBase4.createGfx(600, 100, 0, 0, 20);
		mainBase4.setView(false);
		
		this.addBase(mainBase4);
		
		Base mainBase5 = new Base(this);
		
		mainBase5.setEnergy(1000);
		mainBase5.setOrientation(0);
		mainBase5.setName("Main base");
		mainBase5.setResistance(4);
		mainBase5.setPosX(700);
		mainBase5.setPosY(100);
		mainBase5.setTypeFaction(FUnit.BASE_OUR);
		mainBase5.setType(Base.DUMMYBASE);
		mainBase5.createGfx(700, 100, 0, 0, 20);
		mainBase5.setView(false);
		
		this.addBase(mainBase5);
	}
}
