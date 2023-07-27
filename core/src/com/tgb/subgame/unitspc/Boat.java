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
import java.util.Random;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.UtilsPC;
import com.tgb.subengine.gameentities.Vector2D;
import com.tgb.subengine.gameentities.Waypoint;
import com.tgb.subengine.gfxentities.CirclePC;
import com.tgb.subengine.gfxentities.IAttachable;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.gfxentities.SimpleLinePC;
import com.tgb.subengine.gfxentities.Text;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.ParticleSpritePC;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subgame.TacticalMapPC;
import com.tgb.subgame.RandomContainer;
import com.tgb.subgame.unitspc.sensors.SimpleAround;
import com.tgb.subgame.unitspc.sensors.DeportedRadar;
import com.tgb.subgame.unitspc.sensors.Sensor;
import com.tgb.subgame.GameKeeper;
import com.tgb.subgame.GfxRanks;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.StrategicMap;
import com.tgb.subgame.MapKeeper;

import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;

/**
 * Super-class for the boats
 * @author Alain Becam
 * TODO refactoring... Extract the "common data parts" (with boat, sub and base), and allow a quite smart
 * refactoring (or better, a DB mapping)
 */
public class Boat extends ProgrammableUnit 
{
	public static final String SAVE_ID = "Boat";

	// To Save
	double orientation;
	
	double rudder = 0;
	
	double xShift,yShift;
	
	double currentSpeed=0; // In knots
	
	double maxSpeed=35; // In knots
	double standardSpeed=20;
	double silenceSpeed=15;
	
	// End To Save
	// Two next line eventually to save... (used in the map too)
	double speedX=0,speedY=0,speedZ=0;
	double wantedSpeedX=0,wantedSpeedY=0,wantedSpeedZ=0;
	double tmpSpeedX=0,tmpSpeedY=0,tmpSpeedZ=0,tmpSpeedN=0;
	
	// To Save
	int noiseSignature; // How silent this unit can be
	int visibilitySignature;
	int noiseLevel; // 0-> None, 100-> enough :)
	int visibilityLevel; // 100-> Surface (perfectly visible if no fog/clouds), 0-> Very deep (not visible at all)
	ArrayList<Sensor> sensors;
	ArrayList weapons;
	ArrayList countermeasures;
	
	int typeEnemy; // Mask result to find the enemy. 1 for allies (1 && allies = 1), 0 otherwise (1 && enemy = 0)
	// End To Save
	
	public static final int CARRIER=0x10;
	public static final int FRIGATE=0x11;
	public static final int DESTROYER=0x12;
	public static final int CRUISER=0x13;
	public static final int CORVETTE=0x14;
	public static final int AMPHIBIOUS=0x15;
	
	public static final int CARGO=0x20;
	public static final int TANKER=0x21;
	public static final int FISHING_BOAT=0x22;
	public static final int PLEASANCE=0x23;
	public static final int CRUISESHIP=0x24;
	
	// To Save
	int resistance=4;
	
	long cost;
	
	int tonnage;
	
	// The different parts, ok or damaged.
	double hullState=100;
	double dataLinkState=100;
	double navState=100;
	
	boolean damaged=false;	
	
	boolean unmanned=false;
	// End To Save
	
	double iDead; // to sunk
	double depthWater=200; // How much can we sink :)
	
	transient SpritePC boatBody;
	transient Emitter smokeEmitter; // For enemies's torpedoes, both the torpedo and the bubbles will appear if found (by gravitation or passive/active sonar)
	transient Particles smoke;
	transient Emitter bubblesEmitter; 
	transient Particles bubbles;
	transient SimpleLinePC  fireLine;
	
	transient SpritePC boatSquare;
	transient Text nameGfx;
	
	transient Particles smokeBlack;
	transient Emitter badSmokeEmitter; 
	
	transient SpritePC rankSprite;
	
	transient CirclePC ourCircle;
	
	transient long idSmoke;  // Ids to remove the gfx elements.
	transient long idBody;
	transient long idBubbles;
	transient long idBadSmoke;
	transient long idExplosion;
	transient long idBoatSquare;
	transient long idFireLine;
	transient long idName;
	transient long idRank;
	transient long idCircle;
	
	transient ArrayList<IAttachable> attachedObjects; // Attached object, will be moved and rotate with the entity
	transient AffineTransform transformToApply; // Common affine transform
	
	Point2D.Double onePoint2D ;
	Point2D.Double onePoint2Dtmp ;
	
	int explodeTime=0;
	transient SpritePC explosion;
	
	// To Save
	int nbTorpedoes;
	int nbTorpedoesMax;
	int nbTubes; // Will change the time to fire new torpedoes
	private int nbMissiles;
	private int nbMissilesMax;
	int typeLauncher;
	
	double timeBeforeAirLaunches; // Counter between two airplanes launches
	double timeBetweenAirLaunches; // Time between two airplanes launches
	// End To Save
	
	boolean loadingA=false;
	
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
	
	double timeSinceLastLaunchC;
	double timeBetweenLaunchesC;
	boolean loadingC=false;
	
	// To Save
	int nbAmmo;
	double rangeInterception;
	int explodeTimeMax;
	// End To Save
	
	Random myRandom;
	
	
	/**
	 * @param theMap
	 */
	public Boat(LevelMap theMap)
	{
		super(theMap);
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		
		currentSpeed=0; // In knots
		
		maxSpeed=35; // In knots
		standardSpeed=20;
		silenceSpeed=15;
		
		nbTorpedoes = 80;
		nbTorpedoesMax = 80;
		nbTubes = 6;
		nbMissiles = 400;
		nbMissilesMax = 400;
		
		timeBetweenLaunchesM = 0.4;
		nbInSalveMaxM = 4;
			
		timeBetweenLaunchesT = 3;
		nbInSalveMaxT=20;
		
		nbFighters=1000;
		nbFightersOnFlightMax = 10;
		nbAwacs=2;
		
		timeBetweenAirLaunches= 1;
		
		timeBetweenLaunchesC=0.02;
		nbAmmo=40000;
		
		rangeInterception=4000;
		// Default
		type = CARRIER;
		
		wantedSpeed=maxSpeed;
		
		explodeTimeMax = 10;
		
		myRandom = RandomContainer.getInstance().myRandom;
	}
	
