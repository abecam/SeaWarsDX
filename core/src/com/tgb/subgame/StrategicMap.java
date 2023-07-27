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
import java.util.Date;

import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.*;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.ParticleManager;
import com.tgb.subengine.gamesystems.IGamePart;
import com.tgb.subengine.gameentities.*;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subengine.gfxentities.RectanglePC;
import com.tgb.subengine.gfxentities.Text;
import com.tgb.subgame.levels.*;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.unitspc.sensors.*;

/**
 * The strategic map, the global one
 * @author Alain Becam
 *
 */
public class StrategicMap implements IGamePart
{
	LevelLoaderPC myLevelLoader;
	
	public final static int START_MENU=700; // Where the menu start (so where the map shouldn't be) 
	
	ArrayList<Sensor> globalSensors;
	
	ArrayList<Signal> currentSignals;
	
	// If we have already seen the "won map". Allow to continue to play after it. 
	boolean wonMapDone;
	
	// Units infos
	int nbOurSubs=0;
	int nbOurBoats=0;
	int nbAlliesSubs=0;
	int nbAlliesBoats=0;
	int nbTotalGood=0;
	
	int nbEnemiesSubs=0;
	int nbEnemiesBoats=0;
	int nbEnemiesTotal=0;
	
	long nbSignal = 0;
	
	Missile tmpMissile;
	Torpedo tmpTorpedo;
	
	int currentLevel=0;
	
	int rectSelSize;
	
	long lastTime;
	long tmpTime;
	long timeInterval;
	double timeUsed; // timeInterval (in ms)/ 1000 (sec)
	
	boolean upKey,downKey,leftKey,rightKey=false;
	long timeSinceClick=0;
	
	static final String myName = "StrategicMap";
	
	int idTorpedo = 0;
	int idMissile = 0;
	
	int idBoat = 0;
	
	WPDrawer myWPDrawer;
	
	ProgrammableUnit currentUnit=null; // The selected unit.
//	Submarine currentSub=null; // If it is a sub
//	ProgrammableUnit currentBoat=null; // If it is a boat
	
	ArrayList<ProgrammableUnit> selectedUnits=null; // List of selectedUnits
	
	int xSelLeft=0,xSelRight=0,ySelUp=0,ySelDown=0;
	boolean isEdited=false; // Have we started to draw the selection rectangle ?
	
	int xTarget,yTarget; // Current target to assign to a group of units.
	
	boolean isBoat=true;

	boolean unitSelected=false;
	boolean unitToSelect=false;
	
	String nameTypeUnit;
	
	Scene2D ourScene;
	
//	ImageSprite strategicMap;
	FlatSpritePC background;
	long idBackground;
	
	RectanglePC rectangleSel;
	long idRectSel;
	
	Text textScreen;
	long idTextScreen;
	
	Label infoLabel;
	
	Button fleetStatutButton;
	Button scoreButton;
//	Button selectButton;
	Button zoomButton;
	
	Button playAsSubButton;
	Button playAsSurfaceButton;
	
	// If the rectangle only contain submarine, switch to that, otherwise to boat (might be a corvette there)
	Button autoSwitchButton;
	boolean autoSwitch = true;
	
	Button addWPButton;
//	Button removeWPButton;
	Button moveWPButton;
	
	Label scoreLabel;
	Label costLabel;
	Label miaLabel;
	Label carrierAliveLabel;
	Label otherBoatsAliveLabel;
	Label subsAliveLabel;
	Label basesAliveLabel;
	
	Label labelEne;
	Label costLabelEne;
	Label miaLabelEne;
	Label carrierAliveLabelEne;
	Label otherBoatsAliveLabelEne;
	Label subsAliveLabelEne;
	Label basesAliveLabelEne;
	Label percentageLossLabel;
	
	Button endLevelButton;
	
	final static int ED_NONE=0;
	final static int ED_ADD_WP=1;
	final static int ED_REMOVE_WP=2;
	final static int ED_MOVE_WP=3;
	
	final static int ED_SHOOT_TORPEDO=10;
	final static int ED_SHOOT_MISSILE=11;
	
