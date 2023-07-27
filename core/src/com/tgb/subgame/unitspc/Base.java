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

import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;

import com.tgb.subengine.RenderingManager;
import com.tgb.subengine.gameentities.Waypoint;
import com.tgb.subengine.gfxentities.CirclePC;
import com.tgb.subengine.gfxentities.IAttachable;
import com.tgb.subengine.gfxentities.SimpleLinePC;
import com.tgb.subengine.gfxentities.SpritePC;
import com.tgb.subengine.gfxentities.Text;
import com.tgb.subengine.particlessystem.Emitter;
import com.tgb.subengine.particlessystem.ParticlePC;
import com.tgb.subengine.particlessystem.ParticleSpritePC;
import com.tgb.subengine.particlessystem.MotionBlurParticleSpritePCAdd;
import com.tgb.subengine.particlessystem.Particles;
import com.tgb.subgame.GameKeeper;
import com.tgb.subgame.GfxRanks;
import com.tgb.subgame.LevelMap;
import com.tgb.subgame.RandomContainer;
import com.tgb.subgame.SimNames;
import com.tgb.subgame.unitspc.sensors.DeportedRadar;
import com.tgb.subgame.unitspc.sensors.Sensor;
import com.tgb.subgame.unitspc.sensors.SimpleAround;
/**
 * Super-class for the bases
 * @author Alain Becam
 *
 */
public class Base extends ProgrammableUnit 
{
	public static final String SAVE_ID = "Base";
	static int iBaseNameOurSmall=0;
	static int iBaseNameOurBig=0;
	static int iBaseNameOurMain=0;
	static int iBaseNameAlliesSmall=0;
	static int iBaseNameAlliesBig=0;
	static int iBaseNameAlliesMain=0;
	static int iBaseNameEnemiesSmall=0;
	static int iBaseNameEnemiesBig=0;
	static int iBaseNameEnemiesMain=0;
	
	boolean nameNotCreated=true;
	
	double orientation;
	
	double xShift,yShift;
	
	int visibilityLevel; // 100-> Surface (perfectly visible if no fog/clouds), 0-> Very deep (not visible at all)
	transient ArrayList<Sensor> sensors;
	transient ArrayList weapons;
	transient ArrayList countermeasures;
	
	int typeEnemy; // Mask result to find the enemy. 1 for allies (1 && allies = 1), 0 otherwise (1 && enemy = 0)
	
	public static final int SMALLBASEONE=0x80;
	public static final int SMALLBASETWO=0x81;
	public static final int BIGBASEONE=0x82;
	public static final int BIGBASETWO=0x83;
	public static final int MAINBASEONE=0x84;
	public static final int MAINBASETWO=0x85;
	
	// Invisible bases just here to be destroyed :) 
	public static final int DUMMYBASE=0x1001;
	
	public static final int MAX_AWACS_IN_FLIGHT=4;
	
	int resistance=4;
	
	int cost;
	
	// The different parts, ok or damaged.
	double baseState=100;
	double dataLinkState=100;
	double navState=100;
	
	boolean damaged=false;	
	
	boolean unmanned=false;
	
	double iDead; // to sunk
	
	transient SpritePC boatBody;
	transient Emitter bubblesEmitter; 
	transient Particles bubbles;
	transient SimpleLinePC  fireLine;
	transient SimpleLinePC  fireLine2;
	transient SimpleLinePC  fireLine3;
	
	transient SpritePC boatSquare;
	
	transient Particles smokeBlack;
	transient Emitter badSmokeEmitter; 
	
	transient Particles sparks;
	transient Emitter sparksEmitter; 
	
	transient CirclePC ourCircle;
	
	transient Text nameGfx;
	
	transient SpritePC rankSprite;
	
	transient long idBody; // Ids to remove the gfx elements.
	transient long idBubbles;
	transient long idBadSmoke;
	transient long idExplosion;
	transient long idBoatSquare;
	transient long idFireLine;
	transient long idFireLine2;
	transient long idFireLine3;
	transient long idSparks;
	transient long idName;
	transient long idCircle;
	transient long idRank;
	
///// To check, might need to be created elsewhere than the constructor (createGfx)
	transient ArrayList<IAttachable> attachedObjects; // Attached object, will be moved and rotate with the entity
	transient AffineTransform transformToApply; // Common affine transform
	
	Point2D.Double onePoint2D ;
	Point2D.Double onePoint2Dtmp ;
	
	int explodeTime=0;
	transient SpritePC explosion;
	
	int nbMissiles;
	int typeLauncher;
	
	double timeBeforeAirLaunches; // Counter between two airplane launches
	double timeBetweenAirLaunches; // Time between two airplane launches
	boolean loadingA=false;
	
	double timeSinceLastLaunchM;
	int nbInSalveM=0;
	double timeBetweenLaunchesM;
	int nbInSalveMaxM=0;
	boolean loadingM=false;
	
	double timeSinceLastLaunchM2;
	int nbInSalveM2=0;
	double timeBetweenLaunchesM2;
	int nbInSalveMaxM2=0;
	boolean loadingM2=false;
	
	double timeSinceLastLaunchC;
	double timeBetweenLaunchesC;
	boolean loadingC=false;
	
	double timeSinceLastLaunchC2;
	double timeBetweenLaunchesC2;
	boolean loadingC2=false;
	
	double timeSinceLastLaunchC3;
	double timeBetweenLaunchesC3;
	boolean loadingC3=false;
	
	int nbAmmo;
	double rangeInterception;
	
	int nbAmmo2;
	double rangeInterception2;
	
	int nbAmmo3;
	double rangeInterception3;
	
	// Cannons relatives coordinates (well the names are reversed :( )
	double xCannon1Abs=-30;
	double yCannon1Abs=-10;
	double xCannon2Abs=0;
	double yCannon2Abs=30;
	double xCannon3Abs=30;
	double yCannon3Abs=-10;
	
	// Missiles launcher coordinates
	double xMissLaunch1Abs=-8;
	double yMissLaunch1Abs=5;
	double xMissLaunch2Abs=8;
	double yMissLaunch2Abs=-2;
	
	// Cannons coordinates
	double xCannon1;
	double yCannon1;
	double xCannon2;
	double yCannon2;
	double xCannon3;
	double yCannon3;
	
	// Missiles launcher coordinates
	double xMissLaunch1;
	double yMissLaunch1;
	double xMissLaunch2;
	double yMissLaunch2;
	
