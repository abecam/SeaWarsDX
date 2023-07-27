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
import com.tgb.subengine.gamesystems.StateManager;
import com.tgb.subengine.particlessystem.ParticleManager;
import com.tgb.subengine.gamesystems.IGamePart;
import com.tgb.subengine.gameentities.*;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subgame.levels.*;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.unitspc.sensors.*;
import com.tgb.subgame.ToolDrawMap;

/**
 * The tactical map is the central part, the level, managing the actual (hopefully entertaining)
 * game. Adapted for the tutorial.
 * @author Alain Becam
 */
public class TutorialMap2 extends LevelMap implements IGamePart
{
	public final static int START_MENU=700; // Where the menu start (so where the map shouldn't be) 
	
	boolean upKey,downKey,leftKey,rightKey=false;
	
	static final String myName = "TutorialMap1";
	
	ProgrammableUnit currentUnit=null; // The selected unit.
	Submarine currentSub=null; // If it is a sub
	Boat currentBoat=null; // If it is a boat
	boolean isBoat=true;

	boolean unitSelected=false;
	boolean unitToSelect=false;
	
	boolean endAsked=false;
	long timeSinceClick=0;
	
	String nameTypeUnit;
	
	Label infoLabel;
	Label stateHullLabel;
	Label stateDataLabel;
	Label stateNavLabel;
	Label complementLabel;
	Label speedLabel;
	Slider sliderSpeed;
	Label depthLabel;
	Slider sliderDepth;
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
	Button menuButton;
	
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
	
	ToolDrawMap myDrawer;
	boolean wantDraw=false;
	
	Group tutText;
	SpritePC arrow;
	SpritePC arrow2;
	long idArrow,idArrow2;
	
	Button nextButton;
	Button backButton;
	
//	FlatSpritePC background;
//	long idBackground;
	
	public TutorialMap2()
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
		
		removeUnits();
		
		ourKD.removeMeFromMap();
		
		myWPDrawer.removeMe();
		myWPDrawer = null;
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
		ourScene.remove(depthLabel);
		ourScene.remove(sliderDepth);
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
		ourScene.remove(menuButton);
		
		if (tutText != null)
			ourScene.remove(tutText);
		ourScene.remove(nextButton);
		ourScene.remove(backButton);
		
		cleanMemory();
		
