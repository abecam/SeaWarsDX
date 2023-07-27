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

import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gfxentities.IAttachable;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.gfxentities.Text;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subengine.particlessystem.ParticlePC;
import com.tgb.subgame.GameKeeper;
import com.tgb.subgame.GfxRanks;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.MapKeeper;
import com.tgb.subgame.StrategicMap;
import com.tgb.subgame.TacticalMapPC;
import com.tgb.subgame.unitspc.sensors.Sensor;

/**
 * Super-class for the submarines
 * @author Alain Becam
 *
 */
public class Submarine extends ProgrammableUnit
{
	public static final String SAVE_ID = "Submarine";

	double maxDepth=-300;
	
	double wantedDepth;
	
	double orientation;
	
	double xShift,yShift;
	
	double currentSpeed=0; // In knots
	
	double maxSpeed=35; // In knots
	double standardSpeed=20;
	double silenceSpeed=15;
	
	double speedX=0,speedY=0,speedZ=0;
	double wantedSpeedX=0,wantedSpeedY=0,wantedSpeedZ=0;
	double tmpSpeedX=0,tmpSpeedY=0,tmpSpeedZ=0,tmpSpeedN=0;
	
	int noiseSignature; // How silent this unit can be
	int visibilitySignature;
	int noiseLevel; // 0-> None, 100-> enough :)
	int visibilityLevel; // 100-> Surface (perfectly visible if no fog/clouds), 0-> Very deep (not visible at all)
	java.util.ArrayList<Sensor> sensors;
	java.util.ArrayList weapons;
	java.util.ArrayList countermeasures;
	
	int typeEnemy; // Mask result to find the enemy. 1 for allies (1 && allies = 1), 0 otherwise (1 && enemy = 0)
	
	public static final int NUKE=0;
	public static final int NUKE_SSBN=1;
	public static final int DIESEL=2;
	public static final int DIESEL_AIP=3; // Air Independant Propulsion
	
	public int radarMaxDepth=-10;
	public int missilesMaxDepth=-20;
	
	public boolean missilesOk=false;
	public boolean radarOk=false;
	
	int resistance=2;
	
	int tonnage;
	
	boolean damaged=false;	
	
	boolean unmanned=false;
	
	double iDead; // to sunk
	
	long cost;
	
	// The different parts, ok or damaged.
	double hullState=100;
	double dataLinkState=100;
	double navState=100;
	
	transient SpritePC subBody = null;
	transient Emitter bubblesEmitter = null; // For enemies's torpedoes, both the sub and the bubbles will appear if found (by gravitation or passive/active sonar)
	transient Particles bubbles = null;
	transient ParticlePC bubbleExample = null;
	
	transient SpritePC boatSquare;
	transient Text nameGfx;
	
	transient SpritePC rankSprite;
	
	transient long idBubbles;
	transient long idBody;
	transient long idBoatSquare;
	transient long idName;
	transient long idRank;
	
	ArrayList<IAttachable> attachedObjects; // Attached object, will be moved and rotate with the entity
	AffineTransform transformToApply; // Common affine transform
	
	Point2D.Double onePoint2D ;
	Point2D.Double onePoint2Dtmp ;
	
	int nbTorpedoes;
	int nbTubes; // Will change the time to fire new torpedoes
	int nbMissiles;
	int typeLauncher;
	static final int VERTICAL=0;
	static final int TUBE=1; // Must share with the torpedo
	
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
	
	double depthWater = 200;
	
	/**
	 * @param theMap
	 */
	public Submarine(LevelMap theMap)
	{
		super(theMap);
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		this.depth = 0;
		
		nbTorpedoes = 30;
		nbTubes = 6;
		nbMissiles = 20;
		typeLauncher=Submarine.VERTICAL;
		
		timeBetweenLaunchesM = 0.1;
		nbInSalveMaxM = 8;
			
		timeBetweenLaunchesT = 5;
		nbInSalveMaxT=nbTubes;
		
		// Default
		type = NUKE;
		
		wantedSpeed= maxSpeed;
		
		radarMaxDepth=-10;
		missilesMaxDepth=-20;
		
		SndContainer.callMeFirst();
	}
	
