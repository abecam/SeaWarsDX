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

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subengine.particlessystem.ParticlePC;
import com.tgb.subengine.Utils;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.LevelMap;

/**
 * Sonar sensor. Able to detect underwater, can be active or passive. An active sonar
 * give the sonar position...
 * @author Alain Becam
 *
 */
public class Sonar extends Sensor
{
	Emitter wavesEmitter;
	Particles waves;
	
	long idWaves;
	
	double detectionStrength;
	
	double xT2,yT2,xT3,yT3; // Coordinate (+ position of the Sonar), of the corner of the detection triangle
	
	Submarine tmpSub;
	Boat tmpBoat;
	Signal tmpSignal;
	double tmpDetect;
	
	ProgrammableUnit ourOwner;
	
	public void activate()
	{
		active=true;
		waves.validate();
	}
	
	public void desactivate()
	{
		active = false;
		waves.invalidate();
		removeFUnits();
		setUpDetectionTurn();
		if (existingSignal)
		{
			theMap.removeSignal(ourSignal);
			existingSignal = false;
		}
	}
	
	
	
	@Override
	public void removeMe() {
		// TODO Auto-generated method stub
		 RenderingManager.getInstance().removeParticles(idWaves, 18);
		 dead=true;
		 theMap.removeSignal(ourSignal);
		 removeFUnits();
	}

	@Override
	public void hide()
	{
		waves.invalidate();
	}
	
	@Override
	public void show()
	{
		if (this.active)
			waves.validate();
	}
	
	/**
	 * @param theMap
	 */
	public Sonar(LevelMap theMap)
	{
		super(theMap);
		
		this.setDepth(3);
		
		setUpDetectionTurn();

		this.myType=Sensor.SONAR;
		
		active = false;
	}
	
