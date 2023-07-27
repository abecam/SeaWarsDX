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
import com.tgb.subengine.gameentities.*;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subengine.gfxentities.RectanglePC;
import com.tgb.subengine.gfxentities.Text;
import com.tgb.subgame.levels.*;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.unitspc.sensors.*;

/**
 * A special part to prepare the random creation of the map, start the creation
 * (by RandomMapCreator), and start the game (via the drawing first, like 
 * generally).
 * @author Alain Becam
 *
 */
public class PrepareRandomMap implements IGamePart
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
	
	static final String myName = "PrepareRandomMap";
	
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
	
	public PrepareRandomMap()
	{

	}

	/**
	 * Clean the map
	 */
	protected void cleanMap()
	{
		System.out.println("Clean the prepare random map");

		this.cleanUnit();
			
		myWPDrawer.removeMe();
		
		selectedUnits.clear();
		
		RenderingManager.getInstance().washAllAndPrepare();
		RenderingManager.removeMe();
		ParticleManager.removeMe();
		
		System.out.println("Prepare random map cleaned");
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
	/* (non-Javadoc)
	 * @see com.tgb.subengine.gamesystems.IGamePart#doLoop()
	 */
	public String doLoop()
	{
		
		RandomMapCreator.getInstance().setNewPlace();
		RandomMapCreator.getInstance().removeLocalEvents();
		
		return DrawMap.myName;			
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
		System.out.println("Create prepare random map");
		
		//myLevelLoader = new LevelLoaderPC(this);
		if (RandomMapCreator.getInstance().isInitialised())
			RandomMapCreator.getInstance().resetAll();
		
		RandomMapCreator.getInstance().buildMap();
		
		//MapKeeper2.getInstance().drawMapContent();
		
		currentSignals= new ArrayList<Signal>();
		
//		globalSensors = new ArrayList<Sensor>();
//		
//		wpMissiles= new Journey();
//		wpTorpedoes= new Journey();
		
		selectedUnits= new ArrayList<ProgrammableUnit>();
		// Load the map
		
		// Load the sprites
		
		
		ourScene=(Scene2D )Stage.getScene();
		
		//DrawBackground.drawBack();
		//strategicMap = new ImageSprite (CoreImage.load("MapVis.png"),0,0,700,Stage.getHeight());
		//ourScene.add(strategicMap);
		//DrawBackground.drawBackMap();
		
		
		// Load the good level
		
		
		//setLevel(currentLevel);
		lastTime=new java.util.Date().getTime();
		myWPDrawer = new WPDrawer();
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
