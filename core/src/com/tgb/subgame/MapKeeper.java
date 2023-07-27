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

import java.util.ArrayList;
import java.util.Random;

import com.tgb.subgame.unitspc.ProgrammableUnit;
import com.tgb.subgame.unitspc.Airplane;
import com.tgb.subgame.unitspc.Base;
import com.tgb.subgame.unitspc.Boat;
import com.tgb.subgame.unitspc.FUnit;
import com.tgb.subgame.unitspc.Submarine;
import com.tgb.subgame.unitspc.gfxSprites;
import com.tgb.subgame.unitspc.gfxSelections;
import com.tgb.subgame.unitspc.sensors.KnownDatas;
import com.tgb.subgame.unitspc.sensors.Radar;
import com.tgb.subgame.unitspc.sensors.Satellite;
import com.tgb.subgame.unitspc.sensors.Sonar;
import com.tgb.subgame.levels.LevelGenerated;
import com.tgb.subengine.gameentities.*;

import pulpcore.image.Colors;
import pulpcore.image.CoreImage;

/**
 * Create the strategic map and keep the info, allow the creation of the levels
 * @author Alain Becam
 *
 */
public class MapKeeper {

	boolean isInitialised=false;
	
	int xCurr,yCurr;
	int step=1; // Eventually, step to navigate the map (might be used here to distribute the units, and accelerate the process.
	
	Random myRandom;
	
	int idMap=0;
	
	boolean finished=false;
	
	boolean isBlank=true;
	
	ArrayList<Submarine> ourSubs;
	
	int nbOurSubNuke=40;
	int nbOurSubSSBN=20;
	int nbOurSubDiesel=0;
	int nbOurSubDieselAIP=10;
	
	// The *Max are here to reset the value. So it's better to set the *Max then reset to
	// change the value for a new game
	int nbOurSubNukeMax=40;
	int nbOurSubSSBNMax=20;
	int nbOurSubDieselMax=0;
	int nbOurSubDieselAIPMax=10;
	
	ArrayList<Boat> ourBoats;
	
	int nbOurBoatCarrier=10;
	int nbOurBoatFrigate=50;
	int nbOurBoatDestroyer=20;
	int nbOurBoatCruiser=10;
	int nbOurBoatCorvette=100;
	int nbOurBoatAmphibious=8;
	
	int nbOurBoatCarrierMax=10;
	int nbOurBoatFrigateMax=50;
	int nbOurBoatDestroyerMax=20;
	int nbOurBoatCruiserMax=10;
	int nbOurBoatCorvetteMax=100;
	int nbOurBoatAmphibiousMax=8;
	
	ArrayList<Submarine> alliesSubs;
	
	int nbAlliesSubNuke=20;
	int nbAlliesSubSSBN=10;
	int nbAlliesSubDiesel=30;
	int nbAlliesSubDieselAIP=20;	
	
	int nbAlliesSubNukeMax=20;
	int nbAlliesSubSSBNMax=10;
	int nbAlliesSubDieselMax=30;
	int nbAlliesSubDieselAIPMax=20;	
	
	ArrayList<Boat> alliesBoats;
	
	int nbAlliesBoatCarrier=10;
	int nbAlliesBoatFrigate=40;
	int nbAlliesBoatDestroyer=15;
	int nbAlliesBoatCruiser=10;
	int nbAlliesBoatCorvette=100;
	int nbAlliesBoatAmphibious=10;
	
	int nbAlliesBoatCarrierMax=10;
	int nbAlliesBoatFrigateMax=40;
	int nbAlliesBoatDestroyerMax=15;
	int nbAlliesBoatCruiserMax=10;
	int nbAlliesBoatCorvetteMax=100;
	int nbAlliesBoatAmphibiousMax=10;
	
	ArrayList<Submarine> enemiesSubs;
	
	int nbEnemiesSubNuke=30;
	int nbEnemiesSubSSBN=20;
	int nbEnemiesSubDiesel=40;
	int nbEnemiesSubDieselAIP=10;
	
	int nbEnemiesSubNukeMax=30;
	int nbEnemiesSubSSBNMax=20;
	int nbEnemiesSubDieselMax=40;
	int nbEnemiesSubDieselAIPMax=10;
	
	ArrayList<Boat> enemiesBoats;
	
	int nbEnemiesBoatCarrier=15;
	int nbEnemiesBoatFrigate=50;
	int nbEnemiesBoatDestroyer=20;
	int nbEnemiesBoatCruiser=20;
	int nbEnemiesBoatCorvette=80;
	int nbEnemiesBoatAmphibious=20;
	
	int nbEnemiesBoatCarrierMax=15;
	int nbEnemiesBoatFrigateMax=50;
	int nbEnemiesBoatDestroyerMax=20;
	int nbEnemiesBoatCruiserMax=20;
	int nbEnemiesBoatCorvetteMax=80;
	int nbEnemiesBoatAmphibiousMax=20;
	
	ArrayList<Submarine> neutralSubs;
	ArrayList<Boat> neutralBoats;
	
	// Dynamically added by boats and bases.
	ArrayList<Airplane> ourAirplanes;
	ArrayList<Airplane> alliesAirplanes;
	ArrayList<Airplane> enemiesAirplanes;
	ArrayList<Airplane> neutralAirplanes;
	
	ArrayList<Base> ourBases;
	int nbOurSmallBases=20;
	int nbOurBigBases=10;
	int nbOurMainBases=2;
	
	int nbOurSmallBasesMax=20;
	int nbOurBigBasesMax=10;
	int nbOurMainBasesMax=2;
	ArrayList<Base> alliesBases;
	int nbAlliesSmallBases=18;
	int nbAlliesBigBases=8;
	int nbAlliesMainBases=1;
	
	int nbAlliesSmallBasesMax=18;
	int nbAlliesBigBasesMax=8;
	int nbAlliesMainBasesMax=1;
	ArrayList<Base> enemiesBases;
	int nbEnemiesSmallBases=20;
	int nbEnemiesBigBases=15;
	int nbEnemiesMainBases=10;
	
	int nbEnemiesSmallBasesMax=50;
	int nbEnemiesBigBasesMax=20;
	int nbEnemiesMainBasesMax=3;
	
	boolean carrierGroupsBuilt=false;
	
	ArrayList<Base> neutralBases;
	
	StrategicMap theStrategicMap;
	
	static private MapKeeper instance=null;
	
	int[] rawRefData=null;
	
	private MapKeeper()
	{
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
		
		gfxSprites.callMeFirst();
		gfxSelections.callMeFirst();
		
		CoreImage globalMapImage = CoreImage.load("MapRef.png");

		rawRefData = globalMapImage.getData();
		
		myRandom = new Random();
	}

	public static MapKeeper getInstance()
	{
		if (instance== null)
			instance = new MapKeeper();
		
		return instance;
	}
	
	public static void unload()
	{
		if (instance!= null)
		{
			instance.removeData();
			instance = null;
		}
	}
	
	public void removeData()
	{
		rawRefData=null;
	}
	
