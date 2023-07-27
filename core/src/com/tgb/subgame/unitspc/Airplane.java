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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gfxentities.IAttachable;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.ParticleSpritePC;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subgame.GameKeeper;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.TacticalMapPC;
import com.tgb.subgame.unitspc.sensors.Sensor;

import pulpcore.image.CoreImage;
/**
 * Super-class for the submarines
 * @author Alain Becam
 *
 */
public class Airplane extends ProgrammableUnit
{
	
	public static final String SAVE_ID = "Airplane";

	double orientation;
	
	double xShift,yShift;
	
	double currentSpeed=0; // In knots
	
	double maxSpeed=1500; // 2000; // In knots
	double standardSpeed=600; // 1500;
	
	double maxAlt=100000; // In feet
	
	double speedX=0,speedY=0,speedZ=0;
	double wantedSpeedX=0,wantedSpeedY=0,wantedSpeedZ=0;
	double tmpSpeedX=0,tmpSpeedY=0,tmpSpeedZ=0,tmpSpeedN=0;
	
	int noiseSignature; // How silent this unit can be
	int visibilitySignature;
	int noiseLevel; // 0-> None, 100-> enough :)
	int visibilityLevel; // 100-> Surface (perfectly visible if no fog/clouds), 0-> Very deep (not visible at all)
	ArrayList<Sensor> sensors;
	ArrayList<Sensor> deportedSensors; // Sensors that we do not use ourself!!!
	ArrayList weapons;
	ArrayList countermeasures;
	
	int typeEnemy; // Mask result to find the enemy. 1 for allies (1 && allies = 1), 0 otherwise (1 && enemy = 0)
	
	public static final int FIGHTER=0x30;
	public static final int BOMBER=0x31;
	public static final int HELI=0x32;
	public static final int RADARPLANE=0x33;
	public static final int ESMPLANE=0x34;
	public static final int TRANSPORT=0x35;
	public static final int TANKER=0x36;
	public static final int COMMAND=0x37;
	
	public static final int CARGOPLANE=0x40;
	public static final int AIRLINE=0x41;
	public static final int PLEASANCEPLANE=0x43;
	
	ProgrammableUnit owner= null; // From where did we come. To allow only a limited number of airplanes at the same time in the air. 
	
	int resistance=4;
	
	int cost;
	
	// The different parts, ok or damaged.
	double hullState=100;
	double dataLinkState=100;
	double navState=100;
	
	boolean damaged=false;	
	
	boolean finallydead=false;
	boolean unmanned=false;
	
	double iDead; // to sunk
	double depthWater=200; // How much can we sink :)
	
	transient SpritePC boatBody;
	Emitter bubblesEmitter; 
	Particles bubbles;
	
	transient SpritePC boatSquare;
	
	Particles smokeBlack;
	Emitter badSmokeEmitter; 
	
	long idBody;
	long idBubbles;
	long idBadSmoke;
	long idExplosion;
	long idBoatSquare;
	
	ArrayList<IAttachable> attachedObjects; // Attached object, will be moved and rotate with the entity
	AffineTransform transformToApply; // Common affine transform
	
	Point2D.Double onePoint2D ;
	Point2D.Double onePoint2Dtmp ;
	
	int explodeTime=0;
	transient SpritePC explosion;
	
	int nbTorpedoes;
	int nbTubes; // Will change the time to fire new torpedoes
	int nbMissiles;
	int typeLauncher;
	
	// Give some info about our load, so we can see if we need to reload
	boolean hasTorpedoes=true;
	boolean hasMissiles=true; 
	
	boolean reloadNeeded=false;
	
	// Normal number of torpedoes and missiles (to relaod)
	int nbTorpedoesNorm;
	int nbMissilesNorm;
	
	double xSaveWP,ySaveWP; // used to save the 1st wp.
	
	double timeSinceLastLaunchM;
	int nbInSalveM=0;
	double timeBetweenLaunchesM;
	int nbInSalveMaxM=0;
	boolean loadingM=false;
	
	double timeSinceLastLaunchT;
	int nbInSalveT=0;
	double timeBetweenLaunchesT;
	int nbInSalveMaxT=0;
	boolean loadingT=false;
	
	/**
	 * @param theMap
	 */
	public Airplane(LevelMap theMap)
	{
		super(theMap);
		sensors = new ArrayList<Sensor>();
		deportedSensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		
		nbTorpedoes = 2;
		nbTubes = 2;
		nbMissiles = 8;
		
		nbTorpedoesNorm = 2;
		nbMissilesNorm = 8;
		
		timeBetweenLaunchesM = 0.2;
		nbInSalveMaxM = 4;
			
		timeBetweenLaunchesT = 3;
		nbInSalveMaxT=20;
		
		// Default
		type = Airplane.FIGHTER;
		
		wantedSpeed=standardSpeed;
		
		this.depth=1000;
	}
	
