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

import com.tgb.subgame.unitspc.PoolOfNames;
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
import com.tgb.subengine.gameentities.*;

import pulpcore.image.Colors;
import pulpcore.image.CoreImage;

/**
 * Create one level using a random creation (from the event system of MapKeeper2)
 * @author Alain Becam
 *
 */
public class RandomMapCreator {

	boolean isInitialised=false;
	
	int xCurr,yCurr;
	int step=1; // Eventually, step to navigate the map (might be used here to distribute the units, and accelerate the process.
	
	int forcesBalance=0; // The current balance between the 2 factions: -100 -> attacking their bases (deep), 0-> middle, 100-> attacking our bases
	double enemyRemaining=1; // How many forces the enemy still have (1-> full, 0->none).
	double lastEventTimeStamp=0;
	double nextEventTimeStamp=0;
	double timeTotal=0;
	
	Random myRandom;
	
	int idMap=0;
	
	boolean finished=false;
	
	boolean isBlank=true;
	
	ArrayList<Submarine> ourSubs;
	
	int nbOurSubNuke=20;//40;
	int nbOurSubSSBN=10;//20;
	int nbOurSubDiesel=0;
	int nbOurSubDieselAIP=10;
	
	// The *Max are here to reset the value. So it's better to set the *Max then reset to
	// change the value for a new game
	int nbOurSubNukeMax=20;//40;
	int nbOurSubSSBNMax=10;//20;
	int nbOurSubDieselMax=0;
	int nbOurSubDieselAIPMax=10;
	
	ArrayList<Boat> ourBoats;
	
	int nbOurBoatCarrier=10;
	int nbOurBoatFrigate=25;//50;
	int nbOurBoatDestroyer=10;//20;
	int nbOurBoatCruiser=10;
	int nbOurBoatCorvette=50;//100;
	int nbOurBoatAmphibious=8;
	
	int nbOurBoatCarrierMax=10;
	int nbOurBoatFrigateMax=25;//50;
	int nbOurBoatDestroyerMax=10;//20;
	int nbOurBoatCruiserMax=10;
	int nbOurBoatCorvetteMax=50;//100;
	int nbOurBoatAmphibiousMax=8;
	
	ArrayList<Submarine> alliesSubs;
	
	int nbAlliesSubNuke=10;//20;
	int nbAlliesSubSSBN=5;//10;
	int nbAlliesSubDiesel=30;
	int nbAlliesSubDieselAIP=20;	
	
	int nbAlliesSubNukeMax=10;//20;
	int nbAlliesSubSSBNMax=5;//10;
	int nbAlliesSubDieselMax=30;
	int nbAlliesSubDieselAIPMax=20;	
	
	ArrayList<Boat> alliesBoats;
	
	int nbAlliesBoatCarrier=5;//10;
	int nbAlliesBoatFrigate=20;//40;
	int nbAlliesBoatDestroyer=15;
	int nbAlliesBoatCruiser=10;
	int nbAlliesBoatCorvette=40;//100;
	int nbAlliesBoatAmphibious=10;
	
	int nbAlliesBoatCarrierMax=5;//10;
	int nbAlliesBoatFrigateMax=20;//40;
	int nbAlliesBoatDestroyerMax=15;
	int nbAlliesBoatCruiserMax=10;
	int nbAlliesBoatCorvetteMax=40;//100;
	int nbAlliesBoatAmphibiousMax=10;
	
	ArrayList<Submarine> enemiesSubs;
	
	int nbEnemiesSubNuke=30;
	int nbEnemiesSubSSBN=10;//20;
	int nbEnemiesSubDiesel=40;
	int nbEnemiesSubDieselAIP=10;
	
	int nbEnemiesSubNukeMax=30;
	int nbEnemiesSubSSBNMax=10;//20;
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
	int nbOurSmallBases=15;//20;
	int nbOurBigBases=5;//10;
	int nbOurMainBases=2;
	
	int nbOurSmallBasesMax=12;//20;
	int nbOurBigBasesMax=5;//10;
	int nbOurMainBasesMax=2;
	ArrayList<Base> alliesBases;
	int nbAlliesSmallBases=10;//18;
	int nbAlliesBigBases=4;//8;
	int nbAlliesMainBases=1;
	
	int nbAlliesSmallBasesMax=10;//18;
	int nbAlliesBigBasesMax=4;//8;
	int nbAlliesMainBasesMax=1;
	ArrayList<Base> enemiesBases;
	int nbEnemiesSmallBases=15;//20;
	int nbEnemiesBigBases=10;//15;
	int nbEnemiesMainBases=3;//10
	
	int nbEnemiesSmallBasesMax=15;//50;
	int nbEnemiesBigBasesMax=7;//20;
	int nbEnemiesMainBasesMax=3;
	
	boolean CarrierGroupsBuilt=false;
	
	ArrayList<Base> neutralBases;
	
	ArrayList<MapEvents> ourEvents;
	
	StrategicMap theStrategicMap;
	
	static private RandomMapCreator instance=null;
	
	int[] rawRefData=null;
	
	private RandomMapCreator()
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
		
		ourEvents = new ArrayList<MapEvents> ();
		
		gfxSprites.callMeFirst();
		gfxSelections.callMeFirst();
		
		CoreImage globalMapImage = CoreImage.load("MapRef.png");

		rawRefData = globalMapImage.getData();
		
