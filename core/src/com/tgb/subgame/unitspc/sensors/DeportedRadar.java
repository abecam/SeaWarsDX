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
import com.tgb.subengine.gfxentities.SimpleLinePC;
import com.tgb.subengine.Utils;
import com.tgb.subgame.unitspc.*;
import com.tgb.subgame.LevelMap;

/**
 * Radar sensor. 
 * @author Alain Becam
 *
 */
public class DeportedRadar extends Sensor
{
	boolean existingSignal=false; // Is a signal currently emitted?
	
	double speedRot=5;
	
	long idLine;
	
	double xT2,yT2,xT3,yT3; // Coordinate (+ position of the Sonar), of the corner of the detection triangle
	
	Submarine tmpSub;
	Boat tmpBoat;
	Airplane tmpAirplane;
	Missile tmpMissile;
	double tmpDetect;
	
	ProgrammableUnit ourOwner;
	
	SimpleLinePC detectLine1,detectLine2,detectLine3; // To draw the detection triangle
	
	boolean debugView = false; // Do we see the seeking lines
	
	long idDL1,idDL2,idDL3,idTL;
	
	public static final int typeSensor=3;
	
	@Override
	public void activate()
	{
		active=true;
	}
	
	@Override
	public void desactivate()
	{
		active = false;
		existingSignal = false; // The signal will disappear.
		removeFUnits();
		setUpDetectionTurn();
	}
	
	@Override
	public void removeMe() {
		// TODO Auto-generated method stub
		 RenderingManager.getInstance().removeEntity(idLine, 22);
		 dead=true;
		 theMap.removeSignal(ourSignal);
		 
		 removeFUnits();

		 RenderingManager.getInstance().removeEntity(idDL1, 39);
		 RenderingManager.getInstance().removeEntity(idDL2, 39);
		 RenderingManager.getInstance().removeEntity(idDL3, 39);
		 RenderingManager.getInstance().removeEntity(idTL, 39);
	}
	
	@Override
	public void hide()
	{
		;
	}
	
	@Override
	public void show()
	{
		;
	}
	