	/**
	 * Clean all
	 */
	public void cleanAll()
	{
		System.out.println("Clean the map keeper content");

		if (ourSubs != null)
		{
			for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
			{
				ourSubs.get(iSub).removeMeSM();
			}
			ourSubs.clear();
		}
		if (ourBoats != null)
		{
			for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
			{
				ourBoats.get(iBoat).removeMeSM();
			}
			ourBoats.clear();
		}
		if (alliesSubs != null)
		{
			for (int iSub = 0; iSub < alliesSubs.size() ; iSub++)
			{
				alliesSubs.get(iSub).removeMeSM();
			}
			alliesSubs.clear();
		}
		if (alliesBoats != null)
		{
			for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
			{
				// Update !!!
				alliesBoats.get(iBoat).removeMeSM();
			}
			alliesBoats.clear();
		}
		if (enemiesSubs != null)
		{
			for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
			{
				enemiesSubs.get(iSub).removeMeSM();
			}
			enemiesSubs.clear();
		}
		if (enemiesBoats != null)
		{
			for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
			{
				enemiesBoats.get(iBoat).removeMeSM();
			}
			enemiesBoats.clear();
		}
		if (neutralSubs != null)
		{
			for (int iSub = 0; iSub < neutralSubs.size() ; iSub++)
			{
				neutralSubs.get(iSub).removeMeSM();
			}
			neutralSubs.clear();
		}
		if (neutralBoats != null)
		{
			for (int iBoat = 0; iBoat < neutralBoats.size() ; iBoat++)
			{
				neutralBoats.get(iBoat).removeMeSM();
			}
			neutralBoats.clear();
		}
		//
		// Currently we do not manage any airplanes, but that might come later
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
				ourBases.get(iBoat).removeMeSM();
			}
			ourBases.clear();
		}
		if (alliesBases != null)
		{
			for (int iBoat = 0; iBoat < alliesBases.size() ; iBoat++)
			{
				alliesBases.get(iBoat).removeMeSM();
			}
			alliesBases.clear();
		}
		if (enemiesBases != null)
		{
			for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
			{
				enemiesBases.get(iBoat).removeMeSM();
			}
			enemiesBases.clear();
		}
		if (neutralBases != null)
		{
			for (int iBoat = 0; iBoat < neutralBases.size() ; iBoat++)
			{
				// Update !!!
			neutralBases.get(iBoat).removeMeSM();
			}
			neutralBases.clear();
		}
		System.out.println("Map keeper content cleaned");
		cleanMemory();
		System.out.println("Map keeper content cleaned");
	}
	
	private void cleanMemory()
	{
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
	}
	
	/**
	 * Draw the entities
	 */
	public void drawMapContent()
	{
		Base oneBase;
		Boat oneBoat;
		Submarine oneSub;
		
		// Reset the count of left units, so we can count them now
		GameKeeper.getInstance().nbCarrierLeftOur=GameKeeper.getInstance().nbCarrierTotalOur;
		GameKeeper.getInstance().nbBoatLeftOur=GameKeeper.getInstance().nbBoatTotalOur;
		GameKeeper.getInstance().nbSubLeftOur=GameKeeper.getInstance().nbSubTotalOur;
		GameKeeper.getInstance().nbBasesLeftOur=GameKeeper.getInstance().nbBasesTotalOur;
		
		GameKeeper.getInstance().nbCarrierLeftAllies=GameKeeper.getInstance().nbCarrierTotalAllies;
		GameKeeper.getInstance().nbBoatLeftAllies=GameKeeper.getInstance().nbBoatTotalAllies;
		GameKeeper.getInstance().nbSubLeftAllies=GameKeeper.getInstance().nbSubTotalAllies;
		GameKeeper.getInstance().nbBasesLeftAllies=GameKeeper.getInstance().nbBasesTotalAllies;
		
		GameKeeper.getInstance().nbCarrierLeftEnemies=GameKeeper.getInstance().nbCarrierTotalEnemies;
		GameKeeper.getInstance().nbBoatLeftEnemies=GameKeeper.getInstance().nbBoatTotalEnemies;
		GameKeeper.getInstance().nbSubLeftEnemies=GameKeeper.getInstance().nbSubTotalEnemies;
		GameKeeper.getInstance().nbBasesLeftEnemies=GameKeeper.getInstance().nbBasesTotalEnemies;
		
		
		GameKeeper.getInstance().setScore(0);
		
		for (int iBase=0;iBase <ourBases.size(); ++iBase)
		{
			oneBase = ourBases.get(iBase);
			if (oneBase.isDead())
				GameKeeper.getInstance().nbBasesLeftOur--;
			
			oneBase.createGfxSM(oneBase.getXMap(), oneBase.getYMap(), 0, 0, 0);
		}
		for (int iBase=0;iBase <enemiesBases.size(); ++iBase)
		{
			oneBase=enemiesBases.get(iBase);
			
			if (oneBase.isDead())
			{
				GameKeeper.getInstance().nbBasesLeftEnemies--;
				
				if (oneBase.getType() == Base.MAINBASEONE)
				{
					GameKeeper.getInstance().addPoint(1000);
				}
				else if (oneBase.getType() == Base.MAINBASETWO)
				{
					GameKeeper.getInstance().addPoint(800);
				}
				else if (oneBase.getType() == Base.BIGBASEONE)
				{
					GameKeeper.getInstance().addPoint(500);
				}
				else if (oneBase.getType() == Base.BIGBASETWO)
				{
					GameKeeper.getInstance().addPoint(400);
				}
				else if (oneBase.getType() == Base.SMALLBASEONE)
				{
					GameKeeper.getInstance().addPoint(100);
				}
				else if (oneBase.getType() == Base.SMALLBASETWO)
				{
					GameKeeper.getInstance().addPoint(80);
				}
			}
			
			oneBase.createGfxSM(oneBase.getXMap(), oneBase.getYMap(), 0, 0, 0);
		}
		for (int iBase=0;iBase < alliesBases.size(); ++iBase)
		{
			oneBase=alliesBases.get(iBase);
			
			if (oneBase.isDead())
				GameKeeper.getInstance().nbBasesLeftAllies--;
			
			oneBase.createGfxSM(oneBase.getXMap(), oneBase.getYMap(), 0, 0, 0);
		}
		
		for (int iBoat=0;iBoat <ourBoats.size(); ++iBoat)
		{
			oneBoat=ourBoats.get(iBoat);
			if (oneBoat.isDead())
			{
				if (oneBoat.getType() == Boat.CARRIER)
				{
					GameKeeper.getInstance().nbCarrierLeftOur--;
				}
				else
				{
					GameKeeper.getInstance().nbBoatLeftOur--;
				}
			}
			
			oneBoat.createGfxSM(oneBoat.getXMap(), oneBoat.getYMap(), 0, 0, 0);
		}
		for (int iBoat=0;iBoat <enemiesBoats.size(); ++iBoat)
		{
			oneBoat=enemiesBoats.get(iBoat);
			if (oneBoat.isDead())
			{
				if (oneBoat.getType() == Boat.CARRIER)
				{
					GameKeeper.getInstance().nbCarrierLeftEnemies--;
				}
				else
				{
					GameKeeper.getInstance().nbBoatLeftEnemies--;
				}
				
				if (oneBoat.getType() == Boat.CARRIER)
				{
					GameKeeper.getInstance().addPoint(100);
				}
				else if (oneBoat.getType() == Boat.CRUISER)
				{
					GameKeeper.getInstance().addPoint(50);
				}
				else if (oneBoat.getType() == Boat.AMPHIBIOUS)
				{
					GameKeeper.getInstance().addPoint(80);
				}
				else if (oneBoat.getType() == Boat.DESTROYER)
				{
					GameKeeper.getInstance().addPoint(30);
				}
				else if (oneBoat.getType() == Boat.FRIGATE)
				{
					GameKeeper.getInstance().addPoint(20);
				}
				else if (oneBoat.getType() == Boat.CORVETTE)
				{
					GameKeeper.getInstance().addPoint(5);
				}
			}
			oneBoat.createGfxSM(oneBoat.getXMap(), oneBoat.getYMap(), 0, 0, 0);
		}
		for (int iBoat=0;iBoat < alliesBoats.size(); ++iBoat)
		{
			oneBoat=alliesBoats.get(iBoat);
			
			if (oneBoat.getType() == Boat.CARRIER)
			{
				GameKeeper.getInstance().nbCarrierLeftAllies--;
			}
			else
			{
				GameKeeper.getInstance().nbBoatLeftAllies--;
			}
			
			oneBoat.createGfxSM(oneBoat.getXMap(), oneBoat.getYMap(), 0, 0, 0);
		}
		
		for (int iSub=0;iSub <ourSubs.size(); ++iSub)
		{
			oneSub=ourSubs.get(iSub);
			if (oneSub.isDead())
				GameKeeper.getInstance().nbSubLeftOur--;
			
			oneSub.createGfxSM(oneSub.getXMap(), oneSub.getYMap(), 0, 0, 0);
		}
		for (int iSub=0;iSub <enemiesSubs.size(); ++iSub)
		{
			oneSub=enemiesSubs.get(iSub);
			
			if (oneSub.isDead())
			{
				GameKeeper.getInstance().nbSubLeftEnemies--;
				
				if (oneSub.getType() == Submarine.NUKE)
				{
					GameKeeper.getInstance().addPoint(80);
				}
				else if (oneSub.getType() == Submarine.NUKE_SSBN)
				{
					GameKeeper.getInstance().addPoint(100);
				}
				else if (oneSub.getType() == Submarine.DIESEL)
				{
					GameKeeper.getInstance().addPoint(40);
				}
				else if (oneSub.getType() == Submarine.DIESEL_AIP)
				{
					GameKeeper.getInstance().addPoint(20);
				}
			}
			
			oneSub.createGfxSM(oneSub.getXMap(), oneSub.getYMap(), 0, 0, 0);
		}
		for (int iSub=0;iSub < alliesSubs.size(); ++iSub)
		{
			oneSub=alliesSubs.get(iSub);
			
			if (oneSub.isDead())
				GameKeeper.getInstance().nbSubLeftAllies--;
			
			oneSub.createGfxSM(oneSub.getXMap(), oneSub.getYMap(), 0, 0, 0);
		}
	}
	
	/**
	 * Build the carrier groups: 1 carriers with other units for protection
	 * Similar methods should be written for convoys and others.
 	 */
	public void buildCarrierGroups()
	{
		for (int iOurCarriers=0;iOurCarriers<this.nbOurBoatCarrier; iOurCarriers++)
		{
			
		}
		
		carrierGroupsBuilt=true;
	}
	
	private int findPlaceBoatLeft (final int up,int left)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		//boolean isClear=false;
		int stepDone=0;
		
		final int yTmp=up;
		int xTmp=400;
		
		//int lengthPath=0;
		// Test horizontally left.
		for (;xTmp>400-left;xTmp--)
		{
			final int rgbaValue=Colors.unpremultiply(rawRefData[yTmp*700+xTmp]);
			
			final int returnValue=rgbaValue & 0xFF;
			if (returnValue == 0)
			{
				stepDone++;
			}
			else
			{
				if (stepDone > 6)
				{
					return xTmp+4; // 4 pixel before we found the coast.
				}
				else
					return -1;
			}
		}
		return (xTmp);
	}
	
	private int findPlaceBoatRight (final int up,final int right)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		//boolean isClear=false;
		
		int stepDone=0;
		
		final int yTmp=up;
		int xTmp=300;
		
		//int lengthPath=0;
		// Test horizontally left.
		for (;xTmp<300+right;xTmp++)
		{
			int rgbaValue=Colors.unpremultiply(rawRefData[yTmp*700+xTmp]);
			
			int returnValue=rgbaValue & 0xFF;
			if (returnValue < 5)
			{
				stepDone++;
			}
			else
			{
				if (stepDone > 6)
				{
					return xTmp-4; // 4 pixel before we found the coast.
				}
				else
					return -1;
			}
		}
		return (xTmp);
	}
	
	private int findPlaceBaseLeft (final int up,final int left)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		//boolean isClear=false;
		int stepDone=0;
		int stepSea=0;
		
		int yTmp=up;
		int xTmp=300;
		
		//int lengthPath=0;
		// Test horizontally left.
		for (;xTmp>400-left;xTmp--)
		{
			int rgbaValue=Colors.unpremultiply(rawRefData[yTmp*700+xTmp]);
			
			int returnValue=rgbaValue & 0xFF;
			if (returnValue < 12)
			{
				stepSea++;
				stepDone=0;
			}
			else
			{
				if ((stepDone > 6) && (stepSea >= 12))
				{
					return xTmp-2; // 4 pixel after we found the coast.
				}
				++stepDone;
			}
		}
		return -1;
	}
	
	private int findPlaceBaseRight (int up,int right)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		//boolean isClear=false;
		
		int stepDone=0;
		int stepSea=0;
		
		int yTmp=up;
		int xTmp=350;
		
		//int lengthPath=0;
		// Test horizontally left.
		for (;xTmp<300+right;xTmp++)
		{
			int rgbaValue=Colors.unpremultiply(rawRefData[yTmp*700+xTmp]);
			
			int returnValue=rgbaValue & 0xFF;
			if (returnValue < 12)
			{
				stepSea++;
				stepDone=0;
			}
			else
			{
				if ((stepDone > 6) && (stepSea >= 12))
				{
					return xTmp+2; // 4 pixel before we found the coast.
				}
				stepDone++;
			}
		}
		return -1;
	}
	
	public static double dist(double x,double y,double x1, double y1)
	{
		return Math.sqrt(Math.pow(x-x1, 2)+Math.pow(y-y1, 2));
	}
	
	/**
	 * Check if the place is free of others bases.
	 * @return
	 */
	public boolean isPlaceBaseFree(double x,double y)
	{
		ProgrammableUnit tmpBase;
		
		for (int iBase=0;iBase <ourBases.size(); ++iBase)
		{
			tmpBase=ourBases.get(iBase);
			
			if (MapKeeper.dist(x, y, tmpBase.getXMap(), tmpBase.getYMap()) < 4)
			{
				return false;
			}
		}
		for (int iBase=0;iBase <enemiesBases.size(); ++iBase)
		{
			tmpBase=enemiesBases.get(iBase);
			
			if (MapKeeper.dist(x, y, tmpBase.getXMap(), tmpBase.getYMap()) < 4)
			{
				return false;
			}
		}
		for (int iBase=0;iBase < alliesBases.size(); ++iBase)
		{
			tmpBase=alliesBases.get(iBase);
			
			if (MapKeeper.dist(x, y, tmpBase.getXMap(), tmpBase.getYMap()) < 4)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the place is free of others bases.
	 * @return
	 */
	public boolean isPlaceBoatFree(double x,double y)
	{
		ProgrammableUnit tmpBoat;
		
		for (int iBoat=0;iBoat <ourBoats.size(); ++iBoat)
		{
			tmpBoat=ourBoats.get(iBoat);
			
			if (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
			{
				return false;
			}
		}
		for (int iBoat=0;iBoat <enemiesBoats.size(); ++iBoat)
		{
			tmpBoat=enemiesBoats.get(iBoat);
			
			if (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
			{
				return false;
			}
		}
		for (int iBoat=0;iBoat < alliesBoats.size(); ++iBoat)
		{
			tmpBoat=alliesBoats.get(iBoat);
			
			if (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the boat is close to other boats.
	 * @return
	 */
	public boolean isPlaceBoatFree(ProgrammableUnit ourBoat,double x,double y)
	{
		ProgrammableUnit tmpBoat;
		if (!ourBoat.isDead())
		{
			for (int iBoat = 0; iBoat < ourBoats.size(); ++iBoat)
			{
				tmpBoat = ourBoats.get(iBoat);
				if (!tmpBoat.isDead())
				{
					if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
					{
						return false;
					}
				}
			}
			for (int iBoat = 0; iBoat < enemiesBoats.size(); ++iBoat)
			{
				tmpBoat = enemiesBoats.get(iBoat);
				if (!tmpBoat.isDead())
				{
					if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
					{
						return false;
					}
				}
			}
			for (int iBoat = 0; iBoat < alliesBoats.size(); ++iBoat)
			{
				tmpBoat = alliesBoats.get(iBoat);
				if (!tmpBoat.isDead())
				{
					if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if the sub is close to other boats.
	 * @return
	 */
	public boolean isPlaceSubFree(Submarine ourBoat,double x,double y)
	{
		Submarine tmpBoat;
		if (!ourBoat.isDead())
		{
			for (int iBoat = 0; iBoat < ourSubs.size(); ++iBoat)
			{
				tmpBoat = ourSubs.get(iBoat);
				if (!tmpBoat.isDead())
				{
					if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
					{
						return false;
					}
				}
			}
			for (int iBoat = 0; iBoat < enemiesSubs.size(); ++iBoat)
			{
				tmpBoat = enemiesSubs.get(iBoat);
				if (!tmpBoat.isDead())
				{
					if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
					{
						return false;
					}
				}
			}
			for (int iBoat = 0; iBoat < alliesSubs.size(); ++iBoat)
			{
				tmpBoat = alliesSubs.get(iBoat);
				if (!tmpBoat.isDead())
				{
					if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Calculate a vector of repulsion based on the direct neighborhood.
	 * @param ourBoat
	 * @param x
	 * @param y
	 * @return the repulsion vector
	 */
	public Vector2D calculateRepulsionBoat(ProgrammableUnit ourBoat, double x, double y)
	{
		ProgrammableUnit tmpBoat;
		Vector2D newVector = new Vector2D();

		boolean done=false;
		
		if (!ourBoat.isDead())
		{
			if (ourBoat.getTypeFaction() != FUnit.BOAT_ENEMY)
			{
				for (int iBoat = 0; iBoat < ourBoats.size(); ++iBoat)
				{
					tmpBoat = ourBoats.get(iBoat);

					if (!tmpBoat.isDead() && !done)
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							newVector.addXY(8*(tmpBoat.getXMap() - x),8*(tmpBoat.getYMap() - y));
							done=true;
							break;
						}
					}
				}
				for (int iBoat = 0; iBoat < enemiesBoats.size(); ++iBoat)
				{
					tmpBoat = enemiesBoats.get(iBoat);
					if (!tmpBoat.isDead() && !done)
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							newVector.addXY(8*(tmpBoat.getXMap() - x),8*(tmpBoat.getYMap() - y));
							done=true;
							break;
						}
					}
				}
			}
			else
			{
				for (int iBoat = 0; iBoat < alliesBoats.size(); ++iBoat)
				{
					tmpBoat = alliesBoats.get(iBoat);
					if (!tmpBoat.isDead() && !done)
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							newVector.addXY(8*(tmpBoat.getXMap() - x),8*(tmpBoat.getYMap() - y));
							done=true;
							break;
						}
					}
				}
			}
		}
		return newVector;
	}

	/**
	 * Calculate a vector of repulsion based on the direct neighborhood.
	 * @param ourBoat
	 * @param x
	 * @param y
	 * @return the repulsion vector
	 */
	public Vector2D calculateRepulsionSub(Submarine ourBoat, double x, double y)
	{
		Submarine tmpBoat;
		Vector2D newVector = new Vector2D();

		if (!ourBoat.isDead())
		{
			if (ourBoat.getTypeFaction() != FUnit.SUB_ENEMY)
			{
				for (int iBoat = 0; iBoat < ourSubs.size(); ++iBoat)
				{
					tmpBoat = ourSubs.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							newVector.addXY(8*(tmpBoat.getXMap() - x),8*(tmpBoat.getYMap() - y));
						}
					}
				}
				for (int iBoat = 0; iBoat < enemiesSubs.size(); ++iBoat)
				{
					tmpBoat = enemiesSubs.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							newVector.addXY(8*(tmpBoat.getXMap() - x),8*(tmpBoat.getYMap() - y));
						}
					}
				}
			}
			else
			{
				for (int iBoat = 0; iBoat < alliesSubs.size(); ++iBoat)
				{
					tmpBoat = alliesSubs.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							newVector.addXY(8*(tmpBoat.getXMap() - x),8*(tmpBoat.getYMap() - y));
						}
					}
				}
			}
		}
		return newVector;
	}

	/**
	 * Check if the boat is close to other boats.
	 * @return true if the boat is close to another or several boats
	 */
	public boolean stickBoat(ProgrammableUnit ourBoat, double x, double y)
	{
		ProgrammableUnit tmpBoat;
		Submarine tmpSub;

		if (!ourBoat.isDead())
		{
			if (ourBoat.getTypeFaction() == FUnit.BOAT_ENEMY)
			{
				for (int iBoat = 0; iBoat < ourBoats.size(); ++iBoat)
				{
					tmpBoat = ourBoats.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							return false;
						}
					}
				}
				for (int iBoat = 0; iBoat < alliesBoats.size(); ++iBoat)
				{
					tmpBoat = alliesBoats.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							return false;
						}
					}
				}
				for (int iBoat = 0; iBoat < enemiesSubs.size(); ++iBoat)
				{
					tmpSub = enemiesSubs.get(iBoat);
					if (!tmpSub.isDead())
					{
						if (MapKeeper.dist(x, y, tmpSub.getXMap(), tmpSub.getYMap()) < 8)
						{
							return false;
						}
					}
				}
			}
			else
			{
				for (int iBoat = 0; iBoat < enemiesBoats.size(); ++iBoat)
				{
					tmpBoat = enemiesBoats.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							return false;
						}
					}
				}
				for (int iBoat = 0; iBoat < ourSubs.size(); ++iBoat)
				{
					tmpSub = ourSubs.get(iBoat);
					if (!tmpSub.isDead())
					{
						if (MapKeeper.dist(x, y, tmpSub.getXMap(), tmpSub.getYMap()) < 8)
						{
							return false;
						}
					}
				}
				for (int iBoat = 0; iBoat < alliesSubs.size(); ++iBoat)
				{
					tmpSub = alliesSubs.get(iBoat);
					if (!tmpSub.isDead())
					{
						if (MapKeeper.dist(x, y, tmpSub.getXMap(), tmpSub.getYMap()) < 8)
						{
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if the sub is close to other boats.
	 * @return true if so
	 */
	public boolean stickSub(Submarine ourBoat,double x,double y)
	{
		Submarine tmpBoat;
		
		if (!ourBoat.isDead())
		{
			if (ourBoat.getTypeFaction() == FUnit.SUB_ENEMY)
			{
				for (int iBoat = 0; iBoat < ourSubs.size(); ++iBoat)
				{
					tmpBoat = ourSubs.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							return false;
						}
					}
				}
				for (int iBoat = 0; iBoat < alliesSubs.size(); ++iBoat)
				{
					tmpBoat = alliesSubs.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							return false;
						}
					}
				}	
			}
			else
			{
				for (int iBoat = 0; iBoat < enemiesSubs.size(); ++iBoat)
				{
					tmpBoat = enemiesSubs.get(iBoat);
					if (!tmpBoat.isDead())
					{
						if ((tmpBoat != ourBoat) && (MapKeeper.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
						{
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * In the level defined by its left-up corner xLevel,yLevel, from a position x,y,
	 * find and create a valid journey in the sea (only for now)
	 * @param xLevel
	 * @param yLevel
	 * @param x
	 * @param y
	 * @return the Journey
	 */
	private Journey createWPs(int xLevel,int yLevel, double x, double y)
	{
		// Limits right-down
		int xLevH=xLevel+35;
		int yLevH=yLevel+40;
		
		// We will use the journey as a memory, allowing to go back.
		// For that we use the mark of the waypoint to know what we already did
		Journey currentJourney=new Journey();
		
//		boolean searchFinished=false;
//		
//		while (!searchFinished)
//		{
//			searchFinished=
//		}
		nbRecc=0;
		newDir=0;
		
		searchNextWP(rawRefData,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y);
			
		System.out.println("Found a path of "+currentJourney.size()+" wps");
		
		// Then transform the WP for level coords.
		// Also reduce the number of WPs.
		long dir=0;
		long newdir=0;
		int itDir=0;
		
		for (int iWP=0;iWP < currentJourney.size() ; iWP++)
		{
			Waypoint currentWP=currentJourney.getWP(iWP);
			if (iWP < currentJourney.size()-1)
			{
				newdir = Math.round((currentWP.getXWP()-currentJourney.getWP(iWP+1).getXWP())+(currentWP.getYWP()-currentJourney.getWP(iWP+1).getYWP())*2);
				System.out.println("New dir "+newdir);
				if (newdir == dir)
				{
					itDir++;
					//if (itDir > 1)
					{
						currentJourney.removeWP(iWP);
						iWP--;
						itDir=0;
					}
//					else
//					{
//						currentWP.setXWP((currentWP.getXWP()-xLevel)*DrawMap.step);
//						currentWP.setYWP((currentWP.getYWP()-yLevel)*DrawMap.step);
//					}
				}
				else
				{
					if (Math.random() > 0.4)
					{
						currentJourney.removeWP(iWP);
						iWP--;
					}
					else
					{
						currentWP.setXWP((currentWP.getXWP()-xLevel)*DrawMap.step);
						currentWP.setYWP((currentWP.getYWP()-yLevel)*DrawMap.step);
						dir=newdir;
					}
				}
			}	
			else
			{
				currentWP.setXWP((currentWP.getXWP()-xLevel)*DrawMap.step);
				currentWP.setYWP((currentWP.getYWP()-yLevel)*DrawMap.step);
			}
		}
		//Waypoint newWP=new Waypoint((x-xLevel)*DrawMap.step,(x-yLevel)*DrawMap.step,0);
		return currentJourney;
	}
	
	/**
	 * Check the neigbours
	 * 
	 * @param data
	 * @param x
	 * @param y
	 * @param xLevel
	 * @param yLevel
	 * @param xLevH
	 * @param yLevH
	 * @return the new mark (neighbourhood)
	 */
	private int checkNeighbours(int [] data,double x,double y,int xLevel,int yLevel,int xLevH, int yLevH)
	{
		int newMark=0;
		int xInt=(int ) x;
		int yInt=(int ) y;
		
		if (getValue(xInt-1,yInt,xLevel,yLevel,xLevH, yLevH) >= 1)
		{
			newMark|=1;
		}
		if (getValue(xInt+1,yInt,xLevel,yLevel,xLevH, yLevH) >= 1)
		{
			newMark|=2;
		}
		if (getValue(xInt,yInt-1,xLevel,yLevel,xLevH, yLevH) >= 1)
		{
			newMark|=4;
		}
		if (getValue(xInt,yInt+1,xLevel,yLevel,xLevH, yLevH) >= 1)
		{
			newMark|=8;
		}
		//System.out.println("Mark "+newMark);
		return newMark;
	}
	
	/**
	 * Facility to find the value at a position on the map. As it is a heightmap,
	 * the the RGB values are the same, so we take the B value.
	 * @param x
	 * @param y
	 * @return the height value
	 */
	public int getValue(int x,int y,int xLevel,int yLevel,int xLevH, int yLevH)
	{
		// If out of border, no good ! :)
		if ((x < xLevel+1) || (y < yLevel+1) || (x > xLevH-1) || (y > yLevH-1))
			return 100;
		
		int rgbaValue=Colors.unpremultiply(rawRefData[y*700+x]);
		
		//System.out.println("Value "+(rgbaValue & 0xFF));
		return (rgbaValue & 0xFF);
	}
	
	/**
	 * See if a WP exists close to thie point
	 * @param currentJourney
	 * @param x
	 * @param y
	 * @return true if so
	 */
	boolean existWP(Journey currentJourney,double x,double y)
	{
		for (int iWP=0;iWP < currentJourney.size() ; iWP++)
		{
			Waypoint currentWP=currentJourney.getWP(iWP);
			if ((Math.abs(currentWP.getXWP() - x) < 0.01) && (Math.abs(currentWP.getYWP() - y) < 0.01))
				return true;
		}
		return false;
	}
	
	long newDir;
	int nbRecc;
	
	int dir0=0,dir1=0,dir2=0,dir3=0;
	
	/**
	 * Look for the next WP on the map. Recursive.
	 * @param data
	 * @param currentJourney
	 * @param xLevel
	 * @param yLevel
	 * @param xLevH
	 * @param yLevH
	 * @param x
	 * @param y
	 * @return the journey size if it is OK, -1 otherwise
	 */
	int searchNextWP(int [] data,Journey currentJourney,int xLevel,int yLevel,int xLevH, int yLevH, double x, double y)
	{
		int newMark = checkNeighbours(data,x,y,xLevel,yLevel,xLevH, yLevH);
		
		if (currentJourney.size() > 0)
		{
			newMark=currentJourney.getLast().getMark() | newMark;
			currentJourney.getLast().setMark(newMark);
		}
		
		nbRecc++;
		
		if ((currentJourney.size() > 60) || (nbRecc > 200))
			return currentJourney.size();
		
		
		/**
		 * Follow a random direction
		 */

		if (Math.random() > 0.8)
		{
			newDir=(long)Math.floor(Math.random()*4);
			System.out.println("New dir "+newDir);
		}
		
		if (newDir == 0)
		{
			dir0++;
			if ((newMark & 1) == 0)
			{
				if (!existWP(currentJourney,x-1,y))
				{
					Waypoint newWP=new Waypoint(x-1,y,0);
					newWP.setMark(2);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x-1,y));
				}
			}
			if ((newMark & 2) == 0)
			{
				if (!existWP(currentJourney,x+1,y))
				{
					Waypoint newWP=new Waypoint(x+1,y,0);
					newWP.setMark(1);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x+1,y));
				}
			}
			if ((newMark & 4) == 0)
			{
				if (!existWP(currentJourney,x,y-1))
				{
					Waypoint newWP=new Waypoint(x,y-1,0);
					newWP.setMark(8);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y-1));
				}
			}
			if ((newMark & 8) == 0)
			{
				if (!existWP(currentJourney,x,y+1))
				{
					Waypoint newWP=new Waypoint(x,y+1,0);
					newWP.setMark(4);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y+1));
				}
			}
		}
		else if (newDir == 1)
		{
			dir1++;
			if ((newMark & 2) == 0)
			{
				if (!existWP(currentJourney,x+1,y))
				{
					Waypoint newWP=new Waypoint(x+1,y,0);
					newWP.setMark(1);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x+1,y));
				}
			}
			if ((newMark & 4) == 0)
			{
				if (!existWP(currentJourney,x,y-1))
				{
					Waypoint newWP=new Waypoint(x,y-1,0);
					newWP.setMark(8);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y-1));
				}
			}
			if ((newMark & 8) == 0)
			{
				if (!existWP(currentJourney,x,y+1))
				{
					Waypoint newWP=new Waypoint(x,y+1,0);
					newWP.setMark(4);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y+1));
				}
			}
			if ((newMark & 1) == 0)
			{
				if (!existWP(currentJourney,x-1,y))
				{
					Waypoint newWP=new Waypoint(x-1,y,0);
					newWP.setMark(2);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x-1,y));
				}
			}
		}
		else if (newDir == 2)
		{
			dir2++;
			if ((newMark & 4) == 0)
			{
				if (!existWP(currentJourney,x,y-1))
				{
					Waypoint newWP=new Waypoint(x,y-1,0);
					newWP.setMark(8);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y-1));
				}
			}
			if ((newMark & 8) == 0)
			{
				if (!existWP(currentJourney,x,y+1))
				{
					Waypoint newWP=new Waypoint(x,y+1,0);
					newWP.setMark(4);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y+1));
				}
			}
			if ((newMark & 1) == 0)
			{
				if (!existWP(currentJourney,x-1,y))
				{
					Waypoint newWP=new Waypoint(x-1,y,0);
					newWP.setMark(2);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x-1,y));
				}
			}
			if ((newMark & 2) == 0)
			{
				if (!existWP(currentJourney,x+1,y))
				{
					Waypoint newWP=new Waypoint(x+1,y,0);
					newWP.setMark(1);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x+1,y));
				}
			}
		}
		else if (newDir == 3)
		{
			dir3++;
			if ((newMark & 8) == 0)
			{
				if (!existWP(currentJourney,x,y+1))
				{
					Waypoint newWP=new Waypoint(x,y+1,0);
					newWP.setMark(4);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y+1));
				}
			}
			if ((newMark & 1) == 0)
			{
				if (!existWP(currentJourney,x-1,y))
				{
					Waypoint newWP=new Waypoint(x-1,y,0);
					newWP.setMark(2);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x-1,y));
				}
			}
			if ((newMark & 2) == 0)
			{
				if (!existWP(currentJourney,x+1,y))
				{
					Waypoint newWP=new Waypoint(x+1,y,0);
					newWP.setMark(1);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x+1,y));
				}
			}
			if ((newMark & 4) == 0)
			{
				if (!existWP(currentJourney,x,y-1))
				{
					Waypoint newWP=new Waypoint(x,y-1,0);
					newWP.setMark(8);
					currentJourney.addWP(newWP);
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y-1));
				}
			}
		}
		int tot=dir0+dir1+dir2+dir3;
		System.out.println("Distribution of direction "+dir0*100/tot+" - "+dir1*100/tot+" - "+dir2*100/tot+" - "+dir3*100/tot+" - ");
		// If no path available from this point, look for previous WP.
		
		if ((currentJourney.size() > 60) || (nbRecc > 200))
			return currentJourney.size();
		
		{
			boolean isWP=true;
			Waypoint previousWP=null;
			
			System.out.println("Still "+currentJourney.size()+" WPs");
			
			
			if (currentJourney.size() > 0)
			{
				previousWP=currentJourney.getLast();
			}
			else
			{
				isWP=false;
			}
			if (previousWP != null)
			{
				while ((previousWP.getMark() == 15) && isWP)
				{
					if (currentJourney.size() > 1)
					{
						currentJourney.removeWP(currentJourney.size()-1);
						previousWP=currentJourney.getLast();
					}
					else
					{
						isWP=false;
					}
				}
			}
			System.out.println("Still "+currentJourney.size()+" WPs");
			if (isWP)
			{
				// There might exist a path from this WP
				if ((previousWP.getMark() & 1) == 0)
				{
					previousWP.setMark(previousWP.getMark() | 1);
					
					Waypoint newWP=new Waypoint(x-1,y,0);
					newWP.setMark(2);
					
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x-1,y));
				}
				if ((previousWP.getMark() & 2) == 0)
				{
					previousWP.setMark(previousWP.getMark() | 2);
					
					Waypoint newWP=new Waypoint(x+1,y,0);
					newWP.setMark(1);
					
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x+1,y));
				}
				if ((previousWP.getMark() & 4) == 0)
				{
					previousWP.setMark(previousWP.getMark() | 4);
					
					Waypoint newWP=new Waypoint(x,y-1,0);
					newWP.setMark(8);
					
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y-1));
				}
				if ((previousWP.getMark() & 8) == 0)
				{
					previousWP.setMark(previousWP.getMark() | 8);
					
					Waypoint newWP=new Waypoint(x,y+1,0);
					newWP.setMark(4);
					
					return (searchNextWP(data,currentJourney,xLevel,yLevel,xLevH,yLevH,x,y+1));
				}
			}
		}
		return -1;
	}
	
	/**
	 * Using the reference map, build the global map!
	 */
	public void buildMap() {
		// Using the reference map, build the global map!

		System.out.println("Building the strategic map - " + nbEnemiesBoatCarrier);
		// The left half of the map is for the bad guys :)
		// First find a place for the bases and the carriers
		
		// For each carrier, find a place. Go from the center of the map (not exactly, so the territories overlapped), up randomly, left or right randomly with test.
		// If it is further than the coast, will be on the coast.
		
		for (;nbEnemiesBoatCarrier > 0; --nbEnemiesBoatCarrier)
		{
			int up=(myRandom.nextInt(700)) + 100;
			int left=(myRandom.nextInt(400));
			
			int realLeft=findPlaceBoatLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBoatFree(realLeft,up)))
			{
				up=(myRandom.nextInt(700)) + 100;
				left=myRandom.nextInt(400);
				
				realLeft=findPlaceBoatLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			createCarrierEnemies();
		}
		for (;nbOurBoatCarrier > 0; --nbOurBoatCarrier)
		{
			int up=myRandom.nextInt(700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createCarrierOur();
			else
				createCarrierOur2();
		}
		for (;nbAlliesBoatCarrier > 0; --nbAlliesBoatCarrier)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			createCarrierAllies();
		}
		
		// Cruisers left
		for (;nbEnemiesBoatCruiser > 0; --nbEnemiesBoatCruiser)
		{
			int up=myRandom.nextInt( 700) + 100;
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBoatLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBoatFree(realLeft,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBoatLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			createCruiserEnemies();
		}
		for (;nbOurBoatCruiser > 0; --nbOurBoatCruiser)
		{
			int up=myRandom.nextInt(800);
			int right=myRandom.nextInt(400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt(800);
				right=myRandom.nextInt(400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createCruiserOur();
			else
				createCruiserOur2();
		}
		for (;nbAlliesBoatCruiser > 0; --nbAlliesBoatCruiser)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			createCruiserAllies();
		}
		
		// Destroyer left
		for (;nbEnemiesBoatDestroyer > 0; --nbEnemiesBoatDestroyer)
		{
			int up=myRandom.nextInt( 700) + 100;
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBoatLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBoatFree(realLeft,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBoatLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			createDestroyerEnemies();
		}
		for (;nbOurBoatDestroyer > 0; --nbOurBoatDestroyer)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createDestroyerOur();
			else
				createDestroyerOur2();
		}
		for (;nbAlliesBoatDestroyer > 0; --nbAlliesBoatDestroyer)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			createDestroyerAllies();
		}
		
		
		// Destroyer left
		for (;nbEnemiesBoatFrigate > 0; --nbEnemiesBoatFrigate)
		{
			int up=myRandom.nextInt( 700) + 100;
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBoatLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBoatFree(realLeft,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBoatLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			createFrigateEnemies();
		}
		for (;nbOurBoatFrigate > 0; --nbOurBoatFrigate)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createFrigateOur();
			else
				createFrigateOur2();
		}
		for (;nbAlliesBoatFrigate > 0; --nbAlliesBoatFrigate)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			createFrigateAllies();
		}
		
		
		// Nuke subs
		for (;nbEnemiesSubNuke > 0; --nbEnemiesSubNuke)
		{
			int up=myRandom.nextInt( 700) + 100;
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBoatLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBoatFree(realLeft,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBoatLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			createSubEnemies(Submarine.NUKE);
		}
		for (;nbOurSubNuke > 0; --nbOurSubNuke)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			createSubOur(Submarine.NUKE);

		}
		for (;nbAlliesSubNuke > 0; --nbAlliesSubNuke)
		{
			int up=myRandom.nextInt( 700) + 100;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBoatRight(up,right);
			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBoatRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			createSubAllies(Submarine.NUKE);
		}
		
		// Small bases
		for (;nbEnemiesSmallBases > 0; --nbEnemiesSmallBases)
		{
			int up=myRandom.nextInt( 700) + 100;
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBaseLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBaseFree(realLeft,up)))
			{
				up=myRandom.nextInt( 700) + 100;
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBaseLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			if (Math.random() > 0.5)
				createSmallBaseEnemies();
			else
				createSmallBaseEnemies2();
		}
		for (;nbOurSmallBases > 0; --nbOurSmallBases)
		{
			int up=myRandom.nextInt( 400)+300;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBaseRight(up,right);
			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
			{
				up=myRandom.nextInt( 400)+300;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBaseRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createSmallBaseOur();
			else
				createSmallBaseOur2();
		}
		for (;nbAlliesSmallBases > 0; --nbAlliesSmallBases)
		{
			int up=myRandom.nextInt( 400);
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBaseRight(up,right);
			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
			{
				up=myRandom.nextInt( 400);
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBaseRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createSmallBaseAllies();
			else
				createSmallBaseAllies2();
		}
		// Big bases
		for (;nbEnemiesBigBases > 0; --nbEnemiesBigBases)
		{
			int up=myRandom.nextInt( 800);
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBaseLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBaseFree(realLeft,up)))
			{
				up=myRandom.nextInt( 800);
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBaseLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			if (Math.random() > 0.5)
				createBigBaseEnemies();
			else
				createBigBaseEnemies2();
		}
		for (;nbOurBigBases > 0; --nbOurBigBases)
		{
			int up=myRandom.nextInt( 400)+400;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBaseRight(up,right);
			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
			{
				up=myRandom.nextInt( 400)+400;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBaseRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createBigBaseOur();
			else
				createBigBaseOur2();
		}
		for (;nbAlliesBigBases > 0; --nbAlliesBigBases)
		{
			int up=myRandom.nextInt( 400);
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBaseRight(up,right);
			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
			{
				up=myRandom.nextInt( 400);
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBaseRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createBigBaseAllies();
			else
				createBigBaseAllies2();
		}
		
		// Main bases
		for (;nbEnemiesMainBases > 0; --nbEnemiesMainBases)
		{
			int up=myRandom.nextInt( 800);
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBaseLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBaseFree(realLeft,up)))
			{
				up=myRandom.nextInt( 800);
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBaseLeft(up,left);
			}
			// Should be done !
			yCurr=up;
			xCurr=realLeft;
			if (Math.random() > 0.5)
				createMainBaseEnemies();
			else
				createMainBaseEnemies2();
		}
		for (;nbOurMainBases > 0; --nbOurMainBases)
		{
			int up=myRandom.nextInt( 400)+400;
			int right=myRandom.nextInt( 400);
			
			int realRight=findPlaceBaseRight(up,right);
			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
			{
				up=myRandom.nextInt( 400)+400;
				right=myRandom.nextInt( 400);
				
				realRight=findPlaceBaseRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createMainBaseOur();
			else
				createMainBaseOur2();
		}
		for (;nbAlliesMainBases > 0; --nbAlliesMainBases)
		{
			int up=myRandom.nextInt(400);
			int right=myRandom.nextInt(400);
			
			int realRight=findPlaceBaseRight(up,right);
			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
			{
				up=myRandom.nextInt(400);
				right=myRandom.nextInt(400);
				
				realRight=findPlaceBaseRight(up,right);
			}
			// Should be done !
			yCurr=up;
			xCurr=realRight;
			if (Math.random() > 0.5)
				createMainBaseAllies();
			else
				createMainBaseAllies2();
		}
		isInitialised=true;
	}

	/**
	 * Check if a path exist on the sea
	 * @param data
	 * @param x
	 * @param y
	 * @return true if ol
	 */
	private boolean isPath(int [] data, int x,int y)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		boolean isClear=false;
		
		int xTmp=x-1;
		int yTmp=y;
		
		int lengthPath=0;
		// Test horizontally left.
		if (x > 6)
		{
			int rgbaValue=data[yTmp*700+xTmp];
			
			int returnValue=rgbaValue & 0xFF;
			while ((returnValue == 0) && (xTmp > 0))
			{
				xTmp--;
				rgbaValue=data[yTmp*700+xTmp];
				returnValue=rgbaValue>>24;
				
				lengthPath++;
			}
			if (lengthPath > 5)
			{
				// Big enough !!!
				return true;
			}
		}
		// Test horizontally right.
		xTmp=x+1;
		if (x < 693)
		{
			int rgbaValue=data[yTmp*700+xTmp];
			
			int returnValue=rgbaValue & 0xFF;
			while ((returnValue == 0) && (xTmp < 699))
			{
				xTmp++;
				rgbaValue=data[yTmp*700+xTmp];
				returnValue=rgbaValue & 0xFF;
				
				lengthPath++;
			}
			if (lengthPath > 5)
			{
				// Big enough !!!
				return true;
			}
		}
		// Test vertically up.
		xTmp=x;
		yTmp=y-1;
		if (y > 6)
		{
			int rgbaValue=data[yTmp*700+xTmp];
			
			int returnValue=rgbaValue & 0xFF;
			while ((returnValue == 0) && (yTmp > 0))
			{
				--yTmp;
				rgbaValue=data[yTmp*700+xTmp];
				returnValue=rgbaValue & 0xFF;
				
				lengthPath++;
			}
			if (lengthPath > 6)
			{
				// Big enough !!!
				return true;
			}
		}
		// Test vertically down.
		xTmp=x;
		yTmp=y+1;
		if (y < 793)
		{
			int rgbaValue=data[yTmp*700+xTmp];
			
			int returnValue=rgbaValue & 0xFF;
			while ((returnValue == 0) && (yTmp < 799))
			{
				yTmp++;
				rgbaValue=data[yTmp*700+xTmp];
				returnValue=rgbaValue & 0xFF;
				
				lengthPath++;
			}
			if (lengthPath > 6)
			{
				// Big enough !!!
				return true;
			}
		}
		return isClear;
	}
	
	/**
	 * Select and add all the units from the given rectangle in selectedUnits.
	 * @param selectedUnits
	 * @param xLeft
	 * @param yUp
	 */
	public void selectUnits(ArrayList<ProgrammableUnit> selectedUnits,int xLeft,int yDown,int xRight,int yUp)
	{
		int xyTmp;
		
		if (xRight < xLeft)
		{
			xyTmp=xRight;
			xRight=xLeft;
			xLeft=xyTmp;
		}
		if (yUp < yDown)
		{
			xyTmp=yDown;
			yDown=yUp;
			yUp=xyTmp;
		}
		for (int iBoat = 0 ; iBoat < this.ourBoats.size() ; iBoat++)
		{
			Boat tmpBoat = ourBoats.get(iBoat);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}
		for (int iBoat = 0 ; iBoat < this.alliesBoats.size() ; iBoat++)
		{
			Boat tmpBoat = alliesBoats.get(iBoat);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}
//		for (int iBoat = 0 ; iBoat < this.enemiesBoats.size() ; iBoat++)
//		{
//			ProgrammableUnit tmpBoat = enemiesBoats.get(iBoat);
//			if ((tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
//					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
//			{
//				selectedUnits.add(tmpBoat);
//			}
//		}
//		for (int iSub = 0 ; iSub < this.enemiesSubs.size() ; iSub++)
//		{
//			Submarine tmpBoat = enemiesSubs.get(iSub);
//			if ((tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
//					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
//			{
//				selectedUnits.add(tmpBoat);
//			}
//		}
		
		for (int iSub = 0 ; iSub < this.ourSubs.size() ; iSub++)
		{
			Submarine tmpBoat = ourSubs.get(iSub);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}
		
		for (int iSub = 0 ; iSub < this.alliesSubs.size() ; iSub++)
		{
			Submarine tmpBoat = alliesSubs.get(iSub);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}

		for (int iBase = 0 ; iBase < this.ourBases.size() ; iBase++)
		{
			Base tmpBase = ourBases.get(iBase);
			if ((!tmpBase.isDead()) && (tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
					&& (tmpBase.getYMap() > yDown) && (tmpBase.getYMap() < yUp))
			{
				selectedUnits.add(tmpBase);
				tmpBase.pushSelected();
			}
			else
				tmpBase.unselect();
		}
		for (int iBase = 0 ; iBase < this.alliesBases.size() ; iBase++)
		{
			Base tmpBase = alliesBases.get(iBase);
			if ((!tmpBase.isDead()) && (tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
					&& (tmpBase.getYMap() > yDown) && (tmpBase.getYMap() < yUp))
			{
				selectedUnits.add(tmpBase);
				tmpBase.pushSelected();
			}
			else
				tmpBase.unselect();
		}
//		for (int iBase = 0 ; iBase < this.enemiesBases.size() ; iBase++)
//		{
//			Base tmpBase = enemiesBases.get(iBase);
//			if ((tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
//					&& (tmpBase.getYMap() > yDown) && (tmpBase.getYMap() < yUp))
//			{
//				selectedUnits.add(tmpBase);
//			}
//		}
	}
	
	/**
	 * Select and add units in selectedUnits if the unit is close enough to the coords.
	 * @return boolean true if one unit has been selected, false otherwise
	 * @param selectedUnits
	 * @param xWide
	 * @param yUp
	 */
	public boolean selectUnit(ArrayList<ProgrammableUnit> selectedUnits,int x,int y)
	{
		int xyTmp;
		boolean firstFound=true;
		int xRight=x+4;
		int xLeft=x-4;
		int yUp=y+4;
		int yDown=y-4;
		
		for (int iBoat = 0 ; iBoat < this.ourBoats.size() ; iBoat++)
		{
			Boat tmpBoat = ourBoats.get(iBoat);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				if (firstFound)
				{
					selectedUnits.clear();
					firstFound=false;
				}
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}
		for (int iBoat = 0 ; iBoat < this.alliesBoats.size() ; iBoat++)
		{
			Boat tmpBoat = alliesBoats.get(iBoat);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				if (firstFound)
				{
					selectedUnits.clear();
					firstFound=false;
				}
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}
//		for (int iBoat = 0 ; iBoat < this.enemiesBoats.size() ; iBoat++)
//		{
//			ProgrammableUnit tmpBoat = enemiesBoats.get(iBoat);
//			if ((tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
//					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
//			{
//				if (firstFound)
//				{
//					selectedUnits.clear();
//					firstFound=false;
//				}
//				selectedUnits.add(tmpBoat);
//			}
//		}
//		for (int iSub = 0 ; iSub < this.enemiesSubs.size() ; iSub++)
//		{
//			Submarine tmpBoat = enemiesSubs.get(iSub);
//			if ((tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
//					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
//			{
//				if (firstFound)
//				{
//					selectedUnits.clear();
//					firstFound=false;
//				}
//				selectedUnits.add(tmpBoat);
//			}
//		}
		
		for (int iSub = 0 ; iSub < this.ourSubs.size() ; iSub++)
		{
			Submarine tmpBoat = ourSubs.get(iSub);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				if (firstFound)
				{
					selectedUnits.clear();
					firstFound=false;
				}
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}
		
		for (int iSub = 0 ; iSub < this.alliesSubs.size() ; iSub++)
		{
			Submarine tmpBoat = alliesSubs.get(iSub);
			if ((!tmpBoat.isDead()) && (tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
					&& (tmpBoat.getYMap() > yDown) && (tmpBoat.getYMap() < yUp))
			{
				if (firstFound)
				{
					selectedUnits.clear();
					firstFound=false;
				}
				selectedUnits.add(tmpBoat);
				tmpBoat.pushSelected();
			}
			else
				tmpBoat.unselect();
		}

		for (int iBase = 0 ; iBase < this.ourBases.size() ; iBase++)
		{
			Base tmpBase = ourBases.get(iBase);
			if ((!tmpBase.isDead()) && (tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
					&& (tmpBase.getYMap() > yDown) && (tmpBase.getYMap() < yUp))
			{
				if (firstFound)
				{
					selectedUnits.clear();
					firstFound=false;
				}
				selectedUnits.add(tmpBase);
				tmpBase.pushSelected();
			}
			else
				tmpBase.unselect();
		}
		for (int iBase = 0 ; iBase < this.alliesBases.size() ; iBase++)
		{
			Base tmpBase = alliesBases.get(iBase);
			if ((!tmpBase.isDead()) && (tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
					&& (tmpBase.getYMap() > yDown) && (tmpBase.getYMap() < yUp))
			{
				if (firstFound)
				{
					selectedUnits.clear();
					firstFound=false;
				}
				selectedUnits.add(tmpBase);
				tmpBase.pushSelected();
			}
			else
				tmpBase.unselect();
		}
//		for (int iBase = 0 ; iBase < this.enemiesBases.size() ; iBase++)
//		{
//			Base tmpBase = enemiesBases.get(iBase);
//			if ((tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
//					&& (tmpBase.getYMap() > yDown) && (tmpBase.getYMap() < yUp))
//			{
//				if (firstFound)
//				{
//					selectedUnits.clear();
//					firstFound=false;
//				}
//				selectedUnits.add(tmpBase);
//			}
//		}
		return !firstFound;
	}

	public void updateUnits(double time)
	{
		for (int iBoat = 0 ; iBoat < this.ourBoats.size() ; iBoat++)
		{
			ProgrammableUnit tmpBoat = ourBoats.get(iBoat);
			tmpBoat.doUpdateSM(time);
		}
		for (int iBoat = 0 ; iBoat < this.alliesBoats.size() ; iBoat++)
		{
			ProgrammableUnit tmpBoat = alliesBoats.get(iBoat);
			tmpBoat.doUpdateSM(time);
		}
		for (int iBoat = 0 ; iBoat < this.enemiesBoats.size() ; iBoat++)
		{
			ProgrammableUnit tmpBoat = enemiesBoats.get(iBoat);
			tmpBoat.doUpdateSM(time);
		}
		for (int iSub = 0 ; iSub < this.enemiesSubs.size() ; iSub++)
		{
			Submarine tmpBoat = enemiesSubs.get(iSub);
			tmpBoat.doUpdateSM(time);
		}
		
		for (int iSub = 0 ; iSub < this.ourSubs.size() ; iSub++)
		{
			Submarine tmpBoat = ourSubs.get(iSub);
			tmpBoat.doUpdateSM(time);
		}
		
		for (int iSub = 0 ; iSub < this.alliesSubs.size() ; iSub++)
		{
			Submarine tmpBoat = alliesSubs.get(iSub);
			tmpBoat.doUpdateSM(time);
		}

		for (int iBase = 0 ; iBase < this.ourBases.size() ; iBase++)
		{
			ProgrammableUnit tmpBase = ourBases.get(iBase);
			tmpBase.doUpdateSM(time);
		}
		for (int iBase = 0 ; iBase < this.alliesBases.size() ; iBase++)
		{
			ProgrammableUnit tmpBase = alliesBases.get(iBase);
			tmpBase.doUpdateSM(time);
		}
		for (int iBase = 0 ; iBase < this.enemiesBases.size() ; iBase++)
		{
			ProgrammableUnit tmpBase = enemiesBases.get(iBase);
			tmpBase.doUpdateSM(time);
		}
	}
	
	/**
	 * Check if it is ok to play here!
	 * Nothing here yet...
	 */
	public boolean checkLevel()
	{
		return true;
	}
	
	public void buildLevel(LevelMap theTMap,LevelGenerated theLevelToGenerate)
	{
		int xMin;
		int yMin; // Where do we start on the globalMapImage
		
		// For test first !!!
		xMin= LevelKeeper.getInstance().getNextMapX(); // (int )(Math.random()*664);//420;
		yMin= LevelKeeper.getInstance().getNextMapY();
		
		//CoreImage globalMapImage = CoreImage.load("MapRef.png");

		//int[] rawRefData = globalMapImage.getData();

		for (int iBoat = 0 ; iBoat < this.ourBoats.size() ; iBoat++)
		{
			Boat tmpBoat = ourBoats.get(iBoat);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40)) && !tmpBoat.isDead())
			{
				// One boat on the level
				Sonar ourFrontSonar;
				ourFrontSonar= new Sonar(theTMap,tmpBoat);
				ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
				ourFrontSonar.setPosAttach(40, 0, 0);
				ourFrontSonar.activate();

				Radar ourRadar;
				ourRadar= new Radar(theTMap,tmpBoat);
				ourRadar.createGfx(0, 0, 0, 0, 1, 1);
				
				if (tmpBoat.getType() == Boat.CARRIER)
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

				tmpBoat.getSensors().add(ourFrontSonar);
				tmpBoat.addAttachedObject(ourFrontSonar);
				tmpBoat.getSensors().add(ourRadar);
				tmpBoat.addAttachedObject(ourRadar);
				tmpBoat.createGfx( tmpBoat.getPosX(), tmpBoat.getPosY(), 0, 0, 0);
				tmpBoat.setProgrammedWPs(createWPs(xMin,yMin, Math.round(tmpBoat.getXMap()), Math.round(tmpBoat.getYMap())));
				tmpBoat.setView(false);

				
				KnownDatas ourKD;

				ourKD = new KnownDatas(theTMap);
				tmpBoat.getSensors().add(ourKD);

				tmpBoat.setPosX((tmpBoat.getXMap()-xMin)*DrawMap.step);
				tmpBoat.setPosY((tmpBoat.getYMap()-yMin)*DrawMap.step);
				tmpBoat.setTheMap(theTMap);

				theTMap.addBoat(tmpBoat);
			}
		}
		for (int iBoat = 0 ; iBoat < this.alliesBoats.size() ; iBoat++)
		{
			Boat tmpBoat = alliesBoats.get(iBoat);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				Sonar ourFrontSonar;
				ourFrontSonar= new Sonar(theTMap,tmpBoat);
				ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
				ourFrontSonar.setPosAttach(40, 0, 0);
				ourFrontSonar.activate();

				Radar ourRadar;
				ourRadar= new Radar(theTMap,tmpBoat);
				ourRadar.createGfx(0, 0, 0, 0, 1, 1);
				if (tmpBoat.getType() == Boat.CARRIER)
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

				tmpBoat.getSensors().add(ourFrontSonar);
				tmpBoat.addAttachedObject(ourFrontSonar);
				tmpBoat.getSensors().add(ourRadar);
				tmpBoat.addAttachedObject(ourRadar);
				tmpBoat.createGfx( tmpBoat.getPosX(), tmpBoat.getPosY(), 0, 0, 0);
				tmpBoat.setProgrammedWPs(createWPs(xMin,yMin, tmpBoat.getXMap(), tmpBoat.getYMap()));
				tmpBoat.setView(false);

				KnownDatas ourKD;

				ourKD = new KnownDatas(theTMap);
				tmpBoat.getSensors().add(ourKD);

				tmpBoat.setPosX((tmpBoat.getXMap()-xMin)*DrawMap.step);
				tmpBoat.setPosY((tmpBoat.getYMap()-yMin)*DrawMap.step);
				tmpBoat.setTheMap(theTMap);

				theTMap.addBoat(tmpBoat);
			}
		}
		for (int iBoat = 0 ; iBoat < this.enemiesBoats.size() ; iBoat++)
		{
			Boat tmpBoat = enemiesBoats.get(iBoat);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				Sonar ourFrontSonar;
				ourFrontSonar= new Sonar(theTMap,tmpBoat);
				ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
				ourFrontSonar.setPosAttach(40, 0, 0);
				if (Math.random() > 0.6)
					ourFrontSonar.activate();
				else
					ourFrontSonar.desactivate();

				Radar ourRadar;
				ourRadar= new Radar(theTMap,tmpBoat);
				ourRadar.createGfx(0, 0, 0, 0, 1, 1);
				if (tmpBoat.getType() == Boat.CARRIER)
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

				tmpBoat.getSensors().add(ourFrontSonar);
				tmpBoat.addAttachedObject(ourFrontSonar);
				tmpBoat.getSensors().add(ourRadar);
				tmpBoat.addAttachedObject(ourRadar);
				tmpBoat.createGfx( tmpBoat.getPosX(), tmpBoat.getPosY(), 0, 0, 0);
				tmpBoat.setProgrammedWPs(createWPs(xMin,yMin, tmpBoat.getXMap(), tmpBoat.getYMap()));
				tmpBoat.setLoop(true);
				tmpBoat.setView(false);
				tmpBoat.hideMe();

				KnownDatas ourKD;

				ourKD = new KnownDatas(theTMap);
				tmpBoat.getSensors().add(ourKD);

				tmpBoat.setPosX((tmpBoat.getXMap()-xMin)*DrawMap.step);
				tmpBoat.setPosY((tmpBoat.getYMap()-yMin)*DrawMap.step);
				tmpBoat.setTheMap(theTMap);
				
				theTMap.addBoat(tmpBoat);
			}
		}
		for (int iSub = 0 ; iSub < this.enemiesSubs.size() ; iSub++)
		{
			Submarine tmpBoat = enemiesSubs.get(iSub);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				Sonar ourFrontSonar;
				ourFrontSonar= new Sonar(theTMap,tmpBoat);
				ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
				ourFrontSonar.setPosAttach(40, 0, 0);
				if (Math.random() > 0.6)
					ourFrontSonar.activate();
				else
					ourFrontSonar.desactivate();

				Radar ourRadar;
				ourRadar= new Radar(theTMap,tmpBoat);
				ourRadar.createGfx(0, 0, 0, 0, 1, 1);
				ourRadar.setPosAttach(0, 0, 0);
				ourRadar.setPower(5);
				ourRadar.setSpeedRot(40);
				ourRadar.desactivate();
				ourRadar.setDebugView(false);

				tmpBoat.getSensors().add(ourFrontSonar);
				tmpBoat.addAttachedObject(ourFrontSonar);
				tmpBoat.getSensors().add(ourRadar);
				tmpBoat.addAttachedObject(ourRadar);
				tmpBoat.createGfx( tmpBoat.getPosX(), tmpBoat.getPosY(), 0, 0, 0);
				tmpBoat.setProgrammedWPs(createWPs(xMin,yMin, tmpBoat.getXMap(), tmpBoat.getYMap()));
				tmpBoat.setLoop(true);
				tmpBoat.setView(false);
				tmpBoat.hideMe();

				KnownDatas ourKD;

				ourKD = new KnownDatas(theTMap);
				tmpBoat.getSensors().add(ourKD);

				tmpBoat.setPosX((tmpBoat.getXMap()-xMin)*DrawMap.step);
				tmpBoat.setPosY((tmpBoat.getYMap()-yMin)*DrawMap.step);
				tmpBoat.setTheMap(theTMap);

				theTMap.addSub(tmpBoat);
			}
		}
		
		for (int iSub = 0 ; iSub < this.ourSubs.size() ; iSub++)
		{
			Submarine tmpBoat = ourSubs.get(iSub);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				Sonar ourFrontSonar;
				ourFrontSonar= new Sonar(theTMap,tmpBoat);
				ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
				ourFrontSonar.setPosAttach(40, 0, 0);
				ourFrontSonar.activate();

				Radar ourRadar;
				ourRadar= new Radar(theTMap,tmpBoat);
				ourRadar.createGfx(0, 0, 0, 0, 1, 1);
				ourRadar.setPosAttach(0, 0, 0);
				ourRadar.setPower(5);
				ourRadar.setSpeedRot(40);
				ourRadar.desactivate();
				ourRadar.setDebugView(false);

				tmpBoat.getSensors().add(ourFrontSonar);
				tmpBoat.addAttachedObject(ourFrontSonar);
				tmpBoat.getSensors().add(ourRadar);
				tmpBoat.addAttachedObject(ourRadar);
				tmpBoat.createGfx( tmpBoat.getPosX(), tmpBoat.getPosY(), 0, 0, 0);
				tmpBoat.setProgrammedWPs(createWPs(xMin,yMin, tmpBoat.getXMap(), tmpBoat.getYMap()));
				tmpBoat.setView(false);

				KnownDatas ourKD;

				ourKD = new KnownDatas(theTMap);
				tmpBoat.getSensors().add(ourKD);

				tmpBoat.setPosX((tmpBoat.getXMap()-xMin)*DrawMap.step);
				tmpBoat.setPosY((tmpBoat.getYMap()-yMin)*DrawMap.step);
				tmpBoat.setTheMap(theTMap);

				theTMap.addSub(tmpBoat);
			}
		}
		
		for (int iSub = 0 ; iSub < this.alliesSubs.size() ; iSub++)
		{
			Submarine tmpBoat = alliesSubs.get(iSub);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				Sonar ourFrontSonar;
				ourFrontSonar= new Sonar(theTMap,tmpBoat);
				ourFrontSonar.createGfx(0, 0, 0, 0, 1, 1);
				ourFrontSonar.setPosAttach(40, 0, 0);
				ourFrontSonar.activate();

				Radar ourRadar;
				ourRadar= new Radar(theTMap,tmpBoat);
				ourRadar.createGfx(0, 0, 0, 0, 1, 1);
				ourRadar.setPosAttach(0, 0, 0);
				ourRadar.setPower(5);
				ourRadar.setSpeedRot(40);
				ourRadar.desactivate();
				ourRadar.setDebugView(false);

				tmpBoat.getSensors().add(ourFrontSonar);
				tmpBoat.addAttachedObject(ourFrontSonar);
				tmpBoat.getSensors().add(ourRadar);
				tmpBoat.addAttachedObject(ourRadar);
				tmpBoat.createGfx( tmpBoat.getPosX(), tmpBoat.getPosY(), 0, 0, 0);
				tmpBoat.setProgrammedWPs(createWPs(xMin,yMin, tmpBoat.getXMap(), tmpBoat.getYMap()));
				tmpBoat.setView(false);

				KnownDatas ourKD;

				ourKD = new KnownDatas(theTMap);
				tmpBoat.getSensors().add(ourKD);

				tmpBoat.setPosX((tmpBoat.getXMap()-xMin)*DrawMap.step);
				tmpBoat.setPosY((tmpBoat.getYMap()-yMin)*DrawMap.step);
				tmpBoat.setTheMap(theTMap);

				theTMap.addSub(tmpBoat);
			}
		}

		for (int iBase = 0 ; iBase < this.ourBases.size() ; iBase++)
		{
			Base tmpBase = ourBases.get(iBase);
			if ((tmpBase.getXMap() > xMin) && (tmpBase.getXMap() < (xMin+35))
					&& (tmpBase.getYMap() > yMin) && (tmpBase.getYMap() < (yMin+40)))
			{
				// One Base on the level
				Radar ourRadar3;
				ourRadar3= new Radar(theTMap,tmpBase);
				ourRadar3.createGfx(0, 0, 0, 0, 1, 1);
				ourRadar3.setPosAttach(-28, -16, 0);
				if ((tmpBase.getType() == Base.BIGBASETWO) || (tmpBase.getType() == Base.SMALLBASETWO) || (tmpBase.getType() == Base.MAINBASETWO))
					ourRadar3.setPosAttach(-41, -11, 0);
				ourRadar3.setPower(50);
				ourRadar3.setSpeedRot(20);
				ourRadar3.activate();
				ourRadar3.setDebugView(false);

				tmpBase.setPosX((tmpBase.getXMap()-xMin)*DrawMap.step);
				tmpBase.setPosY((tmpBase.getYMap()-yMin)*DrawMap.step);
				tmpBase.setTheMap(theTMap);
				tmpBase.getSensors().add(ourRadar3);
				tmpBase.addAttachedObject(ourRadar3);
				tmpBase.createGfx( tmpBase.getPosX(), tmpBase.getPosY(), 0, 0, 0);

				theTMap.addBase(tmpBase);
			}
		}
		for (int iBase = 0 ; iBase < this.alliesBases.size() ; iBase++)
		{
			Base tmpBase = alliesBases.get(iBase);
			if ((tmpBase.getXMap() > xMin) && (tmpBase.getXMap() < (xMin+35))
					&& (tmpBase.getYMap() > yMin) && (tmpBase.getYMap() < (yMin+40)))
			{
				// One Base on the level
				Radar ourRadar3;
				ourRadar3= new Radar(theTMap,tmpBase);
				ourRadar3.createGfx(0, 0, 0, 0, 1, 1);
				ourRadar3.setPosAttach(-28, -16, 0);
				if ((tmpBase.getType() == Base.BIGBASETWO) || (tmpBase.getType() == Base.SMALLBASETWO) || (tmpBase.getType() == Base.MAINBASETWO))
					ourRadar3.setPosAttach(-41, -11, 0);
				ourRadar3.setPower(50);
				ourRadar3.setSpeedRot(20);
				ourRadar3.activate();
				ourRadar3.setDebugView(false);

				tmpBase.setPosX((tmpBase.getXMap()-xMin)*DrawMap.step);
				tmpBase.setPosY((tmpBase.getYMap()-yMin)*DrawMap.step);
				tmpBase.setTheMap(theTMap);
				tmpBase.getSensors().add(ourRadar3);
				tmpBase.addAttachedObject(ourRadar3);
				tmpBase.createGfx( tmpBase.getPosX(), tmpBase.getPosY(), 0, 0, 0);

				theTMap.addBase(tmpBase);
			}
		}
		for (int iBase = 0 ; iBase < this.enemiesBases.size() ; iBase++)
		{
			Base tmpBase = enemiesBases.get(iBase);
			if ((tmpBase.getXMap() > xMin) && (tmpBase.getXMap() < (xMin+35))
					&& (tmpBase.getYMap() > yMin) && (tmpBase.getYMap() < (yMin+40)))
			{
				// One Base on the level
				Radar ourRadar3;
				ourRadar3= new Radar(theTMap,tmpBase);
				ourRadar3.createGfx(0, 0, 0, 0, 1, 1);
				ourRadar3.setPosAttach(-28, -16, 0);
				if ((tmpBase.getType() == Base.BIGBASETWO) || (tmpBase.getType() == Base.SMALLBASETWO) || (tmpBase.getType() == Base.MAINBASETWO))
					ourRadar3.setPosAttach(-41, -11, 0);
				ourRadar3.setPower(50);
				ourRadar3.setSpeedRot(20);
				ourRadar3.activate();
				ourRadar3.setDebugView(false);

				tmpBase.setPosX((tmpBase.getXMap()-xMin)*DrawMap.step);
				tmpBase.setPosY((tmpBase.getYMap()-yMin)*DrawMap.step);
				tmpBase.setTheMap(theTMap);
				tmpBase.getSensors().add(ourRadar3);
				tmpBase.addAttachedObject(ourRadar3);
				tmpBase.createGfx( tmpBase.getPosX(), tmpBase.getPosY(), 0, 0, 0);

				theTMap.addBase(tmpBase);
			}
		}

		//rawRefData[(yMin+yCurr)*700+(xMin+xCurr)]=Colors.premultiply(rgbaValue|0xFF);

		Satellite myNewSatellite= new Satellite(theTMap);
		myNewSatellite.createGfx(100, 100, 0, Math.PI/2 + 0.1, 20, 10);

		theTMap.addGlobalSensor(myNewSatellite);
	}
	
	
	/**
	 * @param oneBoat the oneBoat to add
	 */
	public void addOurBoats(Boat oneBoat)
	{
		oneBoat.setIdMap(idMap++);
		ourBoats.add(oneBoat);
	}
	
	/**
	 * @param oneSub the oneSub to add
	 */
	public void addOurSubs(Submarine oneSub)
	{
		oneSub.setIdMap(idMap++);
		ourSubs.add(oneSub);
	}
	
	public void addSub(Submarine oneSub)
	{
		oneSub.setIdMap(idMap++);
		switch(oneSub.getTypeFaction())
		{
			case FUnit.SUB_ALLIED: 
				alliesSubs.add(oneSub);
				break;
			case FUnit.SUB_ENEMY: 
				enemiesSubs.add(oneSub);
				break;
			case FUnit.SUB_OUR: 
				ourSubs.add(oneSub);
				break;
		}
	}
	
	public void addBoat(Boat oneBoat)
	{
		oneBoat.setIdMap(idMap++);
		
		//oneBoat.createGfxSM(oneBoat.getXMap(), oneBoat.getYMap(), 0, 0, 0);
		
		switch(oneBoat.getTypeFaction())
		{
			case FUnit.BOAT_ALLIED: 
				alliesBoats.add(oneBoat);
				break;
			case FUnit.BOAT_ENEMY: 
				enemiesBoats.add(oneBoat);
				break;
			case FUnit.BOAT_OUR: 
				ourBoats.add(oneBoat);
				break;
		}
	}
	
	public void addAirplane(Airplane oneAirplane)
	{
		oneAirplane.setIdMap(idMap++);
		switch(oneAirplane.getTypeFaction())
		{
			case FUnit.AIRPLANE_ALLIED: 
				alliesAirplanes.add(oneAirplane);
				//nbTotalGood++;
				break;
			case FUnit.AIRPLANE_ENEMY: 
				enemiesAirplanes.add(oneAirplane);
				//nbEnemiesTotal++;
				break;
			case FUnit.AIRPLANE_OUR: 
				ourAirplanes.add(oneAirplane);
				//nbTotalGood++;
				break;
		}
	}
	
	public void addBase(Base oneBase)
	{
		oneBase.setIdMap(idMap++);
		//oneBase.createGfxSM(oneBase.getXMap(), oneBase.getYMap(), 0, 0, 0);
		
		switch(oneBase.getTypeFaction())
		{
			case FUnit.BASE_ALLIED: 
				alliesBases.add(oneBase);
				break;
			case FUnit.BASE_ENEMY: 
				enemiesBases.add(oneBase);
				break;
			case FUnit.BASE_OUR: 
				ourBases.add(oneBase);
				break;
		}
	}

	public boolean isInitialised() {
		return isInitialised;
	}

	public void setInitialised(boolean isInitialised) {
		this.isInitialised = isInitialised;
	}
	
	/*
	 * 
	 * Units creation
	 * 
	 */
	
	
	
	
	
	private void createCarrierEnemies()
	{
		Boat oneCarrier = new Boat();
		oneCarrier.setType(Boat.CARRIER);
		oneCarrier.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCarrier.setComplement(5000);
		oneCarrier.setComplementNorm(5000);
		GameKeeper.getInstance().addComplementEnemies(5000);
		oneCarrier.setTonnage(100000);
		oneCarrier.setCost(4000000000L);

		//GameKeeper.getInstance().addCostEnemies(4000000000L);
		oneCarrier.setMaxSpeed(35);
		oneCarrier.setStandardSpeed(20);
		oneCarrier.setNbAwacs(2);
		oneCarrier.setNbFighters(100);
		oneCarrier.setNbTankers(2);
		oneCarrier.setName("USS Bidule");
		oneCarrier.setFireAtWill(true);
		oneCarrier.setNbMissiles(100);
		oneCarrier.setNbTorpedoes(0);
		oneCarrier.setResistance(42);
		// And add the absolute coordinates.
		oneCarrier.setPosMap(xCurr, yCurr);
		oneCarrier.setPosMapTarget(xCurr+2, yCurr);
		oneCarrier.setFollowTargetMap(true);

		this.addBoat(oneCarrier);
		
		GameKeeper.getInstance().nbCarrierTotalEnemies++;
	}

	private void createCarrierOur()
	{
		// Add a carrier here !!!
		Boat oneCarrier = new Boat();
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
		oneCarrier.setName("USS Bidule");
		oneCarrier.setFireAtWill(true);
		oneCarrier.setNbMissiles(100);
		oneCarrier.setNbTorpedoes(0);
		oneCarrier.setResistance(50);
		// And add the absolute coordinates.
		oneCarrier.setPosMap(xCurr, yCurr);
		oneCarrier.setPosMapTarget(xCurr-2, yCurr);
		oneCarrier.setFollowTargetMap(true);

		GameKeeper.getInstance().nbCarrierTotalOur++;
		
		this.addBoat(oneCarrier);
	}

	private void createCarrierOur2()
	{
		// Add a carrier here !!!
		Boat oneCarrier = new Boat();
		oneCarrier.setType(Boat.CARRIER);
		oneCarrier.setTypeFaction(FUnit.BOAT_OUR);
		oneCarrier.setComplement(5000);
		oneCarrier.setComplementNorm(5000);
		GameKeeper.getInstance().addComplementOur(5000);
		oneCarrier.setTonnage(70000);
		oneCarrier.setCost(2000000000L);

		//GameKeeper.getInstance().addCostOur(2000000000L);
		oneCarrier.setMaxSpeed(35);
		oneCarrier.setStandardSpeed(20);
		oneCarrier.setNbAwacs(2);
		oneCarrier.setNbFighters(80);
		oneCarrier.setNbTankers(2);
		oneCarrier.setName("USS Bidule");
		oneCarrier.setFireAtWill(true);
		oneCarrier.setNbMissiles(100);
		oneCarrier.setNbTorpedoes(0);
		oneCarrier.setResistance(45);
		// And add the absolute coordinates.
		oneCarrier.setPosMap(xCurr, yCurr);
		oneCarrier.setPosMapTarget(xCurr-2, yCurr);
		oneCarrier.setFollowTargetMap(true);

		this.addBoat(oneCarrier);
		
		GameKeeper.getInstance().nbCarrierTotalOur++;
	}

	private void createCarrierAllies()
	{
		// Add a carrier here !!!
		Boat oneCarrier = new Boat();
		oneCarrier.setType(Boat.CARRIER);
		oneCarrier.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCarrier.setComplement(3500);
		oneCarrier.setComplementNorm(3500);
		GameKeeper.getInstance().addComplementAllies(3500);
		oneCarrier.setTonnage(60000);
		oneCarrier.setCost(2000000000L);

		//GameKeeper.getInstance().addCostAllies(2000000000L);
		oneCarrier.setMaxSpeed(40);
		oneCarrier.setStandardSpeed(20);
		oneCarrier.setNbAwacs(2);
		oneCarrier.setNbFighters(50);
		oneCarrier.setNbTankers(2);
		oneCarrier.setName("Le Royal");
		oneCarrier.setFireAtWill(true);
		oneCarrier.setNbMissiles(100);
		oneCarrier.setNbTorpedoes(0);
		oneCarrier.setResistance(40);
		// And add the absolute coordinates.
		oneCarrier.setPosMap(xCurr, yCurr);
		oneCarrier.setPosMapTarget(xCurr-2, yCurr);
		oneCarrier.setFollowTargetMap(true);

		this.addBoat(oneCarrier);
		//nbAlliesBoatCarrier--;
		GameKeeper.getInstance().nbCarrierTotalAllies++;
	}

	private void createCruiserEnemies()
	{
		Boat oneCruiser = new Boat();
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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		GameKeeper.getInstance().nbBoatTotalEnemies++;
	}

	private void createCruiserOur()
	{

		Boat oneCruiser = new Boat();
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
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(20);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalOur++;
		//nbOurBoatCarrier--;
	}

	private void createCruiserOur2()
	{
		Boat oneCruiser = new Boat();
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
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(25);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalOur++;
	}

	private void createCruiserAllies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementAllies(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		//GameKeeper.getInstance().addCostAllies(400000000L);
		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(25);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalAllies++;
	}

	
	private void createDestroyerEnemies()
	{
		Boat oneCruiser = new Boat();
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
		oneCruiser.setNbMissiles(100);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		GameKeeper.getInstance().nbBoatTotalEnemies++;
	}

	private void createDestroyerOur()
	{

		Boat oneCruiser = new Boat();
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
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(140);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(12);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalOur++;
		//nbOurBoatCarrier--;
	}

	private void createDestroyerOur2()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.DESTROYER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(120);
		oneCruiser.setComplementNorm(120);
		GameKeeper.getInstance().addComplementOur(120);
		oneCruiser.setTonnage(20000);
		oneCruiser.setCost(80000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(50);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(100);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalOur++;
	}

	private void createDestroyerAllies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.DESTROYER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(110);
		oneCruiser.setComplementNorm(110);
		GameKeeper.getInstance().addComplementAllies(110);
		oneCruiser.setTonnage(15000);
		oneCruiser.setCost(90000000L);

		//GameKeeper.getInstance().addCostAllies(400000000L);
		oneCruiser.setMaxSpeed(52);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("Le Magnifique");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(200);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(15);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalAllies++;
	}
	
	private void createFrigateEnemies()
	{
		Boat oneCruiser = new Boat();
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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		GameKeeper.getInstance().nbBoatTotalEnemies++;
	}

	private void createFrigateOur()
	{

		Boat oneCruiser = new Boat();
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
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(40);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(8);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalOur++;
		//nbOurBoatCarrier--;
	}

	private void createFrigateOur2()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(40);
		oneCruiser.setComplementNorm(40);
		GameKeeper.getInstance().addComplementOur(40);
		oneCruiser.setTonnage(10000);
		oneCruiser.setCost(8000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(47);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(80);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalOur++;
	}

	private void createFrigateAllies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(50);
		oneCruiser.setComplementNorm(50);
		GameKeeper.getInstance().addComplementAllies(50);
		oneCruiser.setTonnage(12000);
		oneCruiser.setCost(9000000L);

		//GameKeeper.getInstance().addCostAllies(400000000L);
		oneCruiser.setMaxSpeed(50);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("La Suprebe");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(80);
		oneCruiser.setNbTorpedoes(50);
		oneCruiser.setResistance(8);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		GameKeeper.getInstance().nbBoatTotalAllies++;
	}
	
	private void createSubEnemies(int type)
	{
		Submarine oneSub = new Submarine();
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
		oneSub.setPosMap(xCurr, yCurr);
		oneSub.setPosMapTarget(xCurr+2, yCurr);
		oneSub.setFollowTargetMap(true);

		this.addSub(oneSub);
		//nbEnemiesSubmarineCarrier--;
		GameKeeper.getInstance().nbSubTotalEnemies++;
	}

	private void createSubOur(int type)
	{

		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_OUR);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		GameKeeper.getInstance().addComplementOur(100);
		oneSub.setTonnage(15000);
		oneSub.setCost(4000000000L);

		//GameKeeper.getInstance().addCostOur(4000000000L);
		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		oneSub.setName("USS Bidule");
		oneSub.setFireAtWill(true);
		oneSub.setNbMissiles(20);
		oneSub.setNbTorpedoes(50);
		oneSub.setDepth(-50);
		oneSub.setWantedDepth(-50);
		oneSub.setResistance(6);
		// And add the absolute coordinates.
		oneSub.setPosMap(xCurr, yCurr);
		oneSub.setPosMapTarget(xCurr-2, yCurr);
		oneSub.setFollowTargetMap(true);

		this.addSub(oneSub);
		//nbOurSubmarineCarrier--;
		GameKeeper.getInstance().nbSubTotalOur++;
	}

	private void createSubAllies(int type)
	{
		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_ALLIED);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		GameKeeper.getInstance().addComplementAllies(5000);
		oneSub.setTonnage(70000);
		oneSub.setCost(400000000L);

		//GameKeeper.getInstance().addCostAllies(400000000L);
		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		oneSub.setName("USS Bidule");
		oneSub.setFireAtWill(true);
		oneSub.setNbMissiles(10);
		oneSub.setNbTorpedoes(40);
		oneSub.setDepth(-50);
		oneSub.setWantedDepth(-50);
		oneSub.setResistance(8);
		// And add the absolute coordinates.
		oneSub.setPosMap(xCurr, yCurr);
		oneSub.setPosMapTarget(xCurr-2, yCurr);
		oneSub.setFollowTargetMap(true);

		this.addSub(oneSub);
		
		GameKeeper.getInstance().nbSubTotalAllies++;
	}

	private void createSmallBaseEnemies()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.setComplement(500);
		oneNewSmallBase.setComplementNorm(500);
		GameKeeper.getInstance().addComplementEnemies(500);
		oneNewSmallBase.setCost(16000000);
		//GameKeeper.getInstance().addCostEnemies(16000000);
		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(100);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesSmallBases--;
		GameKeeper.getInstance().nbBasesTotalEnemies++;
	}

	private void createSmallBaseEnemies2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.setComplement(600);
		oneNewSmallBase.setComplementNorm(600);
		GameKeeper.getInstance().addComplementEnemies(600);
		oneNewSmallBase.setCost(18000000);
		//GameKeeper.getInstance().addCostEnemies(18000000);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(120);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesSmallBases--;
		GameKeeper.getInstance().nbBasesTotalEnemies++;
	}

	private void createBigBaseEnemies()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.BIGBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(2600);
		oneNewSmallBase.setComplementNorm(2600);
		GameKeeper.getInstance().addComplementEnemies(2600);
		oneNewSmallBase.setCost(58000000);
		//GameKeeper.getInstance().addCostEnemies(58000000);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(500);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(40);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesBigBases--;
		GameKeeper.getInstance().nbBasesTotalEnemies++;
	}

	private void createBigBaseEnemies2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.BIGBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(4000);
		oneNewSmallBase.setComplementNorm(4000);
		GameKeeper.getInstance().addComplementEnemies(4000);
		oneNewSmallBase.setCost(88000000);
		//GameKeeper.getInstance().addCostEnemies(88000000);
		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(800);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(40);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesBigBases--;
		GameKeeper.getInstance().nbBasesTotalEnemies++;
	}

	private void createMainBaseEnemies()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.MAINBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(10000);
		oneNewSmallBase.setComplementNorm(10000);
		GameKeeper.getInstance().addComplementEnemies(10000);
		oneNewSmallBase.setCost(324000000);
		//GameKeeper.getInstance().addCostEnemies(324000000);
		oneNewSmallBase.setNbAwacs(16);
		oneNewSmallBase.setNbFighters(2800);
		oneNewSmallBase.setNbTankers(28);
		oneNewSmallBase.setNbFightersOnFlightMax(60);
		oneNewSmallBase.setNbMissiles(16000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesMainBases--;
		GameKeeper.getInstance().nbBasesTotalEnemies++;
	}

	private void createMainBaseEnemies2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.MAINBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(11000);
		oneNewSmallBase.setComplementNorm(11000);
		GameKeeper.getInstance().addComplementEnemies(11000);
		oneNewSmallBase.setCost(344000000);
		//GameKeeper.getInstance().addCostEnemies(344000000);
		oneNewSmallBase.setNbAwacs(16);
		oneNewSmallBase.setNbFighters(3000);
		oneNewSmallBase.setNbTankers(28);
		oneNewSmallBase.setNbFightersOnFlightMax(60);
		oneNewSmallBase.setNbMissiles(18000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesMainBases--;
		GameKeeper.getInstance().nbBasesTotalEnemies++;
	}

	private void createSmallBaseOur()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_OUR);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(500);
		oneNewSmallBase.setComplementNorm(500);
		GameKeeper.getInstance().addComplementOur(500);
		oneNewSmallBase.setCost(16000000);
		//GameKeeper.getInstance().addCostOur(16000000);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(200);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		GameKeeper.getInstance().nbBasesTotalOur++;
		//nbOurSmallBases--;
	}

	private void createSmallBaseOur2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_OUR);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(600);
		oneNewSmallBase.setComplementNorm(600);
		GameKeeper.getInstance().addComplementOur(600);
		oneNewSmallBase.setCost(18000000);
		//GameKeeper.getInstance().addCostOur(18000000);
		oneNewSmallBase.setNbAwacs(3);
		oneNewSmallBase.setNbFighters(300);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		GameKeeper.getInstance().nbBasesTotalOur++;
		//nbOurSmallBases--;
	}

	private void createBigBaseOur()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.BIGBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_OUR);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(2600);
		oneNewSmallBase.setComplementNorm(2600);
		GameKeeper.getInstance().addComplementOur(2600);
		oneNewSmallBase.setCost(58000000);
		//GameKeeper.getInstance().addCostOur(58000000);
		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(800);
		oneNewSmallBase.setNbTankers(10);
		oneNewSmallBase.setNbFightersOnFlightMax(40);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		GameKeeper.getInstance().nbBasesTotalOur++;
		//nbOurBigBases--;
	}

	private void createBigBaseOur2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.BIGBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_OUR);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(4000);
		oneNewSmallBase.setComplementNorm(4000);
		GameKeeper.getInstance().addComplementOur(4000);
		oneNewSmallBase.setCost(88000000);
		//GameKeeper.getInstance().addCostOur(88000000);
		oneNewSmallBase.setNbAwacs(6);
		oneNewSmallBase.setNbFighters(1200);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(40);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		GameKeeper.getInstance().nbBasesTotalOur++;
		//nbOurBigBases--;
	}

	private void createMainBaseOur()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.MAINBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_OUR);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(10000);
		oneNewSmallBase.setComplementNorm(10000);
		GameKeeper.getInstance().addComplementOur(10000);
		oneNewSmallBase.setCost(324000000);
		//GameKeeper.getInstance().addCostOur(324000000);
		oneNewSmallBase.setNbAwacs(10);
		oneNewSmallBase.setNbFighters(1600);
		oneNewSmallBase.setNbTankers(16);
		oneNewSmallBase.setNbFightersOnFlightMax(60);
		oneNewSmallBase.setNbMissiles(16000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		GameKeeper.getInstance().nbBasesTotalOur++;
		//nbOurMainBases--;
	}

	private void createMainBaseOur2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.MAINBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_OUR);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(11000);
		oneNewSmallBase.setComplementNorm(11000);
		GameKeeper.getInstance().addComplementOur(11000);
		oneNewSmallBase.setCost(344000000);
		//GameKeeper.getInstance().addCostOur(344000000);
		oneNewSmallBase.setNbAwacs(20);
		oneNewSmallBase.setNbFighters(2600);
		oneNewSmallBase.setNbTankers(20);
		oneNewSmallBase.setNbFightersOnFlightMax(60);
		oneNewSmallBase.setNbMissiles(18000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbOurMainBases--;
		GameKeeper.getInstance().nbBasesTotalOur++;
	}

	private void createSmallBaseAllies()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ALLIED);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(500);
		oneNewSmallBase.setComplementNorm(500);
		GameKeeper.getInstance().addComplementAllies(500);
		oneNewSmallBase.setCost(16000000);
		//GameKeeper.getInstance().addCostAllies(16000000);
		oneNewSmallBase.setNbAwacs(6);
		oneNewSmallBase.setNbFighters(200);
		oneNewSmallBase.setNbTankers(4);
		oneNewSmallBase.setNbFightersOnFlightMax(20);

		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesSmallBases--;
		GameKeeper.getInstance().nbBasesTotalAllies++;
	}

	private void createSmallBaseAllies2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ALLIED);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(600);
		oneNewSmallBase.setComplementNorm(600);
		GameKeeper.getInstance().addComplementAllies(600);
		oneNewSmallBase.setCost(18000000);
		//GameKeeper.getInstance().addCostAllies(18000000);
		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(300);
		oneNewSmallBase.setNbTankers(4);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesSmallBases--;
		GameKeeper.getInstance().nbBasesTotalAllies++;
	}

	private void createBigBaseAllies()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.BIGBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ALLIED);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(2600);
		oneNewSmallBase.setComplementNorm(2600);
		GameKeeper.getInstance().addComplementAllies(2600);
		oneNewSmallBase.setCost(58000000);
		//GameKeeper.getInstance().addCostAllies(58000000);
		oneNewSmallBase.setNbAwacs(6);
		oneNewSmallBase.setNbFighters(1100);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(40);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesBigBases--;
		GameKeeper.getInstance().nbBasesTotalAllies++;
	}

	private void createBigBaseAllies2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.BIGBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ALLIED);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(4000);
		oneNewSmallBase.setComplementNorm(4000);
		GameKeeper.getInstance().addComplementAllies(4000);
		oneNewSmallBase.setCost(88000000);
		//GameKeeper.getInstance().addCostAllies(88000000);
		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(1300);
		oneNewSmallBase.setNbTankers(18);
		oneNewSmallBase.setNbFightersOnFlightMax(40);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesBigBases--;
		GameKeeper.getInstance().nbBasesTotalAllies++;
	}

	private void createMainBaseAllies()
	{
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.MAINBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ALLIED);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(10000);
		oneNewSmallBase.setComplementNorm(10000);
		GameKeeper.getInstance().addComplementAllies(10000);
		oneNewSmallBase.setCost(324000000);
		//GameKeeper.getInstance().addCostAllies(324000000);
		oneNewSmallBase.setNbAwacs(16);
		oneNewSmallBase.setNbFighters(2200);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(60);
		oneNewSmallBase.setNbMissiles(16000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesMainBases--;
		GameKeeper.getInstance().nbBasesTotalAllies++;
	}

	private void createMainBaseAllies2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.MAINBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ALLIED);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(11000);
		oneNewSmallBase.setComplementNorm(11000);
		GameKeeper.getInstance().addComplementAllies(11000);
		oneNewSmallBase.setCost(344000000);
		//GameKeeper.getInstance().addCostAllies(344000000);
		oneNewSmallBase.setNbAwacs(20);
		oneNewSmallBase.setNbFighters(2400);
		oneNewSmallBase.setNbTankers(20);
		oneNewSmallBase.setNbFightersOnFlightMax(60);
		oneNewSmallBase.setNbMissiles(18000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesMainBases--;
		GameKeeper.getInstance().nbBasesTotalAllies++;
	}

	/*
	 * Group creations
	 */
	private void addCruiserEnemies(ProgrammableUnit groupOwner)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementEnemies(5000);
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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		groupOwner.getMyGroup().add(oneCruiser);
		nbEnemiesBoatCruiser--;
		GameKeeper.getInstance().nbBoatTotalEnemies++;
	}

	private void addCruiserOur(ProgrammableUnit groupOwner)
	{

		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementOur(5000);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(20);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		groupOwner.getMyGroup().add(oneCruiser);
		nbOurBoatCruiser--;
		
		GameKeeper.getInstance().nbBoatTotalOur++;
	}

	private void addCruiserOur2(ProgrammableUnit groupOwner)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementOur(5000);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		//GameKeeper.getInstance().addCostOur(400000000L);
		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(25);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		groupOwner.getMyGroup().add(oneCruiser);
		nbOurBoatCruiser--;
		
		GameKeeper.getInstance().nbBoatTotalOur++;
	}

	private void addCruiserAllies(ProgrammableUnit groupOwner)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		GameKeeper.getInstance().addComplementAllies(5000);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		//GameKeeper.getInstance().addCostAllies(400000000L);
		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(25);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		groupOwner.getMyGroup().add(oneCruiser);
		nbAlliesBoatCruiser--;
		
		GameKeeper.getInstance().nbBoatTotalAllies++;
	}

	private void addSubEnemies(int type,ProgrammableUnit groupOwner) 
	{		
		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_ENEMY);
		switch (type)
		{
			case Submarine.NUKE:
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
				break;
			case Submarine.NUKE_SSBN:
				oneSub.setComplement(120);
				oneSub.setComplementNorm(120);
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
				break;
			case Submarine.DIESEL:
				oneSub.setComplement(40);
				oneSub.setComplementNorm(40);
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
				break;
			case Submarine.DIESEL_AIP:
				oneSub.setComplement(40);
				oneSub.setComplementNorm(40);
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
				break;
			default:
				oneSub.setComplement(100);
				oneSub.setComplementNorm(100);
				GameKeeper.getInstance().addComplementEnemies(100);
				oneSub.setTonnage(10000);
				oneSub.setCost(3400000000L);

				//GameKeeper.getInstance().addCostEnemies(3400000000L);
				oneSub.setMaxSpeed(60);
				oneSub.setStandardSpeed(30);
				oneSub.setName("USS Default");
				oneSub.setFireAtWill(true);
				oneSub.setNbMissiles(20);
				oneSub.setNbTorpedoes(40);
				oneSub.setDepth(-50);
				oneSub.setWantedDepth(-50);
				oneSub.setResistance(5);
					
		}
		
		// And add the absolute coordinates.
		oneSub.setPosMap(xCurr, yCurr);
		oneSub.setPosMapTarget(xCurr+2, yCurr);
		oneSub.setFollowTargetMap(true);
		
		GameKeeper.getInstance().nbSubTotalEnemies++;

		groupOwner.getMyGroup().add(oneSub);
	}

	private void addSubOur(int type, ProgrammableUnit groupOwner)
	{

		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_OUR);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		GameKeeper.getInstance().addComplementOur(100);
		oneSub.setTonnage(15000);
		oneSub.setCost(4000000000L);

		//GameKeeper.getInstance().addCostOur(4000000000L);
		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		oneSub.setName("USS Bidule");
		oneSub.setFireAtWill(true);
		oneSub.setNbMissiles(20);
		oneSub.setNbTorpedoes(50);
		oneSub.setDepth(-50);
		oneSub.setWantedDepth(-50);
		oneSub.setResistance(6);
		// And add the absolute coordinates.
		oneSub.setPosMap(xCurr, yCurr);
		oneSub.setPosMapTarget(xCurr-2, yCurr);
		oneSub.setFollowTargetMap(true);
		
		groupOwner.getMyGroup().add(oneSub);
		
		GameKeeper.getInstance().nbSubTotalOur++;
	}

	private void addSubAllies(int type,ProgrammableUnit groupOwner) {
		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_ALLIED);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		GameKeeper.getInstance().addComplementAllies(5000);
		oneSub.setTonnage(70000);
		oneSub.setCost(400000000L);

		//GameKeeper.getInstance().addCostAllies(400000000L);
		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		oneSub.setName("USS Bidule");
		oneSub.setFireAtWill(true);
		oneSub.setNbMissiles(10);
		oneSub.setNbTorpedoes(40);
		oneSub.setDepth(-50);
		oneSub.setWantedDepth(-50);
		oneSub.setResistance(8);
		// And add the absolute coordinates.
		oneSub.setPosMap(xCurr, yCurr);
		oneSub.setPosMapTarget(xCurr - 2, yCurr);
		oneSub.setFollowTargetMap(true);
		
		GameKeeper.getInstance().nbSubTotalAllies++;

		groupOwner.getMyGroup().add(oneSub);
	}
}