	/**
	 * @param depth the depth to set
	 */
	public void setDepth(double depth)
	{
		this.depth = depth;
		int visib=(int )(255+depth);
		if (visib < 0)
			visib = 0;
		if (visib > 255)
			visib = 255;
		if (boatBody != null)
			boatBody.setAlpha(visib);
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
		wantedSpeed=maxSpeed;
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
	 * @return the weapons
	 */
	public java.util.ArrayList getWeapons()
	{
		return weapons;
	}
	/**
	 * @param weapons the weapons to set
	 */
	public void setWeapons(java.util.ArrayList weapons)
	{
		this.weapons = weapons;
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
	/**
	 * @return the cost
	 */
	public int getCost()
	{
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost)
	{
		this.cost = cost;
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
	}
	
	public void accelerate(double speedToAdd)
	{
		if (!unmanned)
		{
			this.wantedSpeed += speedToAdd;
			if (wantedSpeed > this.maxSpeed)
				wantedSpeed=this.maxSpeed;
		}
	}
	
	public void deccelerate(double speedToRemove)
	{
		if (!unmanned)
		{
			this.wantedSpeed -= speedToRemove;
			if (wantedSpeed < -this.maxSpeed)
				wantedSpeed=-this.maxSpeed;
		}
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
	
	public void turn(double angle)
	{
		if (!unmanned)
			this.orientation += angle;
	}
	/**
	 * @return the resistance
	 */
	public int getResistance()
	{
		return resistance;
	}
	/**
	 * @param resistance the resistance to set
	 */
	public void setResistance(int resistance)
	{
		this.resistance = resistance;
	}
	/**
	 * @return the sensors
	 */
	public java.util.ArrayList<Sensor> getSensors()
	{
		return sensors;
	}
	/**
	 * @param sensors the sensors to set
	 */
	public void setSensors(java.util.ArrayList<Sensor> sensors)
	{
		this.sensors = sensors;
	}

	public ArrayList<Sensor> getDeportedSensors() {
		return deportedSensors;
	}

	public void setDeportedSensors(ArrayList<Sensor> deportedSensors) {
		this.deportedSensors = deportedSensors;
	}

	public void setTypeFaction(int typeFaction) {
		this.typeFaction = typeFaction;
		if ((typeFaction & 1) == 1)
		{
			typeEnemy = 0; // The enemies are not allies!
			//this.typeFaction=1;
		}
		else
		{
			typeEnemy = 1;
			//this.typeFaction=0;
		}
	}
	
	/**
	 * @return the noiseSignature
	 */
	public int getNoiseSignature()
	{
		return noiseSignature;
	}
	/**
	 * @param noiseSignature the noiseSignature to set
	 */
	public void setNoiseSignature(int noiseSignature)
	{
		this.noiseSignature = noiseSignature;
	}
	/**
	 * @return the visibilitySignature
	 */
	public int getVisibilitySignature()
	{
		return visibilitySignature;
	}
	/**
	 * @param visibilitySignature the visibilitySignature to set
	 */
	public void setVisibilitySignature(int visibilitySignature)
	{
		this.visibilitySignature = visibilitySignature;
	}
	/**
	 * Return the default image for the sub.
	 * @return the correct image
	 */
	public static CoreImage getImageForMe()
	{
		return gfxSprites.getImageAirplane();
	}
	/**
	 * Create the graphics entity
	 * If you have more than one sub (should be :) ), it is better to use the second
	 * method, with Image as parameter: so the image is shared, not loaded for all.
	 */
	public void createGfx(double x, double y, double z, double direction, double speed)
	{
		CoreImage boatImage=gfxSprites.imageAirplane;
		
		if (this.typeFaction == FUnit.AIRPLANE_OUR)
		{
			if (this.type == Airplane.RADARPLANE)
				boatImage=gfxSprites.imageAwaks;
			else
				boatImage=gfxSprites.imageAirplane;
		}
		if (this.typeFaction == FUnit.AIRPLANE_ALLIED)
		{
			if (this.type == Airplane.RADARPLANE)
				boatImage=gfxSprites.imageAwaksLight;
			else
				boatImage=gfxSprites.imageAirplaneLight;
		}
		if (this.typeFaction == FUnit.AIRPLANE_ENEMY)
		{
			if (this.type == Airplane.RADARPLANE)
				boatImage=gfxSprites.imageAwaksDark;
			else
				boatImage=gfxSprites.imageAirplaneDark;
		}
		
		createGfx(boatImage, x, y, z, direction, speed);
	}
	
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfx(CoreImage boatImage, double x, double y, double z, double direction, double speed)
	{	
		boatBody = new SpritePC(boatImage);
		boatBody.setRotation(direction);
		boatBody.setSize(0.75);
		xShift=40;
		yShift=10;
		boatBody.setPos(x, y, z);
		idBody = RenderingManager.getInstance().addDrawableEntity(boatBody,30); // Surface
		/*
		boatSquare = new SpritePC(gfxSprites.getSelImage());
		boatSquare.setSize(1.2);
		boatSquare.invalidate();
		boatBody.addAttachedObject(boatSquare);
		idBoatSquare = RenderingManager.getInstance().addDrawableEntity(boatSquare,39);
		*/
		explosion = new SpritePC(gfxSprites.getImageFlash2());	
		explosion.invalidate();
		explosion.setSize(2.25); //3);
		explosion.setPosAttach(Math.random()*10-5, Math.random()*8-4, 0);
		boatBody.addAttachedObject(explosion);
		idExplosion = RenderingManager.getInstance().addDrawableEntity(explosion,31);
		
		/*ParticleSpritePC newParticle= new ParticleSpritePC(gfxSprites.getImageSmoke());
		
		newParticle.setEnergy(10);
		newParticle.setSize(0.05);//1);
		newParticle.setTimeLeft(500);
		newParticle.setSizeIncDec(1.002);
		
		newParticle.setAlive(true);
	    */
	    ParticleSpritePC blackSmoke= new ParticleSpritePC(gfxSprites.getImageDarkSmoke());
		
	    blackSmoke.setEnergy(10);
	    blackSmoke.setSize(0.225);//0.3);
	    blackSmoke.setTimeLeft(2500);
	    blackSmoke.setSizeIncDec(1.004);
		
	    blackSmoke.setAlive(true);
		
	    
	    badSmokeEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 1, 10, 0, 0.1, 500, blackSmoke);
	    badSmokeEmitter.setPosAttach(Math.random()*8-4, 0, 0);
		boatBody.addAttachedObject(badSmokeEmitter);
	    smokeBlack=new Particles(500,badSmokeEmitter,blackSmoke);
	    if (!damaged)
	    	smokeBlack.invalidate();
//		bubblesEmitter = new Emitter(x, y, 1, 10, 10, 0, Math.PI/4, Emitter.POINT_DIRECTIONNAL, 20, 50, 0, 0.1, 5000);
//	    bubbles=new Particles(500,bubblesEmitter,ParticlePC);
//	    bubblesEmitter.setPosAttach(40, 1, 0);
//	    boatBody.addAttachedObject(bubblesEmitter);
	    
	    idBadSmoke =  RenderingManager.getInstance().addParticles(smokeBlack, 30);
//	    idBubbles = RenderingManager.getInstance().addParticles(bubbles, 19);
	}
	
	/**
	 * Update the torpedo
	 * NB: We do not simulate the torpedo here (yet)
	 */
	public void updateMe(double x, double y, double z, double direction, double speed)
	{
		boatBody.setRotation(direction);
		boatBody.setPos(x, y, z);
	}
	
	public void hideMe()
	{
		boatBody.invalidate();
		smokeBlack.invalidate();
		for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
		{
			sensors.get(iSensors).hide();
		}
//		bubbles.invalidate();
	}
	
	public void showMe()
	{
		boatBody.validate();

		if (damaged)
	    	smokeBlack.validate();
//		bubbles.validate();
		if (view)
		{
			for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
			{
				sensors.get(iSensors).show();
			}
		}
	}
	
	public boolean toRemove() {
		if (explodeTime >= 200)
			return true;
		else
			return false;
	}
	
	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMe()
	{
		RenderingManager.getInstance().removeEntity(idBody, 30);
		RenderingManager.getInstance().removeParticles(idBadSmoke, 30);
		RenderingManager.getInstance().removeEntity(idExplosion, 31);
		//RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
		if (sensors != null)
		{
			for (int iSensor = 0; iSensor < sensors.size() ; iSensor++)
			{
				// Update !!!
				sensors.get(iSensor).removeMe();
			}
			sensors.clear();
		}
		if (programmedWPs != null)
		{
			this.programmedWPs.clear();
			this.programmedWPs=null;
		}
	}
	
	public void removeMeWOBody()
	{
		RenderingManager.getInstance().removeParticles(idBadSmoke, 30);
		RenderingManager.getInstance().removeEntity(idExplosion, 31);
		//RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
		if (sensors != null)
		{
			for (int iSensor = 0; iSensor < sensors.size() ; iSensor++)
			{
				// Update !!!
				sensors.get(iSensor).removeMe();
			}
			sensors.clear();
		}
		if (programmedWPs != null)
		{
			this.programmedWPs.clear();
			this.programmedWPs=null;
		}
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
	
	public void resetWP()
	{
		if (indexWP >= this.programmedWPs.size())
			indexWP=this.programmedWPs.size()-1;
		if (this.programmedWPs.size() >0)
		{
			if (indexWP >= this.programmedWPs.size())
				indexWP=0;
			this.targetX=programmedWPs.getWP(indexWP).getXWP();
			this.targetY=programmedWPs.getWP(indexWP).getYWP();	
		}
		else
		{
			indexWP=0;
		}
	}
	
	public void seekWP()
	{
		if (!this.programmedWPs.isEmpty())
		{
			if (!started)
			{
				this.targetX=programmedWPs.getFirst().getXWP();
				this.targetY=programmedWPs.getFirst().getYWP();
				started = true;
			}
			// Target is the next WP, if we reach it, we go to the next one.
			double distToTarget = Torpedo.distSq(this.posX, this.posY, this.targetX, this.targetY);
			if ( distToTarget < 3000)
			{
				if (owner != null)
				{
					if (loop && (indexWP==0) && reloadNeeded && !owner.isDead())
					{
						this.nbMissiles=this.nbMissilesNorm;
						this.nbTorpedoes=this.nbTorpedoesNorm;
						reloadNeeded=false;

						programmedWPs.getFirst().setXWP(xSaveWP);
						programmedWPs.getFirst().setYWP(ySaveWP);
						System.out.println("Airplane "+this.idBoat+ " reloaded");
					}
				}
				if (!programmedWPs.hasOneOrMoreElement())
				{
					// Explode !!!
					this.setCurrentSpeed(0);
				}
				else
				{
					if (!loop)
					{	
						programmedWPs.pull();
						if (!programmedWPs.isEmpty())
						{
							this.targetX=programmedWPs.getFirst().getXWP();
							this.targetY=programmedWPs.getFirst().getYWP();
							System.out.println("Next WP "+programmedWPs.getFirst().getNbWP());
						}
						else
						{
							System.out.println("Arrived");
						}
					}
					else
					{
						indexWP++;
						if (indexWP >= programmedWPs.size())
							indexWP=0;
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
				this.wantedSpeedX*=this.standardSpeed/120;
				this.wantedSpeedY*=this.standardSpeed/120;

				// Try to accelerate
				if ( (distToTarget < 2000) && (this.currentSpeed > this.standardSpeed))
					this.currentSpeed -= 2;
				else if (this.currentSpeed < this.wantedSpeed)
					this.currentSpeed += 2;
				else if (this.currentSpeed > this.wantedSpeed)
						this.currentSpeed -= 2;

				//checkAndNormaliseSpeed();

				this.accX(wantedSpeedX);
				this.accY(wantedSpeedY);

				checkAndNormaliseSpeed();
			}
		}
	}
	
	/**
	 * Attack the detected unit if needed, and eventually the incoming missiles.
	 * More AI might be good here
	 * @param time
	 */
	public void attackIfNeeded(double time)
	{
		if (loadingT)
		{
			timeSinceLastLaunchT-=time;

			if (timeSinceLastLaunchT < 0)
			{
				loadingT=false;
				nbInSalveT=0;
			}
		}
		if (loadingM)
		{
			timeSinceLastLaunchM-=time;
			
			if (timeSinceLastLaunchM < 0)
			{
				loadingM=false;
				nbInSalveM=0;
			}
		}
		if (fireAsked)
		{
			if ((this.typeFireAsked == ProgrammableUnit.FIRE_TORPEDO) && (this.nbTorpedoes > 0) && (nbInSalveT < nbInSalveMaxT))
			{

				Torpedo oneTorpedo= new Torpedo(this.theMap);
//				if (typeEnemy == 1)
//				{
//				oneTorpedo.setKnowAllies(false);
//				oneTorpedo.setKnowEnemies(true);
//				}
				oneTorpedo.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
				oneTorpedo.setTargetPos(xFireAsked, yFireAsked, 0);
				// Ask for the maximum speed (so push more than it)
				oneTorpedo.setOrientation(this.getOrientation()+nbInSalveT*0.1);
				oneTorpedo.setCurrentSpeed(500);
				oneTorpedo.setPosX(this.getPosX());
				oneTorpedo.setPosY(this.getPosY());
				oneTorpedo.setIdOwner(this.idBoat);
				oneTorpedo.setTypeFaction(this.typeFaction);
				oneTorpedo.setSeek(this.fireWithSeek);
				oneTorpedo.setTarget(this.fireWithTarget);
				oneTorpedo.setWpbased(this.fireWithWP);
				oneTorpedo.setProgrammedWPs(this.wpsToFollow);
				this.theMap.addTorpedo(oneTorpedo);

				nbTorpedoes--;

				nbInSalveT++;
				if (fireInSalve)
				{
					if (nbInSalveT == nbInSalveMaxT)
						fireAsked=false;
				}
				else
				{
					fireAsked=false;
				}
			}
			if ((nbInSalveT == nbInSalveMaxT) && !loadingT)
			{
				loadingT=true;
				timeSinceLastLaunchT=timeBetweenLaunchesT;
			}
			if ((this.typeFireAsked == ProgrammableUnit.FIRE_MISSILE) && (this.nbMissiles > 0) && (nbInSalveM < nbInSalveMaxM))
			{

				Missile oneMissile= new Missile(this.theMap);
				if (typeEnemy == 1)
				{
					oneMissile.setKnowAllies(false);
					oneMissile.setKnowEnemies(true);
				}
				if (this.ignoreFriends)
				{
					oneMissile.setKnowAllies(false);
					oneMissile.setKnowEnemies(false);
				}
				oneMissile.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
				oneMissile.setTargetPos(xFireAsked, yFireAsked, 0);
				oneMissile.setOrientation(this.getOrientation()+nbInSalveM*0.1);
				oneMissile.setCurrentSpeed(500);
				oneMissile.setMaxSpeed(1000);
				oneMissile.setPosX(this.getPosX()+nbInSalveM);
				oneMissile.setPosY(this.getPosY());
				oneMissile.setIdOwner(this.idBoat);
				oneMissile.setTypeFaction(this.typeFaction);
				oneMissile.setSeek(this.fireWithSeek);
				oneMissile.setTarget(this.fireWithTarget);
				oneMissile.setWpbased(this.fireWithWP);
				oneMissile.setProgrammedWPs(this.wpsToFollow);
				this.theMap.addMissile(oneMissile);

				nbMissiles--;

				switch (this.typeFaction)
				{
					case FUnit.AIRPLANE_OUR:
						GameKeeper.getInstance().addMissiles(1);
						break;
					case FUnit.AIRPLANE_ALLIED:
						GameKeeper.getInstance().addMissilesAllies(1);
						break;
					case FUnit.AIRPLANE_ENEMY:
						GameKeeper.getInstance().addMissilesEne(1);
						break;
				}
				
				nbInSalveM++;
				if (fireInSalve)
				{
					if (nbInSalveM == nbInSalveMaxM)
						fireAsked=false;
				}
				else
				{
					fireAsked=false;
				}
			}
			if ((nbInSalveM == nbInSalveMaxM) && !loadingM)
			{
				loadingM=true;
				timeSinceLastLaunchM=timeBetweenLaunchesM;
			}
		}

		if (this.autonomous)
		{
			for (int iUnits=0; iUnits< foundUnits.size(); iUnits++)
			{
				FUnit tmpUnit = foundUnits.get(iUnits);
				//System.out.println("Contact to sink - "+tmpUnit.getLevelDetection());
				// Check deep of the unit (missile or torpedo ? )

				if ((tmpUnit.getLevelDetection() > 2) || this.isIgnoreFriends())
				{
					if ((tmpUnit.getDepth() < -1) || (tmpUnit.getType() == FUnit.UNKNOWN_SEA) || 
							((this.nbMissiles == 0) && (tmpUnit.getType() != FUnit.UNKNOWN_AIR) && (tmpUnit.getType() != FUnit.MISSILE)&& (tmpUnit.getType() != FUnit.TORPEDO)
									&& (tmpUnit.getType() != FUnit.AIRPLANE_ALLIED) && (tmpUnit.getType() != FUnit.AIRPLANE_ENEMY) && (tmpUnit.getType() != FUnit.AIRPLANE_OUR)
									&& (tmpUnit.getType() != FUnit.BASE_ALLIED) && (tmpUnit.getType() != FUnit.BASE_ENEMY) && (tmpUnit.getType() != FUnit.BASE_OUR)))
					{
						if (tmpUnit.getType() == FUnit.UNKNOWN_SEA)
							tmpUnit.setType(FUnit.UNKNOWN_SEA | (1-typeEnemy));

						// Torpedo !!!
						if ((this.nbTorpedoes > 0) && (this.fireAtWill) && (nbInSalveT < nbInSalveMaxT))
						{
							if (this.isIgnoreFriends() || 
									(!this.ignoreFriends && ((tmpUnit.getType() & 1) == typeEnemy)) )

							{
								Torpedo oneTorpedo= new Torpedo(this.theMap);
								//System.out.println("Attack sub or boat "+tmpUnit.getLevelDetection());
//								if (typeEnemy == 1)
//								{
//								oneTorpedo.setKnowAllies(false);
//								oneTorpedo.setKnowEnemies(true);
//								}
								
								oneTorpedo.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
								oneTorpedo.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.6, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.6, 0);
								oneTorpedo.setOrientation(this.getOrientation()+nbInSalveT*0.1);
								oneTorpedo.setCurrentSpeed(500);
								oneTorpedo.setMaxSpeed(65);
								oneTorpedo.setDamage(100);
								oneTorpedo.setEnergyLeft(5);
								oneTorpedo.setPosX(this.getPosX());
								oneTorpedo.setPosY(this.getPosY());
								oneTorpedo.setIdOwner(this.idBoat);
								oneTorpedo.setTypeFaction(this.typeFaction);
								this.theMap.addTorpedo(oneTorpedo);

								nbTorpedoes--;

								switch (this.typeFaction)
								{
									case FUnit.AIRPLANE_OUR:
										GameKeeper.getInstance().addTorpedoes(1);
										break;
									case FUnit.AIRPLANE_ALLIED:
										GameKeeper.getInstance().addTorpedoesAllies(1);
										break;
									case FUnit.AIRPLANE_ENEMY:
										GameKeeper.getInstance().addTorpedoesEne(1);
										break;
								}
								
								nbInSalveT++;
							}
						}
						if ((nbInSalveT == nbInSalveMaxT) && !loadingT)
						{
							loadingT=true;
							timeSinceLastLaunchT=timeBetweenLaunchesT;
						}
					}
					else
					{
						if (tmpUnit.getType() == FUnit.UNKNOWN_AIR)
							tmpUnit.setType(FUnit.UNKNOWN_AIR | (1-typeEnemy));
						//System.out.println("Surface to sink");
						// Missile !!!
						if ((this.nbMissiles > 0) && (this.fireAtWill) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() != FUnit.MISSILE))
						{
//							if ((typeEnemy == 1) && (tmpUnit.getType() == FUnit.BASE_OUR))
//							{
//								System.out.println("Bad guy, seen our base "+ tmpUnit.getType()+" - "+ (tmpUnit.getType() & 1)+ " : "+((tmpUnit.getType() & 1) == typeEnemy));
//							}
							if (this.isIgnoreFriends() || 
									(!this.ignoreFriends && ((tmpUnit.getType() & 1) == typeEnemy) ))
							{
								if (owner != null)
								{
									if (owner.getIdMap() != tmpUnit.getIdPU())
									{
										fireMissile(tmpUnit);
									}
								}
								else
								{
									fireMissile(tmpUnit);
								}
								//System.out.println(this.getIdBoat()+" - Fire on "+(tmpUnit.getType()));
							}
						}
						if ((this.nbMissiles > 0) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() == FUnit.MISSILE) && (tmpUnit.getTypeFaction() == this.typeEnemy))
						{
							//System.out.println("Missile to intercept");
							Missile oneMissile= new Missile(this.theMap);
							oneMissile.setKnowAllies(true);
							oneMissile.setKnowEnemies(true);
							oneMissile.setSeek(true);
							oneMissile.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
							oneMissile.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.4, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.4, 0);
							oneMissile.setCurrentSpeed(800);
							oneMissile.setMaxSpeed(1000);
							oneMissile.setOrientation(this.getOrientation());
							oneMissile.setPosX(this.getPosX());
							oneMissile.setPosY(this.getPosY());
							oneMissile.setIdOwner(this.idBoat);
							oneMissile.setTypeFaction(this.typeFaction);
							this.theMap.addMissile(oneMissile);

							nbMissiles--;
							switch (this.typeFaction)
							{
								case FUnit.AIRPLANE_OUR:
									GameKeeper.getInstance().addMissiles(1);
									break;
								case FUnit.AIRPLANE_ALLIED:
									GameKeeper.getInstance().addMissilesAllies(1);
									break;
								case FUnit.AIRPLANE_ENEMY:
									GameKeeper.getInstance().addMissilesEne(1);
									break;
							}
							nbInSalveM++;
						}
						if ((nbInSalveM >= nbInSalveMaxM) && !loadingM)
						{
							loadingM=true;
							timeSinceLastLaunchM=timeBetweenLaunchesM;
						}
					}
				}
			}
		}
			
		if  ( (((nbMissiles == 0) && hasMissiles) || ((nbTorpedoes == 0) && hasTorpedoes))
				&& !reloadNeeded)
		{
			reloadNeeded=true;
			xSaveWP = programmedWPs.getFirst().getXWP();
			ySaveWP = programmedWPs.getFirst().getYWP();
			//System.out.println("Airplane "+this.idBoat+ " need to reload");
			if (owner != null)
			{
				//System.out.println("Airplane "+this.idBoat+ " need to reload 2 "+owner.getType());
				// Reloading owner might be Carrier or base (0x8#)
				if ((owner.getType() == Boat.CARRIER) || ((owner.getType() & 0x80) == 0x80))
				{
					programmedWPs.getFirst().setXWP(owner.posX);
					programmedWPs.getFirst().setYWP(owner.posY);
					//System.out.println("Airplane "+this.idBoat+ " will reload");
				}
			}
		}
	}

	/**
	 * @param tmpUnit
	 */
	private void fireMissile(FUnit tmpUnit)
	{
		Missile oneMissile= new Missile(this.theMap);
		if (typeEnemy == 1)
		{
			//System.out.println(this.getIdBoat()+" - Fire on "+(tmpUnit.getType()));
			oneMissile.setKnowAllies(false);
			oneMissile.setKnowEnemies(true);
		}
		if (this.ignoreFriends)
		{
			oneMissile.setKnowAllies(false);
			oneMissile.setKnowEnemies(false);
		}

		oneMissile.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
		oneMissile.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.6, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.6, 0);
		oneMissile.setOrientation(this.getOrientation()+nbInSalveM*0.1);
		oneMissile.setCurrentSpeed(500);
		oneMissile.setMaxSpeed(1500);
		oneMissile.setDamage(40);
		oneMissile.setPosX(this.getPosX()+nbInSalveM);
		oneMissile.setPosY(this.getPosY());
		oneMissile.setIdOwner(this.idBoat);
		oneMissile.setTypeFaction(this.typeFaction);
		this.theMap.addMissile(oneMissile);

		nbMissiles--;

		switch (this.typeFaction)
		{
			case FUnit.AIRPLANE_OUR:
				GameKeeper.getInstance().addMissiles(1);
				break;
			case FUnit.AIRPLANE_ALLIED:
				GameKeeper.getInstance().addMissilesAllies(1);
				break;
			case FUnit.AIRPLANE_ENEMY:
				GameKeeper.getInstance().addMissilesEne(1);
				break;
		}
		
		nbInSalveM++;
	}

	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.ProgrammableUnit#doUpdate()
	 */
	@Override
	public void doUpdate(double time)
	{
		if (dead && !finallydead)
		{
			updateAttachedObjects();
			
			this.posX = this.posX + time*this.currentSpeed*Math.cos(this.orientation);
			this.posY = this.posY + time*this.currentSpeed*Math.sin(this.orientation);
			
			this.updateMe(posX, posY, 0, this.orientation, this.currentSpeed);
			this.currentSpeed -= 10;
			if (this.currentSpeed <0)
				this.currentSpeed = 0;
		}
		if (!dead)
		{
			if (owner != null)
			{
				this.attackNeutrals = owner.attackNeutrals;
				this.ignoreFriends = owner.ignoreFriends;
			}
			if (reloadNeeded)
			{
				if (owner != null)
				{	
					if ((owner.getType() == Boat.CARRIER) || ((owner.getType() & 0x80) == 0x80))
					{
						programmedWPs.getFirst().setXWP(owner.posX);
						programmedWPs.getFirst().setYWP(owner.posY);
					}
				}
			}
			// TODO Auto-generated method stub
			
			if (this.posX < 0)
			{
				if (this.currentSpeed > 0)
					this.accX(20);
				else
					this.accX(-20);
				checkAndNormaliseSpeed();
			}
			if (this.posY < 0)
			{
				if (this.currentSpeed > 0)
					this.accY(20);
				else
					this.accY(-20);
				checkAndNormaliseSpeed();
			}
			if (this.posX > TacticalMapPC.START_MENU)
			{
				if (this.currentSpeed > 0)
					this.accX(-20);
				else
					this.accX(20);
				checkAndNormaliseSpeed();
			}
			if (this.posY > 800)
			{
				if (this.currentSpeed > 0)
					this.accY(-2);
				else
					this.accY(2);
				checkAndNormaliseSpeed();
			}
			
			this.posX = this.posX + time*this.currentSpeed*Math.cos(this.orientation);
			this.posY = this.posY + time*this.currentSpeed*Math.sin(this.orientation);
			
			depthWater = ourLevelKeeper.getAlpha((int )this.posX, (int )this.posY)-255;
			
			//System.out.println("Depth "+depthWater);
				
			if (!unmanned)
			{
				//	If no waypoints, we turn!
				if (this.programmedWPs.isEmpty() || (!followWP))
				{
					if (this.currentSpeed < this.wantedSpeed)
						this.currentSpeed += 2;
					else if (this.currentSpeed > this.wantedSpeed)
							this.currentSpeed -= 2;
					//this.orientation += 0.04*time;
				}
				else
				{
					// Got to the next WP
					seekWP();
				}
			}

			this.updateMe(posX, posY, 0, this.orientation, this.currentSpeed);
			updateAttachedObjects();

			for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
			{
				sensors.get(iSensors).doUpdate(time);

				foundUnits = sensors.get(iSensors).getFoundUnits();
				
				attackIfNeeded(time);
			}
			foundUnits = this.theMap.getOurKD().getFoundUnits();
			
			attackIfNeeded(time);
			
		}
		else if (explodeTime < 10)
		{
				explodeTime++;
				explosion.setSize(explosion.getSize()+0.2);
				//System.out.println("Explode !!!!");
				SndContainer.playExplosion3Sound();
		}
		else if (iDead > -1)
		{
			explosion.invalidate();
			this.setDepth(iDead--);
			//smokeBlack.invalidate();
		}
		else if (iDead > depthWater)
		{
			this.setDepth(iDead--);
		}
		else if ((iDead >= depthWater) && (!finallydead))
		{
			smokeBlack.invalidate();
			this.currentSpeed=0;
			this.maxSpeed=0;
			wantedSpeed=maxSpeed;
			finallydead=true;
			removeMeWOBody();
		}
		else
		{
			explodeTime++;
		}
	}