	public Boat()
	{
		super();
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		
		currentSpeed=0; // In knots
		
		maxSpeed=35; // In knots
		standardSpeed=20;
		silenceSpeed=15;
		
		nbTorpedoes = 80;
		nbTorpedoesMax = 80;
		nbTubes = 6;
		nbMissiles = 400;
		nbMissilesMax = 400;
		
		timeBetweenLaunchesM = 0.2;
		nbInSalveMaxM = 4;
			
		timeBetweenLaunchesT = 3;
		nbInSalveMaxT=20;
		
		nbFighters=1000;
		nbFightersOnFlightMax = 10;
		nbAwacs=2;
		
		timeBetweenAirLaunches= 1;
		
		timeBetweenLaunchesC=0.02;
		nbAmmo=40000;
		
		rangeInterception=4000;
		// Default
		type = CARRIER;
		
		wantedSpeed=maxSpeed;
		
		explodeTimeMax = 10;
		
		myRandom = RandomContainer.getInstance().myRandom;
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
	 * @return the silenceSpeed
	 */
	public double getSilenceSpeed()
	{
		return silenceSpeed;
	}
	/**
	 * @param silenceSpeed the silenceSpeed to set
	 */
	public void setSilenceSpeed(double silenceSpeed)
	{
		this.silenceSpeed = silenceSpeed;
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
	public long getCost()
	{
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(long cost)
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
		{
			this.angleToTurn = angle*(currentSpeed/10);
			//System.out.println("Turning "+angle+" - "+rudderEfficiency+" - "+currentSpeed+ " - "+angle*rudderEfficiency*(currentSpeed/10));
		}
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
	
	@Override
	public void setFollowWP(boolean followWP)
	{
		if (this.followWP != followWP)
		{
			this.followWP = followWP;
			if (followWP)
			{
				this.wantedSpeed = this.maxSpeed;
			}
		}
	}

	/**
	 * Return the default image for the sub.
	 * @return the correct image
	 */
	public static CoreImage getImageForMe()
	{
		return gfxSprites.getImageBoat();
	}
	/**
	 * Create the graphics entity
	 * If you have more than one sub (should be :) ), it is better to use the second
	 * method, with Image as parameter: so the image is shared, not loaded for all.
	 */
	public void createGfx(double x, double y, double z, double direction, double speed)
	{
		
		CoreImage boatImage;
		
		switch (this.type)
		{
			case Boat.CARRIER:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoat();
					rangeInterception = 8000;
				}
				else
				{
					boatImage=gfxSprites.getImageBoatCarrierEne();
				}
				break;
			case Boat.CRUISER:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatNorm();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatCruiserEne();
				}
				break;
			case Boat.DESTROYER:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatDestroyer();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatDestroyerEne();
				}
				break;
			case Boat.FRIGATE:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatFrigate();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatFrigateEne();
				}
				break;
			case Boat.CORVETTE:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatCorvette();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatCorvetteEne();
				}
				break;
			case Boat.CARGO:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatCargo();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatCargoEne();
				}
				break;
			case Boat.TANKER:
				boatImage=gfxSprites.getImageBoatTanker();
				
				break;
			case Boat.FISHING_BOAT:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatFishingShip();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatFishingShipEne();
				}
				break;
			case Boat.PLEASANCE:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatPleasance();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatPleasanceEne();
				}
				break;
			case Boat.CRUISESHIP:
				if (this.typeFaction != FUnit.BOAT_ENEMY)
				{
					boatImage=gfxSprites.getImageBoatCruise();
				}
				else
				{
					boatImage=gfxSprites.getImageBoatCruiseEne();
				}
				break;
			default:
				boatImage=gfxSprites.getImageBoatNorm();
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
		if (!dead)
		{
			boatBody = new SpritePC(boatImage);
			
			switch (this.type)
			{
			case Boat.CARRIER:
				boatBody.setSize(1.1);
				break;
			case Boat.CRUISER:
				boatBody.setSize(1.0);
				break;
			default:
				boatBody.setSize(1.0);
			}
			if (this.getTonnage() >= 700000)
			{
				boatBody = new SpritePC(gfxSprites.getImageBoatBattleship());
				boatBody.setSize(1.5);
				explodeTimeMax=40;
			}
			
			boatBody.setRotation(direction);
			xShift=40;
			yShift=10;
			boatBody.setPos(x, y, z);
			idBody = RenderingManager.getInstance().addDrawableEntity(boatBody,20); // Surface

			boatSquare = new SpritePC(gfxSprites.getSelImage());
			boatSquare.setSize(1.2);
			boatSquare.invalidate();
			boatBody.addAttachedObject(boatSquare);
			idBoatSquare = RenderingManager.getInstance().addDrawableEntity(boatSquare,39);

			nameGfx = new Text();
			nameGfx.setTextToShow(this.getName()+" "+this.getLevel());
			
			CoreFont font = CoreFont.getSystemFont();
			
			int originalWidth = font.getStringWidth(nameGfx.getTextToShow());
			int originalHeight = font.getHeight();

			nameGfx.setSizeRect(originalWidth,originalHeight);
			nameGfx.setPosAttach(0, -20, 0);
			nameGfx.invalidate();
			boatBody.addAttachedObject(nameGfx);
			idName = RenderingManager.getInstance().addDrawableEntity(nameGfx,39);

			
			explosion = new SpritePC(gfxSprites.getImageFlash2());	
			explosion.invalidate();
			explosion.setSize(3);
			explosion.setPosAttach(myRandom.nextInt(10)-5, myRandom.nextInt(8)-4, 0);
			boatBody.addAttachedObject(explosion);
			
			idExplosion = RenderingManager.getInstance().addDrawableEntity(explosion,21);

			if (this.type != Boat.CARRIER && this.type != Boat.PLEASANCE)
			{
				ParticleSpritePC newParticle= new ParticleSpritePC(gfxSprites.getImageSmoke());

				newParticle.setEnergy(10);
				newParticle.setSize(0.2);
				newParticle.setTimeLeft(5000);
				newParticle.setSizeIncDec(1.002);

				newParticle.setAlive(true);

				smokeEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT_DIRECTIONNAL, 2, 600, 0, 0.1, 5000, newParticle);

				if (this.type == Boat.CARRIER)
				{
					smokeEmitter.setPosAttach(0, -7, 0);
				}
				else if (this.type == Boat.CORVETTE)
				{
					smokeEmitter.setPosAttach(-3, 0, 0);
					newParticle.setSize(0.08);
				}
				else if (this.type == Boat.FRIGATE)
				{
					smokeEmitter.setPosAttach(0, 0, 0);
					newParticle.setSize(0.1);
				}
				else if (this.type == Boat.CARGO)
				{
					smokeEmitter.setPosAttach(-4, 0, 0);
					newParticle.setSize(0.12);
				}
				else if (this.type == Boat.TANKER)
				{
					smokeEmitter.setPosAttach(-4, 0, 0);
					newParticle.setSize(0.12);
				}
				else if (this.type == Boat.CRUISESHIP)
				{
					smokeEmitter.setPosAttach(-2, 0, 0);
					newParticle.setSize(0.1);
				}
				else if (this.type == Boat.FISHING_BOAT)
				{
					smokeEmitter.setPosAttach(0, 0, 0);
					newParticle.setSize(0.06);
				}
				else
				{
					smokeEmitter.setPosAttach(0, 0, 0);
				}

				boatBody.addAttachedObject(smokeEmitter);
				smoke=new Particles(100,smokeEmitter,newParticle);
				idSmoke =  RenderingManager.getInstance().addParticles(smoke, 20);
			}
			ParticleSpritePC blackSmoke= new ParticleSpritePC(gfxSprites.getImageDarkSmoke());

			blackSmoke.setEnergy(10);
			blackSmoke.setSize(0.3);
			blackSmoke.setTimeLeft(5000);
			blackSmoke.setSizeIncDec(1.001);

			blackSmoke.setAlive(true);


			badSmokeEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 4, 600, 0, 0.3, 5000, blackSmoke);
			badSmokeEmitter.setPosAttach(myRandom.nextInt(8)-4, 0, 0);
			boatBody.addAttachedObject(badSmokeEmitter);
			smokeBlack=new Particles(100,badSmokeEmitter,blackSmoke);
			if ((!damaged) || (dead))
				smokeBlack.invalidate();
//			bubblesEmitter = new Emitter(x, y, 1, 10, 10, 0, Math.PI/4, Emitter.POINT_DIRECTIONNAL, 20, 50, 0, 0.1, 5000);
//			bubbles=new Particles(500,bubblesEmitter,ParticlePC);
//			bubblesEmitter.setPosAttach(40, 1, 0);
//			boatBody.addAttachedObject(bubblesEmitter);

			
			idBadSmoke =  RenderingManager.getInstance().addParticles(smokeBlack, 20);
//			idBubbles = RenderingManager.getInstance().addParticles(bubbles, 19);
			
			// Canon fire line
			fireLine = new SimpleLinePC();
			fireLine.setPos(0, 0, 0);
			fireLine.setPosEnd(1,0);

			if (this.type == Boat.CARRIER)
			{
				fireLine.setPosAttach(-33, -9, 0);
			}
			else if (this.type == Boat.CRUISER)
			{
				fireLine.setPosAttach(5.5, -4.5, 0);
			}
			else if (this.type == Boat.CRUISER)
			{
				fireLine.setPosAttach(5.5, -4.5, 0);
			}
			
			fireLine.setOurColor(0xffffff99);
			fireLine.invalidate();
			boatBody.addAttachedObject(fireLine);
			idFireLine =  RenderingManager.getInstance().addDrawableEntity(fireLine, 21);
		}
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
		if ((!dead) && (!damaged))
		{
			boatBody.invalidate();
			if (this.type != Boat.CARRIER && this.type != Boat.PLEASANCE)
				smoke.invalidate();
			smokeBlack.invalidate();
			fireLine.invalidate();
			for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
			{
				sensors.get(iSensors).hide();
			}
		}