	/**
	 * @param theMap
	 */
	public Base(LevelMap theMap)
	{
		super(theMap);
		
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		
		nbMissiles = 4000;
		
		timeBetweenLaunchesM = 0.2;
		nbInSalveMaxM = 8;
			
		
		nbFighters=200;
		nbFightersOnFlightMax = 6;
		
		nbAwacs=5;
		
		timeBetweenAirLaunches= 1;
		
		timeBetweenLaunchesC=0.02;
		nbAmmo=400000;
		
		rangeInterception=8000;
		
		timeBetweenLaunchesC2=0.02;
		nbAmmo2=400000;
		
		rangeInterception2=8000;
		
		timeBetweenLaunchesC3=0.01;
		nbAmmo3=800000;
		
		rangeInterception3=10000;
		// Default
		type = SMALLBASEONE;
		
		SndContainer.callMeFirst();
	}
	
	/**
	 * 
	 */
	public Base()
	{
		super();
		
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		
		nbMissiles = 4000;
		
		timeBetweenLaunchesM = 0.8;
		nbInSalveMaxM = 8;
			
		
		nbFighters=200;
		nbFightersOnFlightMax = 6;
		
		nbAwacs=5;
		
		timeBetweenAirLaunches= 1;
		
		timeBetweenLaunchesC=0.02;
		nbAmmo=400000;
		
		rangeInterception=8000;
		
		timeBetweenLaunchesC2=0.02;
		nbAmmo2=400000;
		
		rangeInterception2=8000;
		
		timeBetweenLaunchesC3=0.01;
		nbAmmo3=800000;
		
		rangeInterception3=10000;
		
		// Default
		type = SMALLBASEONE;
		
		SndContainer.callMeFirst();
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

	public double getTimeBetweenAirLaunches()
	{
		return timeBetweenAirLaunches;
	}

	public void setTimeBetweenAirLaunches(double timeBetweenAirLaunches)
	{
		this.timeBetweenAirLaunches = timeBetweenAirLaunches;
	}

	public int getNbInSalveM()
	{
		return nbInSalveM;
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

	public int getNbInSalveM2()
	{
		return nbInSalveM2;
	}

	public double getTimeBetweenLaunchesM2()
	{
		return timeBetweenLaunchesM2;
	}

	public void setTimeBetweenLaunchesM2(double timeBetweenLaunchesM2)
	{
		this.timeBetweenLaunchesM2 = timeBetweenLaunchesM2;
	}

	public int getNbInSalveMaxM2()
	{
		return nbInSalveMaxM2;
	}

	public void setNbInSalveMaxM2(int nbInSalveMaxM2)
	{
		this.nbInSalveMaxM2 = nbInSalveMaxM2;
	}

	public double getTimeBetweenLaunchesC()
	{
		return timeBetweenLaunchesC;
	}

	public void setTimeBetweenLaunchesC(double timeBetweenLaunchesC)
	{
		this.timeBetweenLaunchesC = timeBetweenLaunchesC;
	}

	public double getTimeBetweenLaunchesC2()
	{
		return timeBetweenLaunchesC2;
	}

	public void setTimeBetweenLaunchesC2(double timeBetweenLaunchesC2)
	{
		this.timeBetweenLaunchesC2 = timeBetweenLaunchesC2;
	}

	public double getTimeBetweenLaunchesC3()
	{
		return timeBetweenLaunchesC3;
	}

	public void setTimeBetweenLaunchesC3(double timeBetweenLaunchesC3)
	{
		this.timeBetweenLaunchesC3 = timeBetweenLaunchesC3;
	}

	public int getNbAmmo()
	{
		return nbAmmo;
	}

	public void setNbAmmo(int nbAmmo)
	{
		this.nbAmmo = nbAmmo;
	}

	public double getRangeInterception()
	{
		return rangeInterception;
	}

	public void setRangeInterception(double rangeInterception)
	{
		this.rangeInterception = rangeInterception;
	}

	public int getNbAmmo2()
	{
		return nbAmmo2;
	}

	public void setNbAmmo2(int nbAmmo2)
	{
		this.nbAmmo2 = nbAmmo2;
	}

	public double getRangeInterception2()
	{
		return rangeInterception2;
	}

	public void setRangeInterception2(double rangeInterception2)
	{
		this.rangeInterception2 = rangeInterception2;
	}

	public int getNbAmmo3()
	{
		return nbAmmo3;
	}

	public void setNbAmmo3(int nbAmmo3)
	{
		this.nbAmmo3 = nbAmmo3;
	}

	public double getRangeInterception3()
	{
		return rangeInterception3;
	}

	public void setRangeInterception3(double rangeInterception3)
	{
		this.rangeInterception3 = rangeInterception3;
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
	
	public void initFields()
	{
		/*
		sensors = new ArrayList<Sensor>();

		attachedObjects = new ArrayList<IAttachable>();
		transformToApply = new AffineTransform();
		
		onePoint2D = new Point2D.Double();
		onePoint2Dtmp = new Point2D.Double();
		*/
	}
	
	public void createName()
	{
		if (nameNotCreated)
		{
			switch (this.type)
			{
				case Base.SMALLBASEONE:
				case Base.SMALLBASETWO:
					if (this.typeFaction == FUnit.BASE_OUR)
					{
						if (iBaseNameOurSmall < PoolOfNames.namesOurSmallBases.length)
						{
							this.name = PoolOfNames.namesOurSmallBases[iBaseNameOurSmall++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hugins 3";
							}
						}
					}
					if (this.typeFaction == FUnit.BASE_ALLIED)
					{
						if (iBaseNameAlliesSmall < PoolOfNames.namesAlliesSmallBases.length)
						{
							this.name = PoolOfNames.namesAlliesSmallBases[iBaseNameAlliesSmall++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
								
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hortin 6";
							}
						}
					}
					if (this.typeFaction == FUnit.BASE_ENEMY)
					{
						if (iBaseNameEnemiesSmall < PoolOfNames.namesEnemiesSmallBases.length)
						{
							this.name = PoolOfNames.namesEnemiesSmallBases[iBaseNameEnemiesSmall++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hkisar 63";
							}
						}
					}
					break;
				case Base.BIGBASEONE:
				case Base.BIGBASETWO:
					if (this.typeFaction == FUnit.BASE_OUR)
					{
						if (iBaseNameOurSmall < PoolOfNames.namesOurBigBases.length)
						{
							this.name = PoolOfNames.namesOurBigBases[iBaseNameOurBig++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hugins 12";
							}
						}
					}
					if (this.typeFaction == FUnit.BASE_ALLIED)
					{
						if (iBaseNameAlliesBig < PoolOfNames.namesAlliesBigBases.length)
						{
							this.name = PoolOfNames.namesAlliesBigBases[iBaseNameAlliesBig++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hortin 600";
							}
						}
					}
					if (this.typeFaction == FUnit.BASE_ENEMY)
					{
						if (iBaseNameEnemiesBig < PoolOfNames.namesEnemiesBigBases.length)
						{
							this.name = PoolOfNames.namesEnemiesBigBases[iBaseNameEnemiesBig++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hkisar 6";
							}
						}
					}
					break;	
					
				case Base.MAINBASEONE:
				case Base.MAINBASETWO:
					if (this.typeFaction == FUnit.BASE_OUR)
					{
						if (iBaseNameOurMain < PoolOfNames.namesOurMainBases.length)
						{
							this.name = PoolOfNames.namesOurMainBases[iBaseNameOurMain++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hortin 16";
							}
						}
					}
					if (this.typeFaction == FUnit.BASE_ALLIED)
					{
						if (iBaseNameAlliesMain < PoolOfNames.namesAlliesMainBases.length)
						{
							this.name = PoolOfNames.namesAlliesMainBases[iBaseNameAlliesMain++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Hortin 62";
							}
						}
					}
					if (this.typeFaction == FUnit.BASE_ENEMY)
					{
						if (iBaseNameEnemiesMain < PoolOfNames.namesEnemiesMainBases.length)
						{
							this.name = PoolOfNames.namesEnemiesMainBases[iBaseNameEnemiesMain++];
						}
						else
						{
							this.name = SimNames.getTheNextName();
							
							if (this.name.equals("Bidule"))
							{
								// We got out of name
								this.name = "Porzta 6";
							}
						}
					}
					break;
			}
			
			nameNotCreated=false;
		}
	}
	/**
	 * Create the graphics entity
	 */
	public void createGfx(double x, double y, double z, double direction, double speed)
	{
		CoreImage boatImage;
		if (this.type == Base.BIGBASEONE)
			boatImage=gfxSprites.getImageBase1();
		else if (this.type == Base.BIGBASETWO)
			boatImage=gfxSprites.getImageBase2();
		else if (this.type == Base.SMALLBASEONE)
			boatImage=gfxSprites.getImageBaseSmall1();
		else if (this.type == Base.SMALLBASETWO)
			boatImage=gfxSprites.getImageBaseSmall2();
		else if (this.type == Base.MAINBASEONE)
			boatImage=gfxSprites.getImageBaseMain1();	
		else if (this.type == Base.MAINBASETWO)
			boatImage=gfxSprites.getImageBaseMain2();
		else 
			boatImage=gfxSprites.getImageBase2();
		
		createGfx(boatImage, x, y, z, direction, speed);
	}
	
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfx(CoreImage boatImage, double x, double y, double z, double direction, double speed)
	{	
		initFields();
		this.nbAwacsSession=this.nbAwacs;
		if (this.nbAwacsSession > this.MAX_AWACS_IN_FLIGHT)
		{
			this.nbAwacsSession = this.MAX_AWACS_IN_FLIGHT;
		}
		
		if (this.type != DUMMYBASE)
		{
			boatBody = new SpritePC(boatImage);

			if (this.isDead())
			{
				if (this.type == Base.BIGBASEONE)
					boatBody.setImageToDraw(gfxSprites.getImageBase1Dest());
				else if (this.type == Base.BIGBASETWO)
					boatBody.setImageToDraw(gfxSprites.getImageBase2Dest());
				else if (this.type == Base.SMALLBASEONE)
					boatBody.setImageToDraw(gfxSprites.getImageBaseSmall1Dest());
				else if (this.type == Base.SMALLBASETWO)
					boatBody.setImageToDraw(gfxSprites.getImageBaseSmall2Dest());
				else if (this.type == Base.MAINBASEONE)
					boatBody.setImageToDraw(gfxSprites.getImageBase1Main1Dest());	
				else if (this.type == Base.MAINBASETWO)
					boatBody.setImageToDraw(gfxSprites.getImageBaseMain2Dest());
				else 
					boatBody.setImageToDraw(gfxSprites.getImageBase2Dest());
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
			explosion.setPosAttach(Math.random()*10-5, Math.random()*8-4, 0);
			boatBody.addAttachedObject(explosion);
			idExplosion = RenderingManager.getInstance().addDrawableEntity(explosion,21);

			ParticleSpritePC blackSmoke= new ParticleSpritePC(gfxSprites.getImageDarkSmoke());

			blackSmoke.setEnergy(10);
			blackSmoke.setSize(0.6);
			blackSmoke.setTimeLeft(5000);
			blackSmoke.setSizeIncDec(1.002);

			blackSmoke.setAlive(true);


			badSmokeEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 4, 600, 0, 0.3, 5000, blackSmoke);
			badSmokeEmitter.setPosAttach(Math.random()*8-4, 0, 0);
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

//			MotionBlurParticleSpritePCAdd spark=new MotionBlurParticleSpritePCAdd(GfxRanks.getImageRBGoldBall(),6);
//			
//			spark.setEnergy(10);
//			spark.setSize(0.1);
//			spark.setTimeLeft(50000);
//			//spark.setAcc(1.2, 1.2, 1.2);
//			spark.setMyColor(0xffffffFF);
//			spark.setColorAIncDec(-1);
//
//			spark.setAlive(true);
//
//			sparksEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 80, 200, 0, 2, 5000, spark);
//			sparksEmitter.setPosAttach(Math.random()*8-4, Math.random()*8-4, 0);
//			boatBody.addAttachedObject(sparksEmitter);
//			sparks=new Particles(80,sparksEmitter,spark);
			
			ParticlePC spark= new ParticlePC();
			
			spark.setEnergy(10);
			spark.setSize(2);
			spark.setTimeLeft(5000);
			//spark.setAcc(1.2, 1.2, 1.2);
			spark.setMyColor(0xffffffFF);
			spark.setColorAIncDec(-1);

			spark.setAlive(true);

			sparksEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 500, 20, 0, 20, 500, spark);
			sparksEmitter.setPosAttach(Math.random()*8-4, Math.random()*8-4, 0);
			boatBody.addAttachedObject(sparksEmitter);
			sparks=new Particles(2000,sparksEmitter,ParticlePC.class);
			if ((!damaged) || (dead))
				sparks.invalidate();

			// Canon fire line
			fireLine = new SimpleLinePC();
			//fireLine.setPos(xCannon1, yCannon1, 0);
			fireLine.setPos(0, 0, 0);
			fireLine.setPosEnd(1,0);
			fireLine.setPosAttach(xCannon1Abs, yCannon1Abs, 0);
			fireLine.setOurColor(0xffffff99);
			fireLine.invalidate();
			boatBody.addAttachedObject(fireLine);
			idFireLine =  RenderingManager.getInstance().addDrawableEntity(fireLine, 21);
			
			fireLine2 = new SimpleLinePC();
			fireLine2.setPos(0, 0, 0);
			//fireLine2.setPos(xCannon2Abs, yCannon2Abs, 0);
			fireLine2.setPosEnd(1,0);
			fireLine2.setPosAttach(xCannon2Abs, yCannon2Abs, 0);
			fireLine2.setOurColor(0xffeeff99);
			fireLine2.invalidate();
			boatBody.addAttachedObject(fireLine2);
			idFireLine2 =  RenderingManager.getInstance().addDrawableEntity(fireLine2, 21);
			
			fireLine3 = new SimpleLinePC();
			fireLine3.setPos(0, 0, 0);
			//fireLine3.setPos(xCannon3, yCannon3, 0);
			fireLine3.setPosEnd(1,0);
			fireLine3.setPosAttach(xCannon3Abs, yCannon3Abs, 0);
			fireLine3.setOurColor(0xffffee99);
			fireLine3.invalidate();
			boatBody.addAttachedObject(fireLine3);
			idFireLine3 =  RenderingManager.getInstance().addDrawableEntity(fireLine3, 21);
			
			setUpCoordinatesDefSys();
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
		boatBody.invalidate();
		smokeBlack.invalidate();
		fireLine.invalidate();
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
	
	/**
	 * Remove the gfx elements
	 *
	 */
	public void removeMe()
	{
		if (this.type != DUMMYBASE)
		{
			RenderingManager.getInstance().removeEntity(idBody, 20);
			RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
			RenderingManager.getInstance().removeEntity(idExplosion, 21);
			RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
			RenderingManager.getInstance().removeEntity(idFireLine, 21);
			RenderingManager.getInstance().removeEntity(idFireLine2, 21);
			RenderingManager.getInstance().removeEntity(idFireLine3, 21);
			RenderingManager.getInstance().removeParticles(idSparks, 20);
			RenderingManager.getInstance().removeEntity(idName, 39);

			smokeBlack = null;
			bubbles = null;
			bubblesEmitter = null; 
			badSmokeEmitter = null;
			sparks = null;
			sparksEmitter = null;
			
			boatBody = null;
			
			fireLine = null;
			fireLine2 = null;
			fireLine3 = null;
			
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
		}
	}
	
	public void removeMeWOBody()
	{
		if (this.type != DUMMYBASE)
		{
			RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
			RenderingManager.getInstance().removeEntity(idExplosion, 21);
			RenderingManager.getInstance().removeEntity(idFireLine, 21);

			smokeBlack = null;
			bubbles = null;
			bubblesEmitter = null; 
			badSmokeEmitter = null;
			
			fireLine = null;
			
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
		}
	}
	
	/**
	 * Attack the detected unit if needed, and eventually the incoming missiles.
	 * More AI might be good here
	 * @param time
	 */
	public void attackIfNeeded(double time)
	{
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
			if ((this.typeFireAsked == ProgrammableUnit.FIRE_MISSILE) && (this.nbMissiles > 0) && (nbInSalveM < nbInSalveMaxM))
			{
				SndContainer.playFireMiss2Sound();
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

				switch (this.typeFaction)
				{
					case FUnit.BASE_OUR:
						GameKeeper.getInstance().addMissiles(1);
						break;
					case FUnit.BASE_ALLIED:
						GameKeeper.getInstance().addMissilesAllies(1);
						break;
					case FUnit.BASE_ENEMY:
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
						if (tmpUnit.getType() == FUnit.UNKNOWN_AIR)
							tmpUnit.setType(FUnit.UNKNOWN_AIR | (1-typeEnemy));
						//System.out.println("Surface to sink");
						// Missile !!!
						if ((this.nbMissiles > 0) && (this.fireAtWill) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() != FUnit.MISSILE))
						{
//							if (typeEnemy == 1)
//							{
//							System.out.println("Bad guy, seen unit "+ tmpUnit.getType()+" - "+ (tmpUnit.getType() & 1)+ " : "+((tmpUnit.getType() & 1) == typeEnemy));
//							}
							if (this.isIgnoreFriends() || 
									(!this.ignoreFriends && ((tmpUnit.getType() & 1) == typeEnemy) ))
							{
								SndContainer.playFireMiss2Sound();
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

								switch (this.typeFaction)
								{
									case FUnit.BASE_OUR:
										GameKeeper.getInstance().addMissiles(1);
										break;
									case FUnit.BASE_ALLIED:
										GameKeeper.getInstance().addMissilesAllies(1);
										break;
									case FUnit.BASE_ENEMY:
										GameKeeper.getInstance().addMissilesEne(1);
										break;
								}
								
								nbInSalveM++;
							}
						}
						if ((this.nbMissiles > 0) && (nbInSalveM < nbInSalveMaxM) && (tmpUnit.getType() == FUnit.MISSILE) && (tmpUnit.getTypeFaction() == this.typeEnemy))
						{
							SndContainer.playFireMiss2Sound();
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
								case FUnit.BASE_OUR:
									GameKeeper.getInstance().addMissiles(1);
									break;
								case FUnit.BASE_ALLIED:
									GameKeeper.getInstance().addMissilesAllies(1);
									break;
								case FUnit.BASE_ENEMY:
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
			// Short range cannon, against airplanes and missiles mostly
			attackShortRange(time);
			if ((this.type != Base.SMALLBASEONE) && (this.type != Base.SMALLBASETWO))
			{
				attackShortRange2(time);
				if ((this.type == Base.MAINBASEONE) || (this.type == Base.MAINBASETWO))
				{
					attackShortRange3(time);
				}
			}
		}
	}

	/*
	 * Set the defence systems coordinates using the base coordinates
	 */
	protected void setUpCoordinatesDefSys()
	{
		xCannon1 = xCannon1Abs+this.posX;
		yCannon1 = xCannon1Abs+this.posY;
		xCannon2 = xCannon2Abs+this.posX;
		yCannon2 = xCannon2Abs+this.posY;
		xCannon3 = xCannon3Abs+this.posX;
		yCannon3 = xCannon3Abs+this.posY;
		
		xMissLaunch1 = xMissLaunch1Abs+this.posX;
		yMissLaunch1 = yMissLaunch1Abs+this.posY;
		xMissLaunch2 = xMissLaunch2Abs+this.posX;
		yMissLaunch2 = yMissLaunch2Abs+this.posY;
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
							(!this.ignoreFriends && (this.getTypeFaction() == FUnit.BASE_ENEMY) ))
					{
						if (ourAirplanes != null)
						{
							for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
							{
								tmpAirplane = ourAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.xCannon1, this.yCannon1, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
									distanceTmp = Missile.distSq(this.xCannon1, this.yCannon1, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
					if ((this.getTypeFaction() != FUnit.BASE_ENEMY) || 
							this.ignoreFriends)
					{
						if (enemiesAirplanes != null)
						{
							for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
							{
								tmpAirplane = enemiesAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.xCannon1, this.yCannon1, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
									distanceTmp = Missile.distSq(this.xCannon1, this.yCannon1, tmpMissile.getPosX(), tmpMissile.getPosY());
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
						case FUnit.BASE_OUR:
							GameKeeper.getInstance().addAmmo(1);
							break;
						case FUnit.BASE_ALLIED:
							GameKeeper.getInstance().addAmmoAllies(1);
							break;
						case FUnit.BASE_ENEMY:
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
	public void attackShortRange2(double time)
	{
		if (nbAmmo2 > 0)
		{
			if (loadingC2)
			{
				fireLine2.invalidate();
				
				timeSinceLastLaunchC2-=time;

				if (timeSinceLastLaunchC2 < 0)
				{
					loadingC2=false;
				}
			}
			if (!loadingC2)
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
							(!this.ignoreFriends && (this.getTypeFaction() == FUnit.BASE_ENEMY) ))
					{
						if (ourAirplanes != null)
						{
							for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
							{
								tmpAirplane = ourAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.xCannon2, this.yCannon2, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
									distanceTmp = Missile.distSq(this.xCannon2, this.yCannon2, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
					if ((this.getTypeFaction() != FUnit.BASE_ENEMY) || 
							this.ignoreFriends)
					{
						if (enemiesAirplanes != null)
						{
							for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
							{
								tmpAirplane = enemiesAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.xCannon2, this.yCannon2, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
									distanceTmp = Missile.distSq(this.xCannon2, this.yCannon2, tmpMissile.getPosX(), tmpMissile.getPosY());
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
				if (found && (distanceFound < rangeInterception2))
				{
					nbAmmo2--;
					switch (this.typeFaction)
					{
						case FUnit.BASE_OUR:
							GameKeeper.getInstance().addAmmo(1);
							break;
						case FUnit.BASE_ALLIED:
							GameKeeper.getInstance().addAmmoAllies(1);
							break;
						case FUnit.BASE_ENEMY:
							GameKeeper.getInstance().addAmmoEne(1);
							break;
					}
					fireLine2.setPosEnd(xToFireAt, yToFireAt);
					//System.out.println("Attack 2-4 "+fireLine2.getX()+" - "+fireLine2.getY());
					if (boatBody.isValide())
						fireLine2.validate();
					if (foundAirplane!=null)
						foundAirplane.damageBoat(20);
					else
					{
						if ((foundMissile !=null) && (Math.random() > 0.7))
							foundMissile.explode();
					}
					loadingC2=true;
					timeSinceLastLaunchC2=timeBetweenLaunchesC2;
				}
				else
				{
					fireLine2.invalidate();
				}
			}
		}
		else
		{
			fireLine2.invalidate();
		}
	}
	public void attackShortRange3(double time)
	{
		if (nbAmmo3 > 0)
		{
			if (loadingC3)
			{
				fireLine3.invalidate();
				
				timeSinceLastLaunchC3-=time;

				if (timeSinceLastLaunchC3 < 0)
				{
					loadingC3=false;
				}
			}
			if (!loadingC3)
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
							(!this.ignoreFriends && (this.getTypeFaction() == FUnit.BASE_ENEMY) ))
					{
						if (ourAirplanes != null)
						{
							for (int iBoat = 0; iBoat < ourAirplanes.size() ; iBoat++)
							{
								tmpAirplane = ourAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.xCannon3, this.yCannon3, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
									distanceTmp = Missile.distSq(this.xCannon3, this.yCannon3, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
					if ((this.getTypeFaction() != FUnit.BASE_ENEMY) || 
							this.ignoreFriends)
					{
						if (enemiesAirplanes != null)
						{
							for (int iBoat = 0; iBoat < enemiesAirplanes.size() ; iBoat++)
							{
								tmpAirplane = enemiesAirplanes.get(iBoat);
								if (tmpAirplane.getDepth() > -1)
								{
									distanceTmp = Missile.distSq(this.xCannon3, this.yCannon3, tmpAirplane.getPosX(), tmpAirplane.getPosY());
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
									distanceTmp = Missile.distSq(this.xCannon3, this.yCannon3, tmpMissile.getPosX(), tmpMissile.getPosY());
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
				if (found && (distanceFound < rangeInterception3))
				{
					nbAmmo3--;
					switch (this.typeFaction)
					{
						case FUnit.BASE_OUR:
							GameKeeper.getInstance().addAmmo(1);
							break;
						case FUnit.BASE_ALLIED:
							GameKeeper.getInstance().addAmmoAllies(1);
							break;
						case FUnit.BASE_ENEMY:
							GameKeeper.getInstance().addAmmoEne(1);
							break;
					}
					fireLine3.setPosEnd(xToFireAt, yToFireAt);
					if (boatBody.isValide())
						fireLine3.validate();
					if (foundAirplane!=null)
						foundAirplane.damageBoat(20);
					else
					{
						if ((foundMissile !=null) && (Math.random() > 0.7))
							foundMissile.explode();
					}
					loadingC3=true;
					timeSinceLastLaunchC3=timeBetweenLaunchesC3;
				}
				else
				{
					fireLine3.invalidate();
				}
			}
		}
		else
		{
			fireLine3.invalidate();
		}
	}

	public void launchInitialForce()
	{
		int iAirplane = 10;

		if (this.type == Base.BIGBASEONE)
		{
			iAirplane = 30;
		}
		else if (this.type == Base.BIGBASETWO)
		{
			iAirplane = 40;
		}
		else if (this.type == Base.MAINBASEONE)
		{
			iAirplane = 60;
		}
		else if (this.type == Base.MAINBASETWO)
		{
			iAirplane = 100;
		}
		
		System.out.println("Nb of fighters:" + nbFighters);

		for (int iLaunch = 0; iLaunch < iAirplane ; iLaunch++)
		{
			double xPlane = Math.random()*700-100;
			double yPlane = Math.random()*700-100;
			
			if ((nbAwacsSession >= 0))
			{
				Airplane caspianAir = new Airplane(theMap);
				caspianAir.getProgrammedWPs().addWP(new Waypoint(xPlane,yPlane,0));
				caspianAir.getProgrammedWPs().addWP(new Waypoint(xPlane,yPlane,0));
				caspianAir.getProgrammedWPs().addWP(new Waypoint(xPlane,yPlane,0));
				caspianAir.getProgrammedWPs().addWP(new Waypoint(xPlane,yPlane,0));
				caspianAir.setLoop(true);
				caspianAir.setFireAtWill(true);
				caspianAir.setIgnoreFriends(false);

				caspianAir.setComplement(20);
				caspianAir.setComplementNorm(20);
				caspianAir.setCost(100000000);
				caspianAir.setOrientation(this.orientation);
				caspianAir.setCurrentSpeed(600);
				caspianAir.setEnergy(1000);
				caspianAir.setMaxSpeed(800);
				caspianAir.setName("Hawk "+nbAwacs);
				caspianAir.setResistance(50);
				caspianAir.setVisibilityLevel(0);
				caspianAir.setNoiseSignature(2);
				caspianAir.setVisibilitySignature(0);
				caspianAir.setPosX(xPlane);
				caspianAir.setPosY(yPlane);
				caspianAir.setOwner(this);

				switch (this.typeFaction)
				{
					case FUnit.BASE_ALLIED:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
						break;
					case FUnit.BASE_OUR:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
						break;
					case FUnit.BASE_ENEMY:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
						break;
					default:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
				}

				caspianAir.setType(Airplane.RADARPLANE);
				caspianAir.createGfx(xPlane, yPlane, 0, 0, 20);

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

				if (this.typeFaction == FUnit.BASE_ENEMY)
					caspianAir.hideMe();

				theMap.addAirplane(caspianAir);

				nbAwacsSession--;

				this.timeBeforeAirLaunches=timeBetweenAirLaunches;
			}

			if ((nbFighters >= 0) && (this.currentNbFightersInFlight < this.nbFightersOnFlightMax))
			{
				Airplane caspianAir = new Airplane(theMap);
				caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
				caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
				caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
				caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
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
				caspianAir.setPosX(xPlane);
				caspianAir.setPosY(yPlane);
				caspianAir.setOwner(this);

				switch (this.typeFaction)
				{
					case FUnit.BASE_ALLIED:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
						break;
					case FUnit.BASE_OUR:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
						break;
					case FUnit.BASE_ENEMY:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
						break;
					default:
						caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
				}

				caspianAir.setType(Airplane.FIGHTER);
				caspianAir.createGfx(xPlane, yPlane, 0, 0, 20);

				SimpleAround ourRadar2;
				ourRadar2= new SimpleAround(theMap,caspianAir);
				ourRadar2.createGfx(0, 0, 0, 0, 1, 1);
				ourRadar2.setPosAttach(0, 0, 0);
				ourRadar2.setPower(10);
				ourRadar2.activate();
				ourRadar2.setDebugView(false);

				caspianAir.getSensors().add(ourRadar2);
				caspianAir.addAttachedObject(ourRadar2);
				caspianAir.getSensors().add(theMap.getOurKD());
				caspianAir.setView(false);

				if (this.typeFaction == FUnit.BASE_ENEMY)
					caspianAir.hideMe();

				theMap.addAirplane(caspianAir);

				nbFighters--;
				loadingA=true;
				this.timeBeforeAirLaunches=timeBetweenAirLaunches;
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.tgb.subgame.units.ProgrammableUnit#doUpdate()
	 */
	@Override
	public void doUpdate(double time)
	{
		if (this.type != DUMMYBASE)
		{
			if (!dead)
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

				{
					if ((nbAwacsSession >= 0) && !loadingA)
					{
						Airplane caspianAir = new Airplane(theMap);
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(this.getPosX()+Math.random()*200-100,this.getPosY()+Math.random()*200-100,0));
						caspianAir.setLoop(true);
						caspianAir.setFireAtWill(true);
						caspianAir.setIgnoreFriends(false);

						caspianAir.setComplement(20);
						caspianAir.setComplementNorm(20);
						caspianAir.setCost(100000000);
						caspianAir.setOrientation(this.orientation);
						caspianAir.setCurrentSpeed(600);
						caspianAir.setEnergy(1000);
						caspianAir.setMaxSpeed(800);
						caspianAir.setName("Hawk "+nbAwacs);
						caspianAir.setResistance(50);
						caspianAir.setVisibilityLevel(0);
						caspianAir.setNoiseSignature(2);
						caspianAir.setVisibilitySignature(0);
						caspianAir.setPosX(this.getPosX());
						caspianAir.setPosY(this.getPosY());
						caspianAir.setOwner(this);

						switch (this.typeFaction)
						{
							case FUnit.BASE_ALLIED:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
								break;
							case FUnit.BASE_OUR:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
								break;
							case FUnit.BASE_ENEMY:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
								break;
							default:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
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

						if (this.typeFaction == FUnit.BASE_ENEMY)
							caspianAir.hideMe();

						theMap.addAirplane(caspianAir);

						nbAwacsSession--;
						loadingA=true;
						this.timeBeforeAirLaunches=timeBetweenAirLaunches;
					}

					if ((nbFighters >= 0) && (this.currentNbFightersInFlight < this.nbFightersOnFlightMax) && !loadingA)
					{
						Airplane caspianAir = new Airplane(theMap);
						caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
						caspianAir.getProgrammedWPs().addWP(new Waypoint(Math.random()*500+100,Math.random()*600+100,0));
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

						switch (this.typeFaction)
						{
							case FUnit.BASE_ALLIED:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ALLIED);
								break;
							case FUnit.BASE_OUR:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_OUR);
								break;
							case FUnit.BASE_ENEMY:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
								break;
							default:
								caspianAir.setTypeFaction(FUnit.AIRPLANE_ENEMY);
						}

						caspianAir.setType(Airplane.FIGHTER);
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
						caspianAir.getSensors().add(theMap.getOurKD());
						caspianAir.setView(false);

						if (this.typeFaction == FUnit.BASE_ENEMY)
							caspianAir.hideMe();

						theMap.addAirplane(caspianAir);

						nbFighters--;
						loadingA=true;
						this.timeBeforeAirLaunches=timeBetweenAirLaunches;
					}
				}

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
			else if (explodeTime < 80)
			{
				explodeTime++;
				explosion.setSize(explosion.getSize()+0.2);
				System.out.println("Explode !!!!");
				sparks.validate();
				SndContainer.playExplosionSound();
			}
			else if (iDead > -1)
			{
				explosion.invalidate();
				this.setDepth(iDead--);
				smokeBlack.invalidate();
				
				if (this.type == Base.BIGBASEONE)
					boatBody.setImageToDraw(gfxSprites.getImageBase1Dest());
				else if (this.type == Base.BIGBASETWO)
					boatBody.setImageToDraw(gfxSprites.getImageBase2Dest());
				else if (this.type == Base.SMALLBASEONE)
					boatBody.setImageToDraw(gfxSprites.getImageBaseSmall1Dest());
				else if (this.type == Base.SMALLBASETWO)
					boatBody.setImageToDraw(gfxSprites.getImageBaseSmall2Dest());
				else if (this.type == Base.MAINBASEONE)
					boatBody.setImageToDraw(gfxSprites.getImageBase1Main1Dest());	
				else if (this.type == Base.MAINBASETWO)
					boatBody.setImageToDraw(gfxSprites.getImageBaseMain2Dest());
				else 
					boatBody.setImageToDraw(gfxSprites.getImageBase2Dest());
			}
			else
			{
				if (explodeTime == 80)
				{
					sparks.invalidate();
					removeMeWOBody();
				}
				else
					explodeTime++;
			}
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
		if (this.type != DUMMYBASE)
		{
			if (!dead)
			{
				double dist=Math.random();
				double dist2=Math.random();
//				this.baseState-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//				this.navState-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//				this.dataLinkState-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//
//				this.complement-=5*RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;
//				this.energy-=RandomContainer.getInstance().myRandom.nextInt(amount)/this.resistance;

				this.baseState-=dist*amount/this.resistance;
				this.navState-=dist2*amount/this.resistance;
				this.dataLinkState-=Math.random()*amount/this.resistance;

				this.complement-=5*Math.random()*amount/this.resistance;
				this.energy-=5*Math.random()*amount/this.resistance;

				//System.out.println("Dommage "+amount+" - "+baseState+" - "+energy);
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
				if ((this.baseState <= 0) && damaged)
				{
					baseState=0;
					dead = true;

					explosion.validate();
					fireLine.invalidate();
					fireLine2.invalidate();
					fireLine3.invalidate();

					for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
					{
						sensors.get(iSensors).removeMe();
					}
					sensors.clear();

					iDead=0;

					theMap.baseDead(this.typeFaction);
					idSparks =  RenderingManager.getInstance().addParticles(sparks, 20);
				}
				if ((this.baseState < 50) && (!damaged))
				{
					damaged = true;

					smokeBlack.validate();
					this.showMe();
				}
			}
		}
		else
		{
		
//			double dist=Math.random();
//			double dist2=Math.random();
//			this.baseState-=dist*amount/this.resistance;
//			this.navState-=dist2*amount/this.resistance;
//			this.dataLinkState-=Math.random()*amount/this.resistance;
//
//			this.complement-=5*Math.random()*amount/this.resistance;
//
//			if (navState <0)
//			{
//				navState=0;
//			}
//			if (dataLinkState <0)
//			{
//				dataLinkState=0;
//			}
//			if (complement <0)
//			{
//				complement=0;
//			}
//			if (this.complement == 0)
//			{
//				unmanned = true;
//			}
//			if (this.baseState <= 0)
//			{
//				baseState=0;
//				dead = true;
//
//				theMap.baseDead(this.typeFaction);
//				//idSparks =  RenderingManager.getInstance().addParticles(sparks, 20);
//			}
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

	public boolean isDead() {
		return dead;
	}

	public int getNbMissiles() {
		return nbMissiles;
	}

	public void setNbMissiles(int nbMissiles) {
		this.nbMissiles = nbMissiles;
	}

	public double getBaseState() {
		return baseState;
	}

	public void setBaseState(int hullState) {
		this.baseState = hullState;
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
		stringToConstruct.append(this.nbInSalveMaxM);
		stringToConstruct.append(":");
		stringToConstruct.append(this.nbMissiles);
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
		stringToConstruct.append(this.timeBetweenLaunchesM);
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
		// Sensors array
		
		return stringToConstruct.toString();
	}
	
	public void loadBoat(String savedBoat)
	{
		//Same thing, just the opposite!
	}
	
	
	/*
	 * Methods for the strategic map
	 */
	
	/**
	 * Can be repaired by a base or a special unit (or anything else)
	 * @param amount
	 */
	public void getRepaired(int amount)
	{
		super.getRepaired(0);
		
		baseState+=amount;
		if (damaged && baseState > 50)
		{
			damaged = false;
		}
		if (baseState> 100)
		{
			baseState=100;
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
		
		switch (type)
		{
			case Base.SMALLBASEONE:
			case Base.SMALLBASETWO:
				if (nbMissiles < 1000)
					nbMissiles++;
				if (this.nbFighters < 200)
					nbFighters++;
				break;
			case Base.BIGBASEONE:
			case Base.BIGBASETWO:
				if (nbMissiles < 8000)
					nbMissiles++;
				if (this.nbFighters < 400)
					nbFighters++;
				break;
			case Base.MAINBASEONE:
			case Base.MAINBASETWO:
				if (nbMissiles < 1800)
					nbMissiles++;
				if (this.nbFighters < 800)
					nbFighters++;
				break;
			default:	
				if (nbMissiles < 1000)
					nbMissiles++;
			if (this.nbFighters < 800)
				nbFighters++;
		}	
	}
	
	/**
	 * Create the graphics entity
	 * If you have more than one torpedo (should be :) ), it is better to use this
	 * method.
	 */
	public void createGfxSM(double x, double y, double z, double direction, double speed)
	{	
		initFields();
		
		if (!this.isDead())
		{
			switch (this.typeFaction)
			{
			case FUnit.BASE_ENEMY:
				boatBody = new SpritePC(gfxSprites.getImageBaseSmallEne());
				break;
			case FUnit.BASE_OUR:
				boatBody = new SpritePC(gfxSprites.getImageBaseSmallOur());
				break;
			default:
				boatBody = new SpritePC(gfxSprites.getImageBaseSmallAllies());
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

			explosion = new SpritePC(gfxSprites.getImageFlash2());	
			explosion.invalidate();
			explosion.setSize(3);
			explosion.setPosAttach(Math.random()*10-5, Math.random()*8-4, 0);
			boatBody.addAttachedObject(explosion);
			idExplosion = RenderingManager.getInstance().addDrawableEntity(explosion,21);

			ParticleSpritePC blackSmoke= new ParticleSpritePC(gfxSprites.getImageDarkSmoke());

			blackSmoke.setEnergy(10);
			blackSmoke.setSize(0.09);
			blackSmoke.setTimeLeft(5000);
			blackSmoke.setSizeIncDec(1.004);

			blackSmoke.setAlive(true);


			badSmokeEmitter = new Emitter(x, y, Math.PI, 0.4, Emitter.POINT, 2, 600, 0, 0.1, 5000, blackSmoke);
			badSmokeEmitter.setPosAttach(Math.random()*8-4, Math.random()*8-4, 0);
			boatBody.addAttachedObject(badSmokeEmitter);
			smokeBlack=new Particles(100,badSmokeEmitter,blackSmoke);
			if (!damaged)
				smokeBlack.invalidate();

			idBadSmoke =  RenderingManager.getInstance().addParticles(smokeBlack, 20);
			
			ourCircle = new CirclePC();
			ourCircle.setPos(x, y, 0);
			//ourCircle.setOurColor(0xff000000);
			ourCircle.setSizeCircle(this.getEnergy()/100, this.getEnergy()/100);
			ourCircle.validate();
			
			idCircle =  RenderingManager.getInstance().addDrawableEntity(ourCircle, 38);
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
		boatBody.invalidate();
		smokeBlack.invalidate();
		for (int iSensors=0;iSensors < sensors.size() ; iSensors++)
		{
			sensors.get(iSensors).hide();
		}
//		bubbles.invalidate();
	}
	
	public void showMeSM()
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
		RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
		RenderingManager.getInstance().removeEntity(idExplosion, 21);
		RenderingManager.getInstance().removeEntity(idBoatSquare, 39);
		RenderingManager.getInstance().removeEntity(idCircle, 38);
		RenderingManager.getInstance().removeEntity(idRank,38);
		
		smokeBlack = null;
		badSmokeEmitter = null; 
		
		boatBody = null;
	
		boatSquare = null;
		
		explosion = null;
		
		ourCircle = null;
		
		rankSprite = null;
		
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
	}
	
	public void removeMeWOBodySM()
	{
		RenderingManager.getInstance().removeParticles(idBadSmoke, 20);
		RenderingManager.getInstance().removeEntity(idExplosion, 21);
		RenderingManager.getInstance().removeEntity(idCircle, 38);
		RenderingManager.getInstance().removeEntity(idRank,38);
		
		smokeBlack = null;
		badSmokeEmitter = null; 
		
		
		boatSquare = null;
		explosion = null;

		ourCircle = null;
		
		rankSprite = null;
		
		if (sensors != null)
		{
			for (int iSensor = 0; iSensor < sensors.size() ; iSensor++)
			{
				// Update !!!
				sensors.get(iSensor).removeMe();
			}
			sensors.clear();
		}
	}

	@Override
	public void doUpdateSM(double time)
	{
		// TODO Auto-generated method stub
		if (!dead)
		{
			ourCircle.setSizeCircle(this.getEnergy()/100, this.getEnergy()/100);
			if (levelChanged)
			{
				RenderingManager.getInstance().removeEntity(idRank,38);
				createAttachedRank(); // Change the rank icon

				levelChanged=false;
			}
		}
	}
	
	@Override
	public void levelUp()
	{
		// TODO Auto-generated method stub
		this.level++;
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
			if (boatBody.isValide())
				nameGfx.validate();
		}
	}

	@Override
	public String saveIfNeeded()
	{
		BaseSavePart saveState = new BaseSavePart(baseState, dataLinkState, navState, damaged, unmanned, iDead, resistance, cost,
				nbMissiles, timeBetweenLaunchesM, timeBeforeAirLaunches);
		
		return "*"+giveMyId() + "\n"+ saveState.saveAll();
	}

	@Override
	public List<String> loadIfSupported(List<String> myPart)
	{
		List<String> whatIsLeft;
		
		BaseSavePart saveState = new BaseSavePart(baseState, dataLinkState, navState, damaged, unmanned, iDead, resistance, cost,
				nbMissiles, timeBetweenLaunchesM, timeBeforeAirLaunches);
		
		whatIsLeft = saveState.load(myPart);
		
		baseState = saveState.baseState;
		
		dataLinkState = saveState.dataLinkState;
		
		navState = saveState.navState;
		
		damaged = saveState.damaged;
		
		unmanned = saveState.unmanned;
		
		iDead = saveState.iDead;
		
		resistance = saveState.resistance;
		
		cost = saveState.cost;
		
		nbMissiles = saveState.nbMissiles;
		
		timeBetweenLaunchesM = saveState.timeBetweenLaunchesM;
		
		timeBeforeAirLaunches = saveState.timeBeforeAirLaunches;
		
		return whatIsLeft;
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
		switch( type )
		{
		case SMALLBASEONE:
			return "Small airfield";
		case SMALLBASETWO:
			return "Small base";
		case BIGBASEONE:
			return "Airfield";
		case BIGBASETWO:
			return "Base";
		case MAINBASEONE:
			return "Command Airfield";
		case MAINBASETWO:
			return "Command Base";
		case DUMMYBASE:
			return "Dummy Base";
		
			
		default:
			return "Unknown";
		}
	}
}
