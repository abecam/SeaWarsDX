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

import java.util.ArrayList;
import java.util.List;

import pulpcore.image.CoreImage;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.Utils;
import com.tgb.subengine.gfxentities.SimpleLinePC;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.gfxentities.SpritePCAdd;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subengine.particlessystem.ParticlePC;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.unitspc.sensors.ISignalCreator;
import com.tgb.subgame.unitspc.sensors.Signal;

/**
 * A Torpedo. Due to the speed, the sensors are faked in it (not "actual ones" like in
 * other units)
 * @author Alain Becam
 *
 */
public class Torpedo extends ProgrammableUnit implements ISignalCreator
{

	int idMissile;
	int idOwner; // Who shot it.
	
	double alt=0;
	double orientation;
	
	double xShift,yShift;
	
	double currentSpeed=0; // In knots
	double speedX,speedY,speedZ=0;
	
	double wantedSpeedX,wantedSpeedY,wantedSpeedZ=0;
	double tmpSpeedX,tmpSpeedY,tmpSpeedZ,tmpSpeedN=0;
	
	double maxSpeed=55; // In knots
	double standardSpeed=45;
	double accelerationMax=20; // In knots/sec
	
	double energyLeft=20;
	
	int noiseLevel; // 0-> None, 100-> enough :)
	int visibilityLevel;
	
	int damage=200; // Damage potential
	double damageRadiusSq=1000; // Square of damage radius !!!
	
	double stopTargetingRadiusSq=100;
	
	int damageType; // To be defined
	
	
	SpritePC torpedoBody;
	Emitter bubblesEmitter; // For enemies's torpedoes, both the torpedo and the bubbles will appear if found (by gravitation or passive/active sonar)
	Particles bubbles;
	long idBubbles;
	
	SpritePCAdd explosion,explosion2;
	
	long idBody;
	long idExplosion,idExplosion2; // Generally, only one flash will be used in the same time!
	

	public static final int ASM=0x50;
	public static final int ASW=0x51;
	public static final int ALM=0x52;
	public static final int AA=0x53;
	public static final int CRUISE=0x54;
	
	boolean seek=true; // Does this missile seek the enemy (or is WP-Target Coord based only, explose on arrival)
	boolean seeking=false; // Are we actually seeking?
	boolean wpbased=false;
	boolean target=true;
	boolean knowAllies=false; // Ignore allies' units?
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
	
	static boolean debugView = false; // Do we see the seeking lines
	
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
	
	
	boolean exploding=false; // Tell the updater (TacticalMap) to explode us!
	boolean exploded=false; // Damage done?
	boolean toRemove=false; // Will be removed from TacticalMap
	int explodeTime=0;
	
	String Name;
	
	LevelMap theMap;
	/**
	 * @param theMap
	 */
	public Torpedo(LevelMap theMap)
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
	public void createGfx(CoreImage torpedoImage, double x, double y, double z, double direction, double speed)
	{	
		
		explosion = new SpritePCAdd(gfxSprites.getImageFlash2());
		explosion2 = new SpritePCAdd(gfxSprites.getImageFlash());
		explosion.invalidate();
		explosion2.invalidate();
		explosion.setSize(2);
		
		idExplosion= RenderingManager.getInstance().addDrawableEntity(explosion, 30);
		
		torpedoBody = new SpritePC(torpedoImage);
		torpedoBody.setRotation(direction);

		torpedoBody.setPos(x, y, z);
		torpedoBody.setSize(0.2);
		idBody = RenderingManager.getInstance().addDrawableEntity(torpedoBody,5); // High
		
		ParticlePC bubbleExample=new ParticlePC();
		bubbleExample.setAlive(true);
		bubbleExample.setEnergy(10);
		bubbleExample.setSize(1);
		bubbleExample.setWeight(10);
		bubbleExample.setTimeLeft(200);
		bubbleExample.setMyColor(0xAA0030AA);
		bubbleExample.setColorAIncDec(-1);
		
		bubblesEmitter = new Emitter(x, y, Math.PI, 0.1, Emitter.POINT_DIRECTIONNAL, 4, 10, 0, speed/20, 200,bubbleExample);
		//bubblesEmitter = new Emitter(x, y, 1, 10, 10, Math.PI, 0.1, Emitter.POINT_DIRECTIONNAL, 4, 10, 0, speed/20, 200);
	    bubbles=new Particles(50,bubblesEmitter,ParticlePC.class);
	    torpedoBody.addAttachedObject(bubblesEmitter);
	    
	    idBubbles = RenderingManager.getInstance().addParticles(bubbles, 5);
	}
	