		myRandom = new Random();
	}

	public static RandomMapCreator getInstance()
	{
		if (instance== null)
			instance = new RandomMapCreator();
		
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
	public void resetAll()
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
		resetNbOfUnits();
		System.out.println("RandomMapCreator resetted");
	}
	
	public void resetNbOfUnits()
	{
		
		// The *Max are here to reset the value. So it's better to set the *Max then reset to
		// change the value for a new game
		
		nbOurSubNuke=nbOurSubNukeMax;//40;
		nbOurSubSSBN=nbOurSubSSBNMax;//20;
		nbOurSubDiesel=nbOurSubDieselMax;
		nbOurSubDieselAIP=nbOurSubDieselAIPMax;
		
		nbOurBoatCarrier=nbOurBoatCarrierMax;
		nbOurBoatFrigate=nbOurBoatFrigateMax;//50;
		nbOurBoatDestroyer=nbOurBoatDestroyerMax;//20;
		nbOurBoatCruiser=nbOurBoatCruiserMax;
		nbOurBoatCorvette=nbOurBoatCorvetteMax;//100;
		nbOurBoatAmphibious=nbOurBoatAmphibiousMax;
		
		nbAlliesSubNuke=nbAlliesSubNukeMax;//20;
		nbAlliesSubSSBN=nbAlliesSubSSBNMax;//10;
		nbAlliesSubDiesel=nbAlliesSubDieselMax;
		nbAlliesSubDieselAIP=nbAlliesSubDieselAIPMax;	
		
		nbAlliesBoatCarrier=nbAlliesBoatCarrierMax;//10;
		nbAlliesBoatFrigate=nbAlliesBoatFrigateMax;//40;
		nbAlliesBoatDestroyer=nbAlliesBoatDestroyerMax;
		nbAlliesBoatCruiser=nbAlliesBoatCruiserMax;
		nbAlliesBoatCorvette=nbAlliesBoatCorvetteMax;//100;
		nbAlliesBoatAmphibious=nbAlliesBoatAmphibiousMax;

		nbEnemiesSubNuke=nbEnemiesSubNukeMax;
		nbEnemiesSubSSBN=nbEnemiesSubSSBNMax;//20;
		nbEnemiesSubDiesel=nbEnemiesSubDieselMax;
		nbEnemiesSubDieselAIP=nbEnemiesSubDieselAIPMax;
		
		
		nbEnemiesBoatCarrier=nbEnemiesBoatCarrierMax;
		nbEnemiesBoatFrigate=nbEnemiesBoatFrigateMax;
		nbEnemiesBoatDestroyer=nbEnemiesBoatDestroyerMax;
		nbEnemiesBoatCruiser=nbEnemiesBoatCruiserMax;
		nbEnemiesBoatCorvette=nbEnemiesBoatCorvetteMax;
		nbEnemiesBoatAmphibious=nbEnemiesBoatAmphibiousMax;
		
		nbOurSmallBases=nbOurSmallBasesMax;
		nbOurBigBases=nbOurBigBasesMax;
		nbOurMainBases=nbOurMainBasesMax;
		
		nbAlliesSmallBases=nbAlliesSmallBasesMax;
		nbAlliesBigBases=nbAlliesBigBasesMax;
		nbAlliesMainBases=nbAlliesMainBasesMax;
		

		nbEnemiesSmallBases=nbEnemiesSmallBasesMax;//20;
		nbEnemiesBigBases=nbEnemiesBigBasesMax;//15;
		nbEnemiesMainBases=nbEnemiesMainBasesMax;//10
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
		cleanMemory();
		System.out.println("RandomMapCreator cleaned");
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
	 * Build the carrier groups: 1 carriers with other units for protection
	 * Similar methods should be written for convoys and others.
 	 */
	public void buildCarrierGroups()
	{
		for (int iOurCarriers=0;iOurCarriers<this.nbOurBoatCarrier; iOurCarriers++)
		{
			
		}
		
		CarrierGroupsBuilt=true;
	}
	
	/**
	 * Check if we have a place and a path from there, so we can place a boat or a sub
	 * @param data the map
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isPlaceOk (int left,int up)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		boolean isClear=false;
		
		
		int rgbaValue=Colors.unpremultiply(rawRefData[up*700+left]);

		int returnValue=rgbaValue & 0xFF;
		if (returnValue == 0)
		{
			isClear=true;
		}
		else
		{
			return false;
		}
		if (isClear)
		{
			if (isPath(rawRefData,left,up))
				return true;
			else
				return false;
		}

		return false;
	}
	
	/**
	 * Using the isPlaceOk, check a surface to see if we have nbMin or more possible positions
	 * @param data
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param nbMin
	 * @return
	 */
	private boolean isMultiPlace(int x,int y,int width,int height,int nbMin)
	{
		int iFound=0;
		
		for (int xCheck=x;xCheck < x+width;xCheck++)
		{
			for (int yCheck=y;yCheck < y+height;yCheck++)
			{
				if (isPlaceOk (xCheck,yCheck))
				{
					iFound++;
				}
			}
		}
		if (iFound >= nbMin)
		{
			return true;
		}
		
		return false;
	}
	
	private int findPlaceBoatLeft (int up,int left)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		//boolean isClear=false;
		int stepDone=0;
		
		int yTmp=up;
		int xTmp=400;
		
		//int lengthPath=0;
		// Test horizontally left.
		for (;xTmp>400-left;xTmp--)
		{
			int rgbaValue=Colors.unpremultiply(rawRefData[yTmp*700+xTmp]);
			
			int returnValue=rgbaValue & 0xFF;
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
	
	private int findPlaceBoatRight (int up,int right)
	{
		// See if a clear path (on sea) is leaving from the coord
		// We test on the global map, and we want wide spaces, so we test only obvious paths
		// (I hope it works well enough).
		//boolean isClear=false;
		
		int stepDone=0;
		
		int yTmp=up;
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
	
	private int findPlaceBaseLeft (int up,int left)
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
			
			if (RandomMapCreator.dist(x, y, tmpBase.getXMap(), tmpBase.getYMap()) < 4)
			{
				return false;
			}
		}
		for (int iBase=0;iBase <enemiesBases.size(); ++iBase)
		{
			tmpBase=enemiesBases.get(iBase);
			
			if (RandomMapCreator.dist(x, y, tmpBase.getXMap(), tmpBase.getYMap()) < 4)
			{
				return false;
			}
		}
		for (int iBase=0;iBase < alliesBases.size(); ++iBase)
		{
			tmpBase=alliesBases.get(iBase);
			
			if (RandomMapCreator.dist(x, y, tmpBase.getXMap(), tmpBase.getYMap()) < 4)
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
			
			if (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
			{
				return false;
			}
		}
		for (int iBoat=0;iBoat <enemiesBoats.size(); ++iBoat)
		{
			tmpBoat=enemiesBoats.get(iBoat);
			
			if (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
			{
				return false;
			}
		}
		for (int iBoat=0;iBoat < alliesBoats.size(); ++iBoat)
		{
			tmpBoat=alliesBoats.get(iBoat);
			
			if (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the sub is close to other boats.
	 * @return
	 */
	public boolean isPlaceSubFree(double x,double y)
	{
		Submarine tmpBoat;
		for (int iBoat = 0; iBoat < ourSubs.size(); ++iBoat)
		{
			tmpBoat = ourSubs.get(iBoat);
			if (!tmpBoat.isDead())
			{
				if (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
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
				if (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
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
				if (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8)
				{
					return false;
				}
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
					if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
					if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
					if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
					if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
					if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
					if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
	 * @return
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
	 * @return
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
	 * @return
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if (RandomMapCreator.dist(x, y, tmpSub.getXMap(), tmpSub.getYMap()) < 8)
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if (RandomMapCreator.dist(x, y, tmpSub.getXMap(), tmpSub.getYMap()) < 8)
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
						if (RandomMapCreator.dist(x, y, tmpSub.getXMap(), tmpSub.getYMap()) < 8)
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
	 * @return
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
						if ((tmpBoat != ourBoat) && (RandomMapCreator.dist(x, y, tmpBoat.getXMap(), tmpBoat.getYMap()) < 8))
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
	 * @return
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
				//System.out.println("New dir "+newdir);
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
	 * @return
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
			//System.out.println("New dir "+newDir);
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
			
			//System.out.println("Still "+currentJourney.size()+" WPs");
			
			
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
			//System.out.println("Still "+currentJourney.size()+" WPs");
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

		System.out.println("Building the random map - " + nbEnemiesBoatCarrier);
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
//		for (;nbAlliesBoatCarrier > 0; --nbAlliesBoatCarrier)
//		{
//			int up=myRandom.nextInt( 700) + 100;
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBoatRight(up,right);
//			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 700) + 100;
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBoatRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			createCarrierAllies();
//		}
		
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
		// One mega-cruiser !
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
			createMegaCruiserEnemies();
		}
		
		for (;nbOurBoatCruiser > 0; --nbOurBoatCruiser)
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
				createCruiserOur();
			else
				createCruiserOur2();
		}
//		for (;nbAlliesBoatCruiser > 0; --nbAlliesBoatCruiser)
//		{
//			int up=myRandom.nextInt( 700) + 100;
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBoatRight(up,right);
//			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 700) + 100;
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBoatRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			createCruiserAllies();
//		}
		
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
//		for (;nbAlliesBoatDestroyer > 0; --nbAlliesBoatDestroyer)
//		{
//			int up=myRandom.nextInt( 700) + 100;
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBoatRight(up,right);
//			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 700) + 100;
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBoatRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			createDestroyerAllies();
//		}
		
		
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
//		for (;nbAlliesBoatFrigate > 0; --nbAlliesBoatFrigate)
//		{
//			int up=myRandom.nextInt( 700) + 100;
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBoatRight(up,right);
//			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 700) + 100;
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBoatRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			createFrigateAllies();
//		}
		
		
		for (;nbEnemiesBoatCorvette > 0; --nbEnemiesBoatCorvette)
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
			createCorvetteEnemies();
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
//		for (;nbAlliesSubNuke > 0; --nbAlliesSubNuke)
//		{
//			int up=myRandom.nextInt( 700) + 100;
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBoatRight(up,right);
//			while ((realRight == -1) || (!isPlaceBoatFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 700) + 100;
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBoatRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			createSubAllies(Submarine.NUKE);
//		}
		
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
//		for (;nbAlliesSmallBases > 0; --nbAlliesSmallBases)
//		{
//			int up=myRandom.nextInt( 400);
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBaseRight(up,right);
//			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 400);
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBaseRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			if (Math.random() > 0.5)
//				createSmallBaseAllies();
//			else
//				createSmallBaseAllies2();
//		}
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
//		for (;nbAlliesBigBases > 0; --nbAlliesBigBases)
//		{
//			int up=myRandom.nextInt( 400);
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBaseRight(up,right);
//			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 400);
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBaseRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			if (Math.random() > 0.5)
//				createBigBaseAllies();
//			else
//				createBigBaseAllies2();
//		}
		
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
//		for (;nbAlliesMainBases > 0; --nbAlliesMainBases)
//		{
//			int up=myRandom.nextInt( 400);
//			int right=myRandom.nextInt( 400);
//			
//			int realRight=findPlaceBaseRight(up,right);
//			while ((realRight == -1) || (!isPlaceBaseFree(realRight,up)))
//			{
//				up=myRandom.nextInt( 400);
//				right=myRandom.nextInt( 400);
//				
//				realRight=findPlaceBaseRight(up,right);
//			}
//			// Should be done !
//			yCurr=up;
//			xCurr=realRight;
//			if (Math.random() > 0.5)
//				createMainBaseAllies();
//			else
//				createMainBaseAllies2();
//		}
		isInitialised=true;
	}

	
	/**
	 * Check if we have a path in the water from the given point, so we can place a boat or a sub
	 * @param data the map
	 * @param x
	 * @param y
	 * @return
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
			if ((!tmpBoat.isDead()) &&(tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
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
			if ((!tmpBoat.isDead()) &&(tmpBoat.getXMap() > xLeft) && (tmpBoat.getXMap() < xRight)
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
			if ((!tmpBase.isDead()) &&(tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
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
			if ((!tmpBase.isDead()) &&(tmpBase.getXMap() > xLeft) && (tmpBase.getXMap() < xRight)
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
		timeTotal+=time;
		
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
		/*
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
		*/
		
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
		/*
		for (int iBase = 0 ; iBase < this.enemiesBases.size() ; iBase++)
		{
			ProgrammableUnit tmpBase = enemiesBases.get(iBase);
			tmpBase.doUpdateSM(time);
		}
		*/
		
		/*
		 * Manage the events
		 */
		if (timeTotal > nextEventTimeStamp)
		{
			createEvent();
			lastEventTimeStamp = timeTotal;
			nextEventTimeStamp = lastEventTimeStamp+ 6*myRandom.nextDouble();
		}

		for (int iEvent = 0 ; iEvent < this.ourEvents.size() ; iEvent++)
		{
			MapEvents tmpEvent = ourEvents.get(iEvent);
			if (tmpEvent.updateEvent(time))
			{
				ourEvents.remove(iEvent);
				iEvent--;
			}
		}
	}
	
	/**
	 * Create event if the time has come...
	 */
	public void createEvent()
	{
		System.out.println("One new event!!!");
		int newType = myRandom.nextInt(5);
		int randomChoice;
		boolean unitFound=false;
		double xEvent=0;
		double yEvent=0;
		
		// Next, find something to attack or a place to go
		
		if (newType == MapEvents.ATTACK)
		{
			Boat tmpBoat;
			Submarine tmpSub;
			// Find a boat !!!
			while (!unitFound)
			{
				randomChoice = myRandom.nextInt(4) ;
				if (randomChoice == 3)
				{
					tmpBoat = ourBoats.get(myRandom.nextInt(this.ourBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else if (randomChoice == 2)
				{
					tmpSub = ourSubs.get(myRandom.nextInt(this.ourSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
				else if (randomChoice == 1)
				{
					tmpBoat = alliesBoats.get(myRandom.nextInt(this.alliesBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else
				{
					tmpSub = alliesSubs.get(myRandom.nextInt(this.alliesSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
			}
		}
		
		if (newType == MapEvents.CONVOYS)
		{
			int up=myRandom.nextInt( 800);
			int left=myRandom.nextInt( 400);
			
			int realLeft=findPlaceBoatLeft(up,left);
			while ((realLeft == -1) || (!isPlaceBoatFree(realLeft,up)))
			{
				up=myRandom.nextInt( 800);
				left=myRandom.nextInt( 400);
				
				realLeft=findPlaceBoatLeft(up,left);
			}
			// Should be done !
			xEvent=realLeft;
			yEvent=up;
		}
		
		if (newType == MapEvents.DEFENSE)
		{
			Boat tmpBoat;
			Submarine tmpSub;
			unitFound=false;
			
			// Find a boat !!!
			while (!unitFound)
			{
				randomChoice = myRandom.nextInt(4) ;
				if (randomChoice == 3)
				{
					tmpBoat = ourBoats.get(myRandom.nextInt(this.ourBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else if (randomChoice == 2)
				{
					tmpSub = ourSubs.get(myRandom.nextInt(this.ourSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
				else if (randomChoice == 1)
				{
					tmpBoat = alliesBoats.get(myRandom.nextInt(this.alliesBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else
				{
					tmpSub = alliesSubs.get(myRandom.nextInt(this.alliesSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
			}
		}
		
		if (newType == MapEvents.DETECTION)
		{
			Boat tmpBoat;
			Submarine tmpSub;
			unitFound=false;
			
			// Find a boat !!!
			while (!unitFound)
			{
				randomChoice = myRandom.nextInt(4) ;
				if (randomChoice == 3)
				{
					tmpBoat = ourBoats.get(myRandom.nextInt(this.ourBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else if (randomChoice == 2)
				{
					tmpSub = ourSubs.get(myRandom.nextInt(this.ourSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
				else if (randomChoice == 1)
				{
					tmpBoat = alliesBoats.get(myRandom.nextInt(this.alliesBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else
				{
					tmpSub = alliesSubs.get(myRandom.nextInt(this.alliesSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
			}
		}
		
		if (newType == MapEvents.SURPRISE)
		{
			Boat tmpBoat;
			Submarine tmpSub;
			unitFound=false;
			
			// Find a boat !!!
			while (!unitFound)
			{
				randomChoice = myRandom.nextInt(4) ;
				if (randomChoice == 3)
				{
					tmpBoat = ourBoats.get(myRandom.nextInt(this.ourBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else if (randomChoice == 2)
				{
					tmpSub = ourSubs.get(myRandom.nextInt(this.ourSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
				else if (randomChoice == 1)
				{
					tmpBoat = alliesBoats.get(myRandom.nextInt(this.alliesBoats.size()));
					if (!tmpBoat.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBoat.getXMap();
						yEvent=tmpBoat.getYMap();
						break;
					}
				}
				else
				{
					tmpSub = alliesSubs.get(myRandom.nextInt(this.alliesSubs.size()));
					if (!tmpSub.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpSub.getXMap();
						yEvent=tmpSub.getYMap();
						break;
					}
				}
			}
		}
		
		if (newType == MapEvents.BASE_ATTACK)
		{
			Base tmpBase;
			unitFound=false;
			// Find a base !!!
			while (!unitFound)
			{
				randomChoice = myRandom.nextInt(2) ;
				if (randomChoice == 1)
				{
					tmpBase = ourBases.get(myRandom.nextInt(this.ourBases.size()));
					if (!tmpBase.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBase.getXMap();
						yEvent=tmpBase.getYMap();
						break;
					}
				}
				else
				{
					tmpBase = alliesBases.get(myRandom.nextInt(this.alliesBases.size()));
					if (!tmpBase.isDead())
					{
						// OK !
						unitFound=true;
						xEvent=tmpBase.getXMap();
						yEvent=tmpBase.getYMap();
						break;
					}
				}
			}
		}
		
		// Hmmm, not gonna work like that... There should be a clash before
		if (newType == MapEvents.BASE_DEFENSE)
		{
			Base tmpBase;
			unitFound=false;
			// Find a base !!!
			while (!unitFound)
			{
				tmpBase = enemiesBases.get(myRandom.nextInt(this.enemiesBases.size()));
				if (!tmpBase.isDead())
				{
					// OK !
					unitFound=true;
					xEvent=tmpBase.getXMap();
					yEvent=tmpBase.getYMap();
					break;
				}
			}
		}
		MapEvents oneNewEvent = new MapEvents(xEvent,yEvent, newType,40);
		oneNewEvent.createGfxSM(oneNewEvent.getPosX(), oneNewEvent.getPosY(), 0, 0, 0);
		ourEvents.add(oneNewEvent);
	}
	/**
	 * Remove the event of a will-be-played level
	 */
	public void removeLocalEvents()
	{
		int xMin;
		int yMin; // Where do we start on the globalMapImage
		
		xMin= LevelKeeper.getInstance().getNextMapX(); // (int )(Math.random()*664);//420;
		yMin= LevelKeeper.getInstance().getNextMapY();

		for (int iEvent = 0 ; iEvent < this.ourEvents.size() ; iEvent++)
		{
			MapEvents tmpEvent = ourEvents.get(iEvent);
			if ((tmpEvent.getPosX() > xMin) && (tmpEvent.getPosX() < (xMin+35))
					&& (tmpEvent.getPosY() > yMin) && (tmpEvent.getPosY() < (yMin+40)))
			{
				// One event on the level
				tmpEvent.consumeEvent();
			}
		}
	}
	/**
	 * Check if it is ok to play here!
	 */
	public boolean checkLevel()
	{
		int xMin;
		int yMin; // Where do we start on the globalMapImage
		
		boolean isAlliedUnit=false; // used with isOurUnit if the allied change faction
		boolean isOurUnit=false;
		boolean isEvent=false;
		
		xMin= LevelKeeper.getInstance().getNextMapX(); // (int )(Math.random()*664);//420;
		yMin= LevelKeeper.getInstance().getNextMapY();
		for (int iBoat = 0 ; iBoat < this.ourBoats.size() ; iBoat++)
		{
			Boat tmpBoat = ourBoats.get(iBoat);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40)) && !tmpBoat.isDead())
			{
				isOurUnit = true;
			}
		}
		for (int iBoat = 0 ; iBoat < this.alliesBoats.size() ; iBoat++)
		{
			Boat tmpBoat = alliesBoats.get(iBoat);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				isAlliedUnit = true;
			}
		}
		/*
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
	*/
		for (int iSub = 0 ; iSub < this.ourSubs.size() ; iSub++)
		{
			Submarine tmpBoat = ourSubs.get(iSub);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				isOurUnit = true;
			}
		}
		
		for (int iSub = 0 ; iSub < this.alliesSubs.size() ; iSub++)
		{
			Submarine tmpBoat = alliesSubs.get(iSub);
			if ((tmpBoat.getXMap() > xMin) && (tmpBoat.getXMap() < (xMin+35))
					&& (tmpBoat.getYMap() > yMin) && (tmpBoat.getYMap() < (yMin+40))&& !tmpBoat.isDead())
			{
				// One boat on the level
				isAlliedUnit = true;
			}
		}

		for (int iBase = 0 ; iBase < this.ourBases.size() ; iBase++)
		{
			Base tmpBase = ourBases.get(iBase);
			if ((tmpBase.getXMap() > xMin) && (tmpBase.getXMap() < (xMin+35))
					&& (tmpBase.getYMap() > yMin) && (tmpBase.getYMap() < (yMin+40)))
			{
				// One Base on the level
				isOurUnit = true;
			}
		}
		for (int iBase = 0 ; iBase < this.alliesBases.size() ; iBase++)
		{
			Base tmpBase = alliesBases.get(iBase);
			if ((tmpBase.getXMap() > xMin) && (tmpBase.getXMap() < (xMin+35))
					&& (tmpBase.getYMap() > yMin) && (tmpBase.getYMap() < (yMin+40)))
			{
				// One Base on the level
				isAlliedUnit = true;
			}
		}
		
		for (int iBase = 0 ; iBase < this.enemiesBases.size() ; iBase++)
		{
			Base tmpBase = enemiesBases.get(iBase);
			if ((tmpBase.getXMap() > xMin) && (tmpBase.getXMap() < (xMin+35))
					&& (tmpBase.getYMap() > yMin) && (tmpBase.getYMap() < (yMin+40)))
			{
				// One Base on the level
				isEvent = true; // Attacking !
			}
		}

		for (int iEvent = 0 ; iEvent < this.ourEvents.size() ; iEvent++)
		{
			MapEvents tmpEvent = ourEvents.get(iEvent);
			if ((tmpEvent.getPosX() > xMin) && (tmpEvent.getPosX() < (xMin+35))
					&& (tmpEvent.getPosY() > yMin) && (tmpEvent.getPosY() < (yMin+40)))
			{
				// One event on the level
				isEvent = true; // One event
			}
		}
		
		return ((isOurUnit || isAlliedUnit) && isEvent);
	}
	
	public void setNewPlace()
	{
		int xMin;
		int yMin; // Where do we start on the globalMapImage
		
		xMin=myRandom.nextInt(700-35);
		yMin=myRandom.nextInt(800-40);
		
		// We must have at least 20% free
		while (!isMultiPlace(xMin,yMin,35,40,280))
		{
			xMin=myRandom.nextInt(700-35);
			yMin=myRandom.nextInt(800-40);
		}
		LevelKeeper.getInstance().setNextMapX(xMin);
		LevelKeeper.getInstance().setNextMapY(yMin);
		
	}
	
	public void buildLevel(LevelMap theTMap)
	{
		ScoreKeeper.getInstance().reset();
		
		int xMin;
		int yMin; // Where do we start on the globalMapImage
		
		int typeEvent = MapEvents.ATTACK;
		
		xMin= LevelKeeper.getInstance().getNextMapX(); // (int )(Math.random()*664);//420;
		yMin= LevelKeeper.getInstance().getNextMapY();
		
		for (int iBase = 0 ; iBase < this.enemiesBases.size() ; iBase++)
		{
			Base tmpBase = enemiesBases.get(iBase);
			if ((tmpBase.getXMap() > xMin) && (tmpBase.getXMap() < (xMin+35))
					&& (tmpBase.getYMap() > yMin) && (tmpBase.getYMap() < (yMin+40)))
			{
				// One Base on the level
				typeEvent = MapEvents.BASE_DEFENSE;
				break;
			}
		}
		if (typeEvent != MapEvents.BASE_DEFENSE)
		{
//			for (int iEvent = 0 ; iEvent < this.ourEvents.size() ; iEvent++)
//			{
//				MapEvents tmpEvent = ourEvents.get(iEvent);
//				if ((tmpEvent.getPosX() > xMin) && (tmpEvent.getPosX() < (xMin+35))
//						&& (tmpEvent.getPosY() > yMin) && (tmpEvent.getPosY() < (yMin+40)))
//				{
//					// One event on the level
//					typeEvent = tmpEvent.getType();
//				}
//			}
			typeEvent = RandomContainer.getInstance().myRandom.nextInt(5);
		}
		addEnemiesUnit(theTMap,
				   xMin, yMin, typeEvent);
//		addAlliesUnit(theTMap,
//				   xMin, yMin, typeEvent);
		addOurUnit(theTMap,
				   xMin, yMin, typeEvent);
				   
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

				
//				KnownDatas ourKD;
//
//				ourKD = new KnownDatas(theTMap);
//				tmpBoat.getSensors().add(ourKD);

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

//				KnownDatas ourKD;
//
//				ourKD = new KnownDatas(theTMap);
//				tmpBoat.getSensors().add(ourKD);

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
				if (tmpBoat.getTonnage() == 700000)
				{
					tmpBoat.showMe();
				}
				

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

//				KnownDatas ourKD;
//
//				ourKD = new KnownDatas(theTMap);
//				tmpBoat.getSensors().add(ourKD);

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

//				KnownDatas ourKD;
//
//				ourKD = new KnownDatas(theTMap);
//				tmpBoat.getSensors().add(ourKD);

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
				tmpBase.createGfx( tmpBase.getPosX(), tmpBase.getPosY(), 0, 0, 0);
				tmpBase.getSensors().add(ourRadar3);
				tmpBase.addAttachedObject(ourRadar3);

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
				tmpBase.createGfx( tmpBase.getPosX(), tmpBase.getPosY(), 0, 0, 0);
				tmpBase.getSensors().add(ourRadar3);
				tmpBase.addAttachedObject(ourRadar3);

				theTMap.addBase(tmpBase);
			}
		}

		//rawRefData[(yMin+yCurr)*700+(xMin+xCurr)]=Colors.premultiply(rgbaValue|0xFF);

		Satellite myNewSatellite= new Satellite(theTMap);
		myNewSatellite.createGfx(100, 100, 0, Math.PI/2 + 0.1, 20, 10);

		theTMap.addGlobalSensor(myNewSatellite);
	}
	
	/**
	 * Following possibilities, add the enemies units for the given event
	 * @param xMin
	 * @param yMin
	 * @param typeEvent
	 */
	public void addEnemiesUnit(LevelMap theTMap,
							   int xMin, int yMin, int typeEvent)
	{
		int nbCarrier=0;
		int nbCruiser=0;
		int nbDestroyer=0;
		int nbFrigate=0;
		int nbCorvette=0;
		int nbSub=0;
		int nbToAdd=0;
		double myRandomHelpNb=Math.random();
		System.out.println("Creating the level, with event "+typeEvent);
		switch (typeEvent)
		{
			case MapEvents.ATTACK:
				// Attack, might be a sole sub or a full bunch of enemies.
				// Three main cases:
				// Sub only (or 2-3)
				// Cruiser + Destroyer + Frigate + Sub
				// Carrier group(s)
				nbToAdd = myRandom.nextInt(7)+1;
				if (myRandomHelpNb < 0.3)
				{
					if (nbToAdd < 5)
					{
						nbSub=myRandom.nextInt(nbToAdd);
					}
					else
					{
						nbSub=myRandom.nextInt(5);
					}
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
					
					
					nbCruiser=myRandom.nextInt(4);
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(4);
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
				
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(3)+1;
				
				nbDestroyer=myRandom.nextInt(4);
				
				nbFrigate=myRandom.nextInt(8)+1;
				
				break;
			case MapEvents.BASE_ATTACK:
				// Base attack, mostly strong groups !
				if (myRandomHelpNb < 0.1)
				{
					nbSub=myRandom.nextInt(5);
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
					
					nbCruiser=myRandom.nextInt(3)+1;
					
					nbDestroyer=myRandom.nextInt(3)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
				
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				//this.nbEnemiesBoatCruiser-=nbCruiser;
				
				nbFrigate=myRandom.nextInt(5);
				
				break;
			case MapEvents.BASE_DEFENSE:
				// Defence, currently more or less like attack... More units in some
				// cases though (subs, corvettes and frigates)
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(4)+1;
						
					nbCorvette=myRandom.nextInt(10)+1;
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
						
					nbCruiser=myRandom.nextInt(3)+1;
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					nbCorvette=myRandom.nextInt(8)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate+nbCorvette > 0)
						break;	
				}
				// All other cases
				nbSub=myRandom.nextInt(4)+1;
			
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				nbDestroyer=myRandom.nextInt(6)+1;
				
				nbFrigate=myRandom.nextInt(5);
				
				nbCorvette=myRandom.nextInt(6)+1;
				
				break;
			case MapEvents.CONVOYS:
				// Convoys, quite stable bunch of boats and subs
				if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(2)+1;
					
					nbCruiser=myRandom.nextInt(2)+1;
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(5)+1;

					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(2)+1;
				
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(2)+1;
				
				nbDestroyer=myRandom.nextInt(4)+1;
				
				nbFrigate=myRandom.nextInt(5)+1;
				
				//this.nbEnemiesBoatFrigate-=nbFrigate;
				break;
			case MapEvents.DEFENSE:
				// Defence, currently more or less like attack... More units in some
				// cases though (subs, corvettes and frigates)
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(3)+1;
						
					nbCorvette=myRandom.nextInt(5)+1;
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(3)+1;
					
					nbCruiser=myRandom.nextInt(2)+1;
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					nbCorvette=myRandom.nextInt(6)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate+nbCorvette > 0)
						break;	
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
				
				nbCarrier=myRandom.nextInt(3);
				if (nbCarrier > this.nbEnemiesBoatCarrier)
				
				nbCruiser=myRandom.nextInt(4);
				
				nbDestroyer=myRandom.nextInt(6);
				
				nbFrigate=myRandom.nextInt(5)+1;
				
				nbCorvette=myRandom.nextInt(5);
				
				break;
			case MapEvents.DETECTION:
				// Detection, subs or mostly small and fast boat
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(2)+1;
					
					break;
				}
				
				// Other cases
				nbSub=myRandom.nextInt(4);

				nbCruiser=myRandom.nextInt(3);
				
				nbDestroyer=myRandom.nextInt(4);
			
				nbFrigate=myRandom.nextInt(4);
				
				if (nbCruiser+nbDestroyer+nbFrigate > 0)
					break;
				
			case MapEvents.SURPRISE:
				// Surprise, to define !!!
				if (myRandomHelpNb < 0.3) 
				{
					nbSub=myRandom.nextInt(3)+1;

					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
					
					nbCruiser=myRandom.nextInt(4);
					
					nbDestroyer=myRandom.nextInt(5);
				
					nbFrigate=myRandom.nextInt(5)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(2)+1;
				
				nbCarrier=myRandom.nextInt(3);

				nbCruiser=myRandom.nextInt(4);
				
				nbDestroyer=myRandom.nextInt(5);
				
				nbFrigate=myRandom.nextInt(4)+1;
			
				break;
			default:
				System.out.println("Unknown event");	
		}
		System.out.println("Will add "+nbCarrier+" carriers");
		for (int iBoat = 0 ; iBoat < nbCarrier ; iBoat++)
		{	
			placeCarrierEnemiesTMap(xMin,yMin,Boat.CARRIER);	
		}
		System.out.println("Will add "+nbCruiser+" cruisers");
		for (int iBoat = 0 ; iBoat < nbCruiser ; iBoat++)
		{	
			placeCarrierEnemiesTMap(xMin,yMin,Boat.CRUISER);	
		}
		System.out.println("Will add "+nbFrigate+" frigates");
		for (int iBoat = 0 ; iBoat < nbFrigate ; iBoat++)
		{	
			placeCarrierEnemiesTMap(xMin,yMin,Boat.FRIGATE);	
		}
		System.out.println("Will add "+nbCorvette+" corvettes");
		for (int iBoat = 0 ; iBoat < nbCorvette ; iBoat++)
		{	
			placeCarrierEnemiesTMap(xMin,yMin,Boat.CORVETTE);	
		}
		System.out.println("Will add "+nbDestroyer+" destroyers");
		for (int iBoat = 0 ; iBoat < nbDestroyer ; iBoat++)
		{	
			placeCarrierEnemiesTMap(xMin,yMin,Boat.DESTROYER);	
		}
		System.out.println("Will add "+nbSub+" subs");
		for (int iSub = 0 ; iSub < nbSub ; iSub++)
		{
			placeSubEnemiesTMap(theTMap,xMin,yMin,Submarine.NUKE);
		}
	}
	
	/**
	 * Following possibilities, add the enemies units for the given event
	 * @param xMin
	 * @param yMin
	 * @param typeEvent
	 */
	public void addAlliesUnit(LevelMap theTMap,
							   int xMin, int yMin, int typeEvent)
	{
		int nbCarrier=0;
		int nbCruiser=0;
		int nbDestroyer=0;
		int nbFrigate=0;
		int nbCorvette=0;
		int nbSub=0;
		int nbToAdd=0;
		double myRandomHelpNb=Math.random();
		System.out.println("Event "+typeEvent+" asked");	
		switch (typeEvent)
		{
			case MapEvents.ATTACK:
				// Attack, might be a sole sub or a full bunch of Allies.
				// Three main cases:
				// Sub only (or 2-3)
				// Cruiser + Destroyer + Frigate + Sub
				// Carrier group(s)
				nbToAdd = myRandom.nextInt(8)+1;
				if (myRandomHelpNb < 0.3)
				{
					if (nbToAdd < 5)
					{
						nbSub=myRandom.nextInt(nbToAdd);
					}
					else
					{
						nbSub=myRandom.nextInt(5);
					}
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
					
					nbCruiser=myRandom.nextInt(4);
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
				
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				nbDestroyer=myRandom.nextInt(5)+1;
				
				nbFrigate=myRandom.nextInt(4)+1;
				
				break;
			case MapEvents.BASE_ATTACK:
				// Base attack, mostly strong groups !
				if (myRandomHelpNb < 0.1)
				{
					nbSub=myRandom.nextInt(5);

					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
					
					nbCruiser=myRandom.nextInt(4)+1;
					
					nbDestroyer=myRandom.nextInt(5)+1;
					
					nbFrigate=myRandom.nextInt(4);
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
				
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				//this.nbAlliesBoatCruiser-=nbCruiser;
				
				nbFrigate=myRandom.nextInt(5);
				
				break;
			case MapEvents.BASE_DEFENSE:
				// Defence, currently more or less like attack... More units in some
				// cases though (subs, corvettes and frigates)
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(4)+1;
					
					nbCorvette=myRandom.nextInt(10)+1;
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
					
					nbCruiser=myRandom.nextInt(3)+1;
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					nbCorvette=myRandom.nextInt(8)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate+nbCorvette > 0)
						break;	
				}
				// All other cases
				nbSub=myRandom.nextInt(4)+1;
				
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				nbDestroyer=myRandom.nextInt(5)+1;
				
				nbFrigate=myRandom.nextInt(6);
				
				nbCorvette=myRandom.nextInt(8)+1;
				
				break;
			case MapEvents.CONVOYS:
				// Convoys, quite stable bunch of boats and subs
				if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(2)+1;
					
					nbCruiser=myRandom.nextInt(2)+1;
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(5)+1;

					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(2)+1;

				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(2)+1;
				
				nbDestroyer=myRandom.nextInt(4)+1;
				
				nbFrigate=myRandom.nextInt(3)+1;
				
				//this.nbAlliesBoatFrigate-=nbFrigate;
				break;
			case MapEvents.DEFENSE:
				// Defence, currently more or less like attack... More units in some
				// cases though (subs, corvettes and frigates)
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(3)+1;
					
					nbCorvette=myRandom.nextInt(10)+1;
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(3)+1;
					
					nbCruiser=myRandom.nextInt(2)+1;
					
					nbDestroyer=myRandom.nextInt(3)+1;
					
					nbFrigate=myRandom.nextInt(4);
					
					nbCorvette=myRandom.nextInt(5)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate+nbCorvette > 0)
						break;	
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
				
				nbCarrier=myRandom.nextInt(3);
				if (nbCarrier > this.nbAlliesBoatCarrier)
				
				nbCruiser=myRandom.nextInt(4);
				
				nbDestroyer=myRandom.nextInt(6);
				
				nbFrigate=myRandom.nextInt(4)+1;
				
				nbCorvette=myRandom.nextInt(5);
				
				break;
			case MapEvents.DETECTION:
				// Detection, subs or mostly small and fast boat
				if (myRandomHelpNb < 0.3) 
				{
					nbSub=myRandom.nextInt(2)+1;
					break;
				}
				
				// Other cases
				nbSub=myRandom.nextInt(4);

				nbCruiser=myRandom.nextInt(3);
				
				nbDestroyer=myRandom.nextInt(4);
			
				nbFrigate=myRandom.nextInt(5);
				
				if (nbCruiser+nbDestroyer+nbFrigate > 0)
					break;
				
			case MapEvents.SURPRISE:
				// Surprise, to define !!!
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(3)+1;
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
						
					nbCruiser=myRandom.nextInt(4);
					
					nbDestroyer=myRandom.nextInt(6);
				
					nbFrigate=myRandom.nextInt(5)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(2)+1;
				
				nbCarrier=myRandom.nextInt(3);

				nbCruiser=myRandom.nextInt(4);
				
				nbDestroyer=myRandom.nextInt(7);
				
				nbFrigate=myRandom.nextInt(6)+1;
			
				break;
			default:
				System.out.println("Unknown event");	
		}
		System.out.println("Will add "+nbCarrier+" carriers");
		for (int iBoat = 0 ; iBoat < nbCarrier ; iBoat++)
		{	
			placeCarrierAlliesTMap(xMin,yMin,Boat.CARRIER);	
		}
		System.out.println("Will add "+nbCruiser+" cruisers");
		for (int iBoat = 0 ; iBoat < nbCruiser ; iBoat++)
		{	
			placeCarrierAlliesTMap(xMin,yMin,Boat.CRUISER);	
		}
		System.out.println("Will add "+nbFrigate+" frigates");
		for (int iBoat = 0 ; iBoat < nbFrigate ; iBoat++)
		{	
			placeCarrierAlliesTMap(xMin,yMin,Boat.FRIGATE);	
		}
		System.out.println("Will add "+nbCorvette+" corvettes");
		for (int iBoat = 0 ; iBoat < nbCorvette ; iBoat++)
		{	
			placeCarrierAlliesTMap(xMin,yMin,Boat.CORVETTE);	
		}
		System.out.println("Will add "+nbDestroyer+" destroyers");
		for (int iBoat = 0 ; iBoat < nbDestroyer ; iBoat++)
		{	
			placeCarrierAlliesTMap(xMin,yMin,Boat.DESTROYER);	
		}
		System.out.println("Will add "+nbSub+" subs");
		for (int iSub = 0 ; iSub < nbSub ; iSub++)
		{
			placeSubAlliesTMap(theTMap,xMin,yMin,Submarine.NUKE);
		}
	}
	
	
	/**
	 * Following possibilities, add our units for the given event
	 * @param xMin
	 * @param yMin
	 * @param typeEvent
	 */
	public void addOurUnit(LevelMap theTMap,
							   int xMin, int yMin, int typeEvent)
	{
		int nbCarrier=0;
		int nbCruiser=0;
		int nbDestroyer=0;
		int nbFrigate=0;
		int nbCorvette=0;
		int nbSub=0;
		int nbToAdd=0;
		double myRandomHelpNb=Math.random();
		
		switch (typeEvent)
		{
			case MapEvents.ATTACK:
				// Attack, might be a sole sub or a full bunch of Our.
				// Three main cases:
				// Sub only (or 2-3)
				// Cruiser + Destroyer + Frigate + Sub
				// Carrier group(s)
				nbToAdd = myRandom.nextInt(8)+1;
				if (myRandomHelpNb < 0.3) 
				{
					if (nbToAdd < 5)
					{
						nbSub=myRandom.nextInt(nbToAdd);
					}
					else
					{
						nbSub=myRandom.nextInt(5);
					}
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
						
					nbCruiser=myRandom.nextInt(4);
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(6);
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
					
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				nbDestroyer=myRandom.nextInt(4)+1;
				
				nbFrigate=myRandom.nextInt(5)+1;
				
				break;
			case MapEvents.BASE_ATTACK:
				// Base attack, mostly strong groups !
				if (myRandomHelpNb < 0.1)
				{
					nbSub=myRandom.nextInt(5);
							break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
						
					nbCruiser=myRandom.nextInt(4)+1;
					
					nbDestroyer=myRandom.nextInt(7)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(4);
					
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				//this.nbOurBoatCruiser-=nbCruiser;
				
				nbFrigate=myRandom.nextInt(5);
				
				break;
			case MapEvents.BASE_DEFENSE:
				// Defence, currently more or less like attack... More units in some
				// cases though (subs, corvettes and frigates)
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(4)+1;
						
					nbCorvette=myRandom.nextInt(10)+1;
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
							
					nbCruiser=myRandom.nextInt(3)+1;
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					nbCorvette=myRandom.nextInt(6)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate+nbCorvette > 0)
						break;	
				}
				// All other cases
				nbSub=myRandom.nextInt(4)+1;
					
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(4)+1;
				
				nbDestroyer=myRandom.nextInt(6)+1;
				
				nbFrigate=myRandom.nextInt(5);
				
				nbCorvette=myRandom.nextInt(6)+1;
				
				break;
			case MapEvents.CONVOYS:
				// Convoys, quite stable bunch of boats and subs
				if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(2)+1;
							
					nbCruiser=myRandom.nextInt(2)+1;
					
					nbDestroyer=myRandom.nextInt(4)+1;
					
					nbFrigate=myRandom.nextInt(4)+1;

					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(2)+1;
					
				nbCarrier=myRandom.nextInt(3);
				
				nbCruiser=myRandom.nextInt(2)+1;
				
				nbDestroyer=myRandom.nextInt(4)+1;
				
				nbFrigate=myRandom.nextInt(6)+1;
				
				//this.nbOurBoatFrigate-=nbFrigate;
				break;
			case MapEvents.DEFENSE:
				// Defence, currently more or less like attack... More units in some
				// cases though (subs, corvettes and frigates)
				if (myRandomHelpNb < 0.3) 
				{
					nbSub=myRandom.nextInt(3)+1;
							
					nbCorvette=myRandom.nextInt(10)+1;
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(3)+1;
						
					nbCruiser=myRandom.nextInt(2)+1;
					
					nbDestroyer=myRandom.nextInt(6)+1;
					
					nbFrigate=myRandom.nextInt(5);
					
					nbCorvette=myRandom.nextInt(6)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate+nbCorvette > 0)
						break;	
				}
				// All other cases
				nbSub=myRandom.nextInt(4);

				nbCarrier=myRandom.nextInt(3);
				if (nbCarrier > this.nbOurBoatCarrier)
				
				nbCruiser=myRandom.nextInt(4);
				
				nbDestroyer=myRandom.nextInt(6);
				
				nbFrigate=myRandom.nextInt(5)+1;
				
				nbCorvette=myRandom.nextInt(6);
				
				break;
			case MapEvents.DETECTION:
				// Detection, subs or mostly small and fast boat
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(2)+1;
					
					break;
				}
				
				// Other cases
				nbSub=myRandom.nextInt(4);

				nbCruiser=myRandom.nextInt(3);
				
				nbDestroyer=myRandom.nextInt(4);
			
				nbFrigate=myRandom.nextInt(6);
				
				if (nbCruiser+nbDestroyer+nbFrigate > 0)
					break;
				
			case MapEvents.SURPRISE:
				// Surprise, to define !!!
				if (myRandomHelpNb < 0.3)
				{
					nbSub=myRandom.nextInt(3)+1;
					
					break;
				}
				else if (myRandomHelpNb < 0.7)
				{
					nbSub=myRandom.nextInt(4);
					
					nbCruiser=myRandom.nextInt(4);
					
					nbDestroyer=myRandom.nextInt(6);
				
					nbFrigate=myRandom.nextInt(5)+1;
					
					if (nbCruiser+nbDestroyer+nbFrigate > 0)
						break;
				}
				// All other cases
				nbSub=myRandom.nextInt(2)+1;
				
				nbCarrier=myRandom.nextInt(3);

				nbCruiser=myRandom.nextInt(4);
				
				nbDestroyer=myRandom.nextInt(5);
				
				nbFrigate=myRandom.nextInt(6)+1;
			
				break;
			default:
				System.out.println("Unknown event");	
		}
		System.out.println("Will add "+nbCarrier+" carriers");
		for (int iBoat = 0 ; iBoat < nbCarrier ; iBoat++)
		{	
			placeCarrierOurTMap(xMin,yMin,Boat.CARRIER);	
		}
		System.out.println("Will add "+nbCruiser+" cruisers");
		for (int iBoat = 0 ; iBoat < nbCruiser ; iBoat++)
		{	
			placeCarrierOurTMap(xMin,yMin,Boat.CRUISER);	
		}
		System.out.println("Will add "+nbFrigate+" frigates");
		for (int iBoat = 0 ; iBoat < nbFrigate ; iBoat++)
		{	
			placeCarrierOurTMap(xMin,yMin,Boat.FRIGATE);	
		}
		System.out.println("Will add "+nbCorvette+" corvettes");
		for (int iBoat = 0 ; iBoat < nbCorvette ; iBoat++)
		{	
			placeCarrierOurTMap(xMin,yMin,Boat.CORVETTE);	
		}
		System.out.println("Will add "+nbDestroyer+" destroyers");
		for (int iBoat = 0 ; iBoat < nbDestroyer ; iBoat++)
		{	
			placeCarrierOurTMap(xMin,yMin,Boat.DESTROYER);	
		}
		System.out.println("Will add "+nbSub+" subs");
		for (int iSub = 0 ; iSub < nbSub ; iSub++)
		{
			placeSubOurTMap(theTMap,xMin,yMin,Submarine.NUKE);
		}
	}
/*
	private Boat createCarrierEnemies(TacticalMapPC theTMap,int xMin, int yMin)
	{
		Boat oneCarrier = new Boat();
		oneCarrier.setType(Boat.CARRIER);
		oneCarrier.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCarrier.setComplement(5000);
		oneCarrier.setComplementNorm(5000);
		ScoreKeeper.getInstance().addComplementEnemies(5000);
		oneCarrier.setTonnage(100000);
		oneCarrier.setCost(4000000000L);

		//ScoreKeeper.getInstance().addCostEnemies(4000000000L);
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
		yCurr=xMin+myRandom.nextInt(40);
		xCurr=yMin+myRandom.nextInt(40);
		
		oneCarrier.setPosMap(xCurr, yCurr);
		oneCarrier.setPosMapTarget(xCurr+2, yCurr);
		oneCarrier.setFollowTargetMap(true);

		//this.addBoat(oneCarrier);
		
		ScoreKeeper.getInstance().nbCarrierTotalEnemies++;
		
		return oneCarrier;
	}
	
	private Boat createCruiserEnemies(TacticalMapPC theTMap,int xMin, int yMin)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementEnemies(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		//ScoreKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("USS Bidule");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(20);
		// And add the absolute coordinates.
		yCurr=xMin+myRandom.nextInt(40);
		xCurr=yMin+myRandom.nextInt(40);
		
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		//this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
		
		return oneCruiser;
	}
	
	private Boat createDestroyerEnemies(TacticalMapPC theTMap,int xMin, int yMin)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(100);
		oneCruiser.setComplementNorm(100);
		ScoreKeeper.getInstance().addComplementEnemies(100);
		oneCruiser.setTonnage(22000);
		oneCruiser.setCost(40000000L);

		//ScoreKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(100);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		yCurr=xMin+myRandom.nextInt(40);
		xCurr=yMin+myRandom.nextInt(40);
		
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		//this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
		
		return oneCruiser;
	}
	
	private Boat createFrigateEnemies(TacticalMapPC theTMap,int xMin, int yMin)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(50);
		oneCruiser.setComplementNorm(50);
		ScoreKeeper.getInstance().addComplementEnemies(50);
		oneCruiser.setTonnage(12000);
		oneCruiser.setCost(20000000L);

		//ScoreKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(50);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(7);
		// And add the absolute coordinates.
		yCurr=xMin+myRandom.nextInt(40);
		xCurr=yMin+myRandom.nextInt(40);
		
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		//this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
		
		return oneCruiser;
	}
	
	private Boat createCorvetteEnemies(TacticalMapPC theTMap,int xMin, int yMin)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CORVETTE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(30);
		oneCruiser.setComplementNorm(30);
		ScoreKeeper.getInstance().addComplementEnemies(30);
		oneCruiser.setTonnage(6000);
		oneCruiser.setCost(10000000L);

		//ScoreKeeper.getInstance().addCostEnemies(400000000L);
		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(50);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(2);
		// And add the absolute coordinates.
		yCurr=xMin+myRandom.nextInt(40);
		xCurr=yMin+myRandom.nextInt(40);
		
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		//this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
		
		return oneCruiser;
	}
	*/
	private Boat moveBoatEnemies(int newX, int newY, int typeBoat)
	{
		Boat tmpBoat;
		
		int iBoat;
		int trial=0; // Limit the search (ie. does not loop infinitively if no good unit found).
		
		// Find and move an available boat!
		while (trial < 2*this.enemiesBoats.size())
		{
			trial++;
			
			iBoat=myRandom.nextInt(this.enemiesBoats.size());
			
			tmpBoat = enemiesBoats.get(iBoat);
			
			if ((tmpBoat.getType() == typeBoat) && !tmpBoat.isDead())
			{
				tmpBoat.setPosMap(newX, newY);
				return tmpBoat;
			}
		}
		
		return null;
	}
	
	private void placeCarrierEnemiesTMap(int xMin, int yMin,int boatType)
	{
		int nbTrials=0;
		boolean okToPlace=true;
		
		xCurr=xMin+myRandom.nextInt(40);
		yCurr=yMin+myRandom.nextInt(40);
		
		while (!isPlaceOk (xCurr,yCurr) || !isPlaceBoatFree(xCurr,yCurr))
		{
			xCurr=xMin+myRandom.nextInt(40);
			yCurr=yMin+myRandom.nextInt(40);
			//System.out.println("New place "+xCurr+" - "+yCurr+" ... "+nbTrials);
			
			nbTrials++;
			if (nbTrials > 40)
			{
				okToPlace=false;
				break;
			}
		}

		if (okToPlace)
		{
			moveBoatEnemies(xCurr, yCurr,boatType);
		}
	}
	
	private Submarine moveSubEnemies(int newX, int newY, int typeSub)
	{
		Submarine tmpSub;
		
		int iBoat;
		int trial=0; // Limit the search (ie. does not loop infinitively if no good unit found).
		
		// Find and move an available boat!
		while (trial < 2*this.enemiesSubs.size())
		{
			trial++;
			iBoat=myRandom.nextInt(this.enemiesSubs.size());
			
			tmpSub = enemiesSubs.get(iBoat);
			System.out.println("Event : Sub wanted "+typeSub+" sub looked at "+tmpSub.getType());
			if ((tmpSub.getType() == typeSub) && !tmpSub.isDead())
			{
				tmpSub.setPosMap(newX, newY);
				return tmpSub;
			}
		}
		
		return null;
	}
	
	private void placeSubEnemiesTMap(LevelMap theTMap,int xMin, int yMin,int type)
	{
		int nbTrials=0;
		boolean okToPlace=true;
		
		xCurr=xMin+myRandom.nextInt(40);
		yCurr=yMin+myRandom.nextInt(40);
		
		while (!isPlaceOk (xCurr,yCurr) || !isPlaceSubFree(xCurr,yCurr))
		{
			xCurr=xMin+myRandom.nextInt(40);
			yCurr=yMin+myRandom.nextInt(40);
			
			nbTrials++;
			if (nbTrials > 20)
			{
				okToPlace=false;
				break;
			}
		}
	
		if (okToPlace)
		{
			moveSubEnemies(xCurr, yCurr,type);
		}
	}
	
	
	private Boat moveBoatAllies(int newX, int newY, int typeBoat)
	{
		Boat tmpBoat;
		
		int iBoat;
		int trial=0; // Limit the search (ie. does not loop infinitively if no good unit found).
		
		// Find and move an available boat!
		while (trial < 2*this.alliesBoats.size())
		{
			trial++;
			
			iBoat=myRandom.nextInt(this.alliesBoats.size());
			
			tmpBoat = alliesBoats.get(iBoat);
			
			if ((tmpBoat.getType() == typeBoat) && !tmpBoat.isDead())
			{
				tmpBoat.setPosMap(newX, newY);
				return tmpBoat;
			}
		}
		
		return null;
	}
	
	private void placeCarrierAlliesTMap(int xMin, int yMin,int boatType)
	{
		int nbTrials=0;
		boolean okToPlace=true;
		
		xCurr=xMin+myRandom.nextInt(40);
		yCurr=yMin+myRandom.nextInt(40);
		
		while (!isPlaceOk (xCurr,yCurr) || !isPlaceBoatFree(xCurr,yCurr))
		{
			xCurr=xMin+myRandom.nextInt(40);
			yCurr=yMin+myRandom.nextInt(40);
			//System.out.println("New place "+xCurr+" - "+yCurr+" ... "+nbTrials);
			
			nbTrials++;
			if (nbTrials > 40)
			{
				okToPlace=false;
				break;
			}
		}

		if (okToPlace)
		{
			moveBoatAllies(xCurr, yCurr,boatType);
		}
	}
	
	private Submarine moveSubAllies(int newX, int newY, int typeSub)
	{
		Submarine tmpSub;
		
		int iBoat;
		int trial=0; // Limit the search (ie. does not loop infinitively if no good unit found).
		
		// Find and move an available boat!
		while (trial < 2*this.alliesSubs.size())
		{
			trial++;
			iBoat=myRandom.nextInt(this.alliesSubs.size());
			
			tmpSub = alliesSubs.get(iBoat);
			System.out.println("Event : Sub wanted "+typeSub+" sub looked at "+tmpSub.getType());
			if ((tmpSub.getType() == typeSub) && !tmpSub.isDead())
			{
				tmpSub.setPosMap(newX, newY);
				return tmpSub;
			}
		}
		
		return null;
	}
	
	private void placeSubAlliesTMap(LevelMap theTMap,int xMin, int yMin,int type)
	{
		int nbTrials=0;
		boolean okToPlace=true;
		
		xCurr=xMin+myRandom.nextInt(40);
		yCurr=yMin+myRandom.nextInt(40);
		
		while (!isPlaceOk (xCurr,yCurr) || !isPlaceSubFree(xCurr,yCurr))
		{
			xCurr=xMin+myRandom.nextInt(40);
			yCurr=yMin+myRandom.nextInt(40);
			
			nbTrials++;
			if (nbTrials > 20)
			{
				okToPlace=false;
				break;
			}
		}
	
		if (okToPlace)
		{
			moveSubAllies(xCurr, yCurr,type);
		}
	}
	
	private Boat moveBoatOur(int newX, int newY, int typeBoat)
	{
		Boat tmpBoat;
		
		int iBoat;
		int trial=0; // Limit the search (ie. does not loop infinitively if no good unit found).
		
		// Find and move an available boat!
		while (trial < 2*this.ourBoats.size())
		{
			trial++;
			
			iBoat=myRandom.nextInt(this.ourBoats.size());
			
			tmpBoat = ourBoats.get(iBoat);
			
			if ((tmpBoat.getType() == typeBoat) && !tmpBoat.isDead())
			{
				tmpBoat.setPosMap(newX, newY);
				return tmpBoat;
			}
		}
		
		return null;
	}
	
	private void placeCarrierOurTMap(int xMin, int yMin,int boatType)
	{
		int nbTrials=0;
		boolean okToPlace=true;
		
		xCurr=xMin+myRandom.nextInt(40);
		yCurr=yMin+myRandom.nextInt(40);
		
		while (!isPlaceOk (xCurr,yCurr) || !isPlaceBoatFree(xCurr,yCurr))
		{
			xCurr=xMin+myRandom.nextInt(40);
			yCurr=yMin+myRandom.nextInt(40);
			//System.out.println("New place "+xCurr+" - "+yCurr+" ... "+nbTrials);
			
			nbTrials++;
			if (nbTrials > 40)
			{
				okToPlace=false;
				break;
			}
		}

		if (okToPlace)
		{
			moveBoatOur(xCurr, yCurr,boatType);
		}
	}
	
	private Submarine moveSubOur(int newX, int newY, int typeSub)
	{
		Submarine tmpSub;
		
		int iBoat;
		int trial=0; // Limit the search (ie. does not loop infinitively if no good unit found).
		
		// Find and move an available boat!
		while (trial < 2*this.ourSubs.size())
		{
			trial++;
			iBoat=myRandom.nextInt(this.ourSubs.size());
			
			tmpSub = ourSubs.get(iBoat);
			System.out.println("Event : Sub wanted "+typeSub+" sub looked at "+tmpSub.getType());
			if ((tmpSub.getType() == typeSub) && !tmpSub.isDead())
			{
				tmpSub.setPosMap(newX, newY);
				return tmpSub;
			}
		}
		
		return null;
	}
	
	private void placeSubOurTMap(LevelMap theTMap,int xMin, int yMin,int type)
	{
		int nbTrials=0;
		boolean okToPlace=true;
		
		xCurr=xMin+myRandom.nextInt(40);
		yCurr=yMin+myRandom.nextInt(40);
		
		while (!isPlaceOk (xCurr,yCurr) || !isPlaceSubFree(xCurr,yCurr))
		{
			xCurr=xMin+myRandom.nextInt(40);
			yCurr=yMin+myRandom.nextInt(40);
			
			nbTrials++;
			if (nbTrials > 20)
			{
				okToPlace=false;
				break;
			}
		}
	
		if (okToPlace)
		{
			moveSubOur(xCurr, yCurr,type);
		}
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
		ScoreKeeper.getInstance().addComplementEnemies(5000);
		oneCarrier.setTonnage(100000);
		oneCarrier.setCost(4000000000L);

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
	}

	int iCarrierOur = 0;
	
	private void createCarrierOur()
	{
		// Add a carrier here !!!
		Boat oneCarrier = new Boat();
		oneCarrier.setType(Boat.CARRIER);
		oneCarrier.setTypeFaction(FUnit.BOAT_OUR);
		oneCarrier.setComplement(5000);
		oneCarrier.setComplementNorm(5000);
		ScoreKeeper.getInstance().addComplementOur(5000);
		oneCarrier.setTonnage(105000);
		oneCarrier.setCost(5000000000L);

		oneCarrier.setMaxSpeed(35);
		oneCarrier.setStandardSpeed(20);
		oneCarrier.setNbAwacs(2);
		oneCarrier.setNbFighters(120);
		oneCarrier.setNbTankers(2);
		if (iCarrierOur < PoolOfNames.namesOurCarriers.length)
		{
			oneCarrier.setName(PoolOfNames.namesOurCarriers[iCarrierOur++]);
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
		oneCarrier.setPosMap(xCurr, yCurr);
		oneCarrier.setPosMapTarget(xCurr-2, yCurr);
		oneCarrier.setFollowTargetMap(true);
		
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
		ScoreKeeper.getInstance().addComplementOur(5000);
		oneCarrier.setTonnage(70000);
		oneCarrier.setCost(2000000000L);

		oneCarrier.setMaxSpeed(35);
		oneCarrier.setStandardSpeed(20);
		oneCarrier.setNbAwacs(2);
		oneCarrier.setNbFighters(80);
		oneCarrier.setNbTankers(2);
		if (iCarrierOur < PoolOfNames.namesOurCarriers.length)
		{
			oneCarrier.setName(PoolOfNames.namesOurCarriers[iCarrierOur++]);
		}
		else
		{
			oneCarrier.setName("USS Bidule");
		}
		oneCarrier.setFireAtWill(true);
		oneCarrier.setNbMissiles(100);
		oneCarrier.setNbTorpedoes(0);
		oneCarrier.setResistance(45);
		// And add the absolute coordinates.
		oneCarrier.setPosMap(xCurr, yCurr);
		oneCarrier.setPosMapTarget(xCurr-2, yCurr);
		oneCarrier.setFollowTargetMap(true);

		this.addBoat(oneCarrier);
	}

	int iCarrierAllies = 0;
	
	private void createCarrierAllies()
	{
		// Add a carrier here !!!
		Boat oneCarrier = new Boat();
		oneCarrier.setType(Boat.CARRIER);
		oneCarrier.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCarrier.setComplement(3500);
		oneCarrier.setComplementNorm(3500);
		ScoreKeeper.getInstance().addComplementAllies(3500);
		oneCarrier.setTonnage(60000);
		oneCarrier.setCost(2000000000L);

		oneCarrier.setMaxSpeed(40);
		oneCarrier.setStandardSpeed(20);
		oneCarrier.setNbAwacs(2);
		oneCarrier.setNbFighters(50);
		oneCarrier.setNbTankers(2);
		if (iCarrierAllies < PoolOfNames.namesAlliesCarriers.length)
		{
			oneCarrier.setName(PoolOfNames.namesAlliesCarriers[iCarrierAllies++]);
		}
		else
		{
			oneCarrier.setName("USS Bidule");
		}
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
	}

	private void createCruiserEnemies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementEnemies(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

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
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
	}

	private void createMegaCruiserEnemies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(5000);
		oneCruiser.setComplementNorm(5000);
		ScoreKeeper.getInstance().addComplementEnemies(5000);
		oneCruiser.setTonnage(700000);
		oneCruiser.setCost(4000000000L);

		oneCruiser.setMaxSpeed(50);
		oneCruiser.setStandardSpeed(40);
		oneCruiser.setName("MMS Huristrous");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(20000);
		oneCruiser.setNbTorpedoes(400);
		oneCruiser.setResistance(70);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
	}

	int iCruiserOur = 0;
	
	private void createCruiserOur()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementOur(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
		//nbOurBoatCarrier--;
	}

	private void createCruiserOur2()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementOur(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

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
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(25);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
	}

	int iCruiserAllies = 0;
	
	private void createCruiserAllies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementAllies(500);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

		oneCruiser.setMaxSpeed(40);
		oneCruiser.setStandardSpeed(30);
		if (iCruiserAllies < PoolOfNames.namesAlliesCruiser.length)
		{
			oneCruiser.setName(PoolOfNames.namesAlliesCruiser[iCruiserAllies++]);
		}
		else
		{
			oneCruiser.setName("USS Bidule");
		}
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(1000);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(25);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalAllies++;
	}

	
	private void createDestroyerEnemies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.DESTROYER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(100);
		oneCruiser.setComplementNorm(100);
		ScoreKeeper.getInstance().addComplementEnemies(100);
		oneCruiser.setTonnage(22000);
		oneCruiser.setCost(40000000L);

		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(350);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
	}

	int iDestroyerOur = 0;
	
	private void createDestroyerOur()
	{

		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.DESTROYER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementOur(500);
		oneCruiser.setTonnage(20000);
		oneCruiser.setCost(100000000L);

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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
		//nbOurBoatCarrier--;
	}

	private void createDestroyerOur2()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.DESTROYER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(120);
		oneCruiser.setComplementNorm(120);
		ScoreKeeper.getInstance().addComplementOur(120);
		oneCruiser.setTonnage(20000);
		oneCruiser.setCost(80000000L);

		oneCruiser.setMaxSpeed(50);
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
		oneCruiser.setNbMissiles(330);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
	}

	int iDestroyerAllies = 0;
	
	private void createDestroyerAllies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.DESTROYER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(110);
		oneCruiser.setComplementNorm(110);
		ScoreKeeper.getInstance().addComplementAllies(110);
		oneCruiser.setTonnage(15000);
		oneCruiser.setCost(90000000L);

		oneCruiser.setMaxSpeed(52);
		oneCruiser.setStandardSpeed(30);
		if (iDestroyerAllies < PoolOfNames.namesAlliesDestroyers.length)
		{
			oneCruiser.setName(PoolOfNames.namesAlliesDestroyers[iDestroyerAllies++]);
		}
		else
		{
			oneCruiser.setName("USS Bidule");
		}
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(400);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(15);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalAllies++;
	}
	
	private void createFrigateEnemies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(50);
		oneCruiser.setComplementNorm(50);
		ScoreKeeper.getInstance().addComplementEnemies(50);
		oneCruiser.setTonnage(12000);
		oneCruiser.setCost(20000000L);

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
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
	}

	int iFrigateOur = 0;
	
	private void createFrigateOur()
	{

		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(60);
		oneCruiser.setComplementNorm(60);
		ScoreKeeper.getInstance().addComplementOur(60);
		oneCruiser.setTonnage(13000);
		oneCruiser.setCost(10000000L);

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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
		//nbOurBoatCarrier--;
	}

	private void createFrigateOur2()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(40);
		oneCruiser.setComplementNorm(40);
		ScoreKeeper.getInstance().addComplementOur(40);
		oneCruiser.setTonnage(10000);
		oneCruiser.setCost(8000000L);

		oneCruiser.setMaxSpeed(47);
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
		oneCruiser.setNbMissiles(80);
		oneCruiser.setNbTorpedoes(60);
		oneCruiser.setResistance(10);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
	}

	int iFrigateAllies=0;
	private void createFrigateAllies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.FRIGATE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(50);
		oneCruiser.setComplementNorm(50);
		ScoreKeeper.getInstance().addComplementAllies(50);
		oneCruiser.setTonnage(12000);
		oneCruiser.setCost(9000000L);

		oneCruiser.setMaxSpeed(50);
		oneCruiser.setStandardSpeed(30);
		if (iFrigateAllies < PoolOfNames.namesAlliesFrigate.length)
		{
			oneCruiser.setName(PoolOfNames.namesAlliesFrigate[iFrigateAllies++]);
		}
		else
		{
			oneCruiser.setName("USS Bidule");
		}
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(80);
		oneCruiser.setNbTorpedoes(50);
		oneCruiser.setResistance(8);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr-2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		
		ScoreKeeper.getInstance().nbBoatTotalAllies++;
	}
	
	private void createCorvetteEnemies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CORVETTE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(30);
		oneCruiser.setComplementNorm(30);
		ScoreKeeper.getInstance().addComplementEnemies(30);
		oneCruiser.setTonnage(6000);
		oneCruiser.setCost(10000000L);

		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		oneCruiser.setName("LNN Zork");
		oneCruiser.setFireAtWill(true);
		oneCruiser.setNbMissiles(50);
		oneCruiser.setNbTorpedoes(40);
		oneCruiser.setResistance(2);
		// And add the absolute coordinates.
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbEnemiesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
	}
	
	int iCorvetteAllies=0;
	
	private void createCorvetteAllies()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CORVETTE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(30);
		oneCruiser.setComplementNorm(30);
		ScoreKeeper.getInstance().addComplementAllies(30);
		oneCruiser.setTonnage(6000);
		oneCruiser.setCost(10000000L);

		oneCruiser.setMaxSpeed(48);
		oneCruiser.setStandardSpeed(30);
		if (iCorvetteAllies < PoolOfNames.namesAlliesCorvette.length)
		{
			oneCruiser.setName(PoolOfNames.namesAlliesCorvette[iCorvetteAllies++]);
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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbAlliesBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalAllies++;
	}
	
	int iCorvetteOur=0;
	
	private void createCorvetteOur()
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CORVETTE);
		oneCruiser.setTypeFaction(FUnit.BOAT_ENEMY);
		oneCruiser.setComplement(30);
		oneCruiser.setComplementNorm(30);
		ScoreKeeper.getInstance().addComplementOur(30);
		oneCruiser.setTonnage(6000);
		oneCruiser.setCost(10000000L);

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
		oneCruiser.setPosMap(xCurr, yCurr);
		oneCruiser.setPosMapTarget(xCurr+2, yCurr);
		oneCruiser.setFollowTargetMap(true);

		this.addBoat(oneCruiser);
		//nbOurBoatCarrier--;
		ScoreKeeper.getInstance().nbBoatTotalOur++;
	}
	
	private void createSubEnemies(int type)
	{
		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_ENEMY);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		ScoreKeeper.getInstance().addComplementEnemies(100);
		oneSub.setTonnage(10000);
		oneSub.setCost(3400000000L);

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
		ScoreKeeper.getInstance().nbSubTotalEnemies++;
	}

	int iSubOur=0;
	
	private void createSubOur(int type)
	{

		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_OUR);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		ScoreKeeper.getInstance().addComplementOur(100);
		oneSub.setTonnage(15000);
		oneSub.setCost(4000000000L);

		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		if (iSubOur < PoolOfNames.namesOurNukes.length)
		{
			oneSub.setName(PoolOfNames.namesOurNukes[iSubOur++]);
		}
		else
		{
			oneSub.setName("USS Bidule");
		}
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
		ScoreKeeper.getInstance().nbSubTotalOur++;
	}

	int iSubAllies = 0;
	
	private void createSubAllies(int type)
	{
		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_ALLIED);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		ScoreKeeper.getInstance().addComplementAllies(5000);
		oneSub.setTonnage(70000);
		oneSub.setCost(400000000L);

		oneSub.setMaxSpeed(40);
		oneSub.setStandardSpeed(30);
		if (iSubAllies < PoolOfNames.namesAlliesNukes.length)
		{
			oneSub.setName(PoolOfNames.namesAlliesNukes[iSubAllies++]);
		}
		else
		{
			oneSub.setName("USS Bidule");
		}
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
		
		ScoreKeeper.getInstance().nbSubTotalAllies++;
	}

	private void createSmallBaseEnemies()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASETWO);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(500);
		oneNewSmallBase.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementEnemies(500);
		oneNewSmallBase.setCost(16000000);

		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(100);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(5);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesSmallBases--;
		ScoreKeeper.getInstance().nbBasesTotalEnemies++;
	}

	private void createSmallBaseEnemies2()
	{
		// Add a small base here !!!
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.SMALLBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ENEMY);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(600);
		oneNewSmallBase.setComplementNorm(600);
		ScoreKeeper.getInstance().addComplementEnemies(600);
		oneNewSmallBase.setCost(18000000);

		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(120);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(7);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesSmallBases--;
		ScoreKeeper.getInstance().nbBasesTotalEnemies++;
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
		ScoreKeeper.getInstance().addComplementEnemies(2600);
		oneNewSmallBase.setCost(58000000);

		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(500);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(10);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesBigBases--;
		ScoreKeeper.getInstance().nbBasesTotalEnemies++;
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
		ScoreKeeper.getInstance().addComplementEnemies(4000);
		oneNewSmallBase.setCost(88000000);

		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(800);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(12);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesBigBases--;
		ScoreKeeper.getInstance().nbBasesTotalEnemies++;
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
		ScoreKeeper.getInstance().addComplementEnemies(10000);
		oneNewSmallBase.setCost(324000000);

		oneNewSmallBase.setNbAwacs(16);
		oneNewSmallBase.setNbFighters(2800);
		oneNewSmallBase.setNbTankers(28);
		oneNewSmallBase.setNbFightersOnFlightMax(18);
		oneNewSmallBase.setNbMissiles(16000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesMainBases--;
		ScoreKeeper.getInstance().nbBasesTotalEnemies++;
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
		ScoreKeeper.getInstance().addComplementEnemies(11000);
		oneNewSmallBase.setCost(344000000);

		oneNewSmallBase.setNbAwacs(16);
		oneNewSmallBase.setNbFighters(3000);
		oneNewSmallBase.setNbTankers(28);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		oneNewSmallBase.setNbMissiles(18000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbEnemiesMainBases--;
		ScoreKeeper.getInstance().nbBasesTotalEnemies++;
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
		ScoreKeeper.getInstance().addComplementOur(500);
		oneNewSmallBase.setCost(16000000);

		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(200);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(5);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		ScoreKeeper.getInstance().nbBasesTotalOur++;
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
		ScoreKeeper.getInstance().addComplementOur(600);
		oneNewSmallBase.setCost(18000000);

		oneNewSmallBase.setNbAwacs(3);
		oneNewSmallBase.setNbFighters(300);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(7);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		ScoreKeeper.getInstance().nbBasesTotalOur++;
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
		ScoreKeeper.getInstance().addComplementOur(2600);
		oneNewSmallBase.setCost(58000000);

		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(800);
		oneNewSmallBase.setNbTankers(10);
		oneNewSmallBase.setNbFightersOnFlightMax(10);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		ScoreKeeper.getInstance().nbBasesTotalOur++;
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
		ScoreKeeper.getInstance().addComplementOur(4000);
		oneNewSmallBase.setCost(88000000);

		oneNewSmallBase.setNbAwacs(6);
		oneNewSmallBase.setNbFighters(1200);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(11);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		ScoreKeeper.getInstance().nbBasesTotalOur++;
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
		ScoreKeeper.getInstance().addComplementOur(10000);
		oneNewSmallBase.setCost(324000000);

		oneNewSmallBase.setNbAwacs(10);
		oneNewSmallBase.setNbFighters(1600);
		oneNewSmallBase.setNbTankers(16);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		oneNewSmallBase.setNbMissiles(16000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		
		ScoreKeeper.getInstance().nbBasesTotalOur++;
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
		ScoreKeeper.getInstance().addComplementOur(11000);
		oneNewSmallBase.setCost(344000000);

		oneNewSmallBase.setNbAwacs(20);
		oneNewSmallBase.setNbFighters(2600);
		oneNewSmallBase.setNbTankers(20);
		oneNewSmallBase.setNbFightersOnFlightMax(21);
		oneNewSmallBase.setNbMissiles(18000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbOurMainBases--;
		ScoreKeeper.getInstance().nbBasesTotalOur++;
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
		ScoreKeeper.getInstance().addComplementAllies(500);
		oneNewSmallBase.setCost(16000000);

		oneNewSmallBase.setNbAwacs(6);
		oneNewSmallBase.setNbFighters(200);
		oneNewSmallBase.setNbTankers(4);
		oneNewSmallBase.setNbFightersOnFlightMax(5);

		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesSmallBases--;
		ScoreKeeper.getInstance().nbBasesTotalAllies++;
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
		ScoreKeeper.getInstance().addComplementAllies(600);
		oneNewSmallBase.setCost(18000000);

		oneNewSmallBase.setNbAwacs(4);
		oneNewSmallBase.setNbFighters(300);
		oneNewSmallBase.setNbTankers(4);
		oneNewSmallBase.setNbFightersOnFlightMax(7);
		oneNewSmallBase.setNbMissiles(1000);
		oneNewSmallBase.setResistance(4);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesSmallBases--;
		ScoreKeeper.getInstance().nbBasesTotalAllies++;
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
		ScoreKeeper.getInstance().addComplementAllies(2600);
		oneNewSmallBase.setCost(58000000);

		oneNewSmallBase.setNbAwacs(6);
		oneNewSmallBase.setNbFighters(1100);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(11);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesBigBases--;
		ScoreKeeper.getInstance().nbBasesTotalAllies++;
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
		ScoreKeeper.getInstance().addComplementAllies(4000);
		oneNewSmallBase.setCost(88000000);

		oneNewSmallBase.setNbAwacs(8);
		oneNewSmallBase.setNbFighters(1300);
		oneNewSmallBase.setNbTankers(18);
		oneNewSmallBase.setNbFightersOnFlightMax(12);
		oneNewSmallBase.setNbMissiles(8000);
		oneNewSmallBase.setResistance(50);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesBigBases--;
		ScoreKeeper.getInstance().nbBasesTotalAllies++;
	}

	private void createMainBaseAllies()
	{
		Base oneNewSmallBase = new Base();
		oneNewSmallBase.setType(Base.MAINBASEONE);
		oneNewSmallBase.setTypeFaction(FUnit.BASE_ALLIED);
		oneNewSmallBase.createName();
		oneNewSmallBase.setComplement(10000);
		oneNewSmallBase.setComplementNorm(10000);
		ScoreKeeper.getInstance().addComplementAllies(10000);
		oneNewSmallBase.setCost(324000000);

		oneNewSmallBase.setNbAwacs(16);
		oneNewSmallBase.setNbFighters(2200);
		oneNewSmallBase.setNbTankers(8);
		oneNewSmallBase.setNbFightersOnFlightMax(20);
		oneNewSmallBase.setNbMissiles(16000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesMainBases--;
		ScoreKeeper.getInstance().nbBasesTotalAllies++;
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
		ScoreKeeper.getInstance().addComplementAllies(11000);
		oneNewSmallBase.setCost(344000000);
		
		oneNewSmallBase.setNbAwacs(20);
		oneNewSmallBase.setNbFighters(2400);
		oneNewSmallBase.setNbTankers(20);
		oneNewSmallBase.setNbFightersOnFlightMax(21);
		oneNewSmallBase.setNbMissiles(18000);
		oneNewSmallBase.setResistance(150);
		// And add the absolute coordinates.
		oneNewSmallBase.setPosMap(xCurr, yCurr);

		this.addBase(oneNewSmallBase);
		//nbAlliesMainBases--;
		ScoreKeeper.getInstance().nbBasesTotalAllies++;
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
		ScoreKeeper.getInstance().addComplementEnemies(5000);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

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
		ScoreKeeper.getInstance().nbBoatTotalEnemies++;
	}

	private void addCruiserOur(ProgrammableUnit groupOwner)
	{

		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementOur(5000);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

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
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
	}

	private void addCruiserOur2(ProgrammableUnit groupOwner)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_OUR);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementOur(5000);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

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
		
		ScoreKeeper.getInstance().nbBoatTotalOur++;
	}

	private void addCruiserAllies(ProgrammableUnit groupOwner)
	{
		Boat oneCruiser = new Boat();
		oneCruiser.setType(Boat.CRUISER);
		oneCruiser.setTypeFaction(FUnit.BOAT_ALLIED);
		oneCruiser.setComplement(500);
		oneCruiser.setComplementNorm(500);
		ScoreKeeper.getInstance().addComplementAllies(5000);
		oneCruiser.setTonnage(70000);
		oneCruiser.setCost(400000000L);

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
		
		ScoreKeeper.getInstance().nbBoatTotalAllies++;
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
				ScoreKeeper.getInstance().addComplementEnemies(100);
				oneSub.setTonnage(10000);
				oneSub.setCost(3400000000L);

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
				ScoreKeeper.getInstance().addComplementEnemies(100);
				oneSub.setTonnage(10000);
				oneSub.setCost(3400000000L);

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
				ScoreKeeper.getInstance().addComplementEnemies(100);
				oneSub.setTonnage(10000);
				oneSub.setCost(3400000000L);

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
				ScoreKeeper.getInstance().addComplementEnemies(100);
				oneSub.setTonnage(10000);
				oneSub.setCost(3400000000L);

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
				ScoreKeeper.getInstance().addComplementEnemies(100);
				oneSub.setTonnage(10000);
				oneSub.setCost(3400000000L);

				System.out.println("Unknown submarine created");
				oneSub.setMaxSpeed(160);
				oneSub.setStandardSpeed(30);
				oneSub.setName("USS Default");
				oneSub.setFireAtWill(true);
				oneSub.setNbMissiles(20);
				oneSub.setNbTorpedoes(40);
				oneSub.setDepth(0);
				oneSub.setWantedDepth(0);
				oneSub.setResistance(5);
					
		}
		
		// And add the absolute coordinates.
		oneSub.setPosMap(xCurr, yCurr);
		oneSub.setPosMapTarget(xCurr+2, yCurr);
		oneSub.setFollowTargetMap(true);
		
		ScoreKeeper.getInstance().nbSubTotalEnemies++;

		groupOwner.getMyGroup().add(oneSub);
	}

	private void addSubOur(int type, ProgrammableUnit groupOwner)
	{

		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_OUR);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		ScoreKeeper.getInstance().addComplementOur(100);
		oneSub.setTonnage(15000);
		oneSub.setCost(4000000000L);

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
		
		ScoreKeeper.getInstance().nbSubTotalOur++;
	}

	private void addSubAllies(int type,ProgrammableUnit groupOwner) {
		Submarine oneSub = new Submarine();
		oneSub.setType(type);
		oneSub.setTypeFaction(FUnit.SUB_ALLIED);
		oneSub.setComplement(100);
		oneSub.setComplementNorm(100);
		ScoreKeeper.getInstance().addComplementAllies(5000);
		oneSub.setTonnage(70000);
		oneSub.setCost(400000000L);

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
		
		ScoreKeeper.getInstance().nbSubTotalAllies++;

		groupOwner.getMyGroup().add(oneSub);
	}
}