	/**
	 * @param theMap
	 */
	public Sonar(LevelMap theMap, ProgrammableUnit owner)
	{
		super(theMap);
		
		this.setDepth(3);
		
		setUpDetectionTurn();

		this.ourOwner=owner;
		
		if ((this.ourOwner.getTypeFaction()& 1) == 1)
		{
			if (this.ourOwner.isAutonomous())
				hiddenFUnits=true;
		}
		else
			hiddenFUnits=true;
		
		this.myType=Sensor.SONAR;
		
		active = false;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#createGfx(double, double, double, double, double, double)
	 */
	@Override
	public void createGfx(double xInit, double yInit, double zInit, double direction, double puissance, double speed)
	{
		// TODO Auto-generated method stub
		ParticlePC waveExample=new ParticlePC();
		waveExample.setAlive(true);
		waveExample.setEnergy(10);
		waveExample.setSize(2);
		waveExample.setWeight(10);
		waveExample.setTimeLeft(5000);
		waveExample.setMyColor(0xFF111111);
		waveExample.setColorAIncDec(-1);
		
		wavesEmitter = new Emitter(0, 0, this.orientation, this.angle, Emitter.POINT_DIRECTIONNAL, 10, 400, 0, 6*speed, 5000,waveExample);
		//wavesEmitter = new Emitter(0, 0, 2, 10, 10, this.orientation, this.angle, Emitter.POINT_DIRECTIONNAL, 10, 400, 0, 6*speed, 5000);
		waves=new Particles(100,wavesEmitter,ParticlePC.class);
		wavesEmitter.setPosAttach(xInit, yInit, zInit);
		if (!this.active)
			waves.invalidate();
		this.addAttachedObject(wavesEmitter);
		idWaves = RenderingManager.getInstance().addParticles(waves, 18);
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#doUpdate(double)
	 */
	@Override
	public void doUpdate(double time)
	{
		updateAttachedObjects();
		// If active, will find a lot more of units in the surrounding
		// More in direct front and close.
		// It also generates a signal.
		if (active)
		{
			ourSignal.setTimeCreation(time);
			ourSignal.setOrientation(this.orientationAttach);
			ourSignal.setPosOrigin(this.x, this.y, this.z);
			ourSignal.setType(Signal.SONAR);
			ourSignal.setIdOwner(this.idOwner);
			
			if (!existingSignal)
			{
				theMap.addSignal(ourSignal);
				existingSignal = true;
			}
			detectionStrength = 10;
		}
		else
			detectionStrength =5;
		
		this.setUpDetectionTurn();
		
		// Determine the detection triangle...
		xT2=this.x+detectionStrength*this.power*30*Math.cos(this.orientation+this.angle/2+this.getOrientationAttach());
		yT2=this.y+detectionStrength*this.power*30*Math.sin(this.orientation+this.angle/2+this.getOrientationAttach());
		xT3=this.x+detectionStrength*this.power*30*Math.cos(this.orientation-this.angle/2+this.getOrientationAttach());
		yT3=this.y+detectionStrength*this.power*30*Math.sin(this.orientation-this.angle/2+this.getOrientationAttach());
		
		ourSubs=theMap.getOurSubs();
		ourBoats=theMap.getOurBoats();
		alliesBoats=theMap.getAlliesBoats();
		alliesSubs=theMap.getAlliesSubs();
		enemiesSubs=theMap.getEnemiesSubs();
		enemiesBoats=theMap.getEnemiesBoats();
		neutralBoats=theMap.getNeutralBoats();
		neutralSubs=theMap.getNeutralSubs();
		signals=theMap.getCurrentSignals();

		
		// Look through the signals
		if (signals != null)
		{
			for (int iSignal = 0; iSignal < signals.size() ; iSignal++)
			{
				tmpSignal = signals.get(iSignal);
				// Update !!!
				if (((tmpSignal.getType() == Signal.LOUD_NOISE) || (tmpSignal.getType() == Signal.SONAR)) && (tmpSignal.getIdOwner() != this.idOwner))
				{
					FUnit oneFoundSign = new FUnit(hiddenFUnits);
					
					oneFoundSign.setX(tmpSignal.getXOrigin());
					oneFoundSign.setY(tmpSignal.getYOrigin());
					oneFoundSign.setSpeedKnown(false);
					oneFoundSign.setSpeedAccurate(false);
					oneFoundSign.setSpeedContact(0);
					oneFoundSign.setAngle(tmpSignal.getOrientation());
					oneFoundSign.setType(FUnit.UNKNOWN_SEA);
					tmpDetect = 10-Utils.distance(x, y, tmpSignal.getXOrigin(), tmpSignal.getYOrigin())/(detectionStrength*this.power*30)*10;
					//System.out.println("Level of detection of Sub "+tmpSub.getName()+" : "+tmpDetect);
					if (tmpDetect > 10)
						tmpDetect = 10;
					if (tmpDetect < 0)
						tmpDetect = 0;
					oneFoundSign.setLevelDetection((int )tmpDetect);		
					oneFoundSign.updateGfx();

					this.addFUnit(oneFoundSign);
				}
			}
			
		}
		{
			if (ourSubs != null)
			{
				for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
				{
					tmpSub = ourSubs.get(iSub);
					if (!tmpSub.isDead())
					{
						// Update !!!
						if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
						{
							FUnit oneFoundSub = new FUnit(hiddenFUnits);
							oneFoundSub.setSubFound(tmpSub);
							oneFoundSub.setX(tmpSub.getPosX());
							oneFoundSub.setY(tmpSub.getPosY());
							oneFoundSub.setSpeedKnown(true);
							oneFoundSub.setSpeedAccurate(false);
							oneFoundSub.setSpeedContact(tmpSub.getCurrentSpeed());
							oneFoundSub.setAngle(tmpSub.getOrientation());
							oneFoundSub.setType(FUnit.SUB_OUR);
							oneFoundSub.setIdPU(tmpSub.getIdMap());

							tmpDetect = 10-Utils.distance(x, y, tmpSub.getPosX(), tmpSub.getPosY())/(detectionStrength*this.power*30)*10;
							//System.out.println("Level of detection of Sub "+tmpSub.getName()+" : "+tmpDetect);
							if (tmpDetect > 10)
								tmpDetect = 10;
							if (tmpDetect < 0)
								tmpDetect = 0;
							oneFoundSub.setLevelDetection((int )tmpDetect);		
							oneFoundSub.updateGfx();

							this.addFUnit(oneFoundSub);
						}
					}
				}
			}
			if (ourBoats != null)
			{
				for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
				{		
					tmpBoat = ourBoats.get(iBoat);
					if (!tmpBoat.isDead())
					{
						// Update !!!
						if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
						{
							FUnit oneFoundBoat = new FUnit(hiddenFUnits);
							oneFoundBoat.setBoatFound(tmpBoat);
							oneFoundBoat.setX(tmpBoat.getPosX());
							oneFoundBoat.setY(tmpBoat.getPosY());
							oneFoundBoat.setSpeedKnown(true);
							oneFoundBoat.setSpeedAccurate(false);
							oneFoundBoat.setSpeedContact(tmpBoat.getCurrentSpeed());
							oneFoundBoat.setAngle(tmpBoat.getOrientation());
							oneFoundBoat.setType(FUnit.BOAT_OUR);
							oneFoundBoat.setIdPU(tmpBoat.getIdMap());

							tmpDetect = 10-Utils.distance(x, y, tmpBoat.getPosX(), tmpBoat.getPosY())/(detectionStrength*this.power*30)*10;
							//System.out.println("Level of detection of Sub "+tmpBoat.getName()+" : "+tmpDetect);
							if (tmpDetect > 10)
								tmpDetect = 10;
							if (tmpDetect < 0)
								tmpDetect = 0;
							oneFoundBoat.setLevelDetection((int )tmpDetect);		
							oneFoundBoat.updateGfx();

							this.addFUnit(oneFoundBoat);
						}
					}
				}
			}
			if (alliesSubs != null)
			{
				for (int iSub = 0; iSub < alliesSubs.size() ; iSub++)
				{
					tmpSub = alliesSubs.get(iSub);
					if (!tmpSub.isDead())
					{
						// Update !!!
						if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
						{
							FUnit oneFoundSub = new FUnit(hiddenFUnits);
							oneFoundSub.setSubFound(tmpSub);
							oneFoundSub.setX(tmpSub.getPosX());
							oneFoundSub.setY(tmpSub.getPosY());
							oneFoundSub.setSpeedKnown(true);
							oneFoundSub.setSpeedAccurate(false);
							oneFoundSub.setSpeedContact(tmpSub.getCurrentSpeed());
							oneFoundSub.setAngle(tmpSub.getOrientation());
							oneFoundSub.setType(FUnit.SUB_ALLIED);
							oneFoundSub.setIdPU(tmpSub.getIdMap());

							tmpDetect = 10-Utils.distance(x, y, tmpSub.getPosX(), tmpSub.getPosY())/(detectionStrength*this.power*30)*10;
							//System.out.println("Level of detection of Sub "+tmpSub.getName()+" : "+tmpDetect);
							if (tmpDetect > 10)
								tmpDetect = 10;
							if (tmpDetect < 0)
								tmpDetect = 0;
							oneFoundSub.setLevelDetection((int )tmpDetect);		
							oneFoundSub.updateGfx();

							this.addFUnit(oneFoundSub);
						}
					}
				}
			}
			if (alliesBoats != null)
			{
				for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
				{
					tmpBoat = alliesBoats.get(iBoat);
					if (!tmpBoat.isDead())
					{
						// Update !!!
						if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
						{
							FUnit oneFoundBoat = new FUnit(hiddenFUnits);
							oneFoundBoat.setBoatFound(tmpBoat);
							oneFoundBoat.setX(tmpBoat.getPosX());
							oneFoundBoat.setY(tmpBoat.getPosY());
							oneFoundBoat.setSpeedKnown(true);
							oneFoundBoat.setSpeedAccurate(false);
							oneFoundBoat.setSpeedContact(tmpBoat.getCurrentSpeed());
							oneFoundBoat.setAngle(tmpBoat.getOrientation());
							oneFoundBoat.setType(FUnit.BOAT_ALLIED);
							oneFoundBoat.setIdPU(tmpBoat.getIdMap());

							tmpDetect = 10-Utils.distance(x, y, tmpBoat.getPosX(), tmpBoat.getPosY())/(detectionStrength*this.power*30)*10;
							//System.out.println("Level of detection of Sub "+tmpBoat.getName()+" : "+tmpDetect);
							if (tmpDetect > 10)
								tmpDetect = 10;
							if (tmpDetect < 0)
								tmpDetect = 0;
							oneFoundBoat.setLevelDetection((int )tmpDetect);		
							oneFoundBoat.updateGfx();

							this.addFUnit(oneFoundBoat);
						}
					}
				}
			}
			if (enemiesSubs != null)
			{
				for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
				{
					tmpSub = enemiesSubs.get(iSub);
					if (!tmpSub.isDead())
					{
						// Update !!!
						if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
						{
							FUnit oneFoundSub = new FUnit(hiddenFUnits);
							oneFoundSub.setSubFound(tmpSub);
							oneFoundSub.setX(tmpSub.getPosX());
							oneFoundSub.setY(tmpSub.getPosY());
							oneFoundSub.setSpeedKnown(true);
							oneFoundSub.setSpeedAccurate(false);
							oneFoundSub.setSpeedContact(tmpSub.getCurrentSpeed());
							oneFoundSub.setAngle(tmpSub.getOrientation());
							oneFoundSub.setType(FUnit.SUB_ENEMY);
							oneFoundSub.setIdPU(tmpSub.getIdMap());

							tmpDetect = 10-Utils.distance(x, y, tmpSub.getPosX(), tmpSub.getPosY())/(detectionStrength*this.power*30)*10;
							//System.out.println("Level of detection of Sub "+tmpSub.getName()+" : "+tmpDetect);
							if (tmpDetect > 10)
								tmpDetect = 10;
							if (tmpDetect < 0)
								tmpDetect = 0;
							oneFoundSub.setLevelDetection((int )tmpDetect);		
							oneFoundSub.updateGfx();

							this.addFUnit(oneFoundSub);
						}
					}
				}
			}
			if (enemiesBoats != null)
			{
				for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
				{
					tmpBoat = enemiesBoats.get(iBoat);
					if (!tmpBoat.isDead())
					{
						// Update !!!
						if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
						{
							FUnit oneFoundBoat = new FUnit(hiddenFUnits);
							oneFoundBoat.setBoatFound(tmpBoat);
							oneFoundBoat.setX(tmpBoat.getPosX());
							oneFoundBoat.setY(tmpBoat.getPosY());
							oneFoundBoat.setSpeedKnown(true);
							oneFoundBoat.setSpeedAccurate(false);
							oneFoundBoat.setSpeedContact(tmpBoat.getCurrentSpeed());
							oneFoundBoat.setAngle(tmpBoat.getOrientation());
							oneFoundBoat.setType(FUnit.BOAT_ENEMY);
							oneFoundBoat.setIdPU(tmpBoat.getIdMap());

							tmpDetect = 10-Utils.distance(x, y, tmpBoat.getPosX(), tmpBoat.getPosY())/(detectionStrength*this.power*30)*10;
							//System.out.println("Level of detection of Sub "+tmpBoat.getName()+" : "+tmpDetect);
							if (tmpDetect > 10)
								tmpDetect = 10;
							if (tmpDetect < 0)
								tmpDetect = 0;
							oneFoundBoat.setLevelDetection((int )tmpDetect);		
							oneFoundBoat.updateGfx();

							this.addFUnit(oneFoundBoat);
						}
					}
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
		}
	}

	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}

}
