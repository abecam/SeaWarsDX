/*
 * Created on 30 nov 2008
 */
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

/**
 * Superclass for the different Level's maps.<br>
 * Copyright (c)2008 Alain Becam
 * @author Alain Becam
 */

package com.tgb.subgame;

import java.util.ArrayList;

import com.tgb.subengine.gameentities.WPDrawer;
import com.tgb.subengine.gamesystems.IGamePart;
import com.tgb.subengine.gamewrappers.CoreImage;
import com.tgb.subengine.gamewrappers.Scene2D;
import com.tgb.subengine.gfxentities.FlatSpritePC;
import com.tgb.subgame.levels.LevelLoaderPC;
import com.tgb.subgame.unitspc.Airplane;
import com.tgb.subgame.unitspc.Base;
import com.tgb.subgame.unitspc.Boat;
import com.tgb.subgame.unitspc.FUnit;
import com.tgb.subgame.unitspc.Missile;
import com.tgb.subgame.unitspc.Submarine;
import com.tgb.subgame.unitspc.Torpedo;
import com.tgb.subgame.unitspc.Trees;
import com.tgb.subgame.unitspc.sensors.KnownDatas;
import com.tgb.subgame.unitspc.sensors.Sensor;
import com.tgb.subgame.unitspc.sensors.Signal;

public class LevelMap
{

	protected LevelLoaderPC myLevelLoader;
	protected ArrayList<Submarine> ourSubs;
	protected ArrayList<Boat> ourBoats;
	protected ArrayList<Submarine> alliesSubs;
	protected ArrayList<Boat> alliesBoats;
	protected ArrayList<Submarine> enemiesSubs;
	protected ArrayList<Boat> enemiesBoats;
	protected ArrayList<Submarine> neutralSubs;
	protected ArrayList<Boat> neutralBoats;
	protected ArrayList<Airplane> ourAirplanes;
	protected ArrayList<Airplane> alliesAirplanes;
	protected ArrayList<Airplane> enemiesAirplanes;
	protected ArrayList<Airplane> neutralAirplanes;
	protected ArrayList<Base> ourBases;
	protected ArrayList<Base> alliesBases;
	protected ArrayList<Base> enemiesBases;
	protected ArrayList<Base> neutralBases;
	protected ArrayList<Sensor> globalSensors;
	protected ArrayList<Signal> currentSignals;
	protected ArrayList<Missile> missiles;
	protected ArrayList<Torpedo> torpedoes;
	
	protected ArrayList<Trees> trees;
	
	protected int nbOurSubs = 0;
	protected int nbOurBoats = 0;
	protected int nbAlliesSubs = 0;
	protected int nbAlliesBoats = 0;
	protected int nbTotalGood = 0;
	protected int nbEnemiesSubs = 0;
	protected int nbEnemiesBoats = 0;
	protected int nbEnemiesTotal = 0;
	protected int nbOurAirplanes;
	protected int nbEnemiesAirplanes;
	protected int nbAlliesAirplanes;
	int nbOurBases;
	int nbEnemiesBases;
	int nbAlliesBases;
	protected long nbSignal = 0;
	protected Missile tmpMissile;
	protected Torpedo tmpTorpedo;
	protected Airplane tmpAirplane;
	protected Trees tmpTree;
	protected int currentLevel = 1;
	protected long lastTime;
	protected long tmpTime;
	protected long timeInterval;
	protected double timeUsed;
	int idTorpedo = 0;
	int idMissile = 0;
	int idBoat = 0;
	protected Scene2D ourScene;
	protected CoreImage levelImage;
	protected FlatSpritePC spriteLevel;
	protected long idSpriteLevel;
	protected KnownDatas ourKD;
	
	protected boolean isOutOfAmmo = false;
	protected boolean notShooting = false;
	
	double timeSinceLastShoot = 0;
	
	WPDrawer myWPDrawer;

	boolean isSubMap = false; // We need to know if we count the subs or the surface units.
	
	public LevelMap()
	{
		;
	}

	public void setLevel(int level)
	{
		System.out.println("Start to load level");
		myLevelLoader.loadLevel(level);
		
		myLevelLoader.addOurSubs();
		myLevelLoader.addOurBoats();
		myLevelLoader.addAlliesSubs();
		myLevelLoader.addAlliesBoats();
		myLevelLoader.addEnemiesSubs();
		myLevelLoader.addEnemiesBoats();
		myLevelLoader.addNeutralSubs();
		myLevelLoader.addNeutralBoats();
		myLevelLoader.addGlobalSensor();
		myLevelLoader.addOthers();
		
		this.globalSensors.add(ourKD);
		
		System.out.println("Level loaded");
	}

	/**
	 * @return the alliesBoats
	 */
	public ArrayList<Boat> getAlliesBoats()
	{
		return alliesBoats;
	}