	/**
	 * @param theMap
	 */
	public Submarine()
	{
		super();
		
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		this.depth = 0;
		
		nbTorpedoes = 30;
		nbTubes = 6;
		nbMissiles = 20;
		typeLauncher=Submarine.VERTICAL;
		
		timeBetweenLaunchesM = 0.1;
		nbInSalveMaxM = 8;
			
		timeBetweenLaunchesT = 5;
		nbInSalveMaxT=nbTubes;
		
		// Default
		type = NUKE;
		
		wantedSpeed= maxSpeed;
		
		radarMaxDepth=-10;
		missilesMaxDepth=-20;
		
		SndContainer.callMeFirst();
	}
	/**
	 * @param depth the depth to set
	 */
	public void setDepth(double depth)
	{
		this.depth = depth;
		if (this.depth< this.maxDepth)
			this.depth=this.maxDepth;
		if (this.depth> -7)
			this.depth=-7;
		int visib=(int )(255+depth);
		if (visib < 0)
			visib = 0;
		if (visib > 255)
			visib = 255;
		if (subBody != null)
			subBody.setAlpha(visib);
		if (bubbleExample != null)
			bubbleExample.setMyColor((bubbleExample.getMyColor() & 0x00FFFFFF) + visib<<24);
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
			//this.typeFaction=1;
			typeEnemy = 0; // The enemies are not allies!
		}
		else
		{
			//this.typeFaction=0;
			typeEnemy=1;
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
		return gfxSprites.getImageSub3D();
	}
	/**
	 * Create the graphics entity
	 * If you have more than one sub (should be :) ), it is better to use the second
	 * method, with Image as parameter: so the image is shared, not loaded for all.
	 */
	public void createGfx(double x, double y, double z, double direction, double speed)
	{
		CoreImage subImage;
		
		if (this.typeFaction != FUnit.SUB_ENEMY)
		{
			subImage=gfxSprites.getImageSub3D();
		}
		else
		{
			subImage=gfxSprites.getImageSub3DEne();
		}

		createGfx(subImage, x, y, z, direction, speed);
	}
	
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfx(CoreImage subImage, double x, double y, double z, double direction, double speed)
	{	
		if (!dead)
		{
			subBody = new SpritePC(subImage);
			subBody.setRotation(direction);
			subBody.setAlpha(100);
			xShift=20;
			yShift=7.5;
			subBody.setPos(x, y, z);
			idBody = RenderingManager.getInstance().addDrawableEntity(subBody,5); // Deep

			boatSquare = new SpritePC(gfxSprites.getSelImage());
			boatSquare.setSize(1.2);
			boatSquare.invalidate();
			subBody.addAttachedObject(boatSquare);
			idBoatSquare = RenderingManager.getInstance().addDrawableEntity(boatSquare,39);

			nameGfx = new Text();
			nameGfx.setTextToShow(this.getName()+" "+this.getLevel());
			
			CoreFont font = CoreFont.getSystemFont();
			
			int originalWidth = font.getStringWidth(nameGfx.getTextToShow());
			int originalHeight = font.getHeight();

			nameGfx.setSizeRect(originalWidth,originalHeight);
			nameGfx.setPosAttach(0, -20, 0);
			nameGfx.invalidate();
			subBody.addAttachedObject(nameGfx);
			idName = RenderingManager.getInstance().addDrawableEntity(nameGfx,39);
			
			bubbleExample=new ParticlePC();
			bubbleExample.setAlive(true);
			bubbleExample.setEnergy(10);
			bubbleExample.setSize(1);
			bubbleExample.setWeight(10);
			bubbleExample.setTimeLeft(5000);
			bubbleExample.setMyColor(0xFF003099);
			bubbleExample.setColorAIncDec(-1);

			bubblesEmitter = new Emitter(x, y, Math.PI, 0.1, Emitter.POINT_DIRECTIONNAL, 1, 100, 0, speed/80, 5000,bubbleExample);
			//bubblesEmitter = new Emitter(x, y, 1, 10, 10, Math.PI, 0.1, Emitter.POINT_DIRECTIONNAL, 1, 100, 0, speed/80, 5000);
			bubbles=new Particles(100,bubblesEmitter,ParticlePC.class);
			bubblesEmitter.setPosAttach(-17, 0, 0);
			subBody.addAttachedObject(bubblesEmitter);

			idBubbles = RenderingManager.getInstance().addParticles(bubbles, 5);
		}
	}
	