	/**
	 * @param theMap
	 */
	public DeportedRadar(LevelMap theMap, ProgrammableUnit owner)
	{
		super(theMap);
		
		detectLine1 = new SimpleLinePC();
		detectLine2 = new SimpleLinePC();
		detectLine3 = new SimpleLinePC();
		
		if (!debugView)
		{
			detectLine1.invalidate();
			detectLine2.invalidate();
			detectLine3.invalidate();
		}

		idDL1 = RenderingManager.getInstance().addDrawableEntity(detectLine1,39);
		idDL2 = RenderingManager.getInstance().addDrawableEntity(detectLine2,39);
		idDL3 = RenderingManager.getInstance().addDrawableEntity(detectLine3,39);
		
		this.setDepth(1);
		
		setUpDetectionTurn();

		this.ourOwner=owner;
		
		if ((this.ourOwner.getTypeFaction()& 1) == 1)
		{
			if (this.ourOwner.isAutonomous())
				hiddenFUnits=true;
		}
		else
			hiddenFUnits=true;
		
		myType=RADAR;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#createGfx(double, double, double, double, double, double)
	 */
	@Override
	public void createGfx(double xInit, double yInit, double zInit, double direction, double puissance, double speed)
	{
		// TODO Auto-generated method stub
		; // None
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.sensors.Sensor#doUpdate(double)
	 */
	@Override
	public void doUpdate(double time)
	{
		updateAttachedObjects();
		
		if (active)
		{
			this.orientation+= time*speedRot;
			
			ourSignal.setTimeCreation(time);
			ourSignal.setOrientation(this.orientationAttach);
			ourSignal.setPosOrigin(this.x, this.y, this.z);
			ourSignal.setType(Signal.RADAR);

			if (!existingSignal)
			{
				theMap.addSignal(ourSignal);
				existingSignal = true;
			}


			if (this.orientation > 2*Math.PI)
			{
				this.orientation=0;
				this.setUpDetectionTurn();
			}

			// Determine the detection triangle...
			xT2=this.x+this.power*100*Math.cos(this.orientation+this.angle/2+this.getOrientationAttach());
			yT2=this.y+this.power*100*Math.sin(this.orientation+this.angle/2+this.getOrientationAttach());
			xT3=this.x+this.power*100*Math.cos(this.orientation-this.angle/2+this.getOrientationAttach());
			yT3=this.y+this.power*100*Math.sin(this.orientation-this.angle/2+this.getOrientationAttach());

			if (debugView)
			{
				detectLine1.setPos(this.x, this.y, 0);
				detectLine1.setPosEnd(xT2,yT2);

				detectLine2.setPos(this.x, this.y, 0);
				detectLine2.setPosEnd(xT3,yT3);

				detectLine3.setPos(xT3, yT3, 0);
				detectLine3.setPosEnd(xT2,yT2);
			}
			
			ourSubs=theMap.getOurSubs();
			ourBoats=theMap.getOurBoats();
			alliesBoats=theMap.getAlliesBoats();
			alliesSubs=theMap.getAlliesSubs();
			enemiesSubs=theMap.getEnemiesSubs();
			enemiesBoats=theMap.getEnemiesBoats();
			neutralBoats=theMap.getNeutralBoats();
			neutralSubs=theMap.getNeutralSubs();
			missiles=theMap.getMissiles();
			ourAirplanes=theMap.getOurAirplanes();
			alliesAirplanes=theMap.getAlliesAirplanes();
			enemiesAirplanes=theMap.getEnemiesAirplanes();
			neutralAirplanes=theMap.getNeutralAirplanes();

			{
				if (ourSubs != null)
				{
					for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
					{
						tmpSub = ourSubs.get(iSub);
						if ((tmpSub.getDepth() > -10) && (tmpSub.getIdBoat() != this.getIdOwner()))
						{
							// Update !!!
							if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundSub = new FUnit(hiddenFUnits);
								oneFoundSub.setSubFound(tmpSub);
								oneFoundSub.setX(tmpSub.getPosX());
								oneFoundSub.setY(tmpSub.getPosY());
								oneFoundSub.setSpeedKnown(true);
								oneFoundSub.setSpeedAccurate(true);
								oneFoundSub.setSpeedContact(tmpSub.getCurrentSpeed());
								oneFoundSub.setAngle(tmpSub.getOrientation());
								oneFoundSub.setType(FUnit.SUB_OUR);

								tmpDetect = 10-Utils.distance(x, y, tmpSub.getPosX(), tmpSub.getPosY())/(this.power*30)*10;
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
						if ((tmpBoat.getDepth() > -10) && (tmpBoat.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundBoat = new FUnit(hiddenFUnits);
								oneFoundBoat.setBoatFound(tmpBoat);
								oneFoundBoat.setX(tmpBoat.getPosX());
								oneFoundBoat.setY(tmpBoat.getPosY());
								oneFoundBoat.setSpeedKnown(true);
								oneFoundBoat.setSpeedAccurate(true);
								oneFoundBoat.setSpeedContact(tmpBoat.getCurrentSpeed());
								oneFoundBoat.setAngle(tmpBoat.getOrientation());
								oneFoundBoat.setType(FUnit.BOAT_OUR);

								tmpDetect = 10-Utils.distance(x, y, tmpBoat.getPosX(), tmpBoat.getPosY())/(this.power*30)*10;
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
						// Update !!!
						if ((tmpSub.getDepth() > -10) && (tmpSub.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundSub = new FUnit(hiddenFUnits);
								oneFoundSub.setSubFound(tmpSub);
								oneFoundSub.setX(tmpSub.getPosX());
								oneFoundSub.setY(tmpSub.getPosY());
								oneFoundSub.setSpeedKnown(true);
								oneFoundSub.setSpeedAccurate(true);
								oneFoundSub.setSpeedContact(tmpSub.getCurrentSpeed());
								oneFoundSub.setAngle(tmpSub.getOrientation());
								oneFoundSub.setType(FUnit.SUB_ALLIED);

								tmpDetect = 10-Utils.distance(x, y, tmpSub.getPosX(), tmpSub.getPosY())/(this.power*30)*10;
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
						if ((tmpBoat.getDepth() > -10) && (tmpBoat.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundBoat = new FUnit(hiddenFUnits);
								oneFoundBoat.setBoatFound(tmpBoat);
								oneFoundBoat.setX(tmpBoat.getPosX());
								oneFoundBoat.setY(tmpBoat.getPosY());
								oneFoundBoat.setSpeedKnown(true);
								oneFoundBoat.setSpeedAccurate(true);
								oneFoundBoat.setSpeedContact(tmpBoat.getCurrentSpeed());
								oneFoundBoat.setAngle(tmpBoat.getOrientation());
								oneFoundBoat.setType(FUnit.BOAT_ALLIED);

								tmpDetect = 10-Utils.distance(x, y, tmpBoat.getPosX(), tmpBoat.getPosY())/(this.power*30)*10;
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
						// Update !!!
						if ((tmpSub.getDepth() > -10) && (tmpSub.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundSub = new FUnit(hiddenFUnits);
								oneFoundSub.setSubFound(tmpSub);
								oneFoundSub.setX(tmpSub.getPosX());
								oneFoundSub.setY(tmpSub.getPosY());
								oneFoundSub.setSpeedKnown(true);
								oneFoundSub.setSpeedAccurate(true);
								oneFoundSub.setSpeedContact(tmpSub.getCurrentSpeed());
								oneFoundSub.setAngle(tmpSub.getOrientation());
								oneFoundSub.setType(FUnit.SUB_ENEMY);

								tmpDetect = 10-Utils.distance(x, y, tmpSub.getPosX(), tmpSub.getPosY())/(this.power*30)*10;
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
						if ((tmpBoat.getDepth() > -10) && (tmpBoat.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundBoat = new FUnit(hiddenFUnits);
								oneFoundBoat.setBoatFound(tmpBoat);
								oneFoundBoat.setX(tmpBoat.getPosX());
								oneFoundBoat.setY(tmpBoat.getPosY());
								oneFoundBoat.setSpeedKnown(true);
								oneFoundBoat.setSpeedAccurate(true);
								oneFoundBoat.setSpeedContact(tmpBoat.getCurrentSpeed());
								oneFoundBoat.setAngle(tmpBoat.getOrientation());
								oneFoundBoat.setType(FUnit.BOAT_ENEMY);

								tmpDetect = 10-Utils.distance(x, y, tmpBoat.getPosX(), tmpBoat.getPosY())/(this.power*30)*10;
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
				if (enemiesBoats != null)
				{
					for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
					{
						tmpBoat = enemiesBoats.get(iBoat);
						if ((tmpBoat.getDepth() > -10) && (tmpBoat.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundBoat = new FUnit(hiddenFUnits);
								oneFoundBoat.setBoatFound(tmpBoat);
								oneFoundBoat.setX(tmpBoat.getPosX());
								oneFoundBoat.setY(tmpBoat.getPosY());
								oneFoundBoat.setSpeedKnown(true);
								oneFoundBoat.setSpeedAccurate(true);
								oneFoundBoat.setSpeedContact(tmpBoat.getCurrentSpeed());
								oneFoundBoat.setAngle(tmpBoat.getOrientation());
								oneFoundBoat.setType(FUnit.BOAT_ENEMY);

								tmpDetect = 10-Utils.distance(x, y, tmpBoat.getPosX(), tmpBoat.getPosY())/(this.power*30)*10;
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
				if (ourAirplanes != null)
				{
					for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
					{
						tmpAirplane = ourAirplanes.get(iBoat);
						if ((tmpAirplane.getDepth() > -10) && (tmpAirplane.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpAirplane.getPosX(), tmpAirplane.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundBoat = new FUnit(true);
								//oneFoundBoat.setBoatFound(tmpAirplane);
								oneFoundBoat.setX(tmpAirplane.getPosX());
								oneFoundBoat.setY(tmpAirplane.getPosY());
								oneFoundBoat.setSpeedKnown(true);
								oneFoundBoat.setSpeedAccurate(true);
								oneFoundBoat.setSpeedContact(tmpAirplane.getCurrentSpeed()/20);
								oneFoundBoat.setAngle(tmpAirplane.getOrientation());
								oneFoundBoat.setType(FUnit.AIRPLANE_OUR);

								tmpDetect = 10-Utils.distance(x, y, tmpAirplane.getPosX(), tmpAirplane.getPosY())/(this.power*30)*10;
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
				if (alliesAirplanes != null)
				{
					for (int iBoat = 0; iBoat < alliesAirplanes.size() ; iBoat++)
					{
						tmpAirplane = alliesAirplanes.get(iBoat);
						if ((tmpAirplane.getDepth() > -10) && (tmpAirplane.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpAirplane.getPosX(), tmpAirplane.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundBoat = new FUnit(true);
								//oneFoundBoat.setBoatFound(tmpAirplane);
								oneFoundBoat.setX(tmpAirplane.getPosX());
								oneFoundBoat.setY(tmpAirplane.getPosY());
								oneFoundBoat.setSpeedKnown(true);
								oneFoundBoat.setSpeedAccurate(true);
								oneFoundBoat.setSpeedContact(tmpAirplane.getCurrentSpeed()/20);
								oneFoundBoat.setAngle(tmpAirplane.getOrientation());
								oneFoundBoat.setType(FUnit.AIRPLANE_ALLIED);

								tmpDetect = 10-Utils.distance(x, y, tmpAirplane.getPosX(), tmpAirplane.getPosY())/(this.power*30)*10;
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
				if (enemiesAirplanes != null)
				{
					for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
					{
						tmpAirplane = enemiesAirplanes.get(iBoat);
						if ((tmpAirplane.getDepth() > -10) && (tmpAirplane.getIdBoat() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpAirplane.getPosX(), tmpAirplane.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
							{
								FUnit oneFoundBoat = new FUnit(hiddenFUnits);
								//oneFoundBoat.setBoatFound(tmpAirplane);
								oneFoundBoat.setX(tmpAirplane.getPosX());
								oneFoundBoat.setY(tmpAirplane.getPosY());
								oneFoundBoat.setSpeedKnown(true);
								oneFoundBoat.setSpeedAccurate(true);
								oneFoundBoat.setSpeedContact(tmpAirplane.getCurrentSpeed()/20);
								oneFoundBoat.setAngle(tmpAirplane.getOrientation());
								oneFoundBoat.setType(FUnit.AIRPLANE_ENEMY);

								tmpDetect = 10-Utils.distance(x, y, tmpAirplane.getPosX(), tmpAirplane.getPosY())/(this.power*30)*10;
								//System.out.println("Level of detection of Airplane "+tmpAirplane.getName()+" : "+tmpDetect);
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
				if (missiles != null)
				{
					for (int iMissile = 0; iMissile < missiles.size() ; iMissile++)
					{
						tmpMissile = missiles.get(iMissile);

						if (Utils.isInTriangle(tmpMissile.getPosX(), tmpMissile.getPosY(), this.x, this.y, xT2, yT2, xT3, yT3))
						{
							//FUnit oneFoundMiss = new FUnit(hiddenFUnits);
							// Always hidden, or too many infos on screen
							FUnit oneFoundMiss = new FUnit(true);
							oneFoundMiss.setX(tmpMissile.getPosX());
							oneFoundMiss.setY(tmpMissile.getPosY());
							oneFoundMiss.setSpeedKnown(true);
							oneFoundMiss.setSpeedAccurate(true);
							oneFoundMiss.setSpeedContact(tmpMissile.getCurrentSpeed()/20);
							oneFoundMiss.setAngle(tmpMissile.getOrientation());
							oneFoundMiss.setType(FUnit.MISSILE);
							oneFoundMiss.setTypeFaction(tmpMissile.getTypeFaction());

							tmpDetect = 10-Utils.distance(x, y, tmpMissile.getPosX(), tmpMissile.getPosY())/(this.power*30)*10;
							//System.out.println("Level of detection of Miss "+tmpBoat.getName()+" : "+tmpDetect);
							if (tmpDetect > 10)
								tmpDetect = 10;
							if (tmpDetect < 0)
								tmpDetect = 0;
							oneFoundMiss.setLevelDetection((int )tmpDetect);		
							oneFoundMiss.updateGfx();

							this.addFUnit(oneFoundMiss);
						}

					}
				}
			}
		}
	}

	public double getSpeedRot() {
		return speedRot;
	}

	public void setSpeedRot(double speedRot) {
		this.speedRot = speedRot;
	}

	public boolean isDebugView() {
		return debugView;
	}

	public void setDebugView(boolean debugView) {
		this.debugView = debugView;
		if (debugView)
		{
			detectLine1.validate();
			detectLine2.validate();
			detectLine3.validate();
		}
		else
		{
			detectLine1.invalidate();
			detectLine2.invalidate();
			detectLine3.invalidate();
		}
	}

	@Override
	public void setAttachableColor(int newColor)
	{
		// TODO Auto-generated method stub
		
	}

}