//		bubbles.invalidate();
	}
	
	public void showMe()
	{
		if (boatBody != null)
			boatBody.validate();
		if (smoke != null)
			smoke.validate();
		if (damaged && !dead)
	    	smokeBlack.validate();
//		bubbles.validate();
		if (view)
		{
			if (sensors != null)
			{
				for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
				{
					sensors.get(iSensors).show();
				}
			}
		}
	}
	
	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMe()
	{
		RenderingManager.getInstance().removeEntity(idBody, 20);
		RenderingManager.getInstance().removeParticles(idSmoke, 20);
		RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
		RenderingManager.getInstance().removeEntity(idExplosion, 21);
		RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
		RenderingManager.getInstance().removeEntity(idFireLine,21);
		RenderingManager.getInstance().removeEntity(idName, 39);
		
		smokeBlack = null;
		bubbles = null;
		smoke = null;
		smokeEmitter = null;
		bubblesEmitter = null; 
		badSmokeEmitter = null; 
		
		boatBody = null;
		
		fireLine = null;
		
		boatSquare = null;
		nameGfx = null;
		
		explosion = null;
		
		if (sensors != null)
		{
			for (int iSensor = 0; iSensor < sensors.size() ; iSensor++)
			{
				// Update !!!
				sensors.get(iSensor).removeMe();
			}
			sensors.clear();
		}
		washAllAttachedObjects();
		if (programmedWPs != null)
		{
			this.programmedWPs.clear();
			this.programmedWPs=null;
		}
	}
	
	public void removeMeWOBody()
	{
		RenderingManager.getInstance().removeParticles(idSmoke, 20);
		RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
		RenderingManager.getInstance().removeEntity(idExplosion, 21);
		RenderingManager.getInstance().removeEntity(idFireLine,21);
		
		smokeBlack = null;
		bubbles = null;
		smoke = null;
		smokeEmitter = null;
		bubblesEmitter = null; 
		badSmokeEmitter = null; 
		
		if (sensors != null)
		{
			for (int iSensor = 0; iSensor < sensors.size() ; iSensor++)
			{
				// Update !!!
				sensors.get(iSensor).removeMe();
			}
			sensors.clear();
		}
		
		washAllAttachedObjects();
		
		if (programmedWPs != null)
		{
			this.programmedWPs.clear();
			//this.programmedWPs=null;
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
		
		if (Math.abs(speedX) > 0.00001)
		{
			this.orientation = Math.acos(speedX/actualSpeed);
		}
		else
		{
			if (Math.abs(speedY) > 0.00001)
			{
				this.orientation = Math.PI/2;
			}
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
			actualSpeed=maxSpeed;
		}	
//		System.out.println("Current speed "+currentSpeed);
//		System.out.println("Actual speed "+actualSpeed);
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
	
//	public void seekWP()
//	{
//		if (!this.programmedWPs.isEmpty())
//		{
//			if (!started)
//			{
//				this.targetX=programmedWPs.getFirst().getXWP();
//				this.targetY=programmedWPs.getFirst().getYWP();
//				started = true;
//			}
//			// Target is the next WP, if we reach it, we go to the next one.
//			double distToTarget = Torpedo.distSq(this.posX, this.posY, this.targetX, this.targetY);
//			if ( distToTarget < 1000)
//			{
//				if (!programmedWPs.hasOneOrMoreElement()  || programmedWPs.isEdited())
//				{
//					// Stop
//					this.setCurrentSpeed(0);
//				}
//				else
//				{
//					if (!loop)
//					{	
//						programmedWPs.pull();
//						this.wpHasBeenUpdated();
//						if (!programmedWPs.isEmpty())
//						{
//							this.targetX=programmedWPs.getFirst().getXWP();
//							this.targetY=programmedWPs.getFirst().getYWP();
//							System.out.println("Next WP "+programmedWPs.getFirst().getNbWP());
//						}
//						else
//						{
//							System.out.println("Arrived");
//							started = false;
//						}
//					}
//					else
//					{
//						indexWP++;
//						if (indexWP >= programmedWPs.size())
//							indexWP=0;
//						this.targetX=programmedWPs.getWP(indexWP).getXWP();
//						this.targetY=programmedWPs.getWP(indexWP).getYWP();							
//					}
//				}
//			}
//			else
//			{
//				this.tmpSpeedX = (this.targetX - this.posX);
//				this.tmpSpeedY = (this.targetY - this.posY);
//				this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
//				this.wantedSpeedX = this.tmpSpeedX / tmpSpeedN;
//				this.wantedSpeedY = this.tmpSpeedY / tmpSpeedN;
//				this.wantedSpeedX*=this.standardSpeed/120;
//				this.wantedSpeedY*=this.standardSpeed/120;
//				//System.out.println("Wanted speed x and Y"+wantedSpeedX+" - "+wantedSpeedX);
//				// Try to accelerate
//				if ( (distToTarget < 2000) && (this.currentSpeed > this.standardSpeed))
//					this.currentSpeed -= 2;
//				else if (this.currentSpeed < this.wantedSpeed)
//					this.currentSpeed += 2;
//				else if (this.currentSpeed > this.wantedSpeed)
//						this.currentSpeed -= 2;
//
//				//checkAndNormaliseSpeed();
//
//				this.accX(wantedSpeedX);
//				this.accY(wantedSpeedY);
//
//				checkAndNormaliseSpeed();
//			}
//		}
//	}
	
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
			if ( distToTarget < 1000)
			{
				if (!programmedWPs.hasOneOrMoreElement()  || programmedWPs.isEdited())
				{
					// Stop
					this.setCurrentSpeed(0);
				}
				else
				{
					if (!loop)
					{	
						programmedWPs.pull();
						this.wpHasBeenUpdated();
						if (!programmedWPs.isEmpty())
						{
							this.targetX=programmedWPs.getFirst().getXWP();
							this.targetY=programmedWPs.getFirst().getYWP();
							System.out.println("Next WP "+programmedWPs.getFirst().getNbWP());
						}
						else
						{
							System.out.println("Arrived");
							started = false;
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
//				Vector2D obtainedSpeed = seek(new Vector2D(targetX, targetY));
				
//				//System.out.println("Wanted speed x and Y"+wantedSpeedX+" - "+wantedSpeedX);
				
				double directionToTake = returnAngle(new Vector2D(targetX, targetY));

//				System.out.println("The target is at angle "+directionToTake);
				
				directionToTake/=10;
				
				// Now turn to the right position
				if (directionToTake > 0.2)
				{
					turn(0.2);
				}
				else if (directionToTake < -0.2)
				{
					turn(-0.2);
				}
				else
				{
					turn(directionToTake);
				}
				
				// Try to accelerate
				if ( (distToTarget < 2000) && (this.currentSpeed > this.standardSpeed))
					this.currentSpeed -= 2;
				else if (this.currentSpeed < this.wantedSpeed)
					this.currentSpeed += 2;
				else if (this.currentSpeed > this.wantedSpeed)
						this.currentSpeed -= 2;

				//checkAndNormaliseSpeed();

//				System.out.println("Seeking by "+obtainedSpeed);
//				this.accX(obtainedSpeed.getX());
//				this.accY(obtainedSpeed.getY());

				checkAndNormaliseSpeed();
			}
		}
	}
	
//	public Vector2D seek(Vector2D targetPos)
//	{
//		Vector2D obtainedSpeed = new Vector2D();
//		
//		this.tmpSpeedX = (this.targetX - this.posX);
//		this.tmpSpeedY = (this.targetY - this.posY);
//		this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
//		this.wantedSpeedX = this.tmpSpeedX / tmpSpeedN;
//		this.wantedSpeedY = this.tmpSpeedY / tmpSpeedN;
//
//		this.wantedSpeedX = this.wantedSpeedX * maxSpeed;
//		this.wantedSpeedY = this.wantedSpeedY * maxSpeed;
//		
//		double velocityX = currentSpeed*Math.cos(orientation);
//		double velocityY = currentSpeed*Math.sin(orientation);
//		
//		//     PVector steer = PVector.sub(desired,velocity);
//		double limiter = rudderEfficiency*0.0001;
//		
//		obtainedSpeed.setX(limiter*(this.wantedSpeedX - velocityX));
//		obtainedSpeed.setY(limiter*(this.wantedSpeedY - velocityY));
//		
//		return obtainedSpeed;
//	}
//	
	
	/**
	 * To avoid hard turn. Will be progressive turn, with a wanter angle.
	 * @param howMuch
	 */
	private void turnRudder(double howMuch)
	{
		if (howMuch > 0 && rudder < (Math.PI/3))
		{
			rudder+=howMuch;
		}
		else if (howMuch < 0 && rudder > (-Math.PI/3))
		{
			rudder+=howMuch;
		}
		turn(rudder);
	}

	public double returnAngle(Vector2D targetPos)
	{
		double obtainedAngle = 0;
		
		Vector2D vectorRelToTarget = new Vector2D();
		
		vectorRelToTarget.setX(targetPos.getX() - this.posX);
		vectorRelToTarget.setY(targetPos.getY() - this.posY);
		
		Vector2D vectorFromOrientation = new Vector2D();
		
		if (currentSpeed >= 0)
		{
			vectorFromOrientation.setX(100*Math.cos(orientation));
			vectorFromOrientation.setY(100*Math.sin(orientation));
		}
		else
		{
			vectorFromOrientation.setX(100*Math.cos(orientation));
			vectorFromOrientation.setY(100*Math.sin(orientation));
		}
		
		obtainedAngle = UtilsPC.angleBetween2VectorsFromOrigin(vectorFromOrientation, vectorRelToTarget);
		
		return obtainedAngle;
	}
	//	public bool isLeft(Point a, Point b, Point c){
//	     return ((b.X - a.X)*(c.Y - a.Y) - (b.Y - a.Y)*(c.X - a.X)) > 0;
//	}
	public double isLeft(double targetPosY, double targetPosX)
	{	
		double nextPosX;
		double nextPosY;
		
		if (currentSpeed >= 0)
		{
			nextPosX = this.posX + 100*Math.cos(orientation);
			nextPosY = this.posY + 100*Math.sin(orientation);
		}
		else
		{
			nextPosX = this.posX - 100*Math.cos(orientation);
			nextPosY = this.posY - 100*Math.sin(orientation);
		}
		
	    return ((nextPosX - this.posX)*(targetPosY - this.posY) - (nextPosY - this.posY)*(targetPosX - this.posX));
	}
	
//	public double returnAngle(Vector2D targetPos)
//	{
//		double obtainedAngle = 0;
//		
//		this.tmpSpeedX = (targetPos.getX() - this.posX);
//		this.tmpSpeedY = (targetPos.getY() - this.posY);
//		this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
//		
//		if (Math.abs(tmpSpeedX) > 0.00001)
//		{
//			obtainedAngle = Math.acos(tmpSpeedX/tmpSpeedN);
//		}
//		else
//		{
//			if (Math.abs(tmpSpeedY) > 0.00001)
//			{
//				obtainedAngle = Math.PI/2;
//			}
//		}
//		if (tmpSpeedY < 0)
//		{
//			obtainedAngle=-obtainedAngle + 2*Math.PI;
//		}
//		
//		return obtainedAngle;
//	}
	
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

				// Inform the map that we did shoot.
				theMap.didShoot();
				
				switch (this.typeFaction)
				{
					case FUnit.BOAT_OUR:
						GameKeeper.getInstance().addTorpedoes(1);
						break;
					case FUnit.BOAT_ALLIED:
						GameKeeper.getInstance().addTorpedoesAllies(1);
						break;
					case FUnit.BOAT_ENEMY:
						GameKeeper.getInstance().addTorpedoesEne(1);
						break;
				}
				
				nbInSalveT++;
				if (fireInSalve)
				{
					if (nbInSalveT >= nbInSalveMaxT)
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

				// Inform the map that we did shoot.
				theMap.didShoot();
				
				switch (this.typeFaction)
				{
					case FUnit.BOAT_OUR:
						GameKeeper.getInstance().addMissiles(1);
						break;
					case FUnit.BOAT_ALLIED:
						GameKeeper.getInstance().addMissilesAllies(1);
						break;
					case FUnit.BOAT_ENEMY:
						GameKeeper.getInstance().addMissilesEne(1);
						break;
				}
				
				nbInSalveM++;
				if (fireInSalve)
				{
					if (nbInSalveM >= nbInSalveMaxM)
						fireAsked=false;
				}
				else
				{
					fireAsked=false;
				}
			}
			if ((nbInSalveM >= nbInSalveMaxM) && !loadingM)
			{
				loadingM=true;
				timeSinceLastLaunchM=timeBetweenLaunchesM;
			}
		}

		if (this.autonomous)
		{
			if (foundUnits != null)
			{
				for (int iUnits=0; iUnits< foundUnits.size(); iUnits++)
				{
					FUnit tmpUnit = foundUnits.get(iUnits);
					//System.out.println("Contact to sink - "+tmpUnit.getLevelDetection());
					// Check deep of the unit (missile or torpedo ? )

					if ((tmpUnit.getLevelDetection() > 2) || this.isIgnoreFriends())
					{
						if (((tmpUnit.getDepth() < -1) && (tmpUnit.getType() != FUnit.TORPEDO)) || (tmpUnit.getType() == FUnit.UNKNOWN_SEA) || 
								((this.nbMissiles == 0) && (tmpUnit.getType() != FUnit.UNKNOWN_AIR) && (tmpUnit.getType() != FUnit.MISSILE) && (tmpUnit.getType() != FUnit.TORPEDO) 
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
//									if (typeEnemy == 1)
//									{
//									oneTorpedo.setKnowAllies(false);
//									oneTorpedo.setKnowEnemies(true);
//									}

									oneTorpedo.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
									oneTorpedo.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.6, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.6, 0);
									oneTorpedo.setOrientation(this.getOrientation()+nbInSalveT*0.1);
									oneTorpedo.setCurrentSpeed(500);
									oneTorpedo.setPosX(this.getPosX());
									oneTorpedo.setPosY(this.getPosY());
									oneTorpedo.setIdOwner(this.idBoat);
									oneTorpedo.setTypeFaction(this.typeFaction);
									this.theMap.addTorpedo(oneTorpedo);

									nbTorpedoes--;

									// Inform the map that we did shoot.
									theMap.didShoot();
									
									switch (this.typeFaction)
									{
										case FUnit.BOAT_OUR:
											GameKeeper.getInstance().addTorpedoes(1);
											break;
										case FUnit.BOAT_ALLIED:
											GameKeeper.getInstance().addTorpedoesAllies(1);
											break;
										case FUnit.BOAT_ENEMY:
											GameKeeper.getInstance().addTorpedoesEne(1);
											break;
									}
									
									nbInSalveT++;
								}
							}
							if ((nbInSalveT >= nbInSalveMaxT) && !loadingT)
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
							if ((this.nbMissiles > 0) && (this.fireAtWill) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() != FUnit.MISSILE) && (tmpUnit.getType() != FUnit.TORPEDO) && ((tmpUnit.getType() & 0x30) != 0x30) )
							{
//								if (typeEnemy == 1)
//								{
//								System.out.println("Bad guy, seen unit "+ tmpUnit.getType()+" - "+ (tmpUnit.getType() & 1)+ " : "+((tmpUnit.getType() & 1) == typeEnemy));
//								}
								if (this.isIgnoreFriends() || 
										(!this.ignoreFriends && ((tmpUnit.getType() & 1) == typeEnemy) ))
								{
									//System.out.println(this.getIdBoat()+" - Fire on "+(tmpUnit.getType()));
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
									oneMissile.setPosX(this.getPosX()+nbInSalveM);
									oneMissile.setPosY(this.getPosY());
									oneMissile.setIdOwner(this.idBoat);
									oneMissile.setTypeFaction(this.typeFaction);
									this.theMap.addMissile(oneMissile);

									nbMissiles--;

									// Inform the map that we did shoot.
									theMap.didShoot();
									
									switch (this.typeFaction)
									{
										case FUnit.BOAT_OUR:
											GameKeeper.getInstance().addMissiles(1);
											break;
										case FUnit.BOAT_ALLIED:
											GameKeeper.getInstance().addMissilesAllies(1);
											break;
										case FUnit.BOAT_ENEMY:
											GameKeeper.getInstance().addMissilesEne(1);
											break;
									}
									
									nbInSalveM++;
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
								
								// Inform the map that we did shoot.
								theMap.didShoot();
								
								switch (this.typeFaction)
								{
									case FUnit.BOAT_OUR:
										GameKeeper.getInstance().addMissiles(1);
										break;
									case FUnit.BOAT_ALLIED:
										GameKeeper.getInstance().addMissilesAllies(1);
										break;
									case FUnit.BOAT_ENEMY:
										GameKeeper.getInstance().addMissilesEne(1);
										break;
								}
								
								nbInSalveM++;
							}
							//if ((this.nbMissiles > 0) && (nbInSalveM < nbInSalveMaxM) && ((tmpUnit.getType() & 0x30) == 0x30) && (tmpUnit.getTypeFaction() == this.typeEnemy))
							if ((this.nbMissiles > 0)&& ((tmpUnit.getType() & 0x30) == 0x30) && (tmpUnit.getTypeFaction() == this.typeEnemy))
							{
								System.out.println("Airplane to intercept");
								Missile oneMissile= new Missile(this.theMap);
								oneMissile.setKnowAllies(true);
								oneMissile.setKnowEnemies(true);
								oneMissile.setSeek(true);
								oneMissile.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
								oneMissile.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.4, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.4, 0);
								oneMissile.setCurrentSpeed(1000);
								oneMissile.setMaxSpeed(2000);
								oneMissile.setOrientation(this.getOrientation());
								oneMissile.setPosX(this.getPosX());
								oneMissile.setPosY(this.getPosY());
								oneMissile.setIdOwner(this.idBoat);
								oneMissile.setTypeFaction(this.typeFaction);
								this.theMap.addMissile(oneMissile);

								nbMissiles--;
								
								// Inform the map that we did shoot.
								theMap.didShoot();
								
								switch (this.typeFaction)
								{
									case FUnit.BOAT_OUR:
										GameKeeper.getInstance().addMissiles(1);
										break;
									case FUnit.BOAT_ALLIED:
										GameKeeper.getInstance().addMissilesAllies(1);
										break;
									case FUnit.BOAT_ENEMY:
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
			
			// Short range cannon, against airplanes and missiles mostly
			attackShortRange(time);
		}
	}

	public void attackShortRange(double time)
	{
		if (nbAmmo > 0)
		{
			if (loadingC)
			{
				fireLine.invalidate();
				
				timeSinceLastLaunchC-=time;

				if (timeSinceLastLaunchC < 0)
				{
					loadingC=false;
				}
			}
			if (!loadingC)
			{
				ArrayList<Airplane> ourAirplanes;
				ArrayList<Airplane> alliesAirplanes;
				ArrayList<Airplane> enemiesAirplanes;
				ArrayList<Airplane> neutralAirplanes;
				ArrayList<Missile> missiles;


				missiles=theMap.getMissiles();

				ourAirplanes=theMap.getOurAirplanes();
				alliesAirplanes=theMap.getAlliesAirplanes();
				enemiesAirplanes=theMap.getEnemiesAirplanes();
				neutralAirplanes=theMap.getNeutralAirplanes();

				Airplane tmpAirplane;
				Airplane foundAirplane=null;

				Missile tmpMissile;
				Missile foundMissile=null;
				double distanceTmp;

				double distanceFound = 100000; // Reset the distance of detection.

				boolean found=false;

				double xToFireAt=0;
				double yToFireAt=0;

				{
					if (this.isIgnoreFriends() || 
							(!this.ignoreFriends && (this.getTypeFaction() == FUnit.BOAT_ENEMY) ))
					{
						if (ourAirplanes != null)
						{
							for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
							{
								tmpAirplane = ourAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
									//System.out.println("Found allies boat - dist "+distanceTmp);
									if (distanceTmp < distanceFound)
									{
										distanceFound= distanceTmp;
										xToFireAt = tmpAirplane.getPosX();
										yToFireAt = tmpAirplane.getPosY();
										foundAirplane=tmpAirplane;

										found = true;
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
									distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
									//System.out.println("Found allies boat - dist "+distanceTmp);
									if (distanceTmp < distanceFound)
									{
										distanceFound= distanceTmp;
										xToFireAt = tmpAirplane.getPosX();
										yToFireAt = tmpAirplane.getPosY();
										foundAirplane=tmpAirplane;

										found = true;
									}
								}
							}
						}
					}
					if (this.isIgnoreFriends() || 
							(!this.ignoreFriends && (this.getTypeFaction() != FUnit.BOAT_ENEMY)))
					{
						if (enemiesAirplanes != null)
						{
							for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
							{
								tmpAirplane = enemiesAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.posX, this.posY, tmpAirplane.getPosX(), tmpAirplane.getPosY());
									//System.out.println("Found allies boat - dist "+distanceTmp);
									if (distanceTmp < distanceFound)
									{
										distanceFound= distanceTmp;
										xToFireAt = tmpAirplane.getPosX();
										yToFireAt = tmpAirplane.getPosY();
										foundAirplane=tmpAirplane;

										found = true;
									}
								}
							}
						}
					}
					if (missiles != null)
					{
						for (int iMissile = 0; iMissile < missiles.size() ; iMissile++)
						{
							tmpMissile = missiles.get(iMissile);

							if (tmpMissile.getTypeFaction() == this.typeEnemy)
							{
								if (tmpMissile.getIdOwner() != this.getIdBoat())
								{
									distanceTmp = Missile.distSq(this.posX, this.posY, tmpMissile.getPosX(), tmpMissile.getPosY());
									//System.out.println("Found allies boat - dist "+distanceTmp);
									if (distanceTmp < distanceFound)
									{
										distanceFound= distanceTmp;
										xToFireAt = tmpMissile.getPosX();
										yToFireAt = tmpMissile.getPosY();
										foundMissile=tmpMissile;

										found = true;
									}
								}
							}

						}
					}
				}
				if (found && (distanceFound < rangeInterception))
				{
					nbAmmo--;
					switch (this.typeFaction)
					{
						case FUnit.BOAT_OUR:
							GameKeeper.getInstance().addAmmo(1);
							break;
						case FUnit.BOAT_ALLIED:
							GameKeeper.getInstance().addAmmoAllies(1);
							break;
						case FUnit.BOAT_ENEMY:
							GameKeeper.getInstance().addAmmoEne(1);
							break;
					}
					fireLine.setPosEnd(xToFireAt, yToFireAt);
					if (boatBody.isValide())
						fireLine.validate();
					if (foundAirplane!=null)
						foundAirplane.damageBoat(20);
					else
					{
						if ((foundMissile !=null) && (Math.random() > 0.7))
							foundMissile.explode();
					}
					loadingC=true;
					timeSinceLastLaunchC=timeBetweenLaunchesC;
				}
				else
				{
					fireLine.invalidate();
				}
			}
		}
		else
		{
			fireLine.invalidate();
		}
	}
	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.ProgrammableUnit#doUpdate()
	 */
	@Override
	public void doUpdate(double time)
	{
		if (!dead)
		{
			if (flightsOn)
			{
				//System.out.println("Energy left "+ energyLeft);
				// Launch a airplane ???
				if (loadingA)
				{
					timeBeforeAirLaunches-=time;

					if (timeBeforeAirLaunches < 0)
					{
						loadingA=false;
					}
				}
				if (this.type == Boat.CARRIER)
				{
					if ((nbAwacs >= 0) && !loadingA)
					{
						Airplane caspianAir = new Airplane(theMap);
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+myRandom.nextInt(200)-100,this.getPosY()+myRandom.nextInt(200)-100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+myRandom.nextInt(200)-100,this.getPosY()+myRandom.nextInt(200)-100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+myRandom.nextInt(200)-100,this.getPosY()+myRandom.nextInt(200)-100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+myRandom.nextInt(200)-100,this.getPosY()+myRandom.nextInt(200)-100,0));
						caspianAir.setLoop(true);
						caspianAir.setFireAtWill(true);
						caspianAir.setIgnoreFriends(false);

						caspianAir.setComplement(20);
						caspianAir.setComplementNorm(20);
						caspianAir.setCost(300000000);
						caspianAir.setOrientation(this.orientation);
						caspianAir.setCurrentSpeed(600);
						caspianAir.setEnergy(1000);
						caspianAir.setMaxSpeed(800);
						caspianAir.setName("Hawk 16");
						caspianAir.setResistance(50);
						caspianAir.setVisibilityLevel(0);
						caspianAir.setNoiseSignature(2);
						caspianAir.setVisibilitySignature(0);
						caspianAir.setPosX(this.getPosX());
						caspianAir.setPosY(this.getPosY());
						caspianAir.setOwner(this);

						switch (this.typeFaction)
						{
							case FUnit.BOAT_ALLIED:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
								break;
							case FUnit.BOAT_OUR:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
								break;
							case FUnit.BOAT_ENEMY:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
								break;
						}

						caspianAir.setType(Airplane.RADARPLANE);
						caspianAir.createGfx(this.getPosX(), this.getPosY(), 0, 0, 20);

						DeportedRadar ourRadar2;
						ourRadar2= new DeportedRadar(theMap,this);
						ourRadar2.createGfx(0, 0, 0, 0, 1, 1);
						ourRadar2.setPosAttach(0, 0, 0);
						ourRadar2.setPower(20);
						ourRadar2.setSpeedRot(30);
						ourRadar2.activate();
						ourRadar2.setDebugView(false);

						this.getSensors().add(ourRadar2);
						caspianAir.getDeportedSensors().add(ourRadar2);
						caspianAir.addAttachedObject(ourRadar2);
						caspianAir.setView(false);

						if (this.typeFaction == FUnit.BOAT_ENEMY)
							caspianAir.hideMe();

						theMap.addAirplane(caspianAir);

						nbAwacs--;
						loadingA=true;
						this.timeBeforeAirLaunches=timeBetweenAirLaunches;
					}
					if ((nbFighters >= 0) && (this.currentNbFightersInFlight < this.nbFightersOnFlightMax) && !loadingA)
					{
						Airplane caspianAir = new Airplane(theMap);
						caspianAir.getProgrammedWPs().addWP(new Waypoint(myRandom.nextInt(500)+100,myRandom.nextInt(600)+100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(myRandom.nextInt(500)+100,myRandom.nextInt(600)+100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(myRandom.nextInt(500)+100,myRandom.nextInt(600)+100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(myRandom.nextInt(500)+100,myRandom.nextInt(600)+100,0));
						caspianAir.setLoop(true);
						caspianAir.setFireAtWill(true);
						caspianAir.setIgnoreFriends(false);

						caspianAir.setComplement(2);
						caspianAir.setComplementNorm(2);
						caspianAir.setCost(100000000);
						caspianAir.setOrientation(this.orientation);
						caspianAir.setCurrentSpeed(1500);
						caspianAir.setEnergy(1000);
						caspianAir.setMaxSpeed(1000);
						caspianAir.setName("F23 Fighter");
						caspianAir.setResistance(10);
						caspianAir.setVisibilityLevel(0);
						caspianAir.setNoiseSignature(2);
						caspianAir.setVisibilitySignature(0);
						caspianAir.setPosX(this.getPosX());
						caspianAir.setPosY(this.getPosY());
						caspianAir.setOwner(this);

						caspianAir.setType(Airplane.FIGHTER);
						switch (this.typeFaction)
						{
							case FUnit.BOAT_ALLIED:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
								break;
							case FUnit.BOAT_OUR:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
								break;
							case FUnit.BOAT_ENEMY:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
								break;
						}

						caspianAir.createGfx(this.getPosX(), this.getPosY(), 0, 0, 20);

						SimpleAround ourRadar2;
						ourRadar2= new SimpleAround(theMap,caspianAir);
						ourRadar2.createGfx(0, 0, 0, 0, 1, 1);
						ourRadar2.setPosAttach(0, 0, 0);
						ourRadar2.setPower(10);
						ourRadar2.activate();
						ourRadar2.setDebugView(false);

						caspianAir.getSensors().add(ourRadar2);
						caspianAir.addAttachedObject(ourRadar2);
						caspianAir.setView(false);
						caspianAir.getSensors().add(theMap.getOurKD());

						if (this.typeFaction == FUnit.BOAT_ENEMY)
							caspianAir.hideMe();

						theMap.addAirplane(caspianAir);

						nbFighters--;
						loadingA=true;
						this.timeBeforeAirLaunches=timeBetweenAirLaunches;
					}
				}
			}
			updateAttachedObjects();
			// TODO Auto-generated method stub
			
			this.orientation+=angleToTurn*time*rudderEfficiency;
			
			double deceleration = Math.abs(0.1*(500 - 200*(maxSpeed-currentSpeed)/maxSpeed)*angleToTurn);
			
			if (currentSpeed < 0)
			{
				currentSpeed+= deceleration;
			}
			else
			{
				currentSpeed-= deceleration;
			}
			
			if (this.boundaryLimited)
			{
				if (this.posX < 0)
				{
					if (this.currentSpeed > 0)
						this.accX(2);
					else
						this.accX(-2);
					checkAndNormaliseSpeed();
				}
				if (this.posY < 0)
				{
					if (this.currentSpeed > 0)
						this.accY(2);
					else
						this.accY(-2);
					checkAndNormaliseSpeed();
				}
				if (this.posX > TacticalMapPC.START_MENU)
				{
					if (this.currentSpeed > 0)
						this.accX(-2);
					else
						this.accX(2);
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
			}
			double deltaX = Math.cos(this.orientation);
			double deltaY = Math.sin(this.orientation);
			
			double futureX = this.posX + 4*time*this.currentSpeed*deltaX;
			double futureY = this.posY + 4*time*this.currentSpeed*deltaY;
			
			depthWater = ourLevelKeeper.getAlpha((int )futureX, (int )futureY)-255;
			
			if (depthWater > -10)
			{
				// We can't move, let try to turn
				//this.turn(0.2);
				// 2019 Nov: Still not working, get boats spinning strangely.
			}
			else 
			{
				this.posX = this.posX + time*this.currentSpeed*deltaX;
				this.posY = this.posY + time*this.currentSpeed*deltaY;
			}
			
			
			//System.out.println("Depth "+depthWater);
			
//			if (( this.currentSpeed > 0) && (depthWater > -10))
//			{
//				// Not good :)
//				//this.turn(0.2);
//				this.wantedSpeed =0;
//			}
//			else if (( this.currentSpeed < -3) && (depthWater > -10))
//			{
//				// Not good :)
//				//this.turn(0.2);
//				this.wantedSpeed = 0;
//			}
			
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
			else
			{
				if (this.currentSpeed < this.wantedSpeed)
					this.currentSpeed += 2;
				else if (this.currentSpeed > this.wantedSpeed)
						this.currentSpeed -= 2;
			}

			this.updateMe(posX, posY, 0, this.orientation, this.currentSpeed);

			for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
			{
				sensors.get(iSensors).doUpdate(time);

				foundUnits = sensors.get(iSensors).getFoundUnits();
				
				attackIfNeeded(time);
			}
			// Global sensors
			// KnownDatas only now (because the satellites infos might be too old...)
			foundUnits = this.theMap.getOurKD().getFoundUnits();
			
			attackIfNeeded(time);	
			
			if ((this.nbTorpedoes > 0) || (this.nbMissiles > 0))
			{
				theMap.notOutOfAmmo(this.idBoat);
			}
		}
		else if (explodeTime < explodeTimeMax)
		{
				explodeTime++;
				explosion.setSize(explosion.getSize()+0.2);
				//System.out.println("Explode !!!!");
				SndContainer.playExplosion2Sound();
		}
		else if (iDead > -1)
		{
			explosion.invalidate();
			this.setDepth(iDead--);
			smokeBlack.invalidate();
			if (this.type != Boat.CARRIER && this.type != Boat.PLEASANCE)
			{
				smoke.invalidate();
			}
			removeMeWOBody();
		}
		else if (iDead > depthWater)
		{
			this.setDepth(iDead--);
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
//		if (isLeft(this.targetX, this.targetY) > 0)
//		{
//			objectToUpdate.setAttachableColor(0xFF888888);
//		}
//		else
//		{
//			objectToUpdate.setAttachableColor(0xFF880000);
//		}
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
			this.hullState-=amount*Math.random()/this.resistance;
			this.navState-=amount*Math.random()/this.resistance;
			this.dataLinkState-=amount*Math.random()/this.resistance;

			// TOO MUCH.
			this.complement-=(0.5+((double )this.complementNorm)/500)*amount*Math.random()/(this.resistance * this.hullState);
			this.energy-=amount*Math.random()/this.resistance;

			//System.out.println("Boat Dommage "+amount+" - "+hullState+" - "+energy);
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
			if ((this.hullState <= 0) && damaged)
			{
				hullState=0;
				dead = true;
				this.currentSpeed=0;
				this.maxSpeed=0;
				wantedSpeed=maxSpeed;
				explosion.validate();

				if (this.type != Boat.CARRIER && this.type != Boat.PLEASANCE)
				{
					smoke.invalidate();
				}
				fireLine.invalidate();
				for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
				{
					sensors.get(iSensors).removeMe();
				}
				sensors.clear();

				iDead=0;
				
				theMap.boatDead(this.typeFaction);
				
				if (depthWater < -200)
					depthWater=-200;
				if (depthWater > -10)
					depthWater=-15;
			}
			if ((this.hullState < 50) && (!damaged))
			{
				damaged = true;
				this.maxSpeed/=2;
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
		if (view)
		{
			if (boatSquare != null)
			{
				boatSquare.validate();
			}
		}
		else
		{
			if (boatSquare != null)
			{
				boatSquare.invalidate();
			}
		}
	}

	public int getNbTorpedoes() {
		return nbTorpedoes;
	}

	/**
	 * Set the initial nb of torpedoes (also the max then)
	 * @param nbTorpedoes
	 */
	public void setNbTorpedoes(int nbTorpedoes) {
		this.nbTorpedoes = nbTorpedoes;
		this.nbTorpedoesMax = nbTorpedoes;
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

	/**
	 * Set the initial nb of missiles (also the max then)
	 * @param nbMissiles
	 */
	public void setNbMissiles(int nbMissiles) {
		this.nbMissiles = nbMissiles;
		this.nbMissilesMax = nbMissiles;
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
		stringToConstruct.append(this.silenceSpeed);
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

	public int getTonnage() {
		return tonnage;
	}

	public void setTonnage(int tonnage) {
		this.tonnage = tonnage;
	}
	
	/**
	 * Can be repaired by a base or a special unit (or anything else)
	 * @param amount
	 */
	public void getRepaired(int amount)
	{
		super.getRepaired(0);
		
		hullState+=amount;
		if (damaged && hullState > 50)
		{
			damaged = false;
			this.maxSpeed*=2;
		}
		if (hullState> 100)
		{
			hullState=100;
		}

		dataLinkState+=amount;
		if (dataLinkState > 100)
		{
			dataLinkState=100;
		}


		navState+=amount;
		if (navState > 100)
		{
			navState=100;
		}

		complement+=amount;
		if (complement > 1)
		{
			unmanned=false;
		}
		if (complement > complementNorm)
		{
			complement=complementNorm;
		}
		if (nbAmmo < 40000)
			nbAmmo+=amount;
		
		energy+=amount;
		if (energy > energyMax)
		{
			energy=energyMax;
		}
		
		if (type != Boat.CARRIER)
		{
			if (nbTorpedoes < nbTorpedoesMax)
				nbTorpedoes+=amount;
		}
		else
		{
			if (this.nbFighters < nbFightersMax)
				nbFighters+=amount;
		}
		
		if (nbMissiles < nbMissilesMax)
			nbMissiles+=amount;

	}
	
	/**
	 * Create the graphics entity for the strategic map
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfxSM(double x, double y, double z, double direction, double speed)
	{	
		if (!this.isDead())
		{
			switch (this.typeFaction)
			{
				case FUnit.BOAT_ENEMY:
					switch (this.type)
					{
						case Boat.CARRIER:
							boatBody = new SpritePC(gfxSprites.getImageCarrierSmallEne());
							break;
						case Boat.CRUISER:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmallEne());
							break;
						case Boat.DESTROYER:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmallEne());
							boatBody.setSize(0.8);
							break;
						case Boat.FRIGATE:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmallEne());
							boatBody.setSize(0.6);
							break;
						default:	
							boatBody = new SpritePC(gfxSprites.getImageBoatSmallEne());
						boatBody.setSize(0.4);
					}

					break;
				case FUnit.BOAT_OUR:
					switch (this.type)
					{
						case Boat.CARRIER:
							boatBody = new SpritePC(gfxSprites.getImageCarrierSmallOur());
							break;
						case Boat.CRUISER:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmall());
							break;
						case Boat.DESTROYER:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmall());
							boatBody.setSize(0.8);
							break;
						case Boat.FRIGATE:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmall());
							boatBody.setSize(0.6);
							break;
						case Boat.CARGO:
							boatBody = new SpritePC(gfxSprites.getImageBoatCargo());
							boatBody.setSize(0.2);
							break;
						case Boat.TANKER:
							boatBody = new SpritePC(gfxSprites.getImageBoatTanker());
							boatBody.setSize(0.2);
							break;
						case Boat.CRUISESHIP:
							boatBody = new SpritePC(gfxSprites.getImageBoatCruise());
							boatBody.setSize(0.2);
							break;
						case Boat.FISHING_BOAT:
							boatBody = new SpritePC(gfxSprites.getImageBoatFishingShip());
							boatBody.setSize(0.4);
							break;
						case Boat.PLEASANCE:
							boatBody = new SpritePC(gfxSprites.getImageBoatPleasance());
							boatBody.setSize(0.4);
							break;
						default:	
							boatBody = new SpritePC(gfxSprites.getImageBoatSmall());
						boatBody.setSize(0.4);
					}
					break;
				default:
					switch (this.type)
					{
						case Boat.CARRIER:
							boatBody = new SpritePC(gfxSprites.getImageCarrierSmallAllies());
							break;
						case Boat.CRUISER:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmallAllies());
							break;
						default:
							boatBody = new SpritePC(gfxSprites.getImageBoatSmallAllies());
						boatBody.setSize(0.8);
					}
			}
			
			switch (this.type)
			{
			case Boat.CARRIER:
				boatBody.setSize(1.2);
				break;
			case Boat.CRUISER:
				boatBody.setSize(0.8);
				break;
			case Boat.DESTROYER:
				boatBody.setSize(0.8);
				break;
			case Boat.FRIGATE:
				boatBody.setSize(0.6);
				break;
			case Boat.CARGO:
				boatBody.setSize(0.3);
				break;
			case Boat.TANKER:
				boatBody.setSize(0.3);
				break;
			case Boat.CRUISESHIP:
				boatBody.setSize(0.3);
				break;
			default:	
				boatBody.setSize(0.4);
			}
			if (this.getTonnage() == 700000)
			{
				boatBody.setSize(2);
			}

			boatBody.setRotation(direction);
			xShift=40;
			yShift=10;
			boatBody.setPos(x, y, z);
			idBody = RenderingManager.getInstance().addDrawableEntity(boatBody,20); // Surface

			createAttachedRank();
			
			boatSquare = new SpritePC(gfxSprites.getSelImage());
			boatSquare.setSize(0.4);
			boatSquare.invalidate();
			boatBody.addAttachedObject(boatSquare);
			idBoatSquare = RenderingManager.getInstance().addDrawableEntity(boatSquare,39);
			
			if (this.getType() == Boat.CARRIER)
			{
				ourCircle = new CirclePC();
				ourCircle.setPos(x, y, 0);
				ourCircle.setPosAttach(0, 0, 0);
				//ourCircle.setOurColor(0xff000000);
				ourCircle.setSizeCircle(this.getEnergy()/100, this.getEnergy()/100);
				ourCircle.validate();
				
				idCircle =  RenderingManager.getInstance().addDrawableEntity(ourCircle, 38);
				boatBody.addAttachedObject(ourCircle);
			}
		}
	}
	
	public void createAttachedRank()
	{
		GfxRanks.callMeFirst();
		
		if (level == 1)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR1Ball());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 2)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR2Ball());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 3)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR3Ball());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 4)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRBBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 5)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR1SilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 6)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR2SilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 7)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR3SilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 8)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRBSilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 9)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR1GoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 10)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR2GoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 11)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR3GoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 12)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRBGoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 13)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRCrystalBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level >= 14)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRShiningBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			boatBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
	}
	
	public void updateAttachedObjectSM(IAttachable objectToUpdate)
	{
		objectToUpdate.setOrientationAttach(this.orientation);

		transformToApply.setToIdentity();
		transformToApply.translate(this.xMap, this.yMap);
		transformToApply.rotate(this.getOrientation(), 0, 0);
		//transformToApply.scale(this.getSize(), this.getSize());
		onePoint2D.setLocation(objectToUpdate.getXAttach(), objectToUpdate.getYAttach());
		transformToApply.transform(onePoint2D, onePoint2Dtmp);
		objectToUpdate.setAbsPos(onePoint2Dtmp.x, onePoint2Dtmp.y,0);
	}
	
	public void updateAttachedObjectsSM()
	{
		for (int iObjects=0;iObjects < attachedObjects.size() ; iObjects++)
		{
			updateAttachedObjectSM(attachedObjects.get(iObjects));
		}
	}
	
	/**
	 * Update the torpedo
	 * NB: We do not simulate the torpedo here (yet)
	 */
	public void updateMeSM(double x, double y, double z, double direction, double speed)
	{
		boatBody.setRotation(direction);
		boatBody.setPos(x, y, z);
	}
	
	public void hideMeSM()
	{
		if (boatBody != null)
			boatBody.invalidate();
	}
	
	public void showMeSM()
	{
		if (boatBody != null)
			boatBody.validate();
	}
	
	public void pushSelected()
	{
		if (boatSquare != null)
		{
			boatSquare.validate();
		}
	}
	
	public void unselect()
	{
		if (boatSquare != null)
		{
			boatSquare.invalidate();
		}
	}
	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMeSM()
	{
		RenderingManager.getInstance().removeEntity(idBody, 20);
		RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
		RenderingManager.getInstance().removeEntity(idCircle, 38);
		RenderingManager.getInstance().removeEntity(idRank,38);
		ourCircle = null;
		
		boatBody = null;
		boatSquare = null;
		rankSprite = null;
		
		washAllAttachedObjects();
	}
	
	

	public void doUpdateSM(double time)
	{	
		boolean avoidOtherBoat=false;
		
		if (!dead)
		{
			if (this.type == Boat.CARRIER)
			{
				ourCircle.setSizeCircle(this.getEnergy()/100, this.getEnergy()/100);
			}
			if (levelChanged)
			{
				RenderingManager.getInstance().removeEntity(idRank,38);
				createAttachedRank(); // Change the rank icon
				
				levelChanged=false;
			}
			
			updateAttachedObjects();

			//if (followTargetMap)
			{
				this.xTmpMap = this.xMap + 8*time*this.xMapSpeed;
				this.yTmpMap = this.yMap + 8*time*this.yMapSpeed;
				
				if (((MapKeeper.getInstance().getValue((int )xTmpMap, (int )yTmpMap, 0, 0, StrategicMap.START_MENU, 800) < 1)
						|| (Math.abs(xMapSpeed)+Math.abs(yMapSpeed) < 0.1))
						&& (MapKeeper.getInstance().stickBoat(this,xTmpMap,yTmpMap)))
				{
//					Vector2D repulseVec=MapKeeper.getInstance().calculateRepulsionBoat(this, xTmpMap, yTmpMap);
//					
//					if ((Math.abs(repulseVec.getX()) > 0.1 ) ||(Math.abs(repulseVec.getY()) > 0.1 ) )
//					{
//						xMapSpeed+=repulseVec.getX();
//						yMapSpeed+=repulseVec.getY();
//						checkAndNormaliseSpeedSM();
//						avoidOtherBoat=true;
//					}	
					
					this.xMap = this.xMap + time*this.xMapSpeed;
					this.yMap = this.yMap + time*this.yMapSpeed;

				}
				if (xMap >= StrategicMap.START_MENU)
					xMap=StrategicMap.START_MENU-1;
				if (yMap >= 800)
					yMap=799;
				if (!unmanned && followTargetMap)
				{
					seekTargetSM();
				}
			}

			this.updateMeSM(xMap, yMap, 0, this.orientation, this.currentSpeed);
			
			if (!followTargetMap)
			{
				this.xMapSpeed/=2;
				this.yMapSpeed/=2;
			}
		}
		if (this.xMap < 0.2)
		{
			System.out.println("Is here "+this.xMap+" - "+this.yMap);
		}
		if (this.xMap > 700)
		{
			System.out.println("Is here "+this.xMap+" - "+this.yMap);
		}
	}
	
	public void turnBoat()
	{
		double actualSpeed = Math.sqrt(xMapSpeed*xMapSpeed+ yMapSpeed*yMapSpeed);
		
		if (speedX != 0)
		{
			this.orientation = Math.acos(xMapSpeed/actualSpeed);
		}
		else
		{
			this.orientation = Math.PI/2;
		}
		if (yMapSpeed < 0)
		{
			this.orientation=-this.orientation + 2*Math.PI;
		}
	}
	
	public void checkAndNormaliseSpeedSM()
	{
		double actualSpeed = Math.sqrt(xMapSpeed*xMapSpeed+ yMapSpeed*yMapSpeed);
		
		if (Math.abs(xMapSpeed) > 0.00001)
		{
			this.orientation = Math.acos(xMapSpeed/actualSpeed);
		}
		else
		{
			if (Math.abs(yMapSpeed) > 0.00001)
			{
				this.orientation = Math.PI/2;
			}
		}
		if (yMapSpeed < 0)
		{
			this.orientation=-this.orientation + 2*Math.PI;
		}
		
		
		if (actualSpeed > this.maxSpeed)
		{
			this.xMapSpeed=0.1*maxSpeed*(this.xMapSpeed/actualSpeed);
			this.yMapSpeed=0.1*maxSpeed*(this.yMapSpeed/actualSpeed);
			
			//this.currentSpeed = this.maxSpeed;	
		}	
		//System.out.println("Current speed "+currentSpeed);
	}
	
	public void seekTargetSM()
	{
		// Target is the next WP, if we reach it, we go to the next one.
		double distToTarget = Torpedo.distSq(this.xMap, this.yMap, this.xMapTarget, this.yMapTarget);
		if ( distToTarget < 4)
		{
			followTargetMap=false;
		}
		else
		{
			this.tmpSpeedX = (this.xMapTarget - this.xMap);
			this.tmpSpeedY = (this.yMapTarget - this.yMap);
			this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
			this.wantedSpeedX = this.tmpSpeedX / tmpSpeedN;
			this.wantedSpeedY = this.tmpSpeedY / tmpSpeedN;
			this.wantedSpeedX*=this.standardSpeed/8;
			this.wantedSpeedY*=this.standardSpeed/8;

			//checkAndNormaliseSpeed();

			xMapSpeed+=wantedSpeedX;
			yMapSpeed+=wantedSpeedY;
			

			checkAndNormaliseSpeedSM();
		}
	}
	
	public double getTimeBeforeAirLaunches()
	{
		return timeBeforeAirLaunches;
	}

	public void setTimeBeforeAirLaunches(double timeBeforeAirLaunches)
	{
		this.timeBeforeAirLaunches = timeBeforeAirLaunches;
	}

	public double getTimeBetweenAirLaunches()
	{
		return timeBetweenAirLaunches;
	}

	public void setTimeBetweenAirLaunches(double timeBetweenAirLaunches)
	{
		this.timeBetweenAirLaunches = timeBetweenAirLaunches;
	}

	public double getTimeBetweenLaunchesM()
	{
		return timeBetweenLaunchesM;
	}

	public void setTimeBetweenLaunchesM(double timeBetweenLaunchesM)
	{
		this.timeBetweenLaunchesM = timeBetweenLaunchesM;
	}

	public int getNbInSalveMaxM()
	{
		return nbInSalveMaxM;
	}

	public void setNbInSalveMaxM(int nbInSalveMaxM)
	{
		this.nbInSalveMaxM = nbInSalveMaxM;
	}

	public double getTimeBetweenLaunchesT()
	{
		return timeBetweenLaunchesT;
	}

	public void setTimeBetweenLaunchesT(double timeBetweenLaunchesT)
	{
		this.timeBetweenLaunchesT = timeBetweenLaunchesT;
	}

	public int getNbInSalveMaxT()
	{
		return nbInSalveMaxT;
	}

	public void setNbInSalveMaxT(int nbInSalveMaxT)
	{
		this.nbInSalveMaxT = nbInSalveMaxT;
	}

	public double getTimeBetweenLaunchesC()
	{
		return timeBetweenLaunchesC;
	}

	public void setTimeBetweenLaunchesC(double timeBetweenLaunchesC)
	{
		this.timeBetweenLaunchesC = timeBetweenLaunchesC;
	}

	public int getNbAmmo()
	{
		return nbAmmo;
	}

	public void setNbAmmo(int nbAmmo)
	{
		this.nbAmmo = nbAmmo;
	}

	@Override
	public void levelUp()
	{
		// TODO Auto-generated method stub
		this.level = this.newLevelReachable;
		this.energy+=10;
		this.resistance+=10;
		this.timeBetweenLaunchesM*=0.99;
		levelChanged=true;
	}
	
	@Override
	public void hideLabel()
	{
		if (nameGfx != null)
		{
			nameGfx.invalidate();
		}
	}

	@Override
	public void showLabel()
	{
		if (nameGfx != null)
		{
			if (boatBody.isValide())
				nameGfx.validate();
		}
	}

	/**
	 * @return the rangeInterception
	 */
	public double getRangeInterception()
	{
		return rangeInterception;
	}

	/**
	 * @param rangeInterception the rangeInterception to set
	 */
	public void setRangeInterception(double rangeInterception)
	{
		this.rangeInterception = rangeInterception;
	}

	@Override
	public String saveIfNeeded()
	{
		BoatSavePart saveState = new BoatSavePart(maxSpeed, standardSpeed, silenceSpeed, noiseSignature, 
				visibilitySignature, noiseLevel, visibilityLevel, typeEnemy, resistance, cost, tonnage, 
				hullState, dataLinkState, navState, damaged, unmanned, nbTorpedoes, nbTorpedoesMax, nbTubes, nbMissiles, nbMissilesMax,
				typeLauncher, timeBeforeAirLaunches, timeBetweenAirLaunches, nbAmmo, rangeInterception, explodeTimeMax, orientation);
		
		return "*"+giveMyId() + "\n"+ saveState.saveAll();
	}

	@Override
	public List<String> loadIfSupported(List<String> myPart)
	{
		// First load using a BoatSavePart
		BoatSavePart saveState = new BoatSavePart(maxSpeed, standardSpeed, silenceSpeed, noiseSignature, 
				visibilitySignature, noiseLevel, visibilityLevel, typeEnemy, resistance, cost, tonnage, 
				hullState, dataLinkState, navState, damaged, unmanned, nbTorpedoes, nbTorpedoesMax, nbTubes, nbMissiles, nbMissilesMax,
				typeLauncher, timeBeforeAirLaunches, timeBetweenAirLaunches, nbAmmo, rangeInterception, explodeTimeMax, orientation);
		
		List<String> remaingList = saveState.load(myPart);
		
		maxSpeed = saveState.maxSpeed;
		standardSpeed = saveState.standardSpeed;
		silenceSpeed = saveState.silenceSpeed;
		noiseSignature = saveState.noiseSignature;
		visibilitySignature = saveState.visibilitySignature;
		noiseLevel = saveState.noiseLevel;
		visibilityLevel = saveState.visibilityLevel;
		typeEnemy = saveState.typeEnemy;
		resistance = saveState.resistance;
		cost = saveState.cost;
		tonnage = saveState.tonnage;
		hullState = saveState.hullState;
		dataLinkState = saveState.dataLinkState;
		navState = saveState.navState;
		damaged = saveState.damaged;
		unmanned = saveState.unmanned;
		nbTorpedoes = saveState.nbTorpedoes;
		nbTorpedoesMax = saveState.nbTorpedoesMax;
		nbTubes = saveState.nbTubes;
		nbMissiles = saveState.nbMissiles;
		nbMissilesMax = saveState.nbMissilesMax;
		
		StackTraceElement[] allStack = Thread.currentThread().getStackTrace();

		if (this.nbMissilesMax == 400)
		{
			System.err.println("Might have hound the culprit!");
			
			for (StackTraceElement oneStackTraceElement: allStack)
			{
				System.err.println(oneStackTraceElement.toString());
			}
		}
		
		typeLauncher = saveState.typeLauncher;
		timeBeforeAirLaunches = saveState.timeBeforeAirLaunches;
		timeBetweenAirLaunches = saveState.timeBetweenAirLaunches;
		nbAmmo = saveState.nbAmmo;
		rangeInterception = saveState.rangeInterception;
		explodeTimeMax = saveState.explodeTimeMax;
		orientation = saveState.orientation;

		return remaingList;
	}

	@Override
	public String giveMyId()
	{
		// TODO Auto-generated method stub
		return SAVE_ID;
	}
	
	@Override
	public String getTypeName()
	{
		String nameTypeUnit;
		
		switch( type )
		{
			case Boat.CARRIER:
				nameTypeUnit="Carrier"; 
				break;
			case Boat.AMPHIBIOUS:
				nameTypeUnit="Amphibious"; 
				break;
			case Boat.CORVETTE:
				nameTypeUnit="Corvette"; 
				break;
			case Boat.CRUISER:
				nameTypeUnit="Cruiser"; 
				break;
			case Boat.DESTROYER:
				nameTypeUnit="Destroyer"; 
				break;
			case Boat.FRIGATE:
				nameTypeUnit="Frigate"; 
				break;
			case Boat.CARGO:
				nameTypeUnit="Cargo"; 
				break;
			case Boat.TANKER:
				nameTypeUnit="Tanker"; 
				break;
			case Boat.CRUISESHIP:
				nameTypeUnit="Streamer"; 
				break;
			case Boat.FISHING_BOAT:
				nameTypeUnit="Fishing Boat"; 
				break;
			case Boat.PLEASANCE:
				nameTypeUnit="Private Boat"; 
				break;
			default:
				nameTypeUnit="Unknown"; 
		}
		
		return nameTypeUnit;
	}
}