	/**
	 * @param alliesBoats the alliesBoats to set
	 */
	public void setAlliesBoats(ArrayList<Boat> alliesBoats)
	{
		this.alliesBoats = alliesBoats;
	}

	/**
	 * @return the alliesSubs
	 */
	public ArrayList<Submarine> getAlliesSubs()
	{
		return alliesSubs;
	}

	/**
	 * @param alliesSubs the alliesSubs to set
	 */
	public void setAlliesSubs(ArrayList<Submarine> alliesSubs)
	{
		this.alliesSubs = alliesSubs;
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

	/**
	 * @return the enemiesBoats
	 */
	public ArrayList<Boat> getEnemiesBoats()
	{
		return enemiesBoats;
	}

	/**
	 * @param enemiesBoats the enemiesBoats to set
	 */
	public void setEnemiesBoats(ArrayList<Boat> enemiesBoats)
	{
		this.enemiesBoats = enemiesBoats;
	}

	/**
	 * @return the enemiesSubs
	 */
	public ArrayList<Submarine> getEnemiesSubs()
	{
		return enemiesSubs;
	}

	/**
	 * @param oneSub the oneSub to add
	 */
	public void addEnemiesSubs(Submarine oneSub)
	{
		oneSub.setIdBoat(idBoat++);
		
		for (int iSensors=0;iSensors < oneSub.getSensors().size();++iSensors)
		{
			oneSub.getSensors().get(iSensors).setIdOwner(oneSub.getIdBoat());
		}
		
		enemiesSubs.add(oneSub);
		nbEnemiesSubs++;
		if (isSubMap)
		{
			nbEnemiesTotal++;
		}
	}

	/**
	 * @return the neutralBoats
	 */
	public ArrayList<Boat> getNeutralBoats()
	{
		return neutralBoats;
	}

	/**
	 * @param neutralBoats the neutralBoats to set
	 */
	public void setNeutralBoats(ArrayList<Boat> neutralBoats)
	{
		this.neutralBoats = neutralBoats;
	}

	/**
	 * @return the neutralSubs
	 */
	public ArrayList<Submarine> getNeutralSubs()
	{
		return neutralSubs;
	}

	/**
	 * @param neutralSubs the neutralSubs to set
	 */
	public void setNeutralSubs(ArrayList<Submarine> neutralSubs)
	{
		this.neutralSubs = neutralSubs;
	}

	/**
	 * @return the ourBoats
	 */
	public ArrayList<Boat> getOurBoats()
	{
		return ourBoats;
	}

	/**
	 * @param ourBoats the ourBoats to set
	 */
	public void setOurBoats(ArrayList<Boat> ourBoats)
	{
		this.ourBoats = ourBoats;
	}

	/**
	 * @return the ourSubs
	 */
	public ArrayList<Submarine> getOurSubs()
	{
		return ourSubs;
	}

	/**
	 * @param ourSubs the ourSubs to set
	 */
	public void setOurSubs(ArrayList<Submarine> ourSubs)
	{
		this.ourSubs = ourSubs;
	}

	public void addSub(Submarine oneSub)
	{
		oneSub.setIdBoat(idBoat++);
		
		for (int iSensors=0;iSensors < oneSub.getSensors().size();++iSensors)
		{
			oneSub.getSensors().get(iSensors).setIdOwner(oneSub.getIdBoat());
		}
		
		switch(oneSub.getTypeFaction())
		{
			case FUnit.SUB_ALLIED: 
				alliesSubs.add(oneSub);
				if (!oneSub.isDead())
				{
					nbAlliesSubs++;
					
					// Count the sub only if it's a map for us!
					if (isSubMap)
					{
						nbTotalGood++;
					}
				}
				break;
			case FUnit.SUB_ENEMY: 
				enemiesSubs.add(oneSub);
				if (!oneSub.isDead())
				{
					nbEnemiesSubs++;
					
					// But we still need to get rid of the enemies subs :)
					nbEnemiesTotal++;
				}
				break;
			case FUnit.SUB_OUR: 
				ourSubs.add(oneSub);
				if (!oneSub.isDead())
				{
					nbOurSubs++;
					if (isSubMap)
					{
						nbTotalGood++;
					}
				}
				break;
		}
	}

	/**
	 * TODO: adapt to the 2 different tactical maps: do not count the other units
	 * @param oneBoat
	 */
	public void addBoat(Boat oneBoat)
	{
		oneBoat.setIdBoat(idBoat++);
		
		for (int iSensors=0;iSensors < oneBoat.getSensors().size();++iSensors)
		{
			oneBoat.getSensors().get(iSensors).setIdOwner(oneBoat.getIdBoat());
		}
		
		switch(oneBoat.getTypeFaction())
		{
			case FUnit.BOAT_ALLIED: 
				alliesBoats.add(oneBoat);
				if (!oneBoat.isDead())
				{
					nbAlliesBoats++;
					if (!isSubMap)
					{
						nbTotalGood++;
					}
				}
				break;
			case FUnit.BOAT_ENEMY: 
				enemiesBoats.add(oneBoat);
				if (!oneBoat.isDead())
				{
					nbEnemiesBoats++;
					nbEnemiesTotal++;
				}
				break;
			case FUnit.BOAT_OUR: 
				ourBoats.add(oneBoat);
				if (!oneBoat.isDead())
				{
					nbAlliesBoats++;
					if (!isSubMap)
					{
						nbTotalGood++;
					}
				}
				break;
		}
	}

	public void addAirplane(Airplane oneAirplane)
	{
		oneAirplane.setIdBoat(idBoat++);
		switch(oneAirplane.getTypeFaction())
		{
			case FUnit.AIRPLANE_ALLIED: 
				alliesAirplanes.add(oneAirplane);
				nbAlliesAirplanes++;
				//nbTotalGood++;
				break;
			case FUnit.AIRPLANE_ENEMY: 
				enemiesAirplanes.add(oneAirplane);
				nbEnemiesAirplanes++;
				//nbEnemiesTotal++;
				break;
			case FUnit.AIRPLANE_OUR: 
				ourAirplanes.add(oneAirplane);
				nbAlliesAirplanes++;
				//nbTotalGood++;
				break;
		}
	}

	public void addBase(Base oneBase)
	{
		oneBase.setIdBoat(idBoat++);
		switch(oneBase.getTypeFaction())
		{
			case FUnit.BASE_ALLIED: 
				alliesBases.add(oneBase);
				if (!oneBase.isDead())
				{
					nbAlliesBases++;
					nbTotalGood++;
				}
				break;
			case FUnit.BASE_ENEMY: 
				enemiesBases.add(oneBase);
				if (!oneBase.isDead())
				{
					nbEnemiesBases++;
					nbEnemiesTotal++;
				}
				break;
			case FUnit.BASE_OUR: 
				ourBases.add(oneBase);
				if (!oneBase.isDead())
				{
					nbAlliesBases++;
					nbTotalGood++;
				}
				break;
		}
	}
	
	public void addTree(Trees oneTree)
	{
		//oneTree.setIdBoat(-1);
		trees.add(oneTree);
	}

	public void subDead(int typeFaction)
	{
		switch(typeFaction)
		{
			case FUnit.SUB_ALLIED: 
				nbAlliesSubs--;
				if (isSubMap)
				{
					nbTotalGood--;
				}
				break;
			case FUnit.SUB_ENEMY: 
				nbEnemiesSubs--;
				nbEnemiesTotal--;
				break;
			case FUnit.SUB_OUR: 
				nbOurSubs--;
				if (isSubMap)
				{
					nbTotalGood--;
				}
				break;
		}
		if (nbEnemiesTotal == 1)
		{
			// If only one enemy left, it can target everything!
			if (enemiesSubs != null)
			{
				for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
				{
					//System.out.println("Sub "+enemiesSubs.get(iSub).getName()+" become agressive");
					enemiesSubs.get(iSub).setIgnoreFriends(true);
				}
			}
			if (enemiesBoats != null)
			{
				for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
				{
					if (enemiesBoats.get(iBoat).getType() != Boat.CARRIER)
					{
						//System.out.println("Boat "+enemiesBoats.get(iBoat).getName()+" become agressive");
						enemiesBoats.get(iBoat).setIgnoreFriends(true);
					}
				}
			}
		}
	}

	public void boatDead(int typeFaction)
	{
		switch(typeFaction)
		{
		case FUnit.BOAT_ALLIED: 
			nbAlliesBoats--;
			if (!isSubMap)
			{
				nbTotalGood--;
			}
			break;
		case FUnit.BOAT_ENEMY: 
			nbEnemiesBoats--;
			nbEnemiesTotal--;
			break;
		case FUnit.BOAT_OUR: 
			nbAlliesBoats--;
			if (!isSubMap)
			{
				nbTotalGood--;
			}
			break;
		}
		if (nbEnemiesTotal == 1)
		{
			// If only one enemy left, it can target everything!
			if (enemiesSubs != null)
			{
				for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
				{
					//System.out.println("Sub "+enemiesSubs.get(iSub).getName()+" become agressive");
					enemiesSubs.get(iSub).setIgnoreFriends(true);
				}
			}
			if (enemiesBoats != null)
			{
				for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
				{
					if (enemiesBoats.get(iBoat).getType() != Boat.CARRIER)
					{
						//System.out.println("Boat "+enemiesBoats.get(iBoat).getName()+" become agressive");
						enemiesBoats.get(iBoat).setIgnoreFriends(true);
					}
				}
			}
		}
	}

	public void baseDead(int typeFaction)
	{
			switch(typeFaction)
			{
				case FUnit.BASE_ALLIED: 
					nbAlliesBases--;
					nbTotalGood--;
					break;
				case FUnit.BASE_ENEMY: 
					nbEnemiesBases--;
					nbEnemiesTotal--;
					break;
				case FUnit.BASE_OUR: 
					nbAlliesBases--;
					nbTotalGood--;
					break;
			}
			if (nbEnemiesTotal == 1)
			{
				// If only one enemy left, it can target everything!
				if (enemiesSubs != null)
				{
					for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
					{
						System.out.println("Sub "+enemiesSubs.get(iSub).getName()+" become agressive");
						enemiesSubs.get(iSub).setIgnoreFriends(true);
					}
				}
				if (enemiesBoats != null)
				{
					for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
					{
						System.out.println("Boat "+enemiesBoats.get(iBoat).getName()+" become agressive");
						enemiesBoats.get(iBoat).setIgnoreFriends(true);
					}
				}
	//			if (enemiesBases != null)
	//			{
	//				for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
	//				{
	//					System.out.println("Base "+enemiesBases.get(iBoat).getName()+" become agressive");
	//					enemiesBases.get(iBoat).setIgnoreFriends(true);
	//				}
	//			}
			}
		}

	public ArrayList<Torpedo> getTorpedoes()
	{
		return torpedoes;
	}

	public void setTorpedoes(ArrayList<Torpedo> torpedoes)
	{
		this.torpedoes = torpedoes;
	}

	public void addTorpedo(Torpedo oneTorpedo)
	{
		oneTorpedo.setIdMissile(idTorpedo++);
		torpedoes.add(oneTorpedo);
	}

	public ArrayList<Missile> getMissiles()
	{
		return missiles;
	}

	public void setMissiles(ArrayList<Missile> missiles)
	{
		this.missiles = missiles;
	}

	public void addMissile(Missile oneMissile)
	{
		oneMissile.setIdMissile(idMissile++);
		missiles.add(oneMissile);
	}

	public ArrayList<Airplane> getOurAirplanes()
	{
		return ourAirplanes;
	}

	public void setOurAirplanes(ArrayList<Airplane> ourAirplanes)
	{
		this.ourAirplanes = ourAirplanes;
	}

	public ArrayList<Airplane> getAlliesAirplanes()
	{
		return alliesAirplanes;
	}

	public void setAlliesAirplanes(ArrayList<Airplane> alliesAirplanes)
	{
		this.alliesAirplanes = alliesAirplanes;
	}

	public ArrayList<Airplane> getEnemiesAirplanes()
	{
		return enemiesAirplanes;
	}

	public void setEnemiesAirplanes(ArrayList<Airplane> enemiesAirplanes)
	{
		this.enemiesAirplanes = enemiesAirplanes;
	}

	public ArrayList<Airplane> getNeutralAirplanes()
	{
		return neutralAirplanes;
	}

	public void setNeutralAirplanes(ArrayList<Airplane> neutralAirplanes)
	{
		this.neutralAirplanes = neutralAirplanes;
	}

	public ArrayList<Base> getOurBases()
	{
		return ourBases;
	}

	public void setOurBases(ArrayList<Base> ourBases)
	{
		this.ourBases = ourBases;
	}

	public ArrayList<Base> getAlliesBases()
	{
		return alliesBases;
	}

	public void setAlliesBases(ArrayList<Base> alliesBases)
	{
		this.alliesBases = alliesBases;
	}

	public ArrayList<Base> getEnemiesBases()
	{
		return enemiesBases;
	}

	public void setEnemiesBases(ArrayList<Base> enemiesBases)
	{
		this.enemiesBases = enemiesBases;
	}

	public ArrayList<Base> getNeutralBases()
	{
		return neutralBases;
	}

	public void setNeutralBases(ArrayList<Base> neutralBases)
	{
		this.neutralBases = neutralBases;
	}

	public ArrayList<Trees> getTrees()
	{
		return trees;
	}

	public KnownDatas getOurKD()
	{
		return ourKD;
	}

	public void setOurKD(KnownDatas ourKD)
	{
		this.ourKD = ourKD;
	}

	public void addGlobalSensor(Sensor oneSensor)
	{
		this.globalSensors.add(oneSensor);
	}

	public ArrayList<Sensor> getGlobalSensors()
	{
		return globalSensors;
	}

	public WPDrawer getMyWPDrawer() {
		return myWPDrawer;
	}

	public void addSignal(Signal oneSignal)
	{
		currentSignals.add(oneSignal);
	}

	public void removeSignal(Signal oneSignal)
	{
		currentSignals.remove(oneSignal);
	}

	public void notOutOfAmmo(int idBoat2)
	{
		isOutOfAmmo = false;
	}
	
	public void didShoot()
	{
		notShooting = false;
	}
}