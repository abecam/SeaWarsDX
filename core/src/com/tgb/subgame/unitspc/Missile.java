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

package com.tgb.subgame.unitspc;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import pulpcore.image.CoreImage;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.Utils;
import com.tgb.subengine.UtilsPC;
import com.tgb.subengine.gfxentities.SimpleLinePC;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.gfxentities.SpritePCAdd;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.ParticleSpritePC;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.unitspc.sensors.ISignalCreator;
import com.tgb.subgame.unitspc.sensors.Signal;

/**
 * A Missile. Due to the speed, the sensors are faked in it (not "actual ones" like in
 * other units)
 * @author Alain Becam
 *
 */
public class Missile extends ProgrammableUnit implements ISignalCreator
{

	int idMissile;
	
	double alt=0;
	double orientation;
	
	double xShift,yShift;
	
	double currentSpeed=0; // In knots
	double speedX,speedY,speedZ=0;
	
	double wantedSpeedX,wantedSpeedY,wantedSpeedZ=0;
	double tmpSpeedX,tmpSpeedY,tmpSpeedZ,tmpSpeedN=0;
	
	double maxSpeed=2000; // 800; // In knots
	double standardSpeed= 1200; // 400;
	double accelerationMax=200; // In knots/sec
	
	double energyLeft=3;
	
	int noiseLevel; // 0-> None, 100-> enough :)
	int visibilityLevel;
	
	int damage=80; // Damage potential
	double damageRadiusSq=2000; // Square of damage radius !!!
	double damageRadiusSqOtherMissile=500; // We can assume that other missiles are either at a different altitude or too fast to be touched.
	
	double stopTargetingRadiusSq=100;
	
	int damageType; // To be defined
	
	
	SpritePC missileBody;
	SpritePCAdd explosion,explosion2;
	Emitter smokeEmitter; // For enemies's torpedoes, both the torpedo and the bubbles will appear if found (by gravitation or passive/active sonar)
	Particles smokes;
	
	long idBody;
	long idSmoke;
	long idExplosion,idExplosion2; // Generally, only one flash will be used in the same time!
	
	public static final int ASM=0x70;
	public static final int ASW=0x71;
	public static final int ALM=0x72;
	public static final int AA=0x73;
	public static final int CRUISE=0x74;
	
	boolean seek=true; // Does this missile seek the enemy (or is WP-Target Coord based only, explose on arrival)
	boolean seeking=false; // Are we actually seeking?
	boolean wpbased=false;
	boolean target=true;
	boolean knowAllies=true; // Ignore allies' units?
	boolean knowEnemies=false; // Or enemies
	
	double distToTarget;
	
	double detectionStrength; // For seeking missile, strength of its sensor
	double power=10; // Power of its sensor.
	boolean active=false; // Might be passive or active. Typical passive sensor will be for Radar-seeking missiles !
	
	Signal ourSignal;
	boolean existingSignal=false; // Is a signal currently emitted?
	double angle= Math.PI/4; // Angle of detection
	
	SimpleLinePC detectLine1,detectLine2,detectLine3; // To draw the detection triangle
	SimpleLinePC targetLine;
	
	boolean debugView = false; // Do we see the seeking lines
	
	long idDL1,idDL2,idDL3,idTL;
	/* 
	 * From the tactical map
	 */
	ArrayList<Submarine> ourSubs;
	ArrayList<Boat> ourBoats;
	ArrayList<Submarine> alliesSubs;
	ArrayList<Boat> alliesBoats;
	ArrayList<Submarine> enemiesSubs;
	ArrayList<Boat> enemiesBoats;
	ArrayList<Submarine> neutralSubs;
	ArrayList<Boat> neutralBoats;
	ArrayList<Airplane> ourAirplanes;
	ArrayList<Airplane> alliesAirplanes;
	ArrayList<Airplane> enemiesAirplanes;
	ArrayList<Airplane> neutralAirplanes;
	ArrayList<Missile> missiles;
	
	ArrayList<Base> ourBases;
	ArrayList<Base> alliesBases;
	ArrayList<Base> enemiesBases;
	ArrayList<Base> neutralBases;
	
	ArrayList<Trees> trees;
	
	boolean exploding=false; // Tell the updater (TacticalMap) to explode us!
	boolean exploded=false; // Damage done?
	boolean toRemove=false; // Will be removed from TacticalMap
	int explodeTime=0;
	
