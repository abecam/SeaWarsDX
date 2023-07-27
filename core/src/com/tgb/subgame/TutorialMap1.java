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
import pulpcore.image.CoreImage;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.ParticleManager;
import com.tgb.subengine.gamesystems.IGamePart;
import com.tgb.subengine.gamesystems.StateManager;
import com.tgb.subengine.gameentities.*;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.gfxentities.RectanglePC;
import com.tgb.subengine.gfxentities.Text;
import com.tgb.subgame.levels.*;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.unitspc.sensors.*;

/**
 * The strategic map, the global one, adapted for the tutorial
 * @author Alain Becam
 *
 */
public class TutorialMap1 implements IGamePart
{
	LevelLoaderPC myLevelLoader;
	
	public final static int START_MENU=700; // Where the menu start (so where the map shouldn't be) 
	
	ArrayList<Sensor> globalSensors;
	
	ArrayList<Signal> currentSignals;
	
	
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
	
	static final String myName = "StrategicMapTutorial";
	
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
	
	Button addWPButton;
//	Button removeWPButton;
	Button moveWPButton;
	Button nextButton;
	Button backButton;
	
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
	
	Group tutText;
	
	SpritePC arrow;
	long idArrow,idArrow2;
	
	public TutorialMap1()
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
		ourScene.remove(nextButton);
		ourScene.remove(backButton);
		ourScene.remove(tutText);
		
		MapKeeperTutorial.unload();
		
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
		//boolean doNotHide=false;
		double divider=10000;
		
		tmpTime=new java.util.Date().getTime();
		timeInterval=tmpTime-lastTime;
		lastTime = tmpTime;
		if (Input.isDown(Input.KEY_N))
			divider=2500;
		if (Input.isDown(Input.KEY_B))
			divider=1000;
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
		
		mouseX = Input.getMouseX();
		mouseY = Input.getMouseY();
/*
		if (arrow != null)
		{
			arrow.setPos(mouseX, mouseY, 0);
			if (Input.isDown(Input.KEY_RIGHT))
			{
				arrow.rotate(0.2);
			}
			if (Input.isDown(Input.KEY_LEFT))
			{
				arrow.rotate(-0.2);
			}
			if (Input.isDown(Input.KEY_UP))
			{
				arrSize+=0.2;
				arrow.setSize(arrSize);
			}
			if (Input.isDown(Input.KEY_DOWN))
			{
				arrSize-=0.2;
				arrow.setSize(arrSize);
			}
			if (Input.isDown(Input.KEY_D))
			{
				System.out.println("Arrow "+arrow.getX()+" , "+arrow.getY()+" y - "+arrow.getSize()+" size - "+arrow.getRotation());
			}
		}
*/		
		int selX = mouseX;
		int selY = mouseY;
		if (selX >= 665)
			selX=664;
		if (selY >= 760)
			selY=759;
			
		scoreLabel.setText("Score "+GameKeeper.getInstance().getScore());

		costLabel.setText("Cost "+GameKeeper.getInstance().getCostOur());

		miaLabel.setText("Missing in Action "+GameKeeper.getInstance().getMiaOur());

		carrierAliveLabel.setText(GameKeeper.getInstance().nbCarrierLeftOur+" carriers left on "+GameKeeper.getInstance().nbCarrierTotalOur);

		otherBoatsAliveLabel.setText(GameKeeper.getInstance().nbBoatLeftOur+" other boats left on "+GameKeeper.getInstance().nbBoatTotalOur);

		subsAliveLabel.setText(GameKeeper.getInstance().nbSubLeftOur+" submarines left on "+GameKeeper.getInstance().nbSubTotalOur);

		basesAliveLabel.setText(GameKeeper.getInstance().nbBasesLeftOur+" bases left on "+GameKeeper.getInstance().nbBasesTotalOur);
		
		costLabelEne.setText("Cost "+GameKeeper.getInstance().getCostEnemies());
		
		miaLabelEne.setText("Missing in Action "+GameKeeper.getInstance().getMiaEnemies());

		carrierAliveLabelEne.setText(GameKeeper.getInstance().nbCarrierLeftEnemies+" carriers left on "+GameKeeper.getInstance().nbCarrierTotalEnemies);

