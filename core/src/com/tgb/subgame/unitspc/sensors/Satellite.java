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

package com.tgb.subgame.unitspc.sensors;

import com.tgb.subgame.unitspc.FUnit;
import com.tgb.subgame.unitspc.ProgrammableUnit;
import com.tgb.subgame.unitspc.Submarine;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.*;
import com.tgb.subgame.LevelMap;

/**
 * Satellite sensor. Detect on the whole map, including close to surface submarines, but with rare updates
 * 
 * @author Alain Becam
 *
 */
public class Satellite extends Sensor
{
	Emitter wavesEmitter;
	Particles waves;
	double ourTime; // time since last update
	double tmpDetect;
	
	long idWaves;
	
	Submarine tmpSub;
	ProgrammableUnit tmpBoat;

	@Override
	public void removeMe() {
		// TODO Auto-generated method stub
		 RenderingManager.getInstance().removeParticles(idWaves, 30);
		 dead=true;
		 removeFUnits();
	}
	
	/**
	 * @param theMap
	 */
	public Satellite(LevelMap theMap)
	{
		super(theMap);
		this.setDepth(1);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#createGfx(double, double, double, double, double, double)
	 */
	@Override
	public void createGfx(double xInit, double yInit, double zInit, double direction, double puissance, double speed)
	{
		//ParticleLine myNewParticle = new ParticleLine();
		wavesEmitter = new Emitter(xInit, yInit, 2, 10, 10, direction, 0, Emitter.POINT_DIRECTIONNAL, 4, 1000, 0, 6*speed, 10000);
		waves=new Particles(10,wavesEmitter,ParticleLinePC.class);
		wavesEmitter.setPosAttach(xInit, yInit, zInit);
		waves.invalidate();
		//this.addAttachedObject(wavesEmitter);
		RenderingManager.getInstance().addParticles(waves, 30);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#doUpdate(double)
	 */
	@Override
	public void doUpdate(double time)
	{
		ourTime+=time;
		if (ourTime > 1)
			waves.invalidate();
		if (ourTime > 300)
		{
			this.setUpDetectionTurn();
			
			ourTime=0;
			waves.validate();
			updateAttachedObjects();
			
			ourSubs=theMap.getOurSubs();
			ourBoats=theMap.getOurBoats();
			alliesBoats=theMap.getAlliesBoats();
			alliesSubs=theMap.getAlliesSubs();
			enemiesSubs=theMap.getEnemiesSubs();
			enemiesBoats=theMap.getEnemiesBoats();
			neutralBoats=theMap.getNeutralBoats();
			neutralSubs=theMap.getNeutralSubs();
			enemiesBases=theMap.getEnemiesBases();
			alliesBases=theMap.getAlliesBases();
			neutralBases=theMap.getNeutralBases();
			ourBases=theMap.getOurBases();

			{
				if (ourSubs != null)
				{
					for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
					{
						tmpSub = ourSubs.get(iSub);

						if (tmpSub.getDepth() > -10)
						{
							FUnit oneFoundSub = new FUnit();
							// Update !!!
							oneFoundSub.setSubFound(tmpSub);
							oneFoundSub.setX(tmpSub.getPosX());
							oneFoundSub.setY(tmpSub.getPosY());
							oneFoundSub.setAngle(tmpSub.getOrientation());
							oneFoundSub.setType(FUnit.SUB_OUR);
							tmpDetect=-tmpSub.getDepth()/2;
							if (tmpDetect > 5)
								tmpDetect = 5;
							if (tmpDetect < 0)
								tmpDetect = 0; 
							oneFoundSub.setLevelDetection((int )tmpDetect);		
							oneFoundSub.updateGfx();

							this.addFUnit(oneFoundSub);
						}
					}
				}
				if (ourBoats != null)
				{
					for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
					{
						// Update !!!
						FUnit oneFoundBoat = new FUnit();

						oneFoundBoat.setBoatFound(ourBoats.get(iBoat));
						oneFoundBoat.setX(ourBoats.get(iBoat).getPosX());
						oneFoundBoat.setY(ourBoats.get(iBoat).getPosY());
						oneFoundBoat.setAngle(ourBoats.get(iBoat).getOrientation());
						oneFoundBoat.setType(FUnit.BOAT_OUR);
						oneFoundBoat.setLevelDetection(5);				
						oneFoundBoat.updateGfx();
						
						this.addFUnit(oneFoundBoat);
					}
				}
				if (alliesSubs != null)
				{
					for (int iSub = 0; iSub < alliesSubs.size() ; iSub++)
					{
						tmpSub = alliesSubs.get(iSub);

						if (tmpSub.getDepth() > -10)
						{
							FUnit oneFoundSub = new FUnit();
							// Update !!!
							oneFoundSub.setSubFound(tmpSub);
							oneFoundSub.setX(tmpSub.getPosX());
							oneFoundSub.setY(tmpSub.getPosY());
							oneFoundSub.setAngle(tmpSub.getOrientation());
							oneFoundSub.setType(FUnit.SUB_ALLIED);
							tmpDetect=-tmpSub.getDepth()/2;
							if (tmpDetect > 5)
								tmpDetect = 5;
							if (tmpDetect < 0)
								tmpDetect = 0; 
							oneFoundSub.setLevelDetection((int )tmpDetect);		
							oneFoundSub.updateGfx();

							this.addFUnit(oneFoundSub);
						}
					}
				}
				if (alliesBoats != null)
				{
					for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
					{
						FUnit oneFoundBoat = new FUnit();

						oneFoundBoat.setBoatFound(alliesBoats.get(iBoat));
						oneFoundBoat.setX(alliesBoats.get(iBoat).getPosX());
						oneFoundBoat.setY(alliesBoats.get(iBoat).getPosY());
						oneFoundBoat.setAngle(alliesBoats.get(iBoat).getOrientation());
						oneFoundBoat.setType(FUnit.BOAT_ALLIED);
						oneFoundBoat.setLevelDetection(5);				
						oneFoundBoat.updateGfx();
						
						this.addFUnit(oneFoundBoat);
					}
				}
				if (enemiesSubs != null)
				{
					for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
					{
						tmpSub = enemiesSubs.get(iSub);

						if (tmpSub.getDepth() > -10)
						{
							FUnit oneFoundSub = new FUnit();
							// Update !!!
							oneFoundSub.setSubFound(tmpSub);
							oneFoundSub.setX(tmpSub.getPosX());
							oneFoundSub.setY(tmpSub.getPosY());
							oneFoundSub.setAngle(tmpSub.getOrientation());
							oneFoundSub.setType(FUnit.SUB_ENEMY);
							tmpDetect=-tmpSub.getDepth()/2;
							if (tmpDetect > 5)
								tmpDetect = 5;
							if (tmpDetect < 0)
								tmpDetect = 0; 
							oneFoundSub.setLevelDetection((int )tmpDetect);		
							oneFoundSub.updateGfx();

							this.addFUnit(oneFoundSub);
						}
					}
				}
				if (enemiesBoats != null)
				{
					for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
					{
						FUnit oneFoundBoat = new FUnit();

						oneFoundBoat.setBoatFound(enemiesBoats.get(iBoat));
						oneFoundBoat.setX(enemiesBoats.get(iBoat).getPosX());
						oneFoundBoat.setY(enemiesBoats.get(iBoat).getPosY());
						oneFoundBoat.setAngle(enemiesBoats.get(iBoat).getOrientation());
						oneFoundBoat.setType(FUnit.BOAT_ENEMY);
						oneFoundBoat.setLevelDetection(5);				
						oneFoundBoat.updateGfx();
						
						this.addFUnit(oneFoundBoat);
					}
				}
				if (neutralSubs != null)
				{
					for (int iSub = 0; iSub < neutralSubs.size() ; iSub++)
					{
						// Update !!!
						neutralSubs.get(iSub);
					}
				}
				if (neutralBoats != null)
				{
					for (int iBoat = 0; iBoat < neutralBoats.size() ; iBoat++)
					{
						// Update !!!
						neutralBoats.get(iBoat);
					}
				}
				if (ourBases != null)
				{
					for (int iBoat = 0; iBoat < ourBases.size() ; iBoat++)
					{
						if (!ourBases.get(iBoat).isDead())
						{
							FUnit oneFoundBase = new FUnit();

							oneFoundBase.setX(ourBases.get(iBoat).getPosX());
							oneFoundBase.setY(ourBases.get(iBoat).getPosY());
							oneFoundBase.setAngle(ourBases.get(iBoat).getOrientation());
							oneFoundBase.setType(FUnit.BASE_OUR);
							oneFoundBase.setLevelDetection(5);				
							oneFoundBase.updateGfx();

							this.addFUnit(oneFoundBase);
						}
					}
				}
				if (alliesBases != null)
				{
					for (int iBoat = 0; iBoat < alliesBases.size() ; iBoat++)
					{
						if (!alliesBases.get(iBoat).isDead())
						{
							FUnit oneFoundBase = new FUnit();

							oneFoundBase.setX(alliesBases.get(iBoat).getPosX());
							oneFoundBase.setY(alliesBases.get(iBoat).getPosY());
							oneFoundBase.setAngle(alliesBases.get(iBoat).getOrientation());
							oneFoundBase.setType(FUnit.BASE_ALLIED);
							oneFoundBase.setLevelDetection(5);				
							oneFoundBase.updateGfx();

							this.addFUnit(oneFoundBase);
						}
					}
				}
				if (enemiesBases != null)
				{
					for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
					{
						if (!enemiesBases.get(iBoat).isDead())
						{
							FUnit oneFoundBase = new FUnit();

							oneFoundBase.setX(enemiesBases.get(iBoat).getPosX());
							oneFoundBase.setY(enemiesBases.get(iBoat).getPosY());
							oneFoundBase.setAngle(enemiesBases.get(iBoat).getOrientation());
							oneFoundBase.setType(FUnit.BASE_ENEMY);
							oneFoundBase.setLevelDetection(5);				
							oneFoundBase.updateGfx();

							this.addFUnit(oneFoundBase);
						}
					}
				}
			}
		}
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Cannot be activated/desactivated
	 */
	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void desactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}
}