	String Name;
	
	LevelMap theMap;
	/**
	 * @param theMap
	 */
	public Missile(LevelMap theMap)
	{
		super(theMap);
		this.theMap=theMap;
		
		ourSignal = new Signal();
		
		detectLine1 = new SimpleLinePC();
		detectLine2 = new SimpleLinePC();
		detectLine3 = new SimpleLinePC();
		targetLine = new SimpleLinePC();
		detectLine1.invalidate();
		detectLine2.invalidate();
		detectLine3.invalidate();
		if (!debugView)
			targetLine.invalidate();
		
		idDL1 = RenderingManager.getInstance().addDrawableEntity(detectLine1,39);
		idDL2 = RenderingManager.getInstance().addDrawableEntity(detectLine2,39);
		idDL3 = RenderingManager.getInstance().addDrawableEntity(detectLine3,39);
		idTL  = RenderingManager.getInstance().addDrawableEntity(targetLine,39);

		// Default
		type = ASM;
		
		wantedSpeed= maxSpeed;
	}
	
	
	/**
	 * @return the accelerationMax
	 */
	public double getAccelerationMax()
	{
		return accelerationMax;
	}

	/**
	 * @param accelerationMax the accelerationMax to set
	 */
	public void setAccelerationMax(double accelerationMax)
	{
		this.accelerationMax = accelerationMax;
	}

	/**
	 * @return the alt
	 */
	public double getAlt()
	{
		return alt;
	}

	/**
	 * @param alt the alt to set
	 */
	public void setAlt(double alt)
	{
		this.alt = alt;
	}

	/**
	 * @return the currentSpeed
	 */
	public double getCurrentSpeed()
	{
		return currentSpeed;
	}

	/**
	 * @param currentSpeed the currentSpeed to set
	 */
	public void setCurrentSpeed(double currentSpeed)
	{
		this.currentSpeed = currentSpeed;
		speedX=currentSpeed*Math.cos(this.orientation);
		speedY=currentSpeed*Math.sin(this.orientation);
	}

	/**
	 * @return the damage
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	/**
	 * @return the maxSpeed
	 */
	public double getMaxSpeed()
	{
		return maxSpeed;
	}

	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(double maxSpeed)
	{
		this.maxSpeed = maxSpeed;
	}

	/**
	 * @return the noiseLevel
	 */
	public int getNoiseLevel()
	{
		return noiseLevel;
	}

	/**
	 * @param noiseLevel the noiseLevel to set
	 */
	public void setNoiseLevel(int noiseLevel)
	{
		this.noiseLevel = noiseLevel;
	}

