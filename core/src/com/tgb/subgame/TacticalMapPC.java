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
import java.io.FileOutputStream;
import java.util.ArrayList;

import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.image.CoreImage;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.*;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.ParticleManager;
import com.tgb.subengine.gamesystems.IGamePart;
import com.tgb.subengine.gamesystems.StateManager;
import com.tgb.subengine.gameentities.*;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subengine.gfxentities.FilledRectanglePC;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subgame.levels.*;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.unitspc.sensors.*;

/**
 * The tactical map is the central part, the level, managing the actual (hopefully entertaining)
 * game
 * @author Alain Becam
 * TODO: Have more generic manageable units and not only boat and sub (so find a way to have
 * unit dependent interface).
 */
public class TacticalMapPC extends LevelMap implements IGamePart
{
	private static final int WIDTH_END_BUTTON = 94;
	private static final double DIVIDER_END_BUTTON = WIDTH_END_BUTTON/3;

	private static final double THIRTY_SECONDS = 3; // 30*1000 / Divider = 10000

	public final static int START_MENU=700; // Where the menu start (so where the map shouldn't be) 
	
	boolean upKey,downKey,leftKey,rightKey=false;
	
	static final String myName = "TacticalMapPC";
	
	ProgrammableUnit currentUnit=null; // The selected unit.
//	Submarine currentSub=null; // If it is a sub
	Boat currentBoat=null; // If it is a boat

	boolean unitSelected=false;
	boolean unitToSelect=false;
	
	long timeSinceClick=0;
	
	String nameTypeUnit;
	
	Label infoLabel;
	Label stateHullLabel;
	Label stateDataLabel;
	Label stateNavLabel;
	Label complementLabel;
	Label speedLabel;
	Slider sliderSpeed;
	//Label depthLabel;
	//Slider sliderDepth;
	Label weaponLabel;
	Label airplanesLabel;
	Label airplanesLabel2;
	Button fireAtWillButton;
	Button autonomousButton;
	Button ignoreFriendsButton;
	Button attackNeutralsButton; // Not used yet!
	
	Button followWPButton;
	Button loopWPButton;
	Button addWPButton;
	Button removeWPButton;
	Button moveWPButton;
	
	Button shootTorpedoButton;
	Button salveTorpedoButton;
	Button wpFollowTorpedoButton;
	Button wpTorpedoButton;
	Button seekTButton;
	Button goToTargetTButton;
	Button shootMissileButton;
	Button salveMissileButton;
	Button wpFollowMissileButton;
	Button wpMissileButton;
	Button seekMButton;
	Button goToTargetMButton;
	
	FilledRectanglePC wpRectangle; // Show which WP you are editing.
	FilledRectanglePC missileRect; // Show if you can fire missiles (useful for submarine)
	FilledRectanglePC radarRect; // Show if you can activate the radar (submarine again)
	FilledRectanglePC endRect; // Show if you can end the game
	
	final static int ED_NONE=0;
	final static int ED_ADD_WP=1;
	final static int ED_REMOVE_WP=2;
	final static int ED_MOVE_WP=3;
	
	final static int ED_SHOOT_TORPEDO=10;
	final static int ED_SHOOT_MISSILE=11;
	
	int stateEdition=ED_NONE;
	
	Button sonarButton;
	Button radarButton;
	
	boolean mapFree=true; // can we click freely on the map
	
	boolean moveWP=false;
//	boolean exitMoveWP=false;
	
	Button endLevelButton;
	
	int indexWPToMove=0;
	
	Journey wpMissiles;
	Journey wpTorpedoes;
	
	SpritePC clouds[]; // decorative clouds
	double cloudSpeed; // Their speed
	double xCloudMove,yCloudMove;
	
	long iFrame = 0; // For video recording
	
	boolean gameOver = false;
	
	// Position of the text showing the result. Slowly scrolling up.
	float yDetailedResult = 900;
	
	public TacticalMapPC()
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
			int enemiesSubsSize=enemiesSubs.size();
			