	/**
	 * Update the torpedo
	 * NB: We do not simulate the torpedo here (yet)
	 */
	public void updateMe(double x, double y, double z, double direction, double speed)
	{
		torpedoBody.setRotation(direction);
		torpedoBody.setPos(x, y, z);
		bubblesEmitter.setSpeed(speed/20);
	}
	
	public void hideMe()
	{
		torpedoBody.invalidate();
		bubbles.invalidate();
	}
	
	public void showMe()
	{
		torpedoBody.validate();
		bubbles.validate();
	}

	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMe()
	{
		RenderingManager.getInstance().removeEntity(idBody, 5);
		RenderingManager.getInstance().removeParticles(idBubbles, 5);
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
		double distanceTmp;
		
		
		boolean found=false;
		double distanceFound = 100000; // Reset the distance of detection.
		
		// If active, will find a lot more of units in the surrounding
		// More in direct front and close.
		// It also generates a signal.

		ourSignal.setTimeCreation(time);
		ourSignal.setOrientation(this.orientation);
		ourSignal.setPosOrigin(this.posX, this.posY, this.alt);
		ourSignal.setType(Signal.SONAR);
		ourSignal.setIdOwner(-1); // None

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

		{
			if (!knowAllies)
			{
				if (ourSubs != null)
				{
					for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
					{
						tmpSub = ourSubs.get(iSub);
						// Update !!!

						if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
						{
							distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
							//System.out.println("Found our sub - dist "+distanceTmp);
							if (distanceTmp < distanceFound && (tmpSub.getDepth() - this.getDepth() < 100))
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
				if (ourBoats != null)
				{
					for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
					{		
						tmpBoat = ourBoats.get(iBoat);
						// Update !!!
						if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
						{
							distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
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
				if (alliesSubs != null)
				{
					for (int iSub = 0; iSub < alliesSubs.size() ; iSub++)
					{
						tmpSub = alliesSubs.get(iSub);
						// Update !!!

						if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
						{
							distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
							//System.out.println("Found allie sub - dist "+distanceTmp);
							if (distanceTmp < distanceFound && (tmpSub.getDepth() - this.getDepth() < 100))
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
				if (alliesBoats != null)
				{
					for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
					{
						tmpBoat = alliesBoats.get(iBoat);
						// Update !!!
						if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
						{
							distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
							//System.out.println("Found allies boat - dist "+distanceTmp);
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
			if (!knowEnemies)
			{
				if (enemiesSubs != null)
				{
					for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
					{
						tmpSub = enemiesSubs.get(iSub);
						// Update !!!

						if (Utils.isInTriangle(tmpSub.getPosX(), tmpSub.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
						{
							distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
							//System.out.println("Found enemy sub - dist "+distanceTmp);
							if (distanceTmp < distanceFound && (tmpSub.getDepth() - this.getDepth() < 100))
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
				if (enemiesBoats != null)
				{
					for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
					{
						tmpBoat = enemiesBoats.get(iBoat);
						// Update !!!
						if (Utils.isInTriangle(tmpBoat.getPosX(), tmpBoat.getPosY(), this.posX, this.posY, xT2, yT2, xT3, yT3))
						{
							distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
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
				this.wantedSpeedX*=this.standardSpeed/8;
				this.wantedSpeedY*=this.standardSpeed/8;

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
				}
				
				this.posX = this.posX + time*speedX; // this.currentSpeed*Math.cos(this.orientation);
				this.posY = this.posY + time*speedY; // this.currentSpeed*Math.sin(this.orientation);
				
				if ( ourLevelKeeper.getAlpha((int )this.posX, (int )this.posY) > 10)
				{
					exploding=true;
					dead=true;
					
					explosion.setPos(this.posX, this.posY, 0);
					explosion.validate();
				}
				
				targetLine.setPos(this.posX,this.posY,0);
				targetLine.setPosEnd(this.targetX, this.targetY);

				//	If no waypoints and no target, we seek!
				if (!this.wpbased && target)
				{
					distToTarget = Torpedo.distSq(this.posX, this.posY, this.targetX, this.targetY);
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
					explosion.setSize(explosion.getSize()+0.2);
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
		double distanceTmp;

		ourSubs=theMap.getOurSubs();
		ourBoats=theMap.getOurBoats();
		alliesBoats=theMap.getAlliesBoats();
		alliesSubs=theMap.getAlliesSubs();
		enemiesSubs=theMap.getEnemiesSubs();
		enemiesBoats=theMap.getEnemiesBoats();
		neutralBoats=theMap.getNeutralBoats();
		neutralSubs=theMap.getNeutralSubs();

		if (ourSubs != null)
		{
			for (int iSub = 0; iSub < ourSubs.size() ; iSub++)
			{
				tmpSub = ourSubs.get(iSub);
				// Update !!!

				distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
				//System.out.println("Found our sub - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpSub.damageBoat(this.damage);
				}

			}
		}
		if (ourBoats != null)
		{
			for (int iBoat = 0; iBoat < ourBoats.size() ; iBoat++)
			{		
				tmpBoat = ourBoats.get(iBoat);

				distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
				//System.out.println("Found our boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpBoat.damageBoat(this.damage);
				}
			}
		}
		if (alliesSubs != null)
		{
			for (int iSub = 0; iSub < alliesSubs.size() ; iSub++)
			{
				tmpSub = alliesSubs.get(iSub);
				// Update !!!

				distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
				//System.out.println("Found our sub - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpSub.damageBoat(this.damage);
				}

			}
		}
		if (alliesBoats != null)
		{
			for (int iBoat = 0; iBoat < alliesBoats.size() ; iBoat++)
			{
				tmpBoat = alliesBoats.get(iBoat);

				distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
				//System.out.println("Found our boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					tmpBoat.damageBoat(this.damage);
				}
			}
		}
		if (enemiesSubs != null)
		{
			for (int iSub = 0; iSub < enemiesSubs.size() ; iSub++)
			{
				tmpSub = enemiesSubs.get(iSub);
				// Update !!!
				distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpSub.getPosX(), tmpSub.getPosY());
				//System.out.println("Found enemy sub - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					//System.out.println("Damage enemy sub ");
					tmpSub.damageBoat(this.damage);
				}
			}
		}
		if (enemiesBoats != null)
		{
			for (int iBoat = 0; iBoat < enemiesBoats.size() ; iBoat++)
			{
				tmpBoat = enemiesBoats.get(iBoat);

				distanceTmp = Torpedo.distSq(this.posX, this.posY, tmpBoat.getPosX(), tmpBoat.getPosY());
				//System.out.println("Found enemy boat - dist "+distanceTmp);
				if (distanceTmp < damageRadiusSq)
				{
					//System.out.println("Damage enemy boat ");
					tmpBoat.damageBoat(this.damage);
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


	public int getIdOwner() {
		return idOwner;
	}


	public void setIdOwner(int idOwner) {
		this.idOwner = idOwner;
	}
	
	@Override
	public void setTypeFaction(int typeFaction) {
		// TODO Auto-generated method stub
		
	}


	public double getEnergyLeft() {
		return energyLeft;
	}


	public void setEnergyLeft(double energyLeft) {
		this.energyLeft = energyLeft;
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
		System.out.println("Torpedo levelled up ?!?");
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
	public String saveIfNeeded()
	{
		// TODO Auto-generated method stub
		return "";
	}


	@Override
	public List<String> loadIfSupported(List<String> myPart)
	{
		// TODO Auto-generated method stub
		return myPart;
	}


	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return "";
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