		System.out.println("Tactical map cleaned");
	}

	private void removeUnits()
	{
		long totalXPWon=0;
		if (enemiesSubs != null)
		{
			for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
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
				}
				enemiesSubs.get(iSub).removeMe();
			}
			enemiesSubs.clear();
		}
		if (enemiesBoats != null)
		{
			for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
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
				}
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
				}
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
				if (ourSubs.get(iSub).isDead())
				{
					;
				}
				else
				{
					ourSubs.get(iSub).addXP(totalXPWon);
				}
				ourSubs.get(iSub).removeMe();
			}
			ourSubs.clear();
		}
		if (ourBoats != null)
		{
			for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
			{
				// Update !!!
				if (ourBoats.get(iBoat).isDead())
				{
					// Inform the game keeper
					;
				}
				else
				{
					ourBoats.get(iBoat).addXP(totalXPWon);
				}
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
				if (alliesSubs.get(iSub).isDead())
				{
					// Inform the game keeper
					;
				}
				else
				{
					alliesSubs.get(iSub).addXP(totalXPWon);
				}
				alliesSubs.get(iSub).removeMe();
			}
			alliesSubs.clear();
		}
		if (alliesBoats != null)
		{
			for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
			{
				// Update !!!
				if (alliesBoats.get(iBoat).isDead())
				{
					// Inform the game keeper
					;
				}
				else
				{
					alliesBoats.get(iBoat).addXP(totalXPWon);
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
					;
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
					;
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
					;
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
					;
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
					;
				}
				neutralBases.get(iBoat).resetAirplaneInFlight();
				neutralBases.get(iBoat).removeMe();
			}
			neutralBases.clear();
		}
	}
	
	private void removeEnemies()
	{
		if (enemiesSubs != null)
		{
			for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
			{
				// Update !!!
				if (enemiesSubs.get(iSub).isDead())
				{
					;
				}
				enemiesSubs.get(iSub).removeMe();
			}
			enemiesSubs.clear();
		}
		if (enemiesBoats != null)
		{
			for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
			{
				// Update !!!
				if (enemiesBoats.get(iBoat).isDead())
				{
					;
				}
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
				if (enemiesBases.get(iBoat).isDead())
				{
					;
				}
				enemiesBases.get(iBoat).resetAirplaneInFlight();
				enemiesBases.get(iBoat).removeMe();
			}
			enemiesBases.clear();
		}
		
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

		infoLabel = null;
		stateHullLabel = null;
		stateDataLabel = null;
		stateNavLabel = null;
		complementLabel = null;
		speedLabel = null;
		sliderSpeed = null;
		depthLabel = null;
		sliderDepth = null;
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
		
		arrow = null;
		arrow2 = null;
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
//	//DEBUG///////////////////
//	double timeSinceStart=0;
	
	boolean nameToShow=false;
	boolean nameToHide=false;
	double timeChange=0;
	
	double arrSize=1;
	int sequenceNb=0;
	
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
		if (Input.isDown(Input.KEY_N))
			divider=2500;
		if (Input.isDown(Input.KEY_B))
			divider=1250;
		timeUsed= ((double )timeInterval)/divider;
		
		mouseX = Input.getMouseX();
		mouseY = Input.getMouseY();
		//System.out.println("Tactical map: do update");
/////DEBUG//////////////////////////////////		
//		timeSinceStart+=timeUsed;
//		if (timeSinceStart > 2.0)
//		{
//			timeSinceStart=0;
//			return StrategicMap.myName;
//		}
//////////////////////////////////////		
//		if (arrow != null)
//		{
//			arrow.setPos(mouseX, mouseY, 0);
//			if (Input.isDown(Input.KEY_RIGHT))
//			{
//				arrow.rotate(0.2);
//			}
//			if (Input.isDown(Input.KEY_LEFT))
//			{
//				arrow.rotate(-0.2);
//			}
//			if (Input.isDown(Input.KEY_UP))
//			{
//				arrSize+=0.2;
//				arrow.setSize(arrSize);
//			}
//			if (Input.isDown(Input.KEY_DOWN))
//			{
//				arrSize-=0.2;
//				arrow.setSize(arrSize);
//			}
//			if (Input.isDown(Input.KEY_D))
//			{
//				System.out.println("Arrow "+arrow.getX()+" , "+arrow.getY()+" y - "+arrow.getSize()+" size - "+arrow.getRotation());
//				System.out.println("RenderingManager.getInstance().removeEntity(idArrow,39);");
//				System.out.println("arrow.setPos("+arrow.getX()+", "+arrow.getY()+", 0);");
//				System.out.println("arrow.setRotation("+arrow.getRotation()+");");
//				System.out.println("arrow.setSize("+arrow.getSize()+");");
//				System.out.println("idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);");
//			}
//		}
		
		if (backButton.isClicked())
		{
			sequenceNb--;
			if (sequenceNb < 0)
			{
				sequenceNb=0;
				StateManager.getInstance().setValueToPass(22); // Might be anything above
				// 0... :) It is not the actual number but an info...
				return TutorialMap1.myName;
			}
			
			goToSequence(sequenceNb);
		}
		if (nextButton.isClicked())
		{
			sequenceNb++;
			if (sequenceNb == 40)
			{
				sequenceNb=0;
				LevelKeeper.getInstance().setNextMapX(0);
				LevelKeeper.getInstance().setNextMapY(0);
				LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.MAIN_MENU);
				return DrawMap.myName;
			}
			else
				goToSequence(sequenceNb);
		}
		
		if (menuButton.isClicked())
		{
			if (tmpTime-timeSinceClick < 3000)
			{
			// Push up the current info and return to the map
				if (LevelKeeper.getInstance().getNextLevelWanted() != 1)
				{
					LevelKeeper.getInstance().setNextMapX(0);
					LevelKeeper.getInstance().setNextMapY(0);
					LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.MAIN_MENU);
					return DrawMap.myName;
				}
				else
					return StrategicMap.myName;
			}
			timeSinceClick=tmpTime;
			menuButton.setSelected(false);
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
//		if (Input.isDown(Input.KEY_F2))
//		{
//			if (!wantDraw)
//				wantDraw = true;
//		}
		if (wantDraw)
		{
			if (myDrawer.transformRoundStep())
			{
				wantDraw = false;
				myDrawer.convertRelief();
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
			if (isBoat)
			{
				currentBoat.setView(true);
				switch (currentBoat.getType())
				{
					case Boat.CARRIER:
						nameTypeUnit="Carrier"; 
						break;
					case Boat.AMPHIBIOUS:
						nameTypeUnit="Amphibious"; 
						break;
					case Boat.CORVETTE:
						nameTypeUnit="Corvette"; 
						break;
					case Boat.CRUISER:
						nameTypeUnit="Cruiser"; 
						break;
					case Boat.DESTROYER:
						nameTypeUnit="Destroyer"; 
						break;
					case Boat.FRIGATE:
						nameTypeUnit="Frigate"; 
						break;
						default:
							nameTypeUnit="Unknown"; 
				}
				infoLabel.setText(nameTypeUnit+"  "+currentBoat.getIdMap()+" "+currentBoat.getName()+" - "+currentBoat.getCurrentSpeed()+" knots");
				sliderSpeed.setRange(-(int )currentBoat.getMaxSpeed(), (int )currentBoat.getMaxSpeed());
				sliderSpeed.value.set(currentBoat.getCurrentSpeed());
				weaponLabel.setText(currentBoat.getNbTorpedoes()+" Torpedoes "+currentBoat.getNbMissiles()+" Missiles");
				autonomousButton.setSelected(currentBoat.isAutonomous());
				fireAtWillButton.setSelected(currentBoat.isFireAtWill());
				ignoreFriendsButton.setSelected(currentBoat.isIgnoreFriends());
				followWPButton.setSelected(currentBoat.isFollowWP());
				loopWPButton.setSelected(currentBoat.isLoop());
				depthLabel.setText("Current Depth "+currentBoat.getDepth()+" Wanted 0");
				sliderDepth.value.set(0);
				shootTorpedoButton.setSelected(false);
				shootMissileButton.setSelected(false);
				
				sonarButton.setSelected(currentBoat.isSonarOn());
				radarButton.setSelected(currentBoat.isRadarOn());
			}
			if (!isBoat)
			{
				currentSub.setView(true);
				switch (currentSub.getType())
				{
					case Submarine.NUKE:
						nameTypeUnit="SSN"; 
						break;
					case Submarine.DIESEL:
						nameTypeUnit="Diesel"; 
						break;
					case Submarine.DIESEL_AIP:
						nameTypeUnit="AIP Diesel"; 
						break;
					case Submarine.NUKE_SSBN:
						nameTypeUnit="SSBN"; 
						break;
						default:
							nameTypeUnit="Unknown"; 
				}
				infoLabel.setText(nameTypeUnit+"  "+currentSub.getIdMap()+" "+currentSub.getName()+" - "+(int )currentSub.getCurrentSpeed()+" knots");
				sliderSpeed.setRange(-(int )currentSub.getMaxSpeed(), (int )currentSub.getMaxSpeed());
				sliderSpeed.value.set(currentSub.getCurrentSpeed());
				weaponLabel.setText(currentSub.getNbTorpedoes()+" Torpedoes "+currentSub.getNbMissiles()+" Missiles");
				autonomousButton.setSelected(currentSub.isAutonomous());
				fireAtWillButton.setSelected(currentSub.isFireAtWill());
				ignoreFriendsButton.setSelected(currentSub.isIgnoreFriends());
				followWPButton.setSelected(currentSub.isFollowWP());
				loopWPButton.setSelected(currentSub.isLoop());
				depthLabel.setText("Current Depth "+(int )currentSub.getDepth()+" Wanted "+(int )currentSub.getDepth());
				sliderDepth.setRange((int )currentSub.getMaxDepth(), -7);
				sliderDepth.value.set(currentSub.getDepth());
				shootTorpedoButton.setSelected(false);
				shootMissileButton.setSelected(false);
				
				sonarButton.setSelected(currentSub.isSonarOn());
				radarButton.setSelected(currentSub.isRadarOn());
			}
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
				if (isBoat)
				{
					currentBoat.getProgrammedWPs().unedit();
				}
				if (!isBoat)
				{
					currentSub.getProgrammedWPs().unedit();
				}
			}
			
			
			// Do something if needed
			if (isBoat)
			{
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
				else if (!currentBoat.isFollowWP())
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
				
				if ((stateEdition==TutorialMap2.ED_MOVE_WP)&& (!wpTorpedoButton.isSelected()) && (!wpMissileButton.isSelected()))
				{
					// If no WP selected, first find one
					if (moveWP)
					{
						if ((currentBoat.getProgrammedWPs() != null) && (mouseX < TutorialMap2.START_MENU))
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
			}
			if (!isBoat)
			{
				if (currentSub.hasWPBeenUpdated())
				{
					currentSub.drawWP();
				}
				stateHullLabel.setText("Integrity - Hull "+(int )currentSub.getHullState()+"%");
				stateNavLabel.setText("DataLink "+(int )currentSub.getDataLinkState()+"%");
				stateDataLabel.setText("Navigation "+(int )currentSub.getNavState()+"%");
				complementLabel.setText("Complement "+currentSub.getComplement());
				depthLabel.setText("Current Depth "+(int )currentSub.getDepth()+" Wanted "+(int )currentSub.getWantedDepth());
				double newDepth= sliderDepth.value.get();
				
				weaponLabel.setText(currentSub.getNbTorpedoes()+" Torpedoes "+currentSub.getNbMissiles()+"Missiles");
				if (Math.abs(newSpeed - currentSub.getCurrentSpeed()) > 0.5)
				{
					currentSub.setWantedSpeed(newSpeed);
					infoLabel.setText(nameTypeUnit+"  "+currentSub.getIdMap()+" "+currentSub.getName()+" - "+(int )currentSub.getCurrentSpeed()+" knots");
				}
				
				if (Math.abs(newDepth - currentSub.getDepth()) > 0.5)
				{
					currentSub.setWantedDepth(newDepth);
				}
				
				if (upKey)
				{
					currentSub.accelerate(1);
					sliderSpeed.value.set(currentSub.getWantedSpeed());
				}
				if (downKey)
				{
					currentSub.deccelerate(1);
					sliderSpeed.value.set(currentSub.getWantedSpeed());
				}
				if (rightKey)
				{
					currentSub.setFollowWP(false);
					followWPButton.setSelected(false);
					currentSub.turn(0.01);
				}
				else if (leftKey)
				{
					currentSub.setFollowWP(false);
					followWPButton.setSelected(false);
					currentSub.turn(-0.01);
				}
				else
				{
					if (currentSub != null)
						currentSub.turn(0.00);
				}
				
				currentSub.setAutonomous(autonomousButton.isSelected());
				currentSub.setFireAtWill(fireAtWillButton.isSelected());
				currentSub.setIgnoreFriends(ignoreFriendsButton.isSelected());
				currentSub.setFollowWP(followWPButton.isSelected());
				if (followWPButton.isClicked() && followWPButton.isSelected())
				{
					sliderSpeed.value.set(currentSub.getWantedSpeed());
				}
				if (loopWPButton.isSelected() != currentSub.isLoop())
				{
					currentSub.setLoop(loopWPButton.isSelected());
					currentSub.drawWP();
				}
				
				if ((stateEdition==TutorialMap2.ED_MOVE_WP) && (!wpTorpedoButton.isSelected()) && (!wpMissileButton.isSelected()))
				{
					// If no WP selected, first find one
					if (moveWP)
					{
						if ((currentSub.getProgrammedWPs() != null) && (mouseX < TutorialMap2.START_MENU))
						{
							// If one selected, move it!
							if (indexWPToMove < currentSub.getProgrammedWPs().size())
							{
								// If one selected, move it!
								currentSub.getProgrammedWPs().getWP(indexWPToMove).setXWP(mouseX);
								currentSub.getProgrammedWPs().getWP(indexWPToMove).setYWP(mouseY);
								currentSub.resetWP();
							}
							currentSub.drawWP();
						}
					}
				}
				
				if (sonarButton.isClicked())
					currentSub.pushSonarOnOff(sonarButton.isSelected());
				
				if (radarButton.isClicked())
					currentSub.pushRadarOnOff(radarButton.isSelected());
			}
			if ((stateEdition==TutorialMap2.ED_MOVE_WP) && wpTorpedoButton.isSelected())
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
			if ((stateEdition==TutorialMap2.ED_MOVE_WP) && wpMissileButton.isSelected())
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
					stateEdition=TutorialMap2.ED_ADD_WP;
					mapFree=false;
				}
				else
				{
					stateEdition=TutorialMap2.ED_NONE;
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
					stateEdition=TutorialMap2.ED_REMOVE_WP;
					mapFree=false;
				}
				else
				{
					stateEdition=TutorialMap2.ED_NONE;
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
					stateEdition=TutorialMap2.ED_MOVE_WP;
					mapFree=false;
					if (isBoat)
					{
						currentBoat.getProgrammedWPs().edit();
					}
					if (!isBoat)
					{
						currentSub.getProgrammedWPs().edit();
					}
				}
				else
				{
					stateEdition=TutorialMap2.ED_NONE;
					mapFree=true;
					if (isBoat)
					{
						currentBoat.getProgrammedWPs().unedit();
					}
					if (!isBoat)
					{
						currentSub.getProgrammedWPs().unedit();
					}
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
					stateEdition=TutorialMap2.ED_SHOOT_TORPEDO;
				}
				else
				{
					stateEdition=TutorialMap2.ED_NONE;
				}
				if (isBoat)
				{
					currentBoat.drawWP();
				}
				if (!isBoat)
				{
					currentSub.drawWP();
				}
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
					stateEdition=TutorialMap2.ED_NONE;
					mapFree=true;
					if (isBoat)
					{
						currentBoat.drawWP();
					}
					if (!isBoat)
					{
						currentSub.drawWP();
					}
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
					stateEdition=TutorialMap2.ED_SHOOT_MISSILE;
				}
				else
				{
					stateEdition=TutorialMap2.ED_NONE;
				}
				if (isBoat)
				{
					currentBoat.drawWP();
				}
				if (!isBoat)
				{
					currentSub.drawWP();
				}
				
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
					stateEdition=TutorialMap2.ED_NONE;
					mapFree=true;
					if (isBoat)
					{
						currentBoat.drawWP();
					}
					if (!isBoat)
					{
						currentSub.drawWP();
					}
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
					if (isBoat)
					{
						if (stateEdition==TutorialMap2.ED_ADD_WP)
						{
							if (currentBoat.getProgrammedWPs() != null)
							{
								if (currentBoat.getProgrammedWPs().size() < 30)
								{
									boolean existWP=false;
									Journey setOfWPS=currentBoat.getProgrammedWPs();
									for (int iWP=0;iWP < setOfWPS.size(); iWP++)
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
						if (stateEdition==TutorialMap2.ED_REMOVE_WP)
						{
							if (currentBoat.getProgrammedWPs() != null)
							{
								if (currentBoat.getProgrammedWPs().size() > 0)
								{
									Journey setOfWPS=currentBoat.getProgrammedWPs();
									for (int iWP=0;iWP < setOfWPS.size(); iWP++)
									{
										Waypoint tmpWP = setOfWPS.getWP(iWP);
										if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
										{
											setOfWPS.removeWP(iWP);
											currentBoat.resetWP();
											break;
										}
									}

									currentBoat.drawWP();
								}
							}
						}
						//if (stateEdition==TacticalMapPC.ED_MOVE_WP)
						if ((stateEdition!=TutorialMap2.ED_REMOVE_WP) && (stateEdition!=TutorialMap2.ED_ADD_WP))
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
										for (int iWP=0;iWP < setOfWPS.size(); iWP++)
										{
											Waypoint tmpWP = setOfWPS.getWP(iWP);
											if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
											{
												moveWP=true;
												indexWPToMove=iWP;
												stateEdition=TutorialMap2.ED_MOVE_WP;
												moveWPButton.setSelected(true);
												
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
						if (stateEdition==TutorialMap2.ED_SHOOT_MISSILE)
						{
							currentBoat.requestFire(salveMissileButton.isSelected(), wpFollowMissileButton.isSelected(), seekMButton.isSelected(), goToTargetMButton.isSelected(), ProgrammableUnit.FIRE_MISSILE, mouseX, mouseY,wpMissiles);
						}
						if (stateEdition==TutorialMap2.ED_SHOOT_TORPEDO)
						{
							currentBoat.requestFire(salveTorpedoButton.isSelected(), wpFollowTorpedoButton.isSelected(), seekTButton.isSelected(), goToTargetTButton.isSelected(), ProgrammableUnit.FIRE_TORPEDO, mouseX, mouseY,wpTorpedoes);
						}
					}
					if (!isBoat)
					{
						if (stateEdition==TutorialMap2.ED_ADD_WP)
						{
							if (currentSub.getProgrammedWPs() != null)
							{
								if (currentSub.getProgrammedWPs().size() < 30)
								{
									boolean existWP=false;
									Journey setOfWPS=currentSub.getProgrammedWPs();
									for (int iWP=0;iWP < setOfWPS.size(); iWP++)
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
										currentSub.getProgrammedWPs().addWP(new Waypoint(mouseX,mouseY,0));
										currentSub.drawWP();
									}
								}
							}
						}
						if (stateEdition==TutorialMap2.ED_REMOVE_WP)
						{
							if (currentSub.getProgrammedWPs() != null)
							{
								if (currentSub.getProgrammedWPs().size() > 0)
								{
									Journey setOfWPS=currentSub.getProgrammedWPs();
									for (int iWP=0;iWP < setOfWPS.size(); iWP++)
									{
										Waypoint tmpWP = setOfWPS.getWP(iWP);
										if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
										{
											setOfWPS.removeWP(iWP);
											currentSub.resetWP();
											break;
										}
									}

									currentSub.drawWP();
								}
							}
						}
						//if (stateEdition==TacticalMapPC.ED_MOVE_WP)
						if ((stateEdition!=TutorialMap2.ED_REMOVE_WP) && (stateEdition!=TutorialMap2.ED_ADD_WP))
						{
							mapFree=true;
							// If no WP selected, first find one
							if (!moveWP)
							{
								if (currentSub.getProgrammedWPs() != null)
								{
									if (currentSub.getProgrammedWPs().size() > 0)
									{
										Journey setOfWPS=currentSub.getProgrammedWPs();
										for (int iWP=0;iWP < setOfWPS.size(); iWP++)
										{
											Waypoint tmpWP = setOfWPS.getWP(iWP);
											if ((Math.abs((tmpWP.getXWP() - mouseX)) < 10) && (Math.abs((tmpWP.getYWP()  - mouseY)) < 10))
											{
												moveWP=true;
												indexWPToMove=iWP;
												stateEdition=TutorialMap2.ED_MOVE_WP;
												moveWPButton.setSelected(true);
												currentSub.getProgrammedWPs().edit();
												break;
											}
										}
										if (moveWP)
											mapFree=false;
									}
								}
							}
							else
							{
								// If one already selected, stop to edit it
								moveWP=false;
								currentSub.resetWP();
							}
						}
						if (stateEdition==TutorialMap2.ED_SHOOT_MISSILE)
						{
							currentSub.requestFire(salveMissileButton.isSelected(), wpFollowMissileButton.isSelected(), seekMButton.isSelected(), goToTargetMButton.isSelected(), ProgrammableUnit.FIRE_MISSILE, mouseX, mouseY,wpMissiles);
						}
						if (stateEdition==TutorialMap2.ED_SHOOT_TORPEDO)
						{
							currentSub.requestFire(salveTorpedoButton.isSelected(), wpFollowTorpedoButton.isSelected(), seekTButton.isSelected(), goToTargetTButton.isSelected(), ProgrammableUnit.FIRE_TORPEDO, mouseX, mouseY,wpTorpedoes);
							System.out.println("Fire requested!!!");
						}
					}
				}
				if (wpTorpedoButton.isSelected())
				{
					if (stateEdition==TutorialMap2.ED_ADD_WP)
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
					if (stateEdition==TutorialMap2.ED_REMOVE_WP)
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
					if ((stateEdition!=TutorialMap2.ED_REMOVE_WP) && (stateEdition!=TutorialMap2.ED_ADD_WP))
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
										stateEdition=TutorialMap2.ED_MOVE_WP;
										moveWPButton.setSelected(true);
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
					if (stateEdition==TutorialMap2.ED_ADD_WP)
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
					if (stateEdition==TutorialMap2.ED_REMOVE_WP)
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
					if ((stateEdition!=TutorialMap2.ED_REMOVE_WP) && (stateEdition!=TutorialMap2.ED_ADD_WP))
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
										stateEdition=TutorialMap2.ED_MOVE_WP;
										moveWPButton.setSelected(true);
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
			
			if (isBoat && (currentBoat != null))
			{
				currentBoat.setView(false);
			}
			if (!isBoat && (currentSub != null))
			{
				currentSub.setView(false);
			}
		}
		if (rightMousePressed)
		{
			myWPDrawer.hideMe();
		}
		// Go through the units and update them!
		if (ourSubs != null)
		{
			
			for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
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
						stateEdition=TutorialMap2.ED_NONE;
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
						isBoat = true;
						// This boat is selected
						ourBoats.get(iBoat).drawWP();
						unitToSelect=true;
						stateEdition=TutorialMap2.ED_NONE;
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
				
				if (nameToShow)
				{
					alliesSubs.get(iSub).showLabel();
				}
				if (nameToHide)
				{
					alliesSubs.get(iSub).hideLabel();
				}
				
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
						stateEdition=TutorialMap2.ED_NONE;
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
						isBoat = true;
						// This boat is selected
						alliesBoats.get(iBoat).drawWP();
						unitToSelect=true;
						stateEdition=TutorialMap2.ED_NONE;
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
			for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
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
			for (int iBoat = 0; iBoat < alliesBases.size() ; iBoat++)
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
			for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
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
//		if (Input.isDown(Input.KEY_E))
//			return DrawMap.myName;
		return null;
	}
	
	public void showStrategicMap()
	{
		DrawBackground.drawBackMap();
	}
	
	public void hideStrategicMap()
	{
		DrawBackground.removeMap();
	}
	
	public void goToSequence(int sequenceNb)
	{
		switch (sequenceNb)
		{
			case 0:sequenceStart();
			//ourScene.remove(backButton);
			break;
			
			case 1:sequence1();
			//ourScene.add(backButton);
			break;

			case 2:sequence2();
			break;

			case 3:sequence3();
			break;

			case 4:sequence4();
			break;

			case 5:sequence5();
			break;

			case 6:sequence6();
			break;

			case 7:sequence7();
			break;

			case 8:sequence8();
			break;

			case 9:sequence9();
			break;

			case 10:sequence10();
			break;

			case 11:sequence11();
			break;

			case 12:sequence12();
			break;

			case 13:sequence13();
			break;

			case 14:sequence14();
			break;

			case 15:sequence15();
			break;

			case 16:sequence16();
			break;

			case 17:sequence17();
			break;

			case 18:sequence18();
			break;

			case 19:sequence19();
			break;

			case 20:sequence20();
			break;

			case 21:sequence21();
			break;
			
			case 22:sequence22();
			break;
			
			case 23:sequence23();
			break;
			
			case 24:sequence23_2();
			break;
			
			case 25:sequence23_3();
			break;
			
			case 26:sequence24();
			break;
			
			case 27:sequence24_2();
			break;
			
			case 28:sequence24_3();
			break;
			
			case 29:sequence25();
			break;
			
			case 30:sequence25_2();
			break;
			
			case 31:sequence26();
			break;
			
			case 32:sequence26_2();
			break;
			
			case 33:sequence27();
			break;
			
			case 34:sequence28();
			break;
			
			case 35:sequence29();
			break;
			
			case 36:sequence30();
			break;
			
			case 37:sequence31();
			break;
			
			case 38:sequence32();
			break;
			
			case 39:sequence33();
			break;
		}
	}
	public void sequenceStart()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The tactical map is the place you actually fight\nIt offers a wide panels of functionalities.\nTo quit and return to the main menu,\ndouble-click on MENU in the bottom-right corner", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		//tutText.setSize(tutText.width.getAsInt()*1.2, tutText.height.getAsInt()*1.2);
		ourScene.add(tutText);
		if (arrow == null)
		{
			arrow = new SpritePC(gfxSprites.getImageArrow());
			arrow2 = new SpritePC(gfxSprites.getImageArrow());
		}
	}
	
	public void sequence1()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Here again, you cannot generally see the enemy.\nBut here, you can detect them!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}

	public void sequence2()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Let place a boat.\nYou have several types of boats.\nLet start with the cruiser, a strong unit,\nwith a big quantity of missiles and torpedoes.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence3()
	{
		removeUnits();
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
		
		if (isBoat && (currentBoat != null))
		{
			currentBoat.setView(false);
		}
		
		this.createCruiserOur(350, 400);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("It is furnished complete, with radar, sonar, and weapons.\nAnd it will use it, by itself!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence4()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("But it needs to see something\nin order to do something.\nAnd you order it what to do.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence5()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("To see what it sees, just select it by clicking on it. Let try now.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence6()
	{
		if (!unitSelected)
		{
			unitToSelect = true;
			currentBoat = this.ourBoats.get(0);
		}
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The turning lines represent the radar\nIt actually sees a lot farer, depending on the radar.\nThe dots represent the sonar.\nIt works approximatively in the defined area.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence7()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Let add an enemy to see what happens", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence8()
	{
		removeEnemies();
		this.createCorvetteEnemies(100, 400);
		this.getEnemiesBoats().get(0).setFireAtWill(false);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("It is a corvette, the smallest unit.\nBut it is not supposed to be visible.\nLet correct that.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence9()
	{
		ourScene.remove(tutText);
		if (this.getEnemiesBoats().get(0).isDead())
		{
			tutText = Label.createMultilineLabel("Oups, it is already dead. \nI created another one, so we can see. \nThis one is not visible yet. The cruiser needs to find it.\nIf the cruiser detects an enemy, it will shoot at it automatically,\nif Attack at will is selected", 350, 50);
			this.createCorvetteEnemies(50, 500);
			this.getEnemiesBoats().get(1).hideMe();
			this.getEnemiesBoats().get(1).setFireAtWill(false);
		}
		else
		{
			this.getEnemiesBoats().get(0).hideMe();
			tutText = Label.createMultilineLabel("It is not visible now. The cruiser needs to find it.\nIf the cruiser detects an enemy,\nit will shoot at it automatically,\nif Attack at will is selected", 350, 50);
		}
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence10()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The cruiser sees some rectangles: the found units.\nThe color around the detection rectangle\nchanges depending on the confidence: grey is unknown,\ndotted red: surely an enemy,\nsolid red: an enemy,\nblue: friendly unit.", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence11()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Some elements are always grey, they are the signals.\nA signal is currently only created by an active sonar.\nYou can see it from everywhere, but you do not know\nto which unit it bellows...", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence12()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("You can also have some land.\n Let draw an island", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
		wantDraw=false;
		myDrawer.start();
		myDrawer.resetDraw();
	}
	public void sequence13()
	{
		myDrawer.start();
		myDrawer.resetDraw();
		if (!this.getEnemiesBoats().get(0).isDead())
		{
			this.getEnemiesBoats().get(0).setPosX(10);
		}
		if (!this.getOurBoats().get(0).isDead())
		{
			this.getOurBoats().get(0).setPosX(600);
		}
		wantDraw=true;
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The land blocks the sea units and the torpedoes.\nThe coasts forces the submarines to go at the surface.\nAnd on the land, we can have a base.\n", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
		if (ourBases != null)
		{
			for (int iBoat = 0; iBoat < ourBases.size() ; iBoat++)
			{
				ourBases.get(iBoat).resetAirplaneInFlight();
				ourBases.get(iBoat).removeMe();
			}
			ourBases.clear();
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
	}
	public void sequence14()
	{
		createMainBaseOur(350,400);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The bases are the primary targets,\nboth for you and your enemy.\nIf you lose all your bases, you lose.\nIf you destroy all the enemy's bases, you win...\nThe bases are visible by all units everywhere.\nThey are considered as known.\nYou cannot control the base, but you can ask\nits planes to fire.", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence15()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("So, what can you do with these units?", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence16()
	{
		ourScene.remove(tutText);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(549.0, 183.0, 0);
		arrow.setRotation(0.4);
		arrow.setSize(1.7999999999999998);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		SpritePC arrow2 = new SpritePC(gfxSprites.getImageArrow());
		arrow2.setPos(295.0 , 116.0, 0);
		arrow2.setRotation(0.4);
		arrow2.setSize(0.6);
		tutText = Label.createMultilineLabel("They are partly autonomous. They sometimes\nfollow a defined way, and they attack\nthe enemies they see.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence17()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(549.0, 183.0, 0);
		arrow.setRotation(0.4);
		arrow.setSize(1.7999999999999998);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("If you unselect this button, they will go straight...\nYou can also use the keyboard's arrows\nto control the boat. If not already done\nit will unselect this button.\nThey will not move by their own,\nbut continue to shoot on sight...", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence18()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(559.0, 190.0, 0);
		arrow.setRotation(0.6000000000000001);
		arrow.setSize(1.7999999999999998);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Unless you unselect this other button. It is especially\nuseful if you know where to shoot,\nor where to not shoot... So you can save ammo or\na friendly unit.", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence19()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(559.0, 190.0, 0);
		arrow.setRotation(0.6000000000000001);
		arrow.setSize(1.7999999999999998);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The units will attack only the identified enemies.\nIt is of course very useful, but, it also\nmeans that they wait until they are sure.\nThis buttons tell them to attack all contacts,\nknowns or unknowns.", 350, 70);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence20()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("You can control the unit even when they are autonomous.\nThey will eventually take their own decisions,\nbut follow your orders. You can tell them where to go\nby several methods. You can control them directly,\nwith the arrows' keys.", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence21()
	{
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(544.0, 241.0, 0);
		arrow.setRotation(0.6000000000000001);
		arrow.setSize(1.9999999999999998);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("You can also change their path, using WayPoints.\nYou have waypoints for the boats, the submarines,\nthe missiles, and the torpedoes. The airplanes also\nfollow a path, but you cannot change it.", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence22()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(544.0, 345.0, 0);
		arrow.setRotation(1.6653345369377348E-16);
		arrow.setSize(1.5999999999999999);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		arrow2.setPos(909.0, 229.0, 0);
		arrow2.setRotation(2.0);
		arrow2.setSize(1.2);
		idArrow2 = RenderingManager.getInstance().addDrawableEntity(arrow2,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("These two buttons tell the selected unit to follow\nthe wp or not, and to considere the wps as a loop\nor not, if so, they will go back to the first\nwaypoint when they arrived at the last. Otherwise,\nthey will \"consume\" the waypoints.", 355, 75);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence23()
	{
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(461.0, 373.0, 0);
		arrow.setRotation(6.283185307179586);
		arrow.setSize(2.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The three buttons on this line are common to the \nselected unit, the missiles and the torpedoes.\nThe first one allows to add a waypoint, just click\nwhere you want to add the new one. If a path already\nexists, the new waypoint will be added at the end.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence23_2()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(637.0, 372.0, 0);
		arrow.setRotation(5.551115123125783E-17);
		arrow.setSize(1.5999999999999999);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("For the weapons, there is no path by default, and\nthere is only one path for all missiles and one for\nall torpedoes. The \"-\" button allows to remove a\nwaypoint, just click on the one you want to delete.", 350, 50);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence23_3()
	{
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(932.0, 253.0, 0);
		arrow.setRotation(1.7999999999999998);
		arrow.setSize(1.2);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The last button is also the most useful. It allows\nto move a waypoint. But you can also, if the add\nor remove buttons are not selected, directly click\non one waypoint, then move it. If you move the\nfirst waypoint of the path, or the next waypoint in\nthe loop, the unit will follow it as well as it can.\nIt is another way to directly control an unit,\nusing only the mouse.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence24()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(496.0, 272.0, 0);
		arrow.setRotation(0.6000000000000001);
		arrow.setSize(2.5999999999999996);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		arrow2.setPos(471.0, 334.0, 0);
		arrow2.setRotation(0.6000000000000001);
		arrow2.setSize(3.0);
		idArrow2 = RenderingManager.getInstance().addDrawableEntity(arrow2,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("If you want the selected unit to open fire, you can\nuse the \"Fire torpedo\" or the \"Fire missile\" buttons.\nThe carriers does not have torpedoes, but will\ncommand their planes to fire them, and the\nsubmarines cannot fire missiles if they are too deep,\nthen the missile controls will be surrounded by red.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence24_2()
	{
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(984.0, 428.0, 0);
		arrow.setRotation(3.400000000000001);
		arrow.setSize(0.9999999999999996);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);

		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("By default you fire only one torpedo or one missile.\nThe unit will shoot wherever you click next.\nIf you select \"Salve\", they will fire as many\ntorpedoes or missiles as they can simultaneously.", 350, 80);
        //                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence24_3()
	{
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(486.0, 290.0, 0);
		arrow.setRotation(0.6);
		arrow.setSize(2.8);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("If follow WP is selected, they will follow a path,\nif you create one before, using the Edit WP button,\nand the common WP edition buttons above.\nIf \"Seek\" is selected, they will follow the path, then\nseek for an unit. Otherwise, they will explode at\nthe last point.", 350, 80);
        //                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |		
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence25()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(459.0, 466.0, 0);
		arrow.setRotation(6.283185307179586);
		arrow.setSize(2.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("But most of the time, you will only need these two\noptions: Seek and Go to target. When they are both\nselected, the weapon will go to the point where\nyou clicked, without exploding at the contact of an\nunit, then seek for an unit after this point.\nA torpedo will still explode if it touch the ground,\nand a missile can be intercepted or be caught in\nan explosion. ", 350, 80);
        //                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence25_2()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(459.0, 466.0, 0);
		arrow.setRotation(6.283185307179586);
		arrow.setSize(2.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("When only \"Seek\" is selected, they only follow\nthe direction of the position you clicked and they\nwill seek for an unit as soon as launched. If only \n\"Go to target\" is selected, they will go to\nthe designated position and explode there. It can be\nuseful to cover a surface.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence26()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(464.0, 267.0, 0);
		arrow.setRotation(0.19999999999999996);
		arrow.setSize(2.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The missile will attack only the enemies' units\nunless \"Attack all units\" is selected.\nThe torpedoes will attack everyone one sight,\nenemy, ally or sunk boats and submarines. ", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence26_2()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(464.0, 267.0, 0);
		arrow.setRotation(0.19999999999999996);
		arrow.setSize(2.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("If a base is present, or if you have one carrier\nselected, asking for fire will also command the\nplanes to do so. With any allied unit selected, you\ncontrol all the planes of the present bases, or,\nonly with a selected carrier, you control its planes.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence27()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(515.0, 595.0, 0);
		arrow.setRotation(1.6653345369377348E-16);
		arrow.setSize(1.9999999999999993);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Almost all units (all for now) have a sonar and a radar.\nThe carriers have also airborne radars on\nspecial planes, allowing to see surfaces units from very far.\nUsing these buttons, you can activate or deactivate\nthe radar and the sonar of the selected unit.\nThe sonar works also if inactive, but closer,\nand will not give your position away so.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence28()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(570.0, 186.0, 0);
		arrow.setRotation(6.283185307179586);
		arrow.setSize(1.3999999999999995);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("For both the boats and the submarines, \nyou can choose their speed with this slider.\nThe middle is 0, to the right to advance,\nto the left to go reverse.", 350, 70);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence29()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(519.0, 659.0, 0);
		arrow.setRotation(0.20000000000000007);
		arrow.setSize(1.9999999999999993);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("And for the submarines only, you can select the\nwanted depth, using this slider. The more on the\nleft, the deeper. Being deep is good, it allows to be\ninvisible to radars, but then you cannot fire\nmissiles and use your radar. If the submarine is\nautonomous and see a surface enemy to shoot with\nmissiles, it will go to the right depth, fires, then\ndives back when out of missiles or if there are no more\nvisible targets.", 350, 90);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence30()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The units has some inherent limits. They need some\ntimes between two salves, cannot stop immediately,\nneed some speed to turn...", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence31()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("You will also remark that two units cannot collide.\nIt is so because you are actually looking at a map,\nnot at the actual event. You are in the command unit,\nand you barely give orders...", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}

	public void sequence32()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(535.0, 721.0, 0);
		arrow.setRotation(0.20000000000000018);
		arrow.setSize(2.3999999999999995);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("When you want to quit the level, just double-click\non this button. If you are in the quick-game mode,\nit will definitively quit the level. In the\nconquest mode, it will send you to the map.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence33()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The tutorial is now over. You can click NEXT to go\nback to the main menu, and enjoy the game. If you\nhave any problem with the game, do not hesitate\nto go to the forum, where you should find all the\nhelp you need.", 350, 80);
		//                                    |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |                                                  |
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	private void createSubOur(int x,int y)
	{

		Submarine oneSub = new Submarine(this);
		oneSub.setType(Submarine.NUKE);
		oneSub.setTypeFaction(FUnit.SUB_OUR);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		GameKeeper.getInstance().addComplementOur(100);
		oneSub.setTonnage(15000);
		oneSub.setCost(4000000000L);

		//GameKeeper.getInstance().addCostOur(4000000000L);
		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		
		oneSub.setName(PoolOfNames.namesOurNukes[0]);
		
		oneSub.setFireAtWill(true);
		oneSub.setNbMissiles(20);
		oneSub.setNbTorpedoes(50);
		oneSub.setDepth(-50);
		oneSub.setWantedDepth(-50);
		oneSub.setResistance(6);
		// And add the absolute coordinates.
		oneSub.setPosX(x);
		oneSub.setPosY(y);

		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneSub);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		if (Math.random() > 0.6)
			ourFrontSonar.activate();
		else
			ourFrontSonar.desactivate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneSub);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar.setPosAttach(0, 0, 0);
		ourRadar.setPower(5);
		ourRadar.setSpeedRot(40);
		ourRadar.desactivate();
		ourRadar.setDebugView(false);

		oneSub.getSensors().add(ourFrontSonar);
		oneSub.addAttachedObject(ourFrontSonar);
		oneSub.getSensors().add(ourRadar);
		oneSub.addAttachedObject(ourRadar);
		oneSub.createGfx( oneSub.getPosX(), oneSub.getPosY(), 0, 0, 0);
		oneSub.setLoop(true);
		oneSub.setView(false);
		
		this.addSub(oneSub);	
	}
	
	private void createCarrierOur(int x,int y)
	{
		// Add a carrier here !!!
		Boat oneCarrier = new Boat(this);
		oneCarrier.setType(Boat.CARRIER);
		oneCarrier.setTypeFaction(FUnit.BOAT_OUR);
		oneCarrier.setComplement(5000);
		oneCarrier.setComplementNorm(5000);
		GameKeeper.getInstance().addComplementOur(5000);
		oneCarrier.setTonnage(105000);
		oneCarrier.setCost(5000000000L);

		//GameKeeper.getInstance().addCostOur(5000000000L);
		oneCarrier.setMaxSpeed(35);
		oneCarrier.setStandardSpeed(20);
		oneCarrier.setNbAwacs(2);
		oneCarrier.setNbFighters(120);
		oneCarrier.setNbTankers(2);
		if (GameKeeper.getInstance().nbCarrierTotalOur < PoolOfNames.namesOurCarriers.length)
		{
			oneCarrier.setName(PoolOfNames.namesOurCarriers[GameKeeper.getInstance().nbCarrierTotalOur]);
		}
		else
		{
			oneCarrier.setName("USS Bidule");
		}
		oneCarrier.setFireAtWill(true);
		oneCarrier.setNbMissiles(100);
		oneCarrier.setNbTorpedoes(0);
		oneCarrier.setResistance(50);
		// And add the absolute coordinates.
		oneCarrier.setPosX(x);
		oneCarrier.setPosY(y);

		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCarrier);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCarrier);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCarrier.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCarrier.getSensors().add(ourFrontSonar);
		oneCarrier.addAttachedObject(ourFrontSonar);
		oneCarrier.getSensors().add(ourRadar);
		oneCarrier.addAttachedObject(ourRadar);
		oneCarrier.createGfx( oneCarrier.getPosX(), oneCarrier.getPosY(), 0, 0, 0);
		oneCarrier.setView(false);
		
		this.addBoat(oneCarrier);
	}

	private void createCruiserEnemies(int x,int y)
	{
		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementEnemies(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		//GameKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(20);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}

	int iCruiserOur = 0;
	
	private void createCruiserOur(int x,int y)
	{
		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementOur(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		if (iCruiserOur < PoolOfNames.namesOurCruiser.length)
		{
			oneCruiser.setName(PoolOfNames.namesOurCruiser[iCruiserOur++]);
		}
		else
		{
			oneCruiser.setName("USS Bidule");
		}
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(20);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}

	
	private void createDestroyerEnemies(int x,int y)
	{
		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(100);
		oneCruiser.setComplementNorm(100);
		GameKeeper.getInstance().addComplementEnemies(100);
		oneCruiser.setTonnage(22000);
		oneCruiser.setCost(40000000L);

		//GameKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(350);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}

	int iDestroyerOur = 0;
	
	private void createDestroyerOur(int x,int y)
	{

		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.DESTROYER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementOur(500);
		oneCruiser.setTonnage(20000);
		oneCruiser.setCost(100000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		if (iDestroyerOur < PoolOfNames.namesOurDestroyer.length)
		{
			oneCruiser.setName(PoolOfNames.namesOurDestroyer[iDestroyerOur++]);
		}
		else
		{
			oneCruiser.setName("USS Bidule");
		}
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(440);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(12);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}

	
	private void createFrigateEnemies(int x,int y)
	{
		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(50);
		oneCruiser.setComplementNorm(50);
		GameKeeper.getInstance().addComplementEnemies(50);
		oneCruiser.setTonnage(12000);
		oneCruiser.setCost(20000000L);

		//GameKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(50);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(7);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}

	int iFrigateOur = 0;
	
	private void createFrigateOur(int x,int y)
	{

		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(60);
		oneCruiser.setComplementNorm(60);
		GameKeeper.getInstance().addComplementOur(60);
		oneCruiser.setTonnage(13000);
		oneCruiser.setCost(10000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		if (iFrigateOur < PoolOfNames.namesOurFrigate.length)
		{
			oneCruiser.setName(PoolOfNames.namesOurFrigate[iFrigateOur++]);
		}
		else
		{
			oneCruiser.setName("USS Bidule");
		}
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(40);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(8);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}
	
	private void createCorvetteEnemies(int x,int y)
	{
		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.CORVETTE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(30);
		oneCruiser.setComplementNorm(30);
		GameKeeper.getInstance().addComplementEnemies(30);
		oneCruiser.setTonnage(6000);
		oneCruiser.setCost(10000000L);

		//GameKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(50);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(2);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}
	
	int iCorvetteOur=0;
	
	private void createCorvetteOur(int x,int y)
	{
		Boat oneCruiser = new Boat(this);
		oneCruiser.setType(Boat.CORVETTE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(30);
		oneCruiser.setComplementNorm(30);
		GameKeeper.getInstance().addComplementOur(30);
		oneCruiser.setTonnage(6000);
		oneCruiser.setCost(10000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		if (iCorvetteOur < PoolOfNames.namesOurCorvette.length)
		{
			oneCruiser.setName(PoolOfNames.namesOurCorvette[iCorvetteOur++]);
		}
		else
		{
			oneCruiser.setName("USS Bidule");
		}
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(50);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(2);
		// And add the absolute coordinates.
		oneCruiser.setPosX(x);
		oneCruiser.setPosY(y);
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneCruiser);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		ourFrontSonar.activate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneCruiser);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		
		if (oneCruiser.getType() == Boat.CARRIER)
		{
			ourRadar.setPosAttach(2, -7, 0);
		}
		else
		{
			ourRadar.setPosAttach(8, 0, 0);
		}
		
		ourRadar.setPower(10);
		ourRadar.setSpeedRot(40);
		ourRadar.activate();
		ourRadar.setDebugView(false);

		oneCruiser.getSensors().add(ourFrontSonar);
		oneCruiser.addAttachedObject(ourFrontSonar);
		oneCruiser.getSensors().add(ourRadar);
		oneCruiser.addAttachedObject(ourRadar);
		oneCruiser.createGfx( oneCruiser.getPosX(), oneCruiser.getPosY(), 0, 0, 0);
		oneCruiser.setView(false);

		this.addBoat(oneCruiser);
	}
	
	private void createSubEnemies(int type,int x,int y)
	{
		Submarine oneSub = new Submarine(this);
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_ENEMY);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		GameKeeper.getInstance().addComplementEnemies(100);
		oneSub.setTonnage(10000);
		oneSub.setCost(3400000000L);

		//GameKeeper.getInstance().addCostEnemies(3400000000L);
		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		oneSub.setName("USS Bidule");
		oneSub.setFireAtWill(true);
		oneSub.setNbMissiles(20);
		oneSub.setNbTorpedoes(40);
		oneSub.setDepth(-50);
		oneSub.setWantedDepth(-50);
		oneSub.setResistance(5);
		// And add the absolute coordinates.
		oneSub.setPosX(x);
		oneSub.setPosY(y);
		
		Sonar ourFrontSonar;
		ourFrontSonar= new Sonar(this,oneSub);
		ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
		ourFrontSonar.setPosAttach(40, 0, 0);
		if (Math.random() > 0.6)
			ourFrontSonar.activate();
		else
			ourFrontSonar.desactivate();

		Radar ourRadar;
		ourRadar= new Radar(this,oneSub);
		ourRadar.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar.setPosAttach(0, 0, 0);
		ourRadar.setPower(5);
		ourRadar.setSpeedRot(40);
		ourRadar.desactivate();
		ourRadar.setDebugView(false);

		oneSub.getSensors().add(ourFrontSonar);
		oneSub.addAttachedObject(ourFrontSonar);
		oneSub.getSensors().add(ourRadar);
		oneSub.addAttachedObject(ourRadar);
		oneSub.createGfx( oneSub.getPosX(), oneSub.getPosY(), 0, 0, 0);
		oneSub.setLoop(true);
		oneSub.setView(false);
		oneSub.hideMe();

		
		this.addSub(oneSub);
	}

	private void createMainBaseOur(int x,int y)
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base(this);
		oneNewSmallBase.setType(Base.MAINBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_OUR);
		oneNewSmallBase.setComplement(10000);
		oneNewSmallBase.setComplementNorm(10000);
		GameKeeper.getInstance().addComplementOur(10000);
		oneNewSmallBase.setCost(324000000);
		//GameKeeper.getInstance().addCostOur(324000000);
		oneNewSmallBase.setNbAwacs(10);
		oneNewSmallBase.setNbFighters(1600);
		oneNewSmallBase.setNbTankers(16);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		oneNewSmallBase.setNbMissiles(16000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosX(x);
		oneNewSmallBase.setPosY(y);
		oneNewSmallBase.createGfx( x, y, 0, 0, 0);

		Radar ourRadar3;
		ourRadar3= new Radar(this,oneNewSmallBase);
		ourRadar3.createGfx(0, 0, 0, 0, 1, 1);
		ourRadar3.setPosAttach(-28, -16, 0);
		if ((oneNewSmallBase.getType() == Base.BIGBASETWO) || (oneNewSmallBase.getType() == Base.SMALLBASETWO) || (oneNewSmallBase.getType() == Base.MAINBASETWO))
			ourRadar3.setPosAttach(-41, -11, 0);
		ourRadar3.setPower(50);
		ourRadar3.setSpeedRot(20);
		ourRadar3.activate();
		ourRadar3.setDebugView(false);

		oneNewSmallBase.getSensors().add(ourRadar3);
		oneNewSmallBase.addAttachedObject(ourRadar3);
	
		this.addBase(oneNewSmallBase);
		
		GameKeeper.getInstance().nbBasesTotalOur++;
		//nbOurMainBases--;
	}
	
	public void subDead(int typeFaction)
	{
		super.subDead(typeFaction);
		
		if ((nbEnemiesTotal == 0) || (nbTotalGood == 0))
			endLevelButton.enabled.set(true);
	}

	public void boatDead(int typeFaction)
	{
		super.boatDead(typeFaction);
		
		if ((nbEnemiesTotal == 0) || (nbTotalGood == 0))
			endLevelButton.enabled.set(true);
	}

	public void baseDead(int typeFaction)
	{
			super.baseDead(typeFaction);
			if ((nbEnemiesTotal == 0) || (nbTotalGood == 0))
				endLevelButton.enabled.set(true);
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
		currentSub=null; // If it is a sub
		currentBoat=null; // If it is a boat
		isBoat=true;

		unitSelected=false;
		unitToSelect=false;
		
		endAsked=false;
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
		
		depthLabel= new Label("Depth wanted", 800, 690);
		depthLabel.setAnchor(Sprite.CENTER);
		ourScene.add(depthLabel);
		sliderDepth = new Slider("SpeedBarre.png", "SpeedCursor.png", 700, 700);
		ourScene.add(sliderDepth);
		
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
		
		endLevelButton= Button.createLabeledToggleButton("End level", 760, 750);
		
		ourScene.add(endLevelButton);
		
		menuButton= Button.createLabeledToggleButton("Menu", 870, 750);
		//menuButton.setAnchor(Sprite.CENTER);
		
		ourScene.add(menuButton);
		
		nextButton=Button.createLabeledToggleButton("Next", 620, 70);
		nextButton.setAnchor(Sprite.CENTER);
		ourScene.add(nextButton);
		backButton=Button.createLabeledToggleButton("Back", 80, 70);
		backButton.setAnchor(Sprite.CENTER);
		ourScene.add(backButton);
		
		//endLevelButton.enabled.set(false);
		
		ourKD = new KnownDatas(this);
		
		// Load the good level
		
		currentLevel = LevelKeeper.getInstance().getNextLevelWanted();
		
		// TODO Auto-generated method stub
		setLevel(currentLevel);
		lastTime=new java.util.Date().getTime();
		myWPDrawer = new WPDrawer();
		
		myDrawer = new ToolDrawMap();
		myDrawer.start();
		
		arrow = null;
		arrow2 = null;
		
		sequenceStart();
	}
}