		otherBoatsAliveLabelEne.setText(GameKeeper.getInstance().nbBoatLeftEnemies+" other boats left on "+GameKeeper.getInstance().nbBoatTotalEnemies);

		subsAliveLabelEne.setText(GameKeeper.getInstance().nbSubLeftEnemies+" submarines left on "+GameKeeper.getInstance().nbSubTotalEnemies);

		basesAliveLabelEne.setText(GameKeeper.getInstance().nbBasesLeftEnemies+" bases left on "+GameKeeper.getInstance().nbBasesTotalEnemies);
		if (backButton.isClicked())
		{
			sequenceNb--;
			if (sequenceNb < 0)
			{
				sequenceNb=0;
			}
			
			goToSequence(sequenceNb);
		}
		if (nextButton.isClicked())
		{
			sequenceNb++;
			if (sequenceNb == 25)
			{
				sequenceNb=0;
				return TutorialMap2.myName;
			}
			else
				goToSequence(sequenceNb);
		}

		if (mouseX < START_MENU)
		{
			if (zoomButton.isSelected())
			{
				rectangleSel.validate();
				rectangleSel.setPos(selX, selY, 0);
				LevelKeeper.getInstance().setNextMapCoord(selX,selY);
				if (MapKeeperTutorial.getInstance().checkLevel())
					rectangleSel.setOurColor(0xff000000);
				else
					rectangleSel.setOurColor(0xffff0000);
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
		
		
		if (endLevelButton.isClicked())
		{
			if (tmpTime-timeSinceClick < 3000)
			{
				// Return to the main menu
				LevelKeeper.getInstance().setNextMapX(0);
				LevelKeeper.getInstance().setNextMapY(0);
				LevelKeeper.getInstance().setNextLevelWanted(LevelKeeper.MAIN_MENU);
				return DrawMap.myName;
			}
			timeSinceClick=tmpTime;
			endLevelButton.setSelected(false);
		}
	
		if (unitSelected)
		{			
			mouseX = Input.getMouseX();
			mouseY = Input.getMouseY();
			// Do something if needed

			if (stateEdition==TutorialMap1.ED_MOVE_WP)
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
//		if (Input.isMouseReleased())
//		{
//			isEdited=false;
//		}
		
		if (Input.isMouseDown())
		{
			mouseX = Input.getMouseX();
			mouseY = Input.getMouseY();
			
			if (isEdited)
			{
				xSelRight=mouseX;
				ySelUp=mouseY;
				selectedUnits.clear();
				MapKeeperTutorial.getInstance().selectUnits(selectedUnits, xSelLeft, ySelDown, xSelRight, ySelUp);
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
				MapKeeperTutorial.getInstance().selectUnits(selectedUnits, xSelLeft, ySelDown, xSelRight, ySelUp);
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
		
		if ((mouseX < START_MENU) && Input.isMousePressed() && !zoomButton.isClicked() && !addWPButton.isSelected())
		{
			mouseX = Input.getMousePressX();
			mouseY = Input.getMousePressY();
			
			if (!MapKeeperTutorial.getInstance().selectUnit(selectedUnits, mouseX, mouseY))
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
		
		if (Input.isMousePressed())
		{
			mousePressed = true;
			mouseX = Input.getMousePressX();
			mouseY = Input.getMousePressY();
			if ((mouseX < START_MENU) && zoomButton.isSelected())
			{
				//LevelKeeper.getInstance().setNextMapCoord(selX,selY);
				
				if (sequenceNb == 18)
				{
					sequence19();
					sequenceNb=19;
				}
				// Nothing here, tutorial !
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
						System.out.println("Size of rectangle: "+rectSelSize);
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
		MapKeeperTutorial.getInstance().updateUnits(timeUsed);
		
		return null;
	}
	
	public void goToSequence(int sequenceNb)
	{
		switch (sequenceNb)
		{
			case 0:sequenceStart();
			ourScene.remove(backButton);
			break;
			
			case 1:sequence1();
			ourScene.add(backButton);
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

			case 15:
			sequenceBase1();
			break;
			
			case 16:
			sequenceBase2();
			break;
			
			case 17:
			sequenceBase3();
			break;
			
			case 18:sequence15();
			break;

			case 19:sequence16();
			break;

			case 20:sequence17();
			break;

			case 21:sequence18();
			break;

			case 22:sequence19();
			break;

			case 23:sequence20();
			break;

			case 24:sequence21();
			break;
		}
	}
	public void sequenceStart()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("              Welcome!\n          You will discover here\n          how to play Sea Wars\n\nTo quit and return to the main menu,\n double-click on MENU in the bottom-right corner", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		//tutText.setSize(tutText.width.getAsInt()*1.2, tutText.height.getAsInt()*1.2);
		ourScene.add(tutText);
	}
	
	public void sequence1()
	{
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("You have two available modes,\nFirst, the strategic map, visible here", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}

	public void sequence2()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("I need an arrow...", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence3()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow = new SpritePC(gfxSprites.getImageArrow());
		arrow.setPos(432.0 , 408.0, 0);
		arrow.setRotation(5.52);
		arrow.setSize(5.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
	}
	
	public void sequence4()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		arrow = new SpritePC(gfxSprites.getImageArrow());
		arrow.setPos(432.0 , 408.0, 0);
		arrow.setRotation(5.52);
		arrow.setSize(5.6);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("SMALLER!!!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence5()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		arrow.setSize(1);
		SpritePC arrow2 = new SpritePC(gfxSprites.getImageArrow());
		arrow2.setPos( 281.0 , 98.0, 0);
		arrow2.setRotation(0.6);
		arrow2.setSize(0.4);
		arrow.setPos(374.0 , 110.0, 0);
		arrow.setRotation(2.2);
		arrow.setSize(0.4);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		idArrow2 = RenderingManager.getInstance().addDrawableEntity(arrow2,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Ok! It shows the entire world\nand where your units are,\nlike here, these small boats.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence6()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("We do not see any enemy.\nIt is normal!!!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence7()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("But for now, let see the commands", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	
	public void sequence8()
	{
		ourScene.remove(tutText);
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(316.0 , 85.0, 0);
		arrow.setRotation(1.4);
		arrow.setSize(0.4);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		tutText = Label.createMultilineLabel("Here is a boat.\nYou can click on it to select it.\nTry now\n", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence9()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(573.0 , 29.0, 0);
		arrow.setRotation(6.28);
		arrow.setSize(1.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Its name appears here.\nOnce selected, you can move it\nby right clinking where you want it to go", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence10()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(640.0 , 227.0, 0);
		arrow.setRotation(0.6);
		arrow.setSize(2.2);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Or you can left click on Go To\nthen where you want the boat to go", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence11()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The world is big, the boat is small...\nBut you can go faster\nUse 'n' to accelerate the time,\n'b' to accelerate more", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence12()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Generally you have more than one unit available.\nYou can select several units by using a selection rectangle.\nTo do so, left click everywhere and drag the mouse\nwhile keeping the button down.\nTry now.", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence13()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(622.0 , 43.0, 0);
		arrow.setRotation(6.08);
		arrow.setSize(1.0);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("You can see how many units you selected,\nand move them as you did for only one boat.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence14()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("At their destination, they will group themselves.\nIt can be dangerous to use such a group\nto fight, as they might shoot at each others...", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequenceBase1()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(329.0 , 247.0, 0);
		arrow.setRotation(1.2);
		arrow.setSize(2);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Here you can see a base. If you lose all your bases,\nit is Game Over. If you kill all the enemy bases, you win.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequenceBase2()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(332.0 , 245.0, 0);
		arrow.setRotation(1.2);
		arrow.setSize(1.8);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("This circle represents the area of influence\nof one base. A boat in this circle\nwill be repaired and refurnished.\nA bigger circle shows a stronger base.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequenceBase3()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("The bases are generally protected\nby the smallest units, the corvettes.\nYou cannot see them on this map,\nbut they will appear if you zoom.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence15()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("You do not see the enemy,\nbut you have a big chance he will attack you\neverywhere you go!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence16()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(387.0 , 276.0, 0);
		arrow.setRotation(0.4);
		arrow.setSize(1.0);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		
		ourScene.remove(tutText);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		MapKeeperTutorial.getInstance().addFixedEvents();
		SpritePC arrow2 = new SpritePC(gfxSprites.getImageArrow());
		arrow2.setPos(295.0 , 116.0, 0);
		arrow2.setRotation(0.4);
		arrow2.setSize(0.6);
		idArrow2 = RenderingManager.getInstance().addDrawableEntity(arrow2,39);
		tutText = Label.createMultilineLabel("An event, such as this one, or this one,\nshows one enemy activity.\nIf you send one unit,\nyou have big chances to discover something!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence17()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		RenderingManager.getInstance().removeEntity(idArrow2,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("But everewhere you go\nyou have a quite big chance\nto find an enemy!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence18()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(675.0 , 102.0, 0);
		arrow.setRotation(0.4);
		arrow.setSize(1.6);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("To see the action, you must zoom using this button.\nThen move the rectangle where you want to zoom,\nand click! You can try now !", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence19()
	{
		ourScene.remove(tutText);
		arrow.setPos(675.0 , 102.0, 0);
		arrow.setRotation(0.4);
		arrow.setSize(1.6);
		tutText = Label.createMultilineLabel("The zoom rectangle is black if an event is present,\nor red when you dispose of no information.\nThe event informs you about what you will encounter,\nthey give a slight tactical advantage.", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence20()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		arrow.setPos(508.0 , 302.0, 0);
		arrow.setRotation(0.6);
		arrow.setSize(2.6);
		ourScene.remove(tutText);
		idArrow = RenderingManager.getInstance().addDrawableEntity(arrow,39);
		tutText = Label.createMultilineLabel("This area shows us various information\nabout your fleet and the enemy fleet.\nNotice that you are helped by some allies,\nand they are not accounted here.\nIn term of score, they are free.\nBut do not be too harsh with them,\nor they might prefer the enemy!", 350, 80);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
	}
	public void sequence21()
	{
		RenderingManager.getInstance().removeEntity(idArrow,39);
		ourScene.remove(tutText);
		tutText = Label.createMultilineLabel("Zooming sends us to the tactical map,\nwhere you can actually fight.\nLet see that now!", 350, 50);
		tutText.setAnchor(Sprite.CENTER);
		ourScene.add(tutText);
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

	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#start()
	 */
	public void start()
	{
		System.out.println("Create strategic map");
		
		//myLevelLoader = new LevelLoaderPC(this);
		if (!MapKeeperTutorial.getInstance().isInitialised())
			MapKeeperTutorial.getInstance().buildMap();
		
		MapKeeperTutorial.getInstance().drawMapContent();
		
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
	
		addWPButton=Button.createLabeledToggleButton("Go to", 850, 360);
		addWPButton.setAnchor(Sprite.CENTER);
		ourScene.add(addWPButton);
//		removeWPButton=Button.createLabeledToggleButton("Stop", 800, 360);
//		ourScene.add(removeWPButton);
		nextButton=Button.createLabeledToggleButton("Next", 620, 70);
		nextButton.setAnchor(Sprite.CENTER);
		ourScene.add(nextButton);
		backButton=Button.createLabeledToggleButton("Back", 80, 70);
		backButton.setAnchor(Sprite.CENTER);
		//ourScene.add(backButton);
		
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
		
		ourScene.add(endLevelButton);
		
		// Load the good level
		
		
		// TODO Auto-generated method stub
		setLevel(currentLevel);
		lastTime=new java.util.Date().getTime();
		myWPDrawer = new WPDrawer();
		
		textScreen=new Text();
		idTextScreen = RenderingManager.getInstance().addDrawableEntity(textScreen,39);
		textScreen.setTextToShow("One simple explanation");
		textScreen.setSizeRect(200,50);
		textScreen.invalidate();
		//myText.setRotation(0.4);
		// Though it might seem fine to actually pass the right value (the last one)
		// when we want to enter by the end, it is actually less dangerous this way
		// (encapsulating the end value)
		if (StateManager.getInstance().getValueToPass() > 0)
		{
			sequenceNb=24;
			goToSequence(sequenceNb);
			ourScene.add(backButton);
		}
		else
			sequenceStart();
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