	public int addAttachedObject(IAttachable newObject)
	{
		newObject.setOrientationAttach(this.orientation);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getPosX(), this.getPosY());
		transformToApply.rotate(this.getOrientation(), 0, 0);
		//transformToApply.scale(this.getSize(), this.getSize());
		onePoint2D.setLocation(newObject.getXAttach(), newObject.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		newObject.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,0);
		
		attachedObjects.add(newObject);
		return (attachedObjects.size() - 1);
	}
	
	public void updateAttachedObject(IAttachable objectToUpdate)
	{
		objectToUpdate.setOrientationAttach(this.orientation);

		transformToApply.setToIdentity();
		transformToApply.translate(this.getPosX(), this.getPosY());
		transformToApply.rotate(this.getOrientation(), 0, 0);
		//transformToApply.scale(this.getSize(), this.getSize());
		onePoint2D.setLocation(objectToUpdate.getXAttach(), objectToUpdate.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		objectToUpdate.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,0);
	}
	
	public void updateAttachedObjects()
	{
		for (int iObjects=0;iObjects < attachedObjects.size() ; iObjects++)
		{
			updateAttachedObject(attachedObjects.get(iObjects));
		}
	}
	
	public void washAllAttachedObjects()
	{
		attachedObjects.clear();
	}
	
	public void damageBoat(int amount)
	{
		if (!dead)
		{
			double dist=Math.random();
			double dist2=Math.random();
			this.hullState-=dist*amount/this.resistance;
			this.navState-=dist2*amount/this.resistance;
			this.dataLinkState-=Math.random()*amount/this.resistance;

			this.complement-=5*Math.random()*amount/this.resistance;

			if (navState <0)
			{
				navState=0;
			}
			if (dataLinkState <0)
			{
				dataLinkState=0;
			}
			if (complement <0)
			{
				complement=0;
			}
			if (this.complement == 0)
			{
				unmanned = true;
			}
			if (this.hullState <= 0)
			{
				hullState=0;
				dead = true;
				explosion.validate();

				for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
				{
					sensors.get(iSensors).removeMe();
				}
				sensors.clear();
				for (int iSensors=0;iSensors < deportedSensors.size() ; iSensors++)
				{
					deportedSensors.get(iSensors).desactivate();
				}
				deportedSensors.clear();

				iDead=0;
				
				theMap.boatDead(this.typeFaction);
				
				if (owner != null)
				{
					owner.airplaneDead();
					if (this.getTypeFaction() == FUnit.AIRPLANE_ENEMY)
					{
						GameKeeper.getInstance().addCostEnemies(this.getCost());
					}
					else if (this.getTypeFaction() == FUnit.AIRPLANE_ALLIED)
					{
						GameKeeper.getInstance().addCostAllies(this.getCost());
					}
					else
					{
						GameKeeper.getInstance().addCostOur(this.getCost());
					}
					if (owner.getComplement() > 2)
					{
						owner.setComplement(owner.getComplement()-this.complementNorm);
					}
				}
				
				if (depthWater < -200)
					depthWater=-200;
				if (depthWater > -10)
					depthWater=-15;
			}
			if ((this.hullState < 50) && (!damaged))
			{
				damaged = true;
				this.maxSpeed-=200;
				wantedSpeed=maxSpeed;
				if (currentSpeed > maxSpeed)
					currentSpeed = maxSpeed;

				smokeBlack.validate();
				this.showMe();
			}
		}
		//System.out.println(" Boat hit Hull "+hullState+"% navState "+navState+"% "+" dataLinkState "+dataLinkState+"% Complement: "+complement);
	}

	@Override
	public void setView(boolean view) {
		// TODO Auto-generated method stub
		super.setView(view);
		for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
		{
			sensors.get(iSensors).setHiddenFUnits(!view);
			if (view)
				sensors.get(iSensors).show();
			else
				sensors.get(iSensors).hide();
		}
		/*
		if (view)
		{
			boatSquare.validate();
		}
		else
		{
			boatSquare.invalidate();
		}
		*/
	}

	public boolean isDead() {
		return dead;
	}

	public int getNbTorpedoes() {
		return nbTorpedoes;
	}

	public void setNbTorpedoes(int nbTorpedoes) {
		this.nbTorpedoes = nbTorpedoes;
	}

	public int getNbTubes() {
		return nbTubes;
	}

	public void setNbTubes(int nbTubes) {
		this.nbTubes = nbTubes;
	}

	public int getNbMissiles() {
		return nbMissiles;
	}

	public void setNbMissiles(int nbMissiles) {
		this.nbMissiles = nbMissiles;
	}

	public double getHullState() {
		return hullState;
	}

	public void setHullState(int hullState) {
		this.hullState = hullState;
	}

	public double getDataLinkState() {
		return dataLinkState;
	}

	public void setDataLinkState(int dataLinkState) {
		this.dataLinkState = dataLinkState;
	}

	public double getNavState() {
		return navState;
	}

	public void setNavState(int navState) {
		this.navState = navState;
	}

	@Override
	public void setIdBoat(int idBoat) {
		// TODO Auto-generated method stub
		super.setIdBoat(idBoat);
		for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
		{
			sensors.get(iSensors).setIdOwner(idBoat);
		}
	}
	
	public boolean isRadarOn()
	{
		for (int iSensors=0;iSensors < sensors.size();iSensors++)
		{
			if (sensors.get(iSensors).getMyType() == Sensor.RADAR)
			{
				if (sensors.get(iSensors).isActive())
					return true;
			}
		}
		return false;
	}
	
	public boolean isSonarOn()
	{
		for (int iSensors=0;iSensors < sensors.size();iSensors++)
		{
			if (sensors.get(iSensors).getMyType() == Sensor.SONAR)
			{
				if (sensors.get(iSensors).isActive())
					return true;
			}
		}
		return false;
	}
	
	public void pushRadarOnOff(boolean isOn)
	{
		for (int iSensors=0;iSensors < sensors.size();iSensors++)
		{
			if (sensors.get(iSensors).getMyType() == Sensor.RADAR)
			{
				if (isOn)
					sensors.get(iSensors).activate();
				else
					sensors.get(iSensors).desactivate();
			}
		}
	}
	
	public void pushSonarOnOff(boolean isOn)
	{
		for (int iSensors=0;iSensors < sensors.size();iSensors++)
		{
			if (sensors.get(iSensors).getMyType() == Sensor.SONAR)
			{
				if (isOn)
					sensors.get(iSensors).activate();
				else
					sensors.get(iSensors).desactivate();
			}
		}
	}
	/*
	 * Level edition facilities (kind of serialisation)
	 */
	public String saveBoat()
	{
		// Create a string containing all needed infos.
		StringBuffer stringToConstruct=new StringBuffer();
		
		stringToConstruct.append(this.idBoat);
		stringToConstruct.append(":");
		stringToConstruct.append(this.attackNeutrals);
		stringToConstruct.append(":");
		stringToConstruct.append(this.complement);
		stringToConstruct.append(":");
		stringToConstruct.append(this.cost);
		stringToConstruct.append(":");
		stringToConstruct.append(this.currentSpeed);
		stringToConstruct.append(":");
		stringToConstruct.append(this.dataLinkState);
		stringToConstruct.append(":");
		stringToConstruct.append(this.dead);
		stringToConstruct.append(":");
		stringToConstruct.append(this.energy);
		stringToConstruct.append(":");
		stringToConstruct.append(this.fireAtWill);
		stringToConstruct.append(":");
		stringToConstruct.append(this.ignoreFriends);
		stringToConstruct.append(":");
		stringToConstruct.append(this.loop);
		stringToConstruct.append(":");
		stringToConstruct.append(this.maxSpeed);
		stringToConstruct.append(":");
		stringToConstruct.append(this.nbInSalveMaxM);
		stringToConstruct.append(":");
		stringToConstruct.append(this.nbInSalveMaxT);
		stringToConstruct.append(":");
		stringToConstruct.append(this.nbMissiles);
		stringToConstruct.append(":");
		stringToConstruct.append(this.nbTorpedoes);
		stringToConstruct.append(":");
		stringToConstruct.append(this.nbTubes);
		stringToConstruct.append(":");
		stringToConstruct.append(this.noiseLevel);
		stringToConstruct.append(":");
		stringToConstruct.append(this.noiseSignature);
		stringToConstruct.append(":");
		stringToConstruct.append(this.name);
		stringToConstruct.append(":");
		stringToConstruct.append(this.orientation);
		stringToConstruct.append(":");
		stringToConstruct.append(this.posX);
		stringToConstruct.append(":");
		stringToConstruct.append(this.posY);
		stringToConstruct.append(":");
		stringToConstruct.append(this.resistance);
		stringToConstruct.append(":");
		stringToConstruct.append(this.standardSpeed);
		stringToConstruct.append(":");
		stringToConstruct.append(this.timeBetweenLaunchesM);
		stringToConstruct.append(":");
		stringToConstruct.append(this.timeBetweenLaunchesT);
		stringToConstruct.append(":");
		stringToConstruct.append(this.type);
		stringToConstruct.append(":");
		stringToConstruct.append(this.typeFaction);
		stringToConstruct.append(":");
		stringToConstruct.append(this.typeLauncher);
		stringToConstruct.append(":");
		stringToConstruct.append(this.unmanned);
		stringToConstruct.append(":");
		stringToConstruct.append(this.visibilityLevel);
		stringToConstruct.append(":");
		stringToConstruct.append(this.visibilitySignature);
		stringToConstruct.append(":WPS-");
		
		// Wps array
		for (int iWp=0;iWp<this.programmedWPs.size();iWp++)
		{
			stringToConstruct.append(iWp+","+programmedWPs.getWP(iWp).getXWP()+","+programmedWPs.getWP(iWp).getYWP()+":");
		}
		// Sensors array
		
		return stringToConstruct.toString();
	}
	
	public void loadBoat(String savedBoat)
	{
		//Same thing, just the opposite!
	}

	public ProgrammableUnit getOwner() {
		return owner;
	}

	public void setOwner(ProgrammableUnit owner) {
		this.owner = owner;
		owner.addAirplaneInFlight();
	}

	@Override
	public void doUpdateSM(double time)
	{
		// TODO Auto-generated method stub
		
	}

	public boolean hasTorpedoes()
	{
		return hasTorpedoes;
	}

	public void setHasTorpedoes(boolean hasTorpedoes)
	{
		this.hasTorpedoes = hasTorpedoes;
	}

	public boolean hasMissiles()
	{
		return hasMissiles;
	}

	public void setHasMissiles(boolean hasMissiles)
	{
		this.hasMissiles = hasMissiles;
	}

	public int getNbTorpedoesNorm()
	{
		return nbTorpedoesNorm;
	}

	public void setNbTorpedoesNorm(int nbTorpedoesNorm)
	{
		this.nbTorpedoesNorm = nbTorpedoesNorm;
	}

	public int getNbMissilesNorm()
	{
		return nbMissilesNorm;
	}

	public void setNbMissilesNorm(int nbMissilesNorm)
	{
		this.nbMissilesNorm = nbMissilesNorm;
	}

	@Override
	public void levelUp()
	{
		// TODO Auto-generated method stub
		this.level = this.newLevelReachable;
		this.energy+=10;
		this.resistance+=10;
		this.timeBetweenLaunchesM/=0.99;
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
	public List<String> loadIfSupported(List<String> myPart)
	{
		// TODO Auto-generated method stub
		return myPart;
	}

	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return SAVE_ID;
	}

	@Override
	public String saveIfNeeded()
	{
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getTypeName()
	{
		switch( type )
		{
		case FIGHTER:
			return "Fighter";
		case BOMBER:
			return "Bomber";
		case HELI:
			return "Helicopter";
		case RADARPLANE:
			return "Radar Plane";
		case ESMPLANE:
			return "ESM Plane";
		case TRANSPORT:
			return "Transport";
		case TANKER:
			return "Tanker";
		case COMMAND:
			return "Command";
		case CARGOPLANE:
			return "Cargo";
		case AIRLINE:
			return "Airline";
		case PLEASANCEPLANE:
			return "Small civil";
			
		default:
			return "Unknown";
		}
	}
}