			for (int iSub = 0; iSub < enemiesSubsSize ; iSub++)
			{
				// Update !!!
				if (enemiesSubs.get(iSub).isDead())
				{
					if (enemiesSubs.get(iSub).getType() == Submarine.NUKE)
					{
						totalXPWon+=80;
					}
					else if (enemiesSubs.get(iSub).getType() == Submarine.NUKE_SSBN)
					{
						totalXPWon+=100;
					}
					else if (enemiesSubs.get(iSub).getType() == Submarine.DIESEL)
					{
						totalXPWon+=40;
					}
					else if (enemiesSubs.get(iSub).getType() == Submarine.DIESEL_AIP)
					{
						totalXPWon+=20;
					}
					// Inform the game keeper
					GameKeeper.getInstance().addCostEnemies(enemiesSubs.get(iSub).getCost());
					GameKeeper.getInstance().addMIAEnemies(enemiesSubs.get(iSub).getComplementNorm()-enemiesSubs.get(iSub).getComplement());
				}
				enemiesSubs.get(iSub).removeMe();
			}
			enemiesSubs.clear();
		}
		if (enemiesBoats != null)
		{
			int enemiesBoatsSize=enemiesBoats.size();
			
			for (int iBoat = 0; iBoat < enemiesBoatsSize ; iBoat++)
			{
				// Update !!!
				if (enemiesBoats.get(iBoat).isDead())
				{
					if (enemiesBoats.get(iBoat).getType() == Boat.CARRIER)
					{
						totalXPWon+=100;
					}
					else if (enemiesBoats.get(iBoat).getType() == Boat.CRUISER)
					{
						totalXPWon+=50;
						if (enemiesBoats.get(iBoat).getTonnage() == 700000)
						{
							totalXPWon+=1000;
						}
					}
					else if (enemiesBoats.get(iBoat).getType() == Boat.AMPHIBIOUS)
					{
						totalXPWon+=80;
					}
					else if (enemiesBoats.get(iBoat).getType() == Boat.DESTROYER)
					{
						totalXPWon+=30;
					}
					else if (enemiesBoats.get(iBoat).getType() == Boat.FRIGATE)
					{
						totalXPWon+=20;
					}
					else if (enemiesBoats.get(iBoat).getType() == Boat.CORVETTE)
					{
						totalXPWon+=5;
					}
					// Inform the game keeper
					GameKeeper.getInstance().addCostEnemies(enemiesBoats.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAEnemies(enemiesBoats.get(iBoat).getComplementNorm()-enemiesBoats.get(iBoat).getComplement());
				}
				enemiesBoats.get(iBoat).resetAirplaneInFlight();
				enemiesBoats.get(iBoat).removeMe();
			}
			enemiesBoats.clear();
		}
		if (enemiesBases != null)
		{
			int enemiesBasesSize=enemiesBases.size();
			
			for (int iBoat = 0; iBoat < enemiesBasesSize ; iBoat++)
			{
				// Update !!!
				if (enemiesBases.get(iBoat).isDead())
				{
					if (enemiesBases.get(iBoat).getType() == Base.MAINBASEONE)
					{
						totalXPWon+=1000;
					}
					else if (enemiesBases.get(iBoat).getType() == Base.MAINBASETWO)
					{
						totalXPWon+=800;
					}
					else if (enemiesBases.get(iBoat).getType() == Base.BIGBASEONE)
					{
						totalXPWon+=500;
					}
					else if (enemiesBases.get(iBoat).getType() == Base.BIGBASETWO)
					{
						totalXPWon+=400;
					}
					else if (enemiesBases.get(iBoat).getType() == Base.SMALLBASEONE)
					{
						totalXPWon+=100;
					}
					else if (enemiesBases.get(iBoat).getType() == Base.SMALLBASETWO)
					{
						totalXPWon+=80;
					}
					// Inform the game keeper
					GameKeeper.getInstance().addCostEnemies(enemiesBases.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAEnemies(enemiesBases.get(iBoat).getComplementNorm()-enemiesBases.get(iBoat).getComplement());
				}
				enemiesBases.get(iBoat).resetAirplaneInFlight();
				enemiesBases.get(iBoat).removeMe();
			}
			enemiesBases.clear();
		}
		
		if (ourSubs != null)
		{
			int ourSubsSize=ourSubs.size();
			
			for (int iSub = 0; iSub < ourSubsSize ; iSub++)
			{
				// Update !!!
				if (ourSubs.get(iSub).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostOur(ourSubs.get(iSub).getCost());
					GameKeeper.getInstance().addMIAOur(ourSubs.get(iSub).getComplementNorm()-ourSubs.get(iSub).getComplement());
				}
				else
				{
					ourSubs.get(iSub).addXP(totalXPWon);
					ourSubs.get(iSub).endFire();
				}
				ourSubs.get(iSub).removeMe();
			}
			ourSubs.clear();
		}
		if (ourBoats != null)
		{
			int ourBoatsSize  = ourBoats.size();
			
			for (int iBoat = 0; iBoat < ourBoatsSize ; iBoat++)
			{
				// Update !!!
				if (ourBoats.get(iBoat).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostOur(ourBoats.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAOur(ourBoats.get(iBoat).getComplementNorm()-ourBoats.get(iBoat).getComplement());
				}
				else
				{
					ourBoats.get(iBoat).addXP(totalXPWon);
					ourBoats.get(iBoat).endFire();
				}
				ourBoats.get(iBoat).resetAirplaneInFlight();
				ourBoats.get(iBoat).removeMe();
			}
			ourBoats.clear();
		}
		if (alliesSubs != null)
		{
			int alliesSubsSize  = alliesSubs.size();
			
			for (int iSub = 0; iSub < alliesSubsSize ; iSub++)
			{
				// Update !!!
				if (alliesSubs.get(iSub).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostAllies(alliesSubs.get(iSub).getCost());
					GameKeeper.getInstance().addMIAAllies(alliesSubs.get(iSub).getComplementNorm()-alliesSubs.get(iSub).getComplement());
				}
				else
				{
					alliesSubs.get(iSub).addXP(totalXPWon);
					alliesSubs.get(iSub).endFire();
				}
				alliesSubs.get(iSub).removeMe();
			}
			alliesSubs.clear();
		}
		if (alliesBoats != null)
		{
			int alliesBoatsSize  = alliesBoats.size();
			
			for (int iBoat = 0; iBoat < alliesBoatsSize ; iBoat++)
			{
				// Update !!!
				if (alliesBoats.get(iBoat).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostAllies(alliesBoats.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAAllies(alliesBoats.get(iBoat).getComplementNorm()-alliesBoats.get(iBoat).getComplement());
				}
				else
				{
					alliesBoats.get(iBoat).addXP(totalXPWon);
					alliesBoats.get(iBoat).endFire();
				}
				alliesBoats.get(iBoat).resetAirplaneInFlight();
				alliesBoats.get(iBoat).removeMe();
			}
			alliesBoats.clear();
		}
		
		if (neutralSubs != null)
		{
			for (int iSub = 0; iSub < neutralSubs.size() ; iSub++)
			{
				// Update !!!
				if (neutralSubs.get(iSub).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostOur(neutralSubs.get(iSub).getCost());
					GameKeeper.getInstance().addMIAOur(neutralSubs.get(iSub).getComplementNorm()-neutralSubs.get(iSub).getComplement());
				}
				neutralSubs.get(iSub).removeMe();
			}
			neutralSubs.clear();
		}
		if (neutralBoats != null)
		{
			for (int iBoat = 0; iBoat < neutralBoats.size() ; iBoat++)
			{
				// Update !!!
				if (neutralBoats.get(iBoat).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostOur(neutralBoats.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAOur(neutralBoats.get(iBoat).getComplementNorm()-neutralBoats.get(iBoat).getComplement());
				}
				neutralBoats.get(iBoat).resetAirplaneInFlight();
				neutralBoats.get(iBoat).removeMe();
			}
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
				if (ourBases.get(iBoat).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostOur(ourBases.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAOur(ourBases.get(iBoat).getComplementNorm()-ourBases.get(iBoat).getComplement());
				}
				else
				{
					ourBases.get(iBoat).addXP(totalXPWon);
				}
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
				if (alliesBases.get(iBoat).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostAllies(alliesBases.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAAllies(alliesBases.get(iBoat).getComplementNorm()-alliesBases.get(iBoat).getComplement());
				}
				else
				{
					alliesBases.get(iBoat).addXP(totalXPWon);
				}
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
				if (neutralBases.get(iBoat).isDead())
				{
					// Inform the game keeper
					GameKeeper.getInstance().addCostOur(neutralBases.get(iBoat).getCost());
					GameKeeper.getInstance().addMIAOur(neutralBases.get(iBoat).getComplementNorm()-neutralBases.get(iBoat).getComplement());
				}
				neutralBases.get(iBoat).resetAirplaneInFlight();
				neutralBases.get(iBoat).removeMe();
			}
			neutralBases.clear();
		}
		trees.clear();
		
		ourKD.removeMeFromMap();
		
		if (myWPDrawer != null)
		{
			myWPDrawer.removeMe();
			myWPDrawer = null;
		}
		else
		{
			System.err.println("Tactical map: myWPDrawer is already null, shouldn't be...");
		}
		RenderingManager.getInstance().washAllAndPrepare();
		RenderingManager.removeMe();
		ParticleManager.removeMe();
		
//		if (!LevelKeeper.getInstance().isBlank())
//			ourScene.remove(spriteLevel);
		
		ourScene.remove(infoLabel);
		ourScene.remove(stateHullLabel);
		ourScene.remove(stateDataLabel);
		ourScene.remove(stateNavLabel);
		ourScene.remove(complementLabel);
		ourScene.remove(speedLabel);
		ourScene.remove(sliderSpeed);
		//ourScene.remove(depthLabel);
		//ourScene.remove(sliderDepth);
		ourScene.remove(weaponLabel);
		ourScene.remove(fireAtWillButton);
		ourScene.remove(autonomousButton);
		ourScene.remove(ignoreFriendsButton);
		ourScene.remove(attackNeutralsButton); // Not used yet!
		
		ourScene.remove(followWPButton);
		ourScene.remove(loopWPButton);
		ourScene.remove(addWPButton);
		ourScene.remove(removeWPButton);
		ourScene.remove(moveWPButton);
		
		ourScene.remove(shootTorpedoButton);
		ourScene.remove(salveTorpedoButton);
		ourScene.remove(wpFollowTorpedoButton);
		ourScene.remove(wpTorpedoButton);
		ourScene.remove(seekTButton);
		ourScene.remove(goToTargetTButton);
		ourScene.remove(shootMissileButton);
		ourScene.remove(salveMissileButton);
		ourScene.remove(wpFollowMissileButton);
		ourScene.remove(wpMissileButton);
		ourScene.remove(seekMButton);
		ourScene.remove(goToTargetMButton);
		
		ourScene.remove(sonarButton);
		ourScene.remove(radarButton);
		
		ourScene.remove(endLevelButton);
		
		ourScene.remove(loadingFillingText);
		
		cleanMemory();
		
		yDetailedResult = 900;
		
		System.out.println("Tactical map cleaned");
	}

	private void cleanMemory()
	{
		System.out.println("Set the tactical map object references to null (so the gc might clean them :) )");
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

		infoLabel = null;
		stateHullLabel = null;
		stateDataLabel = null;
		stateNavLabel = null;
		complementLabel = null;
		speedLabel = null;
		sliderSpeed = null;
		//depthLabel = null;
		//sliderDepth = null;
		weaponLabel = null;
		fireAtWillButton = null;
		autonomousButton = null;
		ignoreFriendsButton = null;
		attackNeutralsButton = null; // Not used yet!
		
		followWPButton = null;
		loopWPButton = null;
		addWPButton = null;
		removeWPButton = null;
		moveWPButton = null;
		
		shootTorpedoButton = null;
		salveTorpedoButton = null;
		wpFollowTorpedoButton = null;
		wpTorpedoButton = null;
		seekTButton = null;
		goToTargetTButton = null;
		shootMissileButton = null;
		salveMissileButton = null;
		wpFollowMissileButton = null;
		wpMissileButton = null;
		seekMButton = null;
		goToTargetMButton = null;
		
		sonarButton = null;
		radarButton = null;
		
		endLevelButton = null;
	}
	
	public void cleanUnit()
	{
		if (currentBoat != null)
		{
			currentBoat.endFire();
			currentBoat.setView(false);
		}
		
		missileRect.invalidate();
		radarRect.invalidate();
	}
//	//DEBUG///////////////////
//	double timeSinceStart=0;
	
	boolean nameToShow=false;
	boolean nameToHide=false;
	double timeChange=0;
	
	boolean cutRadar=false;
	private Group loadingFillingText;
	
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#doLoop()
	 */
	public String doLoop()
	{
		int mouseX=0,mouseY=0;
		boolean mousePressed=false;
		boolean rightMousePressed = false;
		
		
		double divider=10000;
		
//		double divider=20000; // For screenshots, see bellow 
		
		tmpTime=new java.util.Date().getTime();
		timeInterval=tmpTime-lastTime;
		lastTime = tmpTime;
		if (Input.isDown(Input.KEY_N))
			divider=2500;
		if (Input.isDown(Input.KEY_B))
			divider=1250;
		if (Input.isDown(Input.KEY_V))
			divider=125;
		timeUsed= ((double )timeInterval)/divider;
		
//		Screenshots for each frames (very slow, but allow to do videos, w/o sound)
//		try
//		{
//			scrFile = new FileOutputStream("C:\\Gfx\\SW\\SW"+iFrame+".png");
//			iFrame++;
//
//			scrFile.write(pulpcore.image.PNGWriter.write(Stage.getScreenshot()).getData());
//			
//			scrFile.close();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		
		
		if (clouds != null)
		{
			xCloudMove = timeUsed*cloudSpeed*Math.random();
			yCloudMove = timeUsed*cloudSpeed*Math.random();
			int cloudsLength=clouds.length;
			
			for (int iClouds=0;iClouds < cloudsLength ; iClouds++)
			{
				
				clouds[iClouds].moveRelative(xCloudMove, yCloudMove);
				if ((clouds[iClouds].getX() < -200) || (clouds[iClouds].getX() > 1200) || (clouds[iClouds].getY() < -200) || (clouds[iClouds].getY() > 1000))
				{
					clouds[iClouds].setPos(com.tgb.subgame.RandomContainer.getInstance().myRandom.nextInt(700), com.tgb.subgame.RandomContainer.getInstance().myRandom.nextInt(800), 0);
				}
			}
		}
		//System.out.println("Tactical map: do update");
/////DEBUG//////////////////////////////////		
//		timeSinceStart+=timeUsed;
//		if (timeSinceStart > 2.0)
//		{
//			timeSinceStart=0;
//			return StrategicMap.myName;
//		}
//////////////////////////////////////		
		if (endLevelButton.isClicked())
		{
			if (tmpTime-timeSinceClick < 3000)
			{
				// Push up the current info and return to the map
				MapKeeper2.getInstance().saveInstance();
				
				// Check if the game has been won or lost -> i.e. another level than our has been requested
				if (LevelKeeper.getInstance().getNextLevelWanted() != LevelKeeper.GENERATED_LEVEL)
				{
					LevelKeeper.getInstance().setNextMapX(0);
					LevelKeeper.getInstance().setNextMapY(0);
					LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.MAIN_MENU);
					return ScriptedMap.myName;
				}
				else
					return StrategicMap.myName;
			}
			timeSinceClick=tmpTime;
			endLevelButton.setSelected(false);
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
		
		if (Input.isDown(Input.KEY_F1))
		{
			if ((!nameToShow) && (timeChange < 0))
			{
				nameToShow=true;
				nameToHide=false;
				timeChange=0.02;
			}
			else if (timeChange < 0)
			{
				nameToHide=true;
				nameToShow=false;
				timeChange=0.02;
			}
		}
		if (timeChange >= 0)
		{
			timeChange-=timeUsed;
		}
		if (unitToSelect)
		{
			// Set-up the interactive panel!
			unitToSelect=false;
			unitSelected=true;

			currentBoat.setView(true);
			missileRect.invalidate();
			radarRect.invalidate();
			nameTypeUnit = currentBoat.getTypeName();
			
			infoLabel.setText(nameTypeUnit+"  "+currentBoat.getIdMap()+" "+currentBoat.getName()+" - "+currentBoat.getCurrentSpeed()+" knots");
			sliderSpeed.setRange(-(int )currentBoat.getMaxSpeed(), (int )currentBoat.getMaxSpeed());
			sliderSpeed.value.set(currentBoat.getCurrentSpeed());
			weaponLabel.setText(currentBoat.getNbTorpedoes()+" Torpedoes "+currentBoat.getNbMissiles()+" Missiles");
			autonomousButton.setSelected(currentBoat.isAutonomous());
			fireAtWillButton.setSelected(currentBoat.isFireAtWill());
			ignoreFriendsButton.setSelected(currentBoat.isIgnoreFriends());
			followWPButton.setSelected(currentBoat.isFollowWP());
			loopWPButton.setSelected(currentBoat.isLoop());
			//depthLabel.setText("Current Depth "+currentBoat.getDepth()+" Wanted 0");
			//sliderDepth.value.set(0);
			shootTorpedoButton.setSelected(false);
			shootMissileButton.setSelected(false);

			sonarButton.setSelected(currentBoat.isSonarOn());
			radarButton.setSelected(currentBoat.isRadarOn());
		}
		if (unitSelected)
		{
			double newSpeed= sliderSpeed.value.get();
			
			mouseX = Input.getMouseX();
			mouseY = Input.getMouseY();
			
			// Reset the editing state of the WP if needed
			// Should be moved only when the state is removed to gain some FPS
			if (!moveWP)
			{
				currentBoat.getProgrammedWPs().unedit();
			}
			
			
			// Do something if needed
			if (currentBoat.hasWPBeenUpdated())
			{
				currentBoat.drawWP();
			}
			stateHullLabel.setText("Integrity - Hull "+(int )currentBoat.getHullState()+"%");
			stateNavLabel.setText("DataLink "+(int )currentBoat.getDataLinkState()+"%");
			stateDataLabel.setText("Navigation "+(int )currentBoat.getNavState()+"%");
			complementLabel.setText("Complement "+(int )currentBoat.getComplement());

			weaponLabel.setText(currentBoat.getNbTorpedoes()+" Torpedoes "+currentBoat.getNbMissiles()+"Missiles");
			if (Math.abs(newSpeed - currentBoat.getCurrentSpeed()) > 0.5)
			{
				currentBoat.setWantedSpeed(newSpeed);
				infoLabel.setText(nameTypeUnit+"  "+currentBoat.getIdMap()+" "+currentBoat.getName()+" - "+(int )currentBoat.getCurrentSpeed()+" knots");
			}
			if (upKey)
			{
				currentBoat.accelerate(0.4);
				sliderSpeed.value.set(currentBoat.getWantedSpeed());
			}
			if (downKey)
			{
				currentBoat.deccelerate(0.4);
				sliderSpeed.value.set(currentBoat.getWantedSpeed());
			}
			if (rightKey)
			{
				currentBoat.setFollowWP(false);
				followWPButton.setSelected(false);
				currentBoat.turn(0.01);
			}
			else if (leftKey)
			{
				currentBoat.setFollowWP(false);
				followWPButton.setSelected(false);
				currentBoat.turn(-0.01);
			}
			else  if (!currentBoat.isFollowWP())
				currentBoat.turn(0.00);
			if (Input.isPressed(Input.KEY_S))
				System.out.println(currentBoat.saveBoat());

			currentBoat.setAutonomous(autonomousButton.isSelected());
			currentBoat.setFireAtWill(fireAtWillButton.isSelected());
			currentBoat.setIgnoreFriends(ignoreFriendsButton.isSelected());
			currentBoat.setFollowWP(followWPButton.isSelected());
			if (followWPButton.isClicked() && followWPButton.isSelected())
			{
				sliderSpeed.value.set(currentBoat.getWantedSpeed());
			}

			if (loopWPButton.isSelected() != currentBoat.isLoop())
			{
				currentBoat.setLoop(loopWPButton.isSelected());
				currentBoat.drawWP();
			}

			if ((stateEdition==TacticalMapPC.ED_MOVE_WP)&& (!wpTorpedoButton.isSelected()) && (!wpMissileButton.isSelected()))
			{
				// If no WP selected, first find one
				if (moveWP)
				{
					if ((currentBoat.getProgrammedWPs() != null) && (mouseX < TacticalMapPC.START_MENU))
					{
						// If one selected, move it!
						if (indexWPToMove < currentBoat.getProgrammedWPs().size())
						{
							currentBoat.getProgrammedWPs().getWP(indexWPToMove).setXWP(mouseX);
							currentBoat.getProgrammedWPs().getWP(indexWPToMove).setYWP(mouseY);
							currentBoat.resetWP();
						}
						currentBoat.drawWP();
					}
				}
			}

			if (sonarButton.isClicked())
				currentBoat.pushSonarOnOff(sonarButton.isSelected());

			if (radarButton.isClicked())
				currentBoat.pushRadarOnOff(radarButton.isSelected());

			
			if ((stateEdition==TacticalMapPC.ED_MOVE_WP) && wpTorpedoButton.isSelected())
			{
				// If no WP selected, first find one
				if (moveWP)
				{
					// If one selected, move it!
					wpTorpedoes.getWP(indexWPToMove).setXWP(mouseX);
					wpTorpedoes.getWP(indexWPToMove).setYWP(mouseY);
					
					this.myWPDrawer.setUpAndDrawWPs(this.wpTorpedoes);
				}
			}
			if ((stateEdition==TacticalMapPC.ED_MOVE_WP) && wpMissileButton.isSelected())
			{
				// If no WP selected, first find one
				if (moveWP)
				{
					// If one selected, move it!
					wpMissiles.getWP(indexWPToMove).setXWP(mouseX);
					wpMissiles.getWP(indexWPToMove).setYWP(mouseY);
					
					this.myWPDrawer.setUpAndDrawWPs(this.wpMissiles);
				}
			}
			
			

			if (addWPButton.isClicked())
			{
				if (addWPButton.isSelected())
				{
					stateEdition=TacticalMapPC.ED_ADD_WP;
					mapFree=false;
				}
				else
				{
					stateEdition=TacticalMapPC.ED_NONE;
					mapFree=true;
				}
				removeWPButton.setSelected(false);
				moveWPButton.setSelected(false);
				shootTorpedoButton.setSelected(false);
				shootMissileButton.setSelected(false);

				moveWP=false;
			}
			if (removeWPButton.isClicked())
			{
				if (removeWPButton.isSelected())
				{
					stateEdition=TacticalMapPC.ED_REMOVE_WP;
					mapFree=false;
				}
				else
				{
					stateEdition=TacticalMapPC.ED_NONE;
					mapFree=true;
				}
				addWPButton.setSelected(false);
				moveWPButton.setSelected(false);
				shootTorpedoButton.setSelected(false);
				shootMissileButton.setSelected(false);

				moveWP=false;
			}
			if (moveWPButton.isClicked())
			{
				if (moveWPButton.isSelected())
				{
					stateEdition=TacticalMapPC.ED_MOVE_WP;
					mapFree=false;
					currentBoat.getProgrammedWPs().edit();				
				}
				else
				{
					stateEdition=TacticalMapPC.ED_NONE;
					mapFree=true;
					currentBoat.getProgrammedWPs().unedit();
				}
				addWPButton.setSelected(false);
				removeWPButton.setSelected(false);
				shootTorpedoButton.setSelected(false);
				shootMissileButton.setSelected(false);

				moveWP=false;
			}

			// Weapon management
			if (shootTorpedoButton.isClicked())
			{
				if (shootTorpedoButton.isSelected())
				{
					stateEdition=TacticalMapPC.ED_SHOOT_TORPEDO;
				}
				else
				{
					stateEdition=TacticalMapPC.ED_NONE;
				}
				currentBoat.drawWP();

				mapFree=true;
				removeWPButton.setSelected(false);
				moveWPButton.setSelected(false);
				addWPButton.setSelected(false);
				wpTorpedoButton.setSelected(false);
				shootMissileButton.setSelected(false);
				wpMissileButton.setSelected(false);

				moveWP=false;
			}
			if (wpTorpedoButton.isClicked())
			{
				if (wpTorpedoButton.isSelected())
				{
					mapFree=false;
					this.myWPDrawer.setUpAndDrawWPs(this.wpTorpedoes);
				}
				else
				{
					stateEdition=TacticalMapPC.ED_NONE;
					mapFree=true;
					currentBoat.drawWP();
				}
				addWPButton.setSelected(false);
				moveWPButton.setSelected(false);
				shootTorpedoButton.setSelected(false);
				removeWPButton.setSelected(false);
				shootMissileButton.setSelected(false);
				wpMissileButton.setSelected(false);

				moveWP=false;
			}
			if (shootMissileButton.isClicked())
			{
				if (shootMissileButton.isSelected())
				{
					stateEdition=TacticalMapPC.ED_SHOOT_MISSILE;
				}
				else
				{
					stateEdition=TacticalMapPC.ED_NONE;
				}
				currentBoat.drawWP();
				
				mapFree=true;
				addWPButton.setSelected(false);
				removeWPButton.setSelected(false);
				shootTorpedoButton.setSelected(false);
				wpTorpedoButton.setSelected(false);
				moveWPButton.setSelected(false);
				wpMissileButton.setSelected(false);

				moveWP=false;
			}
			if (wpMissileButton.isClicked())
			{
				if (wpMissileButton.isSelected())
				{
					mapFree=false;
					this.myWPDrawer.setUpAndDrawWPs(this.wpMissiles);
				}
				else
				{
					stateEdition=TacticalMapPC.ED_NONE;
					mapFree=true;
					currentBoat.drawWP();
				}
				addWPButton.setSelected(false);
				removeWPButton.setSelected(false);
				shootTorpedoButton.setSelected(false);
				wpTorpedoButton.setSelected(false);
				shootMissileButton.setSelected(false);
				moveWPButton.setSelected(false);
				
				moveWP=false;
			}
		}
//		if (Input.isMouseDown())
//		{
//			mousePressed = true;
//			mouseX = Input.getMousePressX();
//			mouseY = Input.getMousePressY();
//		}
		
		if (Input.isMousePressed())
		{
			mousePressed = true;
			mouseX = Input.getMousePressX();
			mouseY = Input.getMousePressY();
			
			if (unitSelected && (mouseX < START_MENU))
			{
				// WPs managements!
				if (!wpTorpedoButton.isSelected() && !wpMissileButton.isSelected())
				{
					if (stateEdition==TacticalMapPC.ED_ADD_WP)
					{
						if (currentBoat.getProgrammedWPs() != null)
						{
							if (currentBoat.getProgrammedWPs().size() < 30)
							{
								boolean existWP=false;
								Journey setOfWPS=currentBoat.getProgrammedWPs();
								int sizeOfWp= setOfWPS.size();

								for (int iWP=0;iWP < sizeOfWp; iWP++)
								{
									Waypoint tmpWP = setOfWPS.getWP(iWP);
									if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
									{
										existWP=true;
										break;
									}
								}
								if (!existWP)
								{
									currentBoat.getProgrammedWPs().addWP(new Waypoint(mouseX,mouseY,0));
									currentBoat.drawWP();
								}
							}
						}
					}
					if (stateEdition==TacticalMapPC.ED_REMOVE_WP)
					{
						if (currentBoat.getProgrammedWPs() != null)
						{
							if (currentBoat.getProgrammedWPs().size() > 0)
							{
								Journey setOfWPS=currentBoat.getProgrammedWPs();
								int sizeOfWp= setOfWPS.size();

								for (int iWP=0;iWP < sizeOfWp; iWP++)
								{
									Waypoint tmpWP = setOfWPS.getWP(iWP);
									if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
									{
										setOfWPS.removeWP(iWP);
										sizeOfWp--;
										currentBoat.resetWP();
										break;
									}
								}

								currentBoat.drawWP();
							}
						}
					}
					//if (stateEdition==TacticalMapPC.ED_MOVE_WP)
					if ((stateEdition!=TacticalMapPC.ED_REMOVE_WP) && (stateEdition!=TacticalMapPC.ED_ADD_WP))
					{
						mapFree=true;
						// If no WP selected, first find one
						if (!moveWP)
						{
							if (currentBoat.getProgrammedWPs() != null)
							{
								if (currentBoat.getProgrammedWPs().size() > 0)
								{
									Journey setOfWPS=currentBoat.getProgrammedWPs();
									int sizeOfWp= setOfWPS.size();

									for (int iWP=0;iWP < sizeOfWp; iWP++)
									{
										Waypoint tmpWP = setOfWPS.getWP(iWP);
										if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
										{
											moveWP=true;
											indexWPToMove=iWP;
											stateEdition=TacticalMapPC.ED_MOVE_WP;
											moveWPButton.setSelected(true);
											removeWPButton.setSelected(false);
											shootTorpedoButton.setSelected(false);
											shootMissileButton.setSelected(false);
											currentBoat.getProgrammedWPs().edit();

											break;
										}
									}
								}
								if (moveWP)
									mapFree=false;
							}
						}
						else
						{
							// If one already selected, stop to edit it
							moveWP=false;
							currentBoat.resetWP();
						}
					}
					if (stateEdition==TacticalMapPC.ED_SHOOT_MISSILE)
					{
						currentBoat.requestFire(salveMissileButton.isSelected(), wpFollowMissileButton.isSelected(), seekMButton.isSelected(), goToTargetMButton.isSelected(), ProgrammableUnit.FIRE_MISSILE, mouseX, mouseY,wpMissiles);

						if (currentBoat.getType() == Boat.CARRIER)
						{
							if (currentBoat.getTypeFaction() == FUnit.BOAT_ALLIED)
							{

								if (alliesAirplanes != null)
								{
									int alliesAirpSize=alliesAirplanes.size();

									for (int iBoat = 0; iBoat < alliesAirpSize ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = alliesAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && (tmpAirplane.getOwner().getIdBoat() == currentBoat.getIdBoat()))
										{
											tmpAirplane.requestFire(salveMissileButton.isSelected(), wpFollowMissileButton.isSelected(), seekMButton.isSelected(), goToTargetMButton.isSelected(), ProgrammableUnit.FIRE_MISSILE, mouseX, mouseY,wpMissiles);
										}
									}
								}

							}
							if (currentBoat.getTypeFaction() == FUnit.BOAT_OUR)
							{

								if (ourAirplanes != null)
								{
									int  ourAirplSize=ourAirplanes.size();

									for (int iBoat = 0; iBoat < ourAirplSize ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = ourAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && (tmpAirplane.getOwner().getIdBoat() == currentBoat.getIdBoat()))
										{
											tmpAirplane.requestFire(salveMissileButton.isSelected(), wpFollowMissileButton.isSelected(), seekMButton.isSelected(), goToTargetMButton.isSelected(), ProgrammableUnit.FIRE_MISSILE, mouseX, mouseY,wpMissiles);
										}
									}
								}

							}
						}
						else
						{
							// Command the base's planes
							if (currentBoat.getTypeFaction() == FUnit.BOAT_ALLIED)
							{

								if (alliesAirplanes != null)
								{
									for (int iBoat = 0; iBoat < alliesAirplanes.size() ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = alliesAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && ((tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_ALLIED) || (tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_OUR)))
										{
											tmpAirplane.requestFire(salveMissileButton.isSelected(), wpFollowMissileButton.isSelected(), seekMButton.isSelected(), goToTargetMButton.isSelected(), ProgrammableUnit.FIRE_MISSILE, mouseX, mouseY,wpMissiles);
										}
									}
								}

							}
							if (currentBoat.getTypeFaction() == FUnit.BOAT_OUR)
							{

								if (ourAirplanes != null)
								{
									for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = ourAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && ((tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_OUR) || (tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_ALLIED)))
										{
											tmpAirplane.requestFire(salveMissileButton.isSelected(), wpFollowMissileButton.isSelected(), seekMButton.isSelected(), goToTargetMButton.isSelected(), ProgrammableUnit.FIRE_MISSILE, mouseX, mouseY,wpMissiles);
										}
									}
								}
							}
						}
					}
					if (stateEdition==TacticalMapPC.ED_SHOOT_TORPEDO)
					{
						currentBoat.requestFire(salveTorpedoButton.isSelected(), wpFollowTorpedoButton.isSelected(), seekTButton.isSelected(), goToTargetTButton.isSelected(), ProgrammableUnit.FIRE_TORPEDO, mouseX, mouseY,wpTorpedoes);

						if (currentBoat.getType() == Boat.CARRIER)
						{
							if (currentBoat.getTypeFaction() == FUnit.BOAT_ALLIED)
							{

								if (alliesAirplanes != null)
								{
									for (int iBoat = 0; iBoat < alliesAirplanes.size() ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = alliesAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && (tmpAirplane.getOwner().getIdBoat() == currentBoat.getIdBoat()))
										{
											tmpAirplane.requestFire(salveTorpedoButton.isSelected(), wpFollowTorpedoButton.isSelected(), seekTButton.isSelected(), goToTargetTButton.isSelected(), ProgrammableUnit.FIRE_TORPEDO, mouseX, mouseY,wpTorpedoes);	
										}
									}
								}

							}
							if (currentBoat.getTypeFaction() == FUnit.BOAT_OUR)
							{

								if (ourAirplanes != null)
								{
									for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = ourAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && (tmpAirplane.getOwner().getIdBoat() == currentBoat.getIdBoat()))
										{
											tmpAirplane.requestFire(salveTorpedoButton.isSelected(), wpFollowTorpedoButton.isSelected(), seekTButton.isSelected(), goToTargetTButton.isSelected(), ProgrammableUnit.FIRE_TORPEDO, mouseX, mouseY,wpTorpedoes);
										}
									}
								}

							}
						}
						else
						{
							// Command the base's planes
							if (currentBoat.getTypeFaction() == FUnit.BOAT_ALLIED)
							{

								if (alliesAirplanes != null)
								{
									for (int iBoat = 0; iBoat < alliesAirplanes.size() ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = alliesAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && ((tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_ALLIED) || (tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_OUR)))
										{
											tmpAirplane.requestFire(salveTorpedoButton.isSelected(), wpFollowTorpedoButton.isSelected(), seekTButton.isSelected(), goToTargetTButton.isSelected(), ProgrammableUnit.FIRE_TORPEDO, mouseX, mouseY,wpTorpedoes);

										}
									}
								}

							}
							if (currentBoat.getTypeFaction() == FUnit.BOAT_OUR)
							{

								if (ourAirplanes != null)
								{
									for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
									{
										// Request missile fire from the airplanes too
										tmpAirplane = ourAirplanes.get(iBoat);
										if (!tmpAirplane.isDead() && ((tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_OUR) || (tmpAirplane.getOwner().getTypeFaction() == FUnit.BASE_ALLIED)))
										{
											tmpAirplane.requestFire(salveTorpedoButton.isSelected(), wpFollowTorpedoButton.isSelected(), seekTButton.isSelected(), goToTargetTButton.isSelected(), ProgrammableUnit.FIRE_TORPEDO, mouseX, mouseY,wpTorpedoes);
										}
									}
								}

							}
						}
					}
				}
				if (wpTorpedoButton.isSelected())
				{
					if (stateEdition==TacticalMapPC.ED_ADD_WP)
					{
						if (wpTorpedoes.size() < 30)
						{
							boolean existWP=false;

							for (int iWP=0;iWP < wpTorpedoes.size(); iWP++)
							{
								Waypoint tmpWP = wpTorpedoes.getWP(iWP);
								if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
								{
									existWP=true;
									break;
								}
							}
							if (!existWP)
							{
								wpTorpedoes.addWP(new Waypoint(mouseX,mouseY,0));
								this.myWPDrawer.setUpAndDrawWPs(this.wpTorpedoes);
							}
						}
					}
					if (stateEdition==TacticalMapPC.ED_REMOVE_WP)
					{
						if (wpTorpedoes.size() > 0)
						{
							for (int iWP=0;iWP < wpTorpedoes.size(); iWP++)
							{
								Waypoint tmpWP = wpTorpedoes.getWP(iWP);
								if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
								{
									wpTorpedoes.removeWP(iWP);
									this.myWPDrawer.setUpAndDrawWPs(this.wpTorpedoes);
									break;
								}
							}
						}
					}
					//if (stateEdition==TacticalMapPC.ED_MOVE_WP)
					if ((stateEdition!=TacticalMapPC.ED_REMOVE_WP) && (stateEdition!=TacticalMapPC.ED_ADD_WP))
					{
						mapFree=true;
						// If no WP selected, first find one
						if (!moveWP)
						{
							if (wpTorpedoes.size() > 0)
							{
								for (int iWP=0;iWP < wpTorpedoes.size(); iWP++)
								{
									Waypoint tmpWP = wpTorpedoes.getWP(iWP);
									if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
									{
										moveWP=true;
										indexWPToMove=iWP;
										stateEdition=TacticalMapPC.ED_MOVE_WP;
										moveWPButton.setSelected(true);
										removeWPButton.setSelected(false);
										shootTorpedoButton.setSelected(false);
										shootMissileButton.setSelected(false);
										break;
									}
								}
								if (moveWP)
									mapFree=false;
							}
						}
						else
						{
							// If one already selected, stop to edit it
							moveWP=false;
						}
					}
				}
				
				if (wpMissileButton.isSelected())
				{
					if (stateEdition==TacticalMapPC.ED_ADD_WP)
					{
						if (wpMissiles.size() < 30)
						{
							boolean existWP=false;

							for (int iWP=0;iWP < wpMissiles.size(); iWP++)
							{
								Waypoint tmpWP = wpMissiles.getWP(iWP);
								if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
								{
									existWP=true;
									break;
								}
							}
							if (!existWP)
							{
								wpMissiles.addWP(new Waypoint(mouseX,mouseY,0));
								this.myWPDrawer.setUpAndDrawWPs(this.wpMissiles);
							}
						}
					}
					if (stateEdition==TacticalMapPC.ED_REMOVE_WP)
					{
						if (wpMissiles.size() > 0)
						{
							for (int iWP=0;iWP < wpMissiles.size(); iWP++)
							{
								Waypoint tmpWP = wpMissiles.getWP(iWP);
								if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
								{
									wpMissiles.removeWP(iWP);
									this.myWPDrawer.setUpAndDrawWPs(this.wpMissiles);
									break;
								}
							}
						}
					}
					//if (stateEdition==TacticalMapPC.ED_MOVE_WP)
					if ((stateEdition!=TacticalMapPC.ED_REMOVE_WP) && (stateEdition!=TacticalMapPC.ED_ADD_WP))
					{
						mapFree=true;
						// If no WP selected, first find one
						if (!moveWP)
						{
							if (wpMissiles.size() > 0)
							{
								for (int iWP=0;iWP < wpMissiles.size(); iWP++)
								{
									Waypoint tmpWP = wpMissiles.getWP(iWP);
									if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
									{
										moveWP=true;
										indexWPToMove=iWP;
										stateEdition=TacticalMapPC.ED_MOVE_WP;
										moveWPButton.setSelected(true);
										removeWPButton.setSelected(false);
										shootTorpedoButton.setSelected(false);
										shootMissileButton.setSelected(false);
										break;
									}
								}
							}
							if (moveWP)
								mapFree=false;
						}
						else
						{
							// If one already selected, stop to edit it
							moveWP=false;
						}
					}
				}
			}
			