	/**
	 * Update the sub
	 * NB: We do not simulate the torpedo here (yet)
	 */
	public void updateMe(double x, double y, double z, double direction, double speed)
	{
		subBody.setRotation(direction);
		subBody.setPos(x, y, z);
		bubblesEmitter.setSpeed(speed/20);
	}
	
	public void hideMe()
	{
		if ((!dead) && (!damaged))
		{
			subBody.invalidate();
			bubbles.invalidate();
			for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
			{
				sensors.get(iSensors).hide();
			}
		}
	}
	
	public void showMe()
	{
		if (subBody != null)
			subBody.validate();
		if (bubbles != null)
			bubbles.validate();
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
		RenderingManager.getInstance().removeEntity(idBody, 5);
		RenderingManager.getInstance().removeParticles(idBubbles, 5);
		RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
		RenderingManager.getInstance().removeEntity(idName, 39);
		
		bubbles = null;
		bubblesEmitter = null; 
		bubbleExample = null; 
		
		subBody = null;
		
		boatSquare = null;
		nameGfx = null;
		
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
		RenderingManager.getInstance().removeParticles(idBubbles, 5);
		
		bubbles = null;
		bubblesEmitter = null; 
		bubbleExample = null; 
				
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
			//this.programmedWPs=null;
		}
		washAllAttachedObjects();
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
			if (Math.abs(speedY)>0.00001)
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
			indexWP=0;
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
			if ( distToTarget < 1000)
			{
				if (!programmedWPs.hasOneOrMoreElement() || programmedWPs.isEdited())
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
				this.tmpSpeedX = (this.targetX - this.posX);
				this.tmpSpeedY = (this.targetY - this.posY);
				this.tmpSpeedN = Math.sqrt(tmpSpeedX*tmpSpeedX+ tmpSpeedY*tmpSpeedY);
				this.wantedSpeedX = this.tmpSpeedX / tmpSpeedN;
				this.wantedSpeedY = this.tmpSpeedY / tmpSpeedN;
				this.wantedSpeedX*=this.standardSpeed/80;
				this.wantedSpeedY*=this.standardSpeed/80;

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
			//System.out.println("Time to wait "+timeSinceLastLaunchM);
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
				SndContainer.playFireTorpSound();
				
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
				oneTorpedo.setDamage(800);
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
					case FUnit.SUB_OUR:
						GameKeeper.getInstance().addTorpedoes(1);
						break;
					case FUnit.SUB_ALLIED:
						GameKeeper.getInstance().addTorpedoesAllies(1);
						break;
					case FUnit.SUB_ENEMY:
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
			if ((nbInSalveT >= nbInSalveMaxT) && !loadingT)
			{
				loadingT=true;
				timeSinceLastLaunchT=timeBetweenLaunchesT;
			}
			if (missilesOk)
			{
				// No missile launch if too deep
				if ((this.typeFireAsked == ProgrammableUnit.FIRE_MISSILE) && (this.nbMissiles > 0) && (nbInSalveM < nbInSalveMaxM))
				{
					SndContainer.playFireMissSound();
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
					//oneMissile.setDamage(100);
					oneMissile.setDamage(500);
					oneMissile.setDamageRadiusSq(5000);
					oneMissile.setPosX(this.getPosX()+nbInSalveM);
					oneMissile.setPosY(this.getPosY());
					oneMissile.setIdOwner(this.idBoat);
					oneMissile.setTypeFaction(this.typeFaction);
					oneMissile.setSeek(this.fireWithSeek);
					oneMissile.setTarget(this.fireWithTarget);
					oneMissile.setWpbased(this.fireWithWP);
					this.theMap.addMissile(oneMissile);

					nbMissiles--;

					// Inform the map that we did shoot.
					theMap.didShoot();
					
					switch (this.typeFaction)
					{
						case FUnit.SUB_OUR:
							GameKeeper.getInstance().addMissiles(1);
							break;
						case FUnit.SUB_ALLIED:
							GameKeeper.getInstance().addMissilesAllies(1);
							break;
						case FUnit.SUB_ENEMY:
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
			}
			else
			{
				fireAsked=false;
			}
			if ((nbInSalveM >= nbInSalveMaxM) && !loadingM)
			{
				loadingM=true;
				timeSinceLastLaunchM=timeBetweenLaunchesM;
			}
		}

		if (this.autonomous)
		{
			boolean missileFireDone= false;
			
			for (int iUnits=0; iUnits< foundUnits.size(); iUnits++)
			{
				FUnit tmpUnit = foundUnits.get(iUnits);
				//System.out.println("Contact to sink - "+tmpUnit.getLevelDetection());
				// Check deep of the unit (missile or torpedo ? )
				if ((tmpUnit.getLevelDetection() > 2) || this.isIgnoreFriends())
				{
					if (((tmpUnit.getDepth() < -1) && (tmpUnit.getType() != FUnit.TORPEDO)) || (tmpUnit.getType() == FUnit.UNKNOWN_SEA) || ((this.nbMissiles == 0) && (tmpUnit.getType() != FUnit.UNKNOWN_AIR) && (tmpUnit.getType() != FUnit.MISSILE)&& (tmpUnit.getType() != FUnit.TORPEDO) 
							&& (tmpUnit.getType() != FUnit.AIRPLANE_ALLIED) && (tmpUnit.getType() != FUnit.AIRPLANE_ENEMY) && (tmpUnit.getType() != FUnit.AIRPLANE_OUR)
							&& (tmpUnit.getType() != FUnit.BASE_ALLIED) && (tmpUnit.getType() != FUnit.BASE_ENEMY) && (tmpUnit.getType() != FUnit.BASE_OUR)))
					{
						if (tmpUnit.getType() == FUnit.UNKNOWN_SEA)
							tmpUnit.setType(FUnit.UNKNOWN_SEA | (1-typeEnemy));

						//System.out.println("Sub to sink");
						// Torpedo !!!
						if ((this.nbTorpedoes > 0) && (this.fireAtWill) && (nbInSalveT < nbInSalveMaxT))
						{
							if (this.isIgnoreFriends() || 
									(!this.ignoreFriends && ((tmpUnit.getType() & 1) == typeEnemy)))

							{
								SndContainer.playFireTorpSound();
								Torpedo oneTorpedo= new Torpedo(this.theMap);
//								if (typeEnemy == 1)
//								{
//								oneTorpedo.setKnowAllies(false);
//								oneTorpedo.setKnowEnemies(true);
//								}
								oneTorpedo.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
								oneTorpedo.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.6, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.6, 0);
								oneTorpedo.setCurrentSpeed(500);
								oneTorpedo.setDamage(400);
								oneTorpedo.setOrientation(this.getOrientation()+nbInSalveT*0.1);
								oneTorpedo.setPosX(this.getPosX());
								oneTorpedo.setPosY(this.getPosY());
								oneTorpedo.setTypeFaction(this.typeFaction);
								this.theMap.addTorpedo(oneTorpedo);

								nbTorpedoes--;

								// Inform the map that we did shoot.
								theMap.didShoot();
								
								switch (this.typeFaction)
								{
									case FUnit.SUB_OUR:
										GameKeeper.getInstance().addTorpedoes(1);
										break;
									case FUnit.SUB_ALLIED:
										GameKeeper.getInstance().addTorpedoesAllies(1);
										break;
									case FUnit.SUB_ENEMY:
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
						if (missilesOk && (this.nbMissiles > 0) && (this.fireAtWill) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() != FUnit.TORPEDO))
						{
							if (this.isIgnoreFriends() || 
									(!this.ignoreFriends && ((tmpUnit.getType() & 1) == typeEnemy) ))
							{
								SndContainer.playFireMissSound();
								Missile oneMissile= new Missile(this.theMap);
								missileFireDone = true;
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
								oneMissile.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.6, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.6, 0);
								oneMissile.setCurrentSpeed(500);
								oneMissile.setDamageRadiusSq(5000);
								//oneMissile.setDamage(100);
								oneMissile.setDamage(500);
								oneMissile.setOrientation(this.getOrientation()+nbInSalveM*0.1);
								oneMissile.setPosX(this.getPosX());
								oneMissile.setPosY(this.getPosY());
								oneMissile.setTypeFaction(this.typeFaction);
								this.theMap.addMissile(oneMissile);

								nbMissiles--;

								// Inform the map that we did shoot.
								theMap.didShoot();
								
								switch (this.typeFaction)
								{
									case FUnit.SUB_OUR:
										GameKeeper.getInstance().addMissiles(1);
										break;
									case FUnit.SUB_ALLIED:
										GameKeeper.getInstance().addMissilesAllies(1);
										break;
									case FUnit.SUB_ENEMY:
										GameKeeper.getInstance().addMissilesEne(1);
										break;
								}
								
								nbInSalveM++;
							}
						}
						if (!missilesOk && (this.nbMissiles > 0) && (this.fireAtWill) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() != FUnit.TORPEDO))
						{
							// If I am an enemy, request to go up first...
							this.wantedDepth = -9;
							// So we can fire.
						}
						
//						if ((this.nbMissiles > 0) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() == FUnit.MISSILE))
//						{
//						Missile oneMissile= new Missile(this.theMap);
//						oneMissile.setKnowAllies(true);
//						oneMissile.setKnowEnemies(true);
//						oneMissile.setSeek(false);

//						oneMissile.createGfx(this.getPosX(), this.getPosY(), 0,this.getOrientation(), 500);
//						oneMissile.setTargetPos(this.getPosX()+(tmpUnit.getX()-this.getPosX())*0.6, this.getPosY()+(tmpUnit.getY()-this.getPosY())*0.6, 0);
//						oneMissile.setCurrentSpeed(500);
//						oneMissile.setOrientation(this.getOrientation());
//						oneMissile.setPosX(this.getPosX());
//						oneMissile.setPosY(this.getPosY());
//						this.theMap.addMissile(oneMissile);

//						nbMissiles--;
//						nbInSalveM++;
//						}
						if ((nbInSalveM >= nbInSalveMaxM) && !loadingM)
						{
							loadingM=true;
							timeSinceLastLaunchM=timeBetweenLaunchesM;
						}
					}
				}
			}
			if ((!missileFireDone) && (this.depth > -15) )
			{
				// No missile to fire, going down
				this.wantedDepth = -200;
				//System.out.println("No missile to fire, going down "+depth);
			}
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
			updateAttachedObjects();

			this.orientation+=angleToTurn*time*rudderEfficiency;
			
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
			if ((this.depth - this.wantedDepth) > 2)
			{
				depth-=(0.04+0.04*Math.abs(this.currentSpeed));
				if (depth < missilesMaxDepth)
				{
					// No more missiles
					missilesOk=false;
				}
				if (depth < radarMaxDepth)
				{
					// No more radar
					radarOk=false;
					for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
					{
						if (sensors.get(iSensors).getMyType() == Sensor.RADAR)
						{
							sensors.get(iSensors).desactivate();
						}
					}
				}
				this.setDepth(depth);
			}
			if ((this.depth - this.wantedDepth) < -2)
			{
				depth+=(0.04+0.04*Math.abs(this.currentSpeed));
				if (depth >= missilesMaxDepth)
				{
					// missiles ok
					missilesOk=true;
				}
				if (depth >= radarMaxDepth)
				{
					// radar ok
					radarOk=true;
					
					for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
					{
						if (sensors.get(iSensors).getMyType() == Sensor.RADAR)
						{
							sensors.get(iSensors).activate();
						}
					}
				}
				this.setDepth(depth);
			}
			
			this.posX = this.posX + time*this.currentSpeed*Math.cos(this.orientation);
			this.posY = this.posY + time*this.currentSpeed*Math.sin(this.orientation);
			
			depthWater = ourLevelKeeper.getAlpha((int )this.posX, (int )this.posY)-255;
			
			if ((this.getDepth() < depthWater) && (depthWater != 255))
				this.setDepth(depthWater);
			//System.out.println("Depth "+depthWater);
			
			if (( this.currentSpeed > 3) && (depthWater > -10))
			{
				// Not good :)
				//this.turn(0.2);
				this.wantedSpeed =0;
			}
			else if (( this.currentSpeed < -3) && (depthWater > -10))
			{
				// Not good :)
				//this.turn(0.2);
				this.wantedSpeed =0;
			}
			
			if (!unmanned)
			{
				// If no waypoints, we turn!
				if (this.programmedWPs.isEmpty() || (!followWP))
				{
					if (this.currentSpeed < this.wantedSpeed)
						this.currentSpeed += 2;
					else if (this.currentSpeed > this.wantedSpeed)
							this.currentSpeed -= 2;
					//this.orientation += 0.08*time;
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
			foundUnits = this.theMap.getOurKD().getFoundUnits();
			
			attackIfNeeded(time);
			
			if ((this.nbTorpedoes > 0) || (this.nbMissiles > 0))
			{
				theMap.notOutOfAmmo(this.idBoat);
			}
		}
		else if (iDead > depthWater)
		{
			// Die !!!
			this.setDepth(iDead--);
			SndContainer.playExplosion4Sound();
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

			this.complement-=Math.random()*amount/(this.resistance*hullState);

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
				this.showMe();
				bubblesEmitter.setSpeed(0);
				
				// Sunking a submaring should kill most aboard... :(
				this.complement-=Math.random()*amount/(this.resistance);
				if (this.complement == 0)
				{
					unmanned = true;
				}
				
				for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
				{
					sensors.get(iSensors).removeMe();
				}
				sensors.clear();
				iDead = this.getDepth();
				bubbles.invalidate();
				removeMeWOBody();
				
				theMap.subDead(this.typeFaction);
				
				if (depthWater < -200)
					depthWater=-200;
				if (depthWater > -15)
					depthWater=-15;
			}
			if ((this.hullState < 50) && (!damaged))
			{
				damaged = true;
				this.maxSpeed/=2;
				wantedSpeed=maxSpeed;
				if (currentSpeed > maxSpeed)
					currentSpeed = maxSpeed;
				//smokeBlack.validate();
			}
		}
		//System.out.println(" Sub hit Hull "+hullState+"% navState "+navState+"% "+" dataLinkState "+dataLinkState+"% Complement: "+complement);
	}
	public int getNbTorpedoes() {
		return nbTorpedoes;
	}
	public void setNbTorpedoes(int nbTorpedoes) {
		this.nbTorpedoes = nbTorpedoes;
	}
	public int getNbMissiles() {
		return nbMissiles;
	}
	public void setNbMissiles(int nbMissiles) {
		this.nbMissiles = nbMissiles;
	}
	public int getNbTubes() {
		return nbTubes;
	}
	public void setNbTubes(int nbTubes) {
		this.nbTubes = nbTubes;
	}
	public int getTypeLauncher() {
		return typeLauncher;
	}
	public void setTypeLauncher(int typeLauncher) {
		this.typeLauncher = typeLauncher;
	}
	public double getHullState() {
		return hullState;
	}
	public void setHullState(double hullState) {
		this.hullState = hullState;
	}
	public double getDataLinkState() {
		return dataLinkState;
	}
	public void setDataLinkState(double dataLinkState) {
		this.dataLinkState = dataLinkState;
	}
	public double getNavState() {
		return navState;
	}
	public void setNavState(double navState) {
		this.navState = navState;
	}
	public boolean isDead() {
		return dead;
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
	
	@Override
	public void setIdBoat(int idBoat) {
		// TODO Auto-generated method stub
		super.setIdBoat(idBoat);
		for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
		{
			sensors.get(iSensors).setIdOwner(idBoat);
		}
	}
	public double getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(double maxDepth) {
		this.maxDepth = maxDepth;
	}
	public double getWantedDepth() {
		return wantedDepth;
	}
	public void setWantedDepth(double wantedDepth) {
		this.wantedDepth = wantedDepth;
		if (this.wantedDepth< this.maxDepth)
			this.wantedDepth=this.maxDepth;
		if (this.wantedDepth> -7)
			this.wantedDepth=-7;
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
				//System.out.println("Radar found - ok: "+radarOk+" - "+(depth >= radarMaxDepth)+" - "+depth+" : "+radarMaxDepth);
				if (isOn && radarOk)
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
	
	/**
	 * Eventually add a periscope
	 * @param isOn
	 */
	public void pushPeriscopeOnOff(boolean isOn)
	{
		for (int iSensors=0;iSensors < sensors.size();iSensors++)
		{
			if (sensors.get(iSensors).getMyType() == Sensor.PERISCOPE)
			{
				//System.out.println("Radar found - ok: "+radarOk+" - "+(depth >= radarMaxDepth)+" - "+depth+" : "+radarMaxDepth);
				if (isOn && radarOk)
					sensors.get(iSensors).activate();
				else
					sensors.get(iSensors).desactivate();
			}
		}
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
			this.maxSpeed*=2;
			damaged = false;
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
		
		if (nbTorpedoes < 40)
				nbTorpedoes++;
		switch (type)
		{
			case Submarine.NUKE:
				if (nbMissiles < 20)
					nbMissiles++;
				break;
			case Submarine.NUKE_SSBN:
				if (nbMissiles < 20)
					nbMissiles++;
				break;
			case Submarine.DIESEL:
				if (nbMissiles < 10)
					nbMissiles++;
				break;
			case Submarine.DIESEL_AIP:
				if (nbMissiles < 15)
					nbMissiles++;
				break;
			default:	
				if (nbMissiles < 10000)
					nbMissiles++;
		}	
	}
	
	/***
	 * Strategic map methods
	 */
	
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfxSM(double x, double y, double z, double direction, double speed)
	{	
		if (!dead)
		{
			subBody = new SpritePC(gfxSprites.getImageSub3D());
			subBody.setRotation(direction);
			//subBody.setAlpha(100);
			subBody.setSize(0.4);
			xShift=20;
			yShift=7.5;
			subBody.setPos(x, y, z);
			subBody.setRotation(direction);
			idBody = RenderingManager.getInstance().addDrawableEntity(subBody,5); // Deep

			createAttachedRank();
			
			boatSquare = new SpritePC(gfxSprites.getSelImage());
			boatSquare.setSize(0.4);
			boatSquare.invalidate();
			subBody.addAttachedObject(boatSquare);
			idBoatSquare = RenderingManager.getInstance().addDrawableEntity(boatSquare,39);
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
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 2)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR2Ball());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 3)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR3Ball());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 4)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRBBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 5)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR1SilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 6)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR2SilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 7)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR3SilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 8)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRBSilverBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 9)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR1GoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 10)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR2GoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 11)
		{
			rankSprite = new SpritePC(GfxRanks.getImageR3GoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 12)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRBGoldBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level == 13)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRCrystalBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
		if (level >= 14)
		{
			rankSprite = new SpritePC(GfxRanks.getImageRShiningBall());
			rankSprite.setSize(0.2);
			rankSprite.setPosAttach(0, -12, 0);
			subBody.addAttachedObject(rankSprite);
			idRank = RenderingManager.getInstance().addDrawableEntity(rankSprite,38);
		}
	}
	
	/**
	 * Update the sub
	 * NB: We do not simulate the torpedo here (yet)
	 */
	public void updateMeSM(double x, double y, double z, double direction, double speed)
	{
		subBody.setRotation(direction);
		subBody.setPos(x, y, z);
	}
	
	public void doUpdateSM(double time)
	{	
		if (!dead)
		{
			if (levelChanged)
			{
				RenderingManager.getInstance().removeEntity(idRank,38);
				createAttachedRank(); // Change the rank icon
				
				levelChanged=false;
			}
			
			updateAttachedObjects();
			
			if (followTargetMap)
			{
				this.xTmpMap = this.xMap + 4*time*this.xMapSpeed;
				this.yTmpMap = this.yMap + 4*time*this.yMapSpeed;
				
				if (((MapKeeper.getInstance().getValue((int )xTmpMap, (int )yTmpMap, 0, 0, StrategicMap.START_MENU, 800) < 5)
						|| (Math.abs(xMapSpeed)+Math.abs(yMapSpeed) < 0.1))
						&& (MapKeeper.getInstance().stickSub(this,xTmpMap,yTmpMap)))
				{
//					Vector2D repulseVec=MapKeeper.getInstance().calculateRepulsionSub(this, xTmpMap, yTmpMap);
//					
//					if ((Math.abs(repulseVec.getX()) > 0.1 ) ||(Math.abs(repulseVec.getY()) > 0.1 ) )
//					{
//						xMapSpeed+=repulseVec.getX();
//						yMapSpeed+=repulseVec.getY();
//						checkAndNormaliseSpeedSM();
//					}	
					
					this.xMap = this.xMap + time*this.xMapSpeed;
					this.yMap = this.yMap + time*this.yMapSpeed;

				}
				
				if (xMap >= StrategicMap.START_MENU)
					xMap=StrategicMap.START_MENU-1;
				if (yMap >= 800)
					yMap=799;
				
				if (!unmanned)
				{
					seekTargetSM();
				}
			}
			
			this.updateMeSM(xMap, yMap, 0, this.orientation, this.currentSpeed);
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
	
	public void hideMeSM()
	{
		subBody.invalidate();
	}
	
	public void showMeSM()
	{
		subBody.validate();
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
		RenderingManager.getInstance().removeEntity(idBody, 5);
		RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
		RenderingManager.getInstance().removeEntity(idRank,38);
				
		subBody = null;
		
		boatSquare = null;
		
		rankSprite = null;
		
		washAllAttachedObjects();
	}
	
	public void removeMeWOBodySM()
	{
		RenderingManager.getInstance().removeEntity(idRank,38);
		
		rankSprite = null;
	}
	
	@Override
	public void levelUp()
	{
		// TODO Auto-generated method stub
		this.level = this.newLevelReachable;
		this.energy+=10;
		this.resistance+=10;
		this.timeBetweenLaunchesM/=0.99;
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
			if (subBody.isValide())
				nameGfx.validate();
		}
	}

	@Override
	public String saveIfNeeded()
	{
		SubmarineSavePart saveState = new SubmarineSavePart(maxSpeed, standardSpeed, silenceSpeed, noiseSignature, 
				visibilitySignature, noiseLevel, visibilityLevel, typeEnemy, radarMaxDepth, missilesMaxDepth, 
				resistance, tonnage, cost, hullState, dataLinkState, navState, damaged, unmanned, nbTorpedoes, 
				nbTubes, nbMissiles, typeLauncher, timeBetweenLaunchesM, nbInSalveMaxM, timeBetweenLaunchesT, nbInSalveMaxT, orientation);
		
		return saveState.saveAll();
	}

	@Override
	public List<String> loadIfSupported(List<String> myPart)
	{
		SubmarineSavePart saveState = new SubmarineSavePart(maxSpeed, standardSpeed, silenceSpeed, noiseSignature, 
				visibilitySignature, noiseLevel, visibilityLevel, typeEnemy, radarMaxDepth, missilesMaxDepth, 
				resistance, tonnage, cost, hullState, dataLinkState, navState, damaged, unmanned, nbTorpedoes, 
				nbTubes, nbMissiles, typeLauncher, timeBetweenLaunchesM, nbInSalveMaxM, timeBetweenLaunchesT, nbInSalveMaxT, orientation);
		
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
		nbTubes = saveState.nbTubes;
		nbMissiles = saveState.nbMissiles;
		typeLauncher = saveState.typeLauncher;
		timeBetweenLaunchesM = saveState.timeBetweenLaunchesM;
		nbInSalveMaxM = saveState.nbInSalveMaxM;
		timeBetweenLaunchesT = saveState.timeBetweenLaunchesT;
		nbInSalveMaxT = saveState.nbInSalveMaxT;
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
			case Submarine.NUKE:
				nameTypeUnit="SSN"; 
				break;
			case Submarine.DIESEL:
				nameTypeUnit="Diesel"; 
				break;
			case Submarine.DIESEL_AIP:
				nameTypeUnit="AIP Diesel"; 
				break;
			case Submarine.NUKE_SSBN:
				nameTypeUnit="SSBN"; 
				break;
			default:
				nameTypeUnit="Unknown"; 
		}
		
		return nameTypeUnit;
	}
}