	int stateEdition=ED_NONE;

	
	boolean mapFree=true; // can we click freely on the map
	
	boolean moveWP=false;
//	boolean exitMoveWP=false;
	int indexWPToMove=0;
	
	Journey wpMissiles;
	Journey wpTorpedoes;
	
	int percentageLoss=0; // Percentage of allies'loss compared to our loss
	
	public StrategicMap()
	{

	}

	/**
	 * Clean the map
	 */
	protected void cleanMap()
	{
		System.out.println("Clean the strategic map");

		this.cleanUnit();
			
		myWPDrawer.removeMe();
		
		selectedUnits.clear();
		
		RenderingManager.getInstance().removeEntity(idBackground,1);
		RenderingManager.getInstance().washAllAndPrepare();
		RenderingManager.removeMe();
		ParticleManager.removeMe();
		
		ourScene.remove(infoLabel);
		
		//ourScene.remove(fleetStatutButton);
		//ourScene.remove(scoreButton);
//		ourScene.remove(selectButton);
		ourScene.remove(zoomButton);
		
		ourScene.remove(playAsSubButton);
		ourScene.remove(playAsSurfaceButton);
		ourScene.remove(autoSwitchButton);
		
		ourScene.remove(addWPButton);
//		ourScene.remove(removeWPButton);
//		ourScene.remove(moveWPButton);
		
//		ourScene.remove(strategicMap);
		
		ourScene.remove(scoreLabel);
		ourScene.remove(costLabel);
		ourScene.remove(miaLabel);
		ourScene.remove(carrierAliveLabel);
		ourScene.remove(otherBoatsAliveLabel);
		ourScene.remove(subsAliveLabel);
		ourScene.remove(basesAliveLabel);
		
		ourScene.remove(labelEne);
		ourScene.remove(costLabelEne);
		ourScene.remove(miaLabelEne);
		ourScene.remove(carrierAliveLabelEne);
		ourScene.remove(otherBoatsAliveLabelEne);
		ourScene.remove(subsAliveLabelEne);
		ourScene.remove(basesAliveLabelEne);
		ourScene.remove(endLevelButton);
		ourScene.remove(percentageLossLabel);
		
		System.out.println("Tactical map cleaned");
	}