//			if (missiles.size() < 80000)
//			{
//				// Fire a missile !!!
//				for (int iSalve=0;iSalve < 1000;iSalve++)
//				{
//					Missile oneMissile= new Missile(this);
//					oneMissile.createGfx(ourBoats.get(0).getPosX()+2*iSalve, ourBoats.get(0).getPosY(), 0, ourBoats.get(0).getOrientation(), 500);
//					oneMissile.setTargetPos(Input.getMousePressX(), Input.getMousePressY(), 0);
//					oneMissile.setCurrentSpeed(500);
//					oneMissile.setOrientation(ourBoats.get(0).getOrientation());
//					oneMissile.setPosX(ourBoats.get(0).getPosX()+2*iSalve);
//					oneMissile.setPosY(ourBoats.get(0).getPosY());
//
//					missiles.add(oneMissile);
//				}
//			}
//			if (torpedoes.size() < 80000)
//			{
//				// Fire a torpedo !!!
//				for (int iSalve=0;iSalve < 1000;iSalve++)
//				{
//					Torpedo oneTorpedo= new Torpedo(this);
//					oneTorpedo.createGfx(ourSubs.get(0).getPosX()+2*iSalve, ourSubs.get(0).getPosY(), 0, ourSubs.get(0).getOrientation(), 500);
//					oneTorpedo.setTargetPos(Input.getMousePressX(), Input.getMousePressY(), 0);
//					oneTorpedo.setCurrentSpeed(50);
//					oneTorpedo.setOrientation(ourSubs.get(0).getOrientation());
//					oneTorpedo.setPosX(ourSubs.get(0).getPosX()+2*iSalve);
//					oneTorpedo.setPosY(ourSubs.get(0).getPosY());
//					torpedoes.add(oneTorpedo);
//				}
//			}
//			if (torpedoes.size() < 8000)
//			{
//				// Fire a missile !!!
//				for (int iSalve=0;iSalve < 2;iSalve++)
//				{
//					Missile oneMissile= new Missile(this);
//					oneMissile.createGfx(ourSubs.get(0).getPosX()+2*iSalve, ourSubs.get(0).getPosY(), 0, ourSubs.get(0).getOrientation(), 500);
//					oneMissile.setTargetPos(Input.getMousePressX(), Input.getMousePressY(), 0);
//					oneMissile.setCurrentSpeed(500);
//					oneMissile.setOrientation(ourSubs.get(0).getOrientation());
//					oneMissile.setPosX(ourSubs.get(0).getPosX()+2*iSalve);
//					oneMissile.setPosY(ourSubs.get(0).getPosY());
//
//					missiles.add(oneMissile);
//				}
//				// Fire a torpedo !!!
//				for (int iSalve=0;iSalve < 4;iSalve++)
//				{
//					Torpedo oneTorpedo= new Torpedo(this);
//					oneTorpedo.createGfx(ourSubs.get(0).getPosX()+2*iSalve, ourSubs.get(0).getPosY(), 0, ourSubs.get(0).getOrientation(), 500);
//					oneTorpedo.setTargetPos(Input.getMousePressX(), Input.getMousePressY(), 0);
//					oneTorpedo.setCurrentSpeed(50);
//					oneTorpedo.setOrientation(ourSubs.get(0).getOrientation());
//					oneTorpedo.setPosX(ourSubs.get(0).getPosX()+2*iSalve);
//					oneTorpedo.setPosY(ourSubs.get(0).getPosY());
//					torpedoes.add(oneTorpedo);
//				}
//			}
		}
		if (Input.isPressed(Input.KEY_MOUSE_BUTTON_3))
		{
			rightMousePressed=true;
			infoLabel.setText("No unit selected");
			stateHullLabel.setText("Integrity - Hull XXX%");
			stateNavLabel.setText("DataLink XXX%");
			stateDataLabel.setText("Navigation XXX%");
			complementLabel.setText("Complement XXXX");
			
			autonomousButton.setSelected(false);
			fireAtWillButton.setSelected(false);
			ignoreFriendsButton.setSelected(false);
			followWPButton.setSelected(false);
			loopWPButton.setSelected(false);
			stateEdition=ED_NONE;
			
			unitSelected=false;
			mapFree=true;
			moveWP=false;
			
			if (currentBoat != null)
			{
				currentBoat.setView(false);
				currentBoat.resetWP();
			}
		}
		if (rightMousePressed)
		{
			myWPDrawer.hideMe();
		}
		
		// If one unit still has ammo, it will set that to false. Until it is true, the game continue until
		// one side win or the ammo are gone
		isOutOfAmmo = true;
		notShooting = true;
		
		// Go through the units and update them!
		if (ourSubs != null)
		{
			int ourSubsSize = ourSubs.size();
			
			for (int iSub = 0; iSub < ourSubsSize ; iSub++)
			{
				// Update !!!
				ourSubs.get(iSub).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					ourSubs.get(iSub).showLabel();
				}
				if (nameToHide)
				{
					ourSubs.get(iSub).hideLabel();
				}
			}
		}
		if (ourBoats != null)
		{
			int ourBoatsSize = ourBoats.size();
			
			for (int iBoat = 0; iBoat < ourBoatsSize ; iBoat++)
			{
				//System.out.println("Update our boat "+iBoat);
				// Update !!!
				ourBoats.get(iBoat).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					ourBoats.get(iBoat).showLabel();
				}
				if (nameToHide)
				{
					ourBoats.get(iBoat).hideLabel();
				}
				
				if ((mousePressed) && mapFree)
				{
					if ((Math.abs((ourBoats.get(iBoat).getPosX() - mouseX)) < 20) && (Math.abs((ourBoats.get(iBoat).getPosY()  - mouseY)) < 20))
					{
						cleanUnit();
						currentBoat = ourBoats.get(iBoat);
						// This boat is selected
						ourBoats.get(iBoat).drawWP();
						unitToSelect=true;
						stateEdition=TacticalMapPC.ED_NONE;
					}
				}
			}
		}
		if (alliesSubs != null)
		{
			int alliesSubsSize = alliesSubs.size();
			
			for (int iSub = 0; iSub < alliesSubsSize ; iSub++)
			{
				// Update !!!
				alliesSubs.get(iSub).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					alliesSubs.get(iSub).showLabel();
				}
				if (nameToHide)
				{
					alliesSubs.get(iSub).hideLabel();
				}
			}
		}
		if (alliesBoats != null)
		{
			int alliesBoatsSize = alliesBoats.size();
			
			for (int iBoat = 0; iBoat < alliesBoatsSize ; iBoat++)
			{
				// Update !!!
				alliesBoats.get(iBoat).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					alliesBoats.get(iBoat).showLabel();
				}
				if (nameToHide)
				{
					alliesBoats.get(iBoat).hideLabel();
				}
				
				if ((mousePressed) && mapFree)
				{
					if ((Math.abs((alliesBoats.get(iBoat).getPosX() - mouseX)) < 20) && (Math.abs((alliesBoats.get(iBoat).getPosY()  - mouseY)) < 20))
					{
						cleanUnit();
						currentBoat = alliesBoats.get(iBoat);
						// This boat is selected
						alliesBoats.get(iBoat).drawWP();
						unitToSelect=true;
						stateEdition=TacticalMapPC.ED_NONE;
					}
				}
			}
		}
		if (enemiesSubs != null)
		{
			int enemiesSubsSize = enemiesSubs.size();
			
			for (int iSub = 0; iSub < enemiesSubsSize ; iSub++)
			{
				// Update !!!
				enemiesSubs.get(iSub).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					enemiesSubs.get(iSub).showLabel();
				}
				if (nameToHide)
				{
					enemiesSubs.get(iSub).hideLabel();
				}
				
				
				if (Input.isPressed(Input.KEY_D))
					enemiesSubs.get(iSub).showMe();
			}
		}
		if (enemiesBoats != null)
		{
			int enemiesBoatsSize = enemiesBoats.size();
			
			for (int iBoat = 0; iBoat < enemiesBoatsSize ; iBoat++)
			{
				// Update !!!
				enemiesBoats.get(iBoat).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					enemiesBoats.get(iBoat).showLabel();
				}
				if (nameToHide)
				{
					enemiesBoats.get(iBoat).hideLabel();
				}
				
				if (Input.isPressed(Input.KEY_D))
					enemiesBoats.get(iBoat).showMe();
			}
		}
		if (neutralSubs != null)
		{
			int neutralSubsSize = neutralSubs.size();
			
			for (int iSub = 0; iSub < neutralSubsSize ; iSub++)
			{
				// Update !!!
				neutralSubs.get(iSub).doUpdate(timeUsed);
			}
		}
		if (neutralBoats != null)
		{
			int neutralBoatsSize = neutralBoats.size();
			
			for (int iBoat = 0; iBoat < neutralBoatsSize ; iBoat++)
			{
				// Update !!!
				neutralBoats.get(iBoat).doUpdate(timeUsed);
			}
		}
		if (globalSensors != null)
		{
			int globalSensorsSize = globalSensors.size();
			
			for (int iSensors = 0; iSensors < globalSensorsSize ; iSensors++)
			{
				// Update !!!
				globalSensors.get(iSensors).doUpdate(timeUsed);
			}
		}
		if (alliesAirplanes != null)
		{
			int alliesAirplanesSize = alliesAirplanes.size();
			
			for (int iBoat = 0; iBoat < alliesAirplanesSize ; iBoat++)
			{
				// Update !!!
				tmpAirplane = alliesAirplanes.get(iBoat);
				tmpAirplane.doUpdate(timeUsed);
				if (tmpAirplane.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpAirplane.removeMe();
					alliesAirplanes.remove(iBoat);
					alliesAirplanesSize--;
					iBoat--;
				}
			}
		}
		if (ourAirplanes != null)
		{
			int ourAirplanesSize = ourAirplanes.size();
			
			for (int iBoat = 0; iBoat < ourAirplanesSize ; iBoat++)
			{
				// Update !!!
				tmpAirplane = ourAirplanes.get(iBoat);
				tmpAirplane.doUpdate(timeUsed);
				if (tmpAirplane.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpAirplane.removeMe();
					ourAirplanes.remove(iBoat);
					ourAirplanesSize--;
					iBoat--;
				}
			}
		}
		if (enemiesAirplanes != null)
		{
			int enemiesAirplanesSize = enemiesAirplanes.size();
			
			for (int iBoat = 0; iBoat < enemiesAirplanesSize ; iBoat++)
			{
				// Update !!!
				tmpAirplane = enemiesAirplanes.get(iBoat);
				tmpAirplane.doUpdate(timeUsed);
				if (tmpAirplane.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpAirplane.removeMe();
					enemiesAirplanes.remove(iBoat);
					enemiesAirplanesSize--;
					iBoat--;
				}
			}
		}
		if (ourBases != null)
		{
			int ourBasesSize = ourBases.size();
			
			for (int iBoat = 0; iBoat < ourBasesSize ; iBoat++)
			{
				// Update !!!
				ourBases.get(iBoat).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					ourBases.get(iBoat).showLabel();
				}
				if (nameToHide)
				{
					ourBases.get(iBoat).hideLabel();
				}
			}
		}
		if (alliesBases != null)
		{
			int alliesBasesSize = alliesBases.size();
			
			for (int iBoat = 0; iBoat < alliesBasesSize ; iBoat++)
			{
				// Update !!!
				alliesBases.get(iBoat).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					alliesBases.get(iBoat).showLabel();
				}
				if (nameToHide)
				{
					alliesBases.get(iBoat).hideLabel();
				}
			}
		}
		if (enemiesBases != null)
		{
			int enemiesBasesSize = enemiesBases.size();
			
			for (int iBoat = 0; iBoat < enemiesBasesSize ; iBoat++)
			{
				// Update !!!
				enemiesBases.get(iBoat).doUpdate(timeUsed);
				
				if (nameToShow)
				{
					enemiesBases.get(iBoat).showLabel();
				}
				if (nameToHide)
				{
					enemiesBases.get(iBoat).hideLabel();
				}
			}
		}
		if (neutralBases != null)
		{
			int neutralBasesSize = neutralBases.size();
			
			for (int iBoat = 0; iBoat < neutralBasesSize ; iBoat++)
			{
				// Update !!!
				neutralBases.get(iBoat).doUpdate(timeUsed);
			}
		}
		if (missiles != null)
		{
			int missilesSize = missiles.size();
			
			for (int iMissiles = 0; iMissiles < missilesSize ; iMissiles++)
			{
				tmpMissile = missiles.get(iMissiles);
				tmpMissile.doUpdate(timeUsed);
				if (tmpMissile.toRemove())
				{
					//System.out.println("Remove missile "+missiles.size());
					tmpMissile.removeMe();
					missiles.remove(iMissiles);
					missilesSize--;
					iMissiles--;
				}
			}
		}
		if (torpedoes != null)
		{
			int torpedoesSize = torpedoes.size();
			
			for (int iTorpedoes = 0; iTorpedoes < torpedoesSize ; iTorpedoes++)
			{
				tmpTorpedo = torpedoes.get(iTorpedoes);
				tmpTorpedo.doUpdate(timeUsed);
				if (tmpTorpedo.toRemove())
				{
					//System.out.println("Remove torpedo "+torpedoes.size());
					tmpTorpedo.removeMe();
					torpedoes.remove(iTorpedoes);
					torpedoesSize--;
					iTorpedoes--;
				}
			}
		}
		if (trees != null)
		{
			int treesSize = trees.size();
		
			for (int iTree = 0; iTree < treesSize ; iTree++)
			{
				// Update !!!
				tmpTree = trees.get(iTree);
				tmpTree.doUpdate(timeUsed);
				if (tmpTree.toRemove())
				{
					//System.out.println("Remove tree "+missiles.size());
					tmpTree.removeMe();
					trees.remove(iTree);
					treesSize--;
					iTree--;
				}
			}
		}
		
		if (isOutOfAmmo)
		{
			if (!gameOver)
			{
				DrawBackground.drawWinLose(DrawBackground.NO_AMMO_LEFT);
				gameOver = true;
				
				addDetailedScores();
//				endRect.invalidate();
//				endLevelButton.enabled.set(true);
			}
		}
		if (notShooting)
		{
			timeSinceLastShoot+=timeUsed;
			
			endRect.setSizeRect(WIDTH_END_BUTTON-DIVIDER_END_BUTTON*timeSinceLastShoot, 32);
			//System.out.println("Not shooting for "+timeSinceLastShoot);
			if (timeSinceLastShoot > THIRTY_SECONDS)
			{
				endRect.invalidate();
				endLevelButton.enabled.set(true);
			}
		}
		else
		{
			timeSinceLastShoot = 0;
		}
		if (gameOver)
		{
			yDetailedResult-=2000f/divider; // divider = 10000
			
			if (yDetailedResult < -loadingFillingText.height.getAsInt())
			{
				yDetailedResult = 1000;	
			}
			// From time to time do an update of the score
			//if ((yDetailedResult*2) % 20 == 0)
			{
				ourScene.remove(loadingFillingText);
				addDetailedScores();
			}
			loadingFillingText.setLocation(400, yDetailedResult);
		}