	/**
	 * @return the orientation
	 */
	public double getOrientation()
	{
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(double orientation)
	{
		this.orientation = orientation;
	}

	/**
	 * @return the standardSpeed
	 */
	public double getStandardSpeed()
	{
		return standardSpeed;
	}

	/**
	 * @param standardSpeed the standardSpeed to set
	 */
	public void setStandardSpeed(double standardSpeed)
	{
		this.standardSpeed = standardSpeed;
	}

	/**
	 * @return the visibilityLevel
	 */
	public int getVisibilityLevel()
	{
		return visibilityLevel;
	}

	/**
	 * @param visibilityLevel the visibilityLevel to set
	 */
	public void setVisibilityLevel(int visibilityLevel)
	{
		this.visibilityLevel = visibilityLevel;
	}

	public boolean isKnowAllies() {
		return knowAllies;
	}


	public void setKnowAllies(boolean knowAllies) {
		this.knowAllies = knowAllies;
	}


	public boolean isKnowEnemies() {
		return knowEnemies;
	}


	public void setKnowEnemies(boolean knowEnemies) {
		this.knowEnemies = knowEnemies;
	}


	public boolean isSeek() {
		return seek;
	}


	public void setSeek(boolean seek) {
		this.seek = seek;
	}


	public boolean isWpbased() {
		return wpbased;
	}


	public void setWpbased(boolean wpbased) {
		this.wpbased = wpbased;
	}


	public boolean isTarget() {
		return target;
	}


	public void setTarget(boolean target) {
		this.target = target;
	}


	/**
	 * @return the idMissile
	 */
	public int getIdMissile()
	{
		return idMissile;
	}

	/**
	 * @param idMissile the idMissile to set
	 */
	public void setIdMissile(int idMissile)
	{
		this.idMissile = idMissile;
	}
	
	public Signal getOurSignal() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOurSignal(Signal ourSignal) {
		// TODO Auto-generated method stub
		
	}

	public boolean isExploding() {
		return exploding;
	}


	public boolean isDead() {
		return dead;
	}
	
	public void explode() {
		exploding=true;
		dead=true;

		explosion.setPos(this.posX, this.posY, 0);
		explosion.validate();
	}


	public void kill() {
		dead= true;
	}


	/**
	 * Return the default image for the missile.
	 * @return the correct image
	 */
	public static CoreImage getImageForMe()
	{
		return gfxSprites.getImageSub();
	}
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use the second
	 * method, with Image as parameter: so the image is shared, not loaded for all.
	 */
	public void createGfx(double x, double y, double z, double direction, double speed)
	{
		
		CoreImage missileImage=gfxSprites.getImageSub();
		
		createGfx(missileImage, x, y, z, direction, speed);
	}
	
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfx(CoreImage missileImage, double x, double y, double z, double direction, double speed)
	{	
		
		explosion = new SpritePCAdd(gfxSprites.getImageFlash2());
		explosion2 = new SpritePCAdd(gfxSprites.getImageFlash());
		explosion.invalidate();
		explosion2.invalidate();
		explosion.setSize(2);
		
		idExplosion= RenderingManager.getInstance().addDrawableEntity(explosion, 30);
		
		missileBody = new SpritePC(missileImage);
		missileBody.setRotation(direction);

		missileBody.setPos(x, y, z);
		missileBody.setSize(0.2);
		idBody = RenderingManager.getInstance().addDrawableEntity(missileBody,30); // High
		
		ParticleSpritePC newParticle= new ParticleSpritePC(gfxSprites.getImageSmoke());
		
		
		smokeEmitter = new Emitter(x, y, 0.1, 10, 10, Math.PI, 0.1, Emitter.POINT_DIRECTIONNAL, 1, 5, 0, speed/10, 200);
		smokes=new Particles(50,smokeEmitter,newParticle);
		missileBody.addAttachedObject(smokeEmitter);
	    
	    idSmoke = RenderingManager.getInstance().addParticles(smokes, 30);
	}
	
	/**
	 * Update the torpedo
	 * NB: We do not simulate the torpedo here (yet)
	 */
	public void updateMe(double x, double y, double z, double direction, double speed)
	{
		missileBody.setRotation(direction);
		missileBody.setPos(x, y, z);
		smokeEmitter.setSpeed(speed/20);
	}
	
	public void hideMe()
	{
		missileBody.invalidate();
		smokes.invalidate();
	}
	
	public void showMe()
	{
		missileBody.validate();
		smokes.validate();
	}

	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMe()
	{
		RenderingManager.getInstance().removeEntity(idBody, 30);
		RenderingManager.getInstance().removeParticles(idSmoke, 30);
		RenderingManager.getInstance().removeParticles(idExplosion, 30);
		
		RenderingManager.getInstance().removeEntity(idDL1, 39);
		RenderingManager.getInstance().removeEntity(idDL2, 39);
		RenderingManager.getInstance().removeEntity(idDL3, 39);
		RenderingManager.getInstance().removeEntity(idTL, 39);
		theMap.removeSignal(ourSignal);
	}
	
	public void accX(double xAcc)
	{
		speedX+=xAcc;
		//checkAndNormaliseSpeed();
	}
	
	public void accY(double yAcc)
	{
		speedY+=yAcc;
		//checkAndNormaliseSpeed();
	}
	

	public boolean seek(double time)
	{
		// For this type of missile: front radar-based.
		double xT2,yT2,xT3,yT3; // Coordinate (+ position of the Missile), of the corner of the detection triangle
		
		Submarine tmpSub;
		Boat tmpBoat;
		Airplane tmpAirplane;
		ProgrammableUnit tmpBase;
		
		Missile tmpMissile;
		double distanceTmp;
		
		
		boolean found=false;
		double distanceFound = 100000; // Reset the distance of detection.
		
		// If active, will find a lot more of units in the surrounding
		// More in direct front and close.
		// It also generates a signal.

		ourSignal.setTimeCreation(time);
		ourSignal.setOrientation(this.orientation);
		ourSignal.setPosOrigin(this.posX, this.posY, this.alt);
		ourSignal.setType(Signal.RADAR);

		if (!existingSignal)
		{
			theMap.addSignal(ourSignal);
			existingSignal = true;
		}
		detectionStrength = 1;

		
		// Determine the detection triangle...
		xT2=this.posX+detectionStrength*this.power*30*Math.cos(this.orientation+this.angle/2);
		yT2=this.posY+detectionStrength*this.power*30*Math.sin(this.orientation+this.angle/2);
		xT3=this.posX+detectionStrength*this.power*30*Math.cos(this.orientation-this.angle/2);
		yT3=this.posY+detectionStrength*this.power*30*Math.sin(this.orientation-this.angle/2);
		
		detectLine1.setPos(this.posX, this.posY, 0);
		detectLine1.setPosEnd(xT2,yT2);
		
		detectLine2.setPos(this.posX, this.posY, 0);
		detectLine2.setPosEnd(xT3,yT3);
		
		detectLine3.setPos(xT3, yT3, 0);
		detectLine3.setPosEnd(xT2,yT2);
		
		
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
		
		enemiesBases=theMap.getEnemiesBases();
		alliesBases=theMap.getAlliesBases();
		neutralBases=theMap.getNeutralBases();
		ourBases=theMap.getOurBases();
		
		{
			if (!knowAllies)
			{
				if (ourSubs != null)
				{
					for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
					{
						tmpSub = ourSubs.get(iSub);
						// Update !!!
						if (tmpSub.getDepth() > -10)
						{
							if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
								//System.out.println("Found our sub - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpSub.getPosX();
									this.targetY = tmpSub.getPosY();
									this.targetDepth = tmpSub.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (ourBoats != null)
				{
					for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
					{		
						tmpBoat = ourBoats.get(iBoat);
						if (tmpBoat.getDepth() > -1)
						{
							if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
								//System.out.println("Found our boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpBoat.getPosX();
									this.targetY = tmpBoat.getPosY();
									this.targetDepth = tmpBoat.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (alliesBoats != null)
				{
					for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
					{		
						tmpBoat = alliesBoats.get(iBoat);
						if (tmpBoat.getDepth() > -1)
						{
							if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
								//System.out.println("Found our boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpBoat.getPosX();
									this.targetY = tmpBoat.getPosY();
									this.targetDepth = tmpBoat.getDepth();

									found = true;
								}
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
						if (tmpSub.getDepth() > -10)
						{
							if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
								//System.out.println("Found allie sub - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpSub.getPosX();
									this.targetY = tmpSub.getPosY();
									this.targetDepth = tmpSub.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (ourAirplanes != null)
				{
					for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
					{
						tmpAirplane = ourAirplanes.get(iBoat);
						if (tmpAirplane.getDepth() > -1)
						{
							if (Utils.isInTriangle(tmpAirplane.getPosX(), tmpAirplane.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
								//System.out.println("Found allies boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpAirplane.getPosX();
									this.targetY = tmpAirplane.getPosY();
									this.targetDepth = tmpAirplane.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (alliesAirplanes != null)
				{
					for (int iBoat = 0; iBoat < alliesAirplanes.size() ; iBoat++)
					{
						tmpAirplane = alliesAirplanes.get(iBoat);
						if (tmpAirplane.getDepth() > -1)
						{
							if (Utils.isInTriangle(tmpAirplane.getPosX(), tmpAirplane.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
								//System.out.println("Found allies boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpAirplane.getPosX();
									this.targetY = tmpAirplane.getPosY();
									this.targetDepth = tmpAirplane.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (alliesBases != null)
				{
					for (int iBoat = 0; iBoat < alliesBases.size() ; iBoat++)
					{
						tmpBase = alliesBases.get(iBoat);
						if ((tmpBase.getDepth() > -1) && !tmpBase.isDead())
						{
							if (Utils.isInTriangle(tmpBase.getPosX(), tmpBase.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpBase.getPosX(), tmpBase.getPosY());
								//System.out.println("Found allies boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpBase.getPosX();
									this.targetY = tmpBase.getPosY();
									this.targetDepth = tmpBase.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (ourBases != null)
				{
					for (int iBoat = 0; iBoat < ourBases.size() ; iBoat++)
					{
						tmpBase = ourBases.get(iBoat);
						if ((tmpBase.getDepth() > -1) && !tmpBase.isDead())
						{
							if (Utils.isInTriangle(tmpBase.getPosX(), tmpBase.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpBase.getPosX(), tmpBase.getPosY());

								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpBase.getPosX();
									this.targetY = tmpBase.getPosY();
									this.targetDepth = tmpBase.getDepth();

									found = true;
								}
							}
						}
					}
				}
			}
			if (!knowEnemies)
			{
				if (enemiesSubs != null)
				{
					for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
					{
						tmpSub = enemiesSubs.get(iSub);
						// Update !!!
						if (tmpSub.getDepth() > -10)
						{
							if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
								//System.out.println("Found enemy sub - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpSub.getPosX();
									this.targetY = tmpSub.getPosY();
									this.targetDepth = tmpSub.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (enemiesBoats != null)
				{
					for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
					{
						tmpBoat = enemiesBoats.get(iBoat);
						if (tmpBoat.getDepth() > -1)
						{
							if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
								//System.out.println("Found enemy boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpBoat.getPosX();
									this.targetY = tmpBoat.getPosY();
									this.targetDepth = tmpBoat.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (enemiesAirplanes != null)
				{
					for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
					{
						tmpAirplane = enemiesAirplanes.get(iBoat);
						if (tmpAirplane.getDepth() > -1)
						{
							if (Utils.isInTriangle(tmpAirplane.getPosX(), tmpAirplane.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
								//System.out.println("Found allies boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpAirplane.getPosX();
									this.targetY = tmpAirplane.getPosY();
									this.targetDepth = tmpAirplane.getDepth();

									found = true;
								}
							}
						}
					}
				}
				if (enemiesBases != null)
				{
					for (int iBoat = 0; iBoat < enemiesBases.size() ; iBoat++)
					{
						tmpBase = enemiesBases.get(iBoat);
						if ((tmpBase.getDepth() > -1) && !tmpBase.isDead())
						{
							if (Utils.isInTriangle(tmpBase.getPosX(), tmpBase.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpBase.getPosX(), tmpBase.getPosY());
								//System.out.println("Found allies boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpBase.getPosX();
									this.targetY = tmpBase.getPosY();
									this.targetDepth = tmpBase.getDepth();

									found = true;
								}
							}
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
					
					if (tmpMissile.getTypeFaction() != this.getTypeFaction())
					{
						if ((tmpMissile.getIdMissile() != this.getIdMissile()) && (tmpMissile.getIdOwner() != this.getIdOwner()))
						{
							if (Utils.isInTriangle(tmpMissile.getPosX(), tmpMissile.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
							{
								distanceTmp = Missile.distSq(this.posX, this.posY, tmpMissile.getPosX(), tmpMissile.getPosY());
								//System.out.println("Found allies boat - dist "+distanceTmp);
								if (distanceTmp < distanceFound)
								{
									distanceFound= distanceTmp;
									this.targetX = tmpMissile.getPosX();
									this.targetY = tmpMissile.getPosY();
									this.targetDepth = 30;

									found = true;
								}
							}
						}
					}

				}
			}
		}
		
		return found;
	}
	
	public void checkAndNormaliseSpeed()
	{
		actualSpeed = Math.sqrt(speedX*speedX+ speedY*speedY);
		
		
		if (speedX != 0)
		{
			this.orientation = Math.acos(speedX/actualSpeed);
		}
		else
		{
			this.orientation = Math.PI/2;
		}
		if (speedY < 0)
		{
			this.orientation=-this.orientation + 2*Math.PI;
		}
		
		if (actualSpeed > this.maxSpeed)
		{
			this.speedX=maxSpeed*(this.speedX/actualSpeed);
			this.speedY=maxSpeed*(this.speedY/actualSpeed);
			
			//this.currentSpeed = this.maxSpeed;	
		}	
		//System.out.println("Current speed "+currentSpeed);
	}
	
	public static double distSq(double x,double y,double x1, double y1)
	{
		return (Math.pow(x-x1, 2)+Math.pow(y-y1, 2));
	}
	
	public void seekWP()
	{
		if (!this.programmedWPs.isEmpty() && this.wpbased)
		{
			if (!started)
			{
				this.targetX=programmedWPs.getFirst().getXWP();
				this.targetY=programmedWPs.getFirst().getYWP();
				started = true;
			}
			// Target is the next WP, if we reach it, we go to the next one.
			distToTarget = Torpedo.distSq(this.posX, this.posY, this.targetX, this.targetY);
			if ( distToTarget < stopTargetingRadiusSq)
			{
				if (!seek && !programmedWPs.hasOneOrMoreElement())
				{
					// Explode !!!
					exploding=true;
					dead=true;
					this.removeMe();
					explosion.setPos(this.posX, this.posY, 0);
					explosion.validate();
				}
				else
				{
					indexWP++;
					if (indexWP >= programmedWPs.size())
						this.wpbased = false;
					else
					{
						this.targetX=programmedWPs.getWP(indexWP).getXWP();
						this.targetY=programmedWPs.getWP(indexWP).getYWP();		
					}
				}
			}
			else
			{
				this.tmpSpeedX = (this.targetX - this.posX);
				this.tmpSpeedY = (this.targetY - this.posY);
				this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
				this.wantedSpeedX = this.tmpSpeedX / tmpSpeedN;
				this.wantedSpeedY = this.tmpSpeedY / tmpSpeedN;
				this.wantedSpeedX*=this.standardSpeed/4;
				this.wantedSpeedY*=this.standardSpeed/4;

				// Try to accelerate
				if ( (distToTarget < stopTargetingRadiusSq*2) && (this.currentSpeed > this.standardSpeed))
					this.currentSpeed -= 2;
				else if (this.currentSpeed < this.maxSpeed)
					this.currentSpeed += 2;

				//checkAndNormaliseSpeed();

				this.accX(wantedSpeedX);
				this.accY(wantedSpeedY);

				checkAndNormaliseSpeed();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.ProgrammableUnit#doUpdate(double)
	 */
	@Override
	public synchronized void doUpdate(double time)
	{
		if (!dead)
		{
			//System.out.println("Energy left "+ energyLeft);
			energyLeft-=time;
			if (energyLeft < 0)
			{
				dead=true;
				toRemove = true;
				explodeTime = 11;
				exploding = false;
			}
			else
			{
				if (seek && !target && !wpbased)
				{
					this.tmpSpeedX = (this.targetX - this.posX);
					this.tmpSpeedY = (this.targetY - this.posY);
					this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
					this.speedX = this.tmpSpeedX / tmpSpeedN;
					this.speedY = this.tmpSpeedY / tmpSpeedN;
					this.speedX*=this.currentSpeed;
					this.speedY*=this.currentSpeed;
					
					checkAndNormaliseSpeed();
					seek = false;
					seeking = true;
					
					if (debugView)
					{
						detectLine1.validate();
						detectLine2.validate();
						detectLine3.validate();
					}
				}
				
				this.posX = this.posX + time*speedX; // this.currentSpeed*Math.cos(this.orientation);
				this.posY = this.posY + time*speedY; // this.currentSpeed*Math.sin(this.orientation);
				targetLine.setPos(this.posX,this.posY,0);
				targetLine.setPosEnd(this.targetX, this.targetY);

				//	If no waypoints and no target, we seek!
				if (!this.wpbased && target)
				{
					distToTarget = Missile.distSq(this.posX, this.posY, this.targetX, this.targetY);
					if ( distToTarget < stopTargetingRadiusSq)
					{
						if (!seek || seeking)
						{
							// Explode !!!
							exploding=true;
							dead=true;

							explosion.setPos(this.posX, this.posY, 0);
							explosion.validate();
						}
						else
						{
							target = false; // Seek mode !!!
							seeking = true;

							if (debugView)
							{
								detectLine1.validate();
								detectLine2.validate();
								detectLine3.validate();
							}
						}
					}
					else
					{
						this.tmpSpeedX = (this.targetX - this.posX);
						this.tmpSpeedY = (this.targetY - this.posY);
						this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
						this.wantedSpeedX = this.tmpSpeedX / tmpSpeedN;
						this.wantedSpeedY = this.tmpSpeedY / tmpSpeedN;
						this.wantedSpeedX*=this.currentSpeed;
						this.wantedSpeedY*=this.currentSpeed;

						// Try to accelerate
						if ( (distToTarget < stopTargetingRadiusSq*2) && (this.currentSpeed > this.standardSpeed))
							this.currentSpeed -= 10;
						else if (this.currentSpeed < this.maxSpeed)
							this.currentSpeed += 10;

						//checkAndNormaliseSpeed();

						this.accX(wantedSpeedX);
						this.accY(wantedSpeedY);

						checkAndNormaliseSpeed();
					}
				}
				seekWP();
				if (seeking)
				{
					if (this.seek(time))
						target=true;
					else
					{
						// Continue to go straight... Until no more energy
						//target=false;
					}
				}

				this.updateMe(posX, posY, 0, this.orientation, this.currentSpeed);
			}
		}
		else
		{
			if (exploding)
			{
				if (explodeTime < 10)
				{
					explodeTime++;
					explosion.moveRelative(2*(0.5-Math.random()), 2*(0.5-Math.random()));
					//explosion.setSize(explosion.getSize()+0.2);
				}
				else
				{
					explosion.invalidate();
					toRemove = true;
				}
				if (!exploded)
				{
					damageUnits();
					exploded=true;
				}
			}
		}
	}

	public void damageUnits()
	{
		Submarine tmpSub;
		Boat tmpBoat;
		Base tmpBase;
		Airplane tmpAirplane;
		Missile tmpMissile;
		Trees tmpTree;
		double distanceTmp;

		int sizeCollec;
		
		ourSubs=theMap.getOurSubs();
		ourBoats=theMap.getOurBoats();
		alliesBoats=theMap.getAlliesBoats();
		alliesSubs=theMap.getAlliesSubs();
		enemiesSubs=theMap.getEnemiesSubs();
		enemiesBoats=theMap.getEnemiesBoats();
		neutralBoats=theMap.getNeutralBoats();
		neutralSubs=theMap.getNeutralSubs();
		ourAirplanes=theMap.getOurAirplanes();
		alliesAirplanes=theMap.getAlliesAirplanes();
		enemiesAirplanes=theMap.getEnemiesAirplanes();
		neutralAirplanes=theMap.getNeutralAirplanes();
		enemiesBases=theMap.getEnemiesBases();
		alliesBases=theMap.getAlliesBases();
		neutralBases=theMap.getNeutralBases();
		ourBases=theMap.getOurBases();
		missiles=theMap.getMissiles();
		
		trees=theMap.getTrees();

		if (ourSubs != null)
		{
			sizeCollec = ourSubs.size();
			
			for (int iSub = 0; iSub < sizeCollec ; iSub++)
			{
				tmpSub = ourSubs.get(iSub);
				// Update !!!
				if (tmpSub.getDepth() > -10)
				{

					distanceTmp = Missile.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
					//System.out.println("Found our sub - dist "+distanceTmp);
					if (distanceTmp < damageRadiusSq)
					{
						tmpSub.damageBoat(this.damage);
					}

				}
			}
		}
		if (ourBoats != null)
		{
			sizeCollec = ourBoats.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{		
				tmpBoat = ourBoats.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
				//System.out.println("Found our boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpBoat.damageBoat(this.damage);
				}
			}
		}
		if (alliesSubs != null)
		{
			sizeCollec = alliesSubs.size();
			
			for (int iSub = 0; iSub < sizeCollec ; iSub++)
			{
				tmpSub = alliesSubs.get(iSub);
				// Update !!!
				if (tmpSub.getDepth() > -10)
				{

					distanceTmp = Missile.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
					//System.out.println("Found our sub - dist "+distanceTmp);
					if (distanceTmp < damageRadiusSq)
					{
						tmpSub.damageBoat(this.damage);
					}

				}
			}
		}
		if (alliesBoats != null)
		{
			sizeCollec = alliesBoats.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpBoat = alliesBoats.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
				//System.out.println("Found our boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpBoat.damageBoat(this.damage);
				}
			}
		}
		if (enemiesSubs != null)
		{
			sizeCollec = enemiesSubs.size();
			
			for (int iSub = 0; iSub < sizeCollec ; iSub++)
			{
				tmpSub = enemiesSubs.get(iSub);
				// Update !!!
				if (tmpSub.getDepth() > -10)
				{

					distanceTmp = Missile.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
					//System.out.println("Found enemy sub - dist "+distanceTmp);
					if (distanceTmp < damageRadiusSq)
					{
						//System.out.println("Damage enemy sub ");
						tmpSub.damageBoat(this.damage);
					}

				}
			}
		}
		if (enemiesBoats != null)
		{
			sizeCollec = enemiesBoats.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpBoat = enemiesBoats.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					//System.out.println("Damage enemy boat ");
					tmpBoat.damageBoat(this.damage);
				}
			}
		}
		if (ourAirplanes != null)
		{
			sizeCollec = ourAirplanes.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpAirplane = ourAirplanes.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpAirplane.damageBoat(this.damage);
				}
			}
		}
		if (alliesAirplanes != null)
		{
			sizeCollec = alliesAirplanes.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpAirplane = alliesAirplanes.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpAirplane.damageBoat(this.damage);
				}
			}
		}
		if (enemiesAirplanes != null)
		{
			sizeCollec = enemiesAirplanes.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpAirplane = enemiesAirplanes.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpAirplane.damageBoat(this.damage);
				}
			}
		}
		if (enemiesBases != null)
		{
			sizeCollec = enemiesBases.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpBase = enemiesBases.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpBase.getPosX(), tmpBase.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpBase.damageBoat(this.damage);
				}
			}
		}
		if (alliesBases != null)
		{
			sizeCollec = alliesBases.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpBase = alliesBases.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpBase.getPosX(), tmpBase.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpBase.damageBoat(this.damage);
				}
			}
		}
		if (ourBases != null)
		{
			sizeCollec = ourBases.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpBase = ourBases.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpBase.getPosX(), tmpBase.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					//System.out.println("Damage our base - "+tmpBase.getBaseState());
					tmpBase.damageBoat(this.damage);
				}
			}
		}
		if (neutralSubs != null)
		{
			sizeCollec = neutralSubs.size();
			
			for (int iSub = 0; iSub < sizeCollec ; iSub++)
			{
				// Update !!!
				neutralSubs.get(iSub);
			}
		}
		if (neutralBoats != null)
		{
			sizeCollec = neutralBoats.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				// Update !!!
				neutralBoats.get(iBoat);
			}
		}
		if (neutralAirplanes != null)
		{
			sizeCollec = neutralAirplanes.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				// Update !!!
				neutralAirplanes.get(iBoat);
			}
		}
		if (missiles != null)
		{
			sizeCollec = missiles.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpMissile = missiles.get(iBoat);

				distanceTmp = 2*Missile.distSq(this.posX, this.posY, tmpMissile.getPosX(), tmpMissile.getPosY());
				//System.out.println("Found enemy missile - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSqOtherMissile)
				{
					tmpMissile.explode();
				}
			}
		}
		if (trees != null)
		{
			sizeCollec = trees.size();
			
			for (int iBoat = 0; iBoat < sizeCollec ; iBoat++)
			{
				tmpTree = trees.get(iBoat);

				distanceTmp = Missile.distSq(this.posX, this.posY, tmpTree.getPosX(), tmpTree.getPosY());
				//System.out.println("Found our boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpTree.damageBoat(this.damage);
				}
			}
		}
	}

	public double getDamageRadiusSq() {
		return damageRadiusSq;
	}


	public void setDamageRadiusSq(double damageRadiusSq) {
		this.damageRadiusSq = damageRadiusSq;
	}


	public int getDamageType() {
		return damageType;
	}


	public void setDamageType(int damageType) {
		this.damageType = damageType;
	}


	public int getExplodeTime() {
		return explodeTime;
	}


	public boolean toRemove() {
		if (toRemove && (explodeTime >= 10))
			return true;
		else
			return false;
	}


	public void setToRemove(boolean toRemove) {
		this.toRemove = toRemove;
	}


	@Override
	/**
	 * Simplified faction: enemy or ally
	 * @param typeOwner
	 */
	public void setTypeFaction(int typeOwner) {
		if ((typeOwner & 1) == 1)
		{
			this.typeFaction=1;
		}
		else
		{
			this.typeFaction=0;
		}
	}


	@Override
	public void doUpdateSM(double time)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void levelUp()
	{
		// TODO Auto-generated method stub
		this.level = this.newLevelReachable;
		// Nothing, shouldn't be called at all
		System.out.println("Missile levelled up ?!?");
	}
	
	@Override
	public void hideLabel()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLabel()
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "";
	}


	@Override
	public String saveIfNeeded()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<String> loadIfSupported(List<String> myPart)
	{
		// TODO Auto-generated method stub
		return myPart;
	}
	
	@Override
	public String getTypeName()
	{
		switch( type )
		{
		case ASM:
			return "Air To Surface";
		case ASW:
			return "Anti-Submarine";
		case ALM:
			return "ALM";
		case AA:
			return "Anti-Aircraft";
		case CRUISE:
			return "Cruise Missile";
			
		default:
			return "Unknown";
		}
	}
}