	public void setLevel(int level)
	{
		System.out.println("Start to load level");
		//myLevelLoader.loadLevel(level);
		
//		myLevelLoader.addOurSubs();
//		myLevelLoader.addOurBoats();
//		myLevelLoader.addAlliesSubs();
//		myLevelLoader.addAlliesBoats();
//		myLevelLoader.addEnemiesSubs();
//		myLevelLoader.addEnemiesBoats();
//		myLevelLoader.addNeutralSubs();
//		myLevelLoader.addNeutralBoats();
//		globalSensors=myLevelLoader.getGlobalSensors();
		
		System.out.println("Level loaded");
	}
//////DEBUG/////////////////////////
//	int loopNb=0;
//	double timeSinceStart=0;
//////////////////////////////
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#doLoop()
	 */
	public String doLoop()
	{
		int mouseX=0,mouseY=0;
		boolean mousePressed=false;
		boolean rightMousePressed = false;
		//boolean doNotHide=false;
		//double divider=10000;
		double divider=2500;
		
		tmpTime=new java.util.Date().getTime();
		timeInterval=tmpTime-lastTime;
		lastTime = tmpTime;
		if (Input.isDown(Input.KEY_N))
			divider=1000;
		if (Input.isDown(Input.KEY_B))
			divider=500;
		timeUsed= ((double )timeInterval)/divider;
		//System.out.println("Tactical map: do update");
		
////////DEBUG///////////////////////////////		
//		timeSinceStart+=timeUsed;
//		if (timeSinceStart > 0.2)
//		{
//			timeSinceStart=0;
//			LevelKeeper.getInstance().setNextMapCoord((int )(Math.random()*600+100),(int )(Math.random()*500+100));
//			MapKeeper2.getInstance().removeLocalEvents();
//			System.out.println("Automatic loop "+loopNb);
//			loopNb++;
//			return DrawMap.myName;
//		}
//////////////////////////////////////	
		MapKeeper2 theMapKeeper = MapKeeper2.getInstance();
		if (Input.isDown(Input.KEY_O))
			theMapKeeper.alliesTurnEnemies();
		
		mouseX = Input.getMouseX();
		mouseY = Input.getMouseY();
		
		int selX = mouseX;
		int selY = mouseY;
		if (selX >= 665)
			selX=664;
		if (selY >= 760)
			selY=759;
			
		GameKeeper theGameKeeper = GameKeeper.getInstance();
		
		scoreLabel.setText("Score "+theGameKeeper.getScore());

		if (theGameKeeper.getCostOur() > 1000000000)
		{
			costLabel.setText("Cost "+(theGameKeeper.getCostOur()/1000000000)+" Billions "+((theGameKeeper.getCostOur()-1000000000*(theGameKeeper.getCostOur()/1000000000))/1000000)+" Millions "+(theGameKeeper.getCostOur()-1000000*(theGameKeeper.getCostOur()/1000000))+" GBK");
		}
		else if (theGameKeeper.getCostOur() > 1000000)
		{
			costLabel.setText("Cost "+(theGameKeeper.getCostOur()/1000000)+" Millions "+(theGameKeeper.getCostOur()-1000000*(theGameKeeper.getCostOur()/1000000))+" GBK");
		}
		else
		{
			costLabel.setText("Cost "+theGameKeeper.getCostOur()+ " GBK");
		}

		miaLabel.setText("Missing in Action "+theGameKeeper.getMiaOur());

		carrierAliveLabel.setText(theGameKeeper.nbCarrierLeftOur+" carriers left on "+theGameKeeper.nbCarrierTotalOur);

		otherBoatsAliveLabel.setText(theGameKeeper.nbBoatLeftOur+" other boats left on "+theGameKeeper.nbBoatTotalOur);

		subsAliveLabel.setText(theGameKeeper.nbSubLeftOur+" submarines left on "+theGameKeeper.nbSubTotalOur);

		basesAliveLabel.setText(theGameKeeper.nbBasesLeftOur+" bases left on "+theGameKeeper.nbBasesTotalOur);
		
		if (theGameKeeper.getCostEnemies() > 1000000000)
		{
			costLabelEne.setText("Cost "+(theGameKeeper.getCostEnemies()/1000000000)+" Billions "+((theGameKeeper.getCostEnemies()-1000000000*(theGameKeeper.getCostEnemies()/1000000000))/1000000)+" Millions "+(theGameKeeper.getCostEnemies()-1000000*(theGameKeeper.getCostEnemies()/1000000))+" GBK");
		}
		else if (theGameKeeper.getCostOur() > 1000000)
		{
			costLabelEne.setText("Cost "+(theGameKeeper.getCostEnemies()/1000000)+" Millions "+(theGameKeeper.getCostEnemies()-1000000*(theGameKeeper.getCostEnemies()/1000000))+" GBK");
		}
		else
		{
			costLabelEne.setText("Cost "+theGameKeeper.getCostEnemies()+" GBK");
		}
		
		miaLabelEne.setText("Missing in Action "+theGameKeeper.getMiaEnemies());

		carrierAliveLabelEne.setText(theGameKeeper.nbCarrierLeftEnemies+" carriers left on "+theGameKeeper.nbCarrierTotalEnemies);

		otherBoatsAliveLabelEne.setText(theGameKeeper.nbBoatLeftEnemies+" other boats left on "+theGameKeeper.nbBoatTotalEnemies);

		subsAliveLabelEne.setText(theGameKeeper.nbSubLeftEnemies+" submarines left on "+theGameKeeper.nbSubTotalEnemies);

		basesAliveLabelEne.setText(theGameKeeper.nbBasesLeftEnemies+" bases left on "+theGameKeeper.nbBasesTotalEnemies);
		
		if (mouseX < START_MENU)
		{
			if (zoomButton.isSelected())
			{
				rectangleSel.validate();
				rectangleSel.setPos(selX, selY, 0);
				LevelKeeper.getInstance().setNextMapCoord(selX,selY);
				if (theMapKeeper.checkLevel())
					rectangleSel.setOurColor(0xff000000);
				else
					rectangleSel.setOurColor(0xffff0000);
				
				if (autoSwitch)
				{
					//System.out.println("Does it have only sub "+theMapKeeper.gotOnlySub());
					if(theMapKeeper.gotOnlySub())
					{
						playAsSubButton.setSelected(true);
						playAsSurfaceButton.setSelected(false);
					}
					else
					{
						playAsSurfaceButton.setSelected(true);
						playAsSubButton.setSelected(false);
					}
				}
			}
			else if (isEdited)
			{
				rectangleSel.validate();
				rectangleSel.setPos(xSelLeft, ySelDown, 0);
				rectangleSel.setSizeRect(xSelRight-xSelLeft, ySelUp-ySelDown);
			}
			else
			{
				rectangleSel.invalidate();
			}
		}
		else
			rectangleSel.invalidate();
		
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
		
		if (Input.isDown(Input.KEY_F5))
		{
			MapKeeper2.getInstance().saveInstance();
		}
		if (Input.isDown(Input.KEY_F4))
		{
			MapKeeper2.getInstance().loadInstance();
		}
//		
//		if (Input.isDown(Input.KEY_F6))
//		{
//			MapKeeper2.getInstance().semiKillEnemiesBases();
//		}
//		if (Input.isDown(Input.KEY_F7))
//		{
//			MapKeeper2.getInstance().semiKillEnemiesBoats();
//		}
//		if (Input.isDown(Input.KEY_F8))
//		{
//			MapKeeper2.getInstance().killEnemiesBases();
//		}
//		if (Input.isDown(Input.KEY_F9))
//		{
//			MapKeeper2.getInstance().killEnemiesBoats();
//		}
//		if (Input.isDown(Input.KEY_F9))
//		{
//			LevelKeeper.getInstance().setNextMapX(0);
//			LevelKeeper.getInstance().setNextMapY(0);
//			LevelKeeper.getInstance().setNextLevelWanted(3);
//
//			wonMapDone = true;
//
//			return DrawMap.myName;
//		}
		
		if (theMapKeeper.isGameOver())
		{
			if (!wonMapDone)
			{
				if (theMapKeeper.isGameWin())
				{
					LevelKeeper.getInstance().setNextMapX(0);
					LevelKeeper.getInstance().setNextMapY(0);
					LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.WON_SCREEN);

					wonMapDone = true;

					return DrawMap.myName;
				}	
				else 
				{
					DrawBackground.drawWinLose(DrawBackground.LOSE);
					SimMusic.getInstance().playBadEndMusic();
					wonMapDone = true;
				}
			}
		}
		