//		if (Input.isDown(Input.KEY_E))
//			return DrawMap.myName;
		return null;
	}

	/**
	 * Do something when a sub die.
	 * Check here if we print the win/lose message
	 * Cannot be in the parent class as it is also used in the title page,
	 * might appear here so.
	 * 
	 * TODO: Allow ending if no ammo in any live unit left
	 * Allow ending after a time
	 * @see com.tgb.subgame.LevelMap#subDead(int)
	 */
	public void subDead(int typeFaction)
	{
		super.subDead(typeFaction);
		
		checkGameOverCondition();
	}

	public void boatDead(int typeFaction)
	{
		super.boatDead(typeFaction);

		checkGameOverCondition();
	}


	public void baseDead(int typeFaction)
	{
		super.baseDead(typeFaction);
		
		if ((nbEnemiesTotal == 0) || (nbTotalGood == 0))
			endLevelButton.enabled.set(true);
		checkGameOverCondition();
	}

	/**
	 * 
	 */
	private void checkGameOverCondition()
	{
		if ((nbEnemiesTotal == 0) || (nbTotalGood == 0))
		{
			endLevelButton.enabled.set(true);
			endRect.invalidate();
		}
		if ((!gameOver))
		{
			if (nbEnemiesTotal == 0)
			{
				DrawBackground.drawWinLose(DrawBackground.WIN);
				
				addDetailedScores();
		    	
				gameOver = true;
				// For now the score keeper is used only for the quick map, but it
				// might be used later to keep a track of the level achievement in the
				// conquest map. It is ok to set hasWon anyway, it is used or not...
				ScoreKeeper.getInstance().setHasWon(true);
			}
			if (nbTotalGood == 0)
			{
				DrawBackground.drawWinLose(DrawBackground.LOSE);
				
				addDetailedScores();
				
				gameOver = true;
				ScoreKeeper.getInstance().setHasWon(false);
			}
		}
	}

	private void addDetailedScores()
	{
		StringBuffer detailedScore = new StringBuffer();
		
		detailedScore.append("== Your units ==\n\n");
		
		if (ourBoats.size() > 0)
		{
			detailedScore.append("= Ships\n\n");
			for (ProgrammableUnit oneBoat : ourBoats)
			{
				showDetailForAnUnit(detailedScore, oneBoat, true);
			}
		}
		if (ourSubs.size() > 0)
		{
			detailedScore.append("\n= Submarines\n\n");
			for (ProgrammableUnit oneBoat : ourSubs)
			{
				showDetailForAnUnit(detailedScore, oneBoat, true);
			}
		}
		if (ourBases.size() > 0)
		{
			detailedScore.append("\n= Bases\n\n");
			for (ProgrammableUnit oneBoat : ourBases)
			{
				showDetailForAnUnit(detailedScore, oneBoat, false);
			}
		}
		detailedScore.append("\n\n== Allied units ==\n\n");
		if (alliesBoats.size() > 0)
		{
			detailedScore.append("\n= Ships\n\n");
			for (ProgrammableUnit oneBoat : alliesBoats)
			{
				showDetailForAnUnit(detailedScore, oneBoat, true);
			}
		}
		if (alliesSubs.size() > 0)
		{
			detailedScore.append("\n= Submarines\n\n");
			for (ProgrammableUnit oneBoat : alliesSubs)
			{
				showDetailForAnUnit(detailedScore, oneBoat, true);
			}
		}
		if (alliesBases.size() > 0)
		{
			detailedScore.append("\n= Bases\n\n");
			for (ProgrammableUnit oneBoat : alliesBases)
			{
				showDetailForAnUnit(detailedScore, oneBoat, false);
			}
		}
		detailedScore.append("\n\n== Enemies units ==\n\n");
		if (enemiesBoats.size() > 0)
		{
			detailedScore.append("\n= Ships\n\n");
			
			for (ProgrammableUnit oneBoat : enemiesBoats)
			{
				showDetailForAnUnit(detailedScore, oneBoat, true);
			}
		}
		if (enemiesSubs.size() > 0)
		{
			detailedScore.append("\n= Submarines\n\n");
			
			for (ProgrammableUnit oneBoat : enemiesSubs)
			{
				showDetailForAnUnit(detailedScore, oneBoat, true);
			}
		}
		if (enemiesBases.size() > 0)
		{
			detailedScore.append("\n= Bases\n\n");
			
			for (ProgrammableUnit oneBoat : enemiesBases)
			{
				showDetailForAnUnit(detailedScore, oneBoat, false);
			}
		}
		
		loadingFillingText = Label.createMultilineLabel(detailedScore.toString() , 400, yDetailedResult);
    	loadingFillingText.setAnchor(0.5,0);
    	ourScene.add(loadingFillingText);
	}


	/**
	 * @param detailedScore
	 * @param oneUnit
	 */
	private void showDetailForAnUnit(StringBuffer detailedScore, ProgrammableUnit oneUnit, boolean isBoat)
	{
		String destroyMessage = " - Sunk ";
		if (!isBoat)
		{
			destroyMessage = " - Destroyed ";
		}
		detailedScore.append(oneUnit.getTypeName()+" - "+ oneUnit.getName()+(oneUnit.isDead()?destroyMessage:"")+" - "+oneUnit.getComplement()+" persons left of " + oneUnit.getComplementNorm()+"\n");
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
			System.out.println("Quit the Tactical map");
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
		System.out.println("Create tactical map");
		
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
		currentBoat=null; // If it is a boat

		unitSelected=false;
		unitToSelect=false;
		
		timeSinceClick=0;
		
		mapFree=true; // can we click freely on the map
		
		moveWP=false;
		
		stateEdition=ED_NONE;
		
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
		trees = new ArrayList<Trees>();
		
		wpMissiles= new Journey();
		wpTorpedoes= new Journey();
		// Load the map
		DrawBackground.drawBack();
		
		// Load the sprites
		gfxSprites.callMeFirst();
		ourScene=(Scene2D )Stage.getScene();
		
		if (!LevelKeeper.getInstance().isBlank())
		{
			levelImage=LevelKeeper.getInstance().getCurrentLevel();
			spriteLevel= new FlatSpritePC(levelImage);
			spriteLevel.setPos(Stage.getWidth()/2,  Stage.getHeight()/2, 0);
			if (RenderingManager.getInstance().in3D)
				spriteLevel.invalidate();
			
			idSpriteLevel = RenderingManager.getInstance().addDrawableEntity(spriteLevel,1);
		}
        
		infoLabel= new Label("No unit selected", 700, 20);
		ourScene.add(infoLabel);
		stateHullLabel= new Label("Integrity - Hull XXX%", 700, 50);
		ourScene.add(stateHullLabel);
		stateNavLabel= new Label("DataLink XXX%", 700, 80);
		ourScene.add(stateNavLabel);
		stateDataLabel= new Label("Navigation XXX%", 700, 110);
		ourScene.add(stateDataLabel);
		complementLabel= new Label("Complement XXXX", 700, 140);
		ourScene.add(complementLabel);

		speedLabel= new Label("Speed wanted", 800, 170);
		speedLabel.setAnchor(Sprite.CENTER);
		ourScene.add(speedLabel);
		sliderSpeed = new Slider("SpeedBarre.png", "SpeedCursor.png", 700, 180);
		ourScene.add(sliderSpeed);
		
//		depthLabel= new Label("Depth wanted", 800, 690);
//		depthLabel.setAnchor(Sprite.CENTER);
//		ourScene.add(depthLabel);
//		sliderDepth = new Slider("SpeedBarre.png", "SpeedCursor.png", 700, 700);
//		ourScene.add(sliderDepth);
		
		weaponLabel= new Label("000 Torpedoes 000 Missiles", 700, 210);
		ourScene.add(weaponLabel);
		
		autonomousButton=Button.createLabeledToggleButton("Is autonomous", 700, 240);
		fireAtWillButton=Button.createLabeledToggleButton("Attack at will", 700, 270);
		ignoreFriendsButton=Button.createLabeledToggleButton("Attack all units", 700, 300);
		ourScene.add(autonomousButton);
		ourScene.add(fireAtWillButton);
		ourScene.add(ignoreFriendsButton);
		
		// WP buttons.
		followWPButton=Button.createLabeledToggleButton("Follow WP", 700, 330);
		ourScene.add(followWPButton);
		loopWPButton=Button.createLabeledToggleButton("Loop", 820, 330);
		ourScene.add(loopWPButton);
		
		addWPButton=Button.createLabeledToggleButton("+", 700, 360);
		ourScene.add(addWPButton);
		removeWPButton=Button.createLabeledToggleButton("-", 780, 360);
		ourScene.add(removeWPButton);
		moveWPButton=Button.createLabeledToggleButton("Move", 860, 360);
		ourScene.add(moveWPButton);
		
		// Fire buttons
		shootTorpedoButton=Button.createLabeledToggleButton("Fire torpedo", 700, 390);
		salveTorpedoButton=Button.createLabeledToggleButton("Salve", 820, 390);
		wpFollowTorpedoButton=Button.createLabeledToggleButton("Follow WP", 700, 420);
		wpTorpedoButton=Button.createLabeledToggleButton("Edit WP", 820, 420);
		seekTButton=Button.createLabeledToggleButton("Seek", 700, 450);
		goToTargetTButton=Button.createLabeledToggleButton("Go to target", 820, 450);
		
		seekTButton.setSelected(true);
		goToTargetTButton.setSelected(true);
		
		shootMissileButton=Button.createLabeledToggleButton("Fire missile", 700, 480);
		salveMissileButton=Button.createLabeledToggleButton("Salve", 820, 480);
		wpFollowMissileButton=Button.createLabeledToggleButton("Follow WP", 700, 510);
		wpMissileButton=Button.createLabeledToggleButton("Edit WP", 820, 510);
		seekMButton=Button.createLabeledToggleButton("Seek", 700, 540);
		goToTargetMButton=Button.createLabeledToggleButton("Go to target", 820, 540);
		
		seekMButton.setSelected(true);
		goToTargetMButton.setSelected(true);
		
		ourScene.add(shootTorpedoButton);
		ourScene.add(salveTorpedoButton);
		ourScene.add(wpFollowTorpedoButton);
		ourScene.add(wpTorpedoButton);
		ourScene.add(seekTButton);
		ourScene.add(goToTargetTButton);
		ourScene.add(shootMissileButton);
		ourScene.add(salveMissileButton);
		ourScene.add(wpFollowMissileButton);
		ourScene.add(wpMissileButton);
		ourScene.add(seekMButton);
		ourScene.add(goToTargetMButton);
		
		sonarButton = Button.createLabeledToggleButton("Sonar Active", 700, 580);
		radarButton = Button.createLabeledToggleButton("Radar Active", 820, 580);
		
		ourScene.add(sonarButton);
		ourScene.add(radarButton);
		
//		wpRectangle = new FilledRectanglePC(); // Show which WP you are editing.
//		wpRectangle.setSizeRect(200, 100);
//		wpRectangle.setPos(700, 200, 0);
//		wpRectangle.setOurColor(0xFFFF0000);
		//RenderingManager.getInstance().addDrawableEntity(wpRectangle,39);
		
		missileRect = new FilledRectanglePC(); // Show if you can fire missiles (useful for submarine)
		missileRect.setSizeRect(240, 94);
		missileRect.setPos(698, 478, 0);
		missileRect.setOurColor(0xFFFF0000);
		missileRect.invalidate();
		RenderingManager.getInstance().addDrawableEntity(missileRect,39);
		radarRect = new FilledRectanglePC(); // Show if you can activate the radar (submarine again)
		radarRect.setSizeRect(120, 32);
		radarRect.setPos(818, 578, 0);
		radarRect.setOurColor(0xFFFF0000);
		radarRect.invalidate();
		RenderingManager.getInstance().addDrawableEntity(radarRect,39);
		
		endLevelButton= Button.createLabeledToggleButton("End level", 760, 750);
		
		ourScene.add(endLevelButton);
		
		endRect = new FilledRectanglePC(); // Show if you can activate the radar (submarine again)
		endRect.setSizeRect(WIDTH_END_BUTTON, 32);
		endRect.setPos(758, 748, 0);
		endRect.setOurColor(0xFFFF0000);
		//endRect.invalidate();
		RenderingManager.getInstance().addDrawableEntity(endRect,39);
		
		// SWITCHABLE
		endLevelButton.enabled.set(false);
		
		ourKD = new KnownDatas(this);
		
		// Load the good level
		
		currentLevel = LevelKeeper.getInstance().getNextLevelWanted();
		
		// TODO Auto-generated method stub
		try
		{
			setLevel(currentLevel);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		lastTime=new java.util.Date().getTime();
		myWPDrawer = new WPDrawer();
		
		clouds = null;
		
		if (Math.random() < 0.2)
		{
			clouds = new SpritePC[5];
			CoreImage cloudImage=CoreImage.load("Nuage2.png");
			for (int iClouds=0;iClouds < clouds.length ; iClouds++)
			{
				clouds[iClouds]=new SpritePC(cloudImage);
				clouds[iClouds].setSize(Math.random()*3+1);
				clouds[iClouds].setRotation(Math.random()*4);
				clouds[iClouds].setPos(com.tgb.subgame.RandomContainer.getInstance().myRandom.nextInt(700), com.tgb.subgame.RandomContainer.getInstance().myRandom.nextInt(800), 0);
				RenderingManager.getInstance().addDrawableEntity(clouds[iClouds],37);
				
				cloudSpeed = Math.random()*50;
			}
		}
		else if (Math.random() < 0.3)
		{
			clouds = new SpritePC[5];
			CoreImage cloudImage=CoreImage.load("Nuage3.png");
			for (int iClouds=0;iClouds < clouds.length ; iClouds++)
			{
				clouds[iClouds]=new SpritePC(cloudImage);
				clouds[iClouds].setSize(Math.random()*3+1);
				clouds[iClouds].setRotation(Math.random()*4);
				clouds[iClouds].setPos(com.tgb.subgame.RandomContainer.getInstance().myRandom.nextInt(700), com.tgb.subgame.RandomContainer.getInstance().myRandom.nextInt(800), 0);
				RenderingManager.getInstance().addDrawableEntity(clouds[iClouds],37);
				
				cloudSpeed = Math.random()*120;
			}
		}
			
		gameOver = false;
	}
}