		if (endLevelButton.isClicked())
		{
			if (tmpTime-timeSinceClick < 3000)
			{
				// Save the current play
				MapKeeper2.getInstance().saveInstance();
				
				// Return to the main menu
				LevelKeeper.getInstance().setNextMapX(0);
				LevelKeeper.getInstance().setNextMapY(0);
				LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.MAIN_MENU);
				return DrawMap.myName;
			}
			timeSinceClick=tmpTime;
			endLevelButton.setSelected(false);
		}
		
//		if (unitToSelect)
//		{
//			// Set-up the interactive panel!
//			unitToSelect=false;
//			unitSelected=true;
//			if (isBoat)
//			{
//				currentBoat.setView(true);
//				switch (currentBoat.getType())
//				{
//					case Boat.CARRIER:
//						nameTypeUnit="Carrier"; 
//						break;
//					case Boat.AMPHIBIOUS:
//						nameTypeUnit="Amphibious"; 
//						break;
//					case Boat.CORVETTE:
//						nameTypeUnit="Corvette"; 
//						break;
//					case Boat.CRUISER:
//						nameTypeUnit="Cruiser"; 
//						break;
//					case Boat.DESTROYER:
//						nameTypeUnit="Destroyer"; 
//						break;
//					case Boat.FRIGATE:
//						nameTypeUnit="Frigate"; 
//						break;
//						default:
//							nameTypeUnit="Unknown"; 
//				}
//				infoLabel.setText(nameTypeUnit+"  "+currentBoat.getIdBoat()+" "+currentBoat.getName()+" - "+currentBoat.getCurrentSpeed()+" knots");
//			}
//			if (!isBoat)
//			{
//				currentSub.setView(true);
//				switch (currentSub.getType())
//				{
//					case Submarine.NUKE:
//						nameTypeUnit="SSN"; 
//						break;
//					case Submarine.DIESEL:
//						nameTypeUnit="Diesel"; 
//						break;
//					case Submarine.DIESEL_AIP:
//						nameTypeUnit="AIP Diesel"; 
//						break;
//					case Submarine.NUKE_SSBN:
//						nameTypeUnit="SSBN"; 
//						break;
//						default:
//							nameTypeUnit="Unknown"; 
//				}
//				infoLabel.setText(nameTypeUnit+"  "+currentSub.getIdBoat()+" "+currentSub.getName()+" - "+(int )currentSub.getCurrentSpeed()+" knots");
//			}
//		}
		if (unitSelected)
		{			
			mouseX = Input.getMouseX();
			mouseY = Input.getMouseY();
			// Do something if needed

			if (stateEdition==StrategicMap.ED_MOVE_WP)
			{
				// If no WP selected, first find one
				if (moveWP)
				{
					// If one selected, move it!
					xTarget=mouseX;
					yTarget=mouseY;
				}
			}
		}

		if (Input.isMouseDown())
		{
			mouseX = Input.getMouseX();
			mouseY = Input.getMouseY();
			
			if (isEdited)
			{
				xSelRight=mouseX;
				ySelUp=mouseY;
				selectedUnits.clear();
				theMapKeeper.selectUnits(selectedUnits, xSelLeft, ySelDown, xSelRight, ySelUp);
				if (selectedUnits.size() == 0)
				{
					infoLabel.setText("No unit selected");
				}
				else if (selectedUnits.size() == 1)
				{
					infoLabel.setText(selectedUnits.get(0).getName()+" " + selectedUnits.get(0).getIdMap()+" selected");
				}
				else
				{
					infoLabel.setText(selectedUnits.size()+" units selected");
					rectSelSize=(int )Math.sqrt((double )selectedUnits.size());
				}
			}
		}
		
		if (Input.isMouseReleased())
		{
			
			if (isEdited)
			{
				mouseX = Input.getMouseReleaseX();
				mouseY = Input.getMouseReleaseY();
				
				xSelRight=mouseX;
				ySelUp=mouseY;
				
				isEdited=false;
				selectedUnits.clear();
				theMapKeeper.selectUnits(selectedUnits, xSelLeft, ySelDown, xSelRight, ySelUp);
//				selectButton.setSelected(false);
				
				rectangleSel.invalidate();
				rectangleSel.setSizeRect(35, 40);
				if (selectedUnits.size() == 0)
				{
					infoLabel.setText("No unit selected");
				}
				else if (selectedUnits.size() == 1)
				{
					infoLabel.setText(selectedUnits.get(0).getName()+" " + selectedUnits.get(0).getIdMap()+" selected");
				}
				else
				{
					infoLabel.setText(selectedUnits.size()+" units selected");
					rectSelSize=(int )Math.sqrt((double )selectedUnits.size());
				}
			}
		}
		
		if (zoomButton.isClicked())
		{
			addWPButton.setSelected(false);
		}
		
		if (playAsSubButton.isClicked())
		{
			playAsSubButton.setSelected(true);
			playAsSurfaceButton.setSelected(false);
		}
		
		if (playAsSurfaceButton.isClicked())
		{
			playAsSurfaceButton.setSelected(true);
			playAsSubButton.setSelected(false);
		}
		
		if (autoSwitchButton.isClicked())
		{
			autoSwitch = autoSwitchButton.isSelected();
		}
		
		if (zoomButton.isClicked())
		{
			addWPButton.setSelected(false);
		}
		
		if ((mouseX < START_MENU) && Input.isMousePressed() && !zoomButton.isClicked() && !addWPButton.isSelected())
		{
			mouseX = Input.getMousePressX();
			mouseY = Input.getMousePressY();
			
			if (!theMapKeeper.selectUnit(selectedUnits, mouseX, mouseY))
			{
				if (!isEdited)
				{
					xSelLeft=mouseX;
					xSelRight=mouseX;
					ySelUp=mouseY;
					ySelDown=mouseY;
					isEdited=true;
				}
			}
			if (selectedUnits.size() == 0)
			{
				infoLabel.setText("No unit selected");
			}
			else if (selectedUnits.size() == 1)
			{
				infoLabel.setText(selectedUnits.get(0).getName()+" " + selectedUnits.get(0).getIdMap()+" selected");
			}
			else
			{
				infoLabel.setText(selectedUnits.size()+" units selected");
			}
		}
		if (Input.isPressed(Input.KEY_MOUSE_BUTTON_2))
		{
			mouseX = Input.getMousePressX();
			mouseY = Input.getMousePressY();
			if ((mouseX < START_MENU))
			{
				LevelKeeper.getInstance().setNextMapCoord(selX,selY);

				//if (MapKeeper2.getInstance().checkLevel())
				{
					theGameKeeper.addTurn();
					theMapKeeper.removeLocalEvents();
					return DrawMap.myName;
				}
				
			}
		}
		if (Input.isMousePressed())
		{
			mousePressed = true;
			mouseX = Input.getMousePressX();
			mouseY = Input.getMousePressY();
			if ((mouseX < START_MENU) && zoomButton.isSelected())
			{
				LevelKeeper.getInstance().setNextMapCoord(selX,selY);

				//if (MapKeeper2.getInstance().checkLevel())
				{
					theGameKeeper.addTurn();
					theMapKeeper.removeLocalEvents();
					
					/**
					 * Dispatch to the right TacticalMap though the same DrawMap.
					 */
					if (playAsSubButton.isSelected())
					{
						LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.SUB_GENERATED_LEVEL);
					}
					else
					{
						LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.GENERATED_LEVEL);
					}
					return DrawMap.myName;
				}
				
			}
			
			if ((mouseX < START_MENU) && addWPButton.isSelected())
			{
				xTarget=mouseX;
				yTarget=mouseY;
				
				int xRect=0;
				int yRect=0;
				
				ProgrammableUnit tmpUnit;
				
				for (int iUnits=0;iUnits < selectedUnits.size() ; iUnits++)
				{
					tmpUnit=selectedUnits.get(iUnits);
					if ((tmpUnit.getTypeFaction() != FUnit.BOAT_ENEMY) &&
							(tmpUnit.getTypeFaction() != FUnit.SUB_ENEMY) &&
							(tmpUnit.getTypeFaction() != FUnit.BASE_ENEMY)&&
							(tmpUnit.getTypeFaction() != FUnit.BASE_ALLIED)&&
							(tmpUnit.getTypeFaction() != FUnit.BASE_OUR))
					{
						tmpUnit.setPosMapTarget(xTarget+yRect, yTarget+xRect);
						tmpUnit.setFollowTargetMap(true);
						xRect+=8;
						if (xRect > rectSelSize*8)
						{
							xRect=0;
							yRect+=8;
						}
					}
				}
			}
		}
		
		if (Input.isPressed(Input.KEY_MOUSE_BUTTON_3))
		{
			rightMousePressed=true;
			//infoLabel.setText("No unit selected");
	
			//unitSelected=false;
			
			if (mouseX < START_MENU)
			{
				xTarget=mouseX;
				yTarget=mouseY;
				
				int xRect=0;
				int yRect=0;
				
				ProgrammableUnit tmpUnit;
				
				for (int iUnits=0;iUnits < selectedUnits.size() ; iUnits++)
				{
					tmpUnit=selectedUnits.get(iUnits);
					if ((tmpUnit.getTypeFaction() != FUnit.BOAT_ENEMY) &&
							(tmpUnit.getTypeFaction() != FUnit.SUB_ENEMY) &&
							(tmpUnit.getTypeFaction() != FUnit.BASE_ENEMY)&&
							(tmpUnit.getTypeFaction() != FUnit.BASE_ALLIED)&&
							(tmpUnit.getTypeFaction() != FUnit.BASE_OUR))
					{
						tmpUnit.setPosMapTarget(xTarget+yRect, yTarget+xRect);
						tmpUnit.setFollowTargetMap(true);
						xRect+=8;
						//System.out.println("Size of rectangle: "+rectSelSize);
						if (xRect > rectSelSize*8)
						{
							xRect=0;
							yRect+=8;
						}
					}
				}
			}
		}
		if (rightMousePressed)
		{
			myWPDrawer.hideMe();
		}
		// Go through the units and update them!
		theMapKeeper.updateUnits(timeUsed);
		
		return null;
	}

	public void cleanUnit()
	{
//		if (isBoat)
//		{
//			if (currentBoat != null)
//				currentBoat.setView(false);
//		}
//		if (!isBoat)
//		{
//			if (currentSub != null)
//				currentSub.setView(false);
//		}
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
			System.out.println("Quit the Strategic map");
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

	/**
	 * If we want the won map to be available again.
	 */
	public void resetWonMap()
	{
		wonMapDone = false;
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#start()
	 */
	public void start()
	{
		System.out.println("Create strategic map");
		
//		wonMapDone = false;
		
		//myLevelLoader = new LevelLoaderPC(this);
		if (!MapKeeper2.getInstance().isInitialised())
			MapKeeper2.getInstance().buildMap();
		
		// If the player come from a continue, load the previous save.
		MapKeeper2.getInstance().loadInstanceIfAsked();
				
		percentageLoss = GameKeeper.getInstance().calculateLossAlliesOur();
		
		if ((GameKeeper.getInstance().getNbTurn() > 10) && (percentageLoss > 70))
		{
			MapKeeper2.getInstance().alliesTurnEnemies();
		}
		MapKeeper2.getInstance().drawMapContent();
		
		System.out.println("There are "+MapKeeper2.getInstance().ourBases.size()+" bases");
		System.out.println("There are "+MapKeeper2.getInstance().nbOurBigBases+" bases");
		
		currentSignals= new ArrayList<Signal>();
		
//		globalSensors = new ArrayList<Sensor>();
//		
//		wpMissiles= new Journey();
//		wpTorpedoes= new Journey();
		
		selectedUnits= new ArrayList<ProgrammableUnit>();
		// Load the map
		
		// Load the sprites
		
		ourScene=(Scene2D )Stage.getScene();
		
		DrawBackground.drawBack();
		//strategicMap = new ImageSprite (CoreImage.load("MapVis.png"),0,0,700,Stage.getHeight());
		//ourScene.add(strategicMap);
		DrawBackground.drawBackMap();
		
		rectangleSel = new RectanglePC();
		
		rectangleSel.setSizeRect(35, 40);
		rectangleSel.setPos(350, 400, 0);
		
		idRectSel = RenderingManager.getInstance().addDrawableEntity(rectangleSel,39);
		
		infoLabel= new Label("No unit selected", 720, 20);
		ourScene.add(infoLabel);
		
		fleetStatutButton = Button.createLabeledToggleButton("Fleet statut", 720, 60);
		//ourScene.add(fleetStatutButton);
		scoreButton = Button.createLabeledToggleButton("Score", 860, 60);
		//ourScene.add(scoreButton);
//		selectButton = Button.createLabeledToggleButton("Select units", 720, 100);
//		ourScene.add(selectButton);
		
		zoomButton = Button.createLabeledToggleButton("Zoom", 850, 160);
		zoomButton.setAnchor(Sprite.CENTER);
		ourScene.add(zoomButton);
		
		playAsSubButton = Button.createLabeledToggleButton("Submarine", 780, 200);
		playAsSubButton.setAnchor(Sprite.CENTER);
		ourScene.add(playAsSubButton);
		playAsSubButton.setSelected(true);
		
		playAsSurfaceButton = Button.createLabeledToggleButton("Surface", 910, 200);
		playAsSurfaceButton.setAnchor(Sprite.CENTER);
		ourScene.add(playAsSurfaceButton);
		
		autoSwitchButton = Button.createLabeledToggleButton("Autoswitch", 850, 240);
		autoSwitchButton.setAnchor(Sprite.CENTER);
		ourScene.add(autoSwitchButton);
		autoSwitchButton.setSelected(true);
		autoSwitch = true;
	
		addWPButton=Button.createLabeledToggleButton("Go to", 850, 360);
		addWPButton.setAnchor(Sprite.CENTER);
		ourScene.add(addWPButton);
//		removeWPButton=Button.createLabeledToggleButton("Stop", 800, 360);
//		ourScene.add(removeWPButton);
		
		scoreLabel= new Label("Score XXX", 720, 400);
		ourScene.add(scoreLabel);
		costLabel= new Label("Cost XXXXXXXXXXX", 720, 440);
		ourScene.add(costLabel);
		miaLabel= new Label("Missing in Action XXXXXXXX", 720, 420);
		ourScene.add(miaLabel);
		carrierAliveLabel= new Label("XX carriers left on XX", 720, 480);
		ourScene.add(carrierAliveLabel);
		otherBoatsAliveLabel= new Label("XXXX other boats left on XXXX", 720, 460);
		ourScene.add(otherBoatsAliveLabel);
		subsAliveLabel= new Label("XXXX submarines left on XXXX", 720, 500);
		ourScene.add(subsAliveLabel);
		basesAliveLabel= new Label("XXXX bases left on XXXX", 720, 520);
		ourScene.add(basesAliveLabel);
		
		labelEne= new Label("- Enemies -", 740, 580);
		ourScene.add(labelEne);
		costLabelEne= new Label("Cost XXXXXXXXXXX", 720, 600);
		ourScene.add(costLabelEne);
		miaLabelEne= new Label("Missing in Action XXXXXXXX", 720, 620);
		ourScene.add(miaLabelEne);
		carrierAliveLabelEne= new Label("XX carriers left on XX", 720, 640);
		ourScene.add(carrierAliveLabelEne);
		otherBoatsAliveLabelEne= new Label("XXXX other boats left on XXXX", 720, 660);
		ourScene.add(otherBoatsAliveLabelEne);
		subsAliveLabelEne= new Label("XXXX submarines left on XXXX", 720, 680);
		ourScene.add(subsAliveLabelEne);
		basesAliveLabelEne= new Label("XXXX bases left on XXXX", 720, 700);
		ourScene.add(basesAliveLabelEne);
		
		endLevelButton= Button.createLabeledToggleButton("Menu", 850, 750);
		endLevelButton.setAnchor(Sprite.CENTER);
		
		if (percentageLoss > 70)
		{
			// Should print it in red...
		}
		percentageLossLabel = new Label("Allies losses "+percentageLoss+"%", 850, 560);
		percentageLossLabel.setAnchor(Sprite.CENTER);
		ourScene.add(percentageLossLabel);
		
		ourScene.add(endLevelButton);
		
		// Load the good level
		
		
		// TODO Auto-generated method stub
		setLevel(currentLevel);
		lastTime=new Date().getTime();
		myWPDrawer = new WPDrawer();
		
		textScreen=new Text();
		idTextScreen = RenderingManager.getInstance().addDrawableEntity(textScreen,39);
		textScreen.setTextToShow("One simple explanation");
		textScreen.setSizeRect(200,50);
		textScreen.invalidate();
		//myText.setRotation(0.4);
	}

	
	
	/**
	 * @return the currentSignals
	 */
	public ArrayList<Signal> getCurrentSignals()
	{
		return currentSignals;
	}

	/**
	 * @param currentSignals the currentSignals to set
	 */
	public void setCurrentSignals(ArrayList<Signal> currentSignals)
	{
		this.currentSignals = currentSignals;
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
}
